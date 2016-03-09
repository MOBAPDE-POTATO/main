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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String confirmPass = etConfirmPassword.getText().toString();
        boolean checkfname;
        boolean checklname;
        boolean checkEmail;
        boolean checkPassword;
        boolean checkConfirmPassword;
        if(!isEmpty(fname)){
            checkfname = true;
        }else{
            etFirstName.setError("Field must be filled");
            checkfname = false;
        }
        if(!isEmpty(lname)){
            checklname = true;
        }else{
            etLastName.setError("Field must be filled");
            checklname = false;
        }
        if(isEmpty(email)){
            etEmail.setError("Field must be filled");
            checkEmail = false;
        }else {
            if (!isValidEmail(email)) {
                etEmail.setError("Invalid Email");
                checkEmail = false;
            } else {
                checkEmail = true;
            }
        }
        if (!isPassLength(pass)) {
            etPassword.setError("Password must be at least 6 alphanumeric characters");
            checkPassword = false;
        }else{
            if(!isPassAlpha(pass)){
                etPassword.setError("Password must only contain alphanumeric characters");
                checkPassword = false;
            }else{
                checkPassword = true;
            }
        }
        if(isEmpty(confirmPass)){
            etConfirmPassword.setError("Field must be filled");
            checkConfirmPassword = false;
        }else {
            if (!isValidConfirmPassword(pass, confirmPass)) {
                etConfirmPassword.setError("Password does not match");
                checkConfirmPassword = false;
            } else {
                checkConfirmPassword = true;
            }
        }
        if (checkfname == true && checklname == true && checkEmail == true && checkPassword == true && checkConfirmPassword == true) {
            register(fname, lname, email, pass);
        }
    }

    public void register(String fname, String lname, String email, String pass) {
        new RegisterHelper().execute(fname, lname, email, pass);
    }
    private boolean isEmpty(String name) {
        if (name.length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPassLength(String pass) {
        if (pass.length() > 5) {
            return true;
        }
        return false;
    }
    private boolean isPassAlpha(String pass){
        String ALPHANUMERIC = "^[a-zA-Z0-9]*$";
        Pattern pattern = Pattern.compile(ALPHANUMERIC);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }
    private boolean isValidConfirmPassword(String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            return true;
        }
        return false;
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
