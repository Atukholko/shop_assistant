package com.tukholko.assistant.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tukholko.assistant.R;
import com.tukholko.assistant.model.Product;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.widget.Toast.LENGTH_SHORT;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;

    private static class CartProduct {
        public Product product;
        public Integer count;

        public CartProduct(Product product, Integer count) {
            this.product = product;
            this.count = count;
        }
    }

    static ArrayList<CartProduct> products = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public static void addProduct(Product product) {
        products.add(new CartProduct(product, 3));
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
                    products.get(productIndex).count++;
                    notifyItemChanged(productIndex);
                }
            });

            itemView.findViewById(R.id.decrease_product_quantity_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
