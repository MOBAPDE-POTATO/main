package com.example.mikogarcia.findit;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Account;
import com.example.mikogarcia.findit.model.Feature;
import com.example.mikogarcia.findit.model.Report;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    EditFeatureAdapter featureAdapter;

    Button btnAddFeature;
    Button btnReport;
    Button btnCancel;

    EditText etDescription;
    Spinner spnrItemType;

    int acc_id;
    int temp_id;
    SimpleDateFormat sqlFormatter, viewFormatter;

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
        //btnCancel = (Button)findViewById(R.id.btnCancel);
        btnReport = (Button)findViewById(R.id.btnReport);
        etDescription = (EditText)findViewById(R.id.etFeature);
        spnrItemType = (Spinner)findViewById(R.id.spnrItemType);
        featureAdapter = new EditFeatureAdapter(features);
        acc_id = getIntent().getExtras().getInt(Account.COLUMN_ID, -1);
        temp_id = 0;
        sqlFormatter = new SimpleDateFormat("yyyy-MM-dd");
        viewFormatter = new SimpleDateFormat("MM-dd-yyyy");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("File Lost Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrItemType.setAdapter(adapter);

        featureAdapter.setmOnItemClickListener(new EditFeatureAdapter.OnItemClickListener() {
            @Override
            /**
             * @params: id = feature's set id (since wala pa sa db, temp_id ginagamit)
             */
            public void onItemClick(int id) {
                // TODO: 3/16/2016 EDIT FEATURE WHEN I CLICK ON IT
                //AddFeatureDialog
                Bundle args = new Bundle();
                args.putInt("id", id);
                //f.setArguments(args);



            }
        });

        featuresRecycler.setAdapter(featureAdapter);
        featuresRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        etDateLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(AddReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);

                        etDateLost.setText(viewFormatter.format(cal.getTimeInMillis()));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                datePicker.show();
            }
        });

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
                attemptAddReport();

            }
        });

        /*btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });*/

    }



    public void onAddFeature(String description) {
        featureAdapter.addFeature(new Feature(temp_id, description));
        temp_id++;
    }
    public void onEditFeature(){}


    private void attemptAddReport(){
        String itemName = etItemName.getText().toString();
        String placeLost = etPlaceLost.getText().toString();
        String date = etDateLost.getText().toString();
        Date dateLost = null;
        try {
            dateLost = Date.valueOf(sqlFormatter.format(viewFormatter.parse(etDateLost.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int itemType = spnrItemType.getSelectedItemPosition()+1;
        ArrayList<Feature> features = featureAdapter.getFeatures();
        boolean checkItemName;
        boolean checkPlaceLost;
        boolean checkDate;

        if(!isEmpty(itemName)){
            checkItemName = true;
        }else{
            checkItemName = false;
            etItemName.setError("Field must be filled");
        }
        if(!isEmpty(placeLost)){
            checkPlaceLost = true;
        }else{
            checkPlaceLost = false;
            etPlaceLost.setError("Field must be filled");
        }
        if(isEmpty(date)){
            checkDate = false;
            etDateLost.setError("Date must be selected");
        }else{
            if(dateCheck(date)){
                checkDate = true;
            }else{
                etDateLost.setError("Date does not match date format: mm-dd-yyyy");
                checkDate = false;
            }
        }
        // TODO: 3/16/2016 MARTINS MAGIC ERROR CHECKING

        if(checkItemName == true && checkPlaceLost == true && checkDate == true){
            Report report = new Report(itemName, placeLost, dateLost, 1, itemType, features);
            addReport(report);
        }

    }

    private boolean isEmpty(String name) {
        if (name.length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Feature> getFeatures(){
      return featureAdapter.getFeatures();
    }

    private boolean dateCheck(String date){
        String datePattern = "^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
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
                String resp = response.body().string();

                return resp;
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
