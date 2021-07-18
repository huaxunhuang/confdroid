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
package android.app;


class ReceiverRestrictedContext extends android.content.ContextWrapper {
    ReceiverRestrictedContext(android.content.Context base) {
        super(base);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        return registerReceiver(receiver, filter, null, null);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        if (receiver == null) {
            // Allow retrieving current sticky broadcast; this is safe since we
            // aren't actually registering a receiver.
            return super.registerReceiver(null, filter, broadcastPermission, scheduler);
        } else {
            throw new android.content.ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to register to receive intents");
        }
    }

    @java.lang.Override
    public android.content.Intent registerReceiverAsUser(android.content.BroadcastReceiver receiver, android.os.UserHandle user, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        if (receiver == null) {
            // Allow retrieving current sticky broadcast; this is safe since we
            // aren't actually registering a receiver.
            return super.registerReceiverAsUser(null, user, filter, broadcastPermission, scheduler);
        } else {
            throw new android.content.ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to register to receive intents");
        }
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, android.content.ServiceConnection conn, int flags) {
        throw new android.content.ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to bind to services");
    }
}

