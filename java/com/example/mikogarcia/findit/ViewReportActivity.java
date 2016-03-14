package com.example.mikogarcia.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Feature;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {
    TextView tvItemName;
    TextView tvPlaceLost;
    TextView tvDateLost;
    TextView tvItemType;
    RecyclerView featuresRecycler;
    Button btnDone;
    ViewFeatureAdapter viewfeatureAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        tvItemName = (TextView)findViewById(R.id.tvItemName);
        tvPlaceLost = (TextView)findViewById(R.id.tvPlaceLost);
        tvDateLost = (TextView)findViewById(R.id.tvDateLost);
        tvItemType = (TextView)findViewById(R.id.tvItemType);
        featuresRecycler = (RecyclerView)findViewById(R.id.viewReportsFeatureRecycler);
        btnDone = (Button) findViewById(R.id.btnDone);
        final ArrayList<Feature> features = new ArrayList<>();
        viewfeatureAdapter = new ViewFeatureAdapter(features);
        featuresRecycler.setAdapter(viewfeatureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        int id = getIntent().getExtras().getInt("id");


    }
}
