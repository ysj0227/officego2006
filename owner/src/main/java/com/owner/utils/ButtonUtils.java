package com.owner.utils;

import android.widget.Button;

import com.owner.R;

public class ButtonUtils {
    public static void clickButton(Button button, boolean isClick) {
        if (isClick) {
            button.setBackgroundResource(R.drawable.btn_common_bg_primary_normal);
            button.setEnabled(Boolean.TRUE);//启用按钮
        } else {
            button.setBackgroundResource(R.drawable.btn_common_bg_primary_disable);
            button.setEnabled(Boolean.FALSE);//不启用按钮
        }
    }
}
