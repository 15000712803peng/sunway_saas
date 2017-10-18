package com.cnsunway.saas.wash.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.AddrActivity;
import com.cnsunway.saas.wash.activity.HomeActivity2;
import com.cnsunway.saas.wash.activity.LoginActivity;
import com.cnsunway.saas.wash.activity.NapaCardActivity;
import com.cnsunway.saas.wash.activity.ServerSwitchActivity;
import com.cnsunway.saas.wash.activity.WebActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.CallHotlineDialog;
import com.cnsunway.saas.wash.dialog.LogoutDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.framework.utils.NumberUtil;
import com.cnsunway.saas.wash.framework.utils.VersionUtil;
import com.cnsunway.saas.wash.helper.ApkUpgradeHelper;
import com.cnsunway.saas.wash.helper.HxHelper;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.StoreBalance;
import com.cnsunway.saas.wash.model.User;
import com.cnsunway.saas.wash.resp.AccountResp;
import com.cnsunway.saas.wash.resp.StoreCardResp;
import com.cnsunway.saas.wash.resp.UpdateUserResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.ChannelTool;
import com.cnsunway.saas.wash.view.CropImage.CropHelper;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunway.saas.wash.cnst.Const.Request.balance;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class MineFragment extends BaseFragment implements LogoutDialog.OnLogoutOkClickedLinstener {

    public static final int HEAD_FROM_SERVERSWITCH = 2105;
    @Bind(R.id.img_head_portrait)
    RoundedImageView imgHeadPortrait;
    /*@Bind(R.id.text_user_coupon_num)
    TextView tvUserCouponNum;*/
    @Bind(R.id.text_user_balance)
    TextView tvUserBalance;
    ImageLoader imageLoader;
    StringVolley headPortraitVolley;
    CropHelper cropHelper;
    StringVolley accountVolley;
    @Bind(R.id.ll_banlance)
    LinearLayout llBanlance;
    /*@Bind(R.id.ll_user_coupon)
    LinearLayout llUserCoupon;*/
    /*@Bind(R.id.ll_recharge)
    LinearLayout rechargeImage;*/
    /*@Bind(R.id.ll_call_hotline)
    LinearLayout llCallHotline;*/
    @Bind(R.id.ll_logout)
    LinearLayout llLogout;
   /* @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;*/
    @Bind(R.id.ll_addr_manage)
    LinearLayout llAddrManage;

    @Bind(R.id.ll_user_agreement)
    LinearLayout llUserAgreement;
    /*@Bind(R.id.ll_question)
     LinearLayout llQuestion;*/
    /*@Bind(R.id. ll_apply_invoice)
    LinearLayout llApplyInvoice;*/
    @Bind(R.id.text_user)
    TextView userText;
    @Bind(R.id.rl_no_login)
    RelativeLayout noLoginParent;
    @Bind(R.id.ll_info)
    LinearLayout infoParent;
    @Bind(R.id.ll_version)
    LinearLayout versionParent;
    ApkUpgradeHelper updateHelper;
    TextView versionText;
    @Bind(R.id.ll_developer)
    LinearLayout developerParent;
    @Bind(R.id.text_channel)
    TextView channelText;
    /*@Bind(R.id.ll_bind_coupon)
    LinearLayout bindCouponParent;*/
    @Bind(R.id.napa_card_parent)
    LinearLayout napaCardParent;
    JsonVolley cardVolley;
    @Bind(R.id.text_store_banlanc)
    TextView storeBalanceText;
    /*@Bind(R.id.ll_online_custom)
    LinearLayout onlineCustom;*/
    LocationForService locationForService;
    private int messageToIndex = Const.MESSAGE_TO_DEFAULT;
    private int selectedIndex = Const.INTENT_CODE_IMG_SELECTED_DEFAULT;
    String balanceNum ;
    @Override
    protected void handlerMessage(final Message msg) {
        switch (msg.what){
            case Const.Message.MSG_PROFILE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    UpdateUserResp respSucc = (UpdateUserResp) JsonParser.jsonToObject(msg.obj + "", UpdateUserResp.class);
                    User updateUser = respSucc.getData();
                    if(updateUser != null){
                        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
                        User user = userInfos.getUser();
                        user.setHeadPortraitUrl(updateUser.getHeadPortraitUrl());
                        userInfos.saveUser(user);
                        if(!TextUtils.isEmpty(updateUser.getMobile())){
//                            userText.setText(updateUser.getMobile());

                            initDeveloper();
                        }
                       if(!TextUtils.isEmpty(updateUser.getHeadPortraitUrl())){
                           imageLoader.loadImage(updateUser.getHeadPortraitUrl(), headImageListener);
                       }else {
                           imgHeadPortrait.setImageDrawable(getResources().getDrawable(R.mipmap.user_avatar));
                       }
                    }
                }
                break;

            case Const.Message.MSG_ACCOUNT_ALL_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    AccountResp initResp = (AccountResp) JsonParser.jsonToObject(msg.obj + "", AccountResp.class);
                    List<StoreBalance> storeBalances = initResp.getData();

                    if (storeBalances == null || storeBalances.size() == 0) {
                        balanceNum = "0.00";

                    }else {
                        BigDecimal proce = new BigDecimal(0);
                        for (int i = 0;i<storeBalances.size();i++){

                            proce = proce.add(new BigDecimal(storeBalances.get(i).getTotalAmount()));

                        }
                        balanceNum = NumberUtil.formatNumber(proce.floatValue()+"") ;

                    }
                        tvUserBalance.setText(balanceNum + " 元");

                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    Toast.makeText(getActivity(), "获取账户信息失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case Const.Message.MSG_ACCOUNT_ALL_FAIL:
                Toast.makeText(getActivity(), "网络服务异常", Toast.LENGTH_SHORT).show();
                break;

            case Const.Message.MSG_CARD_LIST_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    final  StoreCardResp resp  = (StoreCardResp) JsonParser.jsonToObject(msg.obj+"",StoreCardResp.class);
                    if(resp.getData() == null || resp.getData().size() ==0){
                        //不用显示
                        napaCardParent.setVisibility(View.GONE);
                    }else {
                        storeBalanceText.setText("￥"+ resp.getData().get(0).getSumBalance());
                        napaCardParent.setVisibility(View.VISIBLE);
                        napaCardParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),NapaCardActivity.class);
                                intent.putExtra("resp",JsonParser.objectToJsonStr(resp));
                                startActivity(intent);
                            }
                        });
                    }
                }else {

                }

                break;
            case Const.Message.MSG_CARD_LIST_FAIL:

                break;

        }
    }

    @Override

    protected void initFragmentDatas() {

        getActivity().registerReceiver(refreshReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_TABS));
        getActivity().registerReceiver(refreshMineReceiver,new IntentFilter(Const.MyFilter.FILTER_REFRESH_MINE_TABS));
//        headPortraitVolley = new StringVolley(getActivity(), Const.Message.MSG_PROFILE_SUCC, Const.Message.MSG_PROFILE_FAIL);
        accountVolley = new StringVolley(getActivity(), Const.Message.MSG_ACCOUNT_ALL_SUCC, Const.Message.MSG_ACCOUNT_ALL_FAIL);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        cropHelper = new CropHelper(this, headImageListener);
        accountVolley = new StringVolley(getActivity(), Const.Message.MSG_ACCOUNT_ALL_SUCC, Const.Message.MSG_ACCOUNT_ALL_FAIL);
        cardVolley = new JsonVolley(getActivity(),Const.Message.MSG_CARD_LIST_SUCC,Const.Message.MSG_CARD_LIST_FAIL);
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();


    }

    private void showNoLogin(){
        noLoginParent.setVisibility(View.VISIBLE);
        infoParent.setVisibility(View.INVISIBLE);
    }

    private void showInfo(){
        noLoginParent.setVisibility(View.INVISIBLE);
        infoParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshReceiver);
        getActivity().unregisterReceiver(refreshMineReceiver);
    }

    @Override
    protected void initMyViews(View view) {
        versionText = (TextView) view.findViewById(R.id.text_version);
        channelText = (TextView) view.findViewById(R.id.text_channel);
        versionText.setText("当前版本：" + VersionUtil.getAppVersionName(getActivity()));
        channelText.setText("渠道：" + ChannelTool.getChannel(getActivity().getApplicationContext()));

    }
    private ImageLoadingListener headImageListener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {


        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            // Do whatever you want with Bitmap
            try {
                if (loadedImage != null) {
                    imgHeadPortrait.setImageBitmap(loadedImage);
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };


    CallHotlineDialog callHotlineDialog;
    LogoutDialog logoutDialog;
    @OnClick({R.id.img_head_portrait,/*R.id.ll_user_coupon,*/R.id.ll_banlance,/*R.id.ll_recharge,R.id.ll_call_hotline,*/R.id.ll_addr_manage,R.id.ll_user_agreement,R.id.ll_logout/*,R.id.ll_question, R.id.ll_apply_invoice,R.id.ll_about_us*/,R.id.ll_version,R.id.ll_developer/*,R.id.ll_bind_coupon,R.id.ll_online_custom*/})
    public void onClick(View view) {
        User user = UserInfosPref.getInstance(getActivity()).getUser();
        switch (view.getId()){
            case R.id.img_head_portrait:
                if(user != null){
//                    cropHelper.showChooseDialog();
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            /*case R.id.ll_user_coupon:
                if (user != null) {
                    startActivity(new Intent(getActivity(), CouponActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;*/
            case R.id.ll_banlance:
                if (user != null) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title","余额");
                    intent.putExtra("url", balance);
                    startActivity(intent);
//                    startActivity(new Intent(getActivity(), WebActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            /*case R.id.ll_recharge:
                if (user != null) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title","充值有礼");
                    startActivity(intent);
//                    startActivity(new Intent(getActivity(), WebActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;*/

            /*case R.id.ll_call_hotline:
                callHotlineDialog = new CallHotlineDialog(getActivity()).builder();
                callHotlineDialog.show();
                        *//*new ActionSheetDialog(getActivity())
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true).setTitle("我们将为您提供满意的咨询服务").
                                addSheetItem("在线客服", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
//
                                                if (ChatClient.getInstance().isLoggedInBefore()) {
                                                    HxHelper.getInstance(getActivity()).toChat(Const.MESSAGE_TO_DEFAULT,getActivity());
                                                }else {
//                                                    createRandomAccountThenLoginChatServer();
                                                    HxHelper.getInstance(getActivity()).login(UserInfosPref.getInstance(getActivity()).getUser().getMobile(),UserInfosPref.getInstance(getActivity()).getUser().getHxPwd());
                                                }
                                            }
                                        })
                                .addSheetItem("客服电话：4009-210-682", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4009-210-682"));
                                                startActivity(intent);
                                            }
                                        })
                                .show();*//*

                break;*/

            case R.id.ll_logout:
                logoutDialog = new LogoutDialog(getActivity()).builder();
                logoutDialog.setOkLinstener(this);
                logoutDialog.show();
                break;
            /*case R.id.ll_about_us: {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", Const.Request.about);
                intent.putExtra("title", "关于赛维");
                startActivity(intent);
            }
            break;*/

            case R.id.ll_addr_manage:
                if (user != null) {
                    startActivity(new Intent(getActivity(), AddrActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;

            case R.id.ll_user_agreement: {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", Const.Request.agreement);
                intent.putExtra("title", "用户协议");
                startActivity(intent);
            }
            break;
            /* case R.id.ll_question: {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", Const.Request.question);
                intent.putExtra("title", "常见问题");
                startActivity(intent);
            }
            break;
           case R.id.ll_apply_invoice: {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", Const.Request.invoices);
                intent.putExtra("title", "发票申请");
                startActivity(intent);
            }
            break; */

            case  R.id.ll_version:
                updateHelper = new ApkUpgradeHelper(getActivity());
                updateHelper.check(true);
                break;

            case R.id.ll_developer:
                startActivityForResult(new Intent(getActivity(), ServerSwitchActivity.class), HEAD_FROM_SERVERSWITCH);
                break;
            /*case R.id.ll_bind_coupon:
                startActivity(new Intent(getActivity(),BindCouponActivity.class));
                break;*/
           /* case R.id.ll_online_custom:




                break;*/
        }
    }

    private ProgressDialog progressDialog;

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
        }
        return progressDialog;
    }

    boolean progressShow = false;
    private void login(final String uname, final String upwd) {
        progressShow = true;
        progressDialog = getProgressDialog();
        progressDialog.setMessage("连接中。。。");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        // login huanxin server
//        Log.e("-------","user name:"+ uname);
//        Log.e("-------","upwd:"+ upwd);
        ChatClient.getInstance().login(uname, upwd, new Callback() {
            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"login succ",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
                toChatActivity();
            }
            @Override
            public void onError(int code, String error) {
                if (!progressShow) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

   /* private void toChatActivity() {
       getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!getActivity().isFinishing())

                //此处演示设置技能组,如果后台设置的技能组名称为[shouqian|shouhou],这样指定即分配到技能组中.
                //为null则不按照技能组分配,同理可以设置直接指定客服scheduleAgent

                switch (messageToIndex){
                    case Const.MESSAGE_TO_AFTER_SALES:
                        queueName = "shouhou";
                        break;
                    case Const.MESSAGE_TO_PRE_SALES:
                        queueName = "shouqian";
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Const.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex);
                //设置点击通知栏跳转事件
//                Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
//                String titleName = null;
//                if (conversation.getOfficialAccount() != null){
//                    titleName = conversation.getOfficialAccount().getName();
//                }
//                // 进入主页面
//                Intent intent = new IntentBuilder(getActivity())
//                        .setTargetClass(ChatActivity.class)
//                        .setVisitorInfo(MessageHelper.createVisitorInfo())
//                        .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
//                        .setScheduleQueue(MessageHelper.createQueueIdentity(queueName))
//                        .setTitleName(titleName)
////                .setScheduleAgent(MessageHelper.createAgentIdentity("ceshiok1@qq.com"))
//                        .setShowUserNick(true)
//                        .setBundle(bundle)
//                        .build();
//                startActivity(intent);
//                finish();

            }
        });
    }*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
            setView(inflater.inflate(R.layout.fragment_mine2,container,false));
            initMyViews(getView());
        }
        ButterKnife.bind(this, getView());
        load();



        return getView();
    }

    private void load(){
        User user = UserInfosPref.getInstance(getActivity()).getUser();
        locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        if(user == null){
            showNoLogin();
            return;
        }
         showInfo();
        if (user != null && !TextUtils.isEmpty(user.getToken())) {
            userText.setText(UserInfosPref.getInstance(getActivity()).getUserName());
            if(!TextUtils.isEmpty(user.getHeadPortraitUrl())){
                imageLoader.loadImage(user.getHeadPortraitUrl(), headImageListener);
            }
//            headPortraitVolley.requestGet(Const.Request.profile, getHandler(), user.getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

            accountVolley.requestGet(Const.Request.all, getHandler(), user.getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
            cardVolley.addParams("userMobile",UserInfosPref.getInstance(getActivity()).getUser().getMobile());
//            cardVolley.requestPost(Const.Request.cardList,getHandler(), UserInfosPref.getInstance(getActivity()).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

        }else {
            imgHeadPortrait.setImageDrawable(getResources().getDrawable(R.mipmap.user_avatar));
            llLogout.setVisibility(View.GONE);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());

        if (requestCode == HEAD_FROM_SERVERSWITCH) {
            developerParent.setVisibility(View.GONE);
            if (userInfos == null || userInfos.getUser() == null) {
                return;
            }

            userInfos.saveUser(null);
            HomeActivity2 activity = (HomeActivity2) getActivity();
            activity.setHomeTabHost();
            return;
        }
        cropHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logoutOkClicked() {
        if(ChatClient.getInstance() != null && ChatClient.getInstance().isLoggedInBefore()){
            ChatClient.getInstance().logout(true, new Callback(){
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

        UserInfosPref.getInstance(getActivity()).saveUser(null);
        UserInfosPref.getInstance(getActivity()).saveUserName("");
        MobclickAgent.onProfileSignOff();
        logoutDialog.cancel();
        HomeActivity2 activity = (HomeActivity2) getActivity();
        activity.setHomeTabHost();
        getActivity().sendBroadcast(new Intent(Const.MyFilter.FILTER_LOG_OUT));
//        startActivity(new Intent(getActivity(),LoginActivity.class));
    }

    private void initDeveloper(){
        if(UserInfosPref.getInstance(getActivity()).getUser() == null){
            developerParent.setVisibility(View.GONE);
        }else {
            String phones = "15000712803,13817048334,18917067316,15138990663,18260257769,15021874195," +
                    "18917061010,18917061012,15021874195,18917067316," +
                    "18917067320,18917067325,18260257769,13889777895," +
                    "18221309969,13661449137,13661449137";
          String mobile = UserInfosPref.getInstance(getActivity()).getUser().getMobile();
            if(!TextUtils.isEmpty(mobile)){
                if (phones.contains(mobile)){
                    developerParent.setVisibility(View.VISIBLE);
                }else {
                    developerParent.setVisibility(View.GONE);
                }
            }

        }
    }

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            load();
        }
    };

    BroadcastReceiver refreshMineReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            load();
        }
    };

    String queueName = null;
    private void toChatActivity() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!getActivity().isFinishing()){
                    HxHelper.getInstance(getActivity()).toChat(messageToIndex,getActivity());
                }

                //此处演示设置技能组,如果后台设置的技能组名称为[shouqian|shouhou],这样指定即分配到技能组中.
                //为null则不按照技能组分配,同理可以设置直接指定客服scheduleAgent

//                switch (messageToIndex){
//                    case Constant.MESSAGE_TO_AFTER_SALES:
//                        queueName = "shouhou";
//                        break;
//                    case Constant.MESSAGE_TO_PRE_SALES:
//                        queueName = "shouqian";
//                        break;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex);
//                //设置点击通知栏跳转事件
//                Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
//                String titleName = null;
//                if (conversation.getOfficialAccount() != null){
//                    titleName = conversation.getOfficialAccount().getName();
//                }
//                // 进入主页面
//                Intent intent = new IntentBuilder(getActivity())
//                        .setTargetClass(ChatActivity.class)
//                        .setVisitorInfo(MessageHelper.createVisitorInfo())
//                        .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
//                        .setScheduleQueue(MessageHelper.createQueueIdentity(queueName))
//                        .setTitleName(titleName)
////						.setScheduleAgent(MessageHelper.createAgentIdentity("ceshiok1@qq.com"))
//                        .setShowUserNick(true)
//                        .setBundle(bundle)
//                        .build();
//                intent.putExtra("yanpeng","yanpeng");
//                startActivity(intent);

            }
        });
    }


}
