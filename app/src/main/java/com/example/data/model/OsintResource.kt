package com.example.data.model

data class OsintResource(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val urlTemplate: String, // e.g. "https://www.tiktok.com/@{}" or static url
    val pricingType: String, // "FREE", "FREEMIUM", "PAID API", "COMMUNITY"
    val verificationMethod: String,
    val trustedIndicators: List<String>,
    val unprovenIndicators: List<String>,
    val isDynamicHandleSupported: Boolean = false
) {
    fun getResolvedUrl(targetInput: String): String {
        val clean = targetInput.trim().removePrefix("@")
        return if (isDynamicHandleSupported && clean.isNotEmpty()) {
            urlTemplate.replace("{}", clean)
        } else {
            urlTemplate.replace("{}", "")
        }
    }
}

data class OsintCategoryGroup(
    val categoryName: String,
    val iconName: String,
    val description: String,
    val tools: List<OsintResource>
)
