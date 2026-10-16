package com.example.data.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class DeepSearchPoint(
    val id: String,
    val title: String,
    val category: String, // "Identity & Social", "Document Excavation", "Geospatial & Check-ins", "Leaked Credentials & Code", "Corporate & Legal", "Archives & Historical"
    val query: String,
    val description: String,
    val searchEngine: String = "Google",
    val precisionRating: String = "HIGH PRECISION" // HIGH PRECISION, MODERATE, BROAD SWEEP
) {
    fun getLaunchUrl(engine: String = searchEngine): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        return when (engine.lowercase()) {
            "duckduckgo" -> "https://duckduckgo.com/?q=$encoded"
            "brave" -> "https://search.brave.com/search?q=$encoded"
            "yandex" -> "https://yandex.com/search/?text=$encoded"
            "bing" -> "https://www.bing.com/search?q=$encoded"
            "wayback" -> "https://web.archive.org/web/*/$encoded"
            else -> "https://www.google.com/search?q=$encoded"
        }
    }
}

object DeepSearchEngine {
    fun generateAllVectors(
        name: String,
        handle: String,
        email: String,
        phone: String,
        location: String,
        domain: String,
        keywords: String
    ): List<DeepSearchPoint> {
        val list = mutableListOf<DeepSearchPoint>()
        val cleanName = name.trim()
        val cleanHandle = handle.trim().removePrefix("@")
        val cleanEmail = email.trim()
        val cleanPhone = phone.trim()
        val cleanLocation = location.trim()
        val cleanDomain = domain.trim()
        val cleanKw = keywords.trim()

        val primaryTarget = cleanName.ifEmpty { cleanHandle.ifEmpty { "Target Subject" } }

        // 1. Critical Missing Persons / Crisis Sweep
        if (cleanName.isNotEmpty() || cleanHandle.isNotEmpty()) {
            val crisisKeywords = "(\"missing\" OR \"lost\" OR \"foul play\" OR \"last seen\" OR \"disappeared\" OR \"contact info\" OR \"warrant\")"
            val locPart = if (cleanLocation.isNotEmpty()) " AND \"$cleanLocation\"" else ""
            list.add(
                DeepSearchPoint(
                    id = "crisis_alert_sweep",
                    title = "🚨 Missing Persons & Crisis Bulletins",
                    category = "Identity & Social",
                    query = "\"$primaryTarget\" AND $crisisKeywords$locPart",
                    description = "Searches indexed law enforcement, news bulletins, and missing person registries with high accuracy.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 2. Comprehensive Social Footprint Deep Sweep (12+ platforms simultaneously)
        if (cleanHandle.isNotEmpty() || cleanName.isNotEmpty()) {
            val handleOrName = cleanHandle.ifEmpty { cleanName }
            list.add(
                DeepSearchPoint(
                    id = "social_mega_sweep",
                    title = "📱 12+ Network Social Footprint Matrix",
                    category = "Identity & Social",
                    query = "(site:tiktok.com OR site:youtube.com OR site:facebook.com OR site:instagram.com OR site:reddit.com OR site:linkedin.com OR site:twitter.com OR site:x.com OR site:github.com OR site:threads.net OR site:pinterest.com OR site:medium.com) \"$handleOrName\"",
                    description = "Simultaneous multi-site query across all top social platforms to detect active profiles, usernames, and posts.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 3. Document & Resume Excavation (.pdf, .xlsx, .docx, .kml, .csv)
        if (cleanName.isNotEmpty() || cleanHandle.isNotEmpty()) {
            val target = cleanName.ifEmpty { cleanHandle }
            val extra = if (cleanLocation.isNotEmpty()) " \"$cleanLocation\"" else ""
            list.add(
                DeepSearchPoint(
                    id = "doc_excavation",
                    title = "📄 Public Resumes, Dossiers & Curricula",
                    category = "Document Excavation",
                    query = "\"$target\" filetype:pdf (resume OR cv OR \"curriculum vitae\" OR \"bio\" OR \"contact\" OR \"phone\" OR \"address\")$extra",
                    description = "Discovers indexed PDF resumes, academic publications, or conference speaker dossiers with contact details.",
                    precisionRating = "HIGH PRECISION"
                )
            )

            list.add(
                DeepSearchPoint(
                    id = "spreadsheet_leak_sweep",
                    title = "📊 Financial & Contact Spreadsheets (.xlsx, .csv)",
                    category = "Document Excavation",
                    query = "\"$target\" (filetype:xlsx OR filetype:xls OR filetype:csv) (\"roster\" OR \"attendees\" OR \"budget\" OR \"clients\" OR \"directory\")",
                    description = "Searches for public attendance lists, membership rosters, and leaked spreadsheet databases containing the target.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 4. Geolocation & Itinerary Check-ins
        if (cleanName.isNotEmpty() || cleanHandle.isNotEmpty() || cleanLocation.isNotEmpty()) {
            val target = cleanName.ifEmpty { cleanHandle }
            val loc = if (cleanLocation.isNotEmpty()) " (\"$cleanLocation\" OR \"trail\" OR \"hotel\" OR \"camp\" OR \"summit\" OR \"airport\")" else " (\"checked in\" OR \"located at\" OR \"coordinates\" OR \"geo:\")"
            list.add(
                DeepSearchPoint(
                    id = "geo_checkin_sweep",
                    title = "📍 Historical Geolocation & Forum Check-ins",
                    category = "Geospatial & Check-ins",
                    query = "\"$target\" AND (\"checked in\" OR \"located at\" OR \"itinerary\" OR \"flight\" OR \"trailhead\" OR \"coordinates\")$loc",
                    description = "Locates outdoor forum logs, hiking logs, flight plans, and municipal check-in tags.",
                    precisionRating = "MODERATE"
                )
            )

            if (cleanLocation.isNotEmpty()) {
                list.add(
                    DeepSearchPoint(
                        id = "gps_kml_tracks",
                        title = "🗺️ GPS Tracks & Map Overlays (.kml, .gpx)",
                        category = "Geospatial & Check-ins",
                        query = "(\"$target\" OR \"$cleanLocation\") (filetype:kml OR filetype:gpx OR filetype:kmz) (\"trail\" OR \"route\" OR \"waypoints\")",
                        description = "Excavates uploaded GPS routes, Strava GPX files, and Google Earth KML files in the location of interest.",
                        precisionRating = "HIGH PRECISION"
                    )
                )
            }
        }

        // 5. Email & Phone Telemetry
        if (cleanEmail.isNotEmpty()) {
            list.add(
                DeepSearchPoint(
                    id = "email_exact_sweep",
                    title = "✉️ Email Web Mentions & Mailing Lists",
                    category = "Identity & Social",
                    query = "\"$cleanEmail\" (site:pastebin.com OR site:github.com OR site:groups.google.com OR site:archive.org OR intext:\"$cleanEmail\")",
                    description = "Pinpoints exact email mentions across public mailing lists, commit logs, and archived groups.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        if (cleanPhone.isNotEmpty()) {
            val rawPhone = cleanPhone.replace(Regex("[^0-9]"), "")
            list.add(
                DeepSearchPoint(
                    id = "phone_registry_sweep",
                    title = "📞 Phone Number Public Directory Sweep",
                    category = "Identity & Social",
                    query = "(\"$cleanPhone\" OR \"$rawPhone\") (\"directory\" OR \"contact\" OR \"cell\" OR \"office\" OR \"owner\")",
                    description = "Searches public business listings, municipal contacts, and classified ads quoting this phone number.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 6. Exposed Credentials, Tokens & Git Commits
        if (cleanHandle.isNotEmpty() || cleanName.isNotEmpty() || cleanDomain.isNotEmpty()) {
            val devTarget = cleanHandle.ifEmpty { cleanName }
            list.add(
                DeepSearchPoint(
                    id = "git_credentials_sweep",
                    title = "🔑 Exposed API Keys, Tokens & Git Secrets",
                    category = "Leaked Credentials & Code",
                    query = "site:github.com (\"$devTarget\" OR \"$cleanDomain\") (\"api_key\" OR \"token\" OR \"password\" OR \"BEGIN RSA PRIVATE KEY\" OR \"AWS_SECRET\")",
                    description = "Identifies inadvertently committed secrets, keys, and tokens linked to the user's handle or company domain.",
                    precisionRating = "HIGH PRECISION"
                )
            )

            list.add(
                DeepSearchPoint(
                    id = "paste_leak_dumps",
                    title = "📋 Pastebin, Ghostbin & Text Dumps",
                    category = "Leaked Credentials & Code",
                    query = "(site:pastebin.com OR site:rentry.co OR site:justpaste.it OR site:controlc.com) (\"$devTarget\" OR \"$cleanEmail\")",
                    description = "Extracts plain-text pastes, leaked user lists, and developer logs.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 7. Domain & Subdomain Infrastructure
        if (cleanDomain.isNotEmpty()) {
            list.add(
                DeepSearchPoint(
                    id = "subdomain_enum",
                    title = "🌐 Subdomain & Portal Infrastructure Discovery",
                    category = "Corporate & Legal",
                    query = "site:*.$cleanDomain -www.$cleanDomain (portal OR api OR staging OR dev OR vpn OR login OR test)",
                    description = "Enumerates hidden subdomains, internal developer staging portals, and VPN endpoints.",
                    precisionRating = "HIGH PRECISION"
                )
            )

            list.add(
                DeepSearchPoint(
                    id = "domain_internal_files",
                    title = "📁 Exposed Domain Files (.env, .log, .conf, .sql)",
                    category = "Corporate & Legal",
                    query = "site:$cleanDomain (filetype:env OR filetype:log OR filetype:sql OR filetype:conf OR filetype:ini)",
                    description = "Scans target domain for improperly protected server configuration files and database dumps.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 8. Legal, Court & Corporate Registries
        if (cleanName.isNotEmpty() || cleanDomain.isNotEmpty()) {
            val legalTarget = cleanName.ifEmpty { cleanDomain }
            list.add(
                DeepSearchPoint(
                    id = "court_legal_sweep",
                    title = "⚖️ Court Dockets, Bankruptcies & SEC Filings",
                    category = "Corporate & Legal",
                    query = "\"$legalTarget\" (site:courtlistener.com OR site:justia.com OR site:sec.gov OR site:govinfo.gov) (\"plaintiff\" OR \"defendant\" OR \"director\" OR \"deposition\")",
                    description = "Exhaustive legal docket search across federal and state court filings, judicial opinions, and regulatory entries.",
                    precisionRating = "HIGH PRECISION"
                )
            )

            list.add(
                DeepSearchPoint(
                    id = "corporate_officer_sweep",
                    title = "🏢 Beneficial Ownership & Directorship Web",
                    category = "Corporate & Legal",
                    query = "\"$legalTarget\" (\"director\" OR \"officer\" OR \"incorporator\" OR \"registered agent\" OR \"annual report\") (site:opencorporates.com OR \"articles of incorporation\")",
                    description = "Discovers corporate entities, dissolved shell companies, and legal affiliations.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        // 9. Historical Web Archives & Deleted Footprints
        if (cleanDomain.isNotEmpty() || cleanHandle.isNotEmpty()) {
            val histTarget = cleanDomain.ifEmpty { cleanHandle }
            list.add(
                DeepSearchPoint(
                    id = "wayback_archive_sweep",
                    title = "🏛️ Wayback Machine & Archive.is Caches",
                    category = "Archives & Historical",
                    query = "site:web.archive.org/web/* \"$histTarget\"",
                    description = "Extracts historical snapshots, scrubbed blog posts, and deleted biographies captured by Internet Archive.",
                    precisionRating = "HIGH PRECISION",
                    searchEngine = "Google"
                )
            )
        }

        // 10. Custom Combined Boolean Deep Vector
        if (cleanKw.isNotEmpty()) {
            list.add(
                DeepSearchPoint(
                    id = "custom_deep_vector",
                    title = "🔬 Custom Operator Matrix ($cleanKw)",
                    category = "Identity & Social",
                    query = "\"$primaryTarget\" AND ($cleanKw)",
                    description = "User-customized deep Boolean query injecting specific operation keywords.",
                    precisionRating = "HIGH PRECISION"
                )
            )
        }

        return list
    }
}
