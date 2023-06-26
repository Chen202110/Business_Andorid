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
import com.chen.common.Const;
import com.chen.vo.ProductListVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyProductItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ProductListVo> productListVoList;
    public MyProductItemAdapter(Context context,List<ProductListVo> productListVos){
        this.mContext = context;
        this.productListVoList = productListVos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item,parent,false);
        return new ItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_name.setText(productListVoList.get(position).getName());
        holder1.tv_detail.setText(productListVoList.get(position).getDetail());
        holder1.tv_price.setText(productListVoList.get(position).getPrice()+"");
        Glide.with(mContext).load(Const.IMAGE_URI+productListVoList.get(position).getMainImage()).into(holder1.iv_pic);
        holder1.ll_item.setId(productListVoList.get(position).getId());
        holder1.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productListVoList.size();
    }

    private RecyclerExtras.OnItemClickListenerForMark onItemClickListenerForMark;

    public void setOnItemClickListener(RecyclerExtras.OnItemClickListenerForMark listener){
        this.onItemClickListenerForMark = listener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public LinearLayout ll_item;
        public TextView tv_name,tv_detail,tv_price;
        public ImageView iv_pic;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.ll_item);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_pic = itemView.findViewById(R.id.iv_pic);
        }
    }
}
