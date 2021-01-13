package com.example.monitoringapp.ErrorCatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
