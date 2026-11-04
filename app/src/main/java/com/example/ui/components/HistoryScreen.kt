package com.example.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorySection(
    history: List<DayEvaluationResult>,
    onSelectDay: (DayEvaluationResult) -> Unit,
    onDeleteDay: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val averageScore = if (history.isNotEmpty()) {
        history.map { it.score }.average().toInt()
    } else 0

    val streakDays = history.size.coerceAtLeast(1)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("history_section"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "YOUR REFLECTION JOURNEY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = SleekPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Past Reflections & Progress",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekTextPrimary
            )
        }

        // Stats Row (matching Sleek Interface 2-column cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Reflection Streak",
                value = "$streakDays Days",
                icon = Icons.Default.LocalFireDepartment,
                iconColor = SleekPrimary,
                modifier = Modifier.weight(1f)
            )

            StatsCard(
                title = "Average Score",
                value = "$averageScore / 100",
                icon = Icons.Default.TrendingUp,
                iconColor = SleekPrimaryLight,
                modifier = Modifier.weight(1f)
            )
        }

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SleekSurface)
                    .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No reflections saved yet. Try evaluating your day in the live AI sandbox above!",
                    fontSize = 13.sp,
                    color = SleekTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                history.forEach { item ->
                    HistoryItemCard(
                        item = item,
                        onClick = { onSelectDay(item) },
                        onDelete = { onDeleteDay(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        modifier = modifier.border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = SleekTextSecondary
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SleekContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekTextPrimary
            )
        }
    }
}

@Composable
fun HistoryItemCard(
    item: DayEvaluationResult,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val scoreColor = getScoreColor(item.score)
    val dateStr = remember(item.timestamp) {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        sdf.format(Date(item.timestamp))
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .testTag("history_item_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score Pill in Sleek Container
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(SleekContainer)
                    .border(1.dp, SleekBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.score.toString(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scoreColor
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.emoji,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekTextPrimary,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = SleekTextMuted
                )

                if (item.tomorrowChallenge.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Challenge: ${item.tomorrowChallenge}",
                        fontSize = 11.sp,
                        color = SleekPrimary,
                        maxLines = 1
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = SleekTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
