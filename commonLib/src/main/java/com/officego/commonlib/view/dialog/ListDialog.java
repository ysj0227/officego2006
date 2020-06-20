package com.officego.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.R;
import com.officego.commonlib.view.SmRecyclerView;


/**
 * Description:
 * Created by bruce on 2019/8/21.
 */
public class ListDialog extends Dialog {

    private ListDialog(Context context, int theme) {
        super(context, theme);
    }

    public void showWithOutTouchable(boolean touchable) {
        this.setCanceledOnTouchOutside(touchable);
        this.show();
    }

    public static class Builder<T extends RecyclerView.Adapter> {
        private Context context;
        private String title;
        private String backBtnText; // 对话框返回按钮文本
        private String confirmBtnText; // 对话框确定文本
        private int cancelBtnTextColor, confirmBtnTextColor; // 对话框确定文本颜色
        private boolean hasItemDecoration = true;  //是否显示下划线，默认显示
        private T adapter;
        private int listHeight = 0;
        // 对话框按钮监听事件
        private OnClickListener cancelButtonClickListener, confirmButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置title显示，resource获取
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置title显示，直接设置字符串
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置back按钮的事件和文本（resource资源）
         */
        public Builder setCancelButton(int text, OnClickListener listener) {
            this.backBtnText = (String) context.getText(text);
            this.cancelButtonClickListener = listener;
            return this;
        }

        /**
         * 设置back按钮的事件和文本（resource资源）,直接dismiss
         */
        public Builder setCancelButton(int text) {
            this.backBtnText = (String) context.getText(text);
            this.cancelButtonClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
            return this;
        }

        public Builder setCancelButton(int text, int textColor) {
            this.backBtnText = (String) context.getText(text);
            this.cancelBtnTextColor = textColor;
            this.cancelButtonClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
            return this;
        }

        /**
         * 设置确定按钮事件和文本（resource资源）
         */
        public Builder setConfirmButton(String text, OnClickListener listener) {
            this.confirmBtnText = text;
            this.confirmButtonClickListener = listener;
            return this;
        }

        /**
         * 设置确定按钮事件和文本（resource资源）
         */
        public Builder setConfirmButton(int text, OnClickListener listener) {
            return setConfirmButton((String) context.getText(text), listener);
        }

        /**
         * 设置确定按钮事件和文本（resource资源）
         */
        public Builder setConfirmButton(int text) {
            return setConfirmButton((String) context.getText(text));
        }

        /**
         * 设置确定按钮事件和文本（resource资源）
         */
        public Builder setConfirmButton(String text) {
            this.confirmBtnText = text;
            this.confirmButtonClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
            return this;
        }

        /**
         * 设置确定按钮事件和文本(颜色)（resource资源）
         */
        public Builder setConfirmButton(int text, int textColor, OnClickListener listener) {
            this.confirmBtnText = (String) context.getText(text);
            this.confirmBtnTextColor = textColor;
            this.confirmButtonClickListener = listener;
            return this;
        }

        /**
         * 提示框，设置确定按钮事件和文本(颜色)（resource资源）
         */
        public Builder setConfirmButton(int text, int textColor) {
            this.confirmBtnText = (String) context.getText(text);
            this.confirmBtnTextColor = textColor;
            this.confirmButtonClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
            return this;
        }

        /**
         * 设置确定按钮事件和文本（字符串）
         */
        public Builder setConfirmButton(String text, int textColor, OnClickListener listener) {
            this.confirmBtnText = text;
            this.confirmBtnTextColor = textColor;
            this.confirmButtonClickListener = listener;
            return this;
        }

        public Builder setItemDecoration(boolean isShow) {
            this.hasItemDecoration = isShow;
            return this;
        }

        /**
         * 设置确定按钮事件和文本（字符串）
         */
        public Builder setAdapter(T adapter) {
            this.adapter = adapter;
            return this;
        }

        /**
         * 设置list的高度
         * @param height
         * @return
         */
        public Builder setListHeight(int height) {
            this.listHeight = height;
            return this;
        }

        /**
         * 创建自定义的对话框
         */
        public ListDialog create() {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义的对话框主题
            final ListDialog dialog = new ListDialog(context, R.style.Son_dialog);
            View layout = inflater.inflate(R.layout.dialog_common, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tvTitle = layout.findViewById(R.id.tv_title);
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
            }

            SmRecyclerView rv = layout.findViewById(R.id.rv_content);
            rv.setVisibility(View.VISIBLE);
            if (hasItemDecoration) {
                rv.init(R.drawable.shap_line_divider);
            } else {
                rv.init(0);
            }
            if (listHeight != 0) {
                ViewGroup.LayoutParams params = rv.getLayoutParams();
                params.height = listHeight;
                rv.setLayoutParams(params);
            }
            rv.setAdapter(adapter);

            // 设置返回按钮事件和文本
            if (backBtnText != null) {
                Button bckButton = layout.findViewById(R.id.btn_cancel);
                bckButton.setText(backBtnText);
                if (cancelBtnTextColor > 0) {
                    bckButton.setTextColor(context.getResources().getColor(cancelBtnTextColor));
                }
                if (cancelButtonClickListener != null) {
                    bckButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.cancel();
                            cancelButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
                layout.findViewById(R.id.btn_divider).setVisibility(View.GONE);
            }

            // 设置确定按钮事件和文本
            if (confirmBtnText != null) {
                Button cfmButton = layout.findViewById(R.id.btn_sure);
                cfmButton.setText(confirmBtnText);
                if (confirmBtnTextColor > 0)
                    cfmButton.setTextColor(context.getResources().getColor(confirmBtnTextColor));

                if (confirmButtonClickListener != null) {
                    cfmButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            confirmButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_sure).setVisibility(View.GONE);
                layout.findViewById(R.id.btn_divider).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}