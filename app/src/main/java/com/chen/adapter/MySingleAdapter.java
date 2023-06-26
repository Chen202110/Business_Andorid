package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.R;
import com.chen.common.Const;
import com.chen.vo.OrderVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MySingleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<OrderVo> orderVoList;
    private View view;

    public MySingleAdapter(Context context,List<OrderVo> orderVos){
        this.mContext = context;
        this.orderVoList = orderVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_total_price.setText(String.valueOf(orderVoList.get(position).getPayment().doubleValue()));
        holder1.tv_statusDesc.setText(orderVoList.get(position).getStatusDesc());
        holder1.tv_order_no.setText(String.valueOf(orderVoList.get(position).getOrderNo()));
        holder1.tv_count.setText(String.valueOf(orderVoList.get(position).getOrderItemVoList().size()));
        Glide.with(mContext).load(Const.IMAGE_URI+orderVoList.get(position).getOrderItemVoList().get(0).getProductImage()).into(holder1.iv_productImage);
        holder1.ll_item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemOrderListener != null){
                    onItemOrderListener.onItemNameAndMarkClick(view,position,String.valueOf(orderVoList.get(position).getOrderNo()),1);
                }
            }
        });
        holder1.btn_delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteOrderClickListener != null){
                    onDeleteOrderClickListener.onItemNameAndMarkClick(view,position,String.valueOf(orderVoList.get(position).getOrderNo()),2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderVoList.size();
    }

    private RecyclerExtras.OnItemClickListenerForNameAndMark onDeleteOrderClickListener;
    public void setOnDeleteOrderClickListener(RecyclerExtras.OnItemClickListenerForNameAndMark listener){
        this.onDeleteOrderClickListener = listener;
    }

    private RecyclerExtras.OnItemClickListenerForNameAndMark onItemOrderListener;
    public void setOnItemOrderListener(RecyclerExtras.OnItemClickListenerForNameAndMark listener){
        this.onItemOrderListener = listener;
    }


    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_statusDesc,tv_order_no,tv_count,tv_total_price;
        private LinearLayout ll_item_order;
        private Button btn_delete_order;
        private ImageView iv_productImage;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_statusDesc = itemView.findViewById(R.id.tv_statusDesc);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            ll_item_order = itemView.findViewById(R.id.ll_item_order);
            btn_delete_order = itemView.findViewById(R.id.btn_delete_order);
        }
    }
}
