package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.data.ServiceAreaData;
import com.cnsunway.saas.wash.dialog.AreaIntroductDialog;
import com.cnsunway.saas.wash.dialog.CallDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.ServiceCityDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.ServiceCity;
import com.cnsunway.saas.wash.model.StoreLocation;
import com.cnsunway.saas.wash.resp.AllCityResp;
import com.cnsunway.saas.wash.resp.AllStoresResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FileUtil;
import com.cnsunway.saas.wash.util.LocationManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class ServiceAreaActivity extends InitActivity implements ServiceCityDialog.OnCitySelectedLinstenr,AMap.OnMarkerClickListener {

    @Bind(R.id.text_title)
    TextView titleText;
    @Bind(R.id.map)
     MapView mapView;
     AMap aMap;
    ServiceCity selectedCity;
   @Bind(R.id.text_city)
    TextView cityText;
    ServiceCityDialog cityDialog;
    StringVolley getCityVolley;
    StringVolley selectCityVolley;
    @Bind(R.id.ll_city_select)
    LinearLayout citySelect;
    JsonVolley getStoresVolley;
    @Bind(R.id.store_name)
    TextView storeNameText;
    @Bind(R.id.store_addr)
    TextView storeAddrText;
    @Bind(R.id.store_phone)
    TextView storePhoneText;
    @Bind(R.id.text_title_right)
    TextView rightTitleText;
    LocationForService locationForService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_area);
        ButterKnife.bind(this);
        callDialog = new CallDialog(this).builder();
        locationForService = UserInfosPref.getInstance(this).getLocationServer();

        LocationManager.get().init(this);
        citySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCityVolley = new StringVolley(ServiceAreaActivity.this,Const.Message.MSG_SERVICE_CITY_SUCC,Const.Message.MSG_SERVICE_CITY_FAIL);
                setOperationMsg(getResources().getString(R.string.loading));
                selectCityVolley.requestGet(Const.Request.cityList,getHandler(),ServiceAreaActivity.this,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
            }
        });
        selectedCity = (ServiceCity) JsonParser.jsonToObject(getIntent().getStringExtra("selected_city"), ServiceCity.class);
        getCityVolley = new StringVolley(this, Const.Message.MSG_GET_CITIES_SUCC,Const.Message.MSG_GET_CITIES_FAIL);
        getStoresVolley = new JsonVolley(this,Const.Message.MSG_GET_ALL_STORIES_SUCC,Const.Message.MSG_GET_ALL_STORIES_FAIL);

        cityText.setText(selectedCity.getCityName());
        rightTitleText.setVisibility(View.VISIBLE);
        rightTitleText.setText("说明");
        rightTitleText.setTextColor(getResources().getColor(R.color.text_dark_black));
        rightTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AreaIntroductDialog(ServiceAreaActivity.this).builder().show();
            }
        });
        cityDialog  = new ServiceCityDialog(this).builder();
        cityDialog.setCurrentCity(selectedCity);
        titleText.setText("定位中...");
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMarkerClickListener(this);
        if(selectedCity.getLatitude() != 0){
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedCity.getLatitude(),selectedCity.getLongitude()),11));
        }else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.139974,121.485011),11));
        }

        aMap.addPolygon(ServiceAreaData.getServiceArea(FileUtil.readRaw(this,R.raw.area1)));
        aMap.addPolygon(ServiceAreaData.getServiceArea(FileUtil.readRaw(this,R.raw.area2)));
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getStoresVolley.requestPost(Const.Request.allCityStores,getHandler(),"",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    List<StoreLocation> stores;
    StoreLocation nearestStore;
    float distance = 0;
    float minDistance = 0;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_SERVICE_CITY_SUCC:

                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AllCityResp allCityResp = (AllCityResp) JsonParser.jsonToObject(msg.obj+ "",AllCityResp.class);
                    List<ServiceCity> cities = allCityResp.getData();
                    if(cities == null || cities.size() <= 0){
                        OperationToast.showOperationResult(this,"没有可以选择的城市",0);
                    }else {
                        cityDialog.setCities(cities);
                        cityDialog.setCurrentCity(selectedCity);
                        cityDialog.setOnCitySelectedLinstenr(ServiceAreaActivity.this);
                        cityDialog.show();
                    }
                }else {

                }
                break;

            case Const.Message.MSG_GET_ALL_STORIES_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AllStoresResp resp = (AllStoresResp) JsonParser.jsonToObject(msg.obj+"",AllStoresResp.class);
                    if(resp != null && resp.getData() != null && resp.getData().size() > 0){
                        stores = resp.getData();
                        LatLng position = null;
                        if(longitude != 0 && latitude != 0){
                            position  = new LatLng(latitude,longitude);
                            minDistance = AMapUtils.calculateLineDistance(position,new LatLng(Double.parseDouble(resp.getData().get(0).getLatitude()),Double.parseDouble(resp.getData().get(0).getLongitude())));
                            nearestStore =stores.get(0);
                        }

                        fillStoreMarker(stores.get(0));
                        for(int i = 1; i <  stores.size();++i){
                            StoreLocation store = stores.get(i);
                            fillStoreMarker(store);
                            if(position != null){
                                distance = AMapUtils.calculateLineDistance(position,new LatLng(Double.parseDouble(store.getLatitude()),Double.parseDouble(store.getLongitude())));
                                if(Float.compare(distance,minDistance) <=0){
                                    nearestStore = store;
                                    minDistance = distance;
                                }
                            }
                        }

                        fillBottomInfo(nearestStore);

                        }
                    }
                break;
            case Const.Message.MSG_GET_ALL_STORIES_FAIL:
                break;
        }
    }

    private MarkerOptions markerOption;

    private void fillStoreMarker(StoreLocation store){
        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(Double.parseDouble(store.getLatitude()),Double.parseDouble(store.getLongitude())));
        markerOption.title(store.getStoreName()).snippet(store.getStoreNickName());

        markerOption.draggable(true);
        markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.map_icon_store)));
       Marker mark =  aMap.addMarker(markerOption);
        mark.setObject(store);

    }

    public void back(View view){
        finish();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unregisterReceiver(locationReceiver);
    }

    @Override
    public void onCitySelected(ServiceCity city) {
        if(city != null){
            selectedCity = city;
            cityText.setText(city.getCityName());
            if(selectedCity.getCityCode().equals("021")){
                selectedCity.setLatitude(31.139974);
                selectedCity.setLongitude(121.485011);
            }else if(selectedCity.getCityCode().equals("028")){
                selectedCity.setLatitude(30.579099);
                selectedCity.setLongitude(104.068138);
            }

            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedCity.getLatitude(),selectedCity.getLongitude()),11));
            UserInfosPref.getInstance(this).setServiceCityCode(selectedCity.getCityCode());
            Intent intent = new Intent();
            intent.setAction(Const.MyFilter.FILTER_CITY_CHANGED);
            intent.putExtra("selected_city",JsonParser.objectToJsonStr(city));
            sendBroadcast(intent);
        }
    }
    double longitude = 0;
    double latitude = 0;


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(locationReceiver,new IntentFilter(Const.Action.ACTION_LOCATION_SUCCEED));
    }


    MarkerOptions currentPositionMarker;
    boolean firstLogin = true;

    BroadcastReceiver locationReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(firstLogin){
                titleText.setText("取送范围");
                currentPositionMarker = new MarkerOptions();
                longitude  = intent.getDoubleExtra("longitude",0);
                latitude = intent.getDoubleExtra("latitude",0);
                if(longitude != 0 && latitude != 0){
                    currentPositionMarker.position(new LatLng(latitude,longitude));
                    currentPositionMarker.icon(
                            BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),
                                            R.mipmap.icon_current_store)));
                    aMap.addMarker(currentPositionMarker);
                    if(stores != null && stores.size() > 0){

                        LatLng   position  = new LatLng(latitude,longitude);
                        minDistance = AMapUtils.calculateLineDistance(position,new LatLng(Double.parseDouble(stores.get(0).getLatitude()),Double.parseDouble(stores.get(0).getLongitude())));
                        nearestStore = stores.get(0);

                        for(int i = 1; i <  stores.size();++i){
                            StoreLocation store = stores.get(i);
                            if(position != null){
                                distance = AMapUtils.calculateLineDistance(position,new LatLng(Double.parseDouble(store.getLatitude()),Double.parseDouble(store.getLongitude())));
                                if(Float.compare(distance,minDistance) <=0){
                                    nearestStore = store;
                                    minDistance = distance;
                                }
                            }
                        }

                        fillBottomInfo(nearestStore);

                    }
                }
            }
            firstLogin = false;

        }
    };

    private void fillBottomInfo(final  StoreLocation store){
        if(store  == null){
            return;
        }
        if(store.getAddress() != null){
            storeAddrText.setText(store.getAddress());
        }
        if(store.getStoreNickName() != null){
           storeNameText.setText(store.getStoreNickName());
        };

        if(store.getOwnerTel() != null){
            storePhoneText.setText(store.getOwnerTel());
            storePhoneText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callDialog.setPhone(store.getOwnerTel());
                    callDialog.show();
                }
            });

        }

    }

    CallDialog callDialog;

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            StoreLocation store = (StoreLocation) marker.getObject();
            fillBottomInfo(store);
        }

        return false;
    }
}
