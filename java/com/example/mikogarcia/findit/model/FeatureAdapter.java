package com.example.mikogarcia.findit.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mikogarcia.findit.R;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/13/2016.
 */
public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureHolder>{


    private ArrayList<Feature> features;
    private OnItemClickListener mOnItemClickListener;

    public FeatureAdapter(ArrayList<Feature> features) {
        this.features = features;
    }

    public FeatureAdapter() {
        this.features = new ArrayList<>();
    }


    @Override
    public FeatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feature_item, null);

        return new FeatureHolder(v);
    }

    @Override
    public void onBindViewHolder(FeatureHolder holder, int position) {
        final Feature feature = features.get(position);

        holder.tvDesc.setText(feature.getFeat());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(feature.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    public void setmOnItemClickListener(OnItemClickListener m) {
        this.mOnItemClickListener = m;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public void setFeatureList(ArrayList<Feature> features) {
        this.features = features;
        notifyDataSetChanged();
    }

    public class FeatureHolder extends RecyclerView.ViewHolder {

        TextView tvDesc;
        View container;

        public FeatureHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int id);
    }
}


