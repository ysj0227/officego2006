package com.owner.identity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.view.ClearableEditText;
import com.owner.identity.contract.SendMsgContract;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.SendMsgBean;
import com.owner.identity.presenter.CompanyPresenter;
import com.owner.identity.presenter.SendMsgPresenter;
import com.owner.utils.CommUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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

    @Extra
    SendMsgBean sendMsgBean;

    @AfterViews
    void init() {
        mPresenter = new SendMsgPresenter();
        mPresenter.attachView(this);
        counts();
        CommUtils.showHtmlTextView(tvTitleName, sendMsgBean.getName());
        if (TextUtils.isEmpty(sendMsgBean.getAddress())) {
            tvAddress.setVisibility(View.GONE);
        } else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(sendMsgBean.getAddress());
        }
    }

    @Click(resName = "iv_back")
    void backClick() {
        finish();
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

    @Override
    public void messageSuccess(ApplyLicenceBean data) {

    }
}
