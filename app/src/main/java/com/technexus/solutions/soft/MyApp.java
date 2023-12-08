package com.technexus.solutions.soft;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.technexus.solutions.soft.ads.AppOpenManager;
import com.technexus.solutions.soft.ads.Connectivity;


public class MyApp extends Application {

    public static MyApp wqnjMyApplication;
    Connectivity wqnjConnectivity;
    public static MyApp instance;
    public static AppOpenManager appOpenAdManager;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        wqnjMyApplication = this;
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        wqnjConnectivity = new Connectivity(this);
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
            appOpenAdManager = new AppOpenManager(wqnjMyApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
