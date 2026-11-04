package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.DayScoreViewModel
import com.example.ui.components.ComparisonSection
import com.example.ui.components.DimensionsExplorer
import com.example.ui.components.HeroBanner
import com.example.ui.components.HistorySection
import com.example.ui.components.InteractiveEvaluator
import com.example.ui.components.ResultCard
import com.example.ui.theme.DayScoreTheme
import com.example.ui.theme.SleekBackground
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
import com.example.util.DayScoreIntents

class MainActivity : ComponentActivity() {

    private val viewModel: DayScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DayScoreTheme {
                DayScoreApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScoreApp(viewModel: DayScoreViewModel) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentResult by viewModel.currentResult.collectAsState()
    val isSaved by viewModel.isCurrentSaved.collectAsState()
    val history by viewModel.history.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dayscore_main_scaffold"),
        containerColor = SleekBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_brand_header")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SleekContainer)
                                .border(1.dp, SleekBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = SleekPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "DayScore",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SleekTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SleekContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "AI",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekPrimary
                                    )
                                }
                            }
                            Text(
                                text = "dayscore.ai.studio",
                                fontSize = 11.sp,
                                color = SleekTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    // Quick Web Visit Button
                    IconButton(
                        onClick = { DayScoreIntents.openWebsite(context) },
                        modifier = Modifier.testTag("top_web_link_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Open Web App",
                            tint = SleekPrimary
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = { DayScoreIntents.shareApp(context) },
                        modifier = Modifier.testTag("top_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share DayScore",
                            tint = SleekTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SleekBackground,
                    titleContentColor = SleekTextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SleekSurface,
                modifier = Modifier
                    .border(1.dp, SleekBorder)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == AppTab.SHOWCASE,
                    onClick = { viewModel.selectTab(AppTab.SHOWCASE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Overview"
                        )
                    },
                    label = { Text("Overview") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SleekPrimary,
                        selectedTextColor = SleekPrimary,
                        indicatorColor = SleekContainer,
                        unselectedIconColor = SleekTextSecondary,
                        unselectedTextColor = SleekTextSecondary
                    ),
                    modifier = Modifier.testTag(AppTab.SHOWCASE.testTag)
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.EVALUATE,
                    onClick = { viewModel.selectTab(AppTab.EVALUATE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Coach"
                        )
                    },
                    label = { Text("AI Coach") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SleekPrimary,
                        selectedTextColor = SleekPrimary,
                        indicatorColor = SleekContainer,
                        unselectedIconColor = SleekTextSecondary,
                        unselectedTextColor = SleekTextSecondary
                    ),
                    modifier = Modifier.testTag(AppTab.EVALUATE.testTag)
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.HISTORY,
                    onClick = { viewModel.selectTab(AppTab.HISTORY) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Reflections"
                        )
                    },
                    label = { Text("Reflections") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SleekPrimary,
                        selectedTextColor = SleekPrimary,
                        indicatorColor = SleekContainer,
                        unselectedIconColor = SleekTextSecondary,
                        unselectedTextColor = SleekTextSecondary
                    ),
                    modifier = Modifier.testTag(AppTab.HISTORY.testTag)
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.DIMENSIONS,
                    onClick = { viewModel.selectTab(AppTab.DIMENSIONS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "8 Criteria"
                        )
                    },
                    label = { Text("8 Criteria") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SleekPrimary,
                        selectedTextColor = SleekPrimary,
                        indicatorColor = SleekContainer,
                        unselectedIconColor = SleekTextSecondary,
                        unselectedTextColor = SleekTextSecondary
                    ),
                    modifier = Modifier.testTag(AppTab.DIMENSIONS.testTag)
                )
            }
        }
    ) { innerPadding ->
        Surface(
            color = SleekBackground,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_content_animation"
            ) { tab ->
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (tab) {
                        AppTab.SHOWCASE -> {
                            // Hero Banner with Call to Action to Web and AI Coach
                            HeroBanner(
                                onOpenWeb = { DayScoreIntents.openWebsite(context) },
                                onStartEvaluate = { viewModel.selectTab(AppTab.EVALUATE) }
                            )

                            // Interactive Live AI Sandbox right in showcase
                            InteractiveEvaluator(
                                inputText = inputText,
                                onInputTextChange = { viewModel.onInputTextChanged(it) },
                                isLoading = isLoading,
                                onEvaluate = { viewModel.evaluateCurrentDay() },
                                onSelectScenario = { viewModel.loadScenario(it) }
                            )

                            // Show Result Card if evaluated
                            currentResult?.let { res ->
                                ResultCard(
                                    result = res,
                                    onSave = { viewModel.saveCurrentEvaluation() },
                                    onShare = { DayScoreIntents.shareResult(context, res) },
                                    onOpenWeb = { DayScoreIntents.openWebsite(context) },
                                    onReset = { viewModel.resetEvaluation() },
                                    isSaved = isSaved
                                )
                            }

                            // Comparisons Section
                            ComparisonSection()

                            // Persistent Web Ecosystem Banner
                            WebEcosystemCard(
                                onOpenWeb = { DayScoreIntents.openWebsite(context) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        AppTab.EVALUATE -> {
                            // Focus on DayScore AI reflection engine
                            if (currentResult == null) {
                                InteractiveEvaluator(
                                    inputText = inputText,
                                    onInputTextChange = { viewModel.onInputTextChanged(it) },
                                    isLoading = isLoading,
                                    onEvaluate = { viewModel.evaluateCurrentDay() },
                                    onSelectScenario = { viewModel.loadScenario(it) }
                                )
                            } else {
                                ResultCard(
                                    result = currentResult!!,
                                    onSave = { viewModel.saveCurrentEvaluation() },
                                    onShare = { DayScoreIntents.shareResult(context, currentResult!!) },
                                    onOpenWeb = { DayScoreIntents.openWebsite(context) },
                                    onReset = { viewModel.resetEvaluation() },
                                    isSaved = isSaved
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick option to input another day
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(CircleShape)
                                        .background(SleekContainer)
                                        .border(1.dp, SleekBorder, CircleShape)
                                        .clickable { viewModel.resetEvaluation() }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+ Reflect On Another Day",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = SleekPrimary
                                    )
                                }
                            }

                            WebEcosystemCard(
                                onOpenWeb = { DayScoreIntents.openWebsite(context) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        AppTab.HISTORY -> {
                            HistorySection(
                                history = history,
                                onSelectDay = { viewModel.selectDayFromHistory(it) },
                                onDeleteDay = { viewModel.deleteEvaluation(it) }
                            )

                            WebEcosystemCard(
                                onOpenWeb = { DayScoreIntents.openWebsite(context) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        AppTab.DIMENSIONS -> {
                            DimensionsExplorer()

                            ComparisonSection()

                            WebEcosystemCard(
                                onOpenWeb = { DayScoreIntents.openWebsite(context) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WebEcosystemCard(
    onOpenWeb: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
            .clickable { onOpenWeb() }
            .testTag("web_ecosystem_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SleekContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = SleekPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "https://dayscore.ai.studio",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SleekTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Visit the official web portal to access DayScore AI across all your devices.",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = SleekTextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.OpenInBrowser,
                contentDescription = "Open",
                tint = SleekPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
