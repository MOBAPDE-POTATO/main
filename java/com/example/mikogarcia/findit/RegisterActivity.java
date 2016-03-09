package com.example.mikogarcia.findit;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Account;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final String REGISTER_URL = "register.php";

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button bRegister;
    private Button bCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        etFirstName = (EditText)findViewById(R.id.etFName);
        etLastName = (EditText)findViewById(R.id.etLName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
        bRegister = (Button)findViewById(R.id.bRegister);
        bCancel = (Button)findViewById(R.id.bCancel);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void attemptRegister() {
        String fname = etFirstName.getText().toString();
        String lname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        register(fname, lname, email, pass);
    }

    public void register(String fname, String lname, String email, String pass) {
        new RegisterHelper().execute(fname, lname, email, pass);
    }

    public void checkError(String s) {
        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    class RegisterHelper extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RegisterActivity.this, "Registering", "Please wait a moment", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String fname = params[0];
            String lname = params[1];
            String email = params[2];
            String pass = params[3];

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add(Account.COLUMN_FNAME, fname)
                        .add(Account.COLUMN_LNAME, lname)
                        .add(Account.COLUMN_EMAIL, email)
                        .add(Account.COLUMN_PASSWORD, pass)
                        .build();
                Request request = new Request.Builder()
                        .header("Content-type", "application/json")
                        .url(MainActivity.SERVER_IP + REGISTER_URL)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                String result = response.body().string();

                Log.i("HTML", result);
                return result;

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

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                editor.putString(MainActivity.SP_ACCOUNT_JSON_KEY, s);
                editor.commit();

                finish();
            }
        }
    }
}
