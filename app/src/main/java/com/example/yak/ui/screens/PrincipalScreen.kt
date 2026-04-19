package com.example.yak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.yak.data.db.entities.RachaEntity
import com.example.yak.ui.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.yak.ui.theme.PrimaryGreen
import com.example.yak.ui.theme.PrimaryGreenDark

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun PrincipalScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase) {
    val usuarioNombre = prefs.usuarioNombreLogueado ?: "Usuario"
    val usuarioId = prefs.usuarioIdLogueado
    var rachaDias by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hoyStr = format.format(Date())
        val rachaActual = db.rachaDao().obtener(usuarioId)
        
        if (rachaActual != null) {
            val dias = if (rachaActual.ultimaFecha != hoyStr) rachaActual.dias + 1 else rachaActual.dias
            db.rachaDao().guardar(RachaEntity(usuarioId, dias, hoyStr))
            rachaDias = dias
        } else {
            db.rachaDao().guardar(RachaEntity(usuarioId, 1, hoyStr))
            rachaDias = 1
        }
    }

    PrincipalScreenContent(
        usuarioNombre = usuarioNombre,
        rachaDias = rachaDias,
        onNavigateLengua = { navController.navigate("lengua/$it") },
        onNavigateProgreso = { navController.navigate(Routes.PROGRESO) },
        onNavigatePerfil = { navController.navigate(Routes.PERFIL) },
        onNavigateRepaso = { navController.navigate(Routes.REPASO) }
    )
}

@Composable
fun PrincipalScreenContent(
    usuarioNombre: String,
    rachaDias: Int,
    onNavigateLengua: (String) -> Unit,
    onNavigateProgreso: () -> Unit,
    onNavigatePerfil: () -> Unit,
    onNavigateRepaso: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = { Text("Inicio", fontWeight = FontWeight.Bold) },
                    icon = { Text("🏠", fontSize = 24.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateProgreso,
                    label = { Text("Progreso") },
                    icon = { Text("📈", fontSize = 24.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigatePerfil,
                    label = { Text("Perfil") },
                    icon = { Text("👤", fontSize = 24.sp) }
                )
            }
        }
    ) { padding ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "¡Hola, $usuarioNombre!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreenDark
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("¿Qué aprenderemos hoy?", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray))
            }
            Image(
                painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
                contentDescription = "Logo YAK",
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp))
            )
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(28.dp))
            
            // Premium Racha Card with Gradient
            Card(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xFFFFFDE7), Color(0xFFFFF59D)))), 
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔥 $rachaDias días de racha", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFEF6C00))
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
            
            // Distinct Language Cards
            LanguageCard("Chol", "Descubre el eco de la selva", listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))) { onNavigateLengua("Chol") }
            Spacer(modifier = Modifier.height(16.dp))
            
            LanguageCard("Maya", "La sabiduría peninsular", listOf(Color(0xFF81C784), Color(0xFF388E3C))) { onNavigateLengua("Maya") }
            Spacer(modifier = Modifier.height(16.dp))
            
            LanguageCard("LSM", "Comunícate con las manos", listOf(Color(0xFFA5D6A7), Color(0xFF4CAF50))) { onNavigateLengua("LSM") }
            
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onNavigateRepaso,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("📚  Modo Repaso", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LanguageCard(titulo: String, subtitulo: String, gradient: List<Color>, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(86.dp).clip(RoundedCornerShape(20.dp)).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Brush.linearGradient(gradient)).padding(horizontal = 24.dp), 
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(titulo, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text(subtitulo, fontSize = 14.sp, color = Color(0xEEFFFFFF))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrincipalScreenPreview() {
    PrincipalScreenContent(
        usuarioNombre = "Amigo",
        rachaDias = 15,
        onNavigateLengua = {},
        onNavigateProgreso = {},
        onNavigatePerfil = {},
        onNavigateRepaso = {}
    )
}
