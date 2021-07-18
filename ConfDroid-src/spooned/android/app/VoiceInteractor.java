/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * Interface for an {@link Activity} to interact with the user through voice.  Use
 * {@link android.app.Activity#getVoiceInteractor() Activity.getVoiceInteractor}
 * to retrieve the interface, if the activity is currently involved in a voice interaction.
 *
 * <p>The voice interactor revolves around submitting voice interaction requests to the
 * back-end voice interaction service that is working with the user.  These requests are
 * submitted with {@link #submitRequest}, providing a new instance of a
 * {@link Request} subclass describing the type of operation to perform -- currently the
 * possible requests are {@link ConfirmationRequest} and {@link CommandRequest}.
 *
 * <p>Once a request is submitted, the voice system will process it and eventually deliver
 * the result to the request object.  The application can cancel a pending request at any
 * time.
 *
 * <p>The VoiceInteractor is integrated with Activity's state saving mechanism, so that
 * if an activity is being restarted with retained state, it will retain the current
 * VoiceInteractor and any outstanding requests.  Because of this, you should always use
 * {@link Request#getActivity() Request.getActivity} to get back to the activity of a
 * request, rather than holding on to the activity instance yourself, either explicitly
 * or implicitly through a non-static inner class.
 */
public final class VoiceInteractor {
    static final java.lang.String TAG = "VoiceInteractor";

    static final boolean DEBUG = false;

    static final android.app.VoiceInteractor.Request[] NO_REQUESTS = new android.app.VoiceInteractor.Request[0];

    final com.android.internal.app.IVoiceInteractor mInteractor;

    android.content.Context mContext;

    android.app.Activity mActivity;

    boolean mRetaining;

    final com.android.internal.os.HandlerCaller mHandlerCaller;

    final HandlerCaller.Callback mHandlerCallerCallback = new com.android.internal.os.HandlerCaller.Callback() {
        @java.lang.Override
        public void executeMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
            android.app.VoiceInteractor.Request request;
            boolean complete;
            switch (msg.what) {
                case android.app.VoiceInteractor.MSG_CONFIRMATION_RESULT :
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), true);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (((((("onConfirmResult: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request) + " confirmed=") + msg.arg1) + " result=") + args.arg2);

                    if (request != null) {
                        ((android.app.VoiceInteractor.ConfirmationRequest) (request)).onConfirmationResult(msg.arg1 != 0, ((android.os.Bundle) (args.arg2)));
                        request.clear();
                    }
                    break;
                case android.app.VoiceInteractor.MSG_PICK_OPTION_RESULT :
                    complete = msg.arg1 != 0;
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), complete);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (((((((("onPickOptionResult: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request) + " finished=") + complete) + " selection=") + args.arg2) + " result=") + args.arg3);

                    if (request != null) {
                        ((android.app.VoiceInteractor.PickOptionRequest) (request)).onPickOptionResult(complete, ((android.app.VoiceInteractor.PickOptionRequest.Option[]) (args.arg2)), ((android.os.Bundle) (args.arg3)));
                        if (complete) {
                            request.clear();
                        }
                    }
                    break;
                case android.app.VoiceInteractor.MSG_COMPLETE_VOICE_RESULT :
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), true);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (((("onCompleteVoice: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request) + " result=") + args.arg2);

                    if (request != null) {
                        ((android.app.VoiceInteractor.CompleteVoiceRequest) (request)).onCompleteResult(((android.os.Bundle) (args.arg2)));
                        request.clear();
                    }
                    break;
                case android.app.VoiceInteractor.MSG_ABORT_VOICE_RESULT :
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), true);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (((("onAbortVoice: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request) + " result=") + args.arg2);

                    if (request != null) {
                        ((android.app.VoiceInteractor.AbortVoiceRequest) (request)).onAbortResult(((android.os.Bundle) (args.arg2)));
                        request.clear();
                    }
                    break;
                case android.app.VoiceInteractor.MSG_COMMAND_RESULT :
                    complete = msg.arg1 != 0;
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), complete);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (((((("onCommandResult: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request) + " completed=") + msg.arg1) + " result=") + args.arg2);

                    if (request != null) {
                        ((android.app.VoiceInteractor.CommandRequest) (request)).onCommandResult(msg.arg1 != 0, ((android.os.Bundle) (args.arg2)));
                        if (complete) {
                            request.clear();
                        }
                    }
                    break;
                case android.app.VoiceInteractor.MSG_CANCEL_RESULT :
                    request = pullRequest(((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)), true);
                    if (android.app.VoiceInteractor.DEBUG)
                        android.util.Log.d(android.app.VoiceInteractor.TAG, (("onCancelResult: req=" + ((com.android.internal.app.IVoiceInteractorRequest) (args.arg1)).asBinder()) + "/") + request);

                    if (request != null) {
                        request.onCancel();
                        request.clear();
                    }
                    break;
            }
        }
    };

    final IVoiceInteractorCallback.Stub mCallback = new com.android.internal.app.IVoiceInteractorCallback.Stub() {
        @java.lang.Override
        public void deliverConfirmationResult(com.android.internal.app.IVoiceInteractorRequest request, boolean finished, android.os.Bundle result) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(android.app.VoiceInteractor.MSG_CONFIRMATION_RESULT, finished ? 1 : 0, request, result));
        }

        @java.lang.Override
        public void deliverPickOptionResult(com.android.internal.app.IVoiceInteractorRequest request, boolean finished, android.app.VoiceInteractor.PickOptionRequest.Option[] options, android.os.Bundle result) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOOO(android.app.VoiceInteractor.MSG_PICK_OPTION_RESULT, finished ? 1 : 0, request, options, result));
        }

        @java.lang.Override
        public void deliverCompleteVoiceResult(com.android.internal.app.IVoiceInteractorRequest request, android.os.Bundle result) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(android.app.VoiceInteractor.MSG_COMPLETE_VOICE_RESULT, request, result));
        }

        @java.lang.Override
        public void deliverAbortVoiceResult(com.android.internal.app.IVoiceInteractorRequest request, android.os.Bundle result) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(android.app.VoiceInteractor.MSG_ABORT_VOICE_RESULT, request, result));
        }

        @java.lang.Override
        public void deliverCommandResult(com.android.internal.app.IVoiceInteractorRequest request, boolean complete, android.os.Bundle result) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(android.app.VoiceInteractor.MSG_COMMAND_RESULT, complete ? 1 : 0, request, result));
        }

        @java.lang.Override
        public void deliverCancel(com.android.internal.app.IVoiceInteractorRequest request) throws android.os.RemoteException {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(android.app.VoiceInteractor.MSG_CANCEL_RESULT, request, null));
        }
    };

    final android.util.ArrayMap<android.os.IBinder, android.app.VoiceInteractor.Request> mActiveRequests = new android.util.ArrayMap<>();

    static final int MSG_CONFIRMATION_RESULT = 1;

    static final int MSG_PICK_OPTION_RESULT = 2;

    static final int MSG_COMPLETE_VOICE_RESULT = 3;

    static final int MSG_ABORT_VOICE_RESULT = 4;

    static final int MSG_COMMAND_RESULT = 5;

    static final int MSG_CANCEL_RESULT = 6;

    /**
     * Base class for voice interaction requests that can be submitted to the interactor.
     * Do not instantiate this directly -- instead, use the appropriate subclass.
     */
    public static abstract class Request {
        com.android.internal.app.IVoiceInteractorRequest mRequestInterface;

        android.content.Context mContext;

        android.app.Activity mActivity;

        java.lang.String mName;

        Request() {
        }

        /**
         * Return the name this request was submitted through
         * {@link #submitRequest(android.app.VoiceInteractor.Request, String)}.
         */
        public java.lang.String getName() {
            return mName;
        }

        /**
         * Cancel this active request.
         */
        public void cancel() {
            if (mRequestInterface == null) {
                throw new java.lang.IllegalStateException(("Request " + this) + " is no longer active");
            }
            try {
                mRequestInterface.cancel();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.app.VoiceInteractor.TAG, "Voice interactor has died", e);
            }
        }

        /**
         * Return the current {@link Context} this request is associated with.  May change
         * if the activity hosting it goes through a configuration change.
         */
        public android.content.Context getContext() {
            return mContext;
        }

        /**
         * Return the current {@link Activity} this request is associated with.  Will change
         * if the activity is restarted such as through a configuration change.
         */
        public android.app.Activity getActivity() {
            return mActivity;
        }

        /**
         * Report from voice interaction service: this operation has been canceled, typically
         * as a completion of a previous call to {@link #cancel} or when the user explicitly
         * cancelled.
         */
        public void onCancel() {
        }

        /**
         * The request is now attached to an activity, or being re-attached to a new activity
         * after a configuration change.
         */
        public void onAttached(android.app.Activity activity) {
        }

        /**
         * The request is being detached from an activity.
         */
        public void onDetached() {
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            android.util.DebugUtils.buildShortClassTag(this, sb);
            sb.append(" ");
            sb.append(getRequestTypeName());
            sb.append(" name=");
            sb.append(mName);
            sb.append('}');
            return sb.toString();
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            writer.print(prefix);
            writer.print("mRequestInterface=");
            writer.println(mRequestInterface.asBinder());
            writer.print(prefix);
            writer.print("mActivity=");
            writer.println(mActivity);
            writer.print(prefix);
            writer.print("mName=");
            writer.println(mName);
        }

        java.lang.String getRequestTypeName() {
            return "Request";
        }

        void clear() {
            mRequestInterface = null;
            mContext = null;
            mActivity = null;
            mName = null;
        }

        abstract com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException;
    }

    /**
     * Confirms an operation with the user via the trusted system
     * VoiceInteractionService.  This allows an Activity to complete an unsafe operation that
     * would require the user to touch the screen when voice interaction mode is not enabled.
     * The result of the confirmation will be returned through an asynchronous call to
     * either {@link #onConfirmationResult(boolean, android.os.Bundle)} or
     * {@link #onCancel()} - these methods should be overridden to define the application specific
     *  behavior.
     *
     * <p>In some cases this may be a simple yes / no confirmation or the confirmation could
     * include context information about how the action will be completed
     * (e.g. booking a cab might include details about how long until the cab arrives)
     * so the user can give a confirmation.
     */
    public static class ConfirmationRequest extends android.app.VoiceInteractor.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        final android.os.Bundle mExtras;

        /**
         * Create a new confirmation request.
         *
         * @param prompt
         * 		Optional confirmation to speak to the user or null if nothing
         * 		should be spoken.
         * @param extras
         * 		Additional optional information or null.
         */
        public ConfirmationRequest(@android.annotation.Nullable
        android.app.VoiceInteractor.Prompt prompt, @android.annotation.Nullable
        android.os.Bundle extras) {
            mPrompt = prompt;
            mExtras = extras;
        }

        /**
         * Create a new confirmation request.
         *
         * @param prompt
         * 		Optional confirmation to speak to the user or null if nothing
         * 		should be spoken.
         * @param extras
         * 		Additional optional information or null.
         * @unknown 
         */
        public ConfirmationRequest(java.lang.CharSequence prompt, android.os.Bundle extras) {
            mPrompt = (prompt != null) ? new android.app.VoiceInteractor.Prompt(prompt) : null;
            mExtras = extras;
        }

        /**
         * Handle the confirmation result. Override this method to define
         * the behavior when the user confirms or rejects the operation.
         *
         * @param confirmed
         * 		Whether the user confirmed or rejected the operation.
         * @param result
         * 		Additional result information or null.
         */
        public void onConfirmationResult(boolean confirmed, android.os.Bundle result) {
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
            if (mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(mExtras);
            }
        }

        java.lang.String getRequestTypeName() {
            return "Confirmation";
        }

        com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException {
            return interactor.startConfirmation(packageName, callback, mPrompt, mExtras);
        }
    }

    /**
     * Select a single option from multiple potential options with the user via the trusted system
     * VoiceInteractionService. Typically, the application would present this visually as
     * a list view to allow selecting the option by touch.
     * The result of the confirmation will be returned through an asynchronous call to
     * either {@link #onPickOptionResult} or {@link #onCancel()} - these methods should
     * be overridden to define the application specific behavior.
     */
    public static class PickOptionRequest extends android.app.VoiceInteractor.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        final android.app.VoiceInteractor.PickOptionRequest.Option[] mOptions;

        final android.os.Bundle mExtras;

        /**
         * Represents a single option that the user may select using their voice. The
         * {@link #getIndex()} method should be used as a unique ID to identify the option
         * when it is returned from the voice interactor.
         */
        public static final class Option implements android.os.Parcelable {
            final java.lang.CharSequence mLabel;

            final int mIndex;

            java.util.ArrayList<java.lang.CharSequence> mSynonyms;

            android.os.Bundle mExtras;

            /**
             * Creates an option that a user can select with their voice by matching the label
             * or one of several synonyms.
             *
             * @param label
             * 		The label that will both be matched against what the user speaks
             * 		and displayed visually.
             * @unknown 
             */
            public Option(java.lang.CharSequence label) {
                mLabel = label;
                mIndex = -1;
            }

            /**
             * Creates an option that a user can select with their voice by matching the label
             * or one of several synonyms.
             *
             * @param label
             * 		The label that will both be matched against what the user speaks
             * 		and displayed visually.
             * @param index
             * 		The location of this option within the overall set of options.
             * 		Can be used to help identify the option when it is returned from the
             * 		voice interactor.
             */
            public Option(java.lang.CharSequence label, int index) {
                mLabel = label;
                mIndex = index;
            }

            /**
             * Add a synonym term to the option to indicate an alternative way the content
             * may be matched.
             *
             * @param synonym
             * 		The synonym that will be matched against what the user speaks,
             * 		but not displayed.
             */
            public android.app.VoiceInteractor.PickOptionRequest.Option addSynonym(java.lang.CharSequence synonym) {
                if (mSynonyms == null) {
                    mSynonyms = new java.util.ArrayList<>();
                }
                mSynonyms.add(synonym);
                return this;
            }

            public java.lang.CharSequence getLabel() {
                return mLabel;
            }

            /**
             * Return the index that was supplied in the constructor.
             * If the option was constructed without an index, -1 is returned.
             */
            public int getIndex() {
                return mIndex;
            }

            public int countSynonyms() {
                return mSynonyms != null ? mSynonyms.size() : 0;
            }

            public java.lang.CharSequence getSynonymAt(int index) {
                return mSynonyms != null ? mSynonyms.get(index) : null;
            }

            /**
             * Set optional extra information associated with this option.  Note that this
             * method takes ownership of the supplied extras Bundle.
             */
            public void setExtras(android.os.Bundle extras) {
                mExtras = extras;
            }

            /**
             * Return any optional extras information associated with this option, or null
             * if there is none.  Note that this method returns a reference to the actual
             * extras Bundle in the option, so modifications to it will directly modify the
             * extras in the option.
             */
            public android.os.Bundle getExtras() {
                return mExtras;
            }

            Option(android.os.Parcel in) {
                mLabel = in.readCharSequence();
                mIndex = in.readInt();
                mSynonyms = in.readCharSequenceList();
                mExtras = in.readBundle();
            }

            @java.lang.Override
            public int describeContents() {
                return 0;
            }

            @java.lang.Override
            public void writeToParcel(android.os.Parcel dest, int flags) {
                dest.writeCharSequence(mLabel);
                dest.writeInt(mIndex);
                dest.writeCharSequenceList(mSynonyms);
                dest.writeBundle(mExtras);
            }

            public static final android.os.Parcelable.Creator<android.app.VoiceInteractor.PickOptionRequest.Option> CREATOR = new android.os.Parcelable.Creator<android.app.VoiceInteractor.PickOptionRequest.Option>() {
                public android.app.VoiceInteractor.PickOptionRequest.Option createFromParcel(android.os.Parcel in) {
                    return new android.app.VoiceInteractor.PickOptionRequest.Option(in);
                }

                public android.app.VoiceInteractor.PickOptionRequest.Option[] newArray(int size) {
                    return new android.app.VoiceInteractor.PickOptionRequest.Option[size];
                }
            };
        }

        /**
         * Create a new pick option request.
         *
         * @param prompt
         * 		Optional question to be asked of the user when the options are
         * 		presented or null if nothing should be asked.
         * @param options
         * 		The set of {@link Option}s the user is selecting from.
         * @param extras
         * 		Additional optional information or null.
         */
        public PickOptionRequest(@android.annotation.Nullable
        android.app.VoiceInteractor.Prompt prompt, android.app.VoiceInteractor.PickOptionRequest.Option[] options, @android.annotation.Nullable
        android.os.Bundle extras) {
            mPrompt = prompt;
            mOptions = options;
            mExtras = extras;
        }

        /**
         * Create a new pick option request.
         *
         * @param prompt
         * 		Optional question to be asked of the user when the options are
         * 		presented or null if nothing should be asked.
         * @param options
         * 		The set of {@link Option}s the user is selecting from.
         * @param extras
         * 		Additional optional information or null.
         * @unknown 
         */
        public PickOptionRequest(java.lang.CharSequence prompt, android.app.VoiceInteractor.PickOptionRequest.Option[] options, android.os.Bundle extras) {
            mPrompt = (prompt != null) ? new android.app.VoiceInteractor.Prompt(prompt) : null;
            mOptions = options;
            mExtras = extras;
        }

        /**
         * Called when a single option is confirmed or narrowed to one of several options. Override
         * this method to define the behavior when the user selects an option or narrows down the
         * set of options.
         *
         * @param finished
         * 		True if the voice interaction has finished making a selection, in
         * 		which case {@code selections} contains the final result.  If false, this request is
         * 		still active and you will continue to get calls on it.
         * @param selections
         * 		Either a single {@link Option} or one of several {@link Option}s the
         * 		user has narrowed the choices down to.
         * @param result
         * 		Additional optional information.
         */
        public void onPickOptionResult(boolean finished, android.app.VoiceInteractor.PickOptionRequest.Option[] selections, android.os.Bundle result) {
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
            if (mOptions != null) {
                writer.print(prefix);
                writer.println("Options:");
                for (int i = 0; i < mOptions.length; i++) {
                    android.app.VoiceInteractor.PickOptionRequest.Option op = mOptions[i];
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.println(":");
                    writer.print(prefix);
                    writer.print("    mLabel=");
                    writer.println(op.mLabel);
                    writer.print(prefix);
                    writer.print("    mIndex=");
                    writer.println(op.mIndex);
                    if ((op.mSynonyms != null) && (op.mSynonyms.size() > 0)) {
                        writer.print(prefix);
                        writer.println("    Synonyms:");
                        for (int j = 0; j < op.mSynonyms.size(); j++) {
                            writer.print(prefix);
                            writer.print("      #");
                            writer.print(j);
                            writer.print(": ");
                            writer.println(op.mSynonyms.get(j));
                        }
                    }
                    if (op.mExtras != null) {
                        writer.print(prefix);
                        writer.print("    mExtras=");
                        writer.println(op.mExtras);
                    }
                }
            }
            if (mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(mExtras);
            }
        }

        java.lang.String getRequestTypeName() {
            return "PickOption";
        }

        com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException {
            return interactor.startPickOption(packageName, callback, mPrompt, mOptions, mExtras);
        }
    }

    /**
     * Reports that the current interaction was successfully completed with voice, so the
     * application can report the final status to the user. When the response comes back, the
     * voice system has handled the request and is ready to switch; at that point the
     * application can start a new non-voice activity or finish.  Be sure when starting the new
     * activity to use {@link android.content.Intent#FLAG_ACTIVITY_NEW_TASK
     * Intent.FLAG_ACTIVITY_NEW_TASK} to keep the new activity out of the current voice
     * interaction task.
     */
    public static class CompleteVoiceRequest extends android.app.VoiceInteractor.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        final android.os.Bundle mExtras;

        /**
         * Create a new completed voice interaction request.
         *
         * @param prompt
         * 		Optional message to speak to the user about the completion status of
         * 		the task or null if nothing should be spoken.
         * @param extras
         * 		Additional optional information or null.
         */
        public CompleteVoiceRequest(@android.annotation.Nullable
        android.app.VoiceInteractor.Prompt prompt, @android.annotation.Nullable
        android.os.Bundle extras) {
            mPrompt = prompt;
            mExtras = extras;
        }

        /**
         * Create a new completed voice interaction request.
         *
         * @param message
         * 		Optional message to speak to the user about the completion status of
         * 		the task or null if nothing should be spoken.
         * @param extras
         * 		Additional optional information or null.
         * @unknown 
         */
        public CompleteVoiceRequest(java.lang.CharSequence message, android.os.Bundle extras) {
            mPrompt = (message != null) ? new android.app.VoiceInteractor.Prompt(message) : null;
            mExtras = extras;
        }

        public void onCompleteResult(android.os.Bundle result) {
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
            if (mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(mExtras);
            }
        }

        java.lang.String getRequestTypeName() {
            return "CompleteVoice";
        }

        com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException {
            return interactor.startCompleteVoice(packageName, callback, mPrompt, mExtras);
        }
    }

    /**
     * Reports that the current interaction can not be complete with voice, so the
     * application will need to switch to a traditional input UI.  Applications should
     * only use this when they need to completely bail out of the voice interaction
     * and switch to a traditional UI.  When the response comes back, the voice
     * system has handled the request and is ready to switch; at that point the application
     * can start a new non-voice activity.  Be sure when starting the new activity
     * to use {@link android.content.Intent#FLAG_ACTIVITY_NEW_TASK
     * Intent.FLAG_ACTIVITY_NEW_TASK} to keep the new activity out of the current voice
     * interaction task.
     */
    public static class AbortVoiceRequest extends android.app.VoiceInteractor.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        final android.os.Bundle mExtras;

        /**
         * Create a new voice abort request.
         *
         * @param prompt
         * 		Optional message to speak to the user indicating why the task could
         * 		not be completed by voice or null if nothing should be spoken.
         * @param extras
         * 		Additional optional information or null.
         */
        public AbortVoiceRequest(@android.annotation.Nullable
        android.app.VoiceInteractor.Prompt prompt, @android.annotation.Nullable
        android.os.Bundle extras) {
            mPrompt = prompt;
            mExtras = extras;
        }

        /**
         * Create a new voice abort request.
         *
         * @param message
         * 		Optional message to speak to the user indicating why the task could
         * 		not be completed by voice or null if nothing should be spoken.
         * @param extras
         * 		Additional optional information or null.
         * @unknown 
         */
        public AbortVoiceRequest(java.lang.CharSequence message, android.os.Bundle extras) {
            mPrompt = (message != null) ? new android.app.VoiceInteractor.Prompt(message) : null;
            mExtras = extras;
        }

        public void onAbortResult(android.os.Bundle result) {
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
            if (mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(mExtras);
            }
        }

        java.lang.String getRequestTypeName() {
            return "AbortVoice";
        }

        com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException {
            return interactor.startAbortVoice(packageName, callback, mPrompt, mExtras);
        }
    }

    /**
     * Execute a vendor-specific command using the trusted system VoiceInteractionService.
     * This allows an Activity to request additional information from the user needed to
     * complete an action (e.g. booking a table might have several possible times that the
     * user could select from or an app might need the user to agree to a terms of service).
     * The result of the confirmation will be returned through an asynchronous call to
     * either {@link #onCommandResult(boolean, android.os.Bundle)} or
     * {@link #onCancel()}.
     *
     * <p>The command is a string that describes the generic operation to be performed.
     * The command will determine how the properties in extras are interpreted and the set of
     * available commands is expected to grow over time.  An example might be
     * "com.google.voice.commands.REQUEST_NUMBER_BAGS" to request the number of bags as part of
     * airline check-in.  (This is not an actual working example.)
     */
    public static class CommandRequest extends android.app.VoiceInteractor.Request {
        final java.lang.String mCommand;

        final android.os.Bundle mArgs;

        /**
         * Create a new generic command request.
         *
         * @param command
         * 		The desired command to perform.
         * @param args
         * 		Additional arguments to control execution of the command.
         */
        public CommandRequest(java.lang.String command, android.os.Bundle args) {
            mCommand = command;
            mArgs = args;
        }

        /**
         * Results for CommandRequest can be returned in partial chunks.
         * The isCompleted is set to true iff all results have been returned, indicating the
         * CommandRequest has completed.
         */
        public void onCommandResult(boolean isCompleted, android.os.Bundle result) {
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mCommand=");
            writer.println(mCommand);
            if (mArgs != null) {
                writer.print(prefix);
                writer.print("mArgs=");
                writer.println(mArgs);
            }
        }

        java.lang.String getRequestTypeName() {
            return "Command";
        }

        com.android.internal.app.IVoiceInteractorRequest submit(com.android.internal.app.IVoiceInteractor interactor, java.lang.String packageName, com.android.internal.app.IVoiceInteractorCallback callback) throws android.os.RemoteException {
            return interactor.startCommand(packageName, callback, mCommand, mArgs);
        }
    }

    /**
     * A set of voice prompts to use with the voice interaction system to confirm an action, select
     * an option, or do similar operations. Multiple voice prompts may be provided for variety. A
     * visual prompt must be provided, which might not match the spoken version. For example, the
     * confirmation "Are you sure you want to purchase this item?" might use a visual label like
     * "Purchase item".
     */
    public static class Prompt implements android.os.Parcelable {
        // Mandatory voice prompt. Must contain at least one item, which must not be null.
        private final java.lang.CharSequence[] mVoicePrompts;

        // Mandatory visual prompt.
        private final java.lang.CharSequence mVisualPrompt;

        /**
         * Constructs a prompt set.
         *
         * @param voicePrompts
         * 		An array of one or more voice prompts. Must not be empty or null.
         * @param visualPrompt
         * 		A prompt to display on the screen. Must not be null.
         */
        public Prompt(@android.annotation.NonNull
        java.lang.CharSequence[] voicePrompts, @android.annotation.NonNull
        java.lang.CharSequence visualPrompt) {
            if (voicePrompts == null) {
                throw new java.lang.NullPointerException("voicePrompts must not be null");
            }
            if (voicePrompts.length == 0) {
                throw new java.lang.IllegalArgumentException("voicePrompts must not be empty");
            }
            if (visualPrompt == null) {
                throw new java.lang.NullPointerException("visualPrompt must not be null");
            }
            this.mVoicePrompts = voicePrompts;
            this.mVisualPrompt = visualPrompt;
        }

        /**
         * Constructs a prompt set with single prompt used for all interactions. This is most useful
         * in test apps. Non-trivial apps should prefer the detailed constructor.
         */
        public Prompt(@android.annotation.NonNull
        java.lang.CharSequence prompt) {
            this.mVoicePrompts = new java.lang.CharSequence[]{ prompt };
            this.mVisualPrompt = prompt;
        }

        /**
         * Returns a prompt to use for voice interactions.
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getVoicePromptAt(int index) {
            return mVoicePrompts[index];
        }

        /**
         * Returns the number of different voice prompts.
         */
        public int countVoicePrompts() {
            return mVoicePrompts.length;
        }

        /**
         * Returns the prompt to use for visual display.
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getVisualPrompt() {
            return mVisualPrompt;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            android.util.DebugUtils.buildShortClassTag(this, sb);
            if ((((mVisualPrompt != null) && (mVoicePrompts != null)) && (mVoicePrompts.length == 1)) && mVisualPrompt.equals(mVoicePrompts[0])) {
                sb.append(" ");
                sb.append(mVisualPrompt);
            } else {
                if (mVisualPrompt != null) {
                    sb.append(" visual=");
                    sb.append(mVisualPrompt);
                }
                if (mVoicePrompts != null) {
                    sb.append(", voice=");
                    for (int i = 0; i < mVoicePrompts.length; i++) {
                        if (i > 0)
                            sb.append(" | ");

                        sb.append(mVoicePrompts[i]);
                    }
                }
            }
            sb.append('}');
            return sb.toString();
        }

        /**
         * Constructor to support Parcelable behavior.
         */
        Prompt(android.os.Parcel in) {
            mVoicePrompts = in.readCharSequenceArray();
            mVisualPrompt = in.readCharSequence();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeCharSequenceArray(mVoicePrompts);
            dest.writeCharSequence(mVisualPrompt);
        }

        public static final android.os.Parcelable.Creator<android.app.VoiceInteractor.Prompt> CREATOR = new android.os.Parcelable.Creator<android.app.VoiceInteractor.Prompt>() {
            public android.app.VoiceInteractor.Prompt createFromParcel(android.os.Parcel in) {
                return new android.app.VoiceInteractor.Prompt(in);
            }

            public android.app.VoiceInteractor.Prompt[] newArray(int size) {
                return new android.app.VoiceInteractor.Prompt[size];
            }
        };
    }

    VoiceInteractor(com.android.internal.app.IVoiceInteractor interactor, android.content.Context context, android.app.Activity activity, android.os.Looper looper) {
        mInteractor = interactor;
        mContext = context;
        mActivity = activity;
        mHandlerCaller = new com.android.internal.os.HandlerCaller(context, looper, mHandlerCallerCallback, true);
    }

    android.app.VoiceInteractor.Request pullRequest(com.android.internal.app.IVoiceInteractorRequest request, boolean complete) {
        synchronized(mActiveRequests) {
            android.app.VoiceInteractor.Request req = mActiveRequests.get(request.asBinder());
            if ((req != null) && complete) {
                mActiveRequests.remove(request.asBinder());
            }
            return req;
        }
    }

    private java.util.ArrayList<android.app.VoiceInteractor.Request> makeRequestList() {
        final int N = mActiveRequests.size();
        if (N < 1) {
            return null;
        }
        java.util.ArrayList<android.app.VoiceInteractor.Request> list = new java.util.ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            list.add(mActiveRequests.valueAt(i));
        }
        return list;
    }

    void attachActivity(android.app.Activity activity) {
        mRetaining = false;
        if (mActivity == activity) {
            return;
        }
        mContext = activity;
        mActivity = activity;
        java.util.ArrayList<android.app.VoiceInteractor.Request> reqs = makeRequestList();
        if (reqs != null) {
            for (int i = 0; i < reqs.size(); i++) {
                android.app.VoiceInteractor.Request req = reqs.get(i);
                req.mContext = activity;
                req.mActivity = activity;
                req.onAttached(activity);
            }
        }
    }

    void retainInstance() {
        mRetaining = true;
    }

    void detachActivity() {
        java.util.ArrayList<android.app.VoiceInteractor.Request> reqs = makeRequestList();
        if (reqs != null) {
            for (int i = 0; i < reqs.size(); i++) {
                android.app.VoiceInteractor.Request req = reqs.get(i);
                req.onDetached();
                req.mActivity = null;
                req.mContext = null;
            }
        }
        if (!mRetaining) {
            reqs = makeRequestList();
            if (reqs != null) {
                for (int i = 0; i < reqs.size(); i++) {
                    android.app.VoiceInteractor.Request req = reqs.get(i);
                    req.cancel();
                }
            }
            mActiveRequests.clear();
        }
        mContext = null;
        mActivity = null;
    }

    public boolean submitRequest(android.app.VoiceInteractor.Request request) {
        return submitRequest(request, null);
    }

    /**
     * Submit a new {@link Request} to the voice interaction service.  The request must be
     * one of the available subclasses -- {@link ConfirmationRequest}, {@link PickOptionRequest},
     * {@link CompleteVoiceRequest}, {@link AbortVoiceRequest}, or {@link CommandRequest}.
     *
     * @param request
     * 		The desired request to submit.
     * @param name
     * 		An optional name for this request, or null. This can be used later with
     * 		{@link #getActiveRequests} and {@link #getActiveRequest} to find the request.
     * @return Returns true of the request was successfully submitted, else false.
     */
    public boolean submitRequest(android.app.VoiceInteractor.Request request, java.lang.String name) {
        try {
            if (request.mRequestInterface != null) {
                throw new java.lang.IllegalStateException(("Given " + request) + " is already active");
            }
            com.android.internal.app.IVoiceInteractorRequest ireq = request.submit(mInteractor, mContext.getOpPackageName(), mCallback);
            request.mRequestInterface = ireq;
            request.mContext = mContext;
            request.mActivity = mActivity;
            request.mName = name;
            synchronized(mActiveRequests) {
                mActiveRequests.put(ireq.asBinder(), request);
            }
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.app.VoiceInteractor.TAG, "Remove voice interactor service died", e);
            return false;
        }
    }

    /**
     * Return all currently active requests.
     */
    public android.app.VoiceInteractor.Request[] getActiveRequests() {
        synchronized(mActiveRequests) {
            final int N = mActiveRequests.size();
            if (N <= 0) {
                return android.app.VoiceInteractor.NO_REQUESTS;
            }
            android.app.VoiceInteractor.Request[] requests = new android.app.VoiceInteractor.Request[N];
            for (int i = 0; i < N; i++) {
                requests[i] = mActiveRequests.valueAt(i);
            }
            return requests;
        }
    }

    /**
     * Return any currently active request that was submitted with the given name.
     *
     * @param name
     * 		The name used to submit the request, as per
     * 		{@link #submitRequest(android.app.VoiceInteractor.Request, String)}.
     * @return Returns the active request with that name, or null if there was none.
     */
    public android.app.VoiceInteractor.Request getActiveRequest(java.lang.String name) {
        synchronized(mActiveRequests) {
            final int N = mActiveRequests.size();
            for (int i = 0; i < N; i++) {
                android.app.VoiceInteractor.Request req = mActiveRequests.valueAt(i);
                if ((name == req.getName()) || ((name != null) && name.equals(req.getName()))) {
                    return req;
                }
            }
        }
        return null;
    }

    /**
     * Queries the supported commands available from the VoiceInteractionService.
     * The command is a string that describes the generic operation to be performed.
     * An example might be "org.example.commands.PICK_DATE" to ask the user to pick
     * a date.  (Note: This is not an actual working example.)
     *
     * @param commands
     * 		The array of commands to query for support.
     * @return Array of booleans indicating whether each command is supported or not.
     */
    public boolean[] supportsCommands(java.lang.String[] commands) {
        try {
            boolean[] res = mInteractor.supportsCommands(mContext.getOpPackageName(), commands);
            if (android.app.VoiceInteractor.DEBUG)
                android.util.Log.d(android.app.VoiceInteractor.TAG, (("supportsCommands: cmds=" + commands) + " res=") + res);

            return res;
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Voice interactor has died", e);
        }
    }

    void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        java.lang.String innerPrefix = prefix + "    ";
        if (mActiveRequests.size() > 0) {
            writer.print(prefix);
            writer.println("Active voice requests:");
            for (int i = 0; i < mActiveRequests.size(); i++) {
                android.app.VoiceInteractor.Request req = mActiveRequests.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(req);
                req.dump(innerPrefix, fd, writer, args);
            }
        }
        writer.print(prefix);
        writer.println("VoiceInteractor misc state:");
        writer.print(prefix);
        writer.print("  mInteractor=");
        writer.println(mInteractor.asBinder());
        writer.print(prefix);
        writer.print("  mActivity=");
        writer.println(mActivity);
    }
}

