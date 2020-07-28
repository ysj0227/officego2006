package com.owner.utils;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.h5.WebViewIdifyActivity_;
import com.owner.identity.CompanyActivity_;
import com.owner.identity.JointWorkActivity_;
import com.owner.identity.PersonalActivity_;
import com.owner.identity.SelectIdActivity_;
import com.owner.mine.StringIdentity;
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
     * auditStatus :   0待审核1审核通过2审核未通过 3过期(和2未通过一样处理)-1未认证
     */
    private void unIdifyDialog(Context context, UserOwnerBean data) {
        if (dialog != null && !dialog.isShowing()) {
            dialog = null;
        }
        if (dialog == null) {   //auditStatus为3，按照2驳回处理
            String title, message, butText;
            if (data.getAuditStatus() == 2 || data.getAuditStatus() == 3) {
                title = "审核未通过";
                message = data.getRemark();
                butText = "去认证";
            } else {
                title = "认证提示";
                message = StringIdentity.identityInfo(data) + "\n请您先认证信息";
                butText = "去认证";
            }
            dialog = new CommonDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelButton(R.string.sm_cancel)
                    .setConfirmButton(butText, (dialog12, which) -> {
                        //当驳回时
                        if (data.getAuditStatus() == 2 || data.getAuditStatus() == 3) {
//                            if (data.getIdentityType() == 0) {//个人
//                                PersonalActivity_.intent(context).start();
////                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_PERSION).start();
//                            } else if (data.getIdentityType() == 1) {//企业
//                                CompanyActivity_.intent(context).start();
////                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_COMPANY).start();
//                            } else if (data.getIdentityType() == 2) { //联办
//                                JointWorkActivity_.intent(context).start();
////                                WebViewIdifyActivity_.intent(context).idifyTag(Constants.H5_OWNER_IDIFY_JOINTWORK).start();
//                            }
                            SelectIdActivity_.intent(context)
                                    .isReject(true)
                                    .rejectIdentityType(data.getIdentityType())
                                    .start();
                        } else { //当需要认证时
//                            WebViewIdifyActivity_.intent(context).start();
                            SelectIdActivity_.intent(context).start();
                        }
                        dialog.dismiss();
                        dialog = null;
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }
}
