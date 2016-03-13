package com.example.mikogarcia.findit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mikogarcia.findit.model.Feature;
import com.example.mikogarcia.findit.model.FeatureAdapter;

public class AddReportActivity extends AppCompatActivity {
    EditText etItemName;
    EditText etPlaceLost;
    EditText etDateLost;
    RecyclerView featuresRecycler;
    Button btnAddFeature;
    Button btnRegister;
    Button btnCancel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        etItemName = (EditText)findViewById(R.id.etItemName);
        etPlaceLost = (EditText)findViewById(R.id.etItemName);
        etDateLost = (EditText)findViewById(R.id.etItemName);
        featuresRecycler = (RecyclerView)findViewById(R.id.featuresRecycler);
        btnAddFeature = (Button)findViewById(R.id.btnAddDescription);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnRegister = (Button)findViewById(R.id.bRegister);

        final FeatureAdapter featureAdapter = new FeatureAdapter();
        featuresRecycler.setAdapter(featureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        btnAddFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getBaseContext(), R.layout.dialog_add_feature)
                            .setTitle("Add Description")
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText etDescription = (EditText) findViewById(R.id.etFeature);
                                    featureAdapter.addFeature(new Feature(etDescription.getText().toString()));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

            }
        });





    }

}
