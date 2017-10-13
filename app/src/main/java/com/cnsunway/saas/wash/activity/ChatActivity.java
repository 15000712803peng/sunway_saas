package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.fragment.CustomChatFragment;
import com.cnsunway.saas.wash.helper.MessageHelper;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.recorder.MediaManager;
import com.hyphenate.helpdesk.easeui.ui.BaseActivity;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.util.Config;
import com.hyphenate.helpdesk.model.AgentIdentityInfo;
import com.hyphenate.helpdesk.model.QueueIdentityInfo;
import com.hyphenate.helpdesk.model.VisitorInfo;


public class ChatActivity extends BaseActivity {

    public static ChatActivity instance = null;
    private ChatFragment chatFragment;
    String toChatUsername;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.hd_activity_chat);
        instance = this;
        //IM服务号
        toChatUsername = getIntent().getExtras().getString(Config.EXTRA_SERVICE_IM_NUMBER);
        //可以直接new ChatFragment使用
        chatFragment = new CustomChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        queueIdentityInfo = getIntent().getExtras().getParcelable(Config.EXTRA_QUEUE_INFO);
        //指定客服
        agentIdentityInfo = getIntent().getExtras().getParcelable(Config.EXTRA_AGENT_INFO);
        visitorInfo = getIntent().getExtras().getParcelable(Config.EXTRA_VISITOR_INFO);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
//        if(!TextUtils.isEmpty(getIntent().getStringExtra("order_id"))){
//            sendOrderMessage(1,getIntent().getExtras().getString("order_id"));
//        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("order_id"))){
            sendOrderMessage(1,getIntent().getStringExtra("order_id"));
        }
    }



    /**
     * 发送订单消息
     *
     * 不发送则是saveMessage
     * @param selectedIndex
     */
    private void sendOrderMessage(int selectedIndex){
        Message message = Message.createTxtSendMessage(getMessageContent(selectedIndex), toChatUsername);
        message.addContent(MessageHelper.createOrderInfo(this, selectedIndex));
        ChatClient.getInstance().chatManager().saveMessage(message);
    }

    private void sendOrderMessage(final int selectedIndex,final String orderId){
        Message message = Message.createTxtSendMessage(getMessageContent(selectedIndex), toChatUsername);
//        attachMessageAttrs(message);
        sendOrderMessage(orderId);
//        ChatClient.getInstance().chatManager().saveMessage(message);
//        ChatClient.getInstance().chatManager().sendMessage(message);

    }

    protected void sendOrderMessage(String content) {
        if (content != null && content.length() > 1500){
            Toast.makeText(this, com.hyphenate.helpdesk.R.string.message_content_beyond_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        Message message = Message.createTxtSendMessage(content, toChatUsername);
        attachMessageAttrs(message);
        message.addContent(MessageHelper.createOrderInfo(this, 1,content));
        ChatClient.getInstance().chatManager().sendMessage(message);
    }
    private VisitorInfo visitorInfo;
    private AgentIdentityInfo agentIdentityInfo;
    private QueueIdentityInfo queueIdentityInfo;
    public void attachMessageAttrs(Message message){
        if (visitorInfo != null){
            message.addContent(visitorInfo);
        }
        if (queueIdentityInfo != null){
            message.addContent(queueIdentityInfo);
        }
        if (agentIdentityInfo != null){
            message.addContent(agentIdentityInfo);
        }

    }

    /**
     * 发送轨迹消息
     * @param selectedIndex
     */
    private void sendTrackMessage(int selectedIndex) {
        Message message = Message.createTxtSendMessage(getMessageContent(selectedIndex), toChatUsername);
        message.addContent(MessageHelper.createVisitorTrack(this, selectedIndex));
        ChatClient.getInstance().chatManager().sendMessage(message);
    }

    private String getMessageContent(int selectedIndex){
        switch (selectedIndex){
            case 1:
                return getResources().getString(R.string.em_example1_text);
            case 2:
                return getResources().getString(R.string.em_example2_text);
            case 3:
                return getResources().getString(R.string.em_example3_text);
            case 4:
                return getResources().getString(R.string.em_example4_text);
        }
        // 内容自己随意定义。
        return "";
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        instance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Config.EXTRA_SERVICE_IM_NUMBER);

        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (CommonUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
