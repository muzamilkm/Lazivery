package com.example.lazivery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestAdapter extends FirebaseRecyclerAdapter<Request, RequestAdapter.RequestViewHolder> {

    public RequestAdapter(@NonNull FirebaseRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Request model) {
        holder.bind(model);
        Button acceptButton = holder.itemView.findViewById(R.id.acceptreqbtn);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the accept button click for the specific request at the given position
                onAcceptButtonClick(holder.getAbsoluteAdapterPosition());
            }
        });
    }
    public void onAcceptButtonClick(int position)
    {
        DatabaseReference requestRef = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("requests");
        Log.d("reached onacceptbuttonclick", "you're clicking position "+position);


        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int i = 0;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (i == position) {
                        String creatorId = childSnapshot.getKey(); // Retrieve the creator ID

                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new RequestViewHolder(view);
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        private TextView locationTextView;
        private TextView paymentTextView;
        private TextView itemsTextView;
        private TextView instructionsTextView;
        private TextView methodTextView;

        public RequestViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.textview_location);
            paymentTextView = itemView.findViewById(R.id.textview_payment);
            itemsTextView = itemView.findViewById(R.id.textview_items);
            instructionsTextView = itemView.findViewById(R.id.textview_instructions);
            methodTextView = itemView.findViewById(R.id.textview_method);
        }

        public void bind(Request request) {
            locationTextView.setText(request.getLocation());
            paymentTextView.setText(request.getPayment());
            itemsTextView.setText(request.getItems());
            instructionsTextView.setText(request.getInstructions());
            methodTextView.setText(request.getMethod());
        }
    }
}
