package com.example.lazivery;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestAdapter extends FirebaseRecyclerAdapter<Request, RequestAdapter.RequestViewHolder> {

    String creatorId, acceptorId;
    String fullName, phoneNumber;
    public RequestAdapter(@NonNull FirebaseRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Request model) {


        holder.bind(model);
        Log.d("status: ", "status: "+model.getStatus());

        String requestId = getRef(position).getKey();

        // Check if the request has been accepted
        if (model.getStatus().equals("accepted")) {
            // Hide or disable views related to accepted requests
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            // Show views for non-accepted requests
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Button acceptButton = holder.itemView.findViewById(R.id.acceptreqbtn);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the accept button click for the specific request at the given position
                    onAcceptButtonClick(holder.getAbsoluteAdapterPosition());
                }
            });
        }

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
                        creatorId = childSnapshot.getKey(); // Retrieve the creator ID


                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            acceptorId = firebaseUser.getUid();
                        }

                        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users");
                        String userId = acceptorId;
                        DatabaseReference userRef = usersRef.child(userId);
                        Log.d("acceptor id:", "acceptor id: "+userId);

                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    fullName = dataSnapshot.child("fullName").getValue(String.class);
                                    phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                                    Log.d("acceptor:", "acceptor phone and name: "+fullName + phoneNumber);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    Map<String, Object> acceptanceData = new HashMap<>();
                                    acceptanceData.put("acceptorUserId", acceptorId);
                                    acceptanceData.put("acceptorName", fullName);
                                    acceptanceData.put("acceptorPhoneNumber", phoneNumber);

                                    db.collection("acceptances")
                                            .document(userId)
                                            .set(acceptanceData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Successfully saved the acceptance data to Firestore
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to save the acceptance data to Firestore
                                                }
                                            });

                                    DocumentReference docRef = db.collection("acceptances").document(userId);

                                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @SuppressLint("RestrictedApi")
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w("Listen failed.", e);
                                                return;
                                            }

                                            if (snapshot != null && snapshot.exists()) {
                                                // Document exists and has been updated
                                                // Retrieve the acceptor's user ID and phone number from the snapshot
                                                String acceptorUserId = snapshot.getString("acceptorUserId");
                                                String acceptorName = snapshot.getString("acceptorName");
                                                String acceptorPhoneNumber = snapshot.getString("acceptorPhoneNumber");


                                                if(creatorId.equals(userId))
                                                    Toast.makeText(getApplicationContext(), "Your request was accepted by: " + fullName +" ("+phoneNumber+")", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        requestRef.child(creatorId).child("status").setValue("accepted");
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
