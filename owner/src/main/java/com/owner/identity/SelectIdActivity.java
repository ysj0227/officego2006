package com.owner.identity;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.IDCameraActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/7/10.
 * Descriptions:
 **/
@EActivity(resName = "activity_id_select")
public class SelectIdActivity extends BaseActivity {
    @ViewById(resName = "tv_title")
    TextView tvTitle;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        ViewGroup.LayoutParams params = tvTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(this) + CommonHelper.dp2px(this, 60);
        tvTitle.setLayoutParams(params);
    }

    /**
     * 身份类型: 0个人1企业2联合
     */
    @Click(resName = "rl_company")
    void companyClick() {
        CompanyActivity_.intent(context).start();
    }

    @Click(resName = "rl_jointwork")
    void jointWorkClick() {
        JointWorkActivity_.intent(context).start();
    }

    @Click(resName = "rl_personal")
    void personalClick() {

    }

    @Click(resName = "tv_back")
    void returnTenantClick() {
        Intent intent = new Intent(this, IDCameraActivity.class);
        startActivity(intent);
    }
}
