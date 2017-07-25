package com.cnsunway.wash.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.DoOrderActivity;
import com.cnsunway.wash.activity.EvaluateActivity;
import com.cnsunway.wash.activity.GetPayOrderActivity;
import com.cnsunway.wash.activity.OrderDetailActivity;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.BottomListDialog;
import com.cnsunway.wash.dialog.CancelOrderDialog;
import com.cnsunway.wash.dialog.WayOfShareDialog;
import com.cnsunway.wash.framework.utils.DateUtil;
import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.util.FontUtil;
import com.cnsunway.wash.util.ShareUtil;
import com.cnsunway.wash.view.OrderStatusView;
import com.cnsunway.wash.viewmodel.HomeViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by peter on 16/3/21.
 */
public class OrderAdapter extends BaseAdapter {
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

    public OrderAdapter(Fragment frag, List<Order> lists) {

        orderList = lists;
        fragment = frag;
        activity = fragment.getActivity();
    }

    public interface OnConfirmClickedListenr{
        void confirmClicked(String orderNo);
    }

    OnConfirmClickedListenr confirmClickedListenr;


    public void setConfirmClickedListenr(OnConfirmClickedListenr confirmClickedListenr) {
        this.confirmClickedListenr = confirmClickedListenr;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orders) {
//        if(this.orderList != null){
//            this.orderList.clear();
//        }
//        orderList.addAll(orders);
        this.orderList = orders;

    }

    public void clearOrders(){
        if( this.orderList != null){
            this.orderList.clear();
        }
    }

    @Override
    public int getCount() {

        return orderList.size();
    }

    @Override
    public Object getItem(int position) {

        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void fillOrderStutas(final  Order order, final ViewHolder holder){
        int stutas = HomeViewModel.getOrderSimpleStatus(order);
        holder.tvTips.setVisibility(View.VISIBLE);
        holder.operationText.setTextColor(Color.parseColor("#ffffff"));
        switch (stutas){
            case HomeViewModel.ORDER_STUTAS_ONE:
                //预约完成
                holder.imageArrow.setImageResource(R.mipmap.clock1);
                holder.dotImage.setImageResource(R.drawable.dot1);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                String appointTime = "";
                if (!TextUtils.isEmpty(order.getExpectDateB()) && !TextUtils.isEmpty(order.getExpectDateE())) {
                    appointTime = DateUtil.getServerDate(order.getExpectDateB()).substring(0, order.getExpectDateB().length() - 3) + "-" + DateUtil.getServerDate(order.getExpectDateE()).substring(order.getExpectDateE().length() - 8, order.getExpectDateE().length() - 3);
                }
                holder.tvTips.setText(activity.getString(R.string.tips_appointment_time) + appointTime);

                break;
            case HomeViewModel.ORDER_STUTAS_TWO:
                //待上门
                holder.imageArrow.setImageResource(R.mipmap.clock1);
                holder.dotImage.setImageResource(R.drawable.dot1);
                holder.tvTips.setText(activity.getString(R.string.tips_fetcher_name) + order.getPickerName()+",");
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(order.getPickerMobile())){
                    holder.phnoeText.setText(order.getPickerMobile());
                    holder.phnoeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(activity).setTitle("联系取件员")
                                    .setMessage("确定拨打取件员电话："+  order.getPickerMobile()+"吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(order.getPickerMobile()));
                                            activity.startActivity(intent);
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    });
                }

                break;
            case HomeViewModel.ORDER_STUTAS_THREE:
                //上门中
                holder.imageArrow.setImageResource(R.mipmap.clock1);
                holder.dotImage.setImageResource(R.drawable.dot1);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("计价中，请您仔细检查避免衣物中夹杂个人物品");
//
                break;
            case HomeViewModel.ORDER_STUTAS_FOUR:
                //待支付
                holder.imageArrow.setImageResource(R.mipmap.clock2);
                holder.dotImage.setImageResource(R.drawable.dot2);
                holder.operationText.setVisibility(View.VISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("件数：" + order.getClothesNum() + "件" + "    金额：" + order.getTotalPrice() + "元");
                holder.operationText.setText("立即支付");
                holder.operationText.setBackgroundResource(R.drawable.orange_shape);
//               holder.tvTips.setText();
                holder.operationText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(order.getType() == 1){
                            Intent intent = new Intent(fragment.getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", order.getOrderNo());
                            fragment.startActivityForResult(intent, 1);
                        }else if(order.getType() == 2){
                            Intent intent = new Intent(fragment.getActivity(), OrderDetailActivity.class);
                            intent.putExtra("order_no", order.getOrderNo());
                            fragment.startActivityForResult(intent, 1);
                        }
                    }
                });

                break;
            case HomeViewModel.ORDER_STUTAS_FIVE:
                //已支付
                holder.imageArrow.setImageResource(R.mipmap.clock2);
                holder.dotImage.setImageResource(R.drawable.dot2);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("件数：" + order.getClothesNum() + "件" + "    金额：" + order.getTotalPrice() + "元");
                break;
            case HomeViewModel.ORDER_STUTAS_SIX:
                //取件完成
                holder.imageArrow.setImageResource(R.mipmap.clock2);
                holder.dotImage.setImageResource(R.drawable.dot2);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("门店："+"赛维洗衣 - " + order.getSiteName());
                break;
            case HomeViewModel.ORDER_STUTAS_SEVEN:
                //入库检验
                holder.imageArrow.setImageResource(R.mipmap.clock3);
                holder.dotImage.setImageResource(R.drawable.dot3);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                break;
            case HomeViewModel.ORDER_STUTAS_EIGHT:
                //洗涤中
                holder.imageArrow.setImageResource(R.mipmap.clock3);
                holder.dotImage.setImageResource(R.drawable.dot3);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                break;
            case HomeViewModel.ORDER_STUTAS_NINE:
                //洗涤完成
                holder.imageArrow.setImageResource(R.mipmap.clock3);
                holder.dotImage.setImageResource(R.drawable.dot3);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("门店：" +"赛维洗衣 - "+ order.getSiteName());
                break;
            case HomeViewModel.ORDER_STUTAS_TEN:
                // 送返中
                holder.imageArrow.setImageResource(R.mipmap.clock4);
                holder.dotImage.setImageResource(R.drawable.dot4);
                holder.operationText.setVisibility(View.INVISIBLE);
                holder.phnoeText.setVisibility(View.VISIBLE);
                holder.tvTips.setText(activity.getString(R.string.tips_sender_name) + order.getDelivererName()+",");
                if(!TextUtils.isEmpty(order.getDelivererMobile())){
                    holder.phnoeText.setText(order.getDelivererMobile());
                    holder.phnoeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(activity).setTitle("联系送件员")
                                    .setMessage("确定拨打送件员电话："+  order.getDelivererMobile()+"吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(order.getPickerMobile()));
                                            activity.startActivity(intent);
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    });
                }
                break;
            case HomeViewModel.ORDER_STUTAS_ELEVEN:
                //已送达
                holder.imageArrow.setImageResource(R.mipmap.clock4);
                holder.dotImage.setImageResource(R.drawable.dot4);
                holder.operationText.setVisibility(View.VISIBLE);
                holder.phnoeText.setVisibility(View.GONE);
                holder.tvTips.setText("请您仔细检查衣物后确认收衣");
                holder.operationText.setBackgroundResource(R.drawable.green_shape);
                holder.operationText.setText("确认收衣");
                holder.operationText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       OperationToast.showOperationResult(activity,"confirm", Toast.LENGTH_LONG);
                        if(confirmClickedListenr != null){
                            confirmClickedListenr.confirmClicked(order.getOrderNo());
                        }
                    }
                });

                break;
            case HomeViewModel.ORDER_STUTAS_TWELVE:
                //已完成
                holder.imageArrow.setImageResource(R.mipmap.clock5);
                holder.dotImage.setImageResource(R.drawable.dot5);
                if(order.getOrderStatus() == Const.OrderStatus.ORDER_STATUS_DELIVERED){
                    holder.tvTips.setText("您的评价是我们提升服务的动力");
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.operationText.setVisibility(View.VISIBLE);
                    holder.operationText.setBackgroundResource(R.drawable.yellow_shape);
                    holder.operationText.setText("评价打分");
                    holder.operationText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, EvaluateActivity.class);
                            intent.putExtra("order_no", order.getOrderNo());
                            intent.putExtra("sender_name", order.getPickerName());
                            activity.startActivity(intent);
                        }
                    });
                    //没有评价
                }else if(order.getOrderStatus() == Const.OrderStatus.ORDER_STATUS_COMPLETED){
                    holder.tvTips.setVisibility(View.INVISIBLE);
                    holder.phnoeText.setVisibility(View.GONE);
                    holder.operationText.setVisibility(View.VISIBLE);
                    holder.operationText.setBackgroundResource(R.drawable.gray_border);
                    holder.operationText.setText("再来一单");
                    holder.operationText.setTextColor(Color.parseColor("#878D94"));
//                   holder.operationText.setBackgroundResource(R.drawable.);
                    holder.operationText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(activity, DoOrderActivity.class));
                        }
                    });
                }
                break;
            default:
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Order order = (Order)getItem(position);
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = View.inflate(activity, R.layout.order_item, null);
            holder = new ViewHolder(convertView);
            FontUtil.applyFont(activity, convertView, "OpenSans-Regular.ttf");
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        order.setIndex(position);
        holder.orderStatus.setOrder(order);
//        holder.imageStatus.setOrderPoint(order);
        holder.tvTime.setText(HomeViewModel.getOrderTimeForHome2(order));
        holder.tvStatus.setText(HomeViewModel.getOrderStatus(order));
        fillOrderStutas(order,holder);
//
//        if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_FETCH){
//            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_green);
//        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_WAITPAY){
//
//            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_orange);
//
//        } if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_WASH){
//            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_yellow);
//        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_SENDBACK){
//            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_black);
//        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_ARRIVED){
//
//            holder.imageArrow.setImageResource(R.drawable.home_ic_arrow_black);
//        }else if(holder.orderStatus.getStatus() == OrderStatusView.STATUS_DONE){
//
//            holder.imageArrow.setImageResource(R.drawable.home_ic_complete);
//        }
//        //更新虚状态以及alarm
//        HomeViewModel.getOrderSubProgress(order);
        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.image_arrow)
        ImageView imageArrow;
        @Bind(R.id.image_dot)
        ImageView dotImage;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_tips)
        TextView tvTips;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.order_status)
        OrderStatusView orderStatus;
        @Bind(R.id.tv_operation)
        TextView operationText;
        @Bind(R.id.tv_phone)
        TextView phnoeText;
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
