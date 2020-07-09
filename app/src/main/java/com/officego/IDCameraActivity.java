package com.officego;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by YangShiJie
 * Data 2020/7/8.
 * Descriptions:
 **/
public class IDCameraActivity extends Activity {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CROP_RESULT = 0xa2;
    private ImageView imageview;
    private Button button;

    private String localAvatarPath;
    private Uri localPhotoUri;
    private File imageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_idcard);
        imageview = findViewById(R.id.iv_show);
        button = findViewById(R.id.button);

        localAvatarPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "idcard.jpg";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"身份证正面", "身份证反面", "拍照"};
                new AlertDialog.Builder(IDCameraActivity.this)
                        .setItems(items, (dialogInterface, i) -> {
                            if (i == 0) {
                                IDCardCamera.create(IDCameraActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                            } else if (i == 1) {
                                IDCardCamera.create(IDCameraActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                            } else if (i == 2) {
                                takePhoto();
                            }
                        }).create().show();
            }
        });
    }

    private void takePhoto() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        if (!FileUtils.isSDExist()) {
            return;
        }
        File fileUri = new File(localAvatarPath);
        localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        //单选
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(true)  //设置是否单选
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册

        //限数量的多选(比如最多9张)
//        ImageSelector.builder()
//                .useCamera(false) // 设置是否使用拍照
//                .setSingle(false)  //设置是否单选
//                .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
//                .setSelected(selected) // 把已选的图片传入默认选中。
//                .canPreview(true) //是否可以预览图片，默认为true
//                .start(this, REQUEST_GALLERY); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    imageview.setImageBitmap(BitmapFactory.decodeFile(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    imageview.setImageBitmap(BitmapFactory.decodeFile(path));
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                imageview.setImageBitmap(BitmapFactory.decodeFile(localAvatarPath));
            } else if (requestCode == REQUEST_GALLERY) {
                if (data != null) {
                    //获取选择器返回的数据
                    ArrayList<String> images = data.getStringArrayListExtra(
                            ImageSelector.SELECT_RESULT);
//                    boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
                }
            }
        }
    }
}
