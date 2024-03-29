package com.tukholko.assistant.app.fragments.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity

class LogoutAlertDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_background);
        return inflater.inflate(R.layout.dialog_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.dialog_logout_yes_button).setOnClickListener {
            (activity as AppActivity?)!!.signOut()
        }
        view.findViewById<Button>(R.id.dialog_logout_no_button).setOnClickListener {
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