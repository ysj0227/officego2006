package com.officego.ui.home;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.ui.adapter.CustomisedHouseAdapter;
import com.officego.ui.find.WantFindContract;
import com.officego.ui.find.WantFindPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2021/2/25
 * 定制找房
 **/
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.activity_customised_house)
public class CustomisedHouseActivity extends BaseMvpActivity<WantFindPresenter>
        implements WantFindContract.View {
    @ViewById(R.id.rv_customised_house)
    RecyclerView rvCustomisedHouse;

    @AfterViews
    void init() {
        mPresenter = new WantFindPresenter();
        mPresenter.attachView(this);
        mPresenter.getFactorList();
        initViews();
    }

    private void initViews() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rvCustomisedHouse.setLayoutManager(manager);
    }

    @Override
    public void factorSuccess(List<DirectoryBean.DataBean> factorList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("");
        }
        rvCustomisedHouse.setAdapter(new CustomisedHouseAdapter(context, list,factorList));
    }

    @Override
    public void saveSuccess() {

    }
}
