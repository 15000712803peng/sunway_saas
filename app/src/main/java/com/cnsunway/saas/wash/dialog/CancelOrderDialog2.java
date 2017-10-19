package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.model.BoxReason;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/7.
 */
public class CancelOrderDialog2 implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    View container;
    Display display;
    Dialog dialog;
    View view;
    TextView okBtn, cancelBtn;
    String orderNo;
    JsonVolley orderCancelVolley;
    Handler handler;
    LinearLayout reasonParent1, reasonParent2, reasonParent3, reasonParent4,reasonParent5, reasonParent6, reasonParent7, reasonParent8;
    CheckBox reasonBox1, reasonBox2, reasonBox3,reasonBox4, reasonBox5, reasonBox6,reasonBox7,reasonBox8;
    EditText reasonEdit;
    BoxReason lastReason;
    ArrayList<BoxReason> reasonBoxes = new ArrayList<>();


    public interface OnCancelOrderOkLinstener {
        public void cancelOk();
    }

    OnCancelOrderOkLinstener cancelOkLinstener;

    public void setCancelOkLinstener(OnCancelOrderOkLinstener cancelOkLinstener) {
        this.cancelOkLinstener = cancelOkLinstener;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public CancelOrderDialog2 builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel_order3, null);
        container = view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        cancelBtn = (TextView) container.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        reasonParent1 = (LinearLayout) container.findViewById(R.id.ll_reason1);
        reasonParent1.setOnClickListener(this);
        reasonParent2 = (LinearLayout) container.findViewById(R.id.ll_reason2);
        reasonParent2.setOnClickListener(this);
        reasonParent3 = (LinearLayout) container.findViewById(R.id.ll_reason3);
        reasonParent3.setOnClickListener(this);

        reasonParent4 = (LinearLayout) container.findViewById(R.id.ll_reason4);
        reasonParent4.setOnClickListener(this);
        reasonParent5 = (LinearLayout) container.findViewById(R.id.ll_reason5);
        reasonParent5.setOnClickListener(this);
        reasonParent6 = (LinearLayout) container.findViewById(R.id.ll_reason6);
        reasonParent6.setOnClickListener(this);
        reasonParent7 = (LinearLayout) container.findViewById(R.id.ll_reason7);
        reasonParent7.setOnClickListener(this);
        reasonParent8 = (LinearLayout) container.findViewById(R.id.ll_reason8);
        reasonParent8.setOnClickListener(this);



        reasonBox1 = (CheckBox) container.findViewById(R.id.cb_reason1);
        reasonBox1.setEnabled(false);
        reasonBox2 = (CheckBox) container.findViewById(R.id.cb_reason2);
        reasonBox2.setEnabled(false);
        reasonBox3 = (CheckBox) container.findViewById(R.id.cb_reason3);
        reasonBox3.setEnabled(false);

        reasonBox4 = (CheckBox) container.findViewById(R.id.cb_reason4);
        reasonBox4.setEnabled(false);
        reasonBox5 = (CheckBox) container.findViewById(R.id.cb_reason5);
        reasonBox5.setEnabled(false);
        reasonBox6 = (CheckBox) container.findViewById(R.id.cb_reason6);
        reasonBox6.setEnabled(false);
        reasonBox7 = (CheckBox) container.findViewById(R.id.cb_reason7);
        reasonBox7.setEnabled(false);
        reasonBox8 = (CheckBox) container.findViewById(R.id.cb_reason8);
        reasonBox8.setEnabled(false);



        lastReason = new BoxReason(reasonBox3, "");
        reasonBoxes.add(lastReason);
        reasonBoxes.add(new BoxReason(reasonBox1, context.getString(R.string.cancel_order_reason1)));
        reasonBoxes.add(new BoxReason(reasonBox2, context.getString(R.string.cancel_order_reason2)));

        reasonBoxes.add(new BoxReason(reasonBox4, context.getString(R.string.cancel_order_reason4)));
        reasonBoxes.add(new BoxReason(reasonBox5, context.getString(R.string.cancel_order_reason5)));
        reasonBoxes.add(new BoxReason(reasonBox6, context.getString(R.string.cancel_order_reason6)));
        reasonBoxes.add(new BoxReason(reasonBox7, context.getString(R.string.cancel_order_reason7)));
        reasonBoxes.add(new BoxReason(reasonBox8, context.getString(R.string.cancel_order_reason8)));

        reasonBoxes.add(lastReason);
        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        reasonEdit = (EditText) view.findViewById(R.id.et_reason);
        reasonEdit.setEnabled(false);
        okBtn.setOnClickListener(this);
        dialog.setContentView(view);
//        dialog.setCancelable(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }


    private String check() {
        String reason = reasonEdit.getText().toString().trim();
        lastReason.setReason(reason);
        boolean isSelected = false;
        for (BoxReason box : reasonBoxes) {
            CheckBox b = box.getBox();
            if (b.isChecked()) {
                return box.getReason();
            }
        }

        return reason;
    }

    @Override
    public void onClick(View view) {
        if (view == okBtn) {
            String reason = check();
            if (TextUtils.isEmpty(reason)) {
                Toast.makeText(context, context.getString(R.string.select_reason), Toast.LENGTH_LONG).show();
                return;
            }
            Log.e("reason",reason);
            orderCancelVolley.addParams("memo", reason);
            orderCancelVolley.addParams("orderNo", orderNo);
            LocationForService locationForService = UserInfosPref.getInstance(context).getLocationServer();
            orderCancelVolley.requestPost(Const.Request.cancel,new LoadingDialogInterface() {
                @Override
                public void showLoading() {
                    getLoadingDialog(context.getString(R.string.operating)).show();
                }
                @Override
                public void hideLoading() {
                    hideLoadingDialog();
                }
            },  handler, UserInfosPref.getInstance(context).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        } else if (view == cancelBtn) {
            cancel();
        } else if (view == reasonParent1) {
            reasonBox1.setChecked(!reasonBox1.isChecked());
            if (reasonBox1.isChecked()) {
                reasonEdit.setText("");
                reasonEdit.setEnabled(false);
                reasonBox2.setChecked(false);
                reasonBox3.setChecked(false);
                reasonBox4.setChecked(false);
                reasonBox5.setChecked(false);
                reasonBox6.setChecked(false);
                reasonBox7.setChecked(false);
                reasonBox8.setChecked(false);

            }
        }else if (view == reasonParent2) {
                reasonBox2.setChecked(!reasonBox2.isChecked());
                if (reasonBox2.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox3.setChecked(false);
                    reasonBox4.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox8.setChecked(false);

                }

        } else if (view == reasonParent3) {

                reasonBox3.setChecked(!reasonBox3.isChecked());
                reasonEdit.setEnabled(!reasonEdit.isEnabled());
                if (reasonBox3.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(true);
                    reasonBox2.setChecked(false);
                    reasonBox1.setChecked(false);

                    reasonBox4.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox8.setChecked(false);
                }

        }else if (view == reasonParent4) {

                reasonBox4.setChecked(!reasonBox4.isChecked());
                if (reasonBox4.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox2.setChecked(false);

                    reasonBox3.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox8.setChecked(false);

                }
        }else if (view == reasonParent5) {

                reasonBox5.setChecked(!reasonBox5.isChecked());
                if (reasonBox5.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox2.setChecked(false);

                    reasonBox3.setChecked(false);
                    reasonBox4.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox8.setChecked(false);

                }
        }else if (view == reasonParent6) {

                reasonBox6.setChecked(!reasonBox6.isChecked());
                if (reasonBox6.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox2.setChecked(false);

                    reasonBox3.setChecked(false);
                    reasonBox4.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox8.setChecked(false);

                }
        }else if (view == reasonParent7) {

                reasonBox7.setChecked(!reasonBox7.isChecked());
                if (reasonBox7.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox2.setChecked(false);

                    reasonBox3.setChecked(false);
                    reasonBox4.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox8.setChecked(false);

                }
        }else if (view == reasonParent8) {

                reasonBox8.setChecked(!reasonBox8.isChecked());
                if (reasonBox8.isChecked()) {
                    reasonEdit.setText("");
                    reasonEdit.setEnabled(false);
                    reasonBox1.setChecked(false);
                    reasonBox3.setChecked(false);

                    reasonBox4.setChecked(false);
                    reasonBox5.setChecked(false);
                    reasonBox6.setChecked(false);
                    reasonBox7.setChecked(false);
                    reasonBox2.setChecked(false);

                }
            }
        }

    public CancelOrderDialog2(final Context context) {

        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Const.Message.MSG_ORDER_CANCEL_SUCC:
                        cancel();
                        if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                            showMessageToast(context.getString(R.string.cancel_succ));
                            if (cancelOkLinstener != null) {
                                cancelOkLinstener.cancelOk();
                            }
                        } else {
                            cancel();
                            showMessageToast(context.getString(R.string.cancel_fail));
                        }

                        break;

                    case Const.Message.MSG_ORDER_CANCEL_FAIL:
                        cancel();
                        showMessageToast(context.getString(R.string.request_fail));
                        break;

                }
            }
        };
        orderCancelVolley = new JsonVolley(context, Const.Message.MSG_ORDER_CANCEL_SUCC, Const.Message.MSG_ORDER_CANCEL_SUCC);
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private LoadingDialog getLoadingDialog(String message) {

        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(context, message);
        return loadingDialog;

    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }


    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, 0);
    }

    protected void showImageToast(String message, int image) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, image);
    }

}
