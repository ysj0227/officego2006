package com.officego.commonlib.common.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.officego.commonlib.R;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;


/**
 * Created by YangShiJie
 * Data 2020/6/16.
 * Descriptions:
 **/
public class WeChatShareDialog {
    private Context context;

    public WeChatShareDialog(Context context, ShareBean bean) {
        this.context = context;
        shareWx(context, bean);
    }

    private void shareWx(Context context, ShareBean bean) {
        shareDialog(context, bean);
    }

    //分享
    private void gotoWxActivity(Context context, int type, ShareBean bean) {
        if (!CommonHelper.isInstallWechat(context)) {
            ToastUtils.toastForShort(context, R.string.str_need_install_wx);
            return;
        }
        //当前支持的版本
        if (Constants.WXapi.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.TIMELINE_SUPPORTED_SDK_INT) {
            ComponentName comp = new ComponentName(context, "com.officego.wxapi.WXEntryActivity");
            Intent intent = new Intent();
            intent.putExtra(Constants.WX_TYPE, type);
            intent.putExtra(Constants.WX_DATA, bean);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } else {
            ToastUtils.toastForShort(context, R.string.wx_version_no_support_timeline);
        }
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
