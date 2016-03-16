package com.example.mikogarcia.findit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Feature;
import com.example.mikogarcia.findit.model.Report;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {
    TextView tvItemName;
    TextView tvPlaceLost;
    TextView tvDateLost;
    TextView tvItemType;
    RecyclerView featuresRecycler;
    Button btnPossibleMatches;
    ViewFeatureAdapter viewfeatureAdapter;
    ReportAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        final int id = getIntent().getExtras().getInt("id");
        tvItemName = (TextView)findViewById(R.id.tvItemName);
        tvPlaceLost = (TextView)findViewById(R.id.tvPlaceLost);
        tvDateLost = (TextView)findViewById(R.id.tvDateLost);
        tvItemType = (TextView)findViewById(R.id.tvItemType);
        featuresRecycler = (RecyclerView)findViewById(R.id.viewReportsFeatureRecycler);
        btnPossibleMatches = (Button) findViewById(R.id.btnPossibleMatches);
        final ArrayList<Feature> features = new ArrayList<>();
        viewfeatureAdapter = new ViewFeatureAdapter(features);
        featuresRecycler.setAdapter(viewfeatureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));



        Log.i("TAG", id + " is the ID");
        Report report
                = adapter.getReport(id);

        tvItemName.setText(report.getItemName());
        tvPlaceLost.setText(report.getPlace());
        tvDateLost.setText(report.getDate().toString());
        if(report.getReportType() == 1)
            tvItemType.setText(report.getItemType());
        else if(report.getReportType() == 2)
            tvItemType.setText(report.getItemType());
        else if(report.getReportType() == 3)
            tvItemType.setText(report.getItemType());
        viewfeatureAdapter.setFeatureList(new ArrayList(report.getFeatures()));







        btnPossibleMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ViewPossibleMatchesActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });



    }
}
