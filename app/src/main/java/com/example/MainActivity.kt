package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AiAnalystScreen
import com.example.ui.screens.ApiReferenceScreen
import com.example.ui.screens.DeepSearchScreen
import com.example.ui.screens.DirectoryScreen
import com.example.ui.screens.DorkEngineScreen
import com.example.ui.screens.ForensicsScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TacticalNavyDark
import com.example.ui.theme.TacticalNavySurface
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.viewmodel.OsintViewModel

enum class OsintNavTab(val title: String, val icon: ImageVector, val tag: String) {
    DIRECTORY("Directory", Icons.Default.Category, "nav_directory"),
    DEEP_SEARCH("Deep Search", Icons.Default.Search, "nav_deep_search"),
    DORKS("Dorks", Icons.Default.Code, "nav_dorks"),
    LEDGER("Ledger", Icons.Default.ListAlt, "nav_ledger"),
    FORENSICS("Forensics", Icons.Default.CameraAlt, "nav_forensics"),
    AI_INTEL("AI Analyst", Icons.Default.AutoAwesome, "nav_ai_analyst"),
    APIS("APIs", Icons.Default.FormatListNumbered, "nav_apis")
}

class MainActivity : ComponentActivity() {
    private val viewModel: OsintViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: OsintViewModel) {
    var selectedTab by remember { mutableStateOf(OsintNavTab.DIRECTORY) }

    BackHandler(enabled = selectedTab != OsintNavTab.DIRECTORY) {
        selectedTab = OsintNavTab.DIRECTORY
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpandedScreen = maxWidth >= 600.dp

        if (isExpandedScreen) {
            // Tablet / Foldable layout with NavigationRail
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TacticalNavyDark)
            ) {
                NavigationRail(
                    containerColor = TacticalNavySurface,
                    contentColor = CyberCyan,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    OsintNavTab.values().forEach { tab ->
                        NavigationRailItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(tab.icon, contentDescription = tab.title, modifier = Modifier.size(20.dp)) },
                            label = { Text(tab.title, fontSize = 10.sp) },
                            modifier = Modifier.testTag(tab.tag),
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TacticalNavyDark,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberCyan,
                                unselectedIconColor = TacticalTextMuted,
                                unselectedTextColor = TacticalTextSecondary
                            )
                        )
                    }
                }

                TabScreenContent(
                    selectedTab = selectedTab,
                    viewModel = viewModel,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        } else {
            // Handheld mobile layout with bottom NavigationBar
            Scaffold(
                containerColor = TacticalNavyDark,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    NavigationBar(
                        containerColor = TacticalNavySurface,
                        tonalElevation = 4.dp
                    ) {
                        OsintNavTab.values().forEach { tab ->
                            NavigationBarItem(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                icon = { Icon(tab.icon, contentDescription = tab.title, modifier = Modifier.size(19.dp)) },
                                label = { Text(tab.title, fontSize = 9.sp, maxLines = 1) },
                                modifier = Modifier.testTag(tab.tag),
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TacticalNavyDark,
                                    selectedTextColor = CyberCyan,
                                    indicatorColor = CyberCyan,
                                    unselectedIconColor = TacticalTextMuted,
                                    unselectedTextColor = TacticalTextSecondary
                                )
                            )
                        }
                    }
                }
            ) { innerPadding ->
                TabScreenContent(
                    selectedTab = selectedTab,
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun TabScreenContent(
    selectedTab: OsintNavTab,
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    when (selectedTab) {
        OsintNavTab.DIRECTORY -> DirectoryScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.DEEP_SEARCH -> DeepSearchScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.DORKS -> DorkEngineScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.LEDGER -> LedgerScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.FORENSICS -> ForensicsScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.AI_INTEL -> AiAnalystScreen(viewModel = viewModel, modifier = modifier)
        OsintNavTab.APIS -> ApiReferenceScreen(modifier = modifier)
    }
}
