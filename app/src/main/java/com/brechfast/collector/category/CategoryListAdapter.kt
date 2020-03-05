package com.brechfast.collector.category

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.brechfast.collector.util.CollectorHelper
import com.brechfast.collector.item.ItemListActivity
import com.brechfast.collector.R

class CategoryListAdapter(private val context: Context, private var categories: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
         CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false))

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.title.text = category.title
        holder.title.setBackgroundColor(context.resources.getColor(category.titleBackgroundId, null))

        if (category.description.isNotEmpty()) {
            holder.description.text = category.description
        } else {
            holder.description.setText(R.string.no_description)
            holder.description.setTypeface(null, Typeface.ITALIC)
        }

        holder.background.setBackgroundColor(context.resources.getColor(category.backgroundId, null))
        holder.background.setOnClickListener {
            // Clicking on a Category will bring you to its list of Items
            val intent = Intent(context, ItemListActivity::class.java)
            intent.putExtra(CollectorHelper.CATEGORY_ID_KEY, category.getId())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }

    fun updateCategories(categories: ArrayList<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)

        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.categoryTitle)
        var description: TextView = itemView.findViewById(R.id.categoryDescription)
        var background: ConstraintLayout = itemView.findViewById(R.id.categoryBackground)
    }
}