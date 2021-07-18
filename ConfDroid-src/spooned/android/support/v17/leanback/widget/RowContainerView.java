/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * RowContainerView wraps header and user defined row view
 */
final class RowContainerView extends android.widget.LinearLayout {
    private android.view.ViewGroup mHeaderDock;

    private android.graphics.drawable.Drawable mForeground;

    private boolean mForegroundBoundsChanged = true;

    public RowContainerView(android.content.Context context) {
        this(context, null, 0);
    }

    public RowContainerView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowContainerView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(android.widget.LinearLayout.VERTICAL);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        inflater.inflate(R.layout.lb_row_container, this);
        mHeaderDock = ((android.view.ViewGroup) (findViewById(R.id.lb_row_container_header_dock)));
        setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void addHeaderView(android.view.View headerView) {
        if (mHeaderDock.indexOfChild(headerView) < 0) {
            mHeaderDock.addView(headerView, 0);
        }
    }

    public void removeHeaderView(android.view.View headerView) {
        if (mHeaderDock.indexOfChild(headerView) >= 0) {
            mHeaderDock.removeView(headerView);
        }
    }

    public void addRowView(android.view.View view) {
        addView(view);
    }

    public void showHeader(boolean show) {
        mHeaderDock.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    public void setForeground(android.graphics.drawable.Drawable d) {
        mForeground = d;
        setWillNotDraw(mForeground == null);
        invalidate();
    }

    public void setForegroundColor(@android.support.annotation.ColorInt
    int color) {
        if (mForeground instanceof android.graphics.drawable.ColorDrawable) {
            ((android.graphics.drawable.ColorDrawable) (mForeground.mutate())).setColor(color);
            invalidate();
        } else {
            setForeground(new android.graphics.drawable.ColorDrawable(color));
        }
    }

    public android.graphics.drawable.Drawable getForeground() {
        return mForeground;
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundBoundsChanged = true;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (mForeground != null) {
            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;
                mForeground.setBounds(0, 0, getWidth(), getHeight());
            }
            mForeground.draw(canvas);
        }
    }
}

