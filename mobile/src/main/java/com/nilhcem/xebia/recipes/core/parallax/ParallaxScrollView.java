package com.nilhcem.xebia.recipes.core.parallax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;

public class ParallaxScrollView extends ScrollView {

    private static final float PARALLAX_FACTOR = 0.55f;

    private WeakReference<View> mParallaxedView;

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 0 && getChildAt(0) instanceof ViewGroup) {
            ViewGroup viewsHolder = (ViewGroup) getChildAt(0);
            mParallaxedView = new WeakReference<View>(viewsHolder.getChildAt(0));
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = mParallaxedView.get();
        if (view != null) {
            view.setTranslationY(PARALLAX_FACTOR * t);
        }
    }
}
