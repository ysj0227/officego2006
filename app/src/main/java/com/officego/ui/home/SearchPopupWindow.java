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
import android.widget.Switch;
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
import com.officego.rpc.OfficegoApi;
import com.officego.ui.find.WantFindBean;
import com.officego.ui.home.model.BusinessCircleBean;
import com.officego.ui.home.model.ConditionSearchBean;
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
    //layout
    private final int SEARCH_TYPE_AREA = 0;
    private final int SEARCH_TYPE_OFFICE = 1;
    private final int SEARCH_TYPE_ORDER = 2;
    private final int SEARCH_TYPE_CONDITION = 3;
    private int layout;
    //搜索类型
    private final int mSearchType;
    //height
    private int screenHeight, statusBarHeight, titleBarHeight, searchHeight, bottomTabBarHeight;

    private RadioButton rbJointWork, rbOpenSeats, rbOffice, rbGarden;
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
    //地铁商圈
    private HashSet<Integer> mHashSetLine, mHashSetBusiness;
    private SparseBooleanArray mCheckStatesLine, mCheckStatesBusiness;//记录选中的位置
    private String district = "", business = "";//商圈
    private String line = "", nearbySubway = ""; //地铁
    private int filterType; //搜索类型0,1,2,3，4
    private String sort;//排序
    private final List<MeterBean.DataBean> meterList = new ArrayList<>(); //获取地铁列表
    private final List<BusinessCircleBean.DataBean> businessCircleList = new ArrayList<>();  //商圈列表
    //筛选
    private List<DirectoryBean.DataBean> decorationList = new ArrayList<>();
    private List<DirectoryBean.DataBean> buildingUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> jointWorkUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> brandList = new ArrayList<>();
    //共享办公
    private Map<Integer, String> jointBrandMap, jointUniqueMap;
    //开放工位特色
    private Map<Integer, String> openBrandMap, openUniqueMap;
    //办公室,园区
    private int officeDecoration, gardenDecoration;
    private Map<Integer, String> officeMap, gardenMap;
    private final ConditionSearchBean searchData;

    //自定义接口
    private onSureClickListener onSureClickListener;

    void setOnSureClickListener(SearchPopupWindow.onSureClickListener onSureClickListener) {
        this.onSureClickListener = onSureClickListener;
    }

    interface onSureClickListener {
        //区域确定
        void onSurePopUpWindow(boolean isLine, HashSet<Integer> hashSet, SparseBooleanArray checkStates,
                               String data1, String data2);

        //办公
        void onOfficeTypePopUpWindow(int filterType, int officeText);

        //排序
        void onOfficeOrderPopUpWindow(String order);

        //筛选
        void onConditionPopUpWindow(int filterType, ConditionSearchBean bean);
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
                             int filterType, HashSet<Integer> hashSet, SparseBooleanArray checkStates,
                             String district, String business, String line,
                             String nearbySubway, String sort,
                             List<DirectoryBean.DataBean> decorationList,
                             List<DirectoryBean.DataBean> buildingUniqueList,
                             List<DirectoryBean.DataBean> jointWorkUniqueList,
                             List<DirectoryBean.DataBean> brandList,
                             ConditionSearchBean searchData) {
        super();
        this.mContext = activity;
        this.mSetTitleView = setTextView;
        this.mSearchType = searchType;
        this.filterType = filterType;
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
        this.searchData = searchData;
        this.decorationList = decorationList;
        this.buildingUniqueList = buildingUniqueList;
        this.jointWorkUniqueList = jointWorkUniqueList;
        this.brandList = brandList;
        //初始化设置筛选
        if (mSearchType == 3 && searchData != null) {
            if (filterType == Constants.SEARCH_JOINT_WORK) {
                jointBrandMap = CommonHelper.stringToMap(searchData.getBrand());
                jointUniqueMap = CommonHelper.stringToMap(searchData.getUnique());
            } else if (filterType == Constants.SEARCH_OPEN_SEATS) {
                openBrandMap = CommonHelper.stringToMap(searchData.getBrand());
                openUniqueMap = CommonHelper.stringToMap(searchData.getUnique());
            } else if (filterType == Constants.SEARCH_OFFICE) {
                officeDecoration = TextUtils.isEmpty(searchData.getDecoration()) ? 0 : Integer.parseInt(searchData.getDecoration());
                officeMap = CommonHelper.stringToMap(searchData.getUnique());
            } else if (filterType == Constants.SEARCH_GARDEN) {
                gardenDecoration = TextUtils.isEmpty(searchData.getDecoration()) ? 0 : Integer.parseInt(searchData.getDecoration());
                gardenMap = CommonHelper.stringToMap(searchData.getUnique());
            }
        }
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
        @SuppressLint("NonConstantResourceId") View.OnClickListener clickListener = v -> {
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
        TextView tvPopTenant = viewLayout.findViewById(R.id.tv_pop_tenant);
        TextView tvPopOpenSeats = viewLayout.findViewById(R.id.tv_pop_open_seats);
        TextView tvPopOffice = viewLayout.findViewById(R.id.tv_pop_office);
        TextView tvPopGarden = viewLayout.findViewById(R.id.tv_pop_garden);
        if (filterType == Constants.SEARCH_ALL) {
            tvPopAll.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (filterType == Constants.SEARCH_JOINT_WORK) {
            tvPopTenant.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (filterType == Constants.SEARCH_OPEN_SEATS) {
            tvPopOpenSeats.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (filterType == Constants.SEARCH_OFFICE) {
            tvPopOffice.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        } else if (filterType == Constants.SEARCH_GARDEN) {
            tvPopGarden.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
        }
        tvPopAll.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(Constants.SEARCH_ALL, R.string.str_house_all);
        });
        tvPopTenant.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(Constants.SEARCH_JOINT_WORK, R.string.str_house_tenant);
        });
        tvPopOpenSeats.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(Constants.SEARCH_OPEN_SEATS, R.string.str_house_open_seats);
        });
        tvPopOffice.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(Constants.SEARCH_OFFICE, R.string.str_house_office);
        });
        tvPopGarden.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeTypePopUpWindow(Constants.SEARCH_GARDEN, R.string.str_house_garden);
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
            onSureClickListener.onOfficeOrderPopUpWindow("0");
        });
        tvPopPriceUpDown.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow("1");
        });
        tvPopPriceDownUp.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow("2");
        });
        tvPopAreaUpDown.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow("3");
        });
        tvPopAreaDownUp.setOnClickListener(v -> {
            dismiss();
            onSureClickListener.onOfficeOrderPopUpWindow("4");
        });
    }

    private void handleCondition(View viewLayout) {
        rbJointWork = viewLayout.findViewById(R.id.rb_joint_work);
        rbOpenSeats = viewLayout.findViewById(R.id.rb_open_seats);
        rbOffice = viewLayout.findViewById(R.id.rb_office);
        rbGarden = viewLayout.findViewById(R.id.rb_garden);
        includeJointWork = viewLayout.findViewById(R.id.include_joint_work);
        includeOpenSeats = viewLayout.findViewById(R.id.include_open_seats);
        includeOffice = viewLayout.findViewById(R.id.include_office);
        includeGarden = viewLayout.findViewById(R.id.include_garden);
        Switch swVR = viewLayout.findViewById(R.id.sw_open);
        Button btnClear = viewLayout.findViewById(R.id.btn_clear);
        Button btnSure = viewLayout.findViewById(R.id.btn_sure);
        rbJointWork.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(Constants.SEARCH_JOINT_WORK);
        });
        rbOpenSeats.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(Constants.SEARCH_OPEN_SEATS);
        });
        rbOffice.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(Constants.SEARCH_OFFICE);
        });
        rbGarden.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) showListViews(Constants.SEARCH_GARDEN);
        });
        //共享办公
        jointWorkViews(viewLayout);
        //开放工位
        openSeatViews(viewLayout);
        //办公室
        officeViews(viewLayout);
        //园区
        gardenViews(viewLayout);
        //初始化选中 默认办公室
        if (searchData == null) {
            swVR.setChecked(false);
        } else {
            swVR.setChecked(searchData.isVr());
        }
        showListViews(filterType == Constants.SEARCH_ALL ? Constants.SEARCH_OFFICE : filterType);
        //点击监听
        @SuppressLint("NonConstantResourceId") View.OnClickListener clickListener = v -> {
            switch (v.getId()) {
                case R.id.btn_clear://清理
                    ConditionSearchBean searchBean = new ConditionSearchBean();
                    searchBean.setBrand("");
                    searchBean.setUnique("");
                    searchBean.setDecoration("");
                    searchBean.setRent("0," + CommonList.SEARCH_MAX);
                    searchBean.setArea("0," + CommonList.SEARCH_MAX);
                    searchBean.setSeats("0," + CommonList.SEARCH_MAX);
                    searchBean.setVr(false);
                    if (onSureClickListener != null) {
                        onSureClickListener.onConditionPopUpWindow(filterType, searchBean);
                    }
                    dismiss();
                    break;
                case R.id.btn_sure://确定
                    ConditionSearchBean bean = new ConditionSearchBean();
                    if (rbJointWork.isChecked()) {
                        filterType = Constants.SEARCH_JOINT_WORK;
                        bean.setSeats(setEditText(etJointWorkSeatsMin, etJointWorkSeatsMax));
                        bean.setRent(setEditText(etJointWorkRentMin, etJointWorkRentMax));
                        bean.setBrand(CommonHelper.getKey(jointBrandMap));
                        bean.setUnique(CommonHelper.getKey(jointUniqueMap));
                        //以下默认
                        bean.setArea("0," + CommonList.SEARCH_MAX);
                        bean.setDecoration("");
                    } else if (rbOpenSeats.isChecked()) {
                        filterType = Constants.SEARCH_OPEN_SEATS;
                        bean.setRent(setEditText(etOpenSeatsRentMin, etOpenSeatsRentMax));
                        bean.setBrand(CommonHelper.getKey(openBrandMap));
                        bean.setUnique(CommonHelper.getKey(openUniqueMap));
                        //以下默认
                        bean.setArea("0," + CommonList.SEARCH_MAX);
                        bean.setSeats("0," + CommonList.SEARCH_MAX);
                        bean.setDecoration("");
                    } else if (rbOffice.isChecked()) {
                        filterType = Constants.SEARCH_OFFICE;
                        bean.setArea(setEditText(etOfficeAreaMin, etOfficeAreaMax));
                        bean.setSeats(setEditText(etOfficeSeatsMin, etOfficeSeatsMax));
                        bean.setRent(setEditText(etOfficeRentMin, etOfficeRentMax));
                        bean.setDecoration(officeDecoration + "");
                        bean.setUnique(CommonHelper.getKey(officeMap));
                        //以下默认
                        bean.setBrand("");
                    } else if (rbGarden.isChecked()) {
                        filterType = Constants.SEARCH_GARDEN;
                        bean.setArea(setEditText(etGardenAreaMin, etGardenAreaMax));
                        bean.setSeats(setEditText(etGardenSeatsMin, etGardenSeatsMax));
                        bean.setRent(setEditText(etGardenRentMin, etGardenRentMax));
                        bean.setDecoration(gardenDecoration + "");
                        bean.setUnique(CommonHelper.getKey(gardenMap));
                        //以下默认
                        bean.setBrand("");
                    }
                    bean.setVr(swVR.isChecked());
                    if (onSureClickListener != null) {
                        onSureClickListener.onConditionPopUpWindow(filterType, bean);
                    }
                    dismiss();
                    break;
                default:
            }
        };
        btnClear.setOnClickListener(clickListener);
        btnSure.setOnClickListener(clickListener);
    }

    private void showListViews(int flag) {
        if (flag == Constants.SEARCH_JOINT_WORK) {
            rbJointWork.setChecked(true);
            includeJointWork.setVisibility(View.VISIBLE);
            includeOpenSeats.setVisibility(View.GONE);
            includeOffice.setVisibility(View.GONE);
            includeGarden.setVisibility(View.GONE);
            if (jointRentAdapter == null) {
                jointRentAdapter = new JointRentAdapter(mContext, "", CommonList.rentJointList());
                rvJointWorkRent.setAdapter(jointRentAdapter);
                jointSeatsAdapter = new JointSeatsAdapter(mContext, "", CommonList.seatsJointList());
                rvJointWorkSeats.setAdapter(jointSeatsAdapter);
                jointBrandAdapter = new JointBrandAdapter(mContext, brandList);
                rvJointWorkBrand.setAdapter(jointBrandAdapter);
                jointUniqueAdapter = new JointUniqueAdapter(mContext, jointWorkUniqueList);
                rvJointWorkCharacteristic.setAdapter(jointUniqueAdapter);
            }
        } else if (flag == Constants.SEARCH_OPEN_SEATS) {
            rbOpenSeats.setChecked(true);
            includeJointWork.setVisibility(View.GONE);
            includeOpenSeats.setVisibility(View.VISIBLE);
            includeOffice.setVisibility(View.GONE);
            includeGarden.setVisibility(View.GONE);
            if (openSeatRentAdapter == null) {
                openSeatRentAdapter = new OpenRentAdapter(mContext, "", CommonList.rentJointList());
                rvOpenSeatsRent.setAdapter(openSeatRentAdapter);
                openBrandAdapter = new OpenBrandAdapter(mContext, brandList);
                rvOpenSeatsBrand.setAdapter(openBrandAdapter);
                openSeatsUniqueAdapter = new OpenUniqueAdapter(mContext, jointWorkUniqueList);
                rvOpenSeatsCharacteristic.setAdapter(openSeatsUniqueAdapter);
            }
        } else if (flag == Constants.SEARCH_OFFICE) {
            rbOffice.setChecked(true);
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
        } else if (flag == Constants.SEARCH_GARDEN) {
            rbGarden.setChecked(true);
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
            if (searchData != null && filterType == Constants.SEARCH_JOINT_WORK && !TextUtils.isEmpty(searchData.getSeats())) {
                checkedPos = selectShowEditText(searchData.getSeats(), list);
                showEditText(searchData.getSeats(), etJointWorkSeatsMin, etJointWorkSeatsMax);
            }
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
                        showEditText(bean.getKey(), etJointWorkSeatsMin, etJointWorkSeatsMax);
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
            if (searchData != null && filterType == Constants.SEARCH_JOINT_WORK && !TextUtils.isEmpty(searchData.getRent())) {
                checkedPos = selectShowEditText(searchData.getRent(), list);
                showEditText(searchData.getRent(), etJointWorkRentMin, etJointWorkRentMax);
            }
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
                        showEditText(bean.getKey(), etJointWorkRentMin, etJointWorkRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //品牌
    class JointBrandAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public JointBrandAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (jointBrandMap == null) {
                jointBrandMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            if (jointBrandMap != null) {
                cbType.setChecked(jointBrandMap.containsKey(bean.getDictValue()));
            }
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!jointBrandMap.containsKey(bean.getDictValue())) {
                        jointBrandMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    jointBrandMap.remove(bean.getDictValue());
                }
            });
        }
    }

    //特色
    class JointUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public JointUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (jointUniqueMap == null) {
                jointUniqueMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            if (jointUniqueMap != null) {
                cbType.setChecked(jointUniqueMap.containsKey(bean.getDictValue()));
            }
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (jointUniqueMap.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!jointUniqueMap.containsKey(bean.getDictValue())) {
                        jointUniqueMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    jointUniqueMap.remove(bean.getDictValue());
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
            if (searchData != null && filterType == Constants.SEARCH_OPEN_SEATS && !TextUtils.isEmpty(searchData.getRent())) {
                checkedPos = selectShowEditText(searchData.getRent(), list);
                showEditText(searchData.getRent(), etOpenSeatsRentMin, etOpenSeatsRentMax);
            }
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
                        showEditText(bean.getKey(), etOpenSeatsRentMin, etOpenSeatsRentMax);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //品牌
    class OpenBrandAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public OpenBrandAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (openBrandMap == null) {
                openBrandMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            if (openBrandMap != null) {
                cbType.setChecked(openBrandMap.containsKey(bean.getDictValue()));
            }
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!openBrandMap.containsKey(bean.getDictValue())) {
                        openBrandMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    openBrandMap.remove(bean.getDictValue());
                }
            });
        }
    }

    //特色
    class OpenUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public OpenUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (openUniqueMap == null) {
                openUniqueMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            if (openUniqueMap != null) {
                cbType.setChecked(openUniqueMap.containsKey(bean.getDictValue()));
            }
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (openUniqueMap.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!openUniqueMap.containsKey(bean.getDictValue())) {
                        openUniqueMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    openUniqueMap.remove(bean.getDictValue());
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
            if (searchData != null && filterType == Constants.SEARCH_OFFICE && !TextUtils.isEmpty(searchData.getArea())) {
                checkedPos = selectShowEditText(searchData.getArea(), list);
                showEditText(searchData.getArea(), etOfficeAreaMin, etOfficeAreaMax);
            }
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
                        showEditText(bean.getKey(), etOfficeAreaMin, etOfficeAreaMax);
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
            if (searchData != null && filterType == Constants.SEARCH_OFFICE && !TextUtils.isEmpty(searchData.getRent())) {
                checkedPos = selectShowEditText(searchData.getRent(), list);
                showEditText(searchData.getRent(), etOfficeRentMin, etOfficeRentMax);
            }
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
                        showEditText(bean.getKey(), etOfficeRentMin, etOfficeRentMax);
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
            if (searchData != null && filterType == Constants.SEARCH_OFFICE && !TextUtils.isEmpty(searchData.getSeats())) {
                checkedPos = selectShowEditText(searchData.getSeats(), list);
                showEditText(searchData.getSeats(), etOfficeSeatsMin, etOfficeSeatsMax);
            }
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
                        showEditText(bean.getKey(), etOfficeSeatsMin, etOfficeSeatsMax);
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
            for (int i = 0; i < list.size(); i++) {
                if (officeDecoration == list.get(i).getDictValue()) {
                    checkedPos = i;
                    break;
                }
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            TextView cbType = holder.getView(R.id.cb_item);
            ImageView ivImage = holder.getView(R.id.iv_image);
            cbType.setText(bean.getDictCname());
            if (holder.getAdapterPosition() == checkedPos) {
                cbType.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImg()).into(ivImage);
            } else {
                cbType.setTextColor(ContextCompat.getColor(mContext, R.color.text_main));
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImgBlack()).into(ivImage);
            }
            holder.itemView.setOnClickListener(view -> {
                checkedPos = holder.getAdapterPosition();
                if (!rvOfficeDecorate.isComputingLayout()) {
                    officeDecoration = bean.getDictValue();
                    notifyDataSetChanged();
                }
            });
        }
    }

    //特色
    class OfficeUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public OfficeUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (officeMap == null) {
                officeMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox tvName = holder.getView(R.id.cb_item);
            tvName.setText(bean.getDictCname());
            if (officeMap != null) {
                tvName.setChecked(officeMap.containsKey(bean.getDictValue()));
            }
            tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (officeMap.size() >= 4) {
                        tvName.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!officeMap.containsKey(bean.getDictValue())) {
                        officeMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    officeMap.remove(bean.getDictValue());
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
            if (searchData != null && filterType == Constants.SEARCH_GARDEN && !TextUtils.isEmpty(searchData.getArea())) {
                checkedPos = selectShowEditText(searchData.getArea(), list);
                showEditText(searchData.getArea(), etGardenAreaMin, etGardenAreaMax);
            }
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
                        showEditText(bean.getKey(), etGardenAreaMin, etGardenAreaMax);
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
            if (searchData != null && filterType == Constants.SEARCH_GARDEN && !TextUtils.isEmpty(searchData.getRent())) {
                checkedPos = selectShowEditText(searchData.getRent(), list);
                showEditText(searchData.getRent(), etGardenRentMin, etGardenRentMax);
            }
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
                        showEditText(bean.getKey(), etGardenRentMin, etGardenRentMax);
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
            if (searchData != null && filterType == Constants.SEARCH_GARDEN && !TextUtils.isEmpty(searchData.getSeats())) {
                checkedPos = selectShowEditText(searchData.getSeats(), list);
                showEditText(searchData.getSeats(), etGardenSeatsMin, etGardenSeatsMax);
            }
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
                        showEditText(bean.getKey(), etGardenSeatsMin, etGardenSeatsMax);
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
            for (int i = 0; i < list.size(); i++) {
                if (gardenDecoration == list.get(i).getDictValue()) {
                    checkedPos = i;
                    break;
                }
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            TextView cbType = holder.getView(R.id.cb_item);
            ImageView ivImage = holder.getView(R.id.iv_image);
            cbType.setText(bean.getDictCname());
            if (holder.getAdapterPosition() == checkedPos) {
                cbType.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImg()).into(ivImage);
            } else {
                cbType.setTextColor(ContextCompat.getColor(mContext, R.color.text_main));
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getDictImgBlack()).into(ivImage);
            }
            holder.itemView.setOnClickListener(view -> {
                checkedPos = holder.getAdapterPosition();
                if (!rvGardenDecorate.isComputingLayout()) {
                    gardenDecoration = bean.getDictValue();
                    notifyDataSetChanged();
                }
            });
        }
    }

    //特色
    class GardenUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        @SuppressLint("UseSparseArrays")
        public GardenUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_decroation, list);
            if (gardenMap == null) {
                gardenMap = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_item);
            cbType.setText(bean.getDictCname());
            if (officeMap != null) {
                cbType.setChecked(gardenMap.containsKey(bean.getDictValue()));
            }
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (gardenMap.size() >= 4) {
                        cbType.setChecked(false);
                        ToastUtils.toastForShort(mContext, "最多选择4项");
                        return;
                    }
                    if (!gardenMap.containsKey(bean.getDictValue())) {
                        gardenMap.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    gardenMap.remove(bean.getDictValue());
                }
            });
        }
    }

    private void showEditText(String value, EditText min, EditText max) {
        String a = value.substring(0, value.indexOf(","));
        String b = value.substring(a.length() + 1);
        min.setText((TextUtils.equals("0", a)) ? "" : a);
        max.setText((TextUtils.equals(CommonList.SEARCH_MAX, b)) ? "" : b);
    }

    private int getEditMin(EditText min) {
        String text = min.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return Integer.parseInt(text);
    }

    private int getEditMax(EditText max) {
        String text = max.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return Integer.parseInt(CommonList.SEARCH_MAX);
        }
        return Integer.parseInt(text);
    }

    private String setEditText(EditText min, EditText max) {
        if (getEditMin(min) > getEditMax(max)) {
            return getEditMax(max) + "," + getEditMin(min);
        }
        return getEditMin(min) + "," + getEditMax(max);
    }

    //选择回显
    private int selectShowEditText(String data, List<WantFindBean> list) {
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(data, list.get(i).getKey())) {
                return i;
            }
        }
        return -1;
    }
}
