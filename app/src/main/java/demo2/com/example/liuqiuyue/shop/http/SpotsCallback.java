package demo2.com.example.liuqiuyue.shop.http;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import demo2.com.example.liuqiuyue.shop.ContantsApplication;
import demo2.com.example.liuqiuyue.shop.LoginActivity;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/4/9.
 */

public abstract class SpotsCallback<T> extends BaseCallback<T> {
    private SpotsDialog dialog;
    private Context mContext;

    public SpotsCallback(Context context) {
        mContext = context;
        initLog();
    }

    public void initLog() {
        dialog = new SpotsDialog(mContext);
    }

    public void showDialog() {
        dialog.show();
    }

    public void missDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    @Override
    public void OnRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        missDialog();
    }


    @Override
    public void onResponse(Response response) {
        missDialog();
    }

    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, "重新登录");
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        ContantsApplication.getInstance().clearUser();
    }
//
}
