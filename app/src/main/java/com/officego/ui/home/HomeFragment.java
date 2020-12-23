package com.officego.ui.home;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.BrandAdapter;
import com.officego.ui.adapter.HomeAdapter;
import com.officego.ui.adapter.NewsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint({"NewApi", "NonConstantResourceId"})
@EFragment(R.layout.home_fragment)
public class HomeFragment extends BaseFragment {

    @ViewById(R.id.rv_news)
    RecyclerView rvNews;
    @ViewById(R.id.rv_brand)
    RecyclerView rvBrand;
    @ViewById(R.id.rv_hots)
    RecyclerView rvHots;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        initViews();
        new VersionDialog(mActivity);
        testNews();
        testBrand();
        testHotsList();
        //暂无数据，网络异常 TODO
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvNews.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(mActivity, 2);
        rvBrand.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
        rvHots.setLayoutManager(layoutManager2);
    }

    @Click({R.id.tv_all_house, R.id.btn_query_more})
    void moreHouseClick() {
        if (isFastClick(1200)) {
            return;
        }
        SearchHouseListActivity_.intent(mActivity)
                .searchKeywords("").start();
    }

    @Click(R.id.ctl_search)
    void searchClick() {
        if (isFastClick(1200)) {
            return;
        }
        SensorsTrack.searchButtonIndex();
        SearchRecommendActivity_.intent(mActivity).start();
    }

    private void testNews() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        rvNews.setAdapter(new NewsAdapter(mActivity, listBrand));
    }

    private void testBrand() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        rvBrand.setAdapter(new BrandAdapter(mActivity, listBrand));
    }

    private void testHotsList() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        listBrand.add(2,"https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(3,"https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(5,"https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(7,"https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(9,"https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");

        rvHots.setAdapter(new HomeAdapter(mActivity, listBrand));
    }

}
