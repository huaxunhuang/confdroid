/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * A {@link SeekBar} which supports compatible features on older version of the platform.
 *
 * <p>This will automatically be used when you use {@link SeekBar} in your layouts.
 * You should only need to manually use this class when writing custom views.</p>
 */
public class AppCompatSeekBar extends android.widget.SeekBar {
    private android.support.v7.widget.AppCompatSeekBarHelper mAppCompatSeekBarHelper;

    public AppCompatSeekBar(android.content.Context context) {
        this(context, null);
    }

    public AppCompatSeekBar(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public AppCompatSeekBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAppCompatSeekBarHelper = new android.support.v7.widget.AppCompatSeekBarHelper(this);
        mAppCompatSeekBarHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        mAppCompatSeekBarHelper.drawTickMarks(canvas);
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mAppCompatSeekBarHelper.drawableStateChanged();
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
    }
}

