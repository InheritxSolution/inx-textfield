package com.inheritx.standardedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

@SuppressWarnings("unused")
public class StandardEditText extends TextInputAutoCompleteTextView {

    public int DEFAULT_TEXT_COLOR;
    private OnFocusChangeListener defaultFocusListener;
    private CustomFocusChangeListener focusListener = new CustomFocusChangeListener();

    public StandardEditText(Context context) {

        this(context, null);
        super.setOnFocusChangeListener(focusListener);
        initDefaultColor();
    }

    public StandardEditText(Context context, AttributeSet attrs) {

        this(context, attrs, android.R.attr.editTextStyle);
        super.setOnFocusChangeListener(focusListener);
        initDefaultColor();
    }

    public StandardEditText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        super.setOnFocusChangeListener(focusListener);
        initDefaultColor();
    }

    protected void initDefaultColor() {

        Resources.Theme theme = getContext().getTheme();
        TypedArray themeArray;

        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.textColorTertiary});
        DEFAULT_TEXT_COLOR = themeArray.getColor(0, 0);

        themeArray.recycle();
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {

        focusListener.clearListeners();
        focusListener.registerListener(defaultFocusListener);
        focusListener.registerListener(l);
    }

    void setDefaultOnFocusChangeListener(OnFocusChangeListener l) {

        defaultFocusListener = l;
        focusListener.registerListener(l);
    }

}
