package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.R;
import com.chen.bean.CategoryInfo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CategoryInfo> categoryInfoList;
    public MyCategoryAdapter(Context context, List<CategoryInfo> categoryInfos){
        this.mContext = context;
        this.categoryInfoList = categoryInfos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        Glide.with(mContext).load(categoryInfoList.get(position).getUrl_pic()).into(holder1.iv_pic);
        holder1.tv_title.setText(categoryInfoList.get(position).getName());
        holder1.ll_item.setId(categoryInfoList.get(position).getId());
        holder1.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForDate != null){
                    onItemClickListenerForDate.onItemNameClick(view,position,categoryInfoList.get(position).getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }

    // 获取列表项的类型
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // 获取列表项的编号
    @Override
    public long getItemId(int position) {
        return position;
    }

    private RecyclerExtras.OnItemClickListenerForName onItemClickListenerForDate;

    public void setOnItemClickListenerForName(RecyclerExtras.OnItemClickListenerForName listener){
        this.onItemClickListenerForDate = listener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public LinearLayout ll_item;
        public ImageView iv_pic;
        public TextView tv_title;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.ll_item);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
