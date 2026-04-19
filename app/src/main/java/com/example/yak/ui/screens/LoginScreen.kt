package com.example.yak.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.data.db.YakDatabase
import com.example.yak.ui.Routes

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun LoginScreen(navController: NavController, prefs: YakPrefs, db: YakDatabase) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    LoginScreenContent(
        username = username,
        password = password,
        passwordVisible = passwordVisible,
        errorMsg = errorMsg,
        onUsernameChange = { username = it },
        onPasswordChange = { password = it },
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        onLoginClick = {
            val user = db.usuarioDao().login(username, password)
            if (user != null) {
                prefs.usuarioIdLogueado = user.id
                prefs.usuarioNombreLogueado = user.username
                navController.navigate(Routes.PRINCIPAL) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            } else {
                errorMsg = "Usuario o contraseña incorrectos"
            }
        },
        onNavigateRegistro = { navController.navigate(Routes.REGISTRO) }
    )
}

@Composable
fun LoginScreenContent(
    username: String,
    password: String,
    passwordVisible: Boolean,
    errorMsg: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateRegistro: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
            contentDescription = "Logo YAK",
            modifier = Modifier.size(300.dp).padding(bottom = 16.dp)
        )
        Text(
            text = "",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = onTogglePasswordVisibility) {
                    Text(if (passwordVisible) "Ocultar" else "Mostrar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onNavigateRegistro) {
            Text("¿No tienes cuenta? Crea una")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            username = "",
            password = "",
            passwordVisible = false,
            errorMsg = "",
            onUsernameChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onNavigateRegistro = {}
        )
    }
}
