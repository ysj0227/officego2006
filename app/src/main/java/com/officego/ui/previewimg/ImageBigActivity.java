package com.officego.ui.previewimg;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author yangShiJie
 * @date 2019-11-14
 */
@SuppressLint("Registered")
@EActivity(R.layout.activtiy_big_image)
public class ImageBigActivity extends BaseActivity implements PageAdapter.onLongClickListener {
    @ViewById(R.id.page)
    TextView page;
    @ViewById(R.id.viewPager)
    PhotoViewPager viewPager;
    PageAdapter pagerAdapter;
    @Extra
    ArrayList<String> imagesUrl;
    @Extra
    int current;
//    private String imgUrl;
    @AfterViews
    void init() {
        pagerAdapter = new PageAdapter(imagesUrl, getApplicationContext());
        pagerAdapter.setLongClickListener(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(current);
        page.setText(String.format(Locale.getDefault(), "%d/%d", current + 1, imagesUrl.size()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                imgUrl = imagesUrl.get(position);
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
    @Click(R.id.iv_back)
    void backClick() {
        finish();
    }

    @Override
    public void longItemClick(int position) {
    }
}
