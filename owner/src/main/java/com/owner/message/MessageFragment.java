package com.owner.message;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NotificationUtil;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog1;
import com.owner.R;
import com.owner.dialog.ExitAppDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "conversationlist")
public class MessageFragment extends BaseFragment {
    @ViewById(resName = "ll_root_message")
    LinearLayout llRootMessage;
    @ViewById(resName = "rl_input_text")
    RelativeLayout rlInputText;
    @ViewById(resName = "rl_ibtn_search")
    RelativeLayout rlIbtnSearch;
    @ViewById(resName = "rl_title")
    RelativeLayout rlTitle;
    @ViewById(resName = "conversationlist")
    View conversationList;
    @ViewById(resName = "tv_message_history")
    TextView tvMessageHistory;
    private ConversationListFragment fragment;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
        rlTitle.setLayoutParams(params);
        initIm();
        NotificationUtil.showSettingDialog(mActivity, false);
        getUserInfo();
    }

    //初始化聊天列表
    private void initIm() {
        conversationList.setVisibility(View.VISIBLE);
        if (fragment == null) {
            fragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + mActivity.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置群组会话，该会话非聚合显示
                    .build();
            fragment.setUri(uri);

            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.conversationlist, fragment);
            transaction.commit();
        }
    }

    @Click(resName = "tv_message_history")
    void historyListClick() {
        GotoActivityUtils.gotoMessageHistoryListActivity(mActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ExitAppDialog(mActivity);
        }
    }

    public void getUserInfo() {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance()
                .getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                userExpire(data);
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
            }
        });
    }
    //账号试用到期 1:在试用期 0:账号已过试用期
    private void userExpire(UserMessageBean data) {
        if (CommonHelper.bigDecimal(data.getMsgStatus()) == 0) {
            CommonDialog1 dialog = new CommonDialog1.Builder(mActivity)
                    .setTitle("账号试用到期")
                    .setMessage("您的账号试用期已过，请联系客服重新激活。")
                    .setConfirmButton("联系客服", (dialog12, which) -> {
                        CommonHelper.callPhone(mActivity, Constants.SERVICE_SUPPORT);
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

}