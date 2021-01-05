package com.example.monitoringapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringapp.R;
import com.example.monitoringapp.ServiceItem;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

    private final ArrayList<ServiceItem> mDataList;

    // 3. 외부에서 데이터를 받을 수 있게 constructor 생성
    // 4. 아이템 정의하기
    public ServiceAdapter(ArrayList<ServiceItem> mDataList) {
        this.mDataList = mDataList;
    }

    // 2. 뷰홀더를 만드는 부분 (뷰 얻기)
    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        // 선택적으로 컨텍스트를 변경한 상태에서 기존 LayoutInflater의 복사본인 새 LayoutInflater 인스턴스 생성.
        return new ServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, int position) {
        // 위의 뷰홀더에서 리턴을 해주면, 뷰홀더가 들어와서 데이터를 바인딩해주는 부분
        // 6. 데이터 세팅
        ServiceItem serviceItem = mDataList.get(position);

        // holder에 데이터 넣기
        holder.date.setText(serviceItem.getDate());
        holder.time.setText(serviceItem.getTime());
        holder.account.setText(serviceItem.getAccount());
        holder.service.setText(serviceItem.getService());
        holder.status.setText(serviceItem.getStatus());
    }

    @Override
    public int getItemCount() {
        // 5. 어댑터가 갖는 아이템의 갯수 지정
        return mDataList.size();
    }

    // 1. 제일 먼저 뷰홀더 만들기
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, account, service, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_service_tv_date);
            time = itemView.findViewById(R.id.item_service_tv_time);
            account = itemView.findViewById(R.id.item_service_tv_account);
            service = itemView.findViewById(R.id.item_service_tv_service);
            status = itemView.findViewById(R.id.item_service_tv_status);
        }
    }
}
