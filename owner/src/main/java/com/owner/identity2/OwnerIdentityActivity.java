package com.owner.identity2;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.dialog.IdentityTypeDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shijie
 * Date 2020/11/17
 * 认证
 **/
@EActivity(resName = "activity_owner_identity")
public class OwnerIdentityActivity extends BaseActivity implements
        IdentityTypeDialog.IdentityTypeListener{
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "sil_select_type")
    SettingItemLayout silSelectType;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
    }

    @Click(resName = "sil_select_type")
    void selectTypeOnClick() {
        new IdentityTypeDialog(context).setListener(this);
    }

    @Override
    public void sureType(String text, int Type) {
        silSelectType.setCenterText(text);
    }
}
