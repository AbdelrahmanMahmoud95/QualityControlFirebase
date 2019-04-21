package com.bconnect_egypt.qualitycontrol;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class BranchActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView pharmaciesRecyclerView;
    GridLayoutManager layoutManager;
    BranchAdapter branchAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef, reference, refHistory;
    List<PharmacyClass> list;
    String[] uName;
    private FirebaseAuth mAuth;
    int currentPage = 1;
    int currentPage1 = 1;
    ImageView logOut;
    String representative;
    String key;
    ImageButton search, loadData;
    final int TOTAL_ITEM_EACH_LOAD = 80;
    EditText searchText;
    TextView username, totalNotVisit;
    long notVisitCount, countHistory, totalCount;
    boolean isSearch = false;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        pharmaciesRecyclerView = findViewById(R.id.pharmacies_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        pharmaciesRecyclerView.setLayoutManager(layoutManager);
        username = findViewById(R.id.username);
        logOut = findViewById(R.id.log_out);
        totalNotVisit = findViewById(R.id.total_not_visit);
        searchText = findViewById(R.id.search_text);
        search = findViewById(R.id.search);
        loadData = findViewById(R.id.load_data);
        loadData.setOnClickListener(this);
        logOut.setOnClickListener(this);
        search.setOnClickListener(this);
        searchText.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        representative = dataSaver.getString("representative", "");
        String name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        database = FirebaseDatabase.getInstance();
        Log.e("representative", "representative " + representative);

        if (representative.equals("")) {
            logOut.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference().child("admin").child(uName[0]);

        } else {
            uName[0] = representative;
            logOut.setVisibility(View.GONE);
        }

        username.setText(uName[0]);
        myRef = database.getReference("Sheet");
        refHistory = database.getReference("Sheet");
        loadData();
    }

    private void search() {
        isSearch = true;
        Toast.makeText(BranchActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        myRef.limitToFirst(currentPage * TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    String searchByName = searchText.getText().toString();

                    if (userValue.getRepresentativeName().contains(uName[0])
                            && userValue.getBranch().contains(searchByName)) {
                        PharmacyClass user = new PharmacyClass();
                        String branch = userValue.getBranch();
                        user.setBranch(branch);

                        if (list != null) {
                            if (!list.contains(user)) {
                                list.add(user);
                            }
                        }
                    }
                }
                if (list != null) {
                    branchAdapter = new BranchAdapter(BranchActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(branchAdapter);
                    branchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    private void loadData() {
        isSearch = false;
        Toast.makeText(BranchActivity.this, "جاري تحميل البيانات برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        myRef.limitToLast(currentPage * TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getRepresentativeName().contains(uName[0])) {
                        PharmacyClass user = new PharmacyClass();
                        String branch = userValue.getBranch();
                        user.setBranch(branch);

                        if (list != null) {
                            if (!list.contains(user)) {
                                list.add(user);
                            }
                        }
                    }
                }
                if (list != null) {
                    branchAdapter = new BranchAdapter(BranchActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(branchAdapter);
                    branchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                    } else if (userValue.getRepresentativeName().contains(uName[0])) {
                        list.add(userValue);
                    }
                }
                notVisitCount = list.size();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
        refHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);

                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {

                        if (userValue.getDate_of_visit().contains(uName[0])) {

                            list.add(userValue);
                        }
                    }
                }
                countHistory = list.size();
                Log.e("countHistory", "countHistory " + countHistory);
                totalCount = countHistory + notVisitCount;
                Log.e("totalCount", "totalCount " + totalCount);
                Log.e("representative", "representative " + totalCount);

                if (representative == "") {
                    final Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("totalVisit", countHistory);
                    updates.put("total", totalCount);
                    updates.put("totalNotVisit", notVisitCount);
                    reference.updateChildren(updates);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getRepresentativeName().contains(uName[0])) {
                        if (!list.contains(userValue)) {
                            list.add(userValue);

                        }
                    }
                }
                notVisitCount = list.size();
                Log.e("countNotVisit", "countNotVisit " + notVisitCount);
                totalNotVisit.setText(" عدد الفروع: " + String.valueOf(notVisitCount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    private void loadMoreData() {
        currentPage++;
        loadData();
    }

    private void loadMoreData1() {
        currentPage1++;
        search();
    }

    public void logOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                dataSaver.edit()
                        .putString("representative", "")
                        .apply();
                Intent intent = new Intent(BranchActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setTitle("تسجيل الخروج");
        dialog.setMessage("هل انت متأكد انك تريد تسجيل الخروج؟");
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if (view == search) {
            search();
        }
        if (view == loadData) {

            if (isSearch == true) {

                loadMoreData1();

            } else if (isSearch == false) {

                loadMoreData();
            }
        }
        if (view == logOut) {
            logOut();
        }
    }
}