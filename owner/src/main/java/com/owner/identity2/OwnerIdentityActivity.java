package com.owner.identity2;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.adapter.SearchAdapter;
import com.owner.dialog.IdentityTypeDialog;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity2.contract.IdentityContract;
import com.owner.identity2.presenter.IdentityPresenter;
import com.owner.utils.CommUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/17
 * 认证
 **/
@EActivity(resName = "activity_owner_identity")
public class OwnerIdentityActivity extends BaseMvpActivity<IdentityPresenter>
        implements IdentityContract.View, IdentityTypeDialog.IdentityTypeListener,
        SearchAdapter.IdentityBuildingListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CREATE_BUILDING = 0xa3;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "tv_reject_reason")
    TextView tvRejectReason;
    @ViewById(resName = "iv_expand")
    ImageView ivExpand;
    @ViewById(resName = "cet_name")
    EditText cetName;
    @ViewById(resName = "tv_address")
    TextView tvAddress;
    @ViewById(resName = "tv_building_flay")
    TextView tvBuildingFlay;
    @ViewById(resName = "iv_edit")
    ImageView ivEdit;
    @ViewById(resName = "iv_delete")
    ImageView ivDelete;
    @ViewById(resName = "sil_select_type")
    SettingItemLayout silSelectType;
    @ViewById(resName = "rv_recommend_building")
    RecyclerView rvRecommendBuilding;
    @ViewById(resName = "btn_upload")
    Button btnUpload;
    @ViewById(resName = "include_business_license")
    View includeBusinessLicense;
    @ViewById(resName = "include_owner_personal_id")
    View includeOwnerPersonalId;

    //是否展开
    private boolean isSpread;
    //搜索
    private SearchAdapter searchAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new IdentityPresenter();
        mPresenter.attachView(this);
        initViews();
    }

    private void initViews() {
        //搜索列表
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        searchBuilding();
    }

    @Click(resName = "iv_expand")
    void spreadOnClick() {
        ivExpand.setBackgroundResource(isSpread ?
                com.officego.commonlib.R.mipmap.ic_down_arrow_gray :
                com.officego.commonlib.R.mipmap.ic_up_arrow_gray);
        tvRejectReason.setSingleLine(isSpread);
        isSpread = !isSpread;
    }

    @Click(resName = "sil_select_type")
    void selectTypeOnClick() {
        new IdentityTypeDialog(context).setListener(this);
    }

    @Click(resName = "iv_edit")
    void editOnClick() {
        OwnerCreateBuildingActivity_.intent(context)
                .isEdit(true)
                .startForResult(REQUEST_CREATE_BUILDING);
    }

    @Click(resName = "iv_delete")
    void deleteOnClick() {
        cetName.setText("");
        cetName.setEnabled(true);
        tvAddress.setVisibility(View.INVISIBLE);
        tvBuildingFlay.setVisibility(View.GONE);
        ivEdit.setVisibility(View.GONE);
        hideSearchListView();
    }

    @Override
    public void sureType(String text, int type) {
        //1公司 2个人（自定义参数）
        silSelectType.setCenterText(text);
        includeBusinessLicense.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        includeOwnerPersonalId.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
    }

    //search
    private void searchBuilding() {
        cetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showSearchListView();
                mPresenter.searchBuilding(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(context, mList);
            searchAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(searchAdapter);
            return;
        }
        searchAdapter.setData(mList);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            String inputTx = cetName.getText().toString().trim();
            OwnerCreateBuildingActivity_.intent(context)
                    .inputText(inputTx)
                    .startForResult(REQUEST_CREATE_BUILDING);//创建楼盘、网点
        } else {
            buildingFlayView(bean.getBuildType());
            tvAddress.setVisibility(View.VISIBLE);
            CommUtils.showHtmlView(cetName, bean.getBuildingName());//名称
            CommUtils.showHtmlTextView(tvAddress, bean.getAddress());//地址
            cetName.setSelection(cetName.getText().length());//光标
            cetName.setEnabled(false);
            hideSearchListView();
        }
        ivDelete.setVisibility(View.VISIBLE);
    }

    //创建楼盘成功的回调
    @OnActivityResult(REQUEST_CREATE_BUILDING)
    void onCreateBuildingResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int buildingType = data.getIntExtra("buildingType", 1);
            String buildingName = data.getStringExtra("buildingName");
            String buildingAddress = data.getStringExtra("buildingAddress");
            buildingFlayView(buildingType);
            cetName.setText(buildingName);
            tvAddress.setText(buildingAddress);
            tvAddress.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
            cetName.setEnabled(false);
            hideSearchListView();
        }
    }

    private void showSearchListView() {
        btnUpload.setVisibility(View.GONE);
        rvRecommendBuilding.setVisibility(View.VISIBLE);
    }

    private void hideSearchListView() {
        btnUpload.setVisibility(View.VISIBLE);
        rvRecommendBuilding.setVisibility(View.GONE);
    }

    private void buildingFlayView(int type) {
        tvBuildingFlay.setVisibility(View.VISIBLE);
        if (Constants.TYPE_BUILDING == type) {
            tvBuildingFlay.setText("楼盘");
            tvBuildingFlay.setBackgroundResource(com.officego.commonlib.R.drawable.label_corners_solid_blue);
            silSelectType.setVisibility(View.VISIBLE);
            includeBusinessLicense.setVisibility(View.VISIBLE);
            includeOwnerPersonalId.setVisibility(View.GONE);
        } else {
            tvBuildingFlay.setText("网点");
            tvBuildingFlay.setBackgroundResource(com.officego.commonlib.R.drawable.label_corners_solid_purple);
            silSelectType.setVisibility(View.GONE);
            includeBusinessLicense.setVisibility(View.VISIBLE);
            includeOwnerPersonalId.setVisibility(View.GONE);
        }
    }
}
