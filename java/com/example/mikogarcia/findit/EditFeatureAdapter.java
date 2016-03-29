package com.example.mikogarcia.findit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Feature;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/13/2016.
 */
public class EditFeatureAdapter extends RecyclerView.Adapter<EditFeatureAdapter.FeatureHolder>{

    private ArrayList<Feature> features;
    private OnItemClickListener mOnItemClickListener;

    public EditFeatureAdapter(ArrayList<Feature> features) {
        this.features = features;
    }

    public EditFeatureAdapter() {
        this.features = new ArrayList<>();
    }


    @Override
    public FeatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feature_item, null);

        return new FeatureHolder(v);
    }

    @Override
    public void onBindViewHolder(FeatureHolder holder, final int position) {
        final Feature feature = features.get(position);
        holder.tvDesc.setText(feature.getFeat());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });
        holder.feature_container.setOnClickListener(new View.OnClickListener() {
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
        notifyItemInserted(getItemCount());
    }
    public void editFeature(Feature feature) {
        //features.add(feature);
        for(Feature f:features){
            if(f.getId() == feature.getId()){
                f.setFeat(feature.getFeat());
                break;
            }
        }
        notifyItemChanged(feature.getId());
    }


    public void setFeatureList(ArrayList<Feature> features) {
        this.features = features;
        notifyDataSetChanged();

    }

    public void removeAt(int position) {
        features.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, features.size());
    }

    public Feature getFeature(int id) {
        for(Feature f: features) {
            if(f.getId() == id) {
                return f;
            }
        }

        return null;
    }

    public ArrayList<Feature> getFeatures(){
        return features;
    }

    public class FeatureHolder extends RecyclerView.ViewHolder {

        TextView tvDesc;
        Button btnDelete;
        View feature_container;

        public FeatureHolder(View itemView) {
            super(itemView);

            feature_container = itemView.findViewById(R.id.feature_container);
            btnDelete = (Button) itemView.findViewById(R.id.btn_delete_desc);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int id);
    }
}


