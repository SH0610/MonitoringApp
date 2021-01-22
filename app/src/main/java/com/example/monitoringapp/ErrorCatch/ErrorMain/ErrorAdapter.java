package com.example.monitoringapp.ErrorCatch.ErrorMain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.ErrorCatch.ErrorInfo.ErrorInfoActivity;
import com.example.monitoringapp.R;

import java.util.ArrayList;

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ViewHolder> {

    private final ArrayList<ErrorItem> mDataList;
    private Context mContext;

    public ErrorAdapter(Context context, ArrayList<ErrorItem> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
        return new ErrorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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
        } else { // 완료이면 처리
            holder.status.setBackgroundColor(Color.parseColor("#BDECB6"));
            holder.status.setTextColor(Color.parseColor("#008000"));
            holder.status.setText("처리");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext = view.getContext();

                String item_account = mDataList.get(position).getAgnm();
                String item_service = mDataList.get(position).getSvcnm();
                String item_errdt = mDataList.get(position).getErrdt();
                String item_errtm = mDataList.get(position).getErrtm();
                String item_errmsg = mDataList.get(position).getErr_msg(); // 에러메시지
                String item_status = mDataList.get(position).getComfg(); // 처리여부
                String item_svccd = mDataList.get(position).getSvccd(); // 서비스코드
                String item_seq = mDataList.get(position).getSeq(); // seq
                String item_commsg = mDataList.get(position).getCom_msg();

                Intent intent = new Intent(mContext, ErrorInfoActivity.class);
                intent.putExtra("account", item_account);
                intent.putExtra("service", item_service);
                intent.putExtra("errdt", item_errdt);
                intent.putExtra("errtm", item_errtm);
                intent.putExtra("errmsg", item_errmsg);
                intent.putExtra("status", item_status);
                intent.putExtra("svccd", item_svccd);
                intent.putExtra("seq", item_seq);
                intent.putExtra("commsg", item_commsg);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView error_date, error_time, status, account, service;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            error_date = itemView.findViewById(R.id.item_error_tv_date);
            error_time = itemView.findViewById(R.id.item_error_tv_time_real);
            status = itemView.findViewById(R.id.item_error_tv_status);
            account = itemView.findViewById(R.id.item_error_tv_account);
            service = itemView.findViewById(R.id.item_error_tv_service);
        }
    }
}