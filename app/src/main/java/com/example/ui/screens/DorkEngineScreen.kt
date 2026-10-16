package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.DorkData
import com.example.data.model.DorkTemplate
import com.example.ui.components.CategoryPill
import com.example.ui.components.TacticalCard
import com.example.ui.components.TacticalHeader
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TacticalNavyBorder
import com.example.ui.theme.TacticalNavyCard
import com.example.ui.theme.TacticalNavyDark
import com.example.ui.theme.TacticalNavySurface
import com.example.ui.theme.TacticalTextMuted
import com.example.ui.theme.TacticalTextPrimary
import com.example.ui.theme.TacticalTextSecondary
import com.example.ui.viewmodel.OsintViewModel

@Composable
fun DorkEngineScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val selectedCase by viewModel.selectedCase.collectAsState()

    var targetSubject by remember(selectedCase) {
        mutableStateOf(selectedCase?.targetName?.ifEmpty { selectedCase?.targetHandle } ?: "Julian Vance")
    }
    var targetDomain by remember(selectedCase) {
        mutableStateOf(selectedCase?.targetDomain ?: "vancetech-labs.io")
    }

    var selectedSearchEngine by remember { mutableStateOf("Google") }
    val searchEngines = listOf("Google", "DuckDuckGo", "Brave", "Yandex", "Bing")

    var customDorkQuery by remember {
        mutableStateOf("\"Julian Vance\" AND (\"missing\" OR \"lost\" OR \"foul play\" OR \"last seen\")")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        TacticalHeader(
            title = "Advanced Search Operator Engine",
            subtitle = "Google Dork syntax generator for missing persons, buried documents & unindexed footprints"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Target Parameters Card
            item {
                Spacer(modifier = Modifier.height(6.dp))
                TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "⚙️ SEARCH OPERATOR PARAMETERS",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = targetSubject,
                                onValueChange = { targetSubject = it },
                                label = { Text("Subject Name / Handle", fontSize = 11.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dork_target_subject"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = TacticalNavyBorder,
                                    focusedTextColor = TacticalTextPrimary,
                                    unfocusedTextColor = TacticalTextPrimary,
                                    focusedContainerColor = TacticalNavySurface,
                                    unfocusedContainerColor = TacticalNavySurface
                                ),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = targetDomain,
                                onValueChange = { targetDomain = it },
                                label = { Text("Domain / Org", fontSize = 11.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dork_target_domain"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = TacticalNavyBorder,
                                    focusedTextColor = TacticalTextPrimary,
                                    unfocusedTextColor = TacticalTextPrimary,
                                    focusedContainerColor = TacticalNavySurface,
                                    unfocusedContainerColor = TacticalNavySurface
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "TARGET SEARCH ENGINE:",
                            color = TacticalTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            searchEngines.forEach { engine ->
                                FilterChip(
                                    selected = selectedSearchEngine == engine,
                                    onClick = { selectedSearchEngine = engine },
                                    label = { Text(engine, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = CyberCyan,
                                        containerColor = TacticalNavySurface,
                                        labelColor = TacticalTextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selectedSearchEngine == engine,
                                        borderColor = if (selectedSearchEngine == engine) CyberCyan else TacticalNavyBorder
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Custom Builder & Scratchpad
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "🛠️ DORK SYNTAX SCRATCHPAD",
                            color = TacticalAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customDorkQuery,
                            onValueChange = { customDorkQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dork_scratchpad_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TacticalAmber,
                                unfocusedBorderColor = TacticalNavyBorder,
                                focusedTextColor = TacticalTextPrimary,
                                unfocusedTextColor = TacticalTextPrimary,
                                focusedContainerColor = TacticalNavyDark,
                                unfocusedContainerColor = TacticalNavyDark
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = CyberCyan
                            ),
                            minLines = 2,
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        // Operator injection chips
                        Text(
                            text = "INJECT OPERATOR SHORTCUTS:",
                            color = TacticalTextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OperatorPill("filetype:pdf") { customDorkQuery += " filetype:pdf" }
                            OperatorPill("filetype:xlsx") { customDorkQuery += " filetype:xlsx" }
                            OperatorPill("site:linkedin.com") { customDorkQuery += " site:linkedin.com" }
                            OperatorPill("site:github.com") { customDorkQuery += " site:github.com" }
                            OperatorPill("intitle:\"index of\"") { customDorkQuery += " intitle:\"index of\"" }
                            OperatorPill("cache:") { customDorkQuery = "cache:$customDorkQuery" }
                            OperatorPill("\"exact match\"") { customDorkQuery = "\"$customDorkQuery\"" }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dummyTemplate = DorkData.templates.first()
                                    val url = dummyTemplate.getSearchEngineUrl(selectedSearchEngine, customDorkQuery)
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("launch_custom_dork"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TacticalAmber,
                                    contentColor = TacticalNavyDark
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Search on $selectedSearchEngine", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(customDorkQuery))
                                    Toast.makeText(context, "Dork copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_custom_dork"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TacticalTextSecondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(TacticalNavyBorder)
                                )
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            // Automated Templates Title
            item {
                Text(
                    text = "AUTOMATED DORK TEMPLATES (${DorkData.templates.size})",
                    color = TacticalTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Template items
            items(DorkData.templates, key = { it.id }) { template ->
                val formattedQuery = template.buildQuery(targetSubject, targetDomain)

                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = template.title,
                                color = TacticalTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            CategoryPill(category = template.category)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = template.description,
                            color = TacticalTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        // Monospace query box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(TacticalNavyDark)
                                .border(1.dp, TacticalNavyBorder, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = formattedQuery,
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
                                onClick = {
                                    val url = template.getSearchEngineUrl(selectedSearchEngine, formattedQuery)
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("launch_template_${template.id}"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberCyan,
                                    contentColor = TacticalNavyDark
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Execute on $selectedSearchEngine", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    customDorkQuery = formattedQuery
                                    Toast.makeText(context, "Loaded into Scratchpad", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("load_scratchpad_${template.id}"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TacticalTextSecondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(TacticalNavyBorder)
                                )
                            ) {
                                Text("Load", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(formattedQuery))
                                    Toast.makeText(context, "Dork copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_template_${template.id}"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TacticalTextSecondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(TacticalNavyBorder)
                                )
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                            }
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
private fun OperatorPill(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(TacticalNavySurface)
            .border(1.dp, TacticalNavyBorder, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "+ $label",
            color = TacticalTextPrimary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
