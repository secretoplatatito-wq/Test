package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.api.GeminiTaskType
import com.example.data.db.AppDatabase
import com.example.data.model.CaseEntity
import com.example.data.model.DeepSearchPoint
import com.example.data.model.IntelNodeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ExifMetadata(
    val dateTime: String? = null,
    val make: String? = null,
    val model: String? = null,
    val lensModel: String? = null,
    val software: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val altitude: Double? = null,
    val iso: String? = null,
    val exposureTime: String? = null,
    val fNumber: String? = null,
    val focalLength: String? = null,
    val flash: String? = null,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val rawTags: Map<String, String> = emptyMap()
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class OsintViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val caseDao = database.caseDao()
    private val intelNodeDao = database.intelNodeDao()

    val cases: StateFlow<List<CaseEntity>> = caseDao.getAllCases()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCaseId = MutableStateFlow<Long?>(null)
    val selectedCaseId: StateFlow<Long?> = _selectedCaseId.asStateFlow()

    private val _selectedCase = MutableStateFlow<CaseEntity?>(null)
    val selectedCase: StateFlow<CaseEntity?> = _selectedCase.asStateFlow()

    val currentCaseNodes: StateFlow<List<IntelNodeEntity>> = _selectedCaseId
        .flatMapLatest { id ->
            if (id != null) intelNodeDao.getNodesForCase(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Target query handle for universal lookup
    private val _targetQuery = MutableStateFlow("octoberian1012")
    val targetQuery: StateFlow<String> = _targetQuery.asStateFlow()

    // Forensics state
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _exifData = MutableStateFlow<ExifMetadata?>(null)
    val exifData: StateFlow<ExifMetadata?> = _exifData.asStateFlow()

    private val _forensicsStatus = MutableStateFlow<String>("")
    val forensicsStatus: StateFlow<String> = _forensicsStatus.asStateFlow()

    // AI Analyst state
    private val _aiResponse = MutableStateFlow<String>("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _aiError = MutableStateFlow<String?>(null)
    val aiError: StateFlow<String?> = _aiError.asStateFlow()

    private val _selectedAiTaskType = MutableStateFlow(GeminiTaskType.COMPLEX_DEEP_REASONING)
    val selectedAiTaskType: StateFlow<GeminiTaskType> = _selectedAiTaskType.asStateFlow()

    // Deep Multi-Vector Search Parameters
    private val _deepName = MutableStateFlow("Julian Vance")
    val deepName: StateFlow<String> = _deepName.asStateFlow()

    private val _deepHandle = MutableStateFlow("octoberian1012")
    val deepHandle: StateFlow<String> = _deepHandle.asStateFlow()

    private val _deepEmail = MutableStateFlow("julian.vance@vancetech-labs.io")
    val deepEmail: StateFlow<String> = _deepEmail.asStateFlow()

    private val _deepPhone = MutableStateFlow("+1 (303) 555-0192")
    val deepPhone: StateFlow<String> = _deepPhone.asStateFlow()

    private val _deepLocation = MutableStateFlow("Denver, Colorado")
    val deepLocation: StateFlow<String> = _deepLocation.asStateFlow()

    private val _deepDomain = MutableStateFlow("vancetech-labs.io")
    val deepDomain: StateFlow<String> = _deepDomain.asStateFlow()

    private val _deepKeywords = MutableStateFlow("firmware OR alpine OR cascade")
    val deepKeywords: StateFlow<String> = _deepKeywords.asStateFlow()

    init {
        viewModelScope.launch {
            cases.collect { list ->
                if (list.isNotEmpty() && _selectedCaseId.value == null) {
                    selectCase(list.first().id)
                }
            }
        }
    }

    fun selectCase(caseId: Long) {
        _selectedCaseId.value = caseId
        viewModelScope.launch {
            val c = caseDao.getCaseById(caseId)
            _selectedCase.value = c
            if (c != null) {
                if (c.targetHandle.isNotBlank()) _targetQuery.value = c.targetHandle
                else if (c.targetName.isNotBlank()) _targetQuery.value = c.targetName

                if (c.targetName.isNotBlank()) _deepName.value = c.targetName
                if (c.targetHandle.isNotBlank()) _deepHandle.value = c.targetHandle
                if (c.targetDomain.isNotBlank()) _deepDomain.value = c.targetDomain
                if (c.locationOfInterest.isNotBlank()) _deepLocation.value = c.locationOfInterest
            }
        }
    }

    fun updateDeepParams(
        name: String = _deepName.value,
        handle: String = _deepHandle.value,
        email: String = _deepEmail.value,
        phone: String = _deepPhone.value,
        location: String = _deepLocation.value,
        domain: String = _deepDomain.value,
        keywords: String = _deepKeywords.value
    ) {
        _deepName.value = name
        _deepHandle.value = handle
        _deepEmail.value = email
        _deepPhone.value = phone
        _deepLocation.value = location
        _deepDomain.value = domain
        _deepKeywords.value = keywords
    }

    fun saveDeepSearchAsIntelNode(point: DeepSearchPoint, isProven: Boolean, userFinding: String = "") {
        val caseId = _selectedCaseId.value ?: return
        val rawData = "Deep Query Executed: ${point.query}\nSearch Engine: ${point.searchEngine}\nPrecision: ${point.precisionRating}\nFindings:\n${userFinding.ifEmpty { "Query launched against index. Cross-referencing results across multi-vector search." }}"
        viewModelScope.launch {
            intelNodeDao.insertNode(
                IntelNodeEntity(
                    caseId = caseId,
                    title = "Deep Search: ${point.title}",
                    category = when (point.category) {
                        "Identity & Social" -> "Social Media"
                        "Document Excavation" -> "Public Records"
                        "Geospatial & Check-ins" -> "Geolocation"
                        "Leaked Credentials & Code" -> "Threat Intel / Network"
                        "Corporate & Legal" -> "Corporate / Registry"
                        else -> "Public Records"
                    },
                    rawData = rawData,
                    verificationStatus = if (isProven) "PROVEN" else "UNPROVEN",
                    sourceCount = if (isProven) 2 else 1,
                    confidenceLevel = if (isProven) "HIGH" else "MEDIUM",
                    notes = point.description,
                    pivotClues = "Follow-up on indexed links and check associated domain/author tags."
                )
            )
        }
    }

    fun setTargetQuery(query: String) {
        _targetQuery.value = query
    }

    fun setAiTaskType(taskType: GeminiTaskType) {
        _selectedAiTaskType.value = taskType
    }

    fun createCase(
        title: String,
        targetName: String,
        targetHandle: String,
        targetDomain: String,
        locationOfInterest: String,
        description: String
    ) {
        viewModelScope.launch {
            val newId = caseDao.insertCase(
                CaseEntity(
                    title = title,
                    targetName = targetName,
                    targetHandle = targetHandle,
                    targetDomain = targetDomain,
                    locationOfInterest = locationOfInterest,
                    description = description,
                    updatedAt = System.currentTimeMillis()
                )
            )
            selectCase(newId)
        }
    }

    fun deleteCase(caseEntity: CaseEntity) {
        viewModelScope.launch {
            caseDao.deleteCase(caseEntity)
            if (_selectedCaseId.value == caseEntity.id) {
                _selectedCaseId.value = null
                _selectedCase.value = null
            }
        }
    }

    fun addIntelNode(
        title: String,
        category: String,
        rawData: String,
        verificationStatus: String,
        sourceCount: Int,
        confidenceLevel: String,
        sourceUrls: String,
        notes: String,
        pivotClues: String
    ) {
        val caseId = _selectedCaseId.value ?: return
        viewModelScope.launch {
            intelNodeDao.insertNode(
                IntelNodeEntity(
                    caseId = caseId,
                    title = title,
                    category = category,
                    rawData = rawData,
                    verificationStatus = verificationStatus,
                    sourceCount = sourceCount,
                    confidenceLevel = confidenceLevel,
                    sourceUrls = sourceUrls,
                    notes = notes,
                    pivotClues = pivotClues
                )
            )
        }
    }

    fun toggleNodeVerification(node: IntelNodeEntity) {
        viewModelScope.launch {
            val isNowProven = node.verificationStatus != "PROVEN"
            val updated = node.copy(
                verificationStatus = if (isNowProven) "PROVEN" else "UNPROVEN",
                sourceCount = if (isNowProven) maxOf(node.sourceCount, 2) else 1,
                confidenceLevel = if (isNowProven) "VERIFIED" else "MEDIUM"
            )
            intelNodeDao.updateNode(updated)
        }
    }

    fun deleteIntelNode(node: IntelNodeEntity) {
        viewModelScope.launch {
            intelNodeDao.deleteNode(node)
        }
    }

    // Forensics EXIF Parsing
    fun processImageUri(uri: Uri) {
        _selectedImageUri.value = uri
        _forensicsStatus.value = "Inspecting digital properties & EXIF header..."

        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val exif = ExifInterface(inputStream)

                    val latLong = FloatArray(2)
                    val hasGps = exif.getLatLong(latLong)

                    val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
                        ?: exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                    val make = exif.getAttribute(ExifInterface.TAG_MAKE)
                    val model = exif.getAttribute(ExifInterface.TAG_MODEL)
                    val lens = exif.getAttribute(ExifInterface.TAG_LENS_MODEL)
                    val software = exif.getAttribute(ExifInterface.TAG_SOFTWARE)
                    val iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
                    val exposure = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
                    val fNumber = exif.getAttribute(ExifInterface.TAG_F_NUMBER)
                    val focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
                    val flash = exif.getAttribute(ExifInterface.TAG_FLASH)
                    val altitude = exif.getAltitude(0.0)

                    val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
                    val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

                    val tagsMap = mutableMapOf<String, String>()
                    if (make != null) tagsMap["Camera Make"] = make
                    if (model != null) tagsMap["Camera Model"] = model
                    if (lens != null) tagsMap["Lens"] = lens
                    if (dateTime != null) tagsMap["Capture Time"] = dateTime
                    if (software != null) tagsMap["Software / OS"] = software
                    if (iso != null) tagsMap["ISO Speed"] = iso
                    if (exposure != null) tagsMap["Exposure"] = exposure
                    if (fNumber != null) tagsMap["Aperture (f)"] = fNumber
                    if (hasGps) {
                        tagsMap["GPS Latitude"] = "${latLong[0]}"
                        tagsMap["GPS Longitude"] = "${latLong[1]}"
                    }

                    _exifData.value = ExifMetadata(
                        dateTime = dateTime,
                        make = make,
                        model = model,
                        lensModel = lens,
                        software = software,
                        latitude = if (hasGps) latLong[0].toDouble() else null,
                        longitude = if (hasGps) latLong[1].toDouble() else null,
                        altitude = if (altitude != 0.0) altitude else null,
                        iso = iso,
                        exposureTime = exposure,
                        fNumber = fNumber,
                        focalLength = focalLength,
                        flash = flash,
                        imageWidth = width,
                        imageHeight = height,
                        rawTags = tagsMap
                    )
                    _forensicsStatus.value = if (hasGps) "GPS Geolocation Coordinates Found!" else "EXIF extracted. No embedded GPS tags found (possibly stripped by platform)."
                }
            } catch (e: Exception) {
                _forensicsStatus.value = "Error parsing EXIF: ${e.message}"
            }
        }
    }

    fun attachExifAsIntelNode() {
        val exif = _exifData.value ?: return
        val caseId = _selectedCaseId.value ?: return

        val gpsInfo = if (exif.latitude != null && exif.longitude != null) {
            "GPS: ${exif.latitude}, ${exif.longitude} (Alt: ${exif.altitude ?: 0.0}m)"
        } else {
            "GPS: Not embedded (stripped/absent)"
        }

        val camera = "${exif.make ?: "Unknown"} ${exif.model ?: ""}".trim()
        val time = exif.dateTime ?: "Unknown timestamp"

        val rawData = "Camera: $camera\nTimestamp: $time\n$gpsInfo\nLens: ${exif.lensModel ?: "Default"}\nISO: ${exif.iso ?: "N/A"}"
        val isProven = exif.latitude != null && exif.longitude != null

        viewModelScope.launch {
            intelNodeDao.insertNode(
                IntelNodeEntity(
                    caseId = caseId,
                    title = "EXIF Forensics Extraction (${camera.ifEmpty { "Photo Asset" }})",
                    category = "EXIF / Imagery",
                    rawData = rawData,
                    verificationStatus = if (isProven) "PROVEN" else "UNPROVEN",
                    sourceCount = if (isProven) 2 else 1,
                    confidenceLevel = if (isProven) "HIGH" else "MEDIUM",
                    notes = "Extracted via Super OSINT mobile forensics suite.",
                    pivotClues = if (isProven) "Check local building blueprints and satellite images matching ${exif.latitude}, ${exif.longitude}." else "Calculate sun shadow angle at timestamp $time using SunCalc."
                )
            )
        }
    }

    // AI Tactical Analyst
    fun runAiAnalysis(customPrompt: String? = null) {
        val currentCase = _selectedCase.value
        val nodes = currentCaseNodes.value

        val prompt = if (!customPrompt.isNullOrBlank()) {
            buildCustomAiPrompt(customPrompt, currentCase, nodes)
        } else {
            buildDefaultAuditPrompt(currentCase, nodes)
        }

        _isAiLoading.value = true
        _aiError.value = null

        viewModelScope.launch {
            val result = GeminiClient.analyzeIntel(
                prompt = prompt,
                taskType = _selectedAiTaskType.value
            )

            _isAiLoading.value = false
            result.onSuccess { text ->
                _aiResponse.value = text
            }.onFailure { err ->
                _aiError.value = err.message
                // Provide offline fallback tactical synthesis if key is not yet set
                if (err.message?.contains("GEMINI_API_KEY") == true) {
                    _aiResponse.value = generateOfflineIntelligenceBrief(currentCase, nodes)
                }
            }
        }
    }

    private fun buildDefaultAuditPrompt(case: CaseEntity?, nodes: List<IntelNodeEntity>): String {
        val proven = nodes.filter { it.verificationStatus == "PROVEN" }
        val unproven = nodes.filter { it.verificationStatus == "UNPROVEN" }

        val builder = StringBuilder()
        builder.append("### OSINT CASE AUDIT & STRATEGIC ASSESSMENT\n\n")
        builder.append("TARGET PROFILE:\n")
        builder.append("- Case Title: ${case?.title ?: "Active Case"}\n")
        builder.append("- Subject Name: ${case?.targetName ?: "Unknown"}\n")
        builder.append("- Known Handles: ${case?.targetHandle ?: "N/A"}\n")
        builder.append("- Target Domain: ${case?.targetDomain ?: "N/A"}\n")
        builder.append("- Region of Interest: ${case?.locationOfInterest ?: "N/A"}\n\n")

        builder.append("--- PROVEN ANCHOR NODES (2+ Verified Sources) ---\n")
        if (proven.isEmpty()) builder.append("(No proven nodes established yet)\n")
        proven.forEachIndexed { i, n ->
            builder.append("${i + 1}. [${n.category}] ${n.title}\n   Data: ${n.rawData}\n   Notes: ${n.notes}\n")
        }

        builder.append("\n--- UNPROVEN LEAD SANDBOX (Active Leads To Corroborate) ---\n")
        if (unproven.isEmpty()) builder.append("(No active leads in sandbox)\n")
        unproven.forEachIndexed { i, n ->
            builder.append("${i + 1}. [${n.category}] ${n.title}\n   Data: ${n.rawData}\n   Clues: ${n.pivotClues}\n")
        }

        builder.append("\nREQUESTED TACTICAL DELIVERABLES:\n")
        builder.append("1. Critical Correlation: Analyze unproven leads against proven anchor nodes. Are there temporal or geographic contradictions?\n")
        builder.append("2. High-Yield Google Dorks: Generate 3-5 specific, ready-to-run search operators to corroborate unproven leads.\n")
        builder.append("3. Next High-Probability Pivot Points: Which databases (SEON, CourtListener, SunCalc, WiGLE) should be queried next?\n")
        builder.append("4. Threat/Deception Assessment: Are there indicators of identity spoofing or intentional metadata scrubbing?\n")

        return builder.toString()
    }

    private fun buildCustomAiPrompt(query: String, case: CaseEntity?, nodes: List<IntelNodeEntity>): String {
        return "You are an OSINT Tactical Advisor. Analyze this query regarding the active case:\n\n" +
                "Case Context: ${case?.title} (Subject: ${case?.targetName}, Handle: ${case?.targetHandle})\n" +
                "Current Verified Nodes: ${nodes.count { it.verificationStatus == "PROVEN" }}\n" +
                "Current Unproven Leads: ${nodes.count { it.verificationStatus == "UNPROVEN" }}\n\n" +
                "User Query: $query\n\n" +
                "Provide a precise, factual tactical answer with syntax recommendations and verification steps."
    }

    private fun generateOfflineIntelligenceBrief(case: CaseEntity?, nodes: List<IntelNodeEntity>): String {
        val proven = nodes.filter { it.verificationStatus == "PROVEN" }
        val unproven = nodes.filter { it.verificationStatus == "UNPROVEN" }

        return """
            ### 🛡️ TACTICAL CASE BRIEF (OFFLINE SYNTHESIS)
            
            **Case:** ${case?.title ?: "Active Case"}
            **Subject:** ${case?.targetName ?: "Unknown"} (${case?.targetHandle ?: "No handle"})
            **Target Domain:** ${case?.targetDomain ?: "N/A"}
            
            ---
            #### 1. Core Timeline Anchors (${proven.size} Proven Nodes)
            ${if (proven.isEmpty()) "• No multi-source proven nodes currently established." else proven.joinToString("\n") { "• [${it.category}] ${it.title}: Confirmed across ${it.sourceCount} sources." }}
            
            ---
            #### 2. Active Lead Sandbox (${unproven.size} Unproven Nodes)
            ${if (unproven.isEmpty()) "• Sandbox empty. Add loose indicators from social queries or photos." else unproven.joinToString("\n") { "• [${it.category}] ${it.title}: ${it.pivotClues}" }}
            
            ---
            #### 3. Recommended Automated Dork Actions
            • `"${case?.targetName ?: "Subject"}" AND ("missing" OR "contact" OR "last seen")`
            • `(site:github.com OR site:linkedin.com) "${case?.targetHandle ?: "handle"}"`
            • `site:${case?.targetDomain ?: "domain.com"} (filetype:pdf OR filetype:xlsx)`
            
            ---
            *(Note: To unlock real-time Gemini-4-Argon high thinking analysis and live cognitive correlation, enter your Gemini API key in the Secrets panel in AI Studio).*
        """.trimIndent()
    }

    fun exportDossierMarkdown(): String {
        val currentCase = _selectedCase.value
        val nodes = currentCaseNodes.value
        val proven = nodes.filter { it.verificationStatus == "PROVEN" }
        val unproven = nodes.filter { it.verificationStatus == "UNPROVEN" }

        val sb = StringBuilder()
        sb.append("# 📁 OSINT TACTICAL DOSSIER REPORT\n\n")
        sb.append("**Generated via Super OSINT (7y7g Intelligence Framework)**\n")
        sb.append("Timestamp: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", java.util.Locale.US).format(java.util.Date())}\n\n")
        sb.append("## 👤 SUBJECT PROFILE\n")
        sb.append("- **Case Title:** ${currentCase?.title}\n")
        sb.append("- **Target Name:** ${currentCase?.targetName}\n")
        sb.append("- **Target Handle:** ${currentCase?.targetHandle}\n")
        sb.append("- **Domain / Org:** ${currentCase?.targetDomain}\n")
        sb.append("- **Location of Interest:** ${currentCase?.locationOfInterest}\n")
        sb.append("- **Description:** ${currentCase?.description}\n\n")

        sb.append("## 🔒 PROVEN INTEL NODES (Verified by 2+ Sources)\n")
        if (proven.isEmpty()) {
            sb.append("*None registered yet.*\n\n")
        } else {
            proven.forEachIndexed { idx, n ->
                sb.append("### ${idx + 1}. ${n.title}\n")
                sb.append("- **Category:** ${n.category}\n")
                sb.append("- **Confidence:** ${n.confidenceLevel} (${n.sourceCount} verified sources)\n")
                sb.append("- **Raw Intelligence:**\n```\n${n.rawData}\n```\n")
                if (n.notes.isNotBlank()) sb.append("- **Verification Notes:** ${n.notes}\n")
                sb.append("\n")
            }
        }

        sb.append("## ⚠️ UNPROVEN LEAD SANDBOX (Context Clues For Further Dorking)\n")
        if (unproven.isEmpty()) {
            sb.append("*Sandbox empty.*\n\n")
        } else {
            unproven.forEachIndexed { idx, n ->
                sb.append("### ${idx + 1}. ${n.title}\n")
                sb.append("- **Category:** ${n.category}\n")
                sb.append("- **Raw Finding:**\n```\n${n.rawData}\n```\n")
                if (n.pivotClues.isNotBlank()) sb.append("- **Next Pivot Clues:** ${n.pivotClues}\n")
                sb.append("\n")
            }
        }

        sb.append("---\n*End of Report*\n")
        return sb.toString()
    }
}
