package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2015/12/9.
 */
public class Coupon {
    String amount;
    String couponNo;
    String description;
    String expireDate;
    String name;
    int status;
    String validDate;
    int usable;
    String denomination;
    boolean isSelected;
    boolean expireSoon;

    public int getUseChannel() {
        return useChannel;
    }

    public void setUseChannel(int useChannel) {
        this.useChannel = useChannel;
    }

    int useChannel;

    public boolean isExpireSoon() {
        return expireSoon;
    }

    public void setExpireSoon(boolean expireSoon) {
        this.expireSoon = expireSoon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public int getUsable() {
        return usable;
    }

    public void setUsable(int usable) {
        this.usable = usable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }


}
