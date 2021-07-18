/**
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.speech;


/**
 * This class provides a base class for recognition service implementations. This class should be
 * extended only in case you wish to implement a new speech recognizer. Please note that the
 * implementation of this service is stateless.
 */
public abstract class RecognitionService extends android.app.Service {
    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.speech.RecognitionService";

    /**
     * Name under which a RecognitionService component publishes information about itself.
     * This meta-data should reference an XML resource containing a
     * <code>&lt;{@link android.R.styleable#RecognitionService recognition-service}&gt;</code> tag.
     */
    public static final java.lang.String SERVICE_META_DATA = "android.speech";

    /**
     * Log messages identifier
     */
    private static final java.lang.String TAG = "RecognitionService";

    /**
     * Debugging flag
     */
    private static final boolean DBG = false;

    /**
     * Binder of the recognition service
     */
    private android.speech.RecognitionService.RecognitionServiceBinder mBinder = new android.speech.RecognitionService.RecognitionServiceBinder(this);

    /**
     * The current callback of an application that invoked the
     * {@link RecognitionService#onStartListening(Intent, Callback)} method
     */
    private android.speech.RecognitionService.Callback mCurrentCallback = null;

    private static final int MSG_START_LISTENING = 1;

    private static final int MSG_STOP_LISTENING = 2;

    private static final int MSG_CANCEL = 3;

    private static final int MSG_RESET = 4;

    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.speech.RecognitionService.MSG_START_LISTENING :
                    android.speech.RecognitionService.StartListeningArgs args = ((android.speech.RecognitionService.StartListeningArgs) (msg.obj));
                    dispatchStartListening(args.mIntent, args.mListener, args.mCallingUid);
                    break;
                case android.speech.RecognitionService.MSG_STOP_LISTENING :
                    dispatchStopListening(((android.speech.IRecognitionListener) (msg.obj)));
                    break;
                case android.speech.RecognitionService.MSG_CANCEL :
                    dispatchCancel(((android.speech.IRecognitionListener) (msg.obj)));
                    break;
                case android.speech.RecognitionService.MSG_RESET :
                    dispatchClearCallback();
                    break;
            }
        }
    };

    private void dispatchStartListening(android.content.Intent intent, final android.speech.IRecognitionListener listener, int callingUid) {
        if (mCurrentCallback == null) {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "created new mCurrentCallback, listener = " + listener.asBinder());

            try {
                listener.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() {
                    @java.lang.Override
                    public void binderDied() {
                        mHandler.sendMessage(mHandler.obtainMessage(android.speech.RecognitionService.MSG_CANCEL, listener));
                    }
                }, 0);
            } catch (android.os.RemoteException re) {
                android.util.Log.e(android.speech.RecognitionService.TAG, "dead listener on startListening");
                return;
            }
            mCurrentCallback = new android.speech.RecognitionService.Callback(listener, callingUid);
            this.onStartListening(intent, mCurrentCallback);
        } else {
            try {
                listener.onError(android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY);
            } catch (android.os.RemoteException e) {
                android.util.Log.d(android.speech.RecognitionService.TAG, "onError call from startListening failed");
            }
            android.util.Log.i(android.speech.RecognitionService.TAG, "concurrent startListening received - ignoring this call");
        }
    }

    private void dispatchStopListening(android.speech.IRecognitionListener listener) {
        try {
            if (mCurrentCallback == null) {
                listener.onError(android.speech.SpeechRecognizer.ERROR_CLIENT);
                android.util.Log.w(android.speech.RecognitionService.TAG, "stopListening called with no preceding startListening - ignoring");
            } else
                if (mCurrentCallback.mListener.asBinder() != listener.asBinder()) {
                    listener.onError(android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY);
                    android.util.Log.w(android.speech.RecognitionService.TAG, "stopListening called by other caller than startListening - ignoring");
                } else {
                    // the correct state
                    this.onStopListening(mCurrentCallback);
                }

        } catch (android.os.RemoteException e) {
            // occurs if onError fails
            android.util.Log.d(android.speech.RecognitionService.TAG, "onError call from stopListening failed");
        }
    }

    private void dispatchCancel(android.speech.IRecognitionListener listener) {
        if (mCurrentCallback == null) {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "cancel called with no preceding startListening - ignoring");

        } else
            if (mCurrentCallback.mListener.asBinder() != listener.asBinder()) {
                android.util.Log.w(android.speech.RecognitionService.TAG, "cancel called by client who did not call startListening - ignoring");
            } else {
                // the correct state
                this.onCancel(mCurrentCallback);
                mCurrentCallback = null;
                if (android.speech.RecognitionService.DBG)
                    android.util.Log.d(android.speech.RecognitionService.TAG, "canceling - setting mCurrentCallback to null");

            }

    }

    private void dispatchClearCallback() {
        mCurrentCallback = null;
    }

    private class StartListeningArgs {
        public final android.content.Intent mIntent;

        public final android.speech.IRecognitionListener mListener;

        public final int mCallingUid;

        public StartListeningArgs(android.content.Intent intent, android.speech.IRecognitionListener listener, int callingUid) {
            this.mIntent = intent;
            this.mListener = listener;
            this.mCallingUid = callingUid;
        }
    }

    /**
     * Checks whether the caller has sufficient permissions
     *
     * @param listener
     * 		to send the error message to in case of error
     * @return {@code true} if the caller has enough permissions, {@code false} otherwise
     */
    private boolean checkPermissions(android.speech.IRecognitionListener listener) {
        if (android.speech.RecognitionService.DBG)
            android.util.Log.d(android.speech.RecognitionService.TAG, "checkPermissions");

        if (this.checkCallingOrSelfPermission(android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        try {
            android.util.Log.e(android.speech.RecognitionService.TAG, "call for recognition service without RECORD_AUDIO permissions");
            listener.onError(android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.speech.RecognitionService.TAG, "sending ERROR_INSUFFICIENT_PERMISSIONS message failed", re);
        }
        return false;
    }

    /**
     * Notifies the service that it should start listening for speech.
     *
     * @param recognizerIntent
     * 		contains parameters for the recognition to be performed. The intent
     * 		may also contain optional extras, see {@link RecognizerIntent}. If these values are
     * 		not set explicitly, default values should be used by the recognizer.
     * @param listener
     * 		that will receive the service's callbacks
     */
    protected abstract void onStartListening(android.content.Intent recognizerIntent, android.speech.RecognitionService.Callback listener);

    /**
     * Notifies the service that it should cancel the speech recognition.
     */
    protected abstract void onCancel(android.speech.RecognitionService.Callback listener);

    /**
     * Notifies the service that it should stop listening for speech. Speech captured so far should
     * be recognized as if the user had stopped speaking at this point. This method is only called
     * if the application calls it explicitly.
     */
    protected abstract void onStopListening(android.speech.RecognitionService.Callback listener);

    @java.lang.Override
    public final android.os.IBinder onBind(final android.content.Intent intent) {
        if (android.speech.RecognitionService.DBG)
            android.util.Log.d(android.speech.RecognitionService.TAG, "onBind, intent=" + intent);

        return mBinder;
    }

    @java.lang.Override
    public void onDestroy() {
        if (android.speech.RecognitionService.DBG)
            android.util.Log.d(android.speech.RecognitionService.TAG, "onDestroy");

        mCurrentCallback = null;
        mBinder.clearReference();
        super.onDestroy();
    }

    /**
     * This class receives callbacks from the speech recognition service and forwards them to the
     * user. An instance of this class is passed to the
     * {@link RecognitionService#onStartListening(Intent, Callback)} method. Recognizers may call
     * these methods on any thread.
     */
    public class Callback {
        private final android.speech.IRecognitionListener mListener;

        private final int mCallingUid;

        private Callback(android.speech.IRecognitionListener listener, int callingUid) {
            mListener = listener;
            mCallingUid = callingUid;
        }

        /**
         * The service should call this method when the user has started to speak.
         */
        public void beginningOfSpeech() throws android.os.RemoteException {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "beginningOfSpeech");

            mListener.onBeginningOfSpeech();
        }

        /**
         * The service should call this method when sound has been received. The purpose of this
         * function is to allow giving feedback to the user regarding the captured audio.
         *
         * @param buffer
         * 		a buffer containing a sequence of big-endian 16-bit integers representing a
         * 		single channel audio stream. The sample rate is implementation dependent.
         */
        public void bufferReceived(byte[] buffer) throws android.os.RemoteException {
            mListener.onBufferReceived(buffer);
        }

        /**
         * The service should call this method after the user stops speaking.
         */
        public void endOfSpeech() throws android.os.RemoteException {
            mListener.onEndOfSpeech();
        }

        /**
         * The service should call this method when a network or recognition error occurred.
         *
         * @param error
         * 		code is defined in {@link SpeechRecognizer}
         */
        public void error(int error) throws android.os.RemoteException {
            android.os.Message.obtain(mHandler, android.speech.RecognitionService.MSG_RESET).sendToTarget();
            mListener.onError(error);
        }

        /**
         * The service should call this method when partial recognition results are available. This
         * method can be called at any time between {@link #beginningOfSpeech()} and
         * {@link #results(Bundle)} when partial results are ready. This method may be called zero,
         * one or multiple times for each call to {@link SpeechRecognizer#startListening(Intent)},
         * depending on the speech recognition service implementation.
         *
         * @param partialResults
         * 		the returned results. To retrieve the results in
         * 		ArrayList&lt;String&gt; format use {@link Bundle#getStringArrayList(String)} with
         * 		{@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter
         */
        public void partialResults(android.os.Bundle partialResults) throws android.os.RemoteException {
            mListener.onPartialResults(partialResults);
        }

        /**
         * The service should call this method when the endpointer is ready for the user to start
         * speaking.
         *
         * @param params
         * 		parameters set by the recognition service. Reserved for future use.
         */
        public void readyForSpeech(android.os.Bundle params) throws android.os.RemoteException {
            mListener.onReadyForSpeech(params);
        }

        /**
         * The service should call this method when recognition results are ready.
         *
         * @param results
         * 		the recognition results. To retrieve the results in {@code ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
         * 		{@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter
         */
        public void results(android.os.Bundle results) throws android.os.RemoteException {
            android.os.Message.obtain(mHandler, android.speech.RecognitionService.MSG_RESET).sendToTarget();
            mListener.onResults(results);
        }

        /**
         * The service should call this method when the sound level in the audio stream has changed.
         * There is no guarantee that this method will be called.
         *
         * @param rmsdB
         * 		the new RMS dB value
         */
        public void rmsChanged(float rmsdB) throws android.os.RemoteException {
            mListener.onRmsChanged(rmsdB);
        }

        /**
         * Return the Linux uid assigned to the process that sent you the current transaction that
         * is being processed. This is obtained from {@link Binder#getCallingUid()}.
         */
        public int getCallingUid() {
            return mCallingUid;
        }
    }

    /**
     * Binder of the recognition service
     */
    private static final class RecognitionServiceBinder extends android.speech.IRecognitionService.Stub {
        private final java.lang.ref.WeakReference<android.speech.RecognitionService> mServiceRef;

        public RecognitionServiceBinder(android.speech.RecognitionService service) {
            mServiceRef = new java.lang.ref.WeakReference<android.speech.RecognitionService>(service);
        }

        @java.lang.Override
        public void startListening(android.content.Intent recognizerIntent, android.speech.IRecognitionListener listener) {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "startListening called by:" + listener.asBinder());

            final android.speech.RecognitionService service = mServiceRef.get();
            if ((service != null) && service.checkPermissions(listener)) {
                service.mHandler.sendMessage(android.os.Message.obtain(service.mHandler, android.speech.RecognitionService.MSG_START_LISTENING, service.new StartListeningArgs(recognizerIntent, listener, android.os.Binder.getCallingUid())));
            }
        }

        @java.lang.Override
        public void stopListening(android.speech.IRecognitionListener listener) {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "stopListening called by:" + listener.asBinder());

            final android.speech.RecognitionService service = mServiceRef.get();
            if ((service != null) && service.checkPermissions(listener)) {
                service.mHandler.sendMessage(android.os.Message.obtain(service.mHandler, android.speech.RecognitionService.MSG_STOP_LISTENING, listener));
            }
        }

        @java.lang.Override
        public void cancel(android.speech.IRecognitionListener listener) {
            if (android.speech.RecognitionService.DBG)
                android.util.Log.d(android.speech.RecognitionService.TAG, "cancel called by:" + listener.asBinder());

            final android.speech.RecognitionService service = mServiceRef.get();
            if ((service != null) && service.checkPermissions(listener)) {
                service.mHandler.sendMessage(android.os.Message.obtain(service.mHandler, android.speech.RecognitionService.MSG_CANCEL, listener));
            }
        }

        public void clearReference() {
            mServiceRef.clear();
        }
    }
}

