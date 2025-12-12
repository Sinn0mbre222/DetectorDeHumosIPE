package com.fireAlert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LinearLayout navDevices = findViewById(R.id.navDevices);
        LinearLayout navAlert = findViewById(R.id.navAlerts);
        LinearLayout navHome = findViewById(R.id.navHome);

        FrameLayout btnBack = findViewById(R.id.btnBack);

        navDevices.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, ConnectedDevices.class);
                startActivity(intent);
            }
        });

        navAlert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, Alerts.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });

    }
}