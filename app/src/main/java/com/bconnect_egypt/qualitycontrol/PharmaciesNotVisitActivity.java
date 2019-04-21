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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PharmaciesNotVisitActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView pharmaciesRecyclerView;
    GridLayoutManager layoutManager;
    PharmaciesAdapter pharmaciesAdapter;
    HistoryAdapter historyAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<PharmacyClass> list;
    String[] uName;
    private FirebaseAuth mAuth;
    int currentPage = 1;
    int currentPage1 = 1;
    ImageView logOut;
    String representative;
    String key;
    ImageButton search, loadData;
    EditText searchText;
    TextView totalNotVisit;
    long notVisitCount;
    boolean isSearch = false;
    String branch_name;
    String branch;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies_not_visit);
        pharmaciesRecyclerView = findViewById(R.id.pharmacies_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        pharmaciesRecyclerView.setLayoutManager(layoutManager);
        logOut = findViewById(R.id.log_out);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        dataSaver.edit()
                .putBoolean("total_pharmacies", false)
                .apply();
        totalNotVisit = findViewById(R.id.total_not_visit);
        searchText = findViewById(R.id.search_text);
        search = findViewById(R.id.search);
        loadData = findViewById(R.id.load_data);
        loadData.setOnClickListener(this);
        logOut.setOnClickListener(this);
        search.setOnClickListener(this);
        searchText.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        branch_name = intent.getStringExtra("BranchName");
        String name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        database = FirebaseDatabase.getInstance();
        representative = dataSaver.getString("representative", "");
        Log.e("uName", "uName " + uName[0]);
        if (representative == "") {
            logOut.setVisibility(View.VISIBLE);

        } else {
            uName[0] = representative;
            logOut.setVisibility(View.GONE);
        }

        myRef = database.getReference("Sheet");
        Log.e("branch_name", "branch_name " + branch_name);
        loadData();

    }

    private void search() {
        isSearch = true;
        Toast.makeText(PharmaciesNotVisitActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    String searchByName = searchText.getText().toString();

                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") &&
                            !userValue.getDate_of_visit().equals("NULL")) {
                    } else if (userValue.getRepresentativeName().contains(uName[0]) &&
                            userValue.getBranch().equals(branch_name)) {
                        if (userValue.getPharmacyName().contains(searchByName)
                                || userValue.getAddress().contains(searchByName)
                                || userValue.getCode().contains(searchByName)) {

                            PharmacyClass user = new PharmacyClass();
                            String pharmacyName = userValue.getPharmacyName();
                            String code = userValue.getCode();
                            String address = userValue.getAddress();
                            String date = userValue.getDate_of_visit();
                            String phone = userValue.getPhone();
                            String tele = userValue.getTele();
                            String ownerName = userValue.getOwnerName();
                            String branch = userValue.getBranch();
                            String contactType = userValue.getContactType();
                            String goal = userValue.getGoal();
                            String subscription = userValue.getSubscription();
                            key = dataSnapshot1.getKey();
                            String motahedaCode = userValue.getMotahedaCode();

                            user.setMotahedaCode(motahedaCode);
                            user.setPharmacyName(pharmacyName);
                            user.setCode(code);
                            user.setAddress(address);
                            user.setPhone(phone);
                            user.setKey(key);
                            user.setDate_of_visit(date);
                            user.setGoal(goal);
                            user.setSubscription(subscription);
                            user.setTele(tele);
                            user.setBranch(branch);
                            user.setContactType(contactType);
                            user.setOwnerName(ownerName);
                            Log.e("Key", "Key " + key);
                            list.add(user);
                        }
                    }
                }
                if (representative != "") {
                    historyAdapter = new HistoryAdapter(PharmaciesNotVisitActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(historyAdapter);
                    historyAdapter.notifyDataSetChanged();
                } else {
                    pharmaciesAdapter = new PharmaciesAdapter(PharmaciesNotVisitActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                    pharmaciesAdapter.notifyDataSetChanged();
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
        Toast.makeText(PharmaciesNotVisitActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                    } else if (userValue.getRepresentativeName().contains(uName[0])) {
                        if (userValue.getBranch().equals(branch_name)) {
                            PharmacyClass user = new PharmacyClass();
                            String pharmacyName = userValue.getPharmacyName();
                            String code = userValue.getCode();
                            String address = userValue.getAddress();
                            String date = userValue.getDate_of_visit();
                            String phone = userValue.getPhone();
                            String tele = userValue.getTele();
                            String ownerName = userValue.getOwnerName();
                            String contactType = userValue.getContactType();
                            String goal = userValue.getGoal();
                            String subscription = userValue.getSubscription();
                            key = dataSnapshot1.getKey();
                            String motahedaCode = userValue.getMotahedaCode();
                            user.setMotahedaCode(motahedaCode);
                            user.setPharmacyName(pharmacyName);
                            user.setCode(code);
                            user.setAddress(address);
                            user.setPhone(phone);
                            user.setKey(key);
                            user.setDate_of_visit(date);
                            user.setGoal(goal);
                            user.setSubscription(subscription);
                            user.setTele(tele);
                            user.setContactType(contactType);
                            user.setOwnerName(ownerName);
                            branch = userValue.getBranch();
                            user.setBranch(branch);
                            list.add(user);
                        }
                    }
                }
                if (representative != "") {
                    historyAdapter = new HistoryAdapter(PharmaciesNotVisitActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(historyAdapter);
                    historyAdapter.notifyDataSetChanged();
                } else {
                    pharmaciesAdapter = new PharmaciesAdapter(PharmaciesNotVisitActivity.this, list);
                    pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                    pharmaciesAdapter.notifyDataSetChanged();
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
                    } else if (userValue.getRepresentativeName().contains(uName[0]) && userValue.getBranch().equals(branch)) {
                        list.add(userValue);
                    }
                }
                notVisitCount = list.size();
                Log.e("countNotVisit", "countNotVisit " + notVisitCount);
                totalNotVisit.setText(" عدد الصيدليات الغير مزارة: " + String.valueOf(notVisitCount));
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
                Intent intent = new Intent(PharmaciesNotVisitActivity.this, LoginActivity.class);
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


