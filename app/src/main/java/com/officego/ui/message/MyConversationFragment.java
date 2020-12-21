package com.officego.ui.message;

import android.text.TextUtils;
import android.view.View;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.notification.BaseNotification;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by shijie
 * Date 2020/11/20
 **/
public class MyConversationFragment extends ConversationFragment {

    /**
     * 发送按钮监听
     *
     * @param view 发送控件
     * @param text 发送文本
     */
    @Override
    public void onSendToggleClick(View view, String text) {
        if (phoneEncrypted(text)) {
            text = "";
            BaseNotification.newInstance().postNotificationName(
                    CommonNotifications.conversationPhoneEncrypted, "conversationPhoneEncrypted");
        }
        super.onSendToggleClick(view, text);
    }

    /**
     * 发送手机号加密
     */
    private boolean phoneEncrypted(String text) {
        if (text.length() == 11) {
            return TextUtils.equals("1", text.substring(0, 1)) &&
                    isNumeric(text.substring(0, 3)) &&
                    isNumeric(text.substring(3, 7)) &&
                    isNumeric(text.substring(7, 11));
        } else if (text.length() == 13) {
            return TextUtils.equals("1", text.substring(0, 1)) &&
                    isNumeric(text.substring(0, 3)) &&
                    isNumeric(text.substring(4, 8)) &&
                    isNumeric(text.substring(9, 13));
        } else if (text.length() == 15) {
            return TextUtils.equals("1", text.substring(0, 1)) &&
                    isNumeric(text.substring(0, 3)) &&
                    isNumeric(text.substring(5, 9)) &&
                    isNumeric(text.substring(11, 15));
        }
        return false;
    }

    public boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }
}