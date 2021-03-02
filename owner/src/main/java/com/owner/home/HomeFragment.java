package com.owner.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.adapter.HomeAdapter;
import com.owner.adapter.IdentityStatusAdapter;
import com.owner.dialog.BuildingJointWorkListPopupWindow;
import com.owner.dialog.ExitAppDialog;
import com.owner.dialog.HomeMoreDialog;
import com.owner.dialog.HouseLeadDialog;
import com.owner.dialog.IdentityViewPagerDialog;
import com.owner.h5.WebViewActivity_;
import com.owner.home.contract.HomeContract;
import com.owner.home.presenter.HomePresenter;
import com.owner.identity.OwnerIdentityActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "activity_home")
public class HomeFragment extends BaseMvpFragment<HomePresenter>
        implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener,
        HomeAdapter.HomeItemListener, HomeMoreDialog.HouseMoreListener,
        BuildingJointWorkListPopupWindow.HomePopupListener,
        IdentityViewPagerDialog.IdentityViewPagerListener {
    private static final int HOUSE_ON = 1;//1发布
    private static final int HOUSE_OFF = 2;//2下架
    @ViewById(resName = "rl_title")
    RelativeLayout rlTitle;
    @ViewById(resName = "iv_left_more")
    RelativeLayout ivLeftMore;
    @ViewById(resName = "tv_expand")
    TextView tvExpand;
    @ViewById(resName = "tv_home_title")
    TextView tvHomeTitle;
    @ViewById(resName = "iv_add")
    ImageView ivAdd;
    @ViewById(resName = "bga_refresh")
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(resName = "rv_view")
    RecyclerView rvView;
    //未认证
    @ViewById(resName = "rl_to_identity")
    NestedScrollView rlToIdentity;
    //认证状态流程
    @ViewById(resName = "rl_check_status")
    RelativeLayout rlCheckStatus;
    @ViewById(resName = "rv_identity_step")
    RecyclerView rvIdentityStep;
    @ViewById(resName = "tv_reject_reason")
    TextView tvRejectReason;
    //完善楼盘信息
    @ViewById(resName = "rl_pass_through")
    RelativeLayout rlPassThrough;
    @ViewById(resName = "tv_pass_through")
    TextView tvPassThrough;
    @ViewById(resName = "tv_perfect_msg")
    TextView tvPerfectMsg;
    @ViewById(resName = "btn_perfect")
    TextView btnPerfect;
    //无数据
    @ViewById(resName = "tv_home_no_data")
    TextView tvNoData;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    //账户到期
    @ViewById(resName = "rl_user_expire")
    RelativeLayout rlUserExpire;
    //用户信息
    private UserMessageBean mUserData;
    //楼盘网点列表pop
    private BuildingJointWorkListPopupWindow popupWindow = null;
    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    private HomeAdapter homeAdapter;
    private List<HouseBean.ListBean> houseList = new ArrayList<>();
    //楼盘网点信息
    private BuildingJointWorkBean.ListBean mData;
    //更多dialog-删除，上架刷新
    private int mPosition;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        initViews();
        initRefresh();
        if (NetworkUtils.isNetworkAvailable(mActivity)) {
            new VersionDialog(mActivity, false);
            mPresenter.getUserInfo();
        } else {
            netException();
        }
    }

    private void initViews() {
        CommonHelper.setViewGroupLayoutParams(mActivity, rlTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        rvIdentityStep.setLayoutManager(layoutManager1);
    }

    //认证流程
    private void checkStatusOk(BuildingJointWorkBean.ListBean mData) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("");
        }
        IdentityStatusAdapter statusAdapter = new IdentityStatusAdapter(mActivity, list, mData);
        rvIdentityStep.setAdapter(statusAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80, R.color.common_blue_main);
        rvView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                loadingMoreList();
            }
        });
        //解决下拉刷新快速滑动crash
        rvView.setOnTouchListener((view, motionEvent) ->
                mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    //预约vr录制
    @Click(resName = "btn_vr_record")
    void vrRecordClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_VR_RECORD).start();
    }

    //去认证
    @Click(resName = "btn_identity")
    void toIdentityClick() {
        toIdentityActivity();
    }

    //去认证
    @Override
    public void toIdentity() {
        toIdentityActivity();
    }

    private void toIdentityActivity() {
        OwnerIdentityActivity_.intent(mActivity)
                .flag(Constants.IDENTITY_FIRST).start();
    }

    //网络异常重试
    @Click(resName = "btn_again")
    void netExceptionAgainClick() {
        mPresenter.getBuildingJointWorkList();
    }

    //楼盘网点列表
    @Click(resName = "iv_left_more")
    void leftListClick() {
        if (mUserData != null && mUserData.getAuditStatus() == -1) {//未认证
            shortTip("请先认证");
            return;
        }
        mPresenter.getBuildingJointWorkList();
    }

    //添加房源
    @Click(resName = "iv_add")
    void addClick() {
        if (mData != null) {
            gotoAddHouseActivity(new BuildingManagerBean(mData.getBuildingId(), mData.getIsTemp()));
        }
    }

    //完善楼盘，网点资料
    @Click(resName = "btn_perfect")
    void perfectClick() {
        if (mData != null) {
            BuildingManagerBean bean = new BuildingManagerBean(mData.getBuildingId(), mData.getIsTemp());
            if (Constants.TYPE_JOINTWORK == mData.getBtype()) {
                EditJointWorkActivity_.intent(mActivity).buildingManagerBean(bean).isRefreshHouseList(true).start();
            } else {
                EditBuildingActivity_.intent(mActivity).buildingManagerBean(bean).isRefreshHouseList(true).start();
            }
        }
    }

    //账户过期客服
    @Click(resName = "btn_service")
    void serviceClick() {
        mPresenter.getSupportMobile();
    }

    //当切换tab 是否刷新首页数据
    private void isRefreshHome(boolean isRefresh) {
        Constants.IS_HOME_REFRESH = isRefresh;
    }

    //添加房源
    private void gotoAddHouseActivity(BuildingManagerBean managerBean) {
        if (Constants.TYPE_JOINTWORK == mData.getBtype()) {
            AddEditIndependentActivity_.intent(mActivity)
                    .buildingFlag(Constants.BUILDING_FLAG_ADD)
                    .buildingManagerBean(managerBean)
                    .start();
        } else {
            AddEditHouseActivity_.intent(mActivity)
                    .buildingFlag(Constants.BUILDING_FLAG_ADD)
                    .buildingManagerBean(managerBean)
                    .start();
        }
    }

    //楼盘网点Popup列表
    @Override
    public void buildingJointWorkListSuccess(BuildingJointWorkBean data) {
        if (data == null) {
            shortTip("暂无数据");
        } else {
            ivLeftMore.setVisibility(View.GONE);
            tvHomeTitle.setVisibility(View.GONE);
            ivAdd.setVisibility(View.GONE);
            tvExpand.setVisibility(View.VISIBLE);
            popupWindow = new BuildingJointWorkListPopupWindow(mActivity, mUserData, rlTitle, data.getList());
            popupWindow.setListener(this);
        }
    }

    //默认选择了楼盘
    //0: 下架(未发布),1: 上架(已发布) ;2:资料待完善  3: 置顶推荐;4:已售完;5:删除;6待审核7已驳回
    @Override
    public void initHouseList(BuildingJointWorkBean data) {
        if (data != null) {
            boolean isRecordBuildingFlag = false;
            if (data.getList().size() > 0) {
                for (BuildingJointWorkBean.ListBean bean : data.getList()) {
                    if (TextUtils.equals(Constants.mCurrentBuildingName, bean.getBuildingName())) {
                        isRecordBuildingFlag = true;
                        toLoadHouseData(bean);
                        break;
                    }
                }
                if (!isRecordBuildingFlag) {
                    toLoadHouseData(data.getList().get(0));
                }
            }
        }
    }

    //左侧Popup选择楼盘打开房源
    //0: 下架(未发布),1: 上架(已发布) ;2:资料待完善  3: 置顶推荐;4:已售完;5:删除;6待审核7已驳回
    @Override
    public void popupHouseList(BuildingJointWorkBean.ListBean bean) {
        toLoadHouseData(bean);
    }

    private void toLoadHouseData(BuildingJointWorkBean.ListBean bean) {
        Constants.mCurrentBuildingName = bean.getBuildingName();//选择的第几个楼盘
        currentBuildingMessage(bean);
        if (bean.getIsTemp() == 0) {
            getRefreshHouseList();//房源列表
            return;
        }
        if (6 == bean.getStatus()) { //6待审核
            isRefreshHome(true);//刷新
            identityDoingView();
            checkStatusOk(mData);
        } else if (7 == bean.getStatus()) {//7已驳回
            isRefreshHome(true);//刷新
            identityRejectView();
            checkStatusOk(mData);
            tvRejectReason.setText(reasonsReject());
        }
    }

    private String reasonsReject() {
        StringBuilder builder = new StringBuilder();
        if (mData.getRemark() != null && mData.getRemark().size() > 0) {
            String value;
            for (int i = 0; i < mData.getRemark().size(); i++) {
                value = mData.getRemark().get(i).getDictCname();
                if (i == mData.getRemark().size() - 1) {
                    builder.append(value);
                } else {
                    builder.append(value).append("\n");
                }
            }
            return "驳回原因：" + builder.toString();
        }
        return "驳回原因：无";
    }

    private void getRefreshHouseList() {
        houseList.clear();
        pageNum = 1;
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
        getHouseList();
    }

    //房源列表
    @Override
    public void houseListSuccess(List<HouseBean.ListBean> data, boolean hasMore) {
        if (2 == mData.getStatus()) {//资料待完善
            identityPassThroughView(mData.getBtype(), true);
            return;
        }
        if (data == null || (pageNum == 1 && data.size() == 0)) {
            if (0 == mData.getStatus() || 1 == mData.getStatus()) {//已完善
                identityPassThroughView(mData.getBtype(), false);
                if (TextUtils.isEmpty(SpUtils.getHouseLead())) {
                    new HouseLeadDialog(mActivity);//引导-我知道了
                }
            }
            return;
        }
        if (pageNum > 1 && data.size() == 0) {
            noData();
            return;
        }
        isRefreshHome(false);//不刷新
        hasData();
        this.hasMore = hasMore;
        houseList.addAll(data);
        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(mActivity, houseList);
            rvView.setAdapter(homeAdapter);
            homeAdapter.setListener(this);
        } else {
            homeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void popupDismiss() {
        ivLeftMore.setVisibility(View.VISIBLE);
        tvHomeTitle.setVisibility(View.VISIBLE);
        ivAdd.setVisibility(View.VISIBLE);
        tvExpand.setVisibility(View.GONE);
        if (mData != null) {
            ivAdd.setVisibility(mData.isAddHouse() ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 1发布2下架
     * true开放工位（关闭-下架）false（去发布）
     */
    @Override
    public void itemPublishStatus(int position, HouseBean.ListBean bean, boolean isOpenSeats) {
        mPosition = position;
        mPresenter.isPublishHouse(bean.getHouseId(), isOpenSeats ? HOUSE_OFF : HOUSE_ON, bean.getIsTemp());
    }

    @Override
    public void itemMore(HouseBean.ListBean bean, int position) {
        new HomeMoreDialog(mActivity, bean, position).setListener(this);
    }

    //更多dialog下架 删除房源
    @Override
    public void toMoreHouseManager(boolean isDeleteHouse, HouseBean.ListBean bean, int position) {
        mPosition = position;
        if (isDeleteHouse) {
            CommonDialog dialog = new CommonDialog.Builder(mActivity)
                    .setTitle("确定删除房源吗？")
                    .setCancelButton(R.string.sm_cancel)
                    .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                        dialog12.dismiss();
                        mPresenter.houseDelete(bean.getHouseId(), bean.getIsTemp());
                    }).create();
            dialog.showWithOutTouchable(false);
        } else {
            mPresenter.isPublishHouse(bean.getHouseId(), HOUSE_OFF, bean.getIsTemp());
        }
    }

    //房源删除成功刷新
    @Override
    public void houseDeleteSuccess() {
        if (houseList != null) {
            houseList.remove(mPosition);
            homeAdapter.notifyDataSetChanged();
        }
    }

    //下架或发布成功刷新 1发布2下架
    @Override
    public void publishOrOffHouseSuccess(int currentStatus) {
        shortTip(currentStatus == 1 ? "房源已发布" : "房源已下架");
        houseList.get(mPosition).setHouseStatus(currentStatus);
        homeAdapter.notifyItemChanged(mPosition);
    }

    //获取当前楼盘的信息
    private void currentBuildingMessage(BuildingJointWorkBean.ListBean bean) {
        if (Constants.TYPE_BUILDING == bean.getBtype()) {
            Constants.FLOOR_COUNTS = bean.getTotalFloor();
        } else {
            Constants.FLOOR_JOINT_WORK_COUNTS = bean.getTotalFloor();
        }
        ivAdd.setVisibility(bean.isAddHouse() ? View.VISIBLE : View.GONE);
        tvHomeTitle.setText(bean.getBuildingName());
        mData = bean;
    }

    private void getHouseList() {
        mPresenter.getHouseList(mData.getBuildingId(), mData.getIsTemp(), pageNum);
    }

    /**
     * 身份类型 0个人1企业2联合
     * auditStatus -1未认证 0待审核1审核通过2审核未通过
     */
    @Override
    public void userInfoSuccess(UserMessageBean data) {
        mUserData = data;
        if (data.getAuditStatus() == -1) {//未认证
            isRefreshHome(true);
            cannotIdentity();
        } else {
            mPresenter.getBuildingList();//楼盘网点列表
        }
        //账号试用到期
        userExpire(data);
    }

    //账号试用到期 1:在试用期 0:账号已过试用期
    private void userExpire(UserMessageBean data) {
        rlUserExpire.setEnabled(true);
        rlUserExpire.setClickable(true);
        rlUserExpire.setVisibility(CommonHelper.bigDecimal(data.getMsgStatus()) == 1
                ? View.VISIBLE : View.GONE);
    }

    //加载更多
    private void loadingMoreList() {
        if (NetworkUtils.isNetworkAvailable(mActivity) && hasMore) {
            pageNum++;
            getHouseList();
        }
    }

    //开始下拉刷新
    @Override
    public void onRefresh() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        getRefreshHouseList();
    }

    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //客服
    @Override
    public void supportMobileSuccess(String mobile) {
        CommonHelper.callPhone(mActivity, mobile);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.updateBuildingSuccess,
                CommonNotifications.updateHouseSuccess,
                CommonNotifications.rejectBuildingSuccess,
                CommonNotifications.checkedIdentitySuccess,
                CommonNotifications.refreshHouseSuccess};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args != null) {
            if (id == CommonNotifications.updateBuildingSuccess) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                mPresenter.getBuildingJointWorkList();
            } else if (id == CommonNotifications.updateHouseSuccess) {
                //刷新房源
                getRefreshHouseList();
            } else if (id == CommonNotifications.rejectBuildingSuccess) {
                //驳回重新认证 --先调楼盘列表获取之前的位置在刷新
                mPresenter.getUserInfo();
            } else if (id == CommonNotifications.checkedIdentitySuccess) {
                //认证提交
                mPresenter.getUserInfo();
            } else if (id == CommonNotifications.refreshHouseSuccess) {
                //首页编辑楼盘网点成功
                mPresenter.getUserInfo();
            }
        }
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rlException.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.GONE);
        houseList.clear();
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    private void hasData() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        rvView.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        rlToIdentity.setVisibility(View.GONE);
        rlCheckStatus.setVisibility(View.GONE);
        tvRejectReason.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.GONE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        rvView.setVisibility(View.GONE);
        rlToIdentity.setVisibility(View.GONE);
        rlCheckStatus.setVisibility(View.GONE);
        tvRejectReason.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.GONE);
    }

    private void cannotIdentity() {
        rlToIdentity.setVisibility(View.VISIBLE);
        rlCheckStatus.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        rvView.setVisibility(View.GONE);
        tvRejectReason.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.GONE);
        if (TextUtils.isEmpty(SpUtils.getToIdentity())) {
            //未认证dialog
            new IdentityViewPagerDialog(mActivity).setListener(this);
        }
    }

    //认证中或通过
    private void identityDoingView() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rvView.setVisibility(View.GONE);
        rlToIdentity.setVisibility(View.GONE);
        rlCheckStatus.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        tvRejectReason.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.GONE);
    }

    //认证驳回
    private void identityRejectView() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rvView.setVisibility(View.GONE);
        rlToIdentity.setVisibility(View.GONE);
        rlCheckStatus.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        tvRejectReason.setVisibility(View.VISIBLE);//驳回原因
        rlPassThrough.setVisibility(View.GONE);
    }

    /**
     * 认证通过
     *
     * @param btype       楼盘或网点
     * @param isToPerfect 是否去完善信息
     */
    private void identityPassThroughView(int btype, boolean isToPerfect) {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rvView.setVisibility(View.GONE);
        rlToIdentity.setVisibility(View.GONE);
        rlCheckStatus.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        rlPassThrough.setVisibility(View.VISIBLE);
        if (mData != null) {
            if (isToPerfect) { //信息待完善
                ivAdd.setVisibility(View.GONE);
                tvPassThrough.setVisibility(View.VISIBLE);
                btnPerfect.setVisibility(View.VISIBLE);
                tvPerfectMsg.setText(Constants.TYPE_BUILDING == btype ? R.string.str_to_perfect_building : R.string.str_to_perfect_jointwork);
                btnPerfect.setText(Constants.TYPE_BUILDING == btype ? "完善楼盘信息" : "完善网点信息");
            } else {//已完善，可添加房源
                ivAdd.setVisibility(View.VISIBLE);
                tvPassThrough.setVisibility(View.GONE);
                btnPerfect.setVisibility(View.GONE);
                tvPerfectMsg.setText(R.string.str_tip_add_house);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ExitAppDialog(mActivity);
        }
    }
}
