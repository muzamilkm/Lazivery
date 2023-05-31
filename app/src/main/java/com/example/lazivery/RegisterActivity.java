package com.example.lazivery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phone, email, password;
    private String namestr, phonestr, emailstr, passwordstr;
    private Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.full_name_edit);
        phone = findViewById(R.id.phone_edit);
        email = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit_text);


        registerbtn = findViewById(R.id.register_button);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namestr = name.getText().toString();
                phonestr = phone.getText().toString();
                emailstr = email.getText().toString();
                passwordstr = password.getText().toString();

                if (!namestr.isEmpty() && !phonestr.isEmpty() && !emailstr.isEmpty() && !passwordstr.isEmpty()) {

                        if (emailstr.contains("@") && !emailstr.isEmpty())
                            startRegflow(emailstr, passwordstr);
                        else if (!emailstr.isEmpty())
                            Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void startRegflow(String email, String password) {

        if (!email.isEmpty() && !password.isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    saveAdditionalUserInfo(uid, namestr, phonestr);
                                    if (saveAdditionalUserInfo(uid, namestr, phonestr) == 1);
                                    {
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public int saveAdditionalUserInfo(String uid, String name, String phone) {
        char[] namearr = name.toCharArray();
        for (char c : namearr) {
            if (Character.isDigit(c)) {
                Toast.makeText(RegisterActivity.this, "Invalid name", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        if (phonestr.toCharArray().length != 11)
        {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return 0;
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://lazivery-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("users");
        DatabaseReference userRef = usersRef.child(uid);
        userRef.child("fullName").setValue(name);
        userRef.child("phoneNumber").setValue(phone);
        
        return 1;
    }

}
