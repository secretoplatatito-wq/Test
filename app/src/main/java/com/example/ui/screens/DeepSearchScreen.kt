package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import com.example.data.model.DeepSearchEngine
import com.example.data.model.DeepSearchPoint
import com.example.ui.components.CategoryPill
import com.example.ui.components.TacticalCard
import com.example.ui.components.TacticalHeader
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ProvenBadgeGreen
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TacticalNavyBorder
import com.example.ui.theme.TacticalNavyCard
import com.example.ui.theme.TacticalNavyDark
import com.example.ui.theme.TacticalNavySurface
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextPrimary
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.theme.UnprovenBadgeAmber
import com.example.ui.viewmodel.OsintViewModel

@Composable
fun DeepSearchScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val selectedCase by viewModel.selectedCase.collectAsState()

    val deepName by viewModel.deepName.collectAsState()
    val deepHandle by viewModel.deepHandle.collectAsState()
    val deepEmail by viewModel.deepEmail.collectAsState()
    val deepPhone by viewModel.deepPhone.collectAsState()
    val deepLocation by viewModel.deepLocation.collectAsState()
    val deepDomain by viewModel.deepDomain.collectAsState()
    val deepKeywords by viewModel.deepKeywords.collectAsState()

    var isParamsExpanded by remember { mutableStateOf(true) }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var selectedEngine by remember { mutableStateOf("Google") }

    var saveNodeDialogPoint by remember { mutableStateOf<DeepSearchPoint?>(null) }

    val engines = listOf("Google", "Brave", "DuckDuckGo", "Yandex", "Bing")
    val categories = listOf(
        "ALL",
        "Identity & Social",
        "Document Excavation",
        "Geospatial & Check-ins",
        "Leaked Credentials & Code",
        "Corporate & Legal",
        "Archives & Historical"
    )

    val allSearchPoints = remember(deepName, deepHandle, deepEmail, deepPhone, deepLocation, deepDomain, deepKeywords) {
        DeepSearchEngine.generateAllVectors(
            name = deepName,
            handle = deepHandle,
            email = deepEmail,
            phone = deepPhone,
            location = deepLocation,
            domain = deepDomain,
            keywords = deepKeywords
        )
    }

    val filteredPoints = remember(allSearchPoints, selectedCategoryFilter) {
        if (selectedCategoryFilter == "ALL") allSearchPoints
        else allSearchPoints.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        TacticalHeader(
            title = "Deep Multi-Vector Search Matrix",
            subtitle = "Exhaustive multi-point Google sweeps & operator excavation targeting every digital vector"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Target Data Points Accordion Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎯 MULTI-POINT TARGET ATTRIBUTES",
                                color = CyberCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isParamsExpanded = !isParamsExpanded }) {
                                Icon(
                                    if (isParamsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Toggle",
                                    tint = CyberCyan
                                )
                            }
                        }

                        if (isParamsExpanded) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = deepName,
                                    onValueChange = { viewModel.updateDeepParams(name = it) },
                                    label = { Text("Target Full Name", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f).testTag("deep_search_name"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                                OutlinedTextField(
                                    value = deepHandle,
                                    onValueChange = { viewModel.updateDeepParams(handle = it) },
                                    label = { Text("Target Handle / Alias", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f).testTag("deep_search_handle"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = deepEmail,
                                    onValueChange = { viewModel.updateDeepParams(email = it) },
                                    label = { Text("Known Email Address", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                                OutlinedTextField(
                                    value = deepPhone,
                                    onValueChange = { viewModel.updateDeepParams(phone = it) },
                                    label = { Text("Known Phone / Cell", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = deepLocation,
                                    onValueChange = { viewModel.updateDeepParams(location = it) },
                                    label = { Text("City / Region / Trailhead", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                                OutlinedTextField(
                                    value = deepDomain,
                                    onValueChange = { viewModel.updateDeepParams(domain = it) },
                                    label = { Text("Target Domain / Org", fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = deepKeywords,
                                onValueChange = { viewModel.updateDeepParams(keywords = it) },
                                label = { Text("Additional Custom Boolean Operators / Keywords", fontSize = 10.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = TacticalNavyBorder,
                                    focusedTextColor = TacticalTextPrimary,
                                    unfocusedTextColor = TacticalTextPrimary,
                                    focusedContainerColor = TacticalNavySurface,
                                    unfocusedContainerColor = TacticalNavySurface
                                )
                            )

                            if (selectedCase != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(
                                    onClick = {
                                        viewModel.selectCase(selectedCase!!.id)
                                        Toast.makeText(context, "Attributes synced from Case: ${selectedCase!!.title}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp), tint = TacticalAmber)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sync parameters from active case '${selectedCase!!.title}'", fontSize = 11.sp, color = TacticalAmber)
                                }
                            }
                        }
                    }
                }
            }

            // Engine & Quick Actions
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ENGINE:",
                                color = TacticalTextMuted,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                engines.forEach { eng ->
                                    FilterChip(
                                        selected = selectedEngine == eng,
                                        onClick = { selectedEngine = eng },
                                        label = { Text(eng, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                            selectedLabelColor = CyberCyan,
                                            containerColor = TacticalNavySurface,
                                            labelColor = TacticalTextSecondary
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = selectedEngine == eng,
                                            borderColor = if (selectedEngine == eng) CyberCyan else TacticalNavyBorder
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Full Multi-Vector Deep Sweep Action
                        Button(
                            onClick = {
                                val topPoints = allSearchPoints.take(3)
                                topPoints.forEach { pt ->
                                    val url = pt.getLaunchUrl(selectedEngine)
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Ignore multiple launch intents
                                    }
                                }
                                Toast.makeText(context, "Initiated Multi-Vector Deep Sweep across Google!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("launch_deep_sweep_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan,
                                contentColor = TacticalNavyDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🚀 Launch Multi-Vector Sweep on $selectedEngine",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TacticalAmber.copy(alpha = 0.2f),
                                selectedLabelColor = TacticalAmber,
                                containerColor = TacticalNavySurface,
                                labelColor = TacticalTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategoryFilter == cat,
                                borderColor = if (selectedCategoryFilter == cat) TacticalAmber else TacticalNavyBorder
                            )
                        )
                    }
                }
            }

            // Search Vector Counter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DEEP SEARCH VECTORS (${filteredPoints.size})",
                        color = TacticalTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Points list
            items(filteredPoints, key = { it.id }) { point ->
                DeepSearchPointCard(
                    point = point,
                    selectedEngine = selectedEngine,
                    onExecute = {
                        val url = point.getLaunchUrl(selectedEngine)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(point.query))
                        Toast.makeText(context, "Query copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    onSaveToLedger = {
                        saveNodeDialogPoint = point
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Save finding to case dialog
        if (saveNodeDialogPoint != null) {
            val point = saveNodeDialogPoint!!
            SaveFindingDialog(
                point = point,
                onDismiss = { saveNodeDialogPoint = null },
                onSave = { isProven, findings ->
                    viewModel.saveDeepSearchAsIntelNode(point, isProven, findings)
                    saveNodeDialogPoint = null
                    Toast.makeText(context, "Recorded node to active case ledger!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun DeepSearchPointCard(
    point: DeepSearchPoint,
    selectedEngine: String,
    onExecute: () -> Unit,
    onCopy: () -> Unit,
    onSaveToLedger: () -> Unit
) {
    TacticalCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = point.title,
                    color = TacticalTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberCyan.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = point.precisionRating,
                        color = CyberCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = point.description,
                color = TacticalTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Monospace Query Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(TacticalNavyDark)
                    .border(1.dp, TacticalNavyBorder, RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = point.query,
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onExecute,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("launch_deep_${point.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = TacticalNavyDark
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Search on $selectedEngine", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onCopy,
                    modifier = Modifier.testTag("copy_deep_${point.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalTextSecondary),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(TacticalNavyBorder)
                    )
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                }

                OutlinedButton(
                    onClick = onSaveToLedger,
                    modifier = Modifier.testTag("save_deep_ledger_${point.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalAmber),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(TacticalAmber.copy(alpha = 0.5f))
                    )
                ) {
                    Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Record", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun SaveFindingDialog(
    point: DeepSearchPoint,
    onDismiss: () -> Unit,
    onSave: (isProven: Boolean, findings: String) -> Unit
) {
    var findingsText by remember { mutableStateOf("") }
    var isProven by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Record Search Finding to Ledger", color = CyberCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Query: ${point.query}",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                OutlinedTextField(
                    value = findingsText,
                    onValueChange = { findingsText = it },
                    label = { Text("What did you discover in search results?") },
                    placeholder = { Text("e.g. Found matching GitHub commit from 2026-09-14 with personal email vance@domain.") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Classification:", fontSize = 11.sp, color = TacticalTextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = !isProven,
                        onClick = { isProven = false },
                        label = { Text("Unproven Lead (1 Source)") }
                    )
                    FilterChip(
                        selected = isProven,
                        onClick = { isProven = true },
                        label = { Text("Proven Intel (2+ Sources)") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(isProven, findingsText) },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = TacticalNavyDark)
            ) {
                Text("Save to Ledger")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = TacticalNavyCard
    )
}
