package com.translator.quick.easy.LT_ads;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import com.facebook.ads.Ad;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.translator.quick.easy.R;

import java.util.ArrayList;
import java.util.List;

public class LT_AdClass {

    public static InterstitialAd mInterstitialAd;
    public static AdRequest adRequest = new AdRequest.Builder().build();
    public static RewardedAd mRewardedAd;
    public static boolean rewardEarned = false;
    InterstitialAd interstitialAd;
    public static OnInterClose onInterCloseNew;
    public static com.facebook.ads.InterstitialAd fbInter;
    public static com.facebook.ads.AdView bannerAdViewFB;
    public static LinearLayout adViewNative;
    public static int ads_count = 0;
    public static boolean INTRO_REPEAT = false;
    public static boolean adLoadComplete = false;
    public static Handler mHandler;
    public static Runnable runnable;

    public interface OnInterClose {
        void onInterClose();
    }


    public static void showInterAd(Activity activity, OnInterClose onInterClose) {
        if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_PRELOAD)) {
            if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.INTER_ADS_SHOW) && LT_SharePrefs.getInstance(activity).getInt(LT_Utils.ADS_COUNT) == ads_count) {
                ads_count = 0;
                if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ONLY_URL)) {
                    onInterClose.onInterClose();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                            .setToolbarColor(activity.getResources().getColor(R.color.theme_color))
                            .setNavigationBarColor(activity.getResources().getColor(R.color.theme_color));
                    CustomTabsIntent customTabsIntent = builder.build();
                    try {
                        customTabsIntent.launchUrl(activity, Uri.parse(LT_SharePrefs.getInstance(activity).getString(LT_Utils.URL_LINK)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                mInterstitialAd = null;
                                onInterClose.onInterClose();
                                LT_AdClass.preLoadInterAds(activity);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });
                        mInterstitialAd.show(activity);
                    } else {
                        onInterClose.onInterClose();
                    }
                } else {
                    if (fbInter != null) {
                        onInterCloseNew = onInterClose;
                        fbInter.show();
                    } else {
                        onInterClose.onInterClose();
                    }
                }
            } else {
                onInterClose.onInterClose();
                ads_count++;
            }
        } else {
            loadAndShowInterAd(activity, onInterClose);
        }
    }

    public static void preLoadInterAds(Activity activity) {
        if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_PRELOAD)) {
            if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.INTER_ADS_SHOW)) {
                if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                    preLoadGoogle(activity);
                } else {
                    preLoadFacebook(activity);
                }
            }
        }
    }

    private static void preLoadGoogle(Activity activity) {
        InterstitialAd.load(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.INTERSTITIAL_AD_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private static void preLoadFacebook(Activity activity) {
        fbInter = new com.facebook.ads.InterstitialAd(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.FB_INTER_AD_ID));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                fbInter = null;
                if (onInterCloseNew != null) {
                    onInterCloseNew.onInterClose();
                }
                LT_AdClass.preLoadInterAds(activity);
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                fbInter = null;
                preLoadGoogle(activity);
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        fbInter.loadAd(fbInter.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }


    public static void loadAndShowInterAd(Activity activity, OnInterClose onInterClose) {
        if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.INTER_ADS_SHOW) && LT_SharePrefs.getInstance(activity).getInt(LT_Utils.ADS_COUNT) == ads_count) {
            ads_count = 0;
            Dialog updateDialog = new Dialog(activity);
            updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Rect displayRectangle = new Rect();
            updateDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.75), LinearLayout.LayoutParams.MATCH_PARENT);
            updateDialog.setContentView(R.layout.loading_dialog);
            updateDialog.setCancelable(false);
            updateDialog.show();
            mHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (onInterClose != null) {
                        onInterClose.onInterClose();
                    }
                    updateDialog.dismiss();
                }
            };
            mHandler.postDelayed(runnable, 8000);

            if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                loadAndShowGoogleInter(activity, onInterClose, updateDialog, LT_SharePrefs.getInstance(activity).getString(LT_Utils.INTERSTITIAL_AD_ID));
            } else {
                loadAndShowFBInter(activity, onInterClose, updateDialog);
            }
        } else {
            ads_count++;
            onInterClose.onInterClose();
        }
    }

    private static void loadAndShowFBInter(Activity activity, OnInterClose onInterClose, Dialog updateDialog) {
        fbInter = new com.facebook.ads.InterstitialAd(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.FB_INTER_AD_ID));

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                fbInter = null;
                if (onInterClose != null) {
                    onInterClose.onInterClose();
                }
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                fbInter = null;
                if (mHandler != null && runnable != null) {
                    try {
                        mHandler.removeCallbacks(runnable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                loadAndShowGoogleInter(activity, onInterClose, updateDialog, LT_SharePrefs.getInstance(activity).getString(LT_Utils.INTERSTITIAL_AD_ID));
//                loadGoogleInter(activity);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                updateDialog.dismiss();
                if (mHandler != null && runnable != null) {
                    try {
                        mHandler.removeCallbacks(runnable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                fbInter.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        fbInter.loadAd(fbInter.buildLoadAdConfig().withAdListener(interstitialAdListener).build());

    }

    private static void loadAndShowGoogleInter(Activity activity, OnInterClose onInterClose, Dialog updateDialog, String adId) {
        InterstitialAd.load(activity, adId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        updateDialog.dismiss();
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                onInterClose.onInterClose();
//                                    loadInter(activity);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });
                        interstitialAd.show(activity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        updateDialog.dismiss();
                        onInterClose.onInterClose();
                    }
                });
    }


    public static void showInterAdSplash(Activity activity, OnInterClose onInterClose) {
        if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_PRELOAD)) {
            if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.INTER_ADS_SHOW)) {
                if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                mInterstitialAd = null;
                                onInterClose.onInterClose();
                                LT_AdClass.preLoadInterAds(activity);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {

                                super.onAdShowedFullScreenContent();
                            }
                        });
                        mInterstitialAd.show(activity);
                    } else {
                        onInterClose.onInterClose();
                    }
                } else {
                    if (fbInter != null) {
                        onInterCloseNew = onInterClose;
                        fbInter.show();
                    } else {
                        onInterClose.onInterClose();
                    }
                }
            } else {
                onInterClose.onInterClose();
            }
        } else {
            loadAndShowInterAdSplash(activity, onInterClose);
        }
    }

    public static void loadAndShowInterAdSplash(Activity activity, OnInterClose onInterClose) {
        if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.INTER_ADS_SHOW)) {
            Dialog updateDialog = new Dialog(activity);
            updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Rect displayRectangle = new Rect();
            updateDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.75), LinearLayout.LayoutParams.MATCH_PARENT);
            updateDialog.setContentView(R.layout.loading_dialog);
            updateDialog.setCancelable(false);
            updateDialog.show();
            if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                loadAndShowGoogleInter(activity, onInterClose, updateDialog, LT_SharePrefs.getInstance(activity).getString(LT_Utils.INTERSTITIAL_AD_ID));
            } else {
                loadAndShowFBInter(activity, onInterClose, updateDialog);
            }
        } else {
            onInterClose.onInterClose();
        }
    }


    public static void showBanner(Activity context, LinearLayout layout) {

        AdView adView = new AdView(context);
        adView.setAdSize(getAdSize(context));
        adView.setAdUnitId(LT_SharePrefs.getInstance(context).getString(LT_Utils.BANNER_AD_ID));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        layout.addView(adView);
    }

    public static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void loadBannerAd(Activity activity, LinearLayout linearLayout, LinearLayout linearQureka) {
        if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.BANNER_ADS_SHOW)) {
            if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ONLY_URL)) {

                linearQureka.setVisibility(View.VISIBLE);

                linearQureka.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                                .setToolbarColor(activity.getResources().getColor(R.color.theme_color))
                                .setNavigationBarColor(activity.getResources().getColor(R.color.theme_color));
                        CustomTabsIntent customTabsIntent = builder.build();
                        try {
                            customTabsIntent.launchUrl(activity, Uri.parse(LT_SharePrefs.getInstance(activity).getString(LT_Utils.URL_LINK)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                showBanner(activity, linearLayout);
            } else {
                showFacebookBanner(activity, linearLayout);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    private static void showFacebookBanner(Activity activity, LinearLayout linearLayout) {
        bannerAdViewFB = new com.facebook.ads.AdView(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.FB_BANNER_AD_ID), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.addView(bannerAdViewFB);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                showBanner(activity, linearLayout);
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        bannerAdViewFB.loadAd(bannerAdViewFB.buildLoadAdConfig().withAdListener(adListener).build());
    }

    private static void loadGoogleNative(Activity activity, FrameLayout flNative, int view, ShimmerFrameLayout layout, String adId) {

        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(view, null, false);
        AdLoader.Builder builder = new AdLoader.Builder(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.NATIVE_AD_ID));
        builder.forNativeAd(nativeAd -> {
            flNative.setVisibility(View.VISIBLE);
            populateNativeADView(nativeAd, nativeAdView, flNative, layout);

        });

        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        flNative.setVisibility(View.GONE);
                                        if (layout != null) {
                                            layout.setVisibility(View.GONE);
                                        }
                                    }
                                })
                        .build();

        AdRequest.Builder m_builder = new AdRequest.Builder();
        adLoader.loadAd(m_builder.build());
    }

    public static void showNativeAd(Activity context, FrameLayout flNative, int view, NativeAdLayout fbNativeAdLayout, int fbView, ShimmerFrameLayout layout, int adSize, RelativeLayout relativeNative) {
        if (context != null && LT_SharePrefs.getInstance(context).getBoolean(LT_Utils.ADS_SHOW) && LT_SharePrefs.getInstance(context).getBoolean(LT_Utils.NATIVE_ADS_SHOW)) {
            if (LT_SharePrefs.getInstance(context).getBoolean(LT_Utils.ONLY_URL)) {
                relativeNative.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                relativeNative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                                .setToolbarColor(context.getResources().getColor(R.color.theme_color))
                                .setNavigationBarColor(context.getResources().getColor(R.color.theme_color));
                        CustomTabsIntent customTabsIntent = builder.build();
                        try {
                            customTabsIntent.launchUrl(context, Uri.parse(LT_SharePrefs.getInstance(context).getString(LT_Utils.URL_LINK)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (LT_SharePrefs.getInstance(context).getBoolean(LT_Utils.REMOVE_FACEBOOK)) {
                loadGoogleNative(context, flNative, view, layout, LT_SharePrefs.getInstance(context).getString(LT_Utils.NATIVE_AD_ID));
            } else {
                loadFacebookNative(context, flNative, view, fbNativeAdLayout, fbView, layout, adSize);
            }
        } else {
            flNative.setVisibility(View.GONE);
            if (layout != null) {
                layout.setVisibility(View.GONE);
            }
        }
    }

    public static void loadFacebookNative(Activity activity, FrameLayout flNative, int view, NativeAdLayout fbNativeAdLayout, int fbView, ShimmerFrameLayout layout, int adSize) {
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        adLoadComplete = false;
        com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(activity, LT_SharePrefs.getInstance(activity).getString(LT_Utils.FB_NATIVE_AD_ID));

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                if (!adLoadComplete) {
                    loadGoogleNative(activity, flNative, view, layout, LT_SharePrefs.getInstance(activity).getString(LT_Utils.NATIVE_AD_ID));
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
//                loadGoogleNative(activity, flNative, view, layout);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                adLoadComplete = true;
                if (layout != null) {
                    layout.setVisibility(View.GONE);
                }
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                if (adSize == 0) {
                    inflateAdSmall(activity, fbNativeAdLayout, nativeAd, fbView);
                } else {
                    inflateAd(activity, fbNativeAdLayout, nativeAd, fbView);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };


        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private static void populateNativeADView(NativeAd nativeAd, NativeAdView adView, FrameLayout flNative, ShimmerFrameLayout layout) {
        adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.media_view));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
        adView.setCallToActionView(adView.findViewById(R.id.cta));
        adView.setIconView(adView.findViewById(R.id.adv_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (adView.getHeadlineView() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        if (adView.getMediaView() != null) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            if (adView.getBodyView() != null)
                adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            if (adView.getBodyView() != null) {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (nativeAd.getCallToAction() == null) {
            if (adView.getCallToActionView() != null)
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            if (adView.getCallToActionView() != null) {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        if (nativeAd.getIcon() == null) {
            if (adView.getIconView() != null)
                adView.getIconView().setVisibility(View.GONE);
        } else {
            if (adView.getIconView() != null) {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }


        if (nativeAd.getStarRating() == null) {
            if (adView.getStarRatingView() != null)
                adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            if (adView.getStarRatingView() != null) {
                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }
        adView.setNativeAd(nativeAd);

        flNative.removeAllViews();
        flNative.addView(adView);
        if (layout != null) {
            layout.setVisibility(View.GONE);
        }

    }

    private static void inflateAd(Activity activity, NativeAdLayout nativeAdView, com.facebook.ads.NativeAd nativeAd, int fbView) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewNative = (LinearLayout) inflater.inflate(fbView, nativeAdView, false);
        nativeAdView.addView(adViewNative);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adViewNative.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdView);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adViewNative.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adViewNative.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adViewNative.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adViewNative.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adViewNative.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adViewNative.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adViewNative.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.

        if (nativeAd.getAdvertiserName() != null && !nativeAd.getAdvertiserName().equals("")) {
            nativeAdTitle.setText(nativeAd.getAdvertiserName().toString());
        } else {
            nativeAdTitle.setVisibility(View.GONE);
        }

        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adViewNative, nativeAdMedia, nativeAdIcon, clickableViews);
    }


    private static void inflateAdSmall(Activity activity, NativeAdLayout nativeAdView, com.facebook.ads.NativeAd nativeAd, int fbView) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewNative = (LinearLayout) inflater.inflate(fbView, nativeAdView, false);
        nativeAdView.addView(adViewNative);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adViewNative.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdView);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adViewNative.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adViewNative.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adViewNative.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adViewNative.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adViewNative.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adViewNative.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adViewNative.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.

        if (nativeAd.getAdvertiserName() != null && !nativeAd.getAdvertiserName().equals("")) {
            nativeAdTitle.setText(nativeAd.getAdvertiserName().toString());
        } else {
            nativeAdTitle.setVisibility(View.GONE);
        }


        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adViewNative, nativeAdIcon, clickableViews);
    }

    public static void loadGoogleRectangleBanner(Activity activity, ShimmerFrameLayout layout, LinearLayout linearLayout, ProgressBar progressBar) {
        if (activity != null && LT_SharePrefs.getInstance(activity).getBoolean(LT_Utils.ADS_SHOW)) {
            AdView mAdView = new AdView(activity);
            mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            mAdView.setAdUnitId(LT_SharePrefs.getInstance(activity).getString(LT_Utils.BANNER_AD_ID));

            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }

                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (linearLayout != null) {
                        linearLayout.removeAllViews();
                        linearLayout.addView(mAdView);
                    }
                }
            });
            mAdView.loadAd(adRequest);
        } else {
            linearLayout.setVisibility(View.GONE);
            if (layout != null) {
                layout.setVisibility(View.GONE);
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
