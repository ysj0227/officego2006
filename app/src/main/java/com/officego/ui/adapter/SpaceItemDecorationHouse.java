package com.officego.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.utils.CommonHelper;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class SpaceItemDecorationHouse extends RecyclerView.ItemDecoration {
    private final int column;
    private Context context;


    public SpaceItemDecorationHouse(Context context, int column) {
        this.context = context;
        this.column = column;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 第一列左边贴边、后边列项依次移动一个space和前一项移动的距离之和
        int mod = parent.getChildAdapterPosition(view) % column;
        int screenWidth = CommonHelper.getScreenWidth(context);
        int itemTextWidth = CommonHelper.dp2px(context, 1) * 100;
        int dp2pxMargin = CommonHelper.dp2px(context, 1) * 50;
        int itemWidth = screenWidth-itemTextWidth - dp2pxMargin; //item width
        int space = (itemWidth - itemTextWidth)/2;

        if (mod == 0) {
            outRect.left = 0;
            outRect.right=space;
        } else if (mod == 1) {
            outRect.left = space;
            outRect.right = 0;
        }
        outRect.bottom = 20;
    }
}