package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
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
import com.example.data.model.CaseEntity
import com.example.data.model.IntelNodeEntity
import com.example.ui.components.CategoryPill
import com.example.ui.components.TacticalCard
import com.example.ui.components.TacticalHeader
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ProvenBadgeGreen
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TacticalNavyBorder
import com.example.ui.theme.TacticalNavyCard
import com.example.ui.theme.TacticalNavyDark
import com.example.ui.theme.TacticalNavySurface
import com.example.ui.theme.TacticalRed
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextPrimary
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.theme.UnprovenBadgeAmber
import com.example.ui.viewmodel.OsintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val cases by viewModel.cases.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()
    val nodes by viewModel.currentCaseNodes.collectAsState()

    var nodeFilter by remember { mutableStateOf("ALL") } // ALL, PROVEN, UNPROVEN
    var showNewCaseDialog by remember { mutableStateOf(false) }
    var showNewNodeDialog by remember { mutableStateOf(false) }
    var isCaseDropdownExpanded by remember { mutableStateOf(false) }

    val filteredNodes = remember(nodes, nodeFilter) {
        when (nodeFilter) {
            "PROVEN" -> nodes.filter { it.verificationStatus == "PROVEN" }
            "UNPROVEN" -> nodes.filter { it.verificationStatus == "UNPROVEN" }
            else -> nodes
        }
    }

    val provenCount = remember(nodes) { nodes.count { it.verificationStatus == "PROVEN" } }
    val unprovenCount = remember(nodes) { nodes.count { it.verificationStatus == "UNPROVEN" } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TacticalHeader(
                title = "Investigation Node Ledger",
                subtitle = "Two-Source Proven / Unproven Classification Matrix"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Case Selector Card
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📁 ACTIVE CASE FILE:",
                                    color = CyberCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedButton(
                                    onClick = { showNewCaseDialog = true },
                                    modifier = Modifier.testTag("create_case_button"),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = CyberCyan
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("New Case", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            ExposedDropdownMenuBox(
                                expanded = isCaseDropdownExpanded,
                                onExpandedChange = { isCaseDropdownExpanded = !isCaseDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCase?.title ?: "Select or create case",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCaseDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .testTag("case_dropdown_selector"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TacticalNavyBorder,
                                        focusedTextColor = TacticalTextPrimary,
                                        unfocusedTextColor = TacticalTextPrimary,
                                        focusedContainerColor = TacticalNavySurface,
                                        unfocusedContainerColor = TacticalNavySurface
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = isCaseDropdownExpanded,
                                    onDismissRequest = { isCaseDropdownExpanded = false }
                                ) {
                                    cases.forEach { c ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(c.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                    Text("Subject: ${c.targetName.ifEmpty { "N/A" }} | Handle: ${c.targetHandle.ifEmpty { "N/A" }}", fontSize = 11.sp, color = TacticalTextSecondary)
                                                }
                                            },
                                            onClick = {
                                                viewModel.selectCase(c.id)
                                                isCaseDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (selectedCase != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("SUBJECT:", color = TacticalTextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        Text(selectedCase?.targetName?.ifEmpty { "Unspecified" } ?: "", color = TacticalTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("HANDLE:", color = TacticalTextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        Text(selectedCase?.targetHandle?.ifEmpty { "None" } ?: "", color = CyberCyan, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                                    }
                                    Column(modifier = Modifier.weight(1.2f)) {
                                        Text("LOCATION / DOMAIN:", color = TacticalTextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        Text("${selectedCase?.locationOfInterest?.ifEmpty { "N/A" }}", color = TacticalTextSecondary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Two-Source Classification Matrix Infographic Card
                item {
                    TacticalCard {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "⚖️ INTELLIGENCE CLASSIFICATION PROTOCOL",
                                color = TacticalAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Proven Column
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ProvenBadgeGreen.copy(alpha = 0.08f))
                                        .border(1.dp, ProvenBadgeGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ProvenBadgeGreen, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("PROVEN ($provenCount)", color = ProvenBadgeGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("• 2+ independent records\n• Anchor point for case\n• Core timeline locked", color = TacticalTextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                                    }
                                }

                                // Unproven Column
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UnprovenBadgeAmber.copy(alpha = 0.08f))
                                        .border(1.dp, UnprovenBadgeAmber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = UnprovenBadgeAmber, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("UNPROVEN ($unprovenCount)", color = UnprovenBadgeAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("• Single loose indicator\n• Kept in active sandbox\n• Generates next dorks", color = TacticalTextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Filter & Actions Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            listOf("ALL", "PROVEN", "UNPROVEN").forEach { status ->
                                FilterChip(
                                    selected = nodeFilter == status,
                                    onClick = { nodeFilter = status },
                                    label = { Text(status, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = CyberCyan,
                                        containerColor = TacticalNavySurface,
                                        labelColor = TacticalTextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = nodeFilter == status,
                                        borderColor = if (nodeFilter == status) CyberCyan else TacticalNavyBorder
                                    )
                                )
                            }
                        }

                        // Export Dossier Button
                        OutlinedButton(
                            onClick = {
                                val report = viewModel.exportDossierMarkdown()
                                clipboardManager.setText(AnnotatedString(report))
                                Toast.makeText(context, "Dossier report copied to clipboard!", Toast.LENGTH_SHORT).show()

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, report)
                                    type = "text/plain"
                                }
                                try {
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Tactical Dossier"))
                                } catch (e: Exception) {
                                    // Ignore if no activity
                                }
                            },
                            modifier = Modifier.testTag("export_dossier_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TacticalAmber
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(TacticalAmber.copy(alpha = 0.6f))
                            )
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export Dossier", fontSize = 11.sp)
                        }
                    }
                }

                // Node List
                if (filteredNodes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No intelligence nodes matching '$nodeFilter'.\nTap '+' below to record a lead.",
                                color = TacticalTextMuted,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(filteredNodes, key = { it.id }) { node ->
                        IntelNodeCard(
                            node = node,
                            onToggleStatus = { viewModel.toggleNodeVerification(node) },
                            onDelete = { viewModel.deleteIntelNode(node) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showNewNodeDialog = true },
            containerColor = CyberCyan,
            contentColor = TacticalNavyDark,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_intel_node_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Intel Node")
        }

        // Dialog: Create New Case
        if (showNewCaseDialog) {
            NewCaseDialog(
                onDismiss = { showNewCaseDialog = false },
                onCreate = { title, name, handle, domain, location, desc ->
                    viewModel.createCase(title, name, handle, domain, location, desc)
                    showNewCaseDialog = false
                    Toast.makeText(context, "Case created successfully", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Dialog: Create New Intel Node
        if (showNewNodeDialog) {
            NewNodeDialog(
                onDismiss = { showNewNodeDialog = false },
                onCreate = { title, category, rawData, status, sources, confidence, urls, notes, clues ->
                    viewModel.addIntelNode(title, category, rawData, status, sources, confidence, urls, notes, clues)
                    showNewNodeDialog = false
                    Toast.makeText(context, "Intel node recorded", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun IntelNodeCard(
    node: IntelNodeEntity,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    val isProven = node.verificationStatus == "PROVEN"

    TacticalCard(
        borderColor = if (isProven) ProvenBadgeGreen.copy(alpha = 0.4f) else UnprovenBadgeAmber.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerificationBadge(isProven = isProven, sourcesCount = node.sourceCount)
                Spacer(modifier = Modifier.width(8.dp))
                CategoryPill(category = node.category)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TacticalTextMuted, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = node.title,
                color = TacticalTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))
            // Raw data box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(TacticalNavyDark)
                    .padding(8.dp)
            ) {
                Text(
                    text = node.rawData,
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 15.sp
                )
            }

            if (node.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Notes: ${node.notes}",
                    color = TacticalTextSecondary,
                    fontSize = 12.sp
                )
            }

            if (node.pivotClues.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Next Pivot Clues: ${node.pivotClues}",
                    color = TacticalAmber,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Confidence: ${node.confidenceLevel}",
                    color = TacticalTextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = onToggleStatus,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isProven) UnprovenBadgeAmber else ProvenBadgeGreen
                    ),
                    shape = RoundedCornerShape(6.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isProven) UnprovenBadgeAmber.copy(alpha = 0.5f) else ProvenBadgeGreen.copy(alpha = 0.5f)
                        )
                    )
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isProven) "Demote to Lead" else "Verify as Proven",
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun NewCaseDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, name: String, handle: String, domain: String, location: String, desc: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Investigation Case", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Case Title (e.g. Operation Cascade)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Target Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text("Target Username / Handle") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = domain,
                    onValueChange = { domain = it },
                    label = { Text("Associated Domain or Org") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location of Interest") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, name, handle, domain, location, desc)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = TacticalNavyDark)
            ) {
                Text("Create Case")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = TacticalNavyCard
    )
}

@Composable
private fun NewNodeDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, category: String, rawData: String, status: String, sources: Int, confidence: String, urls: String, notes: String, clues: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Social Media") }
    var rawData by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("UNPROVEN") }
    var sources by remember { mutableStateOf(1) }
    var notes by remember { mutableStateOf("") }
    var clues by remember { mutableStateOf("") }

    val categories = listOf("Social Media", "Public Records", "EXIF / Imagery", "Corporate / Registry", "Geolocation", "Threat Intel / Network")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Intelligence Node", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Node Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text("Category:", fontSize = 11.sp, color = TacticalTextSecondary)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat, fontSize = 10.sp) }
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = rawData,
                        onValueChange = { rawData = it },
                        label = { Text("Raw Data / Extracted Text") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text("Initial Classification:", fontSize = 11.sp, color = TacticalTextSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = status == "UNPROVEN",
                            onClick = {
                                status = "UNPROVEN"
                                sources = 1
                            },
                            label = { Text("Unproven Lead (1 source)") }
                        )
                        FilterChip(
                            selected = status == "PROVEN",
                            onClick = {
                                status = "PROVEN"
                                sources = 2
                            },
                            label = { Text("Proven Intel (2+ sources)") }
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Verification Notes") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = clues,
                        onValueChange = { clues = it },
                        label = { Text("Next Pivot Clues / Search Dorks") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, category, rawData, status, sources, if (status == "PROVEN") "VERIFIED" else "MEDIUM", "", notes, clues)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = TacticalNavyDark)
            ) {
                Text("Save Node")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = TacticalNavyCard
    )
}
