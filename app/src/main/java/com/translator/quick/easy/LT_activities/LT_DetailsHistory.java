package com.translator.quick.easy.LT_activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_ads.LT_AdClass;
import com.translator.quick.easy.LT_ads.LT_SharePrefs;
import com.translator.quick.easy.LT_ads.LT_Utils;
import com.translator.quick.easy.LT_db.LT_HelperDB;
import com.translator.quick.easy.LT_utils.LT_TranslatorConstants;


public class LT_DetailsHistory extends AppCompatActivity {
    ImageView back;
    FloatingActionButton copy;
    FloatingActionButton del;
    LT_HelperDB mydb;
    FloatingActionButton share;
    TextView source_label;
    TextView source_txt;
    TextView target_label;
    TextView target_txt;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history_details);
        mydb = new LT_HelperDB(getApplicationContext());
        source_label = (TextView) findViewById(R.id.source_detale_label);
        target_label = (TextView) findViewById(R.id.target_detale_label);
        source_txt = (TextView) findViewById(R.id.source_detaile_text);
        target_txt = (TextView) findViewById(R.id.target_dtaile_text);
        copy = (FloatingActionButton) findViewById(R.id.copy);
        source_label.setText(LT_TranslatorConstants.source_l);
        target_label.setText(LT_TranslatorConstants.target_l);
        source_txt.setText(LT_TranslatorConstants.source_t);
        target_txt.setText(LT_TranslatorConstants.target_t);
        back = (ImageView) findViewById(R.id.goback);
        del = (FloatingActionButton) findViewById(R.id.del);
        share = (FloatingActionButton) findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_activities.DetailsHistory.1
            @Override
            public void onClick(View view) {
                LT_DetailsHistory.this.finish();
            }
        });
        this.del.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_activities.DetailsHistory.2
            @Override
            public void onClick(View view) {
                if (LT_DetailsHistory.this.mydb.delTrans(String.valueOf(LT_TranslatorConstants.position)).intValue() == 1) {
                    Toast.makeText(LT_DetailsHistory.this.getApplicationContext(), "Translation item successfully deleted.", Toast.LENGTH_SHORT).show();
                    LT_DetailsHistory.this.finish();
                }
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_activities.DetailsHistory.3
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
                    intent.putExtra("android.intent.extra.TEXT", (("Translation result\n" + LT_TranslatorConstants.source_t + "\n\n") + LT_TranslatorConstants.target_t + "\n\n-----------\n") + "For more transltions please download the App\nhttps://play.google.com/store/apps/details?id=" + LT_DetailsHistory.this.getPackageName());
                    LT_DetailsHistory.this.startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception unused) {
                    Toast.makeText(LT_DetailsHistory.this.getApplicationContext(), "Sorry, Something Went Wrong", 0).show();
                }
            }
        });
        this.copy.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_activities.DetailsHistory.4
            @Override
            public void onClick(View view) {
                if (LT_DetailsHistory.this.target_txt.getText().toString().length() != 0) {
                    if (Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager) LT_DetailsHistory.this.getSystemService(Context.CLIPBOARD_SERVICE)).setText(LT_DetailsHistory.this.target_txt.getText().toString());
                    } else {
                        ((ClipboardManager) LT_DetailsHistory.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copy", LT_DetailsHistory.this.target_txt.getText().toString()));
                    }
                    Toast.makeText(LT_DetailsHistory.this.getApplicationContext(), "Translated Text Copied to clipboard", 0).show();
                    return;
                }
                Toast.makeText(LT_DetailsHistory.this.getBaseContext(), "Nothing to Copy", 0).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (LT_SharePrefs.getInstance(LT_DetailsHistory.this).getBoolean(LT_Utils.BACK_PRESS_AD)) {
            LT_AdClass.showInterAd(LT_DetailsHistory.this, new LT_AdClass.OnInterClose() {
                @Override
                public void onInterClose() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }


}
