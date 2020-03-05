package com.brechfast.collector

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brechfast.collector.category.Category
import com.brechfast.collector.item.Item
import com.brechfast.collector.util.CollectorHelper

class CollectorViewModel : ViewModel() {

    var observableCategories = MutableLiveData<List<Category>>()
    var observableCategoryItems = MutableLiveData<List<Item>>()

    fun createCategory(title: String, description: String) {
        val category = Category(title, description)
        CollectorHelper.addCategory(category.getId(), category)

        refreshCategories()
    }

    fun updateCategory(categoryId: String, title: String, description: String) {
        CollectorHelper.updateCategory(categoryId, title, description)

        refreshCategories()
    }

    fun deleteCategory(id: String) {
        CollectorHelper.deleteCategory(id)

        refreshCategories()
    }

    private fun refreshCategories() {
        observableCategories.postValue(CollectorHelper.getAllCategories())
    }

    fun createOrUpdateItem(categoryId: String, itemId: String, name: String, description: String, rating: Float, photo: Drawable) {
        val category = CollectorHelper.getCategory(categoryId)

        if (itemId.isEmpty()) {
            CollectorHelper.createOrUpdate(categoryId, Item(name, description, rating, photo, categoryId))
        } else {
            val item = CollectorHelper.getItem(categoryId, itemId)
            item.name = name
            item.description = description
            item.rating = rating
            item.photo = photo
            CollectorHelper.createOrUpdate(category.getId(), item)
        }
        refreshCategoryItems(categoryId)
    }

    fun deleteItem(categoryId: String, itemId: String) {
        CollectorHelper.deleteItem(categoryId, itemId)
        refreshCategoryItems(categoryId)
    }

    private fun refreshCategoryItems(categoryId: String) {
        observableCategoryItems.postValue(CollectorHelper.getAllItemsInCategory(categoryId))
    }
}