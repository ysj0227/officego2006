package com.officego.ui.chatlist;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shijie
 * Date 2020/9/16
 **/
@EActivity(R.layout.activity_message_list)
public class MessageListActivity extends BaseActivity {
    @ViewById(R.id.rv_message_list)
    RecyclerView rvMessageList;

    @AfterViews
    void init() {

    }
}
