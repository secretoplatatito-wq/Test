package com.example.data.model

object OsintDirectoryData {
    val categories: List<OsintCategoryGroup> = listOf(
        OsintCategoryGroup(
            categoryName = "📱 Username & Social Media",
            iconName = "share",
            description = "Cross-platform handle matching, historical profile archiving, and digital presence tracking.",
            tools = listOf(
                OsintResource(
                    id = "namechk",
                    name = "Namechk Global Search",
                    category = "Username & Social",
                    description = "Check username availability and registration across 100+ social networks simultaneously.",
                    urlTemplate = "https://namechk.com/",
                    pricingType = "FREE",
                    verificationMethod = "HTTP Status Code & Profile Validation",
                    trustedIndicators = listOf("Active account confirmed with matching avatar", "Matching biographical details"),
                    unprovenIndicators = listOf("Account registered but 0 posts or private profile")
                ),
                OsintResource(
                    id = "social_searcher",
                    name = "Social Searcher",
                    category = "Username & Social",
                    description = "Real-time social media search engine for public mentions, sentiments, and posts across major networks.",
                    urlTemplate = "https://www.social-searcher.com/search-users/?q={}",
                    pricingType = "FREEMIUM",
                    verificationMethod = "Live Multi-Network Feed Scrape",
                    trustedIndicators = listOf("Direct post authored by subject with matching timestamps"),
                    unprovenIndicators = listOf("Third-party comments mentioning name phonetically"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "tiktok_direct",
                    name = "TikTok Profile Probe",
                    category = "Username & Social",
                    description = "Direct web endpoint pivot for subject handle lookups and video metadata extraction.",
                    urlTemplate = "https://www.tiktok.com/@{}",
                    pricingType = "FREE",
                    verificationMethod = "Endpoint Verification",
                    trustedIndicators = listOf("Verified badge or self-recorded video content"),
                    unprovenIndicators = listOf("Placeholder bio with no uploaded clips"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "youtube_direct",
                    name = "YouTube Channel Probe",
                    category = "Username & Social",
                    description = "Pivot directly into YouTube user and channel archives to inspect public playlists and upload activity.",
                    urlTemplate = "https://www.youtube.com/@{}",
                    pricingType = "FREE",
                    verificationMethod = "Channel ID & Feed Parsing",
                    trustedIndicators = listOf("Video uploads matching known subject timeline and voice"),
                    unprovenIndicators = listOf("Comments left on regional news or hobby channels"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "github_api",
                    name = "GitHub Profile & GPG Commits",
                    category = "Username & Social",
                    description = "Enumerate public developer repositories, commit timestamps, and public SSH/GPG keys.",
                    urlTemplate = "https://github.com/{}",
                    pricingType = "FREE API",
                    verificationMethod = "REST API & Commit History",
                    trustedIndicators = listOf("Verified commit email matching target domain", "GPG signed commits"),
                    unprovenIndicators = listOf("Starred repositories without direct commit activity"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "facebook_public",
                    name = "Facebook Public Graph",
                    category = "Username & Social",
                    description = "Open directory index lookup for public individual profiles and check-in pages.",
                    urlTemplate = "https://www.facebook.com/public/{}",
                    pricingType = "FREE",
                    verificationMethod = "Public Graph Lookup",
                    trustedIndicators = listOf("High-confidence location check-ins and mutual relatives"),
                    unprovenIndicators = listOf("Identical name accounts without geographical relevance"),
                    isDynamicHandleSupported = true
                )
            )
        ),
        OsintCategoryGroup(
            categoryName = "👤 People Directories & Records",
            iconName = "person",
            description = "Public record registries, voter indices, land records, and identity verification portals.",
            tools = listOf(
                OsintResource(
                    id = "whitepages",
                    name = "Whitepages Public Index",
                    category = "People Directories",
                    description = "U.S. public telephone, current address, and relative record database.",
                    urlTemplate = "https://www.whitepages.com/name/{}",
                    pricingType = "FREEMIUM",
                    verificationMethod = "Public Telecom & Utility Records",
                    trustedIndicators = listOf("Direct matching phone number and residential lease records"),
                    unprovenIndicators = listOf("Secondary addresses associated with distant cousins"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "truepeoplesearch",
                    name = "TruePeopleSearch",
                    category = "People Directories",
                    description = "Comprehensive background relationship mapping, historical addresses, and contact listings.",
                    urlTemplate = "https://www.truepeoplesearch.com/find/person/{}",
                    pricingType = "FREE",
                    verificationMethod = "Aggregated County & Public Utility Records",
                    trustedIndicators = listOf("Exact birth year and historical address match"),
                    unprovenIndicators = listOf("Associates without direct co-habitation records"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "osintframework",
                    name = "OSINT Framework Tree",
                    category = "People Directories",
                    description = "The foundational tactical tree index for free open-source intelligence collection.",
                    urlTemplate = "https://osintframework.com/",
                    pricingType = "FREE",
                    verificationMethod = "Aggregated Resource Index",
                    trustedIndicators = listOf("Multi-source record match across linked municipal registries"),
                    unprovenIndicators = listOf("Single unverified index link")
                ),
                OsintResource(
                    id = "courtlistener",
                    name = "CourtListener Legal Records",
                    category = "People Directories",
                    description = "Free Law Project database indexing federal and state court filings, dockets, and oral arguments.",
                    urlTemplate = "https://www.courtlistener.com/?q={}",
                    pricingType = "FREE API",
                    verificationMethod = "Official Court Dockets & RECAP Archive",
                    trustedIndicators = listOf("Signed judicial depositions or court appearances"),
                    unprovenIndicators = listOf("Witness mentions in unrelated civil filings"),
                    isDynamicHandleSupported = true
                )
            )
        ),
        OsintCategoryGroup(
            categoryName = "🖼️ Images, Video & Forensics",
            iconName = "camera",
            description = "Reverse visual search, metadata inspection, solar shadow calculation, and photo forensics.",
            tools = listOf(
                OsintResource(
                    id = "google_lens",
                    name = "Google Images / Lens",
                    category = "Forensics",
                    description = "Global image index searching visual duplicates, landmarks, and context matches.",
                    urlTemplate = "https://images.google.com/",
                    pricingType = "FREE",
                    verificationMethod = "Visual Embedding Neural Search",
                    trustedIndicators = listOf("Exact matching original high-res photo location source"),
                    unprovenIndicators = listOf("Similar architectural layout in a different region")
                ),
                OsintResource(
                    id = "tineye",
                    name = "TinEye Reverse Search",
                    category = "Forensics",
                    description = "Dedicated reverse image engine sorting by oldest crawl date to identify original upload origins.",
                    urlTemplate = "https://tineye.com/",
                    pricingType = "FREEMIUM",
                    verificationMethod = "Perceptual Image Hash Match",
                    trustedIndicators = listOf("Earliest web crawl date pinpointing original author"),
                    unprovenIndicators = listOf("Stock image duplicates used across marketing blogs")
                ),
                OsintResource(
                    id = "fotoforensics",
                    name = "FotoForensics ELA",
                    category = "Forensics",
                    description = "Error Level Analysis (ELA) and digital compression forensic analyzer for altered photos.",
                    urlTemplate = "http://fotoforensics.com/",
                    pricingType = "FREE",
                    verificationMethod = "Error Level Analysis & Quantization Table Scan",
                    trustedIndicators = listOf("Intact EXIF tags matching physical hardware specifications"),
                    unprovenIndicators = listOf("ELA highlights indicating edited text or superimposed signs")
                ),
                OsintResource(
                    id = "suncalc",
                    name = "SunCalc Solar Position",
                    category = "Forensics",
                    description = "Calculate sun movement, solar altitude, and shadow lengths for any location on Earth at any time.",
                    urlTemplate = "https://www.suncalc.org/",
                    pricingType = "FREE",
                    verificationMethod = "Astronomical Solar Vector Mathematics",
                    trustedIndicators = listOf("Shadow length & azimuth exactly align with recorded EXIF timestamp"),
                    unprovenIndicators = listOf("Ambiguous indoor or overcast lighting approximations")
                )
            )
        ),
        OsintCategoryGroup(
            categoryName = "🏢 Corporate & Financial Registries",
            iconName = "business",
            description = "Company registries, beneficial ownership, statutory officers, and corporate filings.",
            tools = listOf(
                OsintResource(
                    id = "opencorporates",
                    name = "OpenCorporates Global Index",
                    category = "Corporate",
                    description = "The largest open database of companies in the world, covering over 200 million legal entities.",
                    urlTemplate = "https://opencorporates.com/companies?q={}",
                    pricingType = "FREE API",
                    verificationMethod = "Statutory Filing Extraction",
                    trustedIndicators = listOf("Subject listed as active director or authorized officer"),
                    unprovenIndicators = listOf("Holding company managed through nominee registry"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "sec_edgar",
                    name = "SEC EDGAR Filings",
                    category = "Corporate",
                    description = "U.S. Securities and Exchange Commission public filings, quarterly 10-K, and insider transactions.",
                    urlTemplate = "https://www.sec.gov/edgar/searchedgar/companysearch",
                    pricingType = "FREE",
                    verificationMethod = "Regulatory Compliance Documents",
                    trustedIndicators = listOf("Certified corporate disclosure signatures"),
                    unprovenIndicators = listOf("Vendor mentions in general risk statements")
                ),
                OsintResource(
                    id = "uk_companies_house",
                    name = "UK Companies House",
                    category = "Corporate",
                    description = "Official registrar of companies in the United Kingdom with officer birth months and filings.",
                    urlTemplate = "https://find-and-update.company-information.service.gov.uk/",
                    pricingType = "FREE API",
                    verificationMethod = "National Corporate Registry",
                    trustedIndicators = listOf("Active registered director with matching nationality & birth month"),
                    unprovenIndicators = listOf("Resigned officer with ambiguous name overlap")
                )
            )
        ),
        OsintCategoryGroup(
            categoryName = "🌐 Geolocation & Mapping",
            iconName = "map",
            description = "Crowdsourced satellite layers, open topography, IP routing, and cell tower datasets.",
            tools = listOf(
                OsintResource(
                    id = "wikimapia",
                    name = "Wikimapia Open Satellite",
                    category = "Geolocation",
                    description = "Crowdsourced geographical database describing marked buildings, facilities, and landmarks.",
                    urlTemplate = "http://wikimapia.org/",
                    pricingType = "FREE",
                    verificationMethod = "Crowdsourced Polygonal Annotation",
                    trustedIndicators = listOf("Labeled facility matches exact visual roof structure"),
                    unprovenIndicators = listOf("Outdated naming from prior decade")
                ),
                OsintResource(
                    id = "ipinfo",
                    name = "IPinfo ASN & Proxy Inspector",
                    category = "Geolocation",
                    description = "Autonomous System Number (ASN), ISP, and VPN/hosting detector for network IP endpoints.",
                    urlTemplate = "https://ipinfo.io/",
                    pricingType = "FREEMIUM",
                    verificationMethod = "BGP Routing & Reverse DNS",
                    trustedIndicators = listOf("Residential ISP allocation matching subject home territory"),
                    unprovenIndicators = listOf("Cloud data center exit node indicating active VPN routing")
                ),
                OsintResource(
                    id = "wigle",
                    name = "WiGLE Wireless Network Map",
                    category = "Geolocation",
                    description = "Worldwide crowdsourced database of wireless network BSSIDs and cell towers.",
                    urlTemplate = "https://www.wigle.net/",
                    pricingType = "FREEMIUM",
                    verificationMethod = "802.11 Beacon Trilateral Mapping",
                    trustedIndicators = listOf("Subject personal router BSSID geo-located to physical address"),
                    unprovenIndicators = listOf("Common default ISP router SSID (e.g., 'NETGEAR54')")
                ),
                OsintResource(
                    id = "opensky",
                    name = "OpenSky Aviation Telemetry",
                    category = "Geolocation",
                    description = "Community ADS-B flight tracking data platform for aerial search and rescue monitoring.",
                    urlTemplate = "https://opensky-network.org/",
                    pricingType = "FREE API",
                    verificationMethod = "Real-Time ADS-B Receiver Feeds",
                    trustedIndicators = listOf("Aircraft tail number transponder log over search zone"),
                    unprovenIndicators = listOf("Incomplete transponder coverage over mountainous terrain")
                )
            )
        ),
        OsintCategoryGroup(
            categoryName = "📚 Public Archives & Document Registries",
            iconName = "archive",
            description = "Historical web snapshots, legal repositories, and unindexed public agency caches.",
            tools = listOf(
                OsintResource(
                    id = "wayback",
                    name = "Wayback Machine (Internet Archive)",
                    category = "Archives",
                    description = "Access cached versions of deleted websites, past biographies, and historical blog posts.",
                    urlTemplate = "https://web.archive.org/web/*/{}",
                    pricingType = "FREE",
                    verificationMethod = "Cryptographically Timestamped WARC Snapshots",
                    trustedIndicators = listOf("Historical profile text deleted prior to investigation"),
                    unprovenIndicators = listOf("Missing embedded media or partial CSS styling"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "archive_today",
                    name = "Archive.today Cacher",
                    category = "Archives",
                    description = "Permanent snapshot recorder that circumvents dynamic client-side paywalls and scripts.",
                    urlTemplate = "https://archive.is/{}",
                    pricingType = "FREE",
                    verificationMethod = "Static HTML & DOM Raster Dump",
                    trustedIndicators = listOf("Exact mirror of social media post before deletion"),
                    unprovenIndicators = listOf("Truncated comments section"),
                    isDynamicHandleSupported = true
                ),
                OsintResource(
                    id = "usaspending",
                    name = "USAspending Award Index",
                    category = "Archives",
                    description = "Official open data source of federal government awards, grants, and contracts.",
                    urlTemplate = "https://www.usaspending.gov/search/?hash={}",
                    pricingType = "FREE",
                    verificationMethod = "Federal Treasury Audit Logs",
                    trustedIndicators = listOf("Direct sub-grant award citing target business DUNS number"),
                    unprovenIndicators = listOf("Indirect consortium participant mentions"),
                    isDynamicHandleSupported = true
                )
            )
        )
    )
}
