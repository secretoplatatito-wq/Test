package com.example

import com.example.data.model.DeepSearchEngine
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    val points = DeepSearchEngine.generateAllVectors(
      name = "Julian Vance",
      handle = "octoberian1012",
      email = "julian@vancetech-labs.io",
      phone = "303-555-0192",
      location = "Denver",
      domain = "vancetech-labs.io",
      keywords = "firmware"
    )

    assertFalse(points.isEmpty())
    assertTrue(points.any { it.id == "crisis_alert_sweep" })
    assertTrue(points.any { it.id == "social_mega_sweep" })
    assertTrue(points.any { it.id == "doc_excavation" })
    assertTrue(points.any { it.id == "git_credentials_sweep" })
    assertTrue(points.any { it.id == "subdomain_enum" })
    assertTrue(points.any { it.id == "court_legal_sweep" })

    // Verify Google URL formatting
    val crisisPoint = points.first { it.id == "crisis_alert_sweep" }
    val googleUrl = crisisPoint.getLaunchUrl("Google")
    assertTrue(googleUrl.startsWith("https://www.google.com/search?q="))
  }
}
