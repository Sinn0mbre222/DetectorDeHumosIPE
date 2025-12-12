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

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        LinearLayout navDevices = findViewById(R.id.navDevices);
        LinearLayout navAlert = findViewById(R.id.navAlerts);
        LinearLayout navPerfil = findViewById(R.id.navProfile);

        FrameLayout btnAlerts = findViewById(R.id.btnAlerts);

        navDevices.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, ConnectedDevices.class);
                startActivity(intent);
            }
        });

        navAlert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Alerts.class);
                startActivity(intent);
            }
        });

        navPerfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Profile.class);
                startActivity(intent);
            }
        });

        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Alerts.class);
                startActivity(intent);
            }
        });

    }

}