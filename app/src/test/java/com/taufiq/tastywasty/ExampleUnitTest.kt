package com.taufiq.tastywasty

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class BasicLogicTest {

    @Test
    fun testIsFoodExpired() {
        val pastDate = System.currentTimeMillis() - 100000
        val futureDate = System.currentTimeMillis() + 100000

        assertTrue(isFoodExpired(pastDate))
        assertFalse(isFoodExpired(futureDate))
    }

    @Test
    fun testIsRecipeMatch() {
        val ingredients = listOf("egg", "milk")
        val inventory = listOf("egg", "milk", "flour")

        assertTrue(isRecipeMatch(ingredients, inventory))
        assertFalse(isRecipeMatch(listOf("egg", "butter"), inventory))
    }

    @Test
    fun testGetMissingIngredients() {
        val recipe = listOf("rice", "lentils", "salt")
        val inventory = listOf("rice", "salt")

        val missing = getMissingIngredients(recipe, inventory)
        assertEquals(listOf("lentils"), missing)
    }

    // Quantity Validity
    @Test
    fun testIsValidQuantity() {
        assertTrue(isValidQuantity("2kg"))
        assertFalse(isValidQuantity(""))
        assertFalse(isValidQuantity("   "))
    }

    // Filter out expired items
    @Test
    fun testFilterExpiredItems() {
        val now = System.currentTimeMillis()
        val items = listOf(
            mockFoodItem("Apple", now - 100000),  // expired
            mockFoodItem("Banana", now + 100000), // not expired
            mockFoodItem("Milk", now - 500000)    // expired
        )

        val expired = filterExpiredItems(items)
        assertEquals(2, expired.size)
        assertEquals("Apple", expired[0].name)
        assertEquals("Milk", expired[1].name)
    }

    // Simple helpers

    private fun isFoodExpired(expiryDate: Long): Boolean {
        return expiryDate < System.currentTimeMillis()
    }

    private fun isRecipeMatch(recipeIngredients: List<String>, availableItems: List<String>): Boolean {
        return recipeIngredients.all { it in availableItems }
    }

    private fun getMissingIngredients(recipeIngredients: List<String>, availableItems: List<String>): List<String> {
        return recipeIngredients.filterNot { it in availableItems }
    }

    private fun isValidQuantity(quantity: String): Boolean {
        return quantity.trim().isNotEmpty()
    }

    data class FoodItem(val name: String, val expiryDate: Long)

    private fun filterExpiredItems(items: List<FoodItem>): List<FoodItem> {
        val now = System.currentTimeMillis()
        return items.filter { it.expiryDate < now }
    }

    private fun mockFoodItem(name: String, expiryDate: Long): FoodItem {
        return FoodItem(name, expiryDate)
    }
}
