package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.R;
import com.chen.bean.ClassifyInfo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyClassifySubAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ClassifyInfo> classifyInfoList;
    private int selectedPosition=-1;
    public MyClassifySubAdapter(Context context, List<ClassifyInfo> classifyInfos){
        this.mContext = context;
        this.classifyInfoList = classifyInfos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.classify_sub_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_title.setText(classifyInfoList.get(position).parameter1);
        holder1.tv_title.setId(classifyInfoList.get(position).parameter2);
        holder1.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,2);
                }
            }
        });
        holder1.tv_title.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return classifyInfoList.size();
    }

    public void clearSelection(int position) {
        selectedPosition = position;
    }

    private RecyclerExtras.OnItemClickListenerForMark onItemClickListenerForMark;
    public void setOnItemClickListenerForMark(RecyclerExtras.OnItemClickListenerForMark listener) {
        this.onItemClickListenerForMark = listener;
    }

    public  class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
