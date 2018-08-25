package com.felipe.vanhackchallenge.core.Domain;

import com.google.gson.annotations.SerializedName;

public class Wafer {

    public Wafer(String name, String language, String currency) {
        this.name = name;
        this.language = language;
        this.currency = currency;
    }

    @SerializedName("name")
    String name;

    @SerializedName("language")
    String language;

    @SerializedName("currency")
    String currency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Wafer{" +
                "name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
