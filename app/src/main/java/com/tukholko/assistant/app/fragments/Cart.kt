package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tukholko.assistant.R
import com.tukholko.assistant.app.RecyclerAdapter
import java.math.BigDecimal


@Suppress("UNREACHABLE_CODE")
class Cart : Fragment() {
    var text: TextView? = null
    var button: Button? = null
    var counter: Int = 0

    var recyclerView : RecyclerView? = null
    var emptyCartView : LinearLayout? = null
    var adapter : RecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyCartView = view.findViewById(R.id.cart_empty)
        var totalPriceView: TextView = view.findViewById(R.id.total_price)
        totalPriceView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().equals("0")) {
                    recyclerView?.visibility = View.GONE
                    emptyCartView?.visibility = View.VISIBLE
                } else {
                    recyclerView?.visibility = View.VISIBLE
                    emptyCartView?.visibility = View.GONE
                }

            }
        })
        //Recycler
        recyclerView = view.findViewById(R.id.recycler_cart)
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        adapter = RecyclerAdapter(this.context)
        recyclerView?.adapter = adapter

        adapter!!.setTotalPriceView(totalPriceView)
        view.findViewById<Button>(R.id.payment_button).setOnClickListener {
            payment(totalPriceView)
        }
    }

    private fun payment(price: View) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        if (adapter?.itemCount == 0) {
            recyclerView?.visibility = View.GONE
            emptyCartView?.visibility = View.VISIBLE
        } else {
            recyclerView?.visibility = View.VISIBLE
            emptyCartView?.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): Cart = Cart()
    }
}
