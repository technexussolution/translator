package com.translator.quick.easy.LT_ads;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.browser.customtabs.CustomTabsIntent;

import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_activities.LT_StartActivityExtra4;

public class LT_AppOpenManagerQureka {

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
                intent = new Intent(activity, LT_StartActivityExtra4.class);
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
                    customTabsIntent.launchUrl(activity, Uri.parse(LT_SharePrefs.getInstance(activity).getString(LT_Utils.URL_LINK)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

}
