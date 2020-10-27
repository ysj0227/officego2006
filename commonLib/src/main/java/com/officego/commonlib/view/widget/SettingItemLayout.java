package com.officego.commonlib.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.officego.commonlib.R;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.ClearableEditText;

import java.util.Objects;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public class SettingItemLayout extends FrameLayout {
    public static final int DIVIDER_VISIBLE = 0;
    public static final int DIVIDER_GONE = 1;
    private RelativeLayout rlContainer;
    private TextView tvTitle;
    private ImageView ivMarkImage;
    private TextView tvContent;
    private TextView tvRightText;
    private ClearableEditText editTextContent;
    private TextView tvLeftToArrowText;
    private ImageView ivArrowRight;
    private ImageView ivOtherRight;
    private View vTopDivider;
    private View vBottomDivider;

    public SettingItemLayout(@NonNull Context context) {
        super(context);
    }

    public SettingItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setupAttrs(context, attrs);
    }

    public SettingItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        setupAttrs(context, attrs);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_setting_item, this);
        rlContainer = findViewById(R.id.cl_container);
        tvTitle = findViewById(R.id.tv_title);
        ivMarkImage = findViewById(R.id.iv_mark_image);
        tvContent = findViewById(R.id.tv_content);
        editTextContent = findViewById(R.id.et_content);
        tvLeftToArrowText = findViewById(R.id.tv_left_to_arrow_text);
        ivArrowRight = findViewById(R.id.iv_arrow_right);
        ivOtherRight = findViewById(R.id.iv_other_right);
        tvRightText = findViewById(R.id.tv_right_text);
        vTopDivider = findViewById(R.id.top_divider);
        vBottomDivider = findViewById(R.id.bottom_divider);
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItemLayout);
        float height = a.getDimension(R.styleable.SettingItemLayout_parentHeight, CommonHelper.dp2px(context, 60));
        if (rlContainer != null) {
            rlContainer.getLayoutParams().height = (int) height;
        }
        //设置标题
        String leftTitle = a.getString(R.styleable.SettingItemLayout_leftText);
        float leftTitleSize = a.getDimension(R.styleable.SettingItemLayout_leftTextSize, -1);
        if (leftTitleSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTitleSize);
        }
        if (!TextUtils.isEmpty(leftTitle)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(leftTitle);
        }

        //标记图片是否显示
        int markType = a.getInteger(R.styleable.SettingItemLayout_leftMarkShow, DIVIDER_VISIBLE);
        if (a.hasValue(R.styleable.SettingItemLayout_leftMarkShow)) {
            ivMarkImage.setVisibility(markType == DIVIDER_VISIBLE ? VISIBLE : INVISIBLE);
        } else {
            ivMarkImage.setVisibility(INVISIBLE);
        }

        //设置中间文本内容
        String centerContext = a.getString(R.styleable.SettingItemLayout_centerText);
        float centerTextSize = a.getDimension(R.styleable.SettingItemLayout_centerTextSize, -1);
        if (centerTextSize > 0) {
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
        }
        if (!TextUtils.isEmpty(centerContext)) {
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(centerContext);
        }
        //中间提示颜色和文本
        if (a.hasValue(R.styleable.SettingItemLayout_centerTextHint)) {
            tvContent.setHint(a.getString(R.styleable.SettingItemLayout_centerTextHint));
        }
        if (a.hasValue(R.styleable.SettingItemLayout_centerTextHintColor)) {
            tvContent.setHintTextColor(a.getColor(R.styleable.SettingItemLayout_centerTextHintColor, ContextCompat.getColor(context, R.color.text_66_p50)));
        }

        //输入文本EditText
        if (a.hasValue(R.styleable.SettingItemLayout_editTextWidth)) {
            float width = a.getDimension(R.styleable.SettingItemLayout_editTextWidth, CommonHelper.dp2px(context, 120));
            if (editTextContent != null) {
                editTextContent.getLayoutParams().width = (int) width;
            }
        }
        String etContent = a.getString(R.styleable.SettingItemLayout_editTextContent);
        float editTextSize = a.getDimension(R.styleable.SettingItemLayout_editTextSize, -1);
        int inputType = a.getInt(R.styleable.SettingItemLayout_editTextInputType, EditorInfo.TYPE_NULL);
        if (!TextUtils.isEmpty(etContent)) {
            editTextContent.setText(etContent);
        }
        if (a.hasValue(R.styleable.SettingItemLayout_editTextSize) && editTextSize > 0) {
            editTextContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
        }
        //输入类型
        if (a.hasValue(R.styleable.SettingItemLayout_editTextInputType) && inputType > 0) {
            editTextContent.setInputType(inputType);
        }
        //文本颜色
        if (a.hasValue(R.styleable.SettingItemLayout_editTextColor)) {
            int defaultEditTextColor = 0xFF333333;
            editTextContent.setTextColor(a.getColor(R.styleable.SettingItemLayout_editTextColor, defaultEditTextColor));
        }
        //提示颜色和文本
        if (a.hasValue(R.styleable.SettingItemLayout_editTextHint)) {
            editTextContent.setHint(a.getString(R.styleable.SettingItemLayout_editTextHint));
        }
        if (a.hasValue(R.styleable.SettingItemLayout_editTextHintColor)) {
            editTextContent.setHintTextColor(a.getColor(R.styleable.SettingItemLayout_editTextHintColor, ContextCompat.getColor(context, R.color.text_66_p50)));
        }

        //是否显示编辑框
        int editTextType = a.getInteger(R.styleable.SettingItemLayout_showEditText, DIVIDER_GONE);
        if (a.hasValue(R.styleable.SettingItemLayout_showEditText)) {
            editTextContent.setVisibility(VISIBLE);
            editTextContent.setVisibility(editTextType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            editTextContent.setVisibility(GONE);
        }
        //输入字数长度
        if (a.hasValue(R.styleable.SettingItemLayout_editMaxLength)) {
            editTextContent.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(a.getInt(R.styleable.SettingItemLayout_editMaxLength, Integer.MAX_VALUE))});
        }
        //设置输入字符显示
        if (a.hasValue(R.styleable.SettingItemLayout_editDigits)) {
            if (!TextUtils.isEmpty(a.getString(R.styleable.SettingItemLayout_editDigits))) {
                editTextContent.setKeyListener(DigitsKeyListener.getInstance(Objects.requireNonNull(a.getString(R.styleable.SettingItemLayout_editDigits))));
            }
        }
        //箭头左侧文本
        if (a.hasValue(R.styleable.SettingItemLayout_leftToArrowText)) {
            tvLeftToArrowText.setVisibility(VISIBLE);
            String rightText = a.getString(R.styleable.SettingItemLayout_leftToArrowText);
            float rightTextSize = a.getDimension(R.styleable.SettingItemLayout_leftToArrowTextSize, -1);
            if (centerTextSize > 0) {
                tvLeftToArrowText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            }
            if (!TextUtils.isEmpty(rightText)) {
                tvLeftToArrowText.setVisibility(VISIBLE);
                tvLeftToArrowText.setText(rightText);
            } else {
                tvLeftToArrowText.setVisibility(GONE);
            }
        }
        if (a.hasValue(R.styleable.SettingItemLayout_leftToArrowTextColor)) {
            int defaultTextColor = 0xFF333333;
            tvLeftToArrowText.setTextColor(a.getColor(R.styleable.SettingItemLayout_leftToArrowTextColor, defaultTextColor));
        }

        //右侧文本
        if (a.hasValue(R.styleable.SettingItemLayout_rightText)) {
            tvRightText.setVisibility(VISIBLE);
            String rightText = a.getString(R.styleable.SettingItemLayout_rightText);
            float rightTextSize = a.getDimension(R.styleable.SettingItemLayout_rightTextSize, -1);
            if (centerTextSize > 0) {
                tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            }
            if (!TextUtils.isEmpty(rightText)) {
                tvRightText.setVisibility(VISIBLE);
                tvRightText.setText(rightText);
            } else {
                tvRightText.setVisibility(GONE);
            }
            tvRightText.setTextColor(a.getColor(R.styleable.SettingItemLayout_rightTextColor, ContextCompat.getColor(context, R.color.text_66_p50)));
        }

        //右侧箭头图片是否显示
        int rightArrowType = a.getInteger(R.styleable.SettingItemLayout_rightArrowShow, DIVIDER_VISIBLE);
        ivArrowRight.setVisibility(rightArrowType == DIVIDER_VISIBLE ? VISIBLE : GONE);

        //右侧设置其他图片是否显示
        int rightOtherType = a.getInteger(R.styleable.SettingItemLayout_rightOtherImageViewShow, DIVIDER_GONE);
        if (a.hasValue(R.styleable.SettingItemLayout_rightOtherImageViewShow)) {
            ivOtherRight.setVisibility(rightOtherType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            ivOtherRight.setVisibility(GONE);
        }
        //设置图片背景
        Drawable backImage = a.getDrawable(R.styleable.SettingItemLayout_rightImageBackground);
        if (a.hasValue(R.styleable.SettingItemLayout_rightImageBackground)) {
            ivOtherRight.setVisibility(VISIBLE);
            ivOtherRight.setImageDrawable(backImage);
        }
        // 设置分割线
        int topDividerType = a.getInteger(R.styleable.SettingItemLayout_showDividerTop, DIVIDER_GONE);
        int bottomDividerType = a.getInteger(R.styleable.SettingItemLayout_showDividerBottom, DIVIDER_VISIBLE);
        if (a.hasValue(R.styleable.SettingItemLayout_showDividerTop)) {
            vTopDivider.setVisibility(topDividerType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            vTopDivider.setVisibility(GONE);
        }
        if (a.hasValue(R.styleable.SettingItemLayout_showDividerBottom)) {
            vBottomDivider.setVisibility(bottomDividerType == DIVIDER_VISIBLE ? VISIBLE : GONE);
        } else {
            vBottomDivider.setVisibility(GONE);
        }
        a.recycle();
    }


    public TextView getTitleView() {
        return tvTitle;
    }

    public ImageView getMarkImage() {
        return ivMarkImage;
    }

    public TextView getContextView() {
        return tvContent;
    }

    public TextView getLeftToArrowTextView() {
        return tvLeftToArrowText;
    }

    public EditText getEditTextView() {
        return editTextContent;
    }

    public ImageView getRightArrowImage() {
        return ivArrowRight;
    }

    public ImageView getOtherImage() {
        return ivOtherRight;
    }

    public void setTitle(int resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }

    public void setTitle(String resId) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(resId);
    }

    public void setCenterText(int resId) {
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(resId);
    }

    public void setCenterText(String resId) {
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(resId);
    }

    public void setLeftToArrowText(String resId) {
        tvLeftToArrowText.setVisibility(VISIBLE);
        tvLeftToArrowText.setText(resId);
    }

    public void setEditText(int resId) {
        editTextContent.setVisibility(VISIBLE);
        editTextContent.setText(resId);
    }

    public void setEditText(String resId) {
        editTextContent.setVisibility(VISIBLE);
        editTextContent.setText(resId);
    }
}
