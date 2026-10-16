package com.example.data.model

object ApiEcosystemData {
    val apis: List<ApiEcosystemDoc> = listOf(
        ApiEcosystemDoc(
            id = "seon",
            name = "SEON.io Identity API",
            category = "Identity & Social Footprint",
            purpose = "Scans an email address or phone handle across 50+ major social networks simultaneously to discover registered accounts.",
            endpointTemplate = "https://api.seon.io/seonapi/v2.0/identity/{handle}",
            authType = "X-API-KEY Header",
            provenIndicators = "Returns a direct live link to an active profile with matching user avatars and verified timestamps.",
            unprovenIndicators = "Returns a positive match showing an account exists, but the profile is private or contains zero public posts.",
            pricing = "Paid / Free Tier Trial",
            officialDocUrl = "https://seon.io"
        ),
        ApiEcosystemDoc(
            id = "osint_industries",
            name = "OSINT Industries API",
            category = "Identity & Social Footprint",
            purpose = "Performs real-time reverse lookup on phone numbers and emails to extract hidden profile IDs on TikTok, Google, and Skype.",
            endpointTemplate = "https://api.osint.industries/v1/search",
            authType = "Bearer Token",
            provenIndicators = "Extracts verified system IDs and live avatar endpoints that tie directly back to the known subject timeline.",
            unprovenIndicators = "Extracts historical account matches that use phonetic handle variations but lack recent activity.",
            pricing = "Paid / Credits",
            officialDocUrl = "https://osint.industries"
        ),
        ApiEcosystemDoc(
            id = "numlookupapi",
            name = "Numlookupapi & Twilio Lookup",
            category = "Telecom & Phone Lookup",
            purpose = "Validates live network connectivity status, carrier routing, mobile country code (MCC/MNC), and VoIP status.",
            endpointTemplate = "https://api.numlookupapi.com/v1/validate/{number}?apikey=[KEY]",
            authType = "Query Parameter API Key",
            provenIndicators = "Line status returns ACTIVE and registered to the exact domestic mobile carrier used by the subject.",
            unprovenIndicators = "Line returns active but registered to a disposable virtual number provider (VoIP) or temporary web burner.",
            pricing = "Freemium (100 free requests)",
            officialDocUrl = "https://numlookupapi.com"
        ),
        ApiEcosystemDoc(
            id = "ipinfo",
            name = "IPinfo.io VPN & Spoofer Detector",
            category = "Geolocation & Network",
            purpose = "Analyzes incoming IP network hops to identify hosting organizations, residential vs data center status, proxy routing, or active VPNs.",
            endpointTemplate = "https://ipinfo.io/{ip}?token=[TOKEN]",
            authType = "Bearer Token / Query Token",
            provenIndicators = "Connection routes through a residential broadband ISP matching the subject's home municipality.",
            unprovenIndicators = "Connection tests positive for commercial data center routing (AWS, DigitalOcean, Tor), signaling active proxy masking.",
            pricing = "Free (50,000 req/mo) / Paid",
            officialDocUrl = "https://ipinfo.io"
        ),
        ApiEcosystemDoc(
            id = "google_maps_geocoding",
            name = "Google Maps Geocoding & Places",
            category = "Geolocation & Network",
            purpose = "Converts unstructured text descriptions or landmarks found in posts into high-precision latitude/longitude coordinates.",
            endpointTemplate = "https://maps.googleapis.com/maps/api/geocode/json?address={address}&key=[KEY]",
            authType = "Platform API Key",
            provenIndicators = "Text address maps cleanly to physical property boundary matching county property deeds.",
            unprovenIndicators = "Text references point vaguely to a generalized municipal district or a large state park boundary.",
            pricing = "Pay-as-you-go ($200 monthly free credit)",
            officialDocUrl = "https://developers.google.com/maps"
        ),
        ApiEcosystemDoc(
            id = "opencorporates_api",
            name = "OpenCorporates REST API",
            category = "Corporate & Legal",
            purpose = "Query corporate registries worldwide for active directorships, company numbers, incorporation dates, and filings.",
            endpointTemplate = "https://api.opencorporates.com/v1/companies/search?q={query}",
            authType = "api_token Query Parameter",
            provenIndicators = "Target listed as active director or statutory agent with matching birth month and nationality.",
            unprovenIndicators = "Inactive/dissolved companies sharing name variations or managed by nominee agents.",
            pricing = "Free Public / Commercial",
            officialDocUrl = "https://opencorporates.com"
        ),
        ApiEcosystemDoc(
            id = "courtlistener_api",
            name = "CourtListener RECAP API",
            category = "Corporate & Legal",
            purpose = "Search millions of legal opinions, federal court filings, PACER dockets, and judicial disclosure forms.",
            endpointTemplate = "https://www.courtlistener.com/api/rest/v4/search/?q={query}",
            authType = "Token Header Authorization",
            provenIndicators = "Active legal judgment, bankruptcy filing, or civil docket naming the subject directly.",
            unprovenIndicators = "General mention in legal amicus curiae briefs or unrelated litigation.",
            pricing = "Free Open API",
            officialDocUrl = "https://www.courtlistener.com/help/api/"
        ),
        ApiEcosystemDoc(
            id = "shodan_api",
            name = "Shodan Search Engine API",
            category = "Threat Intel & Infrastructure",
            purpose = "Search internet-connected hardware, opened ports, exposed webcams, industrial control systems, and banners.",
            endpointTemplate = "https://api.shodan.io/shodan/host/{ip}?key=[KEY]",
            authType = "Query Parameter Key",
            provenIndicators = "Exposed IP matches target home router certificate or unique device hostname.",
            unprovenIndicators = "Common ISP gateway signature without host-specific telemetry.",
            pricing = "Academic Free / Commercial",
            officialDocUrl = "https://developer.shodan.io"
        ),
        ApiEcosystemDoc(
            id = "consensual_drive",
            name = "Google Drive & Photos REST (Consensual)",
            category = "Consensual Core Integrations",
            purpose = "Extracts technical camera attributes, file modification timelines, and embedded GPS metadata from authorized backups.",
            endpointTemplate = "https://www.googleapis.com/drive/v3/files/{fileId}?fields=properties,imageMediaMetadata",
            authType = "OAuth2 Client Authorization",
            provenIndicators = "Image metadata contains intact GPS tags and hardware serial numbers matching the target's phone.",
            unprovenIndicators = "Saved files show stripped EXIF tags, indicating the files passed through automated social media stripping.",
            pricing = "Free via Google Cloud",
            officialDocUrl = "https://developers.google.com/drive"
        ),
        ApiEcosystemDoc(
            id = "telegram_bot_api",
            name = "Telegram Bot API (Export / Verification)",
            category = "Consensual Core Integrations",
            purpose = "Resolves public usernames to unique system chat IDs and checks profile descriptions or parses authorized chat backups.",
            endpointTemplate = "https://api.telegram.org/bot[BOT_TOKEN]/getChat?chat_id={handle}",
            authType = "Bot Token",
            provenIndicators = "System chat ID resolves to a public bio containing known backup communication links or personal names.",
            unprovenIndicators = "Account lacks custom handle but telephone routing validates account existence.",
            pricing = "Free Official Bot API",
            officialDocUrl = "https://core.telegram.org/bots/api"
        )
    )
}
