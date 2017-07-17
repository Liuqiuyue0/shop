package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * Created by liuqiuyue on 2017/4/30.
 */

public class User<T> implements Serializable {
private long id;
    private String email;
    private String logo_url;
    private String username;

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String mobi;



}
