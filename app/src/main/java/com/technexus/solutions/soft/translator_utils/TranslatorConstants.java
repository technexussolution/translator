package com.technexus.solutions.soft.translator_utils;

import android.content.SharedPreferences;

import com.technexus.solutions.soft.R;


public class TranslatorConstants {
    public static int AD_SOURCE = 0;
    public static int App_Count = 0;
    public static String FILE_NAME = "translator";
    public static String KEY_AD = "add";
    public static String KEY_AD_Source = "ad_source";
    public static String KEY_BIG = "BGG";
    public static String KEY_Language_1 = "l11";
    public static String KEY_Language_2 = "l22";
    public static String KEY_OPEN_COUNT = "count";
    public static String KEY_SAVE_HISTORY = "save history";
    public static String KEY_SOURCE_COLOR = "source color";
    public static String KEY_TARGET_COLOR = "target color";
    public static SharedPreferences.Editor editor = null;
    public static String enable_history = "";
    public static int full_ad;
    public static int language2;
    public static int lnaguage1;
    public static int position;
    public static SharedPreferences sharedPreferences;
    public static int showornot;
    public static int source_color;
    public static String source_l;
    public static String source_t;
    public static int target_color;
    public static String target_l;
    public static String target_t;
    public static int[] flags = {R.drawable.flag_af, R.drawable.flag_sq, R.drawable.flag_am, R.drawable.flag_ar, R.drawable.flag_hy, R.drawable.flag_az, R.drawable.flag_eu, R.drawable.flag_be, R.drawable.flag_bn, R.drawable.flag_bs, R.drawable.flag_bg, R.drawable.flag_ca, R.drawable.flag_ceb, R.drawable.flag_ny, R.drawable.flag_zh, R.drawable.flag_zh, R.drawable.flag_fr, R.drawable.flag_hr, R.drawable.flag_cs, R.drawable.flag_da, R.drawable.flag_nl, R.drawable.flag_en, R.drawable.flag_eo, R.drawable.flag_et, R.drawable.flag_tl, R.drawable.flag_fi, R.drawable.flag_fr, R.drawable.flag_de, R.drawable.flag_gl, R.drawable.flag_ka, R.drawable.flag_de, R.drawable.flag_el, R.drawable.flag_gu, R.drawable.flag_ht, R.drawable.flag_ha, R.drawable.flag_haw, R.drawable.flag_iw, R.drawable.flag_hi, R.drawable.flag_hmn, R.drawable.flag_hu, R.drawable.flag_is, R.drawable.flag_ig, R.drawable.flag_id, R.drawable.flag_ga, R.drawable.flag_it, R.drawable.flag_ja, R.drawable.flag_jw, R.drawable.flag_kn, R.drawable.flag_kk, R.drawable.flag_km, R.drawable.flag_ko, R.drawable.flag_tr, R.drawable.flag_ky, R.drawable.flag_lo, R.drawable.flag_la, R.drawable.flag_lv, R.drawable.flag_lt, R.drawable.flag_lb, R.drawable.flag_mk, R.drawable.flag_mg, R.drawable.flag_ms, R.drawable.flag_ml, R.drawable.flag_mt, R.drawable.flag_mi, R.drawable.flag_mr, R.drawable.flag_mn, R.drawable.flag_my, R.drawable.flag_ne, R.drawable.flag_no, R.drawable.flag_ps, R.drawable.flag_fa, R.drawable.flag_pl, R.drawable.flag_pt, R.drawable.flag_pa, R.drawable.flag_ro, R.drawable.flag_ru, R.drawable.flag_sm, R.drawable.flag_gd, R.drawable.flag_sr, R.drawable.flag_st, R.drawable.flag_sn, R.drawable.flag_ur, R.drawable.flag_si, R.drawable.flag_sk, R.drawable.flag_sl, R.drawable.flag_so, R.drawable.flag_es, R.drawable.flag_su, R.drawable.flag_sw, R.drawable.flag_sv, R.drawable.flag_tg, R.drawable.flag_ta, R.drawable.flag_te, R.drawable.flag_th, R.drawable.flag_tr, R.drawable.flag_uk, R.drawable.flag_ur, R.drawable.flag_uz, R.drawable.flag_vi, R.drawable.flag_cy, R.drawable.flag_af, R.drawable.flag_yi, R.drawable.flag_yo, R.drawable.flag_zu};
    public static String[] All_languages = {"Afrikaans", "Albanian", "Amharic", "Arabic", "Armenian", "Azerbaijani", "Basque", "Belarusian", "Bengali", "Bosnian", "Bulgarian", "Catalan", "Cebuano", "Chichewa", "Chinese ,Simplified", "Chinese ,Traditional", "Corsican", "Croatian", "Czech", "Danish", "Dutch", "English", "Esperanto", "Estonian", "Filipino", "Finnish", "French", "Frisian", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian Creole", "Hausa", "Hawaiian", "Hebrew", "Hindi", "Hmong", "Hungarian", "Icelandic", "Igbo", "Indonesian", "Irish", "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Khmer", "Korean", "Kurdish ,Kurmanji", "Kyrgyz", "Lao", "Latin", "Latvian", "Lithuanian", "Luxembourgish", "Macedonian", "Malagasy", "Malay", "Malayalam", "Maltese", "Maori", "Marathi", "Mongolian", "Myanmar ,Burmese", "Nepali", "Norwegian", "Pashto", "Persian", "Polish", "Portuguese", "Punjabi", "Romanian", "Russian", "Samoan", "Scots Gaelic", "Serbian", "Sesotho", "Shona", "Sindhi", "Sinhala", "Slovak", "Slovenian", "Somali", "Spanish", "Sundanese", "Swahili", "Swedish", "Tajik", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", "Welsh", "Xhosa", "Yiddish", "Yoruba", "Zulu"};
    public static String[] languages_code = {"af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "ny", "zh", "zh-TW", "co", "hr", "cs", "da", "nl", "en", "eo", "et", "tl", "fi", "fr", "fy", "gl", "ka", "de", "el", "gu", "ht", "ha", "haw", "iw", "hi", "hmn", "hu", "is", "ig", "id", "ga", "it", "ja", "jw", "kn", "kk", "km", "ko", "ku", "ky", "lo", "la", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "ps", "fa", "pl", "pt", "pa", "ro", "ru", "sm", "gd", "sr", "st", "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tg", "ta", "te", "th", "tr", "uk", "ur", "uz", "vi", "cy", "xh", "yi", "yo", "zu"};
    public static String[] speach_code = {"af-ZA", "", "am-ET", "ar-SA", "hy-AM", "az-AZ", "eu-ES", "", "bn-BD", "", "bg-BG", "ca-ES", "", "", "zh", "zh-TW", "", "hr-HR", "cs-CZ", "da-DK", "nl-NL", "en-GB", "", "", "fil-PH", "fi-FI", "fr-FR", "", "gl-ES", "ka-GE", "de-DE", "el-GR", "gu-IN", "", "", "", "he-IL", "hi-IN", "", "hu-HU", "is-IS", "", "id-ID", "", "it-IT", "ja-JP", "", "kn-IN", "", "km-KH", "ko-KR", "", "", "lo-LA", "", "lv-LV", "lt-LT", "", "", "", "ms-MY", "ml-IN", "", "", "mr-IN", "", "", "ne-NP", "nb-NO", "", "fa-IR", "pl-PL", "pt-PT", "", "ro-RO", "ru-RU", "", "", "sr-RS", "", "", "", "si-LK", "sk-SK", "", "", "es-ES", "su-ID", "sw-TZ", "sv-SE", "", "ta-IN", "te-IN", "th-TH", "tr-TR", "uk-UA", "ur-PK", "", "vi-VN", "", "", "", "", "zu-ZA"};
}
