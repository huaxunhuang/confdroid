/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bordeaux.services;


/**
 * {@hide }
 * This is used to provide a convenience to access the actual remote running
 * service.
 * TODO: Eventally the remote service will be running in the system server, and
 * this will need to be served as a stub for the remote running service. And
 * extends from IBordeauxManager.stub
 */
public class BordeauxManagerService {
    private static final java.lang.String TAG = "BordeauxMangerService";

    private static android.bordeaux.services.IBordeauxService mService = null;

    private static android.bordeaux.services.ILearning_StochasticLinearRanker mRanker = null;

    private static android.bordeaux.services.IAggregatorManager mAggregatorManager = null;

    private static android.bordeaux.services.IPredictor mPredictor = null;

    private static android.bordeaux.services.ILearning_MulticlassPA mClassifier = null;

    private static boolean mStarted = false;

    public BordeauxManagerService() {
    }

    private static synchronized void bindServices(android.content.Context context) {
        if (android.bordeaux.services.BordeauxManagerService.mStarted)
            return;

        context.bindService(new android.content.Intent(android.bordeaux.services.IBordeauxService.class.getName()), android.bordeaux.services.BordeauxManagerService.mConnection, android.content.Context.BIND_AUTO_CREATE);
        android.bordeaux.services.BordeauxManagerService.mStarted = true;
    }

    // Call the release, before the Context gets destroyed.
    public static synchronized void release(android.content.Context context) {
        if (android.bordeaux.services.BordeauxManagerService.mStarted && (android.bordeaux.services.BordeauxManagerService.mConnection != null)) {
            context.unbindService(android.bordeaux.services.BordeauxManagerService.mConnection);
            android.bordeaux.services.BordeauxManagerService.mService = null;
            android.bordeaux.services.BordeauxManagerService.mStarted = false;
        }
    }

    public static synchronized android.bordeaux.services.IBordeauxService getService(android.content.Context context) {
        if (android.bordeaux.services.BordeauxManagerService.mService == null)
            android.bordeaux.services.BordeauxManagerService.bindServices(context);

        return android.bordeaux.services.BordeauxManagerService.mService;
    }

    public static synchronized android.bordeaux.services.IAggregatorManager getAggregatorManager(android.content.Context context) {
        if (android.bordeaux.services.BordeauxManagerService.mService == null) {
            android.bordeaux.services.BordeauxManagerService.bindServices(context);
            return null;
        }
        try {
            android.bordeaux.services.BordeauxManagerService.mAggregatorManager = IAggregatorManager.Stub.asInterface(android.bordeaux.services.BordeauxManagerService.mService.getAggregatorManager());
        } catch (android.os.RemoteException e) {
            android.bordeaux.services.BordeauxManagerService.mAggregatorManager = null;
        }
        return android.bordeaux.services.BordeauxManagerService.mAggregatorManager;
    }

    public static synchronized android.bordeaux.services.IPredictor getPredictor(android.content.Context context, java.lang.String name) {
        if (android.bordeaux.services.BordeauxManagerService.mService == null) {
            android.bordeaux.services.BordeauxManagerService.bindServices(context);
            return null;
        }
        try {
            android.bordeaux.services.BordeauxManagerService.mPredictor = IPredictor.Stub.asInterface(android.bordeaux.services.BordeauxManagerService.mService.getPredictor(name));
        } catch (android.os.RemoteException e) {
            android.bordeaux.services.BordeauxManagerService.mPredictor = null;
        }
        return android.bordeaux.services.BordeauxManagerService.mPredictor;
    }

    public static synchronized android.bordeaux.services.ILearning_StochasticLinearRanker getRanker(android.content.Context context, java.lang.String name) {
        if (android.bordeaux.services.BordeauxManagerService.mService == null) {
            android.bordeaux.services.BordeauxManagerService.bindServices(context);
            return null;
        }
        try {
            android.bordeaux.services.BordeauxManagerService.mRanker = ILearning_StochasticLinearRanker.Stub.asInterface(android.bordeaux.services.BordeauxManagerService.mService.getRanker(name));
        } catch (android.os.RemoteException e) {
            android.bordeaux.services.BordeauxManagerService.mRanker = null;
        }
        return android.bordeaux.services.BordeauxManagerService.mRanker;
    }

    public static synchronized android.bordeaux.services.ILearning_MulticlassPA getClassifier(android.content.Context context, java.lang.String name) {
        if (android.bordeaux.services.BordeauxManagerService.mService == null) {
            android.bordeaux.services.BordeauxManagerService.bindServices(context);
            return null;
        }
        try {
            android.bordeaux.services.BordeauxManagerService.mClassifier = ILearning_MulticlassPA.Stub.asInterface(android.bordeaux.services.BordeauxManagerService.mService.getClassifier(name));
        } catch (android.os.RemoteException e) {
            android.bordeaux.services.BordeauxManagerService.mClassifier = null;
        }
        return android.bordeaux.services.BordeauxManagerService.mClassifier;
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private static android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            // This is called when the connection with the service has been
            // established.
            android.bordeaux.services.BordeauxManagerService.mService = IBordeauxService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            android.bordeaux.services.BordeauxManagerService.mService = null;
            android.bordeaux.services.BordeauxManagerService.mStarted = false;// needs to bind again

        }
    };
}

