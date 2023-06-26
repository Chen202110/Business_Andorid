package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.R;
import com.chen.vo.ShippingVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyShippingAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ShippingVo> shippingVoList;
    public MyShippingAdapter(Context context, List<ShippingVo> shippingVos){
        this.mContext = context;
        this.shippingVoList = shippingVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shipping_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_receiverName.setText(shippingVoList.get(position).getReceiverName());
        holder1.tv_receiverProvince.setText(shippingVoList.get(position).getReceiverProvince());
        holder1.tv_receiverAddress.setText(shippingVoList.get(position).getReceiverAddress());
        holder1.tv_receiverCity.setText(shippingVoList.get(position).getReceiverCity());
        holder1.tv_receiverDistrict.setText(shippingVoList.get(position).getReceiverDistrict());
        holder1.tv_receiverPhone.setText(shippingVoList.get(position).getReceiverPhone());
        holder1.ll_shipping.setId(shippingVoList.get(position).getId());
        holder1.ll_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
        holder1.ll_shipping.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onItemLongClick(view,position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return shippingVoList.size();
    }

    private RecyclerExtras.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private RecyclerExtras.OnItemLongClickListener onItemLongClickListener;
    public void setOnItemLongClickListener(RecyclerExtras.OnItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_receiverName,tv_receiverPhone,tv_receiverProvince,tv_receiverCity,tv_receiverDistrict,tv_receiverAddress;
        private LinearLayout ll_shipping;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_shipping = itemView.findViewById(R.id.ll_shipping);
            tv_receiverName = itemView.findViewById(R.id.tv_receiverName);
            tv_receiverPhone = itemView.findViewById(R.id.tv_receiverPhone);
            tv_receiverDistrict = itemView.findViewById(R.id.tv_receiverDistrict);
            tv_receiverCity = itemView.findViewById(R.id.tv_receiverCity);
            tv_receiverAddress = itemView.findViewById(R.id.tv_receiverAddress);
            tv_receiverProvince = itemView.findViewById(R.id.tv_receiverProvince);
        }
    }
}
