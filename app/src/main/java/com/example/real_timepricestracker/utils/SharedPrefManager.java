package com.example.real_timepricestracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "stripe_prefs";
    private static final String TOKEN_KEY = "token";
    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(String token) {
        preferences.edit().putString(TOKEN_KEY, token).apply();
    }

    public static String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public static void clearToken() {
        preferences.edit().remove(TOKEN_KEY).apply();
    }

    public static boolean isLoggedIn() {
        return getToken() != null;
    }
}
