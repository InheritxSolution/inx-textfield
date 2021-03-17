package com.inheritx.standardedittext;

import android.view.View;

import java.util.ArrayList;
import java.util.List;



class CustomFocusChangeListener implements View.OnFocusChangeListener {

    private List<View.OnFocusChangeListener> focusChangeListeners = new ArrayList<>();

    void registerListener (View.OnFocusChangeListener listener) {
        focusChangeListeners.add(listener);
    }

    void clearListeners() {
        focusChangeListeners.clear();
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        for(View.OnFocusChangeListener listener: focusChangeListeners) {
            listener.onFocusChange(view, b);
        }
    }
}
