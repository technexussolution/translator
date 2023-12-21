package com.translator.quick.easy.LT_model;


public class LT_DBModel {
    int id;
    String source_language;
    String source_language_txt;
    String target_language;
    String target_language_txt;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getSource_language() {
        return this.source_language;
    }

    public void setSource_language(String str) {
        this.source_language = str;
    }

    public String getSource_language_txt() {
        return this.source_language_txt;
    }

    public void setSource_language_txt(String str) {
        this.source_language_txt = str;
    }

    public String getTarget_language() {
        return this.target_language;
    }

    public void setTarget_language(String str) {
        this.target_language = str;
    }

    public String getTarget_language_txt() {
        return this.target_language_txt;
    }

    public void setTarget_language_txt(String str) {
        this.target_language_txt = str;
    }
}
