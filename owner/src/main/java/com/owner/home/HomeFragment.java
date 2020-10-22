package com.owner.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
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
        implements HomeContract.View, HomeAdapter.HomeItemListener,
        BuildingJointWorkListPopupWindow.HomePopupListener {
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
    @ViewById(resName = "rv_view")
    RecyclerView rvView;
    @ViewById(resName = "tv_no_data")
    TextView tvNoData;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    @ViewById(resName = "btn_again")
    Button btnAgain;

    private UserOwnerBean mUserData;
    private List<String> list = new ArrayList<>();
    private HomeAdapter homeAdapter;

    @AfterViews
    void init() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        CommonHelper.setViewGroupLayoutParams(mActivity, rlTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        if (!fragmentCheckSDCardCameraPermission()) {
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        test();
        new VersionDialog(mActivity);
        mPresenter.getUserInfo();
    }

    private void test() {
        for (int i = 0; i < 8; i++) {
            list.add("");
        }
        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(mActivity, list);
            rvView.setAdapter(homeAdapter);
            homeAdapter.setListener(this);
        } else {
            homeAdapter.notifyDataSetChanged();
        }
    }

    //网络异常重试
    @Click(resName = "btn_again")
    void netExceptionAgainClick() {
        shortTip("再试一次");
    }

    @Click(resName = "iv_left_more")
    void leftListClick() {
        ivLeftMore.setVisibility(View.GONE);
        tvHomeTitle.setVisibility(View.GONE);
        ivAdd.setVisibility(View.GONE);
        tvExpand.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("上海实业大厦");
        }
        new BuildingJointWorkListPopupWindow(mActivity, mUserData, rlTitle, list).setListener(this);
    }

    //添加
    @Click(resName = "iv_add")
    void addClick() {
        final String[] items = {"楼盘", "楼盘_办公室", "网点", "独立办公室", "开放工位"};
        new AlertDialog.Builder(mActivity)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        AddBuildingActivity_.intent(mActivity).start();
                    } else if (i == 1) {
                        AddHouseActivity_.intent(mActivity).start();
                    } else if (i == 2) {
                        AddJointWorkActivity_.intent(mActivity).start();
                    } else if (i == 3) {
                        AddIndependentActivity_.intent(mActivity).start();
                    } else {
                        AddOpenSeatsActivity_.intent(mActivity).start();
                    }
                }).create().show();
    }

    /**
     * 身份类型 0个人1企业2联合
     * getAuditStatus 0待审核1审核通过2审核未通过 3过期(和2未通过一样处理)-1未认证
     */
    @Override
    public void userInfoSuccess(UserOwnerBean data) {
        mUserData = data;
        if (isIdentity(data)) {
            if (data.getAuditStatus() == -1) { //未认证
                SelectIdActivity_.intent(getContext()).start();
            } else {
                new UnIdifyDialog(mActivity, data);
            }
        } else {
            if (data.getIdentityType() == 2) {
                // todo 网点管理 加载list
            } else {
                // todo 楼盘管理 加载list
            }
        }
    }

    // 0待审核1审核通过2审核未通过
    private boolean isIdentity(UserOwnerBean mUserInfo) {
        if (mUserInfo != null) {
            return mUserInfo.getAuditStatus() != 0 && mUserInfo.getAuditStatus() != 1;
        }
        return false;
    }

    @Override
    public void itemPublishStatus() {

    }

    @Override
    public void itemEdit() {
        AddBuildingActivity_.intent(getContext()).start();
    }

    @Override
    public void itemMore() {
        new HomeMoreDialog(mActivity);
    }

    @Override
    public void popupDismiss() {
        ivLeftMore.setVisibility(View.VISIBLE);
        tvHomeTitle.setVisibility(View.VISIBLE);
        ivAdd.setVisibility(View.VISIBLE);
        tvExpand.setVisibility(View.GONE);
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
        list.clear();
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
