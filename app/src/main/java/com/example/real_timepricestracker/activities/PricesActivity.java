package com.example.real_timepricestracker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.real_timepricestracker.R;
import com.example.real_timepricestracker.adapters.PricesAdapter;
import com.example.real_timepricestracker.model.Price;
import com.example.real_timepricestracker.network.ApiClient;
import com.example.real_timepricestracker.network.ApiService;
import com.example.real_timepricestracker.utils.SharedPrefManager;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PricesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PricesAdapter adapter;
    private ApiService apiService;
    private Executor executor;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);

        if (SharedPrefManager.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        checkNotificationPermission();

        executor = ContextCompat.getMainExecutor(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiClient.getClient().create(ApiService.class);

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            SharedPrefManager.clearToken();
            startActivity(new Intent(PricesActivity.this, LoginActivity.class));
            finish();
        });

        showBiometricPrompt();
    }


    private void showBiometricPrompt() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        fetchPrices();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(PricesActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(PricesActivity.this, "Error: " + errString, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Authenticate to access price data")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void fetchPrices() {
        String token = SharedPrefManager.getToken();
        apiService.getPrices( token).enqueue(new Callback<List<Price>>() {
            @Override
            public void onResponse(Call<List<Price>> call, Response<List<Price>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PricesAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(PricesActivity.this, "Failed to load prices", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Price>> call, Throwable t) {
                Toast.makeText(PricesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted — you can now show notifications
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied — handle it gracefully
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}