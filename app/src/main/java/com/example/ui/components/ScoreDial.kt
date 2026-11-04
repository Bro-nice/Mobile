package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ScoreTierDecent
import com.example.ui.theme.ScoreTierDifficult
import com.example.ui.theme.ScoreTierExceptional
import com.example.ui.theme.ScoreTierStrong
import com.example.ui.theme.ScoreTierWeak
import com.example.ui.theme.SleekContainer
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekTextMuted
import com.example.ui.theme.SleekTextSecondary

fun getScoreColor(score: Int): Color {
    return when (score) {
        in 90..100 -> ScoreTierExceptional
        in 75..89 -> ScoreTierStrong
        in 60..74 -> ScoreTierDecent
        in 40..59 -> ScoreTierWeak
        else -> ScoreTierDifficult
    }
}

fun getScoreTierLabel(score: Int): String {
    return when (score) {
        in 90..100 -> "Exceptional Day"
        in 75..89 -> "Strong Day"
        in 60..74 -> "Decent Day"
        in 40..59 -> "Weak Day"
        else -> "Very Difficult Day"
    }
}

@Composable
fun ScoreDial(
    score: Int,
    emoji: String,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
    strokeWidth: Dp = 10.dp,
    showTierLabel: Boolean = true
) {
    val animatedScore = remember { Animatable(0f) }
    val targetScore = score.toFloat().coerceIn(0f, 100f)
    val scoreColor = getScoreColor(score)

    LaunchedEffect(score) {
        animatedScore.animateTo(
            targetValue = targetScore,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.testTag("score_dial_container")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            Canvas(
                modifier = Modifier
                    .size(size)
                    .testTag("score_canvas")
            ) {
                val strokePx = strokeWidth.toPx()
                val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
                val topLeft = Offset(strokePx / 2, strokePx / 2)

                // Background track matching Sleek Interface (#F5DED8)
                drawArc(
                    color = SleekContainer,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )

                // Foreground active sweep matching Sleek Interface (#9C432F)
                val sweepAngle = (animatedScore.value / 100f) * 360f
                drawArc(
                    color = scoreColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }

            // Central Score Number & Emoji in Sleek Interface typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp,
                    modifier = Modifier.testTag("score_emoji")
                )
                Text(
                    text = animatedScore.value.toInt().toString(),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Light,
                    color = scoreColor,
                    modifier = Modifier.testTag("score_number")
                )
                Text(
                    text = "DAILY PEAK",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = SleekTextSecondary.copy(alpha = 0.75f)
                )
            }
        }

        if (showTierLabel) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SleekContainer)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = getScoreTierLabel(score),
                    color = scoreColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.testTag("score_tier_label")
                )
            }
        }
    }
}
