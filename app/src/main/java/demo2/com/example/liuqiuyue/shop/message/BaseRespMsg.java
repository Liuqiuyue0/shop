package demo2.com.example.liuqiuyue.shop.message;

import java.io.Serializable;

/**
 * Created by liuqiuyue on 2017/4/30.
 */

public class BaseRespMsg<T> implements Serializable {
    public final static int STATUS_SUCCESS=1;
    public final static int STATUS_ERROR=0;
    public final static String MSG_SUCCESS="success";

    protected int status=STATUS_SUCCESS;
    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
