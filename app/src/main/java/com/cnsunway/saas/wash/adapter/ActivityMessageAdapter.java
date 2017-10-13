package com.cnsunway.saas.wash.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.dialog.CallHotlineDialog;
import com.cnsunway.saas.wash.model.AllMessage;
import com.cnsunway.saas.wash.model.NewActivityMessage;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


/**
 * Created by hp on 2017/6/22.
 */
public class ActivityMessageAdapter extends BaseAdapter {
    private Activity activity;
    private Fragment fragment;
    List<AllMessage> messagesList;
    CallHotlineDialog callHotlineDialog;
    final  int TYPE_DATE = 1;//时间
    final  int TYPE_CHARGE = 2;//21充值成功
    final  int TYPE_COUPONS = 3;//22优惠券过期提醒
    final  int TYEP_NEW_ACTIVITY = 4; //23新活动上线提醒
    /*DateHolder dateHolder;
    CharegeMessageHolder charegeMessageHolder;
    CouponMessageHolder couponMessageHolder;*/
    NewActivityMessage newActivityMessage;
    ImageLoader imageLoader;
    RoundedImageView imgHeadPortrait;
    public ActivityMessageAdapter(Fragment frag, List<AllMessage> lists) {
        fragment = frag;
        this.messagesList = lists;
        activity = fragment.getActivity();

    }

    public void setMessagesList(List<AllMessage> messagesList) {
        this.messagesList = messagesList;
    }

    public List<AllMessage> getMessagesList() {
        return messagesList;
    }
    public void clear(){
        if(messagesList != null){
            messagesList.clear();
        }
    }
    @Override
    public int getCount() {

        return messagesList.size();

    }

    @Override
    public Object getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /*@Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof String ){
            return TYPE_DATE;
        }else if(messagesList.get(position).getMsgType() == 23){
            return TYEP_NEW_ACTIVITY;
        }else if(messagesList.get(position).getMsgType() == 22){
            return TYPE_COUPONS;
        }else if(messagesList.get(position).getMsgType() == 21){
            return TYPE_CHARGE;
        }
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        return 5;
    }*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AllMessage message = (AllMessage) getItem(position);
        final View view;
        Holder holder;
        /*int type = getItemViewType(position);
        if (type == TYPE_DATE ) {
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.text_date, null);
                dateHolder = new DateHolder();
                dateHolder.date = (TextView) convertView.findViewById(R.id.text_date);
                convertView.setTag(dateHolder);
            } else {
                dateHolder = (DateHolder) convertView.getTag();
            }
        }else if(type == TYPE_CHARGE){
            if(convertView == null){
                convertView = View.inflate(activity,R.layout.recharge_message_item,null);
                charegeMessageHolder = new CharegeMessageHolder();
                convertView.setTag(charegeMessageHolder);
            }else {
                charegeMessageHolder = (CharegeMessageHolder) convertView.getTag();
            }
        }else if(type == TYPE_COUPONS){
            if(convertView == null){
                convertView =  View.inflate(activity,R.layout.coupon_message_item,null);
                couponMessageHolder = new CouponMessageHolder();
                convertView.setTag(couponMessageHolder);
            }else {
                couponMessageHolder = (CouponMessageHolder) convertView.getTag();
            }
        }else if(type == TYEP_NEW_ACTIVITY){
            if(convertView == null){
                convertView = View.inflate(activity,R.layout.new_activity_item,null);
                newActivityMessage = new NewActivityMessage();
//                messageHolder.status = (TextView) convertView.findViewById(R.id.text_date);
                convertView.setTag(newActivityMessage);
            }else {
                newActivityMessage = (NewActivityMessage) convertView.getTag();
            }
        }
         return convertView;
*/

     //============================================
       if (convertView == null){
            view = View.inflate(activity,R.layout.activity_message_item,null);
            setViewHolder(view);
        }else {
            view = convertView;
        }
        holder = (Holder) view.getTag();
        holder.title.setText(message.getTitle());
        if (message.getMsgType() == 21){
            //21充值成功
            holder.userMsgLL.setVisibility(View.VISIBLE);
            holder.activityMsgLL.setVisibility(View.GONE);
            holder.title.setText(message.getTitle());
            holder.user.setText(message.getContent());
            holder.userMsgTV.setText("您已经充值成功\n如遇到余额未及时到账,请致电客服:");
            holder.tel.setVisibility(View.VISIBLE);
            holder.tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callHotlineDialog = new CallHotlineDialog(activity).builder();
                    callHotlineDialog.show();
                }
            });
            holder.keyValue.setVisibility(View.GONE);
            holder.key1.setText("充值金额:");
            holder.key2.setText("赠送金额:");
            holder.key3.setText("当前余额:");
            holder.value1.setText(message.getRechargeAmount()+"元");
            holder.value2.setText(message.getGiveAmount()+"元");
            holder.value3.setText(message.getNowAmount()+"元");
            holder.value3.setTextColor(Color.parseColor("#F8451B"));
            holder.detail.setText("账单明细");

        }else if (message.getMsgType() == 22){
            //22优惠券过期提醒
            holder.userMsgLL.setVisibility(View.VISIBLE);
            holder.activityMsgLL.setVisibility(View.GONE);
            holder.title.setText(message.getTitle());
//            holder.user.setText(message.getUserID());
            holder.user.setText(message.getContent());
            holder.userMsgTV.setText("您的优惠券即将过期，请尽快使用");
            holder.tel.setVisibility(View.GONE);
            holder.keyValue.setVisibility(View.VISIBLE);
            holder.key1.setText("名称:");
            holder.key2.setText("金额:");
            holder.key3.setText("说明:");
            holder.key4.setText("有效期:");
            holder.value1.setText(message.getCouponName());
            holder.value2.setText(message.getCouponAmount()+"元");
            holder.value3.setText(message.getCouponDescription());
            holder.value4.setText(message.getCouponDate());
            holder.detail.setText("查看优惠券");
            holder.value3.setTextColor(Color.parseColor("#1F1F21"));

        }else if (message.getMsgType() == 23){
            //23新活动上线提醒
            holder.userMsgLL.setVisibility(View.GONE);
            holder.activityMsgLL.setVisibility(View.VISIBLE);
            holder.title.setText(message.getTitle());

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));

            imageLoader.displayImage(message.getPicUrl(),holder.activityImg);

            holder.activityMsgTV.setText(message.getContent());
            holder.detail.setText("活动详情");
        }
        String time = message.getPushDate();
        String[] date = time.split(" ");
        String s = date[0];
        holder.timeTitle.setText(s);
        if (position>0){
            if(s.equals(messagesList.get(position-1).getPushDate().split(" ")[0])){
                holder.timeTitle.setVisibility(View.GONE);
            }else {
                holder.timeTitle.setVisibility(View.VISIBLE);
            }
        }else {
            holder.timeTitle.setVisibility(View.VISIBLE);
        }

        if (message.getIsRead() == 0){
            holder.now.setVisibility(View.VISIBLE);
        }else  if (message.getIsRead() == 1){
            holder.now.setVisibility(View.GONE);
        }

        return view;


    }

    private void setViewHolder(View view) {
        Holder holder = new Holder();
        holder.title = (TextView) view.findViewById(R.id.tv_activity_title);
        holder.now = (TextView) view.findViewById(R.id.tv_message_new);
        holder.user = (TextView) view.findViewById(R.id.tv_activity_user);
        holder.userMsgTV = (TextView) view.findViewById(R.id.tv_user_msg);
        holder.tel = (LinearLayout) view.findViewById(R.id.ll_message_tel);
        holder.key1 = (TextView) view.findViewById(R.id.tv_key1);
        holder.key2 = (TextView) view.findViewById(R.id.tv_key2);
        holder.key3 = (TextView) view.findViewById(R.id.tv_key3);
        holder.key4 = (TextView) view.findViewById(R.id.tv_key4);
        holder.value1 = (TextView) view.findViewById(R.id.tv_value1);
        holder.value2 = (TextView) view.findViewById(R.id.tv_value2);
        holder.value3 = (TextView) view.findViewById(R.id.tv_value3);
        holder.value4 = (TextView) view.findViewById(R.id.tv_value4);
        holder.keyValue = (LinearLayout) view.findViewById(R.id.ll_key_value);
        holder.detail = (TextView) view.findViewById(R.id.tv_activity_detail);
        holder.arrow = (ImageView) view.findViewById(R.id.iv_order_arrow);
        holder.userMsgLL = (LinearLayout) view.findViewById(R.id.ll_user_msg);
        holder.activityMsgLL = (LinearLayout) view.findViewById(R.id.ll_activity_msg);
        holder.activityMsgTV = (TextView) view.findViewById(R.id.tv_activity_msg);
        holder.activityImg = (ImageView) view.findViewById(R.id.iv_activity_image);
        holder.timeTitle = (TextView) view.findViewById(R.id.tv_time);
        view.setTag(holder);
    }
     class Holder {
        TextView timeTitle,title, now,user, userMsgTV,key1,key2,key3,key4,value1,value2,value3,value4,detail,activityMsgTV;
        LinearLayout tel,keyValue,userMsgLL,activityMsgLL;
        ImageView arrow,activityImg;
    }
//======================================================================

   /* private class DateHolder {
        TextView date ;
    }

    private class CharegeMessageHolder {
    }

    private class CouponMessageHolder {
    }*/
}
