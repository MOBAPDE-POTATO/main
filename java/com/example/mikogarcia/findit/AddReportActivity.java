package com.example.mikogarcia.findit;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.mikogarcia.findit.model.Feature;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddReportActivity extends AppCompatActivity {
    EditText etItemName;
    EditText etPlaceLost;
    EditText etDateLost;
    RecyclerView featuresRecycler;
    Button btnAddFeature;
    Button btnReport;
    Button btnCancel;
    EditText etDescription;
    FeatureAdapter featureAdapter;
    Spinner spnrItemType;
    RadioButton rBtnLost;
    RadioButton rBtnFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        etItemName = (EditText)findViewById(R.id.etItemName);
        etPlaceLost = (EditText)findViewById(R.id.etPlace);
        etDateLost = (EditText)findViewById(R.id.etDate);
        featuresRecycler = (RecyclerView)findViewById(R.id.featuresRecycler);
        btnAddFeature = (Button)findViewById(R.id.btnAddDescription);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnReport = (Button)findViewById(R.id.btnReport);
        etDescription = (EditText)findViewById(R.id.etFeature);
        spnrItemType = (Spinner)findViewById(R.id.spnrItemType);
        rBtnLost = (RadioButton)findViewById(R.id.rBtnLost);
        rBtnFound = (RadioButton)findViewById(R.id.rBtnFound);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrItemType.setAdapter(adapter);

        final ArrayList<Feature> features = new ArrayList<>();
        featureAdapter = new FeatureAdapter(features);
        featuresRecycler.setAdapter(featureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        btnAddFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new AddFeatureDialog();
                dialogFragment.show(getFragmentManager(), "");

            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(MainActivity.KEY_ITEM_NAME, etItemName.getText().toString());
                result.putExtra(MainActivity.KEY_PLACE_LOST, etPlaceLost.getText().toString());
                SimpleDateFormat df = new SimpleDateFormat("MM/DD/YYYY");
                long dateNum = Long.parseLong(null);
                try {
                    Date date = df.parse(etDateLost.getText().toString());
                    dateNum = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                result.putExtra(MainActivity.KEY_DATE_LOST, dateNum);
                if(rBtnLost.isChecked())
                    result.putExtra(MainActivity.KEY_REPORT_TYPE, 1);
                else
                    result.putExtra(MainActivity.KEY_REPORT_TYPE, 2);
                result.putExtra(MainActivity.KEY_ITEM_TYPE, spnrItemType.getSelectedItemId()+1);
                result.putParcelableArrayListExtra(MainActivity.KEY_FEATURES, featureAdapter.getFeatures());
                setResult(RESULT_OK, result);
                //report stuff
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cancel stuff
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    public void onAddFeature(String description) {
        featureAdapter.addFeature(new Feature(description));
    }
}
