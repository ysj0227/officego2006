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
 * 楼盘下 房源面积
 **/
public class RentSingleTextWatcher implements TextWatcher {
    private EditText editText;
    private Context context;

    public RentSingleTextWatcher(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == null) {
            return;
        }
        String temp = editable.toString();
        try {
            if (!temp.contains(".") && temp.length() >= 2 && Integer.valueOf(temp) > 50) {
                int index = editText.getSelectionStart();//获取光标位置
                editable.delete(index - 1, index);//删除后一位
                ToastUtils.toastForShort(context, "只支持0.1-50正整数或保留2位小数");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保留1位小数
        int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
        int index = editText.getSelectionStart();//获取光标位置
        if (posDot >= 0 && temp.length() - 2 > posDot) {
            editable.delete(index - 1, index);//删除小数点后一位
        }
    }
}
