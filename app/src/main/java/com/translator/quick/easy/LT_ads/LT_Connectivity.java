package com.translator.quick.easy.LT_ads;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LT_Connectivity {

    Context context;

    public LT_Connectivity(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo();
        } else {
            return null;
        }
    }

    public boolean isConnected() {
        NetworkInfo info = getNetworkInfo();
        LT_Utils.IS_CONNECTED = info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi());
        return (info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi()));
    }

    public boolean isConnectedWifi() {
        NetworkInfo info = getNetworkInfo();
//        Config.IS_CONNECTED = info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi());
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedMobile() {
        NetworkInfo info = getNetworkInfo();
//        Config.IS_CONNECTED = (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


}