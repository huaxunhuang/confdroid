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
package android.filterpacks.base;


/**
 *
 *
 * @unknown 
 */
public class GLTextureSource extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "texId")
    private int mTexId;

    @android.filterfw.core.GenerateFieldPort(name = "width")
    private int mWidth;

    @android.filterfw.core.GenerateFieldPort(name = "height")
    private int mHeight;

    @android.filterfw.core.GenerateFieldPort(name = "repeatFrame", hasDefault = true)
    private boolean mRepeatFrame = false;

    /* This timestamp will be used for all output frames from this source.  They
    represent nanoseconds, and should be positive and monotonically
    increasing.  Set to Frame.TIMESTAMP_UNKNOWN if timestamps are not
    meaningful for these textures.
     */
    @android.filterfw.core.GenerateFieldPort(name = "timestamp", hasDefault = true)
    private long mTimestamp = android.filterfw.core.Frame.TIMESTAMP_UNKNOWN;

    private android.filterfw.core.Frame mFrame;

    public GLTextureSource(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addOutputPort("frame", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        // Release frame, so that it is recreated during the next process call
        if (mFrame != null) {
            mFrame.release();
            mFrame = null;
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Generate frame if not generated already
        if (mFrame == null) {
            android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ImageFormat.create(mWidth, mHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
            mFrame = context.getFrameManager().newBoundFrame(outputFormat, android.filterfw.core.GLFrame.EXISTING_TEXTURE_BINDING, mTexId);
            mFrame.setTimestamp(mTimestamp);
        }
        // Push output
        pushOutput("frame", mFrame);
        if (!mRepeatFrame) {
            // Close output port as we are done here
            closeOutputPort("frame");
        }
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mFrame != null) {
            mFrame.release();
        }
    }
}

