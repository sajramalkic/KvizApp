package ba.etfrma.kvizapp.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ba.etfrma.kvizapp.ui.theme.screen.MainScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "kvizovi") {
        composable("kvizovi") {
            MainScreen()
        }
    }
}