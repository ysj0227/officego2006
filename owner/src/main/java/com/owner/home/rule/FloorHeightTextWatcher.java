package com.owner.home.rule;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by shijie
 * Date 2020/10/23
 **/
public class FloorHeightTextWatcher implements TextWatcher {
    private Context context;
    private EditText editText;

    public FloorHeightTextWatcher(Context context,EditText editText) {
        this.context=context;
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
        //只能输入一位整数，如果第二位是整数则删除
        try {
            if (editable.toString().length() > 1 &&
                    !TextUtils.equals(".", editable.toString().substring(1, 2))) {
                int index = editText.getSelectionStart();//获取光标位置
                editable.delete(index - 1, index);
                ToastUtils.toastForShort(context, "请输入0-8之间的整数或一位小数");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保留一位小数
        String temp = editable.toString();
        int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
        int index = editText.getSelectionStart();//获取光标位置
        //如果包含小数点
        if (posDot >= 0 && temp.length() - 2 > posDot) {
            editable.delete(index - 1, index);//删除小数点后一位
        }
    }
}
