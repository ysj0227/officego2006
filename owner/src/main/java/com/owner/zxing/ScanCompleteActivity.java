package com.owner.zxing;

import android.content.Intent;
import android.view.View;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.view.TitleBarView;
import com.owner.zxing.contract.ScanContract;
import com.owner.zxing.presenter.ScanPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 扫描完成
 */
@EActivity(resName = "qr_scan_complete_activity")
public class ScanCompleteActivity extends BaseMvpActivity<ScanPresenter>
        implements ScanContract.View {
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @Extra
    String scanContent;

    @AfterViews
    void init() {
        mPresenter=new ScanPresenter();
        mPresenter.attachView(this);
    }

    @Click(resName = "btn_login")
    void toWebLoginClick() {
        mPresenter.scanLogin(scanContent);
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

    @Override
    public void scanSuccess() {
        scanCompleteFinish();
    }

    @Override
    public void scanFail(int code, String msg) {

    }
}
