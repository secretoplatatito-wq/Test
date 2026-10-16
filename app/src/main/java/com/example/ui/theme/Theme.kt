package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TacticalColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = TacticalNavyDark,
    primaryContainer = TacticalNavyCard,
    onPrimaryContainer = CyberCyan,
    secondary = TacticalAmber,
    onSecondary = TacticalNavyDark,
    secondaryContainer = TacticalNavyBorder,
    onSecondaryContainer = TacticalAmber,
    tertiary = TacticalGreen,
    onTertiary = TacticalNavyDark,
    error = TacticalRed,
    onError = TacticalNavyDark,
    background = TacticalNavyDark,
    onBackground = TacticalTextPrimary,
    surface = TacticalNavySurface,
    onSurface = TacticalTextPrimary,
    surfaceVariant = TacticalNavyCard,
    onSurfaceVariant = TacticalTextSecondary,
    outline = TacticalNavyBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = TacticalNavyDark.toArgb()
                window.navigationBarColor = TacticalNavyDark.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = TacticalColorScheme,
        typography = Typography,
        content = content
    )
}
