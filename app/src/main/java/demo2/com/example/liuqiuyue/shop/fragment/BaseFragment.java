package demo2.com.example.liuqiuyue.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo2.com.example.liuqiuyue.shop.ContantsApplication;
import demo2.com.example.liuqiuyue.shop.LoginActivity;
import demo2.com.example.liuqiuyue.shop.TestActvity;
import demo2.com.example.liuqiuyue.shop.bean.User;

/**
 * Created by liuqiuyue on 2017/5/2.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);

        init();
        initToolBar();
        return view;
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    public void initToolBar() {

    }

    public abstract void init();

    /**
     * intent需要进行保存，在登录之后要进行之前的操作，保存道application中
     *
     * @param intent
     * @param isNeedLogin
     */
    public void StartActivity(Intent intent, boolean isNeedLogin) {
        //判断是否需要登录
        if (isNeedLogin) {
            User user = ContantsApplication.getInstance().getUser();
            //判断该是否正在登录
            if (user != null) {
                super.startActivity(intent);
            } else {
                ContantsApplication.getInstance().putIntent(intent);  //将intent进行保存
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);  //跳转到登录页面
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }

    }

    /**
     * 改写StartActivityForResult，没登录时调转到intent界面，登录时跳转到详情页面
     * @param intent
     * @param requestCode
     */
    public void StartActivityForResult(Intent intent, int requestCode) {

            User user = ContantsApplication.getInstance().getUser();
            if (user == null) {
                super.startActivityForResult(intent,requestCode);
            } else {
                Intent loginIntent = new Intent(getActivity(), TestActvity.class);  //跳转到登录页面
                super.startActivity(loginIntent);
            }
    }


}
