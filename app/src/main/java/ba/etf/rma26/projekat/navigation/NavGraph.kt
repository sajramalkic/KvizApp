package ba.etf.rma26.projekat.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ba.etf.rma26.projekat.ui.theme.screen.*
import ba.etf.rma26.projekat.viewmodel.AuthViewModel
import ba.etf.rma26.projekat.viewmodel.KvizViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Kvizovi : Screen("kvizovi")
    object Upis : Screen("upis")
    object Profil : Screen("profil")
    object KvizDetalji : Screen("kvizDetalji/{idKviza}/{nazivKviza}") {
        fun createRoute(idKviza: Int, nazivKviza: String) =
            "kvizDetalji/$idKviza/$nazivKviza"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Kvizovi, "Kvizovi", Icons.Default.List),
    BottomNavItem(Screen.Upis,    "Upis",    Icons.Default.School),
    BottomNavItem(Screen.Profil,  "Profil",  Icons.Default.Person)
)

// Ekrani koji prikazuju bottom nav
val bottomNavRoutes = setOf(
    Screen.Kvizovi.route,
    Screen.Upis.route,
    Screen.Profil.route
)

@Composable
fun NavGraph(navController: NavHostController) {
    val kvizViewModel: KvizViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    // Odjava → nazad na Login
    val currentHash by authViewModel.currentHash.collectAsState()
    LaunchedEffect(currentHash) {
        if (currentHash == null && currentRoute != Screen.Login.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Kvizovi.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = item.label)
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                val isLoading by authViewModel.isLoading.collectAsState()
                val greska by authViewModel.greska.collectAsState()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        kvizViewModel.osvjeziPodatke()
                        navController.navigate(Screen.Kvizovi.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }

                LoginScreen(
                    isLoading = isLoading,
                    greska = greska,
                    onLogin = { hash -> authViewModel.login(hash) }
                )
            }

            composable(Screen.Kvizovi.route) {
                KvizoviScreen(
                    kvizViewModel = kvizViewModel,
                    authViewModel = authViewModel,
                    onKvizKliknut = { idKviza, nazivKviza ->
                        navController.navigate(
                            Screen.KvizDetalji.createRoute(idKviza, nazivKviza)
                        )
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Upis.route) {
                FilterScreen(kvizViewModel = kvizViewModel)
            }

            composable(Screen.Profil.route) {
                ProfilScreen(
                    authViewModel = authViewModel,
                    kvizViewModel = kvizViewModel,
                    onOdjava = { authViewModel.odjava() }
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
                    onBack = { navController.popBackStack() },
                    onPromijeniGrupu = {
                        navController.navigate(Screen.Upis.route) {
                            popUpTo(Screen.Upis.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}