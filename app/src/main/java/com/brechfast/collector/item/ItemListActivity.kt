package com.brechfast.collector.item

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.brechfast.collector.CollectorViewModel
import com.brechfast.collector.R
import com.brechfast.collector.category.Category
import com.brechfast.collector.category.CategoryActivity
import com.brechfast.collector.util.CollectorHelper
import com.brechfast.collector.category.CategoryDialog
import kotlinx.android.synthetic.main.activity_items.*

class ItemListActivity : AppCompatActivity() {

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
            setupListAdapter()
            setupViewModel()
            setupViewVisibility()
        } else {
            launchCategoryActivity()
        }
    }

    private fun setupListAdapter() {
        itemListAdapter = ItemListAdapter(this, CollectorHelper.getAllItemsInCategory(category.getId()))
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

    private fun setupViewVisibility() {
        if (itemListAdapter.itemCount == 0) {
            longClickText.setText(R.string.add_item_in_menu)
        } else {
            longClickText.setText(R.string.long_click_to_edit)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.items_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> launchCategoryActivity()
            R.id.addItem -> addItem()
            R.id.editCategory -> editCategory()
            R.id.deleteCategory -> deleteCategory()
        }
        return true
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

    private fun editCategory() {
        val dialogView = layoutInflater.inflate(R.layout.category_layout, null)
        val title: EditText = dialogView.findViewById(R.id.createCategoryTitle)
        val description: EditText = dialogView.findViewById(R.id.createCategoryDescription)

        title.append(category.title)
        description.append(category.description)

        CategoryDialog(this,
            DialogInterface.OnClickListener {_, _ ->
                viewModel.updateCategory(category.getId(), title.text.toString(), description.text.toString())
            }, dialogView).create()
    }

    private fun deleteCategory() {
        viewModel.deleteCategory(category.getId())
        launchCategoryActivity()
    }
}