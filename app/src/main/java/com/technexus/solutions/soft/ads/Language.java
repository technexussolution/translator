package com.technexus.solutions.soft.ads;


public class Language {
    private String code;
    private String displayName;
    private String imageName;

    public Language(String str, String str2, String str3) {
        this.code = str;
        this.displayName = str2;
        this.imageName = str3;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String str) {
        this.imageName = str;
    }
}
