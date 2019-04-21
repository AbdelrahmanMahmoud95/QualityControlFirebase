package com.bconnect_egypt.qualitycontrol;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RepresentativeDetailsActivity extends AppCompatActivity {
    String total, total_not_visit, total_visit;
    TextView totalPharmacies, totalNotVisit, totalVisit;
    Button visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_details);
        totalPharmacies = findViewById(R.id.total);
        totalNotVisit = findViewById(R.id.total_not_visit);
        totalVisit = findViewById(R.id.total_visit);
        visit = findViewById(R.id.visit);
        Intent intent = getIntent();
        total = intent.getStringExtra("total");
        total_not_visit = intent.getStringExtra("totalNotVisit");
        total_visit = intent.getStringExtra("totalVisit");

        totalPharmacies.setText(total);
        totalNotVisit.setText(total_not_visit);
        totalVisit.setText(total_visit);

        totalVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RepresentativeDetailsActivity.this, HistoryActivity.class);
                intent1.putExtra("allVisits","allVisits");
                startActivity(intent1);
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RepresentativeDetailsActivity.this, BranchActivity.class);
                startActivity(intent1);
            }
        });
    }
}
