package com.owner.identity;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.view.TitleBarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/7/13.
 * Descriptions:
 **/
@EActivity(resName = "activity_id_company")
public class CompanyActivity extends BaseActivity {

    @ViewById(resName = "title_bar")
    TitleBarView titleBar;

    @AfterViews
    void init() {

    }

    @Click(resName = "rl_identity")
    void identityClick() {

    }

    @Click(resName = "rl_company_name")
    void companyNameClick() {

    }
}
