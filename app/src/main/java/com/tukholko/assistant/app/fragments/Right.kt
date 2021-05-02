package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tukholko.assistant.R

class Right : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rigth, container, false)
    }

    companion object {
        fun newInstance(): Right = Right()
    }
}
