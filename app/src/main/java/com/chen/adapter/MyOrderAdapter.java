package com.chen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.R;
import com.chen.common.Const;
import com.chen.vo.OrderItemVo;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<OrderItemVo> orderItemVoList;
    private View view;
    public MyOrderAdapter(Context context,List<OrderItemVo> orderItemVos){
        this.mContext = context;
        this.orderItemVoList = orderItemVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_order,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        Glide.with(mContext).load(Const.IMAGE_URI+orderItemVoList.get(position).getProductImage()).into(holder1.iv_productImage);
        holder1.tv_quantity.setText(String.valueOf(orderItemVoList.get(position).getQuantity()));
        holder1.tv_title.setText(orderItemVoList.get(position).getProductName());
        holder1.tv_price.setText(String.valueOf(orderItemVoList.get(position).getCurrentUnitPrice().doubleValue()));
        holder1.tv_detail.setText(orderItemVoList.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return orderItemVoList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_title,tv_detail,tv_price,tv_quantity;
        private ImageView iv_productImage;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            iv_productImage = itemView.findViewById(R.id.iv_productImage);
        }
    }
}
