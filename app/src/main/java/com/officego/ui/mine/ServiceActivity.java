package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by YangShiJie
 * Data 2020/6/28.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.service_activity)
public class ServiceActivity extends BaseActivity {
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
    }

    @Click(R.id.iv_email)
    void emailClick() {
        Intent i = new Intent(Intent.ACTION_SEND);
        // i.setType("text/plain"); //模拟器请使用这行
        i.setType("message/rfc822"); //真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole()) ?
                        Constants.SERVICE_EMAIL_OWNER : Constants.SERVICE_EMAIL_TENANT});
        i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
        i.putExtra(Intent.EXTRA_TEXT, " ");
        startActivity(Intent.createChooser(i, "选择邮箱"));
    }

    @Click(R.id.iv_call_phone)
    void callClick() {
        call();
    }

    private void call() {
        final String[] items = {"业务咨询", "技术支持"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        CommonHelper.callPhone(context, Constants.SERVICE_HOT_MOBILE);
                    } else if (i == 1) {
                        CommonHelper.callPhone(context, Constants.SERVICE_TECHNICAL_SUPPORT);
                    }
                }).create().show();
    }
}
