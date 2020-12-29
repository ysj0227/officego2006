package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.find.WantFindBean;
import com.officego.ui.home.model.BusinessCircleBean;
import com.officego.ui.home.model.MeterBean;
import com.officego.utils.CommonList;
import com.officego.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
public class SearchPopupWindow extends PopupWindow implements
        View.OnTouchListener,
        PopupWindow.OnDismissListener {
    private final int spanCount = 3;
    private final int spacing = 15;
    private final Activity mContext;
    private final TextView mSetTitleView;

    private HashSet<Integer> mHashSetLine, mHashSetBusiness;
    private SparseBooleanArray mCheckStatesLine, mCheckStatesBusiness;//记录选中的位置
    private String district = "", business = "";//商圈
    private String line = "", nearbySubway = ""; //地铁
    private int btype;//楼盘,网点
    private String sort;//排序
    //筛选
    private List<DirectoryBean.DataBean> decorationList = new ArrayList<>();
    private List<DirectoryBean.DataBean> buildingUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> jointWorkUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> brandList = new ArrayList<>();

    //layout 类型
    private final int SEARCH_TYPE_AREA = 0;
    private final int SEARCH_TYPE_OFFICE = 1;
    private final int SEARCH_TYPE_ORDER = 2;
    private final int SEARCH_TYPE_CONDITION = 3;
    private int layout;
    //搜索类型
    private final int mSearchType;
    //height
    private int screenHeight, statusBarHeight, titleBarHeight, searchHeight, bottomTabBarHeight;

    //获取地铁列表
    private List<MeterBean.DataBean> meterList = new ArrayList<>();
    //商圈列表
    private List<BusinessCircleBean.DataBean> businessCircleList = new ArrayList<>();

    private onSureClickListener onSureClickListener;

    public SearchPopupWindow.onSureClickListener getOnSureClickListener() {
        return onSureClickListener;
    }

    void setOnSureClickListener(SearchPopupWindow.onSureClickListener onSureClickListener) {
        this.onSureClickListener = onSureClickListener;
    }

    interface onSureClickListener {
        //区域确定
        void onSurePopUpWindow(boolean isLine, HashSet<Integer> hashSet, SparseBooleanArray checkStates,
                               String data1, String data2);

        //办公
        void onOfficeTypePopUpWindow(int searchType, int officeType, int officeText);

        //排序
        void onOfficeOrderPopUpWindow(int searchType, String order);

        //筛选
        void onConditionPopUpWindow(int searchType, int btype, String constructionArea, String rentPrice,
                                    String simple, String decoration, String tags, Map<Integer, String> mMapDecoration);
    }

    //popupWindow设置参数
    private void init(View view) {
        //背景
        ColorDrawable cd = new ColorDrawable(0x99000000);
        setBackgroundDrawable(cd);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setTouchable(true);
        setOutsideTouchable(true);
        //获取isShowing()的状态
        setFocusable(true);
        update();
        //事件的监听
        view.setOnTouchListener(this);
        this.setTouchInterceptor(this);
        this.setOnDismissListener(this);
    }


    /**
     * @param activity             activity
     * @param topToPopupWindowView topToPopupWindowView
     * @param setTextView          setTextView
     * @param searchType           0区域 1写字楼、网点  2排序 3筛选
     */
    @SuppressLint("ClickableViewAccessibility")
    public SearchPopupWindow(Activity activity, View topToPopupWindowView,
                             TextView setTextView, int searchType,
                             int btype, HashSet<Integer> hashSet, SparseBooleanArray checkStates,
                             String district, String business, String line,
                             String nearbySubway, String sort,
                             List<DirectoryBean.DataBean> decorationList,
                             List<DirectoryBean.DataBean> buildingUniqueList,
                             List<DirectoryBean.DataBean> jointWorkUniqueList,
                             List<DirectoryBean.DataBean> brandList) {
        super();
        this.mContext = activity;
        this.mSetTitleView = setTextView;
        this.mSearchType = searchType;
        this.btype = btype;
        if (TextUtils.isEmpty(district)) {
            this.mHashSetLine = hashSet;
            this.mCheckStatesLine = checkStates;
            if (this.mHashSetBusiness != null) {
                this.mHashSetBusiness.clear();
            }
            if (this.mCheckStatesBusiness != null) {
                this.mCheckStatesBusiness.clear();
            }
        } else {
            this.mHashSetBusiness = hashSet;
            this.mCheckStatesBusiness = checkStates;
            if (this.mHashSetLine != null) {
                this.mHashSetLine.clear();
            }
            if (this.mCheckStatesLine != null) {
                this.mCheckStatesLine.clear();
            }
        }
        this.district = district;
        this.business = business;
        this.line = line;
        this.nearbySubway = nearbySubway;
        this.sort = sort;
        //筛选
        this.decorationList = decorationList;
        this.buildingUniqueList = buildingUniqueList;
        this.jointWorkUniqueList = jointWorkUniqueList;
        this.brandList = brandList;
        LogCat.e("TAG", "11111111111 decorationList=" + decorationList.size() + "jointWorkUniqueList ="
                + jointWorkUniqueList.size() + " buildingUniqueList=" + buildingUniqueList.size() + " brandList=" + brandList.size());
        //初始view
        initViews(topToPopupWindowView, searchType);
    }

    private void initViews(View topToPopupWindowView, int searchType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (searchType == SEARCH_TYPE_AREA) {
            layout = R.layout.popup_search_area;
        } else if (searchType == SEARCH_TYPE_OFFICE) {
            layout = R.layout.popup_search_office;
        } else if (searchType == SEARCH_TYPE_ORDER) {
            layout = R.layout.popup_search_order;
        } else if (searchType == SEARCH_TYPE_CONDITION) {
            layout = R.layout.popup_search_condition;
        }
        View viewLayout = inflater.inflate(layout, null);
        setContentView(viewLayout);
        //宽高
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //计算区域高度
        screenHeight = CommonHelper.getScreenHeight(mContext);
        statusBarHeight = CommonHelper.statusHeight(mContext);
        titleBarHeight = (int) mContext.getResources().getDimension(R.dimen.dp_58);
        searchHeight = (int) mContext.getResources().getDimension(R.dimen.dp_50);
        bottomTabBarHeight = (int) mContext.getResources().getDimension(R.dimen.dp_58);
        //动画
        // setAnimationStyle(R.anim.pop_action_sheet_enter);
        //显示位置
        showAsDropDown(topToPopupWindowView);
        init(viewLayout);
        handelLayoutData(searchType, viewLayout);
    }

    private void setImageBackground() {
        if (mSetTitleView != null) {
            mSetTitleView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(mContext, R.mipmap.ic_arrow_down), null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
            int topY = searchHeight + titleBarHeight + statusBarHeight;//状态栏到搜索title的高度
            int bottomY = (int) (mContext.getResources().getDimension(R.dimen.dp_250));
            if (mSearchType == SEARCH_TYPE_OFFICE || mSearchType == SEARCH_TYPE_ORDER) {
                if (this.isShowing() && event.getRawY() < topY || event.getRawY() > topY + bottomY) {
                    dismiss();
                    setImageBackground();
                    return true;
                }
            } else if (mSearchType == SEARCH_TYPE_AREA || mSearchType == SEARCH_TYPE_CONDITION) {
                if (this.isShowing() && event.getRawY() < topY || event.getRawY() > screenHeight - bottomTabBarHeight) {
                    dismiss();
                    setImageBackground();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDismiss() {
        setImageBackground();
    }

    //Android 7.0以上 view显示问题
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置了全屏
            Rect rect = new Rect();
            anchor.getWindowVisibleDisplayFrame(rect);
            Activity activity = (Activity) anchor.getContext();
            Rect outRect1 = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
            int h = outRect1.height() - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    //根据不同的layout处理不同的数据
    private void handelLayoutData(int searchType, View viewLayout) {
        if (searchType == SEARCH_TYPE_AREA) {
            handleArea(viewLayout);
        } else if (searchType == SEARCH_TYPE_OFFICE) {
            handleOffice(viewLayout);
        } else if (searchType == SEARCH_TYPE_ORDER) {
            handleOrder(viewLayout);
        } else if (searchType == SEARCH_TYPE_CONDITION) {
            handleCondition(viewLayout);
        }
    }

    //区域
    private boolean isLine;//是否地铁
    private MeterAdapter meterAdapter;
    private StationAdapter stationAdapter;
    private BusinessCircleAdapter businessCircleAdapter;
    private BusinessCircleDetailsAdapter businessCircleDetailsAdapter;

    @SuppressLint("DefaultLocale")
    private void handleArea(View viewLayout) {
        RecyclerView recyclerViewCenter = viewLayout.findViewById(R.id.rv_center);
        recyclerViewCenter.setLayoutManager(new LinearLayoutManager(mContext));
        RecyclerView recyclerViewRight = viewLayout.findViewById(R.id.rv_right);
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(mContext));
        TextView tvBusinessCircleText = viewLayout.findViewById(R.id.tv_shopping);
        TextView tvBusinessCircleNum = viewLayout.findViewById(R.id.tv_shop_num);
        TextView tvMeterText = viewLayout.findViewById(R.id.tv_meter);
        TextView tvMeterNum = viewLayout.findViewById(R.id.tv_meter_num);
        Button btnClear = viewLayout.findViewById(R.id.btn_clear);
        Button btnSure = viewLayout.findViewById(R.id.btn_sure);
        //获取地铁数据
        getSearchSubwayList(tvMeterText, tvBusinessCircleText, recyclerViewCenter, tvMeterNum, recyclerViewRight);
        //初始化显示商圈
        getSearchDistrictList(tvBusinessCircleText, tvMeterText, recyclerViewCenter, tvBusinessCircleNum, recyclerViewRight);

        if (mHashSetLine != null && mHashSetLine.size() > 0) {
            tvMeterNum.setVisibility(View.VISIBLE);
            tvMeterNum.setText(String.format("%d", mHashSetLine.size()));
            tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else {
            tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
        }
        if (mHashSetBusiness != null && mHashSetBusiness.size() > 0) {
            tvBusinessCircleNum.setVisibility(View.VISIBLE);
            tvBusinessCircleNum.setText(String.format("%d", mHashSetBusiness.size()));
            tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else {
            tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
        }
        //点击监听
        View.OnClickListener clickListener = v -> {
            switch (v.getId()) {
                case R.id.tv_shopping://商圈
                    clearSelectedItem();
                    isLine = false;
                    showBusinessAdapter(tvBusinessCircleText, tvMeterText, recyclerViewCenter, tvBusinessCircleNum, recyclerViewRight);
                    tvMeterNum.setText("");
                    tvMeterNum.setVisibility(View.GONE);
                    break;
                case R.id.tv_meter://地铁
                    clearSelectedItem();
                    isLine = true;
                    showMeterAdapter(tvMeterText, tvBusinessCircleText, recyclerViewCenter, tvMeterNum, recyclerViewRight);
                    tvBusinessCircleNum.setText("");
                    tvBusinessCircleNum.setVisibility(View.GONE);
                    break;
                case R.id.btn_clear://清理
                    if (isLine) {
                        line = "";
                        nearbySubway = "";
                        clearMeterHashSet(tvMeterNum);
                        showMeterAdapter(tvMeterText, tvBusinessCircleText, recyclerViewCenter, tvMeterNum, recyclerViewRight);
                    } else {
                        district = "";
                        business = "";
                        clearBusinessHashSet(tvBusinessCircleNum);
                        showBusinessAdapter(tvBusinessCircleText, tvMeterText, recyclerViewCenter, tvBusinessCircleNum, recyclerViewRight);
                    }
                    clearData();
                    break;
                case R.id.btn_sure://确定
                    clearData();
                    break;
                default:
            }
        };
        tvBusinessCircleText.setOnClickListener(clickListener);
        tvMeterText.setOnClickListener(clickListener);
        btnClear.setOnClickListener(clickListener);
        btnSure.setOnClickListener(clickListener);
    }

    //地铁adapter
    private void showMeterAdapter(TextView tvMeterText, TextView tvBusinessCircleText,
                                  RecyclerView recyclerViewCenter, TextView tvMeterNum,
                                  RecyclerView recyclerViewRight) {
        tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
        meterAdapter = new MeterAdapter(mContext, tvMeterNum, meterList, recyclerViewRight);
        recyclerViewCenter.setAdapter(meterAdapter);
    }

    //商圈adapter
    private void showBusinessAdapter(TextView tvBusinessCircleText, TextView tvMeterText,
                                     RecyclerView recyclerViewCenter,
                                     TextView tvBusinessCircleNum,
                                     RecyclerView recyclerViewRight) {
        tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
        businessCircleAdapter = new BusinessCircleAdapter(mContext, tvBusinessCircleNum, businessCircleList, recyclerViewRight);
        recyclerViewCenter.setAdapter(businessCircleAdapter);
    }

    //切换商圈和地铁的时候清空之前的选项
    private void clearSelectedItem() {
        business = "";
        district = "";
        mapMeter.clear();
        if (businessCircleDetailsAdapter != null) {
            businessCircleDetailsAdapter.setData(null);
            businessCircleDetailsAdapter.notifyDataSetChanged();
        }
        line = "";
        nearbySubway = "";
        mapBusiness.clear();
        if (stationAdapter != null) {
            stationAdapter.setData(null);
            stationAdapter.notifyDataSetChanged();
        }
    }

    private void clearData() {
        dismiss();
        if (isLine) {
            onSureClickListener.onSurePopUpWindow(isLine, mHashSetLine, mCheckStatesLine, line, nearbySubway);
        } else {
            onSureClickListener.onSurePopUpWindow(isLine, mHashSetBusiness, mCheckStatesBusiness, district, business);
        }
    }

    //地铁
    private void getSearchSubwayList(TextView tvMeterText, TextView tvBusinessCircleText,
                                     RecyclerView recyclerViewCenter,
                                     TextView tvMeterNum,
                                     RecyclerView recyclerViewRight) {
        OfficegoApi.getInstance().getSubwayList(new RetrofitCallback<List<MeterBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MeterBean.DataBean> data) {
                meterList.clear();
                meterList.addAll(data);
                if (!TextUtils.isEmpty(line)) {
                    isLine = true;
                    showMeterAdapter(tvMeterText, tvBusinessCircleText, recyclerViewCenter, tvMeterNum, recyclerViewRight);
                }
            }

            @Override
            public void onFail(int code, String msg, List<MeterBean.DataBean> data) {
            }
        });
    }

    //商圈
    private void getSearchDistrictList(TextView tvBusinessCircleText, TextView tvMeterText,
                                       RecyclerView recyclerViewCenter,
                                       TextView tvBusinessCircleNum,
                                       RecyclerView recyclerViewRight) {
        OfficegoApi.getInstance().getDistrictList(new RetrofitCallback<List<BusinessCircleBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BusinessCircleBean.DataBean> data) {
                businessCircleList.clear();
                businessCircleList.addAll(data);
                if (TextUtils.isEmpty(line)) { //初始化默认选中商圈
                    showBusinessAdapter(tvBusinessCircleText, tvMeterText, recyclerViewCenter, tvBusinessCircleNum, recyclerViewRight);
                }
            }

            @Override
            public void onFail(int code, String msg, List<BusinessCircleBean.DataBean> data) {
            }
        });
    }

    //写字楼,共享办公（网点） 类型1:楼盘,2:网点, 0全部
    private void handleOffice(View viewLayout) {
        TextView tvPopAll = viewLayout.findViewById(R.id.tv_pop_all);
        TextView tvPopOffice = viewLayout.findViewById(R.id.tv_pop_office);
        TextView tvPopTenant = viewLayout.findViewById(R.id.tv_pop_tenant);
        if (btype == 0) {
            tvPopAll.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (btype == 1) {
            tvPopOffice.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (btype == 2) {
            tvPopTenant.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        }
        tvPopAll.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(mSearchType, 0, R.string.str_house_all);
        });
        tvPopOffice.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(mSearchType, 1, R.string.str_house_office);
        });
        tvPopTenant.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(mSearchType, 2, R.string.str_house_tenant);
        });
    }

    //排序 0默认 1价格从高到低 2价格从低到高 3面积从大到小 4面积从小到大
    private void handleOrder(View viewLayout) {
        TextView tvPopOrderDef = viewLayout.findViewById(R.id.tv_pop_order_def);
        TextView tvPopPriceUpDown = viewLayout.findViewById(R.id.tv_pop_price_up_down);
        TextView tvPopPriceDownUp = viewLayout.findViewById(R.id.tv_pop_price_down_up);
        TextView tvPopAreaUpDown = viewLayout.findViewById(R.id.tv_pop_area_up_down);
        TextView tvPopAreaDownUp = viewLayout.findViewById(R.id.tv_pop_area_down_up);
        if (TextUtils.equals("0", sort)) {
            tvPopOrderDef.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (TextUtils.equals("1", sort)) {
            tvPopPriceUpDown.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (TextUtils.equals("2", sort)) {
            tvPopPriceDownUp.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (TextUtils.equals("3", sort)) {
            tvPopAreaUpDown.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (TextUtils.equals("4", sort)) {
            tvPopAreaDownUp.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        }
        tvPopOrderDef.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow(mSearchType, "0");
        });
        tvPopPriceUpDown.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow(mSearchType, "1");
        });
        tvPopPriceDownUp.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow(mSearchType, "2");
        });
        tvPopAreaUpDown.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow(mSearchType, "3");
        });
        tvPopAreaDownUp.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow(mSearchType, "4");
        });
    }

    private View includeJointWork, includeOpenSeats, includeOffice, includeGarden;
    //共享办公
    private RecyclerView rvJointWorkRent, rvJointWorkSeats, rvJointWorkBrand, rvJointWorkCharacteristic;
    private EditText etJointWorkRentMin, etJointWorkRentMax, etJointWorkSeatsMin, etJointWorkSeatsMax;
    //开放工位
    private RecyclerView rvOpenSeatsRent, rvOpenSeatsBrand, rvOpenSeatsCharacteristic;
    private EditText etOpenSeatsRentMin, etOpenSeatsRentMax;
    //办公室，园区
    private RecyclerView rvOfficeArea, rvOfficeRent, rvOfficeSeats, rvOfficeDecorate, rvOfficeCharacteristic;
    private EditText etOfficeAreaMin, etOfficeAreaMax, etOfficeRentMin, etOfficeRentMax, etOfficeSeatsMin, etOfficeSeatsMax;
    //园区
    private RecyclerView rvGardenArea, rvGardenRent, rvGardenSeats, rvGardenDecorate, rvGardenCharacteristic;
    private EditText etGardenAreaMin, etGardenAreaMax, etGardenRentMin, etGardenRentMax, etGardenSeatsMin, etGardenSeatsMax;
    //共享办公
    private JointRentAdapter jointRentAdapter;
    private JointSeatsAdapter jointSeatsAdapter;
    private JointBrandAdapter jointBrandAdapter;
    private JointUniqueAdapter jointUniqueAdapter;
    //开放工位
    private OpenRentAdapter openSeatRentAdapter;
    private OpenBrandAdapter openBrandAdapter;
    private OpenUniqueAdapter openSeatsUniqueAdapter;
    //办公室
    private OfficeAreaAdapter officeAreaAdapter;
    private OfficeRentAdapter officeRentAdapter;
    private OfficeSeatsAdapter officeSeatAdapter;
    private OfficeDecorationAdapter officeDecorationAdapter;
    private OfficeUniqueAdapter officeUniqueAdapter;
    //园区
    private GardenAreaAdapter gardenAreaAdapter;
    private GardenRentAdapter gardenRentAdapter;
    private GardenSeatsAdapter gardenSeatAdapter;
    private GardenDecorationAdapter gardenDecorationAdapter;
    private GardenUniqueAdapter gardenUniqueAdapter;

    private void handleCondition(View viewLayout) {
        RadioButton rbJointWork = viewLayout.findViewById(R.id.rb_joint_work);
        RadioButton rbOpenSeats = viewLayout.findViewById(R.id.rb_open_seats);
        RadioButton rbOffice = viewLayout.findViewById(R.id.rb_office);
        RadioButton rbGarden = viewLayout.findViewById(R.id.rb_garden);
        includeJointWork = viewLayout.findViewById(R.id.include_joint_work);
        includeOpenSeats = viewLayout.findViewById(R.id.include_open_seats);
        includeOffice = viewLayout.findViewById(R.id.include_office);
        includeGarden = viewLayout.findViewById(R.id.include_garden);

        rbJointWork.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(0);
        });
        rbOpenSeats.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(1);
        });
        rbOffice.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(2);
        });
        rbGarden.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(3);
        });
        //共享办公
        jointWorkViews(viewLayout);
        //开放工位
        openSeatViews(viewLayout);
        //办公室
        officeViews(viewLayout);
        //园区
        gardenViews(viewLayout);
        //初始化选中
        rbOffice.setChecked(true);
        showListViews(2);
    }

    private void showListViews(int flag) {
        if (flag == 0) {
            includeJointWork.setVisibility(View.VISIBLE);
            includeOpenSeats.setVisibility(View.GONE);
            includeOffice.setVisibility(View.GONE);
            includeGarden.setVisibility(View.GONE);
            if (jointRentAdapter == null) {
                jointRentAdapter = new JointRentAdapter(mContext, "", CommonList.rentList());
                rvJointWorkRent.setAdapter(jointRentAdapter);
                jointSeatsAdapter = new JointSeatsAdapter(mContext, "", CommonList.seatsList());
                rvJointWorkSeats.setAdapter(jointSeatsAdapter);
                jointBrandAdapter = new JointBrandAdapter(mContext, brandList);
                rvJointWorkBrand.setAdapter(jointBrandAdapter);
                jointUniqueAdapter = new JointUniqueAdapter(mContext, jointWorkUniqueList);
                rvJointWorkCharacteristic.setAdapter(jointUniqueAdapter);
            }
        } else if (flag == 1) {
            includeJointWork.setVisibility(View.GONE);
            includeOpenSeats.setVisibility(View.VISIBLE);
            includeOffice.setVisibility(View.GONE);
            includeGarden.setVisibility(View.GONE);
            if (openSeatRentAdapter == null) {
                openSeatRentAdapter = new OpenRentAdapter(mContext, "", CommonList.rentList());
                rvOpenSeatsRent.setAdapter(openSeatRentAdapter);
                openBrandAdapter = new OpenBrandAdapter(mContext, brandList);
                rvOpenSeatsBrand.setAdapter(openBrandAdapter);
                openSeatsUniqueAdapter = new OpenUniqueAdapter(mContext, jointWorkUniqueList);
                rvOpenSeatsCharacteristic.setAdapter(openSeatsUniqueAdapter);
            }
        } else if (flag == 2) {
            includeJointWork.setVisibility(View.GONE);
            includeOpenSeats.setVisibility(View.GONE);
            includeOffice.setVisibility(View.VISIBLE);
            includeGarden.setVisibility(View.GONE);
            if (officeAreaAdapter == null) {
                officeAreaAdapter = new OfficeAreaAdapter(mContext, "", CommonList.areaList());
                rvOfficeArea.setAdapter(officeAreaAdapter);
                officeRentAdapter = new OfficeRentAdapter(mContext, "", CommonList.rentList());
                rvOfficeRent.setAdapter(officeRentAdapter);
                officeSeatAdapter = new OfficeSeatsAdapter(mContext, "", CommonList.seatsList());
                rvOfficeSeats.setAdapter(officeSeatAdapter);
                officeDecorationAdapter = new OfficeDecorationAdapter(mContext, decorationList);
                rvOfficeDecorate.setAdapter(officeDecorationAdapter);
                officeUniqueAdapter = new OfficeUniqueAdapter(mContext, buildingUniqueList);
                rvOfficeCharacteristic.setAdapter(officeUniqueAdapter);
            }
        } else if (flag == 3) {
            includeJointWork.setVisibility(View.GONE);
            includeOpenSeats.setVisibility(View.GONE);
            includeOffice.setVisibility(View.GONE);
            includeGarden.setVisibility(View.VISIBLE);
            if (gardenAreaAdapter == null) {
                gardenAreaAdapter = new GardenAreaAdapter(mContext, "", CommonList.areaList());
                rvGardenArea.setAdapter(gardenAreaAdapter);
                gardenRentAdapter = new GardenRentAdapter(mContext, "", CommonList.rentList());
                rvGardenRent.setAdapter(gardenRentAdapter);
                gardenSeatAdapter = new GardenSeatsAdapter(mContext, "", CommonList.seatsList());
                rvGardenSeats.setAdapter(gardenSeatAdapter);
                gardenDecorationAdapter = new GardenDecorationAdapter(mContext, decorationList);
                rvGardenDecorate.setAdapter(gardenDecorationAdapter);
                gardenUniqueAdapter = new GardenUniqueAdapter(mContext, buildingUniqueList);
                rvGardenCharacteristic.setAdapter(gardenUniqueAdapter);
            }
        }
    }

    private void jointWorkViews(View viewLayout) {
        rvJointWorkRent = viewLayout.findViewById(R.id.rv_joint_work_rent);
        rvJointWorkSeats = viewLayout.findViewById(R.id.rv_joint_work_seats);
        rvJointWorkBrand = viewLayout.findViewById(R.id.rv_joint_work_brand);
        rvJointWorkCharacteristic = viewLayout.findViewById(R.id.rv_joint_work_characteristic);
        etJointWorkRentMin = viewLayout.findViewById(R.id.et_joint_work_rent_min);
        etJointWorkRentMax = viewLayout.findViewById(R.id.et_joint_work_rent_max);
        etJointWorkSeatsMin = viewLayout.findViewById(R.id.et_joint_work_seats_min);
        etJointWorkSeatsMax = viewLayout.findViewById(R.id.et_joint_work_seats_max);

        rvJointWorkRent.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvJointWorkSeats.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvJointWorkBrand.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvJointWorkCharacteristic.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        rvJointWorkRent.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvJointWorkSeats.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvJointWorkBrand.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvJointWorkCharacteristic.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));

    }

    private void openSeatViews(View viewLayout) {
        rvOpenSeatsRent = viewLayout.findViewById(R.id.rv_open_seats_rent);
        rvOpenSeatsBrand = viewLayout.findViewById(R.id.rv_open_seats_brand);
        rvOpenSeatsCharacteristic = viewLayout.findViewById(R.id.rv_open_seats_characteristic);
        etOpenSeatsRentMin = viewLayout.findViewById(R.id.et_open_seats_rent_min);
        etOpenSeatsRentMax = viewLayout.findViewById(R.id.et_open_seats_rent_max);

        rvOpenSeatsRent.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOpenSeatsBrand.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOpenSeatsCharacteristic.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        rvOpenSeatsRent.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOpenSeatsBrand.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOpenSeatsCharacteristic.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
    }

    private void officeViews(View viewLayout) {
        rvOfficeArea = viewLayout.findViewById(R.id.rv_office_area);
        rvOfficeRent = viewLayout.findViewById(R.id.rv_office_rent);
        rvOfficeSeats = viewLayout.findViewById(R.id.rv_office_seats);
        rvOfficeDecorate = viewLayout.findViewById(R.id.rv_office_decorate);
        rvOfficeCharacteristic = viewLayout.findViewById(R.id.rv_office_characteristic);
        etOfficeAreaMin = viewLayout.findViewById(R.id.et_office_area_min);
        etOfficeAreaMax = viewLayout.findViewById(R.id.et_office_area_max);
        etOfficeRentMin = viewLayout.findViewById(R.id.et_office_rent_min);
        etOfficeRentMax = viewLayout.findViewById(R.id.et_office_rent_max);
        etOfficeSeatsMin = viewLayout.findViewById(R.id.et_office_seats_min);
        etOfficeSeatsMax = viewLayout.findViewById(R.id.et_office_seats_max);

        rvOfficeArea.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOfficeRent.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOfficeSeats.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOfficeDecorate.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvOfficeCharacteristic.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        rvOfficeArea.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOfficeRent.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOfficeSeats.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOfficeDecorate.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvOfficeCharacteristic.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
    }

    private void gardenViews(View viewLayout) {
        rvGardenArea = viewLayout.findViewById(R.id.rv_garden_area);
        rvGardenRent = viewLayout.findViewById(R.id.rv_garden_rent);
        rvGardenSeats = viewLayout.findViewById(R.id.rv_garden_seats);
        rvGardenDecorate = viewLayout.findViewById(R.id.rv_garden_decorate);
        rvGardenCharacteristic = viewLayout.findViewById(R.id.rv_garden_characteristic);
        etGardenAreaMin = viewLayout.findViewById(R.id.et_garden_area_min);
        etGardenAreaMax = viewLayout.findViewById(R.id.et_garden_area_max);
        etGardenRentMin = viewLayout.findViewById(R.id.et_garden_rent_min);
        etGardenRentMax = viewLayout.findViewById(R.id.et_garden_rent_max);
        etGardenSeatsMin = viewLayout.findViewById(R.id.et_garden_seats_min);
        etGardenSeatsMax = viewLayout.findViewById(R.id.et_garden_seats_max);

        rvGardenArea.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvGardenRent.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvGardenSeats.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvGardenDecorate.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        rvGardenCharacteristic.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        rvGardenArea.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvGardenRent.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvGardenSeats.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvGardenDecorate.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvGardenCharacteristic.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
    }

    //地铁数据
    private Map<Integer, Boolean> mapMeter = new HashMap<>();

    private class MeterAdapter extends CommonListAdapter<MeterBean.DataBean> {

        private RecyclerView recyclerViewRight;
        private TextView tvNum;
        private boolean onBind;

        public MeterAdapter(Context context, TextView tvNum, List<MeterBean.DataBean> list, RecyclerView recyclerViewRight) {
            super(context, R.layout.item_search_meter, list);
            this.recyclerViewRight = recyclerViewRight;
            this.tvNum = tvNum;
            if (!TextUtils.isEmpty(line)) {
                for (int i = 0; i < list.size(); i++) {
                    if (TextUtils.equals(line, String.valueOf(list.get(i).getLid()))) {
                        stationAdapter = new StationAdapter(mContext, tvNum, list.get(i).getList());
                        recyclerViewRight.setAdapter(stationAdapter);
                    }
                }
            }
        }

        @Override
        public void convert(ViewHolder holder, MeterBean.DataBean meterBean) {
            TextView itemMeter = holder.getView(R.id.tv_item_meter);
            itemMeter.setText(meterBean.getLine());
            //选择筛选条件的
            if (!TextUtils.isEmpty(line)) {
                if (TextUtils.equals(line, String.valueOf(meterBean.getLid()))) {
                    mapMeter.put(holder.getAdapterPosition(), true);//选择筛选条件的
                }
            }
            holder.itemView.setOnClickListener(v -> {
                mapMeter.clear();
                mapMeter.put(holder.getAdapterPosition(), true);
                if (!onBind) {
                    notifyDataSetChanged();
                }
                //clear 之前选中的子项
                clearMeterHashSet(tvNum);
                //赋值
                line = meterBean.getLid();
                //地铁列表详情列表
                stationAdapter = new StationAdapter(mContext, tvNum, meterBean.getList());
                recyclerViewRight.setAdapter(stationAdapter);
                //神策(如果只选地铁线(商圈)则传地铁线，如果选择地铁站(商圈区域)则传地铁站逗号分隔
                Constants.SENSORS_AREA_CONTENT = meterBean.getLine();
            });
            //显示选中的文本
            onBind = true;
            if (mapMeter != null && mapMeter.containsKey(holder.getAdapterPosition())) {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    //清除选中的子项
    private void clearMeterHashSet(TextView tvNum) {
        tvNum.setText("");
        tvNum.setVisibility(View.GONE);
        nearbySubway = "";
        if (mHashSetLine != null) {
            mHashSetLine.clear();
        }
        if (mCheckStatesLine != null) {
            mCheckStatesLine.clear();
        }
    }

    //地铁站详情
    private class StationAdapter extends CommonListAdapter<MeterBean.DataBean.ListBean> {
        private SparseBooleanArray checkStates = new SparseBooleanArray();
        private HashSet<Integer> hashSet; //当前选中的数据列表
        private Map<Integer, String> map = new HashMap<>();//记录神策文本
        private TextView tvNum;

        StationAdapter(Context context, final TextView tvNum, List<MeterBean.DataBean.ListBean> list) {
            super(context, R.layout.item_search_station, list);
            this.tvNum = tvNum;
            hashSet = new HashSet<>();
            if (mHashSetLine != null) {//传递过来的值
                hashSet.addAll(mHashSetLine);
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void convert(ViewHolder holder, MeterBean.DataBean.ListBean bean) {
            CheckBox cbStation = holder.getView(R.id.cb_name);
            cbStation.setText(bean.getStationName());
            cbStation.setTag(holder.getAdapterPosition());
            if (mCheckStatesLine != null) {//传递过来的值
                checkStates = mCheckStatesLine;
            }
            cbStation.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = (int) buttonView.getTag();
                if (isChecked) {
                    checkStates.put(pos, true);
                    hashSet.add(bean.getId());
                    map.put(bean.getId(), bean.getStationName());
                } else {
                    checkStates.delete(pos);
                    hashSet.remove(bean.getId());
                    map.remove(bean.getId());
                }
                if (checkStates.size() > 0) {
                    tvNum.setVisibility(View.VISIBLE);
                    tvNum.setText(String.format("%d", checkStates.size()));
                } else {
                    tvNum.setVisibility(View.GONE);
                }
                //赋值
                nearbySubway = getHashSetKey(hashSet);
                mHashSetLine = hashSet;
                mCheckStatesLine = checkStates;
                //神策埋点
                sensorsLineOrBuisnessEvent(map);
            });
            cbStation.setChecked(checkStates.get(holder.getAdapterPosition(), false));
        }
    }

    //商圈
    private Map<Integer, Boolean> mapBusiness = new HashMap<>();

    private class BusinessCircleAdapter extends CommonListAdapter<BusinessCircleBean.DataBean> {
        private RecyclerView recyclerViewRight;
        private TextView tvNum;
        private boolean onBind;

        BusinessCircleAdapter(Context context, TextView tvNum, List<BusinessCircleBean.DataBean> list, RecyclerView recyclerViewRight) {
            super(context, R.layout.item_search_meter, list);
            this.recyclerViewRight = recyclerViewRight;
            this.tvNum = tvNum;
            if (!TextUtils.isEmpty(district)) {
                for (int i = 0; i < list.size(); i++) {
                    if (TextUtils.equals(district, String.valueOf(list.get(i).getDistrictID()))) {
                        businessCircleDetailsAdapter = new BusinessCircleDetailsAdapter(mContext, tvNum, list.get(i).getList());
                        recyclerViewRight.setAdapter(businessCircleDetailsAdapter);
                    }
                }
            }
        }

        @Override
        public void convert(ViewHolder holder, BusinessCircleBean.DataBean bean) {
            TextView itemBusiness = holder.getView(R.id.tv_item_meter);
            itemBusiness.setText(bean.getDistrict());
            if (!TextUtils.isEmpty(district)) {
                if (TextUtils.equals(district, String.valueOf(bean.getDistrictID()))) {
                    mapBusiness.put(holder.getAdapterPosition(), true);//选择筛选条件的
                }
            }
            holder.itemView.setOnClickListener(v -> {
                mapBusiness.clear();
                mapBusiness.put(holder.getAdapterPosition(), true);
                if (!onBind) {
                    notifyDataSetChanged();
                }
                //clear 之前选中的子项
                clearBusinessHashSet(tvNum);
                //赋值
                district = String.valueOf(bean.getDistrictID());
                businessCircleDetailsAdapter = new BusinessCircleDetailsAdapter(mContext, tvNum, bean.getList());
                recyclerViewRight.setAdapter(businessCircleDetailsAdapter);
                //神策(如果只选地铁线(商圈)则传地铁线，如果选择地铁站(商圈区域)则传地铁站逗号分隔
                Constants.SENSORS_AREA_CONTENT = bean.getDistrict();
            });
            //显示选中的文本
            onBind = true;
            if (mapBusiness != null && mapBusiness.containsKey(holder.getAdapterPosition())) {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    //清除选中的子项
    private void clearBusinessHashSet(TextView tvNum) {
        tvNum.setText("");
        tvNum.setVisibility(View.GONE);
        business = "";
        if (mHashSetBusiness != null) {
            mHashSetBusiness.clear();
        }
        if (mCheckStatesBusiness != null) {
            mCheckStatesBusiness.clear();
        }
    }

    //商圈详情
    private class BusinessCircleDetailsAdapter extends CommonListAdapter<BusinessCircleBean.DataBean.ListBean> {
        private SparseBooleanArray checkStates = new SparseBooleanArray();
        private HashSet<Integer> hashSet; //当前选中的数据列表
        private Map<Integer, String> map = new HashMap<>(); //神策埋点文本
        private TextView tvNum;

        BusinessCircleDetailsAdapter(Context context, final TextView tvNum, List<BusinessCircleBean.DataBean.ListBean> list) {
            super(context, R.layout.item_search_station, list);
            this.tvNum = tvNum;
            hashSet = new HashSet<>();

            if (mHashSetBusiness != null) {//传递过来的值
                hashSet.addAll(mHashSetBusiness);
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void convert(ViewHolder holder, BusinessCircleBean.DataBean.ListBean bean) {
            CheckBox cbStation = holder.getView(R.id.cb_name);
            cbStation.setText(bean.getArea());
            cbStation.setTag(holder.getAdapterPosition());
            if (mCheckStatesBusiness != null) {//传递过来的值
                checkStates = mCheckStatesBusiness;
            }
            cbStation.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = (int) buttonView.getTag();
                if (isChecked) {
                    checkStates.put(pos, true);
                    hashSet.add(bean.getId());
                    map.put(bean.getId(), bean.getArea());
                } else {
                    checkStates.delete(pos);
                    hashSet.remove(bean.getId());
                    map.remove(bean.getId());
                }
                if (checkStates.size() > 0) {
                    tvNum.setVisibility(View.VISIBLE);
                    tvNum.setText(String.format("%d", checkStates.size()));
                } else {
                    tvNum.setVisibility(View.GONE);
                }
                //赋值
                business = getHashSetKey(hashSet);
                mHashSetBusiness = hashSet;
                mCheckStatesBusiness = checkStates;
                //神策埋点
                sensorsLineOrBuisnessEvent(map);
            });
            cbStation.setChecked(checkStates.get(holder.getAdapterPosition(), false));
        }
    }

    //HashSet 选中的数据
    private String getHashSetKey(HashSet<Integer> mSet) {
        Iterator<Integer> iterator = mSet.iterator();
        StringBuilder key = new StringBuilder();
        while (iterator.hasNext()) {
            if (mSet.size() == 1) {
                key.append(iterator.next());
            } else {
                key.append(iterator.next()).append(",");
            }
        }
        if (mSet.size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        return key.toString();
    }

    private String getKey(Map<Integer, String> map) {
        StringBuilder key = new StringBuilder();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (map.size() == 1) {
                key.append(entry.getKey());
            } else {
                key.append(entry.getKey()).append(",");
            }
        }
        if (map.size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        return key.toString();
    }

    //神策埋点装修类型
    private void sensorsDecorationEvent(Map<Integer, String> map) {
        StringBuilder keyName = new StringBuilder();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (map.size() == 1) {
                keyName.append(map.get(entry.getKey()));
            } else {
                keyName.append(map.get(entry.getKey())).append(",");
            }
        }
        if (map.size() > 1) {
            keyName = keyName.replace(keyName.length() - 1, keyName.length(), "");
        }
        Constants.SENSORS_DECORATION = keyName.toString();
    }

    //神策埋点地铁或商圈类型
    private void sensorsLineOrBuisnessEvent(Map<Integer, String> map) {
        StringBuilder keyName = new StringBuilder();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (map.size() == 1) {
                keyName.append(map.get(entry.getKey()));
            } else {
                keyName.append(map.get(entry.getKey())).append(",");
            }
        }
        if (map.size() > 1) {
            keyName = keyName.replace(keyName.length() - 1, keyName.length(), "");
        }
        Constants.SENSORS_AREA_CONTENT = keyName.toString();
    }

    //*****************************************************************
    //*************************    筛选  *******************************
    //*****************************************************************

    /**
     * 共享办公
     */
    //工位
    class JointSeatsAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public JointSeatsAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvJointWorkSeats.isComputingLayout()) {
                        setEditText(bean.getKey(), etJointWorkSeatsMin, etJointWorkSeatsMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //租金
    class JointRentAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public JointRentAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvJointWorkRent.isComputingLayout()) {
                        setEditText(bean.getKey(), etJointWorkRentMin, etJointWorkRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //品牌
    class JointBrandAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public JointBrandAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }
            });
        }
    }

    //特色
    class JointUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public JointUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (map.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }
            });
        }
    }

    /**
     * 开放工位
     */
    //租金
    class OpenRentAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public OpenRentAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvOpenSeatsRent.isComputingLayout()) {
                        setEditText(bean.getKey(), etOpenSeatsRentMin, etOpenSeatsRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //品牌
    class OpenBrandAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public OpenBrandAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }
            });
        }
    }

    //特色
    class OpenUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public OpenUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (map.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }
            });
        }
    }

    /**
     * 办公室
     */
    //面积
    class OfficeAreaAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public OfficeAreaAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvOfficeArea.isComputingLayout()) {
                        setEditText(bean.getKey(), etOfficeAreaMin, etOfficeAreaMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //租金
    class OfficeRentAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public OfficeRentAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvOfficeRent.isComputingLayout()) {
                        setEditText(bean.getKey(), etOfficeRentMin, etOfficeRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //工位
    class OfficeSeatsAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public OfficeSeatsAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvOfficeSeats.isComputingLayout()) {
                        setEditText(bean.getKey(), etOfficeSeatsMin, etOfficeSeatsMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //装修
    class OfficeDecorationAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private int checkedPos = -1;

        @SuppressLint("UseSparseArrays")
        OfficeDecorationAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_unique, list);
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            ImageView ivImage = holder.getView(R.id.iv_image);
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImg()).into(ivImage);
            cbType.setText(bean.getDictCname());
            cbType.setChecked(holder.getAdapterPosition() == checkedPos);
            holder.itemView.setOnClickListener(view -> {
                cbType.setChecked(true);
                checkedPos = holder.getAdapterPosition();
                if (!rvGardenDecorate.isComputingLayout()) {
                    notifyDataSetChanged();
                }
            });
        }
    }

    //特色
    class OfficeUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public OfficeUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getDictCname());
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (map.size() >= 4) {
                        tvName.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }

            });
        }
    }

    /**
     * 园区
     */
    //面积
    class GardenAreaAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public GardenAreaAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvGardenArea.isComputingLayout()) {
                        setEditText(bean.getKey(), etGardenAreaMin, etGardenAreaMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //租金
    class GardenRentAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public GardenRentAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvGardenRent.isComputingLayout()) {
                        setEditText(bean.getKey(), etGardenRentMin, etGardenRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //工位
    class GardenSeatsAdapter extends CommonListAdapter<WantFindBean> {
        private int checkedPos = -1;

        public GardenSeatsAdapter(Context context, String value, List<WantFindBean> list) {
            super(context, R.layout.item_house_decroation, list);
        }

        @Override
        public void convert(ViewHolder holder, final WantFindBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getValue());
            tvName.setChecked(holder.getAdapterPosition() == checkedPos);
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedPos = holder.getAdapterPosition();
                    if (!rvGardenSeats.isComputingLayout()) {
                        setEditText(bean.getKey(), etGardenSeatsMin, etGardenSeatsMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //装修
    class GardenDecorationAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private int checkedPos = -1;

        @SuppressLint("UseSparseArrays")
        GardenDecorationAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_unique, list);

        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            ImageView ivImage = holder.getView(R.id.iv_image);
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImg()).into(ivImage);
            cbType.setText(bean.getDictCname());
            cbType.setChecked(holder.getAdapterPosition() == checkedPos);
            holder.itemView.setOnClickListener(view -> {
                cbType.setChecked(true);
                checkedPos = holder.getAdapterPosition();
                if (!rvGardenDecorate.isComputingLayout()) {
                    notifyDataSetChanged();
                }
            });
        }
    }

    //特色
    class GardenUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        public GardenUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (map.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!map.containsKey(bean.getDictValue())) {
                        map.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    map.remove(bean.getDictValue());
                }

            });
        }
    }

    private void setEditText(String value, EditText min, EditText max) {
        String a = value.substring(0, value.indexOf(","));
        String b = value.substring(a.length() + 1);
        min.setText((TextUtils.equals("0", a)) ? "" : a);
        max.setText((TextUtils.equals(CommonList.SEARCH_MAX, b)) ? "" : b);
    }
}
