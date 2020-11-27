package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.DateTimeUtils;
import com.owner.R;
import com.owner.identity.OwnerIdentityActivity_;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class IdentityStatusAdapter extends CommonListAdapter<String> {
    private Context context;
    private List<String> list;
    private BuildingJointWorkBean.ListBean mData;
    private final int STATUS_ING = 6; //审核中
    private final int STATUS_OK = 1;//审核通过
    private final int STATUS_OK_COMPLETE = 2;//资料待完善
    private final int STATUS_NO = 7;//审核驳回

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public IdentityStatusAdapter(Context context, List<String> list, BuildingJointWorkBean.ListBean mData) {

        super(context, R.layout.item_owner_identity_step, list);
        this.context = context;
        this.list = list;
        this.mData = mData;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, String bean) {
        ImageView ivLineUp = holder.getView(R.id.iv_line_up);
        ImageView ivLineBottom = holder.getView(R.id.iv_line_bottom);
        ImageView ivStatus = holder.getView(R.id.iv_status);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvDate = holder.getView(R.id.tv_date);
        Button btnIdentity = holder.getView(R.id.btn_identity);
        if (holder.getAdapterPosition() == 0) {
            ivLineUp.setVisibility(View.INVISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            tvStatus.setText("提交认证");
            tvDate.setText(DateTimeUtils.stampMinuteToDate(mData.getStartTime(), "yyyy-MM-dd HH:mm"));
        } else if (holder.getAdapterPosition() == 1) {
            ivLineUp.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.GONE);
            if (mData.getStatus() == STATUS_ING) {
                tvStatus.setText("审核中");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_checking);
            } else if (mData.getStatus() == STATUS_OK) {
                tvStatus.setText("审核");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            } else if (mData.getStatus() == STATUS_NO) {
                tvStatus.setText("审核");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            } else {
                tvStatus.setText("审核");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            }
        } else if (holder.getAdapterPosition() == 2) {
            ivLineUp.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.INVISIBLE);
            if (mData.getStatus() == STATUS_ING) {
                tvDate.setVisibility(View.GONE);
                tvStatus.setText("审核通过");
                btnIdentity.setVisibility(View.GONE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok_gray);
            } else if (mData.getStatus() == STATUS_OK) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(DateTimeUtils.stampMinuteToDate(mData.getEndTime(), "yyyy-MM-dd HH:mm"));
                tvStatus.setText("审核通过");
                btnIdentity.setVisibility(View.GONE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            } else if (mData.getStatus() == STATUS_NO) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(DateTimeUtils.stampMinuteToDate(mData.getEndTime(), "yyyy-MM-dd HH:mm"));
                tvStatus.setText("认证未通过");
                btnIdentity.setVisibility(View.VISIBLE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_no);
            } else {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(DateTimeUtils.stampMinuteToDate(mData.getEndTime(), "yyyy-MM-dd HH:mm"));
                tvStatus.setText("审核通过");
                btnIdentity.setVisibility(View.GONE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            }
        }
        //驳回重新认证
        btnIdentity.setOnClickListener(view -> {
            OwnerIdentityActivity_.intent(mContext)
                    .flag(Constants.IDENTITY_REJECT)
                    .buildingId(mData.getBuildingId()).start();
        });
    }
}
