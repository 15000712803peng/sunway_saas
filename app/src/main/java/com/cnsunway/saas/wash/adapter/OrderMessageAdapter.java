package com.cnsunway.saas.wash.adapter;

import android.app.Activity;
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

import java.util.List;

/**
 * Created by hp on 2017/6/22.
 */
public class OrderMessageAdapter extends BaseAdapter  {
    List<AllMessage> allMessageList;
    private Activity activity;
    private Fragment fragment;
    CallHotlineDialog callHotlineDialog;
    public List<AllMessage> getAllMessageList() {
        return allMessageList;
    }

    public void setAllMessageList(List<AllMessage> allMessageList) {
        this.allMessageList = allMessageList;
    }

    public OrderMessageAdapter(Fragment frag, List<AllMessage> lists) {
        fragment = frag;
        activity = fragment.getActivity();
        this.allMessageList = lists;
    }

    @Override
    public int getCount() {
        return allMessageList.size();
    }

    public void clear(){
        if(allMessageList != null){
            allMessageList.clear();
        }
    }

    @Override
    public Object getItem(int position) {
        return allMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final AllMessage message = (AllMessage) getItem(position);
        final View view;

        Holder holder;
        if (convertView == null) {
            view = View.inflate(activity, R.layout.order_message_item, null);
            setViewHolder(view);
        }else {
            view = convertView;
        }
        holder = (Holder) view.getTag();
        holder.num.setText(message.getContent());
        holder.time.setText(message.getPushDate());
        if(message == null || message.getTitle() == null){

        }else {
            holder.state.setText(message.getTitle());
            if (message.getTitle().contains("已接单")){
                holder.msg.setText("您的订单已经被接单，请稍等片刻取送员正在火速赶来~");
                holder.tel.setVisibility(View.GONE);
            }else if (message.getTitle().contains("支付成功")){
                holder.msg.setText("我们已经收到您的洗涤费用，开始为您检查衣物，请耐心等待~");
                holder.tel.setVisibility(View.GONE);
            }else if (message.getTitle().contains("送返中")){
                holder.msg.setText("您的衣服已洗涤完成，取送员正在送返中~");
                holder.tel.setVisibility(View.GONE);
            }else if (message.getTitle().contains("已送达")){
                holder.msg.setText("您的衣服已送达\n如有疑问请联系客服:");
                holder.tel.setVisibility(View.VISIBLE);
            }else if (message.getTitle().contains("已完成")){
                holder.msg.setText("太棒了您的订单已完成了，您可以对我们的服务质量进行评价，您的反馈是我们不断进步的动力");
                holder.tel.setVisibility(View.GONE);
            }
        }


        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHotlineDialog = new CallHotlineDialog(activity).builder();
                callHotlineDialog.show();
            }
        });
        if (message.getIsRead() == 0){
            holder.now.setVisibility(View.VISIBLE);
        }else if(message.getIsRead() == 1){
            holder.now.setVisibility(View.GONE);
        }

        String time = message.getPushDate();
        String[] date = time.split(" ");
        String s = date[0];
        holder.timeTitle.setText(s);
        if (position>0){
            if(s.equals(allMessageList.get(position-1).getPushDate().split(" ")[0])){
                holder.timeTitle.setVisibility(View.GONE);
            }else {
                holder.timeTitle.setVisibility(View.VISIBLE);
            }
        }else {
            holder.timeTitle.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void setViewHolder(View view) {
        Holder holder = new Holder();
        holder.state = (TextView) view.findViewById(R.id.tv_order_state);
        holder.now = (TextView) view.findViewById(R.id.tv_message_new);
        holder.num = (TextView) view.findViewById(R.id.tv_order_number);
        holder.msg = (TextView) view.findViewById(R.id.tv_order_msg);
        holder.tel = (LinearLayout) view.findViewById(R.id.ll_message_tel);
        holder.time = (TextView) view.findViewById(R.id.tv_order_time);
        holder.arrow = (ImageView) view.findViewById(R.id.iv_order_arrow);
        holder.timeTitle = (TextView) view.findViewById(R.id.tv_time);
        view.setTag(holder);
    }
    private class Holder {
        TextView state,now,num,msg,time,timeTitle;
        LinearLayout tel;
        ImageView arrow;
    }
}
