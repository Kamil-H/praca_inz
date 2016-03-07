package com.praca_inz.Adapters.ExpandableAdapters;

/**
 * Created by KamilH on 2015-10-20.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.praca_inz.R;

public class ExpandableItemParentViewHolder extends RecyclerView.ViewHolder {

    private final TextView costTextView;
    private final TextView yearTextView;
    private final TextView monthTextView;
    private final ImageView ivParentExpand;
    private final View view;

    public ExpandableItemParentViewHolder(View view, TextView costTextView, TextView yearTextView, TextView monthTextView, ImageView ivParentExpand) {
        super(view);
        this.costTextView = costTextView;
        this.yearTextView = yearTextView;
        this.monthTextView = monthTextView;
        this.ivParentExpand = ivParentExpand;
        this.view = view;
        costTextView.setSelected(true);
    }

    public static ExpandableItemParentViewHolder newInstance(View view) {
        TextView costTextView = (TextView) view.findViewById(R.id.costTextView);
        TextView yearTextView = (TextView) view.findViewById(R.id.yearTextView);
        TextView monthTextView = (TextView) view.findViewById(R.id.monthTextView);
        ImageView ivParentExpand = (ImageView) view.findViewById(R.id.ivParentExpand);
        return new ExpandableItemParentViewHolder(view, costTextView, yearTextView, monthTextView, ivParentExpand);
    }

    public void setParentText(String cost, int year, int month) {
        costTextView.setText(cost);
        yearTextView.setText(String.valueOf(year));
        monthTextView.setText(String.valueOf(month));
    }

    public void setExpandButtonGone(boolean gone) {
        ivParentExpand.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    public void setExpandClickListener(View.OnClickListener onClickListener) {
        ivParentExpand.setOnClickListener(onClickListener);
    }
}