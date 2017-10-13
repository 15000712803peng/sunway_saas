package com.cnsunway.saas.wash.model;

import java.io.Serializable;

public class Cloth implements Serializable{
    String additional;
            float basePrice;
            String color;
            String colorCode;
            String createdDate;
            String id;
            int insurancePremium;
            boolean isWashable;
            String memo;
            String  orderId;
            String productName;
            float realPrice;
            String rewashMemo;
            int sqmCount;
            String storeId;
            String treatment;
            int type;
            String updatedDate;
           String washCode;
            int washStatus;
            String washType;

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInsurancePremium() {
        return insurancePremium;
    }

    public void setInsurancePremium(int insurancePremium) {
        this.insurancePremium = insurancePremium;
    }

    public boolean isWashable() {
        return isWashable;
    }

    public void setWashable(boolean washable) {
        isWashable = washable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }

    public String getRewashMemo() {
        return rewashMemo;
    }

    public void setRewashMemo(String rewashMemo) {
        this.rewashMemo = rewashMemo;
    }

    public int getSqmCount() {
        return sqmCount;
    }

    public void setSqmCount(int sqmCount) {
        this.sqmCount = sqmCount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getWashCode() {
        return washCode;
    }

    public void setWashCode(String washCode) {
        this.washCode = washCode;
    }

    public int getWashStatus() {
        return washStatus;
    }

    public void setWashStatus(int washStatus) {
        this.washStatus = washStatus;
    }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }
}
