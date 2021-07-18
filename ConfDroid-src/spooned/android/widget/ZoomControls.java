/**
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.widget;


/**
 * The {@code ZoomControls} class displays a simple set of controls used for zooming and
 * provides callbacks to register for events.
 *
 * @deprecated This functionality and UI is better handled with custom views and layouts
rather than a dedicated zoom-control widget
 */
@java.lang.Deprecated
@android.annotation.Widget
public class ZoomControls extends android.widget.LinearLayout {
    @android.annotation.UnsupportedAppUsage
    private final android.widget.ZoomButton mZoomIn;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.ZoomButton mZoomOut;

    public ZoomControls(android.content.Context context) {
        this(context, null);
    }

    public ZoomControls(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        // we are the parent
        inflater.inflate(R.layout.zoom_controls, this, true);
        mZoomIn = ((android.widget.ZoomButton) (findViewById(R.id.zoomIn)));
        mZoomOut = ((android.widget.ZoomButton) (findViewById(R.id.zoomOut)));
    }

    public void setOnZoomInClickListener(android.view.View.OnClickListener listener) {
        mZoomIn.setOnClickListener(listener);
    }

    public void setOnZoomOutClickListener(android.view.View.OnClickListener listener) {
        mZoomOut.setOnClickListener(listener);
    }

    /* Sets how fast you get zoom events when the user holds down the
    zoom in/out buttons.
     */
    public void setZoomSpeed(long speed) {
        mZoomIn.setZoomSpeed(speed);
        mZoomOut.setZoomSpeed(speed);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        /* Consume all touch events so they don't get dispatched to the view
        beneath this view.
         */
        return true;
    }

    public void show() {
        fade(android.view.View.VISIBLE, 0.0F, 1.0F);
    }

    public void hide() {
        fade(android.view.View.GONE, 1.0F, 0.0F);
    }

    private void fade(int visibility, float startAlpha, float endAlpha) {
        android.view.animation.AlphaAnimation anim = new android.view.animation.AlphaAnimation(startAlpha, endAlpha);
        anim.setDuration(500);
        startAnimation(anim);
        setVisibility(visibility);
    }

    public void setIsZoomInEnabled(boolean isEnabled) {
        mZoomIn.setEnabled(isEnabled);
    }

    public void setIsZoomOutEnabled(boolean isEnabled) {
        mZoomOut.setEnabled(isEnabled);
    }

    @java.lang.Override
    public boolean hasFocus() {
        return mZoomIn.hasFocus() || mZoomOut.hasFocus();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ZoomControls.class.getName();
    }
}

