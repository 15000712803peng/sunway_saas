package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Addr;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.FontUtil;

public class AddAddrActivity extends InitActivity implements View.OnClickListener {

    private final int OPERATION_ADD = 1;
    private final int OPERATION_EDIT = 2;
    int operation;
    boolean showMobile;
    EditText addrUserEdit, addrMobileEdit, addrEndfixEdit;
    EditText addrPrefixEdit;
    TextView titleText;
    JsonVolley createAddrVolley;
    Addr addr;
    int defaultFlag = 0;
    UserInfosPref userInfosPref;
    ImageView genderWomanImage, genderManImage;
    LinearLayout genderWomanParent, genderManParent;
    RelativeLayout saveAddrParent;
    TextView addrPrefixText;
    RelativeLayout layoutAddAddress;
    TextView genderWomanText,genderManText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_addr);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutAddAddress, "OpenSans-Regular.ttf");

    }

    @Override
    protected void initData() {
        operation = getIntent().getIntExtra("operation", 0);
        showMobile = getIntent().getBooleanExtra("showMobile", false);
        createAddrVolley = new JsonVolley(this, Const.Message.MSG_CREATE_ADDR_SUCC, Const.Message.MSG_CREATE_ADDR_FAIL);
        userInfosPref = UserInfosPref.getInstance(this);
    }

    @Override
    protected void initViews() {
        layoutAddAddress = (RelativeLayout) findViewById(R.id.rl_add_address);
        titleText = (TextView) findViewById(R.id.text_title);
        addrMobileEdit = (EditText) findViewById(R.id.edit_add_addr_mobile);
        addrEndfixEdit = (EditText) findViewById(R.id.edit_add_addr_endfix);
        addrPrefixEdit = (EditText) findViewById(R.id.edit_add_addr_prefix);
        genderWomanImage = (ImageView) findViewById(R.id.image_gender_woman);
        genderManImage = (ImageView) findViewById(R.id.image_gender_man);
        genderWomanParent = (LinearLayout) findViewById(R.id.gender_woman_parent);
        genderManParent = (LinearLayout) findViewById(R.id.gender_man_parent);
        saveAddrParent = (RelativeLayout) findViewById(R.id.save_addr_parent);
        saveAddrParent.setOnClickListener(this);
        genderManParent.setOnClickListener(this);
        genderWomanParent.setOnClickListener(this);
        addrPrefixEdit.setOnClickListener(this);
        addrPrefixText = (TextView) findViewById(R.id.text_add_addr_prefix);
        addrPrefixText.setOnClickListener(this);
        addrUserEdit = (EditText) findViewById(R.id.edit_add_addr_name);
        genderManText = (TextView) findViewById(R.id.text_gender_man);
        genderWomanText = (TextView) findViewById(R.id.text_gender_woman);
        if(operation == OPERATION_EDIT){
            titleText.setText(R.string.edit_addr_title);

            addr = (Addr) JsonParser.jsonToObject(getIntent().getStringExtra("addr"), Addr.class);
            addrUserEdit.setText(addr.getContact());
            addrMobileEdit.setText(addr.getMobile());
            addrEndfixEdit.setText(addr.getAddressDetail());
            addrPrefixEdit.setText(addr.getAddress());
            longtitule = addr.getLongtitude();
            latitude = addr.getLatitude();
            defaultFlag = addr.getDefaultFlag();
            if (addr.getGender() == 1) {
                genderManImage.setVisibility(View.VISIBLE);
                genderWomanImage.setVisibility(View.INVISIBLE);
                genderManText.setEnabled(true);
                genderWomanText.setEnabled(false);
            }else{
                genderManImage.setVisibility(View.INVISIBLE);
                genderWomanImage.setVisibility(View.VISIBLE);
                genderManText.setEnabled(false);
                genderWomanText.setEnabled(true);
            }
        }else {
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
////                Intent intent = new Intent(AddAddrActivity.this, AddressSelectAMapActivity.class);
////                //intent.putExtra("longtitude",longtitude);
////                //intent.putExtra("latitude",latitude);
////                startActivityForResult(intent, 0);
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
        if (view == genderWomanParent) {
            if (genderWomanImage.isShown()) {
                genderWomanImage.setVisibility(View.INVISIBLE);
                genderManImage.setVisibility(View.VISIBLE);
            }else{                genderWomanImage.setVisibility(View.VISIBLE);
                genderManImage.setVisibility(View.INVISIBLE);
                genderWomanText.setEnabled(true);
                genderManText.setEnabled(false);
            }
        } else if (view == genderManParent) {
            if (genderManImage.isShown()) {
                genderManImage.setVisibility(View.INVISIBLE);
                genderWomanImage.setVisibility(View.VISIBLE);
            }else{
                genderManImage.setVisibility(View.VISIBLE);
                genderWomanImage.setVisibility(View.INVISIBLE);
                genderWomanText.setEnabled(false);
                genderManText.setEnabled(true);
            }

        } else if (view == saveAddrParent) {
            saveAddr();
        } else if (view == addrPrefixEdit || view == addrPrefixText) {
            Intent intent = new Intent(AddAddrActivity.this, AddressSelectAMapActivity.class);

            startActivityForResult(intent, 0);
        }
    }

    String addrName;
    String addrGender;
    String addrMobile;
    String addrPrefix;
    String addrEndfix;


    private void saveAddr() {
        addrName = addrUserEdit.getText().toString().trim();
        if (genderManImage.isShown()) {
            addrGender = "1";
        } else if (genderWomanImage.isShown()) {
            addrGender = "2";
        }
        addrMobile = addrMobileEdit.getText().toString().trim();
        addrPrefix = addrPrefixEdit.getText().toString().trim();
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
            setOperationMsg(getString(R.string.operating));


            createAddrVolley.requestPost(Const.Request.updateAddr,  this,getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

        } else {
            setOperationMsg(getString(R.string.operating));
            createAddrVolley.requestPost(Const.Request.creaetAddr,  this, getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

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
            addrPrefixEdit.setText(data.getStringExtra("address"));
            addrEndfixEdit.setText(data.getStringExtra("addressDetail"));
        }
    }
}
