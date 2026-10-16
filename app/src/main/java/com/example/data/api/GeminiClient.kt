package com.example.data.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class GeminiTaskType(val modelName: String, val useThinking: Boolean) {
    COMPLEX_DEEP_REASONING("gemini-4-argon", true),
    GENERAL_INTEL_ANALYSIS("gemini-3.8-flash", false),
    FAST_LEAD_EVALUATION("gemini-3.1-flash-lite-preview", false)
}

object GeminiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun analyzeIntel(
        prompt: String,
        systemInstruction: String = "You are a senior Open Source Intelligence (OSINT) Tactical Analyst specializing in missing persons, humanitarian crisis intelligence, and public record verification. Deliver concise, actionable intelligence assessments, highlight discrepancy risks, and recommend precise search dorks.",
        taskType: GeminiTaskType = GeminiTaskType.COMPLEX_DEEP_REASONING
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("GEMINI_API_KEY is not configured. Please set your Gemini API key in the Secrets panel in AI Studio.")
            )
        }

        try {
            val root = JSONObject()

            // System Instruction
            val sysPart = JSONObject().put("text", systemInstruction)
            val sysContent = JSONObject().put("parts", JSONArray().put(sysPart))
            root.put("systemInstruction", sysContent)

            // User Content
            val userPart = JSONObject().put("text", prompt)
            val userContent = JSONObject().put("parts", JSONArray().put(userPart))
            root.put("contents", JSONArray().put(userContent))

            // Generation Config
            val genConfig = JSONObject()
            if (taskType.useThinking) {
                // High thinking mode with gemini-4-argon
                val thinkingConfig = JSONObject().put("thinkingLevel", "HIGH")
                genConfig.put("thinkingConfig", thinkingConfig)
            }
            root.put("generationConfig", genConfig)

            val url = "https://generativelanguage.googleapis.com/v1beta/models/${taskType.modelName}:generateContent?key=$apiKey"

            val body = root.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                val responseString = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Gemini API Error (HTTP ${response.code}): $responseString")
                    )
                }

                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val textBuilder = StringBuilder()
                        for (i in 0 until parts.length()) {
                            val part = parts.getJSONObject(i)
                            val txt = part.optString("text")
                            if (txt.isNotEmpty()) {
                                textBuilder.append(txt)
                            }
                        }
                        return@withContext Result.success(textBuilder.toString())
                    }
                }
                Result.failure(Exception("No content returned from model."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
