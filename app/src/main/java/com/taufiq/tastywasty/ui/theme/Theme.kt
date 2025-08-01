package com.taufiq.tastywasty.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EcoColorScheme = lightColorScheme(
    primary = Color(0xFF388E3C),       // Forest Green
    onPrimary = Color.White,
    secondary = Color(0xFF66BB6A),     // Light Green
    onSecondary = Color.Black,
    tertiary = Color(0xFFA5D6A7),      // Mint
    onTertiary = Color.Black,
    background = Color(0xFFF1F8E9),    // Light green background
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun SaveFoodTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EcoColorScheme,
        typography = Typography(),
        content = content
    )
}
