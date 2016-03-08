package com.example.mikogarcia.findit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.mikogarcia.findit.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_IP = "http://192.168.1.102/FindIT-Web-Service/";  // MAKE SURE TO CHANGE THIS DEPENDING ON THE IP OF THE SERVER HOST
    public static final String ERROR_TAG = "ERROR: ";

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
}