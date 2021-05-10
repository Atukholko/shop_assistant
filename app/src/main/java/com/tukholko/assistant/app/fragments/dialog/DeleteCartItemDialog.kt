package com.tukholko.assistant.app.fragments.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity

class DeleteCartItemDialog: DialogFragment() {
    var answer: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_background);
        return inflater.inflate(R.layout.dialog_delete_cart_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.dialog_remove_cart_item_yes_button).setOnClickListener {
            answer = true
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, activity?.intent);
            this.dismiss()
        }
        view.findViewById<Button>(R.id.dialog_remove_cart_item_no_button).setOnClickListener {
            answer = false
            this.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, height)
    }

}