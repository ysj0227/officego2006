package com.owner.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QRScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final int REQUEST_CODE = 1000;
    ZXingView mZXingView;
    TitleBarView titleBar;
    private boolean isOpenFlashlight;
    private LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scan_activity);
        titleBar = findViewById(R.id.title_bar);
        mZXingView = findViewById(R.id.zxing_view);
        mZXingView.setDelegate(this);
        initLightParams();
        titleBar.getRightImg().setOnClickListener(view -> openLight());
    }

    private void initLightParams() {
        layoutParams = (LinearLayout.LayoutParams) titleBar.getRightImg().getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dp_25);
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.dp_25);
        titleBar.getRightImg().setLayoutParams(layoutParams);
        titleBar.getRightImg().setImageResource(R.mipmap.ic_light_close);
    }

    /**
     * 闪光灯
     */
    private void openLight() {
        isOpenFlashlight = !isOpenFlashlight;
        if (mZXingView != null) {
            if (isOpenFlashlight) {
                mZXingView.openFlashlight();
            } else {
                mZXingView.closeFlashlight();
            }
            titleBar.getRightImg().setImageResource(isOpenFlashlight
                    ? R.mipmap.ic_light_open : R.mipmap.ic_light_close);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onResume() {
        super.onResume();
        mZXingView.startSpot(); // 开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        if (!result.contains("officego")) {
            scanQRError();
        } else {
            ScanCompleteActivity_.intent(context)
                    .scanContent(result)
                    .startForResult(REQUEST_CODE);
        }
    }

    private void scanQRError() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("二维码不符合规则，请重新扫描")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    if (mZXingView != null) {
                        mZXingView.startSpot(); // 重新开始识别
                    }
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
