package com.example.medical.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryDark,
    secondary = AccentColor,
    tertiary = PrimaryDark,
    background = BackgroundDark,
    surface = BackgroundDark,
    surfaceVariant = BackgroundDark,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onTertiary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    error = ErrorColor,
    outline = DividerColor,
    outlineVariant = DividerColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueLight,
    secondary = AccentColor,
    tertiary = PrimaryDark,
    background = BackgroundLight,
    surface = androidx.compose.ui.graphics.Color.White,
    surfaceVariant = BackgroundLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = TextPrimaryDark,
    onTertiary = TextPrimaryDark,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    error = ErrorColor,
    outline = DividerColor,
    outlineVariant = DividerColor
)

private val GenZColorScheme = darkColorScheme(
    primary = GenZPrimary,
    secondary = GenZPrimary,
    tertiary = GenZPrimary,
    background = GenZBackground,
    surface = GenZSurface,
    onPrimary = GenZBackground,
    onSecondary = GenZBackground,
    onTertiary = GenZBackground,
    onBackground = GenZText,
    onSurface = GenZText,
)

@Composable
fun MedicalAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeName: String = "standard",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        themeName == "genz" -> GenZColorScheme
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
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
