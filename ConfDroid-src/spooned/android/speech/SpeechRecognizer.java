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
package android.speech;


/**
 * This class provides access to the speech recognition service. This service allows access to the
 * speech recognizer. Do not instantiate this class directly, instead, call
 * {@link SpeechRecognizer#createSpeechRecognizer(Context)}. This class's methods must be
 * invoked only from the main application thread.
 *
 * <p>The implementation of this API is likely to stream audio to remote servers to perform speech
 * recognition. As such this API is not intended to be used for continuous recognition, which would
 * consume a significant amount of battery and bandwidth.
 *
 * <p>Please note that the application must have {@link android.Manifest.permission#RECORD_AUDIO}
 * permission to use this class.
 */
public class SpeechRecognizer {
    /**
     * DEBUG value to enable verbose debug prints
     */
    private static final boolean DBG = false;

    /**
     * Log messages identifier
     */
    private static final java.lang.String TAG = "SpeechRecognizer";

    /**
     * Key used to retrieve an {@code ArrayList<String>} from the {@link Bundle} passed to the
     * {@link RecognitionListener#onResults(Bundle)} and
     * {@link RecognitionListener#onPartialResults(Bundle)} methods. These strings are the possible
     * recognition results, where the first element is the most likely candidate.
     */
    public static final java.lang.String RESULTS_RECOGNITION = "results_recognition";

    /**
     * Key used to retrieve a float array from the {@link Bundle} passed to the
     * {@link RecognitionListener#onResults(Bundle)} and
     * {@link RecognitionListener#onPartialResults(Bundle)} methods. The array should be
     * the same size as the ArrayList provided in {@link #RESULTS_RECOGNITION}, and should contain
     * values ranging from 0.0 to 1.0, or -1 to represent an unavailable confidence score.
     * <p>
     * Confidence values close to 1.0 indicate high confidence (the speech recognizer is confident
     * that the recognition result is correct), while values close to 0.0 indicate low confidence.
     * <p>
     * This value is optional and might not be provided.
     */
    public static final java.lang.String CONFIDENCE_SCORES = "confidence_scores";

    /**
     * Network operation timed out.
     */
    public static final int ERROR_NETWORK_TIMEOUT = 1;

    /**
     * Other network related errors.
     */
    public static final int ERROR_NETWORK = 2;

    /**
     * Audio recording error.
     */
    public static final int ERROR_AUDIO = 3;

    /**
     * Server sends error status.
     */
    public static final int ERROR_SERVER = 4;

    /**
     * Other client side errors.
     */
    public static final int ERROR_CLIENT = 5;

    /**
     * No speech input
     */
    public static final int ERROR_SPEECH_TIMEOUT = 6;

    /**
     * No recognition result matched.
     */
    public static final int ERROR_NO_MATCH = 7;

    /**
     * RecognitionService busy.
     */
    public static final int ERROR_RECOGNIZER_BUSY = 8;

    /**
     * Insufficient permissions
     */
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 9;

    /**
     * action codes
     */
    private static final int MSG_START = 1;

    private static final int MSG_STOP = 2;

    private static final int MSG_CANCEL = 3;

    private static final int MSG_CHANGE_LISTENER = 4;

    /**
     * The actual RecognitionService endpoint
     */
    private android.speech.IRecognitionService mService;

    /**
     * The connection to the actual service
     */
    private android.speech.SpeechRecognizer.Connection mConnection;

    /**
     * Context with which the manager was created
     */
    private final android.content.Context mContext;

    /**
     * Component to direct service intent to
     */
    private final android.content.ComponentName mServiceComponent;

    /**
     * Handler that will execute the main tasks
     */
    private android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.speech.SpeechRecognizer.MSG_START :
                    handleStartListening(((android.content.Intent) (msg.obj)));
                    break;
                case android.speech.SpeechRecognizer.MSG_STOP :
                    handleStopMessage();
                    break;
                case android.speech.SpeechRecognizer.MSG_CANCEL :
                    handleCancelMessage();
                    break;
                case android.speech.SpeechRecognizer.MSG_CHANGE_LISTENER :
                    handleChangeListener(((android.speech.RecognitionListener) (msg.obj)));
                    break;
            }
        }
    };

    /**
     * Temporary queue, saving the messages until the connection will be established, afterwards,
     * only mHandler will receive the messages
     */
    private final java.util.Queue<android.os.Message> mPendingTasks = new java.util.LinkedList<android.os.Message>();

    /**
     * The Listener that will receive all the callbacks
     */
    private final android.speech.SpeechRecognizer.InternalListener mListener = new android.speech.SpeechRecognizer.InternalListener();

    /**
     * The right way to create a {@code SpeechRecognizer} is by using
     * {@link #createSpeechRecognizer} static factory method
     */
    private SpeechRecognizer(final android.content.Context context, final android.content.ComponentName serviceComponent) {
        mContext = context;
        mServiceComponent = serviceComponent;
    }

    /**
     * Basic ServiceConnection that records the mService variable. Additionally, on creation it
     * invokes the {@link IRecognitionService#startListening(Intent, IRecognitionListener)}.
     */
    private class Connection implements android.content.ServiceConnection {
        public void onServiceConnected(final android.content.ComponentName name, final android.os.IBinder service) {
            // always done on the application main thread, so no need to send message to mHandler
            mService = IRecognitionService.Stub.asInterface(service);
            if (android.speech.SpeechRecognizer.DBG)
                android.util.Log.d(android.speech.SpeechRecognizer.TAG, "onServiceConnected - Success");

            while (!mPendingTasks.isEmpty()) {
                mHandler.sendMessage(mPendingTasks.poll());
            } 
        }

        public void onServiceDisconnected(final android.content.ComponentName name) {
            // always done on the application main thread, so no need to send message to mHandler
            mService = null;
            mConnection = null;
            mPendingTasks.clear();
            if (android.speech.SpeechRecognizer.DBG)
                android.util.Log.d(android.speech.SpeechRecognizer.TAG, "onServiceDisconnected - Success");

        }
    }

    /**
     * Checks whether a speech recognition service is available on the system. If this method
     * returns {@code false}, {@link SpeechRecognizer#createSpeechRecognizer(Context)} will
     * fail.
     *
     * @param context
     * 		with which {@code SpeechRecognizer} will be created
     * @return {@code true} if recognition is available, {@code false} otherwise
     */
    public static boolean isRecognitionAvailable(final android.content.Context context) {
        final java.util.List<android.content.pm.ResolveInfo> list = context.getPackageManager().queryIntentServices(new android.content.Intent(android.speech.RecognitionService.SERVICE_INTERFACE), 0);
        return (list != null) && (list.size() != 0);
    }

    /**
     * Factory method to create a new {@code SpeechRecognizer}. Please note that
     * {@link #setRecognitionListener(RecognitionListener)} should be called before dispatching any
     * command to the created {@code SpeechRecognizer}, otherwise no notifications will be
     * received.
     *
     * @param context
     * 		in which to create {@code SpeechRecognizer}
     * @return a new {@code SpeechRecognizer}
     */
    public static android.speech.SpeechRecognizer createSpeechRecognizer(final android.content.Context context) {
        return android.speech.SpeechRecognizer.createSpeechRecognizer(context, null);
    }

    /**
     * Factory method to create a new {@code SpeechRecognizer}. Please note that
     * {@link #setRecognitionListener(RecognitionListener)} should be called before dispatching any
     * command to the created {@code SpeechRecognizer}, otherwise no notifications will be
     * received.
     *
     * Use this version of the method to specify a specific service to direct this
     * {@link SpeechRecognizer} to. Normally you would not use this; use
     * {@link #createSpeechRecognizer(Context)} instead to use the system default recognition
     * service.
     *
     * @param context
     * 		in which to create {@code SpeechRecognizer}
     * @param serviceComponent
     * 		the {@link ComponentName} of a specific service to direct this
     * 		{@code SpeechRecognizer} to
     * @return a new {@code SpeechRecognizer}
     */
    public static android.speech.SpeechRecognizer createSpeechRecognizer(final android.content.Context context, final android.content.ComponentName serviceComponent) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("Context cannot be null)");
        }
        android.speech.SpeechRecognizer.checkIsCalledFromMainThread();
        return new android.speech.SpeechRecognizer(context, serviceComponent);
    }

    /**
     * Sets the listener that will receive all the callbacks. The previous unfinished commands will
     * be executed with the old listener, while any following command will be executed with the new
     * listener.
     *
     * @param listener
     * 		listener that will receive all the callbacks from the created
     * 		{@link SpeechRecognizer}, this must not be null.
     */
    public void setRecognitionListener(android.speech.RecognitionListener listener) {
        android.speech.SpeechRecognizer.checkIsCalledFromMainThread();
        putMessage(android.os.Message.obtain(mHandler, android.speech.SpeechRecognizer.MSG_CHANGE_LISTENER, listener));
    }

    /**
     * Starts listening for speech. Please note that
     * {@link #setRecognitionListener(RecognitionListener)} should be called beforehand, otherwise
     * no notifications will be received.
     *
     * @param recognizerIntent
     * 		contains parameters for the recognition to be performed. The intent
     * 		may also contain optional extras, see {@link RecognizerIntent}. If these values are
     * 		not set explicitly, default values will be used by the recognizer.
     */
    public void startListening(final android.content.Intent recognizerIntent) {
        if (recognizerIntent == null) {
            throw new java.lang.IllegalArgumentException("intent must not be null");
        }
        android.speech.SpeechRecognizer.checkIsCalledFromMainThread();
        if (mConnection == null) {
            // first time connection
            mConnection = new android.speech.SpeechRecognizer.Connection();
            android.content.Intent serviceIntent = new android.content.Intent(android.speech.RecognitionService.SERVICE_INTERFACE);
            if (mServiceComponent == null) {
                java.lang.String serviceComponent = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.VOICE_RECOGNITION_SERVICE);
                if (android.text.TextUtils.isEmpty(serviceComponent)) {
                    android.util.Log.e(android.speech.SpeechRecognizer.TAG, "no selected voice recognition service");
                    mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
                    return;
                }
                serviceIntent.setComponent(android.content.ComponentName.unflattenFromString(serviceComponent));
            } else {
                serviceIntent.setComponent(mServiceComponent);
            }
            if (!mContext.bindService(serviceIntent, mConnection, android.content.Context.BIND_AUTO_CREATE)) {
                android.util.Log.e(android.speech.SpeechRecognizer.TAG, "bind to recognition service failed");
                mConnection = null;
                mService = null;
                mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
                return;
            }
        }
        putMessage(android.os.Message.obtain(mHandler, android.speech.SpeechRecognizer.MSG_START, recognizerIntent));
    }

    /**
     * Stops listening for speech. Speech captured so far will be recognized as if the user had
     * stopped speaking at this point. Note that in the default case, this does not need to be
     * called, as the speech endpointer will automatically stop the recognizer listening when it
     * determines speech has completed. However, you can manipulate endpointer parameters directly
     * using the intent extras defined in {@link RecognizerIntent}, in which case you may sometimes
     * want to manually call this method to stop listening sooner. Please note that
     * {@link #setRecognitionListener(RecognitionListener)} should be called beforehand, otherwise
     * no notifications will be received.
     */
    public void stopListening() {
        android.speech.SpeechRecognizer.checkIsCalledFromMainThread();
        putMessage(android.os.Message.obtain(mHandler, android.speech.SpeechRecognizer.MSG_STOP));
    }

    /**
     * Cancels the speech recognition. Please note that
     * {@link #setRecognitionListener(RecognitionListener)} should be called beforehand, otherwise
     * no notifications will be received.
     */
    public void cancel() {
        android.speech.SpeechRecognizer.checkIsCalledFromMainThread();
        putMessage(android.os.Message.obtain(mHandler, android.speech.SpeechRecognizer.MSG_CANCEL));
    }

    private static void checkIsCalledFromMainThread() {
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            throw new java.lang.RuntimeException("SpeechRecognizer should be used only from the application's main thread");
        }
    }

    private void putMessage(android.os.Message msg) {
        if (mService == null) {
            mPendingTasks.offer(msg);
        } else {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * sends the actual message to the service
     */
    private void handleStartListening(android.content.Intent recognizerIntent) {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            mService.startListening(recognizerIntent, mListener);
            if (android.speech.SpeechRecognizer.DBG)
                android.util.Log.d(android.speech.SpeechRecognizer.TAG, "service start listening command succeded");

        } catch (final android.os.RemoteException e) {
            android.util.Log.e(android.speech.SpeechRecognizer.TAG, "startListening() failed", e);
            mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
        }
    }

    /**
     * sends the actual message to the service
     */
    private void handleStopMessage() {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            mService.stopListening(mListener);
            if (android.speech.SpeechRecognizer.DBG)
                android.util.Log.d(android.speech.SpeechRecognizer.TAG, "service stop listening command succeded");

        } catch (final android.os.RemoteException e) {
            android.util.Log.e(android.speech.SpeechRecognizer.TAG, "stopListening() failed", e);
            mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
        }
    }

    /**
     * sends the actual message to the service
     */
    private void handleCancelMessage() {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            mService.cancel(mListener);
            if (android.speech.SpeechRecognizer.DBG)
                android.util.Log.d(android.speech.SpeechRecognizer.TAG, "service cancel command succeded");

        } catch (final android.os.RemoteException e) {
            android.util.Log.e(android.speech.SpeechRecognizer.TAG, "cancel() failed", e);
            mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
        }
    }

    private boolean checkOpenConnection() {
        if (mService != null) {
            return true;
        }
        mListener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
        android.util.Log.e(android.speech.SpeechRecognizer.TAG, "not connected to the recognition service");
        return false;
    }

    /**
     * changes the listener
     */
    private void handleChangeListener(android.speech.RecognitionListener listener) {
        if (android.speech.SpeechRecognizer.DBG)
            android.util.Log.d(android.speech.SpeechRecognizer.TAG, "handleChangeListener, listener=" + listener);

        mListener.mInternalListener = listener;
    }

    /**
     * Destroys the {@code SpeechRecognizer} object.
     */
    public void destroy() {
        if (mService != null) {
            try {
                mService.cancel(mListener);
            } catch (final android.os.RemoteException e) {
                // Not important
            }
        }
        if (mConnection != null) {
            mContext.unbindService(mConnection);
        }
        mPendingTasks.clear();
        mService = null;
        mConnection = null;
        mListener.mInternalListener = null;
    }

    /**
     * Internal wrapper of IRecognitionListener which will propagate the results to
     * RecognitionListener
     */
    private static class InternalListener extends android.speech.IRecognitionListener.Stub {
        private android.speech.RecognitionListener mInternalListener;

        private static final int MSG_BEGINNING_OF_SPEECH = 1;

        private static final int MSG_BUFFER_RECEIVED = 2;

        private static final int MSG_END_OF_SPEECH = 3;

        private static final int MSG_ERROR = 4;

        private static final int MSG_READY_FOR_SPEECH = 5;

        private static final int MSG_RESULTS = 6;

        private static final int MSG_PARTIAL_RESULTS = 7;

        private static final int MSG_RMS_CHANGED = 8;

        private static final int MSG_ON_EVENT = 9;

        private final android.os.Handler mInternalHandler = new android.os.Handler() {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (mInternalListener == null) {
                    return;
                }
                switch (msg.what) {
                    case android.speech.SpeechRecognizer.InternalListener.MSG_BEGINNING_OF_SPEECH :
                        mInternalListener.onBeginningOfSpeech();
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_BUFFER_RECEIVED :
                        mInternalListener.onBufferReceived(((byte[]) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_END_OF_SPEECH :
                        mInternalListener.onEndOfSpeech();
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_ERROR :
                        mInternalListener.onError(((java.lang.Integer) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_READY_FOR_SPEECH :
                        mInternalListener.onReadyForSpeech(((android.os.Bundle) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_RESULTS :
                        mInternalListener.onResults(((android.os.Bundle) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_PARTIAL_RESULTS :
                        mInternalListener.onPartialResults(((android.os.Bundle) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_RMS_CHANGED :
                        mInternalListener.onRmsChanged(((java.lang.Float) (msg.obj)));
                        break;
                    case android.speech.SpeechRecognizer.InternalListener.MSG_ON_EVENT :
                        mInternalListener.onEvent(msg.arg1, ((android.os.Bundle) (msg.obj)));
                        break;
                }
            }
        };

        public void onBeginningOfSpeech() {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_BEGINNING_OF_SPEECH).sendToTarget();
        }

        public void onBufferReceived(final byte[] buffer) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_BUFFER_RECEIVED, buffer).sendToTarget();
        }

        public void onEndOfSpeech() {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_END_OF_SPEECH).sendToTarget();
        }

        public void onError(final int error) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_ERROR, error).sendToTarget();
        }

        public void onReadyForSpeech(final android.os.Bundle noiseParams) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_READY_FOR_SPEECH, noiseParams).sendToTarget();
        }

        public void onResults(final android.os.Bundle results) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_RESULTS, results).sendToTarget();
        }

        public void onPartialResults(final android.os.Bundle results) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_PARTIAL_RESULTS, results).sendToTarget();
        }

        public void onRmsChanged(final float rmsdB) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_RMS_CHANGED, rmsdB).sendToTarget();
        }

        public void onEvent(final int eventType, final android.os.Bundle params) {
            android.os.Message.obtain(mInternalHandler, android.speech.SpeechRecognizer.InternalListener.MSG_ON_EVENT, eventType, eventType, params).sendToTarget();
        }
    }
}

