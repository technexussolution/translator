package com.technexus.solutions.soft.translator_utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import com.technexus.solutions.soft.R;


public class TranslatorCustomDialog extends Dialog {
    Activity activity;

    public TranslatorCustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.custom_dialog);
    }
}
