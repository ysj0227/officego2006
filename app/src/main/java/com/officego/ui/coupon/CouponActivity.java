package com.officego.ui.coupon;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.CouponAdapter;
import com.officego.view.WrapContentLinearLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
@SuppressLint("Registered")
@EActivity(R.layout.coupon_activity_list)
public class CouponActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener {
    @ViewById(R.id.rv_view)
    RecyclerView rvView;
    @ViewById(R.id.tl_view)
    TabLayout tlView;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        initViews();
        list();
    }

    private void initViews() {
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
        rvView.setLayoutManager(layoutManager);
        tlView.addOnTabSelectedListener(this);
    }

    private void list() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("");
        }
        rvView.setAdapter(new CouponAdapter(context, true, list));
    }

    private void list2() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("");
        }
        rvView.setAdapter(new CouponAdapter(context, false, list));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                list();
                break;
            case 1:
                list2();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
