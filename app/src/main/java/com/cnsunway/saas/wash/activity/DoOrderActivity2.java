package com.cnsunway.saas.wash.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.DoOrderFailDialog;
import com.cnsunway.saas.wash.dialog.DoOrderSuccessDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.TimeSelectedDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Addr;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.ServiceCity;
import com.cnsunway.saas.wash.model.ShippingFee;
import com.cnsunway.saas.wash.resp.DefaultAddrResp;
import com.cnsunway.saas.wash.resp.GetFreightRuleResp;
import com.cnsunway.saas.wash.resp.OrderDetailResp;
import com.cnsunway.saas.wash.resp.ShippingFeeResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by LL on 2016/3/19.
 */
public class DoOrderActivity2 extends InitActivity implements View.OnClickListener, TimeSelectedDialog.SelectedTimeOkListener, DoOrderSuccessDialog.OnDoOrderSuccCancelClickedLinstener {

    TextView titleText, addrText, tipsText,addrNameText,addrGenderText,addrPhoneText;
    LinearLayout commitOrderParent;
    LinearLayout selectAddrParent;
    LinearLayout washShoesParent, unwashShoesParent;
    ImageView washShoesImage, unwashShoesImage;
    private static final int OPERATION_SELECT_ADDR = 1;
    private static final int OPERATION_MAP = 2;
    Addr addr;
    StringVolley showDefaultAddrVolley;
//    TextView orderTimeText;
    TimeSelectedDialog timeDialog;
    String uploadTimeBegin = "";
    String uploadTimeEnd = "";

    String[] hours;
    String hour = "";
    String date = "";
    String[] dates = new String[3];

    JsonVolley createOrderVolley;
    int needWashShoes = 0;
    StringVolley markDefaultVolley;
    RelativeLayout layoutDoOrder;
    TextView washShoesText, unwashShoesText;
    JsonVolley getFreightRuleVolley;
//    TextView noAddrText;
    String addrId;
    JsonVolley addrDetailVolley;
    LinearLayout hasAddrParent;
    LinearLayout noAddrParent;
    ServiceCity defaultCity;
//    ImageView conntactImage;


    private String getDefaultOrderTime() {
        String time = "";

        generateHours();

        time = date + "&" + hour;
        return time;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void generateHours() {
        int calHour = DateUtil.getCurrentHourAfterHalf();
        hours = getResources().getStringArray(R.array.hoursnew);
        if (calHour == 9) {
            hours = Arrays.copyOfRange(hours, 1, hours.length);
        } else if (calHour == 10) {
            hours = Arrays.copyOfRange(hours, 2, hours.length);
        } else if (calHour == 11) {
            hours = Arrays.copyOfRange(hours, 3, hours.length);
        } else if (calHour == 12) {
            hours = Arrays.copyOfRange(hours, 4, hours.length);
        } else if (calHour == 13) {
            hours = Arrays.copyOfRange(hours, 5, hours.length);
        } else if (calHour == 14) {
            hours = Arrays.copyOfRange(hours, 6, hours.length);
        } else if (calHour == 15) {
            hours = Arrays.copyOfRange(hours, 7, hours.length);
        } else if (calHour == 16) {
            hours = Arrays.copyOfRange(hours, 8, hours.length);
        } else if (calHour == 17) {
            hours = Arrays.copyOfRange(hours, 9, hours.length);
        } else if (calHour == 18) {
            hours = Arrays.copyOfRange(hours, 10, hours.length);
        } else if (calHour >= 19 || calHour == 0) {
            hours = new String[]{""};
        }

        hour = hours[0];

        if (TextUtils.isEmpty(hour)) {
            date = dates[1];
            hours = getResources().getStringArray(R.array.hoursnew);
            hour = hours[0];
        }

    }
//    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    private void generateHoursNew() {
//        int calHour = DateUtil.getCurrentHour();
//        hours = getResources().getStringArray(R.array.hoursnew);
//        if (calHour == 9 || calHour == 10) {
//            hours = Arrays.copyOfRange(hours, 1, hours.length);
//        } else if (calHour == 11 || calHour == 12) {
//            hours = Arrays.copyOfRange(hours, 2, hours.length);
//        } else if (calHour == 13 || calHour == 14) {
//            hours = Arrays.copyOfRange(hours, 3, hours.length);
//        } else if (calHour == 15 || calHour == 16) {
//            hours = Arrays.copyOfRange(hours, 4, hours.length);
//        }else if (calHour == 17 || calHour == 18) {
//            hours = Arrays.copyOfRange(hours, 5, hours.length);
//        }else if (calHour >= 19 || calHour == 0) {
//            hours = new String[1];
//            hours[0] = "";
//        }
//        hour = hours[0];
//        if (TextUtils.isEmpty(hour)) {
//            date = dates[1];
//            hours = getResources().getStringArray(R.array.hoursnew);
//            hour = hours[0];
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_do_order3);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutDoOrder, "OpenSans-Regular.ttf");
        defaultCity = new ServiceCity();
    }

    LocationForService locationForService;

    @Override
    protected void initData() {
        locationForService  = UserInfosPref.getInstance(this).getLocationServer();
        showDefaultAddrVolley = new StringVolley(this, Const.Message.MSG_SHOW_DEFAULT_ADDR_SUCC, Const.Message.MSG_SHOW_DEFAULT_ADDR_FAIL);
        markDefaultVolley = new StringVolley(this, Const.Message.MSG_MARK_DEFAULT_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);
        timeDialog = new TimeSelectedDialog(this).builder();
        timeDialog.setOkListener(this);
        dates[0] = DateUtil.getTodayDate();
        dates[1] = DateUtil.getTomorrowDate();
        dates[2] = DateUtil.getOneAfterTomorrowDate();
        hours = getResources().getStringArray(R.array.hoursnew);
        date = dates[0];
        createOrderVolley = new JsonVolley(this, Const.Message.MSG_CREATE_ORDER_SUCC, Const.Message.MSG_CREATE_ORDER_FAIL);
        createOrderVolley.addParams("type", 1);
//        JsonVolley shippingFeeVolley = new JsonVolley(this, Const.Message.MSG_SHIPPINGFEE_SUCC, Const.Message.MSG_SHIPPINGFEE_FAIL);
//        shippingFeeVolley.requestGet(Const.Request.shippingFee, getHandler(), UserInfosPref.getInstance(this).getUser().getToken());
        addrDetailVolley = new JsonVolley(this,Const.Message.MSG_ADDR_DETAIL_SUCC,Const.Message.MSG_ADDR_DETAIL_FAIL);
        getFreightRuleVolley = new JsonVolley(this,Const.Message.MSG_GET_FREIGHT_RULE_SUCC,Const.Message.MSG_GET_FREIGHT_RULE_FAIL);
        getFreightRuleVolley.requestGet(Const.Request.getFreightRule,getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),UserInfosPref.getInstance(this).getServiceCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );


    }

    @Override
    protected void initViews() {
        layoutDoOrder = (RelativeLayout) findViewById(R.id.rl_do_order);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.order_appointment);
        tipsText = (TextView) findViewById(R.id.text_do_order_tips);
        commitOrderParent = (LinearLayout) findViewById(R.id.commit_order_parent);
        commitOrderParent.setOnClickListener(this);
        selectAddrParent = (LinearLayout) findViewById(R.id.select_addr_parent);
        selectAddrParent.setOnClickListener(this);
        washShoesParent = (LinearLayout) findViewById(R.id.wash_shoes_parent);
        unwashShoesParent = (LinearLayout) findViewById(R.id.unwash_shoes_parent);
        washShoesImage = (ImageView) findViewById(R.id.image_wash_shoes);
        unwashShoesImage = (ImageView) findViewById(R.id.image_unwash_shoes);
        addrText = (TextView) findViewById(R.id.text_addr);

        addrNameText = (TextView) findViewById(R.id.text_addr_name);
        addrGenderText = (TextView) findViewById(R.id.text_addr_gender);
        addrPhoneText = (TextView) findViewById(R.id.text_addr_phone);

        washShoesParent.setOnClickListener(this);
        unwashShoesParent.setOnClickListener(this);
//        orderTimeText = (TextView) findViewById(R.id.text_do_order_time);
//        orderTimeText.setOnClickListener(this);
        washShoesText = (TextView) findViewById(R.id.text_wash_shoes);
//        noAddrText = (TextView) findViewById(R.id.text_no_addr);
        unwashShoesText = (TextView) findViewById(R.id.text_unwash_shoes);

        hasAddrParent = (LinearLayout) findViewById(R.id.ll_has_addr);
        noAddrParent = (LinearLayout) findViewById(R.id.ll_no_addr);
//        conntactImage = (ImageView) findViewById(R.id.image_contact_me);
        /*conntactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionSheetDialog(DoOrderActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true).setTitle("我们将为您提供满意的咨询服务").
                        addSheetItem("在线客服", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        if (ChatClient.getInstance().isLoggedInBefore()) {
                                            HxHelper.getInstance(DoOrderActivity.this).toChat(Const.MESSAGE_TO_DEFAULT,DoOrderActivity.this);
                                        }else {
//                                                  createRandomAccountThenLoginChatServer();
                                            HxHelper.getInstance(DoOrderActivity.this).login(UserInfosPref.getInstance(DoOrderActivity.this).getUser().getMobile(),UserInfosPref.getInstance(DoOrderActivity.this).getUser().getHxPwd());
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
                        .show();
            }
        });*/
//        needWashShoes = washShoesBox.isChecked() ? 1:0;
        if (washShoesImage.isShown()) {
            needWashShoes = 1;
            washShoesText.setEnabled(true);
            unwashShoesText.setEnabled(false);
        } else if (unwashShoesImage.isShown()) {
            needWashShoes = 0;
            washShoesText.setEnabled(false);
            unwashShoesText.setEnabled(true);
        }
        String defaultTime = getDefaultOrderTime();

        if (!TextUtils.isEmpty(defaultTime)) {
            String[] times = defaultTime.split("&");
//            + DateUtil.getMyDate(this, times[0], new Date())
//            orderTimeText.setText(times[0] + "   " + times[1]);
            String[] hours = times[1].split("~");
            uploadTimeBegin = times[0] + " " + hours[0].trim() + ":00";
            uploadTimeEnd = times[0] + " " + hours[1].trim() + ":00";
        }
    // ---------add addr
        addrUserEdit = (EditText) findViewById(R.id.edit_add_addr_name);
        addrMobileEdit = (EditText) findViewById(R.id.edit_add_addr_mobile);
        addrPrefixEdit = (TextView) findViewById(R.id.text_add_addr_prefix);
        addrEndfixEdit = (EditText) findViewById(R.id.edit_add_addr_endfix);
        genderWomanText = (TextView) findViewById(R.id.text_gender_woman);
        genderManText = (TextView) findViewById(R.id.text_gender_man);
        addrPrefixEdit.setOnClickListener(this);
        genderManText.setOnClickListener(this);
        genderWomanText.setOnClickListener(this);
        //-----------------------
        showDefaultAddrVolley.requestPost(Const.Request.showDefaultAddr, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    DoOrderFailDialog failDialog;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_SHOW_DEFAULT_ADDR_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    DefaultAddrResp addrResp = (DefaultAddrResp) JsonParser.jsonToObject(msg.obj + "", DefaultAddrResp.class);
                    if (addr == null) {
                        addr = addrResp.getData();
                        if(addr != null){
                            addrId = addr.getId();
                            hasAddrParent.setVisibility(View.VISIBLE);
                            noAddrParent.setVisibility(View.GONE);
                        }else {
                            noAddrParent.setVisibility(View.VISIBLE);
                            hasAddrParent.setVisibility(View.GONE);
                        }

                        refreshAddr(addr);
                    }

                }

                break;

            case Const.Message.MSG_CREATE_ORDER_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
//                    OperationToast.showOperationResult(getApplicationContext(), "下单成功", 0);
//                    if(!TextUtils.isEmpty(UserInfosPref.getInstance(this).getUser().getMobile())){
//                        UmengEventHelper.doOrderEvent(this,UserInfosPref.getInstance(this).getUser().getMobile());
//                    }
//                    DoOrderSuccessDialog dialog = new DoOrderSuccessDialog(this).builder();
//                    dialog.setCancelLinstener(this);
//
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
                    OrderDetailResp resp = (OrderDetailResp) JsonParser.jsonToObject(msg.obj+"",OrderDetailResp.class);
                    Intent intent = new Intent(this,DoOrderSuccActivity.class);
                    intent.putExtra("orderNo",resp.getData().getOrderNo());
                    startActivity(intent);


                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(this,msg.obj + "",0);
//                    failDialog = new DoOrderFailDialog(this).builder(msg.obj + "");
//                    failDialog.show();
                }
                break;

            case Const.Message.MSG_CREATE_ORDER_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail);
                break;
            case Const.Message.MSG_SHIPPINGFEE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    ShippingFeeResp resp = (ShippingFeeResp) JsonParser.jsonToObject(msg.obj + "", ShippingFeeResp.class);
                    if (resp != null && resp.getData() != null) {
                        ShippingFee fee = resp.getData();
                        if (fee.getShippingFee() != null && fee.getTotalPrice() != null) {
                            String formatStr = getResources().getString(R.string.order_appointment_prompt);
                            tipsText.setText(String.format(formatStr, fee.getTotalPrice(), fee.getShippingFee()));
                        }

                    }
                } else {
                }
                break;

            case Const.Message.MSG_SHIPPINGFEE_FAIL:
                break;

            case Const.Message.MSG_GET_FREIGHT_RULE_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    GetFreightRuleResp resp = (GetFreightRuleResp) JsonParser.jsonToObject(msg.obj + "",GetFreightRuleResp.class);

                    if(!TextUtils.isEmpty(resp.getData())){
                        tipsText.setVisibility(View.VISIBLE);
                        tipsText.setText(resp.getData());
                    }

                }else if(msg.arg1 == Const.Request.REQUEST_FAIL){

                }

                break;

            case Const.Message.MSG_GET_FREIGHT_RULE_FAIL:

                break;

            case Const.Message.MSG_ADDR_DETAIL_SUCC:
                if(msg.arg1 == Const.Request.REQUEST_SUCC){
                    DefaultAddrResp resp = (DefaultAddrResp) JsonParser.jsonToObject(msg.obj+"",DefaultAddrResp.class);
                    Addr addr = resp.getData();
                    if(addr != null){
                        refreshAddr(addr);
                    }
                }
                break;

        }
    }

    @Override
    public void doOrderSuccCancelClicked() {
        markDefaultVolley.addParams("addressId", addr.getId());
        markDefaultVolley.requestPost(Const.Request.markDefault, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        Intent intent = new Intent(this, HomeActivity2.class);
        intent.setAction(Const.MyAction.ACTION_REFRESH_INSERVICE_ORDERS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == selectAddrParent) {
            Intent intent = new Intent(this, AddrActivity.class);
            intent.putExtra("operation", OPERATION_SELECT_ADDR);
            startActivityForResult(intent, OPERATION_SELECT_ADDR);
        } else if (v == washShoesParent) {
            if (washShoesImage.isShown()) {
                washShoesImage.setVisibility(View.INVISIBLE);
                unwashShoesImage.setVisibility(View.VISIBLE);
                washShoesText.setEnabled(false);
                unwashShoesText.setEnabled(true);
            } else {
                washShoesImage.setVisibility(View.VISIBLE);
                unwashShoesImage.setVisibility(View.INVISIBLE);
                washShoesText.setEnabled(true);
                unwashShoesText.setEnabled(false);
            }
        } else if (v == unwashShoesParent) {
            if (unwashShoesImage.isShown()) {
                washShoesImage.setVisibility(View.VISIBLE);
                unwashShoesImage.setVisibility(View.INVISIBLE);
                washShoesText.setEnabled(true);
                unwashShoesText.setEnabled(false);
            } else {
                washShoesImage.setVisibility(View.INVISIBLE);
                unwashShoesImage.setVisibility(View.VISIBLE);
                washShoesText.setEnabled(false);
                unwashShoesText.setEnabled(true);
            }
        }  else if (v == commitOrderParent) {
//            createOrder();
//            Toast.makeText(this,"next step",Toast.LENGTH_SHORT).show();
            if(this.addr == null){
                this.addr = getFillAddr();
            }
            if(this.addr == null){
                return;
            }
            Intent intent = new Intent(this,DoOrderNextActivity.class);
           intent.putExtra("addr",JsonParser.objectToJsonStr(this.addr));
            startActivity(intent);
        }else if (v == addrPrefixEdit) {
            Intent intent = new Intent(this, SelAddrAMapActivity.class);
            startActivityForResult(intent, OPERATION_MAP);
        }if (v == genderWomanText) {
            if(genderWomanText.isSelected()){
                return;
            }
            genderWomanText.setSelected(true);
            genderManText.setSelected(false);

//            genderWomancb.setChecked(!genderWomancb.isChecked());
//            if (genderWomancb.isChecked()) {
//                genderWomanText.setEnabled(true);
//                genderMancb.setChecked(false);
//                genderManText.setEnabled(false);
//            }else {
//                genderWomancb.setChecked(false);
//                genderWomanText.setEnabled(false);
//            }
        } else if (v == genderManText) {
            if(genderManText.isSelected()){
                return;
            }

            genderManText.setSelected(true);

            genderWomanText.setSelected(false);

//            genderMancb.setChecked(!genderMancb.isChecked());
//            if (genderMancb.isChecked()){
//                genderWomancb.setChecked(false);
//                genderManText.setEnabled(true);
//                genderWomanText.setEnabled(false);
//            }else{
//                genderMancb.setChecked(false);
//                genderManText.setEnabled(false);
//            }

        }
    }
    String longtitule;
    String latitude;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_SELECT_ADDR && resultCode == RESULT_OK) {
            Addr addr = (Addr) JsonParser.jsonToObject(data.getStringExtra("addr"), Addr.class);
            this.addr = addr;
            if(addr != null){
                addrId = addr.getId();
            }
            refreshAddr(addr);
        }else if(requestCode == OPERATION_SELECT_ADDR && resultCode == RESULT_CANCELED){
            if(addrId != null){
                addrDetailVolley.addParams("id",addrId);
                addrDetailVolley.requestPost(Const.Request.addrDetail,getHandler(),UserInfosPref.getInstance(this)
                        .getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
            }
        }else if(requestCode == OPERATION_MAP && resultCode == RESULT_OK){
            longtitule = data.getStringExtra("longtitule");
            latitude = data.getStringExtra("latitude");
            addrPrefixEdit.setText(data.getStringExtra("address"));
            addrEndfixEdit.setText(data.getStringExtra("addressDetail"));
        }

    }

    private void refreshAddr(Addr addr) {
        if (addr == null) {
//            noAddrText.setVisibility(View.VISIBLE);
            return;
        }
//        noAddrText.setVisibility(View.INVISIBLE);
        if (addr.getGender() == Const.MAN) {
            addrGenderText.setText(R.string.man);
        } else if (addr.getGender() == Const.WOMAN) {
            addrGenderText.setText(R.string.woman);
        }
        addrNameText.setText(addr.getContact());
        addrPhoneText.setText(addr.getMobile());
        addrText.setText(addr.getAddress() + "  " + addr.getAddressDetail());
    }

    @Override
    public void selectedTimeOk(String time) {

        String[] times = time.split("&");
//        + "   " + DateUtil.getMyDate(this, times[0], new Date())
        String[] hours = times[1].split("~");
        uploadTimeBegin = times[0] + " " + hours[0].trim() + ":00";
        uploadTimeEnd = times[0] + " " + hours[1].trim() + ":00";

    }

    private void createOrder() {
        if (TextUtils.isEmpty(uploadTimeEnd)) {
            OperationToast.showOperationResult(getApplicationContext(), R.string.select_time_prompt,R.mipmap.reminder_icon);
            return;
        }
        if (addr == null) {
//            OperationToast.showOperationResult(getApplicationContext(), R.string.select_addr_prompt,R.mipmap.reminder_icon);
//            return;
            Addr a = getFillAddr();
            if(null == a){
                return;
            }
            this.addr = addr;
            createOrderVolley.addParams("needWashShoes", needWashShoes);
            createOrderVolley.addParams("expectDateB", uploadTimeBegin);
            createOrderVolley.addParams("expectDateE", uploadTimeEnd);
            JSONObject addrObject = new JSONObject();

            try {
                addrObject.put("address",a.getAddress());
                addrObject.put("addressDetail",a.getAddressDetail());
                addrObject.put("contact",a.getContact());
                addrObject.put("defaultFlag",0);
                addrObject.put("gender",a.getGender());
                addrObject.put("latitude",a.getLatitude());
                addrObject.put("longtitude",a.getLongtitude());
                addrObject.put("mobile",a.getMobile());
            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(this,"json exception",Toast.LENGTH_LONG).show();
            }
            createOrderVolley.addParams("newAddress",addrObject);
            setOperationMsg(getString(R.string.operating));
            createOrderVolley.requestPost(Const.Request.create, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
        }else {
            if (washShoesImage.isShown()) {
                needWashShoes = 1;
            } else if (unwashShoesImage.isShown()) {
                needWashShoes = 0;
            }

            createOrderVolley.addParams("needWashShoes", needWashShoes);
            createOrderVolley.addParams("expectDateB", uploadTimeBegin);
            createOrderVolley.addParams("expectDateE", uploadTimeEnd);
            createOrderVolley.addParams("addressId", addr.getId());
            setOperationMsg(getString(R.string.operating));
            createOrderVolley.requestPost(Const.Request.create, this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
        }

    }
    EditText addrUserEdit, addrMobileEdit, addrEndfixEdit;
    TextView addrPrefixEdit;
    TextView genderWomanText,genderManText;

    private Addr getFillAddr(){
        String addrGender = "";
        Addr addr = new Addr();
        String addrName = addrUserEdit.getText().toString().trim();
        if (genderManText.isSelected()) {
            addrGender = "1";
            addr.setGender(1);
        } else if (genderWomanText.isSelected()) {
            addrGender = "2";
            addr.setGender(2);
        }else {
            OperationToast.showOperationResult(this, R.string.addr_sel_sex,R.mipmap.reminder_icon);
            return null;
        }
       String addrMobile = addrMobileEdit.getText().toString().trim();
        String addrPrefix = addrPrefixEdit.getText().toString().trim();
        String addrEndfix = addrEndfixEdit.getText().toString().trim();
        if (TextUtils.isEmpty(addrName)) {
            OperationToast.showOperationResult(this, R.string.addr_user_prompt,R.mipmap.reminder_icon);

            return null;
        }
        addr.setContact(addrName);
        if (TextUtils.isEmpty(addrMobile)) {
            OperationToast.showOperationResult(this, R.string.addr_mobile_prompt,R.mipmap.reminder_icon);

            return null;
        }
        addr.setMobile(addrMobile);
        if (TextUtils.isEmpty(addrPrefix)) {

            OperationToast.showOperationResult(this, R.string.addr_prefix_prompt,R.mipmap.reminder_icon);
            return null;
        }
        addr.setAddress(addrPrefix);
        if (TextUtils.isEmpty(addrEndfix)) {

            OperationToast.showOperationResult(this, R.string.addr_endfix_prompt,R.mipmap.reminder_icon);
            return null;
        }
        addr.setAddressDetail(addrEndfix);
        addr.setLatitude(latitude);
        addr.setLongtitude(longtitule);
        return addr;
    }
}
