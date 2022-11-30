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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputLayout namaLengkapInputLayout = findViewById(R.id.namaLengkapInputLayout);
        TextInputEditText namaLengkapInput = findViewById(R.id.namaLengkapInput);

        TextInputLayout emailInputLayout = findViewById(R.id.emailInputLayout);
        TextInputEditText emailInput = findViewById(R.id.emailInput);

        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInputLayout);
        TextInputEditText passwordInput = findViewById(R.id.passwordInput);

        TextInputLayout confirmpasswordInputLayout = findViewById(R.id.confirmpasswordInputLayout);
        TextInputEditText confirmpasswordInput = findViewById(R.id.confirmpasswordInput);

        Button daftarBtn = findViewById(R.id.daftarBtn);


        // CHECK NAMA
        namaLengkapInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.length() < 5 || charSequence.length() > 35) {
                    namaLengkapInputLayout.setError("Nama harus antara 5 - 35 karakter");
                } else {
                    namaLengkapInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // CHECK EMAIL
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
                if (charSequence.length() > 0 && !isValidEmail) {
                    emailInputLayout.setError("Email tidak valid");
                } else {
                    emailInputLayout.setErrorEnabled(false);
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
                    passwordInputLayout.setError("Password harus antara 5 - 20 karakter");
                } else {
                    passwordInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // CHECK CONFIRM PASSWORD
        confirmpasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.length() < 5 || charSequence.length() > 20) {
                    confirmpasswordInputLayout.setError("Konfirmasi password harus antara 5 - 20 karakter");
                } else if (!passwordInput.getText().toString().equals(confirmpasswordInput.getText().toString())) {
                    confirmpasswordInputLayout.setError("Password tidak sama");
                } else {
                    confirmpasswordInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        daftarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches();

                if (namaLengkapInput.getText().length() == 0) {
                    namaLengkapInputLayout.setError("Nama tidak boleh kosong");
                } else if (emailInput.getText().length() == 0) {
                    emailInputLayout.setError("Email tidak boleh kosong");
                } else if (passwordInput.getText().length() == 0) {
                    passwordInputLayout.setError("Password tidak boleh kosong");
                } else if (confirmpasswordInput.getText().length() == 0) {
                    confirmpasswordInputLayout.setError("Konfirmasi password tidak boleh kosong");
                } else if (namaLengkapInput.getText().length() < 5 || namaLengkapInput.getText().length() > 35) {
                    emailInputLayout.setError("Nama harus antara 5 - 35 karakter");
                } else if (!isValidEmail) {
                    emailInputLayout.setError("Email tidak valid");
                } else if (passwordInput.getText().length() < 5 || passwordInput.getText().length() > 20) {
                    passwordInputLayout.setError("Password harus antara 5 - 20 karakter");
                } else if (confirmpasswordInput.getText().length() < 5 || confirmpasswordInput.getText().length() > 20) {
                    confirmpasswordInputLayout.setError("Password harus antara 5 - 20 karakter");
                } else if (!passwordInput.getText().toString().equals(confirmpasswordInput.getText().toString())) {
                    confirmpasswordInputLayout.setError("Password tidak sama");
                } else {

                    String email = emailInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    String username = namaLengkapInput.getText().toString();

                    Map<String, Object> userdata = new HashMap<>();
                    userdata.put("email", email);
                    userdata.put("password", password);
                    userdata.put("username", username);

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean isEmailExist = false;
                                for (DataSnapshot snap : task.getResult().getChildren()) {
                                    if (snap.child("email").getValue().equals(email)) {
                                        Toast.makeText(RegisterActivity.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                                        isEmailExist = true;
                                    }
                                }

                                if (!isEmailExist) {
                                    int key = (int) task.getResult().getChildrenCount();
                                    Map<String, Object> newuser = new HashMap<>();
                                    if (key < 1) {
                                        newuser.put("0", userdata);
                                    } else {
                                        newuser.put(String.valueOf(key), userdata);
                                    }
                                    mDatabase.child("users").updateChildren(newuser);

                                    Toast.makeText(RegisterActivity.this, "Berhasil mendaftar", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });

                }
            }
        });

    }

    public void Register(String username, String email, String password) {
        // TODO REGISTER USER AND ADD TO DATABASE
        //

        Toast.makeText(this, "Sukses Mendaftar", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}