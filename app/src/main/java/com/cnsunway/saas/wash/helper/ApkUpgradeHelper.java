package com.cnsunway.saas.wash.helper;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.ApkUpgradeDialog;
import com.cnsunway.saas.wash.dialog.DownloadProgressDialog;
import com.cnsunway.saas.wash.dialog.LoadingDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.InstallUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.framework.utils.VersionUtil;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.ApkUpgradeResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

import java.io.File;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ApkUpgradeHelper implements ApkUpgradeDialog.OnUpgradeOkClickedListener {
    boolean isNeedUpdate = false;
    Activity activity;
    Handler updateHandler;
    LoadingDialog operationWaitingDialog;
    ApkUpgradeDialog upgradeDialog;
    FileDownLoadHelper apkDownLoadHelper;
    JsonVolley checkUpdateVolley;
    String appVersion, note;

    ProgressBar downloadProgress;
    DownloadProgressDialog downloadProgressDialog;
    File apkParent;
    File apkPath;
    File apkFile;
    int deviceType = 1;
    boolean showPrompt = false;
    boolean forceUpgrade;

    public ApkUpgradeHelper(final Activity act) {
        this.activity = act;

        operationWaitingDialog = new LoadingDialog(activity);
        downloadProgressDialog = new DownloadProgressDialog(activity).builder();
        downloadProgress = downloadProgressDialog.getProgressBar();
        apkParent = Environment.getExternalStoragePublicDirectory("ldj_app");
        apkPath = new File(apkParent, "apk");
        checkUpdateVolley = new JsonVolley(act, Const.Message.MSG_CHECK_UPGRADE_SUCC, Const.Message.MSG_CHECK_UPGRADE_FAIL);
        operationWaitingDialog = new LoadingDialog(activity, "检查版本中");
        operationWaitingDialog.setCancelable(false);
        if (!apkPath.exists()) {
            apkPath.mkdirs();
        }
        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case Const.Message.MSG_CHECK_UPGRADE_FAIL:
                        OperationToast.showOperationResult(activity, "更新失败", 0);
                        break;

                    case Const.Message.MSG_CHECK_UPGRADE_SUCC:
                        if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                            ApkUpgradeResp resp = (ApkUpgradeResp) JsonParser.jsonToObject(msg.obj + "", ApkUpgradeResp.class);
                            String url = resp.getData().getUrl();
                            appVersion = resp.getData().getAppVersion();
                            note = resp.getData().getNote();
                            forceUpgrade = resp.getData().isForceUpgrade();
                            if (TextUtils.isEmpty(url)) {
                                if(showPrompt)
                                Toast.makeText(activity, "已经是最新版本  ", Toast.LENGTH_LONG).show();
                            } else {
                                upgradeDialog = new ApkUpgradeDialog(activity).builder(appVersion, note, forceUpgrade);
                                upgradeDialog.setOkClickedListener(new ApkUpgradeDialog.OnUpgradeOkClickedListener() {
                                    @Override
                                    public void okClicked(String apkUrl) {
                                        if (!Environment.getExternalStorageState().equals(
                                                Environment.MEDIA_MOUNTED)) {
                                            Toast.makeText(activity.getApplicationContext(), "sd卡不可用",
                                                    Toast.LENGTH_LONG).show();
                                            return;

                                        }
                                        if (!apkPath.exists()) {
                                            apkPath.mkdirs();
                                        }
                                        String filename = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
                                        apkFile = new File(apkPath, filename);
                                        apkDownLoadHelper = new FileDownLoadHelper(activity, apkUrl,
                                                updateHandler, apkPath.getAbsolutePath());
                                        apkDownLoadHelper.start();
                                    }
                                });
                                upgradeDialog.show();
                                upgradeDialog.setApkUrl(url);
                            }

                        }

                        break;

                    case Const.Message.MSG_DOWNLOAD:
                        if (msg.arg1 != 0)
                            downloadProgress.setProgress(msg.arg1);
                        break;

                    case Const.Message.MSG_DOWNLOAD_FINISH:
                        downloadProgressDialog.cancel();
                        if (apkFile != null && apkFile.exists()) {
                            InstallUtil.openApkFile(activity,
                                    apkFile.getAbsolutePath());
                        }
                        break;

                    case Const.Message.MSG_DOWNLOAD_FAIL:
                        Toast.makeText(activity.getApplicationContext(), "下载失败",
                                Toast.LENGTH_LONG).show();
                        downloadProgressDialog.cancel();

                        break;

                    case Const.Message.MSG_DOWNLOAD_START:
                        downloadProgressDialog.show();
                        break;

                    default:
                        break;
                }
            }
        };

    }


    public void check(boolean showPrompt) {
        this.showPrompt = showPrompt;
        checkUpdateVolley.addParams("version",VersionUtil.getAppVersionCode(activity)+"");
        checkUpdateVolley.addParams("deviceType",deviceType);
        LocationForService locationForService = UserInfosPref.getInstance(activity).getLocationServer();
        if(showPrompt){
            checkUpdateVolley._requestPost(Const.Request.checkUpdate, new LoadingDialogInterface() {
                @Override
                public void showLoading() {
                    operationWaitingDialog.show();
                }

                @Override
                public void hideLoading() {
                    operationWaitingDialog.dismiss();

                }
            },updateHandler, locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        }else {
            checkUpdateVolley.requestPost(Const.Request.checkUpdate, updateHandler
                    ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
        }


    }


    @Override
    public void okClicked(String apkUrl) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(activity.getApplicationContext(), "sd卡不可用",
                    Toast.LENGTH_LONG).show();
            return;

        }
        if (!apkPath.exists()) {
            apkPath.mkdirs();
        }
        String filename = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        apkFile = new File(apkPath, filename);
        apkDownLoadHelper = new FileDownLoadHelper(activity, apkUrl,
                updateHandler, apkPath.getAbsolutePath());
        apkDownLoadHelper.start();

    }
}
