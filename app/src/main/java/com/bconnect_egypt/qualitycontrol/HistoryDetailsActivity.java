package com.bconnect_egypt.qualitycontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HistoryDetailsActivity extends AppCompatActivity {
    TextView pharmacyName, pharmacyCode, pharmacyAddress,
            pharmacyPhone, pharmacyTele, pharmacyOwner, pharmacySubscription,
            pharmacyGoal, pharmacyBranch, pharmacyContactType, visitDate, commentText, motahedaCode;
    String code, ownerName, address, key, phone, tele,
            subscription, goal, pharmacy_name, branch, contactType, date_of_visit, comment, motaheda_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        pharmacyAddress = findViewById(R.id.pharmacy_address);
        pharmacyCode = findViewById(R.id.pharmacy_code);
        pharmacyName = findViewById(R.id.pharmacy_name);
        pharmacyPhone = findViewById(R.id.pharmacy_phone);
        pharmacyContactType = findViewById(R.id.pharmacy_contact_type);
        pharmacyBranch = findViewById(R.id.pharmacy_branch);
        pharmacyGoal = findViewById(R.id.pharmacy_goal);
        pharmacyOwner = findViewById(R.id.pharmacy_owner);
        pharmacySubscription = findViewById(R.id.pharmacy_subscription);
        pharmacyTele = findViewById(R.id.pharmacy_tele);
        commentText = findViewById(R.id.comment);
        visitDate = findViewById(R.id.visit_date);
        motahedaCode = findViewById(R.id.motaheda_code);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        ownerName = intent.getStringExtra("ownerName");
        address = intent.getStringExtra("address");
        key = intent.getStringExtra("key");
        motaheda_code = intent.getStringExtra("motahedaCode");
        phone = intent.getStringExtra("phone");
        branch = intent.getStringExtra("branch");
        contactType = intent.getStringExtra("contactType");
        tele = intent.getStringExtra("tele");
        goal = intent.getStringExtra("goal");
        pharmacy_name = intent.getStringExtra("pharmacyName");
        subscription = intent.getStringExtra("subscription");
        date_of_visit = intent.getStringExtra("date_of_visit");
        comment = intent.getStringExtra("comment");
        Log.e("date_of_visit", "date_of_visit " + date_of_visit);
        Log.e("comment", "comment " + comment);

        if (date_of_visit != null && !date_of_visit.equals("") && !date_of_visit.equals("NULL")) {
            visitDate.setVisibility(View.VISIBLE);
            String[] date = date_of_visit.split(" ");
            visitDate.setText(" تاريخ الزيارة: " + date[1]);

        }
        if (comment != null && !comment.equals("") && !comment.equals("NULL")) {
            commentText.setVisibility(View.VISIBLE);
            commentText.setText("تقرير الزيارة: " + comment);
        }
        pharmacyName.setText(pharmacy_name);
        pharmacyAddress.setText(address);
        pharmacyCode.setText(code);
        pharmacyPhone.setText(phone);
        pharmacySubscription.setText(subscription);
        pharmacyOwner.setText(ownerName);
        pharmacyTele.setText(tele);
        pharmacyGoal.setText(goal);
        pharmacyBranch.setText(branch);
        pharmacyContactType.setText(contactType);
        motahedaCode.setText(motaheda_code);
    }
}
