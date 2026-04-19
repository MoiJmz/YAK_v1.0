package com.example.yak.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.EjercicioEntity
import com.example.yak.data.db.entities.UsuarioEntity

@Composable
fun AdminScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase) {
    var usuarioLogueado by remember { mutableStateOf<UsuarioEntity?>(null) }
    var tabIndex by remember { mutableStateOf(0) }
    var ejercicios by remember { mutableStateOf<List<EjercicioEntity>>(emptyList()) }
    var usuarios by remember { mutableStateOf<List<UsuarioEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        usuarioLogueado = db.usuarioDao().obtenerTodos().find { it.id == prefs.usuarioIdLogueado }
        ejercicios = db.ejercicioDao().obtenerTodos()
        usuarios = db.usuarioDao().obtenerTodos()
    }

    AdminScreenContent(
        usuarioLogueado = usuarioLogueado,
        tabIndex = tabIndex,
        ejercicios = ejercicios,
        usuarios = usuarios,
        onTabSelected = { tabIndex = it },
        onVolver = { navController.popBackStack() },
        onEliminarEjercicio = { ej ->
            db.ejercicioDao().eliminar(ej)
            ejercicios = db.ejercicioDao().obtenerTodos()
        },
        onCambiarRol = { u ->
            val newRol = if (u.rol == "USUARIO") "STAFF" else "USUARIO"
            db.usuarioDao().actualizar(u.copy(rol = newRol))
            usuarios = db.usuarioDao().obtenerTodos()
        },
        onEliminarUsuario = { u ->
            db.usuarioDao().eliminar(u)
            usuarios = db.usuarioDao().obtenerTodos()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreenContent(
    usuarioLogueado: UsuarioEntity?,
    tabIndex: Int,
    ejercicios: List<EjercicioEntity>,
    usuarios: List<UsuarioEntity>,
    onTabSelected: (Int) -> Unit,
    onVolver: () -> Unit,
    onEliminarEjercicio: (EjercicioEntity) -> Unit,
    onCambiarRol: (UsuarioEntity) -> Unit,
    onEliminarUsuario: (UsuarioEntity) -> Unit
) {
    val tabs = listOf("Ejercicios", "Usuarios")

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            ) 
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (usuarioLogueado?.rol == "OWNER") {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex == index,
                            onClick = { onTabSelected(index) },
                            text = { Text(title) }
                        )
                    }
                }
            }

            if (tabIndex == 0 || usuarioLogueado?.rol == "STAFF") {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item { Text("Administrar Ejercicios", style = MaterialTheme.typography.titleLarge) }
                    items(ejercicios) { ej ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("${ej.lengua}: ${ej.titulo}")
                                Row {
                                    TextButton(onClick = { /* edit modal implementation */ }) { Text("Editar") }
                                    TextButton(onClick = { onEliminarEjercicio(ej) }) { Text("Eliminar") }
                                }
                            }
                        }
                    }
                }
            } else if (tabIndex == 1 && usuarioLogueado?.rol == "OWNER") {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item { Text("Administrar Usuarios", style = MaterialTheme.typography.titleLarge) }
                    items(usuarios) { u ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("${u.username} - Rol: ${u.rol}")
                                Row {
                                    TextButton(onClick = { onCambiarRol(u) }) { Text("Cambiar Rol") }
                                    TextButton(onClick = { onEliminarUsuario(u) }) { Text("Eliminar") }
                                }
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
fun AdminScreenPreview() {
    val sampleOwner = UsuarioEntity(id=1, username="admin", correo="a@a.com", password="", rol="OWNER", fotoPerfil=null)
    val sampleEjercicios = listOf(
        EjercicioEntity(id=1, lengua="Chol", titulo="Saludos", nivel="Básico", pregunta="?", opcion1="", opcion2="", opcion3="", opcion4="", respuestaCorrecta="", imagenUri=null)
    )
    val sampleUsuarios = listOf(
        UsuarioEntity(id=2, username="pepe", correo="p@p.com", password="", rol="USUARIO", fotoPerfil=null)
    )
    MaterialTheme {
        AdminScreenContent(
            usuarioLogueado = sampleOwner,
            tabIndex = 0,
            ejercicios = sampleEjercicios,
            usuarios = sampleUsuarios,
            onTabSelected = {},
            onVolver = {},
            onEliminarEjercicio = {},
            onCambiarRol = {},
            onEliminarUsuario = {}
        )
    }
}
