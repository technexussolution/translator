package com.translator.quick.easy.LT_ads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import com.translator.quick.easy.R;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LT_Utils {

    public static final String IMAGE = "image";
    public static final String PRIVACY_POLICY_LINK = "PRIVACY_POLICY_LINK";
    public static final String TERM_CONDITION_LINK = "TERM_CONDITION_LINK";
    public static boolean IS_CONNECTED = false;
    public static final String VIDEO = "video";
    public static final String VOICE = "status";
    public static final String WALLPAPER = "wallpaper";
    public static final String FROM_PREMIUM = "from_premium";
    public static final String IS_FIRST_TIME = "is_first_time";
    public static final String IS_FROM_SETTING = "IS_FROM_SETTING";
    public static final String VERSION_CODE = "VERSION_CODE";
    public static final String ADS_COUNT = "ADS_COUNT";
    public static final String EXTRA_BANNERS = "EXTRA_BANNERS";
    public static final String LANGUAGE_SELECTED = "LANGUAGE_SELECTED";
    public static final String EXTRA_SCREEN_1 = "EXTRA_SCREEN_1";
    public static final String EXTRA_SCREEN_2 = "EXTRA_SCREEN_2";
    public static final String EXTRA_SCREEN_3 = "EXTRA_SCREEN_3";
    public static final String EXTRA_SCREEN_4 = "EXTRA_SCREEN_4";
    public static final String EXTRA_SCREEN_5 = "EXTRA_SCREEN_5";
    public static final String BACK_PRESS_AD = "BACK_PRESS_AD";
    public static final String LANDING_PAGE = "LANDING_PAGE";
    public static final String INTRO_REPEAT = "INTRO_REPEAT";
    public static final String INTRO_AD = "INTRO_AD_SHOW";
    public static final String ADS_PRELOAD = "ads_preload";
    public static final String ADS_SHOW = "ADS_SHOW";
    public static final String PERMISSION_AD = "permission_ad";
    public static final String IS_LOADER_SHOW = "IS_LOADER_SHOW";
    public static final String INTRO_SHOW = "INTRO_SHOW";
    public static final String INTRO_COMPLETE = "INTRO_COMPLETE";
    public static final String WELCOME_COMPLETE = "WELCOME_COMPLETE";
    public static final String WELCOME_SCREEN_INTER = "WELCOME_SCREEN_INTER";


    public static final String IS_SUBSCRIBE = "IS_SUBSCRIBE";
    public static final String BANNER_AD_ID = "BANNER_AD_ID";
    public static final String REWARD_AD_ID = "REWARD_AD_ID";
    public static final String INTERSTITIAL_AD_ID = "INTERSTITIAL_AD_ID";
    public static final String NATIVE_AD_ID = "NATIVE_AD_ID";
    public static final String NATIVE_AD_POS = "NATIVE_AD_POS";
    public static final String OPEN_AD_ID = "OPEN_AD_ID";
    public static final String FB_NATIVE_AD_ID = "FB_NATIVE_AD_ID";
    public static final String FB_INTER_AD_ID = "FB_INTER_AD_ID";
    public static final String FB_BANNER_AD_ID = "FB_BANNER_AD_ID";
    public static final String FB_REWARD_AD_ID = "FB_REWARD_AD_ID";
    public static final String REMOVE_FACEBOOK = "REMOVE_FACEBOOK";
    public static final String EXIT_DIALOG_AD_SHOW = "EXIT_DIALOG_AD_SHOW";
    public static final String INTER_ADS_SHOW = "INTER_ADS_SHOW";
    public static final String NATIVE_ADS_SHOW = "NATIVE_ADS_SHOW";
    public static final String BANNER_ADS_SHOW = "BANNER_ADS_SHOW";
    public static final String ONLY_URL = "ONLY_URL";
    public static final String URL_LINK = "URL_LINK";
    public static final String SPLASH_APP_OPEN_SHOW = "SPLASH_APP_OPEN_SHOW";
    public static final String INDIA_COUNTRY_CODE = "IN";
    public static final String ARABIC_LANGUAGE_CODE = "ar";
    public static final String EXTRA_APP_INDEX = "extraAppIndex";
    public static final String EXTRA_CHANGE_LANGUAGE = "extraChangeLanguage";
    public static final String EXTRA_CHANGE_UNLOCK_PASS = "extra_change_unlock_pass";
    public static final String EXTRA_FIRST_TIME_SET_PASS = "extra_first_time_set_pass";
    public static final String EXTRA_INSTRUCTION = "extraInstruction";
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";
    public static final String EXTRA_SETTING = "extraSetting";
    public static final int LIST_APP_AD_INDEX = 0;
    public static final int RESULT_CHANGE_LANGUAGE = 101;
    public static final long SECOND_DELAY_MILLIS_TO_SHOW_AD = 3000;
    public static final String URDU_LANGUAGE_CODE = "ur";


    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString());
        sb.append("/WhatsApp/Media/WhatsApp Images");
    }

    public static void mediaScanner(Context context, String str, String str2, String str3) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{str + new File(str2).getName()}, new String[]{str3}, new MediaScannerConnection.MediaScannerConnectionClient() { // from class: com.downloader.easy.video.util.Utils.1
                @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
                public void onMediaScannerConnected() {
                }

                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String str4, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static boolean copyFileInSavedDir(Context context, String str) {
        String absolutePath;
        absolutePath = getDir().getAbsolutePath();
        Uri fromFile = Uri.fromFile(new File(absolutePath + File.separator + new File(str).getName()));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Status_log", "error : " + e.toString());
            return false;
        }
    }

    static File getDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "StatusSaver" + File.separator + "Whatsapp");
        file.mkdirs();
        return file;
    }

    public static void shareFile(Context context, boolean z, String str) {
        Uri uriForFile;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
//        if (str.startsWith(AppLovinEventTypes.USER_VIEWED_CONTENT)) {
//            uriForFile = Uri.parse(str);
//        } else {
        uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
//        }
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        context.startActivity(intent);
    }

    public static void repostWhatsApp(Context context, boolean z, String str) {
        Uri uriForFile;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
//        if (str.startsWith(AppLovinEventTypes.USER_VIEWED_CONTENT)) {
//            uriForFile = Uri.parse(str);
//        } else {
        uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
//        }
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.setPackage("com.whatsapp");
        context.startActivity(intent);
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void MoreApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static List<LT_Language> getListLanguage(Context context) {
        TypedArray obtainTypedArray;
        TypedArray obtainTypedArray2;
        TypedArray obtainTypedArray3;
        ArrayList arrayList = new ArrayList();
        if (Locale.getDefault().getCountry().toUpperCase().equals(LT_Utils.INDIA_COUNTRY_CODE)) {
            obtainTypedArray = context.getResources().obtainTypedArray(R.array.language_code_india);
            obtainTypedArray2 = context.getResources().obtainTypedArray(R.array.language_display_name_india);
            obtainTypedArray3 = context.getResources().obtainTypedArray(R.array.language_image_name_india);
        } else {
            obtainTypedArray = context.getResources().obtainTypedArray(R.array.language_code);
            obtainTypedArray2 = context.getResources().obtainTypedArray(R.array.language_display_name);
            obtainTypedArray3 = context.getResources().obtainTypedArray(R.array.language_image_name);
        }
        for (int i = 0; i < obtainTypedArray.length(); i++) {
            arrayList.add(new LT_Language(obtainTypedArray.getString(i), obtainTypedArray2.getString(i), obtainTypedArray3.getString(i)));
        }
        obtainTypedArray.recycle();
        obtainTypedArray2.recycle();
        obtainTypedArray3.recycle();
        return arrayList;
    }

    public static Drawable getDrawable(Context context, int i) {
        return ResourcesCompat.getDrawable(context.getResources(), i, null);
    }


    public static Drawable getDrawableFromName(Context context, String str) {
        return getDrawable(context, context.getResources().getIdentifier(str, "drawable", context.getPackageName()));
    }




}