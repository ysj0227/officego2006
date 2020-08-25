package com.owner.zxing;

import android.content.Intent;
import android.view.View;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.view.TitleBarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 扫描完成
 */
@EActivity(resName = "qr_scan_complete_activity")
public class ScanCompleteActivity extends BaseActivity {
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;

    @AfterViews
    void init() {
    }

    @Click(resName = "btn_login")
    void toWebLoginClick() {
        scanCompleteFinish();
    }

    @Click(resName = "btn_login_cancel")
    void toWebLoginCancelClick() {
        scanCompleteFinish();
    }

    private void scanCompleteFinish() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
