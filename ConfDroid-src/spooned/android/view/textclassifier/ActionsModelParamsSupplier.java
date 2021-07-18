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
package android.view.textclassifier;


/**
 * Parses the {@link Settings.Global#TEXT_CLASSIFIER_ACTION_MODEL_PARAMS} flag.
 *
 * @unknown 
 */
public final class ActionsModelParamsSupplier implements java.util.function.Supplier<android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams> {
    private static final java.lang.String TAG = android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG;

    @com.android.internal.annotations.VisibleForTesting
    static final java.lang.String KEY_REQUIRED_MODEL_VERSION = "required_model_version";

    @com.android.internal.annotations.VisibleForTesting
    static final java.lang.String KEY_REQUIRED_LOCALES = "required_locales";

    @com.android.internal.annotations.VisibleForTesting
    static final java.lang.String KEY_SERIALIZED_PRECONDITIONS = "serialized_preconditions";

    private final android.content.Context mAppContext;

    private final android.view.textclassifier.ActionsModelParamsSupplier.SettingsObserver mSettingsObserver;

    private final java.lang.Object mLock = new java.lang.Object();

    private final java.lang.Runnable mOnChangedListener;

    @android.annotation.Nullable
    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams mActionsModelParams;

    @com.android.internal.annotations.GuardedBy("mLock")
    private boolean mParsed = true;

    public ActionsModelParamsSupplier(android.content.Context context, @android.annotation.Nullable
    java.lang.Runnable onChangedListener) {
        mAppContext = com.android.internal.util.Preconditions.checkNotNull(context).getApplicationContext();
        mOnChangedListener = (onChangedListener == null) ? () -> {
        } : onChangedListener;
        mSettingsObserver = new android.view.textclassifier.ActionsModelParamsSupplier.SettingsObserver(mAppContext, () -> {
            synchronized(mLock) {
                android.view.textclassifier.Log.v(android.view.textclassifier.ActionsModelParamsSupplier.TAG, "Settings.Global.TEXT_CLASSIFIER_ACTION_MODEL_PARAMS is updated");
                mParsed = true;
                mOnChangedListener.run();
            }
        });
    }

    /**
     * Returns the parsed actions params or {@link ActionsModelParams#INVALID} if the value is
     * invalid.
     */
    @java.lang.Override
    public android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams get() {
        synchronized(mLock) {
            if (mParsed) {
                mActionsModelParams = parse(mAppContext.getContentResolver());
                mParsed = false;
            }
        }
        return mActionsModelParams;
    }

    private android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams parse(android.content.ContentResolver contentResolver) {
        java.lang.String settingStr = Settings.Global.getString(contentResolver, Settings.Global.TEXT_CLASSIFIER_ACTION_MODEL_PARAMS);
        if (android.text.TextUtils.isEmpty(settingStr)) {
            return android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID;
        }
        try {
            android.util.KeyValueListParser keyValueListParser = new android.util.KeyValueListParser(',');
            keyValueListParser.setString(settingStr);
            int version = keyValueListParser.getInt(android.view.textclassifier.ActionsModelParamsSupplier.KEY_REQUIRED_MODEL_VERSION, -1);
            if (version == (-1)) {
                android.view.textclassifier.Log.w(android.view.textclassifier.ActionsModelParamsSupplier.TAG, "ActionsModelParams.Parse, invalid model version");
                return android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID;
            }
            java.lang.String locales = keyValueListParser.getString(android.view.textclassifier.ActionsModelParamsSupplier.KEY_REQUIRED_LOCALES, null);
            if (locales == null) {
                android.view.textclassifier.Log.w(android.view.textclassifier.ActionsModelParamsSupplier.TAG, "ActionsModelParams.Parse, invalid locales");
                return android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID;
            }
            java.lang.String serializedPreconditionsStr = keyValueListParser.getString(android.view.textclassifier.ActionsModelParamsSupplier.KEY_SERIALIZED_PRECONDITIONS, null);
            if (serializedPreconditionsStr == null) {
                android.view.textclassifier.Log.w(android.view.textclassifier.ActionsModelParamsSupplier.TAG, "ActionsModelParams.Parse, invalid preconditions");
                return android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID;
            }
            byte[] serializedPreconditions = android.util.Base64.decode(serializedPreconditionsStr, Base64.NO_WRAP);
            return new android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams(version, locales, serializedPreconditions);
        } catch (java.lang.Throwable t) {
            android.view.textclassifier.Log.e(android.view.textclassifier.ActionsModelParamsSupplier.TAG, "Invalid TEXT_CLASSIFIER_ACTION_MODEL_PARAMS, ignore", t);
        }
        return android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mAppContext.getContentResolver().unregisterContentObserver(mSettingsObserver);
        } finally {
            super.finalize();
        }
    }

    /**
     * Represents the parsed result.
     */
    public static final class ActionsModelParams {
        public static final android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams INVALID = new android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams(-1, "", new byte[0]);

        /**
         * The required model version to apply {@code mSerializedPreconditions}.
         */
        private final int mRequiredModelVersion;

        /**
         * The required model locales to apply {@code mSerializedPreconditions}.
         */
        private final java.lang.String mRequiredModelLocales;

        /**
         * The serialized params that will be applied to the model file, if all requirements are
         * met. Do not modify.
         */
        private final byte[] mSerializedPreconditions;

        public ActionsModelParams(int requiredModelVersion, java.lang.String requiredModelLocales, byte[] serializedPreconditions) {
            mRequiredModelVersion = requiredModelVersion;
            mRequiredModelLocales = com.android.internal.util.Preconditions.checkNotNull(requiredModelLocales);
            mSerializedPreconditions = com.android.internal.util.Preconditions.checkNotNull(serializedPreconditions);
        }

        /**
         * Returns the serialized preconditions. Returns {@code null} if the the model in use does
         * not meet all the requirements listed in the {@code ActionsModelParams} or the params
         * are invalid.
         */
        @android.annotation.Nullable
        public byte[] getSerializedPreconditions(android.view.textclassifier.ModelFileManager.ModelFile modelInUse) {
            if (this == android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams.INVALID) {
                return null;
            }
            if (modelInUse.getVersion() != mRequiredModelVersion) {
                android.view.textclassifier.Log.w(android.view.textclassifier.ActionsModelParamsSupplier.TAG, java.lang.String.format("Not applying mSerializedPreconditions, required version=%d, actual=%d", mRequiredModelVersion, modelInUse.getVersion()));
                return null;
            }
            if (!java.util.Objects.equals(modelInUse.getSupportedLocalesStr(), mRequiredModelLocales)) {
                android.view.textclassifier.Log.w(android.view.textclassifier.ActionsModelParamsSupplier.TAG, java.lang.String.format("Not applying mSerializedPreconditions, required locales=%s, actual=%s", mRequiredModelLocales, modelInUse.getSupportedLocalesStr()));
                return null;
            }
            return mSerializedPreconditions;
        }
    }

    private static final class SettingsObserver extends android.database.ContentObserver {
        private final java.lang.ref.WeakReference<java.lang.Runnable> mOnChangedListener;

        SettingsObserver(android.content.Context appContext, java.lang.Runnable listener) {
            super(null);
            mOnChangedListener = new java.lang.ref.WeakReference<>(listener);
            /* notifyForDescendants */
            appContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(Settings.Global.TEXT_CLASSIFIER_ACTION_MODEL_PARAMS), false, this);
        }

        public void onChange(boolean selfChange) {
            if (mOnChangedListener.get() != null) {
                mOnChangedListener.get().run();
            }
        }
    }
}

