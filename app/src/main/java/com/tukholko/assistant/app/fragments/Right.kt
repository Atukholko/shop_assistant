package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity

class Right : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.map_reload_button).setOnClickListener {
            (activity as AppActivity?)!!.initializeMapWithShops()
        }

    }

    companion object {
        fun newInstance(): Right = Right()
    }
}
