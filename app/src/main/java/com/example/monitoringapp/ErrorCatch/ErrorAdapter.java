package com.example.monitoringapp.ErrorCatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.R;

import java.util.ArrayList;

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ViewHolder> {

    private final ArrayList<ErrorItem> mDataList;

    public ErrorAdapter(ArrayList<ErrorItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
        return new ErrorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ErrorItem errorItem = mDataList.get(position);

        holder.error_date.setText(errorItem.getErrdt());
        holder.error_time.setText(errorItem.getErrtm());
        holder.account.setText(errorItem.getAgnm());
        holder.service.setText(errorItem.getSvcnm());

        // 처리 여부 초기 상태 (흰색 배경에 검은색 글씨)
        holder.status.setText(errorItem.getComfg());
        holder.status.setBackgroundColor(Color.WHITE);
        holder.status.setTextColor(Color.parseColor("#000000"));

        if (errorItem.getComfg().equals("0")) { // 미완료이면 빨간색
            holder.status.setBackgroundColor(Color.parseColor("#FFC0CB"));
            holder.status.setTextColor(Color.RED);
            holder.status.setText("미처리");
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView error_date, error_time, status, account, service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            error_date = itemView.findViewById(R.id.item_error_tv_date);
            error_time = itemView.findViewById(R.id.item_error_tv_time_real);
            status = itemView.findViewById(R.id.item_error_tv_status);
            account = itemView.findViewById(R.id.item_error_tv_account);
            service = itemView.findViewById(R.id.item_error_tv_service);
        }
    }
}
