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
public class SimpleFrameManager extends android.filterfw.core.FrameManager {
    public SimpleFrameManager() {
    }

    @java.lang.Override
    public android.filterfw.core.Frame newFrame(android.filterfw.core.FrameFormat format) {
        return createNewFrame(format);
    }

    @java.lang.Override
    public android.filterfw.core.Frame newBoundFrame(android.filterfw.core.FrameFormat format, int bindingType, long bindingId) {
        android.filterfw.core.Frame result = null;
        switch (format.getTarget()) {
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                {
                    android.filterfw.core.GLFrame glFrame = new android.filterfw.core.GLFrame(format, this, bindingType, bindingId);
                    glFrame.init(getGLEnvironment());
                    result = glFrame;
                    break;
                }
            default :
                throw new java.lang.RuntimeException(("Attached frames are not supported for target type: " + android.filterfw.core.FrameFormat.targetToString(format.getTarget())) + "!");
        }
        return result;
    }

    private android.filterfw.core.Frame createNewFrame(android.filterfw.core.FrameFormat format) {
        android.filterfw.core.Frame result = null;
        switch (format.getTarget()) {
            case android.filterfw.core.FrameFormat.TARGET_SIMPLE :
                result = new android.filterfw.core.SimpleFrame(format, this);
                break;
            case android.filterfw.core.FrameFormat.TARGET_NATIVE :
                result = new android.filterfw.core.NativeFrame(format, this);
                break;
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                {
                    android.filterfw.core.GLFrame glFrame = new android.filterfw.core.GLFrame(format, this);
                    glFrame.init(getGLEnvironment());
                    result = glFrame;
                    break;
                }
            case android.filterfw.core.FrameFormat.TARGET_VERTEXBUFFER :
                {
                    result = new android.filterfw.core.VertexFrame(format, this);
                    break;
                }
            default :
                throw new java.lang.RuntimeException(("Unsupported frame target type: " + android.filterfw.core.FrameFormat.targetToString(format.getTarget())) + "!");
        }
        return result;
    }

    @java.lang.Override
    public android.filterfw.core.Frame retainFrame(android.filterfw.core.Frame frame) {
        frame.incRefCount();
        return frame;
    }

    @java.lang.Override
    public android.filterfw.core.Frame releaseFrame(android.filterfw.core.Frame frame) {
        int refCount = frame.decRefCount();
        if ((refCount == 0) && frame.hasNativeAllocation()) {
            frame.releaseNativeAllocation();
            return null;
        } else
            if (refCount < 0) {
                throw new java.lang.RuntimeException("Frame reference count dropped below 0!");
            }

        return frame;
    }
}

