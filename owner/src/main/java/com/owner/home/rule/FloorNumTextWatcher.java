package com.owner.home.rule;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.officego.commonlib.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shijie
 * Date 2020/10/23
 * 楼层数
 **/
public class FloorNumTextWatcher implements TextWatcher {
    private int number, length; //这个值存储输入超过两位数时候显示的内容
    private EditText input;
    private Context context;

    public FloorNumTextWatcher(Context context, int number, int length, EditText input) {
        this.number = number;
        this.length = length;
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
        if (!TextUtils.isEmpty(words)) {
            if (words.length() >= length && Integer.valueOf(words) > number) {
                int index = input.getSelectionStart();//获取光标位置
                editable.delete(index - 1, index);//删除小数点后一位
                ToastUtils.toastForShort(context, "请输入0-" + number + "之间的整数");
            }
        }
    }
}
