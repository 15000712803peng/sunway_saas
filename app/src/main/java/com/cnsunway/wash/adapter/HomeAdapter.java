package com.cnsunway.wash.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.EvaluateActivity;
import com.cnsunway.wash.activity.GetPayOrderActivity;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.BottomListDialog;
import com.cnsunway.wash.dialog.CancelOrderDialog;
import com.cnsunway.wash.dialog.WayOfShareDialog;
import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.util.ShareUtil;
import com.cnsunway.wash.view.OrderStatusView;
import com.cnsunway.wash.viewmodel.HomeViewModel;
import com.cnsunway.wash.viewmodel.OrderCircle;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by peter on 16/3/21.
 */
public class HomeAdapter extends BaseAdapter {
    private Activity activity;
    private Fragment fragment;
    private List<Order> orderList;
    private final static int TAG_ORDER_CANCEL = 0;
    private final static int TAG_ORDER_PAY = 1;
    private final static int TAG_ORDER_SHARE = 2;
    private final static int TAG_ORDER_EVAL = 3;
    private final static int TAG_CALL_DILIVERMAN = 4;
    private final static int TAG_CALL_CUSTOMER = 5;
    private final String[] titlesAll = new String[]{"取消订单","支付","分享有礼","收衣评价","呼叫取送员","呼叫客服","分享有礼"};
    public static final int OPERATION_ORDER_PAY = 2;

    public HomeAdapter(Fragment frag, List<Order> lists) {

        orderList = lists;
        fragment = frag;
        activity = fragment.getActivity();
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (orderList != null) {
            return orderList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (orderList != null && position >= 0 && position < orderList.size()) {
            return orderList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(activity, R.layout.layout_home, null);
            ViewHolder holder = new ViewHolder(convertView);
            FontUtil.applyFont(activity, convertView, "OpenSans-Regular.ttf");
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder)convertView.getTag();
        Order order = (Order)getItem(position);
        order.setIndex(position);
        holder.orderStatus.setOrder(order);
        holder.imageStatus.setOrderPoint(order);
        holder.tvTime.setText(HomeViewModel.getOrderTimeForHome(order));
        holder.tvStatus.setText(HomeViewModel.getOrderStatus(order));

        holder.textAction.setVisibility(View.GONE);
        if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_FETCH){
            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_green);
        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_WAITPAY){
            holder.textAction.setText("立即支付");
            holder.textAction.setVisibility(View.VISIBLE);
            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_orange);
            holder.textAction.setBackgroundResource(R.drawable.orange_shape);
        } if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_WASH){
            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_yellow);
        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_SENDBACK){
            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_black);
        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_ARRIVED){
            holder.textAction.setText("确认收衣并评价");
            holder.textAction.setVisibility(View.VISIBLE);
            holder.textAction.setBackgroundResource(R.drawable.black_shape);
            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_black);
        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_DONE){
            holder.textAction.setBackgroundResource(R.drawable.yellow_shape);
            holder.imageArrow.setImageResource(R.drawable.home_ic_complete);
        }
        holder.btnMore.setTag(order);
        holder.btnMore.setOnClickListener(moreClick);
        holder.textAction.setTag(order);
        holder.textAction.setOnClickListener(actionClick);
        //更新虚状态以及alarm
        HomeViewModel.getOrderSubProgress(order);
        return convertView;
    }
    private View.OnClickListener moreClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            final Order order = (Order)v.getTag();
            OrderCircle status = HomeViewModel.getVirtualOrderStatus(order);
            final BottomListDialog dialog = new BottomListDialog(activity);
            int[] tags = null;
            switch (status){
                case CIRCLE_NEW:
                    tags = new int[]{TAG_ORDER_CANCEL};
                    break;
                case CIRCLE_ACCEPTED:
                case CIRCLE_ONDOORING:
                case CIRCLE_WAITPAY:
                    tags = new int[]{TAG_ORDER_CANCEL,TAG_CALL_DILIVERMAN};
                    break;
                case CIRCLE_PAIED:
                case CIRCLE_PICKED:
                    if(order.getType() == 2){
                        tags = new int[]{TAG_CALL_DILIVERMAN};
                    }else {
                        if(order.getAction().equals("share")|| order.getAction().equals("direct")){
                            tags = new int[]{TAG_ORDER_SHARE, TAG_CALL_CUSTOMER};
                        }else {
                            tags = new int[]{TAG_CALL_CUSTOMER};
                        }
                    }
                    break;
                case CIRCLE_INSTORE:
                case CIRCLE_WASHING:
                case CIRCLE_DISINFECTING:
                case CIRCLE_CARING:
                case CIRCLE_QUALIFYING:
                case CIRCLE_PACKING:
                case CIRCLE_OUTSTORE:
                    if(order.getType() == 2){
                        tags = new int[]{TAG_CALL_CUSTOMER};
                    }else {
                        if(order.getAction().equals("share")|| order.getAction().equals("direct")){
                            tags = new int[]{TAG_ORDER_SHARE, TAG_CALL_CUSTOMER};
                        }else {
                            tags = new int[]{TAG_CALL_CUSTOMER};
                        }

                    }
                    break;
                case CIRCLE_WAYBACK:
                case CIRCLE_ARRIVED:
                    if(order.getType() == 2){
                        tags = new int[]{ TAG_CALL_DILIVERMAN};
                    }else {
                        if(order.getAction().equals("share")|| order.getAction().equals("direct")){
                            tags = new int[]{TAG_ORDER_SHARE, TAG_CALL_CUSTOMER};
                        }else {
                            tags = new int[]{TAG_CALL_CUSTOMER};
                        }
                    }
                    break;
                case CIRCLE_DONE:
                    if(order.getType() == 2){
                        tags = new int[]{TAG_CALL_CUSTOMER};
                    }else {
                        if(order.getAction().equals("share")|| order.getAction().equals("direct")){
                            tags = new int[]{TAG_ORDER_SHARE, TAG_CALL_CUSTOMER};
                        }else {
                            tags = new int[]{TAG_CALL_CUSTOMER};
                        }
                    }
                    break;

            }
            if(tags == null){
                return;
            }
            String[] titles = new String[tags.length];
            for(int i=0;i<tags.length;i++){
                if(tags[i] == TAG_ORDER_SHARE){
                    if(order.getShareInfo() != null){
                        titles[i] = order.getShareInfo().getShareBtnText();//order.getShareInfo().btnTitle
                    }else if(order.getDirectInfo() != null){
                        titles[i] = order.getDirectInfo().getBtnText();
                    }
                }else {
                    titles[i] = titlesAll[tags[i]];
                }
            }
            dialog.builder(titles,tags,createOrderMenuListener(order,dialog));
            dialog.show();
        }
    };
    private View.OnClickListener actionClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Order order = (Order)v.getTag();
            if(order.getStatus() == Order.STATUS_TO_PICK) {
                Intent intent = new Intent(activity, GetPayOrderActivity.class);
                intent.putExtra("order_no", order.getOrderNo());
                intent.putExtra("order_price", order.getTotalPrice());
                intent.putExtra("order", order);
                intent.putExtra("order_index",order.getIndex());
                fragment.startActivityForResult(intent,OPERATION_ORDER_PAY);
            }else{
                Intent intent = new Intent(activity, EvaluateActivity.class);
                intent.putExtra("order_no", order.getOrderNo());
                intent.putExtra("sender_name", order.getPickerName());
                activity.startActivity(intent);
            }
        }
    };


    static class ViewHolder {
        @Bind(R.id.image_arrow)
        ImageView imageArrow;
        @Bind(R.id.image_status)
        OrderStatusView imageStatus;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_time_tips)
        TextView tvTimeTips;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.btn_more)
        ImageButton btnMore;
        @Bind(R.id.order_status)
        OrderStatusView orderStatus;
        @Bind(R.id.text_action)
        TextView textAction;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public abstract class OrderListener implements AdapterView.OnItemClickListener,CancelOrderDialog.OnCancelOrderOkLinstener {
        private Order order;
        public OrderListener(Order order){
            this.order = order;
        }

        public Order getOrder(){
            return order;
        }
        public void cancelOk() {
            activity.sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_HOME_ORDERS));
        }
    }
    private OrderListener createOrderMenuListener(final Order order,final BottomListDialog dialog){
            return new OrderListener(order){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch ((int)id){
                    case TAG_ORDER_CANCEL: {
                        CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(activity).builder();
                        cancelOrderDialog.setOrderNo(getOrder().getOrderNo());
                        cancelOrderDialog.setCancelOkLinstener(this);
                        cancelOrderDialog.show();
                    }
                break;
                    case TAG_ORDER_PAY: {
                        Intent intent = new Intent(activity, GetPayOrderActivity.class);
                        intent.putExtra("order_no", order.getOrderNo());
                        intent.putExtra("order", order);
                        intent.putExtra("order_price", order.getTotalPrice());
                        fragment.startActivityForResult(intent,OPERATION_ORDER_PAY);
                    }
                        break;
                    case TAG_ORDER_EVAL: {
                        Intent intent = new Intent(activity, EvaluateActivity.class);
                        intent.putExtra("order_no", order.getOrderNo());
                        intent.putExtra("site_name",order.getSiteName());
                        activity.startActivityForResult(intent, 0);
                    }
                        break;
                    case TAG_CALL_DILIVERMAN: {
                        if(order.getPickerMobile() == null){
                            return;
                        }
                        new AlertDialog.Builder(activity).setTitle("拨打取送员")
                                .setMessage("确定拨打电话："+order.getPickerMobile() +" 吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(order.getDelivererMobile()));
                                        activity.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                    }
                        break;
                    case TAG_CALL_CUSTOMER:
                        new AlertDialog.Builder(activity).setTitle("客服热线")
                                .setMessage("确定拨打电话：4009-210-682 吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4009-210-682"));
                                        activity.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                        break;
                    case TAG_ORDER_SHARE: {
                        WayOfShareDialog wayOfShareDialog = new WayOfShareDialog(activity).builder();
                        new ShareUtil(wayOfShareDialog).share(activity, order);
                    }
                        break;
                }

                dialog.cancel();
            }
        };
    }
}
