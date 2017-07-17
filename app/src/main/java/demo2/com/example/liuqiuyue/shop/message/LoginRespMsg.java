package demo2.com.example.liuqiuyue.shop.message;

import demo2.com.example.liuqiuyue.shop.bean.User;

/**
 * Created by liuqiuyue on 2017/4/30.
 */

public class LoginRespMsg<T> extends BaseRespMsg {
    private String token;

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
