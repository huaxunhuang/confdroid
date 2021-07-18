/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Mock version of the SurfaceView.
 * Only non override public methods from the real SurfaceView have been added in there.
 * Methods that take an unknown class as parameter or as return object, have been removed for now.
 *
 * TODO: generate automatically.
 */
public class SurfaceView extends com.android.layoutlib.bridge.MockView {
    public SurfaceView(android.content.Context context) {
        this(context, null);
    }

    public SurfaceView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SurfaceView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean gatherTransparentRegion(android.graphics.Region region) {
        return false;
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
    }

    public void setZOrderOnTop(boolean onTop) {
    }

    public void setSecure(boolean isSecure) {
    }

    public android.view.SurfaceHolder getHolder() {
        return mSurfaceHolder;
    }

    private android.view.SurfaceHolder mSurfaceHolder = new android.view.SurfaceHolder() {
        @java.lang.Override
        public boolean isCreating() {
            return false;
        }

        @java.lang.Override
        public void addCallback(android.view.SurfaceHolder.Callback callback) {
        }

        @java.lang.Override
        public void removeCallback(android.view.SurfaceHolder.Callback callback) {
        }

        @java.lang.Override
        public void setFixedSize(int width, int height) {
        }

        @java.lang.Override
        public void setSizeFromLayout() {
        }

        @java.lang.Override
        public void setFormat(int format) {
        }

        @java.lang.Override
        public void setType(int type) {
        }

        @java.lang.Override
        public void setKeepScreenOn(boolean screenOn) {
        }

        @java.lang.Override
        public android.graphics.Canvas lockCanvas() {
            return null;
        }

        @java.lang.Override
        public android.graphics.Canvas lockCanvas(android.graphics.Rect dirty) {
            return null;
        }

        @java.lang.Override
        public void unlockCanvasAndPost(android.graphics.Canvas canvas) {
        }

        @java.lang.Override
        public android.view.Surface getSurface() {
            return null;
        }

        @java.lang.Override
        public android.graphics.Rect getSurfaceFrame() {
            return null;
        }
    };
}

