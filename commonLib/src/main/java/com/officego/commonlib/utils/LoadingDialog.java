package com.officego.commonlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.utils.log.LogCat;


/**
 * @Description 加载中等待框（纯动画，show即可）
 */
public class LoadingDialog extends Dialog {

    private TextView tvLoading;
    private LinearLayout loadingBackground;
    private int layoutId;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutId > 0) {
            setContentView(layoutId);
        } else {
            setContentView(R.layout.dialog_loading);
            tvLoading = this.findViewById(R.id.tv_loading);
            loadingBackground = this.findViewById(R.id.ll_loading_background);
        }
    }

    public void setLoadingContent(String content) {
        if (tvLoading == null) {
            this.show();
            this.dismiss();
            return;
        }
        if (!TextUtils.isEmpty(content)) {
            tvLoading.setVisibility(View.VISIBLE);
            tvLoading.setText(content);
        } else {
            if (loadingBackground != null){
                loadingBackground.setBackgroundResource(R.color.transparent);
            }
            tvLoading.setVisibility(View.GONE);
        }
    }

    public void setTipColorText(String content, int colorRes) {
        if (tvLoading == null) {
            this.show();
            this.dismiss();
            return;
        }
        if (!TextUtils.isEmpty(content)) {
            tvLoading.setVisibility(View.VISIBLE);
            tvLoading.setTextColor(colorRes);
            tvLoading.setText(content);
        } else {
            tvLoading.setVisibility(View.GONE);
            if (loadingBackground != null){
                loadingBackground.setBackgroundResource(R.color.transparent);
            }
        }
    }

    public void setTipBackground(String content, int colorRes, int bgColor) {
        if (tvLoading == null) {
            this.show();
            this.dismiss();
            return;
        }
        if (!TextUtils.isEmpty(content)) {
            tvLoading.setVisibility(View.VISIBLE);
            tvLoading.setTextColor(colorRes);
            tvLoading.setText(content);
            if (loadingBackground != null){
                loadingBackground.setBackgroundResource(bgColor);
            }
        } else {
            tvLoading.setVisibility(View.GONE);
            if (loadingBackground != null){
                loadingBackground.setBackgroundResource(R.color.transparent);
            }
        }
    }

    @Override
    public void dismiss() {
        try {
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
