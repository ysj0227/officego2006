package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.model.BusinessCircleBean;
import com.officego.ui.home.model.MeterBean;
import com.officego.utils.SpaceItemDecoration;
import com.officego.view.SeekBarPressure;

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
    /**
     * 当店铺列表大于固定数量，RecyclerView设置固定高度
     */
    private Activity mContext;
    private TextView mSetTitleView;

    private HashSet<Integer> mHashSetLine, mHashSetBusiness;
    private SparseBooleanArray mCheckStatesLine, mCheckStatesBusiness;//记录选中的位置
    private String district = "", business = "";//商圈
    private String line = "", nearbySubway = ""; //地铁
    private int btype;//楼盘,网点
    private String constructionArea = "";//面积
    private String rentPrice = "", rentPrice2 = ""; //写字楼，联合办公
    private String simple = "", simple2 = "";//写字楼，联合办公
    private String decoration = "";//装修类型
    private String houseTags = "";//装修特色
    private String sort = "0";//排序

    private final int SEARCH_TYPE_AREA = 0;
    private final int SEARCH_TYPE_OFFICE = 1;
    private final int SEARCH_TYPE_ORDER = 2;
    private final int SEARCH_TYPE_CONDITION = 3;
    private int layout;
    //搜索类型
    private int mSearchType;
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
                                    String simple, String decoration, String tags);
    }

    /**
     * popupWindow设置参数
     */
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
                             String nearbySubway, String area, String dayPrice, String seats,
                             String decoration, String houseTags, String sort) {
        super();
        this.mContext = activity;
        this.mSetTitleView = setTextView;
        this.mSearchType = searchType;
        this.btype = btype;
        if (TextUtils.isEmpty(district) && TextUtils.isEmpty(business)) {
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
        this.constructionArea = area;
        if (btype == 0 || btype == 1) {
            this.rentPrice = dayPrice;
            this.simple = seats;
        } else {
            this.rentPrice2 = dayPrice;
            this.simple2 = seats;
        }
        this.decoration = decoration;
        this.houseTags = houseTags;
        this.sort = sort;

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
        //数据处理
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
            // LogCat.e("TAG", "topY:" + topY + "  event.getY: " + event.getY() + "  getRawY:" + event.getRawY());
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

    /**
     * Android 7.0以上 view显示问题
     */
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

    /**
     * 根据不同的layout处理不同的数据
     */
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

        //点击监听
        View.OnClickListener clickListener = v -> {
            switch (v.getId()) {
                case R.id.tv_shopping://商圈
                    isLine = false;
                    tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                    tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                    recyclerViewCenter.setAdapter(new BusinessCircleAdapter(mContext, tvBusinessCircleNum, businessCircleList, recyclerViewRight));
                    tvMeterNum.setText("");
                    tvMeterNum.setVisibility(View.GONE);
                    break;
                case R.id.tv_meter://地铁
                    isLine = true;
                    tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                    tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                    meterAdapter = new MeterAdapter(mContext, tvMeterNum, meterList, recyclerViewRight);
                    recyclerViewCenter.setAdapter(meterAdapter);
                    tvBusinessCircleNum.setText("");
                    tvBusinessCircleNum.setVisibility(View.GONE);
                    break;
                case R.id.btn_clear://清理
                    if (isLine) {
                        line = "";
                        nearbySubway = "";
                        clearMeterHashSet(tvMeterNum);
                        tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                        tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                        meterAdapter = new MeterAdapter(mContext, tvMeterNum, meterList, recyclerViewRight);
                        recyclerViewCenter.setAdapter(meterAdapter);
                    } else {
                        district = "";
                        business = "";
                        clearBusinessHashSet(tvBusinessCircleNum);
                        tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                        tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                        recyclerViewCenter.setAdapter(new BusinessCircleAdapter(mContext, tvBusinessCircleNum, businessCircleList, recyclerViewRight));
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
                LogCat.e("TAG", "getSubwayList onSuccess =" + data);
                meterList.clear();
                meterList = data;
                if (!TextUtils.isEmpty(nearbySubway)) {
                    isLine = true;
                    tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                    tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                    meterAdapter = new MeterAdapter(mContext, tvMeterNum, meterList, recyclerViewRight);
                    recyclerViewCenter.setAdapter(meterAdapter);
                }
            }

            @Override
            public void onFail(int code, String msg, List<MeterBean.DataBean> data) {
                LogCat.e("TAG", "getSubwayList onFail code=" + code + "  msg=" + msg);
            }
        });
    }

    //商圈
    private BusinessCircleAdapter businessCircleAdapter;

    private void getSearchDistrictList(TextView tvBusinessCircleText, TextView tvMeterText,
                                       RecyclerView recyclerViewCenter,
                                       TextView tvBusinessCircleNum,
                                       RecyclerView recyclerViewRight) {
        OfficegoApi.getInstance().getDistrictList(new RetrofitCallback<List<BusinessCircleBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BusinessCircleBean.DataBean> data) {
                LogCat.e("TAG", "getDistrictList onSuccess =" + data);
                businessCircleList.clear();
                businessCircleList = data;
                //初始化默认选中商圈
                tvBusinessCircleText.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
                tvMeterText.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
                businessCircleAdapter = new BusinessCircleAdapter(mContext, tvBusinessCircleNum,
                        businessCircleList, recyclerViewRight);
                recyclerViewCenter.setAdapter(businessCircleAdapter);
            }

            @Override
            public void onFail(int code, String msg, List<BusinessCircleBean.DataBean> data) {
                LogCat.e("TAG", "getDistrictList onFail code=" + code + "  msg=" + msg);

            }
        });
    }

    //写字楼,联合办公（网点） 类型1:楼盘,2:网点, 0全部
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

    //筛选
    private void setSeekBarPressure(String officeText, SeekBarPressure sbpSeekBar) {
        if (officeText.contains(",")) {
            String str1 = officeText.substring(0, officeText.indexOf(","));
            String start = officeText.substring(0, str1.length());
            String end = officeText.substring(str1.length() + 1);
            sbpSeekBar.setProgressLow(Double.valueOf(start));
            sbpSeekBar.setProgressHigh(Double.valueOf(end));
        }
    }

    private void handleCondition(View viewLayout) {
        RadioGroup rgHouseGroup = viewLayout.findViewById(R.id.rg_house_type);
        RadioButton rbOffice = viewLayout.findViewById(R.id.rb_office);
        RadioButton rbJointWork = viewLayout.findViewById(R.id.rb_joint_work);
        RecyclerView rvDecorationType = viewLayout.findViewById(R.id.rv_decoration_type);
        RecyclerView rvHouseUnique = viewLayout.findViewById(R.id.rv_house_characteristic);
        TextView tvDecorationType = viewLayout.findViewById(R.id.tv_decoration_type);
        //选择面积租金工位
        TextView tvArea = viewLayout.findViewById(R.id.tv_area);
        SeekBarPressure sbpArea = viewLayout.findViewById(R.id.sbp_area);
        TextView tvRent = viewLayout.findViewById(R.id.tv_rent);
        SeekBarPressure sbpRent = viewLayout.findViewById(R.id.sbp_rent);
        TextView tvWorkstation = viewLayout.findViewById(R.id.tv_workstation);
        SeekBarPressure sbpSimple = viewLayout.findViewById(R.id.sbp_workstation);
        //联合办公 租金/工位
        SeekBarPressure sbpRent2 = viewLayout.findViewById(R.id.sbp_rent2);
        SeekBarPressure sbpSimple2 = viewLayout.findViewById(R.id.sbp_workstation2);
        Button btnClear = viewLayout.findViewById(R.id.btn_clear);
        Button btnSure = viewLayout.findViewById(R.id.btn_sure);
        //初始文本
        tvArea.setText(Html.fromHtml(mContext.getString(R.string.str_text_area)));
        tvRent.setText(Html.fromHtml(mContext.getString(R.string.str_text_rent)));
        tvWorkstation.setText(Html.fromHtml(mContext.getString(R.string.str_text_workstation)));
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        rvDecorationType.setLayoutManager(layoutManager);
        rvDecorationType.addItemDecoration(new SpaceItemDecoration(mContext, 3));
        GridLayoutManager layoutManager1 = new GridLayoutManager(mContext, 3);
        rvHouseUnique.setLayoutManager(layoutManager1);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(mContext, 3));
        if (btype == 0) {
            rgHouseGroup.setVisibility(View.VISIBLE);
        } else if (btype == 1) {
            rgHouseGroup.setVisibility(View.GONE);
            showConditionOfficeLayout(true, rvDecorationType, tvDecorationType,
                    tvArea, sbpArea, sbpRent, tvWorkstation, sbpSimple, sbpRent2, sbpSimple2);
        } else if (btype == 2) {
            rgHouseGroup.setVisibility(View.GONE);
            showConditionOfficeLayout(false, rvDecorationType, tvDecorationType,
                    tvArea, sbpArea, sbpRent, tvWorkstation, sbpSimple, sbpRent2, sbpSimple2);
        }
        //请求list
        getDecorationTypeList(rvDecorationType);
        getHouseUniqueList(rvHouseUnique);
        LogCat.e("TAG", "constructionArea=" + constructionArea + "rentPrice=" + rentPrice);
        //初始化数据
        sbpArea.setProgressMax(1000, 1000);
        sbpRent.setProgressMax(10, 10);
        sbpSimple.setProgressMax(500, 500);
        sbpRent2.setProgressMax(100000, 100000);
        sbpSimple2.setProgressMax(20, 20);
        if (btype == 0 || btype == 1) {
            setSeekBarPressure(this.constructionArea, sbpArea);
            setSeekBarPressure(this.rentPrice, sbpRent);
            setSeekBarPressure(this.simple, sbpSimple);
        } else if (btype == 2) {
            setSeekBarPressure(this.rentPrice2, sbpRent2);
            setSeekBarPressure(this.simple2, sbpSimple2);
        }
        //监听
        sbpArea.setOnSeekBarChangeListener(listener, 0);
        sbpRent.setOnSeekBarChangeListener(listener, 1);
        sbpSimple.setOnSeekBarChangeListener(listener, 2);
        sbpRent2.setOnSeekBarChangeListener(listener, 3);
        sbpSimple2.setOnSeekBarChangeListener(listener, 4);
        rbOffice.setOnClickListener(v -> {
            showConditionOfficeLayout(true, rvDecorationType, tvDecorationType,
                    tvArea, sbpArea, sbpRent, tvWorkstation, sbpSimple, sbpRent2, sbpSimple2);
        });
        rbJointWork.setOnClickListener(v -> {
            showConditionOfficeLayout(false, rvDecorationType, tvDecorationType,
                    tvArea, sbpArea, sbpRent, tvWorkstation, sbpSimple, sbpRent2, sbpSimple2);
        });
        //clear
        btnClear.setOnClickListener(v -> {
            //clear text
            this.constructionArea = "";
            this.rentPrice = "";
            this.simple = "";
            this.rentPrice2 = "";
            this.simple2 = "";
            this.decoration = "";
            this.houseTags = "";
            //请求list
            getDecorationTypeList(rvDecorationType);
            getHouseUniqueList(rvHouseUnique);
            //初始化数
            sbpArea.setProgressLow(0);
            sbpArea.setProgressHigh(1000);
            sbpRent.setProgressLow(0);
            sbpRent.setProgressHigh(10);
            sbpSimple.setProgressLow(0);
            sbpSimple.setProgressHigh(500);
            sbpRent2.setProgressLow(0);
            sbpRent2.setProgressHigh(100000);
            sbpSimple2.setProgressLow(0);
            sbpSimple2.setProgressHigh(20);
            //清理
            clearCondition(rbOffice);
        });
        btnSure.setOnClickListener(v -> {
            //清理
            clearCondition(rbOffice);
        });
    }

    private void clearCondition(RadioButton rbOffice) {
        dismiss();
        String mArea, mRentPrice, mSimple;
        btype = rbOffice.isChecked() ? 1 : 2;
        mRentPrice = rbOffice.isChecked() ? rentPrice : rentPrice2;
        mSimple = rbOffice.isChecked() ? simple : simple2;
        mArea = rbOffice.isChecked() ? constructionArea : "";
        onSureClickListener.onConditionPopUpWindow(mSearchType, btype, mArea, mRentPrice, mSimple, decoration, houseTags);

    }


    private void showConditionOfficeLayout(boolean isOffice, RecyclerView rvDecorationType, TextView tvDecorationType,
                                           TextView tvArea, SeekBarPressure sbpArea, SeekBarPressure sbpRent, TextView tvWorkstation,
                                           SeekBarPressure sbpSimple, SeekBarPressure sbpRent2, SeekBarPressure sbpSimple2) {
        if (isOffice) {
            tvDecorationType.setVisibility(View.VISIBLE);
            rvDecorationType.setVisibility(View.VISIBLE);
            tvArea.setVisibility(View.VISIBLE);
            sbpArea.setVisibility(View.VISIBLE);
            sbpRent.setVisibility(View.VISIBLE);
            tvWorkstation.setVisibility(View.GONE);// 办公室没有工位
            sbpSimple.setVisibility(View.GONE);// 办公室没有工位
            sbpRent2.setVisibility(View.GONE);
            sbpSimple2.setVisibility(View.GONE);
        } else {
            tvDecorationType.setVisibility(View.GONE);
            rvDecorationType.setVisibility(View.GONE);
            tvArea.setVisibility(View.GONE);
            sbpArea.setVisibility(View.GONE);
            sbpRent.setVisibility(View.GONE);
            sbpSimple.setVisibility(View.GONE);
            tvWorkstation.setVisibility(View.VISIBLE);
            sbpRent2.setVisibility(View.VISIBLE);
            sbpSimple2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * seekBar
     */
    SeekBarPressure.OnSeekBarChangeListener listener = new SeekBarPressure.OnSeekBarChangeListener() {
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
                simple = (int) progressLow + "," + (int) progressHigh;
            } else if (type == 3) {
                //联合办公租金
                rentPrice2 = (int) progressLow + "," + (int) progressHigh;
            } else if (type == 4) {
                //联合办公工位
                simple2 = (int) progressLow + "," + (int) progressHigh;
            }
        }

        @Override
        public void onProgressAfter() {

        }
    };

    //装修类型
    private void getDecorationTypeList(RecyclerView rvDecorationType) {
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                LogCat.e("TAG", "getDecoratedType onSuccess =" + data);
                DecorationTypeAdapter adapter = new DecorationTypeAdapter(mContext, data);
                rvDecorationType.setAdapter(adapter);
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
            }
        });
    }

    //房源特色
    private void getHouseUniqueList(RecyclerView rvHouseUnique) {
        OfficegoApi.getInstance().getHouseUnique(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                LogCat.e("TAG", "getHouseUnique onSuccess =" + data);
                HouseUniqueAdapter adapter = new HouseUniqueAdapter(mContext, data);
                rvHouseUnique.setAdapter(adapter);
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                LogCat.e("TAG", "getHouseUnique onFail code=" + code + "  msg=" + msg);
            }
        });
    }

    /**
     * 地铁数据
     */
    private class MeterAdapter extends CommonListAdapter<MeterBean.DataBean> {
        /**
         * @param context 上下文
         * @param list    列表数据
         */
        private RecyclerView recyclerViewRight;
        private TextView tvNum;

        private Map<Integer, Boolean> map = new HashMap<>();
        private boolean onBind;
        private int checkedPosition = 0;//默认选中第一个

        public MeterAdapter(Context context, TextView tvNum, List<MeterBean.DataBean> list, RecyclerView recyclerViewRight) {
            super(context, R.layout.item_search_meter, list);
            this.recyclerViewRight = recyclerViewRight;
            this.tvNum = tvNum;
            if (TextUtils.isEmpty(line)) {
                //如果没有选择条件，默认选择第一个
                recyclerViewRight.setAdapter(new StationAdapter(mContext, tvNum, list.get(0).getList()));
            } else {
                recyclerViewRight.setAdapter(new StationAdapter(mContext, tvNum, list.get(Integer.valueOf(line) - 1).getList()));
            }
        }

        @Override
        public void convert(ViewHolder holder, MeterBean.DataBean meterBean) {
            TextView itemMeter = holder.getView(R.id.tv_item_meter);
            itemMeter.setText(meterBean.getLine());
            //选择筛选条件的
            if (TextUtils.isEmpty(line)) {
                if (checkedPosition == 0) {
                    map.put(0, true);
                }
            } else {
                map.put(Integer.valueOf(line) - 1, true);
            }
            holder.itemView.setOnClickListener(v -> {
                map.clear();
                checkedPosition = holder.getAdapterPosition();
                map.put(holder.getAdapterPosition(), true);
                if (!onBind) {
                    notifyDataSetChanged();
                }
                //clear 之前选中的子项
                clearMeterHashSet(tvNum);
                //赋值
                line = meterBean.getLid();
                //地铁列表详情列表
                recyclerViewRight.setAdapter(new StationAdapter(mContext, tvNum, meterBean.getList()));
            });
            //显示选中的文本
            onBind = true;
            if (map != null && map.containsKey(holder.getAdapterPosition())) {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    //clear 之前选中的子项
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
                } else {
                    checkStates.delete(pos);
                    hashSet.remove(bean.getId());
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
            });
            cbStation.setChecked(checkStates.get(holder.getAdapterPosition(), false));
        }
    }

    /**
     * 商圈
     */
    private class BusinessCircleAdapter extends CommonListAdapter<BusinessCircleBean.DataBean> {
        private RecyclerView recyclerViewRight;
        private TextView tvNum;
        private Map<Integer, Boolean> map = new HashMap<>();
        private boolean onBind;
        private int checkedPosition = 0;//默认选中第一个

        BusinessCircleAdapter(Context context, TextView tvNum, List<BusinessCircleBean.DataBean> list, RecyclerView recyclerViewRight) {
            super(context, R.layout.item_search_meter, list);
            this.recyclerViewRight = recyclerViewRight;
            this.tvNum = tvNum;
            if (TextUtils.isEmpty(district)) {
                //如果没有选择条件，默认选择第一个
                recyclerViewRight.setAdapter(new BusinessCircleDetailsAdapter(mContext, tvNum, list.get(0).getList()));
            } else {

                recyclerViewRight.setAdapter(new BusinessCircleDetailsAdapter(mContext, tvNum, list.get(Integer.valueOf(district) - 1).getList()));
            }
        }

        @Override
        public void convert(ViewHolder holder, BusinessCircleBean.DataBean bean) {
            TextView itemMeter = holder.getView(R.id.tv_item_meter);
            itemMeter.setText(bean.getDistrict());
            if (TextUtils.isEmpty(district)) {
                if (checkedPosition == 0) {
                    map.put(0, true);
                }
            } else {
                map.put(Integer.valueOf(district) - 1, true);//选择筛选条件的
            }
            holder.itemView.setOnClickListener(v -> {
                map.clear();
                checkedPosition = holder.getAdapterPosition();
                map.put(holder.getAdapterPosition(), true);
                if (!onBind) {
                    notifyDataSetChanged();
                }
                //clear 之前选中的子项
                clearBusinessHashSet(tvNum);
                //赋值
                district = String.valueOf(bean.getDistrictID());
                recyclerViewRight.setAdapter(new BusinessCircleDetailsAdapter(mContext, tvNum, bean.getList()));
            });
            //显示选中的文本
            onBind = true;
            if (map != null && map.containsKey(holder.getAdapterPosition())) {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemMeter.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    //clear 之前选中的子项
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
                } else {
                    checkStates.delete(pos);
                    hashSet.remove(bean.getId());
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
            });
            cbStation.setChecked(checkStates.get(holder.getAdapterPosition(), false));
        }
    }

    private class HouseUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        //当前选中的数据列表
        private Map<Integer, String> map;

        public HouseUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_type, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_type);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    map.put(bean.getDictValue(), bean.getDictCname());
                } else {
                    map.remove(bean.getDictValue());
                }
                houseTags = getKey(map);
            });
        }
    }

    private class DecorationTypeAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
        //当前选中的数据列表
        private Map<Integer, String> map;

        @SuppressLint("UseSparseArrays")
        DecorationTypeAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_house_type, list);
            map = new HashMap<>();
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbType = holder.getView(R.id.cb_type);
            cbType.setText(bean.getDictCname());
            cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    map.put(bean.getDictValue(), bean.getDictCname());
                } else {
                    map.remove(bean.getDictValue());
                }
                decoration = getKey(map);
            });
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
        //LogCat.e("TAG", "getHashSetKey key = " + key);
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
        //LogCat.e("TAG", "key = " + key);
        return key.toString();
    }
}
