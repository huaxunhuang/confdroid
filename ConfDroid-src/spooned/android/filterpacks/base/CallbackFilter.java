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
public class CallbackFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "listener", hasDefault = true)
    private android.filterfw.core.FilterContext.OnFrameReceivedListener mListener;

    @android.filterfw.core.GenerateFieldPort(name = "userData", hasDefault = true)
    private java.lang.Object mUserData;

    @android.filterfw.core.GenerateFinalPort(name = "callUiThread", hasDefault = true)
    private boolean mCallbacksOnUiThread = true;

    private android.os.Handler mUiThreadHandler;

    private class CallbackRunnable implements java.lang.Runnable {
        private android.filterfw.core.Filter mFilter;

        private android.filterfw.core.Frame mFrame;

        private java.lang.Object mUserData;

        private android.filterfw.core.FilterContext.OnFrameReceivedListener mListener;

        public CallbackRunnable(android.filterfw.core.FilterContext.OnFrameReceivedListener listener, android.filterfw.core.Filter filter, android.filterfw.core.Frame frame, java.lang.Object userData) {
            mListener = listener;
            mFilter = filter;
            mFrame = frame;
            mUserData = userData;
        }

        public void run() {
            mListener.onFrameReceived(mFilter, mFrame, mUserData);
            mFrame.release();
        }
    }

    public CallbackFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addInputPort("frame");
    }

    public void prepare(android.filterfw.core.FilterContext context) {
        if (mCallbacksOnUiThread) {
            mUiThreadHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        }
    }

    public void process(android.filterfw.core.FilterContext context) {
        // Get frame and forward to listener
        final android.filterfw.core.Frame input = pullInput("frame");
        if (mListener != null) {
            if (mCallbacksOnUiThread) {
                input.retain();
                android.filterpacks.base.CallbackFilter.CallbackRunnable uiRunnable = new android.filterpacks.base.CallbackFilter.CallbackRunnable(mListener, this, input, mUserData);
                if (!mUiThreadHandler.post(uiRunnable)) {
                    throw new java.lang.RuntimeException("Unable to send callback to UI thread!");
                }
            } else {
                mListener.onFrameReceived(this, input, mUserData);
            }
        } else {
            throw new java.lang.RuntimeException("CallbackFilter received frame, but no listener set!");
        }
    }
}

