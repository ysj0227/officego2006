package com.owner.home;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.HouseDecorationAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;
import com.owner.home.rule.BuildingHouseAreaTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.RentSingleTextWatcher;
import com.owner.home.rule.RentSumTextWatcher;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class AddHouseActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View, RentDialog.SureClickListener,
        FloorTypeDialog.FloorListener, UniqueAdapter.UniqueListener,
        HouseDecorationAdapter.DecorationListener {
    @ViewById(resName = "sil_house_title")
    SettingItemLayout silHouseTitle;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "et_seat_start")
    EditText etSeatStart;
    @ViewById(resName = "et_seat_end")
    EditText etSeatEnd;
    @ViewById(resName = "sil_rent_single")
    SettingItemLayout silRentSingle;
    @ViewById(resName = "tv_rent_sum_tip")
    TextView tvRentSumTip;
    @ViewById(resName = "sil_rent_sum")
    SettingItemLayout silRentSum;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    //第几层 总楼层
    @ViewById(resName = "et_floors")
    EditText etFloors;
    @ViewById(resName = "tv_counts_floor")
    TextView tvCountsFloor;
    //净高
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    @ViewById(resName = "sil_tier_height")
    SettingItemLayout silTierHeight;
    @ViewById(resName = "sil_rent_time")
    SettingItemLayout silRentTime;
    @ViewById(resName = "sil_free_rent")
    SettingItemLayout silFreeRent;
    //介绍
    @ViewById(resName = "cet_desc_content")
    ClearableEditText cetDescContent;
    @ViewById(resName = "rv_decoration_type")
    RecyclerView rvDecorationType;
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;

    //记录上次面积
    private String recordArea;
    //面积是否修改
    private boolean isFixArea;
    //租金总价
    private int rentCounts;
    //特色
    private Map<Integer, String> uniqueMap;
    //装修类型
    private int decorationId;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
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

    private void initDigits() {
        //标题 长度最大25
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silHouseTitle.getEditTextView(), 25);
        //面积 10-100000正数数字，保留2位小数，单位 M
        silArea.getEditTextView().addTextChangedListener(new BuildingHouseAreaTextWatcher(context, silArea.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context,silStoreyHeight.getEditTextView()));
        silTierHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context,silTierHeight.getEditTextView()));
        //最短租期
        silRentTime.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 60, silRentTime.getEditTextView()));
        //租金单价
        silRentSingle.getEditTextView().addTextChangedListener(new RentSingleTextWatcher(context, silRentSingle.getEditTextView()));
        //租金总价
        silRentSum.getEditTextView().addTextChangedListener(new RentSumTextWatcher(context, silRentSum.getEditTextView()));
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

    @FocusChange(resName = "et_seat_start")
    void onStartSeatFocusChange(View view, boolean isFocus) {
        if (isFocus) setSeats();
    }

    @FocusChange(resName = "et_seat_end")
    void onEndSeatFocusChange(View view, boolean isFocus) {
        if (isFocus) setSeats();
    }

    @Click(resName = "tv_rent_sum_tip")
    void textRentSumTipOnClick() {
        silRentSum.setEditText(rentCounts + "");
        tvRentSumTip.setVisibility(View.GONE);
    }

    private void itemListener() {
        //租金总价
        silRentSum.getEditTextView().setOnFocusChangeListener((view, b) -> {
            String area = silArea.getEditTextView().getText().toString();//整数
            String rentSingle = silRentSingle.getEditTextView().getText().toString();//保留两位小数
            if (!TextUtils.isEmpty(area) && !TextUtils.isEmpty(rentSingle)) {
                if (b) {
                    rentCounts = (int) (Float.valueOf(rentSingle) * Float.valueOf(area) * 30);
                    tvRentSumTip.setVisibility(View.VISIBLE);
                    tvRentSumTip.setText("租金总价：" + rentCounts + "元/月");
                } else {
                    tvRentSumTip.setVisibility(View.GONE);
                }
            }
        });
        //面积监听 是否修改过
        silArea.getEditTextView().setOnFocusChangeListener((view, b) -> {
            if (b) {
                isFixArea = !TextUtils.equals(recordArea, silArea.getEditTextView().getText().toString());
            }
        });
        silArea.getEditTextView().addTextChangedListener(new MyTextWatcher(recordArea));
    }

    @SuppressLint("SetTextI18n")
    private void setSeats() {
        String seatStart = etSeatStart.getText().toString();
        String seatEnd = etSeatEnd.getText().toString();
        String area = silArea.getEditTextView().getText().toString();
        if (!TextUtils.isEmpty(area)) {
            if (isFixArea || (TextUtils.isEmpty(seatStart) && TextUtils.isEmpty(seatEnd))) {
                recordArea = silArea.getEditTextView().getText().toString();
                etSeatStart.setText((Integer.valueOf(area) / 5) + "");
                etSeatEnd.setText((Integer.valueOf(area) / 3) + "");
            }
        }
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        UniqueAdapter adapter = new UniqueAdapter(context, uniqueMap, data);
        adapter.setListener(this);
        rvHouseUnique.setAdapter(adapter);
    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {
        HouseDecorationAdapter decorationAdapter = new HouseDecorationAdapter(context, decorationId, data);
        decorationAdapter.setListener(this);
        rvDecorationType.setAdapter(decorationAdapter);
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }

    @Override
    public void sureFloor(String text) {
        silFloorNo.setLeftToArrowText(text);
    }

    @Override
    public void decorationResult(int decId) {
        this.decorationId = decId;
    }

    @Override
    public void uniqueResult(Map<Integer, String> uniqueMap) {
        this.uniqueMap = uniqueMap;
    }

    private class MyTextWatcher implements TextWatcher {
        private String text;

        MyTextWatcher(String text) {
            this.text = text;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            isFixArea = false;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.equals(text, editable.toString())) {
                isFixArea = true;
            }
        }
    }
}
