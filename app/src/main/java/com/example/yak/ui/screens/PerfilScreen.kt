package com.example.yak.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.UsuarioEntity
import com.example.yak.ui.Routes

@Composable
fun PerfilScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase, onThemeToggle: (Boolean) -> Unit) {
    val uid = prefs.usuarioIdLogueado
    var usuario by remember { mutableStateOf<UsuarioEntity?>(null) }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var racha by remember { mutableStateOf(0) }
    var modoOscuro by remember { mutableStateOf(prefs.modoOscuro) }
    
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            usuario?.let { u ->
                val updated = u.copy(fotoPerfil = it.toString())
                db.usuarioDao().actualizar(updated)
                usuario = updated
            }
        }
    }

    LaunchedEffect(Unit) {
        val u = db.usuarioDao().obtenerTodos().find { it.id == uid }
        if (u != null) {
            usuario = u
            correo = u.correo
            password = u.password
        }
        racha = db.rachaDao().obtener(uid)?.dias ?: 0
    }

    PerfilScreenContent(
        usuario = usuario,
        correo = correo,
        password = password,
        racha = racha,
        modoOscuro = modoOscuro,
        onCorreoChange = { correo = it },
        onPasswordChange = { password = it },
        onChangeFoto = { galleryLauncher.launch("image/*") },
        onModoOscuroToggle = { isDark ->
            modoOscuro = isDark
            prefs.modoOscuro = isDark
            onThemeToggle(isDark)
        },
        onGuardar = {
            usuario?.let { u ->
                val updated = u.copy(correo = correo, password = password)
                db.usuarioDao().actualizar(updated)
                usuario = updated
            }
        },
        onNavigateAdmin = { navController.navigate(Routes.ADMIN) },
        onNavigateAcercaDe = { navController.navigate(Routes.ACERCA_DE) },
        onCerrarSesion = {
            prefs.usuarioIdLogueado = -1
            prefs.usuarioNombreLogueado = null
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.PRINCIPAL) { inclusive = true }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreenContent(
    usuario: UsuarioEntity?,
    correo: String,
    password: String,
    racha: Int,
    modoOscuro: Boolean,
    onCorreoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChangeFoto: () -> Unit,
    onModoOscuroToggle: (Boolean) -> Unit,
    onGuardar: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onNavigateAcercaDe: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Perfil") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (usuario?.fotoPerfil != null) {
                AsyncImage(
                    model = usuario.fotoPerfil,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(96.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier.size(96.dp)) {
                    Text("No Foto", modifier = Modifier.align(Alignment.Center))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onChangeFoto) {
                Text("Cambiar Foto")
            }
            
            Text("Usuario: ${usuario?.username ?: ""}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("🔥 Racha: $racha días")
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Modo Oscuro")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = modoOscuro, 
                    onCheckedChange = onModoOscuroToggle
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onGuardar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (usuario?.rol == "OWNER" || usuario?.rol == "STAFF") {
                TextButton(onClick = onNavigateAdmin) { Text("Panel Admin") }
            }
            TextButton(onClick = onNavigateAcercaDe) { Text("Acerca de") }
            
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    val sampleUser = UsuarioEntity(id=1, username="Guillermo", correo="g@g.com", password="password", rol="OWNER", fotoPerfil=null)
    MaterialTheme {
        PerfilScreenContent(
            usuario = sampleUser,
            correo = sampleUser.correo,
            password = sampleUser.password,
            racha = 10,
            modoOscuro = false,
            onCorreoChange = {},
            onPasswordChange = {},
            onChangeFoto = {},
            onModoOscuroToggle = {},
            onGuardar = {},
            onNavigateAdmin = {},
            onNavigateAcercaDe = {},
            onCerrarSesion = {}
        )
    }
}
