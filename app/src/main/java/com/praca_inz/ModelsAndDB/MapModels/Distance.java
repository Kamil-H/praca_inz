package com.praca_inz.ModelsAndDB.MapModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KamilH on 2015-11-02.
 */
public class Distance {
    @SerializedName("value")
    private long value;
    @SerializedName("text")
    private String text;

    public Distance(long value, String text) {
        this.value = value;
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
}
