package com.dropbox.integration.dropboxconnect.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.dropbox.integration.dropboxconnect.config.DropboxConfig;
import com.dropbox.integration.dropboxconnect.models.AuthRequest;
import com.dropbox.integration.dropboxconnect.models.AuthResponse;
import com.dropbox.integration.dropboxconnect.models.Event;
import com.dropbox.integration.dropboxconnect.models.Member;
import com.dropbox.integration.dropboxconnect.models.TeamInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DropboxService {

	private final WebClient webClient;
	
	@Autowired
	DropboxConfig dropboxConfig;
	
	 private final ObjectMapper objectMapper;
	
	public DropboxService(DropboxConfig dropboxConfig,ObjectMapper objectMapper) {
		this.webClient = WebClient.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)).build();
		 this.objectMapper = objectMapper;
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public String generateAuthUrl(String clientId, String redirectUri) {
		String state = UUID.randomUUID().toString();

		String encodedScopes = dropboxConfig.getScopes().replace(" ", "%20");
		
		return String.format("%s/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s&state=%s&token_access_type=offline",
	        dropboxConfig.getAuthUrl(),
	        clientId,
	        redirectUri,
	        encodedScopes,
	        state
	    );
	}

	public Mono<AuthResponse> exchangeCodeForToken(AuthRequest authRequest) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", authRequest.getCode());
		formData.add("grant_type", "authorization_code");
		formData.add("redirect_uri", authRequest.getRedirectUri());
		
		log.info("Getting token for formData: {}",formData);

		String basicAuth = Base64.getEncoder()
				.encodeToString((authRequest.getClientId() + ":" + authRequest.getClientSecret()).getBytes());

		return webClient.post().uri(dropboxConfig.getBaseUrl() + dropboxConfig.getEndpoints().getToken())
				.header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.body(BodyInserters.fromFormData(formData)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							try {
								JsonNode errorNode = objectMapper.readTree(errorBody);
								log.info(errorNode.toString());
								String errorMessage = errorNode.has("error_description")
										? errorNode.get("error_description").asText()
										: "Token exchange failed";
								return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
							} catch (Exception e) {
								return Mono.error(
										new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token exchange failed"));
							}
						}))
				.bodyToMono(String.class).map(response -> {
					try {
						log.info(response);
						return objectMapper.readValue(response, AuthResponse.class);
					} catch (JsonProcessingException e) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Failed to parse token response");
					}
				}).onErrorResume(ResponseStatusException.class, e -> Mono.just(new AuthResponse(e.getReason())));
	}

	public Mono<TeamInfo> getTeamInfo(String accessToken) {
		return makeDropboxRequest(dropboxConfig.getEndpoints().getTeamInfo(), accessToken).map(response -> {
			try {
				log.info("response: "+response);
				return objectMapper.readValue(response, TeamInfo.class);
			} catch (JsonProcessingException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse team info");
			}
		});
	}

	public Mono<List<Member>> getAllMembers(String accessToken) {
		return getMembersPage(accessToken, null, new ArrayList<>());
	}

	private Mono<List<Member>> getMembersPage(String accessToken, String cursor, List<Member> accumulator) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("limit", 100);

		String endpoint = dropboxConfig.getEndpoints().getMembersList();
		if (cursor != null) {
			endpoint = dropboxConfig.getEndpoints().getMembersContinue();
			requestBody.put("cursor", cursor);
		}

		try {
			String bodyJson = objectMapper.writeValueAsString(requestBody);
			return makeDropboxRequest(endpoint, accessToken, bodyJson).flatMap(response -> {
				try {
					JsonNode responseNode = objectMapper.readTree(response);

					if (responseNode.has("members")) {
						List<Member> members = objectMapper.convertValue(responseNode.get("members"),
								new TypeReference<>() {
								});
						accumulator.addAll(members);
					}

					boolean hasMore = responseNode.has("has_more") && responseNode.get("has_more").asBoolean();
					if (hasMore && responseNode.has("cursor")) {
						String nextCursor = responseNode.get("cursor").asText();
						return getMembersPage(accessToken, nextCursor, accumulator);
					} else {
						return Mono.just(accumulator);
					}

				} catch (JsonProcessingException e) {
					return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Failed to parse members response"));
				}
			});
		} catch (JsonProcessingException e) {
			return Mono.error(
					new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create request body"));
		}
	}

	public Mono<List<Event>> getEvents(String accessToken, int limit) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("limit", limit);
		requestBody.put("category", "sharing");
		try {
			String bodyJson = objectMapper.writeValueAsString(requestBody);
			
			log.info("Event request: {}",bodyJson);
			return makeDropboxRequest(dropboxConfig.getEndpoints().getEvents(), accessToken, bodyJson).map(response -> {
				try {
					JsonNode responseNode = objectMapper.readTree(response);
					log.info("Event response: {}",responseNode);
					if (responseNode.has("events")) {
						return objectMapper.convertValue(responseNode.get("events"), new TypeReference<List<Event>>() {
						});
					}

					return new ArrayList<>();

				} catch (JsonProcessingException e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Failed to parse events response");
				}
			});
		} catch (JsonProcessingException e) {
			return Mono.error(
					new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create request body"));
		}
	}

	private Mono<String> makeDropboxRequest(String endpoint, String accessToken) {
	    log.info("Making Dropbox request to endpoint: {}",dropboxConfig.getBaseUrl()+ endpoint);
	    
	    return webClient.post()
	            .uri(dropboxConfig.getBaseUrl() + endpoint)
	            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
	            .retrieve()
	            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
	                    response -> response.bodyToMono(String.class)
	                            .doOnNext(errorBody -> log.error("Error response: {}", errorBody))
	                            .flatMap(errorBody -> {
	                                try {
	                                    JsonNode errorNode = objectMapper.readTree(errorBody);
	                                    String errorMessage = errorNode.has("error_summary")
	                                            ? errorNode.get("error_summary").asText()
	                                            : "API request failed";
	                                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
	                                } catch (Exception e) {
	                                	log.info("Exception occurred: {}",e.getMessage());
	                                    return Mono.error(
	                                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "API request failed"));
	                                }
	                            }))
	            .bodyToMono(String.class)
	            .doOnNext(response -> log.info("Success response: {}", response))
	            .doOnError(error -> log.error("Request failed: {}", error.getMessage()));
	}
	
	
	
	private Mono<String> makeDropboxRequest(String endpoint, String accessToken, String body) {
	    log.info("Making Dropbox request to endpoint: {}",dropboxConfig.getBaseUrl()+ endpoint);
	    log.info("Request body: {}", body);
	    
	    
	    return webClient.post()
	            .uri(dropboxConfig.getBaseUrl() + endpoint)
	            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
	            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	            .bodyValue(body)
	            .retrieve()
	            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
	                    response -> response.bodyToMono(String.class)
	                            .doOnNext(errorBody -> log.error("Error response: {}", errorBody))
	                            .flatMap(errorBody -> {
	                                try {
	                                    JsonNode errorNode = objectMapper.readTree(errorBody);
	                                    String errorMessage = errorNode.has("error_summary")
	                                            ? errorNode.get("error_summary").asText()
	                                            : "API request failed";
	                                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
	                                } catch (Exception e) {
	                                	log.info("Exception occurred: {}",e.getMessage());
	                                    return Mono.error(
	                                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "API request failed"));
	                                }
	                            }))
	            .bodyToMono(String.class)
	            .doOnNext(response -> log.info("Success response: {}", response))
	            .doOnError(error -> log.error("Request failed: {}", error.getMessage()));
	}
}
