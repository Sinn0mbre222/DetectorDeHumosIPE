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

public class Alerts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        FrameLayout btnBack = findViewById(R.id.btnBack);

        LinearLayout mvHome = findViewById(R.id.navHome);
        LinearLayout mvDevices = findViewById(R.id.navDevices);
        LinearLayout mvPerfil = findViewById(R.id.navProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alerts.this, HomePage.class);
                startActivity(intent);
            }
        });

        mvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alerts.this, HomePage.class);
                startActivity(intent);
            }
        });

        mvDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alerts.this, ConnectedDevices.class);
                startActivity(intent);
            }
        });

        mvPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alerts.this, Profile.class);
                startActivity(intent);
            }
        });

    }
}