package com.officego.commonlib.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by YangShiJie
 * Data 2020/5/28.
 * Descriptions:
 **/
public class CopyTextUtils {
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private Context context;
    private String text;

    public CopyTextUtils(Context context, String text) {
        this.context = context;
        this.text = text;
        init();
    }

    private void init() {
        myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(context, text + " 已复制", Toast.LENGTH_SHORT).show();
    }

}