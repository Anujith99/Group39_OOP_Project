package com.example.food_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverOrdersActivity extends AppCompatActivity {

        private static final String TAG = "DriverOrders";
        private RecyclerView recyclerView;
        private DatabaseReference databaseReference;
        FirebaseRecyclerOptions<OrderDetails> options;
        FirebaseRecyclerAdapter<OrderDetails, ViewHolder> adapter;
        private Button accept;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_driver_orders);

            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");

            options = new FirebaseRecyclerOptions.Builder<OrderDetails>()
                    .setQuery(databaseReference, OrderDetails.class).build();

            adapter = new FirebaseRecyclerAdapter<OrderDetails, ViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final OrderDetails orderDetails) {
                    viewHolder.id.setText(orderDetails.getId());
                    viewHolder.home.setText(orderDetails.getHome());
                    viewHolder.phone.setText(orderDetails.getPhone());
                    viewHolder.phone.setVisibility(View.INVISIBLE);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            accept.setEnabled(false);
                            accept.setText("Waiting for Confirmation");
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderDetails.getId());
                            ref.child("status").setValue("accepted");

                            ref.child("status").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String status = dataSnapshot.getValue(String.class);
                                    if(status.equals("confirmed")){
                                        accept.setBackgroundColor(Color.WHITE);
                                        accept.setTextColor(Color.GREEN);
                                        accept.setText("Confirmed!");
                                        viewHolder.phone.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }

                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details, parent, false);

                    return new ViewHolder(view);
                }
            };

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter.startListening();
            recyclerView.setAdapter(adapter);


        }

        @Override
        protected void onStart() {
            super.onStart();
            if (adapter != null)
                adapter.startListening();
        }

        @Override
        protected void onStop() {
            if (adapter != null)
                adapter.stopListening();
            super.onStop();

        }

        @Override
        protected void onResume() {
            super.onResume();
            if (adapter != null)
                adapter.startListening();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView id;
            TextView home;
            TextView phone;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                id = itemView.findViewById(R.id.order_id);
                home = itemView.findViewById(R.id.order_home);
                phone = itemView.findViewById(R.id.order_phone);
                accept = itemView.findViewById(R.id.accept);
            }


        }

    }
