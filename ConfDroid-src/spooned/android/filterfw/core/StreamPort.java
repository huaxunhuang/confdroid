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
public class StreamPort extends android.filterfw.core.InputPort {
    private android.filterfw.core.Frame mFrame;

    private boolean mPersistent;

    public StreamPort(android.filterfw.core.Filter filter, java.lang.String name) {
        super(filter, name);
    }

    @java.lang.Override
    public void clear() {
        if (mFrame != null) {
            mFrame.release();
            mFrame = null;
        }
    }

    @java.lang.Override
    public void setFrame(android.filterfw.core.Frame frame) {
        assignFrame(frame, true);
    }

    @java.lang.Override
    public void pushFrame(android.filterfw.core.Frame frame) {
        assignFrame(frame, false);
    }

    protected synchronized void assignFrame(android.filterfw.core.Frame frame, boolean persistent) {
        assertPortIsOpen();
        checkFrameType(frame, persistent);
        if (persistent) {
            if (mFrame != null) {
                mFrame.release();
            }
        } else
            if (mFrame != null) {
                throw new java.lang.RuntimeException(("Attempting to push more than one frame on port: " + this) + "!");
            }

        mFrame = frame.retain();
        mFrame.markReadOnly();
        mPersistent = persistent;
    }

    @java.lang.Override
    public synchronized android.filterfw.core.Frame pullFrame() {
        // Make sure we have a frame
        if (mFrame == null) {
            throw new java.lang.RuntimeException(("No frame available to pull on port: " + this) + "!");
        }
        // Return a retained result
        android.filterfw.core.Frame result = mFrame;
        if (mPersistent) {
            mFrame.retain();
        } else {
            mFrame = null;
        }
        return result;
    }

    @java.lang.Override
    public synchronized boolean hasFrame() {
        return mFrame != null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "input " + super.toString();
    }

    @java.lang.Override
    public synchronized void transfer(android.filterfw.core.FilterContext context) {
        if (mFrame != null) {
            checkFrameManager(mFrame, context);
        }
    }
}

