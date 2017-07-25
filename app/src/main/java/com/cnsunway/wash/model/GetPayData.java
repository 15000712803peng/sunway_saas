package com.cnsunway.wash.model;

import java.util.List;

/**
 * Created by LL on 2015/12/11.
 */
public class GetPayData {
    Coupon dftCoupon;
    List<Coupon> coupons;
    List<Coupon> unusableCoupons;
    String balance;
    int couponCount;
    int unusableCouponCount;
    public int getUnusableCouponCount() {
        return unusableCouponCount;
    }

    public void setUnusableCouponCount(int unusableCouponCount) {
        this.unusableCouponCount = unusableCouponCount;
    }




    public List<Coupon> getUnusableCoupons() {
        return unusableCoupons;
    }

    public void setUnusableCoupons(List<Coupon> unusableCoupons) {
        this.unusableCoupons = unusableCoupons;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public Coupon getDftCoupon() {
        return dftCoupon;
    }

    public void setDftCoupon(Coupon dftCoupon) {
        this.dftCoupon = dftCoupon;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
