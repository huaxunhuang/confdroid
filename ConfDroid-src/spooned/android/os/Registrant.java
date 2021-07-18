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
package android.os;


/**
 *
 *
 * @unknown 
 */
public class Registrant {
    public Registrant(android.os.Handler h, int what, java.lang.Object obj) {
        refH = new java.lang.ref.WeakReference(h);
        this.what = what;
        userObj = obj;
    }

    public void clear() {
        refH = null;
        userObj = null;
    }

    public void notifyRegistrant() {
        internalNotifyRegistrant(null, null);
    }

    public void notifyResult(java.lang.Object result) {
        internalNotifyRegistrant(result, null);
    }

    public void notifyException(java.lang.Throwable exception) {
        internalNotifyRegistrant(null, exception);
    }

    /**
     * This makes a copy of @param ar
     */
    public void notifyRegistrant(android.os.AsyncResult ar) {
        internalNotifyRegistrant(ar.result, ar.exception);
    }

    /* package */
    void internalNotifyRegistrant(java.lang.Object result, java.lang.Throwable exception) {
        android.os.Handler h = getHandler();
        if (h == null) {
            clear();
        } else {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = what;
            msg.obj = new android.os.AsyncResult(userObj, result, exception);
            h.sendMessage(msg);
        }
    }

    /**
     * NOTE: May return null if weak reference has been collected
     */
    public android.os.Message messageForRegistrant() {
        android.os.Handler h = getHandler();
        if (h == null) {
            clear();
            return null;
        } else {
            android.os.Message msg = h.obtainMessage();
            msg.what = what;
            msg.obj = userObj;
            return msg;
        }
    }

    public android.os.Handler getHandler() {
        if (refH == null)
            return null;

        return ((android.os.Handler) (refH.get()));
    }

    java.lang.ref.WeakReference refH;

    int what;

    java.lang.Object userObj;
}

