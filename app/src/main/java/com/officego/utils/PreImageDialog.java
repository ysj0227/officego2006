package com.officego.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.officego.R;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.previewimg.PageAdapter;
import com.officego.ui.previewimg.PhotoViewPager;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by YangShiJie
 * Data 2020/6/16.
 * Descriptions:
 **/
public class PreImageDialog {
    private Context context;
    private int current;

    public PreImageDialog(Context context, ArrayList<String> list, int current) {
        this.context = context;
        this.current = current;
        init(context, list);
    }

    private void init(Context context, ArrayList<String> list) {
        showDialog(context, list);
    }

    private void showDialog(Context context, ArrayList<String> list) {
        StatusBarUtils.setStatusBarColor((Activity) context);
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_big_image, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //底部弹出dialog
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        dialogWindow.setAttributes(lp);
        //处理view
        handelView(context, list, viewLayout);
        viewLayout.findViewById(R.id.iv_back).setOnClickListener(v -> {
            StatusBarUtils.setStatusBarFullTransparent((Activity) context);
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void handelView(Context context, ArrayList<String> imagesUrl, View viewLayout) {
        TextView page = viewLayout.findViewById(R.id.page);
        PhotoViewPager viewPager = viewLayout.findViewById(R.id.viewPager);
        PageAdapter pagerAdapter = new PageAdapter(imagesUrl, context);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(current);
        page.setText(String.format(Locale.getDefault(), "%d/%d", current + 1, imagesUrl.size()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                page.setText(String.format(Locale.getDefault(), "%d/%d", current + 1, imagesUrl.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
