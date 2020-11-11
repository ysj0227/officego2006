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
        if (editable == null) {
            return;
        }
        String words = editable.toString();
        if (!TextUtils.isEmpty(words)) {
            try {
                if (words.length() > 3 && Integer.valueOf(words) > 5000) {
                    int index = input.getSelectionStart();//获取光标位置
                    editable.delete(index - 1, index);//删除小数点后一位
                    ToastUtils.toastForShort(context, "仅支持0-5000正整数");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
