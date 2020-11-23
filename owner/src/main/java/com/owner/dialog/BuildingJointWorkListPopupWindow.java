package com.owner.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.owner.R;
import com.owner.home.EditBuildingActivity_;
import com.owner.home.EditJointWorkActivity_;
import com.owner.identity2.OwnerIdentityActivity_;
import com.owner.mine.model.UserOwnerBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shijie
 * Date 2020/10/21
 **/
public class BuildingJointWorkListPopupWindow extends PopupWindow implements
        View.OnTouchListener,
        PopupWindow.OnDismissListener {
    private Activity mContext;
    private List<BuildingJointWorkBean.ListBean> list;
    private UserOwnerBean mUserData;
    private int statusBarHeight, titleBarHeight;
    private final int identityType = 2;//0个人1企业2联合，网点
    //筛选楼盘网点列表
    private List<BuildingJointWorkBean.ListBean> buildingList;
    private List<BuildingJointWorkBean.ListBean> jointWorkList;

    public HomePopupListener getListener() {
        return listener;
    }

    public void setListener(HomePopupListener listener) {
        this.listener = listener;
    }

    private HomePopupListener listener;

    public interface HomePopupListener {
        void popupDismiss();

        void popupHouseList(BuildingJointWorkBean.ListBean bean);
    }

    @SuppressLint("ClickableViewAccessibility")
    public BuildingJointWorkListPopupWindow(Activity activity, UserOwnerBean mUserData, View topToPopupWindowView,
                                            List<BuildingJointWorkBean.ListBean> list) {
        super();
        this.mContext = activity;
        this.mUserData = mUserData;
        this.list = list;
        //楼盘，网点
        buildingList = new ArrayList<>();
        jointWorkList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBtype() == Constants.TYPE_BUILDING) {
                buildingList.add(list.get(i));
            } else {
                jointWorkList.add(list.get(i));
            }
        }
        //view
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View viewLayout = inflater.inflate(R.layout.pop_building_jointwork_list, null);
        setContentView(viewLayout);
        //宽高
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //计算区域高度
        statusBarHeight = CommonHelper.statusHeight(mContext);
        titleBarHeight = (int) mContext.getResources().getDimension(R.dimen.dp_58);
        //显示位置
        showAsDropDown(topToPopupWindowView);
        init(viewLayout);
        handleView(viewLayout);
    }

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE || motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int topY = titleBarHeight + statusBarHeight;//状态栏到title的高度
            float heightY = mContext.getResources().getDimension(R.dimen.dp_472) + topY + mContext.getResources().getDimension(R.dimen.dp_8);
            if (this.isShowing() && motionEvent.getRawY() < topY ||
                    motionEvent.getRawY() > heightY) {
                dismiss();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDismiss() {
        if (listener != null) {
            listener.popupDismiss();
        }
    }

    private void handleView(View viewLayout) {
        TextView tvBuilding = viewLayout.findViewById(R.id.tv_title);
        RecyclerView recyclerViewList = viewLayout.findViewById(R.id.rv_list);
        View vLine = viewLayout.findViewById(R.id.v_split_line);
        TextView tvJointWork = viewLayout.findViewById(R.id.tv_title_joint_work);
        RecyclerView rvListJointWork = viewLayout.findViewById(R.id.rv_list_joint_work);
        if (buildingList.size() == 0) {
            tvBuilding.setVisibility(View.GONE);
            recyclerViewList.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        } else if (jointWorkList.size() == 0) {
            tvJointWork.setVisibility(View.GONE);
            rvListJointWork.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        } else {
            vLine.setVisibility(View.VISIBLE);
        }
        recyclerViewList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewList.setAdapter(new BuildingAdapter(mContext, buildingList));
        rvListJointWork.setLayoutManager(new LinearLayoutManager(mContext));
        rvListJointWork.setAdapter(new BuildingAdapter(mContext, jointWorkList));
        //添加楼盘/网点
        viewLayout.findViewById(R.id.tv_add).setOnClickListener(view ->
                OwnerIdentityActivity_.intent(mContext).flag(Constants.IDENTITY_NO_FIRST).start());
    }

    private void gotoEditActivity(BuildingManagerBean managerBean) {
        if (mUserData.getIdentityType() == identityType) {
            EditJointWorkActivity_.intent(mContext).buildingManagerBean(managerBean).start();
        } else {
            EditBuildingActivity_.intent(mContext).buildingManagerBean(managerBean).start();
        }
    }

    //房源特色
    private class BuildingAdapter extends CommonListAdapter<BuildingJointWorkBean.ListBean> {
        private Context context;

        @SuppressLint("UseSparseArrays")
        BuildingAdapter(Context context, List<BuildingJointWorkBean.ListBean> list) {
            super(context, R.layout.item_popup_building_jointwork, list);
            this.context = context;
        }

        /**
         * "isEdit": 0,//是否可以编辑 为0时不可以编辑楼盘，为1时编辑楼盘
         * "isTemp": 0,//0是正式的楼盘（认证通过），1是临时的楼盘（审核中的）
         * "status": 2//-1:不是管理员 暂无权限编辑楼盘(临时楼盘),0: 下架(未发布),1: 上架(已发布) ;2:资料待完善 ,
         * 3: 置顶推荐;4:已售完;5:删除;6待审核7已驳回 注意：（IsTemp为1时，status状态标记 1:待审核 -转6 ,2:已驳回 -转7 ）
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void convert(ViewHolder holder, final BuildingJointWorkBean.ListBean bean) {
            ImageView ivStatus = holder.getView(R.id.iv_status);
            ImageView ivEdit = holder.getView(R.id.iv_edit);
            RelativeLayout rlPreview = holder.getView(R.id.rl_preview);
            RelativeLayout rlEdit = holder.getView(R.id.rl_edit);
            ImageView ivPoint = holder.getView(R.id.iv_point);
            TextView tvTitle = holder.getView(R.id.tv_title);
            ivEdit.setBackgroundResource(1 == bean.getIsEdit() ? R.mipmap.ic_edit_blue : R.mipmap.ic_edit_gray);
            String name = bean.getBuildingName();
            if (!TextUtils.isEmpty(name) && name.length() > 13) {
                tvTitle.setText(name.substring(0, 13) + "...");
            } else {
                tvTitle.setText(name);
            }
            ivPoint.setVisibility(View.GONE);
            if (2 == bean.getStatus()) {
                rlPreview.setVisibility(View.VISIBLE);
                rlEdit.setVisibility(View.VISIBLE);
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setBackgroundResource(R.mipmap.ic_complete_more_mes);
            } else if (7 == bean.getStatus()) {
                rlPreview.setVisibility(View.GONE);
                rlEdit.setVisibility(View.GONE);
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setBackgroundResource(R.mipmap.ic_check_no);
            } else if (6 == bean.getStatus()) {
                rlPreview.setVisibility(View.VISIBLE);
                rlEdit.setVisibility(View.VISIBLE);
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setBackgroundResource(R.mipmap.ic_checking);
            } else {
                rlPreview.setVisibility(View.VISIBLE);
                rlEdit.setVisibility(View.VISIBLE);
                ivStatus.setVisibility(View.GONE);
            }
            //预览
            rlPreview.setOnClickListener(view -> {
                if (mUserData != null) {
                    BundleUtils.ownerGotoDetailsActivity(mContext, bean.getStatus() != 1, true,
                            bean.getBtype(), bean.getBuildingId(), bean.getIsTemp());
                }
            });
            //编辑
            rlEdit.setOnClickListener(view -> {
                if (1 == bean.getIsEdit()) {
                    gotoEditActivity(new BuildingManagerBean(bean.getBuildingId(), bean.getIsTemp()));
                }
            });
            //房源列表
            holder.itemView.setOnClickListener(view -> {
                dismiss();
                listener.popupHouseList(bean);
            });
        }
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
}
