package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.wash.framework.net.StringVolley;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.sharef.UserInfosPref;


public class DelAddrDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView okBtn, content;
    TextView cancelBtn;
    String addrId;
    StringVolley delAddrVolley;
    Handler handler;

    public interface OnDelOkLinstener {
        public void delOk();
    }

    OnDelOkLinstener delOkLinstener;


    public void setDelOkLinstener(OnDelOkLinstener delOkLinstener) {
        this.delOkLinstener = delOkLinstener;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getAddrId(){
        return this.addrId;
    }

    public DelAddrDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel_ok, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);

        content = (TextView) view.findViewById(R.id.tv_dialog_content);

        content.setText(R.string.del_addr_prompt);
        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.80), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }


    @Override
    public void onClick(View view) {
        LocationForService locationForService = UserInfosPref.getInstance(context).getLocationServer();
        if (view == okBtn) {
            delAddrVolley.addParams("addressId", addrId);
            delAddrVolley.requestPost(Const.Request.deleteAddr, handler, new LoadingDialogInterface() {
                @Override
                public void showLoading() {
                    getLoadingDialog(context.getString(R.string.operating)).show();
                }

                @Override
                public void hideLoading() {
                    hideLoadingDialog();
                }
            }, UserInfosPref.getInstance(context).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

        } else if (view == cancelBtn) {
            cancel();
        }
    }

    public DelAddrDialog(final Context context) {
        this.context = context;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Const.Message.MSG_DEL_ADDR_SUCC:
                        cancel();
                        if (delOkLinstener != null) {
                            delOkLinstener.delOk();
                        }
                        break;

                    case Const.Message.MSG_DEL_ADDR_FAIL:
                        showMessageToast(context.getString(R.string.request_fail));
                        break;

                }
            }
        };

        delAddrVolley = new StringVolley(context, Const.Message.MSG_DEL_ADDR_SUCC, Const.Message.MSG_ORDER_CANCEL_SUCC);
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

    private void hideLoadingDialog() {
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
