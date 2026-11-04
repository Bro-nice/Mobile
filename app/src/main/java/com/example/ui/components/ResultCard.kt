package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DayEvaluationResult
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekContainer
import com.example.ui.theme.SleekContainerAccent
import com.example.ui.theme.SleekOnContainer
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekPrimaryLight
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekTextMuted
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary

@Composable
fun ResultCard(
    result: DayEvaluationResult,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onOpenWeb: () -> Unit,
    onReset: () -> Unit,
    isSaved: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scoreColor = getScoreColor(result.score)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("evaluation_result_card"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Summary Card - Sleek Interface White Card with #F5DED8 Border
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sleek Circular Gauge
                ScoreDial(
                    score = result.score,
                    emoji = result.emoji,
                    size = 180.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = result.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SleekTextPrimary,
                    modifier = Modifier.testTag("result_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = result.summary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = SleekTextSecondary,
                    modifier = Modifier.testTag("result_summary")
                )
            }
        }

        // Two-Column Grid: Strength & Tomorrow's Challenge matching Sleek Interface card pairs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First Column: Demonstrated Strength (White card with #F5DED8 border)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurface),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SleekContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Strength",
                            tint = SleekPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Key Strength",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = result.strength,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = SleekTextSecondary,
                        modifier = Modifier.testTag("result_strength")
                    )
                }
            }

            // Second Column: Tomorrow's Challenge (Rose peach #FFDAD3 container)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekContainerAccent),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SleekOnContainer.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Tomorrow Challenge",
                            tint = SleekOnContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tomorrow",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekOnContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = result.tomorrowChallenge,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = SleekOnContainer.copy(alpha = 0.85f),
                        modifier = Modifier.testTag("result_challenge")
                    )
                }
            }
        }

        // Actionable Improvements List
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SleekContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Improvements",
                            tint = SleekPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ACTIONABLE IMPROVEMENTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = SleekPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                result.improvements.forEachIndexed { index, improvement ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(SleekContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = improvement,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = SleekTextPrimary
                        )
                    }
                }
            }
        }

        // Closing Encouragement
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SleekContainer.copy(alpha = 0.5f))
                .border(1.dp, SleekBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "💬 \"${result.encouragement}\"",
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                color = SleekTextSecondary,
                modifier = Modifier.testTag("result_encouragement")
            )
        }

        // Action Buttons Row - Sleek Pill-shaped Buttons (rounded-full)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onShare,
                colors = ButtonDefaults.buttonColors(containerColor = SleekPrimary),
                shape = CircleShape,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("share_score_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share Score",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaved) SleekContainerAccent else SleekContainer
                ),
                shape = CircleShape,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("save_score_button")
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                    contentDescription = "Save",
                    tint = SleekOnContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSaved) "Saved" else "Save Day",
                    color = SleekOnContainer,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }

        // Web Link & Reset Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onOpenWeb,
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                modifier = Modifier
                    .weight(1.1f)
                    .height(50.dp)
                    .testTag("visit_dayscore_web_button")
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInBrowser,
                    contentDescription = "Open Web",
                    tint = SleekPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "dayscore.ai.studio",
                    fontSize = 12.sp,
                    color = SleekPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onReset,
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                modifier = Modifier
                    .weight(0.9f)
                    .height(50.dp)
                    .testTag("reflect_again_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reflect Again",
                    tint = SleekTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "New Day",
                    fontSize = 12.sp,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
