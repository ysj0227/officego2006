package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class IdentityStatusAdapter extends CommonListAdapter<String> {
    private Context context;
    private List<String> list;
    private int status; //0,1,2   审核中  审核通过  审核驳回

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public IdentityStatusAdapter(Context context, List<String> list, int status) {

        super(context, R.layout.item_owner_identity_step, list);
        this.context = context;
        this.list = list;
        this.status = status;
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

        tvDate.setText("10-17 12:12");
        if (holder.getAdapterPosition() == 0) {
            ivLineUp.setVisibility(View.INVISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            tvStatus.setText("提交认证");
        } else if (holder.getAdapterPosition() == 1) {
            ivLineUp.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            if (status == 0) {
                tvStatus.setText("审核中");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_checking);
            } else if (status == 1) {
                tvStatus.setText("审核");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            } else if (status == 2) {
                tvStatus.setText("审核");
                ivLineBottom.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            }
        } else if (holder.getAdapterPosition() == 2) {
            ivLineUp.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.INVISIBLE);
            if (status == 0) {
                tvStatus.setText("审核通过");
                btnIdentity.setVisibility(View.GONE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_gray);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok_gray);
            } else if (status == 1) {
                tvStatus.setText("审核通过");
                btnIdentity.setVisibility(View.GONE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_ok);
            } else if (status == 2) {
                tvStatus.setText("认证未通过");
                btnIdentity.setVisibility(View.VISIBLE);
                ivLineUp.setBackgroundResource(R.drawable.bg_dash_blue);
                ivStatus.setBackgroundResource(R.mipmap.ic_identity_no);
            }
        }
    }

}
