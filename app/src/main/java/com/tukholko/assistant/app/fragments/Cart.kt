package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tukholko.assistant.R
import com.tukholko.assistant.app.RecyclerAdapter

class Cart : Fragment() {
    var text: TextView? = null
    var button: Button? = null
    var counter: Int = 0

    var recyclerView : RecyclerView? = null;
    var adapter : RecyclerAdapter? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_left, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler
        recyclerView = view.findViewById(R.id.recycler_cart)
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        adapter = RecyclerAdapter(this.context)
        recyclerView?.adapter = adapter
        // ---
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        fun newInstance(): Cart = Cart()
    }
}
