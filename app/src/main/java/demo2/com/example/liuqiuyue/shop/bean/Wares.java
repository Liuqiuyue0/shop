package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * Created by liuqiuyue on 2017/4/12.
 */

public class Wares implements Serializable {
    private Long id;
    private Long categoryId;
    private Long campaignId;
    private String name;
    private String imgUrl;
    private float price;
    private Long sale;

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


}
