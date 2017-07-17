package demo2.com.example.liuqiuyue.shop.http;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import demo2.com.example.liuqiuyue.shop.ContantsApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/4/9.
 * 辅助OKHttp
 */

public class OkHttpHelper {
    private static final int TOKEN_MISSING=401;  //token丢失
    private static final int TOKEN_ERROR=402;   //token错误
    private static final int TOKEN_EXPIRE=403 ; //token过期
    private static final String TAG="OkHttpHelper" ;


    //采用单例模式使用OkHttpClient
    private static OkHttpHelper okHttpHelper;
    private static OkHttpClient okHttpClient;
    //Gson解析数据
    private Gson gson;
    //handler：http不能再主线程中操作UI，使用handler进行处理
    private Handler handler;

    /**
     * 单例模式，私有构造函数，在函数中做一些初始化
     */
    private OkHttpHelper() {
        //okHttp3超时方法不同
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        ClientBuilder.readTimeout(10, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(10, TimeUnit.SECONDS);//写入超时
        okHttpClient = ClientBuilder.build();
        gson = new Gson();
        //主线程 用来刷新UI  Looper.getMainLooper()
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkHttpHelper getInstance() {

        return new OkHttpHelper();
    }


    /**
     * 封装的一个request方法，GET PIOST方法都能用
     *
     * @param request
     * @param callback
     */
    public void doRequest(final Request request, final BaseCallback callback) {
        //在访问网络之前做的操作,对话框等
        callback.OnRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            //失败时调用，返回失败
            @Override
            public void onFailure(Call call, IOException e) {
                CallbackFailure(call, callback, e);
            }

            //成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {   //状态码大于200小于300
                    callback.onResponse(response);
                    String resultStr = response.body().string();
                    Log.d(TAG, "result=" + resultStr);

                    //如果我们需要返回String类型，则不用解析
                    if (callback.mType == String.class) {
                        //调用主线程回调方法，http不能再内部操作UI
                        CallbackSuccess(call, callback, response, resultStr);

                    }
                    //如果我们需要返回爱其他类型，则用Gson解析
                    else {
                        //手动添加try catch，避免解析错误情况的发生
                        try {
                            Object object = gson.fromJson(resultStr, callback.mType);
                            CallbackSuccess(call, callback, response, object);
                        } catch (com.google.gson.JsonParseException e) {
                            callback.onError(call,response,response.code(),e);
                        }
                    }
                } else if(response.code()==TOKEN_ERROR||response.code()==TOKEN_EXPIRE||response.code()==TOKEN_MISSING){
                    //如果token丢失

                    CallbackTokenError(callback,response);
                }
                else {
                    //返回错误
                    CallbackError(call, callback, response, response.code(), null);
                }
            }
        });
    }

    /**
     * 再主线程中执行的回调
     *
     * @param call
     * @param callback
     * @param response
     * @param object
     */
    private void CallbackSuccess(final Call call, final BaseCallback callback, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(call, response, object);
            }
        });

    }

    /**
     * 再主线程中执行的回调
     *
     * @param call
     * @param callback
     * @param response
     * @param code
     * @param e
     */
    private void CallbackError(final Call call, final BaseCallback callback, final Response response, int code, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                callback.onError(call, response, response.code(), e);
            }
        });
    }

    /**
     * 再主线程中执行的回调
     *
     * @param call
     * @param callback
     * @param e
     */
    private void CallbackFailure(final Call call, final BaseCallback callback, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call, e);
            }
        });
    }
    /**
     * 再主线程中执行的回调
     *
     * @param callback
     */
    private void CallbackTokenError( final BaseCallback callback,final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
             callback.onTokenError(response,response.code());
            }
        });
    }

    /**
     * 对外公开的GET方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, Map<String, Object> params, BaseCallback callback) {
        //调用buildRequest，触发if,使用GET请求方式,并执行相应的方法
        Request request = buildGetRequest(url, params);
        //调用doRequest，将buildRequest返回的request传入
        doRequest(request, callback);
    }

    public void get(String url, BaseCallback callback) {
        get(url, null, callback);
    }

    /**
     * 对外公开的POST方法
     *
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        //调用buildRequest，触发if,POST方式,并执行相应的方法
        Request request = buildPostRequest(url, params);
        //调用doRequest，将buildRequest返回的request传入
        doRequest(request, callback);
    }

    private Request buildPostRequest(String url, Map<String, Object> params) {
        return buildRequest(url, params, HttpMethodType.POST);
    }

    private Request buildGetRequest(String url, Map<String, Object> params) {

        return buildRequest(url, params, HttpMethodType.GET);
    }

    private String buildRequest(String url, Map<String, Object> params) {
        if (params == null)
            params = new HashMap<>(1);
        String token = ContantsApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token))   //put token数据
            params.put("token", token);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }
        return url;
    }

    /**
     * 创建request对象
     */
    private Request buildRequest(String url, Map<String, Object> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //如果是GET方式就调用get请求
        if (methodType == HttpMethodType.GET) {
            url = buildRequest(url, params);
            builder.url(url);
            builder.get();

        }
        //如果是POST方式就调用post请求
        else if (methodType == HttpMethodType.POST) {
            //获取body
            RequestBody body = builderFrmData(params);
            builder.post(body);
        }
        return builder.build();
    }

    /**
     * 构建表单body
     *
     * @param params
     * @return
     */
    private RequestBody builderFrmData(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);


        if (params != null) {
            //Map循环有4种写法
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(),entry.getValue()==null?"": entry.getValue().toString());
            }
            //如果token不为空，添加token到post方法
            String token = ContantsApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                builder.addFormDataPart("token", token);

        }
        return builder.build();
    }

    /**
     * 这个枚举用于指明是哪一种提交方式
     */
    enum HttpMethodType {
        GET,
        POST
    }

}
