package com.example.lazivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainMenu extends AppCompatActivity {
    private ImageView profile, settings;
    private FloatingActionButton logout;
    private Button createreq;

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

        settings = (ImageView) findViewById(R.id.settings_icon);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

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
}