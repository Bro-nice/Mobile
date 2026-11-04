package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DayScoreDataDefaults
import com.example.data.EvaluationDimension
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
fun ComparisonSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("comparison_section"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "WHY DAYSCORE AI IS DIFFERENT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = SleekPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Habit Trackers vs. Compassionate AI",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekTextPrimary
            )
        }

        DayScoreDataDefaults.trackerComparisons.forEach { comparison ->
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
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = comparison.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekTextPrimary
                    )

                    // Traditional Tracker Way (Muted alert tint)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFBA1A1A).copy(alpha = 0.06f))
                            .border(1.dp, Color(0xFFBA1A1A).copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Standard Trackers:\n${comparison.traditionalWay}",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = SleekTextSecondary
                        )
                    }

                    // DayScore Way (Sleek Container Accent #FFDAD3)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SleekContainerAccent.copy(alpha = 0.7f))
                            .border(1.dp, SleekBorder, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "DayScore AI:\n${comparison.dayScoreWay}",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = SleekOnContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DimensionsExplorer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dimensions_explorer"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column {
            Text(
                text = "THE 8 EVALUATION CRITERIA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = SleekPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "How DayScore Evaluates Your Day",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap any dimension to see how our AI coach analyzes context rather than mere checklist volume.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = SleekTextSecondary
            )
        }

        DayScoreDataDefaults.dimensions.forEach { dimension ->
            DimensionCard(dimension = dimension)
        }
    }
}

@Composable
fun DimensionCard(dimension: EvaluationDimension) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) SleekContainer.copy(alpha = 0.35f) else SleekSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (expanded) SleekPrimary.copy(alpha = 0.4f) else SleekBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dimension.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekTextPrimary
                    )
                    Text(
                        text = dimension.subtitle,
                        fontSize = 12.sp,
                        color = SleekPrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = SleekTextSecondary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Core Question: \"${dimension.question}\"",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = SleekPrimary
                    )

                    Text(
                        text = dimension.description,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = SleekTextSecondary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SleekContainer)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Key Signal: ${dimension.keySignal}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SleekOnContainer
                        )
                    }
                }
            }
        }
    }
}
