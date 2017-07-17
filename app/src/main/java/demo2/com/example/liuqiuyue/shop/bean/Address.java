
package demo2.com.example.liuqiuyue.shop.bean;

import java.io.Serializable;

/**
 * 地址适配器
 * Comparable 排序
 */
public class Address implements Serializable,Comparable<Address>{


    private Long id;

    private String consignee;
    private String phone;
    private String addr;
    private String zipCode;
    private Boolean isDefault;

    public Address(){};

    public Address(String consignee, String phone, String addr, String zipCode){
        this.consignee = consignee;
        this.phone=phone;
        this.addr= addr;
        this.zipCode = zipCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     *默认排序，默认放在第一位
     * @param another
     * @return
     */
    @Override
    public int compareTo(Address another) {

        if(another.getIsDefault()!=null && this.getIsDefault() !=null)
            return another.getIsDefault().compareTo(this.getIsDefault());

        return -1;
    }
}
