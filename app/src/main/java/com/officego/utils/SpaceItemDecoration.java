package com.officego.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int column;
    private Context context;


    public SpaceItemDecoration(Context context, int column) {
        this.context = context;
        this.column = column;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 第一列左边贴边、后边列项依次移动一个space和前一项移动的距离之和
        int mod = parent.getChildAdapterPosition(view) % column;
        int recyclerWidth = CommonHelper.getScreenWidth(context);
        int itemTextWidth = CommonHelper.dp2px(context, 1) * 98;
        int dp2pxMargin = CommonHelper.dp2px(context, 1) * 36;
//        int itemW = CommonHelper.dp2px(context, (float) context.getResources().getDimensionPixelOffset(R.dimen.dp_98));
        int itemWidth = (recyclerWidth - dp2pxMargin) / 3; //item width
        int space = itemWidth - itemTextWidth;
        int centerMargin = (itemWidth - itemTextWidth) / 2;
        LogCat.e("TAG", space + ";;" + centerMargin);
        if (mod == 0) {
            outRect.left = 0;
            outRect.right = space;
        } else if (mod == 1) {
            outRect.left = centerMargin;
            outRect.right = centerMargin;
        } else if (mod == 2) {
            outRect.left = space;
            outRect.right = 0;
        }
        outRect.bottom = 20;
    }
}