package com.taufiq.tastywasty

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Test 1: Check package name
    @Test
    fun useAppContext() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("com.taufiq.tastywasty", appContext.packageName)
    }

    // Test 2: Check that the Sign Up link is visible
    @Test
    fun signUpLinkIsVisible() {
        composeTestRule.onNodeWithText("Don't have an account? Sign up").assertIsDisplayed()
    }
}
