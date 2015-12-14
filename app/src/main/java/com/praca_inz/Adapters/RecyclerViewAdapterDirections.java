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

import com.praca_inz.ModelsAndDB.MapModels.Leg;
import com.praca_inz.ModelsAndDB.MapModels.Route;
import com.praca_inz.ModelsAndDB.RoutesModel;
import com.praca_inz.R;
import com.praca_inz.Utilities;

public class RecyclerViewAdapterDirections extends RecyclerView.Adapter<RowViewHolderDirections> {
    Context context;
    List<Route> itemsList;

    public RecyclerViewAdapterDirections(Context context, List<Route> itemsList) {
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
    public RowViewHolderDirections onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.single_directions, null);
        RowViewHolderDirections viewHolder = new RowViewHolderDirections(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderDirections rowViewHolder, final int position) {
        Route items = itemsList.get(position);

        rowViewHolder.summary.setText("przez: " + items.getSummary());
        rowViewHolder.distance.setText("  (" + items.getLegsList().get(0).getDistance().getText() + ")");
        rowViewHolder.time.setText(items.getLegsList().get(0).getDuration().getText());
    }
}

class RowViewHolderDirections extends RecyclerView.ViewHolder {
    public View view;
    public TextView summary;
    public TextView distance;
    public TextView time;

    public RowViewHolderDirections(View view) {
        super(view);
        this.summary = (TextView) view.findViewById(R.id.summaryTextView);
        this.distance = (TextView) view.findViewById(R.id.distanceTextView);
        this.time = (TextView) view.findViewById(R.id.timeTextView);
    }
}