package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class LocationForService {

    String cityCode = "";
    String province = "";
    String adcode = "";
    String district = "";

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
