package com.bw.zweidu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.MainActivity;
import com.bw.zweidu.R;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.LoginBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.util.RegularUtil;
import com.bw.zweidu.view.IView;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements IView {

    private EditText edit_phone;
    private EditText edit_pass;
    private CheckBox box_remember;
    private TextView text_reg;
    private Button button_reg;
    private SharedPreferences mSharedPreferences;
    private IpresenterImpl mIpresenterImpl;
    private SharedPreferences.Editor edit;
    private ImageView image_eye;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        edit_phone = findViewById(R.id.login_edit_phone);
        edit_pass = findViewById(R.id.login_edit_pass);
        box_remember = findViewById(R.id.login_box_remember);
        text_reg = findViewById(R.id.login_text_reg);
        button_reg= findViewById(R.id.login_button_login);
        image_eye = findViewById(R.id.login_image_pass_eye);
        mSharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        //互绑
        initPresenter();
    }

    @Override
    protected void initData() {
        //记住密码
        getEdit();
        //登录
        clickMain();
        //跳转到注册界面
        touchRegister();

        //触摸显示密码
        touchEye();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void touchEye() {


        image_eye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //判断事件的动作，按下，抬起
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    //从密码不可见模式变为密码可见模式
                    edit_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else if (event.getAction()==MotionEvent.ACTION_UP) {

                        //从密码可见模式变为密码不可见模式
                        edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });
    }

    private void getEdit() {
        //获取记住密码的状态值
        boolean box_ischeck = mSharedPreferences.getBoolean("box_ischeck", false);
        if (box_ischeck){
            String phone = mSharedPreferences.getString("phone", null);
            String pass = mSharedPreferences.getString("pass", null);
            edit_phone.setText(phone);
            edit_pass.setText(pass);
            box_remember.setChecked(true);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void touchRegister() {
        text_reg.setOnTouchListener(new View.OnTouchListener() {

            private float x;
            private float y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    text_reg.setTextColor(Color.parseColor("#ff6699"));
                    x = event.getX();
                    y = event.getY();
                }else if(event.getAction()==MotionEvent.ACTION_UP) {
                    text_reg.setTextColor(Color.parseColor("#ffffff"));
                    if (event.getX() == x || event.getY() == y) {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return true;
            }
        });

    }

    private void clickMain() {
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取手机号和密码
                String mphone = edit_phone.getText().toString();
                String mpass = edit_pass.getText().toString();

                //进行判断

                if (RegularUtil.isNull(mphone)){
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (RegularUtil.isNull(mphone)){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (!(RegularUtil.isPhone(mphone))){
                    Toast.makeText(LoginActivity.this, "手机格式错误", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (RegularUtil.isPass(mpass)){
                    Toast.makeText(LoginActivity.this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断复选框是否选中
                if (box_remember.isChecked()){
                    //记住密码的状态
                    edit.putString("phone",mphone);
                    edit.putString("pass",mpass);
                    edit.putBoolean("box_ischeck",true);
                    edit.commit();

                }else{
                    //清除所有的状态
                    edit.clear();
                    edit.commit();
                }

                //发送网络请求
                getUrl(mphone,mpass);
            }
        });
    }

    private void getUrl(String mphone, String mpass) {
        Map<String,String> params = new HashMap<>();
        params.put("phone",mphone);
        params.put("pwd",mpass);
        mIpresenterImpl.postRequestIpresenter(Apis.LOGIN_URL,params,LoginBean.class);
    }

    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    @Override
    public void success(Object object) {
        if (object instanceof LoginBean){
            LoginBean loginBean= (LoginBean) object;
            if(loginBean.getStatus().equals("0000")) {
                LoginBean.ResultBean result = loginBean.getResult();
                Log.i("TAG_ID",result.getUserId()+"     "+result.getSessionId());
                edit.putString("sessionId", result.getSessionId());
                edit.putString("userId", result.getUserId()+"");
                edit.commit();

                //跳转到主界面进行商品展示
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("result", (Serializable) result);
                startActivity(intent);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, loginBean.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void failure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
