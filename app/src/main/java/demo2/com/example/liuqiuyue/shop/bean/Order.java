package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuqiuyue on 2017/5/13.
 * 订单返回值
 */

public class Order implements Serializable {
    public static final int STATUS_PAY_WAIT = 0;   //待支付
    public static final int STATUS_PAY_FALL = 2;  //支付失败
    public static final int STATUS_SUCCESS = 1;  //支付成功

    private Long id;
    private String orderNum;
    private Long createdTime;
    private Float amount;
    private int  status;
    private List<OrderItem> items;
    private Address address;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}




