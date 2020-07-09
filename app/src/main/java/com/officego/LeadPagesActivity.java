package com.officego;

/**
 * Created by YangShiJie
 * Data 2020/7/7.
 * Descriptions:
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.ui.IdSelectActivity_;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@SuppressLint("Registered")
@EActivity(R.layout.viewpager_view)
public class LeadPagesActivity extends BaseActivity {
    @ViewById(R.id.viewpager)
    ViewPager mPager;
    @ViewById(R.id.point1)
    RadioButton point1;
    @ViewById(R.id.point2)
    RadioButton point2;
    @ViewById(R.id.point3)
    RadioButton point3;
    @ViewById(R.id.point4)
    RadioButton point4;

    private SparseArray<View> mPageCache = new SparseArray<>();
    private final int mCount = 4;//page数量

    @AfterViews
    protected void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPager.setAdapter(new ViewPagerAdapter(this));
        point1.setChecked(true);
        mPager.addOnPageChangeListener(new OnViewPageChangeListener());
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater mInflater;

        private ViewPagerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = mPageCache.get(position);
            if (page == null) {
                page = mInflater.inflate(R.layout.viewpager_view_item, container, false);
                ImageView imgContent = page.findViewById(R.id.viewpager_view_page_content);
                Button btnText = page.findViewById(R.id.btn_text);
                switch (position) {
                    case 0:
                        imgContent.setImageResource(R.mipmap.ic_lead_page1);
                        btnText.setVisibility(View.INVISIBLE);

                        Intent a = new Intent(LeadPagesActivity.this, IDCameraActivity.class);
                        startActivity(a);
                        break;
                    case 1:
                        imgContent.setImageResource(R.mipmap.ic_lead_page2);
                        btnText.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        imgContent.setImageResource(R.mipmap.ic_lead_page3);
                        btnText.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        btnText.setVisibility(View.VISIBLE);
                        imgContent.setImageResource(R.mipmap.ic_lead_page4);
                        btnText.setOnClickListener(v -> gotoActivity());
                        break;
                    default:
                        break;
                }
                mPageCache.append(position, page);
            }
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void gotoActivity() {
        if (isFastClick(1000)) return;
        SpUtils.saveLead();//保存是否开启了引导页
        if (!TextUtils.isEmpty(SpUtils.getRole())) {
            if (TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole())) {
                MainActivity_.intent(context).start();
            } else {
                LoginActivity_.intent(context).start();
            }
        } else {
            IdSelectActivity_.intent(context).start();
        }
        finish();
    }


    private class OnViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            LogCat.e("TAG", "position: " + position);
            switch (position) {
                case 0:
                    point1.setChecked(true);
                    break;
                case 1:
                    point2.setChecked(true);
                    break;
                case 2:
                    point3.setChecked(true);
                    break;
                case 3:
                    point4.setChecked(true);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE://空闲状态
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING://滑动状态
                    break;
                case ViewPager.SCROLL_STATE_SETTLING://滑动后自然沉降的状态
                    break;
            }
        }
    }

}