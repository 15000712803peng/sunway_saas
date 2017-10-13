package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class LogoutDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView title, content, okBtn, cancelBtn;


    public LogoutDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel_ok2, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);

        title = (TextView) view.findViewById(R.id.tv_dialog_title);
//        content = (TextView) view.findViewById(R.id.tv_dialog_content);
        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        cancelBtn.setText("再想一下");
        title.setText("您确定要退出当前用户吗？");
//        content.setText("确认要退出当前用户吗？");
        okBtn.setText("去意已决");
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    public interface OnLogoutOkClickedLinstener {
        public void logoutOkClicked();
    }

    OnLogoutOkClickedLinstener logoutOkClickedLinstener;


    public void setOkLinstener(OnLogoutOkClickedLinstener logoutOkClickedLinstener) {
        this.logoutOkClickedLinstener = logoutOkClickedLinstener;
    }

    @Override
    public void onClick(View view) {
        if (view == okBtn) {
            if (logoutOkClickedLinstener != null) {
                logoutOkClickedLinstener.logoutOkClicked();
            }

        } else if (view == cancelBtn) {
            cancel();
        }
    }

    public LogoutDialog(final Context context) {
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
}
