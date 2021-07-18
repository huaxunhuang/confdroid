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
package android.support.v4.content;


/**
 * Helper to register for and send broadcasts of Intents to local objects
 * within your process.  This has a number of advantages over sending
 * global broadcasts with {@link android.content.Context#sendBroadcast}:
 * <ul>
 * <li> You know that the data you are broadcasting won't leave your app, so
 * don't need to worry about leaking private data.
 * <li> It is not possible for other applications to send these broadcasts to
 * your app, so you don't need to worry about having security holes they can
 * exploit.
 * <li> It is more efficient than sending a global broadcast through the
 * system.
 * </ul>
 */
public final class LocalBroadcastManager {
    private static class ReceiverRecord {
        final android.content.IntentFilter filter;

        final android.content.BroadcastReceiver receiver;

        boolean broadcasting;

        ReceiverRecord(android.content.IntentFilter _filter, android.content.BroadcastReceiver _receiver) {
            filter = _filter;
            receiver = _receiver;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
            builder.append("Receiver{");
            builder.append(receiver);
            builder.append(" filter=");
            builder.append(filter);
            builder.append("}");
            return builder.toString();
        }
    }

    private static class BroadcastRecord {
        final android.content.Intent intent;

        final java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> receivers;

        BroadcastRecord(android.content.Intent _intent, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> _receivers) {
            intent = _intent;
            receivers = _receivers;
        }
    }

    private static final java.lang.String TAG = "LocalBroadcastManager";

    private static final boolean DEBUG = false;

    private final android.content.Context mAppContext;

    private final java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>> mReceivers = new java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>>();

    private final java.util.HashMap<java.lang.String, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord>> mActions = new java.util.HashMap<java.lang.String, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord>>();

    private final java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.BroadcastRecord> mPendingBroadcasts = new java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.BroadcastRecord>();

    static final int MSG_EXEC_PENDING_BROADCASTS = 1;

    private final android.os.Handler mHandler;

    private static final java.lang.Object mLock = new java.lang.Object();

    private static android.support.v4.content.LocalBroadcastManager mInstance;

    public static android.support.v4.content.LocalBroadcastManager getInstance(android.content.Context context) {
        synchronized(android.support.v4.content.LocalBroadcastManager.mLock) {
            if (android.support.v4.content.LocalBroadcastManager.mInstance == null) {
                android.support.v4.content.LocalBroadcastManager.mInstance = new android.support.v4.content.LocalBroadcastManager(context.getApplicationContext());
            }
            return android.support.v4.content.LocalBroadcastManager.mInstance;
        }
    }

    private LocalBroadcastManager(android.content.Context context) {
        mAppContext = context;
        mHandler = new android.os.Handler(context.getMainLooper()) {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case android.support.v4.content.LocalBroadcastManager.MSG_EXEC_PENDING_BROADCASTS :
                        executePendingBroadcasts();
                        break;
                    default :
                        super.handleMessage(msg);
                }
            }
        };
    }

    /**
     * Register a receive for any local broadcasts that match the given IntentFilter.
     *
     * @param receiver
     * 		The BroadcastReceiver to handle the broadcast.
     * @param filter
     * 		Selects the Intent broadcasts to be received.
     * @see #unregisterReceiver
     */
    public void registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        synchronized(mReceivers) {
            android.support.v4.content.LocalBroadcastManager.ReceiverRecord entry = new android.support.v4.content.LocalBroadcastManager.ReceiverRecord(filter, receiver);
            java.util.ArrayList<android.content.IntentFilter> filters = mReceivers.get(receiver);
            if (filters == null) {
                filters = new java.util.ArrayList<android.content.IntentFilter>(1);
                mReceivers.put(receiver, filters);
            }
            filters.add(filter);
            for (int i = 0; i < filter.countActions(); i++) {
                java.lang.String action = filter.getAction(i);
                java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> entries = mActions.get(action);
                if (entries == null) {
                    entries = new java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord>(1);
                    mActions.put(action, entries);
                }
                entries.add(entry);
            }
        }
    }

    /**
     * Unregister a previously registered BroadcastReceiver.  <em>All</em>
     * filters that have been registered for this BroadcastReceiver will be
     * removed.
     *
     * @param receiver
     * 		The BroadcastReceiver to unregister.
     * @see #registerReceiver
     */
    public void unregisterReceiver(android.content.BroadcastReceiver receiver) {
        synchronized(mReceivers) {
            java.util.ArrayList<android.content.IntentFilter> filters = mReceivers.remove(receiver);
            if (filters == null) {
                return;
            }
            for (int i = 0; i < filters.size(); i++) {
                android.content.IntentFilter filter = filters.get(i);
                for (int j = 0; j < filter.countActions(); j++) {
                    java.lang.String action = filter.getAction(j);
                    java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> receivers = mActions.get(action);
                    if (receivers != null) {
                        for (int k = 0; k < receivers.size(); k++) {
                            if (receivers.get(k).receiver == receiver) {
                                receivers.remove(k);
                                k--;
                            }
                        }
                        if (receivers.size() <= 0) {
                            mActions.remove(action);
                        }
                    }
                }
            }
        }
    }

    /**
     * Broadcast the given intent to all interested BroadcastReceivers.  This
     * call is asynchronous; it returns immediately, and you will continue
     * executing while the receivers are run.
     *
     * @param intent
     * 		The Intent to broadcast; all receivers matching this
     * 		Intent will receive the broadcast.
     * @see #registerReceiver
     */
    public boolean sendBroadcast(android.content.Intent intent) {
        synchronized(mReceivers) {
            final java.lang.String action = intent.getAction();
            final java.lang.String type = intent.resolveTypeIfNeeded(mAppContext.getContentResolver());
            final android.net.Uri data = intent.getData();
            final java.lang.String scheme = intent.getScheme();
            final java.util.Set<java.lang.String> categories = intent.getCategories();
            final boolean debug = android.support.v4.content.LocalBroadcastManager.DEBUG || ((intent.getFlags() & android.content.Intent.FLAG_DEBUG_LOG_RESOLUTION) != 0);
            if (debug)
                android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, (((("Resolving type " + type) + " scheme ") + scheme) + " of intent ") + intent);

            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> entries = mActions.get(intent.getAction());
            if (entries != null) {
                if (debug)
                    android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, "Action list: " + entries);

                java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord> receivers = null;
                for (int i = 0; i < entries.size(); i++) {
                    android.support.v4.content.LocalBroadcastManager.ReceiverRecord receiver = entries.get(i);
                    if (debug)
                        android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, "Matching against filter " + receiver.filter);

                    if (receiver.broadcasting) {
                        if (debug) {
                            android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, "  Filter's target already added");
                        }
                        continue;
                    }
                    int match = receiver.filter.match(action, type, scheme, data, categories, "LocalBroadcastManager");
                    if (match >= 0) {
                        if (debug)
                            android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, "  Filter matched!  match=0x" + java.lang.Integer.toHexString(match));

                        if (receivers == null) {
                            receivers = new java.util.ArrayList<android.support.v4.content.LocalBroadcastManager.ReceiverRecord>();
                        }
                        receivers.add(receiver);
                        receiver.broadcasting = true;
                    } else {
                        if (debug) {
                            java.lang.String reason;
                            switch (match) {
                                case android.content.IntentFilter.NO_MATCH_ACTION :
                                    reason = "action";
                                    break;
                                case android.content.IntentFilter.NO_MATCH_CATEGORY :
                                    reason = "category";
                                    break;
                                case android.content.IntentFilter.NO_MATCH_DATA :
                                    reason = "data";
                                    break;
                                case android.content.IntentFilter.NO_MATCH_TYPE :
                                    reason = "type";
                                    break;
                                default :
                                    reason = "unknown reason";
                                    break;
                            }
                            android.util.Log.v(android.support.v4.content.LocalBroadcastManager.TAG, "  Filter did not match: " + reason);
                        }
                    }
                }
                if (receivers != null) {
                    for (int i = 0; i < receivers.size(); i++) {
                        receivers.get(i).broadcasting = false;
                    }
                    mPendingBroadcasts.add(new android.support.v4.content.LocalBroadcastManager.BroadcastRecord(intent, receivers));
                    if (!mHandler.hasMessages(android.support.v4.content.LocalBroadcastManager.MSG_EXEC_PENDING_BROADCASTS)) {
                        mHandler.sendEmptyMessage(android.support.v4.content.LocalBroadcastManager.MSG_EXEC_PENDING_BROADCASTS);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Like {@link #sendBroadcast(Intent)}, but if there are any receivers for
     * the Intent this function will block and immediately dispatch them before
     * returning.
     */
    public void sendBroadcastSync(android.content.Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    private void executePendingBroadcasts() {
        while (true) {
            android.support.v4.content.LocalBroadcastManager.BroadcastRecord[] brs = null;
            synchronized(mReceivers) {
                final int N = mPendingBroadcasts.size();
                if (N <= 0) {
                    return;
                }
                brs = new android.support.v4.content.LocalBroadcastManager.BroadcastRecord[N];
                mPendingBroadcasts.toArray(brs);
                mPendingBroadcasts.clear();
            }
            for (int i = 0; i < brs.length; i++) {
                android.support.v4.content.LocalBroadcastManager.BroadcastRecord br = brs[i];
                for (int j = 0; j < br.receivers.size(); j++) {
                    br.receivers.get(j).receiver.onReceive(mAppContext, br.intent);
                }
            }
        } 
    }
}

