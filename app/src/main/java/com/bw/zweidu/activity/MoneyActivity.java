package com.bw.zweidu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.widget.TextView;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.MoneyAdapter;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.MoneyBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
public class MoneyActivity extends BaseActivity implements IView {

    private IpresenterImpl mIpresenterImpl;
    private XRecyclerView money_xrecy;
    private final int COUNT = 10;
    private int page;
    private  boolean money_flag;
    private TextView text_qian;
    private MoneyAdapter moneyAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_money;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        money_xrecy = findViewById(R.id.money_xrecy);
        text_qian = findViewById(R.id.money_qian);
        //互绑
        initPreseter();
    }

    @Override
    protected void initData() {
        initRecy();
    }

    private void initRecy() {
        page=1;
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        money_xrecy.setLayoutManager(linearLayoutManager);
        //允许刷新和加载
        money_xrecy.setLoadingMoreEnabled(true);
        money_xrecy.setPullRefreshEnabled(true);
        moneyAdapter = new MoneyAdapter(this);
        money_xrecy.setAdapter(moneyAdapter);
        money_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                initUrl(page);
            }

            @Override
            public void onLoadMore() {
                if (money_flag){
                    money_xrecy.loadMoreComplete();
                }else{
                    money_xrecy.setLoadingMoreEnabled(true);
                }
                initUrl(page);
            }
        });
        initUrl(page);
    }

    private void initUrl(int page) {
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_SELECT_MONEY_URL,page,COUNT),MoneyBean.class);
    }

    private void initPreseter() {
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
        if (object instanceof MoneyBean){
            MoneyBean moneyBean= (MoneyBean) object;
            text_qian.setText(moneyBean.getResult().getBalance()+"");
            if (page==1){
                moneyAdapter.setList(moneyBean.getResult().getDetailList());
            }else{
                moneyAdapter.addList(moneyBean.getResult().getDetailList());
            }
            //停止刷新加载
            money_xrecy.refreshComplete();
            money_xrecy.loadMoreComplete();
            page++;
            if (moneyBean.getResult().getDetailList().size()==0){
                money_flag=true;
            }else{
                money_flag=false;
            }
        }
    }

    @Override
    public void failure(String error) {

    }
}
