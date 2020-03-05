package com.brechfast.collector.item

import android.graphics.drawable.Drawable
import java.util.*

class Item(var name: String,
           var description: String,
           var rating: Float,
           var photo: Drawable?,
           val categoryId: String) {

    private val id: String = UUID.randomUUID().toString()

    fun getId(): String = id
}