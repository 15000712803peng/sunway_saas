//package com.cnsunway.wash.fragment;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.cnsunway.wash.R;
//import com.cnsunway.wash.activity.AddrActivity;
//import com.cnsunway.wash.activity.WebActivity;
//import com.cnsunway.wash.activity.CouponActivity;
//import com.cnsunway.wash.activity.LoginActivity;
//import com.cnsunway.wash.activity.ServerSwitchActivity;
//import com.cnsunway.wash.cnst.Const;
//import com.cnsunway.wash.dialog.CallHotlineDialog;
//import com.cnsunway.wash.dialog.LogoutDialog;
//import com.cnsunway.wash.framework.net.StringVolley;
//import com.cnsunway.wash.framework.utils.JsonParser;
//import com.cnsunway.wash.framework.utils.NumberUtil;
//import com.cnsunway.wash.helper.ApkUpgradeHelper;
//import com.cnsunway.wash.resp.AccountResp;
//import com.cnsunway.wash.sharef.UserInfosPref;
//import com.cnsunway.wash.util.FontUtil;
//import com.cnsunway.wash.util.LoginManager;
//import com.cnsunway.wash.view.BadgeView;
//import com.cnsunway.wash.view.CropImage.CropHelper;
//import com.makeramen.roundedimageview.RoundedImageView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
//
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class LeftMenuFragment extends BaseFragment implements OnClickListener, SlidingMenu.OnOpenedListener, LogoutDialog.OnLogoutOkClickedLinstener {
//    public static final int HEAD_FROM_SERVERSWITCH = 2105;
//    int couponNum;
//    String balanceNum;
//    UserInfosPref userInfos;
//    StringVolley headPortraitVolley;
//    StringVolley accountVolley;
//    CropHelper cropHelper;
//    String token;
//    protected ImageLoader imageLoader;
//    ApkUpgradeHelper updateHelper;
//    OnLeftMenuItemClickedListener menuItemClickedListener;
//    LogoutDialog logoutDialog;
//    CallHotlineDialog callHotlineDialog;
//
//    @Bind(R.id.img_head_portrait)
//    RoundedImageView imgHeadPortrait;
//    @Bind(R.id.tv_user_phone)
//    TextView tvUserPhone;
//    @Bind(R.id.ll_user_portrait)
//    LinearLayout llUserPortrait;
//    @Bind(R.id.tv_user_balance)
//    TextView tvUserBalance;
//    @Bind(R.id.ll_banlance)
//    LinearLayout llBanlance;
//    @Bind(R.id.tv_addr_manage)
//    TextView tvAddrManage;
//    @Bind(R.id.tv_common_problem)
//    TextView tvCommonProblem;
//    @Bind(R.id.tv_user_agreement)
//    TextView tvUserAgreement;
//    @Bind(R.id.tv_user_coupon_num)
//    BadgeView tvUserCouponNum;
//    @Bind(R.id.ll_user_coupon)
//    LinearLayout llUserCoupon;
//    @Bind(R.id.tv_call_hotline)
//    TextView tvCallHotline;
//    @Bind(R.id.ll_logout)
//    ViewGroup llLogout;
//    @Bind(R.id.ll_about_us)
//    LinearLayout llAboutUs;
//    @Bind(R.id.tv_about_us)
//    TextView tvAboutUs;
//    @Bind(R.id.tv_version_update)
//    TextView tvVersionUpdate;
//    @Bind(R.id.tv_serverurl)
//    TextView tvServerUrl;
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//
//    @OnClick({R.id.ll_user_portrait, R.id.ll_banlance, R.id.tv_addr_manage, R.id.tv_common_problem, R.id.tv_user_agreement, R.id.ll_logout, R.id.ll_user_coupon, R.id.tv_about_us, R.id.tv_version_update, R.id.tv_call_hotline})
//    public void onClick(View view) {
//        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//        switch (view.getId()) {
//            case R.id.ll_user_portrait:
//                if (LoginManager.get(getActivity()).isLogined()) {
//                    cropHelper.showChooseDialog();
//                } else {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//
//                break;
//            case R.id.ll_banlance:
//                if (userInfos.getUser() != null) {
////                    if(isMarketingStart()){
////
////                    }else {
////                        startActivity(new Intent(getActivity(), BalanceActivity.class));
////                    }
//
//                    startActivity(new Intent(getActivity(), WebActivity.class));
//
//                }else {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.tv_addr_manage:
//                if (userInfos.getUser() != null) {
//                    startActivity(new Intent(getActivity(), AddrActivity.class));
//                }else {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.tv_common_problem: {
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", Const.Request.question);
//                intent.putExtra("title", "常见问题");
//                startActivity(intent);
//            }
//            break;
//            case R.id.tv_user_agreement: {
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", Const.Request.agreement);
//                intent.putExtra("title", "用户协议");
//                startActivity(intent);
//            }
//            break;
//            case R.id.ll_user_coupon:
//                if (userInfos.getUser() != null) {
//                    startActivity(new Intent(getActivity(), CouponActivity.class));
//                }else {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.ll_logout:
//                logoutDialog = new LogoutDialog(getActivity()).builder();
//                logoutDialog.setOkLinstener(this);
//                logoutDialog.show();
//                break;
//            case R.id.tv_about_us: {
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", Const.Request.about);
//                intent.putExtra("title", "关于我们");
//                startActivity(intent);
////                startActivity(new Intent(getActivity(), AboutUsActivity.class));
//            }
//            break;
//            case R.id.tv_version_update:
//                updateHelper = new ApkUpgradeHelper(getActivity());
//                updateHelper.check(true);
//                break;
//            case R.id.tv_call_hotline:
//                callHotlineDialog = new CallHotlineDialog(getActivity()).builder();
//                callHotlineDialog.show();
//                break;
//        }
//    }
//
//    @Override
//    public void logoutOkClicked() {
//        if (LoginManager.get(getActivity()).isLogined()) {
//            userInfos.saveUser(null);
//            onLogout();
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            getActivity().startActivity(intent);
//            getActivity().finish();
//        }
//    }
//
//
//    @Override
//    public void onOpened() {
//        if (userInfos.getUser() != null) {
//            onLogin();
//        }
//    }
//
//
//    public interface OnLeftMenuItemClickedListener {
//
//        void leftItemClicked(int position);
//    }
//
//    private boolean isMarketingStart(){
//        Calendar startCalendar = new GregorianCalendar();
//        startCalendar.set(GregorianCalendar.MONTH,Calendar.MAY);
//        startCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
//        startCalendar.set(GregorianCalendar.MINUTE,0);
//        return System.currentTimeMillis() >= startCalendar.getTimeInMillis() ;
//    }
//
//    public void setMenuItemClickedListener(
//            OnLeftMenuItemClickedListener menuItemClickedListener) {
//        this.menuItemClickedListener = menuItemClickedListener;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        cropHelper = new CropHelper(this, headImageListener);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (getView() == null) {
//            setView(inflater.inflate(R.layout.menu_left, container, false));
//            FontUtil.applyFont(getActivity(), getView(), "OpenSans-Regular.ttf");
//            ButterKnife.bind(this, getView());
//        }
//        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//
//    @Override
//    protected void handlerMessage(Message msg) {
//        switch (msg.what) {
//            case Const.Message.MSG_ACCOUNT_ALL_SUCC:
//
//                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    AccountResp initResp = (AccountResp) JsonParser
//                            .jsonToObject(msg.obj + "", AccountResp.class);
//                    couponNum = initResp.getData().getCouponCount();
//                    balanceNum = initResp.getData().getBalance();
//                    if (userInfos.getUser() != null) {
//                        tvUserCouponNum.setText(couponNum + "");
//                        tvUserBalance.setText(NumberUtil.formatNumber(balanceNum));
//                        tvUserCouponNum.setVisibility(View.VISIBLE);
//                    }
//
//                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
//                    Toast.makeText(getActivity(), "获取账户信息失败", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//
//            case Const.Message.MSG_ACCOUNT_ALL_FAIL:
//                Toast.makeText(getActivity(), "网络服务异常", Toast.LENGTH_SHORT).show();
//                break;
//            case Const.Message.MSG_PROFILE_SUCC:
////                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
////                    UpdateUserResp respSucc = (UpdateUserResp) JsonParser.jsonToObject(msg.obj + "", UpdateUserResp.class);
////                    UpdateUser updateUser = respSucc.getData();
////                    if (updateUser != null && updateUser.headPortraitUrl != null) {
////                        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
////                        if (userInfos != null && userInfos.getUser() != null) {
////                            User user = userInfos.getUser();
////                            user.setHeadImgUrl(updateUser.headPortraitUrl);
////                            userInfos.saveUser(user);
////                        }
////                        imageLoader.loadImage(updateUser.headPortraitUrl, headImageListener);
////                    }
////                }
//                break;
//            case Const.Message.MSG_PROFILE_FAIL:
//                break;
//            default:
//                break;
//        }
//    }
//
//    public static final String FILTER_REFRESH_MY_ACCOUNT = "refresh_my_account";
//    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//            if (userInfos.getUser() != null) {
//                onLogin();
//            }
//        }
//    };
//
//    @Override
//    protected void initFragmentDatas() {
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//        userInfos = UserInfosPref.getInstance(getActivity());
//        getActivity().registerReceiver(refreshReceiver, new IntentFilter(FILTER_REFRESH_MY_ACCOUNT));
//
//    }
//
//    private void onLogin() {
//        tvUserPhone.setText(userInfos.getUserName());
//        token = userInfos.getUser().getToken();
//        accountVolley = new StringVolley(getActivity(), Const.Message.MSG_ACCOUNT_ALL_SUCC, Const.Message.MSG_ACCOUNT_ALL_FAIL);
//        headPortraitVolley = new StringVolley(getActivity(), Const.Message.MSG_PROFILE_SUCC, Const.Message.MSG_PROFILE_FAIL);
//        llLogout.setVisibility(View.VISIBLE);
//        accountVolley.requestGet(Const.Request.all, getHandler(), userInfos.getUser().getToken());
//        updateHeadPortrait();
//        initDeveloper(userInfos.getUserName());
//    }
//
//    private void onLogout() {
//        tvUserCouponNum.setVisibility(View.INVISIBLE);
//        llLogout.setVisibility(View.INVISIBLE);
//        imgHeadPortrait.setImageResource(R.drawable.left_ic_avatar);
//        tvUserPhone.setText("登录");
//        tvUserBalance.setText("0.00");
//        getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
//    }
//
//    @Override
//    protected void initMyViews(View view) {
//        tvUserCouponNum.setBadgeBackgroundColor(getResources().getColor(R.color.green));
//        if (LoginManager.get(getActivity()).isLogined()) {
//            onLogin();
//        } else {
//            onLogout();
//        }
//    }
//
//    private void updateHeadPortrait() {
//        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//        if (userInfos.getUser() != null && userInfos.getUser().getHeadPortraitUrl() != null) {
//            imageLoader.loadImage(userInfos.getUser().getHeadPortraitUrl(), headImageListener);
//        }
//        if (userInfos.getUser() != null && userInfos.getUser().getToken() != null) {
//            headPortraitVolley.requestGet(Const.Request.profile, getHandler(), token);
//        }
//    }
//
//
//    private ImageLoadingListener headImageListener = new ImageLoadingListener() {
//        @Override
//        public void onLoadingStarted(String s, View view) {
//
//        }
//
//        @Override
//        public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//        }
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            // Do whatever you want with Bitmap
//            try {
//                if (loadedImage != null) {
//                    imgHeadPortrait.setImageBitmap(loadedImage);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//
//        @Override
//        public void onLoadingCancelled(String s, View view) {
//
//        }
//    };
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        getActivity().unregisterReceiver(refreshReceiver);
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//        if (requestCode == HEAD_FROM_SERVERSWITCH) {
//            if (userInfos == null || userInfos.getUser() == null) {
//                return;
//            }
//            String userMobile = userInfos.getUserName();
//            initDeveloper(userMobile);
//            Context context = getActivity().getApplicationContext();
//            userInfos.saveUser(null);
//            onLogout();
//            getActivity().finish();
//            Intent intent = new Intent(context, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            return;
//        }
//        cropHelper.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void initDeveloper(String mobile) {
//        if (tvServerUrl == null) {
//            return;
//        }
//        tvServerUrl.setVisibility(View.GONE);
//        if (mobile == null) {
//            return;
//        }
//        String phones = "13817048334,18917067316,15138990663,18260257769,15021874195," +
//                "18917067316,18917061010,18917061012,15021874195,18917067316," +
//                "18917067316,18917067320,18917067325,15021874195,18260257769,13889777895," +
//                "18221309969";
//
//        if (!phones.contains(mobile)) {
//            return;
//        }
//
//        tvServerUrl.setVisibility(View.VISIBLE);
//        tvServerUrl.setText(Const.Request.SERVER);
//        tvServerUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivityForResult(new Intent(getActivity(), ServerSwitchActivity.class), HEAD_FROM_SERVERSWITCH);
//            }
//        });
//    }
//
//}
