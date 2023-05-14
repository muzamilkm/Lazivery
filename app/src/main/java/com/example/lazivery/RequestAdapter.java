package com.example.lazivery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class RequestAdapter extends FirebaseRecyclerAdapter<Request, RequestAdapter.RequestViewHolder> {

    private List<Request> mRequests;

    public RequestAdapter(@NonNull FirebaseRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Request model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new RequestViewHolder(view);
    }

//    @Override
//    public int getItemCount() {
//        return mRequests.size();
//    }

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
//            Log.d("CurrentActivity", "Retrieved request: " + request.toString()); // add this log statement
        }
    }
}
