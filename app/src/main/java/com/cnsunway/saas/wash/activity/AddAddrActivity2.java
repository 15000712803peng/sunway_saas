package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.DelAddrDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Addr;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.EditTextUtils;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.IEditTextChangeListener;

public class AddAddrActivity2 extends InitActivity implements View.OnClickListener , DelAddrDialog.OnDelOkLinstener{

    private final int OPERATION_ADD = 1;
    private final int OPERATION_EDIT = 2;
    int operation;
    boolean showMobile;
    EditText addrUserEdit, addrMobileEdit, addrEndfixEdit;
    TextView titleText;
    JsonVolley createAddrVolley;
    Addr addr;
    int defaultFlag = 0;
    UserInfosPref userInfosPref;
    TextView saveAddrParent;
    TextView addrPrefixText;
    RelativeLayout layoutAddAddress;
    TextView genderWomanText,genderManText;
    CheckBox defaultAddrBox;
    TextView rightTitleText;
    DelAddrDialog delAddrDialog;
    LinearLayout addrPrefixLL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_addr2);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutAddAddress, "OpenSans-Regular.ttf");
    }

    @Override
    protected void initData() {
        operation = getIntent().getIntExtra("operation", 0);
        showMobile = getIntent().getBooleanExtra("showMobile", false);
        createAddrVolley = new JsonVolley(this, Const.Message.MSG_CREATE_ADDR_SUCC, Const.Message.MSG_CREATE_ADDR_FAIL);
        userInfosPref = UserInfosPref.getInstance(this);
        delAddrDialog = new DelAddrDialog(this).builder();
        delAddrDialog.setDelOkLinstener(this);
    }

    @Override
    protected void initViews() {
        layoutAddAddress = (RelativeLayout) findViewById(R.id.rl_add_address);
        titleText = (TextView) findViewById(R.id.text_title);
        addrMobileEdit = (EditText) findViewById(R.id.edit_add_addr_mobile);
        addrEndfixEdit = (EditText) findViewById(R.id.edit_add_addr_endfix);
        rightTitleText = (TextView) findViewById(R.id.text_title_right);
        rightTitleText.setOnClickListener(this);
        saveAddrParent = (TextView) findViewById(R.id.save_addr_parent);
        saveAddrParent.setOnClickListener(this);
        defaultAddrBox = (CheckBox) findViewById(R.id.box_default_addr);
        addrPrefixText = (TextView) findViewById(R.id.edit_add_addr_prefix);
        addrPrefixLL = (LinearLayout) findViewById(R.id.ll_add_addr_prefix);
//        addrPrefixText.setOnClickListener(this);
        addrPrefixLL.setOnClickListener(this);
        addrUserEdit = (EditText) findViewById(R.id.edit_add_addr_name);
        genderManText = (TextView) findViewById(R.id.text_gender_man);
        genderWomanText = (TextView) findViewById(R.id.text_gender_woman);
        genderManText.setOnClickListener(this);
        genderWomanText.setOnClickListener(this);
        genderWomanText.setSelected(true);
        if(operation == OPERATION_EDIT){
            titleText.setText(R.string.edit_addr_title);
            saveAddrParent.setSelected(true);
            rightTitleText.setVisibility(View.VISIBLE);
            rightTitleText.setText(R.string.del);
            rightTitleText.setTextColor(Color.parseColor("#F8451B"));
            addr = (Addr) JsonParser.jsonToObject(getIntent().getStringExtra("addr"), Addr.class);
            addrUserEdit.setText(addr.getContact());
            addrMobileEdit.setText(addr.getMobile());
            addrEndfixEdit.setText(addr.getAddressDetail());
            addrPrefixText.setText(addr.getAddress());
            longtitule = addr.getLongtitude();
            latitude = addr.getLatitude();
            defaultFlag = addr.getDefaultFlag();
            delAddrDialog.setAddrId(addr.getId());
            if(defaultFlag == 1){
                defaultAddrBox.setChecked(true);
            }else {
                defaultAddrBox.setChecked(false);
            }
            if (addr.getGender() == 1) {
                genderManText.setSelected(true);
                genderWomanText.setSelected(false);
            }else{
                genderManText.setSelected(false);
                genderWomanText.setSelected(true);
            }
        }else {
            saveAddrParent.setSelected(false);
            titleText.setText(R.string.add_addr_title);
            if(!TextUtils.isEmpty(userInfosPref.getUserName()) && showMobile){
                addrMobileEdit.setText(userInfosPref.getUserName());
            }

        }


//        womenBox = (CheckBox) findViewById(R.id.cb_addr_women);
//        manBox = (CheckBox) findViewById(R.id.cb_addr_men);
//        womenBoxParent = (LinearLayout) findViewById(R.id.ll_addr_gender_women);
//        manBoxParent = (LinearLayout) findViewById(R.id.ll_addr_gender_men);
//        womenBoxParent.setOnClickListener(this);
//        manBoxParent.setOnClickListener(this);
//        manBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                womenBox.setChecked(!b);
//            }
//        });
//        womenBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                manBox.setChecked(!b);
//            }
//        });
//
//        addrPrefixEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//            }
//        });
//
//        if(operation == OPERATION_ADD){
//
//            boolean isFirst = getIntent().getBooleanExtra("is_first",false);
//            if(!TextUtils.isEmpty(userInfosPref.getUserName()) && isFirst){
//                addrMobileEdit.setText(userInfosPref.getUserName());
//            }
//            titleText.setText(R.string.add_addr_title);
//            womenBox.setChecked(true);
//            manBox.setChecked(false);
//        }else if(operation == OPERATION_EDIT){
//            titleText.setText(R.string.edit_addr_title);
//            addr = (Addr) JsonParser.jsonToObject(getIntent().getStringExtra("addr"), Addr.class);
//
//            addrUserEdit.setText(addr.getContact());
//            addrMobileEdit.setText(addr.getMobile());
//            addrEndfixEdit.setText(addr.getAddressDetail());
//            addrPrefixEdit.setText(addr.getAddress());
//            if(addr.getGender() == 1){
//                manBox.setChecked(true);
//                womenBox.setChecked(false);
//            }else{
//                manBox.setChecked(false);
//                womenBox.setChecked(true);
//            }
//            longtitule = addr.getLongtitude();
//            latitude = addr.getLatitude();
//            defaultFlag = addr.getDefaultFlag();
//        }

        EditTextUtils.ButtonChangeListener textChangeListener = new EditTextUtils.ButtonChangeListener();
        textChangeListener.addAllEditText(addrUserEdit,addrMobileEdit,addrEndfixEdit);
        EditTextUtils.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if(isHasContent){
                    saveAddrParent.setSelected(true);
                }else {
                    saveAddrParent.setSelected(false);
                }
            }

            @Override
            public void lengthChange(boolean hasSpecificLength) {

            }
        });

    }




    @Override
    protected void handlerMessage(Message msg) {

        switch (msg.what) {
            case Const.Message.MSG_CREATE_ADDR_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    if (operation == OPERATION_ADD) {
                        OperationToast.showOperationResult(this, R.string.add_succ_prompt);
                    } else if (operation == OPERATION_EDIT) {
                        OperationToast.showOperationResult(this, R.string.edit_succ_prompt);
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {

                    if (operation == OPERATION_ADD) {
                        OperationToast.showOperationResult(this, R.string.add_fail_prompt);
                    } else if (operation == OPERATION_EDIT) {
                        OperationToast.showOperationResult(this, R.string.edit_fail_prompt);
                    }
                }
                break;

            case Const.Message.MSG_CREATE_ADDR_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail);
                break;
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == genderWomanText) {
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
        } else if (view == genderManText) {
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

        } else if (view == saveAddrParent) {
            saveAddr();
        } else if (view == addrPrefixLL) {
            if(operation == OPERATION_EDIT){
                Intent intent = new Intent(AddAddrActivity2.this, SelAddrAMapActivity.class);
                intent.putExtra("addr", JsonParser.objectToJsonStr(addr));
                startActivityForResult(intent, 0);
            }else {
                Intent intent = new Intent(AddAddrActivity2.this, SelAddrAMapActivity.class);
                startActivityForResult(intent, 0);
            }
        }else if(view == rightTitleText){
            if(!TextUtils.isEmpty(delAddrDialog.getAddrId())){
                delAddrDialog.show();
            }
        }
    }

    String addrName;
    String addrGender;
    String addrMobile;
    String addrPrefix;
    String addrEndfix;


    private void saveAddr() {
        addrName = addrUserEdit.getText().toString().trim();
        if (genderManText.isSelected()) {
            addrGender = "1";
        } else if (genderWomanText.isSelected()) {
            addrGender = "2";
        }
        addrMobile = addrMobileEdit.getText().toString().trim();
        addrPrefix = addrPrefixText.getText().toString().trim();
        addrEndfix = addrEndfixEdit.getText().toString().trim();
        if (TextUtils.isEmpty(addrName)) {
            OperationToast.showOperationResult(this, R.string.addr_user_prompt);
            return;
        }
        if (TextUtils.isEmpty(addrMobile)) {
            OperationToast.showOperationResult(this, R.string.addr_mobile_prompt);
            return;
        }
        if (TextUtils.isEmpty(addrPrefix)) {
            OperationToast.showOperationResult(this, R.string.addr_prefix_prompt);
            return;
        }
        if (TextUtils.isEmpty(addrEndfix)) {
            OperationToast.showOperationResult(this, R.string.addr_endfix_prompt);
            return;
        }
        defaultFlag = defaultAddrBox.isChecked() ? 1:0;
        createAddrVolley.addParams("contact", addrName);
        createAddrVolley.addParams("gender", addrGender);
        createAddrVolley.addParams("address", addrPrefix);
        createAddrVolley.addParams("addressDetail", addrEndfix);
        createAddrVolley.addParams("mobile", addrMobile);
        createAddrVolley.addParams("longtitude", longtitule);
        createAddrVolley.addParams("latitude", latitude);
        createAddrVolley.addParams("defaultFlag", defaultFlag + "");


        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        if (operation == OPERATION_EDIT) {
            createAddrVolley.addParams("id", addr.getId());
            Log.e("------","default flag:" + defaultFlag);
            createAddrVolley.addParams("cityCode", addr.getCityCode());
            createAddrVolley.addParams("cityName", addr.getCityName());
            createAddrVolley.addParams("districtCode",addr.getDistrictCode());
            createAddrVolley.addParams("districtName", addr.getDistrictName());
            setOperationMsg(getString(R.string.operating));
            createAddrVolley.requestPost(Const.Request.updateAddr, this,getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );

        } else {
            setOperationMsg(getString(R.string.operating));
            createAddrVolley.requestPost(Const.Request.creaetAddr,this, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

        }


    }

    String latitude = "";
    String longtitule = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            longtitule = data.getStringExtra("longtitule");
            latitude = data.getStringExtra("latitude");
            createAddrVolley.addParams("longtitude", longtitule);
            createAddrVolley.addParams("latitude", latitude);
            addrPrefixText.setText(data.getStringExtra("address"));
            addrEndfixEdit.setText(data.getStringExtra("addressDetail"));
        }
    }

    @Override
    public void delOk() {
        OperationToast.showOperationResult(this, R.string.del_succ);
        setResult(RESULT_OK);
        finish();
    }
}
