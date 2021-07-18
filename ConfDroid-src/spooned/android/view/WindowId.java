/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.view;


/**
 * Safe identifier for a window.  This currently allows you to retrieve and observe
 * the input focus state of the window.  Most applications will
 * not use this, instead relying on the simpler (and more efficient) methods available
 * on {@link View}.  This classes is useful when window input interactions need to be
 * done across processes: the class itself is a Parcelable that can be passed to other
 * processes for them to interact with your window, and it provides a limited safe API
 * that doesn't allow the other process to negatively harm your window.
 */
public class WindowId implements android.os.Parcelable {
    @android.annotation.NonNull
    private final android.view.IWindowId mToken;

    /**
     * Subclass for observing changes to the focus state of an {@link WindowId}.
     * You should use the same instance of this class for observing multiple
     * {@link WindowId} objects, since this class is fairly heavy-weight -- the
     * base class includes all of the mechanisms for connecting to and receiving updates
     * from the window.
     */
    public static abstract class FocusObserver {
        final IWindowFocusObserver.Stub mIObserver = new android.view.IWindowFocusObserver.Stub() {
            @java.lang.Override
            public void focusGained(android.os.IBinder inputToken) {
                android.view.WindowId token;
                synchronized(mRegistrations) {
                    token = mRegistrations.get(inputToken);
                }
                if (mHandler != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(1, token));
                } else {
                    onFocusGained(token);
                }
            }

            @java.lang.Override
            public void focusLost(android.os.IBinder inputToken) {
                android.view.WindowId token;
                synchronized(mRegistrations) {
                    token = mRegistrations.get(inputToken);
                }
                if (mHandler != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(2, token));
                } else {
                    onFocusLost(token);
                }
            }
        };

        final java.util.HashMap<android.os.IBinder, android.view.WindowId> mRegistrations = new java.util.HashMap<>();

        class H extends android.os.Handler {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1 :
                        onFocusGained(((android.view.WindowId) (msg.obj)));
                        break;
                    case 2 :
                        onFocusLost(((android.view.WindowId) (msg.obj)));
                        break;
                    default :
                        super.handleMessage(msg);
                }
            }
        }

        final android.os.Handler mHandler;

        /**
         * Construct a new observer.  This observer will be configured so that all
         * of its callbacks are dispatched on the current calling thread.
         */
        public FocusObserver() {
            mHandler = new android.view.WindowId.FocusObserver.H();
        }

        /**
         * Called when one of the monitored windows gains input focus.
         */
        public abstract void onFocusGained(android.view.WindowId token);

        /**
         * Called when one of the monitored windows loses input focus.
         */
        public abstract void onFocusLost(android.view.WindowId token);
    }

    /**
     * Retrieve the current focus state of the associated window.
     */
    public boolean isFocused() {
        try {
            return mToken.isFocused();
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * Start monitoring for changes in the focus state of the window.
     */
    public void registerFocusObserver(android.view.WindowId.FocusObserver observer) {
        synchronized(observer.mRegistrations) {
            if (observer.mRegistrations.containsKey(mToken.asBinder())) {
                throw new java.lang.IllegalStateException("Focus observer already registered with input token");
            }
            observer.mRegistrations.put(mToken.asBinder(), this);
            try {
                mToken.registerFocusObserver(observer.mIObserver);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Stop monitoring changes in the focus state of the window.
     */
    public void unregisterFocusObserver(android.view.WindowId.FocusObserver observer) {
        synchronized(observer.mRegistrations) {
            if (observer.mRegistrations.remove(mToken.asBinder()) == null) {
                throw new java.lang.IllegalStateException("Focus observer not registered with input token");
            }
            try {
                mToken.unregisterFocusObserver(observer.mIObserver);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Comparison operator on two IntentSender objects, such that true
     * is returned then they both represent the same operation from the
     * same package.
     */
    @java.lang.Override
    public boolean equals(@android.annotation.Nullable
    java.lang.Object otherObj) {
        if (otherObj instanceof android.view.WindowId) {
            return equals(((android.view.WindowId) (otherObj)).mToken.asBinder());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return hashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("IntentSender{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(": ");
        sb.append(mToken.asBinder());
        sb.append('}');
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeStrongBinder(mToken.asBinder());
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.WindowId> CREATOR = new android.os.Parcelable.Creator<android.view.WindowId>() {
        @java.lang.Override
        public android.view.WindowId createFromParcel(android.os.Parcel in) {
            android.os.IBinder target = in.readStrongBinder();
            return target != null ? new android.view.WindowId(target) : null;
        }

        @java.lang.Override
        public android.view.WindowId[] newArray(int size) {
            return new android.view.WindowId[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.view.IWindowId getTarget() {
        return mToken;
    }

    /**
     *
     *
     * @unknown 
     */
    public WindowId(@android.annotation.NonNull
    android.view.IWindowId target) {
        mToken = target;
    }

    /**
     *
     *
     * @unknown 
     */
    public WindowId(@android.annotation.NonNull
    android.os.IBinder target) {
        mToken = IWindowId.Stub.asInterface(target);
    }
}

