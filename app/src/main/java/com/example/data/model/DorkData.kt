package com.example.data.model

object DorkData {
    val templates: List<DorkTemplate> = listOf(
        DorkTemplate(
            id = "missing_person_dork",
            title = "Missing Person & Crisis Notice Dork",
            category = "Crisis & Missing Persons",
            description = "Isolate official police bulletins, crisis alerts, rescue appeals, and last-seen reports.",
            syntaxPattern = "\"[TARGET]\" AND (\"missing\" OR \"lost\" OR \"foul play\" OR \"last seen\" OR \"contact\" OR \"help locate\")",
            primaryFieldPlaceholder = "Missing Subject Name",
            sampleValues = Pair("Julian Vance", "")
        ),
        DorkTemplate(
            id = "social_footprint_matrix",
            title = "Multi-Network Social Handle Pivot",
            category = "Social Footprint",
            description = "Search all major social networks simultaneously for an exact username handle or alias.",
            syntaxPattern = "(site:tiktok.com OR site:youtube.com OR site:facebook.com OR site:instagram.com OR site:reddit.com OR site:linkedin.com) \"[TARGET]\"",
            primaryFieldPlaceholder = "Target Handle or Username",
            sampleValues = Pair("octoberian1012", "")
        ),
        DorkTemplate(
            id = "historical_geo_dork",
            title = "Historical Geolocation & Check-ins",
            category = "Geolocation",
            description = "Locate public forum posts, trip itineraries, GPS logs, or check-in tags mentioning the subject.",
            syntaxPattern = "\"[TARGET]\" AND (\"checked in\" OR \"located at\" OR \"trailhead\" OR \"coordinates\" OR \"geo:\" OR \"summit\")",
            primaryFieldPlaceholder = "Subject Name or Handle",
            sampleValues = Pair("Julian Vance", "")
        ),
        DorkTemplate(
            id = "exposed_credentials",
            title = "Leaked Credentials & Code Mentions",
            category = "Exposed / Leaks",
            description = "Check GitHub, GitLab, and public repositories for inadvertently committed keys, tokens, or emails.",
            syntaxPattern = "site:github.com \"[TARGET]\" (\"api_key\" OR \"password\" OR \"token\" OR \"secret\" OR \"AWS_KEY\")",
            primaryFieldPlaceholder = "Developer Handle or Target",
            sampleValues = Pair("octoberian1012", "")
        ),
        DorkTemplate(
            id = "public_resumes_dossiers",
            title = "Resumes, CVs & Employment History",
            category = "Documents & Files",
            description = "Find unindexed PDF resumes, executive bios, and personal curriculum vitae containing phone numbers or addresses.",
            syntaxPattern = "\"[TARGET]\" filetype:pdf (resume OR cv OR \"curriculum vitae\" OR \"contact info\" OR \"cell\")",
            primaryFieldPlaceholder = "Target Full Name",
            sampleValues = Pair("Julian Vance", "")
        ),
        DorkTemplate(
            id = "unindexed_corporate_files",
            title = "Confidential Corporate File Scan",
            category = "Documents & Files",
            description = "Scan a target organization's domain for exposed spreadsheets, contracts, and internal presentations.",
            syntaxPattern = "site:[DOMAIN] (filetype:pdf OR filetype:xlsx OR filetype:docx OR filetype:csv) \"confidential\" OR \"internal only\" OR \"[TARGET]\"",
            primaryFieldPlaceholder = "Target Subject / Department",
            secondaryFieldPlaceholder = "Target Domain (e.g. vancetech-labs.io)",
            sampleValues = Pair("Engineering", "vancetech-labs.io")
        ),
        DorkTemplate(
            id = "subdomain_enumeration",
            title = "Subdomain & Portal Enumeration",
            category = "Infrastructure",
            description = "Enumerate third-party staging servers, APIs, VPN portals, and internal subdomains of a target web property.",
            syntaxPattern = "site:*.[DOMAIN] -www.[DOMAIN]",
            primaryFieldPlaceholder = "Target Domain",
            secondaryFieldPlaceholder = "",
            sampleValues = Pair("vancetech-labs.io", "")
        ),
        DorkTemplate(
            id = "paste_site_dumps",
            title = "Pastebin & Dump Sites Scan",
            category = "Exposed / Leaks",
            description = "Discover unindexed data dumps, account compromises, or whistleblower text dumps.",
            syntaxPattern = "(site:pastebin.com OR site:justpaste.it OR site:rentry.co) \"[TARGET]\"",
            primaryFieldPlaceholder = "Target Email or Handle",
            sampleValues = Pair("julian.vance@gmail.com", "")
        )
    )
}
