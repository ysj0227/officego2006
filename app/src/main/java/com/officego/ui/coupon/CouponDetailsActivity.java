package com.officego.ui.coupon;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
@SuppressLint("Registered")
@EActivity(R.layout.coupon_activity_details)
public class CouponDetailsActivity extends BaseActivity {
    @ViewById(R.id.iv_qr)
    ImageView ivQR;
    @ViewById(R.id.iv_spread)
    ImageView ivSpread;
    @ViewById(R.id.tv_qr)
    TextView tvQR;
    @ViewById(R.id.tv_content)
    TextView tvContent;

    //是否展开
    private boolean isSpread;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        createQR();
        tvQR.setText("12344444555");
    }

    @Click(R.id.rl_spread)
    void spreadOnClick() {
        isSpread = !isSpread;
        ivSpread.setBackgroundResource(isSpread ?
                R.mipmap.ic_down_arrow_gray : R.mipmap.ic_up_arrow_gray);
        tvContent.setVisibility(isSpread ? View.GONE : View.VISIBLE);
    }

    @Click(R.id.tv_can_use_meeting_room)
    void queryMeetingRoomOnClick() {
        shortTip("H5");
    }

    private void createQR() {
        runOnUiThread(() -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode("11111111111",
                    (int) getResources().getDimension(R.dimen.dp_240));
            ivQR.setImageBitmap(bitmap);
        });
    }

}
