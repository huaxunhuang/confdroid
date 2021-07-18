/**
 * Copyright (C) 2013 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public final class PrinterDiscoverySession {
    private static final java.lang.String LOG_TAG = "PrinterDiscoverySession";

    private static final int MSG_PRINTERS_ADDED = 1;

    private static final int MSG_PRINTERS_REMOVED = 2;

    private final java.util.LinkedHashMap<android.print.PrinterId, android.print.PrinterInfo> mPrinters = new java.util.LinkedHashMap<android.print.PrinterId, android.print.PrinterInfo>();

    private final android.print.IPrintManager mPrintManager;

    private final int mUserId;

    private final android.os.Handler mHandler;

    private android.print.IPrinterDiscoveryObserver mObserver;

    private android.print.PrinterDiscoverySession.OnPrintersChangeListener mListener;

    private boolean mIsPrinterDiscoveryStarted;

    public static interface OnPrintersChangeListener {
        public void onPrintersChanged();
    }

    PrinterDiscoverySession(android.print.IPrintManager printManager, android.content.Context context, int userId) {
        mPrintManager = printManager;
        mUserId = userId;
        mHandler = new android.print.PrinterDiscoverySession.SessionHandler(context.getMainLooper());
        mObserver = new android.print.PrinterDiscoverySession.PrinterDiscoveryObserver(this);
        try {
            mPrintManager.createPrinterDiscoverySession(mObserver, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error creating printer discovery session", re);
        }
    }

    public final void startPrinterDiscovery(@android.annotation.Nullable
    java.util.List<android.print.PrinterId> priorityList) {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring start printers discovery - session destroyed");
            return;
        }
        if (!mIsPrinterDiscoveryStarted) {
            mIsPrinterDiscoveryStarted = true;
            try {
                mPrintManager.startPrinterDiscovery(mObserver, priorityList, mUserId);
            } catch (android.os.RemoteException re) {
                android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error starting printer discovery", re);
            }
        }
    }

    public final void stopPrinterDiscovery() {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring stop printers discovery - session destroyed");
            return;
        }
        if (mIsPrinterDiscoveryStarted) {
            mIsPrinterDiscoveryStarted = false;
            try {
                mPrintManager.stopPrinterDiscovery(mObserver, mUserId);
            } catch (android.os.RemoteException re) {
                android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error stopping printer discovery", re);
            }
        }
    }

    public final void startPrinterStateTracking(@android.annotation.NonNull
    android.print.PrinterId printerId) {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring start printer state tracking - session destroyed");
            return;
        }
        try {
            mPrintManager.startPrinterStateTracking(printerId, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error starting printer state tracking", re);
        }
    }

    public final void stopPrinterStateTracking(@android.annotation.NonNull
    android.print.PrinterId printerId) {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring stop printer state tracking - session destroyed");
            return;
        }
        try {
            mPrintManager.stopPrinterStateTracking(printerId, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error stopping printer state tracking", re);
        }
    }

    public final void validatePrinters(java.util.List<android.print.PrinterId> printerIds) {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring validate printers - session destroyed");
            return;
        }
        try {
            mPrintManager.validatePrinters(printerIds, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error validating printers", re);
        }
    }

    public final void destroy() {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring destroy - session destroyed");
        }
        destroyNoCheck();
    }

    public final java.util.List<android.print.PrinterInfo> getPrinters() {
        if (isDestroyed()) {
            android.util.Log.w(android.print.PrinterDiscoverySession.LOG_TAG, "Ignoring get printers - session destroyed");
            return java.util.Collections.emptyList();
        }
        return new java.util.ArrayList<android.print.PrinterInfo>(mPrinters.values());
    }

    public final boolean isDestroyed() {
        android.print.PrinterDiscoverySession.throwIfNotCalledOnMainThread();
        return isDestroyedNoCheck();
    }

    public final boolean isPrinterDiscoveryStarted() {
        android.print.PrinterDiscoverySession.throwIfNotCalledOnMainThread();
        return mIsPrinterDiscoveryStarted;
    }

    public final void setOnPrintersChangeListener(android.print.PrinterDiscoverySession.OnPrintersChangeListener listener) {
        android.print.PrinterDiscoverySession.throwIfNotCalledOnMainThread();
        mListener = listener;
    }

    @java.lang.Override
    protected final void finalize() throws java.lang.Throwable {
        if (!isDestroyedNoCheck()) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Destroying leaked printer discovery session");
            destroyNoCheck();
        }
        super.finalize();
    }

    private boolean isDestroyedNoCheck() {
        return mObserver == null;
    }

    private void destroyNoCheck() {
        stopPrinterDiscovery();
        try {
            mPrintManager.destroyPrinterDiscoverySession(mObserver, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrinterDiscoverySession.LOG_TAG, "Error destroying printer discovery session", re);
        } finally {
            mObserver = null;
            mPrinters.clear();
        }
    }

    private void handlePrintersAdded(java.util.List<android.print.PrinterInfo> addedPrinters) {
        if (isDestroyed()) {
            return;
        }
        // No old printers - do not bother keeping their position.
        if (mPrinters.isEmpty()) {
            final int printerCount = addedPrinters.size();
            for (int i = 0; i < printerCount; i++) {
                android.print.PrinterInfo printer = addedPrinters.get(i);
                mPrinters.put(printer.getId(), printer);
            }
            notifyOnPrintersChanged();
            return;
        }
        // Add the printers to a map.
        android.util.ArrayMap<android.print.PrinterId, android.print.PrinterInfo> addedPrintersMap = new android.util.ArrayMap<android.print.PrinterId, android.print.PrinterInfo>();
        final int printerCount = addedPrinters.size();
        for (int i = 0; i < printerCount; i++) {
            android.print.PrinterInfo printer = addedPrinters.get(i);
            addedPrintersMap.put(printer.getId(), printer);
        }
        // Update printers we already have.
        for (android.print.PrinterId oldPrinterId : mPrinters.keySet()) {
            android.print.PrinterInfo updatedPrinter = addedPrintersMap.remove(oldPrinterId);
            if (updatedPrinter != null) {
                mPrinters.put(oldPrinterId, updatedPrinter);
            }
        }
        // Add the new printers, i.e. what is left.
        mPrinters.putAll(addedPrintersMap);
        // Announce the change.
        notifyOnPrintersChanged();
    }

    private void handlePrintersRemoved(java.util.List<android.print.PrinterId> printerIds) {
        if (isDestroyed()) {
            return;
        }
        boolean printersChanged = false;
        final int removedPrinterIdCount = printerIds.size();
        for (int i = 0; i < removedPrinterIdCount; i++) {
            android.print.PrinterId removedPrinterId = printerIds.get(i);
            if (mPrinters.remove(removedPrinterId) != null) {
                printersChanged = true;
            }
        }
        if (printersChanged) {
            notifyOnPrintersChanged();
        }
    }

    private void notifyOnPrintersChanged() {
        if (mListener != null) {
            mListener.onPrintersChanged();
        }
    }

    private static void throwIfNotCalledOnMainThread() {
        if (!android.os.Looper.getMainLooper().isCurrentThread()) {
            throw new java.lang.IllegalAccessError("must be called from the main thread");
        }
    }

    private final class SessionHandler extends android.os.Handler {
        public SessionHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case android.print.PrinterDiscoverySession.MSG_PRINTERS_ADDED :
                    {
                        java.util.List<android.print.PrinterInfo> printers = ((java.util.List<android.print.PrinterInfo>) (message.obj));
                        handlePrintersAdded(printers);
                    }
                    break;
                case android.print.PrinterDiscoverySession.MSG_PRINTERS_REMOVED :
                    {
                        java.util.List<android.print.PrinterId> printerIds = ((java.util.List<android.print.PrinterId>) (message.obj));
                        handlePrintersRemoved(printerIds);
                    }
                    break;
            }
        }
    }

    public static final class PrinterDiscoveryObserver extends android.print.IPrinterDiscoveryObserver.Stub {
        private final java.lang.ref.WeakReference<android.print.PrinterDiscoverySession> mWeakSession;

        public PrinterDiscoveryObserver(android.print.PrinterDiscoverySession session) {
            mWeakSession = new java.lang.ref.WeakReference<android.print.PrinterDiscoverySession>(session);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("rawtypes")
        public void onPrintersAdded(android.content.pm.ParceledListSlice printers) {
            android.print.PrinterDiscoverySession session = mWeakSession.get();
            if (session != null) {
                session.mHandler.obtainMessage(android.print.PrinterDiscoverySession.MSG_PRINTERS_ADDED, printers.getList()).sendToTarget();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("rawtypes")
        public void onPrintersRemoved(android.content.pm.ParceledListSlice printerIds) {
            android.print.PrinterDiscoverySession session = mWeakSession.get();
            if (session != null) {
                session.mHandler.obtainMessage(android.print.PrinterDiscoverySession.MSG_PRINTERS_REMOVED, printerIds.getList()).sendToTarget();
            }
        }
    }
}

