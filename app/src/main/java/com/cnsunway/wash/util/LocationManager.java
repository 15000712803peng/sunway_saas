package com.cnsunway.wash.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.ServiceCity;
import com.cnsunway.wash.sharef.UserInfosPref;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by peter on 15/12/10.
 */
public class LocationManager {

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;
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

    public boolean isLocationSucceed() {
        return locationSucceed;
    }

    public void setLocationSucceed(boolean locationSucceed) {
        this.locationSucceed = locationSucceed;
    }

    private double longitude;
    private double latitude;
    private boolean locationSucceed;

    public String getCity() {
        return city;
    }

    private String city;
    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener(){

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                latitude = amapLocation.getLatitude();//获取经度
                longitude = amapLocation.getLongitude();//获取纬度
                locationSucceed = true;
                ServiceCity serviceCity = new ServiceCity();
                serviceCity.setCityCode(amapLocation.getCityCode());
                serviceCity.setCityCode(amapLocation.getCityCode());
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                LocationForService locationForService = new LocationForService();
                locationForService.setCityCode(amapLocation.getCityCode());
                locationForService.setDistrict(amapLocation.getDistrict());
                locationForService.setProvince(amapLocation.getProvince());
                locationForService.setAdcode(amapLocation.getAdCode());
                UserInfosPref.getInstance(context).saveLocationForServer(locationForService);
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getRoad();//街道信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                if(amapLocation.getCity() != null && amapLocation.getCity().length() > 0){
                    city = amapLocation.getCity();
                }else{
                    city = amapLocation.getProvince();
                }

                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
                mLocationClient = null;
                Intent intent =  new Intent(Const.Action.ACTION_LOCATION_SUCCEED);
                intent.putExtra("latitude",amapLocation.getLatitude());
                intent.putExtra("longitude",amapLocation.getLongitude());
                intent.putExtra("city", JsonParser.objectToJsonStr(serviceCity));
                context.sendBroadcast(intent);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }

        }
    };
    //初始化定位
    static LocationManager instance;
    public static LocationManager get(){
        if(instance == null){
            instance = new LocationManager();
        }
        return instance;
    }

    public void init(Context context){
            this.context = context;
            if(mLocationClient == null){
                mLocationClient = new AMapLocationClient(context);
                //设置定位回调监听
                mLocationClient.setLocationListener(mLocationListener);

                AMapLocationClientOption mLocationOption = null;
//初始化定位参数
                mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(false);
//设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
//给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
            }

//启动定位
            mLocationClient.startLocation();

    }
}
