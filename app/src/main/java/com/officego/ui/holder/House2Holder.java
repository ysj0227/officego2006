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
public class House2Holder extends RecyclerView.ViewHolder {
    public RoundImageView ivImage, rivHouseRightUp, rivHouseRightDown;
    public LabelsView llLabelsHouse;
    public TextView tvName,tvTips;

    public House2Holder(View itemView) {
        super(itemView);
        ivImage = itemView.findViewById(R.id.riv_house_left);
        rivHouseRightUp = itemView.findViewById(R.id.riv_house_right_up);
        rivHouseRightDown = itemView.findViewById(R.id.riv_house_right_down);
        llLabelsHouse = itemView.findViewById(R.id.ll_labels_house);
        tvName = itemView.findViewById(R.id.tv_name);
        tvTips = itemView.findViewById(R.id.tv_tips);
    }
}
