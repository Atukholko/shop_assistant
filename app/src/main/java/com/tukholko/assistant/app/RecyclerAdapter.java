package com.tukholko.assistant.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tukholko.assistant.R;
import com.tukholko.assistant.model.Product;

import java.util.LinkedList;

import static android.widget.Toast.LENGTH_SHORT;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;

    LinkedList<String> titles = new LinkedList<String>();
    static LinkedList<Product> products = new LinkedList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
        addProduct(new Product("Moloko+", 22.5, "Belarus", "a", 28.5));
        addProduct(new Product("Zaloopa", 211.2, "Italy", "a", 7.33));
        addProduct(new Product("Big name aboba amogus", 20010.2, "Russian Federation", "a", 85.100));
    }

    public static void addProduct(Product product) {
        products.add(product);
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
        Product product = products.get(position);
        holder.productTitle.setText(product.getName());
        holder.productWeight.setText(product.getWeight() + "g");
        holder.productManufacturer.setText(product.getManufacturer());
        holder.productPrice.setText(product.getPrice() + " BYN");
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_name);
            productWeight = itemView.findViewById(R.id.product_weight);
            productManufacturer = itemView.findViewById(R.id.product_manufacturer);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
