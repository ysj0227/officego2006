package com.officego.ui.find;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.MainActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.ui.adapter.FactorAdapter;
import com.officego.ui.adapter.PersonAdapter;
import com.officego.ui.adapter.RentAdapter;
import com.officego.ui.login.LoginActivity_;
import com.officego.utils.CommonList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.activity_want_to_find)
public class WantToFindActivity extends BaseMvpActivity<WantFindPresenter>
        implements WantFindContract.View, PersonAdapter.PersonListener,
        RentAdapter.RentListener, FactorAdapter.FactorListener {
    @ViewById(R.id.rv_rent)
    RecyclerView rvRent;
    @ViewById(R.id.rv_person)
    RecyclerView rvPerson;
    @ViewById(R.id.rv_factor)
    RecyclerView rvFactor;
    @ViewById(R.id.iv_close)
    ImageView ivClose;
    @Extra
    boolean isBack;

    private String mPerson = "", mRent = "", mFactor = "";
    private Map<Integer, String> factorMap = new HashMap<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new WantFindPresenter();
        mPresenter.attachView(this);
        ivClose.setVisibility(isBack ? View.VISIBLE : View.GONE);
        initViews();
        data();
        mPresenter.getFactorList();
    }

    private void initViews() {
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvPerson.setLayoutManager(layoutManager1);
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 2);
        rvRent.setLayoutManager(layoutManager2);
        GridLayoutManager layoutManager3 = new GridLayoutManager(context, 2);
        rvFactor.setLayoutManager(layoutManager3);
    }

    private void data() {
        mPerson = TextUtils.isEmpty(SpUtils.getWantFindPerson()) ? "" : SpUtils.getWantFindPerson();
        mRent = TextUtils.isEmpty(SpUtils.getWantFindRent()) ? "" : SpUtils.getWantFindRent();
        if (!TextUtils.isEmpty(SpUtils.getWantFindFactor())) {
            mFactor = SpUtils.getWantFindFactor();
            String[] list = SpUtils.getWantFindFactor().split(",");
            for (String s : list) {
                factorMap.put(Integer.parseInt(s), "");
            }
        } else {
            mFactor = "";
        }
        PersonAdapter personAdapter = new PersonAdapter(context, mPerson, CommonList.peopleNumList());
        personAdapter.setListener(this);
        rvPerson.setAdapter(personAdapter);
        RentAdapter rentAdapter = new RentAdapter(context, mRent, CommonList.rentTimeList());
        rentAdapter.setListener(this);
        rvRent.setAdapter(rentAdapter);
    }

    @Click(R.id.iv_close)
    void closeClick() {
        finish();
    }

    @Click(R.id.btn_skip)
    void skipClick() {
        SpUtils.saveFindDate();
        gotoActivity();
    }

    @Click(R.id.btn_save)
    void saveClick() {
        if (TextUtils.isEmpty(mPerson) ||
                TextUtils.isEmpty(mRent)) {
            shortTip("还有资料没填哦～");
            return;
        }
        mPresenter.save(mPerson, mRent, mFactor);
    }

    private void saveData() {
        SpUtils.saveWantFind();
        SpUtils.saveWantFindData(mPerson, mRent, mFactor);
        gotoActivity();
    }

    private void gotoActivity() {
        if (isBack) {
            finish();
            return;
        }
        if (TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole())) {
            LoginActivity_.intent(context).start();
        } else {
            MainActivity_.intent(context).start();
        }
    }

    @Override
    public void factorResult(Map<Integer, String> map) {
        factorMap = map;
        mFactor = CommonHelper.getKey(map);
    }

    @Override
    public void personResult(String value) {
        mPerson = value;
    }

    @Override
    public void rentResult(String value) {
        mRent = value;
    }

    @Override
    public void factorSuccess(List<DirectoryBean.DataBean> list) {
        FactorAdapter factorAdapter = new FactorAdapter(context, factorMap, list);
        factorAdapter.setListener(this);
        rvFactor.setAdapter(factorAdapter);
    }

    @Override
    public void saveSuccess() {
        saveData();
    }
}
