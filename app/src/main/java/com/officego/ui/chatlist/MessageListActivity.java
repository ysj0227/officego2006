package com.officego.ui.chatlist;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.ChatListAdapter;
import com.officego.ui.chatlist.contract.ChatListContract;
import com.officego.ui.chatlist.presenter.ChatListPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/9/16
 **/
@SuppressLint("Registered")
@EActivity(R.layout.activity_message_list)
public class MessageListActivity extends BaseMvpActivity<ChatListPresenter>
        implements ChatListContract.View {
    @ViewById(R.id.rv_message_list)
    RecyclerView rvMessageList;
    @ViewById(R.id.rl_no_data)
    RelativeLayout tvNoData;
    private ChatListAdapter adapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ChatListPresenter();
        mPresenter.attachView(this);
        mPresenter.getChatList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvMessageList.setLayoutManager(layoutManager);
    }

    @Override
    public void chatListSuccess(List<ChatListBean.ListBean> data) {
        if (data != null && data.size() > 0) {
            hasData();
            if (adapter == null) {
                adapter = new ChatListAdapter(context, data);
                rvMessageList.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        } else {
            noData();
        }
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rvMessageList.setVisibility(View.GONE);
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rvMessageList.setVisibility(View.VISIBLE);
    }
}
