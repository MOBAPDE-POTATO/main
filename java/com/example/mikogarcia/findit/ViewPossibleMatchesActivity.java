package com.example.mikogarcia.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class ViewPossibleMatchesActivity extends AppCompatActivity {

    RecyclerView matchesRecycler;
    ReportAdapter reportAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_possible_matches);
        final int id = getIntent().getExtras().getInt("id");


        matchesRecycler = (RecyclerView) findViewById(R.id.matchesRecycler);
        reportAdapter = new ReportAdapter();
        matchesRecycler.setAdapter(reportAdapter);
        matchesRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

    }
}
