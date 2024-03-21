package com.github.se.polyfit

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.MockNavigation
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.NavigationInterface
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PolyfitTheme {
                val navController = rememberNavController()
                val navigation = Navigation(navController)
                NavHost(navController = navigation.getController(), startDestination = Route.Register) {
                    composable(Route.Register) {
                        LoginScreen(navigation)
                    }

                    composable(Route.Home) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}


@HiltAndroidApp
class ExampleApplication : Application() {}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Singleton
    @Provides
    fun provideNavigation(app : Application): NavigationInterface {
        // Real implementation provided
        val navController = NavHostController(context = app)
        return Navigation(navController)
        //return MockNavigation(app)
    }*/
}

