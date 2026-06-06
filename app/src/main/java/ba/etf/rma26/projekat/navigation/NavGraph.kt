package ba.etf.rma26.projekat.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ba.etf.rma26.projekat.ui.theme.screen.FilterScreen
import ba.etf.rma26.projekat.ui.theme.screen.KvizDetaljiScreen
import ba.etf.rma26.projekat.ui.theme.screen.KvizoviScreen
import ba.etf.rma26.projekat.viewmodel.KvizViewModel

sealed class Screen(val route: String) {
    object Filter : Screen("filter")
    object Kvizovi : Screen("kvizovi")
    object KvizDetalji : Screen("kvizDetalji/{idKviza}/{nazivKviza}") {
        fun createRoute(idKviza: Int, nazivKviza: String) =
            "kvizDetalji/$idKviza/${nazivKviza}"
    }
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
                onBack = { navController.popBackStack() },
                onKvizKliknut = { idKviza, nazivKviza ->
                    navController.navigate(Screen.KvizDetalji.createRoute(idKviza, nazivKviza))
                }
            )
        }
        composable(
            route = Screen.KvizDetalji.route,
            arguments = listOf(
                navArgument("idKviza") { type = NavType.IntType },
                navArgument("nazivKviza") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idKviza = backStackEntry.arguments?.getInt("idKviza") ?: 0
            val nazivKviza = backStackEntry.arguments?.getString("nazivKviza") ?: ""
            KvizDetaljiScreen(
                idKviza = idKviza,
                nazivKviza = nazivKviza,
                onBack = { navController.popBackStack() }
            )
        }
    }
}