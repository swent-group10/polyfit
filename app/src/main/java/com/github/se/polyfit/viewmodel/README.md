# Package: `com.github.se.polyfit.viewmodel`

This package is responsible for managing the ViewModel layer of the application. It interacts with
the data layer and provides data to the UI layer. This package handles user interactions and updates
the UI based on changes in the data.

## Key Components

1. **dailyRecap**: Provides data for the Daily Recap feature. This folder is responsible for
   fetching and processing data related to the user's daily intake, presenting it in a user-friendly
   format.
2. **graph**: Provides data for the Graph screen. It fetches and processes data related to the
   user's progress, making it suitable for graphical representation.
3. **map**: Provides data for the Map feature. It fetches posts within a certain distance from the
   user's location, displays them on a map, and updates them dynamically.
4. **meal**: Manages meal-related data. It fetches meal data from the data layer, processes it for
   display in the UI, and handles preprocessing images for image recognition.
5. **post**: Manages the data for posts, including fetching existing posts and handling the creation
   of new ones. This folder fetches post data from the data layer, processes it for display in the
   UI, and manages the creation of new posts, including image processing and uploading.
6. **qrCode**: Provides data for the QR Code feature. This folder processes QR code data.
7. **recipe**: Provides data for the Recipe recommendation feature. This folder fetches and
   processes recipe data, updating the variables used by the corresponding UI.
8. **settings**: Manages user settings and profile information. This folder provides the
   functionalities for users to update their profile information.
