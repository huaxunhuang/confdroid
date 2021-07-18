/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.printservice.recommendation;


/**
 * Base class for the print service recommendation services.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class RecommendationService extends android.app.Service {
    private static final java.lang.String LOG_TAG = "PrintServiceRecS";

    /**
     * Used to push onConnect and onDisconnect on the main thread
     */
    private android.os.Handler mHandler;

    /**
     * The {@link Intent} action that must be declared as handled by a service in its manifest for
     * the system to recognize it as a print service recommendation service.
     */
    public static final java.lang.String SERVICE_INTERFACE = "android.printservice.recommendation.RecommendationService";

    /**
     * Registered callbacks, only modified on main thread
     */
    private android.printservice.recommendation.IRecommendationServiceCallbacks mCallbacks;

    @java.lang.Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        mHandler = new android.printservice.recommendation.RecommendationService.MyHandler();
    }

    /**
     * Update the print service recommendations.
     *
     * @param recommendations
     * 		The new set of recommendations
     */
    public final void updateRecommendations(@android.annotation.Nullable
    java.util.List<android.printservice.recommendation.RecommendationInfo> recommendations) {
        mHandler.obtainMessage(android.printservice.recommendation.RecommendationService.MyHandler.MSG_UPDATE, recommendations).sendToTarget();
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return new android.printservice.recommendation.IRecommendationService.Stub() {
            @java.lang.Override
            public void registerCallbacks(android.printservice.recommendation.IRecommendationServiceCallbacks callbacks) {
                // The callbacks come in order of the caller on oneway calls. Hence while the caller
                // cannot know at what time the connection is made, he can know the ordering of
                // connection and disconnection.
                // 
                // Similar he cannot know when the disconnection is processed, hence he has to
                // handle callbacks after calling disconnect.
                if (callbacks != null) {
                    mHandler.obtainMessage(android.printservice.recommendation.RecommendationService.MyHandler.MSG_CONNECT, callbacks).sendToTarget();
                } else {
                    mHandler.obtainMessage(android.printservice.recommendation.RecommendationService.MyHandler.MSG_DISCONNECT).sendToTarget();
                }
            }
        };
    }

    /**
     * Called when the client connects to the recommendation service.
     */
    public abstract void onConnected();

    /**
     * Called when the client disconnects from the recommendation service.
     */
    public abstract void onDisconnected();

    private class MyHandler extends android.os.Handler {
        static final int MSG_CONNECT = 1;

        static final int MSG_DISCONNECT = 2;

        static final int MSG_UPDATE = 3;

        MyHandler() {
            super(android.os.Looper.getMainLooper());
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.printservice.recommendation.RecommendationService.MyHandler.MSG_CONNECT :
                    mCallbacks = ((android.printservice.recommendation.IRecommendationServiceCallbacks) (msg.obj));
                    onConnected();
                    break;
                case android.printservice.recommendation.RecommendationService.MyHandler.MSG_DISCONNECT :
                    onDisconnected();
                    mCallbacks = null;
                    break;
                case android.printservice.recommendation.RecommendationService.MyHandler.MSG_UPDATE :
                    // Note that there might be a connection change in progress. In this case the
                    // message is handled as before the change. This is acceptable as the caller of
                    // the connection change has not guarantee when the connection change binder
                    // transaction is actually processed.
                    try {
                        mCallbacks.onRecommendationsUpdated(((java.util.List<android.printservice.recommendation.RecommendationInfo>) (msg.obj)));
                    } catch (android.os.RemoteException | java.lang.NullPointerException e) {
                        android.util.Log.e(android.printservice.recommendation.RecommendationService.LOG_TAG, "Could not update recommended services", e);
                    }
                    break;
            }
        }
    }
}

