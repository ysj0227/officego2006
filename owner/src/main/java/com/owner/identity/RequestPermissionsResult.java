package com.owner.identity;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.owner.R;

/**
 * 认证获取权限
 */
public class RequestPermissionsResult {
    private Context context;
    private PermissionsListener listener;

    public PermissionsListener getListener() {
        return listener;
    }

    public void setListener(PermissionsListener listener) {
        this.listener = listener;
    }

    public RequestPermissionsResult(Context context, int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        this.context = context;
        requestPermissions(requestCode, permissions, grantResults);
    }

    public interface PermissionsListener {
        void gotoTakePhoto();

        void gotoOpenGallery();
    }

    private void requestPermissions(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (FileUtils.isSDExist()) {
                        listener.gotoTakePhoto();
                    } else {
                        ToastUtils.toastForShort(context, context.getString(R.string.str_no_sd));
                    }
                } else {
                    ToastUtils.toastForShort(context, context.getString(R.string.str_please_open_camera));
                }
                break;
            case PermissionUtils.REQ_PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listener.gotoOpenGallery();
                } else {
                    ToastUtils.toastForShort(context, context.getString(R.string.str_please_open_sd));
                }
                break;
            default:
        }
    }
}
