package com.officego.ui.coupon;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.widget.AutoFitTextView;
import com.officego.h5.WebViewCouponActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
@SuppressLint("Registered")
@EActivity(R.layout.coupon_activity_details)
public class CouponDetailsActivity extends BaseActivity {
    @ViewById(R.id.tv_rmb_unit)
    TextView tvRmbUnit;
    @ViewById(R.id.tv_rmb)
    AutoFitTextView tvRmb;
    @ViewById(R.id.tv_use_range)
    AutoFitTextView tvUseRange;
    @ViewById(R.id.tv_active_name)
    TextView tvActiveName;
    @ViewById(R.id.tv_use_way)
    TextView tvUseWay;
    @ViewById(R.id.tv_use_date)
    TextView tvUseDate;
    @ViewById(R.id.iv_qr)
    ImageView ivQR;
    @ViewById(R.id.iv_spread)
    ImageView ivSpread;
    @ViewById(R.id.tv_qr)
    TextView tvQR;
    @ViewById(R.id.tv_content)
    TextView tvContent;
    @ViewById(R.id.tv_spread)
    TextView tvSpread;
    @Extra
    CouponListBean.ListBean couponBean;

    //是否展开
    private boolean isSpread;
    private String code;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        if (couponBean != null) {
            code = "officegoTC_" + couponBean.getBatchCode();
            createQR();
            tvUseRange.setText(couponBean.getAmountRangeText());
            tvActiveName.setText(couponBean.getBatchTitle());
            tvUseWay.setText("仅限到店核销使用");
            tvUseDate.setText(couponBean.getShelfLife());
            tvQR.setText(couponBean.getBatchCode());
            if (couponBean.getCouponType() == 1) {
                tvRmbUnit.setVisibility(View.GONE);
                tvRmbUnit.setTextSize(20f);
                tvRmbUnit.setText("");
                if (!TextUtils.isEmpty(couponBean.getDiscount())) {
                    tvRmb.setText(CommonHelper.digits(Integer.parseInt(couponBean.getDiscount()), 10) + "折");
                }
            } else if (couponBean.getCouponType() == 2) {
                tvRmbUnit.setVisibility(View.VISIBLE);
                tvRmbUnit.setTextSize(20f);
                tvRmbUnit.setText("¥");
                tvRmb.setText(couponBean.getDiscountMax());
            } else {
                tvRmbUnit.setVisibility(View.VISIBLE);
                tvRmbUnit.setTextSize(10f);
                tvRmbUnit.setText("减至");
                tvRmb.setText(couponBean.getDiscountMax());
            }
            tvContent.setText(couponBean.getRemark());
        }
    }

    @Click(R.id.rl_spread)
    void spreadOnClick() {
        isSpread = !isSpread;
        ivSpread.setBackgroundResource(isSpread ?
                R.mipmap.ic_down_arrow_gray : R.mipmap.ic_up_arrow_gray);
        tvContent.setVisibility(isSpread ? View.GONE : View.VISIBLE);
        tvSpread.setText(isSpread ? "收起使用说明" : "查看使用说明");
    }

    @Click(R.id.tv_can_use_meeting_room)
    void queryMeetingRoomOnClick() {
        WebViewCouponActivity_.intent(context).amountRange(couponBean.getAmountRange()).start();
    }

    private void createQR() {
        runOnUiThread(() -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(code, (int) getResources().getDimension(R.dimen.dp_240));
            ivQR.setImageBitmap(bitmap);
        });
    }
}
