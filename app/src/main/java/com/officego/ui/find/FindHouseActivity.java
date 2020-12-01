package com.officego.ui.find;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.DecorationTypeAdapter;
import com.officego.ui.adapter.HouseUniqueAdapter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.utils.GotoActivityUtils;
import com.officego.utils.SpaceItemDecoration;
import com.officego.view.SeekBarPressure;
import com.officego.commonlib.view.TitleBarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:find house
 **/
@SuppressLint("Registered")
@EActivity(R.layout.find_house_activity)
public class FindHouseActivity extends BaseMvpActivity<FindhousePresenter>
        implements FindHouseContract.View,
        SeekBarPressure.OnSeekBarChangeListener,
        HouseUniqueAdapter.onSelectedHouseListener,
        DecorationTypeAdapter.onSelectedDecorationListener {
    @ViewById(R.id.title_bar)
    TitleBarView titleBar;
    @ViewById(R.id.rb_office)
    RadioButton rbOffice;
    @ViewById(R.id.rb_joint_work)
    RadioButton rbJointWork;
    @ViewById(R.id.rv_decoration_type)
    RecyclerView rvDecorationType;
    @ViewById(R.id.tv_decoration_type)
    TextView tvDecorationType;
    @ViewById(R.id.rv_house_characteristic)
    RecyclerView rvHouseUnique;
    //选择面积租金工位
    @ViewById(R.id.tv_area)
    TextView tvArea;
    @ViewById(R.id.sbp_area)
    SeekBarPressure sbpArea;
    @ViewById(R.id.tv_rent)
    TextView tvRent;
    @ViewById(R.id.sbp_rent)
    SeekBarPressure sbpRent;
    @ViewById(R.id.tv_workstation)
    TextView tvWorkstation;
    @ViewById(R.id.sbp_workstation)
    SeekBarPressure sbpseats;
    //共享办公 租金/工位
    @ViewById(R.id.sbp_rent2)
    SeekBarPressure sbpRent2;
    @ViewById(R.id.sbp_workstation2)
    SeekBarPressure sbpseats2;
    /**
     * * btype 	是 	int 	类型,1:楼盘 写字楼,2:网点 共享办公
     * * constructionArea 	是 	String 	建筑面积 范围值,逗号分隔
     * * rentPrice 	是 	String 	租金 范围值,逗号分隔
     * * seats 	是 	String 	工位数范围,逗号分隔
     * * decoration 	是 	String 	装修类型 字典数据
     * * houseTags 	是 	String 	房源特色,字典数据
     * * ** ** ** ** ** ** ** ** ** ** ** ** ** *
     * 共享办公
     * 租金： 范围 0 - 10万   默认 2000 - 20000
     * 工位： 范围 0 - 20    默认 2 -10
     * <p>
     * 写字楼
     * 面积： 范围 0 - 1000    默认 0 - 200
     * 租金： 范围 0- 10    默认 0 - 4
     * 工位： 范围 0 - 500     默认 0 - 100
     */
    private String btype;
    private String constructionArea = "0,200";
    private String rentPrice = "0,4", rentPrice2 = "2000,20000"; //写字楼，共享办公
    private String seats = "0,100", seats2 = "2,10";//写字楼，共享办公
    private String decoration = "";//装修类型
    private String houseTags = "";//装修特色

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new FindhousePresenter(context);
        mPresenter.attachView(this);
        titleBar.getRightTextView().setOnClickListener(v -> GotoActivityUtils.gotoMainActivity(context));
        initSeekBarListener();
        tvArea.setText(Html.fromHtml(getString(R.string.str_text_area)));
        tvRent.setText(Html.fromHtml(getString(R.string.str_text_rent)));
        tvWorkstation.setText(Html.fromHtml(getString(R.string.str_text_workstation)));
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvDecorationType.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager1);
        mPresenter.getDecoratedType();
        mPresenter.getHouseUnique();
        initSeekBar();
    }

    private void initSeekBarListener() {
        sbpArea.setOnSeekBarChangeListener(this, 0);
        sbpRent.setOnSeekBarChangeListener(this, 1);
        sbpseats.setOnSeekBarChangeListener(this, 2);
        sbpRent2.setOnSeekBarChangeListener(this, 3);
        sbpseats2.setOnSeekBarChangeListener(this, 4);
    }

    private void initSeekBar() {
        sbpArea.setProgressMax(1000, 1000);
        sbpRent.setProgressMax(10, 10);
        sbpseats.setProgressMax(500, 500);
        sbpRent2.setProgressMax(100000, 100000);
        sbpseats2.setProgressMax(20, 20);
    }

    /**
     * 写字楼
     */
    @Click(R.id.rb_office)
    void officeClick() {
        tvDecorationType.setVisibility(View.VISIBLE);
        rvDecorationType.setVisibility(View.VISIBLE);
        tvArea.setVisibility(View.VISIBLE);
        sbpArea.setVisibility(View.VISIBLE);
        sbpRent.setVisibility(View.VISIBLE);
        sbpseats.setVisibility(View.GONE);// 办公室没有工位
        sbpRent2.setVisibility(View.GONE);
        sbpseats2.setVisibility(View.GONE);
    }

    /**
     * 共享办公
     */
    @Click(R.id.rb_joint_work)
    void jointWorkClick() {
        tvDecorationType.setVisibility(View.GONE);
        rvDecorationType.setVisibility(View.GONE);
        tvArea.setVisibility(View.GONE);
        sbpArea.setVisibility(View.GONE);
        sbpRent.setVisibility(View.GONE);
        sbpseats.setVisibility(View.GONE);
        sbpRent2.setVisibility(View.VISIBLE);
        sbpseats2.setVisibility(View.VISIBLE);
    }

    /**
     * save
     */
    @Click(R.id.btn_save)
    void saveClick() {
        if (isFastClick(1200)) {
            return;
        }
        String mRentPrice, mseats;
        btype = rbOffice.isChecked() ? "1" : "2";
        mRentPrice = rbOffice.isChecked() ? rentPrice : rentPrice2;
        mseats = rbOffice.isChecked() ? seats : seats2;
        mPresenter.wantToFind(btype, constructionArea, mRentPrice, mseats, decoration, houseTags);
    }

    /**
     * 房源特色
     */
    @Override
    public void getHouseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        HouseUniqueAdapter adapter = new HouseUniqueAdapter(context, data);
        adapter.setSelectedHouseListener(this);
        rvHouseUnique.setAdapter(adapter);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Override
    public void getHouseUniqueFail(int code, String msg) {

    }

    /**
     * 装修类型
     */
    @Override
    public void getDecoratedTypeSuccess(List<DirectoryBean.DataBean> data) {
        DecorationTypeAdapter adapter = new DecorationTypeAdapter(context, data);
        adapter.setSelectedDecorationListener(this);
        rvDecorationType.setAdapter(adapter);
        rvDecorationType.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Override
    public void getDecoratedTypeFail(int code, String msg) {

    }

    /**
     * seekBar
     */
    @Override
    public void onProgressBefore() {

    }

    @Override
    public void onProgressChanged(int type, SeekBarPressure seekBar, double progressLow, double progressHigh) {
        if (type == 0) {
            //面积
            constructionArea = (int) progressLow + "," + (int) progressHigh;
        } else if (type == 1) {
            //租金
            rentPrice = (int) progressLow + "," + (int) progressHigh;
        } else if (type == 2) {
            //工位
            seats = (int) progressLow + "," + (int) progressHigh;
        } else if (type == 3) {
            //共享办公租金
            rentPrice2 = (int) progressLow + "," + (int) progressHigh;
        } else if (type == 4) {
            //共享办公工位
            seats2 = (int) progressLow + "," + (int) progressHigh;
        }
    }

    @Override
    public void onProgressAfter() {
    }

    @Override
    public void onSelectedHouseUnique(String data) {
        houseTags = data;//房源特色
    }

    @Override
    public void onSelectedDecoration(String data) {
        decoration = data; //装修类型
    }
}
