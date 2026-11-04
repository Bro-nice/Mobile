package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.DayScoreDataDefaults
import com.example.data.SampleScenario
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
fun InteractiveEvaluator(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    isLoading: Boolean,
    onEvaluate: () -> Unit,
    onSelectScenario: (SampleScenario) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_evaluator_section"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Title & Guidance
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(SleekContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = SleekPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI REFLECTION ENGINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = SleekPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Describe Your Day Freely",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Type casually or pick a real scenario below. Study, work, rest, emotions, emergencies, or distractions — DayScore balances it all fairly.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = SleekTextSecondary
            )
        }

        // Scenario Quick-Pick Chips
        Column {
            Text(
                text = "TRY REALISTIC SCENARIOS:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = SleekTextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DayScoreDataDefaults.sampleScenarios.forEach { scenario ->
                    ScenarioChip(
                        scenario = scenario,
                        onClick = { onSelectScenario(scenario) }
                    )
                }
            }
        }

        // Multi-line Input Card (rounded-3xl white surface with #F5DED8 border)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChange,
                    placeholder = {
                        Text(
                            text = "Example: Studied 3 hours for finals, worked out, took family member to clinic, rested with tea, scrolled social media for 40 minutes...",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = SleekTextMuted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .testTag("day_input_field"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPrimary,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        cursorColor = SleekPrimary,
                        focusedContainerColor = SleekSurface,
                        unfocusedContainerColor = SleekSurface
                    ),
                    trailingIcon = {
                        if (inputText.isNotEmpty()) {
                            IconButton(onClick = { onInputTextChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear input",
                                    tint = SleekTextSecondary
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${inputText.length} chars",
                        fontSize = 11.sp,
                        color = SleekTextMuted
                    )

                    Text(
                        text = "Supports any language & informal notes",
                        fontSize = 11.sp,
                        color = SleekPrimaryLight
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button - Sleek Pill-shaped (#9C432F with white text)
                Button(
                    onClick = onEvaluate,
                    enabled = inputText.isNotBlank() && !isLoading,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPrimary,
                        disabledContainerColor = SleekContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("evaluate_day_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Evaluating Day with AI...",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Calculate My DayScore",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioChip(
    scenario: SampleScenario,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SleekSurface)
            .border(1.dp, SleekBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("scenario_chip_${scenario.id}")
    ) {
        Column {
            Text(
                text = scenario.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = SleekTextPrimary
            )
            Text(
                text = scenario.badge,
                fontSize = 10.sp,
                color = SleekPrimary
            )
        }
    }
}
