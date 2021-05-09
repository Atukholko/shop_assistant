package com.tukholko.assistant.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tukholko.assistant.R;
import com.tukholko.assistant.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    TextView totalPriceView;
    Double totalPrice = 0.0;

    public void setTotalPriceView(TextView textView) {
        totalPriceView = textView;
    }

    private static class CartProduct {
        public Product product;
        public Integer count;

        public CartProduct(Product product, Integer count) {
            this.product = product;
            this.count = count;
        }
    }

    ArrayList<CartProduct> products = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void addProduct(Product product) {
        products.add(new CartProduct(product, 1));
        totalPrice += product.getPrice();
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        totalPriceView.setText(new DecimalFormat("#.0#").format(totalPrice).toString());
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_cart_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Product product = products.get(position).product;
        holder.productTitle.setText(product.getName());
        holder.productWeight.setText(product.getWeight() + "g");
        holder.productManufacturer.setText(product.getManufacturer());
        holder.productPrice.setText(product.getPrice() + " BYN");
        holder.productQuantity.setText(products.get(position).count.toString());
        holder.productIndex = position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle;
        TextView productWeight;
        TextView productManufacturer;
        TextView productPrice;
        TextView productQuantity;
        Integer productIndex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_name);
            productWeight = itemView.findViewById(R.id.product_weight);
            productManufacturer = itemView.findViewById(R.id.product_manufacturer);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);

            initializeListeners(itemView);
        }

        private void initializeListeners(@NonNull View itemView) {
            itemView.findViewById(R.id.increase_product_quantity_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalPrice += products.get(productIndex).product.getPrice();
                    updateTotalPrice();
                    products.get(productIndex).count++;
                    notifyItemChanged(productIndex);
                }
            });

            itemView.findViewById(R.id.decrease_product_quantity_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalPrice -= products.get(productIndex).product.getPrice();
                    updateTotalPrice();
                    if(products.get(productIndex).count > 1) {
                        products.get(productIndex).count--;
                        notifyItemChanged(productIndex);
                    } else {

                        products.remove(productIndex.intValue());
                        notifyItemRemoved(productIndex);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
