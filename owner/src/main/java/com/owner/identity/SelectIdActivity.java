package com.owner.identity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.rpc.OfficegoApi;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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
    @Extra
    boolean isReject;
    @Extra
    int rejectIdentityType; //驳回身份

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isReject) {
            if (rejectIdentityType == 0) {//个人
                PersonalActivity_.intent(context).start();
            } else if (rejectIdentityType == 1) {//企业
                CompanyActivity_.intent(context).start();
            } else if (rejectIdentityType == 2) { //联办
                JointWorkActivity_.intent(context).start();
            }
        }
    }

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
        PersonalActivity_.intent(context).start();
    }

    @Click(resName = "tv_back")
    void returnTenantClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        switchDialog();
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.are_you_sure_switch_tenant)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId(Constants.TYPE_TENANT);
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    //用户身份标：0租户，1户主
    private void switchId(String role) {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(role, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "switchId onSuccess code");
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(role);
                //跳转租户首页
                GotoActivityUtils.mainActivity(context);
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "switchId owner onFail code=" + code + "  msg=" + msg);
                hideLoadingDialog();
                if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(context, true);
                } else {
                    shortTip("切换角色失败");
                }
            }
        });
    }
}
