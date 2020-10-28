package com.owner.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
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
import com.owner.dialog.BuildingJointWorkListPopupWindow;
import com.owner.dialog.HomeMoreDialog;
import com.owner.home.contract.HomeContract;
import com.owner.home.presenter.HomePresenter;
import com.owner.identity.SelectIdActivity_;
import com.owner.mine.model.UserOwnerBean;
import com.owner.utils.UnIdifyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.officego.commonlib.utils.PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE;

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
        BuildingJointWorkListPopupWindow.HomePopupListener {
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
    @ViewById(resName = "tv_no_data")
    TextView tvNoData;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    @ViewById(resName = "btn_again")
    Button btnAgain;

    private UserOwnerBean mUserData;
    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    private HomeAdapter homeAdapter;
    private List<HouseBean.ListBean> houseList = new ArrayList<>();
    //楼盘网点信息
    private BuildingJointWorkBean.ListBean mData;
    private int buildingId;
    //更多dialog-删除，上架刷新
    private int mPosition;

    @AfterViews
    void init() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        CommonHelper.setViewGroupLayoutParams(mActivity, rlTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        initRefresh();
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        new VersionDialog(mActivity);
        mPresenter.getUserInfo();
        fragmentCheckSDCardCameraPermission();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
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

    //网络异常重试
    @Click(resName = "btn_again")
    void netExceptionAgainClick() {
        mPresenter.getBuildingJointWorkList();
    }

    //楼盘网点列表
    @Click(resName = "iv_left_more")
    void leftListClick() {
        mPresenter.getBuildingJointWorkList();
    }

    //添加房源
    @Click(resName = "iv_add")
    void addClick() {
//        final String[] items = {"楼盘", "楼盘_办公室", "网点", "独立办公室", "开放工位"};
//        new AlertDialog.Builder(mActivity)
//                .setItems(items, (dialogInterface, i) -> {
//                    if (i == 0) {
//                        AddBuildingActivity_.intent(mActivity).start();
//                    } else if (i == 1) {
//                        AddHouseActivity_.intent(mActivity).start();
//                    } else if (i == 2) {
//                        AddJointWorkActivity_.intent(mActivity).start();
//                    } else if (i == 3) {
//                        AddIndependentActivity_.intent(mActivity).start();
//                    } else {
//                        AddOpenSeatsActivity_.intent(mActivity).start();
//                    }
//                }).create().show();
        gotoAddHouseActivity();
    }

    //添加房源
    private void gotoAddHouseActivity() {
        if (mUserData.getIdentityType() == Constants.TYPE_JOINTWORK) {
            AddIndependentActivity_.intent(mActivity).start();
        } else {
            AddHouseActivity_.intent(mActivity).start();
        }
    }

    //楼盘网点列表
    @Override
    public void buildingJointWorkListSuccess(BuildingJointWorkBean data) {
        ivLeftMore.setVisibility(View.GONE);
        tvHomeTitle.setVisibility(View.GONE);
        ivAdd.setVisibility(View.GONE);
        tvExpand.setVisibility(View.VISIBLE);
        new BuildingJointWorkListPopupWindow(mActivity, mUserData, rlTitle, data.getList()).setListener(this);
    }

    //房源列表
    @Override
    public void houseListSuccess(List<HouseBean.ListBean> data, boolean hasMore) {
        if (data == null || pageNum == 1 && data.size() == 0) {
            noData();
            return;
        }
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
    }

    @Override
    public void initHouseData(BuildingJointWorkBean.ListBean bean) {
        tvHomeTitle.setText(bean.getBuildingName());
        buildingId = bean.getBuildingId();
        mData = bean;
        getHouseList();
    }


    @Override
    public void itemEdit(HouseBean.ListBean bean) {
        //编辑房源 独立办公室 开放工位
        if (Constants.TYPE_BUILDING == bean.getBtype()) {
            AddHouseActivity_.intent(getContext()).start();
        } else {
            if (bean.getOfficeType() == 2) {//2开放工位
                AddOpenSeatsActivity_.intent(mActivity).start();
            } else {//1独立办公室
                AddIndependentActivity_.intent(mActivity).start();
            }
        }
    }

    /**
     * 1发布2下架
     * true开放工位（关闭-下架）false（去发布）
     */
    @Override
    public void itemPublishStatus(int position, HouseBean.ListBean bean, boolean isOpenSeats) {
        mPosition = position;
        if (isOpenSeats) {
            mPresenter.isPublishHouse(bean.getHouseId(), HOUSE_OFF, bean.getIsTemp());
        } else {
            mPresenter.isPublishHouse(bean.getHouseId(), HOUSE_ON, bean.getIsTemp());
        }
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
            mPresenter.houseDelete(bean.getHouseId(), bean.getIsTemp());
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

    //下架或发布成功刷新
    @Override
    public void publishOrOffHouseSuccess(int currentStatus) {
        houseList.get(mPosition).setHouseStatus(currentStatus);
        homeAdapter.notifyItemChanged(mPosition);
    }

    //左侧Popup选择
    @Override
    public void popupHouseList(BuildingJointWorkBean.ListBean bean) {
        tvHomeTitle.setText(bean.getBuildingName());
        buildingId = bean.getBuildingId();
        mData = bean;
        getRefreshHouseList();
    }

    private void getRefreshHouseList() {
        houseList.clear();
        pageNum = 1;
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
        getHouseList();
    }

    private void getHouseList() {
        mPresenter.getHouseList(buildingId, mData.getIsTemp(), pageNum, mData.getStatus());
    }

    /**
     * 身份类型 0个人1企业2联合
     * auditStatus 0待审核1审核通过2审核未通过 3过期(和2未通过一样处理)-1未认证
     */
    @Override
    public void userInfoSuccess(UserOwnerBean data) {
        mUserData = data;
        if (data.getAuditStatus() == 0 || data.getAuditStatus() == 1) {
            mPresenter.initHouseList();//初始化列表
        } else if (data.getAuditStatus() == -1) {
            SelectIdActivity_.intent(getContext()).start(); //未认证
        } else {
            new UnIdifyDialog(mActivity, data);
        }
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
        getRefreshHouseList();
    }

    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.ownerIdentityHandle};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args != null) {
            if (id == CommonNotifications.ownerIdentityHandle) {
                mPresenter.getUserInfo();
            }
        }
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rlException.setVisibility(View.GONE);
        houseList.clear();
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        rvView.setVisibility(View.VISIBLE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        rvView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            CommonDialog dialog = new CommonDialog.Builder(getContext())
                    .setMessage("账号已退出，请重新登录")
                    .setConfirmButton(com.officego.commonlib.R.string.str_login, (dialog12, which) -> {
                        GotoActivityUtils.gotoLoginActivity(getActivity());
                        dialog12.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

    // SD卡,相机 fragment
    private boolean fragmentCheckSDCardCameraPermission() {
        //mActivity1 必须使用this 在fragment
        String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission1 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[0]);
            int permission2 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[1]);
            if (permission1 != PackageManager.PERMISSION_GRANTED ||
                    permission2 != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{
                        PERMISSIONS_STORAGE[0], PERMISSIONS_STORAGE[1]}, REQ_PERMISSIONS_CAMERA_STORAGE);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.getUserInfo();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
