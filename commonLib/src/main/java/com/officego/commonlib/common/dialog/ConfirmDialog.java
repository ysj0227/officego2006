package com.officego.commonlib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.common.config.CommonNotifications;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class ConfirmDialog {

    public ConfirmDialog(Context context, boolean isPhone,String title, String weChat) {
        serviceDialog(context, isPhone,title, weChat);
    }

    public void serviceDialog(Context context, boolean isPhone,String title, String weChat) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(
                R.layout.conversation_dialog_exchange_tip, null);
        //将布局设置给Dialog
        dialog.setContentView(viewLayout);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = CommonHelper.dp2px(context, 255);
        lp.height = CommonHelper.dp2px(context, 115);
        dialogWindow.setAttributes(lp);
        TextView tvTitle = viewLayout.findViewById(R.id.tv_content);
        tvTitle.setText(title);

        viewLayout.findViewById(R.id.btn_Confirm).setOnClickListener(v -> {
            dialog.dismiss();
            if (isPhone){
                BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationBindPhone, "conversationBindPhone");
            }else {
                BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationBindWeChat, weChat);
            }
        });
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v ->
                dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }
}
