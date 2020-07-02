package com.officego.commonlib.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;

import com.officego.commonlib.R;
import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;

import java.io.File;
import java.util.Objects;


/**
 * 版本更新
 */

public class AppUpdate {
    /**
     * test apk
     */
//    public static String updateUrl = "https://download.dgstaticresources.net/fusion/android/app-c6-release.apk";
    public static String updateUrl = "https://github.com/WVector/AppUpdate/blob/master/apk/sample-debug.apk";

    /**
     * 更新进度
     *
     * @param context
     * @param updateUrl
     */
    public static void versionUpdate(final Activity context, final String updateUrl) {
        UpdateAppBean updateAppBean = new UpdateAppBean();
        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(updateUrl);

        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            try {
                path = Objects.requireNonNull(context.getExternalCacheDir()).getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }

        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);
        //实现网络接口，只实现下载就可以
        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(context, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
                HProgressDialogUtils.showHorizontalProgressDialog(context, context.getString(R.string.str_app_download_progress), false);
                //Log.d(TAG, "onStart() called");
            }

            @Override
            public void onProgress(float progress, long totalSize) {
                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                //Log.d(TAG, "onProgress() called with: progress = [" + progress + "], totalSize = [" + totalSize + "]");
            }

            @Override
            public void setMax(long totalSize) {
                //Log.d(TAG, "setMax() called with: totalSize = [" + totalSize + "]");
            }

            @Override
            public boolean onFinish(File file) {
                HProgressDialogUtils.cancel();
                // LogCat.e("TAG", "appUpdate onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                AppUpdateUtils.installApp(context, file);//下载完成跳转安装
                return true;
            }

            @Override
            public void onError(String msg) {
                //LogCat.e("TAG", "appUpdate onError() called with: file = [" + msg + "]");
                HProgressDialogUtils.cancel();
                retryDialog(context, updateUrl);
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                //LogCat.e("TAG", "appUpdate onInstallAppAndAppOnForeground() called with: file = [" + file + "]");
                return false;
            }
        });
    }

    public static void retryDialog(final Activity context, final String url) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.update_app_exception)
                .setConfirmButton(R.string.str_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        versionUpdate(context, url);
                    }
                }).setCancelButton(R.string.str_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.getInstance().quit();
                    }
                }).create();
        dialog.showWithOutTouchable(false);
    }
}

