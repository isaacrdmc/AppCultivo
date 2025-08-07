package com.example.cropmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cropmonitor.Login.AccesoScreen
import com.example.cropmonitor.Login.RegistroScreen
import com.example.cropmonitor.auth.TokenManager
import com.example.cropmonitor.screens.cultivos.CultivoDetailScreen
import com.example.cropmonitor.screens.cultivos.GuiaScreen
import com.example.cropmonitor.screens.AjustesScreen
import com.example.cropmonitor.screens.modulos.ModulosScreen
import com.example.cropmonitor.ui.components.BottomNavBar
import com.example.cropmonitor.ui.components.Screen
import com.example.cropmonitor.ui.theme.CropMonitorTheme
import com.example.cropmonitor.screens.modulos.SensoresScreen
import com.example.cropmonitor.screens.modulos.SensorDetailScreen
import androidx.navigation.NavType
import com.example.cropmonitor.screens.cultivos.FavoritosScreen
import com.example.cropmonitor.screens.notificaciones.NotificacionConfiguracionScreen
import com.example.cropmonitor.screens.notificaciones.NotificacionesScreen
import com.example.cropmonitor.screens.recetas.RecetaDetailScreen
import com.example.cropmonitor.screens.recetas.RecetasScreen
import android.os.Build
import androidx.annotation.RequiresApi

class MainActivity : ComponentActivity() {

    private val appContainer by lazy { (application as App).container }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CropMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val tokenManager = remember { TokenManager(context) }

                    val startDestination = if (tokenManager.getToken() != null) "main_app" else "acceso"

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("acceso") {
                            AccesoScreen(
                                onLoginSuccess = {
                                    navController.navigate("main_app") {
                                        popUpTo("acceso") { inclusive = true }
                                    }
                                },
                                onCrearCuentaClick = {
                                    navController.navigate("registro")
                                },
                                appContainer = appContainer
                            )
                        }

                        composable("registro") {
                            RegistroScreen(
                                onRegistroSuccess = { navController.popBackStack() },
                                onRegresarClick = { navController.popBackStack() },
                                appContainer = appContainer
                            )
                        }

                        composable("main_app") {
                            val mainNavController = rememberNavController()
                            val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            var reloadKey by remember { mutableStateOf(0) }

                            Scaffold(
                                bottomBar = {
                                    BottomNavBar(currentRoute = currentRoute) { screen ->
                                        mainNavController.navigate(screen.route) {
                                            popUpTo(mainNavController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            ) { paddingValues ->
                                NavHost(
                                    navController = mainNavController,
                                    startDestination = Screen.Modulos.route,
                                    modifier = Modifier.padding(paddingValues)
                                ) {
                                    composable(Screen.Modulos.route) {
                                        ModulosScreen(
                                            reloadKey = reloadKey,
                                            onModuloClick = { moduloId ->
                                                mainNavController.navigate("sensores/$moduloId")
                                            },
                                            onLogoutClick = {
                                                tokenManager.clearToken()
                                                navController.navigate("acceso") {
                                                    popUpTo("main_app") { inclusive = true }
                                                }
                                            },
                                            onNotificationsClick = { mainNavController.navigate("notificaciones") },
                                            appContainer = appContainer
                                        )
                                    }

                                    // Rutas para la guía de cultivos
                                    composable(Screen.Guia.route) {
                                        GuiaScreen(
                                            reloadKey = reloadKey,
                                            appContainer = appContainer,
                                            onCultivoClick = { cultivoId ->
                                                mainNavController.navigate("cultivoDetail/$cultivoId")
                                            },
                                            onLogoutClick = {
                                                tokenManager.clearToken()
                                                navController.navigate("acceso") {
                                                    popUpTo("main_app") { inclusive = true }
                                                }
                                            },
                                            onNotificationsClick = { mainNavController.navigate("notificaciones") }
                                        )
                                    }

                                    composable(
                                        route = "cultivoDetail/{cultivoId}",
                                        arguments = listOf(navArgument("cultivoId") { type = NavType.IntType })
                                    ) { backStackEntry ->
                                        CultivoDetailScreen(
                                            appContainer = appContainer,
                                            onBackClick = {
                                                mainNavController.popBackStack()
                                            }
                                        )
                                    }

                                    // Las otras composables
                                    // Ruta para la pantalla de Favoritos
                                    composable(Screen.Favoritos.route) {
                                        FavoritosScreen(
                                            reloadKey = reloadKey,
                                            appContainer = appContainer,
                                            onCultivoClick = { cultivoId ->
                                                mainNavController.navigate("cultivoDetail/$cultivoId")
                                            },
                                            onLogoutClick = {
                                                tokenManager.clearToken()
                                                navController.navigate("acceso") {
                                                    popUpTo("main_app") { inclusive = true }
                                                }
                                            },
                                            onNotificationsClick = { mainNavController.navigate("notificaciones") } // Parámetro añadido
                                        )
                                    }
                                    // Agrega la composable para la lista de recetas
                                    composable(Screen.Recetas.route) {
                                        RecetasScreen(
                                            reloadKey = reloadKey,
                                            appContainer = appContainer,
                                            onRecetaClick = { recetaId ->
                                                mainNavController.navigate("recetaDetail/$recetaId")
                                            },
                                            onLogoutClick = {
                                                tokenManager.clearToken()
                                                navController.navigate("acceso") {
                                                    popUpTo("main_app") { inclusive = true }
                                                }
                                            },
                                            onNotificationsClick = { mainNavController.navigate("notificaciones") }
                                        )
                                    }
                                    composable(
                                        route = "recetaDetail/{recetaId}",
                                        arguments = listOf(navArgument("recetaId") { type = NavType.IntType })
                                    ) { backStackEntry ->
                                        val recetaId = backStackEntry.arguments?.getInt("recetaId")
                                        if (recetaId != null) {
                                            RecetaDetailScreen(
                                                appContainer = appContainer,
                                                recetaId = recetaId,
                                                onBackClick = {
                                                    mainNavController.popBackStack()
                                                },
                                                onNotificationsClick = { mainNavController.navigate("notificaciones") }
                                            )
                                        }
                                    }
                                    // NUEVA RUTA PARA LA PANTALLA DE NOTIFICACIONES
                                    composable("notificaciones") {
                                        NotificacionesScreen(
                                            appContainer = appContainer,
                                            onBackClick = { mainNavController.popBackStack() },
                                            onConfiguracionClick = { mainNavController.navigate("notificaciones_configuracion") }
                                        )
                                    }
                                    composable(Screen.Ajustes.route) {
                                        AjustesScreen(
                                            reloadKey = reloadKey,
                                            appContainer = appContainer,
                                            onLogoutClick = {
                                                tokenManager.clearToken()
                                                navController.navigate("acceso") {
                                                    popUpTo("main_app") { inclusive = true }
                                                }
                                            },
                                            onNotificacionConfigClick = { mainNavController.navigate("notificaciones_configuracion") }
                                        )
                                    }
                                    // Agregamos la nueva ruta para la configuración de notificaciones
                                    composable("notificaciones_configuracion") {
                                        NotificacionConfiguracionScreen(
                                            appContainer = appContainer,
                                            onBackClick = {
                                                mainNavController.popBackStack()
                                            }
                                        )
                                    }
                                    composable("sensores/{moduloId}") { backStackEntry ->
                                        val moduloId = backStackEntry.arguments?.getString("moduloId")?.toIntOrNull()
                                        if (moduloId != null) {
                                            SensoresScreen(
                                                moduloId = moduloId,
                                                onCultivoSlotClick = { medidorSlotIndex ->
                                                    mainNavController.navigate("cultivoSlot/${moduloId}/${medidorSlotIndex}")
                                                },
                                                appContainer = appContainer,
                                                onNotificationsClick = { mainNavController.navigate("notificaciones") }
                                            )
                                        }
                                    }
                                    composable("cultivoSlot/{moduloId}/{medidorSlotIndex}") { backStackEntry ->
                                        val moduloId = backStackEntry.arguments?.getString("moduloId")?.toIntOrNull()
                                        val medidorSlotIndex = backStackEntry.arguments?.getString("medidorSlotIndex")?.toIntOrNull()

                                        if (moduloId != null && medidorSlotIndex != null) {
                                            SensorDetailScreen(
                                                moduloId = moduloId,
                                                medidorSlotIndex = medidorSlotIndex,
                                                appContainer = appContainer,
                                                onNotificationsClick = { mainNavController.navigate("notificaciones") },
                                                onBackClick = { mainNavController.popBackStack() }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
