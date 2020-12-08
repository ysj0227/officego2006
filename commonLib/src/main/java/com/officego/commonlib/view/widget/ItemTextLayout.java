package com.officego.commonlib.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.officego.commonlib.R;
import com.officego.commonlib.utils.CommonHelper;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public class ItemTextLayout extends FrameLayout {
    public static final int DIVIDER_VISIBLE = 0;
    public static final int DIVIDER_GONE = 1;
    private RelativeLayout rlContainer;
    private TextView tvTitle;
    private ImageView ivImage;
    private View vBottomDivider;

    public ItemTextLayout(@NonNull Context context) {
        super(context);
    }

    public ItemTextLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setupAttrs(context, attrs);
    }

    public ItemTextLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        setupAttrs(context, attrs);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_common_item_textview, this);
        rlContainer = findViewById(R.id.cl_container);
        tvTitle = findViewById(R.id.tv_title);
        ivImage = findViewById(R.id.iv_image);
        vBottomDivider = findViewById(R.id.bottom_divider);
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemTextLayout);
        float height = a.getDimension(R.styleable.ItemTextLayout_parentHeight, CommonHelper.dp2px(context, 56));
        if (rlContainer != null) {
            rlContainer.getLayoutParams().height = (int) height;
        }
        //设置标题
        String leftTitle = a.getString(R.styleable.ItemTextLayout_textTitle);
        float leftTitleSize = a.getDimension(R.styleable.ItemTextLayout_textTitleSize, -1);
        if (leftTitleSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTitleSize);
        }
        if (!TextUtils.isEmpty(leftTitle)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(leftTitle);
        }
        if (a.hasValue(R.styleable.ItemTextLayout_textTitleColor)) {
            tvTitle.setTextColor(a.getColor(R.styleable.ItemTextLayout_textTitleColor,
                    ContextCompat.getColor(context, R.color.text_33)));
        }

        //图片宽高
        float imageWidth = a.getDimension(R.styleable.ItemTextLayout_imageWidth, CommonHelper.dp2px(context, 16));
        float imageHeight = a.getDimension(R.styleable.ItemTextLayout_imageHeight, CommonHelper.dp2px(context, 16));
        if (ivImage != null) {
            ivImage.getLayoutParams().width = (int) imageWidth;
            ivImage.getLayoutParams().height = (int) imageHeight;
        }
        //设置图片背景
        Drawable backImage = a.getDrawable(R.styleable.ItemTextLayout_imageBackground);
        if (a.hasValue(R.styleable.ItemTextLayout_imageBackground)) {
            ivImage.setImageDrawable(backImage);
        }

        // 设置分割线
        int bottomDividerType = a.getInteger(R.styleable.ItemTextLayout_dividerLine, DIVIDER_VISIBLE);
        if (a.hasValue(R.styleable.ItemTextLayout_dividerLine)) {
            vBottomDivider.setVisibility(bottomDividerType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            vBottomDivider.setVisibility(GONE);
        }
        a.recycle();
    }

    public void setTitle(int resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }

    public void setTitle(String resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }
}
