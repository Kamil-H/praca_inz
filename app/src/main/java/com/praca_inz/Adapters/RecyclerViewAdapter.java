package com.praca_inz.Adapters;

/**
 * Created by KamilH on 2015-10-23.
 */
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.praca_inz.Models.RoutesModel;
import com.praca_inz.R;
import com.praca_inz.Utilities;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RowViewHolder> {
    Context context;
    List<RoutesModel> itemsList;

    public RecyclerViewAdapter(Context context, List<RoutesModel> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.single_row, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolder rowViewHolder, final int position) {
        RoutesModel items = itemsList.get(position);

        String distance = String.format("%.2f", items.getDistance());

        rowViewHolder.date.setText(items.getDate());
        rowViewHolder.distance.setText(String.valueOf(distance + " km"));
        rowViewHolder.time.setText(Utilities.timeConversion(items.getTime()));
    }
}

class RowViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView date;
    public TextView distance;
    public TextView time;

    public RowViewHolder(View view) {
        super(view);
        this.date = (TextView) view.findViewById(R.id.dateTextView);
        this.distance = (TextView) view.findViewById(R.id.timeTextView);
        this.time = (TextView) view.findViewById(R.id.distanceTextView);
    }
}