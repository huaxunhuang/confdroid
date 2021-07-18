/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class CheckableImageButton extends android.support.v7.widget.AppCompatImageButton implements android.widget.Checkable {
    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{ android.R.attr.state_checked };

    private boolean mChecked;

    public CheckableImageButton(android.content.Context context) {
        this(context, null);
    }

    public CheckableImageButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.imageButtonStyle);
    }

    public CheckableImageButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(this, new android.support.v4.view.AccessibilityDelegateCompat() {
            @java.lang.Override
            public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                event.setChecked(isChecked());
            }

            @java.lang.Override
            public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCheckable(true);
                info.setChecked(isChecked());
            }
        });
    }

    @java.lang.Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            sendAccessibilityEvent(android.support.v4.view.accessibility.AccessibilityEventCompat.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    @java.lang.Override
    public boolean isChecked() {
        return mChecked;
    }

    @java.lang.Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @java.lang.Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mChecked) {
            return android.view.View.mergeDrawableStates(super.onCreateDrawableState(extraSpace + android.support.design.widget.CheckableImageButton.DRAWABLE_STATE_CHECKED.length), android.support.design.widget.CheckableImageButton.DRAWABLE_STATE_CHECKED);
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }
}

