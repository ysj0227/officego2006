package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shijie
 * Date 2021/1/20
 * 打招呼语
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.mine_activity_say_hi_edit)
public class SayHiEditActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    TitleBarView titleBar;
    @ViewById(R.id.cet_text)
    ClearableEditText cetText;
    @Extra
    boolean isAdd;
    @Extra
    String contextText;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        titleBar.setAppTitle(isAdd ? "添加打招呼语" : "编辑打招呼语");
        cetText.setText(TextUtils.isEmpty(contextText) ? "" : contextText);
        titleBar.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
