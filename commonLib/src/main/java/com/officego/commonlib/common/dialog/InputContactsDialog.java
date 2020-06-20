package com.officego.commonlib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.officego.commonlib.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.ClearableEditText;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class InputContactsDialog {

    public InputContactsDialog(Context context) {
        serviceDialog(context);
    }

    public void serviceDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.conversation_dialog_cantacts_input, null);
        //将布局设置给Dialog
        dialog.setContentView(viewLayout);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = CommonHelper.dp2px(context, 255);
        lp.height = CommonHelper.dp2px(context, 152);
        dialogWindow.setAttributes(lp);

        ClearableEditText etWx = viewLayout.findViewById(R.id.et_wechat);
        viewLayout.findViewById(R.id.btn_Confirm).setOnClickListener(v -> {
            String weChat = etWx.getText() == null ? "" : etWx.getText().toString().trim();
            if (TextUtils.isEmpty(weChat)) {
                ToastUtils.toastForShort(context, R.string.str_input_wechat);
            } else {
                //todo 设置微信接口
                setWechat(weChat);
                dialog.dismiss();
                BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationBindWeChat, weChat);
            }
        });
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v ->
                dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();//显示对话框
    }

    private void setWechat(String wx) {
        OfficegoApi.getInstance().addWechat(wx, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                SpUtils.saveWechat(wx);
            }

            @Override
            public void onFail(int code, String msg, Object data) {
            }
        });
    }
}
