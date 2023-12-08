package com.technexus.solutions.soft.translator_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;


import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.ads.Language;
import com.technexus.solutions.soft.ads.SharePrefs;
import com.technexus.solutions.soft.ads.Utils;
import com.technexus.solutions.soft.databinding.ActivitySelectLanguageBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectLanguageActivity extends AppCompatActivity {

    public Language deviceXwsdLanguage = null;
    private String deviceLanguageCode;
    private List<Language> listXwsdLanguage;
    //    LinearLayout llListLanguage;
    private Language selectedXwsdLanguage;
    private View selectedLanguageView;
    ActivitySelectLanguageBinding binding;
//    TextView tvTitle;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivitySelectLanguageBinding) DataBindingUtil.setContentView(this, R.layout.activity_select_language);

        AdClass.showNativeAd(SelectLanguageActivity.this, binding.nativeAdView, R.layout.native_ad_300_language, binding.nativeFacebook, R.layout.facebook_native, binding.shimmerAdView, 1, binding.relativeNative);


        binding.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdClass.showInterAd(SelectLanguageActivity.this, new AdClass.OnInterClose() {
                    @Override
                    public void onInterClose() {
                        onClickNext();
                    }
                });

            }
        });

        ArrayList arrayList = new ArrayList(Utils.getListLanguage(this));
        this.listXwsdLanguage = arrayList;
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Language Language = (Language) it.next();
            if (Language.getCode().equals(this.deviceLanguageCode)) {
                this.deviceXwsdLanguage = Language;
                break;
            }
        }
        int pos = -1;
        for (Language wqnjLanguage4 : listXwsdLanguage) {
            pos++;
            binding.llListLanguage.addView(layoutLanguage(wqnjLanguage4, pos));
        }
        View childAt = binding.llListLanguage.getChildAt(0);
        this.selectedXwsdLanguage = this.listXwsdLanguage.get(0);
        this.selectedLanguageView = childAt;
        updateSelectedLanguage(childAt);

    }

    void onClickNext() {
        SharePrefs.getInstance(SelectLanguageActivity.this).putBoolean(Utils.LANGUAGE_SELECTED, true);
        Intent intent;
        intent = new Intent(SelectLanguageActivity.this, StartActivityExtra4.class);
        startActivity(intent);
        finish();
    }


    private boolean checkPermission() {
        return Build.VERSION.SDK_INT >= 33 ? checkPermission(this, "android.permission.READ_MEDIA_IMAGES") && checkPermission(this, "android.permission.READ_MEDIA_VIDEO") : Build.VERSION.SDK_INT >= 29 ? checkPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") : checkPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
    }


    public static boolean checkPermission(Activity activity, String str) {
        return ContextCompat.checkSelfPermission(activity, str) == 0;
    }

    public ConstraintLayout layoutLanguage(final Language Language, int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_language, (ViewGroup) binding.llListLanguage, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_display_name);

        int[] image = {R.drawable.en, R.drawable.fr, R.drawable.es, R.drawable.pt, R.drawable.de, R.drawable.it, R.drawable.pl, R.drawable.zh, R.drawable.ja, R.drawable.ko, R.drawable.th, R.drawable.tr, R.drawable.ar, R.drawable.vi, R.drawable.hi, R.drawable.bn, R.drawable.hi, R.drawable.hi, R.drawable.hi, R.drawable.ur, R.drawable.hi, R.drawable.hi, R.drawable.hi, R.drawable.hi};

        ((ImageView) inflate.findViewById(R.id.iv_flag)).setImageResource(image[i]);
        textView.setText(Language.getDisplayName());
        if (Language.getCode().equals(Utils.ARABIC_LANGUAGE_CODE) || Language.getCode().equals(Utils.URDU_LANGUAGE_CODE)) {
            textView.setGravity(GravityCompat.END);
        } else {
            textView.setGravity(GravityCompat.START);
        }
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLanguageActivity.this.m54xab0c5acb(Language, view);
            }
        });
        return (ConstraintLayout) inflate;
    }

    void m54xab0c5acb(Language Language, View view) {
        updateDeselectLanguage();
        this.selectedXwsdLanguage = Language;
        this.selectedLanguageView = view;
        updateSelectedLanguage(view);
    }

    private void updateSelectedLanguage(View view) {
        view.setBackground(Utils.getDrawable(this, R.drawable.bg_shape_selected));
        ((ImageView) view.findViewById(R.id.iv_checked)).setVisibility(View.VISIBLE);
        ((ImageView) this.selectedLanguageView.findViewById(R.id.iv_checked)).setImageResource(R.drawable.ic_radio_button_checked);
    }

    private void updateDeselectLanguage() {
        View view = this.selectedLanguageView;
        if (view != null) {
            view.setBackground(Utils.getDrawable(this, R.drawable.bg_shape));

            ((ImageView) this.selectedLanguageView.findViewById(R.id.iv_checked)).setImageResource(R.drawable.ic_radio_button_unchecked);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }
}
