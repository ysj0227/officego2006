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
 * 物业费
 **/
public class EstateFeeTextWatcher implements TextWatcher {
    private EditText editText;
    private int number, length;
    private Context context;

    public EstateFeeTextWatcher(Context context, int number, EditText editText) {
        this.context = context;
        this.number = number;
        this.editText = editText;
        length = String.valueOf(number).length();
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
        if (temp.length() == 1 && TextUtils.equals("0", temp)) {
            editable.clear();
            return;
        }
        try {
            if (!temp.contains(".") && temp.length() >= length && Integer.valueOf(temp) > number) {
                int index = editText.getSelectionStart();//获取光标位置
                editable.delete(index - 1, index);
                ToastUtils.toastForShort(context, "只支持0.1-" + number + "之间正数，保留1位小数");
                return;
            }
            int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
            int index = editText.getSelectionStart();//获取光标位置
            if (posDot >= 0 && temp.length() - 2 > posDot) {
                editable.delete(index - 1, index);//删除小数点后一位
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
