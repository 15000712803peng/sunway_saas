package com.cnsunway.saas.wash.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.helper.HxHelper;
import com.cnsunway.saas.wash.receiver.EmptyCallBack;
import com.cnsunway.saas.wash.sharef.Preferences;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.helpdesk.easeui.util.UserUtil;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

import java.util.List;

/**
 * Created by Sunway on 2017/8/7.
 */

public class CustomMessageFragment extends BaseFragment {

    TextView lastMessageText;
    TextView countText;
    TextView timeText;
    TextView customNew;
    ImageView kefuImage;
    TextView nameText;
    LinearLayout kefuParent;
    LinearLayout hasMessagParent;
    LinearLayout noMessageParent;
    TextView toChatText;

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    protected void initFragmentDatas() {

    }

    @Override
    protected void initMyViews(View view) {

    }

    public void onRefresh() {

    }

    BroadcastReceiver hasNewMessageReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!getActivity().isFinishing()){
                onSetupView();
            }

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customNew = (TextView) getView().findViewById(R.id.tv_custom_new);
        nameText = (TextView) getView().findViewById(R.id.text_name);
        toChatText = (TextView) getView().findViewById(R.id.text_to_chat);
        toChatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HxHelper.getInstance(getActivity()).safeToChae(Const.MESSAGE_TO_DEFAULT,getActivity());
            }
        });
        kefuParent = (LinearLayout) getView().findViewById(R.id.kefu_parent);
        hasMessagParent = (LinearLayout) getView().findViewById(R.id.ll_has_message);
        noMessageParent = (LinearLayout) getView().findViewById(R.id.ll_no_message);
        kefuParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HxHelper.getInstance(getActivity()).toChat(Const.MESSAGE_TO_DEFAULT,getActivity());
                HxHelper.getInstance(getActivity()).safeToChae(Const.MESSAGE_TO_DEFAULT,getActivity());
            }
        });
        lastMessageText = (TextView) getView().findViewById(R.id.text_last_message);
        countText = (TextView) getView().findViewById(R.id.text_new_message_count);
        timeText = (TextView) getView().findViewById(R.id.tv_time);
        kefuImage = (ImageView) getView().findViewById(R.id.iv_userhead);

        if(!ChatClient.getInstance().isLoggedInBefore()){
            ChatClient.getInstance().login(UserInfosPref.getInstance(getActivity()).getUser().getMobile(), UserInfosPref.getInstance(getActivity()).getUser().getHxPwd(),new EmptyCallBack());
        }
        getActivity().registerReceiver(hasNewMessageReceiver,new IntentFilter(Const.MyFilter.FILTER_HAS_NEW_HX_MESSAGE));
        onSetupView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getView() == null){
//            setView(inflater.inflate(R.layout.custom_message_item,container,false));
            setView(inflater.inflate(R.layout.fragment_custom_message,container,false));
        }
        return getView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(hasNewMessageReceiver);
    }

    private void onSetupView(){
        if(ChatClient.getInstance().isLoggedInBefore()){
            Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
            if(conversation != null){
                List<com.hyphenate.chat.Message> msgs = conversation.getAllMessages();
                if(msgs == null || msgs.size() == 0){
                    noMessageParent.setVisibility(View.VISIBLE);
                    hasMessagParent.setVisibility(View.INVISIBLE);
                }else {
                    noMessageParent.setVisibility(View.INVISIBLE);
                    hasMessagParent.setVisibility(View.VISIBLE);
                }
            }

            com.hyphenate.chat.Message msg = conversation.getLastMessage();
            if(msg != null){
//                lastMessageText.setText(msg.get);
//                if(msg.direct() == com.hyphenate.chat.Message.Direct.RECEIVE){
                    if(msg.getType() == com.hyphenate.chat.Message.Type.TXT){
                        if(MessageHelper.getOrderInfo(msg) != null){
                            lastMessageText.setText("[订单]");
                        }else {
                            EMTextMessageBody txtBody = (EMTextMessageBody) msg.getBody();
                            String textMsg =   txtBody.getMessage().replaceAll("\\[.{2,3}\\]", "[表情]");
//                            Log.e("---------","txtBody:" +msg.getBody());
//                            Spannable span = SmileUtils.getSmiledText(getActivity(), txtBody.getMessage());
//                            lastMessageText.setText(span, TextView.BufferType.SPANNABLE);
                            lastMessageText.setText(textMsg);
                        }

                        // 设置内容

                    }else if(msg.getType() == com.hyphenate.chat.Message.Type.IMAGE){
                        lastMessageText.setText("[图片]");
                    }else if(msg.getType() == com.hyphenate.chat.Message.Type.LOCATION){
                        lastMessageText.setText("[位置]");
                    }
                if(msg.direct() == com.hyphenate.chat.Message.Direct.RECEIVE){
                    UserUtil.setAgentNickAndAvatar(getActivity(), msg,kefuImage ,null);
                    AgentInfo agentInfo = com.hyphenate.helpdesk.model.MessageHelper.getAgentInfo(msg);
                    if (agentInfo != null){

                        if (!TextUtils.isEmpty(agentInfo.getNickname())) {
                            nameText.setText(agentInfo.getNickname());
                        }
                    }else {
                        nameText.setText("赛维客服");
                    }
                }

//                }

                if(conversation.getUnreadMsgCount() > 0){
                    countText.setVisibility(View.VISIBLE);
                    countText.setText("["+ conversation.getUnreadMsgCount()+"条未读消息]");
                    customNew.setVisibility(View.VISIBLE);
                }else {
                    countText.setVisibility(View.GONE);
                    customNew.setVisibility(View.INVISIBLE);
                }
                timeText.setText(DateUtil.getMsgTime(msg.getMsgTime()));
                //---------------
            }else {
                timeText.setText(DateUtil.getMsgTime(System.currentTimeMillis()));
                lastMessageText.setText("您好！请问有什么可以帮您？");
            }
            kefuParent.setBackgroundResource(R.drawable.bg_3dp_white);
        }else {

        }
    }
}
