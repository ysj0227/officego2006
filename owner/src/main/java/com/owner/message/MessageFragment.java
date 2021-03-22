package com.owner.message;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NotificationUtil;
import com.officego.commonlib.utils.StatusBarUtils;
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
@EFragment(resName = "conversationlist_owner")
public class MessageFragment extends BaseFragment {
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
        CommonHelper.setViewGroupLayoutParams(mActivity, rlTitle);
        initIm();
        NotificationUtil.showSettingDialog(mActivity, false);
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
}