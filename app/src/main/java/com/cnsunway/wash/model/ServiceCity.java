package com.cnsunway.wash.model;

/**
 * Created by hp on 2017/6/15.
 */
public class ServiceCity {
    String cityCode;
    String cityName;
    String createdBy;
    String createdDate;
    String deletedFlag;
    int id;
    String updatedBy;
    String updatedDate;
    String version;
    double latitude;
    double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isLCity() {
        return isLCity;
    }

    public void setLCity(boolean LCity) {
        isLCity = LCity;
    }

    boolean isLCity;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceCity that = (ServiceCity) o;

        return cityCode != null ? cityCode.equals(that.cityCode) : that.cityCode == null;

    }

    @Override
    public int hashCode() {
        return cityCode != null ? cityCode.hashCode() : 0;
    }
}
