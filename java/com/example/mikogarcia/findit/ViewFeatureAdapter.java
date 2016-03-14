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
 * Created by Daniel on 3/14/2016.
 */
public class ViewFeatureAdapter extends RecyclerView.Adapter<ViewFeatureAdapter.ViewFeatureHolder>{
    private ArrayList<Feature> features;
    private OnItemClickListener mOnItemClickListener;

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
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
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
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFeatureList(ArrayList<Feature> features) {
        this.features = features;
        notifyDataSetChanged();

    }

    public class ViewFeatureHolder extends RecyclerView.ViewHolder {

        TextView tvDesc;
        Button btnDelete;
        View feature_container;

        public ViewFeatureHolder(View itemView) {
            super(itemView);

            feature_container = itemView.findViewById(R.id.feature_container);
            btnDelete = (Button) itemView.findViewById(R.id.btn_delete_desc);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int id);
    }
    public void removeAt(int position) {
        features.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, features.size());
    }
    public ArrayList<Feature> getFeatures(){
        return features;
    }
}
