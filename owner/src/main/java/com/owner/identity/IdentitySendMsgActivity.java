package com.owner.identity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.ClearableEditText;
import com.owner.identity.contract.SendMsgContract;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.SendMsgBean;
import com.owner.identity.presenter.SendMsgPresenter;
import com.owner.utils.CommUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

@EActivity(resName = "activity_go_send_message")
public class IdentitySendMsgActivity extends BaseMvpActivity<SendMsgPresenter>
        implements SendMsgContract.View {

    @ViewById(resName = "tv_title_name")
    TextView tvTitleName;
    @ViewById(resName = "tv_address")
    TextView tvAddress;
    @ViewById(resName = "cet_send_content")
    ClearableEditText cetSendContent;
    @ViewById(resName = "tv_counts")
    TextView tvCounts;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_position")
    TextView tvPosition;

    @Extra
    SendMsgBean sendMsgBean;
    private ApplyLicenceBean mData;

    @AfterViews
    void init() {
        mPresenter = new SendMsgPresenter();
        mPresenter.attachView(this);
        //获取信息
        mPresenter.getDetails(sendMsgBean.getIdentityType(), sendMsgBean.getId());
        counts();
        CommUtils.showHtmlTextView(tvTitleName, sendMsgBean.getName());
        cetSendContent.setText("我是" + SpUtils.getNickName() + "，希望加入公司，请通过。");
        if (TextUtils.isEmpty(sendMsgBean.getAddress())) {
            tvAddress.setVisibility(View.GONE);
        } else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(sendMsgBean.getAddress());
        }
//        SendMessageManager.getInstance().sendIdApplyMessage(
//                "4331",
//                sendMsgBean.getId(),
//                Objects.requireNonNull(cetSendContent.getText()).toString(),
//                "");
//        gotoConversationActivity(context, "4331");
    }

    @Click(resName = "iv_back")
    void backClick() {
        finish();
    }

    @Click(resName = "btn_send")
    void sendClick() {
        mPresenter.sendApply(sendMsgBean.getId());
    }

    @Override
    public void messageSuccess(ApplyLicenceBean data) {
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
        tvName.setText(data.getProprietorRealname());
        tvPosition.setText(data.getProprietorJob());
    }

    /**
     * 发送申请成功
     * 发送自定义消息
     */
    @Override
    public void sendApplySuccess() {
        if (mData != null) {
            SendMessageManager.getInstance().sendIdApplyMessage(
                    mData.getTargetId(),
                    sendMsgBean.getId(),
                    Objects.requireNonNull(cetSendContent.getText()).toString(),
                    "");
            gotoConversationActivity(context, mData.getTargetId());
        }
    }

    //进入聊天页面
    public static void gotoConversationActivity(Context context,String targetId) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.message.ConversationActivity_");
        Intent intent = new Intent();
        intent.putExtra("isSendApply", true);
        intent.putExtra("targetId", targetId);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    /**
     * 撤销申请
     */
    @Override
    public void cancelApplySuccess() {

    }

    private void counts() {
        tvCounts.setText(cetSendContent.getText().toString().length() + "/100");
        int num = 100;
        cetSendContent.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                tvCounts.setText(100 - number + "/100");
                selectionStart = cetSendContent.getSelectionStart();
                selectionEnd = cetSendContent.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    cetSendContent.setText(s);
                    cetSendContent.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }
}
