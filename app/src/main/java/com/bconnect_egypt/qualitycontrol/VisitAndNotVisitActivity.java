package com.bconnect_egypt.qualitycontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class VisitAndNotVisitActivity extends AppCompatActivity implements View.OnClickListener {
    Button notVisit, history, allPramacies;
    String branch_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_and_not_vist);
        notVisit = findViewById(R.id.not_visit);
        history = findViewById(R.id.history);
        allPramacies = findViewById(R.id.all_pharmacies);
        history.setOnClickListener(this);
        notVisit.setOnClickListener(this);
        allPramacies.setOnClickListener(this);
        Intent intent = getIntent();
        branch_name = intent.getStringExtra("BranchName");
        Log.e("branch_name ","branch_name "+branch_name);
    }

    @Override
    public void onClick(View view) {
        if (view == notVisit) {
            Intent intent = new Intent(this, PharmaciesNotVisitActivity.class);
            intent.putExtra("BranchName", branch_name);
            startActivity(intent);
        }
        if (view == history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("BranchName", branch_name);

            startActivity(intent);
        }
        if (view == allPramacies) {
            Intent intent = new Intent(this, PharmaciesActivity.class);
            intent.putExtra("BranchName", branch_name);

            startActivity(intent);
        }
    }
}
