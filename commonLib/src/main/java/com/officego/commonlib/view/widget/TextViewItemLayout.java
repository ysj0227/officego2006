package com.officego.commonlib.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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
public class TextViewItemLayout extends FrameLayout {
    public static final int DIVIDER_VISIBLE = 0;
    public static final int DIVIDER_GONE = 1;
    private RelativeLayout rlContainer;
    private TextView tvTitle;
    private TextView tvContent;
    private View vTopDivider;
    private View vBottomDivider;

    public TextViewItemLayout(@NonNull Context context) {
        super(context);
    }

    public TextViewItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setupAttrs(context, attrs);
    }

    public TextViewItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        setupAttrs(context, attrs);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_text_item, this);
        rlContainer = findViewById(R.id.cl_container);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        vTopDivider = findViewById(R.id.top_divider);
        vBottomDivider = findViewById(R.id.bottom_divider);
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewItemLayout);
        float height = a.getDimension(R.styleable.TextViewItemLayout_parentHeight, CommonHelper.dp2px(context, 56));
        if (rlContainer != null) {
            rlContainer.getLayoutParams().height = (int) height;
        }
        //设置标题
        String leftTitle = a.getString(R.styleable.TextViewItemLayout_title);
        float leftTitleSize = a.getDimension(R.styleable.TextViewItemLayout_titleSize, -1);
        if (leftTitleSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTitleSize);
        }
        if (!TextUtils.isEmpty(leftTitle)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(leftTitle);
        }
        if (a.hasValue(R.styleable.TextViewItemLayout_titleColor)) {
            tvTitle.setTextColor(a.getColor(R.styleable.TextViewItemLayout_titleColor,
                    ContextCompat.getColor(context, R.color.text_66_p50)));
        }

        //设置文本内容
        String centerContext = a.getString(R.styleable.TextViewItemLayout_contentText);
        float centerTextSize = a.getDimension(R.styleable.TextViewItemLayout_contentTextSize, -1);
        if (centerTextSize > 0) {
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
        }
        if (!TextUtils.isEmpty(centerContext)) {
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(centerContext);
        }
        if (a.hasValue(R.styleable.TextViewItemLayout_contentTextColor)) {
            tvContent.setTextColor(a.getColor(R.styleable.TextViewItemLayout_contentTextColor,
                    ContextCompat.getColor(context, R.color.text_33)));
        }

        // 设置分割线
        int topDividerType = a.getInteger(R.styleable.TextViewItemLayout_dividerTop, DIVIDER_GONE);
        int bottomDividerType = a.getInteger(R.styleable.TextViewItemLayout_dividerBottom, DIVIDER_VISIBLE);
        if (a.hasValue(R.styleable.TextViewItemLayout_dividerTop)) {
            vTopDivider.setVisibility(topDividerType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            vTopDivider.setVisibility(GONE);
        }
        if (a.hasValue(R.styleable.TextViewItemLayout_dividerBottom)) {
            vBottomDivider.setVisibility(bottomDividerType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            vBottomDivider.setVisibility(GONE);
        }
        a.recycle();
    }


    public TextView getTitleView() {
        return tvTitle;
    }

    public TextView getContextView() {
        return tvContent;
    }

    public void setTitle(int resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }

    public void setTitle(String resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }

    public void setContext(int resId) {
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(resId);
    }

    public void setContext(String resId) {
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(resId);
    }
}
