package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariel.Storage.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RecyclerViewManagerProduct extends RecyclerView.Adapter<RecyclerViewManagerProduct.ViewHolder> {
    private ArrayList<Product> mProducts;
    private Set<Product> mProductsIsFull;
    private Context context;

    public RecyclerViewManagerProduct(Context context, ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
        this.mProductsIsFull = new HashSet<>(mProducts);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewManagerProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_products, parent, false);
        return new RecyclerViewManagerProduct.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewManagerProduct.ViewHolder holder, int position) {
        holder.name.setText(mProducts.get(position).getName());
        holder.id.setText( mProducts.get(position).getProduct_id());
        holder.desc.setText(mProducts.get(position).getDescription());
        holder.textViewNothing.setVisibility(View.GONE);
        if (getItemCount() == 0) holder.textViewNothing.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void filterProductName (String charText) {
        mProductsIsFull.addAll(mProducts);
        mProducts.clear();
        if(charText == null || charText.length() == 0){
            mProducts.addAll(mProductsIsFull);
        }else{
            String filterPattern = charText.toLowerCase().trim();
            for(Product product : mProductsIsFull){
                if(product.getName().toLowerCase().contains(filterPattern)){
                    mProducts.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView id;
        private TextView desc;
        private CardView cardView;
        private LinearLayout linearLayout;
        private TextView textViewNothing;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_product_manager_name_item);
            id = (TextView) itemView.findViewById(R.id.recycler_product_manager_id_item);
            desc = (TextView) itemView.findViewById(R.id.recycler_product_manager_desc_item);
            cardView = (CardView) itemView.findViewById(R.id.recycler_product_manager_parent_card_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_product_manager_child_linear_layout);
            textViewNothing = (TextView) itemView.findViewById(R.id.no_item_to_show_product);
        }
    }
}

