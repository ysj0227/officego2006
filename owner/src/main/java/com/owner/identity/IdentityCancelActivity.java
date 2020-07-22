package com.owner.identity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.view.CircleImage;
import com.owner.identity.contract.CancelSendMsgContract;
import com.owner.identity.model.CancelSendMsgBean;
import com.owner.identity.presenter.CancelSendMsgPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(resName = "activity_go_send_message")
public class IdentityCancelActivity extends BaseMvpActivity<CancelSendMsgPresenter>
        implements CancelSendMsgContract.View {

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

    @AfterViews
    void init() {
        mPresenter = new CancelSendMsgPresenter();
        mPresenter.attachView(this);
        rlEdit.setVisibility(View.GONE);
        tvTip.setVisibility(View.GONE);
        btnSend.setText("撤销");
        mPresenter.getIdentityInfo(0);
    }

    @Click(resName = "iv_back")
    void backClick() {
        finish();
    }

    @Click(resName = "btn_send")
    void sendClick() {
        mPresenter.cancelApply(0);
    }

    @Override
    public void identityInfoSuccess(CancelSendMsgBean data) {

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
