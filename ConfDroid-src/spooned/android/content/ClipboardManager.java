/**
 * Copyright (c) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.content;


/**
 * Interface to the clipboard service, for placing and retrieving text in
 * the global clipboard.
 *
 * <p>
 * The ClipboardManager API itself is very simple: it consists of methods
 * to atomically get and set the current primary clipboard data.  That data
 * is expressed as a {@link ClipData} object, which defines the protocol
 * for data exchange between applications.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using the clipboard framework, read the
 * <a href="{@docRoot }guide/topics/clipboard/copy-paste.html">Copy and Paste</a>
 * developer guide.</p>
 * </div>
 */
@android.annotation.SystemService(android.content.Context.CLIPBOARD_SERVICE)
public class ClipboardManager extends android.text.ClipboardManager {
    private final android.content.Context mContext;

    private final android.os.Handler mHandler;

    private final android.content.IClipboard mService;

    private final java.util.ArrayList<android.content.ClipboardManager.OnPrimaryClipChangedListener> mPrimaryClipChangedListeners = new java.util.ArrayList<android.content.ClipboardManager.OnPrimaryClipChangedListener>();

    private final IOnPrimaryClipChangedListener.Stub mPrimaryClipChangedServiceListener = new android.content.IOnPrimaryClipChangedListener.Stub() {
        @java.lang.Override
        public void dispatchPrimaryClipChanged() {
            mHandler.post(() -> {
                reportPrimaryClipChanged();
            });
        }
    };

    /**
     * Defines a listener callback that is invoked when the primary clip on the clipboard changes.
     * Objects that want to register a listener call
     * {@link android.content.ClipboardManager#addPrimaryClipChangedListener(OnPrimaryClipChangedListener)
     * addPrimaryClipChangedListener()} with an
     * object that implements OnPrimaryClipChangedListener.
     */
    public interface OnPrimaryClipChangedListener {
        /**
         * Callback that is invoked by {@link android.content.ClipboardManager} when the primary
         * clip changes.
         */
        void onPrimaryClipChanged();
    }

    /**
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage
    public ClipboardManager(android.content.Context context, android.os.Handler handler) throws android.os.ServiceManager.ServiceNotFoundException {
        mContext = context;
        mHandler = handler;
        mService = IClipboard.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow(android.content.Context.CLIPBOARD_SERVICE));
    }

    /**
     * Sets the current primary clip on the clipboard.  This is the clip that
     * is involved in normal cut and paste operations.
     *
     * @param clip
     * 		The clipped data item to set.
     * @see #getPrimaryClip()
     * @see #clearPrimaryClip()
     */
    public void setPrimaryClip(@android.annotation.NonNull
    android.content.ClipData clip) {
        try {
            com.android.internal.util.Preconditions.checkNotNull(clip);
            clip.prepareToLeaveProcess(true);
            mService.setPrimaryClip(clip, mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Clears any current primary clip on the clipboard.
     *
     * @see #setPrimaryClip(ClipData)
     */
    public void clearPrimaryClip() {
        try {
            mService.clearPrimaryClip(mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the current primary clip on the clipboard.
     *
     * <em>If the application is not the default IME or does not have input focus this return
     * {@code null}.</em>
     *
     * @see #setPrimaryClip(ClipData)
     */
    @android.annotation.Nullable
    public android.content.ClipData getPrimaryClip() {
        try {
            return mService.getPrimaryClip(mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a description of the current primary clip on the clipboard
     * but not a copy of its data.
     *
     * <em>If the application is not the default IME or does not have input focus this return
     * {@code null}.</em>
     *
     * @see #setPrimaryClip(ClipData)
     */
    @android.annotation.Nullable
    public android.content.ClipDescription getPrimaryClipDescription() {
        try {
            return mService.getPrimaryClipDescription(mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if there is currently a primary clip on the clipboard.
     *
     * <em>If the application is not the default IME or the does not have input focus this will
     * return {@code false}.</em>
     */
    public boolean hasPrimaryClip() {
        try {
            return mService.hasPrimaryClip(mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addPrimaryClipChangedListener(android.content.ClipboardManager.OnPrimaryClipChangedListener what) {
        synchronized(mPrimaryClipChangedListeners) {
            if (mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    mService.addPrimaryClipChangedListener(mPrimaryClipChangedServiceListener, mContext.getOpPackageName(), mContext.getUserId());
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            mPrimaryClipChangedListeners.add(what);
        }
    }

    public void removePrimaryClipChangedListener(android.content.ClipboardManager.OnPrimaryClipChangedListener what) {
        synchronized(mPrimaryClipChangedListeners) {
            mPrimaryClipChangedListeners.remove(what);
            if (mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    mService.removePrimaryClipChangedListener(mPrimaryClipChangedServiceListener, mContext.getOpPackageName(), mContext.getUserId());
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    /**
     *
     *
     * @deprecated Use {@link #getPrimaryClip()} instead.  This retrieves
    the primary clip and tries to coerce it to a string.
     */
    @java.lang.Deprecated
    public java.lang.CharSequence getText() {
        android.content.ClipData clip = getPrimaryClip();
        if ((clip != null) && (clip.getItemCount() > 0)) {
            return clip.getItemAt(0).coerceToText(mContext);
        }
        return null;
    }

    /**
     *
     *
     * @deprecated Use {@link #setPrimaryClip(ClipData)} instead.  This
    creates a ClippedItem holding the given text and sets it as the
    primary clip.  It has no label or icon.
     */
    @java.lang.Deprecated
    public void setText(java.lang.CharSequence text) {
        setPrimaryClip(android.content.ClipData.newPlainText(null, text));
    }

    /**
     *
     *
     * @deprecated Use {@link #hasPrimaryClip()} instead.
     */
    @java.lang.Deprecated
    public boolean hasText() {
        try {
            return mService.hasClipboardText(mContext.getOpPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @android.annotation.UnsupportedAppUsage
    void reportPrimaryClipChanged() {
        java.lang.Object[] listeners;
        synchronized(mPrimaryClipChangedListeners) {
            final int N = mPrimaryClipChangedListeners.size();
            if (N <= 0) {
                return;
            }
            listeners = mPrimaryClipChangedListeners.toArray();
        }
        for (int i = 0; i < listeners.length; i++) {
            ((android.content.ClipboardManager.OnPrimaryClipChangedListener) (listeners[i])).onPrimaryClipChanged();
        }
    }
}

