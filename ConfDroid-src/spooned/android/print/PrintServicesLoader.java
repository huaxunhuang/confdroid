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
package android.print;


/**
 * Loader for the list of print services. Can be parametrized to select a subset.
 *
 * @unknown 
 */
public class PrintServicesLoader extends android.content.Loader<java.util.List<android.printservice.PrintServiceInfo>> {
    /**
     * What type of services to load.
     */
    private final int mSelectionFlags;

    /**
     * The print manager to be used by this object
     */
    @android.annotation.NonNull
    private final android.print.PrintManager mPrintManager;

    /**
     * Handler to sequentialize the delivery of the results to the main thread
     */
    @android.annotation.NonNull
    private final android.os.Handler mHandler;

    /**
     * Listens for updates to the data from the platform
     */
    private android.print.PrintManager.PrintServicesChangeListener mListener;

    /**
     * Create a new PrintServicesLoader.
     *
     * @param printManager
     * 		The print manager supplying the data
     * @param context
     * 		Context of the using object
     * @param selectionFlags
     * 		What type of services to load.
     */
    public PrintServicesLoader(@android.annotation.NonNull
    android.print.PrintManager printManager, @android.annotation.NonNull
    android.content.Context context, int selectionFlags) {
        super(com.android.internal.util.Preconditions.checkNotNull(context));
        mHandler = new android.print.PrintServicesLoader.MyHandler();
        mPrintManager = com.android.internal.util.Preconditions.checkNotNull(printManager);
        mSelectionFlags = com.android.internal.util.Preconditions.checkFlagsArgument(selectionFlags, android.print.PrintManager.ALL_SERVICES);
    }

    @java.lang.Override
    protected void onForceLoad() {
        queueNewResult();
    }

    /**
     * Read the print services and queue it to be delivered on the main thread.
     */
    private void queueNewResult() {
        android.os.Message m = mHandler.obtainMessage(0);
        m.obj = mPrintManager.getPrintServices(mSelectionFlags);
        mHandler.sendMessage(m);
    }

    @java.lang.Override
    protected void onStartLoading() {
        mListener = new android.print.PrintManager.PrintServicesChangeListener() {
            @java.lang.Override
            public void onPrintServicesChanged() {
                queueNewResult();
            }
        };
        mPrintManager.addPrintServicesChangeListener(mListener);
        // Immediately deliver a result
        deliverResult(mPrintManager.getPrintServices(mSelectionFlags));
    }

    @java.lang.Override
    protected void onStopLoading() {
        if (mListener != null) {
            mPrintManager.removePrintServicesChangeListener(mListener);
            mListener = null;
        }
        mHandler.removeMessages(0);
    }

    @java.lang.Override
    protected void onReset() {
        onStopLoading();
    }

    /**
     * Handler to sequentialize all the updates to the main thread.
     */
    private class MyHandler extends android.os.Handler {
        /**
         * Create a new handler on the main thread.
         */
        public MyHandler() {
            super(getContext().getMainLooper());
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (isStarted()) {
                deliverResult(((java.util.List<android.printservice.PrintServiceInfo>) (msg.obj)));
            }
        }
    }
}

