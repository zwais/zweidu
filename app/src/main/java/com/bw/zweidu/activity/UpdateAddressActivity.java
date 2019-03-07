package com.bw.zweidu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.AddressListBean;
import com.bw.zweidu.bean.UpAddressBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.google.gson.Gson;
import com.lljjcoder.citypickerview.widget.CityPicker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class UpdateAddressActivity extends BaseActivity implements IView {


    private String name;
    private String phone;
    private String address;
    private String zipCode;
    private EditText edit_name;
    private EditText edit_phone;
    private TextView text_city;
    private EditText edit_address;
    private EditText edit_zipCode;
    private ImageView image_open;
    private Button button_save;
    private String mId;
    private IpresenterImpl mIpresenterImpl;
    private String city;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_address;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        edit_name = findViewById(R.id.update_address_edit_name);
        edit_phone = findViewById(R.id.update_address_edit_phone);
        text_city = findViewById(R.id.update_address_text_city);
        edit_address = findViewById(R.id.update_address_edit_address);
        edit_zipCode = findViewById(R.id.update_address_edit_zipCode);
        image_open = findViewById(R.id.update_address_image_open);
        button_save = findViewById(R.id.update_address_text_save);
        //互绑
        initPresenter();
    }
    @Override
    protected void initData() {
        //获取传递过来的值
        initIntent();
        //设置值
        initEdit();
        //点击选择城市
        setCity();

        //点击修改
        setSave();
    }

    private void setSave() {
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String save_name = edit_name.getText().toString();
                String save_phone = edit_phone.getText().toString();
                String save_city = text_city.getText().toString();
                String save_add = edit_address.getText().toString();
                String save_zipCode = edit_zipCode.getText().toString();
                AddressListBean addressListBean=new AddressListBean(save_city,save_add);
                String save_address = new Gson().toJson(addressListBean);
                Map<String,String> params=new HashMap<>();
                params.put("id",mId);
                params.put("realName",save_name);
                params.put("phone",save_phone);
                params.put("address",save_address);
                params.put("zipCode",save_zipCode);
                mIpresenterImpl.putRequestIpresenter(Apis.SHOW_UPDATE_ADDRESS_URL,params,UpAddressBean.class);
            }
        });
    }

    private void setCity() {
        image_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cityPicker.show();
                showPicker();
            }
        });
    }

    private void initEdit() {
        edit_name.setText(name);
        edit_phone.setText(phone);
        edit_zipCode.setText(zipCode);
        text_city.setText(city);
        edit_address.setText(address);

    }

    private void initIntent() {
        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        city = intent.getStringExtra("city");
        address=intent.getStringExtra("address");
        zipCode = intent.getStringExtra("zipCode");
         mId = intent.getStringExtra("Id");


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
        if (object instanceof UpAddressBean){
            UpAddressBean upAddressBean= (UpAddressBean) object;
            if (upAddressBean.getStatus().equals("0000")){
                Toast.makeText(this, upAddressBean.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateAddressActivity.this,AddressActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, upAddressBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }

    private void showPicker() {
        CityPicker cityPicker = new CityPicker.Builder(UpdateAddressActivity.this)
                .textSize(14)
                .title("请选择所在地区")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#ff0000")
                .cancelTextColor("#696969")
                .province(" 北京市")
                .city("北京市")
                .district("昌平区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                text_city.setText(province+" "+city+" "+district+" ");
            }
        });
    }


}
