package com.technexus.solutions.soft.translator_activities;

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
import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.ads.SharePrefs;
import com.technexus.solutions.soft.ads.Utils;
import com.technexus.solutions.soft.translator_db.HelperDB;
import com.technexus.solutions.soft.translator_utils.TranslatorConstants;


public class DetailsHistory extends AppCompatActivity {
    ImageView back;
    FloatingActionButton copy;
    FloatingActionButton del;
    HelperDB mydb;
    FloatingActionButton share;
    TextView source_label;
    TextView source_txt;
    TextView target_label;
    TextView target_txt;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history_details);
        mydb = new HelperDB(getApplicationContext());
        source_label = (TextView) findViewById(R.id.source_detale_label);
        target_label = (TextView) findViewById(R.id.target_detale_label);
        source_txt = (TextView) findViewById(R.id.source_detaile_text);
        target_txt = (TextView) findViewById(R.id.target_dtaile_text);
        copy = (FloatingActionButton) findViewById(R.id.copy);
        source_label.setText(TranslatorConstants.source_l);
        target_label.setText(TranslatorConstants.target_l);
        source_txt.setText(TranslatorConstants.source_t);
        target_txt.setText(TranslatorConstants.target_t);
        back = (ImageView) findViewById(R.id.goback);
        del = (FloatingActionButton) findViewById(R.id.del);
        share = (FloatingActionButton) findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_activities.DetailsHistory.1
            @Override
            public void onClick(View view) {
                DetailsHistory.this.finish();
            }
        });
        this.del.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_activities.DetailsHistory.2
            @Override
            public void onClick(View view) {
                if (DetailsHistory.this.mydb.delTrans(String.valueOf(TranslatorConstants.position)).intValue() == 1) {
                    Toast.makeText(DetailsHistory.this.getApplicationContext(), "Translation item successfully deleted.", Toast.LENGTH_SHORT).show();
                    DetailsHistory.this.finish();
                }
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_activities.DetailsHistory.3
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
                    intent.putExtra("android.intent.extra.TEXT", (("Translation result\n" + TranslatorConstants.source_t + "\n\n") + TranslatorConstants.target_t + "\n\n-----------\n") + "For more transltions please download the App\nhttps://play.google.com/store/apps/details?id=" + DetailsHistory.this.getPackageName());
                    DetailsHistory.this.startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception unused) {
                    Toast.makeText(DetailsHistory.this.getApplicationContext(), "Sorry, Something Went Wrong", 0).show();
                }
            }
        });
        this.copy.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_activities.DetailsHistory.4
            @Override
            public void onClick(View view) {
                if (DetailsHistory.this.target_txt.getText().toString().length() != 0) {
                    if (Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager) DetailsHistory.this.getSystemService(Context.CLIPBOARD_SERVICE)).setText(DetailsHistory.this.target_txt.getText().toString());
                    } else {
                        ((ClipboardManager) DetailsHistory.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copy", DetailsHistory.this.target_txt.getText().toString()));
                    }
                    Toast.makeText(DetailsHistory.this.getApplicationContext(), "Translated Text Copied to clipboard", 0).show();
                    return;
                }
                Toast.makeText(DetailsHistory.this.getBaseContext(), "Nothing to Copy", 0).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (SharePrefs.getInstance(DetailsHistory.this).getBoolean(Utils.BACK_PRESS_AD)) {
            AdClass.showInterAd(DetailsHistory.this, new AdClass.OnInterClose() {
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
