package com.owner.home.rule;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shijie
 * Date 2020/10/23
 * 文字统计
 **/
@SuppressLint("SetTextI18n")
public class TextCountsWatcher implements TextWatcher {
    private EditText editText;
    private TextView tvCounts;
    private int num = 100;
    private CharSequence wordNum;//记录输入的字数
    private int selectionEnd;

    public TextCountsWatcher(TextView tvCounts, EditText editText) {
        this.tvCounts = tvCounts;
        this.editText = editText;
        tvCounts.setText(editText.getText().toString().length() + "/100");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        wordNum = s;//实时记录输入的字数
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            tvCounts.setText("0/100");
            return;
        }
        tvCounts.setText(s.length() + "/100");
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        if (wordNum.length() > num) {
            //删除多余输入的字（不会显示出来）
            s.delete(selectionStart - 1, selectionEnd);
            editText.setText(s);
            editText.setSelection(s.length());//设置光标在最后
        }
    }
}
