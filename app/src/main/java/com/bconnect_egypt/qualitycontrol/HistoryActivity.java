package com.bconnect_egypt.qualitycontrol;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView pharmaciesRecyclerView;
    GridLayoutManager layoutManager;
    PharmaciesAdapter pharmaciesAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<PharmacyClass> list;
    private FirebaseAuth mAuth;
    final int TOTAL_ITEM_EACH_LOAD = 50;
    int currentPage = 1, currentPage1 = 1;
    ImageButton loadData, search;
    EditText searchText;
    long visitCount;
    TextView totalVisit;
    boolean isSearch = false;
    String representative = "";
    String all_visits = "";
    String[] uName;
    String branch_name;
    String branch;
    SharedPreferences dataSaver;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pharmaciesRecyclerView = findViewById(R.id.pharmacies_recycler);
        loadData = findViewById(R.id.load_data);
        totalVisit = findViewById(R.id.total_visit);
        loadData.setOnClickListener(this);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        searchText = findViewById(R.id.search_text);
        layoutManager = new GridLayoutManager(this, 1);
        pharmaciesRecyclerView.setLayoutManager(layoutManager);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        dataSaver.edit()
                .putBoolean("total_pharmacies", true)
                .apply();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        representative = dataSaver.getString("representative", "");
        branch_name = intent.getStringExtra("BranchName");
        all_visits = intent.getStringExtra("allVisits");
        Log.e("allVisits", "allVisits " + all_visits);
        String name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        database = FirebaseDatabase.getInstance();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);
        Log.e("newDateStr", "newDateStr " + formattedDate);

        if (representative.equals("")) {
            myRef = database.getReference("Sheet");

        } else {
            myRef = database.getReference("Sheet");
            uName[0] = representative;
        }

        Toast.makeText(HistoryActivity.this, "جاري تحميل البيانات برجاء الانتظار ...", Toast.LENGTH_LONG).show();

        if (all_visits == null) {
            loadDailyHistory();
            historyCount();
        } else if (all_visits.equals("allVisits")) {
            loadAllDailyHistory();
            allHistoryCount();
        }
    }

    public void historyCount() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);

                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {

                        if (userValue.getDate_of_visit().contains(uName[0]) && userValue.getBranch().equals(branch_name)) {

                            list.add(userValue);
                        }
                    }
                }
                visitCount = list.size();
                Log.e("visitCount", "visitCount " + visitCount);
                totalVisit.setText(" عدد الصيدليات المزارة: " + String.valueOf(visitCount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    public void allHistoryCount() {
        myRef.addValueEventListener(new ValueEventListener() {
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
                visitCount = list.size();
                Log.e("visitCount", "visitCount " + visitCount);
                totalVisit.setText(" عدد الصيدليات المزارة: " + String.valueOf(visitCount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    private void search() {
        isSearch = true;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    String searchByName = searchText.getText().toString();
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                        if (userValue.getDate_of_visit().contains(uName[0]) &&
                                userValue.getBranch().equals(branch_name)) {
                            if (userValue.getAddress().contains(searchByName)
                                    || userValue.getDate_of_visit().contains(searchByName)
                                    || userValue.getPharmacyName().contains(searchByName)) {
                                PharmacyClass user = new PharmacyClass();
                                String pharmacyName = userValue.getPharmacyName();
                                String code = userValue.getCode();
                                String address = userValue.getAddress();
                                String date = userValue.getDate_of_visit();
                                String comment = userValue.getComment();
                                String phone = userValue.getPhone();
                                String tele = userValue.getTele();
                                String ownerName = userValue.getOwnerName();
                                String branch = userValue.getBranch();
                                String contactType = userValue.getContactType();
                                String goal = userValue.getGoal();
                                String subscription = userValue.getSubscription();
                                String key = dataSnapshot1.getKey();
                                String motahedaCode = userValue.getMotahedaCode();
                                user.setMotahedaCode(motahedaCode);

                                user.setPharmacyName(pharmacyName);
                                user.setCode(code);
                                user.setAddress(address);
                                user.setPhone(phone);
                                user.setKey(key);
                                user.setDate_of_visit(date);
                                user.setComment(comment);
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
                }
                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                pharmaciesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value." + error.getMessage());
            }
        });
    }

    private void searchAllHistory() {
        isSearch = true;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    String searchByName = searchText.getText().toString();
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                        if (userValue.getDate_of_visit().contains(uName[0])) {
                            if (userValue.getAddress().contains(searchByName)
                                    || userValue.getDate_of_visit().contains(searchByName)
                                    || userValue.getPharmacyName().contains(searchByName)) {
                                PharmacyClass user = new PharmacyClass();
                                String pharmacyName = userValue.getPharmacyName();
                                String code = userValue.getCode();
                                String address = userValue.getAddress();
                                String date = userValue.getDate_of_visit();
                                String comment = userValue.getComment();
                                String phone = userValue.getPhone();
                                String tele = userValue.getTele();
                                String ownerName = userValue.getOwnerName();
                                String branch = userValue.getBranch();
                                String contactType = userValue.getContactType();
                                String goal = userValue.getGoal();
                                String subscription = userValue.getSubscription();
                                String key = dataSnapshot1.getKey();
                                String motahedaCode = userValue.getMotahedaCode();
                                user.setMotahedaCode(motahedaCode);

                                user.setPharmacyName(pharmacyName);
                                user.setCode(code);
                                user.setAddress(address);
                                user.setPhone(phone);
                                user.setKey(key);
                                user.setDate_of_visit(date);
                                user.setComment(comment);
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
                }

                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                pharmaciesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value." + error.getMessage());
            }
        });
    }

    public void loadMoreData() {
        currentPage++;

        if (all_visits == null) {
            loadHistory();
        } else if (all_visits.equals("allVisits")) {
            loadAllHistory();
        }
    }

    private void loadMoreData1() {
        currentPage1++;
        if (all_visits == null) {
            search();
        } else if (all_visits.equals("allVisits")) {
            searchAllHistory();
        }
    }

    public void loadHistory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {

                        if (userValue.getDate_of_visit().contains(uName[0])) {
                            if (userValue.getBranch().equals(branch_name)) {
                                PharmacyClass user = new PharmacyClass();
                                String pharmacyName = userValue.getPharmacyName();
                                String code = userValue.getCode();
                                String address = userValue.getAddress();
                                String date = userValue.getDate_of_visit();
                                String comment = userValue.getComment();
                                String phone = userValue.getPhone();
                                String tele = userValue.getTele();
                                String ownerName = userValue.getOwnerName();
                                branch = userValue.getBranch();
                                String contactType = userValue.getContactType();
                                String goal = userValue.getGoal();
                                String subscription = userValue.getSubscription();
                                String key = dataSnapshot1.getKey();
                                String motahedaCode = userValue.getMotahedaCode();
                                user.setMotahedaCode(motahedaCode);

                                user.setPharmacyName(pharmacyName);
                                user.setCode(code);
                                user.setComment(comment);
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
                }
                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });


    }


    public void loadDailyHistory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {

                        if (userValue.getDate_of_visit().contains(uName[0])) {
                            if (userValue.getBranch().equals(branch_name) && userValue.getDate_of_visit().contains(formattedDate)) {
                                PharmacyClass user = new PharmacyClass();
                                Log.e("formattedDate1", "formattedDate11 " + formattedDate);
                                String pharmacyName = userValue.getPharmacyName();
                                String code = userValue.getCode();
                                String address = userValue.getAddress();
                                String date = userValue.getDate_of_visit();
                                String comment = userValue.getComment();
                                String phone = userValue.getPhone();
                                String tele = userValue.getTele();
                                String ownerName = userValue.getOwnerName();
                                branch = userValue.getBranch();
                                String contactType = userValue.getContactType();
                                String goal = userValue.getGoal();
                                String subscription = userValue.getSubscription();
                                String key = dataSnapshot1.getKey();
                                String motahedaCode = userValue.getMotahedaCode();
                                user.setMotahedaCode(motahedaCode);

                                user.setPharmacyName(pharmacyName);
                                user.setCode(code);
                                user.setComment(comment);
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
                                if (list.size() == 0) {
                                    Toast.makeText(HistoryActivity.this, "لا يوجد زيارات اليوم", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

    }

    public void loadAllHistory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                        if (userValue.getDate_of_visit().contains(uName[0])) {
                            PharmacyClass user = new PharmacyClass();
                            String pharmacyName = userValue.getPharmacyName();
                            String code = userValue.getCode();
                            String address = userValue.getAddress();
                            String date = userValue.getDate_of_visit();
                            String comment = userValue.getComment();
                            String phone = userValue.getPhone();
                            String tele = userValue.getTele();
                            String ownerName = userValue.getOwnerName();
                            branch = userValue.getBranch();
                            String contactType = userValue.getContactType();
                            String goal = userValue.getGoal();
                            String subscription = userValue.getSubscription();
                            String key = dataSnapshot1.getKey();
                            String motahedaCode = userValue.getMotahedaCode();
                            user.setMotahedaCode(motahedaCode);

                            user.setPharmacyName(pharmacyName);
                            user.setCode(code);
                            user.setComment(comment);
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
                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

    }


    public void loadAllDailyHistory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                        if (userValue.getDate_of_visit().contains(uName[0]) && userValue.getDate_of_visit().contains(formattedDate)) {
                            PharmacyClass user = new PharmacyClass();
                            Log.e("formattedDate1", "formattedDate11 " + formattedDate);
                            String pharmacyName = userValue.getPharmacyName();
                            String code = userValue.getCode();
                            String address = userValue.getAddress();
                            String date = userValue.getDate_of_visit();
                            String comment = userValue.getComment();
                            String phone = userValue.getPhone();
                            String tele = userValue.getTele();
                            String ownerName = userValue.getOwnerName();
                            branch = userValue.getBranch();
                            String contactType = userValue.getContactType();
                            String goal = userValue.getGoal();
                            String subscription = userValue.getSubscription();
                            String key = dataSnapshot1.getKey();
                            String motahedaCode = userValue.getMotahedaCode();
                            user.setMotahedaCode(motahedaCode);

                            user.setPharmacyName(pharmacyName);
                            user.setCode(code);
                            user.setComment(comment);
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
                            if (list.size() == 0) {
                                Toast.makeText(HistoryActivity.this, "لا يوجد زيارات اليوم", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            search();
        }
        if (view == loadData) {

            if (isSearch == true) {
                Toast.makeText(HistoryActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
                loadMoreData1();

            } else if (isSearch == false) {
                Toast.makeText(HistoryActivity.this, "جاري تحميل المزيد برجاء الانتظار ...", Toast.LENGTH_LONG).show();
                loadMoreData();
            }
        }
    }
}