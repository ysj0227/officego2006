package com.officego.ui.holder;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.RoundImageView;

/**
 * Created by shijie
 * Date 2020/12/23
 **/
public class DiscountHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvType, tvLocation, tvLines, tvRmbMoney, tvUnit, tvDiscount;
    public LabelsView llLabelsDiscount;
    public RoundImageView ivHouse;
    public ImageView ivVrFlag;

    public DiscountHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_house_name);
        llLabelsDiscount = itemView.findViewById(R.id.ll_labels_discount);
        ivHouse = itemView.findViewById(R.id.iv_house);
        ivVrFlag = itemView.findViewById(R.id.iv_vr_flag);
        tvType = itemView.findViewById(R.id.tv_type);
        tvLocation = itemView.findViewById(R.id.tv_location);
        tvLines = itemView.findViewById(R.id.tv_lines);
        tvRmbMoney = itemView.findViewById(R.id.tv_rmb_money);
        tvUnit = itemView.findViewById(R.id.tv_unit);
        tvDiscount = itemView.findViewById(R.id.tv_discount);
        // 设置中划线并加清晰
        tvDiscount.getPaint().setAntiAlias(true);//抗锯齿
        tvDiscount.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

    }
}
