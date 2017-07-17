package demo2.com.example.liuqiuyue.shop.bean;

import java.util.List;

/**
 * Created by liuqiuyue on 2017/4/12.
 */

public class Page<T> {
    private String copyright;
    private int totalCount;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private List<T> orders;
    private List<T> list;

    public List<T> getOrders() {
        return orders;
    }

    public void setOrders(List<T> orders) {
        this.orders = orders;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
