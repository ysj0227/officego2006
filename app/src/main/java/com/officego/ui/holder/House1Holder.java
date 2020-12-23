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
public class House1Holder extends RecyclerView.ViewHolder {
    public RoundImageView ivImage;
    public LabelsView llLabelsHouse;
    public TextView tvName;

    public House1Holder(View itemView) {
        super(itemView);
        ivImage = itemView.findViewById(R.id.riv_house);
        llLabelsHouse = itemView.findViewById(R.id.ll_labels_house);
        tvName = itemView.findViewById(R.id.tv_name);
    }
}
