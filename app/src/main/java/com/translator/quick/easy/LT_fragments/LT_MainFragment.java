package com.translator.quick.easy.LT_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_db.LT_HelperDB;
import com.translator.quick.easy.LT_model.LT_LanguageModel;
import com.translator.quick.easy.LT_utils.LT_TranslatorConstants;
import com.translator.quick.easy.LT_utils.LT_CustomDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;


public class LT_MainFragment extends Fragment {
    Activity activity;
    ImageView changer;
    TextView destination_name;
    LT_CustomDialog dialog;
    Editable etext;
    LT_HelperDB LTHelperDB;
    RecyclerView lang_rev;
    String mText;
    int mpostion;
    ProgressDialog progressDialog;
    ScrollView scrollView;
    EditText search_lang;
    String selection;
    LT_LanguageModel source_data;
    ImageView source_flag;
    EditText source_input;
    TextView source_name;
    RelativeLayout source_picker;
    String speakMode;
    LT_LanguageModel target_data;
    ImageView target_flag;
    RelativeLayout target_picker;
    TextToSpeech textToSpeech;
    TextView translated_text;
    List<LT_LanguageModel> languages_data = new ArrayList();
    List<LT_LanguageModel> search_list1 = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);
        this.activity = getActivity();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        this.progressDialog = progressDialog;
        progressDialog.setMessage("Translating...");
        this.LTHelperDB = new LT_HelperDB(this.activity);
        this.source_flag = (ImageView) inflate.findViewById(R.id.flag_source);
        this.target_flag = (ImageView) inflate.findViewById(R.id.flag_final);
        this.source_name = (TextView) inflate.findViewById(R.id.source_name);
        this.destination_name = (TextView) inflate.findViewById(R.id.target_name);
        this.source_picker = (RelativeLayout) inflate.findViewById(R.id.source_picker);
        this.target_picker = (RelativeLayout) inflate.findViewById(R.id.target_picker);
        this.scrollView = (ScrollView) inflate.findViewById(R.id.scrollView);
        this.source_input = (EditText) inflate.findViewById(R.id.source_text);
        this.translated_text = (TextView) inflate.findViewById(R.id.translated_text);
        this.changer = (ImageView) inflate.findViewById(R.id.interchange);
        for (int i = 0; i < LT_TranslatorConstants.All_languages.length; i++) {
            LT_LanguageModel LTLanguageModel = new LT_LanguageModel();
            LTLanguageModel.setLanguage_name(LT_TranslatorConstants.All_languages[i]);
            LTLanguageModel.setLanguage_flag(LT_TranslatorConstants.flags[i]);
            LTLanguageModel.setLanguage_code(LT_TranslatorConstants.languages_code[i]);
            LTLanguageModel.setLanguage_speech_code(LT_TranslatorConstants.speach_code[i]);
            this.languages_data.add(LTLanguageModel);
        }
        LT_TranslatorConstants.sharedPreferences = getActivity().getSharedPreferences(LT_TranslatorConstants.FILE_NAME, 0);
        LT_TranslatorConstants.editor = LT_TranslatorConstants.sharedPreferences.edit();
        LT_TranslatorConstants.lnaguage1 = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_Language_1, LT_TranslatorConstants.lnaguage1);
        LT_TranslatorConstants.language2 = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_Language_2, LT_TranslatorConstants.language2);
        if (LT_TranslatorConstants.lnaguage1 != 0) {
            this.source_data = this.languages_data.get(LT_TranslatorConstants.lnaguage1);
            if (LT_TranslatorConstants.language2 != 0) {
                this.target_data = this.languages_data.get(LT_TranslatorConstants.language2);
            } else {
                this.target_data = this.languages_data.get(96);
            }
        } else {
            this.source_data = this.languages_data.get(21);
            this.target_data = this.languages_data.get(96);
        }
        this.source_name.setText(this.source_data.getLanguage_name());
        this.source_flag.setImageResource(this.source_data.getLanguage_flag());
        this.destination_name.setText(this.target_data.getLanguage_name());
        this.target_flag.setImageResource(this.target_data.getLanguage_flag());
        this.source_picker.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.1
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.selection = "source";
                LT_MainFragment.this.PickLanguage();
            }
        });
        this.target_picker.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.2
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.selection = "target";
                LT_MainFragment.this.PickLanguage();
            }
        });
        this.changer.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.3
            @Override 
            public void onClick(View view) {
                LT_LanguageModel LTLanguageModel2 = LT_MainFragment.this.source_data;
                LT_MainFragment LTMainFragment = LT_MainFragment.this;
                LTMainFragment.source_data = LTMainFragment.target_data;
                LT_MainFragment.this.target_data = LTLanguageModel2;
                LT_MainFragment.this.source_name.setText(LT_MainFragment.this.source_data.getLanguage_name());
                LT_MainFragment.this.source_flag.setImageResource(LT_MainFragment.this.source_data.getLanguage_flag());
                LT_MainFragment.this.destination_name.setText(LT_MainFragment.this.target_data.getLanguage_name());
                LT_MainFragment.this.target_flag.setImageResource(LT_MainFragment.this.target_data.getLanguage_flag());
                LT_TranslatorConstants.full_ad = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                if (LT_TranslatorConstants.full_ad == 3) {
                    LT_TranslatorConstants.full_ad = 0;
                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, 0);
                    LT_TranslatorConstants.editor.commit();
                    return;
                }
                LT_TranslatorConstants.full_ad++;
                LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                LT_TranslatorConstants.editor.commit();
            }
        });
        ((ImageView) inflate.findViewById(R.id.paste_source)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.4
            @Override 
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 11) {
                    LT_MainFragment.this.source_input.setText(((ClipboardManager) LT_MainFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).getText().toString());
                    LT_MainFragment LTMainFragment = LT_MainFragment.this;
                    LTMainFragment.mpostion = LTMainFragment.source_input.length();
                    LT_MainFragment LTMainFragment2 = LT_MainFragment.this;
                    LTMainFragment2.etext = LTMainFragment2.source_input.getText();
                    Selection.setSelection(LT_MainFragment.this.etext, LT_MainFragment.this.mpostion);
                    Toast.makeText(LT_MainFragment.this.getActivity(), "Text Pasted from Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) LT_MainFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboardManager.hasPrimaryClip()) {
                        try {
                            LT_MainFragment.this.source_input.setText(clipboardManager.getPrimaryClip().getItemAt(0).getText().toString());
                            LT_MainFragment.this.mpostion = LT_MainFragment.this.source_input.length();
                            LT_MainFragment.this.etext = LT_MainFragment.this.source_input.getText();
                            Selection.setSelection(LT_MainFragment.this.etext, LT_MainFragment.this.mpostion);
                            Toast.makeText(LT_MainFragment.this.getActivity(), "Text Pasted from Clipboard", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LT_MainFragment.this.activity, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LT_MainFragment.this.getActivity(), "Nothing to paste", Toast.LENGTH_SHORT).show();
                    }
                }
                LT_TranslatorConstants.full_ad = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                if (LT_TranslatorConstants.full_ad == 3) {
                    LT_TranslatorConstants.full_ad = 0;
                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, 0);
                    LT_TranslatorConstants.editor.commit();
                    return;
                }
                LT_TranslatorConstants.full_ad++;
                LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                LT_TranslatorConstants.editor.commit();
            }
        });
        ((ImageView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.5
            @Override 
            public void onClick(View view) {
                LT_TranslatorConstants.full_ad = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                if (LT_TranslatorConstants.full_ad == 3) {
                    LT_TranslatorConstants.full_ad = 0;
                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, 0);
                    LT_TranslatorConstants.editor.commit();
                } else {
                    LT_TranslatorConstants.full_ad++;
                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                    LT_TranslatorConstants.editor.commit();
                }
                if (LT_MainFragment.this.translated_text.getText().toString().length() != 0) {
                    if (Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager) LT_MainFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setText(LT_MainFragment.this.translated_text.getText().toString());
                    } else {
                        ((ClipboardManager) LT_MainFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copy", LT_MainFragment.this.translated_text.getText().toString()));
                    }
                    Toast.makeText(LT_MainFragment.this.activity, "Text Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LT_MainFragment.this.getActivity(), "Nothing to Copy", Toast.LENGTH_SHORT).show();
            }
        });
        ((ImageView) inflate.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.6
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.source_input.setText("");
                LT_MainFragment.this.translated_text.setText("");
                Toast.makeText(LT_MainFragment.this.activity, "Text Deleted.", Toast.LENGTH_SHORT).show();
                LT_TranslatorConstants.full_ad = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                if (LT_TranslatorConstants.full_ad == 3) {
                    LT_TranslatorConstants.full_ad = 0;
                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, 0);
                    LT_TranslatorConstants.editor.commit();
                    return;
                }
                LT_TranslatorConstants.full_ad++;
                LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_BIG, LT_TranslatorConstants.full_ad);
                LT_TranslatorConstants.editor.commit();
            }
        });
        ((ImageView) inflate.findViewById(R.id.play_source)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.7
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.speakMode = "source";
                LT_MainFragment LTMainFragment = LT_MainFragment.this;
                LTMainFragment.ChangeTextToSpeech(LTMainFragment.speakMode);
            }
        });
        ((ImageView) inflate.findViewById(R.id.play_target)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.8
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.speakMode = "target";
                LT_MainFragment LTMainFragment = LT_MainFragment.this;
                LTMainFragment.ChangeTextToSpeech(LTMainFragment.speakMode);
            }
        });
        ((ImageView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.9
            @Override 
            public void onClick(View view) {
                String charSequence = LT_MainFragment.this.translated_text.getText().toString();
                if (charSequence.isEmpty()) {
                    Toast.makeText(LT_MainFragment.this.activity, "Nothing to share", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Translated Text");
                intent.putExtra("android.intent.extra.TEXT", charSequence);
                LT_MainFragment.this.startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
        ((Button) inflate.findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.10
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.CloseKeyboard();
                if (LT_MainFragment.this.source_input.getText().length() == 0) {
                    Toast.makeText(LT_MainFragment.this.activity, "Write Some Text", Toast.LENGTH_SHORT).show();
                    return;
                }
                LT_MainFragment LTMainFragment = LT_MainFragment.this;
                if (!LTMainFragment.isOnline(LTMainFragment.getActivity())) {
                    Toast.makeText(LT_MainFragment.this.activity, "Internet Connection is Required to Perform Translation", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LT_MainFragment.this.progressDialog.show();
                    LT_MainFragment.this.DoTranslation();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        this.scrollView.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.11
            @Override 
            public void onClick(View view) {
                LT_MainFragment.this.OpenKeyBoard();
            }
        });
        try {
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.source_input.setText(arguments.getString("key"));
            }
        } catch (Exception e) {
            Toast.makeText(this.activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return inflate;
    }

    
    public void ChangeTextToSpeech(String str) {
        LT_TranslatorConstants.showornot = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
        if (LT_TranslatorConstants.showornot == 4) {
            LT_TranslatorConstants.showornot = 1;
            LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
            LT_TranslatorConstants.editor.commit();
        } else {
            LT_TranslatorConstants.showornot++;
            LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
            LT_TranslatorConstants.editor.commit();
        }
        if (str.equals("source")) {
            if (!this.source_input.getText().toString().isEmpty()) {
                this.textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() { // from class: com.translator.quick.easy.translator_fragments.-$$Lambda$iY15-SehKIhb-DP3lhuI3fXygx4
                    @Override // android.speech.tts.TextToSpeech.OnInitListener
                    public final void onInit(int i) {
                        LT_MainFragment.this.onInit(i);
                    }
                });
                onInit(0);
                return;
            }
            Toast.makeText(getActivity(), "Nothing to Speak.", Toast.LENGTH_SHORT).show();
        } else if (str.equals("target")) {
            if (!this.translated_text.getText().toString().isEmpty()) {
                this.textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() { // from class: com.translator.quick.easy.translator_fragments.-$$Lambda$iY15-SehKIhb-DP3lhuI3fXygx4
                    @Override // android.speech.tts.TextToSpeech.OnInitListener
                    public final void onInit(int i) {
                        LT_MainFragment.this.onInit(i);
                    }
                });
                onInit(0);
                return;
            }
            Toast.makeText(getActivity(), "Nothing to Speak.", Toast.LENGTH_SHORT).show();
        }
    }

    
    public boolean isOnline(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT > 26) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {
                    return true;
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }

    public void onInit(int i) {
        if (i == 0) {
            if (this.speakMode.equals("source")) {
                try {
                    this.textToSpeech.setLanguage(new Locale(this.source_data.getLanguage_name(), ""));
                    this.textToSpeech.speak(this.source_input.getText().toString(), 0, null);
                    return;
                } catch (Exception unused) {
                    Toast.makeText(getActivity(), "Text to speech feature is not available in target language.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            try {
                this.textToSpeech.setLanguage(new Locale(this.target_data.getLanguage_name(), ""));
                this.textToSpeech.speak(this.translated_text.getText().toString(), 0, null);
            } catch (Exception unused2) {
                Toast.makeText(getActivity(), "Text to speech feature is not available in target language.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String readJSON(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(str));
            if (execute != null) {
                if (execute.getStatusLine().getStatusCode() == 200) {
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        sb.append(readLine);
                    }
                    content.close();
                } else {
                    Toast.makeText(this.activity, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSON", e.getLocalizedMessage());
            sb.append("[\"ERROR\"]");
        }
        return sb.toString();
    }

    public void DoTranslation() throws UnsupportedEncodingException {
        LT_TranslatorConstants.showornot = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
        if (LT_TranslatorConstants.showornot == 4) {
            LT_TranslatorConstants.showornot = 1;
            LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
            LT_TranslatorConstants.editor.commit();
        } else {
            LT_TranslatorConstants.showornot++;
            LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_AD, LT_TranslatorConstants.showornot);
            LT_TranslatorConstants.editor.commit();
        }
        this.mText = URLEncoder.encode(this.source_input.getText().toString(), "UTF-8");
        new ReadLanguageTask().execute("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + this.source_data.getLanguage_code() + "&tl=" + this.target_data.getLanguage_code() + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + this.mText);
    }

    
    public void CloseKeyboard() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.source_input.getWindowToken(), 0);
    }

    
    public void OpenKeyBoard() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.source_input, 0);
    }

    
    public void PickLanguage() {
        this.languages_data.clear();
        this.search_list1.clear();
        for (int i = 0; i < LT_TranslatorConstants.All_languages.length; i++) {
            LT_LanguageModel LTLanguageModel = new LT_LanguageModel();
            LTLanguageModel.setLanguage_name(LT_TranslatorConstants.All_languages[i]);
            LTLanguageModel.setLanguage_flag(LT_TranslatorConstants.flags[i]);
            LTLanguageModel.setLanguage_code(LT_TranslatorConstants.languages_code[i]);
            LTLanguageModel.setLanguage_speech_code(LT_TranslatorConstants.speach_code[i]);
            this.languages_data.add(LTLanguageModel);
        }
        this.search_list1.addAll(this.languages_data);
        LT_CustomDialog LTCustomDialog = new LT_CustomDialog(this.activity);
        this.dialog = LTCustomDialog;
        LTCustomDialog.requestWindowFeature(1);
        this.dialog.getWindow().setLayout(-1, -1);
        this.dialog.show();
        RecyclerView recyclerView = (RecyclerView) this.dialog.findViewById(R.id.lang_rev);
        this.lang_rev = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.lang_rev.setAdapter(new LanguageAdapter(getContext()));
        this.search_lang = (EditText) this.dialog.findViewById(R.id.search_lang);
        isOnline(getActivity().getApplicationContext());
        this.search_lang.addTextChangedListener(new TextWatcher() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.12
            @Override 
            public void afterTextChanged(Editable editable) {
            }

            @Override 
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override 
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                LT_MainFragment.this.filter(charSequence.toString());
            }
        });
    }

    
    public void filter(String str) {
        str.toLowerCase();
        this.languages_data.clear();
        if (str.length() == 0) {
            this.languages_data.addAll(this.search_list1);
        } else {
            for (int i = 0; i < this.search_list1.size(); i++) {
                LT_LanguageModel LTLanguageModel = this.search_list1.get(i);
                if (LTLanguageModel.getLanguage_name().toLowerCase().contains(str)) {
                    this.languages_data.add(LTLanguageModel);
                }
            }
        }
        this.lang_rev.setAdapter(new LanguageAdapter(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPREFRENCE();
    }

    public void getPREFRENCE() {
        LT_TranslatorConstants.sharedPreferences = getActivity().getSharedPreferences(LT_TranslatorConstants.FILE_NAME, 0);
        LT_TranslatorConstants.editor = LT_TranslatorConstants.sharedPreferences.edit();
        LT_TranslatorConstants.source_color = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_SOURCE_COLOR, LT_TranslatorConstants.source_color);
        LT_TranslatorConstants.target_color = LT_TranslatorConstants.sharedPreferences.getInt(LT_TranslatorConstants.KEY_TARGET_COLOR, LT_TranslatorConstants.target_color);
        LT_TranslatorConstants.enable_history = LT_TranslatorConstants.sharedPreferences.getString(LT_TranslatorConstants.KEY_SAVE_HISTORY, LT_TranslatorConstants.enable_history);
        if (LT_TranslatorConstants.source_color != 0) {
            this.source_input.setTextColor(LT_TranslatorConstants.source_color);
        } else {
            LT_TranslatorConstants.source_color = ViewCompat.MEASURED_STATE_MASK;
            this.source_input.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
        if (LT_TranslatorConstants.target_color != 0) {
            this.translated_text.setTextColor(LT_TranslatorConstants.target_color);
            return;
        }
        LT_TranslatorConstants.target_color = ViewCompat.MEASURED_STATE_MASK;
        this.translated_text.setTextColor(ViewCompat.MEASURED_STATE_MASK);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if ((i == 1 || i == 101) && i2 == -1 && intent != null) {
            this.source_input.setText(intent.getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
            this.mpostion = this.source_input.length();
            Editable text = this.source_input.getText();
            this.etext = text;
            Selection.setSelection(text, this.mpostion);
            try {
                this.progressDialog.show();
                DoTranslation();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        TextToSpeech textToSpeech = this.textToSpeech;
        if (textToSpeech != null) {
            textToSpeech.stop();
            this.textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public void setData() {
        if (this.selection.equals("source")) {
            this.source_name.setText(this.source_data.getLanguage_name());
            this.source_flag.setImageResource(this.source_data.getLanguage_flag());
            return;
        }
        this.destination_name.setText(this.target_data.getLanguage_name());
        this.target_flag.setImageResource(this.target_data.getLanguage_flag());
    }

    
    private class ReadLanguageTask extends AsyncTask<String, Void, String> {
        private ReadLanguageTask() {
        }

        
        @Override 
        public String doInBackground(String... strArr) {
            return LT_MainFragment.this.readJSON(strArr[0]);
        }

        
        @Override 
        public void onPostExecute(String str) {
            JSONArray jSONArray = null;
            try {
                jSONArray = new JSONArray(str);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (str.equals("[\"ERROR\"]")) {
                return;
            }
            try {
                String str2 = "";
                for (int i = 0; i < new JSONArray(str).getJSONArray(0).length(); i++) {
                    str2 = str2 + jSONArray.getJSONArray(0).getJSONArray(i).getString(0);
                }
                LT_MainFragment.this.translated_text.setText(str2);
                LT_MainFragment.this.progressDialog.dismiss();
                LT_TranslatorConstants.enable_history = LT_TranslatorConstants.sharedPreferences.getString(LT_TranslatorConstants.KEY_SAVE_HISTORY, LT_TranslatorConstants.enable_history);
                if (LT_TranslatorConstants.enable_history.equals("") || LT_TranslatorConstants.enable_history == null || LT_TranslatorConstants.enable_history.equals("1")) {
                    LT_MainFragment.this.LTHelperDB.InsertRecord("Translation_Data", LT_MainFragment.this.source_data.getLanguage_name(), LT_MainFragment.this.source_input.getText().toString(), LT_MainFragment.this.target_data.getLanguage_name(), str2);
                }
            } catch (Exception unused) {
                LT_MainFragment.this.progressDialog.dismiss();
            }
        }
    }

    
    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
        Context context;
        ViewHolder holder;
        LT_LanguageModel language_model;

        LanguageAdapter(Context context) {
            this.context = context;
        }

        @Override 
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_item_lang, viewGroup, false));
        }

        @Override 
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            this.holder = viewHolder;
            try {
                this.language_model = LT_MainFragment.this.languages_data.get(i);
                viewHolder.source_langg.setText(this.language_model.getLanguage_name());
                viewHolder.flag.setImageResource(this.language_model.getLanguage_flag());
            } catch (Exception e) {
                e.printStackTrace();
                Context context = this.context;
                Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override 
        public int getItemCount() {
            return LT_MainFragment.this.languages_data.size();
        }

        
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView flag;
            TextView source_langg;

            ViewHolder(View view) {
                super(view);
                this.source_langg = (TextView) view.findViewById(R.id.name_txt);
                this.flag = (ImageView) view.findViewById(R.id.img);
                view.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.MainFragment.LanguageAdapter.ViewHolder.1
                    @Override 
                    public void onClick(View view2) {
                        if (LT_MainFragment.this.selection.equals("source")) {
                            try {
                                LT_MainFragment.this.source_data = LT_MainFragment.this.languages_data.get(ViewHolder.this.getAdapterPosition());
                                LT_MainFragment.this.dialog.dismiss();
                                LT_MainFragment.this.setData();
                                for (int i = 0; i < LT_MainFragment.this.search_list1.size(); i++) {
                                    if (LT_MainFragment.this.search_list1.get(i).getLanguage_name().equals(LT_MainFragment.this.source_data.getLanguage_name())) {
                                        LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_Language_1, i);
                                        LT_TranslatorConstants.editor.commit();
                                        return;
                                    }
                                }
                                return;
                            } catch (Exception e) {
                                Toast.makeText(LT_MainFragment.this.getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        try {
                            LT_MainFragment.this.target_data = LT_MainFragment.this.languages_data.get(ViewHolder.this.getAdapterPosition());
                            LT_MainFragment.this.dialog.dismiss();
                            LT_MainFragment.this.setData();
                            for (int i2 = 0; i2 < LT_MainFragment.this.search_list1.size(); i2++) {
                                if (LT_MainFragment.this.search_list1.get(i2).getLanguage_name().equals(LT_MainFragment.this.target_data.getLanguage_name())) {
                                    LT_TranslatorConstants.editor.putInt(LT_TranslatorConstants.KEY_Language_2, i2);
                                    LT_TranslatorConstants.editor.commit();
                                    return;
                                }
                            }
                        } catch (Exception e2) {
                            Toast.makeText(LT_MainFragment.this.getActivity(), "" + e2.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
