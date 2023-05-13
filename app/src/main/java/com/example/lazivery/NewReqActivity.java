package com.example.lazivery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    public void submitReq(DatabaseReference orderRef)
    {
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

        if (!TextUtils.isEmpty(locationstr) && !TextUtils.isEmpty(paystr) && !TextUtils.isEmpty(itemstr) &&
                !TextUtils.isEmpty(instructionstr) && !TextUtils.isEmpty(paymethodstr))
        {
            ArrayList<String> order = new ArrayList<>();
            order.add(locationstr);
            order.add(paystr);
            order.add(itemstr);
            order.add(instructionstr);
            order.add(paymethodstr);

            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get the existing data at the location
                    ArrayList<String> existingData = (ArrayList<String>) dataSnapshot.getValue();

                    // Add the new data to the existing data
                    if (existingData == null)
                    {
                        existingData = new ArrayList<String>();
                    }
                    existingData.addAll(order);

                    // Save the updated data to the database
                    orderRef.setValue(existingData);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                }
            });
        }
        else
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }

}
