package com.example.mikogarcia.findit;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Report;

import java.util.ArrayList;

/**
 * Created by Miko Garcia on 3/6/2016.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {

    private ArrayList<Report> reports;
    private OnItemClickListener mOnItemClickListener;

    public ReportAdapter(ArrayList<Report> reports) {
        this.reports = reports;
    }

    public ReportAdapter() {
        this.reports = new ArrayList<>();
    }


    @Override
    public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, null);

        return new ReportHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportHolder holder, int position) {
        final Report report = reports.get(position);

        holder.tvItem.setText(report.getItemName());
        holder.tvDate.setText(report.getReportDateString());
        holder.tvPlace.setText(report.getPlace());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(report.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void setmOnItemClickListener(OnItemClickListener m) {
        this.mOnItemClickListener = m;
    }

    public void addReport(Report report) {
        reports.add(report);
    }

    public void setReportList(ArrayList<Report> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    public class ReportHolder extends RecyclerView.ViewHolder {

        TextView tvItem, tvDate, tvPlace;
        View container;

        public ReportHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvPlace = (TextView) itemView.findViewById(R.id.tv_place);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int id);
    }
}
