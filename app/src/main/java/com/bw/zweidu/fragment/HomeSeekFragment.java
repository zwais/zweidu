package com.bw.zweidu.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.ShowClassifyAdapter;
import com.bw.zweidu.adapter.ShowNavigationOneAdapter;
import com.bw.zweidu.adapter.ShowNavigationTwoAdapter;
import com.bw.zweidu.adapter.ShowSearchAdapter;
import com.bw.zweidu.adapter.ShowTwoShopAdapter;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.ShowClassifyBean;
import com.bw.zweidu.bean.ShowNavigationMsgBean;
import com.bw.zweidu.bean.ShowNavigationOneBean;
import com.bw.zweidu.bean.ShowNavigationShopBean;
import com.bw.zweidu.bean.ShowNavigationTwoBean;
import com.bw.zweidu.bean.ShowSearchBean;
import com.bw.zweidu.bean.ShowShopMsgBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeSeekFragment extends BaseFragment implements IView {

    private XRecyclerView seek_xrecy;
    private ImageView seek_navigation;
    private ImageView seek_search;
    private ImageView seek_classify_image;
    private TextView seek_classify_text;
    private int page;
    private IpresenterImpl mIpresenterImpl;
    private final int URL_COUNT=5;
    private final int COUNT_ITEM=2;
    private String shop_id;
    private boolean show_shop_flag;
    private boolean show_search_flag;
    private boolean show_navigation_flag;
    private ShowClassifyAdapter showClassifyAdapter;
    private EditText seek_search_edit;
    private ImageView seek_search_image;
    private String text;
    private XRecyclerView seek_search_xrecy;
    private ShowSearchAdapter showSearchAdapter;
    private int search_page;
    private LinearLayout seek_no_lin;
    private LinearLayout seek_navigation_lin;
    private ShowNavigationOneAdapter showNavigationOneAdapter;
    private ShowNavigationTwoAdapter showNavigationTwoAdapter;
    private PopupWindow popupWindow;
    private int two_shop_page;
    private XRecyclerView seek_navigation_xrecy;
    private ShowTwoShopAdapter showTwoShopAdapter;
    private RecyclerView pop_navigation_one;
    private RecyclerView pop_navigation_two;
    private String navigationid;
    private Boolean pop_flag;
    @Override
    protected int getlayoutResId() {
        return R.layout.home_seek_fragment;
    }

    @Override
    protected void initView(View view) {
        //获取资源ID
        seek_xrecy = view.findViewById(R.id.seek_xrecy);
        seek_navigation = view.findViewById(R.id.seek_navigation);
        seek_search = view.findViewById(R.id.seek_search);
        seek_classify_image = view.findViewById(R.id.seek_classify_image);
        seek_classify_text = view.findViewById(R.id.seek_classify_text);
        seek_search_edit = view.findViewById(R.id.seek_search_edit);
        seek_search_image = view.findViewById(R.id.seek_search_image);
        seek_search_xrecy = view.findViewById(R.id.seek_search_xrecy);
        seek_no_lin = view.findViewById(R.id.seek_no_lin);
        seek_navigation_lin = view.findViewById(R.id.seek_navigation_lin);
        seek_navigation_xrecy = view.findViewById(R.id.seek_navigation_xrecy);
        ConstraintLayout seek_con=view.findViewById(R.id.seek_con);
        //注册
        EventBus.getDefault().register(this);
        initPresenter();
        //点击空白区域系统软键盘消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                getActivity().onTouchEvent(motionEvent);
                getVisibility(true);

                return false;
            }
        });
        seek_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    protected void initData() {
        //初始化分类xrecy
        initXrecy();
        //初始化搜索商品xrecy
        initSearchXrecy();
        //点击按钮显示搜索框和搜索按钮
        initSearch();
        //点击搜索框进行搜索
        initSearchShop();
        //初始化popupwindow
        initPoPupwindow();
        //初始化一级列表
        initNavigationOne();
        //初始化二级列表
        initNavigationTwo();
        //初始化二级商品xrecy
        initNavigationXrecy();
    }

    private void initNavigationXrecy() {

        //布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),COUNT_ITEM);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        seek_navigation_xrecy.setLayoutManager(gridLayoutManager);
        //支持刷新和加载
        seek_navigation_xrecy.setPullRefreshEnabled(true);
        seek_navigation_xrecy.setLoadingMoreEnabled(true);

        seek_navigation_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                two_shop_page=1;
                initTwoShopUrl(navigationid,two_shop_page);
            }

            @Override
            public void onLoadMore() {
                if (show_navigation_flag){
                    seek_search_xrecy.loadMoreComplete();

                }else{
                    seek_search_xrecy.setLoadingMoreEnabled(true);
                }
                //加载
                initTwoShopUrl(navigationid,two_shop_page);
            }
        });
        showTwoShopAdapter = new ShowTwoShopAdapter(getActivity());
        seek_navigation_xrecy.setAdapter(showTwoShopAdapter);
    }

    private void initPoPupwindow() {

        // 用于PopupWindow的View
        View contentView=LayoutInflater.from(getActivity()).inflate(R.layout.pop_navigation, null, false);
        pop_navigation_one = contentView.findViewById(R.id.pop_navigation_one);
        pop_navigation_two = contentView.findViewById(R.id.pop_navigation_two);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击，即：事件拦截消费
        popupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色
        ColorDrawable dw = new ColorDrawable(getActivity().getResources().getColor(R.color.colorpop));
        // 设置弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);

        seek_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seek_navigation_xrecy.setVisibility(View.VISIBLE);
                initOneUrl();
                initTwoUrl("1001002");
               getOthreVisibility();
                popupWindow.showAsDropDown(v,0,25);
            }
        });
        popupWindow.dismiss();
    }

    private void getOthreVisibility() {
        seek_search_edit.setVisibility(View.INVISIBLE);
        seek_search_image.setVisibility(View.INVISIBLE);
        seek_search.setVisibility(View.VISIBLE);
        seek_classify_image.setVisibility(View.GONE);
        seek_classify_text.setVisibility(View.GONE);
        seek_xrecy.setVisibility(View.GONE);
        seek_search_xrecy.setVisibility(View.GONE);
        seek_no_lin.setVisibility(View.GONE);
        seek_xrecy.setVisibility(View.GONE);
        seek_search_xrecy.setVisibility(View.GONE);
        seek_navigation_lin.setVisibility(View.GONE);
    }

    private void initNavigationTwo() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        pop_navigation_two.setLayoutManager(linearLayoutManager);
        showNavigationTwoAdapter = new ShowNavigationTwoAdapter(getActivity());
        pop_navigation_two.setAdapter(showNavigationTwoAdapter);
    }

    private void initNavigationOne() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        pop_navigation_one.setLayoutManager(linearLayoutManager);

        showNavigationOneAdapter = new ShowNavigationOneAdapter(getActivity());
        pop_navigation_one.setAdapter(showNavigationOneAdapter);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNavigationOne(ShowNavigationMsgBean bean){
        if (bean.getFlag().equals("one")){
            String firstCategoryId = (String) bean.getObject();
            initTwoUrl(firstCategoryId);
        }
        if (bean.getFlag().equals("two")){
            String categoryId = (String) bean.getObject();
            two_shop_page=1;
            initTwoShopUrl(categoryId,two_shop_page);
        }
    }

    private void initTwoShopUrl(String categoryId, int two_shop_page) {
        navigationid=categoryId;
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_NAV_TWO_SHOP_URL,categoryId,two_shop_page,URL_COUNT),ShowNavigationShopBean.class);
    }

    private void initTwoUrl(String firstCategoryId) {
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_NAV_TWO_URL,firstCategoryId),ShowNavigationTwoBean.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void get(ShowShopMsgBean msgBean){

        switch (msgBean.getFlag()){
            //热销新品
            case "new":
                getVisibility(true);
                page=1;
                seek_classify_image.setImageResource(R.mipmap.bg_rxxp_syf);
                seek_classify_text.setText("热销新品");
                String newid = (String) msgBean.getObject();
                initClassifyUrl(newid,page);
                break;
            //魔力时尚
            case "fashion":
                getVisibility(true);
                page=1;
                seek_classify_image.setImageResource(R.mipmap.bg_mlss_syf);
                seek_classify_text.setText("魔力时尚");
                String fashionid = (String) msgBean.getObject();
                initClassifyUrl(fashionid,page);
                break;
            //品质生活
            case "live":
                getVisibility(true);
                page=1;
                seek_classify_image.setImageResource(R.mipmap.bg_pzsh_syf);
                seek_classify_text.setText("品质生活");
                String liveid = (String) msgBean.getObject();
                initClassifyUrl(liveid,page);
                break;
            case "search":
                //搜索框和搜索按钮显示
                getVisibility(false);
                //自动加载软键盘
                setEditTextState();
                break;
            case "navigation":
                View vv= (View) msgBean.getObject();
                seek_navigation_xrecy.setVisibility(View.VISIBLE);
                getOthreVisibility();
                popupWindow.showAsDropDown(vv,0,25);
                initOneUrl();
                initTwoUrl("1001002");
                initTwoShopUrl("1001002001",1);
                break;
            default:break;
        }

    }



    private void initOneUrl() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_NAV_ONE_URL,ShowNavigationOneBean.class);
    }

    private void initSearch() {
        seek_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisibility(false);
                seek_no_lin.setVisibility(View.GONE);
                //自动加载软键盘
                setEditTextState();
            }
        });
    }

    private void initSearchShop() {
        seek_search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seek_search_xrecy.setVisibility(View.VISIBLE);
                search_page=1;
                String shop_text = seek_search_edit.getText().toString();
                if (!(shop_text.equals(""))){
                    initReachUrl(shop_text,search_page);
                }
                hideKeyboard(v);
                seek_no_lin.setVisibility(View.GONE);
                seek_search_edit.setVisibility(View.INVISIBLE);
                seek_search_image.setVisibility(View.INVISIBLE);
                seek_search.setVisibility(View.VISIBLE);
                //seek_search_image.setDuplicateParentStateEnabled(true);
            }
        });
    }

    private void initSearchXrecy() {
        //布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),COUNT_ITEM);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        seek_search_xrecy.setLayoutManager(gridLayoutManager);
        //支持刷新和加载
        seek_search_xrecy.setPullRefreshEnabled(true);
        seek_search_xrecy.setLoadingMoreEnabled(true);

        seek_search_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                search_page=1;
                initReachUrl(text,search_page);
            }

            @Override
            public void onLoadMore() {
                if (show_search_flag){
                    seek_search_xrecy.loadMoreComplete();

                }else{
                    seek_search_xrecy.setLoadingMoreEnabled(true);
                }
                //加载
                initReachUrl(text,search_page);
            }
        });
        showSearchAdapter = new ShowSearchAdapter(getActivity());
        seek_search_xrecy.setAdapter(showSearchAdapter);
    }

    private void initXrecy() {
        //布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),COUNT_ITEM);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        seek_xrecy.setLayoutManager(gridLayoutManager);
        //支持刷新和加载
        seek_xrecy.setPullRefreshEnabled(true);
        seek_xrecy.setLoadingMoreEnabled(true);
        seek_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                page=1;
                initClassifyUrl(shop_id,page);

            }

            @Override
            public void onLoadMore() {
                if (show_shop_flag){
                    seek_xrecy.loadMoreComplete();
                    Toast.makeText(getActivity(), "没有更多商品啦", Toast.LENGTH_SHORT).show();
                }else{
                    seek_xrecy.setLoadingMoreEnabled(true);
                }
                //加载
                initClassifyUrl(shop_id,page);

            }
        });
        showClassifyAdapter = new ShowClassifyAdapter(getActivity());
        seek_xrecy.setAdapter(showClassifyAdapter);
    }


    private void initClassifyUrl(String id, int page) {
        shop_id=id;
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_CLASSIFY_URL,id,page,URL_COUNT),ShowClassifyBean.class);
    }

    private void initReachUrl(String shop_text, int search_page) {
        text=shop_text;
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_SEARCH_URL,shop_text,search_page,URL_COUNT),ShowSearchBean.class);
    }

    public void getVisibility(Boolean flag){
        if (flag){
            seek_search_edit.setVisibility(View.INVISIBLE);
            seek_search_image.setVisibility(View.INVISIBLE);
            seek_search.setVisibility(View.VISIBLE);
            seek_classify_image.setVisibility(View.VISIBLE);
            seek_classify_text.setVisibility(View.VISIBLE);
            seek_xrecy.setVisibility(View.VISIBLE);
            seek_search_xrecy.setVisibility(View.GONE);
            seek_no_lin.setVisibility(View.GONE);
            seek_navigation_xrecy.setVisibility(View.GONE);
        }else{
            seek_search_edit.setVisibility(View.VISIBLE);
            seek_search_image.setVisibility(View.VISIBLE);
            seek_search.setVisibility(View.GONE);
            seek_classify_image.setVisibility(View.GONE);
            seek_classify_text.setVisibility(View.GONE);
            seek_xrecy.setVisibility(View.GONE);
            seek_search_xrecy.setVisibility(View.VISIBLE);
            seek_navigation_lin.setVisibility(View.GONE);
            seek_navigation_xrecy.setVisibility(View.GONE);
        }
    }

    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //反注册
        EventBus.getDefault().unregister(this);
        seek_search_edit.setText("");

    }
    /*private long exitTime=0;
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
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    @Override
    public void success(Object object) {
        if (object instanceof ShowClassifyBean){
            ShowClassifyBean showClassifyBean= (ShowClassifyBean) object;
            if (page==1){
                showClassifyAdapter.setList(showClassifyBean.getResult());
            }else{
                showClassifyAdapter.addList(showClassifyBean.getResult());
            }
            //停止刷新加载
            seek_xrecy.refreshComplete();
            seek_xrecy.loadMoreComplete();
            page++;
            if (showClassifyBean.getResult().size()==0){
                show_shop_flag=true;
            }else{
                show_shop_flag=false;
            }

        }
        if (object instanceof ShowSearchBean){
            ShowSearchBean showSearchBean= (ShowSearchBean) object;
            if (search_page==1){
                showSearchAdapter.setList(showSearchBean.getResult());
            }else{
                showSearchAdapter.addList(showSearchBean.getResult());
            }
            //停止刷新加载
            seek_search_xrecy.refreshComplete();
            seek_search_xrecy.loadMoreComplete();

            if (showSearchBean.getResult().size()==0){
                show_search_flag=true;
            }else{
                show_search_flag=false;
            }
            if (showSearchBean.getResult().size()==0 && search_page==1){
                seek_no_lin.setVisibility(View.VISIBLE);
            }
        }
        if (object instanceof ShowNavigationOneBean){
            ShowNavigationOneBean showNavigationOneBean= (ShowNavigationOneBean) object;
            showNavigationOneAdapter.setList(showNavigationOneBean.getResult());

        }
        if (object instanceof ShowNavigationTwoBean){
            ShowNavigationTwoBean showNavigationTwoBean= (ShowNavigationTwoBean) object;
            showNavigationTwoAdapter.setList(showNavigationTwoBean.getResult());
        }
        if (object instanceof ShowNavigationShopBean){
            ShowNavigationShopBean showNavigationShopBean= (ShowNavigationShopBean) object;
            if (two_shop_page==1){
                showTwoShopAdapter.setList(showNavigationShopBean.getResult());
            }else{
                showTwoShopAdapter.addList(showNavigationShopBean.getResult());
            }
            //停止刷新加载
            seek_navigation_xrecy.refreshComplete();
            seek_navigation_xrecy.loadMoreComplete();
            two_shop_page++;
            if (showNavigationShopBean.getResult().size()==0){
                show_navigation_flag=true;
            }else{
                show_navigation_flag=false;
            }

        }
    }

    @Override
    public void failure(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void setEditTextState() {
        //自动加载软键盘
        seek_search_xrecy.setVisibility(View.GONE);
        seek_no_lin.setVisibility(View.GONE);
        seek_search_edit.setFocusableInTouchMode(true);
        seek_search_edit.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager)seek_search_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(seek_search_edit, 0);



    }


    //隐藏虚拟键盘
    public static void hideKeyboard(View v){
    InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
    if ( imm.isActive( ) ) {

        imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
    }
 }


}
