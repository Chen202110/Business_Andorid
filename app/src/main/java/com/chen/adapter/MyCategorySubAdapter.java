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
import com.chen.bean.CategoryInfo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyCategorySubAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<CategoryInfo> categoryInfoList;
    private int selectedPosition=-1;
    public MyCategorySubAdapter(Context context,List<CategoryInfo> categoryInfos){
        this.mContext = context;
        this.categoryInfoList = categoryInfos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_sub_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_title.setText(categoryInfoList.get(position).getName());
        holder1.tv_title.setId(categoryInfoList.get(position).getId());
        holder1.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,1);
                }
            }
        });
        holder1.tv_title.setSelected(selectedPosition == position);
    }



    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }

    public void clearSelection(int position) {
        selectedPosition = position;
    }

    private RecyclerExtras.OnItemClickListenerForMark onItemClickListenerForMark;

    public void setOnItemClickListener(RecyclerExtras.OnItemClickListenerForMark listener) {
        this.onItemClickListenerForMark = listener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}