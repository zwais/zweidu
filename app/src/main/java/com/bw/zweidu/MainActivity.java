package com.bw.zweidu;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.CreationMsgBean;
import com.bw.zweidu.bean.LoginBean;
import com.bw.zweidu.bean.ShowShopMsgBean;
import com.bw.zweidu.customview.CustomViewpagerMain;
import com.bw.zweidu.fragment.BillFragment;
import com.bw.zweidu.fragment.CircleFragment;
import com.bw.zweidu.fragment.HomeFragment;
import com.bw.zweidu.fragment.MyFragment;
import com.bw.zweidu.fragment.ShopFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private RadioGroup group_bottom;
    private CustomViewpagerMain main_viewPager;
    private List<Fragment> list;
    private LoginBean.ResultBean result;
    private HomeFragment homeFragment;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        group_bottom = findViewById(R.id.main_buttom_group);
        main_viewPager = findViewById(R.id.main_viewpager);
        list=new ArrayList<>();
        //注册
        EventBus.getDefault().register(this);
        Intent intent=getIntent();
        result = (LoginBean.ResultBean) intent.getSerializableExtra("result");

    }

    public LoginBean.ResultBean getResult(){
        return result;
    }
    @Override
    protected void initData() {
        //添加fragment
        addFragment();
        setViewpager();
        setRadioGroup();
    }

    private void setRadioGroup() {
        group_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home_button_home:
                        main_viewPager.setCurrentItem(0);
                        break;
                    case R.id.home_button_circle:
                        main_viewPager.setCurrentItem(1);
                        break;
                    case R.id.home_button_shop:
                        main_viewPager.setCurrentItem(2);
                        break;
                    case R.id.home_button_bill:
                        main_viewPager.setCurrentItem(3);
                        break;
                    case R.id.home_button_my:
                        main_viewPager.setCurrentItem(4);
                        break;
                    default:break;
                }
            }
        });
    }

    private void setViewpager() {
        main_viewPager.addOnPageChangeListener(new CustomViewpagerMain.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        group_bottom.check(R.id.home_button_home);
                        EventBus.getDefault().post(new ShowShopMsgBean("moren","select"));
                        break;
                    case 1:
                        group_bottom.check(R.id.home_button_circle);
                        break;
                    case 2:
                        group_bottom.check(R.id.home_button_shop);
                        break;
                    case 3:
                        group_bottom.check(R.id.home_button_bill);
                        break;
                    case 4:
                        group_bottom.check(R.id.home_button_my);
                        break;
                    default:break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void addFragment() {
        homeFragment = new HomeFragment();
        list.add(homeFragment);
        list.add(new CircleFragment());
        list.add(new ShopFragment());
        list.add(new BillFragment());
        list.add(new MyFragment());
        main_viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return list.get(i);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

    /*
     * 重写onTouchEvent方法监听
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击空白区域系统软键盘消失
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (MainActivity.this.getCurrentFocus() != null) {
                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getComid(CreationMsgBean msgBean) {
        if (msgBean.getFlag().equals("send")) {
            main_viewPager.setCurrentItem(3);
            EventBus.getDefault().postSticky("open");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
