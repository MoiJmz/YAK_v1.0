package com.example.yak.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.EjercicioEntity

@Composable
fun RepasoScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase) {
    var ejercicios by remember { mutableStateOf<List<EjercicioEntity>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var respondido by remember { mutableStateOf(false) }
    var opcionSeleccionada by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        val completados = db.progresoDao().obtenerCompletados(prefs.usuarioIdLogueado)
        val allEjercicios = db.ejercicioDao().obtenerTodos()
        ejercicios = allEjercicios.filter { ej -> completados.any { it.ejercicioId == ej.id } }.shuffled()
    }

    RepasoScreenContent(
        ejercicios = ejercicios,
        currentIndex = currentIndex,
        respondido = respondido,
        opcionSeleccionada = opcionSeleccionada,
        onOpcionSelected = { opcion ->
            if (!respondido) {
                opcionSeleccionada = opcion
                respondido = true
            }
        },
        onNextClick = {
            respondido = false
            opcionSeleccionada = ""
            currentIndex++
        },
        onVolver = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepasoScreenContent(
    ejercicios: List<EjercicioEntity>,
    currentIndex: Int,
    respondido: Boolean,
    opcionSeleccionada: String,
    onOpcionSelected: (String) -> Unit,
    onNextClick: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Repaso") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("← Salir") }
                }
            ) 
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (ejercicios.isEmpty()) {
                Text("¡Completa algunos ejercicios primero!")
            } else if (currentIndex < ejercicios.size) {
                val ej = ejercicios[currentIndex]
                Text(ej.pregunta, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))
                
                val opciones = listOf(ej.opcion1, ej.opcion2, ej.opcion3, ej.opcion4)
                
                opciones.forEach { opcion ->
                    val color = when {
                        !respondido -> MaterialTheme.colorScheme.surface
                        opcion == ej.respuestaCorrecta -> MaterialTheme.colorScheme.primary
                        opcionSeleccionada == opcion -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.surface
                    }
                    OutlinedButton(
                        onClick = { onOpcionSelected(opcion) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = color)
                    ) {
                        Text(opcion)
                    }
                }
                
                if (respondido) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(if (opcionSeleccionada == ej.respuestaCorrecta) "✅ ¡Correcto!" else "❌ Incorrecto")
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = onNextClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (currentIndex == ejercicios.lastIndex) "Terminar" else "Siguiente")
                    }
                }
            } else {
                Text("¡Modo repaso terminado!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onVolver) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepasoScreenPreview() {
    val sampleEjercicios = listOf(
        EjercicioEntity(id=1, lengua="Chol", titulo="Saludos", nivel="Básico", pregunta="¿Hola?", opcion1="A", opcion2="B", opcion3="C", opcion4="D", respuestaCorrecta="A", imagenUri=null)
    )
    MaterialTheme {
        RepasoScreenContent(
            ejercicios = sampleEjercicios,
            currentIndex = 0,
            respondido = false,
            opcionSeleccionada = "",
            onOpcionSelected = {},
            onNextClick = {},
            onVolver = {}
        )
    }
}
