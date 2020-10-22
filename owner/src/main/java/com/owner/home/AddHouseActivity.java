package com.owner.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.HouseDecorationAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class AddHouseActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View, RentDialog.SureClickListener,
        FloorTypeDialog.FloorListener {
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_rent_single")
    SettingItemLayout silRentSingle;
    @ViewById(resName = "tv_rent_sum_tip")
    TextView tvRentSumTip;
    @ViewById(resName = "sil_rent_sum")
    SettingItemLayout silRentSum;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    @ViewById(resName = "sil_free_rent")
    SettingItemLayout silFreeRent;

    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    @ViewById(resName = "rv_decoration_type")
    RecyclerView rvDecorationType;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    private float rentCounts;//租金总价

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
        initViews();
        itemListener();
        mPresenter.getDecoratedType();
        mPresenter.getHouseUnique();
    }

    private void initViews() {
        //特色
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
        //装修类型
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvDecorationType.setLayoutManager(layoutManager1);
        rvDecorationType.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        UploadVideoVrActivity_.intent(context).start();
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    @Click(resName = "sil_floor_no")
    void floorNoOnClick() {
        new FloorTypeDialog(context).setListener(this);
    }

    @Click(resName = "sil_free_rent")
    void freeRentOnClick() {
        new RentDialog(context, getString(R.string.str_free_rent)).setSureListener(this);
    }

    @Click(resName = "iv_seat_tip")
    void seatOnClick() {
        CommonDialog dialog = new CommonDialog.Builder(this)
                .setTitle("可置工位根据面积生成，可修改")
                .setConfirmButton("我知道了", (dialog12, which) -> {
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    @Click(resName = "sil_rent_sum")
    void rentSumOnClick() {
        CommonDialog dialog = new CommonDialog.Builder(this)
                .setTitle("总价根据单价生成，可修改")
                .setMessage("总价计算规则：单价X面积X天数\n总价：0.3万元/月（1元 X 100㎡ X 30天）")
                .setConfirmButton("我知道了", (dialog12, which) -> {
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    private void itemListener() {
        silRentSum.getEditTextView().setOnFocusChangeListener((view, b) -> {
            String area = silArea.getEditTextView().getText().toString();//整数
            String rentSingle = silRentSingle.getEditTextView().getText().toString();//保留两位小数
            if (!TextUtils.isEmpty(area) && !TextUtils.isEmpty(rentSingle)) {
                if (b) {
                    rentCounts = Integer.valueOf(rentSingle) * Float.valueOf(area) * 30;
                    tvRentSumTip.setVisibility(View.VISIBLE);
                    tvRentSumTip.setText("租金总价：" + rentCounts + "元/月");
                } else {
                    tvRentSumTip.setVisibility(View.GONE);
                }
            }
        });
        tvRentSumTip.setOnClickListener(view -> {
            silRentSum.setEditText(rentCounts + "");
            tvRentSumTip.setVisibility(View.GONE);
        });
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        rvHouseUnique.setAdapter(new UniqueAdapter(context,new HashMap<>(), data));
    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {
        rvDecorationType.setAdapter(new HouseDecorationAdapter(context, data));
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }

    @Override
    public void sureFloor(String text) {
        silFloorNo.setLeftToArrowText(text);
    }
}
