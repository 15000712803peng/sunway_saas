package com.cnsunway.wash.model;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class StoreLocation {

    String id;
    String latitude;
    String longitude;
    String storeName;
    String storeNickName;
    String address;
    String ownerTel;

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNickName() {
        return storeNickName;
    }

    public void setStoreNickName(String storeNickName) {
        this.storeNickName = storeNickName;
    }
}
