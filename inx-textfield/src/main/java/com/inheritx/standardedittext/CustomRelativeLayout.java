package com.inheritx.standardedittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

class CustomRelativeLayout extends RelativeLayout {

    Context context;
    Float radius;
    Rect rect = new Rect();
    RectF rectF = new RectF();
    Path path = new Path();

    public CustomRelativeLayout(Context context) {

        super(context);
        this.context = context;
        init();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    protected void init() {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        radius = context.getResources().getDimension(R.dimen.corner_radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.getClipBounds(rect);
        rectF.set(rect);
        path.reset();
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
