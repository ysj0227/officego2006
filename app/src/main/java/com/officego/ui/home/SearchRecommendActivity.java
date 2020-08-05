package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.LabelsView;
import com.officego.ui.adapter.KeywordsAdapter;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.contract.SearchContract;
import com.officego.ui.home.model.QueryHistoryKeywordsBean;
import com.officego.ui.home.model.SearchListBean;
import com.officego.ui.home.presenter.SearchKeywordsPresenter;
import com.officego.ui.home.utils.BundleUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Objects;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions: 搜索推荐
 **/
@SuppressLint("Registered")
@EActivity(R.layout.home_activity_search_recommend)
public class SearchRecommendActivity extends BaseMvpActivity<SearchKeywordsPresenter>
        implements SearchContract.View, TextView.OnEditorActionListener,
        TextWatcher, KeywordsAdapter.SearchKeywordsListener {
    @ViewById(R.id.rl_title)
    RelativeLayout rlTitle;
    @ViewById(R.id.et_search)
    ClearableEditText etSearch;
    @ViewById(R.id.tv_history)
    TextView tvHistory;
    @ViewById(R.id.iv_clear_history)
    ImageView ivClearHistory;
    @ViewById(R.id.label_history)
    LabelsView labelHistory;
    @ViewById(R.id.label_find)
    LabelsView labelFind;
    @ViewById(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @ViewById(R.id.rv_search_list)
    RecyclerView rvSearchList;
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new SearchKeywordsPresenter(context);
        mPresenter.attachView(this);
        rvSearchList.setLayoutManager(new LinearLayoutManager(context));
        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            tvHistory.setVisibility(View.GONE);
            ivClearHistory.setVisibility(View.GONE);
            labelHistory.setVisibility(View.GONE);
        } else {
            tvHistory.setVisibility(View.VISIBLE);
            ivClearHistory.setVisibility(View.VISIBLE);
            labelHistory.setVisibility(View.VISIBLE);
            mPresenter.getHistory();
        }
        mPresenter.getHot();
    }

    @Click(R.id.btn_cancel)
    void cancelClick() {
        finish();
    }

    /**
     * clear
     */
    @Click(R.id.iv_clear_history)
    void clearClick() {
        mPresenter.clearSearchKeywords();
    }

    @Click(R.id.iv_refresh)
    void refreshClick() {
        mPresenter.getHot();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            String input = Objects.requireNonNull(etSearch.getText()).toString().trim();
            if (!TextUtils.isEmpty(input)) {
                mPresenter.addSearchKeywords(input);
                gotoSearchList(input);
            }
            return true;
        }
        return false;
    }

    @Override
    public void historySuccess(List<QueryHistoryKeywordsBean.DataBean> list) {
        labelHistory.setLabels(list, (label, position, data) -> data.getKeywords());
        labelHistory.setOnLabelClickListener((label, data, position) -> gotoSearchList(list.get(position).getKeywords()));
    }

    @Override
    public void hotSuccess(List<DirectoryBean.DataBean> list) {
        labelFind.setLabels(list, (label, position, data) -> data.getDictCname());
        labelFind.setOnLabelClickListener((label, data, position) -> gotoSearchList(list.get(position).getDictCname()));
    }

    @Override
    public void clearHistorySuccess() {
        labelHistory.setLabels(null, (label, position, data) -> (CharSequence) data);
    }


    private KeywordsAdapter keywordsAdapter;

    @Override
    public void searchListSuccess(List<SearchListBean.DataBean> list) {
        if (keywordsAdapter == null) {
            keywordsAdapter = new KeywordsAdapter(context, list);
            keywordsAdapter.setListener(this);
            rvSearchList.setAdapter(keywordsAdapter);
        }
        keywordsAdapter.notifyDataSetChanged();
    }

    private void gotoSearchList(String text) {
        SearchHouseListActivity_.intent(context)
                .searchKeywords(text)
                .start();
    }

    @Override
    public void searchListItemOnClick(SearchListBean.DataBean data) {
        //详情 1:楼盘,2:网点
        if (isFastClick(1200)) {
            return;
        }
        if (data.getBuildType() == 1) {
            BuildingDetailsActivity_.intent(context)
                    .mBuildingBean(BundleUtils.BuildingMessage(data.getBuildType(), data.getBid()))
                    .start();
        } else if (data.getBuildType() == 2) {
            BuildingDetailsJointWorkActivity_.intent(context)
                    .mBuildingBean(BundleUtils.BuildingMessage(data.getBuildType(), data.getBid()))
                    .start();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s.toString())) {
            rvSearchList.setVisibility(View.GONE);
            nsvView.setVisibility(View.VISIBLE);
        } else {
            rvSearchList.setVisibility(View.VISIBLE);
            nsvView.setVisibility(View.GONE);
            mPresenter.searchList(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
