package com.example.cofinder.common;

import android.content.Context;

import com.example.cofinder.MainActivity;
import com.example.cofinder.R;

public abstract class Utils {
    public static String buildApiUrl(String businessId) {
        Context context = MainActivity.getContext();
        String apiUrl = context.getResources().getString(R.string.apiUrl);
        return apiUrl + businessId;
    }
}
