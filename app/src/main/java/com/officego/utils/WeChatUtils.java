package com.officego.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.officego.R;
import com.officego.application.MyApplication;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.config.Constants;
import com.officego.model.ShareBean;
import com.officego.wxapi.WXEntryActivity;

import java.util.List;


/**
 * Created by YangShiJie
 * Data 2020/6/16.
 * Descriptions:
 **/
public class WeChatUtils {
    private Context context;

    public WeChatUtils(Context context, ShareBean bean) {
        this.context = context;
        shareWx(context, bean);
    }

    private void shareWx(Context context, ShareBean bean) {
        final String[] items = {"分享微信好友", "分享到微信朋友圈"};
        new AlertDialog.Builder(context)
                .setItems(items, (dialogInterface, i) -> {
                    gotoWxActivity(context, i, bean);
                }).create().show();
    }

    //分享
    private void gotoWxActivity(Context context, int type, ShareBean bean) {
        if (!isInstallWechat(context)) {
            ToastUtils.toastForShort(context, R.string.str_need_install_wx);
            return;
        }
        //当前支持的版本
        if (MyApplication.WXapi.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.TIMELINE_SUPPORTED_SDK_INT) {
            Intent intent = new Intent(context, WXEntryActivity.class);
            intent.putExtra(Constants.WX_TYPE, type);
            intent.putExtra(Constants.WX_DATA, bean);
            context.startActivity(intent);
        } else {
            ToastUtils.toastForShort(context, R.string.wx_version_no_support_timeline);
        }
    }

    /**
     * 是否安装微信
     */
    private boolean isInstallWechat(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
