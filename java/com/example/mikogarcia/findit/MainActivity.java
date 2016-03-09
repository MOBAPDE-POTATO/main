package com.example.mikogarcia.findit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Account;
import com.example.mikogarcia.findit.model.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_IP = "http://112.206.29.72/FindIT-Web-Service/";  // MAKE SURE TO CHANGE THIS DEPENDING ON THE IP OF THE SERVER HOST
    public static final String ERROR_TAG = "ERROR: ";
    public static final String GET_REPORTS_URL = "getAccountLostReports.php";

    public final static int REQUEST_CODE_LOGIN = 0;
    public final static int REQUEST_CODE_REGISTER = 1;
    public final static String SP_ACCOUNT_JSON_KEY = "accJson";

    private RecyclerView reportsList;

    private Account account;
    private ReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportsList = (RecyclerView) findViewById(R.id.report_list);
        adapter = new ReportAdapter();

        adapter.setmOnItemClickListener(new ReportAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int id) {
                // TODO: 3/8/2016 OPEN VIEW REPORT
            }
        });

        reportsList.setAdapter(adapter);
        reportsList.setLayoutManager(new LinearLayoutManager(getBaseContext()));

//        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = sharedPreferences.getString(SP_ACCOUNT_JSON_KEY, null);
        if(json != null){
            try {
                this.account = new Account(new JSONObject(json));
                new ViewReportHelper().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            startActivityForResult(new Intent(getBaseContext(), LoginActivity.class), REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String json = sharedPreferences.getString(SP_ACCOUNT_JSON_KEY, null);

            if(json != null){
                try {
                    this.account = new Account(new JSONObject(json));
                    new ViewReportHelper().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadContent(String json) throws JSONException {
        JSONArray j_rep = new JSONObject(json).getJSONArray(Report.TABLE_NAME);
        ArrayList<Report> reports = new ArrayList<>();

        for(int i = 0; i < j_rep.length(); i++) {
            Report report = new Report(j_rep.getJSONObject(i));

            reports.add(report);
        }

        // TODO: 3/8/2016 SET ADAPTER'S LIST TO reports
        adapter.setReportList(reports);
    }

    public void checkError(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    class ViewReportHelper extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Fetching your Reports", "Please wait a moment", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody reqBody = new FormBody.Builder()
                        .add(Account.COLUMN_ID, String.valueOf(account.getId()))
                        .build();

                Request request = new Request.Builder()
                        .url(SERVER_IP + GET_REPORTS_URL)
                        .header("Content-type", "application/json")
                        .post(reqBody)
                        .build();

                Response response = client.newCall(request).execute();

                String jsonData = response.body().string();
                Log.i("HTML", jsonData);
                return jsonData;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            if(s.startsWith(MainActivity.ERROR_TAG)) {
                checkError(s);
            } else {
                try {
                    loadContent(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}