/**
 * Copyright (C) 2015 The Android Open Source Project
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


/**
 * Base class for implementing the resolver service.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class EphemeralResolverService extends android.app.Service {
    public static final java.lang.String EXTRA_RESOLVE_INFO = "android.app.extra.RESOLVE_INFO";

    public static final java.lang.String EXTRA_SEQUENCE = "android.app.extra.SEQUENCE";

    private static final java.lang.String EXTRA_PREFIX = "android.app.PREFIX";

    private android.os.Handler mHandler;

    /**
     * Called to retrieve resolve info for ephemeral applications.
     *
     * @param digestPrefix
     * 		The hash prefix of the ephemeral's domain.
     * @param prefixMask
     * 		A mask that was applied to each digest prefix. This should
     * 		be used when comparing against the digest prefixes as all bits might
     * 		not be set.
     */
    public abstract java.util.List<android.content.pm.EphemeralResolveInfo> onEphemeralResolveInfoList(int[] digestPrefix, int prefixMask);

    @java.lang.Override
    public final void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        mHandler = new android.app.EphemeralResolverService.ServiceHandler(base.getMainLooper());
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return new android.app.IEphemeralResolver.Stub() {
            @java.lang.Override
            public void getEphemeralResolveInfoList(android.os.IRemoteCallback callback, int[] digestPrefix, int prefixMask, int sequence) {
                final android.os.Message msg = mHandler.obtainMessage(android.app.EphemeralResolverService.ServiceHandler.MSG_GET_EPHEMERAL_RESOLVE_INFO, prefixMask, sequence, callback);
                final android.os.Bundle data = new android.os.Bundle();
                data.putIntArray(android.app.EphemeralResolverService.EXTRA_PREFIX, digestPrefix);
                msg.setData(data);
                msg.sendToTarget();
            }
        };
    }

    private final class ServiceHandler extends android.os.Handler {
        public static final int MSG_GET_EPHEMERAL_RESOLVE_INFO = 1;

        public ServiceHandler(android.os.Looper looper) {
            /* callback */
            /* async */
            super(looper, null, true);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message message) {
            final int action = message.what;
            switch (action) {
                case android.app.EphemeralResolverService.ServiceHandler.MSG_GET_EPHEMERAL_RESOLVE_INFO :
                    {
                        final android.os.IRemoteCallback callback = ((android.os.IRemoteCallback) (message.obj));
                        final int[] digestPrefix = message.getData().getIntArray(android.app.EphemeralResolverService.EXTRA_PREFIX);
                        final java.util.List<android.content.pm.EphemeralResolveInfo> resolveInfo = onEphemeralResolveInfoList(digestPrefix, message.arg1);
                        final android.os.Bundle data = new android.os.Bundle();
                        data.putInt(android.app.EphemeralResolverService.EXTRA_SEQUENCE, message.arg2);
                        data.putParcelableList(android.app.EphemeralResolverService.EXTRA_RESOLVE_INFO, resolveInfo);
                        try {
                            callback.sendResult(data);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    break;
                default :
                    {
                        throw new java.lang.IllegalArgumentException("Unknown message: " + action);
                    }
            }
        }
    }
}

