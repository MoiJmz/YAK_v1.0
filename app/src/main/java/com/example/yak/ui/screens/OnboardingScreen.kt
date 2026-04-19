package com.example.yak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yak.data.YakPrefs
import com.example.yak.ui.Routes
import com.example.yak.ui.theme.PrimaryGreen
import com.example.yak.ui.theme.PrimaryGreenDark

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun OnboardingScreen(navController: NavController, prefs: YakPrefs) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    OnboardingScreenContent(
        pagerState = pagerState,
        onComenzar = {
            prefs.onboardingCompletado = true
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.ONBOARDING) { inclusive = true }
            }
        }
    )
}

@Composable
fun OnboardingScreenContent(
    pagerState: PagerState,
    onComenzar: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF))))) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = com.example.yak.R.drawable.yaklogo),
                        contentDescription = "Logo YAK",
                        modifier = Modifier.size(330.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            val title = when(page) {
                                0 -> "Aprende lenguas tabasqueñas"
                                1 -> "Chol, Maya y LSM"
                                else -> "El poder de tu racha"
                            }
                            val subtitle = when(page) {
                                0 -> "Conecta con las raíces culturales de Tabasco practicando diario."
                                1 -> "Cada lengua ofrece un módulo único para que aprendas vocabulario auténtico."
                                else -> "Mantén tu constancia jugando cada día y desbloquea el modo repaso."
                            }
                            Text(
                                text = title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = PrimaryGreenDark
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = subtitle,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
            
            Row(
                Modifier.height(50.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { iteration ->
                    val color = if (pagerState.currentPage == iteration) PrimaryGreen else Color.LightGray
                    val size = if (pagerState.currentPage == iteration) 12.dp else 8.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(size)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (pagerState.currentPage == 2) {
                Button(
                    onClick = onComenzar,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp).height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text("¡Empezar Aventura!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(modifier = Modifier.height(92.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    MaterialTheme {
        OnboardingScreenContent(pagerState = pagerState, onComenzar = {})
    }
}
