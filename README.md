# iBookProject

**iBookProject** is an app for managing books, where users can upload, rate, and comment on books. It integrates Firebase for user authentication, and uses Room for local data storage. The app also retrieves quotes from an external API to display to users.

### Features

- **User Authentication**: Sign up and log in using Firebase Authentication.
- **Book Management**: Upload and manage books, including title, description, cover image, and ratings.
- **Ratings and Comments**: Users can rate books and leave comments.
- **Profile Management**: Users can view and edit their profiles, including updating their name and profile picture.
- **External API Integration**: Fetches random book-related quotes from an external API.
- **Offline Support**: Local data storage using Room for books, ratings, and comments.

### Technologies Used

- **Firebase**: Authentication, Firestore, and Storage.
- **Room**: Local data storage.
- **NavController** and **SafeArgs**: Safe navigation between fragments.
- **Picasso**: Efficient image loading for book covers.
- **Material Design**: UI components following Google’s design guidelines.

### Requirements

- Android Studio 4.0 or higher.
- Minimum SDK: 21.
- Target SDK: 35.
- Firebase setup (for Authentication, Firestore, and Storage).

### Installation and Setup

1. **Clone the Repository**:
   ```bash
   git clone <repository_url>

### Install Dependencies
- Ensure that all dependencies are set up in the build.gradle files.

### Set up Firebase
- Go to Firebase Console.
- Create a new project and enable Authentication and Firestore.
- Download the google-services.json and place it in your project’s app/ directory.

### Run the App
- Open the project in Android Studio.
- Build and run the app on an emulator or physical device.

### Use the App
- On the first run, sign up or log in with Firebase.
- Navigate through the app to add books, rate them, leave comments, and view quotes.

