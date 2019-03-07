package com.bw.zweidu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.widget.ImageView;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.SelectCircleAdapter;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.SelectCircleBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class CircleActivity extends BaseActivity implements IView {

    private XRecyclerView cir_xrecy;
    private ImageView image_delete;
    private IpresenterImpl mIpresenterImpl;
    private final int COUNT = 10;
    private int page;
    private  boolean circle_flag;
    private SelectCircleAdapter selectCircleAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_circle;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        cir_xrecy = findViewById(R.id.cir_xrecy);
        image_delete = findViewById(R.id.cir_image_delete);
    }

    @Override
    protected void initData() {
        //互绑
        initPresenter();
        //初始化xrecy
        initXrecy();
    }

    private void initXrecy() {
        page=1;
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        cir_xrecy.setLayoutManager(linearLayoutManager);
        //允许刷新和加载
        cir_xrecy.setLoadingMoreEnabled(true);
        cir_xrecy.setPullRefreshEnabled(true);
        selectCircleAdapter = new SelectCircleAdapter(this);
        cir_xrecy.setAdapter(selectCircleAdapter);

        cir_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                initSelectUrl(page);
            }

            @Override
            public void onLoadMore() {
                if (circle_flag){
                    cir_xrecy.loadMoreComplete();
                }else{
                    cir_xrecy.setLoadingMoreEnabled(true);
                }
                initSelectUrl(page);
            }
        });
        initSelectUrl(page);
    }
    //查询圈子
    private void initSelectUrl(int page) {
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_SELECT_CIRCLE_URL,page,COUNT),SelectCircleBean.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    public void success(Object object) {
        if (object instanceof SelectCircleBean){
            SelectCircleBean selectCircleBean= (SelectCircleBean) object;
            if (page==1){
                selectCircleAdapter.setList(selectCircleBean.getResult());
            }else{
                selectCircleAdapter.addList(selectCircleBean.getResult());
            }
            //停止刷新加载
            cir_xrecy.refreshComplete();
            cir_xrecy.loadMoreComplete();
            page++;
            if (selectCircleBean.getResult().size()==0){
                circle_flag=true;
            }else{
                circle_flag=false;
            }
        }
    }

    @Override
    public void failure(String error) {

    }
}
