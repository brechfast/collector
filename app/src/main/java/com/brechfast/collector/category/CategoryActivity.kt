package com.brechfast.collector.category

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.brechfast.collector.util.CollectorHelper
import com.brechfast.collector.CollectorViewModel
import com.brechfast.collector.MenuListener
import com.brechfast.collector.R
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), MenuListener {

    private lateinit var categoryListAdapter: CategoryListAdapter

    private var viewModel = CollectorViewModel()
    private var categories = CollectorHelper.getAllCategories()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setupListAdapter()
        setupViewModel()

        addCategory.setOnClickListener {
            createOrUpdateCategory("")
        }
    }

    private fun setupListAdapter() {
        categoryListAdapter = CategoryListAdapter(this, categories, this)
        categoryList.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            adapter = categoryListAdapter
        }
    }

    private fun setupViewModel() {
        ViewModelProvider(ViewModelStore(), ViewModelProvider.NewInstanceFactory()).get(CollectorViewModel::class.java)
        viewModel.observableCategories.observe(this, Observer {
            categoryListAdapter.updateCategories(it as ArrayList<Category>)
        })
    }

    private fun createOrUpdateCategory(categoryId: String) {
        val dialogView = layoutInflater.inflate(R.layout.category_layout, null)
        val title: EditText = dialogView.findViewById(R.id.createCategoryTitle)
        val description: EditText = dialogView.findViewById(R.id.createCategoryDescription)

        val listener = if (categoryId.isEmpty()) {
            DialogInterface.OnClickListener { _, _ ->
                viewModel.createCategory(title.text.toString(), description.text.toString())
            }
        } else {
            val category = CollectorHelper.getCategory(categoryId)
            title.append(category.title)
            description.append(category.description)

            DialogInterface.OnClickListener { _, _ ->
                viewModel.updateCategory(category.getId(), title.text.toString(), description.text.toString())
            }
        }

        CategoryDialog(this, listener, dialogView).create()
    }

    override fun edit(id: String) {
        createOrUpdateCategory(id)
    }

    override fun delete(id: String) {
        viewModel.deleteCategory(id)
    }
}
