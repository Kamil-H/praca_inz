package com.praca_inz.Adapters.ExpandableAdapters;

/**
 * Created by KamilH on 2015-10-20.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.praca_inz.R;
import com.praca_inz.Utilities;

public class ExpandableItemChildViewHolder extends RecyclerView.ViewHolder {

    private final TextView dateTextView, costTextView, litresTextView, priceTextView;
    private final View parent;

    public ExpandableItemChildViewHolder(View parent, TextView dateTextView, TextView costTextView, TextView litresTextView, TextView priceTextView) {
        super(parent);
        this.dateTextView = dateTextView;
        this.parent = parent;
        this.costTextView = costTextView;
        this.litresTextView = litresTextView;
        this.priceTextView = priceTextView;
    }

    public static ExpandableItemChildViewHolder newInstance(View parent) {
        TextView dateTextView = (TextView) parent.findViewById(R.id.dateTextView);
        TextView costTextView = (TextView) parent.findViewById(R.id.costTextView);
        TextView litresTextView = (TextView) parent.findViewById(R.id.litresTextView);
        TextView priceTextView = (TextView) parent.findViewById(R.id.priceTextView);
        return new ExpandableItemChildViewHolder(parent, dateTextView, costTextView, litresTextView, priceTextView);
    }

    public void setChildText(String date, String cost, String litres, String price) {
        dateTextView.setText(date);
        costTextView.setText(cost);
        litresTextView.setText(litres);
        priceTextView.setText(price);
    }
}