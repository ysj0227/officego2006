package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.ui.adapter.FactorAdapter;
import com.officego.ui.adapter.PersonAdapter;
import com.officego.ui.adapter.RentAdapter;
import com.officego.ui.find.WantFindContract;
import com.officego.ui.find.WantFindPresenter;
import com.officego.ui.login.CommonLoginTenant;
import com.officego.utils.CommonList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2021/2/25
 * 定制找房
 **/
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.activity_customised_house)
public class CustomisedHouseActivity extends BaseMvpActivity<WantFindPresenter>
        implements WantFindContract.View, PersonAdapter.PersonListener,
        RentAdapter.RentListener, FactorAdapter.FactorListener {
    @ViewById(R.id.rv_rent)
    RecyclerView rvRent;
    @ViewById(R.id.rv_person)
    RecyclerView rvPerson;
    @ViewById(R.id.rv_factor)
    RecyclerView rvFactor;
    @ViewById(R.id.tv_content_right1)
    TextView tvContentRight1;
    @ViewById(R.id.tv_content_right2)
    TextView tvContentRight2;
    @ViewById(R.id.tv_content_right3)
    TextView tvContentRight3;
    @ViewById(R.id.rl_right1)
    RelativeLayout rlRight1;
    @ViewById(R.id.rl_left3)
    RelativeLayout rlLeft3;
    @ViewById(R.id.rl_item2)
    RelativeLayout rlItem2;
    @ViewById(R.id.rl_right2)
    RelativeLayout rlRight2;
    @ViewById(R.id.rl_left4)
    RelativeLayout rlLeft4;
    @ViewById(R.id.rl_item3)
    RelativeLayout rlItem3;
    @ViewById(R.id.rl_right3)
    RelativeLayout rlRight3;
    @ViewById(R.id.iv_right1)
    ImageView ivRight1;
    @ViewById(R.id.iv_right2)
    ImageView ivRight2;
    @ViewById(R.id.iv_right3)
    ImageView ivRight3;

    private String mPerson = "", mRent = "", mFactor = "";
    private Map<Integer, String> factorMap = new HashMap<>();

    @AfterViews
    void init() {
        mPresenter = new WantFindPresenter();
        mPresenter.attachView(this);
        mPresenter.getFactorList();
        initViews();
        data();
    }

    private void initViews() {
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvPerson.setLayoutManager(layoutManager1);
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 2);
        rvRent.setLayoutManager(layoutManager2);
        GridLayoutManager layoutManager3 = new GridLayoutManager(context, 2);
        rvFactor.setLayoutManager(layoutManager3);

        if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.avaOoptions())
                    .load(SpUtils.getHeaderImg()).into(ivRight1);
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.avaOoptions())
                    .load(SpUtils.getHeaderImg()).into(ivRight2);
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.avaOoptions())
                    .load(SpUtils.getHeaderImg()).into(ivRight3);
        }
    }

    private void data() {
        PersonAdapter personAdapter = new PersonAdapter(context, mPerson, CommonList.peopleNumList(), true);
        personAdapter.setListener(this);
        rvPerson.setAdapter(personAdapter);
        RentAdapter rentAdapter = new RentAdapter(context, mRent, CommonList.rentTimeList(), true);
        rentAdapter.setListener(this);
        rvRent.setAdapter(rentAdapter);
    }

    @Click(R.id.iv_close)
    void closeClick() {
        finish();
    }

    @Click(R.id.btn_save)
    void saveClick() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new CommonLoginTenant(context);
            return;
        }
        if (TextUtils.isEmpty(mPerson) || TextUtils.isEmpty(mRent)) {
            shortTip("还有资料没填哦～");
            return;
        }
        mPresenter.save(mPerson, mRent, mFactor);
    }

    @Override
    public void personResult(String key, String value) {
        mPerson = value;
        tvContentRight1.setText(key);
        rlRight1.setVisibility(View.VISIBLE);
        rlLeft3.setVisibility(View.VISIBLE);
        rlItem2.setVisibility(View.VISIBLE);
    }

    @Override
    public void rentResult(String key, String value) {
        mRent = value;
        tvContentRight2.setText(key);
        rlRight2.setVisibility(View.VISIBLE);
        rlLeft4.setVisibility(View.VISIBLE);
        rlItem3.setVisibility(View.VISIBLE);
    }

    @Override
    public void factorResult(Map<Integer, String> map) {
        factorMap = map;
        mFactor = CommonHelper.getKey(map);
        tvContentRight3.setText(CommonHelper.readValue(map));
        rlRight3.setVisibility(View.VISIBLE);
    }

    @Override
    public void factorSuccess(List<DirectoryBean.DataBean> list) {
        FactorAdapter factorAdapter = new FactorAdapter(context, factorMap, list, true);
        factorAdapter.setListener(this);
        rvFactor.setAdapter(factorAdapter);
    }

    @Override
    public void saveSuccess() {
        dialogSubmit();
    }

    private void dialogSubmit() {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).
                inflate(R.layout.dialog_customized_house_success, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            viewLayout.findViewById(R.id.btn_know).setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
            dialog.show();
        }
    }
}
