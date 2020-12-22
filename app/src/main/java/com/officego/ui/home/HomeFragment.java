package com.officego.ui.home;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.BrandAdapter;
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

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        initViews();
        new VersionDialog(mActivity);
        testNews();
        testBrand();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvNews.setLayoutManager(layoutManager);

        GridLayoutManager layoutManager1 = new GridLayoutManager(mActivity, 2);
        rvBrand.setLayoutManager(layoutManager1);
    }

    @Click(R.id.ctl_search)
    void searchClick() {
        gotoSearchActivity();
    }

    private void gotoSearchActivity() {
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

}
