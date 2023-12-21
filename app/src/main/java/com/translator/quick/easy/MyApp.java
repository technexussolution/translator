package com.translator.quick.easy;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.translator.quick.easy.LT_ads.LT_AppOpenManager;
import com.translator.quick.easy.LT_ads.LT_Connectivity;


public class MyApp extends Application {

    public static MyApp wqnjMyApplication;
    LT_Connectivity wqnjLTConnectivity;
    public static MyApp instance;
    public static LT_AppOpenManager appOpenAdManager;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        wqnjMyApplication = this;
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        wqnjLTConnectivity = new LT_Connectivity(this);
        instance = this;

        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        showOpenAds();
    }

    public static void showOpenAds() {
        try {
            appOpenAdManager = new LT_AppOpenManager(wqnjMyApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
