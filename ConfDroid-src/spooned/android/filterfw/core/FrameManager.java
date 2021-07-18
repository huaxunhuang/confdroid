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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public abstract class FrameManager {
    private android.filterfw.core.FilterContext mContext;

    public abstract android.filterfw.core.Frame newFrame(android.filterfw.core.FrameFormat format);

    public abstract android.filterfw.core.Frame newBoundFrame(android.filterfw.core.FrameFormat format, int bindingType, long bindingId);

    public android.filterfw.core.Frame duplicateFrame(android.filterfw.core.Frame frame) {
        android.filterfw.core.Frame result = newFrame(frame.getFormat());
        result.setDataFromFrame(frame);
        return result;
    }

    public android.filterfw.core.Frame duplicateFrameToTarget(android.filterfw.core.Frame frame, int newTarget) {
        android.filterfw.core.MutableFrameFormat newFormat = frame.getFormat().mutableCopy();
        newFormat.setTarget(newTarget);
        android.filterfw.core.Frame result = newFrame(newFormat);
        result.setDataFromFrame(frame);
        return result;
    }

    public abstract android.filterfw.core.Frame retainFrame(android.filterfw.core.Frame frame);

    public abstract android.filterfw.core.Frame releaseFrame(android.filterfw.core.Frame frame);

    public android.filterfw.core.FilterContext getContext() {
        return mContext;
    }

    public android.filterfw.core.GLEnvironment getGLEnvironment() {
        return mContext != null ? mContext.getGLEnvironment() : null;
    }

    public void tearDown() {
    }

    void setContext(android.filterfw.core.FilterContext context) {
        mContext = context;
    }
}

