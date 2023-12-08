package com.technexus.solutions.soft.ads;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.browser.customtabs.CustomTabsIntent;

import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.translator_activities.HomeActivity;
import com.technexus.solutions.soft.translator_activities.StartActivityExtra4;

public class AppOpenManagerQureka {

    public static void showAppOpenQureka(Activity activity) {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_open_qureka);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout btnContinue = dialog.findViewById(R.id.btnContinue);
        LinearLayout linear_redirect = dialog.findViewById(R.id.linear_redirect);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(activity, StartActivityExtra4.class);
                activity.startActivity(intent);
                dialog.dismiss();
                activity.finish();
            }
        });

        linear_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                        .setToolbarColor(activity.getResources().getColor(R.color.ads_bg_color))
                        .setNavigationBarColor(activity.getResources().getColor(R.color.ads_bg_color));
                CustomTabsIntent customTabsIntent = builder.build();
                try {
                    customTabsIntent.launchUrl(activity, Uri.parse(SharePrefs.getInstance(activity).getString(Utils.URL_LINK)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

}
