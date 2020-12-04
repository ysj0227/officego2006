package com.officego.ui.coupon;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
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
public class CouponActivity extends BaseActivity {
    @ViewById(R.id.rv_view)
    RecyclerView rvView;


    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        initViews();
        list();
    }

    private void initViews() {
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
        rvView.setLayoutManager(layoutManager);
    }


    private void list() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("");
        }
        rvView.setAdapter(new CouponAdapter(context, list));
    }

    private class CouponAdapter extends CommonListAdapter<String> {

        /**
         * @param context 上下文
         * @param list    列表数据
         */
        public CouponAdapter(Context context, List<String> list) {
            super(context, R.layout.item_coupon, list);
        }

        @Override
        public void convert(ViewHolder holder, String s) {

        }
    }
}
