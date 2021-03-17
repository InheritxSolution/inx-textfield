package com.inheritx.standardedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Field;


@SuppressWarnings("unused")
public class CustomFrameLayout extends FrameLayout {

    public int DEFAULT_ERROR_COLOR;
    public int DEFAULT_PRIMARY_COLOR;
    public int DEFAULT_TEXT_COLOR;
    public int DEFAULT_DISABLED_COLOR;
    public int DEFAULT_BG_COLOR;
    public int DEFAULT_FG_COLOR;

    protected boolean enabled;

    protected String labelText;

    protected String helperText;

    protected int helperTextColor;

    protected int errorColor;

    protected int primaryColor;

    protected int secondaryColor;

    protected int layoutBackgroundColor;

    protected boolean hasFocus;

    protected boolean showHint;

    protected boolean useSpacing;

    protected boolean rtl;

    protected int labelColor = -1;
    protected int labelTopMargin = -1;
    protected int ANIMATION_DURATION = 100;
    protected boolean onError = false;
    protected boolean activated = false;

    protected boolean isManualValidateError = false;

    protected View panel;
    protected View bottomLine;
    protected Space labelSpace;
    protected Space labelSpaceBelow;
    protected ViewGroup editTextLayout;
    protected StandardEditText editText;
    protected RelativeLayout rightShell;
    protected RelativeLayout upperPanel;
    protected RelativeLayout bottomPart;
    protected RelativeLayout inputLayout;
    protected AppCompatTextView helperLabel;
    protected AppCompatTextView floatingLabel;
    protected AppCompatImageButton clearButton;
    protected InputMethodManager inputMethodManager;

    protected SimpleTextChangedWatcher textChangeListener;

    public CustomFrameLayout(Context context) {

        super(context);
        init();
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
        handleAttributes(context, attrs);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        handleAttributes(context, attrs);
    }

    protected void init() {
        initDefaultColor();
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initDefaultColor() {

        Resources.Theme theme = getContext().getTheme();
        TypedArray themeArray;

        DEFAULT_ERROR_COLOR = ContextCompat.getColor(getContext(), R.color.color_red);

        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.colorForeground});
        DEFAULT_BG_COLOR = adjustAlpha(themeArray.getColor(0, 0), 0.06f);

        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.colorBackground});
        DEFAULT_FG_COLOR = themeArray.getColor(0, 0);

        themeArray = theme.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        if (isLight(DEFAULT_BG_COLOR))
            DEFAULT_PRIMARY_COLOR = lighter(themeArray.getColor(0, 0), 0.2f);
        else DEFAULT_PRIMARY_COLOR = themeArray.getColor(0, 0);

        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.textColorTertiary});
        DEFAULT_TEXT_COLOR = themeArray.getColor(0, 0);

        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.disabledAlpha});
        float disabledAlpha = themeArray.getFloat(0, 0);
        themeArray = theme.obtainStyledAttributes(new int[]{android.R.attr.textColorTertiary});
        DEFAULT_DISABLED_COLOR = adjustAlpha(themeArray.getColor(0, 0), disabledAlpha);
        themeArray.recycle();
    }

    protected StandardEditText findEditTextChild() {

        if (getChildCount() > 0 && getChildAt(0) instanceof StandardEditText)
            return (StandardEditText) getChildAt(0);
        return null;
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        initViews();
        triggerSetters();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {

            /* match_parent or specific value */
            this.inputLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            this.upperPanel.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            this.editTextLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        } else if (widthMode == MeasureSpec.AT_MOST) {

            /* wrap_content */
            this.inputLayout.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.upperPanel.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.editTextLayout.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (heightMode == MeasureSpec.EXACTLY) {

            /* match_parent or specific value */
            this.panel.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            this.rightShell.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            this.upperPanel.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            ((RelativeLayout.LayoutParams) this.bottomPart.getLayoutParams())
                    .addRule(RelativeLayout.BELOW, 0);
            ((RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams())
                    .addRule(RelativeLayout.BELOW, 0);
            ((RelativeLayout.LayoutParams) this.bottomPart.getLayoutParams())
                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ((RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams())
                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ((RelativeLayout.LayoutParams) this.panel.getLayoutParams())
                    .addRule(RelativeLayout.ABOVE, R.id._bottom);

        } else if (heightMode == MeasureSpec.AT_MOST) {

            /* wrap_content */
            this.panel.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.rightShell.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.upperPanel.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

            ((RelativeLayout.LayoutParams) this.bottomPart.getLayoutParams())
                    .addRule(RelativeLayout.BELOW, R.id._panel);
            ((RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams())
                    .addRule(RelativeLayout.BELOW, R.id._upper_panel);
            ((RelativeLayout.LayoutParams) this.bottomPart.getLayoutParams())
                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            ((RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams())
                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            ((RelativeLayout.LayoutParams) this.panel.getLayoutParams())
                    .addRule(RelativeLayout.ABOVE, 0);
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initViews() {

        this.editText = findEditTextChild();
        if (editText == null) return;
        this.addView(LayoutInflater.from(getContext()).inflate(rtl ?
                        R.layout.custom_edittext_layout_rtl :
                        R.layout.custom_edittext_layout,
                this, false));
        removeView(this.editText);

        this.editText.setBackgroundColor(Color.TRANSPARENT);
        this.editText.setDropDownBackgroundDrawable(new ColorDrawable(DEFAULT_FG_COLOR));
        this.editText.setMinimumWidth(10);
        this.inputLayout = this.findViewById(R.id._input_layout);
        this.floatingLabel = findViewById(R.id._label);
        this.panel = findViewById(R.id._panel);
        this.labelSpace = findViewById(R.id._label_space);
        this.labelSpaceBelow = findViewById(R.id._label_space_below);
        this.bottomLine = findViewById(R.id.bg_bottom_line);
        this.rightShell = findViewById(R.id._right_shell);
        this.upperPanel = findViewById(R.id._upper_panel);
        this.bottomPart = findViewById(R.id._bottom);
        this.clearButton = findViewById(R.id._clear_button);
        this.helperLabel = findViewById(R.id._helper);
        this.editTextLayout = findViewById(R.id._editTextLayout);

        this.inputLayout.addView(this.editText);
        this.editTextLayout.setAlpha(0f);
        this.floatingLabel.setPivotX(0f);
        this.floatingLabel.setPivotY(0f);
        this.labelColor = this.floatingLabel.getCurrentTextColor();
        this.clearButton.setColorFilter(DEFAULT_TEXT_COLOR);
        this.clearButton.setAlpha(0.35f);
        this.labelTopMargin = RelativeLayout.LayoutParams.class
                .cast(this.floatingLabel.getLayoutParams()).topMargin;

        initOnClick();

        setUseSpacing(this.useSpacing);
        updateDimens(this.useSpacing);
        if (!this.editText.getText().toString().isEmpty() || this.hasFocus)
            activate(false);
    }

    private void initOnClick() {

        final FrameLayout mainBody = this;

        this.panel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isActivated()) activate(true);
                setHasFocus(true);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                mainBody.performClick();
            }
        });

        this.editText.setDefaultOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) setHasFocus(true);
                else setHasFocus(false);
            }
        });

        this.editText.addTextChangedListener(new TextWatcher() {

            private String lastValue = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!activated && !editable.toString().isEmpty()) activate(true);
                if (activated && editable.toString().isEmpty() && !hasFocus) deactivate();
                if (isManualValidateError) {
                    //updateCounterText(false);
                } else {
                    validate();
                }


                if (!lastValue.equals(editable.toString())){
                    lastValue = editable.toString();
                    if (textChangeListener != null) {
                        textChangeListener.onTextChanged(editable.toString(), onError);
                    }
                }

            }
        });

        this.clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {

        try {

            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.StandardEdittext);

            this.labelText = styledAttrs.getString(R.styleable.StandardEdittext_labelText)
                    == null ? "" : styledAttrs.getString(R.styleable.StandardEdittext_labelText);
            this.helperText = styledAttrs.getString(R.styleable.StandardEdittext_helperText)
                    == null ? "" : styledAttrs.getString(R.styleable.StandardEdittext_helperText);

            this.helperTextColor = styledAttrs
                    .getInt(R.styleable.StandardEdittext_helperTextColor, DEFAULT_TEXT_COLOR);
            this.errorColor = styledAttrs
                    .getInt(R.styleable.StandardEdittext_errorColor, DEFAULT_ERROR_COLOR);
            this.primaryColor = styledAttrs
                    .getColor(R.styleable.StandardEdittext_primaryColor, DEFAULT_PRIMARY_COLOR);
            this.secondaryColor = styledAttrs
                    .getColor(R.styleable.StandardEdittext_secondaryColor, DEFAULT_TEXT_COLOR);
            this.layoutBackgroundColor = styledAttrs
                    .getColor(R.styleable.StandardEdittext_layoutBackgroundColor, DEFAULT_BG_COLOR);

            this.isManualValidateError = styledAttrs.getBoolean(R.styleable.StandardEdittext_manualValidateError, false);
            this.enabled = styledAttrs.getBoolean(R.styleable.StandardEdittext_enabled, true);
            this.hasFocus = styledAttrs.getBoolean(R.styleable.StandardEdittext_hasFocus, false);
            this.showHint = styledAttrs.getBoolean(R.styleable.StandardEdittext_showHint, false);
            this.useSpacing = styledAttrs.getBoolean(R.styleable.StandardEdittext_useSpacing, false);
            this.rtl = styledAttrs.getBoolean(R.styleable.StandardEdittext_rtl, false);

            styledAttrs.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSimpleTextChangeWatcher(SimpleTextChangedWatcher textChangeListener) {
        this.textChangeListener = textChangeListener;
    }


    protected void deactivate() {

        if (this.editText.getText().toString().isEmpty()) {

            if (this.showHint && !this.editText.getHint().toString().isEmpty()) {

                this.editTextLayout.setAlpha(1f);
                this.floatingLabel.setScaleX(0.75f);
                this.floatingLabel.setScaleY(0.75f);
                this.floatingLabel.setTranslationY(-labelTopMargin +
                        getContext().getResources().getDimensionPixelOffset(R.dimen.label_active_margin_top));

            } else {

                this.editTextLayout.setAlpha(0);
                ViewCompat.animate(floatingLabel)
                        .alpha(1)
                        .scaleX(1)
                        .scaleY(1)
                        .translationY(0)
                        .setDuration(ANIMATION_DURATION);
            }

            if (this.editText.hasFocus()) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                this.editText.clearFocus();
            }
        }
        this.activated = false;
    }

    protected void activate(boolean animated) {

        this.editText.setAlpha(1);

        if (this.editText.getText().toString().isEmpty() && !isActivated()) {

            this.editTextLayout.setAlpha(0f);
            this.floatingLabel.setScaleX(1f);
            this.floatingLabel.setScaleY(1f);
            this.floatingLabel.setTranslationY(0);
        }

        final boolean keepHint = this.showHint && !this.editText.getHint().toString().isEmpty();
        if (animated && !keepHint) {

            ViewCompat.animate(this.editTextLayout)
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(this.floatingLabel)
                    .scaleX(0.75f)
                    .scaleY(0.75f)
                    .translationY(-labelTopMargin +
                            getContext().getResources().getDimensionPixelOffset(R.dimen.label_active_margin_top))
                    .setDuration(ANIMATION_DURATION);
        } else {

            this.editTextLayout.setAlpha(1f);
            this.floatingLabel.setScaleX(0.75f);
            this.floatingLabel.setScaleY(0.75f);
            this.floatingLabel.setTranslationY(-labelTopMargin +
                    getContext().getResources().getDimensionPixelOffset(R.dimen.label_active_margin_top));
        }

        activated = true;
    }

    protected void makeCursorBlink() {

        CharSequence hintCache = this.editText.getHint();
        this.editText.setHint(" ");
        this.editText.setHint(hintCache);
    }

    protected void setHighlightColor(int colorRes) {

        this.floatingLabel.setTextColor(colorRes);
        setCursorDrawableColor(this.editText, colorRes);

        this.bottomLine.setBackgroundColor(colorRes);
    }

    protected void setManualValidateError(boolean isManualValidateError) {
        this.isManualValidateError = isManualValidateError;
    }


    public boolean validate() {
        removeError();
        if (onError) {
            setError(null, false);
        }
        return !onError;
    }


    @Deprecated
    public boolean validateError() {
        return validate();
    }


    protected void updateDimens(boolean useSpacing) {

        final Resources res = getContext().getResources();

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.floatingLabel.getLayoutParams();
        lp.topMargin = res.getDimensionPixelOffset(
                useSpacing ?
                        R.dimen.dense_label_idle_margin_top :
                        R.dimen.label_idle_margin_top
        );
        this.floatingLabel.setLayoutParams(lp);

        this.inputLayout.setPadding(
                0, res.getDimensionPixelOffset(
                        useSpacing ?
                                R.dimen.dense_editTextLayout_padding_top :
                                R.dimen.editTextLayout_padding_top
                ),
                0, res.getDimensionPixelOffset(R.dimen.editTextLayout_padding_bottom));

        this.clearButton.setMinimumHeight(
                res.getDimensionPixelOffset(
                        useSpacing ?
                                R.dimen.clear_button_min_height :
                                R.dimen.dense_clear_button_min_height
                )
        );
        this.clearButton.setMinimumWidth(
                res.getDimensionPixelOffset(
                        useSpacing ?
                                R.dimen.clear_button_min_width :
                                R.dimen.dense_clear_button_min_width
                )
        );

        lp = (RelativeLayout.LayoutParams) this.bottomPart.getLayoutParams();
        lp.topMargin = res.getDimensionPixelOffset(
                useSpacing ?
                        R.dimen.dense_bottom_marginTop :
                        R.dimen.bottom_marginTop
        );
        this.bottomPart.setLayoutParams(lp);

        this.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(
                useSpacing ?
                        R.dimen.dense_edittext_text_size :
                        R.dimen.edittext_text_size
        ));

        this.labelTopMargin = RelativeLayout.LayoutParams.class
                .cast(this.floatingLabel.getLayoutParams()).topMargin;
        this.requestLayout();
    }

    protected void updateBottomViewVisibility() {

        if (this.helperLabel.getText().toString().isEmpty())
            this.bottomPart.setVisibility(View.GONE);
        else this.bottomPart.setVisibility(View.VISIBLE);
    }

    public void setError(@Nullable String errorText, boolean giveFocus) {
        if (this.enabled) {
            this.onError = true;
            activate(true);
            setHighlightColor(this.errorColor);
            this.helperLabel.setTextColor(this.errorColor);
            if (errorText != null) {
                this.helperLabel.setText(errorText);
                updateBottomViewVisibility();
            }
            if (giveFocus) setHasFocus(true);
            makeCursorBlink();
        }
    }

    public void removeError() {
        this.onError = false;
        if (this.hasFocus) setHighlightColor(this.primaryColor);
        else setHighlightColor(this.secondaryColor);
        this.helperLabel.setTextColor(this.helperTextColor);
        this.helperLabel.setText(this.helperText);
        updateBottomViewVisibility();
    }

    protected void showClearButton(boolean show) {

        if (show) this.clearButton.setVisibility(View.VISIBLE);
        else this.clearButton.setVisibility(View.GONE);
    }

    private void triggerSetters() {

        setLabelText(this.labelText);
        setHelperText(this.helperText);

        setHelperTextColor(this.helperTextColor);
        setErrorColor(this.errorColor);
        setPrimaryColor(this.primaryColor);
        setSecondaryColor(this.secondaryColor);
        setLayoutBackgroundColor(this.layoutBackgroundColor);

        setEnabled(this.enabled);
        setHasFocus(this.hasFocus);
        setShowHint(this.showHint);
        updateBottomViewVisibility();
    }

    public void setLabelText(String labelText) {

        this.labelText = labelText;
        this.floatingLabel.setText(this.labelText);

        if (labelText.isEmpty()) {
            this.floatingLabel.setVisibility(View.GONE);
            this.labelSpace.setVisibility(View.GONE);
            this.labelSpaceBelow.setVisibility(View.VISIBLE);
        } else {
            this.floatingLabel.setVisibility(View.VISIBLE);
            this.labelSpace.setVisibility(View.VISIBLE);
            this.labelSpaceBelow.setVisibility(View.GONE);
        }
    }

    public void setHelperText(String helperText) {

        this.helperText = helperText;
        this.helperLabel.setText(this.helperText);
    }

    public void setHelperTextColor(int colorRes) {

        this.helperTextColor = colorRes;
        this.helperLabel.setTextColor(this.helperTextColor);
    }

    public void setErrorColor(int colorRes) {
        this.errorColor = colorRes;
    }

    public void setPrimaryColor(int colorRes) {

        this.primaryColor = colorRes;
        if (this.hasFocus) setHighlightColor(this.primaryColor);
    }

    public void setSecondaryColor(int colorRes) {

        this.secondaryColor = colorRes;
        if (!this.hasFocus) setHighlightColor(this.secondaryColor);
    }

    public void setLayoutBackgroundColor(int colorRes) {

        this.layoutBackgroundColor = colorRes;
        this.panel.getBackground()
                .setColorFilter(new PorterDuffColorFilter(colorRes, PorterDuff.Mode.SRC_IN));
    }

   public void setEnabled(boolean enabled) {

        this.enabled = enabled;
        if (this.enabled) {
            this.editText.setEnabled(true);
            this.editText.setFocusableInTouchMode(true);
            this.editText.setFocusable(true);
            this.helperLabel.setVisibility(View.VISIBLE);
            this.bottomLine.setVisibility(View.VISIBLE);
            this.panel.setEnabled(true);
            setHighlightColor(secondaryColor);

        } else {
            removeError();
            setHasFocus(false);
            this.editText.setEnabled(false);
            this.editText.setFocusableInTouchMode(false);
            this.editText.setFocusable(false);
            this.helperLabel.setVisibility(View.INVISIBLE);
            this.bottomLine.setVisibility(View.INVISIBLE);
            this.panel.setEnabled(false);
            setHighlightColor(DEFAULT_DISABLED_COLOR);
        }
    }

    public void setHasFocus(boolean hasFocus) {

        this.hasFocus = hasFocus;
        if (this.hasFocus) {
            activate(true);
            this.editText.requestFocus();
            makeCursorBlink();

            /* if there's an error, keep the error color */
            if (!this.onError && this.enabled) setHighlightColor(this.primaryColor);

        } else {
            deactivate();

            /* if there's an error, keep the error color */
            if (!this.onError && this.enabled) setHighlightColor(this.secondaryColor);
        }
    }

    public void setShowHint(boolean showHint) {
        this.showHint = showHint;
    }

    public void setUseSpacing(boolean useSpacing) {
        this.useSpacing = useSpacing;
    }

    public String getLabelText() {
        return this.labelText;
    }

    public String getHelperText() {
        return this.helperText;
    }

    public int getHelperTextColor() {
        return this.helperTextColor;
    }

    public int getErrorColor() {
        return this.errorColor;
    }

    public int getPrimaryColor() {
        return this.primaryColor;
    }

    public int getSecondaryColor() {
        return this.secondaryColor;
    }

    public int getLayoutBackgroundColor() {
        return this.layoutBackgroundColor;
    }

    public View getPanel() {
        return this.panel;
    }

    public View getBottomLine() {
        return this.bottomLine;
    }

    public AppCompatTextView getHelperLabel() {
        return this.helperLabel;
    }

    public AppCompatTextView getFloatingLabel() {
        return this.floatingLabel;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isOnError() {
        return this.onError;
    }

    public boolean getHasFocus() {
        return this.hasFocus;
    }

    public boolean getShowHint() {
        return this.showHint;
    }

    public boolean getUseSpacing() {
        return this.useSpacing;
    }

    protected static void setCursorDrawableColor(EditText _editText, int _colorRes) {

        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(_editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(_editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(_editText.getContext(), mCursorDrawableRes);
            drawables[1] = ContextCompat.getDrawable(_editText.getContext(), mCursorDrawableRes);
            drawables[0].setColorFilter(_colorRes, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(_colorRes, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (Throwable ignored) {
        }
    }

    protected static int lighter(int color, float factor) {

        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    protected static boolean isLight(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068) > 130;
    }

    protected static int adjustAlpha(int color, float _toAlpha) {

        int alpha = Math.round(255 * _toAlpha);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
