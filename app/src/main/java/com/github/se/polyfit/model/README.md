# Package: `com.github.se.polyfit.model`

This package is responsible for managing the Model layer of the application. It contains classes
that define the structure of the data used in the application. These classes represent data in a
format that can be easily manipulated and processed by other layers of the application. Most of the
classes are modeled after the format used by the Spoonacular API.

## Key Components

1. **ingredient**: Classes that define the structure of an ingredient in the application, a
   subcomponent of a meal.
2. **meal**: Classes that define the structure of a meal in the application.
3. **nutritionalInformation**: Classes that define the structure of nutritional information in the
   application, representing the nutritional content of a meal, recipe, or ingredient.
4. **post**: Classes that define the structure of a post in the application, including properties
   such as ID, location, and more.
5. **recipe**: Classes that define the structure of a recipe in the application. This includes
   the `RecipeInformation` class, which stores additional information about the recipe such as
   instructions.
6. **settings**: Classes that define the different options available in the application's settings.
7. **user**: Classes that define the structure of a user in the application, including properties
   such as ID, name, email, and more.
