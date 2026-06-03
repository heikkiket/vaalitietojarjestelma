## Architecture
Controller -> Service -> Repository

## Technologies
Kotlin 2.3, Spring Boot

## Persistence
Exposed, PostgreSQL

## API
Spring Web MVC, Kotlinx Serialization

## Security
Spring Security

## Test structure
Use kotest.
Behavioral tests validate use cases against service layer
Integration tests test HTTP API
Unit tests test code under service layer
