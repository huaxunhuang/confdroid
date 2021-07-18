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
public class ObjectSource extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "object")
    private java.lang.Object mObject;

    @android.filterfw.core.GenerateFinalPort(name = "format", hasDefault = true)
    private android.filterfw.core.FrameFormat mOutputFormat = android.filterfw.core.FrameFormat.unspecified();

    @android.filterfw.core.GenerateFieldPort(name = "repeatFrame", hasDefault = true)
    boolean mRepeatFrame = false;

    private android.filterfw.core.Frame mFrame;

    public ObjectSource(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addOutputPort("frame", mOutputFormat);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // If no frame has been created, create one now.
        if (mFrame == null) {
            if (mObject == null) {
                throw new java.lang.NullPointerException("ObjectSource producing frame with no object set!");
            }
            android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ObjectFormat.fromObject(mObject, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
            mFrame = context.getFrameManager().newFrame(outputFormat);
            mFrame.setObjectValue(mObject);
            mFrame.setTimestamp(android.filterfw.core.Frame.TIMESTAMP_UNKNOWN);
        }
        // Push output
        pushOutput("frame", mFrame);
        // Wait for free output
        if (!mRepeatFrame) {
            closeOutputPort("frame");
        }
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        mFrame.release();
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        // Release our internal frame, so that it is regenerated on the next call to process().
        if (name.equals("object")) {
            if (mFrame != null) {
                mFrame.release();
                mFrame = null;
            }
        }
    }
}

