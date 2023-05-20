package com.example.lazivery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewReqActivity extends AppCompatActivity {

    private EditText location, pay, items, instructions, paymethod;
    private Button sendreq;


    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newreq);

        database = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app");

        DatabaseReference orderRef = database.getReference("requests");

        sendreq = (Button) findViewById(R.id.submit_req);
        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { submitReq(orderRef); }
        });
    }

    public void submitReq(DatabaseReference orderRef) {
        location = (EditText) findViewById(R.id.location_edit_text);
        String locationstr = location.getText().toString();

        pay = (EditText) findViewById(R.id.offered_payment_edit_text);
        String paystr = pay.getText().toString();

        items = (EditText) findViewById(R.id.required_items);
        String itemstr = items.getText().toString();

        instructions = (EditText) findViewById(R.id.special_instructions);
        String instructionstr = instructions.getText().toString();

        paymethod = (EditText) findViewById(R.id.payment_method);
        String paymethodstr = paymethod.getText().toString();

        String status = "open";

        if (!TextUtils.isEmpty(locationstr) && !TextUtils.isEmpty(paystr) && !TextUtils.isEmpty(itemstr) &&
                !TextUtils.isEmpty(instructionstr) && !TextUtils.isEmpty(paymethodstr)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String requestId = user.getUid();
            Map<String, Object> order = new HashMap<>();
            order.put("location", locationstr);
            order.put("payment", paystr);
            order.put("items", itemstr);
            order.put("instructions", instructionstr);
            order.put("method", paymethodstr);
            order.put("status", status);

            orderRef.child(requestId).setValue(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(NewReqActivity.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewReqActivity.this, "Error while sending request", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }


}