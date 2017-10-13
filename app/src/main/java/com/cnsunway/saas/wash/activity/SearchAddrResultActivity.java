package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.AddrHistoryItem;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sunway on 2017/7/20.
 */

public  class SearchAddrResultActivity extends InitActivity implements View.OnClickListener {
    @Bind(R.id.image_back)
    ImageView backImage;
    @Bind(R.id.image_search)
    ImageView searchImage;
    @Bind(R.id.list_search)
    ListView searchList;
    @Bind(R.id.tv_search_keyword)
    TextView keywordTv;
    @Bind(R.id.tv_search_num)
    TextView numTv;
    @Bind(R.id.tv_search)
    TextView searchTv;
    private String keyword;
    private int num;

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_addr_result);
        ButterKnife.bind(this);
        backImage.setOnClickListener(this);
        searchImage.setOnClickListener(this);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!TextUtils.isEmpty(keyword)){
                    UserInfosPref.getInstance(SearchAddrResultActivity.this).addHistory(new AddrHistoryItem(keyword));
                }
                PoiItem item = (PoiItem) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplication(),SelAddrAMapActivity.class);
                intent.putExtra("poi_item",item);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                startActivityForResult(intent,0);
//                setResult(RESULT_OK,intent);

            }
        });
        keyword = getIntent().getStringExtra("keyword");
        num = getIntent().getIntExtra("num",0);
        keywordTv.setText(keyword);
        numTv.setText(num+"");
        searchTv.setText(keyword);
        doSearchQuery(keyword);
    }
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private GeocodeSearch geocoderSearch;
    private int currentPage = 0;// 当前页面，从0开始计数

    List<PoiItem> poiItems;
    protected void doSearchQuery(final  String keyWord) {
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "上海");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> items = poiResult.getPois();
//                poiType = POI_TYPE_SEARCH;
                poiItems = items;
//                adapter.notifyDataSetChanged();
//                setPoiCenter();
                searchList.setAdapter(new AddrSearchAdapter(items,keyWord));
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }
    public class AddrSearchAdapter extends BaseAdapter {

        List<PoiItem> poiItems;
        String searchContent;

        public AddrSearchAdapter(List<PoiItem> poiItems,String searchContent){
            this.poiItems = poiItems;
            this.searchContent = searchContent;
        }

        @Override
        public int getCount() {
            if (poiItems != null) {
                return poiItems.size();
            }
            return 0;
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
            if (convertView == null) {
                convertView = View.inflate(SearchAddrResultActivity.this, R.layout.search_addr_item, null);
                FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
                AddressSelectAMapActivity.ViewHolder holder = new AddressSelectAMapActivity.ViewHolder();
                holder.textAddress = (TextView) convertView.findViewById(R.id.tv_poi_name);
                holder.textDetail = (TextView) convertView.findViewById(R.id.tv_poi_detail);
                convertView.setTag(holder);
            }
            AddressSelectAMapActivity.ViewHolder holder = (AddressSelectAMapActivity.ViewHolder) convertView.getTag();

            PoiItem item = (PoiItem) getItem(position);
            holder.textDetail.setText(item.getSnippet());
            int index = item.getTitle().indexOf(searchContent);
            SpannableString str = new SpannableString(item.getTitle());
            if(index >= 0){
                str.setSpan(new ForegroundColorSpan(Color.parseColor("#20B1D9")),index,index + searchContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE );
            }else {
                str = new SpannableString(item.getTitle());
            }
            holder.textAddress.setText(str);
            return convertView;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_search:
                finish();
                break;
        }
    }
}
