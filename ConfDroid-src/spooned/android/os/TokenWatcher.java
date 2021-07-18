/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.os;


/**
 * Helper class that helps you use IBinder objects as reference counted
 * tokens.  IBinders make good tokens because we find out when they are
 * removed
 */
public abstract class TokenWatcher {
    /**
     * Construct the TokenWatcher
     *
     * @param h
     * 		A handler to call {@link #acquired} and {@link #released}
     * 		on.  If you don't care, just call it like this, although your thread
     * 		will have to be a Looper thread.
     * 		<code>new TokenWatcher(new Handler())</code>
     * @param tag
     * 		A debugging tag for this TokenWatcher
     */
    public TokenWatcher(android.os.Handler h, java.lang.String tag) {
        mHandler = h;
        mTag = (tag != null) ? tag : "TokenWatcher";
    }

    /**
     * Called when the number of active tokens goes from 0 to 1.
     */
    public abstract void acquired();

    /**
     * Called when the number of active tokens goes from 1 to 0.
     */
    public abstract void released();

    /**
     * Record that this token has been acquired.  When acquire is called, and
     * the current count is 0, the acquired method is called on the given
     * handler.
     *
     * @param token
     * 		An IBinder object.  If this token has already been acquired,
     * 		no action is taken.
     * @param tag
     * 		A string used by the {@link #dump} method for debugging,
     * 		to see who has references.
     */
    public void acquire(android.os.IBinder token, java.lang.String tag) {
        synchronized(mTokens) {
            // explicitly checked to avoid bogus sendNotification calls because
            // of the WeakHashMap and the GC
            int oldSize = mTokens.size();
            android.os.TokenWatcher.Death d = new android.os.TokenWatcher.Death(token, tag);
            try {
                token.linkToDeath(d, 0);
            } catch (android.os.RemoteException e) {
                return;
            }
            mTokens.put(token, d);
            if ((oldSize == 0) && (!mAcquired)) {
                sendNotificationLocked(true);
                mAcquired = true;
            }
        }
    }

    public void cleanup(android.os.IBinder token, boolean unlink) {
        synchronized(mTokens) {
            android.os.TokenWatcher.Death d = mTokens.remove(token);
            if (unlink && (d != null)) {
                d.token.unlinkToDeath(d, 0);
                d.token = null;
            }
            if ((mTokens.size() == 0) && mAcquired) {
                sendNotificationLocked(false);
                mAcquired = false;
            }
        }
    }

    public void release(android.os.IBinder token) {
        cleanup(token, true);
    }

    public boolean isAcquired() {
        synchronized(mTokens) {
            return mAcquired;
        }
    }

    public void dump() {
        java.util.ArrayList<java.lang.String> a = dumpInternal();
        for (java.lang.String s : a) {
            android.util.Log.i(mTag, s);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        java.util.ArrayList<java.lang.String> a = dumpInternal();
        for (java.lang.String s : a) {
            pw.println(s);
        }
    }

    private java.util.ArrayList<java.lang.String> dumpInternal() {
        java.util.ArrayList<java.lang.String> a = new java.util.ArrayList<java.lang.String>();
        synchronized(mTokens) {
            java.util.Set<android.os.IBinder> keys = mTokens.keySet();
            a.add("Token count: " + mTokens.size());
            int i = 0;
            for (android.os.IBinder b : keys) {
                a.add((((("[" + i) + "] ") + mTokens.get(b).tag) + " - ") + b);
                i++;
            }
        }
        return a;
    }

    private java.lang.Runnable mNotificationTask = new java.lang.Runnable() {
        public void run() {
            int value;
            synchronized(mTokens) {
                value = mNotificationQueue;
                mNotificationQueue = -1;
            }
            if (value == 1) {
                acquired();
            } else
                if (value == 0) {
                    released();
                }

        }
    };

    private void sendNotificationLocked(boolean on) {
        int value = (on) ? 1 : 0;
        if (mNotificationQueue == (-1)) {
            // empty
            mNotificationQueue = value;
            mHandler.post(mNotificationTask);
        } else
            if (mNotificationQueue != value) {
                // it's a pair, so cancel it
                mNotificationQueue = -1;
                mHandler.removeCallbacks(mNotificationTask);
            }

        // else, same so do nothing -- maybe we should warn?
    }

    private class Death implements android.os.IBinder.DeathRecipient {
        android.os.IBinder token;

        java.lang.String tag;

        Death(android.os.IBinder token, java.lang.String tag) {
            this.token = token;
            this.tag = tag;
        }

        public void binderDied() {
            cleanup(token, false);
        }

        protected void finalize() throws java.lang.Throwable {
            try {
                if (token != null) {
                    android.util.Log.w(mTag, "cleaning up leaked reference: " + tag);
                    release(token);
                }
            } finally {
                super.finalize();
            }
        }
    }

    private java.util.WeakHashMap<android.os.IBinder, android.os.TokenWatcher.Death> mTokens = new java.util.WeakHashMap<android.os.IBinder, android.os.TokenWatcher.Death>();

    private android.os.Handler mHandler;

    private java.lang.String mTag;

    private int mNotificationQueue = -1;

    private volatile boolean mAcquired = false;
}

