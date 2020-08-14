package com.owner.identity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.QueryApplyLicenceBean;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.CircleImage;
import com.owner.identity.contract.CancelSendMsgContract;
import com.owner.identity.presenter.CancelSendMsgPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(resName = "activity_go_send_message")
public class IdentityCancelActivity extends BaseMvpActivity<CancelSendMsgPresenter>
        implements CancelSendMsgContract.View {

    @ViewById(resName = "tv_identity")
    TextView tvIdentity;
    @ViewById(resName = "tv_title_name")
    TextView tvTitleName;
    @ViewById(resName = "tv_address")
    TextView tvAddress;
    @ViewById(resName = "rl_edit")
    ConstraintLayout rlEdit;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_position")
    TextView tvPosition;
    @ViewById(resName = "tv_tip")
    TextView tvTip;
    @ViewById(resName = "btn_send")
    Button btnSend;
    private QueryApplyLicenceBean mData;

    @AfterViews
    void init() {
        mPresenter = new CancelSendMsgPresenter();
        mPresenter.attachView(this);
        rlEdit.setVisibility(View.GONE);
        tvTip.setVisibility(View.GONE);
        btnSend.setText("撤销申请");
        mPresenter.getIdentityInfo();
    }

    @Click(resName = "iv_back")
    void backClick() {
        finish();
    }

    @Click(resName = "btn_send")
    void sendClick() {
        if (mData != null) {
            mPresenter.cancelApply(mData.getUserLicenceId());
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void identityInfoSuccess(QueryApplyLicenceBean data) {
        mData = data;
        tvTitleName.setText(data.getTitle());
        if (TextUtils.isEmpty(data.getAddress())) {
            tvAddress.setVisibility(View.GONE);
        } else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(data.getAddress());
        }
        tvIdentity.setVisibility(mData.getIdentityType() == 1 ? View.VISIBLE : View.GONE);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
        tvName.setText((TextUtils.isEmpty(data.getAuthority()) ? "" : data.getAuthority() + ":") + data.getProprietorRealname());
        tvPosition.setText(data.getProprietorJob());
    }

    /**
     * 撤销申请
     */
    @Override
    public void cancelApplySuccess() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
