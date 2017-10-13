package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.StoreCard;
import com.cnsunway.saas.wash.resp.StoreCardResp;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class NapaCardActivity extends InitActivity{
    @Bind(R.id.card_list)
    ListView cardList;
    @Bind(R.id.text_title)
    TextView titleText;
    JsonVolley cardVolley;
    List<StoreCard> storeCards;
    @Bind(R.id.text_store_banlanc)
    TextView storeBanlanceText;
    @Override
    protected void initData() {


    }

    public void back(View view){
        finish();
    }

    @Override
    protected void initViews() {
        titleText.setText("加盟店卡");
        StoreCardResp resp = (StoreCardResp) JsonParser.jsonToObject(getIntent().getStringExtra("resp"),StoreCardResp.class);
        storeCards = resp.getData();
        storeBanlanceText.setText(storeCards.get(0).getSumBalance());
        cardList.setAdapter(new NapaCardAdapter(storeCards));
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StoreCard card = (StoreCard) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(NapaCardActivity.this,StoreCardDetailActivity.class);
                intent.putExtra("cardId",card.getCardID());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_CARD_LIST_SUCC:
                break;

            case Const.Message.MSG_CARD_LIST_FAIL:
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_napa_card);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

    }

    class NapaCardAdapter extends BaseAdapter{
        List<StoreCard> cards;
        public NapaCardAdapter(List<StoreCard> cards){
            this.cards = cards;
        }

        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public Object getItem(int i) {
            return cards.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            StoreCard card = (StoreCard) getItem(i);
            Holder holder;
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.napa_card_item,null);
                holder = new Holder();
                holder.moneyText = (TextView) view.findViewById(R.id.text_money);
                holder.storeText = (TextView) view.findViewById(R.id.text_store);
                view.setTag(holder);

            }else {
                holder = (Holder) view.getTag();
            }
            holder.moneyText.setText(card.getBalance());
            holder.storeText.setText(card.getStoreName());
            return view;
        }

        class Holder{
            TextView moneyText;
            TextView storeText;
        }
    }
}
