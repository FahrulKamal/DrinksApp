package com.rannstudio.drinksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailLayout = findViewById(R.id.emailInputLayout);
        TextInputEditText emailInput = findViewById(R.id.emailInput);

        TextInputLayout passwordLayout = findViewById(R.id.passwordInputLayout);
        TextInputEditText passwordInput = findViewById(R.id.passwordInput);

        Button loginBtn = findViewById(R.id.loginBtn);

        // CHECK EMAIL
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
                if (charSequence.length() > 0 && !isValidEmail) {
                    emailLayout.setError("Email tidak valid");
                } else {
                    emailLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // CHECK PASSWORD
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.length() < 5 || charSequence.length() > 20) {
                    passwordLayout.setError("Password harus antara 5 - 20 karakter");
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // CLICK MASUK
        loginBtn.setOnClickListener(view -> {
            boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches();

            if (emailInput.getText().length() == 0) {
                emailLayout.setError("Email tidak noleh kosong");
            } else if (passwordInput.getText().length() == 0) {
                passwordLayout.setError("Password tidak boleh kosong");
            } else if (!isValidEmail) {
                emailLayout.setError("Email tidak valid");
            } else if (passwordInput.getText().length() < 5 || passwordInput.getText().length() > 20) {
                passwordLayout.setError("Password harus antara 5 - 20 karakter");
            } else {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                for (DataSnapshot snap: task.getResult().getChildren()) {
                                    Login data = snap.getValue(Login.class);
                                    if (data.getEmail().equals(email) && data.getPassword().equals(password)) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        prefs.edit().putString("email", email).apply();
                                        prefs.edit().putBoolean("loggedIn", true).apply();

                                        Toast.makeText(LoginActivity.this, "Berhasil masuk", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}