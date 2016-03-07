package com.example.mikogarcia.findit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tvGreeting;
    TextView tvChangeUsername;

    final static int REQUEST_CODE_LOGIN = 0;
    final static String SP_KEY_USERNAME = "username";
    final static String KEY_SP_HAS_USERNAME = "has_username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGreeting = (TextView) findViewById(R.id.tv_username);
        tvChangeUsername = (TextView) findViewById(R.id.tv_change_username);
        tvChangeUsername.setText(Html.fromHtml("<u>Not you? Click here.</u>"));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString(SP_KEY_USERNAME, null);
        if(username != null){
            bindGreetingView(username);
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

    private void bindGreetingView(String username) {
        tvGreeting.setText("Greetings, " + username + "!");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String username = sharedPreferences.getString(SP_KEY_USERNAME, null);
            if(username != null){
                bindGreetingView(username);
            }
        }
    }
}