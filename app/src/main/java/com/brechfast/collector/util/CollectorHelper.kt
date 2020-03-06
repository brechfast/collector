package com.brechfast.collector.util

import androidx.annotation.VisibleForTesting
import com.brechfast.collector.category.Category
import com.brechfast.collector.item.Item
import kotlin.collections.HashMap

/**
 * This Helper class is being used in place of a database to store, access, and update all of the Categories
 * and their Items.
 */
class CollectorHelper {

    companion object {
        const val CATEGORY_ID_KEY = "category_id_key"
        const val ITEM_ID_KEY = "item_id_key"

        private var categoryMap: HashMap<String, Category> = HashMap()

        fun getAllCategories(): ArrayList<Category> {
            val list = ArrayList<Category>()
            list.addAll(categoryMap.values.sortedBy { it.title.toLowerCase() })
            return list
        }

        fun addCategory(categoryId: String, category: Category) {
            categoryMap[categoryId] = category
        }

        fun updateCategory(categoryId: String, title: String, description: String) {
            categoryMap[categoryId]?.apply {
                this.title = title
                this.description = description
            }
        }

        fun deleteCategory(categoryId: String) {
            if (categoryMap.containsKey(categoryId)) {
                categoryMap.remove(categoryId)
            }
        }

        fun getCategory(categoryId: String): Category = categoryMap.getValue(categoryId)

        fun containsCategoryId(categoryId: String): Boolean = categoryMap.containsKey(categoryId)

        fun getItem(categoryId: String, itemId: String): Item =
            getCategory(categoryId).getItems().getValue(itemId)

        fun getAllItemsInCategory(categoryId: String): ArrayList<Item> {
            val list = ArrayList<Item>()
            val category = getCategory(categoryId)
            list.addAll(category.getItems().values.sortedBy { it.name.toLowerCase() })
            return list
        }

        fun createOrUpdate(categoryId: String, item: Item) {
            val category = getCategory(categoryId)
            category.createOrUpdateItem(item)

            categoryMap[categoryId] = category
        }

        fun deleteItem(categoryId: String, itemId: String) {
            getCategory(categoryId).getItems().remove(itemId)
        }

        @VisibleForTesting
        fun setMap(map: HashMap<String, Category>) {
            categoryMap.clear()
            categoryMap = map
        }
    }
}