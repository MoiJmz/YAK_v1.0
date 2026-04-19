package com.example.yak.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yak.data.db.YakDatabase
import com.example.yak.data.db.entities.UsuarioEntity
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun RegistroScreen(navController: NavController, db: YakDatabase) {
    var username by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    RegistroScreenContent(
        username = username,
        correo = correo,
        password = password,
        confirmPassword = confirmPassword,
        errorMsg = errorMsg,
        onUsernameChange = { username = it },
        onCorreoChange = { correo = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegistroClick = {
            when {
                username.isBlank() || correo.isBlank() || password.isBlank() -> {
                    errorMsg = "Todos los campos son obligatorios"
                }
                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                    errorMsg = "Correo no válido"
                }
                password.length < 6 -> {
                    errorMsg = "La contraseña debe tener mínimo 6 caracteres"
                }
                password != confirmPassword -> {
                    errorMsg = "Las contraseñas no coinciden"
                }
                else -> {
                    val existing = db.usuarioDao().buscarPorUsername(username)
                    if (existing != null) {
                        errorMsg = "El usuario ya existe"
                    } else {
                        db.usuarioDao().insertar(
                            UsuarioEntity(
                                username = username,
                                correo = correo,
                                password = password,
                                rol = "USUARIO",
                                fotoPerfil = null
                            )
                        )
                        navController.popBackStack()
                    }
                }
            }
        }
    )
}

@Composable
fun RegistroScreenContent(
    username: String,
    correo: String,
    password: String,
    confirmPassword: String,
    errorMsg: String,
    onUsernameChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegistroClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
            contentDescription = "Logo YAK",
            modifier = Modifier.size(250.dp).padding(bottom = 16.dp)
        )
        Text("¡Bienvenido a YAK!", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo electrónico") },
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
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Button(
            onClick = onRegistroClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    MaterialTheme {
        RegistroScreenContent(
            username = "",
            correo = "",
            password = "",
            confirmPassword = "",
            errorMsg = "",
            onUsernameChange = {},
            onCorreoChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegistroClick = {}
        )
    }
}
