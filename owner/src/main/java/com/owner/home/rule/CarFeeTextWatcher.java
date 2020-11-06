package com.owner.home.rule;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.officego.commonlib.utils.ToastUtils;

/**
 * Created by shijie
 * Date 2020/10/23
 * 车位费0-5000
 **/
public class CarFeeTextWatcher implements TextWatcher {
    private EditText input;
    private Context context;

    public CarFeeTextWatcher(Context context, EditText input) {
        this.context = context;
        this.input = input;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String words = editable.toString();

        try {
            if (words.length() == 1 && (TextUtils.equals(".", words))) {
                editable.clear();
                return;
            }
            if (!TextUtils.isEmpty(words)) {
                if (!words.contains(".") && words.length() >= 4 && Integer.valueOf(words) > 5000) {
                    int index = input.getSelectionStart();//获取光标位置
                    editable.delete(index - 1, index);//删除小数点后一位
                    ToastUtils.toastForShort(context, "只支持0.1-5000正数数字，保留1位小数");
                    return;
                }
                if (words.contains(".") && words.length() >= 4) {
                    if (Integer.valueOf(words.replace(".", "")) > 5000) {
                        int index = input.getSelectionStart();//获取光标位置
                        editable.delete(index - 1, index);//删除小数点后一位
                        ToastUtils.toastForShort(context, "只支持0.1-5000正数数字，保留1位小数");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
