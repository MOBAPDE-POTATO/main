package com.example.mikogarcia.findit;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int PASSWORD_LENGTH = 6;
    public static final String LOGIN_URL = "login.php";
    public static final String ERROR_TAG = "ERROR: ";

    // UI references.
    private EditText et_email;
    private EditText et_pass;
    private TextView createAccountView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccountView = (TextView)findViewById(R.id.createAccount);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
                i.setClass(getBaseContext(),RegisterActivity.class);
                startActivity(i);
            }

        });


    }

    public void attemptLogin() {

        //WHEN ERROR CHECKING IS GUD
        login();
    }

    public void checkError(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     *  Assuming that the credentials are all valid
     */
    public void login() {
        String email = et_email.getText().toString();
        String pass = et_pass.getText().toString();

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

    class LogInHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String pass = params[1];

            try {
                Document doc = Jsoup.connect(MainActivity.SERVER_IP+LOGIN_URL)
                        .data(Account.COLUMN_EMAIL, email)
                        .data(Account.COLUMN_PASSWORD, pass)
                        .post();

                Log.i("HTML", doc.body().toString());
                String result = doc.body().toString();

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.startsWith(ERROR_TAG)) {
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

