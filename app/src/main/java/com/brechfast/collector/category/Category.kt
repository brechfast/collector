package com.brechfast.collector.category

import com.brechfast.collector.item.Item
import com.brechfast.collector.util.RandomColorUtil
import java.util.*

data class Category(
    var title: String,
    var description: String,
    var backgroundId: Int = RandomColorUtil.getRandomColor(),
    var titleBackgroundId: Int = RandomColorUtil.getDarkColor(backgroundId)) {

    private val id: String = UUID.randomUUID().toString()
    private var items = HashMap<String, Item>()

    fun getId(): String = id

    fun getItems(): HashMap<String, Item> = items

    fun update(title: String, description: String) {
        this.title = title
        this.description = description
    }

    fun createOrUpdateItem(item: Item) {
        items[item.getId()] = item
    }
}