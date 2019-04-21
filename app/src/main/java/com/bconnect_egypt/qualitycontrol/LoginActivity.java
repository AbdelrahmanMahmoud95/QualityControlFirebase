package com.bconnect_egypt.qualitycontrol;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText username, password;
    Button login;
    String currentVersion, latestVersion;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        getCurrentVersion();
        Log.e("currentVersion ", "currentVersion " + currentVersion);
        Log.e("latestVersion ", "latestVersion " + latestVersion);

        if (currentVersion.equals(latestVersion) || latestVersion == null) {
            if (mAuth.getCurrentUser() != null) {
                String name = mAuth.getCurrentUser().getEmail();
                String[] uName = name.split("@");
                if (uName[0].equals("mohamednoaman") || uName[0].equals("ahmedamer") || uName[0].equals("mamdouhamin") || uName[0].equals("mohamedkamal")) {
                    startActivity(new Intent(this, AdminActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(this, BranchActivity.class));
                    finish();
                }
            }
        }

//        DatabaseReference myRef;
//        FirebaseDatabase database;
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Sheet");
//        myRef.removeValue();
    }

    @Override
    public void onClick(View view) {
        if (view == login) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("برجاء الانتظار...");
            progressDialog.show();
            String name = username.getText().toString();
            String pass = password.getText().toString();
            if (name.equals("") || pass.equals("")) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "ادخل الايميل وكلمة السر",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                String name = mAuth.getCurrentUser().getEmail();
                                String[] uName = name.split("@");
                                if (uName[0].equals("mohamednoaman") || uName[0].equals("ahmedamer") || uName[0].equals("mamdouhamin") || uName[0].equals("mohamedkamal")) {
                                    Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, BranchActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "خطأ مستخدم غير موجود",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        Log.e("currentVersion ", "currentVersion " + currentVersion);
        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.bconnect_egypt.qualitycontrol&hl=en_US").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.e("latestVersion", "latestVersion " + latestVersion);
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) {
                        showUpdateDialog();
                    }
                }
            } else
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.bconnect_egypt.qualitycontrol")));
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }
}