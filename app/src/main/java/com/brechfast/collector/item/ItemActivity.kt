package com.brechfast.collector.item

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.brechfast.collector.R
import com.brechfast.collector.util.CollectorHelper
import com.brechfast.collector.CollectorViewModel
import kotlinx.android.synthetic.main.new_item_layout.*

class ItemActivity : AppCompatActivity() {

    companion object {
        private const val GALLERY_REQUEST_CODE: Int = 101
    }

    private var categoryId: String = ""
    private var itemId: String = ""

    private var viewModel = CollectorViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_item_layout)

        categoryId = intent.extras[CollectorHelper.CATEGORY_ID_KEY] as String

        val itemIdExists = intent.extras[CollectorHelper.ITEM_ID_KEY] != null
        if (itemIdExists) {
            itemId = intent.extras[CollectorHelper.ITEM_ID_KEY] as String
        }

        if (itemId.isNotEmpty()) {
            setupExistingItemInfo()
        }

        ViewModelProvider(ViewModelStore(), ViewModelProvider.NewInstanceFactory()).get(CollectorViewModel::class.java)

        setupViewListeners()
    }

    private fun setupViewListeners() {
        saveItem.setOnClickListener {
            viewModel.createOrUpdateItem(categoryId, itemId, addItemName.text.toString(), addItemDescription.text.toString(),
                addItemRating.rating, addItemPhoto.drawable)
            launchItemListActivity()
        }

        cancelItem.setOnClickListener {
            launchItemListActivity()
        }

        addItemPhoto.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        addItemRating.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating < 1.0F) {
                ratingBar.rating = 1.0F
            }
        }
    }

    private fun launchItemListActivity() {
        val intent = Intent(this, ItemListActivity::class.java)
        intent.putExtra(CollectorHelper.CATEGORY_ID_KEY, categoryId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    private fun setupExistingItemInfo() {
        val item = CollectorHelper.getItem(categoryId, itemId)

        addItemName.append(item.name)
        addItemDescription.append(item.description)
        addItemRating.rating = item.rating
        addItemPhoto.setImageDrawable(item.photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    addItemPhoto.setImageDrawable(BitmapDrawable(resources, bitmap))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)

        // Don't show the delete icon during item creation
        if (itemId.isEmpty()) {
            menu?.findItem(R.id.deleteItem)?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.deleteItem -> {
                viewModel.deleteItem(categoryId, itemId)
                launchItemListActivity()
            }
        }
        return true
    }
}