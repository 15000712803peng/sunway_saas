package com.cnsunway.saas.wash.dialog;

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

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.sharef.UserInfosPref;


public class AreaIntroductDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView  content;
    TextView cancelBtn;
    String addrId;

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getAddrId(){
        return this.addrId;
    }

    public AreaIntroductDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_area_introduce, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        content = (TextView) view.findViewById(R.id.tv_dialog_content);
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
       if (view == cancelBtn) {
            cancel();
        }
    }

    public AreaIntroductDialog(final Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

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

    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, 0);
    }

    protected void showImageToast(String message, int image) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, image);
    }

}
