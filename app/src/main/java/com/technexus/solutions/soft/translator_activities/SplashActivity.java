package com.technexus.solutions.soft.translator_activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.ads.AppOpenManager;
import com.technexus.solutions.soft.ads.AppOpenManagerQureka;
import com.technexus.solutions.soft.ads.Connectivity;
import com.technexus.solutions.soft.ads.SharePrefs;
import com.technexus.solutions.soft.ads.Utils;
import com.technexus.solutions.soft.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Connectivity ywfefXwsdConnectivity;
    private Handler handler;
    Runnable runnable;
    AppOpenAd appOpenAd;

    public void hideSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        hideSystemUI();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        ActivitySplashBinding inflate = ActivitySplashBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        ywfefXwsdConnectivity = new Connectivity(this);
        if (ywfefXwsdConnectivity.isConnected()) {
            getDataFromConfig();
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    redirect();
                }
            };
            handler.postDelayed(runnable, 10000);
        } else {
            showNoInternetDialog();
        }
    }


    public void getDataFromConfig() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            handler.removeCallbacks(runnable);
                            String data = mFirebaseRemoteConfig.getString("data");
                            JSONObject jsonObject = new JSONObject(data);
                            int versionCode = jsonObject.getInt("version_code");
                            String privacy_policy_url = jsonObject.getString("privacy_policy_url");
                            String terms_and_condition_url = jsonObject.getString("terms_and_condition_url");
                            String url_link = jsonObject.getString("url_link");
                            int ads_count = jsonObject.getInt("ads_count");

                            boolean remove_facebook = jsonObject.getBoolean("remove_facebook");
                            boolean splash_app_open_show = jsonObject.getBoolean("splash_app_open_show");
                            boolean exit_dialog_ad_show = jsonObject.getBoolean("exit_dialog_ad_show");

                            boolean native_ads_show = jsonObject.getBoolean("native_ads_show");
                            boolean inter_ads_show = jsonObject.getBoolean("inter_ads_show");
                            boolean banner_ads_show = jsonObject.getBoolean("banner_ads_show");
                            boolean welcome_screen_inter = jsonObject.getBoolean("welcome_screen_inter");
                            boolean intro_ad = jsonObject.getBoolean("intro_ad");
                            boolean ads_preload = jsonObject.getBoolean("ads_preload");
                            boolean ads_show = jsonObject.getBoolean("ads_show");
                            boolean back_press_ad = jsonObject.getBoolean("back_press_ad");
                            boolean extra_banners = jsonObject.getBoolean("extra_banners");
                            boolean extra_screen_1 = jsonObject.getBoolean("extra_screen_1");
                            boolean extra_screen_2 = jsonObject.getBoolean("extra_screen_2");
                            boolean extra_screen_3 = jsonObject.getBoolean("extra_screen_3");
                            boolean extra_screen_4 = jsonObject.getBoolean("extra_screen_4");
                            boolean extra_screen_5 = jsonObject.getBoolean("extra_screen_5");
//                            boolean landing_page = jsonObject.getBoolean("landing_page");
                            boolean only_url = jsonObject.getBoolean("only_url");

                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.PRIVACY_POLICY_LINK, privacy_policy_url);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.TERM_CONDITION_LINK, terms_and_condition_url);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.URL_LINK, url_link);
                            SharePrefs.getInstance(SplashActivity.this).putInt(Utils.VERSION_CODE, versionCode);
                            SharePrefs.getInstance(SplashActivity.this).putInt(Utils.ADS_COUNT, ads_count);

                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.REMOVE_FACEBOOK, remove_facebook);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.SPLASH_APP_OPEN_SHOW, splash_app_open_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXIT_DIALOG_AD_SHOW, exit_dialog_ad_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.WELCOME_SCREEN_INTER, welcome_screen_inter);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.INTRO_AD, intro_ad);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.ADS_PRELOAD, ads_preload);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.ADS_SHOW, ads_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_BANNERS, extra_banners);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_SCREEN_1, extra_screen_1);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_SCREEN_2, extra_screen_2);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_SCREEN_3, extra_screen_3);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_SCREEN_4, extra_screen_4);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.EXTRA_SCREEN_5, extra_screen_5);

                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.INTER_ADS_SHOW, inter_ads_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.NATIVE_ADS_SHOW, native_ads_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.BANNER_ADS_SHOW, banner_ads_show);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.ONLY_URL, only_url);
                            SharePrefs.getInstance(SplashActivity.this).putBoolean(Utils.BACK_PRESS_AD, back_press_ad);

//                            JSONObject adsJsonObject = jsonObject.getJSONObject("ads_data");
                            JSONObject adsJsonObject = jsonObject.getJSONObject("ads_data_test");

                            String inter_ad_id = adsJsonObject.getString("inter_ad_id");
                            String banner_ad_id = adsJsonObject.getString("banner_ad_id");
                            String native_ad_id = adsJsonObject.getString("native_ad_id");
                            String reward_ad_id = adsJsonObject.getString("reward_ad_id");
                            String app_open_ad_id = adsJsonObject.getString("app_open_ad_id");

                            String fb_inter_ad_id = adsJsonObject.getString("fb_inter_ad_id");
                            String fb_banner_ad_id = adsJsonObject.getString("fb_banner_ad_id");
                            String fb_native_ad_id = adsJsonObject.getString("fb_native_ad_id");
                            String fb_reward_ad_id = adsJsonObject.getString("fb_reward_ad_id");

                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.INTERSTITIAL_AD_ID, inter_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.BANNER_AD_ID, banner_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.REWARD_AD_ID, reward_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.NATIVE_AD_ID, native_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.OPEN_AD_ID, app_open_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.FB_INTER_AD_ID, fb_inter_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.FB_BANNER_AD_ID, fb_banner_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.FB_NATIVE_AD_ID, fb_native_ad_id);
                            SharePrefs.getInstance(SplashActivity.this).putString(Utils.FB_REWARD_AD_ID, fb_reward_ad_id);

                            if (SharePrefs.getInstance(SplashActivity.this).getBoolean(Utils.ADS_PRELOAD)) {
                                AdClass.preLoadInterAds(SplashActivity.this);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    redirect();
                                }
                            }, 3000);

                        } catch (JSONException e) {
                            handler.removeCallbacks(runnable);
                            redirect();
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.removeCallbacks(runnable);
                        redirect();
                    }
                });
    }

    public void showNoInternetDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tv_retry = dialog.findViewById(R.id.tv_retry);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                tv_retry.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ywfefXwsdConnectivity.isConnected()) {
                            progressBar.setVisibility(View.GONE);
                            dialog.dismiss();
                            getDataFromConfig();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.splash_progress).setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        } else {
                            Toast.makeText(SplashActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                            tv_retry.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }, 4000);
            }
        });
        dialog.show();
    }


    public void loadAdQueka() {
        AppOpenManagerQureka.showAppOpenQureka(SplashActivity.this);
    }

    public void loadAd() {

        if (SharePrefs.getInstance(SplashActivity.this).getBoolean(Utils.ONLY_URL)) {
            loadAdQueka();
        } else {
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    SplashActivity.this, SharePrefs.getInstance(SplashActivity.this).getString(Utils.OPEN_AD_ID), request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd ad) {
                            appOpenAd = ad;
                            showAppOpen(appOpenAd);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            goToNextActivity();
                            // Called when an app open ad has failed to load.
                        }
                    });
        }
    }


    private void showAppOpen(AppOpenAd appOpenAd) {

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();

                goToNextActivity();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                goToNextActivity();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }
        });
        appOpenAd.show(SplashActivity.this);
    }


    private void redirect() {
        if (!SharePrefs.getInstance(SplashActivity.this).getBoolean("is_first")) {
            SharePrefs.getInstance(SplashActivity.this).putBoolean("is_first", true);
            AppOpenManager.fetchAd();
        }
        if (SharePrefs.getInstance(SplashActivity.this).getBoolean(Utils.SPLASH_APP_OPEN_SHOW)) {
            loadAd();
        } else {
            goToNextActivity();
        }
    }

    private void goToNextActivity() {
        Intent intent;

        if (!SharePrefs.getInstance(SplashActivity.this).getBoolean(Utils.LANGUAGE_SELECTED)) {
            intent = new Intent(SplashActivity.this, SelectLanguageActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, StartActivityExtra4.class);
        }

        startActivity(intent);
        finish();
    }

   /* private boolean checkPermission() {
        return Build.VERSION.SDK_INT >= 33 ? checkPermission(this, "android.permission.READ_MEDIA_IMAGES") && checkPermission(this, "android.permission.READ_MEDIA_VIDEO") : Build.VERSION.SDK_INT >= 29 ? checkPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") : checkPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
    }*/

 /*   public static boolean checkPermission(Activity activity, String str) {
        return ContextCompat.checkSelfPermission(activity, str) == 0;
    }*/


    @Override
    public void onResume() {
        super.onResume();
    }
}
