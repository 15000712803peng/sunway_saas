package com.cnsunway.wash.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.BackWashOrderDetailActivity;
import com.cnsunway.wash.activity.DoOrderActivity;
import com.cnsunway.wash.activity.LoginActivity;
import com.cnsunway.wash.activity.OrderDetailActivity2;
import com.cnsunway.wash.activity.WebActivity;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.dialog.ShareGiftDialog2;
import com.cnsunway.wash.dialog.WayOfShareDialog;
import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.helper.ApkUpgradeHelper;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.Marketing;
import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.model.User;
import com.cnsunway.wash.resp.GetImagesResp;
import com.cnsunway.wash.resp.OrderDetailResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.ShareUtil;
import com.cnsunway.wash.view.AdImagesPlayViewV2;
import com.cnsunway.wash.view.MyRecycleView;
import com.cnsunway.wash.view.OrderStatusView;
import com.cnsunway.wash.viewmodel.HomeOrderModel;
import com.cnsunway.wash.viewmodel.HomeViewModel;
import com.cnsunway.wash.viewmodel.ViewModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class HomeFragment2 extends BaseFragment{
    AdImagesPlayViewV2 adPlayView;
    List<View> adViews = new ArrayList<View>();
    JsonVolley getAdsVolley;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ViewPager orderPager;
    private boolean isLoadLastState = false;
    private int HORIZONTAL_VIEW_X = 0;
    MyRecycleView cRecycler;
    TextView doOrderText;

    LinearLayout serviceProcessParent;
    JsonVolley catogoriesVolley;
    LinearLayout servicerangeParent;
    LinearLayout healthwashParent;
    LinearLayout priceListParent;
    ApkUpgradeHelper updateHelper;
    LocationForService locationForService;

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
                    String action = order.getAction();
                    if(action.equals("share") || action.equals("direct")){
                        ShareGiftDialog2 shareGiftDialog  =  new ShareGiftDialog2(getActivity(),order).builder();
                        shareGiftDialog.setShareBtnClickedLinstener(new ShareGiftDialog2.OnShareBtnClickedLinstener() {
                            @Override
                            public void shareBtnClicked() {

                                WayOfShareDialog wayOfShareDialog = new WayOfShareDialog(getActivity()).builder();
                                new ShareUtil(wayOfShareDialog).share(getActivity(),order);
                            }
                        });
                        shareGiftDialog.show();
                    }
                }
                break;
        }


    }


    @Override
    protected void initFragmentDatas() {
        getActivity().registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_TABS));
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.bannder_loading)
                .showImageForEmptyUri(R.mipmap.bannder_loading)
                .showImageOnFail(R.mipmap.bannder_loading).cacheInMemory()
                .cacheOnDisc()
                .build();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();


    }

    @Override
    protected void initMyViews(View view) {
        adPlayView = (AdImagesPlayViewV2) view.findViewById(R.id.ad_play_view);
        orderPager = (ViewPager) view.findViewById(R.id.order_items_pager);
        cRecycler = (MyRecycleView) view.findViewById(R.id.c_recycler);
        doOrderText = (TextView) view.findViewById(R.id.text_do_order);
        serviceProcessParent = (LinearLayout) view.findViewById(R.id.ll_serviceprocess);
        serviceProcessParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebActivity.class);
                intent.putExtra("url",Const.Request.serviceprocess);
                intent.putExtra("title","服务流程");
                startActivity(intent);
            }
        });
        servicerangeParent = (LinearLayout) view.findViewById(R.id.ll_servicerange);
        servicerangeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebActivity.class);
                intent.putExtra("url",Const.Request.servicerange);
                intent.putExtra("title","取送范围");
                startActivity(intent);
            }
        });
        healthwashParent = (LinearLayout) view.findViewById(R.id.ll_healthwash);
        healthwashParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebActivity.class);
                intent.putExtra("url",Const.Request.healthwash);
                intent.putExtra("title","健康洗衣");
                startActivity(intent);
            }
        });
        priceListParent = (LinearLayout) view.findViewById(R.id.ll_priceList);
        priceListParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebActivity.class);
                intent.putExtra("url", Const.Request.storeShow);
                intent.putExtra("title","门店展示");
                startActivity(intent);
            }
        });
        doOrderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserInfosPref.getInstance(getActivity()).getUser() == null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(getActivity(), DoOrderActivity.class));
                }
            }
        });

        initAds();
        initOrderPager();
        initCategoryRecycler();
    }

    private void loadData(){

        requestAds();
        requestArea();
        User user = UserInfosPref.getInstance(getActivity()).getUser();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        if(user == null){
            initOrderPager();
            return;
        }
        requestHomeOrders();
    }

    private void initAds(){
        View defaultAd = LayoutInflater.from(getActivity()).inflate(
                R.layout.ad_item, null);
        adViews.clear();;
        adViews.add(defaultAd);
        adPlayView.addViews(adViews);
    }

    private void initOrderPager(){
            orderPager.setAdapter(new DefaultOrderAdapter());
    }

    private int dip2px(float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getActivity().getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
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
            getAdsVolley.requestGet(Const.Request.showImages,getHandler(), "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );

    }

    private void requestArea(){
        catogoriesVolley = new JsonVolley(getActivity(), Const.Message.MSG_CATEGORIS_SUCC,Const.Message.MSG_CATEGORIS_FAIL);
        catogoriesVolley.requestGet(Const.Request.categoris,getHandler(), "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void initCategoryRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = cRecycler.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        }
        //高度等于＝条目的高度＋ 10dp的间距 ＋ 10dp（为了让条目居中）
        layoutParams.height = dip2px(100);
        cRecycler.setLayoutParams(layoutParams);
        cRecycler.setLayoutManager(layoutManager);
        cRecycler.addItemDecoration(new SpaceItemDecoration(8));
        cRecycler.setAdapter(new CategoryAdapter());

    }

    class CategoryAdapter extends RecyclerView.Adapter{
        int[] imges = new int[]{R.mipmap.area1,R.mipmap.area2,R.mipmap.area3,R.mipmap.area4,R.mipmap.area5,R.mipmap.area6,R.mipmap.area7};

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.imge_item,null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MyHolder myHolder= (MyHolder) holder;
            myHolder.categoryImage.setImageResource(imges[position]);
            myHolder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",Const.Request.priceList+"?jumpToIdx="+(position + 1));
                    intent.putExtra("title","价目表");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imges.length;
        }

        class MyHolder extends RecyclerView.ViewHolder{

            public ImageView categoryImage;
            public MyHolder(View itemView) {
                super(itemView);
                categoryImage = (ImageView) itemView.findViewById(R.id.image_category);
            }
        }
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
            orderPager.setAdapter(new OrderAdapter(homeOrderModel.getHomeLists()));

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
                View orderView = inflater.inflate(R.layout.home_order2,null);
                orderView.setTag(o);
                fillViewByOrder(o,orderView);
                orderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(o.getType() == 1){
                            Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                            intent.putExtra("order_no", o.getOrderNo());
                            startActivityForResult(intent, 1);
                        }else if(o.getType() == 2){
                            Intent intent = new Intent(getActivity(), BackWashOrderDetailActivity.class);
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

        adViews.clear();
            for (final Marketing marketing : marketings) {
                View view = LayoutInflater.from(getActivity()).inflate(
                        R.layout.market_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
                view.setTag(marketing);
                imageLoader.displayImage(marketing.getPicUrl(), imageView, options);
                adViews.add(view);

            }

            adPlayView.addViews(adViews);
            adPlayView.setItemClickedListener(new AdImagesPlayViewV2.ItemClickedListener() {
                @Override
                public void itemClicked(int position) {

                    Marketing marketing = (Marketing) adViews.get(position).getTag();
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
            });
            adPlayView.recycle();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_home2,container,false));
            initMyViews(getView());
            updateHelper = new ApkUpgradeHelper(getActivity());
            updateHelper.check(false);

        }
        loadData();

        return getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(refreshReceiver);
        super.onDestroy();
    }
}
