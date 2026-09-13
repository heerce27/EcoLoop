# 🌱 EcoLoop

> **Small actions. Big impact.**

EcoLoop is an Android application designed to help users understand and reduce their everyday environmental impact. The app combines carbon-footprint tracking, eco-friendly recommendations, sustainable action tracking, recycling guidance, and gamified eco challenges in one mobile application.

## 📱 Project Overview

EcoLoop encourages users to make practical, sustainable choices by allowing them to:

- Create an account and log in
- Track carbon-emitting daily activities
- View their estimated carbon footprint
- Earn Eco Points for sustainable actions
- Get eco-friendly recommendations
- Explore circular/sustainable actions
- Check recommendations before buying products
- Find nearby recycling locations using maps
- Complete sustainability challenges and track progress
- Upload proof for selected activities

## ✨ Main Features

### 🔐 Login & Registration
- User registration with:
  - Name
  - Mobile number
  - Email
  - Password
- Email and mobile validation
- Password hashing before local storage
- Login authentication using the local SQLite database
- Login state persistence using `SharedPreferences`

### 📊 Carbon Footprint Tracking
Users can add everyday activities and receive an estimated carbon-footprint value.

The activity module supports:
- Activity selection
- Dynamic input fields
- Carbon calculation
- Eco Point calculation
- Activity history storage
- Optional proof-image upload

### 🤖 Eco Advisor
The Eco Advisor provides sustainability suggestions based on selected activities and frequency.

Examples include recommendations related to:
- Transport
- Energy use
- Food
- Daily habits

> **Current implementation note:** the advisor in this version is implemented with predefined in-app logic rather than a connected AI/ML API.

### ♻️ Circular Actions
Users can explore sustainable alternatives and complete actions that can reduce environmental impact.

The module connects actions with:
- Carbon savings
- Eco Points
- Sustainable product choices

### 🛒 Before You Buy
Users can select a product category and receive an eco-friendly recommendation before making a purchase.

Supported categories include:
- Clothing
- Electronics
- Food
- Furniture
- Plastic products
- Daily-use products

### ♻️ Recycling Finder
Users can:
- Select a waste category
- Use their device location
- Search for nearby recycling-related locations
- Open locations in Google Maps
- Get directions

Supported waste categories include:
- E-Waste
- Plastic
- Paper
- Glass
- Metal
- Clothes
- Batteries
- Electronics
- Furniture
- General recyclable waste

### 🏆 Eco Challenges
The challenge module provides sustainability tasks with:
- Challenge title
- Description
- Category
- Duration
- Eco Point reward
- Completion tracking
- Saved progress using `SharedPreferences`

### 📸 Activity Proof
For supported activities, users can select an image from their gallery. The selected image is copied into the app's internal storage and can be associated with the activity record.

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Platform | Android |
| Language | Java |
| IDE | Android Studio |
| Build System | Gradle Kotlin DSL |
| UI | XML layouts |
| Local Database | SQLite |
| Local Preferences | Android SharedPreferences |
| Maps | Android Maps/Google Maps intents |
| Minimum Android Version | API 24 |
| Target Android Version | API 36 |
| Compile SDK | API 36 |
| Java Compatibility | Java 11 |
| UI Libraries | AndroidX AppCompat, Material, ConstraintLayout |

## 🏗️ Application Flow

```text
Splash Screen
      ↓
Home / Login
      ↓
Login / Registration
      ↓
Dashboard
 ┌────┼────────┬───────────┬───────────┐
 ↓    ↓        ↓           ↓           ↓
Add  Eco     Circular   Before You   Recycling
Activity Advisor  Actions      Buy        Finder
 ↓                                     
Carbon Calculation
 ↓
Eco Points / Dashboard
```

The dashboard also provides access to the Eco Challenges and user profile.

## 🗂️ Project Structure

```text
EcoLoop/
├── app/
│   ├── src/
│   │   ├── androidTest/
│   │   ├── main/
│   │   │   ├── java/com/example/ecoloop/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── MainActivity2.java
│   │   │   │   ├── MainActivity3.java
│   │   │   │   ├── MainActivity4.java
│   │   │   │   ├── MainActivity5.java
│   │   │   │   ├── MainActivity6.java
│   │   │   │   ├── MainActivity7.java
│   │   │   │   ├── MainActivity8.java
│   │   │   │   ├── MainActivity9.java
│   │   │   │   ├── MainActivity10.java
│   │   │   │   └── sa.java
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   ├── mipmap-*/
│   │   │   │   ├── anim/
│   │   │   │   ├── values/
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

## 💾 Data Storage

EcoLoop currently uses an on-device SQLite database named:

```text
EcoLoopDB
```

The implementation contains a `users` table for account information and an `activities` table for recorded activities.

User preferences such as:
- Login state
- User ID/name
- Carbon footprint
- Eco Points
- Challenge progress

are stored using:

```text
EcoLoopPrefs
```

## 🚀 How to Run

### Requirements

- Android Studio
- Android SDK
- Java/JDK compatible with the project's Java 11 compile configuration
- Android emulator or Android device
- Gradle dependencies available to Android Studio

### Steps

1. Clone or download the repository.
2. Open the `EcoLoop` folder in Android Studio.
3. Allow Android Studio to sync the Gradle project.
4. Install/select an Android SDK compatible with the project.
5. Connect an Android device or create an emulator.
6. Run the `app` configuration.
7. Start using EcoLoop.

## 🔒 Security & Privacy

The current version stores application data locally on the device.

Important security considerations for future production deployment:

- Move authentication to a secure backend.
- Use a modern password-hashing algorithm such as Argon2id, bcrypt, or scrypt with unique salts.
- Avoid storing authentication credentials only in a local SQLite database.
- Use HTTPS for backend communication.
- Store sensitive tokens using Android Keystore-backed secure storage.
- Add proper session/token expiration.
- Validate and sanitize data on both client and server sides.

## 🧪 Testing

The project currently includes Android/JUnit test templates.

Recommended testing areas before production release:

- Registration validation
- Duplicate account handling
- Login with valid/invalid credentials
- Carbon calculation accuracy
- Activity saving/loading
- Eco Point updates
- Challenge completion and persistence
- Location permission handling
- Google Maps intent handling
- Image upload and storage
- Screen rotation/state restoration

## ⚠️ Current Limitations

This version is primarily a local Android prototype/MVP.

- Data is stored locally rather than through a cloud backend.
- The Eco Advisor is rule-based/predefined rather than connected to an AI model.
- No dedicated Spring Boot backend is included in the current Android project.
- No MongoDB/MySQL/Firebase integration is included in this codebase.
- Carbon calculations use in-app logic and should be validated against a reliable, versioned emission-factor dataset.
- Recycling results rely on map/location intents rather than a dedicated recycling-center database.
- The project contains several generically named Activities (`MainActivity2`, `MainActivity3`, etc.), which makes long-term maintenance harder.
- The Android manifest currently contains a reference to a `.recycling` Activity, while the provided Java source list does not contain `recycling.java`; this should be fixed/verified before release.
- The uploaded project archive contains generated Gradle/build/IDE files. These should normally not be committed to GitHub.

## 🔮 Future Improvements

### Backend
- Add a Spring Boot REST API.
- Add MySQL/MongoDB/Firebase as appropriate.
- Synchronize user activity across devices.
- Store carbon-factor data centrally.

### AI/ML
- Add a real AI recommendation service.
- Personalize suggestions using user activity history.
- Predict high-impact habits.
- Recommend the most effective next action.

### Carbon Engine
- Use a maintained emission-factor database.
- Separate emission factors from application code.
- Show calculation details and assumptions.
- Support regional factors and units.

### Gamification
- Add badges and achievement levels.
- Add daily/weekly streaks.
- Add leaderboards.
- Add team/friend challenges.

### UX
- Rename Activities to meaningful names such as:
  - `SplashActivity`
  - `AuthActivity`
  - `DashboardActivity`
  - `AddActivityActivity`
  - `AdvisorActivity`
  - `CircularActionsActivity`
  - `BeforeYouBuyActivity`
  - `RecyclingFinderActivity`
  - `ChallengesActivity`
- Add consistent navigation/back behavior.
- Improve accessibility and screen-reader labels.
- Add loading/error/empty states.
- Add charts for carbon trends.

## 📈 Suggested Hackathon Positioning

EcoLoop can be presented as a **personal sustainability companion** rather than only a carbon calculator.

The strongest product story is:

```text
Measure → Understand → Act → Prove → Earn → Improve
```

This makes the application more engaging than a simple carbon-footprint calculator because the user is given concrete actions and motivation to continue.

## 👥 Team

**Team:** VISIONX

**Project:** EcoLoop

**Tagline:** Small actions. Big impact.

## 📄 License

This project is currently a student/hackathon project. Add an appropriate open-source license if the project is intended for public reuse.
