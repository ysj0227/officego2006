package com.officego.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.officego.R;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class MonitorEditTextUtils {
    private Button button;
    private EditText editText;

    public MonitorEditTextUtils(Button button, EditText editText) {
        this.button = button;
        this.editText = editText;
        //初始化
        button.setBackgroundResource(R.drawable.btn_common_bg_primary_disable);
        button.setEnabled(Boolean.FALSE);//不启用按钮
        setEdit(button, editText);
    }

    private void setEdit(Button button, EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    button.setBackgroundResource(R.drawable.btn_common_bg_primary_disable);
                    button.setEnabled(Boolean.FALSE);//不启用按钮
                } else {
                    button.setBackgroundResource(R.drawable.btn_common_bg_primary_normal);
                    button.setEnabled(Boolean.TRUE);//启用按钮
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
