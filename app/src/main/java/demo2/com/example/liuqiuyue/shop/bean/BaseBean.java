package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * Created by Ivan on 15/9/24.
 */
public class BaseBean implements Serializable {


    protected   long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
