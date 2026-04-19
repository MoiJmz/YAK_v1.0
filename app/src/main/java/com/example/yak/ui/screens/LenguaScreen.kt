package com.example.yak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.EjercicioEntity
import com.example.yak.ui.theme.PrimaryGreen

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun LenguaScreen(navController: NavController, lengua: String, prefs: YakPrefs, db: YakDatabase) {
    var ejercicios by remember { mutableStateOf<List<EjercicioEntity>>(emptyList()) }
    val usuarioId = prefs.usuarioIdLogueado
    val progresoMap = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(lengua) {
        val list = db.ejercicioDao().obtenerPorLengua(lengua)
        ejercicios = list
        list.forEach { ej ->
            progresoMap[ej.id] = db.progresoDao().buscar(usuarioId, ej.id)?.completado == true
        }
    }

    LenguaScreenContent(
        lengua = lengua,
        ejercicios = ejercicios,
        progresoMap = progresoMap,
        onEjercicioClick = { id -> navController.navigate("ejercicio/$id") },
        onVolver = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LenguaScreenContent(
    lengua: String,
    ejercicios: List<EjercicioEntity>,
    progresoMap: Map<Int, Boolean>,
    onEjercicioClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val gradientColors = when (lengua) {
        "Chol" -> listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
        "Maya" -> listOf(Color(0xFF81C784), Color(0xFF388E3C))
        else -> listOf(Color(0xFFA5D6A7), Color(0xFF4CAF50))
    }
    
    val desc = when (lengua) {
        "Chol" -> "Habla de la selva y las raíces profundas."
        "Maya" -> "Idioma de sabiduría, ciencia y astronomía."
        else -> "La poderosa expresión mediante las manos."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Módulo $lengua", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.verticalGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(lengua, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, fontSize = 16.sp, color = Color(0xEEFFFFFF))
                    }
                    Image(
                        painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
                        contentDescription = "Logo YAK",
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp))
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Text("Ejercicios Disponibles", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                items(ejercicios) { ejercicio ->
                    val completado = progresoMap[ejercicio.id] == true
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onEjercicioClick(ejercicio.id) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (completado) PrimaryGreen.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (completado) Text("✅") else Text("🎯")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(ejercicio.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("Nivel: ${ejercicio.nivel}", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LenguaScreenPreview() {
    val sampleEjercicios = listOf(
        EjercicioEntity(id=1, lengua="Chol", titulo="Saludos Básicos", nivel="Básico", pregunta="", opcion1="", opcion2="", opcion3="", opcion4="", respuestaCorrecta="", imagenUri=null),
        EjercicioEntity(id=2, lengua="Chol", titulo="Números Ancestrales", nivel="Básico", pregunta="", opcion1="", opcion2="", opcion3="", opcion4="", respuestaCorrecta="", imagenUri=null)
    )
    val sampleProgreso = mapOf(1 to true, 2 to false)
    MaterialTheme {
        LenguaScreenContent(
            lengua = "Chol",
            ejercicios = sampleEjercicios,
            progresoMap = sampleProgreso,
            onEjercicioClick = {},
            onVolver = {}
        )
    }
}
