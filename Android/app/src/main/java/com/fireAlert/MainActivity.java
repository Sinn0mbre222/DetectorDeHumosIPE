package com.fireAlert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tokenTextView;

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvForgotPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private ImageButton btnGoogle;
    private ImageButton btnFacebook;
    private ImageButton btnApple;
    private FrameLayout loadingOverlay;

    // Credenciales de prueba
    private static final String TEST_USER = "PruebasApp";
    private static final String TEST_PASSWORD = "FireAlert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        initViews();

        // Configurar listeners
        setupListeners();
        obtenerTokenFCM();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnApple = findViewById(R.id.btnApple);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void setupListeners() {
        // Botón de iniciar sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(v);
            }
        });

        // Enlace de registro
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // Olvidé mi contraseña
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Funcionalidad en desarrollo", Snackbar.LENGTH_SHORT).show();
            }
        });

        // Botones de redes sociales
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Inicio con Google en desarrollo", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Inicio con Facebook en desarrollo", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Inicio con Apple en desarrollo", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin(View v) {
        // Obtener texto de los campos
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validar campos vacíos
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Introduce tu correo o usuario");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Introduce tu contraseña");
            etPassword.requestFocus();
            return;
        }

        // Mostrar loading
        showLoading(true);

        // Verificar credenciales
        if (email.equals(TEST_USER) && password.equals(TEST_PASSWORD)) {
            // Login exitoso
            showLoading(false);
            Snackbar.make(v, "¡Bienvenido " + TEST_USER + "!", Snackbar.LENGTH_SHORT).show();

            // Navegar a HomePage
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish(); // Opcional: cierra MainActivity para que no vuelva atrás

        } else {
            // Credenciales incorrectas
            showLoading(false);
            Snackbar.make(v, "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG)
                    .setAction("Reintentar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etPassword.setText("");
                            etPassword.requestFocus();
                        }
                    })
                    .show();
        }
    }

    private void showLoading(boolean show) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        btnLogin.setEnabled(!show);
    }

    private void obtenerTokenFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Obtener el nuevo token
                        String token = task.getResult();

                        // Log y Toast
                        String msg = "Token: " + token;
                        Log.d(TAG, msg);
                        tokenTextView.setText(token);
                        Toast.makeText(MainActivity.this, "Token obtenido (ver Logcat)", Toast.LENGTH_SHORT).show();

                        // COPIA ESTE TOKEN DEL LOGCAT O PANTALLA PARA USARLO EN TU SERVER EXPRESS
                    }
                });
    }

    // Declarar el launcher para la solicitud de permisos
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // El usuario dio permiso
                } else {
                    Toast.makeText(this, "No podrás ver las notificaciones", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        // Esto solo es necesario para API level 33 (Tiramisu) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Ya tengo permiso
            } else {
                // Pedir permiso directamente
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}