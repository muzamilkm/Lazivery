package com.example.lazivery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public FloatingActionButton loginBtn;
    private Button register;
    private EditText emailfield;
    private EditText passwordfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (FloatingActionButton) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                emailfield = findViewById(R.id.username_edit_text);
                passwordfield = findViewById(R.id.password_edit_text);
                String email = emailfield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                if (!email.isEmpty() || !password.isEmpty())
                    loginUser(email,password);
                else
                    Toast.makeText(MainActivity.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoregister();
            }
        });

    }

    private void loginUser(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is successfully authenticated
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
//                                if (user.isEmailVerified())
                                    openMainMenu();
//                                else
//                                    Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Authentication failed
                            Exception exception = task.getException();
                            // Handle the error
                            Toast.makeText(MainActivity.this, "Incorrect email/password", Toast.LENGTH_SHORT).show();;
                        }
                    }
                });
    }

    public void openMainMenu()
    {
        Intent intent = new Intent(MainActivity.this, MainMenu.class);
        startActivity(intent);
    }
    public void gotoregister()
    {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


}