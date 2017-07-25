package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class DoOrderSuccessDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    LinearLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView title, content, cancelBtn;

    public DoOrderSuccessDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel, null);
        container = (LinearLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        title = (TextView) container.findViewById(R.id.tv_dialog_title);
        content = (TextView) container.findViewById(R.id.tv_dialog_content);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        title.setText("下单成功！");
        content.setText("耐心等待取送员接单吧~");
        cancelBtn.setOnClickListener(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }
    public interface OnDoOrderSuccCancelClickedLinstener {
        public void doOrderSuccCancelClicked();
    }

    OnDoOrderSuccCancelClickedLinstener doOrderSuccCancelClickedLinstener;


    public void setCancelLinstener(OnDoOrderSuccCancelClickedLinstener doOrderSuccCancelClickedLinstener) {
        this.doOrderSuccCancelClickedLinstener = doOrderSuccCancelClickedLinstener;
    }

    @Override
    public void onClick(View view) {
        if (view == cancelBtn) {
            if (doOrderSuccCancelClickedLinstener != null) {
                doOrderSuccCancelClickedLinstener.doOrderSuccCancelClicked();
            }

        }
    }

    public DoOrderSuccessDialog(Context context) {
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

    private LoadingDialog getLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(context, message);
        return loadingDialog;

    }
}
