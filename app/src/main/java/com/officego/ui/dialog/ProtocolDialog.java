package com.officego.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.officego.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.h5.WebViewActivity_;

public class ProtocolDialog {

    private Context context;

    public AgreeProtocolListener getListener() {
        return listener;
    }

    public void setListener(AgreeProtocolListener listener) {
        this.listener = listener;
    }

    private AgreeProtocolListener listener;
    public interface  AgreeProtocolListener{
        void sureProtocol();
    }

    public ProtocolDialog(Context context) {
        this.context = context;
        dialog(context);
    }

    private void dialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_protocol, null);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        TextView tvContent = inflate.findViewById(R.id.tv_content);
        String str = context.getString(R.string.str_protocol);

        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(ToDBC(str));
        // 设置字体大小
//        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(30);
//        // 相对于默认字体大小的倍数,这里是1.3倍
//        // RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan((float) 1.3);
//        spannableBuilder.setSpan(sizeSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 单独设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#46C3C2"));//#FF3838
        spannableBuilder.setSpan(colorSpan, str.indexOf("《"), str.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 单独设置点击事件
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        spannableBuilder.setSpan(new TextClickableSpan(context, 0), str.indexOf("《"), str.indexOf("《") + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableBuilder.setSpan(new TextClickableSpan(context, 1), str.lastIndexOf("《"), str.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvContent.setText(spannableBuilder);
        tvContent.setHighlightColor(Color.parseColor("#00000000"));

        inflate.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            SpUtils.saveProtocol();
            dialog.dismiss();
            listener.sureProtocol();
        });
        inflate.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            System.exit(0);
            System.gc();
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();//显示对话框
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {// 全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    class TextClickableSpan extends ClickableSpan {
        private Context context;
        private int type;

        TextClickableSpan(Context context, int type) {
            this.context = context;
            this.type = type;
        }

        @Override
        public void onClick(@NonNull View widget) {
            WebViewActivity_.intent(context).flags(Constants.H5_PROTOCOL).start();
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            //paint.setColor(Color.parseColor("#3072F6"));
            // 设置下划线 true显示、false不显示
            paint.setUnderlineText(false);
        }
    }
}
