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
 * 楼层数
 **/
public class IntegerTextWatcher implements TextWatcher {
    private int number, length; //这个值存储输入超过两位数时候显示的内容
    private EditText input;
    private Context context;

    public IntegerTextWatcher(Context context, int number, EditText input) {
        this.number = number;
        this.length = String.valueOf(number).length();
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
        //会议室数量
        if (number == 10) {
            //首位为0
            if (words.length() == 1 && TextUtils.equals("0", words)) {
                editable.clear();
                return;
            }
        }
        try {
            if (!TextUtils.isEmpty(words)) {
                if (words.length() >= length && Integer.valueOf(words) > number) {
                    int index = input.getSelectionStart();//获取光标位置
                    editable.delete(index - 1, index);//删除小数点后一位
                    ToastUtils.toastForShort(context, "请输入0-" + number + "之间的正整数");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
