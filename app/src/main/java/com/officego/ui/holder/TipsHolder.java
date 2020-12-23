package com.officego.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.RoundImageView;

/**
 * Created by shijie
 * Date 2020/12/23
 **/
public class TipsHolder extends RecyclerView.ViewHolder {
    public TextView tvTips;
    public RoundImageView ivImageTips;
    public LabelsView llLabelsTips;

    public TipsHolder(View itemView) {
        super(itemView);
        ivImageTips = itemView.findViewById(R.id.riv_tips);
        tvTips = itemView.findViewById(R.id.tv_tips);
        llLabelsTips = itemView.findViewById(R.id.ll_labels_tips);
    }
}
