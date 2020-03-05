package com.brechfast.collector.category

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.brechfast.collector.R

class CategoryDialog(private val ctx: Context, private val positiveButtonCallback: DialogInterface.OnClickListener,
                     private val view: View) : AlertDialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = Builder(ctx)
            .setTitle(R.string.create_new_category)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, positiveButtonCallback)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setView(view)

        // Show the keyboard when the dialog opens
        val window = builder.show().window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}