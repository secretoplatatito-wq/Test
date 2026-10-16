package com.example.data.model

data class ApiEcosystemDoc(
    val id: String,
    val name: String,
    val category: String, // "Identity & Social Footprint", "Telecom & Phone Lookup", "Geolocation & Network", "Corporate & Legal", "Consensual Core Integrations"
    val purpose: String,
    val endpointTemplate: String,
    val authType: String,
    val provenIndicators: String,
    val unprovenIndicators: String,
    val pricing: String,
    val officialDocUrl: String
)
