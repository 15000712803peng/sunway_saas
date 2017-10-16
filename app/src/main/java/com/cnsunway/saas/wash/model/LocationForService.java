package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class LocationForService {

    String cityCode = "";
    String province = "";
    String adcode = "";
    String district = "";
    String cityName;

    public String getCityName() {
        return cityName;
    }
    /*"address": "string",
  "addressDetail": "string",
  "cityCode": "string",
  "cityName": "string",
  "contact": "string",
  "cityCode": "string",
  "districtName": "string",
  "gender": 0,
  "id": 0,
  "latitude": 0,
  "longtitude": 0,
  "mobile": "string",
  "provinceName": "string"*/

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
