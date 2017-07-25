package com.cnsunway.wash.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cnsunway.wash.R;
import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.viewmodel.HomeViewModel;
import com.cnsunway.wash.viewmodel.OrderCircle;

import org.apache.http.client.methods.HttpOptions;

/**
 * Created by peter on 16/3/21.
 * 三大状态取件中，洗涤中，送返中
 * 16个小状态
 * public final static Integer CIRCLE_NEW = 0;
 public final static Integer CIRCLE_ACCEPTED = 1;
 public final static Integer CIRCLE_ONDOORING = 2;
 public final static Integer CIRCLE_WAITPAY = 3;
 public final static Integer CIRCLE_PAIED = 4;
 public final static Integer CIRCLE_PICKED = 5;

 public final static Integer CIRCLE_INSTORE = 6;
 public final static Integer CIRCLE_WASHING = 7;
 public final static Integer CIRCLE_DISINFECTING = 8;
 public final static Integer CIRCLE_CARING = 9;
 public final static Integer CIRCLE_QUALIFYING = 10;
 public final static Integer CIRCLE_PACKING = 11;
 public final static Integer CIRCLE_OUTSTORE = 12;

 public final static Integer CIRCLE_WAYBACK = 13;
 public final static Integer CIRCLE_ARRIVED = 14;
 public final static Integer CIRCLE_DONE = 15;
 */

public class OrderStatusView extends View {

    private Order order;

    private int status;
    private int type;
    private float angle;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_POINT = 1;
    private static final int TYPE_PROGRESS = 2;
    public static final int STATUS_FETCH    = 21;
    public static final int STATUS_WAITPAY    = 22;
    public static final int STATUS_WASH     = 23;
    public static final int STATUS_SENDBACK = 24;
    public static final int STATUS_ARRIVED = 25;
    public static final int STATUS_DONE = 26;


    public OrderStatusView(Context context) {
        super(context);
    }

    public OrderStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Order getOrder() {
        return order;
    }

    /**
     * 大状态
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
        OrderCircle status = HomeViewModel.getVirtualOrderStatus(order);
        if(order.getStatus() < Order.STATUS_IN_STORE){
//
//            if(status == OrderCircle.CIRCLE_WAITPAY){
//                setStatus(STATUS_WAITPAY);
//            }else {
//                setStatus(STATUS_FETCH);
//            }
            int s = HomeViewModel.getOrderSimpleStatus(order);
            if(s == HomeViewModel.ORDER_STUTAS_ONE || s == HomeViewModel.ORDER_STUTAS_TWO || s == HomeViewModel.ORDER_STUTAS_THREE){
                setStatus(STATUS_FETCH);
            }else if(s == HomeViewModel.ORDER_STUTAS_FOUR || s == HomeViewModel.ORDER_STUTAS_FIVE || s == HomeViewModel.ORDER_STUTAS_SIX){
                setStatus(STATUS_WAITPAY);
            }

        }else if(order.getStatus() < Order.STATUS_WAY_BACK){
            setStatus(STATUS_WASH);
        }else if(order.getStatus() == Order.STATUS_DONE){
            setStatus(STATUS_DONE);
        }else if(order.getStatus() == Order.STATUS_ARRIVED){
            setStatus(STATUS_ARRIVED);
        }else{
            setStatus(STATUS_SENDBACK);
        }
    }

    /**
     * 小状态
     * @param order
     */
    public void setOrderProgress(Order order,float angle) {
        type = TYPE_PROGRESS;
        this.angle = angle;
        setOrder(order);
        invalidate();
    }
    public void setOrderPoint(Order order) {
        type = TYPE_POINT;
        setOrder(order);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = getResources().getDisplayMetrics().density * 9;//10dp
        RectF rect = new RectF(radius/2,radius/2,getWidth()-radius,getWidth()-radius);
        Paint paint = new Paint();
        int color = 0;
        if(status == STATUS_FETCH){
            color = getResources().getColor(R.color.order_fetch);
        }if(status == STATUS_WAITPAY){
            color = getResources().getColor(R.color.orange);
        }else if(status == STATUS_WASH){
            color = getResources().getColor(R.color.order_wash);
        }else if(status == STATUS_SENDBACK || status == STATUS_ARRIVED){
            color = getResources().getColor(R.color.order_sendback);
        }else if(status == STATUS_DONE ){
            color = getResources().getColor(R.color.order_done);
        }
        paint.setColor(color);
        if(type == TYPE_POINT){
            rect = new RectF(0,0,getWidth(),getHeight());
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);  //消除锯齿
            canvas.drawArc(rect, (float) (-90), 360, false, paint);
            return;
        }else if(type == TYPE_NORMAL) {
            paint.setStyle(Paint.Style.STROKE); //设置空心
            paint.setStrokeWidth((int)(radius / 1.8)); //设置圆环的宽度
            paint.setAntiAlias(true);  //消除锯齿
            paint.setAlpha((int) (0.3 * 255));
            canvas.drawArc(rect, (float) (-90), 360, false, paint);
            paint.setAlpha((int) (255));
            if (status == STATUS_FETCH || status == STATUS_WAITPAY) {
                canvas.drawArc(rect, (float) (-90), 90, false, paint);
            } else if (status == STATUS_WASH) {
                canvas.drawArc(rect, (float) (-90), 180, false, paint);
            } else if (status == STATUS_SENDBACK || status == STATUS_ARRIVED) {
                canvas.drawArc(rect, (float) (-90), 270, false, paint);
            } else if (status == STATUS_DONE) {
                canvas.drawArc(rect, (float) (-90), 360, false, paint);
            }
        }else{
            paint.setColor(getResources().getColor(R.color.order_sendback));
            paint.setAntiAlias(true);  //消除锯齿
            rect = new RectF(getWidth()/4,getWidth()/4,getWidth()*3/4,getHeight()*3/4);
            paint.setStyle(Paint.Style.STROKE); //设置空心
            paint.setStrokeWidth((int)(getWidth() / 3.8)); //设置圆环的宽度
            paint.setAlpha((int) (0.3 * 255));
            canvas.drawArc(rect, (float) (-90), 360, false, paint);

            paint.setAlpha((int) (1.0 * 255));
            canvas.drawArc(rect, (float) (-90), this.angle*360, false, paint);
        }
    }
}
