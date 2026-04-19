package com.example.yak.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    secondary = AccentYellow,
    tertiary = PrimaryGreenLight,
    background = PremiumDark, 
    surface = PremiumSurfaceDark,
    onPrimary = SecondaryWhite,
    onSecondary = PremiumDark,
    onTertiary = PremiumDark,
    onBackground = SecondaryWhite,
    onSurface = SecondaryWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = AccentYellow,
    tertiary = PrimaryGreenDark,
    background = BackgroundSoft,
    surface = SecondaryWhite,
    onPrimary = SecondaryWhite,
    onSecondary = TextDark,
    onTertiary = SecondaryWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun YakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // uses default for now, will enhance typography directly on screens
        content = content
    )
}