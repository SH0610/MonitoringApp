package com.example.monitoringapp.ServerDisk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.R;

import java.util.ArrayList;

public class ServerDiskAdapter extends RecyclerView.Adapter<ServerDiskAdapter.ViewHolder>{

    private final ArrayList<ServerDiskItem> mDataList;

    public ServerDiskAdapter(ArrayList<ServerDiskItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_disk, parent, false);
        return new ServerDiskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServerDiskItem serverDiskItem = mDataList.get(position);

        holder.ip.setText(serverDiskItem.getIp());
        holder.remark.setText(serverDiskItem.getRemark());
        holder.driveName.setText(serverDiskItem.getDriveName());
        holder.freeSize.setText(serverDiskItem.getFreeSize());
        holder.totalSize.setText(serverDiskItem.getTotalSize());
//        holder.usagePercent.setText(serverDiskItem.getUsagePercent());
//        holder.usagePercent.setText("퍼센트");
        holder.date.setText(serverDiskItem.getDate());
        holder.time.setText(serverDiskItem.getTime());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ip, remark, driveName, totalSize, freeSize, usagePercent, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ip = itemView.findViewById(R.id.item_server_disk_tv_ip);
            remark = itemView.findViewById(R.id.item_server_disk_tv_remark);
            driveName = itemView.findViewById(R.id.item_disk_tv_name);
            totalSize = itemView.findViewById(R.id.item_disk_tv_total);
            freeSize = itemView.findViewById(R.id.item_disk_tv_remain);
            date = itemView.findViewById(R.id.item_disk_tv_date);
            time = itemView.findViewById(R.id.item_disk_tv_time);
        }
    }
}
