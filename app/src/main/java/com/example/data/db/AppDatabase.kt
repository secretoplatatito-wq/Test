package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CaseEntity
import com.example.data.model.IntelNodeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CaseEntity::class, IntelNodeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun caseDao(): CaseDao
    abstract fun intelNodeDao(): IntelNodeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "super_osint_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.caseDao(), database.intelNodeDao())
                    }
                }
            }

            private suspend fun populateInitialData(caseDao: CaseDao, intelNodeDao: IntelNodeDao) {
                val defaultCaseId = caseDao.insertCase(
                    CaseEntity(
                        title = "Operation North Star (Missing Subject Search)",
                        targetName = "Julian Vance",
                        targetHandle = "octoberian1012",
                        targetDomain = "vancetech-labs.io",
                        locationOfInterest = "Denver, Colorado & Cascade Range",
                        caseCategory = "Missing Persons / Crisis Intel",
                        description = "Subject disappeared following backcountry transit route. Active multi-platform digital footprint correlation in progress."
                    )
                )

                // Proven Node (Verified by 2+ independent sources)
                intelNodeDao.insertNode(
                    IntelNodeEntity(
                        caseId = defaultCaseId,
                        title = "Verified GitHub & LinkedIn Handle Match",
                        category = "Social Media",
                        rawData = "Handle: octoberian1012\nCommit PGP Key: 0x9AF4B12D\nProfile URL: https://github.com/octoberian1012",
                        verificationStatus = "PROVEN",
                        sourceCount = 2,
                        confidenceLevel = "VERIFIED",
                        sourceUrls = "https://github.com/octoberian1012, https://linkedin.com/in/julian-vance",
                        notes = "Both profiles cite Denver-based firmware development and share identical GPG signing keys.",
                        pivotClues = "Commit timestamp cluster: 04:00 - 07:00 UTC (matches US Mountain Time evening activity)."
                    )
                )

                // Proven Node (Public record property record)
                intelNodeDao.insertNode(
                    IntelNodeEntity(
                        caseId = defaultCaseId,
                        title = "Colorado Municipal Property Registry Match",
                        category = "Public Records",
                        rawData = "Parcel ID: 04-992-182\nRegistered Owner: Julian M. Vance\nTax Assessment Year: 2025",
                        verificationStatus = "PROVEN",
                        sourceCount = 2,
                        confidenceLevel = "VERIFIED",
                        sourceUrls = "https://www.whitepages.com/name/Julian-Vance, County Assessor Portal",
                        notes = "Matched with secondary utility connection record.",
                        pivotClues = "Vehicle registration linked: Dark green Subaru Outback."
                    )
                )

                // Unproven Lead Node (Kept active in sandbox for context & dork generation)
                intelNodeDao.insertNode(
                    IntelNodeEntity(
                        caseId = defaultCaseId,
                        title = "Unconfirmed Spotify & TikTok Activity Spike",
                        category = "Social Media",
                        rawData = "Spotify User: user_october_v\nPlaylist 'Rocky Mountain Alpine Ascent' modified 48h before disappearance.",
                        verificationStatus = "UNPROVEN",
                        sourceCount = 1,
                        confidenceLevel = "MEDIUM",
                        sourceUrls = "https://open.spotify.com/user/user_october_v",
                        notes = "Loose pseudonym match. No public email or photo yet confirms identical subject.",
                        pivotClues = "Generate targeted dork: site:spotify.com 'Rocky Mountain Alpine' AND 'Julian' to find collaborators."
                    )
                )

                // Unproven Lead Node (EXIF camera serial candidate)
                intelNodeDao.insertNode(
                    IntelNodeEntity(
                        caseId = defaultCaseId,
                        title = "Trailhead Parking Photo with Partial Exif",
                        category = "EXIF / Imagery",
                        rawData = "Camera: Sony ILCE-7RM4\nTimestamp: 2026-10-12 14:22:10\nGPS: Coordinates stripped by web upload.",
                        verificationStatus = "UNPROVEN",
                        sourceCount = 1,
                        confidenceLevel = "LOW",
                        sourceUrls = "Forum trail report post #4491",
                        notes = "Shadow angle indicates roughly 38 degrees solar elevation, aligning with 2 PM Mountain Daylight Time.",
                        pivotClues = "Run SunCalc solar shadow alignment for Trailhead 40.0150° N, 105.2705° W."
                    )
                )
            }
        }
    }
}
