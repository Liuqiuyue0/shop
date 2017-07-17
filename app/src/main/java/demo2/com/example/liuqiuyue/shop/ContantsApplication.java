package demo2.com.example.liuqiuyue.shop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.x;

import demo2.com.example.liuqiuyue.shop.bean.User;
import demo2.com.example.liuqiuyue.shop.utils.UserLocalData;

/**
 * Created by liuqiuyue on 2017/4/13.
 */

public class ContantsApplication extends Application {
    private User user;
    private Intent intent;

    private static ContantsApplication mInstance;

    //返回ContantsApplication对象，用于外界使用
    public static ContantsApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco初始化
        Fresco.initialize(this);
        //xutils3初始化
        x.Ext.init(this);
        initUser();
        mInstance=this;
    }

    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return UserLocalData.getToken(this);
    }

    /**
     * 添加
     *
     * @param user
     * @param token
     */
    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, this.user);
        UserLocalData.putToken(this, token);
    }

    /**
     * 清理
     */
    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    /**
     * 用于保存intent
     * @param intent
     */
    public void putIntent(Intent intent){
        this.intent=intent;
    }
    public Intent getIntent(){
        return this.intent;
    }

    /**
     * 执行intent后进行清空intent
     * @param context
     */
    public void jumToTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent=null;

    }

}
