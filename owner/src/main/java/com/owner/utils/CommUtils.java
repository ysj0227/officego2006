package com.owner.utils;

import android.text.Html;
import android.widget.EditText;

/**
 * Created by YangShiJie
 * Data 2020/7/15.
 * Descriptions:
 **/
public class CommUtils {

    public static void showHtmlView(EditText textView, String info) {
        if (info.contains("strong style='color:")) {
            String next = info.replace("strong", "font");
            textView.setText(Html.fromHtml(next.trim()));
        } else {
            textView.setText(info.trim());
        }
    }
}
