# DropboxConnect
A comprehensive Spring Boot application that provides seamless integration with Dropbox Business APIs, featuring both backend services and a user-friendly web interface.

## Features

- OAuth2 authentication with Dropbox
- Team management capabilities
- Member information retrieval
- Event logging and monitoring
- RESTful API endpoints
- Interactive web interface
- Secure token management

## Technical Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Security
- Spring WebFlux
- Project Lombok
- Maven 3.8+

### Frontend
- HTML5/CSS3
- JavaScript
- Bootstrap 5
- Axios for API calls

## Prerequisites

- Java 21
- Maven 3.8+
- Dropbox Business API credentials:
  - App Key (Client ID)
  - App Secret (Client Secret)
  - Required scopes: 
    - team_info.read
    - members.read
    - events.read
    - team_data.member

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd dropboxconnect
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## API Endpoints

### Authentication
- `POST /api/dropbox/auth-url`: Generate authorization URL
- `POST /api/dropbox/token`: Exchange code for access token

### Team Operations
- `GET /api/dropbox/team-info`: Retrieve team information
- `GET /api/dropbox/members`: List team members
- `GET /api/dropbox/events`: Get team events

## Frontend Integration

### Authentication Flow
1. User clicks "Connect to Dropbox"
2. System generates authorization URL
3. User authenticates with Dropbox
4. System exchanges code for access token
5. Token stored for API calls

### API Usage Example
```javascript
// Authenticate
const response = await axios.post('/api/dropbox/auth-url', {
  clientId: clientId,
  redirectUri: redirectUri
});

// Make API calls
const teamInfo = await axios.get('/api/dropbox/team-info', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});
```

## Security Considerations

- OAuth2 secure flow implementation
- Token encryption at rest
- HTTPS enforcement
- CORS configuration
- Input validation
- Rate limiting

## Error Handling

The application implements comprehensive error handling:
- API error responses
- User-friendly error messages
- Token refresh mechanism
- Network error recovery

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package -P production
```

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Open pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions:
1. Check existing issues
2. Create new issue with detailed description
3. Include relevant logs and screenshots
