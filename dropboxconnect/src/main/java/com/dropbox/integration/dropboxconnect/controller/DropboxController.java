package com.dropbox.integration.dropboxconnect.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dropbox.integration.dropboxconnect.models.AuthRequest;
import com.dropbox.integration.dropboxconnect.models.AuthResponse;
import com.dropbox.integration.dropboxconnect.models.Event;
import com.dropbox.integration.dropboxconnect.models.Member;
import com.dropbox.integration.dropboxconnect.models.OAuthUrlResponse;
import com.dropbox.integration.dropboxconnect.models.TeamInfo;
import com.dropbox.integration.dropboxconnect.service.DropboxService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/dropbox")
@CrossOrigin(origins = { "http://localhost:8080" })
public class DropboxController {

	@Autowired
	DropboxService dropboxService;

	@PostMapping("/auth-url")
	public ResponseEntity<OAuthUrlResponse> generateAuthUrl(@RequestBody Map<String, String> request) {
		String clientId = request.get("clientId");
		String redirectUri = request.get("redirectUri");

		if (clientId == null || clientId.trim().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		if (redirectUri == null || redirectUri.trim().isEmpty()) {
			redirectUri = "http://localhost:8080/api/dropbox/callback";
		}

		String authUrl = dropboxService.generateAuthUrl(clientId, redirectUri);
		return ResponseEntity.ok(new OAuthUrlResponse(authUrl, "generated"));
	}

	@PostMapping("/token")
	public Mono<ResponseEntity<AuthResponse>> exchangeToken(@Valid @RequestBody AuthRequest authRequest) {
		log.info("Getting token");
		return dropboxService.exchangeCodeForToken(authRequest).map(authResponse -> {
			if (authResponse.isSuccess()) {
				return ResponseEntity.ok(authResponse);
			} else {
				return ResponseEntity.badRequest().body(authResponse);
			}
		});
	}

	@PostMapping("/team-info")
	public Mono<ResponseEntity<TeamInfo>> getTeamInfo(@RequestHeader("Authorization") String authHeader) {
		log.info("Getting team info");
		String accessToken = extractAccessToken(authHeader);
		if (accessToken == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}
		log.info("accessToken: "+accessToken);
		return dropboxService.getTeamInfo(accessToken).map(ResponseEntity::ok)
				.onErrorReturn(ResponseEntity.badRequest().build());
	}

	@PostMapping("/members")
	public Mono<ResponseEntity<List<Member>>> getMembers(@RequestHeader("Authorization") String authHeader) {
		String accessToken = extractAccessToken(authHeader);
		if (accessToken == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return dropboxService.getAllMembers(accessToken).map(ResponseEntity::ok)
				.onErrorReturn(ResponseEntity.badRequest().build());
	}

	@PostMapping("/events")
	public Mono<ResponseEntity<List<Event>>> getEvents(@RequestHeader("Authorization") String authHeader,
			@RequestParam(defaultValue = "50") int limit) {
		String accessToken = extractAccessToken(authHeader);
		log.info("accessToken: "+accessToken);
		if (accessToken == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return dropboxService.getEvents(accessToken, limit).map(ResponseEntity::ok)
				.onErrorReturn(ResponseEntity.badRequest().build());
	}

	private String extractAccessToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	@RequestMapping("/callback")
	public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
		log.info("Received authorization code: {}", code);
		return ResponseEntity.ok("Authorization code received. You can close this window and use the code: " + code);
	}
}
