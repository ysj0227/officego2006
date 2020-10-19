package com.owner.home;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by shijie
 * Date 2020/10/19
 **/
@EActivity(resName = "activity_home_independent_manager")
public class AddIndependentActivity extends BaseActivity {
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
    }
    @Click(resName = "btn_next")
    void nextOnClick() {
        UploadVideoVrActivity_.intent(context).start();
    }
}
