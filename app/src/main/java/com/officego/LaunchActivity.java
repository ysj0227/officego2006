package com.officego;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.IdSelectActivity_;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@SuppressLint("Registered")
@EActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
        }
    }

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        new Handler().postDelayed(this::gotoMainActivity, 500);
    }

    private void gotoMainActivity() {
        if (TextUtils.isEmpty(SpUtils.getLead())) {
            LeadPagesActivity_.intent(context).start();
        } else {
            if (!TextUtils.isEmpty(SpUtils.getRole())) {
                if (TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole())) {
                    MainActivity_.intent(context).start();
                } else {
                    LoginActivity_.intent(context).start();
                }
            } else {
                IdSelectActivity_.intent(context).start();
            }
        }
        finish();
    }
}
