package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
fun ForensicsScreen(
    viewModel: OsintViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageUri by viewModel.selectedImageUri.collectAsState()
    val exifData by viewModel.exifData.collectAsState()
    val forensicsStatus by viewModel.forensicsStatus.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.processImageUri(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TacticalNavyDark)
    ) {
        TacticalHeader(
            title = "Digital Forensics & EXIF Inspector",
            subtitle = "Extract camera serials, timestamps, embedded GPS & solar shadow telemetry"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Pick image button card
            item {
                Spacer(modifier = Modifier.height(6.dp))
                TacticalCard(borderColor = CyberCyan.copy(alpha = 0.5f)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TacticalNavyDark)
                            ) {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "Inspected Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("select_photo_for_forensics"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan,
                                contentColor = TacticalNavyDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (imageUri == null) "Select Photo for Forensics" else "Select Another Photo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (forensicsStatus.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = forensicsStatus,
                                color = if (exifData?.latitude != null) ProvenBadgeGreen else TacticalAmber,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // EXIF Results Card
            if (exifData != null) {
                val exif = exifData!!
                val hasGps = exif.latitude != null && exif.longitude != null

                item {
                    TacticalCard(
                        borderColor = if (hasGps) ProvenBadgeGreen.copy(alpha = 0.5f) else TacticalNavyBorder
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "EXTRACTED EXIF METADATA",
                                    color = CyberCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                if (hasGps) {
                                    CategoryPill(category = "GPS INTACT")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Metadata Grid
                            ExifRow("Camera:", "${exif.make ?: "Unknown"} ${exif.model ?: ""}".trim().ifEmpty { "Not detected" })
                            ExifRow("Timestamp:", exif.dateTime ?: "Not found (often stripped on upload)")
                            ExifRow("Lens:", exif.lensModel ?: "Standard / Not embedded")
                            ExifRow("Exposure / ISO:", "Exp: ${exif.exposureTime ?: "N/A"} | ISO: ${exif.iso ?: "N/A"} | f/${exif.fNumber ?: "N/A"}")
                            ExifRow("Focal Length:", exif.focalLength ?: "N/A")
                            ExifRow("Software:", exif.software ?: "Original hardware firmware")

                            if (hasGps) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ProvenBadgeGreen.copy(alpha = 0.1f))
                                        .border(1.dp, ProvenBadgeGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "📍 EMBEDDED GEOLOCATION COORDINATES:",
                                            color = ProvenBadgeGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Latitude: ${exif.latitude}\nLongitude: ${exif.longitude}${if (exif.altitude != null) "\nAltitude: ${exif.altitude} meters" else ""}",
                                            color = TacticalTextPrimary,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                val uri = Uri.parse("geo:${exif.latitude},${exif.longitude}?q=${exif.latitude},${exif.longitude}(Photo+Location)")
                                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                                try {
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${exif.latitude},${exif.longitude}")
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
                                                }
                                            },
                                            modifier = Modifier.testTag("open_in_maps_button"),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ProvenBadgeGreen,
                                                contentColor = TacticalNavyDark
                                            ),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Plot Coordinates in Maps", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            // Save to case button
                            Button(
                                onClick = {
                                    viewModel.attachExifAsIntelNode()
                                    Toast.makeText(context, "EXIF lead attached to active case!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("attach_exif_to_case_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasGps) ProvenBadgeGreen else TacticalAmber,
                                    contentColor = TacticalNavyDark
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (hasGps) "Attach as Proven Intel to '${selectedCase?.title ?: "Case"}'" else "Attach as Unproven Lead to '${selectedCase?.title ?: "Case"}'",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // SunCalc & Shadow Analysis Guide Card
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = TacticalAmber, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "☀️ SUNCALC & SOLAR SHADOW PROTOCOL",
                                color = TacticalAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "When web platforms strip GPS and EXIF data, use shadow direction (azimuth) and shadow length ratio (altitude) to determine the precise time of day and cardinal heading:",
                            color = TacticalTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(TacticalNavyDark)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "1. Object Height vs Shadow Length: tan(Solar Altitude) = Object Height / Shadow Length.\n" +
                                        "2. Azimuth Angle: Points directly 180° opposite the sun's direction.\n" +
                                        "3. Cross-reference SunCalc (suncalc.org) with the suspected latitude/date to lock the time window within ±15 minutes.",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.suncalc.org/"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalAmber),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Launch SunCalc Solar Calculator", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Reverse Image Search Engines Card
            item {
                TacticalCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "🌐 REVERSE VISUAL INVESTIGATION PIVOTS",
                            color = TacticalTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ForensicPivotButton("Google Lens", "https://images.google.com/", context, Modifier.weight(1f))
                            ForensicPivotButton("TinEye", "https://tineye.com/", context, Modifier.weight(1f))
                            ForensicPivotButton("FotoForensics", "http://fotoforensics.com/", context, Modifier.weight(1f))
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
private fun ExifRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = TacticalTextMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            color = TacticalTextPrimary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun ForensicPivotButton(
    label: String,
    url: String,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
