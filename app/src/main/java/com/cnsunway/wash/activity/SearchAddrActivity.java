package com.cnsunway.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cnsunway.wash.R;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.AddrHistoryItem;
import com.cnsunway.wash.model.MyLocation;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.util.LocationManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class SearchAddrActivity extends InitActivity implements AdapterView.OnItemClickListener{

    @Bind(R.id.back_container)
    RelativeLayout backImage;
    @Bind(R.id.edit_search)
    EditText searchEdit;
    @Bind(R.id.search_content)
    LinearLayout searchContentParent;
    @Bind(R.id.addr_history)
    LinearLayout historyContentParent;
    @Bind(R.id.list_search)
    ListView  searchList;
    MyLocation myLocation;
    @Bind(R.id.text_addr_prefix)
    TextView currentPrefixText;
    @Bind(R.id.text_addr_endfix)
    TextView currentEndfixText;
    @Bind(R.id.image_search)
    ImageView searchImage;
    @Bind(R.id.tv_search)
    TextView searchText;
    @Bind(R.id.list_history)
    ListView historyList;
    @Bind(R.id.current_location)
    LinearLayout currentLocation;
    @Bind(R.id.image_clear_search)
    ImageView clearSearchImage;
    String searchContent;
    int num;
    @Bind(R.id.text_no_find)
    TextView noFindText;

    private void refreshHistoryList(List<AddrHistoryItem> items){
        if(items == null || items.size() == 0){
            historyList.setVisibility(View.GONE);
            return;
        }
        historyList.setVisibility(View.VISIBLE);
        historyList.setAdapter(new HistoryAdapter(items));
        historyList.setOnItemClickListener(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager.get().init(getApplicationContext());
        setContentView(R.layout.activity_search_addr);
        ButterKnife.bind(this);

        String location = getIntent().getStringExtra("my_location");
        if(location != null && !TextUtils.isEmpty(location)){
            myLocation = (MyLocation) JsonParser.jsonToObject(location,MyLocation.class);
        }
        if(myLocation != null && !TextUtils.isEmpty(myLocation.getTitle())){
            currentPrefixText.setText(myLocation.getTitle());
        }
        if(myLocation != null &&!TextUtils.isEmpty(myLocation.getSnap())){
            currentEndfixText.setText(myLocation.getSnap());
        }

        refreshHistoryList(UserInfosPref.getInstance(this).getAllHistory());
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //输入完毕
                searchContent = editable.toString().trim();
                if(TextUtils.isEmpty(searchContent)){
                    //输入为空
//                    historyContentParent.setVisibility(View.VISIBLE);
//                    searchContentParent.setVisibility(View.GONE);
                    fillNoSearchContent();
                }else {
                    //有输入内容
                    fillSearchContent();
                    doSearchQuery(searchContent);
                }
            }
        });
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = (PoiItem) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("poi_item",item);
                setResult(RESULT_OK,intent);
                if(!TextUtils.isEmpty(searchEdit.getText().toString())){
                    UserInfosPref.getInstance(SearchAddrActivity.this).addHistory(new AddrHistoryItem(searchEdit.getText().toString()));
                    refreshHistoryList(UserInfosPref.getInstance(SearchAddrActivity.this).getAllHistory());
                }
                finish();
            }
        });

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusd) {
                if(focusd){
                    fillSearchContent();
                }
            }
        });
        searchEdit.clearFocus();
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(searchContent)){
                    OperationToast.showOperationResult(SearchAddrActivity.this,"请输入地址",R.mipmap.reminder_icon);
                    return;
                }
                Intent intent = new Intent(getApplication(),SearchAddrResultActivity.class);
                intent.putExtra("keyword",searchContent);
                intent.putExtra("num",num);
                startActivity(intent);
                finish();
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myLocation != null){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        clearSearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setText("");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AddrHistoryItem item = (AddrHistoryItem) adapterView.getAdapter().getItem(i);
        if(item != null && !TextUtils.isEmpty(item.getAddr())){
            searchEdit.setText(item.getAddr());
        }
    }

    class HistoryAdapter extends BaseAdapter{

        List<AddrHistoryItem> items;
        public HistoryAdapter(List<AddrHistoryItem> items){
            this.items = items;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {

            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            AddrHistoryItem item = (AddrHistoryItem) getItem(i);
            Holder holder = null;
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.addr_history_item,null);
                holder = new Holder();
                holder.historyText = (TextView) view.findViewById(R.id.text_history);
                view.setTag(holder);
            }else {
                holder = (Holder) view.getTag();
            }
            holder.historyText.setText(item.getAddr());
            return view;
        }

        class Holder{
            TextView historyText;
        }
    }

    private void fillNoSearchContent(){
        clearSearchImage.setVisibility(View.GONE);
        historyContentParent.setVisibility(View.VISIBLE);
        searchContentParent.setVisibility(View.GONE);
        searchImage.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.GONE);
    }

    private void fillSearchContent(){
        clearSearchImage.setVisibility(View.VISIBLE);
        historyContentParent.setVisibility(View.GONE);
        searchContentParent.setVisibility(View.VISIBLE);
        searchImage.setVisibility(View.GONE);
        searchText.setVisibility(View.VISIBLE);
        /*if (num >0) {
            searchText.setVisibility(View.VISIBLE);
        }else {
            searchText.setVisibility(View.GONE);
        }*/
    }

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private GeocodeSearch geocoderSearch;
    private int currentPage = 0;// 当前页面，从0开始计数

    List<PoiItem> poiItems;
    protected void doSearchQuery(final String keyWord) {
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "上海");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> items = poiResult.getPois();
                Log.e("-------","items:" + items.size());
//                poiType = POI_TYPE_SEARCH;
                if(items == null || items.size() == 0){
                    noFindText.setText("没有找到" +keyWord +"相关结果");
                    noFindText.setVisibility(View.VISIBLE);
                }else {
                    noFindText.setVisibility(View.GONE);
                }
                poiItems = items;
//                adapter.notifyDataSetChanged();
//                setPoiCenter();
                searchList.setAdapter(new AddrSearchAdapter(items,keyWord));

            }
        });
        poiSearch.searchPOIAsyn();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    public class AddrSearchAdapter extends BaseAdapter{

        List<PoiItem> poiItems;
        String searchContent;

        public AddrSearchAdapter(List<PoiItem> poiItems,String searchContent){
            this.poiItems = poiItems;
            this.searchContent = searchContent;
        }

        @Override
        public int getCount() {
            if (poiItems != null) {
                num = poiItems.size();
                return poiItems.size();
            }else {
                num = 0;
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {

            return poiItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(SearchAddrActivity.this, R.layout.search_addr_item, null);
                FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
                holder = new ViewHolder();
                holder.textAddress = (TextView) convertView.findViewById(R.id.tv_poi_name);
                holder.textDetail = (TextView) convertView.findViewById(R.id.tv_poi_detail);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            PoiItem item = (PoiItem) getItem(position);
            SpannableString str = new SpannableString(item.getTitle());
            int index = item.getTitle().indexOf(searchContent);
            if(index >= 0){
                str.setSpan(new ForegroundColorSpan(Color.parseColor("#20B1D9")),index,index + searchContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE );
            }else {
                str = new SpannableString(item.getTitle());
            }
            holder.textAddress.setText(str);
            holder.textDetail.setText(item.getSnippet());
            return convertView;
        }


    };

    public static class ViewHolder {
        public TextView textAddress;
        public TextView textDetail;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean isSucceed = LocationManager.get().isLocationSucceed();
            if (!isSucceed) {
                return;
            }
//            longtitude = LocationManager.get().getLongitude();
//            latitude = LocationManager.get().getLatitude();
//            setCenter();
        }
    };
}
