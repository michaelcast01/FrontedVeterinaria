package com.example.veterinaria.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.veterinaria.ui.theme.VeterinariaShapes

private val DarkColorScheme = darkColorScheme(
    primary = VetGreen,
    onPrimary = OnPrimary,
    primaryContainer = VetGreenLight,
    onPrimaryContainer = OnSurface,
    secondary = VetTeal,
    secondaryContainer = VetBlue,
    tertiary = VetAccent,
    surface = SurfaceDark,
    onSurface = OnPrimary,
    background = SurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = VetGreen,
    onPrimary = OnPrimary,
    primaryContainer = VetGreenLight,
    onPrimaryContainer = OnSurface,
    secondary = VetTeal,
    secondaryContainer = VetBlue,
    tertiary = VetAccent,
    surface = SurfaceLight,
    onSurface = OnSurface,
    background = SurfaceLight
)

@Composable
fun VeterinariaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but disabled by default here to keep the app palette consistent
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = VeterinariaShapes,
        content = content
    )
}