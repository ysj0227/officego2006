package com.officego.commonlib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description:
 * Created by bruce on 2019/7/4.
 */
public class SmRecyclerView extends RecyclerView {
    public SmRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SmRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int dividerDrawableRes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        if (dividerDrawableRes > 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), dividerDrawableRes);
            if (drawable == null) {
                return;
            }
            DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            decoration.setDrawable(drawable);
            addItemDecoration(decoration);
        }
    }

}
