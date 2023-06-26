package com.chen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.LoginActivity;
import com.chen.MyOrderActivity;
import com.chen.MyShippingActivity;
import com.chen.R;
import com.chen.UserSafeActivity;
import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.UserVo;

public class UserCenterFragment extends Fragment implements View.OnClickListener {
    TextView tv_username;
    LinearLayout ll_shipping,ll_order,ll_safe;
    private Button btn_quit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_center,container,false);
        tv_username = view.findViewById(R.id.tv_username);
        ll_shipping = view.findViewById(R.id.ll_shipping);
        ll_order = view.findViewById(R.id.ll_order);
        ll_safe = view.findViewById(R.id.ll_safe);
        btn_quit = view.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
        ll_shipping.setOnClickListener(this);
        ll_safe.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        //判断用户是否登录
        boolean isLogin = SharedPreferencesUtil.getInstance(getActivity()).readBoolean("isLogin");
        if (isLogin){//已登录
            //获取用户信息
            UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(getActivity()).readObject("user",UserVo.class);
            tv_username.setText(userVo.getUsername());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.ll_shipping:
                intent = new Intent(getActivity(), MyShippingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_safe:
                intent = new Intent(getActivity(), UserSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_quit:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_order:
                intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
