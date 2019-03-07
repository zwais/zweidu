package com.bw.zweidu.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bw.zweidu.MyApplication;
import com.bw.zweidu.util.RetrofitUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ImodelImpl implements Imodel {

//    //get请求
    @Override
   public void getRequestModel(String url, final Class clazz, final ModelCallBack callBack) {
       if (!isNetWork()){
           callBack.failure("网络状态不可用");
           return;
        }
       RetrofitUtil.getInstance().get(url,new RetrofitUtil.ICallBack() {
           @Override
           public void success(String result) {
               Object object = getGson(result, clazz);
                callBack.success(object);
           }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
       });
   }

    @Override
    public void getShopcart_list( String url, Map<String, String> map, final Class clazz, final ModelCallBack callBack ) {
        RetrofitUtil.getInstance().shopcart_list(url, map, new RetrofitUtil.ICallBack() {
            @Override
            public void success( String result ) {
                Gson gson=new Gson();
                Object o = gson.fromJson(result, clazz);
                callBack.success(o);
            }

            @Override
            public void failure( String error ) {

            }
        });
    }


    //post请求
    @Override
    public void postRequestModel(String url, Map<String, String> params, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().post(url, params, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void deleteRequestModel(String url, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().delete(url, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });

    }
    //put请求
    @Override
    public void putRequestModel(String url, Map<String, String> params, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().put(url, params, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postImageRequestModel(String url, List<File> image_list, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().postImage(url, image_list, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postimageRequestModel(String url, File file, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().postimage(url, file, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postImageConRequestModel(String url, Map<String, String> params, File file, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().postimagecon(url,params ,file, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postDuoConRequestModel(String url, Map<String, String> params, List<File> list, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("网络状态不可用");
            return;
        }
        RetrofitUtil.getInstance().postduocon(url,params ,list, new RetrofitUtil.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    //gson解析
    private Object getGson(String result, Class clazz) {
        Object o = new Gson().fromJson(result, clazz);
        return o;
    }

    //判断网络状态
    public static boolean isNetWork(){
        ConnectivityManager cm = (ConnectivityManager) MyApplication.instance.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }
}
