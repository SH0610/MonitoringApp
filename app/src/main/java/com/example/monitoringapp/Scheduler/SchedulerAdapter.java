package com.example.monitoringapp.Scheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.R;

import java.util.ArrayList;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerAdapter.ViewHolder>{

    private final ArrayList<SchedulerItem> mDataList;

    public SchedulerAdapter(ArrayList<SchedulerItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scheduler, parent, false );
        return new SchedulerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchedulerItem schedulerItem = mDataList.get(position);

        holder.real_date.setText(schedulerItem.getReal_date());
        holder.real_time.setText(schedulerItem.getReal_time());
        holder.pre_date.setText(schedulerItem.getPre_date());
        holder.pre_time.setText(schedulerItem.getPre_time());
        holder.account.setText(schedulerItem.getAccount());
        holder.service.setText(schedulerItem.getService());

        if (schedulerItem.getStatus().equals("1")) {
            holder.status.setText("처리 완료");
        } else if (schedulerItem.getStatus().equals("0")) {
            holder.status.setText("미처리");


        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView real_date, real_time, pre_date, pre_time, account, service, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            real_date = itemView.findViewById(R.id.item_scheduler_tv_date_real);
            real_time = itemView.findViewById(R.id.item_scheduler_tv_time_real);
            pre_date = itemView.findViewById(R.id.item_scheduler_tv_date_pre);
            pre_time = itemView.findViewById(R.id.item_scheduler_tv_time_pre);
            account = itemView.findViewById(R.id.item_scheduler_tv_account);
            service = itemView.findViewById(R.id.item_scheduler_tv_service);
            status = itemView.findViewById(R.id.item_scheduler_tv_status);
        }
    }
}
