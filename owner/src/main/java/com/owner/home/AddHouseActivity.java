package com.owner.home;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.adapter.HouseDecorationAdapter;
import com.owner.adapter.HouseUniqueAdapter;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class AddHouseActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View, RentDialog.SureClickListener {
    @ViewById(resName = "sil_building_type")
    SettingItemLayout silBuildingType;
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

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
        initViews();
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
    @Click(resName = "sil_floor_no")
    void floorNoOnClick() {
        new FloorTypeDialog(context);
    }
    @Click(resName = "sil_free_rent")
    void freeRentOnClick() {
        new RentDialog(context).setSureListener(this);
    }

    @Click(resName = "iv_seat_tip")
    void seatOnClick() {
        CommonDialog dialog = new CommonDialog.Builder(this)
                .setMessage("可置工位根据面积生成，可修改")
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

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        rvHouseUnique.setAdapter(new HouseUniqueAdapter(context, data));
    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {
        rvDecorationType.setAdapter(new HouseDecorationAdapter(context, data));
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }
}
