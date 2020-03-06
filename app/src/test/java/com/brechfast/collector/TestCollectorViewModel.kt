package com.brechfast.collector

import android.graphics.drawable.Drawable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.brechfast.collector.category.Category
import com.brechfast.collector.item.Item
import com.brechfast.collector.util.CollectorHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class TestCollectorViewModel {

    @Rule @JvmField
    var rule = InstantTaskExecutorRule()

    private var categoryId = ""
    private var itemId = ""
    private var viewModel = CollectorViewModel()
    private var categoryMap = HashMap<String, Category>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        createCategoryMap()
        categoryId = CollectorHelper.getAllCategories()[0].getId()
        itemId = CollectorHelper.getAllItemsInCategory(categoryId)[0].getId()

        viewModel.observableCategories.postValue(CollectorHelper.getAllCategories())
        viewModel.observableCategoryItems.postValue(CollectorHelper.getAllItemsInCategory(categoryId))
    }

    @After
    fun cleanup() {
        categoryMap.clear()
        CollectorHelper.setMap(categoryMap)
    }

    private fun createCategoryMap() {
        val category = Category(
            "Books",
            "Like a library",
            R.color.materialGreen,
            R.color.materialGreenDark
        )

        val item = Item(
            "Harry Potter",
            "All 7 of them.",
            5f,
            mock(Drawable::class.java),
            categoryId
        )

        category.createOrUpdateItem(item)
        CollectorHelper.addCategory(category.getId(), category)
    }

    @Test
    fun testCreateCategory() {
        assertEquals(1, viewModel.observableCategories.value?.size)

        viewModel.createCategory("New Category", "New Description")

        assertEquals(2, viewModel.observableCategories.value?.size)
    }

    @Test
    fun testUpdateCategory() {
        assertEquals(1, viewModel.observableCategories.value?.size)

        val oldCategory = viewModel.observableCategories.value?.get(0)
        val oldId = oldCategory?.getId()
        val oldTitle = oldCategory?.title
        val oldDescription = oldCategory?.description

        viewModel.updateCategory(oldId!!, "Better Books", "Better library.")

        val newCategory = viewModel.observableCategories.value?.get(0)
        val newTitle = newCategory?.title
        val newDescription = newCategory?.description

        assertEquals(1, viewModel.observableCategories.value?.size)
        assertEquals(oldId, newCategory?.getId())
        assertNotEquals(oldTitle, newTitle)
        assertNotEquals(oldDescription, newDescription)
    }

    @Test
    fun testDeleteCategory() {
        assertEquals(1, viewModel.observableCategories.value?.size)

        viewModel.deleteCategory(categoryId)

        assertEquals(0, viewModel.observableCategories.value?.size)
    }

    @Test
    fun testCreateOrUpdateItem_create() {
        var category = viewModel.observableCategories.value?.get(0)!!
        assertEquals(1, category.getItems().size)

        viewModel.createOrUpdateItem(categoryId, "", "newName", "newDescription", 3.0f, mock(Drawable::class.java))

        category = viewModel.observableCategories.value?.get(0)!!
        assertEquals(2, category.getItems().size)
    }

    @Test
    fun testCreateOrUpdateItem_update() {
        assertEquals(1, viewModel.observableCategories.value?.size)
        val oldItem = viewModel.observableCategoryItems.value?.get(0)
        val oldName = oldItem?.name
        val oldDescription = oldItem?.description
        val oldRating = oldItem?.rating

        viewModel.createOrUpdateItem(categoryId, itemId, "newName", "newDescription", 1.0f, mock(Drawable::class.java))

        val newItem = viewModel.observableCategoryItems.value?.get(0)

        assertEquals(oldItem?.getId(), newItem?.getId())
        assertEquals(oldItem?.categoryId, newItem?.categoryId)
        assertNotEquals(oldName, newItem?.name)
        assertNotEquals(oldDescription, newItem?.description)
        assertNotEquals(oldRating, newItem?.rating)
        assertEquals(1, viewModel.observableCategories.value?.size)
    }

    @Test
    fun testDeleteItem() {
        assertEquals(1, viewModel.observableCategoryItems.value?.size)

        viewModel.deleteItem(categoryId, itemId)

        assertEquals(0, viewModel.observableCategoryItems.value?.size)
    }
}
