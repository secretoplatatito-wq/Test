package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.data.api.GeminiTaskType
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
import com.example.ui.theme.TacticalRed
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextPrimary
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.viewmodel.OsintViewModel

@Composable
fun AiAnalystScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val aiError by viewModel.aiError.collectAsState()
    val selectedTaskType by viewModel.selectedAiTaskType.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()

    var customPrompt by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        TacticalHeader(
            title = "Gemini Cognitive Intel Analyst",
            subtitle = "High-Thinking Correlation, Discrepancy Auditing & Tactical Strategy Synthesis"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Model Configuration & Thinking Mode Selector
            item {
                Spacer(modifier = Modifier.height(6.dp))
                TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🧠 REASONING ENGINE & THINKING MODE",
                                color = CyberCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedTaskType == GeminiTaskType.COMPLEX_DEEP_REASONING,
                                onClick = { viewModel.setAiTaskType(GeminiTaskType.COMPLEX_DEEP_REASONING) },
                                label = { Text("gemini-4-argon (Thinking: HIGH)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = CyberCyan,
                                    containerColor = TacticalNavySurface,
                                    labelColor = TacticalTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedTaskType == GeminiTaskType.COMPLEX_DEEP_REASONING,
                                    borderColor = if (selectedTaskType == GeminiTaskType.COMPLEX_DEEP_REASONING) CyberCyan else TacticalNavyBorder
                                )
                            )

                            FilterChip(
                                selected = selectedTaskType == GeminiTaskType.GENERAL_INTEL_ANALYSIS,
                                onClick = { viewModel.setAiTaskType(GeminiTaskType.GENERAL_INTEL_ANALYSIS) },
                                label = { Text("gemini-3.8-flash (General)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TacticalAmber.copy(alpha = 0.2f),
                                    selectedLabelColor = TacticalAmber,
                                    containerColor = TacticalNavySurface,
                                    labelColor = TacticalTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedTaskType == GeminiTaskType.GENERAL_INTEL_ANALYSIS,
                                    borderColor = if (selectedTaskType == GeminiTaskType.GENERAL_INTEL_ANALYSIS) TacticalAmber else TacticalNavyBorder
                                )
                            )

                            FilterChip(
                                selected = selectedTaskType == GeminiTaskType.FAST_LEAD_EVALUATION,
                                onClick = { viewModel.setAiTaskType(GeminiTaskType.FAST_LEAD_EVALUATION) },
                                label = { Text("gemini-3.1-flash-lite (Fast)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ProvenBadgeGreen.copy(alpha = 0.2f),
                                    selectedLabelColor = ProvenBadgeGreen,
                                    containerColor = TacticalNavySurface,
                                    labelColor = TacticalTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedTaskType == GeminiTaskType.FAST_LEAD_EVALUATION,
                                    borderColor = if (selectedTaskType == GeminiTaskType.FAST_LEAD_EVALUATION) ProvenBadgeGreen else TacticalNavyBorder
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (selectedTaskType) {
                                GeminiTaskType.COMPLEX_DEEP_REASONING -> "Active: gemini-4-argon with thinkingLevel: HIGH. Deep multi-step hypothesis audit, contradiction checking & behavioral inference."
                                GeminiTaskType.GENERAL_INTEL_ANALYSIS -> "Active: gemini-3.8-flash. Balanced speed & intelligence for case reporting and dossier drafting."
                                GeminiTaskType.FAST_LEAD_EVALUATION -> "Active: gemini-3.1-flash-lite. High-throughput evaluation of loose indicators and dork suggestions."
                            },
                            color = TacticalTextMuted,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Quick Tactical Presets
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "⚡ PRESET TACTICAL INVESTIGATION AUDITS",
                            color = TacticalAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            PresetPromptButton(
                                title = "🔍 Full Case Audit & Discrepancy Detection",
                                subtitle = "Audits all proven anchors against unproven leads to detect timeline or travel contradictions.",
                                onClick = { viewModel.runAiAnalysis() }
                            )

                            PresetPromptButton(
                                title = "🎯 High-Yield Google Dork Generation",
                                subtitle = "Recommends 5 targeted search engine dorks designed to turn unproven leads into proven anchors.",
                                onClick = {
                                    viewModel.runAiAnalysis(
                                        "Generate 5 precise, weaponized Google Dorks specifically targeting the unproven leads in this case. Include explanations of which operator bypasses platform index limits."
                                    )
                                }
                            )

                            PresetPromptButton(
                                title = "🌐 Cross-Platform Digital Footprint Pivot Strategy",
                                subtitle = "Identifies whether subject's handles (e.g. on GitHub, Reddit, TikTok) suggest alternative aliases.",
                                onClick = {
                                    viewModel.runAiAnalysis(
                                        "Analyze the target's username patterns, commit histories, and social profiles. Suggest alternative alias derivations and specialized search databases (SEON, Namechk, Spiderfoot) to query next."
                                    )
                                }
                            )

                            PresetPromptButton(
                                title = "🚀 Exhaustive Deep Multi-Vector Search Plan",
                                subtitle = "Constructs high-precision Google Dorks and database pivots across all target attributes and unproven leads.",
                                onClick = {
                                    viewModel.runAiAnalysis(
                                        "Perform an EXHAUSTIVE multi-point intelligence audit on this case. Formulate high-precision Google Dorks and database queries for EVERY known point (Name, Handle, Location, Domain, EXIF, unproven leads). Identify potential alibis, travel timelines, and exact steps to turn all unproven leads into proven anchors."
                                    )
                                }
                            )

                            PresetPromptButton(
                                title = "📋 Executive Missing Person Dossier Brief",
                                subtitle = "Generates a structured executive intelligence brief formatted for humanitarian search teams.",
                                onClick = {
                                    viewModel.runAiAnalysis(
                                        "Synthesize all recorded nodes into an Executive Intelligence Brief. Include Last Known Physical Coordinates, Confirmed Online Activities, High-Risk Unverified Leads, and Immediate Search Priorities."
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Custom Question Box
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "💬 AD-HOC TACTICAL QUERY",
                            color = TacticalTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customPrompt,
                            onValueChange = { customPrompt = it },
                            placeholder = { Text("Ask the analyst: e.g. What databases trace the BSSID from WiGLE? How to verify this phone carrier?", color = TacticalTextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_custom_query_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = TacticalNavyBorder,
                                focusedTextColor = TacticalTextPrimary,
                                unfocusedTextColor = TacticalTextPrimary,
                                focusedContainerColor = TacticalNavyDark,
                                unfocusedContainerColor = TacticalNavyDark
                            ),
                            minLines = 2,
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (customPrompt.isNotBlank()) {
                                    viewModel.runAiAnalysis(customPrompt)
                                }
                            },
                            enabled = !isAiLoading && customPrompt.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("send_ai_query_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan,
                                contentColor = TacticalNavyDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isAiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = TacticalNavyDark, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Engaging High-Thinking Cognition...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Transmit Query to ${selectedTaskType.modelName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // AI Assessment Output Card
            if (isAiLoading) {
                item {
                    TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = CyberCyan, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Analyzing Case Dossier...", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Model: ${selectedTaskType.modelName} ${if (selectedTaskType.useThinking) "• Thinking: HIGH" else ""}", color = TacticalTextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            } else if (aiResponse.isNotBlank()) {
                item {
                    TacticalCard(borderColor = CyberCyan.copy(alpha = 0.6f)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "INTELLIGENCE ASSESSMENT REPORT",
                                    color = CyberCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(aiResponse))
                                        Toast.makeText(context, "Report copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TacticalTextSecondary, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TacticalNavyDark)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = aiResponse,
                                    color = TacticalTextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            } else if (aiError != null) {
                item {
                    TacticalCard(borderColor = TacticalRed.copy(alpha = 0.5f)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("⚠️ ANALYST ERROR", color = TacticalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(aiError ?: "Unknown error", color = TacticalTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PresetPromptButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(TacticalNavySurface)
            .border(1.dp, TacticalNavyBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Text(title, color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = TacticalTextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}
