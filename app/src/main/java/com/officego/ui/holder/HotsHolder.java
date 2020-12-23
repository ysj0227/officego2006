package com.officego.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.view.RoundImageView;

/**
 * Created by shijie
 * Date 2020/12/23
 **/
public class HotsHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvType, tvOfficeIndependent, tvOpenSeats;
    public RoundImageView ivImageHots;
    public ImageView ivVrFlag;

    public HotsHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_house_name);
        ivImageHots = itemView.findViewById(R.id.iv_house);
        ivVrFlag = itemView.findViewById(R.id.iv_vr_flag);
        tvType = itemView.findViewById(R.id.tv_type);
        tvOfficeIndependent = itemView.findViewById(R.id.tv_office_independent);
        tvOpenSeats = itemView.findViewById(R.id.tv_open_seats);
    }
}
