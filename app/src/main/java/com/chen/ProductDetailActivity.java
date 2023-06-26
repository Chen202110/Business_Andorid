package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.adapter.ProductReviewAdapter;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpCallbackFile;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.ProductListVo;
import com.chen.vo.ProductReviewVo;
import com.chen.vo.ServerResponse;
import com.chen.widget.MyCartAddDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PRODUCT_DATE = 1;
    private static final int PRODUCT_REVIEW = 2;
    private static final int SEND_REVIEW_SUCCESS = 3;
    private static final int NO_SEND_REVIEW = 4;
    private ImageView iv_productImage;
    private TextView tv_productName,tv_productPrice,tv_productStock,tv_obtain_detail,tv_no_review;
    private Button btn_add_cart,btn_send;
    private RecyclerView rv_review;
    private EditText et_review;
    //productId
    int productId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        iv_productImage = findViewById(R.id.iv_productImage);
        tv_productName = findViewById(R.id.tv_productName);
        tv_productPrice = findViewById(R.id.tv_productPrice);
        tv_productStock = findViewById(R.id.tv_productStock);
        tv_obtain_detail = findViewById(R.id.tv_obtain_detail);
        tv_no_review = findViewById(R.id.tv_no_review);
        btn_add_cart = findViewById(R.id.btn_add_cart);
        rv_review = findViewById(R.id.rv_review);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_review = findViewById(R.id.et_review);
        et_review.setOnClickListener(this);
        btn_add_cart.setOnClickListener(this);
        productId = getIntent().getIntExtra("productId",0);
        OkHttpUtils.get(Const.PORTAL_URI+"product/detail.do/"+productId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
                Gson gson = new Gson();
                ServerResponse<ProductListVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<ProductListVo>>(){}.getType());
                ProductListVo productListVo = serverResponse.getDate();
                Message message = mHandler.obtainMessage();
                message.what = PRODUCT_DATE;
                message.obj = productListVo;
                mHandler.sendMessage(message);
            }
        });
        get_product_review(productId);
    }

    private void render_product_date(ProductListVo productListVo){
        String productName = productListVo.getName();
        BigDecimal productPrice = productListVo.getPrice();
        Integer productStock = productListVo.getStock();
        String detail = productListVo.getDetail();
        String url_image = productListVo.getMainImage();
        tv_productName.setText(productName);
        tv_productPrice.setText(productPrice+"");
        tv_productStock.setText(productStock+"");
        tv_obtain_detail.setText(detail);
        Glide.with(this).load(Const.IMAGE_URI+url_image).into(iv_productImage);
    }

    private void get_product_review(Integer productId){
        OkHttpUtils.get(Const.PORTAL_URI+"review/list/"+productId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<ProductReviewVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ProductReviewVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = PRODUCT_REVIEW;
                    message.obj = serverResponse.getDate();
                    mHandler.sendMessage(message);
                }else if (status1 == 55){
                    Message message = mHandler.obtainMessage();
                    message.what = NO_SEND_REVIEW;
                    mHandler.sendMessage(message);
                }
            }
        });
    }
    private void render_product_review(List<ProductReviewVo> productReviewVoList){
        ProductReviewAdapter adapter = new ProductReviewAdapter(this,productReviewVoList);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_review.setAdapter(adapter);
        rv_review.setLayoutManager(manager);
        rv_review.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void send_review(){
        String review = et_review.getText().toString();
        if (TextUtils.isEmpty(review)){
            Toast.makeText(this,"发送内容不能为空",Toast.LENGTH_SHORT).show();
        }
        OkHttpUtils.get(Const.PORTAL_URI+"review/add.do?productId="+productId+"&reviews="+review,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = SEND_REVIEW_SUCCESS;
                    mHandler.sendMessage(message);
                }
            }
        });
        et_review.setText("");
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case PRODUCT_DATE:
                    ProductListVo productListVo = (ProductListVo) msg.obj;
                    render_product_date(productListVo);
                    break;
                case PRODUCT_REVIEW:
                    tv_no_review.setVisibility(View.GONE);
                    List<ProductReviewVo> productReviewVoList = (List<ProductReviewVo>) msg.obj;
                    render_product_review(productReviewVoList);
                    break;
                case SEND_REVIEW_SUCCESS:
                    Toast.makeText(ProductDetailActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    rv_review.setVisibility(View.VISIBLE);
                    get_product_review(productId);
                    break;
                case NO_SEND_REVIEW:
                    rv_review.setVisibility(View.GONE);
                    break;
            }
            }
        };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_cart:
                MyCartAddDialog dialog = new MyCartAddDialog(ProductDetailActivity.this,productId);
                dialog.show();
                break;
            case R.id.et_review:
                btn_send.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_send:
                send_review();
                break;
        }

    }
}