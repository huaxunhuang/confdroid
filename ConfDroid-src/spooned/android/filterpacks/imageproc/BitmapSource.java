/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterpacks.imageproc;


/**
 *
 *
 * @unknown 
 */
public class BitmapSource extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "target")
    java.lang.String mTargetString;

    @android.filterfw.core.GenerateFieldPort(name = "bitmap")
    private android.graphics.Bitmap mBitmap;

    @android.filterfw.core.GenerateFieldPort(name = "recycleBitmap", hasDefault = true)
    private boolean mRecycleBitmap = true;

    @android.filterfw.core.GenerateFieldPort(name = "repeatFrame", hasDefault = true)
    boolean mRepeatFrame = false;

    private int mTarget;

    private android.filterfw.core.Frame mImageFrame;

    public BitmapSource(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        // Setup output format
        android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED);
        // Add output port
        addOutputPort("image", outputFormat);
    }

    public void loadImage(android.filterfw.core.FilterContext filterContext) {
        // Create frame with bitmap
        mTarget = android.filterfw.core.FrameFormat.readTargetString(mTargetString);
        android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ImageFormat.create(mBitmap.getWidth(), mBitmap.getHeight(), android.filterfw.format.ImageFormat.COLORSPACE_RGBA, mTarget);
        mImageFrame = filterContext.getFrameManager().newFrame(outputFormat);
        mImageFrame.setBitmap(mBitmap);
        mImageFrame.setTimestamp(android.filterfw.core.Frame.TIMESTAMP_UNKNOWN);
        // Free up space used by bitmap
        if (mRecycleBitmap) {
            mBitmap.recycle();
        }
        mBitmap = null;
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        // Clear image (to trigger reload) in case parameters have been changed
        if (name.equals("bitmap") || name.equals("target")) {
            if (mImageFrame != null) {
                mImageFrame.release();
                mImageFrame = null;
            }
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        if (mImageFrame == null) {
            loadImage(context);
        }
        pushOutput("image", mImageFrame);
        if (!mRepeatFrame) {
            closeOutputPort("image");
        }
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext env) {
        if (mImageFrame != null) {
            mImageFrame.release();
            mImageFrame = null;
        }
    }
}

