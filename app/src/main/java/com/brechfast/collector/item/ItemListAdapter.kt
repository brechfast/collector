package com.brechfast.collector.item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.brechfast.collector.util.CollectorHelper
import com.brechfast.collector.R

class ItemListAdapter(private val context: Context, private val items: ArrayList<Item>) :
        RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_item, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        holder.description.text = item.description
        holder.rating.text = item.rating.toInt().toString()

        if (item.photo == null) {
            // Default photo in case no photo is added
            item.photo = context.resources.getDrawable(R.drawable.ic_image_gray_24dp, null)
        }
        holder.photo.setImageDrawable(item.photo)

        holder.background.setBackgroundColor(context.resources.getColor(CollectorHelper.getCategory(item.categoryId).backgroundId, null))
        holder.background.setOnLongClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(CollectorHelper.CATEGORY_ID_KEY, item.categoryId)
            intent.putExtra(CollectorHelper.ITEM_ID_KEY, item.getId())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            (context as ItemListActivity).finish()
            context.startActivity(intent)
            true
        }
    }

    fun updateItems(items: ArrayList<Item>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.itemName)
        var description: TextView = itemView.findViewById(R.id.itemDescription)
        var photo: ImageView = itemView.findViewById(R.id.itemImage)
        var rating: TextView = itemView.findViewById(R.id.itemRating)
        var background: ConstraintLayout = itemView.findViewById(R.id.itemBackground)
    }
}