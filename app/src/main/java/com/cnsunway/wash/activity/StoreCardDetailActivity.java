package com.cnsunway.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.framework.utils.NumberUtil;
import com.cnsunway.wash.model.BalanceDetail;
import com.cnsunway.wash.model.BalanceItemParent;
import com.cnsunway.wash.model.BanlanceItemCategory;
import com.cnsunway.wash.model.CardConsumeItem;
import com.cnsunway.wash.model.DayBalanceDetail;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.PagerBanlanceDetail;
import com.cnsunway.wash.resp.CardConsumeResp;
import com.cnsunway.wash.resp.GetBanlanceLogResp;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LL on 2016/3/25.
 */

public class StoreCardDetailActivity extends LoadingActivity implements XListView.IXListViewListener {
    XListView banlanceList;
    TextView titleText;
    List<CardConsumeItem> detailItems = new ArrayList<>();
    JsonVolley getBanlanceDetailVolley;
    LinearLayout topParent;
    LinearLayout layoutBalanceDetail;
    int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_consume_detail);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutBalanceDetail, "OpenSans-Regular.ttf");
    }

    @Override
    protected void initData() {
        String cardId = getIntent().getStringExtra("cardId");
        getBanlanceDetailVolley = new JsonVolley(this, Const.Message.MSG_GET_BANLANCE_LOG_SUCC, Const.Message.MSG_GET_BANLANCE_LOG_FAIL);
        getBanlanceDetailVolley.addParams("cardID",cardId);
        getBanlanceDetailVolley.addParams("userMobile",UserInfosPref.getInstance(this).getUser().getMobile());
    }

    @Override
    protected void initViews() {
        layoutBalanceDetail = (LinearLayout) findViewById(R.id.ll_balance_detail);
        banlanceList = (XListView) findViewById(R.id.list_balance);
        banlanceList.setPullLoadEnable(false);
        banlanceList.setPullRefreshEnable(false);
        topParent = (LinearLayout) findViewById(R.id.ll_top);
        titleText = (TextView) findViewById(R.id.text_title);
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        getBanlanceDetailVolley.requestPost(Const.Request.cardConsumeDetail,getHandler(),UserInfosPref.getInstance(this).getUser().getToken()
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

        titleText.setText("消费详情");
//        getBanlanceDetailVolley.addParams("start", start);
//        getBanlanceDetailVolley.addParams("days", dayRanges);
//        getBanlanceDetailVolley.requestGet(Const.Request.balancelog, getHandler(), UserInfosPref.getInstance(this).getUser().getToken());
        showCenterLoading();

    }

    int totalDays = 0;
    int dayRanges = 5;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_GET_BANLANCE_LOG_SUCC:
                hideCenterLoading();
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    CardConsumeResp resp = (CardConsumeResp) JsonParser.jsonToObject(msg.obj + "", CardConsumeResp.class);
                    List<CardConsumeItem> items = resp.getData();
                    if (items != null && items.size() > 0) {
                        addPagerBalanceDataToAdapter(items);
                    } else {

                        showNoData("没有消费明细哦");
                    }

                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    banlanceList.stopLoadMore();
                }

                break;

            case Const.Message.MSG_GET_BANLANCE_LOG_FAIL:
                showNetFail();
                break;
        }

    }

    public class ConsumerAdapter extends BaseAdapter{
        List<CardConsumeItem> items;
        public ConsumerAdapter(List<CardConsumeItem> items){
            this.items = items;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CardConsumeItem item = (CardConsumeItem) getItem(i);
            Holder holder;
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.consume_item,null);
                holder = new Holder();
                holder.chargeTime = (TextView) view.findViewById(R.id.text_consume_time);
                holder.consumeAmmout = (TextView) view.findViewById(R.id.text_banlance_change);
                holder.balance = (TextView) view.findViewById(R.id.text_current_banlance);
                view.setTag(holder);
            }else {
                holder = (Holder) view.getTag();
            }
            holder.chargeTime.setText(item.getPayDate());
            holder.consumeAmmout.setText(item.getChangeAmount());
            holder.balance.setText("余额："+ item.getPostBalance());
            return view;
        }
        class Holder{
            TextView consumeAmmout;
            TextView balance;
            TextView chargeTime;
        }
    }

    private void addPagerBalanceDataToAdapter(List<CardConsumeItem> items) {
        topParent.setVisibility(View.VISIBLE);
        banlanceList.setVisibility(View.VISIBLE);
        banlanceList.setAdapter(new ConsumerAdapter(items));

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public class BalanceDetailAdapter extends BaseAdapter {

        private final int CATEGORY_YEAR = 1;
        private final int CATEGORY_ITME = 2;
        List<BalanceItemParent> items;

        public BalanceDetailAdapter(List<BalanceItemParent> items) {
            this.items = items;
        }

        public List<BalanceItemParent> getItems() {
            return items;
        }

        public void setItems(List<BalanceItemParent> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {

            if (items.get(position) instanceof BanlanceItemCategory) {
                return CATEGORY_YEAR;
            } else if (items.get(position) instanceof BalanceDetail) {
                return CATEGORY_ITME;
            }
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            Holder holder = null;
            Holder2 holder2 = null;
            BalanceItemParent itemParent = (BalanceItemParent) getItem(i);
            int type = getItemViewType(i);
            if (convertView == null) {
                switch (type) {
                    case CATEGORY_ITME:
                        holder = new Holder();
                        convertView = getLayoutInflater().inflate(R.layout.balance_item, null);
                        FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
                        holder.desText = (TextView) convertView.findViewById(R.id.text_banlance_desc);
                        holder.balanceText = (TextView) convertView.findViewById(R.id.text_current_banlance);
                        holder.balanceChangeText = (TextView) convertView.findViewById(R.id.text_banlance_change);
                        holder.rechargeTypeText = (TextView) convertView.findViewById(R.id.text_recharge_type);
                        convertView.setTag(holder);
                        break;
                    case CATEGORY_YEAR:
                        holder2 = new Holder2();
                        convertView = getLayoutInflater().inflate(R.layout.balance_category, null);
                        FontUtil.applyFont(getApplicationContext(), convertView, "OpenSans-Regular.ttf");
                        holder2.yearText = (TextView) convertView.findViewById(R.id.text_balance_catogery);
                        convertView.setTag(holder2);
                        break;
                }
            } else {
                switch (type) {
                    case CATEGORY_ITME:
                        holder = (Holder) convertView.getTag();
                        break;
                    case CATEGORY_YEAR:
                        holder2 = (Holder2) convertView.getTag();
                        break;
                }

            }

            switch (getItemViewType(i)) {
                case CATEGORY_YEAR:
                    BanlanceItemCategory itemCategory = (BanlanceItemCategory) itemParent;
                    holder2.yearText.setText(itemCategory.getCategory());
                    break;

                case CATEGORY_ITME:
                    BalanceDetail balanceDetail = (BalanceDetail) itemParent;
                    if (!TextUtils.isEmpty(balanceDetail.getMemo())) {
                        holder.desText.setText(balanceDetail.getMemo());
                    }
//            if(item.getAction().equals(RECHARGE)){
//                changeAmmout = "+" + NumberUtil.formatNumber(changeAmmout) + getString(R.string.yuan);
//                if(item.getRechargeChannel().equals(ALIPAY_RECHARGE)){
//                    operationDes = getString(R.string.alipay_recharge);
//                }else if(item.getRechargeChannel().equals(WX_RECHARGE)){
//                    operationDes = getString(R.string.wepay_recharge);
//                }
//            }else if(item.getAction().equals(USED)){
//                changeAmmout = "-" + NumberUtil.formatNumber(changeAmmout) + getString(R.string.yuan);
//                operationDes = getString(R.string.balance_pay_desc);
//            }
                    if (!TextUtils.isEmpty(balanceDetail.getChangeAmountDesc())) {
                        holder.balanceChangeText.setText(balanceDetail.getChangeAmountDesc());
                    }
                    String currentAmmount = balanceDetail.getCurrentAmount();
                    if (!TextUtils.isEmpty(currentAmmount)) {
                        holder.balanceText.setText(getString(R.string.balance_tips) + NumberUtil.formatNumber(currentAmmount) + "元");
                    } else {
                        holder.balanceText.setText("");
                    }
                    if (!TextUtils.isEmpty(balanceDetail.getExtraMemo())) {
                        holder.rechargeTypeText.setVisibility(View.VISIBLE);
                        holder.rechargeTypeText.setText(balanceDetail.getExtraMemo());
                    } else {
                        holder.rechargeTypeText.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            return convertView;
        }

        public class Holder {
            public TextView desText;
            public TextView balanceText;
            public TextView balanceChangeText;
            public TextView rechargeTypeText;
        }

        public class Holder2 {
            public TextView yearText;
        }
    }

    public void back(View view) {
        finish();
    }
}
