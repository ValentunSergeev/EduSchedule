package com.valentun.eduschedule.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.valentun.eduschedule.MyApplication;

public class NetworkStatusChecker {
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.INSTANCE
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
