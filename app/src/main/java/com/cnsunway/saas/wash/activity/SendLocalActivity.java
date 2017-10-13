package com.cnsunway.saas.wash.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.model.Addr;
import com.cnsunway.saas.wash.util.LocationManager;

import java.util.List;

@SuppressLint("NewApi")
public class SendLocalActivity extends Activity {

    private MapView mapView;
    private AMap aMap;
//    private SearchView searchView;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private GeocodeSearch geocoderSearch;
//    private ListView listView;
    private List<PoiItem> poiItems;
    private int poiType;
    private RegeocodeAddress centerRegeoAddress;
    private static final int POI_TYPE_REGEO = 0;
    private static final int POI_TYPE_SEARCH = 1;

    private double longtitude = 0;
    private double latitude = 0;
    private static final int MapLevel = 16;

    TextView locationTipsText;
    RelativeLayout backImage;
    Button addrConfirmBtn;
    final  int REQUEST_SEARCH_ADDR = 2;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager.get().init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_local);
         currentLocationParent = (LinearLayout) findViewById(R.id.current_location);
         addrPrefixText = (TextView) findViewById(R.id.text_addr_prefix);
         addrEndfixText = (TextView) findViewById(R.id.text_addr_endfix);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText("发送位置");
        addrConfirmBtn = (Button) findViewById(R.id.btn_addr_confirm);
        addrConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendLocation();
            }

        });
        locationTipsText = (TextView) findViewById(R.id.location_tips);
        locationTipsText.setVisibility(View.VISIBLE);
//        backImage = (RelativeLayout) findViewById(R.id.back_container);
//        backImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        mapView = (MapView) findViewById(R.id.map);
//        listView = (ListView) findViewById(R.id.listview);
//        listView.setAdapter(adapter);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng latLng = cameraPosition.target;
//                Toast.makeText(getApplicationContext(),"camera change finish",Toast.LENGTH_SHORT).show();
                if (latLng.longitude != longtitude ||
                        latLng.latitude != latitude) {
                    longtitude = latLng.longitude;
                    latitude = latLng.latitude;
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longtitude), 2000,
                            GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
                }
            }
        });
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult == null ||
                        regeocodeResult.getRegeocodeAddress() == null) {
                    return;
                }

                List<PoiItem> items = regeocodeResult.getRegeocodeAddress().getPois();
                poiType = POI_TYPE_REGEO;
                centerRegeoAddress = regeocodeResult.getRegeocodeAddress();
                poiItems = items;
                if(needGeoSearch){

                }

                setPoiCenter();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        unableMove();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            PoiItem item = intent.getParcelableExtra("poi_item");
            if(item != null){
                needGeoSearch = false;
                fillCurrentLocation(item);
            }
        }else {
            if(currentLocationAddr != null){
                fillCurrentLocation(currentLocationAddr);
            }
        }
        setIntent(intent);
    }

    public void back(View view){
        finish();
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        //onCreate时候，map还没resume，设置不成功y
        if (latitude <= 0 || longtitude <= 0) {
            registerReceiver(receiver, new IntentFilter(Const.Action.ACTION_LOCATION_SUCCEED));
            Boolean isSucceed = LocationManager.get().isLocationSucceed();
            if (isSucceed) {
                longtitude = LocationManager.get().getLongitude();
                latitude = LocationManager.get().getLatitude();
                setCenter();
            }
        }
        init();

    }

    LinearLayout currentLocationParent;
    TextView addrPrefixText,addrEndfixText;

    Addr currentLocationAddr;
    private void fillCurrentLocation(PoiItem item){

        addrConfirmBtn.setTag(item);
        locationTipsText.setVisibility(View.INVISIBLE);
        currentLocationParent.setVisibility(View.VISIBLE);
        addrPrefixText.setText(item.getTitle());
        addrEndfixText.setText(item.getSnippet());
//        if(myLocation == null){
//            myLocation = new MyLocation();
//            myLocation.setSnap(item.getSnippet());
//            myLocation.setTitle(item.getTitle());
//        }

//        fillPositinMarker(item);

    }
    private void fillCurrentLocation(Addr item){
        addrConfirmBtn.setTag(item);
        locationTipsText.setVisibility(View.INVISIBLE);
        currentLocationParent.setVisibility(View.VISIBLE);
        addrPrefixText.setText(item.getAddress());
        addrEndfixText.setText(item.getAddressDetail());
//        fillPositinMarker(item);
    }

    MarkerOptions markerOption;

    private void fillPositinMarker(Addr item){
        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongtitude())));
        markerOption.draggable(false);
        markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.icon_current_location)));
        aMap.clear();
        aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongtitude())), MapLevel));

    }

    private void fillPositinMarker(PoiItem item){
        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude()));
        markerOption.draggable(false);
        markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.icon_current_location)));
        aMap.clear();
        aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(item.getLatLonPoint().getLatitude(),
                        item.getLatLonPoint().getLongitude()), MapLevel));

    }

    boolean needGeoSearch = true;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void init() {

    }

    private void setPoiCenter() {
        if (poiItems != null && poiItems.size() > 0) {
            PoiItem item = poiItems.get(0);
            latitude = item.getLatLonPoint().getLatitude();
            longtitude = item.getLatLonPoint().getLongitude();
            setCenter();
            fillCurrentLocation(item);
        }
    }

    /**
     * 判断edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord) {
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "上海");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.d("", "");
                List<PoiItem> items = poiResult.getPois();
                if(items != null && items.size() > 0){
                    fillCurrentLocation(items.get(0));
                }
                poiType = POI_TYPE_SEARCH;
                poiItems = items;
//                adapter.notifyDataSetChanged();
                setPoiCenter();
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean isSucceed = LocationManager.get().isLocationSucceed();
            if (!isSucceed) {
                return;
            }
            longtitude = LocationManager.get().getLongitude();
            latitude = LocationManager.get().getLatitude();
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
                currentLocationAddr = new Addr();
                currentLocationAddr.setLatitude(latitude+"");
                currentLocationAddr.setLongtitude(longtitude+"");
                currentLocationAddr.setAddress(name+"");
                currentLocationAddr.setAddressDetail(address+"");
              fillCurrentLocation(currentLocationAddr);

            setCenter();
        }
    };

    private void setCenter() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude,
                        longtitude), MapLevel));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void unableMove(){
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);


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
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendLocation() {
        String addr = addrPrefixText.getText().toString().trim();
        if(TextUtils.isEmpty(addr)){
            OperationToast.showOperationResult(this,"定位中...",R.mipmap.reminder_icon);
            return;
        }
        String addrEnd = addrEndfixText.getText().toString().trim();
        if(!TextUtils.isEmpty(addrEnd)){
            addr = addr + "&" + addrEnd;
        }
        Intent intent = this.getIntent();
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longtitude);
        intent.putExtra("address", addr);
        this.setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.hd_slide_in_from_left, R.anim.hd_slide_out_to_right);
    }
//    private BaseAdapter adapter = new BaseAdapter() {
//        @Override
//        public int getCount() {
//            if (poiItems != null) {
//                return poiItems.size();
//            }
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            if (poiItems != null && position < poiItems.size()) {
//                return poiItems.get(position);
//            }
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = View.inflate(SelAddrAMapActivity.this, R.layout.layout_addr_item, null);
//                FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
//                ViewHolder holder = new ViewHolder();
//                holder.textAddress = (TextView) convertView.findViewById(R.id.tv_poi_name);
//                holder.textDetail = (TextView) convertView.findViewById(R.id.tv_poi_detail);
//                holder.imageIcon = (ImageView) convertView.findViewById(R.id.image_poi_icon);
//                convertView.setTag(holder);
//            }
//            ViewHolder holder = (ViewHolder) convertView.getTag();
//
//            PoiItem item = (PoiItem) getItem(position);
//            holder.textAddress.setText(item.getTitle());
//            holder.textDetail.setText(item.getSnippet());
//            if (position > 0) {
//                holder.imageIcon.setVisibility(View.GONE);
//            } else {
//                holder.imageIcon.setVisibility(View.VISIBLE);
//            }
//            return convertView;
//        }
//
//
//    };

    public static class ViewHolder {
        public TextView textAddress;
        public TextView textDetail;
        public ImageView imageIcon;
    }


}
