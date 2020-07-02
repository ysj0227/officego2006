package com.officego.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.officego.MainActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.id_select_activity)
public class IdSelectActivity extends BaseActivity {
    @ViewById(R.id.ib_tenant)
    RadioButton ibTenant;
    @ViewById(R.id.ib_house_owner)
    RadioButton ibOwner;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
    }

    @Click(R.id.ib_tenant)
    void tenantClick() {
        if (isFastClick(1200)) {
            return;
        }
        ibTenant.setBackgroundResource(R.mipmap.ic_tenant_check);
        ibOwner.setBackgroundResource(R.mipmap.ic_owner);
        SpUtils.saveRole(Constants.TYPE_TENANT);
        //租户首次不登录进入首页
        MainActivity_.intent(context).start();
    }

    @Click(R.id.ib_house_owner)
    void ownerClick() {
        if (isFastClick(1200)) {
            return;
        }
        ibTenant.setBackgroundResource(R.mipmap.ic_tenant);
        ibOwner.setBackgroundResource(R.mipmap.ic_owner_check);
        //业主端必须先登录
        SpUtils.saveRole(Constants.TYPE_OWNER);
        LoginActivity_.intent(context).start();
    }
}
