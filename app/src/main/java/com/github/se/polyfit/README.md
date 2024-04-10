## Documentation
### Figma/material3 to Code

In the [material3 doc](https://m3.material.io/components) you can find every usable component along with its "generic" appearance and functionality.  
<br>For example for the Navigation Bar

<p align="center">
  <img src="https://github.com/swent-group10/polyfit/assets/56965385/0b48556f-92d3-482c-bb65-a397f368112a" alt="Navigation Bar look" width="300"       heights="200">
  </p>
<br>In the Specs tab, you can find all the information on how to access different parts of the component and many other details.

<p align="center">
<img src="https://github.com/swent-group10/polyfit/assets/56965385/1fc25c35-1cb3-42f1-ae0b-504771f0ed15" alt="Navigation Bar look" width="300" heights="200">
  </p>
   
<br>The Guidelines tab provides really cool pieces of advice on how to implement it correctly.

<br>To use all of this in figma access this <a href="https://www.figma.com/community/file/1035203688168086460" target="_blank">Figma file</a>

<br>To implement the componant in Kotlin code, refer to this [doc](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) or you can directly access it through M3 doc 

### Color theme
It is important to correctly apply the appropriate color to the component, not only for the uniformity of the project but also because themes are dynamic and can change. Depending on how you configure your operating system's light/dark theme, the app will automatically switch between light and dark themes. [Color roles](https://m3.material.io/styles/color/roles)

<br> Like for the [inverse](https://m3.material.io/styles/color/roles#7fc6b47e-db22-4e98-8359-7649a099e4a1), the text displayed will always be readable.

<br> In other words, if you don't know what color to apply, follow this guide as a first step.

### UPDATE
If you apply the function (in [Theme.kt](https://github.com/swent-group10/polyfit/blob/e23c2893838d6ca3d49c43c7a60bf706d92118c8/app/src/main/java/com/github/se/polyfit/ui/theme/Theme.kt#L37)) tn your composable, the default color set by the M3 developers is applied automatically. If you choose to override this color and pass your composable as an argument to a function, a strange color is applied. (I have not yet fully understood how this works)

### Samples of project
<a href="https://github.com/android/compose-samples" target="_blank">Here</a>, you can find many different project made by android team, and can be helpful to solve many implementation problem.
#### Hilt
In this [sample](https://github.com/android/compose-samples/tree/main/Crane), android seems to have implement Hilt.
#### Graph
Like [Jetlagged](https://github.com/android/compose-samples/tree/main/JetLagged), that seems to implement some graphics



<p align="center">
<img src="https://github.com/swent-group10/polyfit/assets/56965385/de1b802f-42e7-442f-b978-b7f8aeeb98d1" alt="Navigation Bar look" width="600" heights="1200">
  </p>

