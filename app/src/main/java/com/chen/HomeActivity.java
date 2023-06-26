package com.chen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chen.fragment.CartFragment;
import com.chen.fragment.HomeFragment;
import com.chen.fragment.ClassifyFragment;
import com.chen.fragment.UserCenterFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_home,ll_cart,ll_classify,ll_user_center;
    public static final String HOMEFRAGMENT_TAG = "HOME";
    public static final String CARTFRAGMENT_TAG = "CART";
    public static final String CLASSIFYFRAGMENT_TAG = "CLASSIFICATION";
    public static final String USERCENTERFRAGMENT_TAG = "USERCENTER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ll_home = findViewById(R.id.ll_home);
        ll_cart = findViewById(R.id.ll_cart);
        ll_classify = findViewById(R.id.ll_classify);
        ll_user_center = findViewById(R.id.ll_user_center);
        ll_home.setOnClickListener(this);
        ll_cart.setOnClickListener(this);
        ll_classify.setOnClickListener(this);
        ll_user_center.setOnClickListener(this);
        changeContainerView(ll_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getIntExtra("id",0);
        switch (id){
            case 2:
                changeContainerView(ll_cart);
                break;
            case 3:
                changeContainerView(ll_user_center);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_home || v.getId() == R.id.ll_classify || v.getId() == R.id.ll_cart || v.getId() == R.id.ll_user_center){
            changeContainerView(v);
        }
    }

    private void changeContainerView(View v){
        ll_home.setSelected(false);
        ll_cart.setSelected(false);
        ll_classify.setSelected(false);
        ll_user_center.setSelected(false);
        v.setSelected(true);
        if (v == ll_home){
            attachFragment(HOMEFRAGMENT_TAG);
        } else if(v == ll_classify){
            attachFragment(CLASSIFYFRAGMENT_TAG);
        } else if(v == ll_cart){
            attachFragment(CARTFRAGMENT_TAG);
        } else if(v == ll_user_center){
            attachFragment(USERCENTERFRAGMENT_TAG);
        }
    }


    private void attachFragment(String fragmentTag){

        //step1:获取Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null){
            if (fragmentTag.equals(HOMEFRAGMENT_TAG)){
                fragment = new HomeFragment();
                fragmentTransaction.add(fragment,HOMEFRAGMENT_TAG);
            } else if (fragmentTag.equals(CLASSIFYFRAGMENT_TAG)){
                fragment = new ClassifyFragment();
                fragmentTransaction.add(fragment,CLASSIFYFRAGMENT_TAG);
            } else if (fragmentTag.equals(CARTFRAGMENT_TAG)){
                fragment = new CartFragment();
                fragmentTransaction.add(fragment,CARTFRAGMENT_TAG);
            } else if (fragmentTag.equals(USERCENTERFRAGMENT_TAG)){
                fragment = new UserCenterFragment();
                fragmentTransaction.add(fragment,USERCENTERFRAGMENT_TAG);
            }
        }
        fragmentTransaction.replace(R.id.ll_container,fragment,fragmentTag);
        fragmentTransaction.commit();
    }
}