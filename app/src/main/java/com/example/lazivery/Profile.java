package com.example.lazivery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {
    private EditText passwordedit, emailedit, phoneedit, nameedit, currentpass;
    private Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        passwordedit = findViewById(R.id.password_edit);
        emailedit = findViewById(R.id.email_edit);
        phoneedit = findViewById(R.id.phone_no_edit);
        nameedit = findViewById(R.id.full_name_edit);
        currentpass = findViewById(R.id.currentpass_text);

        savebtn = findViewById(R.id.manage_profile_btn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordstr = passwordedit.getText().toString();
                String emailstr = emailedit.getText().toString();
                String phonestr = phoneedit.getText().toString();
                String namestr = nameedit.getText().toString();
                String currentpw = currentpass.getText().toString();

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (currentpw.isEmpty())
                    {
                        Toast.makeText(Profile.this, "Verify current password to make profile changes", Toast.LENGTH_LONG).show();
                    }
                    else {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentpw);

                        user.reauthenticate(credential)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        if (!emailstr.isEmpty() && emailstr.contains("@")) {
                                            user.updateEmail(emailstr);
                                        }
                                        else if (!emailstr.isEmpty())
                                            Toast.makeText(Profile.this, "New email is invalid", Toast.LENGTH_SHORT).show();
                                        if (!passwordstr.isEmpty()) {
                                            user.updatePassword(passwordstr);
                                        }

                                        if (!phonestr.isEmpty() && phonestr.toCharArray().length == 11) {
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app");
                                            DatabaseReference userRef = firebaseDatabase.getReference("users").child(user.getUid());

                                            userRef.child("phoneNumber").setValue(phonestr);
                                        }
                                        else if (!phonestr.isEmpty())
                                            Toast.makeText(Profile.this, "New phone number is invalid", Toast.LENGTH_SHORT).show();

                                        if (!namestr.isEmpty()) {
                                            char[] namearr = namestr.toCharArray();
                                            for (char c : namearr)
                                            {
                                                if(Character.isDigit(c))
                                                {
                                                    Toast.makeText(Profile.this, "Invalid name", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app");
                                                    DatabaseReference userRef = firebaseDatabase.getReference("users").child(user.getUid());

                                                    userRef.child("fullName").setValue(namestr);
                                                }
                                            }

                                        }

                                        Toast.makeText(Profile.this, "Information Updated!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile.this, "The entered current password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                }
            }
        });
    }
}
