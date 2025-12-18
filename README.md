# ShoppingList 🛒

A simple Android shopping list app built with Kotlin, Jetpack Compose and Material Design 3, featuring persistent local storage and a clean MVVM architecture.

https://github.com/user-attachments/assets/79b5734f-0720-4fef-adc1-4dd56c915304

## ✨ Features

- **Add & Edit Items** – Quickly add new items or edit existing ones
- **Simple Organization** – Automatically separates items into "Shopping List" and "On Cart" sections
- **Persistent Storage** – All items are saved locally and restored on app restart
- **Item Timestamps** – Track when each item was added
- **Intuitive UI** – Clean Material Design 3 interface with custom fonts and colors

## 🛠️ Technologies

- **Kotlin 2.0.21** – Modern, concise programming language
- **Jetpack Compose** – Declarative UI toolkit with Material3 components
- **MVVM Architecture** – Clear separation of concerns between UI and business logic
- **Repository Pattern** – Abstracted data access layer
- **DataStore** – Type-safe data persistence with preferences
- **StateFlow** – Reactive state management for UI updates
- **Kotlin Serialization** – JSON encoding/decoding with custom Date serializer

## 🏗️ Architecture

The app follows clean architecture principles with three distinct layers:

```
┌─────────────────────────────────────┐
│   UI Layer (Jetpack Compose)        │
│   - Composable functions            │
│   - Material3 components            │
└──────────────┬──────────────────────┘
               │ StateFlow
┌──────────────▼──────────────────────┐
│   ViewModel Layer                   │
│   - ShoppingListViewModel           │
│   - State management                │
└──────────────┬──────────────────────┘
               │ Repository Interface
┌──────────────▼──────────────────────┐
│   Data Layer                        │
│   - Repository implementation       │
│   - DataStore (local persistence)   │
└─────────────────────────────────────┘
```

**Key Components:**
- **UI Layer**: Reactive Compose UI that observes StateFlow
- **ViewModel**: Manages UI state and coordinates data operations
- **Repository**: Abstracts data source (DataStore)
- **DataStore**: Persists shopping list as JSON with custom Date serialization

## 📱 Installation

1. Clone this repository

2. Open the project in Android Studio

3. Sync Gradle and run on an emulator or physical device

