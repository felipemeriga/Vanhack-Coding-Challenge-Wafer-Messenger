package com.felipe.vanhackchallange.Activities.Domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wafer {

    @SerializedName("name")
    String name;

    @SerializedName("languages")
    List<Object> languages;

    @SerializedName("currencies")
    List<Object> currencies;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Object> languages) {
        this.languages = languages;
    }

    public List<Object> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Object> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "Wafer{" +
                "name='" + name + '\'' +
                ", languages=" + languages +
                ", currencies=" + currencies +
                '}';
    }
}
