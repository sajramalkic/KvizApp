package ba.etfrma.kvizapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ba.etfrma.kvizapp.ui.theme.screen.FilterScreen
import ba.etfrma.kvizapp.ui.theme.screen.KvizoviScreen
import ba.etfrma.kvizapp.viewmodel.KvizViewModel

sealed class Screen(val route: String) {
    object Filter : Screen("filter")
    object Kvizovi : Screen("kvizovi")
}

@Composable
fun NavGraph(navController: NavHostController) {
    val kvizViewModel: KvizViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Filter.route
    ) {
        composable(Screen.Filter.route) {
            FilterScreen(
                kvizViewModel = kvizViewModel,
                onPrikaziKvizove = {
                    navController.navigate(Screen.Kvizovi.route)
                }
            )
        }
        composable(Screen.Kvizovi.route) {
            KvizoviScreen(
                kvizViewModel = kvizViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}