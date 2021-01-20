package com.officego.ui.mine;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.SayHiManagerAdapter;
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
@EActivity(R.layout.mine_activity_say_hi_manager)
public class SayHiManagerActivity extends BaseActivity implements
        SayHiManagerAdapter.SayHiListener {
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
            bean.setContent("我对你发布的房源很感兴趣");
            list.add(bean);
        }
        SayHiManagerAdapter adapter = new SayHiManagerAdapter(context, list);
        adapter.setListener(this);
        rvSayHi.setAdapter(adapter);
    }

    @Click(R.id.btn_save)
    void addClick() {
        SayHiEditActivity_.intent(context).isAdd(true).start();
    }

    @Override
    public void delete(int id) {

    }
}
