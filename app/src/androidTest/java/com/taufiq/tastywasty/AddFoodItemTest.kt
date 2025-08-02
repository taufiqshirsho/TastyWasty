package com.taufiq.tastywasty

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.taufiq.tastywasty.ui.screens.inventory.AddFoodDialog
import org.junit.Rule
import org.junit.Test

class AddFoodDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addFoodDialog_displaysTitle() {
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    AddFoodDialog(
                        onDismiss = {},
                        onAdd = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Add Food Item", substring = true)
            .assertIsDisplayed()
    }
}
