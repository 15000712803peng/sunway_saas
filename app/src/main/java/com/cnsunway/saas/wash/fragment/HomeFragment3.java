package com.cnsunway.saas.wash.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.BackWashOrderDetailActivity;
import com.cnsunway.saas.wash.activity.DoOrderActivity;
import com.cnsunway.saas.wash.activity.EvaluateActivity;
import com.cnsunway.saas.wash.activity.GetPayOrderActivity;
import com.cnsunway.saas.wash.activity.LoginActivity;
import com.cnsunway.saas.wash.activity.OrderDetailActivity;
import com.cnsunway.saas.wash.activity.OrderDetailActivity2;
import com.cnsunway.saas.wash.activity.ServiceAreaActivity;
import com.cnsunway.saas.wash.activity.WebActivity;
import com.cnsunway.saas.wash.adapter.HomeStoreAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.LoadingDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.ServiceCityDialog;
import com.cnsunway.saas.wash.dialog.ShareGiftDialog2;
import com.cnsunway.saas.wash.dialog.WayOfShareDialog;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.helper.ApkUpgradeHelper;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Marketing;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.RowsOrder;
import com.cnsunway.saas.wash.model.RowsStore;
import com.cnsunway.saas.wash.model.ServiceCity;
import com.cnsunway.saas.wash.model.Store;
import com.cnsunway.saas.wash.model.User;
import com.cnsunway.saas.wash.resp.AllCityResp;
import com.cnsunway.saas.wash.resp.GetImagesResp;
import com.cnsunway.saas.wash.resp.OrderDetailResp;
import com.cnsunway.saas.wash.resp.RowsOrderResp;
import com.cnsunway.saas.wash.resp.RowsStoreResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.LocationManager;
import com.cnsunway.saas.wash.util.ShareUtil;
import com.cnsunway.saas.wash.view.OrderStatusView;
import com.cnsunway.saas.wash.view.ScaleInTransformer;
import com.cnsunway.saas.wash.viewmodel.HomeOrderModel;
import com.cnsunway.saas.wash.viewmodel.HomeViewModel;
import com.cnsunway.saas.wash.viewmodel.ViewModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment3 extends BaseFragment implements View.OnClickListener,OnBannerListener,LoadingDialogInterface,ServiceCityDialog.OnCitySelectedLinstenr {
    Banner banner;
    List<String> imageUrls = new ArrayList<String>();
    JsonVolley getAdsVolley;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ViewPager orderPager;
    private boolean isLoadLastState = false;
    private int HORIZONTAL_VIEW_X = 0;
    TextView doOrderText;

    JsonVolley catogoriesVolley;
    LinearLayout citySelect;

    JsonVolley recommendStoresVolley;


    ApkUpgradeHelper updateHelper;


    JsonVolley confirmDoneVolley;
    TextView cityText;
    ServiceCity defaultCity;
    ServiceCityDialog cityDialog;

    ServiceCity locateCity;
    LocationForService locationForService;
    ListView storeList;
    TextView loadStoreText;
    BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String city = intent.getStringExtra("city");
            if(!TextUtils.isEmpty(city)){
                locateCity  = (ServiceCity) JsonParser.jsonToObject(city,ServiceCity.class);
                if(cityDialog != null && cityDialog.isShowing()){
                    cityDialog.setCurrentCity(selectedCity);
                }
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        getCityVolley.requestGet(Const.Request.cityList,handler,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                    }
                });
            }
//            Toast.makeText(getActivity(),"location succ:" + city,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_IMAGES_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    UserInfosPref.getInstance(getActivity()).hasShowMarkeing(true);
                    GetImagesResp resp = (GetImagesResp) JsonParser.jsonToObject(msg.obj + "", GetImagesResp.class);
                    List<Marketing> marketings = resp.getData();
                    if(marketings != null && marketings.size() > 0){
                        loadAds(marketings);
                    }else{
                        OperationToast.showOperationResult(getActivity(), R.string.no_marketing);
                    }
                }else{
                    OperationToast.showOperationResult(getActivity(),R.string.get_marketing_fail);
                }
                break;
            case Const.Message.MSG_GET_IMAGES_FAIL:
                OperationToast.showOperationResult(getActivity(),R.string.get_marketing_fail);
                break;

            case Const.Message.MSG_ORDER_DETAIL_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
                    final Order order = detailResp.getData();

                }
                break;

            case Const.Message.MSG_CONFIRM_DONE_SUCC:

                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OperationToast.showOperationResult(getActivity(),"确认成功",0);
                    getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                }else {
                    OperationToast.showOperationResult(getActivity(),"操作失败",0);
                }
                break;

            case Const.Message.MSG_SERVICE_CITY_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AllCityResp allCityResp = (AllCityResp) JsonParser.jsonToObject(msg.obj+ "",AllCityResp.class);
                    List<ServiceCity> cities = allCityResp.getData();
                    if(cities == null || cities.size() <= 0){
                        OperationToast.showOperationResult(getActivity(),"没有可以选择的城市",0);
                    }else {

                        cityDialog.setCities(cities);
                        cityDialog.setCurrentCity(selectedCity);
                        cityDialog.setOnCitySelectedLinstenr(HomeFragment3.this);
                        cityDialog.show();
                    }
                }else {

                }
                break;
            case Const.Message.MSG_SERVICE_CITY_FAIL:
                OperationToast.showOperationResult(getActivity(),"网络异常",0);
                break;

            case Const.Message.MSG_GET_CITIES_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AllCityResp allCityResp = (AllCityResp) JsonParser.jsonToObject(msg.obj+ "",AllCityResp.class);
                    List<ServiceCity> cities = allCityResp.getData();
                    if(cities == null || cities.size() <= 0){

                    }else {
                        if(locateCity != null){
                            for(ServiceCity city : cities){
                                if(locateCity.equals(city)){
                                    selectedCity = city;
                                    cityText.setText(selectedCity.getCityName());
                                    UserInfosPref.getInstance(getActivity()).setServiceCityCode(selectedCity.getCityCode());
                                    return;
                                }
                            }
                        }
                    }
                }else {

                }
                break;

            case Const.Message.MSG_GET_CITIES_FAIL:
                break;

            case Const.Message.MSG_GET_RECC_STORES_SUCC:
                loadStoreText.setVisibility(View.GONE);

                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    RowsStoreResp initResp = (RowsStoreResp) JsonParser.jsonToObject(msg.obj + "", RowsStoreResp.class);

//                    RowsStore rowsStore = initResp.getData();
                    if(initResp != null) {
                        List<Store> stores = initResp.getData();
                        storeList.setVisibility(View.VISIBLE);
                        storeList.setAdapter(new HomeStoreAdapter(getActivity(),stores));

                    }else{

                    }

                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {

                    Bundle bundle = msg.getData();
                    if(bundle != null && bundle.getString("data") != null){
                        BaseResp resp = (BaseResp) JsonParser.jsonToObject(bundle.getString("data"), BaseResp.class);

                    }
                }
                break;
            case Const.Message.MSG_GET_RECC_STORES_FAIL:

                break;

        }


    }

    StringVolley selectCityVolley;
    StringVolley getCityVolley;
    ServiceCity selectedCity;


    @Override
    protected void initFragmentDatas() {
        getActivity().registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.bannder_loading)
                .showImageForEmptyUri(R.mipmap.bannder_loading)
                .showImageOnFail(R.mipmap.bannder_loading).cacheInMemory()
                .cacheOnDisc()
                .build();
        defaultCity = new ServiceCity();
        defaultCity.setCityName("上海市");
        defaultCity.setCityCode("021");
        defaultCity.setLatitude(31.139974);
        defaultCity.setLongitude(121.485011);
        selectedCity = defaultCity;
        cityDialog  = new ServiceCityDialog(getActivity()).builder();
        cityDialog.setCurrentCity(selectedCity);
        getCityVolley = new StringVolley(getActivity(),Const.Message.MSG_GET_CITIES_SUCC,Const.Message.MSG_GET_CITIES_FAIL);
        orderDetailVolley = new JsonVolley(getActivity(), Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);

        getActivity().registerReceiver(locationReceiver,new IntentFilter(Const.Action.ACTION_LOCATION_SUCCEED));
        getActivity().registerReceiver(cityChangedReciver,new IntentFilter(Const.MyFilter.FILTER_CITY_CHANGED));
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();

    }

    @Override
    protected void initMyViews(View view) {
        banner = (Banner) view.findViewById(R.id.ad_play_view);
        orderPager = (ViewPager) view.findViewById(R.id. order_items_pager);
//        doOrderText = (TextView) view.findViewById(R.id.text_do_order);

        cityText = (TextView) view.findViewById(R.id.text_city);
        banner.setFocusable(true);
        banner.setFocusableInTouchMode(true);
        banner.requestFocus();
        storeList = (ListView) view.findViewById(R.id.list_store);
        loadStoreText = (TextView) view.findViewById(R.id.txt_load_store);
        storeList.setVisibility(View.INVISIBLE);
//        storeList.setAdapter(new HomeStoreAdapter(getActivity(),null));

        cityText.setText(selectedCity.getCityName());
        UserInfosPref.getInstance(getActivity()).setServiceCityCode(selectedCity.getCityCode());



        citySelect = (LinearLayout) view.findViewById(R.id.ll_city_select);
        citySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityVolley = new StringVolley(getActivity(),Const.Message.MSG_SERVICE_CITY_SUCC,Const.Message.MSG_SERVICE_CITY_FAIL);
                selectCityVolley.requestGet(Const.Request.cityList,handler,HomeFragment3.this,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
            }
        });


//        doOrderText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(UserInfosPref.getInstance(getActivity()).getUser() == null){
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }else {
//                    startActivity(new Intent(getActivity(), DoOrderActivity.class));
//                }
//            }
//        });
        confirmDoneVolley = new JsonVolley(getActivity(),Const.Message.MSG_CONFIRM_DONE_SUCC,Const.Message.MSG_PAY_CONFIRM_FAIL);

//        cardVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUser().getMobile());

        initAds();
        initOrderPager();
        initCategoryRecycler();
    }

    private void loadData(){

        requestAds();
        requestArea();
        requestRecommendStores();
        User user = UserInfosPref.getInstance(getActivity()).getUser();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        if(user == null){
            initOrderPager();
            return;
        }
        requestHomeOrders();
    }

    private void initAds(){
//        View defaultAd = LayoutInflater.from(getActivity()).inflate(
//                R.layout.ad_item, null);
//        List<String> defalutUrls = new ArrayList<>();
//        adPlayView.setImages(adViews)
//                .setImageLoader(null).start();
//        adPlayView.addViews(adViews);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

    }

    private void initOrderPager(){

        orderPager.setAdapter(new DefaultOrderAdapter());

    }

    private int dip2px(float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getActivity().getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void OnBannerClick(int position) {
        List<Marketing> marketings = (List<Marketing>) banner.getImages();
        Marketing marketing = marketings.get(position);
        if(marketing.isNeedLogin()){
            if(UserInfosPref.getInstance(getActivity()).getUser() != null){
                if (!TextUtils.isEmpty(marketing.getMarketingUrl())) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", marketing.getMarketingUrl());
                    intent.putExtra("title", "活动");
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }else {
            if (!TextUtils.isEmpty(marketing.getMarketingUrl())) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", marketing.getMarketingUrl());
                intent.putExtra("title", "活动");
                startActivity(intent);
            }
        }



    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    LoadingDialog loadingDialog;
    protected void showLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(getActivity(), message);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void showLoading() {
        showLoadingDialog(getString(R.string.operating));
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    @Override
    public void onCitySelected(ServiceCity city) {
        if(city != null){
            selectedCity = city;
            if(selectedCity.getCityCode().equals("021")){
                selectedCity.setLatitude(31.139974);
                selectedCity.setLongitude(121.485011);
            }else if(selectedCity.getCityCode().equals("028")){
                selectedCity.setLatitude(30.579099);
                selectedCity.setLongitude(104.068138);
            }
            cityText.setText(city.getCityName());
            UserInfosPref.getInstance(getActivity()).setServiceCityCode(selectedCity.getCityCode());
        }

    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(int space) {
            this.mSpace = dip2px(space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
//                Log.d(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);

            outRect.left = mSpace;
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.right = 0;

//
//                if (pos != (itemCount -1)) {
//                    outRect.right = mSpace;
//                } else {
//                    outRect.right = 0;
//                }
        }
    }

    private void requestAds(){
        getAdsVolley = new JsonVolley(getActivity(), Const.Message.MSG_GET_IMAGES_SUCC,Const.Message.MSG_GET_IMAGES_FAIL);
        getAdsVolley.requestGet(Const.Request.showImages,getHandler(), "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    private void requestRecommendStores(){
        recommendStoresVolley = new JsonVolley(getActivity(),Const.Message.MSG_GET_RECC_STORES_SUCC,Const.Message.MSG_GET_RECC_STORES_FAIL);
        recommendStoresVolley.addParams("lat","31.088448");
        recommendStoresVolley.addParams("lng","121.432977");
        recommendStoresVolley._requestPost(Const.Request.recommendStores,getHandler(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    private void requestArea(){
        catogoriesVolley = new JsonVolley(getActivity(), Const.Message.MSG_CATEGORIS_SUCC,Const.Message.MSG_CATEGORIS_FAIL);
        catogoriesVolley.requestGet(Const.Request.categoris,getHandler(), "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void initCategoryRecycler(){

    }


    HomeOrderModel homeOrderModel;
    private void requestHomeOrders(){
        User user = UserInfosPref.getInstance(getActivity()).getUser();
        if(user != null){
            homeOrderModel = new HomeOrderModel();
            homeOrderModel.addPropertyChangeListener(ViewModel.PROPERTY_NETREQUEST_STATUS, volleyStatusListener);
            homeOrderModel.addPropertyChangeListener(HomeViewModel.PROPERTY_RESPONSE, serverErrorListener);
            homeOrderModel.addPropertyChangeListener(HomeViewModel.PROPERTY_HOMELISTS, homeListListener);
            homeOrderModel.requestHomeData(getActivity());

        }

    }



    private PropertyChangeListener volleyStatusListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            switch (homeOrderModel.getRequestStatus()) {
                case ViewModel.STATUS_NET_ERROR:
                case ViewModel.STATUS_SERVER_ERROR:
                    break;
                case ViewModel.STATUS_REQUEST_SUCC:
                    break;
                case ViewModel.STATUS_REQUEST_FAIL:
                    break;
            }
        }
    };

    private PropertyChangeListener homeListListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {

            if (homeOrderModel.getHomeLists() == null || homeOrderModel.getHomeLists().size() <= 0) {
                orderPager.setAdapter(new DefaultOrderAdapter());
                return;
            }
            if(getActivity() == null){
                return;
            }
            orderPager.setOffscreenPageLimit(homeOrderModel.getHomeLists().size());
            orderPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen._10dp));
//            orderPager.setPageTransformer(true, new ZoomOutPageTransformer());
            orderPager.setPageTransformer(true, new ScaleInTransformer());
            orderPager.setAdapter(new OrderAdapter2(homeOrderModel.getHomeLists()));

        }
    };

    class DefaultOrderAdapter extends PagerAdapter{

        List<View> views = new ArrayList<>();
        public DefaultOrderAdapter(){
            views.add(LayoutInflater.from(getActivity()).inflate(R.layout.padding_image,null));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    class OrderAdapter extends PagerAdapter{

        List<Order> orders;
        List<View> views = new ArrayList<>();
        LayoutInflater inflater;

        private void fillViewByOrder(Order order,View view){
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTime.setText(HomeViewModel.getOrderTimeForHome2(order));
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
            ImageView arrowImage = (ImageView) view.findViewById(R.id.image_arrow);
            ImageView dotImage = (ImageView) view.findViewById(R.id.image_dot);
            tvStatus.setText(HomeViewModel.getOrderStatus(order));
            OrderStatusView orderStatus = (OrderStatusView) view.findViewById(R.id.order_status);
            orderStatus.setOrder(order);
            switch (orderStatus.getStatus()){
                case OrderStatusView.STATUS_FETCH:
                    arrowImage.setImageResource(R.mipmap.clock1);
                    dotImage.setImageResource(R.drawable.dot1);
                    break;
                case OrderStatusView.STATUS_WAITPAY:
                    arrowImage.setImageResource(R.mipmap.clock2);
                    dotImage.setImageResource(R.drawable.dot2);
                    break;
                case OrderStatusView.STATUS_WASH:
                    arrowImage.setImageResource(R.mipmap.clock3);
                    dotImage.setImageResource(R.drawable.dot3);
                    break;
                case OrderStatusView.STATUS_SENDBACK:
                    arrowImage.setImageResource(R.mipmap.clock4);
                    dotImage.setImageResource(R.drawable.dot4);
                    break;
                case OrderStatusView.STATUS_DONE:
                case OrderStatusView.STATUS_ARRIVED:
                    arrowImage.setImageResource(R.mipmap.clock5);
                    dotImage.setImageResource(R.drawable.dot5);
                    break;
            }
        }


        public OrderAdapter(List<Order> orders){
            this.orders = orders;
            inflater =  LayoutInflater.from(getActivity());
            for(final Order o : orders){
                View orderView = inflater.inflate(R.layout.home_order_item,null);
                orderView.setTag(o);
                fillViewByOrder(o,orderView);
                orderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(o.getType() == 1){
                            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", o.getOrderNo());
                            startActivityForResult(intent, 1);
                        }else if(o.getType() == 2){
                            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", o.getOrderNo());
                            startActivityForResult(intent, 1);
                        }
                    }
                });
                views.add(orderView);
            }
        }


        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Order order = orders.get(position);
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    final int OPERATION_ORDER_PAY = 2;

    class OrderAdapter2 extends PagerAdapter{

        private void fillOrderStutas(final  Order order, OrderAdapter2.ViewHolder holder){
            int stutas = HomeViewModel.getOrderSimpleStatus(order);
            holder.tvTips.setVisibility(View.VISIBLE);
            holder.operationText.setTextColor(Color.parseColor("#ffffff"));
            switch (stutas){
                case HomeViewModel.ORDER_STUTAS_ONE:
                    //预约完成
                    holder.imageArrow.setImageResource(R.mipmap.clock1);
                    holder.dotImage.setImageResource(R.drawable.dot1);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    String appointTime = "";
                    if (!TextUtils.isEmpty(order.getExpectDateB()) && !TextUtils.isEmpty(order.getExpectDateE())) {
                        appointTime = DateUtil.getServerDate(order.getExpectDateB()).substring(0, order.getExpectDateB().length() - 3) + "-" + DateUtil.getServerDate(order.getExpectDateE()).substring(order.getExpectDateE().length() - 8, order.getExpectDateE().length() - 3);
                    }
                    holder.tvTips.setText(getString(R.string.tips_appointment_time) + appointTime);

                    break;
                case HomeViewModel.ORDER_STUTAS_TWO:
                    //待上门
                    holder.imageArrow.setImageResource(R.mipmap.clock1);
                    holder.dotImage.setImageResource(R.drawable.dot1);
                    holder.tvTips.setText(getString(R.string.tips_fetcher_name) + order.getPickerName()+",");
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(order.getPickerMobile())){
                        holder.phnoeText.setText(order.getPickerMobile());
                        holder.phnoeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(getActivity()).setTitle("联系取件员")
                                        .setMessage("确定拨打取件员电话："+  order.getPickerMobile()+"吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(order.getPickerMobile()));
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                            }
                        });
                    }

                    break;
                case HomeViewModel.ORDER_STUTAS_THREE:
                    //上门中
                    holder.imageArrow.setImageResource(R.mipmap.clock1);
                    holder.dotImage.setImageResource(R.drawable.dot1);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("计价中，请您仔细检查避免衣物中夹杂个人物品");
//
                    break;
                case HomeViewModel.ORDER_STUTAS_FOUR:
                    //待支付
                    holder.imageArrow.setImageResource(R.mipmap.clock2);
                    holder.dotImage.setImageResource(R.drawable.dot2);
                    holder.operationText.setVisibility(View.VISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("件数：" + order.getClothesNum() + "件" + "    金额：" + order.getTotalPrice() + "元");
                    holder.operationText.setText("立即支付");
                    holder.operationText.setBackgroundResource(R.drawable.orange_shape);
//               holder.tvTips.setText();
                    holder.operationText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), GetPayOrderActivity.class);
//                            intent.putExtra("order_no", order.getOrderNo());
//                            intent.putExtra("order_price", order.getTotalPrice());
//                            intent.putExtra("order", order);
//                            intent.putExtra("order_index",order.getIndex());
//                            startActivityForResult(intent,OPERATION_ORDER_PAY);
                            if(order.getType() == 1){
                                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                                intent.putExtra("order_no", order.getOrderNo());
                                startActivityForResult(intent, 1);
                            }else if(order.getType() == 2){
                                Intent intent = new Intent(getActivity(), BackWashOrderDetailActivity.class);
                                intent.putExtra("order_no", order.getOrderNo());
                                startActivityForResult(intent, 1);
                            }
                        }
                    });

                    break;
                case HomeViewModel.ORDER_STUTAS_FIVE:
                    //已支付
                    holder.imageArrow.setImageResource(R.mipmap.clock2);
                    holder.dotImage.setImageResource(R.drawable.dot2);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("件数：" + order.getClothesNum() + "件" + "    金额：" + order.getTotalPrice() + "元");
                    break;
                case HomeViewModel.ORDER_STUTAS_SIX:
                    //取件完成
                    holder.imageArrow.setImageResource(R.mipmap.clock2);
                    holder.dotImage.setImageResource(R.drawable.dot2);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                    break;
                case HomeViewModel.ORDER_STUTAS_SEVEN:
                    //入库检验
                    holder.imageArrow.setImageResource(R.mipmap.clock3);
                    holder.dotImage.setImageResource(R.drawable.dot3);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                    break;
                case HomeViewModel.ORDER_STUTAS_EIGHT:
                    //洗涤中
                    holder.imageArrow.setImageResource(R.mipmap.clock3);
                    holder.dotImage.setImageResource(R.drawable.dot3);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                    break;
                case HomeViewModel.ORDER_STUTAS_NINE:
                    //洗涤完成
                    holder.imageArrow.setImageResource(R.mipmap.clock3);
                    holder.dotImage.setImageResource(R.drawable.dot3);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                    break;
                case HomeViewModel.ORDER_STUTAS_TEN:
                    // 送返中
                    holder.imageArrow.setImageResource(R.mipmap.clock4);
                    holder.dotImage.setImageResource(R.drawable.dot4);
                    holder.operationText.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.VISIBLE);
                    holder.tvTips.setText(getString(R.string.tips_sender_name) + order.getDelivererName()+",");
                    if(!TextUtils.isEmpty(order.getDelivererMobile())){
                        holder.phnoeText.setText(order.getDelivererMobile());
                        holder.phnoeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(getActivity()).setTitle("联系送件员")
                                        .setMessage("确定拨打送件员电话："+  order.getDelivererMobile()+"吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(order.getPickerMobile()));
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                            }
                        });
                    }
                    break;
                case HomeViewModel.ORDER_STUTAS_ELEVEN:
                    //已送达
                    holder.imageArrow.setImageResource(R.mipmap.clock4);
                    holder.dotImage.setImageResource(R.drawable.dot4);
                    holder.operationText.setVisibility(View.VISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.tvTips.setText("请您仔细检查衣物后确认收衣");
                    holder.operationText.setBackgroundResource(R.drawable.green_shape);
                    holder.operationText.setText("确认收衣");
                    holder.operationText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                       OperationToast.showOperationResult(activity,"confirm", Toast.LENGTH_LONG);
//                            if(confirmClickedListenr != null){
//                                confirmClickedListenr.confirmClicked(order.getOrderNo());
//                            }
                            confirmDoneVolley.addParams("orderNo", order.getOrderNo());
                            confirmDoneVolley.requestPost(Const.Request.confirmDone,HomeFragment3.this,getHandler(),UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                        }
                    });

                    break;
                case HomeViewModel.ORDER_STUTAS_TWELVE:
                    //已完成
                    holder.imageArrow.setImageResource(R.mipmap.clock5);
                    holder.dotImage.setImageResource(R.drawable.dot5);
                    if(order.getOrderStatus() == Const.OrderStatus.ORDER_STATUS_DELIVERED){
                        holder.tvTips.setText("您的评价是我们提升服务的动力");
                        holder.phnoeText.setVisibility(View.GONE);
                        holder.operationText.setVisibility(View.VISIBLE);
                        holder.operationText.setBackgroundResource(R.drawable.yellow_shape);
                        holder.operationText.setText("评价打分");
                        holder.operationText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), EvaluateActivity.class);
                                intent.putExtra("order_no", order.getOrderNo());
                                intent.putExtra("sender_name", order.getPickerName());
                                startActivity(intent);
                            }
                        });
                        //没有评价
                    }else if(order.getOrderStatus() == Const.OrderStatus.ORDER_STATUS_COMPLETED){
                        holder.tvTips.setVisibility(View.INVISIBLE);
                        holder.phnoeText.setVisibility(View.GONE);
                        holder.operationText.setVisibility(View.VISIBLE);
                        holder.operationText.setBackgroundResource(R.drawable.gray_border);
                        holder.operationText.setText("再来一单");
                        holder.operationText.setTextColor(Color.parseColor("#878D94"));
//                   holder.operationText.setBackgroundResource(R.drawable.);
                        holder.operationText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getActivity(), DoOrderActivity.class));
                            }
                        });
                    }
                    break;
                default:
            }
        }

        class ViewHolder {
            @Bind(R.id.image_arrow)
            ImageView imageArrow;
            @Bind(R.id.image_dot)
            ImageView dotImage;
            @Bind(R.id.tv_time)
            TextView tvTime;
            @Bind(R.id.tv_tips)
            TextView tvTips;
            @Bind(R.id.tv_status)
            TextView tvStatus;
            @Bind(R.id.order_status)
            OrderStatusView orderStatus;
            @Bind(R.id.tv_operation)
            TextView operationText;
            @Bind(R.id.tv_phone)
            TextView phnoeText;
            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        List<Order> orders;
        List<View> views = new ArrayList<>();
        LayoutInflater inflater;

        private void fillViewByOrder(Order order,View view){
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTime.setText(HomeViewModel.getOrderTimeForHome2(order));
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
            ImageView arrowImage = (ImageView) view.findViewById(R.id.image_arrow);
            ImageView dotImage = (ImageView) view.findViewById(R.id.image_dot);
            tvStatus.setText(HomeViewModel.getOrderStatus(order));
            OrderStatusView orderStatus = (OrderStatusView) view.findViewById(R.id.order_status);
            orderStatus.setOrder(order);
            switch (orderStatus.getStatus()){
                case OrderStatusView.STATUS_FETCH:
                    arrowImage.setImageResource(R.mipmap.clock1);
                    dotImage.setImageResource(R.drawable.dot1);
                    break;
                case OrderStatusView.STATUS_WAITPAY:
                    arrowImage.setImageResource(R.mipmap.clock2);
                    dotImage.setImageResource(R.drawable.dot2);
                    break;
                case OrderStatusView.STATUS_WASH:
                    arrowImage.setImageResource(R.mipmap.clock3);
                    dotImage.setImageResource(R.drawable.dot3);
                    break;
                case OrderStatusView.STATUS_SENDBACK:
                    arrowImage.setImageResource(R.mipmap.clock4);
                    dotImage.setImageResource(R.drawable.dot4);
                    break;
                case OrderStatusView.STATUS_DONE:
                case OrderStatusView.STATUS_ARRIVED:
                    arrowImage.setImageResource(R.mipmap.clock5);
                    dotImage.setImageResource(R.drawable.dot5);
                    break;
            }
        }



        public OrderAdapter2(List<Order> orders){
            this.orders = orders;
            inflater =  LayoutInflater.from(getActivity());
            for(final Order o : orders){
                View orderView = inflater.inflate(R.layout.home_order_item,null);
                orderView.setTag(o);
                ViewHolder viewHolder = new ViewHolder(orderView);
                viewHolder.orderStatus.setOrder(o);
                viewHolder.tvTime.setText(HomeViewModel.getOrderTimeForHome2(o));
                viewHolder.tvStatus.setText(HomeViewModel.getOrderStatus(o));
                fillOrderStutas(o,viewHolder);
                orderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(o.getType() == 1){
                            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", o.getOrderNo());
                            startActivityForResult(intent, 1);
                        }else if(o.getType() == 2){
                            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", o.getOrderNo());
                            startActivityForResult(intent, 1);
                        }
                    }
                });
                views.add(orderView);
            }
        }


        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Order order = orders.get(position);
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    private PropertyChangeListener serverErrorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            BaseResp resp = homeOrderModel.getResp();
            if (resp.getResponseCode() != 0) {
                String msg = resp.getResponseMsg();
                //显示错误
            }
        }
    };

    private void loadAds(List<Marketing> marketings){

//            for (final Marketing marketing : marketings) {
//                View view = LayoutInflater.from(getActivity()).inflate(
//                        R.layout.market_item, null);
//                ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
//                view.setTag(marketing);
//
//                adViews.add(view);
//            }
        banner.setImages(marketings)
                .setImageLoader(new ImageLoaderInterface(){
                    @Override
                    public void displayImage(Context context, Object path, View view) {
                        Marketing marketing = (Marketing) path;
                        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
                        imageLoader.displayImage(marketing.getPicUrl(), imageView, options);
                    }

                    @Override
                    public View createImageView(Context context) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.market_item, null);
                        return view;
                    }
                }).setOnBannerListener(this).start();


//            adPlayView.addViews(adViews);
//            adPlayView.setItemClickedListener(new AdImagesPlayViewV2.ItemClickedListener() {
//                @Override
//                public void itemClicked(int position) {
//
//                    Marketing marketing = (Marketing) adViews.get(position).getTag();
//                    if(marketing.isNeedLogin()){
//                        if(UserInfosPref.getInstance(getActivity()).getUser() != null){
//                            if (!TextUtils.isEmpty(marketing.getMarketingUrl())) {
//                                Intent intent = new Intent(getActivity(), WebActivity.class);
//                                intent.putExtra("url", marketing.getMarketingUrl());
//                                intent.putExtra("title", "活动");
//                                startActivity(intent);
//                            }
//                        }else {
//                            Intent intent = new Intent(getActivity(), LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    }else {
//                        if (!TextUtils.isEmpty(marketing.getMarketingUrl())) {
//                            Intent intent = new Intent(getActivity(), WebActivity.class);
//                            intent.putExtra("url", marketing.getMarketingUrl());
//                            intent.putExtra("title", "活动");
//                            startActivity(intent);
//                        }FV
//                    }
//
//                }
//            });
//            adPlayView.recycle();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_home3,container,false));
//            FontUtil.applyFont(getActivity(), getView(), "OpenSans-Regular.ttf");
            initMyViews(getView());
            updateHelper = new ApkUpgradeHelper(getActivity());
            updateHelper.check(false);
            LocationManager.get().init(getActivity().getApplicationContext());

        }
        loadData();

        return getView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_ORDER_PAY && resultCode == Activity.RESULT_OK) {
            if(data == null){
                return;
            }
            final Order order = (Order)data.getSerializableExtra("order");
            if(order == null){
                return;
            }
//            if(order.getIndex() == -1){
//                return;
//            }
            if(TextUtils.isEmpty(order.getOrderNo())){
                return;
            }
            orderDetailVolley.requestGet(Const.Request.detail + "/" + order.getOrderNo(), getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
//            getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));

        }

    }

    JsonVolley orderDetailVolley;

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(refreshReceiver);
        getActivity().unregisterReceiver(locationReceiver);
        getActivity().unregisterReceiver(cityChangedReciver);
        super.onDestroy();
    }




    BroadcastReceiver cityChangedReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String cityStr = intent.getStringExtra("selected_city");
                if(!TextUtils.isEmpty(cityStr)){
                    ServiceCity city = (ServiceCity) JsonParser.jsonToObject(cityStr,ServiceCity.class);
                    onCitySelected(city);

                }
            }
        }
    };
}
