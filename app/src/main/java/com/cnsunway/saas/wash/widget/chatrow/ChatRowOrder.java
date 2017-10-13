package com.cnsunway.saas.wash.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.OrderDetailActivity;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.widget.chatrow.ChatRow;
import com.hyphenate.helpdesk.model.MessageHelper;
import com.hyphenate.helpdesk.model.OrderInfo;

public class ChatRowOrder extends ChatRow {

    TextView mChatTextView;
    private TextView userNameText;
    private TextView orderNoText;

    public ChatRowOrder(Context context, Message message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);

    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == Message.Direct.RECEIVE ? R.layout.hd_row_received_message : R.layout.em_row_sent_order, this);
    }

    @Override
    protected void onFindViewById() {
        userNameText = (TextView) findViewById(R.id.text_username);
        orderNoText = (TextView) findViewById(R.id.text_order_id);
    }

    @Override
    protected void onUpdateView() {
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        if (message.direct() == Message.Direct.RECEIVE) {
            //设置内容
            mChatTextView.setText(txtBody.getMessage());
            //设置长按事件监听
            mChatTextView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    activity.startActivityForResult(new Intent(activity, ContextMenuActivity.class)
//                            .putExtra("position", position)
//                            .putExtra("type", Message.Type.TXT.ordinal()), CustomChatFragment.REQUEST_CODE_CONTEXT_MENU);
                    return true;
                }
            });
            return;
        }
        OrderInfo orderInfo = MessageHelper.getOrderInfo(message);
        if (orderInfo == null) {
            return;
        }
        if(userNameText != null){
            userNameText.setText(message.getFrom());
        }
        if(!TextUtils.isEmpty(orderInfo.getOrderTitle())){
            orderNoText.setText(Html.fromHtml("<u>" + orderInfo.getOrderTitle() + "</u>"));
        }

//        String imageUrl = orderInfo.getImageUrl();
//        if (!TextUtils.isEmpty(imageUrl)){
//            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(com.hyphenate.helpdesk.R.drawable.hd_default_image).into(mImageView);
//        }



//        ChatClient.getInstance().chatManager().reSendMessage(message);

//        mBtnSend.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (message.getStatus() == Message.Status.INPROGRESS){
////                    Toast.makeText(context, R.string.em_notice_sending, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//            }
//        });


    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        OrderInfo orderInfo = MessageHelper.getOrderInfo(message);
        if(orderInfo != null){
            intent.putExtra("order_no", orderInfo.getOrderTitle());
            intent.putExtra("is_show_contact", false);
            context.startActivity(intent);
        }

    }
}
