package com.officego.ui.coupon;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by shijie
 * Date 2020/12/3
 **/
@EActivity(R.layout.coupon_activtiy_card_qr)
public class CardQrActivity extends BaseActivity {
    @ViewById(R.id.imageView)
    ImageView imageView;

    @AfterViews
    void init() {

    }

    @Click(R.id.button2)
    void makeUp() {
        createQR();
    }

    private void createQR() {
        runOnUiThread(() -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode("11111111111", (int) getResources().getDimension(R.dimen.dp_200));
            imageView.setImageBitmap(bitmap);
        });
    }
}
