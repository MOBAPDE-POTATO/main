package com.example.mikogarcia.findit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_IP = "http://192.168.1.102/FindIT-Web-Service/";  // MAKE SURE TO CHANGE THIS DEPENDING ON THE IP OF THE SERVER HOST
    public static final String ERROR_TAG = "ERROR: ";
    public static final String GET_REPORTS_URL = "getAccountLostReports.php";

    public final static int REQUEST_CODE_LOGIN = 0;
    public final static String SP_ACCOUNT_JSON_KEY = "accJson";
    public final static String KEY_SP_HAS_USERNAME = "has_username";

    private TextView tvGreeting;
    private TextView tvChangeUsername;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGreeting = (TextView) findViewById(R.id.tv_username);
        tvChangeUsername = (TextView) findViewById(R.id.tv_change_username);
        tvChangeUsername.setText(Html.fromHtml("<u>Not you? Click here.</u>"));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString(SP_ACCOUNT_JSON_KEY, null);
        if(username != null){
            try {
                bindGreetingView(username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            startActivityForResult(new Intent(getBaseContext(), LoginActivity.class), REQUEST_CODE_LOGIN);
        }

        tvChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), LoginActivity.class)
                        .putExtra(KEY_SP_HAS_USERNAME, true), REQUEST_CODE_LOGIN);
            }
        });
    }

    private void bindGreetingView(String accJson) throws JSONException {
        this.account = new Account(new JSONObject(accJson));

        tvGreeting.setText("Greetings, " + account.getName() + "!");

    }

    public void checkError(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String json = sharedPreferences.getString(SP_ACCOUNT_JSON_KEY, null);

            if(json != null){
                try {
                    bindGreetingView(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadContent(String json) throws JSONException {
        JSONArray j_rep = new JSONArray(json);
        ArrayList<Report> reports = new ArrayList<>();

        for(int i = 0; i < j_rep.length(); i++) {
            reports.add(new Report(j_rep.getJSONObject(i)));
        }

        // TODO: 3/8/2016 SET ADAPTER'S LIST TO reports
    }

    class ViewReportHelper extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Fetching your Reports", "Please wait a moment", true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(SERVER_IP + GET_REPORTS_URL)
                        .data(Account.COLUMN_ID, String.valueOf(account.getId()))
                        .get();

                String result = doc.body().text();

                return result;

            } catch (IOException e) {
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