package com.tukholko.assistant.app.fragments.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity
import com.tukholko.assistant.model.Product
import org.w3c.dom.Text

class NewProductAlertDialog(var product: Product): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_background);
        return inflater.inflate(R.layout.dialog_new_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var prodImage: ImageView = view.findViewById(R.id.product_img)
        var prodName: TextView = view.findViewById(R.id.dialog_new_product_name)
        var prodManufacturer: TextView = view.findViewById(R.id.dialog_new_product_manufacturer)
        var prodPrice: TextView = view.findViewById(R.id.dialog_new_product_price)

        prodName.text = product.name
        prodManufacturer.text = product.manufacturer
        prodPrice.text = product.price.toString()

        if(product.image != null) {
            Picasso.get().load(product.image).error(R.drawable.ic_shop).into(prodImage)
        }

        view.findViewById<Button>(R.id.dialog_new_product_yes_button).setOnClickListener {
            var activity = activity as AppActivity
            activity.addProductToCart(product)
            this.dismiss()
        }
        view.findViewById<Button>(R.id.dialog_new_product_no_button).setOnClickListener {
            this.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.60).toInt()
        dialog!!.window?.setLayout(width, height)
    }

}