package com.technexus.solutions.soft.translator_fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.translator_db.HelperDB;
import com.technexus.solutions.soft.translator_model.LanguageModel;
import com.technexus.solutions.soft.translator_utils.TranslatorConstants;
import com.technexus.solutions.soft.translator_utils.TranslatorCustomDialog;

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


public class VoiceFragment extends Fragment {
    Activity activity;
    ImageView changer;
    Context context;
    TextView destination_name;
    TranslatorCustomDialog dialog;
    DrawerLayout drawer;
    ImageView drawer_icon;
    Editable etext;
    HelperDB helperDB;
    RecyclerView lang_rev;
    String mText;
    int mpostion;
    ProgressBar progressBar;
    ScrollView scrollView;
    EditText search_lang;
    String selection;
    LanguageModel source_data;
    ImageView source_flag;
    EditText source_input;
    TextView source_name;
    RelativeLayout source_picker;
    String speakMode;
    LanguageModel target_data;
    ImageView target_flag;
    RelativeLayout target_picker;
    TextToSpeech textToSpeech;
    TextView translated_text;
    List<LanguageModel> languages_data = new ArrayList();
    List<LanguageModel> search_list1 = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_voice, viewGroup, false);
        this.activity = getActivity();
        this.helperDB = new HelperDB(this.activity);
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
        this.progressBar = (ProgressBar) inflate.findViewById(R.id.progress_circular);
        for (int i = 0; i < TranslatorConstants.All_languages.length; i++) {
            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguage_name(TranslatorConstants.All_languages[i]);
            languageModel.setLanguage_flag(TranslatorConstants.flags[i]);
            languageModel.setLanguage_code(TranslatorConstants.languages_code[i]);
            languageModel.setLanguage_speech_code(TranslatorConstants.speach_code[i]);
            this.languages_data.add(languageModel);
        }
        TranslatorConstants.sharedPreferences = getActivity().getSharedPreferences(TranslatorConstants.FILE_NAME, 0);
        TranslatorConstants.editor = TranslatorConstants.sharedPreferences.edit();
        TranslatorConstants.lnaguage1 = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_Language_1, TranslatorConstants.lnaguage1);
        TranslatorConstants.language2 = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_Language_2, TranslatorConstants.language2);
        if (TranslatorConstants.lnaguage1 != 0) {
            this.source_data = this.languages_data.get(TranslatorConstants.lnaguage1);
            if (TranslatorConstants.language2 != 0) {
                this.target_data = this.languages_data.get(TranslatorConstants.language2);
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
        this.source_picker.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                VoiceFragment.this.selection = "source";
                VoiceFragment.this.PickLanguage();
            }
        });
        this.target_picker.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                VoiceFragment.this.selection = "target";
                VoiceFragment.this.PickLanguage();
            }
        });
        this.changer.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                LanguageModel languageModel2 = VoiceFragment.this.source_data;
                VoiceFragment voiceFragment = VoiceFragment.this;
                voiceFragment.source_data = voiceFragment.target_data;
                VoiceFragment.this.target_data = languageModel2;
                VoiceFragment.this.source_name.setText(VoiceFragment.this.source_data.getLanguage_name());
                VoiceFragment.this.source_flag.setImageResource(VoiceFragment.this.source_data.getLanguage_flag());
                VoiceFragment.this.destination_name.setText(VoiceFragment.this.target_data.getLanguage_name());
                VoiceFragment.this.target_flag.setImageResource(VoiceFragment.this.target_data.getLanguage_flag());
                TranslatorConstants.full_ad = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                if (TranslatorConstants.full_ad == 3) {
                    TranslatorConstants.full_ad = 0;
                    TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, 0);
                    TranslatorConstants.editor.commit();
                    return;
                }
                TranslatorConstants.full_ad++;
                TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                TranslatorConstants.editor.commit();
            }
        });
        ((ImageView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                TranslatorConstants.full_ad = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                if (TranslatorConstants.full_ad == 3) {
                    TranslatorConstants.full_ad = 0;
                    TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, 0);
                    TranslatorConstants.editor.commit();
                } else {
                    TranslatorConstants.full_ad++;
                    TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                    TranslatorConstants.editor.commit();
                }
                if (VoiceFragment.this.translated_text.getText().toString().length() != 0) {
                    if (Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager) VoiceFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setText(VoiceFragment.this.translated_text.getText().toString());
                    } else {
                        ((ClipboardManager) VoiceFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copy", VoiceFragment.this.translated_text.getText().toString()));
                    }
                    Toast.makeText(VoiceFragment.this.activity, "Text Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(VoiceFragment.this.getActivity(), "Nothing to Copy", Toast.LENGTH_SHORT).show();
            }
        });
        ((ImageView) inflate.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                VoiceFragment.this.source_input.setText("");
                VoiceFragment.this.translated_text.setText("");
                Toast.makeText(VoiceFragment.this.activity, "Text Deleted.", Toast.LENGTH_SHORT).show();
                TranslatorConstants.full_ad = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                if (TranslatorConstants.full_ad == 3) {
                    TranslatorConstants.full_ad = 0;
                    TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, 0);
                    TranslatorConstants.editor.commit();
                    return;
                }
                TranslatorConstants.full_ad++;
                TranslatorConstants.editor.putInt(TranslatorConstants.KEY_BIG, TranslatorConstants.full_ad);
                TranslatorConstants.editor.commit();
            }
        });
        ((Button) inflate.findViewById(R.id.SpechToText)).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (VoiceFragment.this.source_data.getLanguage_speech_code() != "") {
                    VoiceFragment.this.ChangeSpeechToText();
                } else {
                    Toast.makeText(VoiceFragment.this.activity, "Speech to text feature is not available in selected language.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((ImageView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_fragments.VoiceFragment.7
            @Override 
            public void onClick(View view) {
                String charSequence = VoiceFragment.this.translated_text.getText().toString();
                if (charSequence.isEmpty()) {
                    Toast.makeText(VoiceFragment.this.activity, "Nothing to share", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Translated Text");
                intent.putExtra("android.intent.extra.TEXT", charSequence);
                VoiceFragment.this.startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
        this.scrollView.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_fragments.VoiceFragment.8
            @Override 
            public void onClick(View view) {
                VoiceFragment.this.OpenKeyBoard();
            }
        });
        return inflate;
    }

    
    public void ChangeSpeechToText() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", this.source_data.getLanguage_name());
        intent.putExtra("android.speech.extra.LANGUAGE", this.source_data.getLanguage_speech_code());
        intent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", this.source_data.getLanguage_name());
        try {
            startActivityForResult(intent, 1);
            this.source_input.setText("");
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline(Context context) {
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
        TranslatorConstants.showornot = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_AD, TranslatorConstants.showornot);
        if (TranslatorConstants.showornot == 4) {
            TranslatorConstants.showornot = 1;
            TranslatorConstants.editor.putInt(TranslatorConstants.KEY_AD, TranslatorConstants.showornot);
            TranslatorConstants.editor.commit();
        } else {
            TranslatorConstants.showornot++;
            TranslatorConstants.editor.putInt(TranslatorConstants.KEY_AD, TranslatorConstants.showornot);
            TranslatorConstants.editor.commit();
        }
        this.mText = URLEncoder.encode(this.source_input.getText().toString(), "UTF-8");
        new ReadLanguageTask().execute("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + this.source_data.getLanguage_code() + "&tl=" + this.target_data.getLanguage_code() + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + this.mText);
    }

    private void CloseKeyboard() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.source_input.getWindowToken(), 0);
    }

    
    public void OpenKeyBoard() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.source_input, 0);
    }

    
    public void PickLanguage() {
        this.languages_data.clear();
        this.search_list1.clear();
        for (int i = 0; i < TranslatorConstants.All_languages.length; i++) {
            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguage_name(TranslatorConstants.All_languages[i]);
            languageModel.setLanguage_flag(TranslatorConstants.flags[i]);
            languageModel.setLanguage_code(TranslatorConstants.languages_code[i]);
            languageModel.setLanguage_speech_code(TranslatorConstants.speach_code[i]);
            this.languages_data.add(languageModel);
        }
        this.search_list1.addAll(this.languages_data);
        TranslatorCustomDialog translatorCustomDialog = new TranslatorCustomDialog(this.activity);
        this.dialog = translatorCustomDialog;
        translatorCustomDialog.requestWindowFeature(1);
        this.dialog.getWindow().setLayout(-1, -2);
        this.dialog.show();
        RecyclerView recyclerView = (RecyclerView) this.dialog.findViewById(R.id.lang_rev);
        this.lang_rev = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.lang_rev.setAdapter(new Language_Adapter(getContext()));
        this.search_lang = (EditText) this.dialog.findViewById(R.id.search_lang);
        isOnline(getActivity().getApplicationContext());
        this.search_lang.addTextChangedListener(new TextWatcher() { // from class: com.technexus.solutions.soft.translator_fragments.VoiceFragment.9
            @Override 
            public void afterTextChanged(Editable editable) {
            }

            @Override 
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override 
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                VoiceFragment.this.filter(charSequence.toString());
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
                LanguageModel languageModel = this.search_list1.get(i);
                if (languageModel.getLanguage_name().toLowerCase().contains(str)) {
                    this.languages_data.add(languageModel);
                }
            }
        }
        this.lang_rev.setAdapter(new Language_Adapter(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPREFRENCE();
    }

    public void getPREFRENCE() {
        TranslatorConstants.sharedPreferences = getActivity().getSharedPreferences(TranslatorConstants.FILE_NAME, 0);
        TranslatorConstants.editor = TranslatorConstants.sharedPreferences.edit();
        TranslatorConstants.source_color = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_SOURCE_COLOR, TranslatorConstants.source_color);
        TranslatorConstants.target_color = TranslatorConstants.sharedPreferences.getInt(TranslatorConstants.KEY_TARGET_COLOR, TranslatorConstants.target_color);
        TranslatorConstants.enable_history = TranslatorConstants.sharedPreferences.getString(TranslatorConstants.KEY_SAVE_HISTORY, TranslatorConstants.enable_history);
        if (TranslatorConstants.source_color != 0) {
            this.source_input.setTextColor(TranslatorConstants.source_color);
        } else {
            TranslatorConstants.source_color = ViewCompat.MEASURED_STATE_MASK;
            this.source_input.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
        if (TranslatorConstants.target_color != 0) {
            this.translated_text.setTextColor(TranslatorConstants.target_color);
            return;
        }
        TranslatorConstants.target_color = ViewCompat.MEASURED_STATE_MASK;
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
                this.progressBar.setVisibility(View.VISIBLE);
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
            return VoiceFragment.this.readJSON(strArr[0]);
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
                VoiceFragment.this.translated_text.setText(str2);
                VoiceFragment.this.progressBar.setVisibility(View.GONE);
                TranslatorConstants.enable_history = TranslatorConstants.sharedPreferences.getString(TranslatorConstants.KEY_SAVE_HISTORY, TranslatorConstants.enable_history);
                if (TranslatorConstants.enable_history.equals("") || TranslatorConstants.enable_history == null || TranslatorConstants.enable_history.equals("1")) {
                    VoiceFragment.this.helperDB.InsertRecord("Translation_Data", VoiceFragment.this.source_data.getLanguage_name(), VoiceFragment.this.source_input.getText().toString(), VoiceFragment.this.target_data.getLanguage_name(), str2);
                }
            } catch (Exception unused) {
                VoiceFragment.this.progressBar.setVisibility(View.GONE);
            }
        }
    }

    
    class Language_Adapter extends RecyclerView.Adapter<Language_Adapter.ViewHolder> {
        Context context;
        ViewHolder holder;
        LanguageModel language_model;

        Language_Adapter(Context context) {
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
                this.language_model = VoiceFragment.this.languages_data.get(i);
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
            return VoiceFragment.this.languages_data.size();
        }

        
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView flag;
            TextView source_langg;

            ViewHolder(View view) {
                super(view);
                this.source_langg = (TextView) view.findViewById(R.id.name_txt);
                this.flag = (ImageView) view.findViewById(R.id.img);
                view.setOnClickListener(new View.OnClickListener() { // from class: com.technexus.solutions.soft.translator_fragments.VoiceFragment.Language_Adapter.ViewHolder.1
                    @Override 
                    public void onClick(View view2) {
                        if (VoiceFragment.this.selection.equals("source")) {
                            try {
                                VoiceFragment.this.source_data = VoiceFragment.this.languages_data.get(ViewHolder.this.getAdapterPosition());
                                VoiceFragment.this.dialog.dismiss();
                                VoiceFragment.this.setData();
                                for (int i = 0; i < VoiceFragment.this.search_list1.size(); i++) {
                                    if (VoiceFragment.this.search_list1.get(i).getLanguage_name().equals(VoiceFragment.this.source_data.getLanguage_name())) {
                                        TranslatorConstants.editor.putInt(TranslatorConstants.KEY_Language_1, i);
                                        TranslatorConstants.editor.commit();
                                        return;
                                    }
                                }
                                return;
                            } catch (Exception e) {
                                Toast.makeText(VoiceFragment.this.getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        try {
                            VoiceFragment.this.target_data = VoiceFragment.this.languages_data.get(ViewHolder.this.getAdapterPosition());
                            VoiceFragment.this.dialog.dismiss();
                            VoiceFragment.this.setData();
                            for (int i2 = 0; i2 < VoiceFragment.this.search_list1.size(); i2++) {
                                if (VoiceFragment.this.search_list1.get(i2).getLanguage_name().equals(VoiceFragment.this.target_data.getLanguage_name())) {
                                    TranslatorConstants.editor.putInt(TranslatorConstants.KEY_Language_2, i2);
                                    TranslatorConstants.editor.commit();
                                    return;
                                }
                            }
                        } catch (Exception e2) {
                            Toast.makeText(VoiceFragment.this.getActivity(), "" + e2.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
