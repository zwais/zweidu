package com.bw.zweidu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.bw.zweidu.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
public class RetrofitUtil {

    private static RetrofitUtil instance;
    private ObservedApis mObservedApis;
    private final String BaseUrl="http://mobile.bwstudent.com/small/";
    public static synchronized RetrofitUtil getInstance(){
        if (instance==null){
            instance=new RetrofitUtil();
        }
        return instance;
    }

    private RetrofitUtil(){
        //拦截器
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mClient=new OkHttpClient.Builder()
                //读取超时
                .readTimeout(10,TimeUnit.SECONDS)
                //连接超时
                .connectTimeout(10,TimeUnit.SECONDS)
                //写超时
                .writeTimeout(10,TimeUnit.SECONDS)
                //添加拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original=chain.request();
                        //取出保存的userID，sessionID
                        SharedPreferences mSharedPreferences=MyApplication.getContext().getSharedPreferences("User",Context.MODE_PRIVATE);
                        String userId = mSharedPreferences.getString("userId","");
                        String sessionId = mSharedPreferences.getString("sessionId", "");
                        Request.Builder builder1 = original.newBuilder();
                        builder1.method(original.method(),original.body());

                        if(!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(sessionId)){
                            builder1.addHeader("userId",userId);
                            builder1.addHeader("sessionId",sessionId);
                        }

                        Request request = builder1.build();

                        return chain.proceed(request);
                    }
                })
                .build();

        //Retrofit的创建
        Retrofit mRetrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseUrl)
                .client(mClient)
                .build();
        mObservedApis=mRetrofit.create(ObservedApis.class);
    }

    //get请求
    public void get(String url,ICallBack callBack){
        mObservedApis.get(url)
                //后执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }
    public void shopcart_list(String url,Map<String,String> map,ICallBack callBack){
        mObservedApis.shopcart_list(url,map)
                //后执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }


    //post请求
    public void post(String url, Map<String,String> params,ICallBack callBack){
        if (params==null){
            params=new HashMap<>();
        }
        mObservedApis.post(url,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));
    }

    //delete请求
    public void delete(String url,ICallBack callBack){
        mObservedApis.delete(url)
                //后执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }

    //put请求
    public void put(String url, Map<String,String> params,ICallBack callBack){
        if (params==null){
            params=new HashMap<>();
        }
        mObservedApis.put(url,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));
    }
    //图片上传
    public void postImage(String url, List<File> image_list,ICallBack callBack){
        //组装pathmap对象
        Map<String, RequestBody> map=new HashMap<>();
        for (File file : image_list){
            RequestBody fileBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            map.put("image",fileBody);
        }

        mObservedApis.postmap(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }

    //图片上传
    public void postimage(String url, File file,ICallBack callBack){

      RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part filePart=MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        mObservedApis.postImage(url,filePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }

    //图文上传
    public void postimagecon(String url,Map<String,String> params, File file,ICallBack callBack){

        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part filePart=MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        mObservedApis.postImageContent(url,params,filePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }
    //多图文上传
    public void postduocon(String url,Map<String,String> params, List<File> list,ICallBack callBack){
        MultipartBody.Part[] parts=new MultipartBody.Part[list.size()];
        int index=0;
        for (File file: list){
            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part filePart=MultipartBody.Part.createFormData("image",file.getName(),requestBody);
            parts[index]=filePart;
            index++;
        }

        mObservedApis.postDuoContent(url,params,parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callBack));

    }




    private Observer getObserver(final ICallBack callBack) {
        //Rxjava
         Observer observer=new Observer<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (callBack!=null){
                    callBack.failure(e.getMessage());
                    Log.i("Tag",e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    if (callBack!=null){
                        callBack.success(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack!=null){
                        callBack.failure(e.getMessage());
                    }
                }
            }
        };
    return observer;
    }
    public interface ICallBack{
        void success( String result );
        void failure( String error );
    }
}
