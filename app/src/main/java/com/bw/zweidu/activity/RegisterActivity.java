package com.bw.zweidu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bw.zweidu.R;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.RegisterBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.util.RegularUtil;
import com.bw.zweidu.view.IView;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity implements IView {

    private IpresenterImpl mIpresenterImpl;
    private EditText edit_phone;
    private EditText edit_pass;
    private TextView text_login;
    private Button button_login;
    private ImageView image_eye;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        edit_phone = findViewById(R.id.reg_edit_phone);
        edit_pass = findViewById(R.id.reg_edit_pass);
        text_login = findViewById(R.id.reg_text_login);
        button_login = findViewById(R.id.reg_button_login);
        image_eye = findViewById(R.id.reg_image_pass_eye);
        //互绑
        initPresenter();
    }

    @Override
    protected void initData() {
        //注册完毕跳转到登录页面
        clickLogin();
        //跳转到登录界面
        touchLogin();

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
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    //从密码可见模式变为密码不可见模式
                    edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void touchLogin() {
        text_login.setOnTouchListener(new View.OnTouchListener() {

            private float y;
            private float x;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    text_login.setTextColor(Color.parseColor("#ff6699"));
                    x = event.getX();
                    y = event.getY();
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    text_login.setTextColor(Color.parseColor("#ffffff"));
                    if (event.getX()==x||event.getY()==y){
                        Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }

                }
                return true;
            }
        });
    }

    private void clickLogin() {
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取手机号和密码
                String mphone = edit_phone.getText().toString();
                String mpass = edit_pass.getText().toString();

                //进行判断

                if (RegularUtil.isNull(mphone)){
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (RegularUtil.isNull(mphone)){
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (!(RegularUtil.isPhone(mphone))){
                    Toast.makeText(RegisterActivity.this, "手机格式错误", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (RegularUtil.isPass(mpass)){
                    Toast.makeText(RegisterActivity.this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                    return;
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
        mIpresenterImpl.postRequestIpresenter(Apis.REGISTER_URL,params,RegisterBean.class);
    }

    private void initPresenter() {
        //互绑
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
        if (object instanceof RegisterBean){
            RegisterBean registerBean= (RegisterBean) object;
            if (registerBean.getStatus().equals("0000")){
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, registerBean.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
