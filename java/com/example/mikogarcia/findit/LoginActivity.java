package com.example.mikogarcia.findit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikogarcia.findit.model.Account;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int PASSWORD_LENGTH = 6;
    public static final String LOGIN_URL = "login.php";

    // UI references.
    private EditText et_email;
    private EditText et_pass;
    private TextView createAccountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccountView = (TextView)findViewById(R.id.createAccount);
        et_email = (EditText) findViewById(R.id.loginEmail);
        et_pass = (EditText) findViewById(R.id.loginPassword);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        et_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        createAccountView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent();
                i.setClass(getBaseContext(), RegisterActivity.class);
                startActivityForResult(i, MainActivity.REQUEST_CODE_REGISTER);
            }

        });
    }

    public void attemptLogin() {
        String email = et_email.getText().toString();
        String pass = et_pass.getText().toString();
        boolean checkemail = false;
        boolean checkpass = false;

        // FIELDS ARE FILLED
        if(isEmpty(email) || isEmpty(pass)) {

            if (isEmpty(email)) {
                et_email.setError("Field must be filled");
            }
            if (isEmpty(pass)) {
                et_pass.setError("Field must be filled");
            }
        }else {
            // VALID EMAIL
            if (!isValidEmail(email)) {
                et_email.setError("Invalid Email address");
                checkemail = false;
            }else{
                checkemail = true;
            }
            // VALID PASSWORD
            if (!isPassLength(pass)) {
                et_pass.setError("Password must be at least 6 alphanumeric characters");
                checkpass = false;
            }else{
                if(!isPassAlpha(pass)){
                    et_pass.setError("Password must only contain alphanumeric characters");
                    checkpass = false;
                }else{
                    checkpass = true;
                }

            }

        }
        //WHEN ERROR CHECKING IS GUD
        if(checkemail == true && checkpass == true) {
            login(email, pass);
        }
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

    public void checkError(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     *  Assuming that the credentials are all valid
     */
    public void login(String email, String pass) {
        new LogInHelper().execute(email, pass);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > PASSWORD_LENGTH;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainActivity.REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    class LogInHelper extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "Logging In", "Please wait a moment", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String pass = params[1];

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add(Account.COLUMN_EMAIL, email)
                        .add(Account.COLUMN_PASSWORD, pass)
                        .build();
                Request request = new Request.Builder()
                        .header("Content-type", "application/json")
                        .url(MainActivity.SERVER_IP + LOGIN_URL)
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

                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
