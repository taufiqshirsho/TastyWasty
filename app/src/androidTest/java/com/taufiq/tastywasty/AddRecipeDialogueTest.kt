package com.taufiq.tastywasty

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.taufiq.tastywasty.ui.screens.recipe.AddRecipeDialog
import org.junit.Rule
import org.junit.Test

class AddRecipeDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule() // ✅ Not tied to MainActivity

    @Test
    fun addRecipeDialog_displaysTitle() {
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    AddRecipeDialog(
                        onDismiss = {},
                        onAdd = {},
                        existing = null
                    )
                }
            }
        }

        // ✅ Checks dialog title
        composeTestRule.onNodeWithText("Add New Recipe", substring = true)
            .assertIsDisplayed()
    }
}
