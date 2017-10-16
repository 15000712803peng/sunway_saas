package com.cnsunway.saas.wash.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class StoreDetail {
    String address;
    String addressDetail;
    String beginService;
    List<Object> comments;
    int commentsCount;
    String createdDate;
    int districtId;
    String endService;
    List<Product>hotProducts;
    int id;
    boolean isClosed;
    String latitude;
    String longitude;
    int ownerId;
    List<StoreImage> pics;
    Object polygonPoints; //"[{\"lng\":121.435038,\"lat\":31.094584},{\"lng\":121.435038,\"lat\":31.07591},{\"lng\":121.413282,\"lat\":31.07591},{\"lng\":121.413282,\"lat\":31.094584}]",
    int score;
    int serviceCount;
    int status;
    String storeName;
    String storeNo;
    String updatedDate; //2017-10-09 10:36:39",
    String withdrawerBank; // 中国银行",
    String withdrawerBankAccount; //13242353466867987",
    String withdrawerName; //

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getBeginService() {
        return beginService;
    }

    public void setBeginService(String beginService) {
        this.beginService = beginService;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getEndService() {
        return endService;
    }

    public void setEndService(String endService) {
        this.endService = endService;
    }

    public List<Product> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(List<Product> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<StoreImage> getPics() {
        return pics;
    }

    public void setPics(List<StoreImage> pics) {
        this.pics = pics;
    }

    public Object getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoints(Object polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getWithdrawerBank() {
        return withdrawerBank;
    }

    public void setWithdrawerBank(String withdrawerBank) {
        this.withdrawerBank = withdrawerBank;
    }

    public String getWithdrawerBankAccount() {
        return withdrawerBankAccount;
    }

    public void setWithdrawerBankAccount(String withdrawerBankAccount) {
        this.withdrawerBankAccount = withdrawerBankAccount;
    }

    public String getWithdrawerName() {
        return withdrawerName;
    }

    public void setWithdrawerName(String withdrawerName) {
        this.withdrawerName = withdrawerName;
    }
}
