package com.translator.quick.easy.LT_utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import com.translator.quick.easy.R;


public class LT_CustomDialog extends Dialog {
    Activity activity;

    public LT_CustomDialog(Activity activity) {
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
