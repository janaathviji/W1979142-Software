// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// This is the theme of the application

// Light colour scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryCyan,
    primaryContainer = CardLight,

    secondary = SecondaryBlue,
    secondaryContainer = SecondaryBlue,

    surfaceVariant = CardLight,

)

// Dark colour scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNavy,

    secondary = SecondaryBlue,

    secondaryContainer = SecondaryBlue,

)


// This function sets the app’s theme.
// It checks if the system is in dark mode and chooses either
// a dark or light colour scheme accordingly.
// Then it applies that colour scheme to all UI content passed into it.

@Composable
fun DermoInspectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}