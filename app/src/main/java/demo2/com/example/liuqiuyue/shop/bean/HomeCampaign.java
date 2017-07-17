package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * Created by liuqiuyue on 2017/4/9.
 */

public class HomeCampaign implements Serializable {
    private Long id;
    private String title;
    private Campaign cpOne;
    private Campaign cpTwo;
    private Campaign cpThree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
