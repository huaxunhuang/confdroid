/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.view;


/**
 * Exercise View's ability to change their visibility: GONE, INVISIBLE and
 * VISIBLE.
 */
public class VisibilityCallback extends android.app.Activity {
    private static final boolean DEBUG = false;

    private android.view.VisibilityCallback.MonitoredTextView mVictim;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.visibility_callback);
        // Find the view whose visibility will change
        mVictim = ((android.view.VisibilityCallback.MonitoredTextView) (findViewById(R.id.victim)));
        // Find our buttons
        android.widget.Button visibleButton = ((android.widget.Button) (findViewById(R.id.vis)));
        android.widget.Button invisibleButton = ((android.widget.Button) (findViewById(R.id.invis)));
        android.widget.Button goneButton = ((android.widget.Button) (findViewById(R.id.gone)));
        // Wire each button to a click listener
        visibleButton.setOnClickListener(mVisibleListener);
        invisibleButton.setOnClickListener(mInvisibleListener);
        goneButton.setOnClickListener(mGoneListener);
    }

    android.view.View.OnClickListener mVisibleListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            mVictim.setVisibility(android.view.View.VISIBLE);
        }
    };

    android.view.View.OnClickListener mInvisibleListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            mVictim.setVisibility(android.view.View.INVISIBLE);
        }
    };

    android.view.View.OnClickListener mGoneListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            mVictim.setVisibility(android.view.View.GONE);
        }
    };

    public static class MonitoredTextView extends android.widget.TextView {
        private android.view.View mLastVisChangedView;

        private int mLastChangedVisibility;

        public MonitoredTextView(android.content.Context context) {
            super(context);
        }

        public MonitoredTextView(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
        }

        public MonitoredTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public android.view.View getLastVisChangedView() {
            return mLastVisChangedView;
        }

        public int getLastChangedVisibility() {
            return mLastChangedVisibility;
        }

        @java.lang.Override
        protected void onVisibilityChanged(android.view.View changedView, int visibility) {
            mLastVisChangedView = changedView;
            mLastChangedVisibility = visibility;
            if (android.view.VisibilityCallback.DEBUG) {
                android.util.Log.d("viewVis", "visibility: " + visibility);
            }
        }
    }
}

