package com.officego.ui.find;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.FactorAdapter;
import com.officego.ui.adapter.PersonAdapter;
import com.officego.ui.adapter.RentAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.activity_want_to_find)
public class WantToFindActivity extends BaseActivity implements PersonAdapter.PersonListener,
        RentAdapter.RentListener, FactorAdapter.FactorListener {
    @ViewById(R.id.rv_rent)
    RecyclerView rvRent;
    @ViewById(R.id.rv_person)
    RecyclerView rvPerson;
    @ViewById(R.id.rv_factor)
    RecyclerView rvFactor;


    private String mPerson = "", mRent = "", mFactor = "";
    private Map<Integer, String> factorMap = new HashMap<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        initViews();
        data();
    }

    private void initViews() {
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 2);
        rvPerson.setLayoutManager(layoutManager1);
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 2);
        rvRent.setLayoutManager(layoutManager2);
        GridLayoutManager layoutManager3 = new GridLayoutManager(context, 2);
        rvFactor.setLayoutManager(layoutManager3);
    }

    private void data() {
        PersonAdapter personAdapter = new PersonAdapter(context, rvPerson, mPerson, CommonFindList.peopleNumList());
        personAdapter.setListener(this);
        rvPerson.setAdapter(personAdapter);
        RentAdapter rentAdapter = new RentAdapter(context, rvRent, mRent, CommonFindList.rentTimeList());
        rentAdapter.setListener(this);
        rvRent.setAdapter(rentAdapter);
        FactorAdapter factorAdapter = new FactorAdapter(context, factorMap, CommonFindList.factorList());
        factorAdapter.setListener(this);
        rvFactor.setAdapter(factorAdapter);
    }

    @Click(R.id.iv_close)
    void closeClick() {
        finish();
    }

    @Click(R.id.btn_skip)
    void skipClick() {
        SpUtils.saveFindDate();
    }

    @Click(R.id.btn_save)
    void saveClick() {

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
}
