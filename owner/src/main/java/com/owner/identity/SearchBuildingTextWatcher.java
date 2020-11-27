package com.owner.identity;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by shijie
 * Date 2020/11/19
 **/
public class SearchBuildingTextWatcher implements TextWatcher {
    public SearchListener getListener() {
        return listener;
    }

    public void setListener(SearchListener listener) {
        this.listener = listener;
    }

    private SearchListener listener;

    public interface SearchListener {
        void searchEditTextList(String str);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (listener != null) {
            listener.searchEditTextList(charSequence.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
