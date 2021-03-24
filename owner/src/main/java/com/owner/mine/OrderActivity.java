package com.owner.mine;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.adapter.OrderAdapter;
import com.owner.mine.model.OrderBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/24
 **/
@EActivity(resName = "mine_activity_pay_order")
public class OrderActivity extends BaseActivity {

    @ViewById(resName = "rv_order")
    RecyclerView rvOrder;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        rvOrder.setLayoutManager(new LinearLayoutManager(context));
        List<OrderBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new OrderBean());
        }
        OrderAdapter adapter = new OrderAdapter(context, list);
        rvOrder.setAdapter(adapter);
    }
}
