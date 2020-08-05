package com.owner.utils;

import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

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

    public static void showHtmlTextView(TextView textView, String info) {
        if (info.contains("strong style='color:")) {
            String next = info.replace("strong", "font");
            textView.setText(Html.fromHtml(next.trim()));
        } else {
            textView.setText(info.trim());
        }
    }

    public static void searchHtmlTextView(TextView textView, String info) {
        if (info.contains("strong style='color:#46C3C2'")) {
            String next = info.replace("strong style='color:#46C3C2'", "font color=#46C3C2");
            String next1 = next.replace("strong", "font");
            textView.setText(Html.fromHtml(next1.trim()));
        } else {
            textView.setText(info.trim());
        }
    }

}
