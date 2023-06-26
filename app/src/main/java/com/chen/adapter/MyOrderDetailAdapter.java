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
import com.chen.vo.OrderItemVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyOrderDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<OrderItemVo> orderItemVoList;
    private View view;
    public MyOrderDetailAdapter(Context context,List<OrderItemVo> orderItemVos){
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_title.setText(orderItemVoList.get(position).getProductName());
        holder1.tv_quantity.setText(String.valueOf(orderItemVoList.get(position).getQuantity()));
        holder1.tv_detail.setText(orderItemVoList.get(position).getDetail());
        holder1.tv_price.setText(String.valueOf(orderItemVoList.get(position).getCurrentUnitPrice().doubleValue()));
        Glide.with(mContext).load(Const.IMAGE_URI+orderItemVoList.get(position).getProductImage()).into(holder1.iv_productImage);
        holder1.ll_item_order.setId(orderItemVoList.get(position).getProductId());
        holder1.ll_item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemVoList.size();
    }

    private RecyclerExtras.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout ll_item_order;
        private ImageView iv_productImage;
        private TextView tv_title,tv_detail,tv_price,tv_quantity;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_item_order = itemView.findViewById(R.id.ll_item_order);
            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
