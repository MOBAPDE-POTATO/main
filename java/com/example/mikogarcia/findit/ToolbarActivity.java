package com.example.mikogarcia.findit;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ToolbarActivity extends AppCompatActivity {

    LinearLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FindIT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contentView = (LinearLayout) findViewById(R.id.content);

    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean bool) {
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(bool);
    }

    public void changeContentView(int layoutResource){
        View v = LayoutInflater.from(getBaseContext()).inflate(layoutResource, null);

        ViewGroup.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(v, layoutParams);
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
}
