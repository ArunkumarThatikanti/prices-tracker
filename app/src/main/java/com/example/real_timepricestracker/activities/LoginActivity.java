package com.example.real_timepricestracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.real_timepricestracker.R;
import com.example.real_timepricestracker.model.LoginRequest;
import com.example.real_timepricestracker.model.LoginResponse;
import com.example.real_timepricestracker.network.ApiClient;
import com.example.real_timepricestracker.network.ApiService;
import com.example.real_timepricestracker.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefManager.init(getApplicationContext());

        if (SharedPrefManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, PricesActivity.class);
            startActivity(intent);
            finish(); // Prevent going back to login
            return;
        }


        setContentView(R.layout.activity_main);

        SharedPrefManager.init(getApplicationContext());

        usernameInput = findViewById(R.id.usernameEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Send login request
            LoginRequest loginRequest = new LoginRequest(username, password);
            apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        System.out.println("token  "+response.body().getToken());
                        SharedPrefManager.saveToken(response.body().getToken());
                        runOnUiThread(() -> {
                            startActivity(new Intent(LoginActivity.this, PricesActivity.class));
                            finish();
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



}