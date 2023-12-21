package com.translator.quick.easy.LT_activities;

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
import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_ads.LT_AdClass;
import com.translator.quick.easy.LT_ads.LT_AppOpenManager;
import com.translator.quick.easy.LT_ads.LT_AppOpenManagerQureka;
import com.translator.quick.easy.LT_ads.LT_Connectivity;
import com.translator.quick.easy.LT_ads.LT_SharePrefs;
import com.translator.quick.easy.LT_ads.LT_Utils;
import com.translator.quick.easy.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LT_SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    LT_Connectivity ywfefXwsdLTConnectivity;
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
        ywfefXwsdLTConnectivity = new LT_Connectivity(this);
        if (ywfefXwsdLTConnectivity.isConnected()) {
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

                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.PRIVACY_POLICY_LINK, privacy_policy_url);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.TERM_CONDITION_LINK, terms_and_condition_url);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.URL_LINK, url_link);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putInt(LT_Utils.VERSION_CODE, versionCode);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putInt(LT_Utils.ADS_COUNT, ads_count);

                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.REMOVE_FACEBOOK, remove_facebook);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.SPLASH_APP_OPEN_SHOW, splash_app_open_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXIT_DIALOG_AD_SHOW, exit_dialog_ad_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.WELCOME_SCREEN_INTER, welcome_screen_inter);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.INTRO_AD, intro_ad);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.ADS_PRELOAD, ads_preload);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.ADS_SHOW, ads_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_BANNERS, extra_banners);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_SCREEN_1, extra_screen_1);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_SCREEN_2, extra_screen_2);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_SCREEN_3, extra_screen_3);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_SCREEN_4, extra_screen_4);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.EXTRA_SCREEN_5, extra_screen_5);

                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.INTER_ADS_SHOW, inter_ads_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.NATIVE_ADS_SHOW, native_ads_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.BANNER_ADS_SHOW, banner_ads_show);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.ONLY_URL, only_url);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean(LT_Utils.BACK_PRESS_AD, back_press_ad);

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

                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.INTERSTITIAL_AD_ID, inter_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.BANNER_AD_ID, banner_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.REWARD_AD_ID, reward_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.NATIVE_AD_ID, native_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.OPEN_AD_ID, app_open_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.FB_INTER_AD_ID, fb_inter_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.FB_BANNER_AD_ID, fb_banner_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.FB_NATIVE_AD_ID, fb_native_ad_id);
                            LT_SharePrefs.getInstance(LT_SplashActivity.this).putString(LT_Utils.FB_REWARD_AD_ID, fb_reward_ad_id);

                            if (LT_SharePrefs.getInstance(LT_SplashActivity.this).getBoolean(LT_Utils.ADS_PRELOAD)) {
                                LT_AdClass.preLoadInterAds(LT_SplashActivity.this);
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
                        if (ywfefXwsdLTConnectivity.isConnected()) {
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
                            Toast.makeText(LT_SplashActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
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
        LT_AppOpenManagerQureka.showAppOpenQureka(LT_SplashActivity.this);
    }

    public void loadAd() {

        if (LT_SharePrefs.getInstance(LT_SplashActivity.this).getBoolean(LT_Utils.ONLY_URL)) {
            loadAdQueka();
        } else {
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    LT_SplashActivity.this, LT_SharePrefs.getInstance(LT_SplashActivity.this).getString(LT_Utils.OPEN_AD_ID), request,
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
        appOpenAd.show(LT_SplashActivity.this);
    }


    private void redirect() {
        if (!LT_SharePrefs.getInstance(LT_SplashActivity.this).getBoolean("is_first")) {
            LT_SharePrefs.getInstance(LT_SplashActivity.this).putBoolean("is_first", true);
            LT_AppOpenManager.fetchAd();
        }
        if (LT_SharePrefs.getInstance(LT_SplashActivity.this).getBoolean(LT_Utils.SPLASH_APP_OPEN_SHOW)) {
            loadAd();
        } else {
            goToNextActivity();
        }
    }

    private void goToNextActivity() {
        Intent intent;

        if (!LT_SharePrefs.getInstance(LT_SplashActivity.this).getBoolean(LT_Utils.LANGUAGE_SELECTED)) {
            intent = new Intent(LT_SplashActivity.this, LT_SelectLanguageActivity.class);
        } else {
            intent = new Intent(LT_SplashActivity.this, LT_StartActivityExtra4.class);
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
