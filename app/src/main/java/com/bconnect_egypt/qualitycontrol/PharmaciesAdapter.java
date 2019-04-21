package com.bconnect_egypt.qualitycontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.PharmaciesViewHolder> {

    List<PharmacyClass> list;
    Context context;
    SharedPreferences dataSaver;
    boolean total_pharmacies;

    public PharmaciesAdapter(Context context, List<PharmacyClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PharmaciesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout, viewGroup, false);
        PharmaciesViewHolder pharmaciesViewHolder = new PharmaciesViewHolder(view);
        return pharmaciesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PharmaciesViewHolder holder, final int i) {
        dataSaver = getDefaultSharedPreferences(context);
        total_pharmacies = dataSaver.getBoolean("total_pharmacies", false);
        holder.pharmacyName.setText(list.get(i).getPharmacyName());
        holder.pharmacyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.relative.setBackgroundColor(Color.parseColor("#EFEDED"));
                if (total_pharmacies == true) {
                    Intent intent = new Intent(context, HistoryDetailsActivity.class);
                    intent.putExtra("code", "كود الصيدلية: " + list.get(i).getCode());
                    intent.putExtra("pharmacyName", "اسم الصيدلية: " + list.get(i).getPharmacyName());
                    intent.putExtra("address", "العنوان: " + list.get(i).getAddress());
                    intent.putExtra("phone", "تليفون: " + list.get(i).getPhone());
                    intent.putExtra("key", list.get(i).getKey());
                    intent.putExtra("branch", "فرع: " + list.get(i).getBranch());
                    intent.putExtra("contactType", "نوع العقد: " + list.get(i).getContactType());
                    intent.putExtra("ownerName", "اسم المالك: " + list.get(i).getOwnerName());
                    intent.putExtra("goal", "الهدف: " + list.get(i).getGoal());
                    intent.putExtra("subscription", "قيمة الاشتراك: " + list.get(i).getSubscription());
                    intent.putExtra("motahedaCode", "كود المتحدة: " + list.get(i).getMotahedaCode());

                    intent.putExtra("date_of_visit", list.get(i).getDate_of_visit());
                    intent.putExtra("comment", list.get(i).getComment());

                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, NotVisitDetailsActivity.class);
                    intent.putExtra("code", "كود الصيدلية: " + list.get(i).getCode());
                    intent.putExtra("pharmacyName", "اسم الصيدلية: " + list.get(i).getPharmacyName());
                    intent.putExtra("address", "العنوان: " + list.get(i).getAddress());
                    intent.putExtra("phone", "تليفون: " + list.get(i).getPhone());
                    intent.putExtra("key", list.get(i).getKey());
                    intent.putExtra("branch", "فرع: " + list.get(i).getBranch());
                    intent.putExtra("contactType", "نوع العقد: " + list.get(i).getContactType());
                    intent.putExtra("ownerName", "اسم المالك: " + list.get(i).getOwnerName());
                    intent.putExtra("goal", "الهدف: " + list.get(i).getGoal());
                    intent.putExtra("motahedaCode", "كود المتحدة: " + list.get(i).getMotahedaCode());
                    intent.putExtra("subscription", "قيمة الاشتراك: " + list.get(i).getSubscription());


                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PharmaciesViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        RelativeLayout relative;

        public PharmaciesViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);
        }
    }
}
