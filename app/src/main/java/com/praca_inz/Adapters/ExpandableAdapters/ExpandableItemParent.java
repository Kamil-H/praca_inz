package com.praca_inz.Adapters.ExpandableAdapters;

/**
 * Created by KamilH on 2015-10-20.
 */
import java.util.List;

public class ExpandableItemParent {

    private final int year;
    private final int month;
    private final String cost;
    private boolean expanded;
    private List<Object> childObjects;

    public ExpandableItemParent(String cost, int year, int month) {
        this.cost = cost;
        this.year = year;
        this.month = month;
        this.expanded = false;
    }

    public String getCost() {
        return cost;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Object> getChildObjectList() {
        return childObjects;
    }

    public void setChildObjectList(List<Object> list) {
        this.childObjects = list;
    }
}
