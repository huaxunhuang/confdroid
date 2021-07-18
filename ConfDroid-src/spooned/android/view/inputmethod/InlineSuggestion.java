/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.inputmethod;


/**
 * This class represents an inline suggestion which is made by one app and can be embedded into the
 * UI of another. Suggestions may contain sensitive information not known to the host app which
 * needs to be protected from spoofing. To address that the suggestion view inflated on demand for
 * embedding is created in such a way that the hosting app cannot introspect its content and cannot
 * interact with it.
 */
// @formatter:on
// End of generated code
@com.android.internal.util.DataClass(genEqualsHashCode = true, genToString = true, genHiddenConstDefs = true, genHiddenConstructor = true)
public final class InlineSuggestion implements android.os.Parcelable {
    private static final java.lang.String TAG = "InlineSuggestion";

    @android.annotation.NonNull
    private final android.view.inputmethod.InlineSuggestionInfo mInfo;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.Nullable
    private final com.android.internal.view.inline.IInlineContentProvider mContentProvider;

    /**
     * Used to keep a strong reference to the callback so it doesn't get garbage collected.
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.ParcelWith(android.view.inputmethod.InlineSuggestion.InlineContentCallbackImplParceling.class)
    @android.annotation.Nullable
    private android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl mInlineContentCallback;

    /**
     * Creates a new {@link InlineSuggestion}, for testing purpose.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.NonNull
    public static android.view.inputmethod.InlineSuggestion newInlineSuggestion(@android.annotation.NonNull
    android.view.inputmethod.InlineSuggestionInfo info) {
        return /* inlineContentCallback */
        new android.view.inputmethod.InlineSuggestion(info, null, null);
    }

    /**
     * Creates a new {@link InlineSuggestion}.
     *
     * @unknown 
     */
    public InlineSuggestion(@android.annotation.NonNull
    android.view.inputmethod.InlineSuggestionInfo info, @android.annotation.Nullable
    com.android.internal.view.inline.IInlineContentProvider contentProvider) {
        /* inlineContentCallback */
        this(info, contentProvider, null);
    }

    /**
     * Inflates a view with the content of this suggestion at a specific size.
     *
     * <p> Each dimension of the size must satisfy one of the following conditions:
     *
     * <ol>
     *     <li>between {@link android.widget.inline.InlinePresentationSpec#getMinSize()} and
     * {@link android.widget.inline.InlinePresentationSpec#getMaxSize()} of the presentation spec
     * from {@code mInfo}
     *     <li>{@link ViewGroup.LayoutParams#WRAP_CONTENT}
     * </ol>
     *
     * If the size is set to {@link ViewGroup.LayoutParams#WRAP_CONTENT}, then the size of the inflated view will be just large
     * enough to fit the content, while still conforming to the min / max size specified by the
     * {@link android.widget.inline.InlinePresentationSpec}.
     *
     * <p> The caller can attach an {@link android.view.View.OnClickListener} and/or an
     * {@link android.view.View.OnLongClickListener} to the view in the {@code callback} to receive
     * click and long click events on the view.
     *
     * @param context
     * 		Context in which to inflate the view.
     * @param size
     * 		The size at which to inflate the suggestion. For each dimension, it maybe an
     * 		exact value or {@link ViewGroup.LayoutParams#WRAP_CONTENT}.
     * @param callback
     * 		Callback for receiving the inflated view, where the {@link ViewGroup.LayoutParams} of the view is set as the actual size of the
     * 		underlying remote view.
     * @throws IllegalArgumentException
     * 		If an invalid argument is passed.
     * @throws IllegalStateException
     * 		If this method is already called.
     */
    public void inflate(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.util.Size size, @android.annotation.NonNull
    @android.annotation.CallbackExecutor
    java.util.concurrent.Executor callbackExecutor, @android.annotation.NonNull
    java.util.function.Consumer<android.widget.inline.InlineContentView> callback) {
        final android.util.Size minSize = mInfo.getInlinePresentationSpec().getMinSize();
        final android.util.Size maxSize = mInfo.getInlinePresentationSpec().getMaxSize();
        if ((!android.view.inputmethod.InlineSuggestion.isValid(size.getWidth(), minSize.getWidth(), maxSize.getWidth())) || (!android.view.inputmethod.InlineSuggestion.isValid(size.getHeight(), minSize.getHeight(), maxSize.getHeight()))) {
            throw new java.lang.IllegalArgumentException(((("size is neither between min:" + minSize) + " and max:") + maxSize) + ", nor wrap_content");
        }
        mInlineContentCallback = getInlineContentCallback(context, callbackExecutor, callback);
        if (mContentProvider == null) {
            callbackExecutor.execute(() -> /* view */
            callback.accept(null));
            return;
        }
        try {
            mContentProvider.provideContent(size.getWidth(), size.getHeight(), new android.view.inputmethod.InlineSuggestion.InlineContentCallbackWrapper(mInlineContentCallback));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(android.view.inputmethod.InlineSuggestion.TAG, "Error creating suggestion content surface: " + e);
            callbackExecutor.execute(() -> /* view */
            callback.accept(null));
        }
    }

    /**
     * Returns true if the {@code actual} length is within [min, max] or is {@link ViewGroup.LayoutParams#WRAP_CONTENT}.
     */
    private static boolean isValid(int actual, int min, int max) {
        if (actual == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
            return true;
        }
        return (actual >= min) && (actual <= max);
    }

    private synchronized android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl getInlineContentCallback(android.content.Context context, java.util.concurrent.Executor callbackExecutor, java.util.function.Consumer<android.widget.inline.InlineContentView> callback) {
        if (mInlineContentCallback != null) {
            throw new java.lang.IllegalStateException("Already called #inflate()");
        }
        return new android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl(context, mContentProvider, callbackExecutor, callback);
    }

    /**
     * A wrapper class around the {@link InlineContentCallbackImpl} to ensure it's not strongly
     * reference by the remote system server process.
     */
    private static final class InlineContentCallbackWrapper extends com.android.internal.view.inline.IInlineContentCallback.Stub {
        private final java.lang.ref.WeakReference<android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl> mCallbackImpl;

        InlineContentCallbackWrapper(android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl callbackImpl) {
            mCallbackImpl = new java.lang.ref.WeakReference<>(callbackImpl);
        }

        @java.lang.Override
        @android.annotation.BinderThread
        public void onContent(android.view.SurfaceControlViewHost.SurfacePackage content, int width, int height) {
            final android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl callbackImpl = mCallbackImpl.get();
            if (callbackImpl != null) {
                callbackImpl.onContent(content, width, height);
            }
        }

        @java.lang.Override
        @android.annotation.BinderThread
        public void onClick() {
            final android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl callbackImpl = mCallbackImpl.get();
            if (callbackImpl != null) {
                callbackImpl.onClick();
            }
        }

        @java.lang.Override
        @android.annotation.BinderThread
        public void onLongClick() {
            final android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl callbackImpl = mCallbackImpl.get();
            if (callbackImpl != null) {
                callbackImpl.onLongClick();
            }
        }
    }

    /**
     * Handles the communication between the inline suggestion view in current (IME) process and
     * the remote view provided from the system server.
     *
     * <p>This class is thread safe, because all the outside calls are piped into a single
     * handler thread to be processed.
     */
    private static final class InlineContentCallbackImpl {
        @android.annotation.NonNull
        private final android.os.Handler mMainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

        @android.annotation.NonNull
        private final android.content.Context mContext;

        @android.annotation.Nullable
        private final com.android.internal.view.inline.IInlineContentProvider mInlineContentProvider;

        @android.annotation.NonNull
        private final java.util.concurrent.Executor mCallbackExecutor;

        /**
         * Callback from the client (IME) that will receive the inflated suggestion view. It'll
         * only be called once when the view SurfacePackage is first sent back to the client. Any
         * updates to the view due to attach to window and detach from window events will be
         * handled under the hood, transparent from the client.
         */
        @android.annotation.NonNull
        private final java.util.function.Consumer<android.widget.inline.InlineContentView> mCallback;

        /**
         * Indicates whether the first content has been received or not.
         */
        private boolean mFirstContentReceived = false;

        /**
         * The client (IME) side view which internally wraps a remote view. It'll be set when
         * {@link #onContent(SurfaceControlViewHost.SurfacePackage, int, int)} is called, which
         * should only happen once in the lifecycle of this inline suggestion instance.
         */
        @android.annotation.Nullable
        private android.widget.inline.InlineContentView mView;

        /**
         * The SurfacePackage pointing to the remote view. It's cached here to be sent to the next
         * available consumer.
         */
        @android.annotation.Nullable
        private android.view.SurfaceControlViewHost.SurfacePackage mSurfacePackage;

        /**
         * The callback (from the {@link InlineContentView}) which consumes the surface package.
         * It's cached here to be called when the SurfacePackage is returned from the remote
         * view owning process.
         */
        @android.annotation.Nullable
        private java.util.function.Consumer<android.view.SurfaceControlViewHost.SurfacePackage> mSurfacePackageConsumer;

        InlineContentCallbackImpl(@android.annotation.NonNull
        android.content.Context context, @android.annotation.Nullable
        com.android.internal.view.inline.IInlineContentProvider inlineContentProvider, @android.annotation.NonNull
        @android.annotation.CallbackExecutor
        java.util.concurrent.Executor callbackExecutor, @android.annotation.NonNull
        java.util.function.Consumer<android.widget.inline.InlineContentView> callback) {
            mContext = context;
            mInlineContentProvider = inlineContentProvider;
            mCallbackExecutor = callbackExecutor;
            mCallback = callback;
        }

        @android.annotation.BinderThread
        public void onContent(android.view.SurfaceControlViewHost.SurfacePackage content, int width, int height) {
            mMainHandler.post(() -> handleOnContent(content, width, height));
        }

        @android.annotation.MainThread
        private void handleOnContent(android.view.SurfaceControlViewHost.SurfacePackage content, int width, int height) {
            if (!mFirstContentReceived) {
                handleOnFirstContentReceived(content, width, height);
                mFirstContentReceived = true;
            } else {
                handleOnSurfacePackage(content);
            }
        }

        /**
         * Called when the view content is returned for the first time.
         */
        @android.annotation.MainThread
        private void handleOnFirstContentReceived(android.view.SurfaceControlViewHost.SurfacePackage content, int width, int height) {
            mSurfacePackage = content;
            if (mSurfacePackage == null) {
                mCallbackExecutor.execute(() -> /* view */
                mCallback.accept(null));
            } else {
                mView = new android.widget.inline.InlineContentView(mContext);
                mView.setLayoutParams(new android.view.ViewGroup.LayoutParams(width, height));
                mView.setChildSurfacePackageUpdater(getSurfacePackageUpdater());
                mCallbackExecutor.execute(() -> mCallback.accept(mView));
            }
        }

        /**
         * Called when any subsequent SurfacePackage is returned from the remote view owning
         * process.
         */
        @android.annotation.MainThread
        private void handleOnSurfacePackage(android.view.SurfaceControlViewHost.SurfacePackage surfacePackage) {
            if (surfacePackage == null) {
                return;
            }
            if ((mSurfacePackage != null) || (mSurfacePackageConsumer == null)) {
                // The surface package is not consumed, release it immediately.
                surfacePackage.release();
                try {
                    mInlineContentProvider.onSurfacePackageReleased();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(android.view.inputmethod.InlineSuggestion.TAG, "Error calling onSurfacePackageReleased(): " + e);
                }
                return;
            }
            mSurfacePackage = surfacePackage;
            if (mSurfacePackage == null) {
                return;
            }
            if (mSurfacePackageConsumer != null) {
                mSurfacePackageConsumer.accept(mSurfacePackage);
                mSurfacePackageConsumer = null;
            }
        }

        @android.annotation.MainThread
        private void handleOnSurfacePackageReleased() {
            if (mSurfacePackage != null) {
                try {
                    mInlineContentProvider.onSurfacePackageReleased();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(android.view.inputmethod.InlineSuggestion.TAG, "Error calling onSurfacePackageReleased(): " + e);
                }
                mSurfacePackage = null;
            }
            // Clear the pending surface package consumer, if any. This can happen if the IME
            // attaches the view to window and then quickly detaches it from the window, before
            // the surface package requested upon attaching to window was returned.
            mSurfacePackageConsumer = null;
        }

        @android.annotation.MainThread
        private void handleGetSurfacePackage(java.util.function.Consumer<android.view.SurfaceControlViewHost.SurfacePackage> consumer) {
            if (mSurfacePackage != null) {
                consumer.accept(mSurfacePackage);
            } else {
                mSurfacePackageConsumer = consumer;
                try {
                    mInlineContentProvider.requestSurfacePackage();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(android.view.inputmethod.InlineSuggestion.TAG, "Error calling getSurfacePackage(): " + e);
                    consumer.accept(null);
                    mSurfacePackageConsumer = null;
                }
            }
        }

        private android.widget.inline.InlineContentView.SurfacePackageUpdater getSurfacePackageUpdater() {
            return new android.widget.inline.InlineContentView.SurfacePackageUpdater() {
                @java.lang.Override
                public void onSurfacePackageReleased() {
                    mMainHandler.post(() -> handleOnSurfacePackageReleased());
                }

                @java.lang.Override
                public void getSurfacePackage(java.util.function.Consumer<android.view.SurfaceControlViewHost.SurfacePackage> consumer) {
                    mMainHandler.post(() -> handleGetSurfacePackage(consumer));
                }
            };
        }

        @android.annotation.BinderThread
        public void onClick() {
            mMainHandler.post(() -> {
                if ((mView != null) && mView.hasOnClickListeners()) {
                    mView.callOnClick();
                }
            });
        }

        @android.annotation.BinderThread
        public void onLongClick() {
            mMainHandler.post(() -> {
                if ((mView != null) && mView.hasOnLongClickListeners()) {
                    mView.performLongClick();
                }
            });
        }
    }

    /**
     * This class used to provide parcelling logic for InlineContentCallbackImpl. It's intended to
     * make this parcelling a no-op, since it can't be parceled and we don't need to parcel it.
     */
    private static class InlineContentCallbackImplParceling implements com.android.internal.util.Parcelling<android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl> {
        @java.lang.Override
        public void parcel(android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl item, android.os.Parcel dest, int parcelFlags) {
        }

        @java.lang.Override
        public android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl unparcel(android.os.Parcel source) {
            return null;
        }
    }

    // Code below generated by codegen v1.0.15.
    // 
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    // 
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/view/inputmethod/InlineSuggestion.java
    // 
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    // Settings > Editor > Code Style > Formatter Control
    // @formatter:off
    /**
     * Creates a new InlineSuggestion.
     *
     * @param inlineContentCallback
     * 		Used to keep a strong reference to the callback so it doesn't get garbage collected.
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    public InlineSuggestion(@android.annotation.NonNull
    android.view.inputmethod.InlineSuggestionInfo info, @android.annotation.Nullable
    com.android.internal.view.inline.IInlineContentProvider contentProvider, @android.annotation.Nullable
    android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl inlineContentCallback) {
        this.mInfo = info;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInfo);
        this.mContentProvider = contentProvider;
        this.mInlineContentCallback = inlineContentCallback;
        // onConstructed(); // You can define this method to get a callback
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public android.view.inputmethod.InlineSuggestionInfo getInfo() {
        return mInfo;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.Nullable
    public com.android.internal.view.inline.IInlineContentProvider getContentProvider() {
        return mContentProvider;
    }

    /**
     * Used to keep a strong reference to the callback so it doesn't get garbage collected.
     *
     * @unknown 
     */
    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.Nullable
    public android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl getInlineContentCallback() {
        return mInlineContentCallback;
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public java.lang.String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }
        return (((((((("InlineSuggestion { " + "info = ") + mInfo) + ", ") + "contentProvider = ") + mContentProvider) + ", ") + "inlineContentCallback = ") + mInlineContentCallback) + " }";
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(InlineSuggestion other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        @java.lang.SuppressWarnings("unchecked")
        android.view.inputmethod.InlineSuggestion that = ((android.view.inputmethod.InlineSuggestion) (o));
        // noinspection PointlessBooleanExpression
        return ((true && java.util.Objects.equals(mInfo, that.mInfo)) && java.util.Objects.equals(mContentProvider, that.mContentProvider)) && java.util.Objects.equals(mInlineContentCallback, that.mInlineContentCallback);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }
        int _hash = 1;
        _hash = (31 * _hash) + java.util.Objects.hashCode(mInfo);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mContentProvider);
        _hash = (31 * _hash) + java.util.Objects.hashCode(mInlineContentCallback);
        return _hash;
    }

    @com.android.internal.util.DataClass.Generated.Member
    static com.android.internal.util.Parcelling<android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl> sParcellingForInlineContentCallback = Parcelling.Cache.get(android.view.inputmethod.InlineSuggestion.InlineContentCallbackImplParceling.class);

    static {
        if (android.view.inputmethod.InlineSuggestion.sParcellingForInlineContentCallback == null) {
            android.view.inputmethod.InlineSuggestion.sParcellingForInlineContentCallback = Parcelling.Cache.put(new android.view.inputmethod.InlineSuggestion.InlineContentCallbackImplParceling());
        }
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }
        byte flg = 0;
        if (mContentProvider != null)
            flg |= 0x2;

        if (mInlineContentCallback != null)
            flg |= 0x4;

        dest.writeByte(flg);
        dest.writeTypedObject(mInfo, flags);
        if (mContentProvider != null)
            dest.writeStrongInterface(mContentProvider);

        android.view.inputmethod.InlineSuggestion.sParcellingForInlineContentCallback.parcel(mInlineContentCallback, dest, flags);
    }

    @java.lang.Override
    @com.android.internal.util.DataClass.Generated.Member
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    /* package-private */
    @java.lang.SuppressWarnings({ "unchecked", "RedundantCast" })
    @com.android.internal.util.DataClass.Generated.Member
    InlineSuggestion(@android.annotation.NonNull
    android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }
        byte flg = in.readByte();
        android.view.inputmethod.InlineSuggestionInfo info = ((android.view.inputmethod.InlineSuggestionInfo) (in.readTypedObject(this.CREATOR)));
        com.android.internal.view.inline.IInlineContentProvider contentProvider = ((flg & 0x2) == 0) ? null : IInlineContentProvider.Stub.asInterface(in.readStrongBinder());
        android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl inlineContentCallback = android.view.inputmethod.InlineSuggestion.sParcellingForInlineContentCallback.unparcel(in);
        this.mInfo = info;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, null, mInfo);
        this.mContentProvider = contentProvider;
        this.mInlineContentCallback = inlineContentCallback;
        // onConstructed(); // You can define this method to get a callback
    }

    @com.android.internal.util.DataClass.Generated.Member
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestion> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InlineSuggestion>() {
        @java.lang.Override
        public android.view.inputmethod.InlineSuggestion[] newArray(int size) {
            return new android.view.inputmethod.InlineSuggestion[size];
        }

        @java.lang.Override
        public android.view.inputmethod.InlineSuggestion createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            return new android.view.inputmethod.InlineSuggestion(in);
        }
    };

    @DataClass.Generated(time = 1589396017700L, codegenVersion = "1.0.15", sourceFile = "frameworks/base/core/java/android/view/inputmethod/InlineSuggestion.java", inputSignatures = "private static final  java.lang.String TAG\nprivate final @android.annotation.NonNull android.view.inputmethod.InlineSuggestionInfo mInfo\nprivate final @android.annotation.Nullable com.android.internal.view.inline.IInlineContentProvider mContentProvider\nprivate @com.android.internal.util.DataClass.ParcelWith(android.view.inputmethod.InlineSuggestion.InlineContentCallbackImplParceling.class) @android.annotation.Nullable android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl mInlineContentCallback\npublic static @android.annotation.TestApi @android.annotation.NonNull android.view.inputmethod.InlineSuggestion newInlineSuggestion(android.view.inputmethod.InlineSuggestionInfo)\npublic  void inflate(android.content.Context,android.util.Size,java.util.concurrent.Executor,java.util.function.Consumer<android.widget.inline.InlineContentView>)\nprivate static  boolean isValid(int,int,int)\nprivate synchronized  android.view.inputmethod.InlineSuggestion.InlineContentCallbackImpl getInlineContentCallback(android.content.Context,java.util.concurrent.Executor,java.util.function.Consumer<android.widget.inline.InlineContentView>)\nclass InlineSuggestion extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genEqualsHashCode=true, genToString=true, genHiddenConstDefs=true, genHiddenConstructor=true)")
    @java.lang.Deprecated
    private void __metadata() {
    }
}

