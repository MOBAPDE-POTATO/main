package com.example.mikogarcia.findit.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikogarcia.findit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miko Garcia on 3/17/2016.
 */
public class ViewFeatureAdapter extends RecyclerView.Adapter<ViewFeatureAdapter.ViewFeatureHolder> {
    private ArrayList<Feature> features;

    public ViewFeatureAdapter(ArrayList<Feature> features) {
        this.features = features;
    }

    public ViewFeatureAdapter() {
        this.features = new ArrayList<>();
    }

    @Override
    public ViewFeatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_feature_item, null);

        return new ViewFeatureHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewFeatureHolder holder, final int position) {
        final Feature feature = features.get(position);
        holder.tvDesc.setText(feature.getFeat());
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    public void setFeatureList(ArrayList<Feature> features) {
        this.features = features;
        notifyDataSetChanged();
    }

    public List<Feature> getFeatureList() {
        return this.features;
    }


    public class ViewFeatureHolder extends RecyclerView.ViewHolder {

        TextView tvDesc;
        View feature_container;

        public ViewFeatureHolder(View itemView) {
            super(itemView);

            feature_container = itemView.findViewById(R.id.feature_container);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }
}
