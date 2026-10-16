package com.example.data.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class DorkTemplate(
    val id: String,
    val title: String,
    val category: String, // "Crisis & Missing Persons", "Social Footprint", "Data Leak / Exposed", "Documents & Files", "Infrastructure"
    val description: String,
    val syntaxPattern: String, // e.g. "\"[TARGET]\" AND (\"missing\" OR \"lost\" OR \"foul play\" OR \"last seen\")"
    val defaultEngine: String = "Google",
    val primaryFieldPlaceholder: String = "Target Name or Handle",
    val secondaryFieldPlaceholder: String = "",
    val sampleValues: Pair<String, String> = Pair("Julian Vance", "")
) {
    fun buildQuery(primary: String, secondary: String = ""): String {
        var query = syntaxPattern
        query = query.replace("[TARGET]", primary.trim().ifEmpty { sampleValues.first })
        if (secondary.isNotBlank()) {
            query = query.replace("[DOMAIN]", secondary.trim())
            query = query.replace("[SECONDARY]", secondary.trim())
        } else {
            query = query.replace("[DOMAIN]", sampleValues.second.ifEmpty { "example.com" })
            query = query.replace("[SECONDARY]", "")
        }
        return query.trim()
    }

    fun getSearchEngineUrl(engine: String, query: String): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        return when (engine.lowercase()) {
            "duckduckgo" -> "https://duckduckgo.com/?q=$encoded"
            "brave" -> "https://search.brave.com/search?q=$encoded"
            "yandex" -> "https://yandex.com/search/?text=$encoded"
            "bing" -> "https://www.bing.com/search?q=$encoded"
            else -> "https://www.google.com/search?q=$encoded"
        }
    }
}
