//package com.cnsunway.wash.fragment;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.cnsunway.wash.R;
//import com.cnsunway.wash.activity.BackWashOrderDetailActivity;
//import com.cnsunway.wash.activity.DoOrderActivity;
//import com.cnsunway.wash.activity.LoginActivity;
//import com.cnsunway.wash.activity.MenuActivity;
//import com.cnsunway.wash.activity.OrderDetailActivity2;
//import com.cnsunway.wash.activity.WebActivity;
//import com.cnsunway.wash.adapter.HomeAdapter;
//import com.cnsunway.wash.cnst.Const;
//import com.cnsunway.wash.dialog.LoadingDialog;
//import com.cnsunway.wash.dialog.MarketingDialog;
//import com.cnsunway.wash.dialog.OperationToast;
//import com.cnsunway.wash.dialog.ShareGiftDialog;
//import com.cnsunway.wash.dialog.WayOfShareDialog;
//import com.cnsunway.wash.framework.inter.LoadingDialogInterface;
//import com.cnsunway.wash.framework.net.BaseResp;
//import com.cnsunway.wash.framework.net.JsonVolley;
//import com.cnsunway.wash.framework.utils.JsonParser;
//import com.cnsunway.wash.helper.ApkUpgradeHelper2;
//import com.cnsunway.wash.model.LocationForService;
//import com.cnsunway.wash.model.Marketing;
//import com.cnsunway.wash.model.Order;
//import com.cnsunway.wash.resp.GetImagesResp;
//import com.cnsunway.wash.resp.OrderDetailResp;
//import com.cnsunway.wash.sharef.UserInfosPref;
//import com.cnsunway.wash.util.ShareUtil;
//import com.cnsunway.wash.view.XListView;
//import com.cnsunway.wash.viewmodel.HomeViewModel;
//import com.cnsunway.wash.viewmodel.ViewModel;
//
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.List;
//
///**
// * Created by LL on 2016/3/19.
// */
//public class HomeFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener,XListView.IXListViewListener ,LoadingDialogInterface{
//
//    protected HomeViewModel viewModel;
//    public static final int OPERATION_ORDER_PAY = 2;
//    MenuActivity.OpenLeftMenuLinstenr openLeftMenuLinstenr;
//    ImageButton menuImage;
//    TextView doOrderText, priceListText;
//    View shaddow;
//    View layoutNoOrder;
//    View layoutHasOrder;
//    XListView listView;
//    HomeAdapter homeAdapter;
//    ImageButton markingImage;
//    JsonVolley getImagesVolley;
//    ApkUpgradeHelper2 updateHelper;
//    boolean showGift = false;
//    JsonVolley orderDetailVolley;
//    LocationForService locationForService;
//
//    public void setOpenLeftMenuLinstenr(MenuActivity.OpenLeftMenuLinstenr openLeftMenuLinstenr) {
//        this.openLeftMenuLinstenr = openLeftMenuLinstenr;
//    }
//
//    MarketingDialog marketingDialog;
//
//    @Override
//    protected void handlerMessage(Message msg) {
//        switch (msg.what){
//            case Const.Message.MSG_GET_IMAGES_SUCC:
//                if(msg.arg1 == Const.Request.REQUEST_SUCC){
//                    UserInfosPref.getInstance(getActivity()).hasShowMarkeing(true);
//                    GetImagesResp resp = (GetImagesResp) JsonParser.jsonToObject(msg.obj + "", GetImagesResp.class);
//                    List<Marketing> marketings = resp.getData();
//                    if(marketings != null && marketings.size() > 0){
//                        marketingDialog.setMarketings(marketings);
//                        marketingDialog.show();
//
//
//                    }else{
//                        OperationToast.showOperationResult(getActivity(), R.string.no_marketing);
//                    }
//                }else{
//                    OperationToast.showOperationResult(getActivity(),R.string.get_marketing_fail);
//                }
//
//                break;
//
//            case Const.Message.MSG_GET_IMAGES_FAIL:
//
//                OperationToast.showOperationResult(getActivity(),R.string.get_marketing_fail);
//                break;
//
//            case Const.Message.MSG_ORDER_DETAIL_SUCC:
//                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    OrderDetailResp detailResp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj + "", OrderDetailResp.class);
//                    final Order order = detailResp.getData();
//                    String action = order.getAction();
//                    if(action.equals("share") || action.equals("direct")){
//                        ShareGiftDialog shareGiftDialog  =  new ShareGiftDialog(getActivity(),order).builder();
//                        shareGiftDialog.setShareBtnClickedLinstener(new ShareGiftDialog.OnShareBtnClickedLinstener() {
//                            @Override
//                            public void shareBtnClicked() {
//
//                                WayOfShareDialog wayOfShareDialog = new WayOfShareDialog(getActivity()).builder();
//                                new ShareUtil(wayOfShareDialog).share(getActivity(),order);
//                            }
//                        });
//                        shareGiftDialog.show();
//                    }
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void initFragmentDatas() {
//
//
//        marketingDialog = new MarketingDialog(getActivity()).builder();
//        updateHelper = new ApkUpgradeHelper2(getActivity());
//        updateHelper.check(false);
//
//
//    }
//
//    @Override
//    protected void initMyViews(View view) {
//        menuImage = (ImageButton) view.findViewById(R.id.image_title_menu);
//        menuImage.setOnClickListener(this);
//        doOrderText = (TextView) view.findViewById(R.id.text_do_order);
//        doOrderText.setOnClickListener(this);
//        priceListText = (TextView) view.findViewById(R.id.text_price_list);
//        priceListText.setOnClickListener(this);
////        shaddow = view.findViewById(R.id.shaddow);
//        markingImage = (ImageButton) view.findViewById(R.id.image_title_marketing);
//        markingImage.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }
//
//    @Override
//     public void onResume() {
//        super.onResume();
//
//    }
//
//    public void load(){
//        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//        locationForService = userInfos.getLocationServer();
//        viewModel = new HomeViewModel();
//        if(userInfos.getUser() != null){
//            getImagesVolley = new JsonVolley(getActivity(), Const.Message.MSG_GET_IMAGES_SUCC,Const.Message.MSG_GET_IMAGES_FAIL);
//            viewModel = new HomeViewModel();
//            viewModel.addPropertyChangeListener(ViewModel.PROPERTY_NETREQUEST_STATUS, volleyStatusListener);
//            viewModel.addPropertyChangeListener(HomeViewModel.PROPERTY_RESPONSE, serverErrorListener);
//            viewModel.addPropertyChangeListener(HomeViewModel.PROPERTY_HOMELISTS, homeListListener);
//            //showLoading();
//            viewModel.requestHomeData(getActivity());
//            if(!UserInfosPref.getInstance(getActivity()).isShowMarketing()){
//                getImagesVolley.requestGet(Const.Request.showImages,getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken()
//                        ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
//                );
//            }
//            homeListListener.propertyChange(null);
//        }else {
//            viewModel.setHomeLists(null);
//        }
//
//
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (getView() == null) {
//            setView(inflater.inflate(R.layout.fragment_home, container, false));
//            layoutNoOrder = getView().findViewById(R.id.ll_noorder);
//            layoutHasOrder = getView().findViewById(R.id.ll_hasorder);
//            layoutNoOrder.setOnClickListener(refreshClick);
//            listView = (XListView) getView().findViewById(R.id.listview);
//            listView.setXListViewListener(this);
//            listView.setPullLoadEnable(true);
//            listView.setPullRefreshEnable(true);
//            listView.setOnScrollListener(scrollListener);
//        }
//        //刷新界面
//        load();
//        return getView();
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getActivity().registerReceiver(refreshHomeOrdersReceiver, new IntentFilter(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
//        orderDetailVolley = new JsonVolley(getActivity(), Const.Message.MSG_ORDER_DETAIL_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
//        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getActivity().unregisterReceiver(refreshHomeOrdersReceiver);
//    }
//    BroadcastReceiver refreshHomeOrdersReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            onRefresh();
//        };
//    };
//
//    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            if(scrollState == SCROLL_STATE_IDLE) {
//                if (listView.getmFooterView().isShown()){
//                    shaddow.setVisibility(View.GONE);
//                }else{
//                    shaddow.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            //totalItemCount包含header和footer
//            int lastItem = totalItemCount-1;
//        }
//    };
//    @Override
//    public void onClick(View v) {
//        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//        if (v == menuImage) {
//            if (openLeftMenuLinstenr != null) {
//                openLeftMenuLinstenr.openLeftMenu();
//            }
//        } else if (v == doOrderText) {
//            if(userInfos.getUser() == null){
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }else {
//                startActivity(new Intent(getActivity(), DoOrderActivity.class));
//            }
//
//        } else if (v == priceListText) {
//            Intent intent = new Intent(getActivity(),WebActivity.class);
//            intent.putExtra("url",Const.Request.priceList);
//            intent.putExtra("title","价目表");
//            startActivity(intent);
////            startActivity(new Intent(getActivity(), PriceListActivity.class));
//        }else if(v == markingImage){
//
//            if(userInfos.getUser() == null){
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }else {
//                getImagesVolley.requestGet(Const.Request.showImages, this, getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken()
//                        ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
//                );
//            }
//
////            new ShareGiftDialog(getActivity()).builder().show();
//
//        }
//    }
//
//    private PropertyChangeListener volleyStatusListener = new PropertyChangeListener() {
//        @Override
//        public void propertyChange(PropertyChangeEvent event) {
//            switch (viewModel.getRequestStatus()) {
//                case ViewModel.STATUS_NET_ERROR:
//                case ViewModel.STATUS_SERVER_ERROR:
//                    break;
//                case ViewModel.STATUS_REQUEST_SUCC:
//                    break;
//                case ViewModel.STATUS_REQUEST_FAIL:
//                    layoutNoOrder.setVisibility(View.VISIBLE);
//                    layoutHasOrder.setVisibility(View.GONE);
//                    break;
//            }
//
//            hideLoading();
//            listView.stopLoadMore();
//            listView.stopRefresh();
//        }
//    };
//    private PropertyChangeListener homeListListener = new PropertyChangeListener() {
//        @Override
//        public void propertyChange(PropertyChangeEvent event) {
//            if (viewModel.getHomeLists() == null || viewModel.getHomeLists().size() <= 0) {
//                layoutNoOrder.setVisibility(View.VISIBLE);
//                layoutHasOrder.setVisibility(View.INVISIBLE);
//                return;
//            }
//            if(homeAdapter == null) {
//                homeAdapter = new HomeAdapter(HomeFragment.this, viewModel.getHomeLists());
//                listView.setAdapter(homeAdapter);
//            }else{
//                homeAdapter.setOrderList(viewModel.getHomeLists());
//            }
//            listView.setOnItemClickListener(HomeFragment.this);
//            layoutNoOrder.setVisibility(View.INVISIBLE);
//            layoutHasOrder.setVisibility(View.VISIBLE);
//
//            int count = viewModel.getHomeLists().size();
//            if(count >= viewModel.getTotal()){
//                listView.setPullLoadEnable(false);
//            }else{
//                listView.setPullLoadEnable(true);
//            }
//        }
//    };
//
//    private PropertyChangeListener serverErrorListener = new PropertyChangeListener() {
//        @Override
//        public void propertyChange(PropertyChangeEvent event) {
//            BaseResp resp = viewModel.getResp();
//            if (resp.getResponseCode() != 0) {
//                String msg = resp.getResponseMsg();
//                //显示错误
//            }
//        }
//    };
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//       Order o = (Order) adapterView.getAdapter().getItem(i);
//        if(o.getType() == 1){
//            Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
//            intent.putExtra("order_no", o.getOrderNo());
//            startActivityForResult(intent, 1);
//        }else if(o.getType() == 2){
//            Intent intent = new Intent(getActivity(), BackWashOrderDetailActivity.class);
//            intent.putExtra("order_no", o.getOrderNo());
//            startActivityForResult(intent, 1);
//        }
//    }
//
//    private View.OnClickListener refreshClick = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View v) {
//            onRefresh();
//        }
//    };
//    @Override
//    public void onRefresh() {
//        Log.e("HomeFragment", "refresh");
//        viewModel.onRefresh();
//    }
//
//    @Override
//    public void onLoadMore() {
//        viewModel.onLoadMore();
//    }
//
//    @Override
//    public void showLoading() {
//        showLoadingDialog(getString(R.string.operating));
//    }
//
//    @Override
//    public void hideLoading() {
//        hideLoadingDialog();
//    }
//
//    LoadingDialog loadingDialog;
//
//    protected void showLoadingDialog(String message) {
//        if (loadingDialog != null) {
//            loadingDialog.cancel();
//        }
//        loadingDialog = new LoadingDialog(getActivity(), message);
//        loadingDialog.setCancelable(false);
//        loadingDialog.show();
//    }
//
//    protected void hideLoadingDialog() {
//        if (loadingDialog != null) {
//            loadingDialog.cancel();
//            loadingDialog = null;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == OPERATION_ORDER_PAY && resultCode == Activity.RESULT_OK) {
//            if(data == null){
//                return;
//            }
//            final Order order = (Order)data.getSerializableExtra("order");
//            if(order == null){
//                return;
//            }
//            if(order.getIndex() == -1){
//                return;
//            }
//            if(TextUtils.isEmpty(order.getOrderNo())){
//                return;
//            }
//            orderDetailVolley.requestGet(Const.Request.detail + "/" + order.getOrderNo(), getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken()
//                    ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
//            );
////            getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
//
//        }
//    }
//}
