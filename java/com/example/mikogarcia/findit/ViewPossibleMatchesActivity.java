package com.example.mikogarcia.findit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ViewPossibleMatchesActivity extends AppCompatActivity {

    public static final String GET_MATCHES_URL = "possibleMatches.php";

    RecyclerView matchesRecycler;
    ReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_possible_matches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Possible Matches");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getExtras().getInt(Report.COLUMN_ID);

        matchesRecycler = (RecyclerView) findViewById(R.id.matchesRecycler);
        adapter = new ReportAdapter();
        adapter.setmOnItemClickListener(new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Report report) {
                try {
                    String json = report.toJSONString();

                    Log.i("JSON", json);

                    Intent i = new Intent();

                    i.setClass(getBaseContext(), ViewMatchActivity.class);
                    i.putExtra(MainActivity.REPORT_JSON_KEY, json);

                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        matchesRecycler.setAdapter(adapter);
        matchesRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        new PossibleMatchHelper().execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: finish(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void loadContent(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        ArrayList<Report> reports = new ArrayList<>();

        try {
            JSONArray j_rep = obj.getJSONArray(Report.TABLE_NAME);

            for (int i = 0; i < j_rep.length(); i++) {
                Report report = new Report(j_rep.getJSONObject(i));

                reports.add(report);
            }
        } catch (JSONException e) {
            JSONObject rep = obj.getJSONObject(Report.TABLE_NAME);

            reports.add(new Report(rep));
        }

        adapter.setReportList(reports);
    }

    public void checkError(String s) {
        Toast.makeText(ViewPossibleMatchesActivity.this, s, Toast.LENGTH_LONG).show();
    }

    public class PossibleMatchHelper extends AsyncTask<Integer, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ViewPossibleMatchesActivity.this, "Getting Matches", "Looking...", true);
        }

        @Override
        protected String doInBackground(Integer... params) {
            int id = params[0];

            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add(Report.COLUMN_ID, String.valueOf(id))
                    .build();

            Request request = new Request.Builder()
                    .url(MainActivity.SERVER_IP + GET_MATCHES_URL)
                    .header("Content-type", "application/json")
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();

                return json;
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
                try {
                    Log.i("WEB-SERVER", s);
                    loadContent(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
