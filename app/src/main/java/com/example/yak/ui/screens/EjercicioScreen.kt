package com.example.yak.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.EjercicioEntity
import com.example.yak.data.db.entities.ProgresoEntity
import com.example.yak.ui.theme.PrimaryGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EjercicioScreen(navController: NavController, ejercicioId: Int, prefs: YakPrefs, db: YakDatabase) {
    var ejercicio by remember { mutableStateOf<EjercicioEntity?>(null) }
    var respondido by remember { mutableStateOf(false) }
    var opcionSeleccionada by remember { mutableStateOf("") }
    
    LaunchedEffect(ejercicioId) {
        ejercicio = db.ejercicioDao().obtenerTodos().find { it.id == ejercicioId }
    }

    EjercicioScreenContent(
        ejercicio = ejercicio,
        respondido = respondido,
        opcionSeleccionada = opcionSeleccionada,
        onOpcionSelected = { opcion ->
            if (!respondido) {
                opcionSeleccionada = opcion
                respondido = true
                ejercicio?.let { ej ->
                    if (opcion == ej.respuestaCorrecta) {
                        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        db.progresoDao().insertar(
                            ProgresoEntity(0, prefs.usuarioIdLogueado, ej.id, true, format.format(Date()))
                        )
                    }
                }
            }
        },
        onVolver = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EjercicioScreenContent(
    ejercicio: EjercicioEntity?,
    respondido: Boolean,
    opcionSeleccionada: String,
    onOpcionSelected: (String) -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text(ejercicio?.titulo ?: "Ejercicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            ) 
        }
    ) { padding ->
        ejercicio?.let { ej ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ej.pregunta, 
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(48.dp))
                
                val opciones = listOf(ej.opcion1, ej.opcion2, ej.opcion3, ej.opcion4)
                
                opciones.forEach { opcion ->
                    val isCorrect = opcion == ej.respuestaCorrecta
                    val isSelected = opcionSeleccionada == opcion
                    
                    val bgColor = when {
                        !respondido -> MaterialTheme.colorScheme.surface
                        isCorrect -> Color(0xFFC8E6C9)
                        isSelected && !isCorrect -> Color(0xFFFFCDD2)
                        else -> MaterialTheme.colorScheme.surface
                    }
                    
                    val borderColor = when {
                        !respondido -> Color.LightGray
                        isCorrect -> PrimaryGreen
                        isSelected && !isCorrect -> Color.Red
                        else -> Color.LightGray
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(enabled = !respondido) { onOpcionSelected(opcion) }
                            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (respondido) 2.dp else 6.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text(opcion, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                AnimatedVisibility(
                    visible = respondido,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val wasCorrect = opcionSeleccionada == ej.respuestaCorrecta
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (wasCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (wasCorrect) "¡Excelente trabajo! 🎉" else "Casi. La respuesta era: ${ej.respuestaCorrecta}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (wasCorrect) PrimaryGreen else Color.Red,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        
                        Button(
                            onClick = onVolver,
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                        ) {
                            Text("Continuar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EjercicioScreenPreview() {
    val sampleEjercicio = EjercicioEntity(
        id = 1, lengua = "Chol", titulo = "Saludos", nivel = "Básico", 
        pregunta = "Bajche' añet?", 
        opcion1 = "¿Cómo estás?", opcion2 = "¿Dónde vas?", opcion3 = "¿Qué haces?", opcion4 = "¿Quién eres?", 
        respuestaCorrecta = "¿Cómo estás?", imagenUri = null
    )
    MaterialTheme {
        EjercicioScreenContent(
            ejercicio = sampleEjercicio,
            respondido = false,
            opcionSeleccionada = "",
            onOpcionSelected = {},
            onVolver = {}
        )
    }
}
