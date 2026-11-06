package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("My English Studio", appName)
  }

  @Test
  fun `verify index html contains realistic 3D character engine`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val htmlContent = context.assets.open("index.html").bufferedReader().use { it.readText() }

    // Stage 1: Verify Realistic 3D character and environment functions exist
    assert(htmlContent.contains("function drawPixar3DCharacter")) { "Missing drawPixar3DCharacter function" }
    assert(htmlContent.contains("function draw3DEnvironment")) { "Missing draw3DEnvironment function" }

    // Stage 2: Verify legacy 2D Peppa cartoon functions are completely removed
    assert(!htmlContent.contains("drawPeppaPigEnvironment")) { "Legacy drawPeppaPigEnvironment still present" }
    assert(!htmlContent.contains("drawPeppaPigCharacter")) { "Legacy drawPeppaPigCharacter still present" }

    // Stage 3: Verify Three.js WebGL Engine Integration
    assert(htmlContent.contains("three.min.js")) { "Missing Three.js CDN script import" }
    assert(htmlContent.contains("class ThreeStudioEngine")) { "Missing ThreeStudioEngine class" }
    assert(htmlContent.contains("globalThreeStudioEngine")) { "Missing globalThreeStudioEngine instance" }

    // Stage 4: Verify Master Upgrade Professional Features
    assert(htmlContent.contains("function exportSrtFile")) { "Missing exportSrtFile function" }
    assert(htmlContent.contains("function playSoundEffect")) { "Missing playSoundEffect function" }
    assert(htmlContent.contains("function exportProjectJson")) { "Missing exportProjectJson function" }
    assert(htmlContent.contains("function updateVoiceAccent")) { "Missing updateVoiceAccent function" }
    assert(htmlContent.contains("function updateVoiceSpeed")) { "Missing updateVoiceSpeed function" }

    // Stage 5: Verify left navigation and producer credit
    assert(htmlContent.contains("id=\"left-dashboard\"")) { "Left dashboard navigation missing" }
    assert(htmlContent.contains("Mr. Zaafouri Abdelmalek")) { "Producer attribution credit missing" }
  }
}
