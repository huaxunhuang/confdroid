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
public class RegistrantList {
    java.util.ArrayList registrants = new java.util.ArrayList();// of Registrant


    public synchronized void add(android.os.Handler h, int what, java.lang.Object obj) {
        add(new android.os.Registrant(h, what, obj));
    }

    public synchronized void addUnique(android.os.Handler h, int what, java.lang.Object obj) {
        // if the handler is already in the registrant list, remove it
        remove(h);
        add(new android.os.Registrant(h, what, obj));
    }

    public synchronized void add(android.os.Registrant r) {
        removeCleared();
        registrants.add(r);
    }

    public synchronized void removeCleared() {
        for (int i = registrants.size() - 1; i >= 0; i--) {
            android.os.Registrant r = ((android.os.Registrant) (registrants.get(i)));
            if (r.refH == null) {
                registrants.remove(i);
            }
        }
    }

    public synchronized int size() {
        return registrants.size();
    }

    public synchronized java.lang.Object get(int index) {
        return registrants.get(index);
    }

    private synchronized void internalNotifyRegistrants(java.lang.Object result, java.lang.Throwable exception) {
        for (int i = 0, s = registrants.size(); i < s; i++) {
            android.os.Registrant r = ((android.os.Registrant) (registrants.get(i)));
            r.internalNotifyRegistrant(result, exception);
        }
    }

    /* synchronized */
    public void notifyRegistrants() {
        internalNotifyRegistrants(null, null);
    }

    /* synchronized */
    public void notifyException(java.lang.Throwable exception) {
        internalNotifyRegistrants(null, exception);
    }

    /* synchronized */
    public void notifyResult(java.lang.Object result) {
        internalNotifyRegistrants(result, null);
    }

    /* synchronized */
    public void notifyRegistrants(android.os.AsyncResult ar) {
        internalNotifyRegistrants(ar.result, ar.exception);
    }

    public synchronized void remove(android.os.Handler h) {
        for (int i = 0, s = registrants.size(); i < s; i++) {
            android.os.Registrant r = ((android.os.Registrant) (registrants.get(i)));
            android.os.Handler rh;
            rh = r.getHandler();
            /* Clean up both the requested registrant and
            any now-collected registrants
             */
            if ((rh == null) || (rh == h)) {
                r.clear();
            }
        }
        removeCleared();
    }
}

