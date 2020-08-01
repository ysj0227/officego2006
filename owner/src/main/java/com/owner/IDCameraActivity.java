package com.owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                selectedDialog();
            }
        });
    }

    private void selectedDialog() {
        final String[] items = {"身份证正面", "身份证反面", "拍照", "相册"};
        new AlertDialog.Builder(IDCameraActivity.this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        IDCardCamera.create(IDCameraActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                    } else if (i == 1) {
                        IDCardCamera.create(IDCameraActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                    } else if (i == 2) {
                        takePhoto();
                    } else if (i == 3) {
                        openGallery();
                    }
                }).create().show();
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
//        ImageSelector.builder()
//                .useCamera(false) // 设置是否使用拍照
//                .setSingle(true)  //设置是否单选
//                .canPreview(true) //是否可以预览图片，默认为true
//                .start(this, REQUEST_GALLERY); // 打开相册
        //限数量的多选(比如最多9张)
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(false)  //设置是否单选
                .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
//                .setCropRatio(0.5f)
//                .setSelected(selected) // 把已选的图片传入默认选中。
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册
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
            if (requestCode == REQUEST_CAMERA) {//拍照
                saveCropImageView(localAvatarPath);
                imageview.setImageBitmap(BitmapFactory.decodeFile(localAvatarPath));

            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                for (int i = 0; i < images.size(); i++) {
                    LogCat.e("TAG", "11111111111 images=" + images.get(i));
                    saveCropImageView(images.get(0));
                    imageview.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (FileUtils.isSDExist()) {
                        takePhoto();
                    } else {
                        ToastUtils.toastForShort(this, getString(R.string.str_no_sd));
                    }
                } else {
                    ToastUtils.toastForShort(this, getString(R.string.str_please_open_camera));
                }
                break;
            case PermissionUtils.REQ_PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ToastUtils.toastForShort(this, getString(R.string.str_please_open_sd));
                }
                break;
            default:
        }
    }

    private void saveCropImageView(String path) {
        Bitmap bitMap = BitmapFactory.decodeFile(path);
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int setNewMax = 1500;
        float scaleScale;
        if (width >= height && width > setNewMax) {
            scaleScale = ((float) setNewMax) / width;
        } else if (width < height && height > setNewMax) {
            scaleScale = ((float) setNewMax) / height;
        } else {
            return;
        }
        LogCat.d("TAG", "1111 width=" + width + "  height=" + height);
        LogCat.d("TAG", "1111 scaleWidth=" + scaleScale + "  scaleHeight=" + scaleScale);
        //  取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleScale, scaleScale);
        // 得到新的图片
        bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        imageview.setImageBitmap(bitMap);
        //将新文件回写到本地
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitMap != null) {
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        }
    }

//        private void saveCropImageView(String path) {
////        BitmapFactory.Options op = new BitmapFactory.Options();
//            Bitmap bitMap = BitmapFactory.decodeFile(path);
//            int width = bitMap.getWidth();
//            int height = bitMap.getHeight();
//            LogCat.d("TAG", "1111 width=" + width + "  height=" + height);
//            // 设置想要的大小
//            int newWidth = 1500;
//            int newHeight = 600;
//            // 计算缩放比例
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            LogCat.d("TAG", "1111 scaleWidth=" + scaleWidth + "  scaleHeight=" + scaleHeight);
////         取得想要缩放的matrix参数
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            // 得到新的图片
//            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
//            imageview.setImageBitmap(bitMap);
//        //将新文件回写到本地
//        FileOutputStream b = null;
//        try {
//            b = new FileOutputStream(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (bitMap != null) {
//            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, b);
//        }
}
