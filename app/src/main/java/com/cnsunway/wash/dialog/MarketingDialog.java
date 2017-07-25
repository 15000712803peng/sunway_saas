package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.WebActivity;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.model.Marketing;
import com.cnsunway.wash.view.AdImagesPlayView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class MarketingDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;

    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    LinearLayout cancelImage;
    AdImagesPlayView marketingPlayView;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    List<Marketing> marketings;
    List<View> images = new ArrayList<View>();

    public void setMarketings(List<Marketing> marketings) {
        this.marketings = marketings;
    }

    public MarketingDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_marketing, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        cancelImage = (LinearLayout) container.findViewById(R.id.ll_dialog_cancel);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        marketingPlayView = (AdImagesPlayView) view.findViewById(R.id.marketing_play_view);
        dialog.setContentView(view);
//        dialog.setCancelable(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth()), (int) (display
                .getHeight()));
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                marketingPlayView.remoevRecycle();
            }
        });
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    @Override
    public void onClick(View view) {

    }

    public MarketingDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.markting_loading)
                .showImageForEmptyUri(R.mipmap.markting_loading_fail)
                .showImageOnFail(R.mipmap.markting_loading_fail).cacheInMemory()
                .cacheOnDisc()
                .build();
    }
    public void show() {
        images.clear();
        for (final Marketing marketing : marketings) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.market_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
            if (!TextUtils.isEmpty(marketing.getMarketingUrl())) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", marketing.getMarketingUrl());
                        context.startActivity(intent);
                        cancel();

                    }
                });
            }

            imageLoader.displayImage(marketing.getPicUrl(), imageView, options);
            images.add(view);

            if (marketing.getName() == null || TextUtils.isEmpty(marketing.getName())) {
                //不是充值活动
            }else if(!marketing.getName().equals(Const.MARKET_BALANCE)){
                //不是充值活动
            }else if (marketing.getName().equals(Const.MARKET_BALANCE)) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, WebActivity.class));
                    }
                });
            }

        }

        marketingPlayView.addViews(images);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            marketingPlayView.recycle();
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

    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, 0);
    }

    protected void showImageToast(String message, int image) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, image);
    }

}
