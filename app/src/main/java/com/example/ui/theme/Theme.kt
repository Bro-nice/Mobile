package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DayScoreColorScheme = lightColorScheme(
  primary = SleekPrimary,
  onPrimary = Color.White,
  primaryContainer = SleekContainer,
  onPrimaryContainer = SleekOnContainer,
  secondary = SleekPrimaryLight,
  onSecondary = Color.White,
  secondaryContainer = SleekContainerAccent,
  onSecondaryContainer = SleekOnContainer,
  tertiary = SleekOnContainer,
  onTertiary = Color.White,
  background = SleekBg,
  onBackground = SleekTextPrimary,
  surface = SleekSurface,
  onSurface = SleekTextPrimary,
  surfaceVariant = SleekContainer,
  onSurfaceVariant = SleekTextSecondary,
  outline = SleekBorder
)

@Composable
fun DayScoreTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DayScoreColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  DayScoreTheme(content = content)
}


