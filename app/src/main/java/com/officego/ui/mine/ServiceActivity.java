package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.ServiceBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
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
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.service_activity)
public class ServiceActivity extends BaseActivity {
    private boolean isEmail;
    private ServiceBean data;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
    }

    @Click(R.id.iv_email)
    void emailClick() {
        isEmail = true;
        if (data == null) {
            getMobile();
            return;
        }
        email();
    }

    @Click(R.id.iv_call_phone)
    void callClick() {
        isEmail = false;
        if (data == null) {
            getMobile();
            return;
        }
        call();
    }

    private void email() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822"); //真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole()) ?
                        data.getOwnerEmail() : data.getTenantEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
        i.putExtra(Intent.EXTRA_TEXT, " ");
        startActivity(Intent.createChooser(i, "选择邮箱"));
    }

    private void call() {
        final String[] items = {"业务咨询", "技术支持"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        CommonHelper.callPhone(context, TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole()) ?
                                data.getOwnerConsultation() : data.getTenantConsultation());
                    } else if (i == 1) {
                        CommonHelper.callPhone(context, data.getTechnicalSupport());
                    }
                }).create().show();
    }

    public void getMobile() {
        showLoadingDialog();
        OfficegoApi.getInstance().serviceMobile(new RetrofitCallback<ServiceBean>() {
            @Override
            public void onSuccess(int code, String msg, ServiceBean bean) {
                hideLoadingDialog();
                data = bean;
                if (isEmail) {
                    email();
                } else {
                    call();
                }
            }

            @Override
            public void onFail(int code, String msg, ServiceBean data) {
                hideLoadingDialog();
                if (code == Constants.DEFAULT_ERROR_CODE) {
                   shortTip(msg);
                }
            }
        });
    }
}
