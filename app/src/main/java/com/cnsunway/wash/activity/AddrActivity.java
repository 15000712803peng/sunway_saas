package com.cnsunway.wash.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.net.StringVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.Addr;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.resp.AddrResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.swipelist.SwipeMenu;
import com.cnsunway.wash.swipelist.SwipeMenuCreator;
import com.cnsunway.wash.swipelist.SwipeMenuItem;
import com.cnsunway.wash.swipelist.SwipeMenuListView;
import com.cnsunway.wash.util.FontUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by LL on 2015/12/2.
 */
public class AddrActivity extends LoadingActivity implements  AdapterView.OnItemClickListener, View.OnClickListener {
    TextView titleText;
    SwipeMenuListView addrList;
    List<Addr> addrs = new ArrayList<>();
    StringVolley searchAddrVolley;
    StringVolley markDefaultVolley;

    int operation = 0;
    private final int OPERATION_SELECT = 1;
    private final int DEFALUT_ADDR = 1;

    TextView addText;
    LinearLayout layoutAddress;
    AddrAdapter addrAdapter;
    boolean hasAddr = false;
    RelativeLayout addAddrParent;
    private final int OPERATION_ADD_ADDR = 1;
    private final int OPERATION_EDIT_ADDR = 2;

    @Override
    protected void initData() {
        searchAddrVolley = new StringVolley(this, Const.Message.MSG_SEARCH_ADDR_SUCC, Const.Message.MSG_SEARCH_ADDR_FAIL);
        markDefaultVolley = new StringVolley(this, Const.Message.MSG_MARK_DEFAULT_SUCC, Const.Message.MSG_ORDER_DETAIL_FAIL);

        operation = getIntent().getIntExtra("operation", 0);
    }

    public void back(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void initViews() {
        layoutAddress = (LinearLayout) findViewById(R.id.ll_address);
        addrList = (SwipeMenuListView) findViewById(R.id.list_addr);
//         addrList.setAdapter(new AddrAdapter(addrs));
        titleText = (TextView) findViewById(R.id.text_title);
        addAddrParent = (RelativeLayout) findViewById(R.id.add_addr_parent);
        addAddrParent.setOnClickListener(this);
        if (operation == OPERATION_SELECT) {
            titleText.setText(R.string.select_addr);
        } else {
            titleText.setText(R.string.title_addr_mgr);
        }

        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();

        searchAddrVolley.requestGet(Const.Request.search, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        showCenterLoading();
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_SEARCH_ADDR_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    addAddrParent.setVisibility(View.VISIBLE);
                    AddrResp addrResp = (AddrResp) JsonParser.jsonToObject(msg.obj + "", AddrResp.class);
                    List<Addr> addrs = addrResp.getData();
                    if (addrs == null || addrs.size() == 0) {
                        showNoData(getString(R.string.no_order_prompt));
                        addrAdapter = new AddrAdapter(new ArrayList<Addr>());
                        addrList.setAdapter(addrAdapter);
                        return;
                    } else {
                        hideNoData();
                    }
                    initList(addrs);
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    showNoData(msg.obj + "");
                    hasAddr = false;
                }
                break;

            case Const.Message.MSG_SEARCH_ADDR_FAIL:
                hasAddr = false;
                hideLoading();
                showNetFail();
                break;

            case Const.Message.MSG_MARK_DEFAULT_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                    searchAddrVolley.requestGet(Const.Request.search, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
                    OperationToast.showOperationResult(this, R.string.setting_succ);
                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    OperationToast.showOperationResult(this, R.string.setting_fail);
                }
                break;

            case Const.Message.MSG_MARK_DEFAULT_FAIL:
                OperationToast.showOperationResult(this, R.string.setting_fail);
                break;

        }
    }

    public void initList(List<Addr> addrs) {

        if (addrAdapter == null) {
            addrAdapter = new AddrAdapter(addrs);
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem deleteAddrItem = new SwipeMenuItem(
                            getApplicationContext());
                    deleteAddrItem.setWidth(dp2px(55));
                    deleteAddrItem.setBackground(new ColorDrawable(Color.rgb(0xfb,
                            0x45, 0x1b)));
                    deleteAddrItem.setIcon(R.mipmap.icon_addr_del);
                    menu.addMenuItem(deleteAddrItem);
                }
            };
//            addrList.setMenuCreator(creator);
//            addrList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(int position, SwipeMenu menu,
//                                               int index) {
//                    Addr addr = (Addr) addrAdapter.getItem(position);
//                    delAddrDialog.setAddrId(addr.getId());
//                    delAddrDialog.show();
//                    return false;
//                }
//            });
            addrList.setAdapter(addrAdapter);
            addrList.setOnItemClickListener(this);

        } else {
            addrAdapter.getAddrs().clear();
            addrAdapter.getAddrs().addAll(addrs);
            addrAdapter.notifyDataSetChanged();
            addrList.setOnItemClickListener(this);

        }

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addr);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutAddress, "OpenSans-Regular.ttf");
    }

    public void delOk() {
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        searchAddrVolley.requestGet(Const.Request.search, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (operation == OPERATION_SELECT) {

            Addr addr = (Addr) adapterView.getAdapter().getItem(i);
            Intent data = new Intent();
            data.putExtra("addr", JsonParser.objectToJsonStr(addr));
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == addAddrParent) {
            Intent intent = new Intent(AddrActivity.this, AddAddrActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("operation", OPERATION_ADD_ADDR);
            intent.putExtra("showMobile",addrAdapter != null && addrAdapter.getCount() <= 0);
//
            startActivityForResult(intent, REQUEST_ADD_ADDR);
        }
    }

    class AddrAdapter extends BaseAdapter {
        List<Addr> addrs;

        public AddrAdapter(List<Addr> addrs) {
            this.addrs = addrs;
        }

        public List<Addr> getAddrs() {
            return addrs;
        }

        public void setAddrs(List<Addr> addrs) {
            this.addrs = addrs;
        }

        @Override
        public int getCount() {
            return addrs.size();
        }

        @Override
        public Object getItem(int i) {
            return addrs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Holder holder;
            final Addr addr = (Addr) getItem(i);
            if (view == null) {
                holder = new Holder();
                view = getLayoutInflater().inflate(R.layout.addr_item, null);
                FontUtil.applyFont(getApplicationContext(), view, "OpenSans-Regular.ttf");
                holder.name = (TextView) view.findViewById(R.id.text_addr_name);
                holder.phone = (TextView) view.findViewById(R.id.text_addr_phone);
                holder.addr = (TextView) view.findViewById(R.id.text_addr);
                holder.gender = (TextView) view.findViewById(R.id.text_addr_gender);
                holder.editAddrImage = (ImageButton) view.findViewById(R.id.image_edit_addr);
                holder.addrParent = (LinearLayout) view.findViewById(R.id.addr_parent);
                holder.editAddrParent = (LinearLayout) view.findViewById(R.id.edit_addr_parent);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.name.setText(addr.getContact());
            holder.phone.setText(addr.getMobile());

            if (addr.getDefaultFlag() == DEFALUT_ADDR) {
                holder.addr.setText("[默认]"+addr.getAddress() + addr.getAddressDetail());
                holder.name.setTextColor(getResources().getColor(R.color.white));
                holder.addr.setTextColor(getResources().getColor(R.color.white));
                holder.gender.setTextColor(getResources().getColor(R.color.white));
                holder.phone.setTextColor(getResources().getColor(R.color.white));
                holder.addrParent.setBackgroundResource(R.drawable.blue_cornor);
                holder.editAddrImage.setImageResource(R.mipmap.icon_edit_addr_white);
            } else {
                holder.addr.setText(addr.getAddress() + addr.getAddressDetail());
                holder.addrParent.setBackgroundResource(R.drawable.white_shape);
                holder.editAddrImage.setImageResource(R.mipmap.icon_edit_addr);
                holder.name.setTextColor(Color.parseColor("#111D2D"));
                holder.addr.setTextColor(getResources().getColor(R.color.common_title));
                holder.gender.setTextColor(getResources().getColor(R.color.common_title));
                holder.phone.setTextColor(getResources().getColor(R.color.common_title));
            }
            if (addr.getGender() == Const.MAN) {
                holder.gender.setText(R.string._man);
            } else if (addr.getGender() == Const.WOMAN) {
                holder.gender.setText(R.string._woman);
            }
            holder.editAddrImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddrActivity.this, AddAddrActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("operation", OPERATION_EDIT_ADDR);
                    intent.putExtra("addr", JsonParser.objectToJsonStr(addr));
                    startActivityForResult(intent, REQUEST_ADD_ADDR);
                }
            });

            holder.editAddrParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddrActivity.this, AddAddrActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("operation", OPERATION_EDIT_ADDR);
                    intent.putExtra("addr", JsonParser.objectToJsonStr(addr));
                    startActivityForResult(intent, REQUEST_ADD_ADDR);
                }
            });

            return view;
        }

        class Holder {
            public TextView name;
            public TextView phone;
            public TextView addr;
            public ImageButton editAddrImage;
            public TextView gender;
            public LinearLayout editAddrParent;
            public LinearLayout addrParent;
        }
    }

    private int REQUEST_ADD_ADDR = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_ADDR) {
            if (resultCode == RESULT_OK) {
                LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                searchAddrVolley.requestGet(Const.Request.search, getHandler(), UserInfosPref.getInstance(this).getUser().getToken()
                        ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
            }
        }
    }
}
