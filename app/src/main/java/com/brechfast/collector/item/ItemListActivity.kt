package com.brechfast.collector.item

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.brechfast.collector.CollectorViewModel
import com.brechfast.collector.R
import com.brechfast.collector.category.Category
import com.brechfast.collector.category.CategoryActivity
import com.brechfast.collector.MenuListener
import com.brechfast.collector.util.CollectorHelper
import kotlinx.android.synthetic.main.activity_items.*

class ItemListActivity : AppCompatActivity(), MenuListener {

    private lateinit var category: Category
    private lateinit var itemListAdapter: ItemListAdapter
    private lateinit var items: ArrayList<Item>

    private var viewModel = CollectorViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val categoryId: String = intent.extras[CollectorHelper.CATEGORY_ID_KEY] as String
        if (CollectorHelper.containsCategoryId(categoryId)) {
            items = CollectorHelper.getAllItemsInCategory(categoryId)
            category = CollectorHelper.getCategory(categoryId)
            supportActionBar?.title = category.title
            setupListAdapter()
            setupViewModel()
            setupAddButton()
        } else {
            launchCategoryActivity()
        }
    }

    private fun setupListAdapter() {
        itemListAdapter = ItemListAdapter(this, CollectorHelper.getAllItemsInCategory(category.getId()), this)
        itemList.apply {
            layoutManager = LinearLayoutManager(this@ItemListActivity)
            adapter = itemListAdapter
        }
    }

    private fun setupViewModel() {
        ViewModelProvider(ViewModelStore(), ViewModelProvider.NewInstanceFactory()).get(CollectorViewModel::class.java)
        viewModel.observableCategoryItems.observe(this, Observer {
            itemListAdapter.updateItems(it as ArrayList<Item>)
        })
    }

    private fun setupAddButton() {
        addItem.setOnClickListener {
            addItem()
        }
    }

    private fun launchCategoryActivity() {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    private fun addItem() {
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(CollectorHelper.CATEGORY_ID_KEY, category.getId())
        startActivity(intent)
    }

    override fun edit(id: String) {
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(CollectorHelper.CATEGORY_ID_KEY, category.getId())
        intent.putExtra(CollectorHelper.ITEM_ID_KEY, id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    override fun delete(id: String) {
        viewModel.deleteItem(category.getId(), id)
    }
}