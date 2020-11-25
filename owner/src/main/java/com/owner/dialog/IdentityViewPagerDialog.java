package com.owner.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.officego.commonlib.common.SpUtils;
import com.owner.R;

/**
 * Created by shijie
 * Date 2020/10/15
 * 业主认证
 **/
public class IdentityViewPagerDialog {
    private Context context;
    private SparseArray<View> mPageCache = new SparseArray<>();
    private final int mCount = 4;//page数量
    private TextView tvTitle;
    private RadioButton point1, point2, point3, point4;

    private IdentityViewPagerListener listener;

    public interface IdentityViewPagerListener {
        void toIdentity();
    }

    public IdentityViewPagerListener getListener() {
        return listener;
    }

    public void setListener(IdentityViewPagerListener listener) {
        this.listener = listener;
    }

    public IdentityViewPagerDialog(Context context) {
        this.context = context;
        dialog(context);
    }

    private void dialog(Context context) {
        SpUtils.saveToIdentity();//去认证标志
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_home_identity, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.CENTER);
        handleLayout(viewLayout, dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog) {
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        ViewPager mPager = viewLayout.findViewById(R.id.viewpager);
        tvTitle = viewLayout.findViewById(R.id.tv_title);
        point1 = viewLayout.findViewById(R.id.point1);
        point2 = viewLayout.findViewById(R.id.point2);
        point3 = viewLayout.findViewById(R.id.point3);
        point4 = viewLayout.findViewById(R.id.point4);
        Button btnIdentity = viewLayout.findViewById(R.id.btn_identity);
        //去认证
        btnIdentity.setOnClickListener(view -> {
            if (listener != null) {
                listener.toIdentity();
                dialog.dismiss();
            }
        });
        mPager.setAdapter(new ViewPagerAdapter(context));
        mPager.addOnPageChangeListener(new OnViewPageChangeListener());
        point1.setChecked(true);
        tvTitle.setText("成为房东，房源全网触达");
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
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View page = mPageCache.get(position);
            if (page == null) {
                page = mInflater.inflate(R.layout.viewpager_image_item, container, false);
                ImageView imgContent = page.findViewById(R.id.viewpager_view_page_content);
                switch (position) {
                    case 0:
                        imgContent.setImageResource(R.mipmap.ic_owner_identity1);
                        break;
                    case 1:
                        imgContent.setImageResource(R.mipmap.ic_owner_identity2);
                        break;
                    case 2:
                        imgContent.setImageResource(R.mipmap.ic_owner_identity3);
                        break;
                    case 3:
                        imgContent.setImageResource(R.mipmap.ic_owner_identity4);
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

    private class OnViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    point1.setChecked(true);
                    tvTitle.setText("成为房东，房源全网触达");
                    break;
                case 1:
                    point2.setChecked(true);
                    tvTitle.setText("720°VR全方位呈现房源");
                    break;
                case 2:
                    point3.setChecked(true);
                    tvTitle.setText("与客户线上直接沟通");
                    break;
                case 3:
                    point4.setChecked(true);
                    tvTitle.setText("交易数据保密，无中介费");
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
