package com.bconnect_egypt.qualitycontrol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotVisitDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView pharmacyName, pharmacyCode, pharmacyAddress,
            pharmacyPhone, pharmacyTele, pharmacyOwner, pharmacySubscription,
            pharmacyGoal, pharmacyBranch, pharmacyContactType, commentText, visitDate, motahedaCode;
    Button finishVisit;
    String code, ownerName, address, key, phone, tele,
            subscription, goal, pharmacy_name, branch, contactType, date_of_visit, comment, motaheda_code;
    private FirebaseAuth mAuth;
    String[] uName;
    DatabaseReference ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_not_visit);
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
        finishVisit = findViewById(R.id.finish_visit);
        motahedaCode = findViewById(R.id.motaheda_code);


        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        ownerName = intent.getStringExtra("ownerName");
        address = intent.getStringExtra("address");
        key = intent.getStringExtra("key");
        phone = intent.getStringExtra("phone");
        branch = intent.getStringExtra("branch");
        contactType = intent.getStringExtra("contactType");
        tele = intent.getStringExtra("tele");
        goal = intent.getStringExtra("goal");
        motaheda_code = intent.getStringExtra("motahedaCode");
        pharmacy_name = intent.getStringExtra("pharmacyName");
        subscription = intent.getStringExtra("subscription");
        Log.e("branch", "branch " + branch);
        commentText = findViewById(R.id.comment);
        visitDate = findViewById(R.id.visit_date);
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

        finishVisit.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        String name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        ref2 = FirebaseDatabase.getInstance().getReference("Sheet").child(key);
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
    }

    public void postComment() {

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        final View v = LayoutInflater.from(this).inflate(R.layout.comment_layout, null);

        Button submit = v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText commentText = v.findViewById(R.id.comment);
                String comment = commentText.getText().toString();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                final Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("date_of_visit", uName[0] + " " + formattedDate);
                updates.put("comment", comment);

                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.child("date_of_visit").getValue() != null && !snapshot.child("date_of_visit").getValue().equals("") && !snapshot.child("date_of_visit").getValue().equals("NULL")) {
                            Toast.makeText(NotVisitDetailsActivity.this, "تمت الزيارة من قبل", Toast.LENGTH_SHORT).show();
                            builder.dismiss();
                        } else {
                            Toast.makeText(NotVisitDetailsActivity.this, "جاري اتمام الزيارة برجاء الانتظار ...", Toast.LENGTH_LONG).show();

                            ref2.updateChildren(updates);
                            builder.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(NotVisitDetailsActivity.this, "خطا", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
        builder.setView(v);
        // dialog.setCancelable(false);
        builder.show();

    }

    @Override
    public void onClick(View view) {
        postComment();
//        Intent intent = new Intent(NotVisitDetailsActivity.this, ContractActivity.class);
//        startActivity(intent);

    }
}
