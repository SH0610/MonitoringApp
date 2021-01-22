package com.example.monitoringapp.Scheduler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.monitoringapp.BaseActivity.getTodayDate;

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
        holder.status.setBackgroundColor(Color.WHITE);
        holder.status.setTextColor(Color.parseColor("#000000"));

        if (schedulerItem.getStatus().equals("1")) {
            holder.status.setText("완료");
            holder.status.setBackgroundColor(Color.parseColor("#BDECB6"));
            holder.status.setTextColor(Color.parseColor("#008000"));
        } else {
            holder.status.setText("미처리");

            String realDate = schedulerItem.getReal_date() + schedulerItem.getReal_time();
            String preDate = schedulerItem.getPre_date() + schedulerItem.getPre_time();

            String tmpReal = realDate.replace("-", "");
            tmpReal = tmpReal.replace(":", "");

            String tmpPre = preDate.replace("-", "");
            tmpPre = tmpPre.replace(":", "");

            System.out.println(" 미처리  real: " + tmpReal);
            System.out.println(" 미처리 pre : " + tmpPre);

            // 초기 날짜 (미리 지정되어있음)
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("y");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");
            SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("H");
            SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("m");
            SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("s");

            int init_year = Integer.parseInt(simpleDateFormat1.format(mDate));
            int init_month = Integer.parseInt(simpleDateFormat2.format(mDate));
            int init_day = Integer.parseInt(simpleDateFormat3.format(mDate));
            int init_hour = Integer.parseInt(simpleDateFormat4.format(mDate));
            int init_min = Integer.parseInt(simpleDateFormat5.format(mDate));
            int init_sec = Integer.parseInt(simpleDateFormat6.format(mDate));

            System.out.println("년도 : " + init_year + " 월 : " + init_month + " 일 : " + init_day);

            final String init_dt = getTodayDate();
            String nowDate = init_dt.replaceAll("-", "");

            String real_hour, real_min, real_sec;
            if (init_hour < 10) {
                real_hour = "0" + init_hour;
            } else {
                real_hour = Integer.toString(init_hour);
            }

            if (init_min < 10) {
                real_min = "0" + init_min;
            } else {
                real_min = Integer.toString(init_min);
            }

            if (init_sec < 10) {
                real_sec = "0" + init_sec;
            } else {
                real_sec = Integer.toString(init_sec);
            }

            nowDate = nowDate + real_hour + real_min + real_sec;
            System.out.println(nowDate.length());
            System.out.println("현재 날짜 및 시간 " + nowDate);

            if (tmpReal.equals("업데이트 정보 없음")) {
                // 현재 시간 > 예상 실행 시간
                // nowDate  > tmpPre 일때

                System.out.println("현재 날짜 및 시간 숫자 " + Long.parseLong(nowDate));
                System.out.println("예상 날짜 및 시간 정수 " + Long.parseLong(tmpPre));

                if (Long.parseLong(nowDate) > Long.parseLong(tmpPre)) {
                    holder.status.setBackgroundColor(Color.parseColor("#FFC0CB"));
                    holder.status.setTextColor(Color.RED);
                }

            }
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
