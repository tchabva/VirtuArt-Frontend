# VirtuArt Frontend

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.x-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.08.01-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This is the official Android frontend for the VirtuArt Exhibit Curator. It allows users to browse artwork from world-renowned museums, create their own virtual exhibitions, and manage their collections with a modern, intuitive interface.

This application is powered by the **[VirtuArt-Backend](https://github.com/tchabva/virtuart-backend)**.

## Features
-   **Browse Artworks**: Explore artwork collections from multiple museum APIs with pagination support
-   **Advanced Search**: Search artworks with simple and advanced filtering options
-   **Artwork Details**: View comprehensive information including high-resolution images, artist details, and provenance
-   **Virtual Exhibitions**: Create, view, update, and delete personal virtual exhibitions
-   **Exhibition Management**: Add and remove artworks from your exhibitions with intuitive drag-and-drop interface
-   **User Authentication**: Secure Google Sign-In integration with token management
-   **Offline Support**: Cached artwork data for improved performance
-   **Modern UI**: Material Design 3 with dark/light theme support
-   **Responsive Design**: Optimized for various screen sizes and orientations

## Technologies Used
-   **Kotlin**: Official language for Android development with latest features
-   **Jetpack Compose**: Modern declarative UI toolkit with Material Design 3
-   **ViewModel & StateFlow**: Lifecycle-aware data management with reactive state handling
-   **Coroutines**: Asynchronous programming with structured concurrency
-   **Retrofit & OkHttp **: Type-safe HTTP client with advanced networking features
-   **Hilt**: Dependency injection framework for scalable architecture
-   **Paging**: Efficient loading and display of large datasets
-   **Glide**: Image loading and caching library with Compose integration
-   **Navigation Compose**: Type-safe navigation with deep linking support
-   **DataStore**: Modern data storage solution for preferences
-   **Google Identity Services**: Secure authentication with Google Sign-In
-   **Material Design 3**: Latest design system with dynamic theming

## Prerequisites
-   Android Studio (latest stable version recommended)
-   JDK 17 or higher

## Getting Started

### 1. Set Up the Backend
This application requires the **[VirtuArt-Backend](https://github.com/tchabva/virtuart-backend)** to be running. Please follow the setup instructions in the backend repository to get it up and running before you proceed.

### 2. Clone the Frontend Repository
```bash
git clone <your-frontend-repo-url>
cd virtuart-frontend
```

### 3. Configure Backend URL and Client ID
You need to tell the app where to find the backend API and your Google Sign-In Client ID.

1.  In the root of the project, create a file named `local.properties` if it doesn't already exist.
2.  Add the following lines to your `local.properties` file:

    ```properties
    # Your computer's local IP address for wireless device connection
    wireless.ip=192.168.1.100
    # Your Google Sign-In Web Client ID
    web.client.id=YOUR_GOOGLE_SIGN_IN_WEB_CLIENT_ID
    # Backend base URL for wireless connection
    base.url.backend.wireless=http://192.168.1.100:8080/api/v1/
    ```
    **Note:** Replace `192.168.1.100` with your computer's actual local IP address. You can find this by running `ipconfig` (Windows) or `ifconfig` (Mac/Linux).

    You can obtain the `web.client.id` from your Google API Console.

### 4. Network Security Configuration
The app automatically generates a network security configuration file during the build process. This allows the app to connect to your local backend server over HTTP.

The configuration is automatically created from the template file and your local IP address. No manual configuration is required.

### 5. Build and Run
Open the project in Android Studio, let Gradle sync, and then run the application on an emulator or a physical device.

---

## ⚠️ Files You Need to Create

Some files are ignored by version control and must be created/configured by each developer:

- **`local.properties`**: Add your SDK path, `wireless.ip`, `web.client.id`, and `base.url.backend.wireless` (see above example).
- **Keystore files (`*.jks`, `*.keystore`)**: For release builds, generate your own keystore. Not required for debug/development.
- **`app/src/main/res/xml/network_security_config.xml`**: Automatically generated during build from the template file. No manual creation required.
## Project Structure
```
app/src/main/java/uk/techreturners/virtuart/
├── ui/                     // UI components, screens, and themes (Jetpack Compose)
│   ├── common/            // Reusable UI components
│   ├── navigation/        // Navigation setup and routing
│   ├── screens/           // Individual screen implementations
│   │   ├── artworks/      // Artwork browsing and listing
│   │   ├── artworkdetail/ // Detailed artwork view
│   │   ├── exhibitions/   // Exhibition management
│   │   ├── exhibitiondetail/ // Detailed exhibition view
│   │   ├── search/        // Search functionality
│   │   └── profile/       // User profile and settings
│   └── theme/             // Material Design 3 theming
├── data/                  // Data layer implementation
│   ├── model/            // Data models and DTOs
│   ├── paging/           // Paging data source
│   ├── remote/           // API interfaces and network layer
│   └── repository/       // Repository implementations
├── domain/               // Domain layer
│   ├── model/           // Domain models
│   └── repository/      // Repository interfaces
└── di/                  // Dependency injection modules (Hilt)
```

## Contributing
Contributions are welcome! If you'd like to contribute, please fork the repository and open a pull request.

## License
This project is licensed under the MIT License.
