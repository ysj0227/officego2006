package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.widget.Switch;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.SayHiAdapter;
import com.officego.ui.mine.model.SayHiBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2021/1/20
 * 打招呼语
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.mine_activity_say_hi_list)
public class SayHiListActivity extends BaseActivity {
    @ViewById(R.id.sw_say_hi)
    Switch swSayHi;
    @ViewById(R.id.rv_say_hi)
    RecyclerView rvSayHi;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        rvSayHi.setLayoutManager(new LinearLayoutManager(context));
        List<SayHiBean.DataBean> list = new ArrayList<>();
        SayHiBean.DataBean bean;
        for (int i = 0; i < 6; i++) {
            bean = new SayHiBean.DataBean();
            bean.setId(i);
            bean.setContent("我对你发布的房源很感兴趣，希望能和您聊聊兴趣");
            list.add(bean);
        }
        rvSayHi.setAdapter(new SayHiAdapter(context, list));
    }

    @Click(R.id.tv_edit)
    void addClick() {
        SayHiEditActivity_.intent(context).isAdd(true).start();
    }

    @Click(R.id.tv_manager)
    void managerClick() {
        SayHiManagerActivity_.intent(context).start();
    }
}
