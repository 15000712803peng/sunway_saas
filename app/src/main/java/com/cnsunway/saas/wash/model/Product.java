package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class Product {

    String basePrice;
    int categoryId;
    String createdDate;
    int hotValue;
    int id;
    boolean needInsurance;
    String productName;
    int sortValue;
    int storeId;
    String updatedDate;
    int valuationModel;
    String imgUrl = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getHotValue() {
        return hotValue;
    }

    public void setHotValue(int hotValue) {
        this.hotValue = hotValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNeedInsurance() {
        return needInsurance;
    }

    public void setNeedInsurance(boolean needInsurance) {
        this.needInsurance = needInsurance;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getValuationModel() {
        return valuationModel;
    }

    public void setValuationModel(int valuationModel) {
        this.valuationModel = valuationModel;
    }
}
