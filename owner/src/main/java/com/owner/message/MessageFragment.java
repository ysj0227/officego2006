package com.owner.message;

import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentTransaction;

import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
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

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
        rlTitle.setLayoutParams(params);
        initIm();
    }

    /**
     * 初始化聊天列表
     */
    private void initIm() {
        conversationList.setVisibility(View.VISIBLE);
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + mActivity.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置群组会话，该会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversationlist, fragment);
        transaction.commit();
    }
}