package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Order;


public class ShareGiftDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    ImageView cancelImage;
    TextView shareText;
    Order order;
    public interface OnShareBtnClickedLinstener{
        void shareBtnClicked();
    };

    OnShareBtnClickedLinstener shareBtnClickedLinstener;
    public void setShareBtnClickedLinstener(OnShareBtnClickedLinstener shareBtnClickedLinstener) {
        this.shareBtnClickedLinstener = shareBtnClickedLinstener;
    }

    public ShareGiftDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_share_gift, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        cancelImage = (ImageView) container.findViewById(R.id.iv_dialog_cancel);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        shareText = (TextView) container.findViewById(R.id.text_share);
        shareText.setOnClickListener(this);
        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.75), (int) (display
                .getHeight()));
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    @Override
    public void onClick(View view) {
        if(view == shareText){
           if(shareBtnClickedLinstener != null){
               shareBtnClickedLinstener.shareBtnClicked();
           }
            cancel();

        }
    }

    public ShareGiftDialog(Context context,Order order) {
        this.context = context;
        this.order = order;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancel(){
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

    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, 0);
    }

    protected void showImageToast(String message, int image) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, image);
    }

}
