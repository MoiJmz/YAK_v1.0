package com.example.yak.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreen(navController: NavController) {
    AcercaDeScreenContent(onVolver = { navController.popBackStack() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreenContent(onVolver: () -> Unit) {
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Acerca de") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            ) 
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
                        contentDescription = "Logo YAK",
                        modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("YAK", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("YAK es una app dedicada a preservar y difundir las lenguas tradicionales del estado de Tabasco, México.")
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                Text("¿Por qué es importante?", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("La importancia cultural de las lenguas indígenas tabasqueñas.")
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                Text("Lenguas disponibles", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Chol, Maya y LSM.")
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                Text("Desarrollado por:", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Equipo YAK")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AcercaDeScreenPreview() {
    MaterialTheme {
        AcercaDeScreenContent(onVolver = {})
    }
}
