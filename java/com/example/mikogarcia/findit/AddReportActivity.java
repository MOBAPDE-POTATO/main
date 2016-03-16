package com.example.mikogarcia.findit;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Account;
import com.example.mikogarcia.findit.model.Feature;
import com.example.mikogarcia.findit.model.Report;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddReportActivity extends AppCompatActivity {

    public static final String ADD_REPORT_URL = "insertReport.php";

    EditText etItemName;
    EditText etPlaceLost;
    EditText etDateLost;

    RecyclerView featuresRecycler;
    FeatureAdapter featureAdapter;

    Button btnAddFeature;
    Button btnReport;
    Button btnCancel;

    EditText etDescription;
    Spinner spnrItemType;

    int acc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        final ArrayList<Feature> features = new ArrayList<>();

        etItemName = (EditText)findViewById(R.id.etItemName);
        etPlaceLost = (EditText)findViewById(R.id.etPlace);
        etDateLost = (EditText)findViewById(R.id.etDate);
        featuresRecycler = (RecyclerView)findViewById(R.id.featuresRecycler);
        btnAddFeature = (Button)findViewById(R.id.btnAddDescription);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnReport = (Button)findViewById(R.id.btnReport);
        etDescription = (EditText)findViewById(R.id.etFeature);
        spnrItemType = (Spinner)findViewById(R.id.spnrItemType);
        featureAdapter = new FeatureAdapter(features);
        acc_id = getIntent().getExtras().getInt(Account.COLUMN_ID, -1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrItemType.setAdapter(adapter);

        featuresRecycler.setAdapter(featureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        btnAddFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new AddFeatureDialog();
                dialogFragment.show(getFragmentManager(), "");
            }
        });

        btnAddFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddReport();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    public void onAddFeature(String description) {
        featureAdapter.addFeature(new Feature(description));
    }

    private void attemptAddReport(){
        // TODO: 3/16/2016 MARTINS MAGIC ERROR CHECKING
    }

    private void addReport(Report r) {
        new AddReportHelper().execute(r);
    }

    private void checkError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public class AddReportHelper extends AsyncTask<Report, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AddReportActivity.this, "Sending Report", "Please wait a moment", true);
        }

        @Override
        protected String doInBackground(Report... params) {
            Report r = params[0];

            OkHttpClient client = new OkHttpClient();
            FormBody.Builder bodyBuilder = new FormBody.Builder()
                    .add(Report.COLUMN_ITEM_NAME, r.getItemName())
                    .add(Report.COLUMN_ITEM_TYPE, String.valueOf(r.getItemType()))
                    .add(Report.COLUMN_REPORT_DATE, r.getDate().toString())
                    .add(Report.COLUMN_REPORT_TYPE, String.valueOf(r.getReportType()))
                    .add(Report.COLUMN_REPORT_PLACE, r.getPlace())
                    .add(Account.COLUMN_ID, String.valueOf(acc_id));

            for (Feature feat: r.getFeatures()) {
                bodyBuilder.add(Report.COLUMN_FEATURES, feat.getFeat());
            }

            RequestBody body = bodyBuilder.build();
            Request request = new Request.Builder()
                    .url(MainActivity.SERVER_IP+ADD_REPORT_URL)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            if(s.startsWith(MainActivity.ERROR_TAG)) {
                checkError(s);
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
