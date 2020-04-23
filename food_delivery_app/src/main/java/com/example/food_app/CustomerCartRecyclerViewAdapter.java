package com.example.food_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class CustomerCartRecyclerViewAdapter extends RecyclerView.Adapter<CustomerCartRecyclerViewAdapter.ViewHolder>{

    public ArrayList<String> mItemNames, mItemSizes,mItemPrices;
    Context mContext;

    public CustomerCartRecyclerViewAdapter(Context mContext, ArrayList<String> mItemNames, ArrayList<String> mItemSizes, ArrayList<String> mItemPrices)  {
        this.mItemNames = mItemNames;
        this.mItemSizes = mItemSizes;
        this.mItemPrices = mItemPrices;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_cart_recycler_view_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.customer_cart_itemName_textView.setText(mItemNames.get(position));
        holder.customer_cart_itemSize_textView.setText(mItemSizes.get(position));
        holder.customer_cart_itemPrice_textView.setText(mItemPrices.get(position));
        holder.customer_cart_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMainActivity.cart.remove(position);
                CustomerCartActivity.final_cart.remove(position);
                mItemNames.remove(position);
                mItemSizes.remove(position);
                mItemPrices.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView customer_cart_itemName_textView, customer_cart_itemSize_textView, customer_cart_itemPrice_textView;
        Button customer_cart_remove_btn;
        CardView customer_cart_recyclerView_card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_cart_itemName_textView = itemView.findViewById(R.id.customer_cart_itemName_textView);
            customer_cart_itemSize_textView = itemView.findViewById(R.id.customer_cart_size_textView);
            customer_cart_itemPrice_textView = itemView.findViewById(R.id.customer_cart_price_textView);
            customer_cart_remove_btn = itemView.findViewById(R.id.customer_cart_remove_btn);
            customer_cart_recyclerView_card_view = itemView.findViewById(R.id.customer_cart_recyclerView_card_view);

        }
    }
}

