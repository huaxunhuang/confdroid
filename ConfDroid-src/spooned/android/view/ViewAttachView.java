/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.view;


/**
 * A View that will throw a RuntimeException if onAttachedToWindow and
 * onDetachedFromWindow is called in the wrong order for ViewAttachTest
 */
public class ViewAttachView extends android.view.View {
    public static final java.lang.String TAG = "OnAttachedTest";

    private boolean attached;

    public ViewAttachView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public ViewAttachView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ViewAttachView(android.content.Context context) {
        super(context);
        init(null, 0);
    }

    private void init(android.util.AttributeSet attrs, int defStyle) {
        android.os.SystemClock.sleep(2000);
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        android.util.Log.d(android.view.ViewAttachView.TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
        if (attached) {
            throw new java.lang.RuntimeException("OnAttachedToWindow called more than once in a row");
        }
        attached = true;
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        android.util.Log.d(android.view.ViewAttachView.TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
        if (!attached) {
            throw new java.lang.RuntimeException("onDetachedFromWindowcalled without prior call to OnAttachedToWindow");
        }
        attached = false;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(android.graphics.Color.BLUE);
    }
}

