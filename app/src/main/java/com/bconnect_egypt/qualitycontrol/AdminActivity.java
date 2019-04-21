package com.bconnect_egypt.qualitycontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView adminRecyclerView;
    GridLayoutManager layoutManager;
    AdminAdapter adminAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef, RefNotVisit;
    List<AdminClass> list;
    TextView username, totalNotVisit;
    EditText searchText;
    String[] uName;
    long countAllNotVisit;
    private FirebaseAuth mAuth;
    ImageButton search;
    ImageView logOut;
    String name;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminRecyclerView = findViewById(R.id.admin_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        adminRecyclerView.setLayoutManager(layoutManager);
        username = findViewById(R.id.username);
        totalNotVisit = findViewById(R.id.total_not_visit);
        logOut = findViewById(R.id.log_out);
        searchText = findViewById(R.id.search_text);
        dataSaver = getDefaultSharedPreferences(this);
        search = findViewById(R.id.search);
        logOut.setOnClickListener(this);
        search.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        name = mAuth.getCurrentUser().getEmail();
        uName = name.split("@");
        username.setText(uName[0]);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("admin");
        RefNotVisit = database.getReference("Sheet");
        Toast.makeText(AdminActivity.this, "جاري تحميل البيانات برجاء الانتظار ...", Toast.LENGTH_LONG).show();

        loadAdminData();
        countNotVisit();
    }

    public void countNotVisit() {
        RefNotVisit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PharmacyClass> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PharmacyClass userValue = dataSnapshot1.getValue(PharmacyClass.class);
                    if (userValue.getDate_of_visit() != null && !userValue.getDate_of_visit().equals("") && !userValue.getDate_of_visit().equals("NULL")) {
                    } else {
                        list.add(userValue);
                    }
                }
                countAllNotVisit = list.size();
                Log.e("notVisitCount", "notVisitCount " + countAllNotVisit);
                totalNotVisit.setText(" عدد الصيدليات الغير مزارة: " + String.valueOf(countAllNotVisit));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadAdminData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AdminClass userValue = dataSnapshot1.getValue(AdminClass.class);
                    AdminClass user = new AdminClass();
                    long total = userValue.getTotal();
                    long totalNotVisit = userValue.getTotalNotVisit();
                    long totalVisit = userValue.getTotalVisit();
                    String key = dataSnapshot1.getKey();
                    user.setKey(key);
                    user.setTotal(total);
                    user.setTotalNotVisit(totalNotVisit);
                    user.setTotalVisit(totalVisit);
                    list.add(user);
                }
                adminAdapter = new AdminAdapter(list, AdminActivity.this);
                adminRecyclerView.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    public void search() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    AdminClass userValue = dataSnapshot1.getValue(AdminClass.class);
                    if (dataSnapshot1.getKey().contains(searchText.getText().toString())) {
                        AdminClass user = new AdminClass();
                        long total = userValue.getTotal();
                        long totalNotVisit = userValue.getTotalNotVisit();
                        long totalVisit = userValue.getTotalVisit();
                        String key = dataSnapshot1.getKey();
                        user.setKey(key);
                        user.setTotal(total);
                        user.setTotalNotVisit(totalNotVisit);
                        user.setTotalVisit(totalVisit);
                        list.add(user);
                    }
                }
                adminAdapter = new AdminAdapter(list, AdminActivity.this);
                adminRecyclerView.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    public void logOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataSaver.edit()
                        .putString("representative", "")
                        .apply();
                mAuth.signOut();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
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
        if (view == logOut) {
            logOut();
        }
    }
}
