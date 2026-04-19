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

@Composable
fun ProgresoScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase) {
    val uid = prefs.usuarioIdLogueado
    var progresoMap by remember { mutableStateOf<Map<String, Pair<Int, Int>>>(emptyMap()) } // Lengua -> <completados, total>

    LaunchedEffect(Unit) {
        val lenguas = listOf("Chol", "Maya", "LSM")
        val map = mutableMapOf<String, Pair<Int, Int>>()
        val todosCompletados = db.progresoDao().obtenerCompletados(uid)
        
        for (lengua in lenguas) {
            val total = db.ejercicioDao().obtenerPorLengua(lengua)
            val completadosLen = total.count { t -> todosCompletados.any { it.ejercicioId == t.id } }
            map[lengua] = Pair(completadosLen, total.size)
        }
        progresoMap = map
    }

    ProgresoScreenContent(
        progresoMap = progresoMap,
        onVolver = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgresoScreenContent(
    progresoMap: Map<String, Pair<Int, Int>>,
    onVolver: () -> Unit
) {
    val lenguas = listOf("Chol", "Maya", "LSM")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tu progreso") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            lenguas.forEach { lengua ->
                val stats = progresoMap[lengua] ?: Pair(0, 1)
                val completados = stats.first
                val total = if (stats.second == 0) 1 else stats.second
                val progress = completados.toFloat() / total
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(lengua, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$completados de ${stats.second} ejercicios completados")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgresoScreenPreview() {
    val sampleMap = mapOf(
        "Chol" to Pair(2, 4),
        "Maya" to Pair(1, 4),
        "LSM" to Pair(4, 4)
    )
    MaterialTheme {
        ProgresoScreenContent(progresoMap = sampleMap, onVolver = {})
    }
}
