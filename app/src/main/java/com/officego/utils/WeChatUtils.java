package com.officego.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.officego.R;
import com.officego.application.MyApplication;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.ToastUtils;
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
//        final String[] items = {"分享微信好友", "分享到微信朋友圈"};
//        new AlertDialog.Builder(context)
//                .setItems(items, (dialogInterface, i) -> {
//                    gotoWxActivity(context, i, bean);
////                }).create().show();
        shareDialog(context, bean);
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

    private void shareDialog(Context context, ShareBean bean) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        dialogWindow.setAttributes(lp);
        viewLayout.findViewById(R.id.rl_friend).setOnClickListener(v -> {
            gotoWxActivity(context, 0, bean);
            dialog.dismiss();
        });
        viewLayout.findViewById(R.id.rl_timeline).setOnClickListener(v -> {
            gotoWxActivity(context, 1, bean);
            dialog.dismiss();
        });
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

}
