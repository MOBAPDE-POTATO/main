package com.example.mikogarcia.findit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int PASSWORD_LENGTH = 6;

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

    public void login() {
        String email = et_email.getText().toString();
        String pass = et_pass.getText().toString();

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        editor.putString(MainActivity.SP_KEY_USERNAME, et_email.getText().toString());
        editor.commit();

        setResult(RESULT_OK);
        finish();
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > PASSWORD_LENGTH;
    }
}

