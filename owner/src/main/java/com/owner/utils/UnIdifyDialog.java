package com.owner.utils;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.h5.WebViewIdifyActivity_;
import com.owner.mine.model.UserOwnerBean;

/**
 * Created by YangShiJie
 * Data 2020/6/28.
 * 业主未认证
 **/
public class UnIdifyDialog {
    private CommonDialog dialog;

    public UnIdifyDialog(Context context, UserOwnerBean data) {
        unIdifyDialog(context, data);
    }

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过
     */
    private void unIdifyDialog(Context context, UserOwnerBean data) {
        if (dialog != null && !dialog.isShowing()) {
            dialog = null;
        }
        if (dialog == null) {
            String title, message, butText;
            if (data.getAuditStatus() == 2) {
                title = "审核未通过";
                message = data.getRemark();
                butText = "去认证";
            } else {
                title = "认证提示";
                message = idify(data) + "\n请您先认证信息";
                butText = "去认证";
            }
            dialog = new CommonDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelButton(R.string.sm_cancel)
                    .setConfirmButton(butText, (dialog12, which) -> {
                        //当驳回时
                        if (data.getAuditStatus() == 2) {
                            if (data.getIdentityType() == 0) {//个人
                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_PERSION).start();
                            } else if (data.getIdentityType() == 1) {//企业
                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_COMPANY).start();
                            } else if (data.getIdentityType() == 2) { //联办
                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_JOINTWORK).start();
                            }
                        } else { //当需要认证时
                            WebViewIdifyActivity_.intent(context).start();
                        }
                        dialog.dismiss();
                        dialog = null;
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过
     */
    private String idify(UserOwnerBean data) {
        String id, status;
        if (data.getIdentityType() == 0) {
            id = "";
        } else if (data.getIdentityType() == 1) {
            id = "";
        } else if (data.getIdentityType() == 2) {
            id = "";
        } else {
            id = "未认证";
        }
        if (data.getAuditStatus() == 0) {
            status = "待审核";
        } else if (data.getAuditStatus() == 1) {
            status = "已认证";
        } else if (data.getAuditStatus() == 2) {
            status = "审核驳回";
        } else {
            status = "";
        }
        if (TextUtils.isEmpty(id)) {
            return status;
        }
        return id + status;
    }

}
