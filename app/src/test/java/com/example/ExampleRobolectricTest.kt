package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.TechSpikeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TechSpike Solutions", appName)
  }

  @Test
  fun `splash screen click triggers completion`() {
    var completed = false
    composeTestRule.setContent {
      TechSpikeTheme {
        SplashScreen(
          onSplashComplete = { completed = true }
        )
      }
    }
    composeTestRule.onNodeWithTag("splash_continue_button").performClick()
    assertTrue(completed)
  }
}
