package com.chen.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.AddShippingActivity;
import com.chen.MyShippingActivity;
import com.chen.ProductDetailActivity;
import com.chen.R;
import com.chen.SettlementActivity;
import com.chen.adapter.MyCartAdapter;
import com.chen.adapter.MySelectShippingAdapter;
import com.chen.bean.MessageEvent;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CartProductVo;
import com.chen.vo.CartVo;
import com.chen.vo.ServerResponse;
import com.chen.vo.ShippingVo;
import com.chen.widget.MyCartCountDialog;
import com.chen.widget.RecyclerExtras;
import com.chen.widget.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CartFragment extends Fragment implements View.OnClickListener, RecyclerExtras.OnItemClickListenerForMark,RecyclerExtras.OnItemClickListenerForCount,RecyclerExtras.OnItemLongClickListener{
    private RecyclerView rv_content,rv_shipping;
    private CheckBox cb_all;
    private TextView tv_total_price,tv_go_shipping;
    private Button btn_buy,btn_add_shipping;
    MySelectShippingAdapter mySelectShippingAdapter;
    private Boolean is_checked = false;
    private Integer ShippingId;
    private int total_checks = 0;
    private static final int CART_NOTIFY = 1;
    private static final int CHOICE_ALL = 2;
    private static final int CHOICE_ITEM = 3;
    private static final int CART_DELETE = 4;
    private static final int SHIPPING_DATE = 5;
    private static final int SHIPPING_NULL = 6;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cart,container,false);
        rv_content = view.findViewById(R.id.rv_content);
        rv_shipping = view.findViewById(R.id.rv_shipping);
        cb_all = view.findViewById(R.id.cb_all);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        tv_go_shipping = view.findViewById(R.id.tv_go_shipping);
        btn_buy = view.findViewById(R.id.btn_buy);
        btn_add_shipping = view.findViewById(R.id.btn_add_shipping);
        btn_add_shipping.setOnClickListener(this);
        cb_all.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        EventBus.getDefault().register(this);
        getCartDate();
        getShippingDate();
        return view;
    }

    private void getCartDate(){
        OkHttpUtils.get(Const.PORTAL_URI+"cart/list.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    CartVo cartVo = serverResponse.getDate();
                    Message message = new Message();
                    message.obj = cartVo;
                    message.what = CART_NOTIFY;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void render_cart(CartVo cartVo){
        tv_total_price.setText(String.valueOf(cartVo.getCartTotalPrice().doubleValue()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void render_cart_item(CartVo cartVo){
        List<CartProductVo> cartProductVoList = cartVo.getCartProductVoList();
        total_checks = 0;
        for (CartProductVo cartProductVo:cartProductVoList){
            int check = cartProductVo.getProductChecked();
            total_checks += check;
        }
        int CartProductVoListSize = cartProductVoList.size();
        cb_all.setSelected(total_checks == CartProductVoListSize);
        MyCartAdapter adapter = new MyCartAdapter(getActivity(),cartProductVoList);
        adapter.setOnItemClickListenerForMark(this);
        adapter.setOnItemClickListenerForCount(this);
        adapter.setOnItemLongClickListener(this);
        adapter.notifyDataSetChanged();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_content.setLayoutManager(manager);
        rv_content.setAdapter(adapter);
        rv_content.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

//    private void render_choice(CartVo cartVo){
//        int total_checks = 0;
//        List<CartProductVo> cartProductVoList = cartVo.getCartProductVoList();
//        for (CartProductVo cartProductVo:cartProductVoList){
//            int check = cartProductVo.getProductChecked();
//            total_checks += check;
//        }
//        int CartProductVoListSize = cartProductVoList.size();
//        cb_all.setSelected(total_checks == CartProductVoListSize);
//        MyCartAdapter adapter = new MyCartAdapter(getActivity(),cartProductVoList);
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        rv_content.setLayoutManager(manager);
//        rv_content.setAdapter(adapter);
//        rv_content.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//    }

    /**
     *全选或者全不选
     * */
    private void getChoiceAll(){
        OkHttpUtils.get(Const.PORTAL_URI+"cart/choice",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    CartVo cartVo = serverResponse.getDate();
                    Message message = new Message();
                    message.obj = cartVo;
                    message.what = CHOICE_ALL;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 单选
     * */
    private void getChoiceItem(int productId){
        OkHttpUtils.get(Const.PORTAL_URI+"cart/choice?productId="+productId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    CartVo cartVo = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.obj = cartVo;
                    message.what = CHOICE_ITEM;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void getShippingDate(){
        OkHttpUtils.get(Const.PORTAL_URI+"shipping/list.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<ShippingVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ShippingVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<ShippingVo> shippingVoList = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.obj = shippingVoList;
                    message.what = SHIPPING_DATE;
                    mHandler.sendMessage(message);
                }
                if (status1 == 35){
                    Message message = mHandler.obtainMessage();
                    message.what = SHIPPING_NULL;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void render_shipping(List<ShippingVo> shippingVos){
        mySelectShippingAdapter = new MySelectShippingAdapter(getContext(),shippingVos);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv_shipping.setLayoutManager(manager);
        rv_shipping.setAdapter(mySelectShippingAdapter);
        rv_shipping.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mySelectShippingAdapter.setOnItemClickListenerForMark(this);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case CART_NOTIFY:
                case CHOICE_ALL:
                case CHOICE_ITEM:
                    CartVo cartVo = (CartVo) msg.obj;
                    if (cartVo.getCartProductVoList() != null || cartVo.getCartTotalPrice() != null){
                        render_cart(cartVo);
                        render_cart_item(cartVo);
                        tv_go_shipping.setVisibility(View.GONE);
                        rv_content.setVisibility(View.VISIBLE);
                    } else {
                        tv_go_shipping.setVisibility(View.VISIBLE);
                        rv_content.setVisibility(View.GONE);
                        tv_total_price.setText("0.00");
                    }
                    break;
                case CART_DELETE:
                    Toast.makeText(getContext(),"商品删除成功",Toast.LENGTH_SHORT).show();
                    CartVo cartVo1 = (CartVo) msg.obj;
                    if (cartVo1.getCartProductVoList() != null || cartVo1.getCartTotalPrice() != null){
                        render_cart(cartVo1);
                        render_cart_item(cartVo1);
                        tv_go_shipping.setVisibility(View.GONE);
                        rv_content.setVisibility(View.VISIBLE);
                    } else {
                        tv_go_shipping.setVisibility(View.VISIBLE);
                        rv_content.setVisibility(View.GONE);
                        tv_total_price.setText("0.00");
                        total_checks = 0;
                    }
                    break;
                case SHIPPING_DATE:
                    btn_add_shipping.setVisibility(View.GONE);
                    List<ShippingVo> shippingVoList = (List<ShippingVo>) msg.obj;
                    render_shipping(shippingVoList);
                    break;
                case SHIPPING_NULL:
                    rv_shipping.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cb_all:
                getChoiceAll();
                break;
            case R.id.btn_buy:
                if (is_checked == false){
                    Toast.makeText(getContext(),"请选一个收货地址",Toast.LENGTH_SHORT).show();
                } else if (total_checks == 0){
                    Toast.makeText(getContext(),"购物车为空或者没有被选中的商品",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SettlementActivity.class);
                    intent.putExtra("ShippingId",ShippingId);
                    startActivity(intent);
                }
                break;
            case R.id.btn_add_shipping:
                Intent intent = new Intent(getActivity(), AddShippingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemMarkClick(View view, int position, Integer mark) {
        switch (mark){
            case 1:
                getChoiceItem(view.getId());
                break;
            case 2:
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productId",view.getId());
                startActivity(intent);
                break;
            case 3:
                Intent intent1 = new Intent(getActivity(), MyShippingActivity.class);
                startActivity(intent1);
                break;
            case 4:
                mySelectShippingAdapter.clearSelection(position);
                mySelectShippingAdapter.notifyDataSetChanged();
                ShippingId = view.getId();
                is_checked = true;
                break;
        }
    }

    @Override
    public void onItemCountClick(View view, int position, int count) {
        MyCartCountDialog dialog = new MyCartCountDialog(getActivity(),view.getId(),count);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventMain(MessageEvent messageEvent){
        CartVo cartVo = messageEvent.getCartVo();
        render_cart(cartVo);
        render_cart_item(cartVo);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("提示");
        dialog.setMessage("您真的要从购物车中删除该商品吗？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OkHttpUtils.get(Const.PORTAL_URI+"cart/delete.do/"+view.getId(),new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        Gson gson = new Gson();
                        ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                        int status1 = serverResponse.getStatus();
                        if (status1 == 0){
                            CartVo cartVo = serverResponse.getDate();
                            Message message = mHandler.obtainMessage();
                            message.obj = cartVo;
                            message.what = CART_DELETE;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
