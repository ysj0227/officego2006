package com.owner.home;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.R;
import com.owner.adapter.HouseUniqueAdapter;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.JointWorkContract;
import com.owner.home.presenter.JointWorkPresenter;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_jointwork_manager")
public class AddJointWorkActivity extends BaseMvpActivity<JointWorkPresenter>
        implements JointWorkContract.View {
    @ViewById(resName = "iv_mark_image_lift")
    ImageView ivMarkImageLift;
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    @ViewById(resName = "rv_create_service")
    RecyclerView rvCreateService;
    @ViewById(resName = "rv_base_service")
    RecyclerView rvBaseService;
    @ViewById(resName = "rv_join_company")
    RecyclerView rvJoinCompany;
    //加入企业
    private JointCompanyAdapter adapter;
    private List<String> jointCompanyList = new ArrayList<String>();
    //服务
    private List<DirectoryBean.DataBean> serviceList = new ArrayList<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initViews();
        mPresenter.getHouseUnique();
    }

    private void initViews() {
        ivMarkImageLift.setVisibility(View.INVISIBLE);
        //入住企业
        rvJoinCompany.setLayoutManager(new LinearLayoutManager(context));
        jointCompanyList.add(0, "");
        adapter = new JointCompanyAdapter(this, jointCompanyList);
        rvJoinCompany.setAdapter(adapter);
        //特色
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        UploadVideoVrActivity_.intent(context).start();
    }

    //service logo dialog
    @Click(resName = "iv_arrow_create")
    void serviceCompanyClick() {
        mPresenter.getCompanyService();
    }

    @Click(resName = "iv_arrow_base")
    void serviceBaseClick() {
        mPresenter.getBaseService();
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        rvHouseUnique.setAdapter(new HouseUniqueAdapter(context, data));
    }

    @Override
    public void baseServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, getString(R.string.str_title_base_sservice), data);
    }

    @Override
    public void companyServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, getString(R.string.str_title_create_service), data);
    }


}
