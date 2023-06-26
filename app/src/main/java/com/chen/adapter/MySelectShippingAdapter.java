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

public class MySelectShippingAdapter extends RecyclerView.Adapter {

    private View view;
    private Context mContext;
    private List<ShippingVo> shippingVoList;
    private int selectedPosition = -1;
    public MySelectShippingAdapter(Context context,List<ShippingVo> shippingVos){
        this.mContext = context;
        this.shippingVoList = shippingVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.cart_shipping_item,parent,false);
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
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,3);
                }
            }
        });
        holder1.ll_select.setId(shippingVoList.get(position).getId());
        holder1.ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,4);
                }
            }
        });
        holder1.ll_select.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return shippingVoList.size();
    }

    public void clearSelection(int position) {
        selectedPosition = position;
    }

    private RecyclerExtras.OnItemClickListenerForMark onItemClickListenerForMark;
    public void setOnItemClickListenerForMark(RecyclerExtras.OnItemClickListenerForMark listener){
        this.onItemClickListenerForMark = listener;
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_receiverName,tv_receiverPhone,tv_receiverProvince,tv_receiverCity,tv_receiverDistrict,tv_receiverAddress;
        private LinearLayout ll_shipping,ll_select;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_shipping = itemView.findViewById(R.id.ll_shipping);
            ll_select = itemView.findViewById(R.id.ll_select);
            tv_receiverName = itemView.findViewById(R.id.tv_receiverName);
            tv_receiverPhone = itemView.findViewById(R.id.tv_receiverPhone);
            tv_receiverDistrict = itemView.findViewById(R.id.tv_receiverDistrict);
            tv_receiverCity = itemView.findViewById(R.id.tv_receiverCity);
            tv_receiverAddress = itemView.findViewById(R.id.tv_receiverAddress);
            tv_receiverProvince = itemView.findViewById(R.id.tv_receiverProvince);
        }
    }
}
