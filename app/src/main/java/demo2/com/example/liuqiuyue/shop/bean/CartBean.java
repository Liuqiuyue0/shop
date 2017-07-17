package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * Created by liuqiuyue on 2017/4/20.
 * 购物车列表bean
 */


public class CartBean extends Wares implements Serializable {
    private int count;
    //默认选中状态
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
