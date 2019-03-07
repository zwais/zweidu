package com.bw.zweidu.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.CircleListAdapter;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.CircleLikeBean;
import com.bw.zweidu.bean.CircleListBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

public class CircleFragment extends BaseFragment implements IView {

    private XRecyclerView circle_xrecy;
    private int circle_page;
    private boolean circle_flag;
    private IpresenterImpl mIpresenterImpl;
    private final int COUNT_ITEM = 5;
    private CircleListAdapter circleListAdapter;

    @Override
    protected int getlayoutResId() {
        return R.layout.circle_fragment;
    }

    @Override
    protected void initView(View view) {
        //获取资源ID
        circle_xrecy = view.findViewById(R.id.circle_xrecy);
    }

    @Override
    protected void initData() {
        initPresenter();
        initXrecy();
    }
    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }
    //解绑

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    private void initXrecy() {
        circle_page=1;
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        circle_xrecy.setLayoutManager(linearLayoutManager);

        //支持刷新和加载
        circle_xrecy.setPullRefreshEnabled(true);
        circle_xrecy.setLoadingMoreEnabled(true);
        circleListAdapter = new CircleListAdapter(getActivity());
        circle_xrecy.setAdapter(circleListAdapter);
        circle_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                circle_page=1;
                initCircleUrl(circle_page);
            }
            @Override
            public void onLoadMore() {
                if (circle_flag){
                    circle_xrecy.loadMoreComplete();

                }else{
                    circle_xrecy.setLoadingMoreEnabled(true);
                }
                //加载
                initCircleUrl(circle_page);
            }
        });
        initCircleUrl(circle_page);


    }
     //点赞
    private void needlCircleUrl(int id) {
        Map<String,String> params=new HashMap<>();
        params.put("circleId",String.valueOf(id));
        mIpresenterImpl.postRequestIpresenter(Apis.SHOW_CIRCLE_LIKE_URL,params,CircleLikeBean.class);
    }
      //取消点赞
    private void cancelCircleUrl(int id) {
        mIpresenterImpl.deleteRequestIpresenter(String.format(Apis.SHOW_CIRCLE_CANCEL_URL,id),CircleLikeBean.class);
    }
     //查询圈子
    private void initCircleUrl(int circle_page) {
       mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_CIRCLE_LIST_URL,circle_page,COUNT_ITEM),CircleListBean.class);

    }

    @Override
    public void success(Object object) {
        if (object instanceof CircleListBean){
            CircleListBean circleListBean= (CircleListBean) object;
            if (circle_page==1){
                circleListAdapter.setList(circleListBean.getResult());
            }else{
                circleListAdapter.addList(circleListBean.getResult());
            }
            //停止刷新加载
            circle_xrecy.refreshComplete();
            circle_xrecy.loadMoreComplete();
            circle_page++;
            if (circleListBean.getResult().size()==0){
                circle_flag=true;
            }else{
                circle_flag=false;
            }
            circleListAdapter.setOnClick(new CircleListAdapter.OnClick() {
                @Override
                public void getdata(int id, int great, int position) {
                    if (great==1){
                        //已点赞，需取消
                        cancelCircleUrl(id);
                        circleListAdapter.getcancel(position);
                    }else{
                        //未点赞，需点赞
                        needlCircleUrl(id);
                        circleListAdapter.getlike(position);
                    }
                }
            });
        }
        if (object instanceof CircleLikeBean){
            CircleLikeBean circleLikeBean= (CircleLikeBean) object;
            Toast.makeText(getActivity(), circleLikeBean.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void failure(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
    private long exitTime=0;
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    //双击退出
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }
}
