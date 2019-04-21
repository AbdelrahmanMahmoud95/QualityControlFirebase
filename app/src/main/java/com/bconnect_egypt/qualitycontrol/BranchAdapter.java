package com.bconnect_egypt.qualitycontrol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    List<PharmacyClass> list;
    Context context;

    public BranchAdapter(Context context, List<PharmacyClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout, viewGroup, false);
        BranchViewHolder pharmaciesViewHolder = new BranchViewHolder(view);
        return pharmaciesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BranchViewHolder holder, final int i) {

        holder.pharmacyName.setText(list.get(i).getBranch());

        holder.pharmacyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.relative.setBackgroundColor(Color.parseColor("#EFEDED"));
                Intent intent = new Intent(context, VisitAndNotVisitActivity.class);
                intent.putExtra("BranchName",  list.get(i).getBranch());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        RelativeLayout relative;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);
        }
    }

}
