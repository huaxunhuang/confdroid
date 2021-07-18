/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Manages model files that are listed by the model files supplier.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class ModelFileManager {
    private final java.lang.Object mLock = new java.lang.Object();

    private final java.util.function.Supplier<java.util.List<android.view.textclassifier.ModelFileManager.ModelFile>> mModelFileSupplier;

    private java.util.List<android.view.textclassifier.ModelFileManager.ModelFile> mModelFiles;

    public ModelFileManager(java.util.function.Supplier<java.util.List<android.view.textclassifier.ModelFileManager.ModelFile>> modelFileSupplier) {
        mModelFileSupplier = com.android.internal.util.Preconditions.checkNotNull(modelFileSupplier);
    }

    /**
     * Returns an unmodifiable list of model files listed by the given model files supplier.
     * <p>
     * The result is cached.
     */
    public java.util.List<android.view.textclassifier.ModelFileManager.ModelFile> listModelFiles() {
        synchronized(mLock) {
            if (mModelFiles == null) {
                mModelFiles = java.util.Collections.unmodifiableList(mModelFileSupplier.get());
            }
            return mModelFiles;
        }
    }

    /**
     * Returns the best model file for the given localelist, {@code null} if nothing is found.
     *
     * @param localeList
     * 		the required locales, use {@code null} if there is no preference.
     */
    public android.view.textclassifier.ModelFileManager.ModelFile findBestModelFile(@android.annotation.Nullable
    android.os.LocaleList localeList) {
        final java.lang.String languages = ((localeList == null) || localeList.isEmpty()) ? android.os.LocaleList.getDefault().toLanguageTags() : localeList.toLanguageTags();
        final java.util.List<java.util.Locale.LanguageRange> languageRangeList = java.util.Locale.LanguageRange.parse(languages);
        android.view.textclassifier.ModelFileManager.ModelFile bestModel = null;
        for (android.view.textclassifier.ModelFileManager.ModelFile model : listModelFiles()) {
            if (model.isAnyLanguageSupported(languageRangeList)) {
                if (model.isPreferredTo(bestModel)) {
                    bestModel = model;
                }
            }
        }
        return bestModel;
    }

    /**
     * Default implementation of the model file supplier.
     */
    public static final class ModelFileSupplierImpl implements java.util.function.Supplier<java.util.List<android.view.textclassifier.ModelFileManager.ModelFile>> {
        private final java.io.File mUpdatedModelFile;

        private final java.io.File mFactoryModelDir;

        private final java.util.regex.Pattern mModelFilenamePattern;

        private final java.util.function.Function<java.lang.Integer, java.lang.Integer> mVersionSupplier;

        private final java.util.function.Function<java.lang.Integer, java.lang.String> mSupportedLocalesSupplier;

        public ModelFileSupplierImpl(java.io.File factoryModelDir, java.lang.String factoryModelFileNameRegex, java.io.File updatedModelFile, java.util.function.Function<java.lang.Integer, java.lang.Integer> versionSupplier, java.util.function.Function<java.lang.Integer, java.lang.String> supportedLocalesSupplier) {
            mUpdatedModelFile = com.android.internal.util.Preconditions.checkNotNull(updatedModelFile);
            mFactoryModelDir = com.android.internal.util.Preconditions.checkNotNull(factoryModelDir);
            mModelFilenamePattern = java.util.regex.Pattern.compile(com.android.internal.util.Preconditions.checkNotNull(factoryModelFileNameRegex));
            mVersionSupplier = com.android.internal.util.Preconditions.checkNotNull(versionSupplier);
            mSupportedLocalesSupplier = com.android.internal.util.Preconditions.checkNotNull(supportedLocalesSupplier);
        }

        @java.lang.Override
        public java.util.List<android.view.textclassifier.ModelFileManager.ModelFile> get() {
            final java.util.List<android.view.textclassifier.ModelFileManager.ModelFile> modelFiles = new java.util.ArrayList<>();
            // The update model has the highest precedence.
            if (mUpdatedModelFile.exists()) {
                final android.view.textclassifier.ModelFileManager.ModelFile updatedModel = createModelFile(mUpdatedModelFile);
                if (updatedModel != null) {
                    modelFiles.add(updatedModel);
                }
            }
            // Factory models should never have overlapping locales, so the order doesn't matter.
            if (mFactoryModelDir.exists() && mFactoryModelDir.isDirectory()) {
                final java.io.File[] files = mFactoryModelDir.listFiles();
                for (java.io.File file : files) {
                    final java.util.regex.Matcher matcher = mModelFilenamePattern.matcher(file.getName());
                    if (matcher.matches() && file.isFile()) {
                        final android.view.textclassifier.ModelFileManager.ModelFile model = createModelFile(file);
                        if (model != null) {
                            modelFiles.add(model);
                        }
                    }
                }
            }
            return modelFiles;
        }

        /**
         * Returns null if the path did not point to a compatible model.
         */
        @android.annotation.Nullable
        private android.view.textclassifier.ModelFileManager.ModelFile createModelFile(java.io.File file) {
            if (!file.exists()) {
                return null;
            }
            android.os.ParcelFileDescriptor modelFd = null;
            try {
                modelFd = android.os.ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                if (modelFd == null) {
                    return null;
                }
                final int modelFdInt = modelFd.getFd();
                final int version = mVersionSupplier.apply(modelFdInt);
                final java.lang.String supportedLocalesStr = mSupportedLocalesSupplier.apply(modelFdInt);
                if (supportedLocalesStr.isEmpty()) {
                    android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Ignoring " + file.getAbsolutePath());
                    return null;
                }
                final java.util.List<java.util.Locale> supportedLocales = new java.util.ArrayList<>();
                for (java.lang.String langTag : supportedLocalesStr.split(",")) {
                    supportedLocales.add(java.util.Locale.forLanguageTag(langTag));
                }
                return new android.view.textclassifier.ModelFileManager.ModelFile(file, version, supportedLocales, supportedLocalesStr, android.view.textclassifier.ModelFileManager.ModelFile.LANGUAGE_INDEPENDENT.equals(supportedLocalesStr));
            } catch (java.io.FileNotFoundException e) {
                android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Failed to find " + file.getAbsolutePath(), e);
                return null;
            } finally {
                android.view.textclassifier.ModelFileManager.ModelFileSupplierImpl.maybeCloseAndLogError(modelFd);
            }
        }

        /**
         * Closes the ParcelFileDescriptor, if non-null, and logs any errors that occur.
         */
        private static void maybeCloseAndLogError(@android.annotation.Nullable
        android.os.ParcelFileDescriptor fd) {
            if (fd == null) {
                return;
            }
            try {
                fd.close();
            } catch (java.io.IOException e) {
                android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Error closing file.", e);
            }
        }
    }

    /**
     * Describes TextClassifier model files on disk.
     */
    public static final class ModelFile {
        public static final java.lang.String LANGUAGE_INDEPENDENT = "*";

        private final java.io.File mFile;

        private final int mVersion;

        private final java.util.List<java.util.Locale> mSupportedLocales;

        private final java.lang.String mSupportedLocalesStr;

        private final boolean mLanguageIndependent;

        public ModelFile(java.io.File file, int version, java.util.List<java.util.Locale> supportedLocales, java.lang.String supportedLocalesStr, boolean languageIndependent) {
            mFile = com.android.internal.util.Preconditions.checkNotNull(file);
            mVersion = version;
            mSupportedLocales = com.android.internal.util.Preconditions.checkNotNull(supportedLocales);
            mSupportedLocalesStr = com.android.internal.util.Preconditions.checkNotNull(supportedLocalesStr);
            mLanguageIndependent = languageIndependent;
        }

        /**
         * Returns the absolute path to the model file.
         */
        public java.lang.String getPath() {
            return mFile.getAbsolutePath();
        }

        /**
         * Returns a name to use for id generation, effectively the name of the model file.
         */
        public java.lang.String getName() {
            return mFile.getName();
        }

        /**
         * Returns the version tag in the model's metadata.
         */
        public int getVersion() {
            return mVersion;
        }

        /**
         * Returns whether the language supports any language in the given ranges.
         */
        public boolean isAnyLanguageSupported(java.util.List<java.util.Locale.LanguageRange> languageRanges) {
            com.android.internal.util.Preconditions.checkNotNull(languageRanges);
            return mLanguageIndependent || (java.util.Locale.lookup(languageRanges, mSupportedLocales) != null);
        }

        /**
         * Returns an immutable lists of supported locales.
         */
        public java.util.List<java.util.Locale> getSupportedLocales() {
            return java.util.Collections.unmodifiableList(mSupportedLocales);
        }

        /**
         * Returns the original supported locals string read from the model file.
         */
        public java.lang.String getSupportedLocalesStr() {
            return mSupportedLocalesStr;
        }

        /**
         * Returns if this model file is preferred to the given one.
         */
        public boolean isPreferredTo(@android.annotation.Nullable
        android.view.textclassifier.ModelFileManager.ModelFile model) {
            // A model is preferred to no model.
            if (model == null) {
                return true;
            }
            // A language-specific model is preferred to a language independent
            // model.
            if ((!mLanguageIndependent) && model.mLanguageIndependent) {
                return true;
            }
            if (mLanguageIndependent && (!model.mLanguageIndependent)) {
                return false;
            }
            // A higher-version model is preferred.
            if (mVersion > model.getVersion()) {
                return true;
            }
            return false;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(getPath());
        }

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof android.view.textclassifier.ModelFileManager.ModelFile) {
                final android.view.textclassifier.ModelFileManager.ModelFile otherModel = ((android.view.textclassifier.ModelFileManager.ModelFile) (other));
                return android.text.TextUtils.equals(getPath(), otherModel.getPath());
            }
            return false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            final java.util.StringJoiner localesJoiner = new java.util.StringJoiner(",");
            for (java.util.Locale locale : mSupportedLocales) {
                localesJoiner.add(locale.toLanguageTag());
            }
            return java.lang.String.format(java.util.Locale.US, "ModelFile { path=%s name=%s version=%d locales=%s }", getPath(), getName(), mVersion, localesJoiner.toString());
        }
    }
}

