# GitHub User Explorer Project

This project is a terminal-based application written in Kotlin that allows users to search and view information about GitHub users. The application uses the official GitHub API and stores data in memory and file to prevent redundant requests.

## Features

- Fetch user information by username
- Display list of users stored in memory
- Search users by username from cached data
- Search repositories by name from cached data
- Store data in memory and file for reuse

## Technologies and Libraries

- Kotlin 1.8.0
- Retrofit 2.9.0 for API communication
- Gson 2.10.1 for JSON conversion
- Kotlinx Coroutines 1.6.4 for asynchronous operations
- Kotlinx Serialization 1.5.0 for data serialization
- OkHttp 4.10.0 for HTTP logging

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   ├── Main.kt                  # Application entry point
│   │   ├── api/                     # API communication layer
│   │   │   ├── GitHubApi.kt         # API interface definition
│   │   │   └── RetrofitClient.kt    # Retrofit configuration
│   │   ├── model/                   # Data models
│   │   │   ├── User.kt              # User model
│   │   │   └── Repository.kt        # Repository model
│   │   ├── service/                 # Service layer
│   │   │   ├── GitHubService.kt     # Main application service
│   │   │   └── CacheService.kt      # Data storage service
│   │   └── ui/                      # User interface layer
│   │       └── TerminalUI.kt        # Terminal user interface
│   └── resources/
│       └── cache.json               # Data storage file
└── build.gradle.kts                 # Project configuration
```

## How to Run

1. Make sure you have JDK 21 or higher installed.
2. Build the project with Gradle:
   ```
   ./gradlew build
   ```
3. Run the application:
   ```
   ./gradlew run
   ```

## How to Use

After running the application, the main menu will be displayed:

```
===== GitHub User Explorer =====
1. Fetch user information by username
2. Display all cached users
3. Search users by username
4. Search repositories by name
5. Exit
================================
```

- Option 1: Fetch user information by entering a username
- Option 2: Display the list of users stored in memory
- Option 3: Search users by username
- Option 4: Search repositories by name
- Option 5: Exit the application

## Application Architecture

This project uses a service-oriented architecture:

- **API Layer**: Responsible for communication with the GitHub API
- **Model Layer**: Defines data structures
- **Service Layer**: Main application logic and data management
- **UI Layer**: Terminal user interface

## Error Handling

The application uses Kotlin's `Result` class for API error handling. Potential errors such as invalid users, network issues, etc. are properly managed.

## Data Storage

Data received from the API is stored in memory and also kept in the `cache.json` file to be preserved between different application runs.
