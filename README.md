# VirtuArt Frontend

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.x-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.x-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This is the official Android frontend for the VirtuArt Exhibit Curator. It allows users to browse artwork from world-renowned museums, create their own virtual exhibitions, and manage their collections.

This application is powered by the **[VirtuArt-Backend](https://github.com/tchabva/virtuart-backend)**.

## Features
-   Browse and search for artwork from multiple museum APIs.
-   View detailed information and high-resolution images of each artwork.
-   Create, view, update, and delete personal virtual exhibitions.
-   Add and remove artworks from your exhibitions.
-   User authentication via Google Sign-In.

## Technologies Used
-   **Kotlin**: Official language for Android development.
-   **Jetpack Compose**: Modern toolkit for building native Android UI.
-   **ViewModel**: Manage UI-related data in a lifecycle-conscious way.
-   **Coroutines & StateFlow**: For asynchronous programming and reactive state management.
-   **Retrofit & OkHttp**: For making network requests to the backend API.
-   **Hilt**: For dependency injection.
-   **Material Design 3**: For UI components and styling.

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
    # Use 10.0.2.2 to connect to localhost from the Android emulator
    BACKEND_URL="http://10.00.2.2:8080/api/v1/"
    # Your Google Sign-In Web Client ID
    WEB_CLIENT_ID="YOUR_GOOGLE_SIGN_IN_WEB_CLIENT_ID"
    ```
    **Note:** If you are running the app on a physical device, replace `10.0.2.2` with your computer's local IP address.

    You can obtain the `WEB_CLIENT_ID` from your Google API Console.

### 4. Expose Properties in Gradle
To make these properties available in the app, you need to add them to your `app/build.gradle.kts` file:

```kotlin
// In app/build.gradle.kts

import java.util.Properties
import java.io.FileInputStream

// ... other gradle config

// Load properties from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    // ...
    defaultConfig {
        // ...
        buildConfigField("String", "BACKEND_URL", "\"${localProperties.getProperty("BACKEND_URL")}\"")
    }
    // ...
}

// And add the client ID as a string resource
android {
    // ...
    defaultConfig {
        // ...
        resValue("string", "web_client_id", "\"${localProperties.getProperty("WEB_CLIENT_ID")}\"")
    }
    // ...
}
```

### 5. Build and Run
Open the project in Android Studio, let Gradle sync, and then run the application on an emulator or a physical device.

---

## ⚠️ Files You Need to Create

Some files are ignored by version control and must be created/configured by each developer:

- **`local.properties`**: Add your SDK path, `BACKEND_URL`, and `WEB_CLIENT_ID` (see above example).
- **Keystore files (`*.jks`, `*.keystore`)**: For release builds, generate your own keystore. Not required for debug/development.
- **`app/src/main/res/xml/network_security_config.xml`**: If you need to allow cleartext traffic for local development, create this file. Example:

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <domain-config cleartextTrafficPermitted="true">
            <!-- For the Android Emulator -->
            <domain includeSubdomains="true">10.0.2.2</domain>
            <!-- For a physical device (replace with your computer's IP) -->
            <domain includeSubdomains="true">YOUR.LOCAL.IP.ADDRESS</domain>
        </domain-config>
    </network-security-config>
    ```
---

## Project Structure
```
app/src/main/java/com/example/virtuart
├── ui              // UI components, screens, and themes (Jetpack Compose)
├── viewmodel       // ViewModels for each screen
├── data            // Repository, data sources, network API definitions
├── di              // Dependency injection modules (Hilt)
└── util            // Utility classes and helpers
```

## Contributing
Contributions are welcome! If you'd like to contribute, please fork the repository and open a pull request.

## License
This project is licensed under the MIT License.