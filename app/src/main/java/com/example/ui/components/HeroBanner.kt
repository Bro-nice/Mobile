package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun HeroBanner(
    onOpenWeb: () -> Unit,
    onStartEvaluate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_banner_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Image Card with Sleek Rounded Edges (rounded-3xl)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                // Hero Visual Artwork
                Image(
                    painter = painterResource(id = R.drawable.hero_dayscore),
                    contentDescription = "DayScore Hero Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Smooth gradient overlay for elegant contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF201A19).copy(alpha = 0.45f),
                                    Color(0xFF201A19).copy(alpha = 0.92f)
                                )
                            )
                        )
                )

                // Live Web Badge Pill on top-right (rounded-full)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF201A19).copy(alpha = 0.85f))
                        .border(1.dp, SleekContainer, CircleShape)
                        .clickable { onOpenWeb() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("hero_web_badge")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SleekPrimaryLight)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "dayscore.ai.studio",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Sleek Interface Hero Typography: "Master your daily flow."
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Master your\ndaily flow.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 30.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "AI-powered tracking that turns your chaos into a science.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = SleekContainer
                    )
                }
            }
        }

        // Sleek Interface 2-Column Grid Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First Grid Card: AI Insights (White background, #F5DED8 border)
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
                            contentDescription = null,
                            tint = SleekPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "AI Insights",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = SleekTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Smart daily analysis without judgment.",
                        fontSize = 11.sp,
                        color = SleekTextSecondary
                    )
                }
            }

            // Second Grid Card: Live Scoring (Peach/Terracotta #FFDAD3 container)
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
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SleekOnContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Live Scoring",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = SleekOnContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Real-time feedback across 8 dimensions.",
                        fontSize = 11.sp,
                        color = SleekOnContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Value Proposition Pitch & Primary CTA Buttons
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
                Text(
                    text = "A Coach That Understands Real Life",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SleekTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "DayScore AI evaluates your day as one complete human unit — honoring effort, personal circumstances, healthy rest, and growth mindset rather than punishing broken streaks.",
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = SleekTextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Sleek Interface Rounded-Full Primary & Secondary CTA Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onStartEvaluate,
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPrimary),
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(52.dp)
                            .testTag("try_evaluator_hero_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Get Started Free",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onOpenWeb,
                        colors = ButtonDefaults.buttonColors(containerColor = SleekContainer),
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(0.9f)
                            .height(52.dp)
                            .testTag("open_web_hero_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = SleekOnContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Visit Web",
                            color = SleekOnContainer,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
