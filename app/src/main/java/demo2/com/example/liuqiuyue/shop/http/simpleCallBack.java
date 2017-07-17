package demo2.com.example.liuqiuyue.shop.http;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import demo2.com.example.liuqiuyue.shop.ContantsApplication;
import demo2.com.example.liuqiuyue.shop.LoginActivity;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/5/1.
 */

public abstract class simpleCallBack<T> extends BaseCallback<T> {
    private Context mContext;
    public simpleCallBack(Context context){
        this.mContext=context;
    }
    @Override
    protected void OnRequestBefore(Request request) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    /**
     //     * 如果token失效则需要重新登录
     //     * @param response
     //     * @param code
     //     */
    @Override
    public void onTokenError(Response response, int code) {

          ToastUtils.show(mContext,"重新登录");
Intent intent=new Intent(mContext, LoginActivity.class);
    mContext.startActivity(intent);
    ContantsApplication.getInstance().clearUser();

    }
}
