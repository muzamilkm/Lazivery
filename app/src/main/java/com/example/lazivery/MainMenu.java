package com.example.lazivery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainMenu extends AppCompatActivity {
    private ImageView profile;
    private FloatingActionButton logout;
    private Button createreq, checkreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        profile = (ImageView) findViewById(R.id.prof_icon);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        logout = (FloatingActionButton) findViewById(R.id.logout_fab);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotologin();
            }
        });

        createreq = (Button) findViewById(R.id.create_request_button);
        createreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, NewReqActivity.class);
                startActivity(intent);
            }
        });

        checkreq = findViewById(R.id.check_open_requests_button);
        checkreq.setOnClickListener(v -> checkOpenRequests());

    }
    @Override
    public void onBackPressed()
    {
        showExitPrompt();
    }

    public void openProfile()
    {
        Intent intent = new Intent(MainMenu.this, Profile.class);
        startActivity(intent);
    }

    public void gotologin()
    {
        Intent intent = new Intent(MainMenu.this,MainActivity.class);
        startActivity(intent);
    }

    public void openSettings()
    {
        Intent intent = new Intent(MainMenu.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void checkOpenRequests()
    {
        Intent intent = new Intent(MainMenu.this, CurrentActivity.class);
        startActivity(intent);
    }

    private void showExitPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0); // Exit the app
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss the dialog and stay on the page
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}