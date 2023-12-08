package com.technexus.solutions.soft.translator_activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.ads.SharePrefs;
import com.technexus.solutions.soft.ads.Utils;
import com.technexus.solutions.soft.databinding.ActivityStartExtra4Binding;


public class StartActivityExtra4 extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    ActivityStartExtra4Binding binding;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_extra_4);
        this.mActivity = this;

        AdClass.showNativeAd(StartActivityExtra4.this, binding.nativeAdView, R.layout.native_ad_300, binding.nativeFacebook, R.layout.facebook_native, binding.shimmerAdView, 1, binding.relativeNative);


        binding.ivPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = SharePrefs.getInstance(StartActivityExtra4.this).getString(Utils.PRIVACY_POLICY_LINK);
                if (link != null && !link.equals("")) {
                    redirectLink(link);
                } else {
                    Toast.makeText(StartActivityExtra4.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.ivShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdClass.showInterAd(StartActivityExtra4.this, new AdClass.OnInterClose() {
                    @Override
                    public void onInterClose() {
                        shareApp();
                    }
                });
            }
        });

        binding.ivRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdClass.showInterAd(StartActivityExtra4.this, new AdClass.OnInterClose() {
                    @Override
                    public void onInterClose() {
                        launchMarket();
                    }
                });
            }
        });

        binding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityes(new Intent(StartActivityExtra4.this, HomeActivity.class));
            }
        });

    }


    @Override
    public void onClick(View view) {
    }

    void startActivityes(Intent intent) {
        AdClass.showInterAd(StartActivityExtra4.this, new AdClass.OnInterClose() {
            @Override
            public void onInterClose() {
                startActivity(intent);
            }
        });
    }


    public static boolean checkPermissions(Context context, String... strArr) {
        if (context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }


    public void redirectLink(String str) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void launchMarket() {
        try {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(viewIntent);
        } catch (Exception e) {
            Toast.makeText(StartActivityExtra4.this, "Unable to Connect Try Again...",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
            String shareMessage = "\nCheck out the App\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.xwer_exit_app_dialog);
        //        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView tv_exit = dialog.findViewById(R.id.tv_exit);
        TextView tv_stay = dialog.findViewById(R.id.tv_stay);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout linear_banner = dialog.findViewById(R.id.linear_banner);


        if (SharePrefs.getInstance(StartActivityExtra4.this).getBoolean(Utils.EXIT_DIALOG_AD_SHOW)) {
            AdClass.loadGoogleRectangleBanner(StartActivityExtra4.this, null, linear_banner, progressBar);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        //        cancel_text = dialog.findViewById(R.id.cancel_text);
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivityExtra4.super.onBackPressed();
            }
        });

        tv_stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
