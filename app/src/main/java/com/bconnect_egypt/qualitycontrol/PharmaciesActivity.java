package com.bconnect_egypt.qualitycontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PharmaciesActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView totalRecycler;
    GridLayoutManager layoutManager1;
    PharmaciesAdapter pharmaciesAdapter;
    FirebaseDatabase database;
    DatabaseReference refNotVisit;
    List<PharmacyClass> list;
    long totalCount;
    String key, representative;
    String[] uName;
    private FirebaseAuth mAuth;
    int currentPage = 1, currentPage1 = 1;
    final int TOTAL_ITEM_EACH_LOAD = 600;
    ImageButton search, loadData;
    EditText searchText;
    TextView totalPharmacies;
    boolean isSearch = false;
    String branch_name;
    String branch;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);
        totalPharmacies = findViewById(R.id.total_pharmacies);
        totalRecycler = findViewById(R.id.total_recycler);
        layoutManager1 = new GridLayoutManager(this, 1);
        totalRecycler.setLayoutManager(layoutManager1);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        dataSaver.edit()
                .putBoolean("total_pharmacies", true)
                .apply();
        mAuth = FirebaseAuth.getInstance();
        searchText = findViewById(R.id.search_text);
        search = findViewById(R.id.search);
        loadData = findViewById(R.id.load_data);
        loadData.setOnClickListener(this);
        search.setOnClickListener(this);
        Intent intent = getIntent();
        representative = dataSaver.getString("representative", "");
        branch_name = intent.getStringExtra("BranchName");
        String name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        database = FirebaseDatabase.getInstance();

        if (representative == "") {
            refNotVisit = database.getReference("Sheet");

        } else {
            uName[0] = representative;
            refNotVisit = database.getReference("Sheet");

        }
        loadData();
    }

    private void search() {
        isSearch = true;
        Toast.makeText(PharmaciesActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        refNotVisit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    String searchByName = searchText.getText().toString();
                    if (userValue.getRepresentativeName().contains(uName[0])
                            && userValue.getBranch().equals(branch_name)) {
                        if (userValue.getPharmacyName().contains(searchByName)
                                || userValue.getAddress().contains(searchByName)) {
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
                            String motahedaCode = userValue.getMotahedaCode();
                            String goal = userValue.getGoal();
                            String subscription = userValue.getSubscription();
                            key = dataSnapshot1.getKey();

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

                            list.add(user);
                        }
                    }
                }

                pharmaciesAdapter = new PharmaciesAdapter(PharmaciesActivity.this, list);
                totalRecycler.setAdapter(pharmaciesAdapter);
                pharmaciesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    public void loadData() {
        isSearch = false;
        Toast.makeText(PharmaciesActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
        refNotVisit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getRepresentativeName().contains(uName[0]) &&
                            userValue.getBranch().equals(branch_name)) {
                        PharmacyClass user = new PharmacyClass();
                        String pharmacyName = userValue.getPharmacyName();
                        String code = userValue.getCode();
                        String address = userValue.getAddress();
                        String date = userValue.getDate_of_visit();
                        String phone = userValue.getPhone();
                        String tele = userValue.getTele();
                        String ownerName = userValue.getOwnerName();
                        branch = userValue.getBranch();
                        String contactType = userValue.getContactType();
                        String goal = userValue.getGoal();
                        String subscription = userValue.getSubscription();
                        String motahedaCode = userValue.getMotahedaCode();
                        user.setMotahedaCode(motahedaCode);
                        key = dataSnapshot1.getKey();

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
                        list.add(user);

                    }
                }

                pharmaciesAdapter = new PharmaciesAdapter(PharmaciesActivity.this, list);
                totalRecycler.setAdapter(pharmaciesAdapter);
                pharmaciesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

        refNotVisit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getRepresentativeName().contains(uName[0]) && userValue.getBranch().equals(branch)) {

                        list.add(userValue);

                    }
                }
                totalCount = list.size();

                totalPharmacies.setText(" عدد الصيدليات: " + String.valueOf(totalCount));

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
    }
}

