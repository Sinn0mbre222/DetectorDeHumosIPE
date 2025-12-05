package com.fireAlert;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.fireAlert.MainActivity;
import com.fireAlert.R;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    // Views
    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private ImageView ivNameCheck, ivEmailCheck, ivPasswordMatch;
    private TextView tvNameError, tvEmailError, tvPasswordError, tvConfirmError, tvLogin;
    private LinearLayout passwordStrengthContainer;
    private View strengthBar1, strengthBar2, strengthBar3, strengthBar4;
    private TextView tvPasswordRules;
    private ImageButton btnTogglePassword;
    private CheckBox cbTerms;
    private Button btnRegister;
    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;

    // Password strength colors
    private final int COLOR_WEAK = 0xFFFF5252;    // Red
    private final int COLOR_MEDIUM = 0xFFFF9800;  // Orange
    private final int COLOR_STRONG = 0xFF4CAF50;  // Green
    private final int COLOR_VERY_STRONG = 0xFF2E7D32; // Dark Green

    // Password visibility state
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity); // Asegúrate de que el layout se llame activity_register.xml

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        // Initialize all views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        ivNameCheck = findViewById(R.id.ivNameCheck);
        ivEmailCheck = findViewById(R.id.ivEmailCheck);
        ivPasswordMatch = findViewById(R.id.ivPasswordMatch);

        tvNameError = findViewById(R.id.tvNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        tvConfirmError = findViewById(R.id.tvConfirmError);
        tvLogin = findViewById(R.id.tvLogin);

        passwordStrengthContainer = findViewById(R.id.passwordStrengthContainer);
        strengthBar1 = findViewById(R.id.strengthBar1);
        strengthBar2 = findViewById(R.id.strengthBar2);
        strengthBar3 = findViewById(R.id.strengthBar3);
        strengthBar4 = findViewById(R.id.strengthBar4);

        tvPasswordRules = findViewById(R.id.tvPasswordRules);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressBar = loadingOverlay.findViewById(android.R.id.progress); // Si no funciona, cambia a findViewById directo

        // Configurar ProgressBar si es necesario
        if (progressBar == null) {
            // Buscar el ProgressBar dentro del FrameLayout
            for (int i = 0; i < loadingOverlay.getChildCount(); i++) {
                if (loadingOverlay.getChildAt(i) instanceof ProgressBar) {
                    progressBar = (ProgressBar) loadingOverlay.getChildAt(i);
                    break;
                }
            }
        }
    }

    private void setupListeners() {
        // Text change listeners for real-time validation
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFullName();
                updateRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
                updateRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
                checkPasswordMatch();
                updateRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordMatch();
                updateRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Toggle password visibility
        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Terms checkbox listener
        cbTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateRegisterButtonState();
        });

        // Register button
        btnRegister.setOnClickListener(v -> attemptRegistration());

        // Login link
        tvLogin.setOnClickListener(v -> {
            // Navegar a la actividad de Login
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Opcional: cierra esta actividad si no quieres que regrese
        });

        // Social login buttons (implementación básica)
        findViewById(R.id.btnGoogle).setOnClickListener(v -> {
            showLoading(true);
            // Simular registro con Google
            Toast.makeText(this, "Registro con Google - En desarrollo", Toast.LENGTH_SHORT).show();
            showLoading(false);
        });

        findViewById(R.id.btnFacebook).setOnClickListener(v -> {
            showLoading(true);
            // Simular registro con Facebook
            Toast.makeText(this, "Registro con Facebook - En desarrollo", Toast.LENGTH_SHORT).show();
            showLoading(false);
        });

        findViewById(R.id.btnApple).setOnClickListener(v -> {
            showLoading(true);
            // Simular registro con Apple
            Toast.makeText(this, "Registro con Apple - En desarrollo", Toast.LENGTH_SHORT).show();
            showLoading(false);
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            // Mostrar contraseña
            etPassword.setTransformationMethod(null);
            etConfirmPassword.setTransformationMethod(null);
            btnTogglePassword.setImageResource(R.drawable.ic_visibility); // Necesitarás este drawable
            btnTogglePassword.setContentDescription("Ocultar contraseña");
        } else {
            // Ocultar contraseña
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
            etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);
            btnTogglePassword.setContentDescription("Mostrar contraseña");
        }

        // Mover cursor al final
        etPassword.setSelection(etPassword.getText().length());
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    private boolean validateFullName() {
        String name = etFullName.getText().toString().trim();

        if (name.isEmpty()) {
            showError(tvNameError, "El nombre completo es requerido");
            hideValidationIcon(ivNameCheck);
            return false;
        }

        if (name.length() < 3) {
            showError(tvNameError, "El nombre debe tener al menos 3 caracteres");
            hideValidationIcon(ivNameCheck);
            return false;
        }

        if (!name.contains(" ")) {
            showError(tvNameError, "Ingresa nombre y apellido");
            hideValidationIcon(ivNameCheck);
            return false;
        }

        hideError(tvNameError);
        showSuccessIcon(ivNameCheck);
        return true;
    }

    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            showError(tvEmailError, "El correo electrónico es requerido");
            hideValidationIcon(ivEmailCheck);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(tvEmailError, "Ingresa un correo electrónico válido");
            hideValidationIcon(ivEmailCheck);
            return false;
        }

        hideError(tvEmailError);
        showSuccessIcon(ivEmailCheck);
        return true;
    }

    private boolean validatePassword() {
        String password = etPassword.getText().toString();

        if (password.isEmpty()) {
            showError(tvPasswordError, "La contraseña es requerida");
            passwordStrengthContainer.setVisibility(View.GONE);
            return false;
        }

        if (password.length() < 8) {
            showError(tvPasswordError, "La contraseña debe tener al menos 8 caracteres");
            updatePasswordStrength(1); // Muy débil
            return false;
        }

        // Calcular fortaleza de la contraseña
        int strength = calculatePasswordStrength(password);
        updatePasswordStrength(strength);

        // Validaciones adicionales
        boolean hasLetter = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        if (!hasLetter || !hasDigit) {
            showError(tvPasswordError, "Usa letras y números");
            return false;
        }

        hideError(tvPasswordError);
        return true;
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        // Longitud mínima
        if (password.length() >= 8) strength++;

        // Contiene letras
        if (Pattern.compile("[a-zA-Z]").matcher(password).find()) strength++;

        // Contiene números
        if (Pattern.compile("[0-9]").matcher(password).find()) strength++;

        // Contiene caracteres especiales
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) strength++;

        // Contiene mayúsculas y minúsculas
        if (Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[A-Z]").matcher(password).find()) {
            strength = Math.min(strength + 1, 4);
        }

        return strength;
    }

    private void updatePasswordStrength(int strength) {
        passwordStrengthContainer.setVisibility(View.VISIBLE);

        // Reset all bars to default
        strengthBar1.setBackgroundColor(0xFF2C2C2C); // Color por defecto
        strengthBar2.setBackgroundColor(0xFF2C2C2C);
        strengthBar3.setBackgroundColor(0xFF2C2C2C);
        strengthBar4.setBackgroundColor(0xFF2C2C2C);

        // Update bars based on strength
        if (strength >= 1) {
            strengthBar1.setBackgroundColor(COLOR_WEAK);
        }
        if (strength >= 2) {
            strengthBar2.setBackgroundColor(COLOR_MEDIUM);
        }
        if (strength >= 3) {
            strengthBar3.setBackgroundColor(COLOR_STRONG);
        }
        if (strength >= 4) {
            strengthBar4.setBackgroundColor(COLOR_VERY_STRONG);
        }

        // Update password rules text
        String[] strengthTexts = {"Muy débil", "Débil", "Moderada", "Fuerte", "Muy fuerte"};
        if (strength >= 0 && strength <= 4) {
            tvPasswordRules.setText("Seguridad: " + strengthTexts[strength]);
        }
    }

    private boolean checkPasswordMatch() {
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (confirmPassword.isEmpty()) {
            hideValidationIcon(ivPasswordMatch);
            hideError(tvConfirmError);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError(tvConfirmError, "Las contraseñas no coinciden");
            hideValidationIcon(ivPasswordMatch);
            return false;
        }

        if (password.length() < 8) {
            hideValidationIcon(ivPasswordMatch);
            return false;
        }

        hideError(tvConfirmError);
        showSuccessIcon(ivPasswordMatch);
        return true;
    }

    private void updateRegisterButtonState() {
        boolean isNameValid = validateFullName();
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();
        boolean isPasswordMatch = checkPasswordMatch();
        boolean termsAccepted = cbTerms.isChecked();

        boolean allValid = isNameValid && isEmailValid && isPasswordValid && isPasswordMatch && termsAccepted;

        btnRegister.setEnabled(allValid);
        btnRegister.setAlpha(allValid ? 1.0f : 0.5f);
    }

    private void attemptRegistration() {
        // Validar todos los campos
        if (!validateFullName() || !validateEmail() || !validatePassword() || !checkPasswordMatch()) {
            Toast.makeText(this, "Por favor, corrige los errores en el formulario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar loading
        showLoading(true);

        // Obtener datos del formulario
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Aquí iría la lógica de registro real (API, Firebase, etc.)
        // Por ahora simulamos un registro exitoso después de 2 segundos
        new android.os.Handler().postDelayed(
                () -> {
                    showLoading(false);

                    // Simular registro exitoso
                    Log.d("RegistroActivity", "Registro exitoso: " + email);

                    // Navegar a la siguiente actividad
                    Toast.makeText(RegistroActivity.this,
                            "¡Registro exitoso! Bienvenido " + fullName,
                            Toast.LENGTH_LONG).show();

                    // Ejemplo: Navegar al MainActivity
                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                    intent.putExtra("USER_EMAIL", email);
                    intent.putExtra("USER_NAME", fullName);
                    startActivity(intent);
                    finish();
                },
                2000
        );
    }

    // Helper methods
    private void showError(TextView errorView, String message) {
        errorView.setText(message);
        errorView.setVisibility(View.VISIBLE);
    }

    private void hideError(TextView errorView) {
        errorView.setVisibility(View.GONE);
    }

    private void showSuccessIcon(ImageView iconView) {
        iconView.setVisibility(View.VISIBLE);
        iconView.setImageResource(R.drawable.ic_check_circle); // Necesitarás este drawable
        iconView.setColorFilter(0xFF4CAF50); // Verde
    }

    private void hideValidationIcon(ImageView iconView) {
        iconView.setVisibility(View.GONE);
    }

    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);

        // Deshabilitar otros controles mientras carga
        etFullName.setEnabled(!show);
        etEmail.setEnabled(!show);
        etPassword.setEnabled(!show);
        etConfirmPassword.setEnabled(!show);
        cbTerms.setEnabled(!show);
    }

    @Override
    protected void onDestroy() {
        // Limpiar recursos si es necesario
        super.onDestroy();
    }
}