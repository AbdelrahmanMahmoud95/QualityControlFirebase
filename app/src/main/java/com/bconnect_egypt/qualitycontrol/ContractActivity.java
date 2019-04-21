package com.bconnect_egypt.qualitycontrol;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ContractActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup replicationGroup, problemSolvingGroup, monthly_fees_group, static_group,
            recommend_group, customer_service_group, internet_group, network_group,
            data_base_group, anti_virus_group, ts_evaluation_group, company_products_group,
            android_app_group, bconnect_website_group, egypttec_group, help_desk_group, facebook_group;

    LinearLayout replicationLinear, averageDaysLinear, monthly_fees_linear,
            monthly_fees_linear1, down_recommend, down_arrow, static_linear;

    TextView date_fees;

    EditText code_text, ts_name, sn_text, ph_name_text, address_text, mobile_sn_text,
            ph_manager_text, owner_text, mobile_text, land_text, ucp_text, date_text,
            soft_name_text, soft_version_text, pc_number, comment_text, available_text,
            total_space_text, from, to, fees, serial, service, comment_fees,
            customer_suggestions_text, other_fees_text;

    String replication, problemSolving, monthly_fees, staticInternet,
            recommend, customer_service, internet, network,
            data_base, anti_virus, ts_evaluation, company_products,
            android_app, bconnect_website, egypttec, help_desk, facebook;

    Button clear_signature, finish_visit;
    SignaturePad signaturePad;

    double lat, lon;
    int rate_recommend, rate_customer_service;
    FusedLocationProviderClient fusedLocationProviderClient;
    static final int External_Permission_Request_code = 0505;
    static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    static final String Fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    static final String coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    boolean mExternalPermissionGranted = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        getExternalPermission();
        findById();
        down_arrow.setOnClickListener(this);
        down_recommend.setOnClickListener(this);
        clear_signature.setOnClickListener(this);
        date_fees.setOnClickListener(this);
        finish_visit.setOnClickListener(this);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                getDeviceLocation();
            }

            @Override
            public void onClear() {

            }
        });
        intilizeRadioGroup();

    }

    public void findById() {
        code_text = findViewById(R.id.code_text);
        ts_name = findViewById(R.id.ts_name);
        sn_text = findViewById(R.id.sn_text);
        ph_name_text = findViewById(R.id.ph_name_text);
        address_text = findViewById(R.id.address_text);
        mobile_sn_text = findViewById(R.id.mobile_sn_text);
        ph_manager_text = findViewById(R.id.ph_manager_text);
        owner_text = findViewById(R.id.owner_text);
        mobile_text = findViewById(R.id.mobile_text);
        land_text = findViewById(R.id.land_text);

        date_text = findViewById(R.id.date_text);
        soft_name_text = findViewById(R.id.soft_name_text);
        soft_version_text = findViewById(R.id.soft_version_text);
        pc_number = findViewById(R.id.pc_number);
        comment_text = findViewById(R.id.comment_text);
        static_group = findViewById(R.id.static_group);
        available_text = findViewById(R.id.available_text);
        total_space_text = findViewById(R.id.total_space_text);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        fees = findViewById(R.id.fees);
        serial = findViewById(R.id.serial);
        service = findViewById(R.id.service);
        comment_fees = findViewById(R.id.comment_fees);
        customer_suggestions_text = findViewById(R.id.customer_suggestions_text);
        other_fees_text = findViewById(R.id.other_fees_text);

        network_group = findViewById(R.id.network_group);
        data_base_group = findViewById(R.id.data_base_group);
        anti_virus_group = findViewById(R.id.anti_virus_group);
        ts_evaluation_group = findViewById(R.id.ts_evaluation_group);
        company_products_group = findViewById(R.id.company_products_group);
        android_app_group = findViewById(R.id.android_app_group);
        bconnect_website_group = findViewById(R.id.bconnect_website_group);
        egypttec_group = findViewById(R.id.egypttec_group);
        help_desk_group = findViewById(R.id.help_desk_group);
        facebook_group = findViewById(R.id.facebook_group);
        replicationGroup = findViewById(R.id.replication_group);
        problemSolvingGroup = findViewById(R.id.problem_solving_group);
        monthly_fees_group = findViewById(R.id.monthly_fees_group);
        recommend_group = findViewById(R.id.recommend_group);
        customer_service_group = findViewById(R.id.customer_service_group);
        internet_group = findViewById(R.id.internet_group);

        averageDaysLinear = findViewById(R.id.average_days);
        replicationLinear = findViewById(R.id.replication_linear);
        monthly_fees_linear = findViewById(R.id.monthly_fees_linear);
        monthly_fees_linear1 = findViewById(R.id.monthly_fees_linear1);
        date_fees = findViewById(R.id.date_fees);
        finish_visit = findViewById(R.id.finish_visit);
        clear_signature = findViewById(R.id.clear_signature);
        down_recommend = findViewById(R.id.down_arrow2);
        down_arrow = findViewById(R.id.down_arrow);
        static_linear = findViewById(R.id.static_linear);
        signaturePad = findViewById(R.id.signaturePad);
    }

    public void intilizeRadioGroup() {
        replicationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.replication_yes:
                        replication = "replication_yes";
                        replicationLinear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.replication_no:
                        replication = "replication_no";
                        replicationLinear.setVisibility(View.GONE);
                        break;

                }
            }
        });
        network_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.network_yes:
                        network = "network_yes";
                        break;

                    case R.id.network_no:
                        network = "network_no";
                        break;

                }
            }
        });
        data_base_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.data_base_yes:
                        data_base = "data_base_yes";
                        break;

                    case R.id.data_base_no:
                        data_base = "data_base_no";
                        break;

                }
            }
        });
        problemSolvingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.problem_solving_yes:
                        problemSolving = "problem_solving_yes";
                        break;

                    case R.id.problem_solving_no:
                        problemSolving = "problem_solving_no";
                        break;

                }
            }
        });
        anti_virus_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.anti_virus_yes:
                        anti_virus = "anti_virus_yes";
                        break;

                    case R.id.anti_virus_no:
                        anti_virus = "anti_virus_no";
                        break;
                }
            }
        });
        ts_evaluation_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.excellent:
                        ts_evaluation = "excellent";
                        break;

                    case R.id.good:
                        ts_evaluation = "good";
                        break;

                    case R.id.weak:
                        ts_evaluation = "weak";
                        break;

                    case R.id.fair:
                        ts_evaluation = "fair";
                        break;
                }
            }
        });
        company_products_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.company_products_yes:
                        company_products = "company_products_yes";
                        break;

                    case R.id.company_products_no:
                        company_products = "company_products_no";
                        break;

                }
            }
        });
        bconnect_website_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bconnect_website_yes:
                        bconnect_website = "bconnect_website_yes";
                        break;

                    case R.id.bconnect_website_no:
                        bconnect_website = "bconnect_website_no";
                        break;

                }
            }
        });
        android_app_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.android_app_yes:
                        android_app = "android_app_yes";
                        break;

                    case R.id.android_app_no:
                        android_app = "android_app_no";
                        break;

                }
            }
        });
        egypttec_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.egypttec_yes:
                        egypttec = "egypttec_yes";
                        break;

                    case R.id.egypttec_no:
                        egypttec = "egypttec_no";
                        break;

                }
            }
        });
        help_desk_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.help_desk_yes:
                        help_desk = "help_desk_yes";
                        break;

                    case R.id.help_desk_no:
                        help_desk = "help_desk_no";
                        break;

                }
            }
        });
        facebook_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.facebook_yes:
                        facebook = "facebook_yes";
                        break;

                    case R.id.facebook_no:
                        facebook = "facebook_no";
                        break;
                }
            }
        });

        monthly_fees_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.monthly_fees_yes:
                        monthly_fees_linear1.setVisibility(View.VISIBLE);
                        monthly_fees_linear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.monthly_fees_no:
                        monthly_fees_linear1.setVisibility(View.GONE);
                        monthly_fees_linear.setVisibility(View.GONE);
                        break;

                }
            }
        });

        recommend_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.recommend_yes:
                        down_recommend.setVisibility(View.VISIBLE);
                        break;

                    case R.id.recommend_no:
                        down_recommend.setVisibility(View.GONE);
                        break;

                }
            }
        });

        customer_service_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.customer_service_yes:
                        down_arrow.setVisibility(View.VISIBLE);
                        break;

                    case R.id.customer_service_no:
                        down_arrow.setVisibility(View.GONE);
                        break;

                }
            }
        });

        internet_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.internet_yes:
                        internet = "internet_yes";
                        static_linear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.internet_no:
                        internet = "internet_no";
                        static_linear.setVisibility(View.GONE);
                        break;

                }
            }
        });
        static_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.static_yes:
                        staticInternet = "static_yes";
                        break;

                    case R.id.static_no:
                        staticInternet = "static_no";
                        break;

                }
            }
        });
    }

    public void selectPopupMenu() {
        final PopupMenu popup = new PopupMenu(ContractActivity.this, down_arrow, Gravity.CENTER);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
                    rate_recommend = 1;
                    Toast.makeText(ContractActivity.this, "تم اختيار 1", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.two) {
                    rate_recommend = 2;
                    Toast.makeText(ContractActivity.this, "تم اختيار 2", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.three) {
                    rate_recommend = 3;
                    Toast.makeText(ContractActivity.this, "تم اختيار 3", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.four) {
                    rate_recommend = 4;
                    Toast.makeText(ContractActivity.this, "تم اختيار 4", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.five) {
                    rate_recommend = 5;
                    Toast.makeText(ContractActivity.this, "تم اختيار 5", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.six) {
                    rate_recommend = 6;
                    Toast.makeText(ContractActivity.this, "تم اختيار 6", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.seven) {
                    rate_recommend = 7;
                    Toast.makeText(ContractActivity.this, "تم اختيار 7", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.eight) {
                    rate_recommend = 8;
                    Toast.makeText(ContractActivity.this, "تم اختيار 8", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.nine) {
                    rate_recommend = 9;
                    Toast.makeText(ContractActivity.this, "تم اختيار 9", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.ten) {
                    rate_recommend = 10;
                    Toast.makeText(ContractActivity.this, "تم اختيار 10", Toast.LENGTH_SHORT).show();
                    return true;

                }

                return false;
            }
        });
        popup.show();

    }

    public void selectPopupMenu2() {
        final PopupMenu popup = new PopupMenu(ContractActivity.this, down_arrow, Gravity.CENTER);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
                    rate_customer_service = 1;
                    Toast.makeText(ContractActivity.this, "تم اختيار 1", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.two) {
                    rate_customer_service = 2;
                    Toast.makeText(ContractActivity.this, "تم اختيار 2", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.three) {
                    rate_customer_service = 3;
                    Toast.makeText(ContractActivity.this, "تم اختيار 3", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.four) {
                    rate_customer_service = 4;
                    Toast.makeText(ContractActivity.this, "تم اختيار 4", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.five) {
                    rate_customer_service = 5;
                    Toast.makeText(ContractActivity.this, "تم اختيار 5", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.six) {
                    rate_customer_service = 6;
                    Toast.makeText(ContractActivity.this, "تم اختيار 6", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.seven) {
                    rate_customer_service = 7;
                    Toast.makeText(ContractActivity.this, "تم اختيار 7", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.eight) {
                    rate_customer_service = 8;
                    Toast.makeText(ContractActivity.this, "تم اختيار 8", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.nine) {
                    rate_customer_service = 9;
                    Toast.makeText(ContractActivity.this, "تم اختيار 9", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.ten) {
                    rate_customer_service = 10;
                    Toast.makeText(ContractActivity.this, "تم اختيار 10", Toast.LENGTH_SHORT).show();
                    return true;

                }

                return false;
            }
        });
        popup.show();

    }

    public void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lon = location.getLongitude();
                        lat = location.getLatitude();
                        Log.e("TAG", " lat " + lat + " lon " + lon);
                    } else {
                        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        // startActivity(intent);
                        Log.e("TAG", " lat " + lat + " lon " + lon);
                    }

                }
            });
        } catch (SecurityException e) {
            e.getMessage();
        }
    }

    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                coarse_Location) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mExternalPermissionGranted = true;

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mExternalPermissionGranted = true;
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                            Fine_location) == PackageManager.PERMISSION_GRANTED) {
                        mExternalPermissionGranted = true;
                    } else {
                        ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                }
            } else {
                ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        ContractActivity.this.sendBroadcast(mediaScanIntent);
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        Toast.makeText(ContractActivity.this, file.getPath(), Toast.LENGTH_LONG).show();
        Log.e("SignaturePad", "file.getPath() " + file.getPath());
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void addJpgSignatureToGallery(Bitmap signature) {

        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d" + "abdo" + ".jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void finishVisit() {

    }

    @Override
    public void onClick(View view) {
        if (view == down_arrow) {
            selectPopupMenu2();
        }
        if (view == down_recommend) {
            selectPopupMenu();
        }
        if (view == clear_signature) {
            signaturePad.clear();
        }
        if (view == date_fees) {
            DateDialog dateDialog = new DateDialog(view);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            dateDialog.show(fragmentTransaction, "Date Picker");
        }
        if (view == finish_visit) {
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
            addJpgSignatureToGallery(signatureBitmap);
            finishVisit();
        }
    }

}
