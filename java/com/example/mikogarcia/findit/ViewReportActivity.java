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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {
    TextView tvItemName;
    TextView tvPlaceLost;
    TextView tvDateLost;
    TextView tvItemType;
    RecyclerView featuresRecycler;
    Button btnPossibleMatches;
    ViewFeatureAdapter viewfeatureAdapter;
    Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        String json = getIntent().getExtras().getString(MainActivity.REPORT_JSON_KEY);
        try {
            report = new Report(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        tvItemName.setText(report.getItemName());
        tvPlaceLost.setText(report.getPlace());
        tvDateLost.setText(report.getDate().toString());

        String itemType = "Others";
        switch(report.getItemType()) {
            case Report.ITEM_TYPE_ID: itemType = "ID"; break;
            case Report.ITEM_TYPE_GADGET: itemType = "Gadget"; break;
            case Report.ITEM_TYPE_MONEY: itemType = "Money"; break;
        }

        tvItemType.setText(itemType);

        viewfeatureAdapter.setFeatureList(new ArrayList(report.getFeatures()));
        btnPossibleMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getBaseContext(), ViewPossibleMatchesActivity.class);
                    i.putExtra(MainActivity.REPORT_JSON_KEY, report.toJSONString());

                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
