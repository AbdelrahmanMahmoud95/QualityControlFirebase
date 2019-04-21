package com.bconnect_egypt.qualitycontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    List<PharmacyClass> list;
    Context context;

    public HistoryAdapter(Context context, List<PharmacyClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout, viewGroup, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, final int i) {

        holder.pharmacyName.setText(list.get(i).getPharmacyName());

        holder.pharmacyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.relative.setBackgroundColor(Color.parseColor("#EFEDED"));

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

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        RelativeLayout relative;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);

        }
    }

}
