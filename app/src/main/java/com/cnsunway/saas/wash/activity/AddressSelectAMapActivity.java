package com.cnsunway.saas.wash.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
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
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.LocationManager;

import java.util.List;

@SuppressLint("NewApi")
public class AddressSelectAMapActivity extends Activity {

    private MapView mapView;
    private AMap aMap;
    private SearchView searchView;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private GeocodeSearch geocoderSearch;
    private ListView listView;
    private List<PoiItem> poiItems;
    private int poiType;
    private RegeocodeAddress centerRegeoAddress;
    private static final int POI_TYPE_REGEO = 0;
    private static final int POI_TYPE_SEARCH = 1;

    private double longtitude;
    private double latitude;
    private static final int MapLevel = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager.get().init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select_amap);
        longtitude = getIntent().getDoubleExtra("longtitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);

        TextView titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText("选择地理位置");
        mapView = (MapView) findViewById(R.id.map);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
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
                adapter.notifyDataSetChanged();
//                setPoiCenter();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

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


    private void init() {
        searchView = (SearchView) findViewById(R.id.searchview);
        if (searchView == null) {
            return;
        }
        searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();
        searchView.setFocusable(false);
        searchView.clearFocus();
//      mSearchView.setIconifiedByDefault(true);
        //ew android.support.v7.widget.


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                doSearchQuery(queryText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                if (searchView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(
                                searchView.getWindowToken(), 0);
                    }
                    Log.e("-----------","search query:" +searchView.getQuery());
                    doSearchQuery(queryText);
                    searchView.clearFocus();
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem item = (PoiItem) parent.getItemAtPosition(position);
                if (item != null) {
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
                poiType = POI_TYPE_SEARCH;
                poiItems = items;
                adapter.notifyDataSetChanged();
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

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (poiItems != null) {
                return poiItems.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (poiItems != null && position < poiItems.size()) {
                return poiItems.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(AddressSelectAMapActivity.this, R.layout.layout_addr_item, null);
                FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
                ViewHolder holder = new ViewHolder();
                holder.textAddress = (TextView) convertView.findViewById(R.id.tv_poi_name);
                holder.textDetail = (TextView) convertView.findViewById(R.id.tv_poi_detail);
                holder.imageIcon = (ImageView) convertView.findViewById(R.id.image_poi_icon);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();

            PoiItem item = (PoiItem) getItem(position);
            holder.textAddress.setText(item.getTitle());
            holder.textDetail.setText(item.getSnippet());
            if (position > 0) {
                holder.imageIcon.setVisibility(View.GONE);
            } else {
                holder.imageIcon.setVisibility(View.VISIBLE);
            }
            return convertView;
        }


    };

    public static class ViewHolder {
        public TextView textAddress;
        public TextView textDetail;
        public ImageView imageIcon;
    }


}
