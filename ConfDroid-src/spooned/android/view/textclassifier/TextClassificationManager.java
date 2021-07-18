/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Interface to the text classification service.
 */
@android.annotation.SystemService(android.content.Context.TEXT_CLASSIFICATION_SERVICE)
public final class TextClassificationManager {
    private static final java.lang.String LOG_TAG = "TextClassificationManager";

    private static final android.view.textclassifier.TextClassificationConstants sDefaultSettings = new android.view.textclassifier.TextClassificationConstants(() -> null);

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.view.textclassifier.TextClassificationSessionFactory mDefaultSessionFactory = ( classificationContext) -> new android.view.textclassifier.TextClassificationSession(classificationContext, getTextClassifier());

    private final android.content.Context mContext;

    private final android.view.textclassifier.TextClassificationManager.SettingsObserver mSettingsObserver;

    @com.android.internal.annotations.GuardedBy("mLock")
    @android.annotation.Nullable
    private android.view.textclassifier.TextClassifier mCustomTextClassifier;

    @com.android.internal.annotations.GuardedBy("mLock")
    @android.annotation.Nullable
    private android.view.textclassifier.TextClassifier mLocalTextClassifier;

    @com.android.internal.annotations.GuardedBy("mLock")
    @android.annotation.Nullable
    private android.view.textclassifier.TextClassifier mSystemTextClassifier;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.TextClassificationSessionFactory mSessionFactory;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.TextClassificationConstants mSettings;

    /**
     *
     *
     * @unknown 
     */
    public TextClassificationManager(android.content.Context context) {
        mContext = com.android.internal.util.Preconditions.checkNotNull(context);
        mSessionFactory = mDefaultSessionFactory;
        mSettingsObserver = new android.view.textclassifier.TextClassificationManager.SettingsObserver(this);
    }

    /**
     * Returns the text classifier that was set via {@link #setTextClassifier(TextClassifier)}.
     * If this is null, this method returns a default text classifier (i.e. either the system text
     * classifier if one exists, or a local text classifier running in this process.)
     * <p>
     * Note that requests to the TextClassifier may be handled in an OEM-provided process rather
     * than in the calling app's process.
     *
     * @see #setTextClassifier(TextClassifier)
     */
    @android.annotation.NonNull
    public android.view.textclassifier.TextClassifier getTextClassifier() {
        synchronized(mLock) {
            if (mCustomTextClassifier != null) {
                return mCustomTextClassifier;
            } else
                if (isSystemTextClassifierEnabled()) {
                    return getSystemTextClassifier();
                } else {
                    return getLocalTextClassifier();
                }

        }
    }

    /**
     * Sets the text classifier.
     * Set to null to use the system default text classifier.
     * Set to {@link TextClassifier#NO_OP} to disable text classifier features.
     */
    public void setTextClassifier(@android.annotation.Nullable
    android.view.textclassifier.TextClassifier textClassifier) {
        synchronized(mLock) {
            mCustomTextClassifier = textClassifier;
        }
    }

    /**
     * Returns a specific type of text classifier.
     * If the specified text classifier cannot be found, this returns {@link TextClassifier#NO_OP}.
     *
     * @see TextClassifier#LOCAL
     * @see TextClassifier#SYSTEM
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.view.textclassifier.TextClassifier getTextClassifier(@android.view.textclassifier.TextClassifier.TextClassifierType
    int type) {
        switch (type) {
            case android.view.textclassifier.TextClassifier.LOCAL :
                return getLocalTextClassifier();
            default :
                return getSystemTextClassifier();
        }
    }

    private android.view.textclassifier.TextClassificationConstants getSettings() {
        synchronized(mLock) {
            if (mSettings == null) {
                mSettings = new android.view.textclassifier.TextClassificationConstants(() -> Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.TEXT_CLASSIFIER_CONSTANTS));
            }
            return mSettings;
        }
    }

    /**
     * Call this method to start a text classification session with the given context.
     * A session is created with a context helping the classifier better understand
     * what the user needs and consists of queries and feedback events. The queries
     * are directly related to providing useful functionality to the user and the events
     * are a feedback loop back to the classifier helping it learn and better serve
     * future queries.
     *
     * <p> All interactions with the returned classifier are considered part of a single
     * session and are logically grouped. For example, when a text widget is focused
     * all user interactions around text editing (selection, editing, etc) can be
     * grouped together to allow the classifier get better.
     *
     * @param classificationContext
     * 		The context in which classification would occur
     * @return An instance to perform classification in the given context
     */
    @android.annotation.NonNull
    public android.view.textclassifier.TextClassifier createTextClassificationSession(@android.annotation.NonNull
    android.view.textclassifier.TextClassificationContext classificationContext) {
        com.android.internal.util.Preconditions.checkNotNull(classificationContext);
        final android.view.textclassifier.TextClassifier textClassifier = mSessionFactory.createTextClassificationSession(classificationContext);
        com.android.internal.util.Preconditions.checkNotNull(textClassifier, "Session Factory should never return null");
        return textClassifier;
    }

    /**
     *
     *
     * @see #createTextClassificationSession(TextClassificationContext, TextClassifier)
     * @unknown 
     */
    public android.view.textclassifier.TextClassifier createTextClassificationSession(android.view.textclassifier.TextClassificationContext classificationContext, android.view.textclassifier.TextClassifier textClassifier) {
        com.android.internal.util.Preconditions.checkNotNull(classificationContext);
        com.android.internal.util.Preconditions.checkNotNull(textClassifier);
        return new android.view.textclassifier.TextClassificationSession(classificationContext, textClassifier);
    }

    /**
     * Sets a TextClassificationSessionFactory to be used to create session-aware TextClassifiers.
     *
     * @param factory
     * 		the textClassification session factory. If this is null, the default factory
     * 		will be used.
     */
    public void setTextClassificationSessionFactory(@android.annotation.Nullable
    android.view.textclassifier.TextClassificationSessionFactory factory) {
        synchronized(mLock) {
            if (factory != null) {
                mSessionFactory = factory;
            } else {
                mSessionFactory = mDefaultSessionFactory;
            }
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            // Note that fields could be null if the constructor threw.
            if (mSettingsObserver != null) {
                getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsObserver);
                if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                    android.provider.DeviceConfig.removeOnPropertiesChangedListener(mSettingsObserver);
                }
            }
        } finally {
            super.finalize();
        }
    }

    private android.view.textclassifier.TextClassifier getSystemTextClassifier() {
        synchronized(mLock) {
            if ((mSystemTextClassifier == null) && isSystemTextClassifierEnabled()) {
                try {
                    mSystemTextClassifier = new android.view.textclassifier.SystemTextClassifier(mContext, getSettings());
                    android.view.textclassifier.Log.d(android.view.textclassifier.TextClassificationManager.LOG_TAG, "Initialized SystemTextClassifier");
                } catch (android.os.ServiceManager e) {
                    android.view.textclassifier.Log.e(android.view.textclassifier.TextClassificationManager.LOG_TAG, "Could not initialize SystemTextClassifier", e);
                }
            }
        }
        if (mSystemTextClassifier != null) {
            return mSystemTextClassifier;
        }
        return android.view.textclassifier.TextClassifier.NO_OP;
    }

    /**
     * Returns a local textclassifier, which is running in this process.
     */
    @android.annotation.NonNull
    private android.view.textclassifier.TextClassifier getLocalTextClassifier() {
        synchronized(mLock) {
            if (mLocalTextClassifier == null) {
                if (getSettings().isLocalTextClassifierEnabled()) {
                    mLocalTextClassifier = new android.view.textclassifier.TextClassifierImpl(mContext, getSettings(), android.view.textclassifier.TextClassifier.NO_OP);
                } else {
                    android.view.textclassifier.Log.d(android.view.textclassifier.TextClassificationManager.LOG_TAG, "Local TextClassifier disabled");
                    mLocalTextClassifier = android.view.textclassifier.TextClassifier.NO_OP;
                }
            }
            return mLocalTextClassifier;
        }
    }

    private boolean isSystemTextClassifierEnabled() {
        return getSettings().isSystemTextClassifierEnabled() && (android.service.textclassifier.TextClassifierService.getServiceComponentName(mContext) != null);
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public void invalidateForTesting() {
        invalidate();
    }

    private void invalidate() {
        synchronized(mLock) {
            mSettings = null;
            mLocalTextClassifier = null;
            mSystemTextClassifier = null;
        }
    }

    android.content.Context getApplicationContext() {
        return mContext.getApplicationContext() != null ? mContext.getApplicationContext() : mContext;
    }

    /**
     *
     *
     * @unknown *
     */
    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        getLocalTextClassifier().dump(pw);
        getSystemTextClassifier().dump(pw);
        getSettings().dump(pw);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.view.textclassifier.TextClassificationConstants getSettings(android.content.Context context) {
        com.android.internal.util.Preconditions.checkNotNull(context);
        final android.view.textclassifier.TextClassificationManager tcm = context.getSystemService(android.view.textclassifier.TextClassificationManager.class);
        if (tcm != null) {
            return tcm.getSettings();
        } else {
            // Use default settings if there is no tcm.
            return android.view.textclassifier.TextClassificationManager.sDefaultSettings;
        }
    }

    private static final class SettingsObserver extends android.database.ContentObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final java.lang.ref.WeakReference<android.view.textclassifier.TextClassificationManager> mTcm;

        SettingsObserver(android.view.textclassifier.TextClassificationManager tcm) {
            super(null);
            mTcm = new java.lang.ref.WeakReference<>(tcm);
            /* notifyForDescendants */
            tcm.getApplicationContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(Settings.Global.TEXT_CLASSIFIER_CONSTANTS), false, this);
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                android.provider.DeviceConfig.addOnPropertiesChangedListener(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, android.app.ActivityThread.currentApplication().getMainExecutor(), this);
            }
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            invalidateSettings();
        }

        @java.lang.Override
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            invalidateSettings();
        }

        private void invalidateSettings() {
            final android.view.textclassifier.TextClassificationManager tcm = mTcm.get();
            if (tcm != null) {
                tcm.invalidate();
            }
        }
    }
}

