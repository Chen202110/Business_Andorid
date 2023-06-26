package com.chen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.UserVo;

public class VerityActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_obtain_question;
    private EditText et_answer;
    private Button btn_verity;
    UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(this).readObject("user", UserVo.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verity);
        tv_obtain_question = findViewById(R.id.tv_obtain_question);
        et_answer = findViewById(R.id.et_answer);
        btn_verity = findViewById(R.id.btn_verity);
        btn_verity.setOnClickListener(this);
        tv_obtain_question.setText(userVo.getQuestion());
    }

    @Override
    public void onClick(View v) {
        String user_answer = et_answer.getText().toString();
        if (TextUtils.isEmpty(user_answer)){
            Toast.makeText(this,"密保答案不能为空",Toast.LENGTH_LONG).show();
        }else{
            String obtain_user_answer = userVo.getAnswer();
            if (user_answer.equals(obtain_user_answer)){
                Toast.makeText(this,"验证成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,ResetActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"您输入的密保答案有误",Toast.LENGTH_LONG).show();
            }
        }
    }
}