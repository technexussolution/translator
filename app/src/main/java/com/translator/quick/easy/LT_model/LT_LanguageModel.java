package com.translator.quick.easy.LT_model;


public class LT_LanguageModel {
    String language_code;
    int language_flag;
    String language_name;
    String language_speech_code;

    public String getLanguage_name() {
        return this.language_name;
    }

    public int getLanguage_flag() {
        return this.language_flag;
    }

    public void setLanguage_flag(int i) {
        this.language_flag = i;
    }

    public void setLanguage_name(String str) {
        this.language_name = str;
    }

    public String getLanguage_code() {
        return this.language_code;
    }

    public void setLanguage_code(String str) {
        this.language_code = str;
    }

    public String getLanguage_speech_code() {
        return this.language_speech_code;
    }

    public void setLanguage_speech_code(String str) {
        this.language_speech_code = str;
    }
}
