package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.R;
import com.chen.common.Const;
import com.chen.vo.CartProductVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter {
    private int is_checked;
    private Context mContext;
    private List<CartProductVo> cartProductVoList;

    public MyCartAdapter(Context context,List<CartProductVo> cartProductVos){
        this.mContext = context;
        this.cartProductVoList = cartProductVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        Glide.with(mContext).load(Const.IMAGE_URI+cartProductVoList.get(position).getProductMainImage())
                .into(holder1.iv_productImage);
        holder1.tv_title.setText(cartProductVoList.get(position).getProductName());
        holder1.tv_detail.setText(cartProductVoList.get(position).getDetail());
        holder1.tv_price.setText(String.valueOf(cartProductVoList.get(position).getProductPrice().doubleValue()));
        holder1.tv_quantity.setText(String.valueOf(cartProductVoList.get(position).getQuantity()));
        is_checked = cartProductVoList.get(position).getProductChecked();
        holder1.cb_check.setSelected(is_checked == 1);
        holder1.cb_check.setId(cartProductVoList.get(position).getProductId());
        holder1.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,1);
                }
            }
        });
        holder1.ll_cart_product.setId(cartProductVoList.get(position).getProductId());
        holder1.ll_cart_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForMark != null){
                    onItemClickListenerForMark.onItemMarkClick(view,position,2);
                }
            }
        });
        holder1.ll_cart_product.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onItemLongClick(view,position);
                }
                return true;
            }
        });
        holder1.ll_count.setId(cartProductVoList.get(position).getProductId());
        holder1.ll_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerForCount != null){
                    onItemClickListenerForCount.onItemCountClick(view,position,cartProductVoList.get(position).getQuantity());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartProductVoList.size();
    }

    private RecyclerExtras.OnItemClickListenerForMark onItemClickListenerForMark;
    public void setOnItemClickListenerForMark(RecyclerExtras.OnItemClickListenerForMark listener){
        this.onItemClickListenerForMark = listener;
    }

    private RecyclerExtras.OnItemClickListenerForCount onItemClickListenerForCount;
    public void setOnItemClickListenerForCount(RecyclerExtras.OnItemClickListenerForCount listener){
        this.onItemClickListenerForCount = listener;
    }

    private RecyclerExtras.OnItemLongClickListener onItemLongClickListener;
    public void setOnItemLongClickListener(RecyclerExtras.OnItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private CheckBox cb_check;
        private ImageView iv_productImage;
        private TextView tv_title,tv_detail,tv_price,tv_quantity;
        private LinearLayout ll_cart_product,ll_count;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            cb_check = itemView.findViewById(R.id.cb_check);
            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            ll_cart_product = itemView.findViewById(R.id.ll_cart_product);
            ll_count = itemView.findViewById(R.id.ll_count);
        }
    }
}
