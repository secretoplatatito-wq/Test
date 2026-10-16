package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OsintDirectoryData
import com.example.data.model.OsintResource
import com.example.ui.components.PricingTierTag
import com.example.ui.components.TacticalCard
import com.example.ui.components.TacticalHeader
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TacticalGreen
import com.example.ui.theme.TacticalNavyBorder
import com.example.ui.theme.TacticalNavyCard
import com.example.ui.theme.TacticalNavyDark
import com.example.ui.theme.TacticalNavySurface
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextPrimary
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.viewmodel.OsintViewModel

@Composable
fun DirectoryScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val targetHandle by viewModel.targetQuery.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    val filterOptions = listOf(
        "ALL",
        "Username & Social",
        "People Directories",
        "Forensics",
        "Corporate",
        "Geolocation",
        "Archives"
    )

    val allTools = remember {
        OsintDirectoryData.categories.flatMap { it.tools }
    }

    val filteredTools = remember(searchQuery, selectedCategoryFilter) {
        allTools.filter { tool ->
            val matchesCategory = when (selectedCategoryFilter) {
                "ALL" -> true
                "Username & Social" -> tool.category.contains("Social", ignoreCase = true) || tool.category.contains("Username", ignoreCase = true)
                "People Directories" -> tool.category.contains("People", ignoreCase = true)
                "Forensics" -> tool.category.contains("Forensics", ignoreCase = true)
                "Corporate" -> tool.category.contains("Corporate", ignoreCase = true)
                "Geolocation" -> tool.category.contains("Geolocation", ignoreCase = true)
                "Archives" -> tool.category.contains("Archives", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                    tool.name.contains(searchQuery, ignoreCase = true) ||
                    tool.description.contains(searchQuery, ignoreCase = true) ||
                    tool.verificationMethod.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        TacticalHeader(
            title = "OSINT Directory & Framework Tree",
            subtitle = "7y7g Intelligence Node Directory with live verification endpoints"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Target Handle Pivot Box
            item {
                Spacer(modifier = Modifier.height(6.dp))
                TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎯 ACTIVE INVESTIGATION TARGET",
                                color = CyberCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = targetHandle,
                            onValueChange = { viewModel.setTargetQuery(it) },
                            placeholder = { Text("e.g. octoberian1012 or Julian Vance", color = TacticalTextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("target_handle_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = TacticalNavyBorder,
                                focusedTextColor = TacticalTextPrimary,
                                unfocusedTextColor = TacticalTextPrimary,
                                focusedContainerColor = TacticalNavySurface,
                                unfocusedContainerColor = TacticalNavySurface
                            ),
                            singleLine = true,
                            trailingIcon = {
                                if (targetHandle.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setTargetQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TacticalTextMuted)
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "⚡ QUICK PIVOT ENDPOINTS:",
                            color = TacticalTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        val cleanHandle = targetHandle.trim().removePrefix("@")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            QuickPivotChip("TikTok", "https://www.tiktok.com/@$cleanHandle", context)
                            QuickPivotChip("YouTube", "https://www.youtube.com/@$cleanHandle", context)
                            QuickPivotChip("Facebook", "https://www.facebook.com/public/$cleanHandle", context)
                            QuickPivotChip("GitHub", "https://github.com/$cleanHandle", context)
                            QuickPivotChip("Namechk", "https://namechk.com/", context)
                            QuickPivotChip("Social Searcher", "https://www.social-searcher.com/search-users/?q=$cleanHandle", context)
                            QuickPivotChip("Whitepages", "https://www.whitepages.com/name/$cleanHandle", context)
                        }
                    }
                }
            }

            // Search Filter & Chips
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter intelligence databases, APIs, or tools...", color = TacticalTextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = CyberCyan)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("directory_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = TacticalNavyBorder,
                        focusedTextColor = TacticalTextPrimary,
                        unfocusedTextColor = TacticalTextPrimary,
                        focusedContainerColor = TacticalNavyCard,
                        unfocusedContainerColor = TacticalNavyCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { filter ->
                        FilterChip(
                            selected = selectedCategoryFilter == filter,
                            onClick = { selectedCategoryFilter = filter },
                            label = { Text(filter, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan,
                                containerColor = TacticalNavySurface,
                                labelColor = TacticalTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategoryFilter == filter,
                                borderColor = if (selectedCategoryFilter == filter) CyberCyan else TacticalNavyBorder
                            )
                        )
                    }
                }
            }

            // Results Counter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DISCOVERED DIRECTORIES (${filteredTools.size})",
                        color = TacticalTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Tool Items
            items(filteredTools, key = { it.id }) { tool ->
                OsintToolCard(
                    tool = tool,
                    targetHandle = targetHandle,
                    onLaunch = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Cannot open URL: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onCopy = { url ->
                        clipboardManager.setText(AnnotatedString(url))
                        Toast.makeText(context, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun QuickPivotChip(
    label: String,
    url: String,
    context: Context
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TacticalNavyCard)
            .border(1.dp, TacticalNavyBorder, RoundedCornerShape(6.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "➔ $label",
            color = CyberCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun OsintToolCard(
    tool: OsintResource,
    targetHandle: String,
    onLaunch: (String) -> Unit,
    onCopy: (String) -> Unit
) {
    val resolvedUrl = tool.getResolvedUrl(targetHandle)

    TacticalCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tool.name,
                    color = TacticalTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                PricingTierTag(pricing = tool.pricingType)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = tool.description,
                color = TacticalTextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            // Method & Indicators
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(TacticalNavySurface)
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text(
                            text = "Verification: ",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = tool.verificationMethod,
                            color = TacticalTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                    if (tool.trustedIndicators.isNotEmpty()) {
                        Row {
                            Text(
                                text = "Proven Criteria: ",
                                color = TacticalGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = tool.trustedIndicators.first(),
                                color = TacticalTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    if (tool.unprovenIndicators.isNotEmpty()) {
                        Row {
                            Text(
                                text = "Unproven Clues: ",
                                color = TacticalAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = tool.unprovenIndicators.first(),
                                color = TacticalTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onLaunch(resolvedUrl) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("launch_${tool.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = TacticalNavyDark
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (tool.isDynamicHandleSupported && targetHandle.isNotBlank()) "Probe Handle" else "Open Directory",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onCopy(resolvedUrl) },
                    modifier = Modifier.testTag("copy_${tool.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TacticalTextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(TacticalNavyBorder)
                    )
                ) {
                    Text("Copy URL", fontSize = 12.sp)
                }
            }
        }
    }
}
