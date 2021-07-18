/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.net.sip;


/**
 * Adapter class for {@link ISipSessionListener}. Default implementation of all
 * callback methods is no-op.
 *
 * @unknown 
 */
public class SipSessionAdapter extends android.net.sip.ISipSessionListener.Stub {
    public void onCalling(android.net.sip.ISipSession session) {
    }

    public void onRinging(android.net.sip.ISipSession session, android.net.sip.SipProfile caller, java.lang.String sessionDescription) {
    }

    public void onRingingBack(android.net.sip.ISipSession session) {
    }

    public void onCallEstablished(android.net.sip.ISipSession session, java.lang.String sessionDescription) {
    }

    public void onCallEnded(android.net.sip.ISipSession session) {
    }

    public void onCallBusy(android.net.sip.ISipSession session) {
    }

    public void onCallTransferring(android.net.sip.ISipSession session, java.lang.String sessionDescription) {
    }

    public void onCallChangeFailed(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
    }

    public void onError(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
    }

    public void onRegistering(android.net.sip.ISipSession session) {
    }

    public void onRegistrationDone(android.net.sip.ISipSession session, int duration) {
    }

    public void onRegistrationFailed(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
    }

    public void onRegistrationTimeout(android.net.sip.ISipSession session) {
    }
}

