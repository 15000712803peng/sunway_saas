package com.cnsunway.wash.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.MyLocation;
import com.cnsunway.wash.util.LocationManager;

import java.util.List;

@SuppressLint("NewApi")
public class SelAddrAMapActivity extends Activity {

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
    TextView editAddrText;
    Button addrConfirmBtn;
    final  int REQUEST_SEARCH_ADDR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager.get().init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_addr_amap);
//        longtitude = getIntent().getDoubleExtra("longtitude", 0);
//        latitude = getIntent().getDoubleExtra("latitude", 0);

         currentLocationParent = (LinearLayout) findViewById(R.id.current_location);
         addrPrefixText = (TextView) findViewById(R.id.text_addr_prefix);
         addrEndfixText = (TextView) findViewById(R.id.text_addr_endfix);
        editAddrText = (TextView) findViewById(R.id.text_edit);
        addrConfirmBtn = (Button) findViewById(R.id.btn_addr_confirm);
        addrConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object obj = addrConfirmBtn.getTag();
                if(obj != null){
                    PoiItem item = (PoiItem) obj;
                    Intent intent = new Intent();
                    intent.putExtra("longtitule", item.getLatLonPoint().getLongitude() + "");
                    intent.putExtra("latitude", item.getLatLonPoint().getLatitude() + "");
                    intent.putExtra("address", item.getTitle());
                    intent.putExtra("addressDetail", item.getSnippet());

                    if(poiType == POI_TYPE_REGEO && centerRegeoAddress != null){
                        intent.putExtra("cityCode", centerRegeoAddress.getCityCode());
                        intent.putExtra("cityName", centerRegeoAddress.getCity());
                        intent.putExtra("provinceName", centerRegeoAddress.getProvince());
                        intent.putExtra("countyName", centerRegeoAddress.getDistrict());
                    }else{
                        intent.putExtra("cityCode", item.getCityCode());
                        intent.putExtra("cityName", item.getCityName());
                        intent.putExtra("provinceName", item.getProvinceName());
                        intent.putExtra("countyName", item.getAdName());
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        editAddrText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelAddrAMapActivity.this,SearchAddrActivity.class);
                 if(myLocation != null){
                     intent.putExtra("my_location",JsonParser.objectToJsonStr(myLocation));
                 }

                startActivityForResult(intent,REQUEST_SEARCH_ADDR);

            }
        });
        locationTipsText = (TextView) findViewById(R.id.location_tips);
        locationTipsText.setVisibility(View.VISIBLE);
        backImage = (RelativeLayout) findViewById(R.id.back_container);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



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
                    if(items != null && items.size() > 0){
                        fillCurrentLocation(items.get(0));
                        needGeoSearch = false;
                    }
                }

//                setPoiCenter();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

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
            if(firstItem != null){
                fillCurrentLocation(firstItem);
            }
        }
        setIntent(intent);
    }

    /**
     *
     */

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        //onCreate时候，map还没resume，设置不成功
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
    MyLocation myLocation;
    PoiItem firstItem;
    private void fillCurrentLocation(PoiItem item){

        addrConfirmBtn.setTag(item);
        locationTipsText.setVisibility(View.INVISIBLE);
        currentLocationParent.setVisibility(View.VISIBLE);
        addrPrefixText.setText(item.getTitle());
        addrEndfixText.setText(item.getSnippet());
        if(myLocation == null){
            myLocation = new MyLocation();
            myLocation.setSnap(item.getSnippet());
            myLocation.setTitle(item.getTitle());
        }
        if(firstItem == null){
            firstItem = item;
        }

        fillPositinMarker(item);

    }

    MarkerOptions markerOption;

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
        if(requestCode == REQUEST_SEARCH_ADDR && resultCode == RESULT_OK){
            if(data != null){
                PoiItem item = data.getParcelableExtra("poi_item");
                if(item != null){
                    needGeoSearch = false;
                    fillCurrentLocation(item);
                }
            }else {
                if(firstItem != null){
                    fillCurrentLocation(firstItem);
                }
            }

        }
    }

    private void init() {
//        searchView = (SearchView) findViewById(R.id.searchview);
//        if (searchView == null) {
//            return;
//        }
//        searchView.setIconifiedByDefault(true);
//        searchView.onActionViewExpanded();
//        searchView.setFocusable(false);
//        searchView.clearFocus();
////      mSearchView.setIconifiedByDefault(true);
//        //ew android.support.v7.widget.
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String queryText) {
//                doSearchQuery(queryText);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String queryText) {
//                if (searchView != null) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(
//                                searchView.getWindowToken(), 0);
//                    }
//                    doSearchQuery(queryText);
//                    searchView.clearFocus();
//                }
//                return true;
//            }
//        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PoiItem item = (PoiItem) parent.getItemAtPosition(position);
//                if (item != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("longtitule", item.getLatLonPoint().getLongitude() + "");
//                    intent.putExtra("latitude", item.getLatLonPoint().getLatitude() + "");
//                    intent.putExtra("address", item.getTitle());
//                    intent.putExtra("addressDetail", item.getSnippet());
//
//                    if(poiType == POI_TYPE_REGEO && centerRegeoAddress != null){
//                        intent.putExtra("cityCode", centerRegeoAddress.getCityCode());
//                        intent.putExtra("cityName", centerRegeoAddress.getCity());
//                        intent.putExtra("provinceName", centerRegeoAddress.getProvince());
//                        intent.putExtra("countyName", centerRegeoAddress.getDistrict());
//                    }else{
//                        intent.putExtra("cityCode", item.getCityCode());
//                        intent.putExtra("cityName", item.getCityName());
//                        intent.putExtra("provinceName", item.getProvinceName());
//                        intent.putExtra("countyName", item.getAdName());
//                    }
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            }
//        });
    }

    private void setPoiCenter() {
        if (poiItems != null && poiItems.size() > 0) {
            PoiItem item = poiItems.get(0);
            latitude = item.getLatLonPoint().getLatitude();
            longtitude = item.getLatLonPoint().getLongitude();
            setCenter();
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
