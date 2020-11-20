package com.owner.schedule;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.date.CustomDayView;
import com.officego.commonlib.common.date.ThemeDayView;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.dialog.ExitAppDialog;
import com.owner.schedule.contract.ViewingDateContract;
import com.owner.schedule.model.ViewingDateBean;
import com.owner.schedule.presenter.ViewingDatePresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "schedule_fragment")
public class ScheduleFragment extends BaseMvpFragment<ViewingDatePresenter>
        implements ViewingDateContract.View {
    private MonthPager monthPager;
    private RecyclerView rvToDoList;
    private TextView tvCurrentDate;
    private TextView tvNoData;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
    private boolean initiated = false;
    //adapter
    private ViewingDateAdapter viewingDateAdapter;
    //数据列表
    private List<ViewingDateBean.DataBean> viewingDateAllList = new ArrayList<>();
    //某天数据列表
    private List<ViewingDateBean.DataBean.ScheduleListBean> viewingDateDayList = new ArrayList<>();
    //滑动显示的年月
    private int mSelectedYear, mSelectedMonth;
    //今天日期
    private String mCurrentDayDate;
    //是否第一次获取数据
    private boolean isFirstGetListData;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new ViewingDatePresenter();
        mPresenter.attachView(this);
//        RelativeLayout rlTitle = mActivity.findViewById(R.id.rl_title);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
//        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
//        rlTitle.setLayoutParams(params);
        tvCurrentDate = mActivity.findViewById(R.id.tv_current_date);
        tvNoData = mActivity.findViewById(R.id.tv_no_data);
        monthPager = mActivity.findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewHeight(Utils.dpi2px(mActivity, 270));
        rvToDoList = mActivity.findViewById(R.id.rv_viewing_date);
        rvToDoList.setHasFixedSize(true);
        rvToDoList.setLayoutManager(new LinearLayoutManager(mActivity));
        //初始化
        viewingDateAdapter = null;
        currentCalendars = null;
        calendarAdapter = null;
        isFirstGetListData = true;
        initCurrentDate();
        initCalendarView();
        getViewingDateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ExitAppDialog(mActivity);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    //今天
    @Click(resName = "ll_appointment_record")
    void todayClick() {
        isFirstGetListData = true;
        onSwitchBackToDay();
        initCurrentDate();
        getViewingDateList();
    }

    //是否看房行程|看房记录
    private void getViewingDateList() {
        viewingDateAllList.clear();
        viewingDateDayList.clear();
        mPresenter.getViewingDate(DateTimeUtils.getFirstDayOfMonth(mSelectedYear, mSelectedMonth),
                DateTimeUtils.getLastDayOfMonth(mSelectedYear, mSelectedMonth));
    }

    @Click(resName = "rl_last")
    void lastClick() {
        monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
    }

    @Click(resName = "rl_next")
    void nextClick() {
        monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     */
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && !initiated) {
//            refreshMonthPager();
//            initiated = true;
//        }
//    }

    /**
     * 如果你想以周模式启动你的日历，请在onResume是调用
     * Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
     * calendarAdapter.switchToWeek(monthPager.getRowIndex());
     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    /**
     * 初始化currentDate
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvCurrentDate.setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        mSelectedYear = currentDate.getYear();
        mSelectedMonth = currentDate.getMonth();
        String mMonth = String.valueOf(currentDate.getMonth()).length() == 1 ? "0" + currentDate.getMonth() : currentDate.getMonth() + "";
        String mDay = String.valueOf(currentDate.getDay()).length() == 1 ? "0" + currentDate.getDay() : currentDate.getDay() + "";
        mCurrentDayDate = currentDate.getYear() + "-" + mMonth + "-" + mDay;
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(mActivity, R.layout.date_custom_day);
        calendarAdapter = new CalendarViewAdapter(
                mActivity,
                onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);

        calendarAdapter.setOnCalendarTypeChangedListener(type -> rvToDoList.scrollToPosition(0));
        //initMarkData();
        calendarAdapter.setMarkData(new HashMap<>());
        initMonthPager();

    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    /**
     * 点击某天日历刷新列表
     */
    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvCurrentDate.setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        String mCurrentDate;
        String mMonth = String.valueOf(currentDate.getMonth()).length() == 1 ? "0" + currentDate.getMonth() : currentDate.getMonth() + "";
        String mDay = String.valueOf(currentDate.getDay()).length() == 1 ? "0" + currentDate.getDay() : currentDate.getDay() + "";
        mCurrentDate = currentDate.getYear() + "-" + mMonth + "-" + mDay;
        selectedDayDataList(mCurrentDate);
    }

    /**
     * 选择某一天的数据列表
     * 格式2020-6-20 ok
     */
    private boolean isHasList;

    private void selectedDayDataList(String mCurrentDate) {
        if (viewingDateAllList == null) {
//            shortTip(R.string.tip_current_day_no_data);
            return;
        }
        viewingDateDayList.clear();
        isHasList = false;
        for (int i = 0; i < viewingDateAllList.size(); i++) {
            if (TextUtils.equals(mCurrentDate, viewingDateAllList.get(i).getDay())) {
                if (viewingDateAllList.get(i) != null && viewingDateAllList.get(i).getScheduleList().size() > 0) {
                    isHasList = true;
                    viewingDateDayList.addAll(viewingDateAllList.get(i).getScheduleList());
                }
                if (viewingDateAdapter == null) {
                    viewingDateAdapter = new ViewingDateAdapter(mActivity, viewingDateDayList);
                    rvToDoList.setAdapter(viewingDateAdapter);
                }
                hasData();
                viewingDateAdapter.notifyDataSetChanged();
                return;
            } else if (i == viewingDateAllList.size() - 1 && !isHasList) {
                noData();
            }
        }
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, (page, position) -> {
            position = (float) Math.sqrt(1 - Math.abs(position));
            page.setAlpha(position);
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    currentDate = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    tvCurrentDate.setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
                    //如果切换了月份
                    if (currentDate.getYear() != mSelectedYear || currentDate.getMonth() != mSelectedMonth) {
                        mSelectedYear = currentDate.getYear();
                        mSelectedMonth = currentDate.getMonth();
                        getViewingDateList();
                        //如果切回当前月，重新选中当天
                        CalendarDate mD = new CalendarDate();
                        if (mSelectedYear == mD.getYear() && mSelectedMonth == mD.getMonth()) {
                            onSwitchBackToDay();
                        }
                        return;
                    }
                    mSelectedYear = currentDate.getYear();
                    mSelectedMonth = currentDate.getMonth();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //切回当天
    public void onSwitchBackToDay() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tvCurrentDate.setText(today.getYear() + "年" + today.getMonth() + "月");
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(mActivity, R.layout.date_custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

    /**
     * 存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     * 添加日历的有数据的mark 2020-6-23 ok  | 2020-06-23 异常
     */
    private void calendarMarks(List<ViewingDateBean.DataBean> data) {
        calendarAdapter.setMarkData(new HashMap<>());//清空标记
        HashMap<String, String> markData = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDay().substring(5, 6).contains("0")) {
                StringBuilder sb = new StringBuilder(data.get(i).getDay());
                sb.replace(5, 6, "");
                String bb = sb.toString();
                if (bb.substring(7, 8).contains("0")) {
                    StringBuilder sb1 = new StringBuilder(bb);
                    sb1.replace(7, 8, "");
                    markData.put(sb1.toString(), "0");
                } else {
                    markData.put(sb.toString(), "0");
                }
            } else {
                if (data.get(i).getDay().substring(8, 9).contains("0")) {
                    StringBuilder sb = new StringBuilder(data.get(i).getDay());
                    sb.replace(8, 9, "");
                    markData.put(sb.toString(), "0");
                } else {
                    markData.put(data.get(i).getDay(), "0");
                }
            }
        }
        if (calendarAdapter != null) {
            calendarAdapter.setMarkData(markData);
            //TODO 是否首次刷新 ，此时刷新选择上一月下一月，日历异常
            if (isFirstGetListData) {
                calendarAdapter.notifyDataChanged();
                isFirstGetListData = false;
            }
        }
    }

    /**
     * 数据返回
     */
    @Override
    public void viewingDateSuccess(List<ViewingDateBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            noData();
            return;
        }
        hasData();
        calendarMarks(data); //标记
        //获取当天日期显示最近的一天数据
        viewingDateAllList.addAll(data);
        selectedDayDataList(mCurrentDayDate);
//        selectedDayDataList(viewingDateAllList.get(0).getDay());
    }

    @Override
    public void viewingDateFail(int code, String msg) {
        noData();
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rvToDoList.setVisibility(View.GONE);
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rvToDoList.setVisibility(View.VISIBLE);
    }
}


