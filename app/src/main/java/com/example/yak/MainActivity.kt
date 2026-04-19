package com.example.yak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.EjercicioEntity
import com.example.yak.data.db.entities.UsuarioEntity
import com.example.yak.ui.Routes
import com.example.yak.ui.screens.*
import com.example.yak.ui.theme.YakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = YakDatabase.getInstance(this)
        val prefs = YakPrefs(this)
        
        // Inicializar la bd si es la primera vez o si faltan ejercicios
        val currentEjercicios = database.ejercicioDao().obtenerTodos()
        if (!prefs.bdInicializada || currentEjercicios.size < 24) {
            if (currentEjercicios.isNotEmpty()) {
                database.ejercicioDao().eliminarTodo()
            }
            
            if (database.usuarioDao().obtenerTodos().isEmpty()) {
                database.usuarioDao().insertar(
                    UsuarioEntity(
                        username = "admin",
                        correo = "admin@yak.com",
                        password = "admin123",
                        rol = "OWNER",
                        fotoPerfil = null
                    )
                )
            }
            
            val ejercicios = listOf(
                // CHOL
                EjercicioEntity(lengua="Chol", titulo="Saludos básicos", nivel="Básico", pregunta="¿Cómo se dice 'Buenos días' en Chol?", opcion1="Bix a beel", opcion2="Jach ma'alob k'iin", opcion3="Pejtel lak'al", opcion4="Wokol a wale", respuestaCorrecta="Pejtel lak'al", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Saludos básicos", nivel="Básico", pregunta="¿Cómo se dice 'Gracias' en Chol?", opcion1="Yum bo'otik", opcion2="Bix a k'aaba", opcion3="Ko'one'ex", opcion4="Ja' a k'aaba", respuestaCorrecta="Yum bo'otik", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Números del 1 al 5", nivel="Básico", pregunta="¿Qué número representa 'oxib' en Chol?", opcion1="1", opcion2="2", opcion3="3", opcion4="4", respuestaCorrecta="3", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Números del 1 al 5", nivel="Básico", pregunta="¿Cómo se dice el número 5 en Chol?", opcion1="Jun", opcion2="Cha'", opcion3="Oxib", opcion4="Jo'ob", respuestaCorrecta="Jo'ob", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Animales del entorno", nivel="Intermedio", pregunta="¿Cómo se llama el jaguar en Chol?", opcion1="Chuj", opcion2="Balam", opcion3="Pek", opcion4="Mis", respuestaCorrecta="Balam", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Animales del entorno", nivel="Intermedio", pregunta="¿Cómo se dice 'perro' en Chol?", opcion1="Balam", opcion2="Mis", opcion3="Pek", opcion4="Wakax", respuestaCorrecta="Pek", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Colores", nivel="Intermedio", pregunta="¿Cómo se dice 'verde' en Chol?", opcion1="Chak", opcion2="Yax", opcion3="Ek'", opcion4="Sak", respuestaCorrecta="Yax", imagenUri=null),
                EjercicioEntity(lengua="Chol", titulo="Colores", nivel="Intermedio", pregunta="¿Qué color representa 'chak' en Chol?", opcion1="Azul", opcion2="Negro", opcion3="Rojo", opcion4="Blanco", respuestaCorrecta="Rojo", imagenUri=null),
                
                // MAYA
                EjercicioEntity(lengua="Maya", titulo="Saludos y presentaciones", nivel="Básico", pregunta="¿Cómo se dice 'Hola' o saludo general en Maya?", opcion1="Bix a beel", opcion2="Ma'alob", opcion3="Ko'one'ex", opcion4="Taan in kaajal", respuestaCorrecta="Bix a beel", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Saludos y presentaciones", nivel="Básico", pregunta="¿Cómo se pregunta 'Cómo te llamas?' en Maya?", opcion1="Bix a beel", opcion2="Ba'ax ka wa'alik", opcion3="Bix a k'aaba", opcion4="Ma'alob k'iin", respuestaCorrecta="Bix a k'aaba", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Días de la semana", nivel="Básico", pregunta="¿Cómo se dice 'lunes' en Maya?", opcion1="Junp'el k'iin", opcion2="Luunes", opcion3="K'in chich", opcion4="Saamal", respuestaCorrecta="Luunes", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Días de la semana", nivel="Básico", pregunta="¿Cómo se dice 'mañana' (día siguiente) en Maya?", opcion1="Bejla'e'", opcion2="Saamal", opcion3="Ichil", opcion4="Naachil", respuestaCorrecta="Saamal", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Comida tradicional", nivel="Intermedio", pregunta="¿Cómo se dice 'tortilla' en Maya?", opcion1="Waaj", opcion2="Kool", opcion3="Pibil", opcion4="Sikil", respuestaCorrecta="Waaj", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Comida tradicional", nivel="Intermedio", pregunta="¿Qué significa 'kool' en Maya?", opcion1="Tortilla", opcion2="Atole de maíz", opcion3="Tamales", opcion4="Frijoles", respuestaCorrecta="Atole de maíz", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Frases cotidianas", nivel="Intermedio", pregunta="¿Cómo se dice 'No entiendo' en Maya?", opcion1="In wohelel", opcion2="Ma' in najtal", opcion3="Bix a beel", opcion4="Taan in bin", respuestaCorrecta="Ma' in najtal", imagenUri=null),
                EjercicioEntity(lengua="Maya", titulo="Frases cotidianas", nivel="Intermedio", pregunta="¿Cómo se dice 'Me voy' en Maya?", opcion1="Taan in bin", opcion2="Ko'one'ex", opcion3="Naachil", opcion4="Yum bo'otik", respuestaCorrecta="Taan in bin", imagenUri=null),
                
                // LSM
                EjercicioEntity(lengua="LSM", titulo="Abecedario básico", nivel="Básico", pregunta="¿Cómo se representa la letra 'A' en LSM?", opcion1="Mano cerrada con pulgar fuera", opcion2="Mano en forma de garra", opcion3="Mano plana hacia arriba", opcion4="Dedos índice y medio cruzados", respuestaCorrecta="Mano cerrada con pulgar fuera", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Abecedario básico", nivel="Básico", pregunta="¿Cómo se hace la letra 'C' en LSM?", opcion1="Dedos en círculo abierto", opcion2="Mano plana de lado", opcion3="Mano en puño", opcion4="Dedos extendidos en V", respuestaCorrecta="Dedos en círculo abierto", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Saludos comunes", nivel="Básico", pregunta="¿Cuál es la seña para decir 'Hola'?", opcion1="Mano en la frente hacia afuera", opcion2="Mano en el pecho", opcion3="Dedos en la barbilla", opcion4="Palma hacia abajo", respuestaCorrecta="Mano en la frente hacia afuera", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Saludos comunes", nivel="Básico", pregunta="¿Cómo se dice 'Gracias' en LSM?", opcion1="Dedo medio toca palma y sube", opcion2="Palma golpea el pecho", opcion3="Mano sobre la boca", opcion4="Dedos cruzados", respuestaCorrecta="Dedo medio toca palma y sube", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Números del 1 al 5", nivel="Intermedio", pregunta="¿Cuál es la seña para el número 3?", opcion1="Pulgar, índice y medio extendidos", opcion2="Índice, medio y anular", opcion3="Mano abierta completa", opcion4="Puño cerrado", respuestaCorrecta="Pulgar, índice y medio extendidos", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Números del 1 al 5", nivel="Intermedio", pregunta="¿Cómo se diferencia el 2 del V en LSM?", opcion1="2 palma hacia uno, V hacia fuera", opcion2="No hay diferencia", opcion3="Se usan las dos manos", opcion4="Se mueve la mano", respuestaCorrecta="2 palma hacia uno, V hacia fuera", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Cosas del hogar", nivel="Intermedio", pregunta="¿Cuál es la seña para 'Casa'?", opcion1="Manos juntas formando un techo", opcion2="Mano en forma de círculo", opcion3="Palma plana sobre la mesa", opcion4="Dedos entrelazados", respuestaCorrecta="Manos juntas formando un techo", imagenUri=null),
                EjercicioEntity(lengua="LSM", titulo="Cosas del hogar", nivel="Intermedio", pregunta="¿Cómo se dice 'Comer' en LSM?", opcion1="Mano en capullo hacia la boca", opcion2="Mano plana moviéndose", opcion3="Dedo en la mejilla", opcion4="Puño golpeando la palma", respuestaCorrecta="Mano en capullo hacia la boca", imagenUri=null)
            )
            ejercicios.forEach { database.ejercicioDao().insertar(it) }
            prefs.bdInicializada = true
        }

        setContent {
            var darkTheme by remember { mutableStateOf(prefs.modoOscuro) }
            // Observe dark theme changes by passing callback to UI if needed,
            // or just rely on state hoisting
            YakTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val startDest = when {
                        !prefs.onboardingCompletado -> Routes.ONBOARDING
                        prefs.usuarioIdLogueado != -1 -> Routes.PRINCIPAL
                        else -> Routes.LOGIN
                    }

                    NavHost(navController = navController, startDestination = startDest) {
                        composable(Routes.ONBOARDING) {
                            OnboardingScreen(navController, prefs)
                        }
                        composable(Routes.LOGIN) {
                            LoginScreen(navController, prefs, database)
                        }
                        composable(Routes.REGISTRO) {
                            RegistroScreen(navController, database)
                        }
                        composable(Routes.PRINCIPAL) {
                            PrincipalScreen(navController, prefs, database)
                        }
                        composable(
                            route = Routes.LENGUA,
                            arguments = listOf(navArgument("lengua") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val lengua = backStackEntry.arguments?.getString("lengua") ?: ""
                            LenguaScreen(navController, lengua, prefs, database)
                        }
                        composable(
                            route = Routes.EJERCICIO,
                            arguments = listOf(navArgument("ejercicioId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val ejercicioId = backStackEntry.arguments?.getInt("ejercicioId") ?: 0
                            EjercicioScreen(navController, ejercicioId, prefs, database)
                        }
                        composable(Routes.REPASO) {
                            RepasoScreen(navController, prefs, database)
                        }
                        composable(Routes.PROGRESO) {
                            ProgresoScreen(navController, prefs, database)
                        }
                        composable(Routes.PERFIL) {
                            PerfilScreen(
                                navController = navController, 
                                prefs = prefs, 
                                db = database,
                                onThemeToggle = { isDark ->
                                    darkTheme = isDark
                                }
                            )
                        }
                        composable(Routes.ACERCA_DE) {
                            AcercaDeScreen(navController)
                        }
                        composable(Routes.ADMIN) {
                            AdminScreen(navController, prefs, database)
                        }
                    }
                }
            }
        }
    }
}