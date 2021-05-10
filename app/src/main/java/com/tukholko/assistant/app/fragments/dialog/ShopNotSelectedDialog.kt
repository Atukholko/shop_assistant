package com.tukholko.assistant.app.fragments.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity

class ShopNotSelectedDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_background);
        return inflater.inflate(R.layout.dialog_shop_not_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.dialog_shop_not_selected_yes_button).setOnClickListener {
            var activity = activity as AppActivity
            activity.findViewById<BottomNavigationItemView>(R.id.navigation_map).callOnClick()
            this.dismiss()
        }
        view.findViewById<Button>(R.id.dialog_shop_not_selected_no_button).setOnClickListener {
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