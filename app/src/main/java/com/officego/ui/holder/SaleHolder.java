package com.officego.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.view.RoundImageView;

/**
 * Created by shijie
 * Date 2020/12/23
 **/
public class SaleHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public RoundImageView ivImageHots;

    public SaleHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_house_name);
        ivImageHots = itemView.findViewById(R.id.iv_house);
    }
}
