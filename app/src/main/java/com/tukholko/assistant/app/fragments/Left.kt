package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tukholko.assistant.R

class Left : Fragment() {
    var text: TextView? = null
    var button: Button? = null
    var counter: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_left, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view?.findViewById(R.id.ddd)
        button = view?.findViewById(R.id.button)
        button?.setOnClickListener {
            counter++
            Log.e("GGGG", counter.toString())
            text?.text = counter.toString()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        fun newInstance(): Left = Left()
    }
}