# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.3 application using Gradle as the build tool. The project is configured with Java 17 and includes Spring Web and Spring Boot DevTools dependencies.

## Key Commands

### Build and Run
- `./gradlew bootRun` - Run the Spring Boot application
- `./gradlew build` - Build the project
- `./gradlew clean` - Clean build artifacts

### Testing
- `./gradlew test` - Run all tests
- `./gradlew test --tests "*ClassName*"` - Run specific test class
- `./gradlew test --tests "*methodName*"` - Run specific test method

### Development
- `./gradlew bootRun` - Run with DevTools hot reload enabled
- `./gradlew classes` - Compile main classes only
- `./gradlew testClasses` - Compile test classes only

## Project Structure

- **Main Application**: `src/main/java/com/example/demo/DemoApplication.java` - Entry point with `@SpringBootApplication`
- **Configuration**: `src/main/resources/application.properties` - Contains OpenAI API configuration
- **Package Structure**: Uses `com.example.demo` as base package
- **Testing**: JUnit 5 with Spring Boot Test support

## Important Configuration

The application is configured with OpenAI API integration:
- API key and URL are configured in `application.properties`
- Uses OpenRouter API endpoint for OpenAI compatibility

## Development Notes

- Spring Boot DevTools is included for automatic restart during development
- Uses Gradle wrapper (`./gradlew`) for consistent build environment
- JUnit Platform launcher is configured for test execution