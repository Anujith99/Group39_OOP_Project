package com.example.food_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerMainRecyclerViewAdapter extends RecyclerView.Adapter<CustomerMainRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "CustomerMainRecyclerVie";
    public ArrayList<String> mItemNames;
    public ArrayList<String> mPrices;
    Context context;
    String data;

    public CustomerMainRecyclerViewAdapter(Context context, ArrayList<String> mItemNames, ArrayList<String> mPrices) {
        this.mItemNames = mItemNames;
        this.mPrices = mPrices;
        this.context = context;

    }
    public String getSpinData() {
        String temp = data;
        data = "Small";
        return temp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_main_recycler_view_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.d(TAG, "binder method");
        holder.customer_main_item_name_textView.setText(mItemNames.get(position));
        holder.customer_main_price_textView.setText(mPrices.get(position));
        final String name = mItemNames.get(position);
        final String[] price = {mPrices.get(position)};
        holder.customer_main_size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                holder.spin_data.setSelected(i);
                data = holder.spin_data.getText();
                switch (i){
                    case 0:
                        holder.customer_main_price_textView.setText(price[0]);
                        break;

                    case 1:
                        String m = Double.toString(1.25*Double.parseDouble(price[0]));
                        price[0] = m;
                        holder.customer_main_price_textView.setText(m);
                        break;

                    case 2:
                        String l = Double.toString(1.5*Double.parseDouble(price[0]));
                        price[0] = l;
                        holder.customer_main_price_textView.setText(l);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ignore
            }
        });

        holder.customer_main_add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_name = name;
                String item_size = getSpinData();
                String item_price = price[0];
                CustomerMainActivity.cart.add(new String[]{item_name, item_size, item_price});
                Toast.makeText(context, item_size+" "+item_name+" added to cart", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView customer_main_item_name_textView,customer_main_price_textView;
        Spinner customer_main_size_spinner;
        Button customer_main_add_to_cart_btn;
        CardView customer_main_recyclerView_card_view;
        SizeAdapter spin_data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_main_item_name_textView = itemView.findViewById(R.id.customer_item_name_textView);
            customer_main_price_textView = itemView.findViewById(R.id.customer_price_textView);
            customer_main_size_spinner = itemView.findViewById(R.id.customer_size_spinner);
            customer_main_add_to_cart_btn = itemView.findViewById(R.id.customer_addToCart_btn);
            customer_main_recyclerView_card_view = itemView.findViewById(R.id.customer_main_recyclerView_relativeLayout);
            spin_data =new SizeAdapter(context);
            customer_main_size_spinner.setAdapter(spin_data.getAdapter());

        }
    }
}
