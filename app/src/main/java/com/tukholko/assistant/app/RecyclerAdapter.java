package com.tukholko.assistant.app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tukholko.assistant.R;
import com.tukholko.assistant.app.fragments.dialog.DeleteCartItemAlertDialog;
import com.tukholko.assistant.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    TextView totalPriceView;
    Double totalPrice = 0.0;
    ArrayList<CartProduct> products = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setTotalPriceView(TextView textView) {
        totalPriceView = textView;
    }

    public void addProduct(Product product) {
        boolean productFound = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).product.getName().equals(product.getName())) {
                productFound = true;
                products.get(i).count++;
                notifyItemChanged(i);
            }
        }
        if (!productFound) {
            products.add(new CartProduct(product, 1));
        }
        totalPrice += product.getPrice();
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        totalPriceView.setText(new DecimalFormat("#.##").format(Math.abs(totalPrice)));
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

        if(product.getImage() != null) {
            Picasso.get().load(product.getImage()).error(R.drawable.ic_shop).into(holder.productImage);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private static class CartProduct {
        public Product product;
        public Integer count;

        public CartProduct(Product product, Integer count) {
            this.product = product;
            this.count = count;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle;
        TextView productWeight;
        TextView productManufacturer;
        TextView productPrice;
        TextView productQuantity;
        RoundedImageView productImage;
        Integer productIndex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_name);
            productWeight = itemView.findViewById(R.id.product_weight);
            productManufacturer = itemView.findViewById(R.id.product_manufacturer);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productImage = itemView.findViewById(R.id.product_img);

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
                    if (products.get(productIndex).count > 1) {
                        totalPrice -= products.get(productIndex).product.getPrice();
                        updateTotalPrice();
                        products.get(productIndex).count--;
                        notifyItemChanged(productIndex);
                        Log.e("GGGGGGGGGGGGGGGG", "ddd");
                    } else {
                        Log.e("ggg", products.get(productIndex).product.getName());
                        showDeleteDialog(context);
                    }
                }
            });
        }

        public void onPositive() {
            totalPrice -= products.get(productIndex).product.getPrice();
            updateTotalPrice();
            deleteItem(itemView, productIndex.intValue());
//            products.remove(productIndex.intValue());
//            notifyItemRemoved(productIndex);
//            notifyDataSetChanged();
        }

        private void deleteItem(View rowView, final int position) {

            Animation anim = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_out_right);
            anim.setDuration(300);
            rowView.startAnimation(anim);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    products.remove(position);
                    notifyItemRemoved(productIndex);
                    notifyDataSetChanged();
                }
            }, anim.getDuration());
        }

        private boolean getDialogAnswer(Context context) {
            return showDeleteDialog(context).getAnswer();
        }

        private DeleteCartItemAlertDialog showDeleteDialog(Context context) {
            AppActivity activity = (AppActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            DeleteCartItemAlertDialog deleteCartItemDialog = new DeleteCartItemAlertDialog();
            deleteCartItemDialog.setViewHolder(this);
            deleteCartItemDialog.show(fm, "fff");
            return deleteCartItemDialog;
        }
    }

    public void deleteAll() {
        products.clear();
        notifyDataSetChanged();
        totalPrice = (double) 0;
        updateTotalPrice();
    }
}
