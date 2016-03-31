package com.example.mikogarcia.findit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Feature;
import com.example.mikogarcia.findit.model.Report;
import com.example.mikogarcia.findit.model.ViewFeatureAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewMatchActivity extends ToolbarActivity {
    TextView tvItemName;
    TextView tvPlaceLost;
    TextView tvDateLost;
    TextView tvItemType;
    RecyclerView featuresRecycler;
    ViewFeatureAdapter viewfeatureAdapter;
    Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_match);

        String json = getIntent().getExtras().getString(MainActivity.REPORT_JSON_KEY);
        try {
            report = new Report(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setTitle("Match Item");

        tvItemName = (TextView)findViewById(R.id.tvItemName);
        tvPlaceLost = (TextView)findViewById(R.id.tvPlaceLost);
        tvDateLost = (TextView)findViewById(R.id.tvDateLost);
        tvItemType = (TextView)findViewById(R.id.tvItemType);
        featuresRecycler = (RecyclerView)findViewById(R.id.viewReportsFeatureRecycler);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
