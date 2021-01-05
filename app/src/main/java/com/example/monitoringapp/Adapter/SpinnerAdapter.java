package com.example.monitoringapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (position == getCount()) {
            // 마지막 포지션의 textView 를 힌트 용으로 사용
            ((TextView) v.findViewById(android.R.id.text1)).setText("");
            // 아이템의 마지막 값을 불러와 hint로 추가
            ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
        }
        return v;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }
}
