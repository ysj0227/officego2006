package com.owner.identity;

import android.widget.ImageView;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/7/14.
 * Descriptions:
 **/
@EActivity(resName = "activity_id_jointwork_create")
public class CreateJointWorkActivity extends BaseActivity {
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "et_name_content")
    ClearableEditText etNameContent;
    @ViewById(resName = "et_address_content")
    ClearableEditText etAddressContent;
    @ViewById(resName = "iv_image")
    ImageView ivImage;


    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
    }

    @Click(resName = "btn_save")
    void saveClick() {

    }

}
