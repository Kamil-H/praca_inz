package com.praca_inz.Adapters.ExpandableAdapters;

/**
 * Created by KamilH on 2015-10-20.
 */
public class ExpandableItemChild {

    private final String date;
    private final String cost, litres, price;

    public ExpandableItemChild(String date, String cost, String litres, String price) {
        this.date = date;
        this.cost = cost;
        this.litres = litres;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getCost() {
        return cost;
    }

    public String getLitres() {
        return litres;
    }

    public String getPrice() {
        return price;
    }
}
