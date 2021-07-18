/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.result;


/**
 *
 *
 * @unknown 
 */
public class ParseTypeImpl implements android.content.pm.parsing.result.ParseInput , android.content.pm.parsing.result.ParseResult<java.lang.Object> {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingUtils.TAG;

    public static final boolean DEBUG_FILL_STACK_TRACE = false;

    public static final boolean DEBUG_LOG_ON_ERROR = false;

    public static final boolean DEBUG_THROW_ALL_ERRORS = false;

    @android.annotation.NonNull
    private android.content.pm.parsing.result.ParseInput.Callback mCallback;

    private java.lang.Object mResult;

    private int mErrorCode = android.content.pm.PackageManager.INSTALL_SUCCEEDED;

    @android.annotation.Nullable
    private java.lang.String mErrorMessage;

    @android.annotation.Nullable
    private java.lang.Exception mException;

    /**
     * Errors encountered before targetSdkVersion is known.
     * The size upper bound is the number of longs in {@link DeferredError}
     */
    @android.annotation.Nullable
    private android.util.ArrayMap<java.lang.Long, java.lang.String> mDeferredErrors = null;

    private java.lang.String mPackageName;

    private java.lang.Integer mTargetSdkVersion;

    /**
     * Specifically for {@link PackageManager#getPackageArchiveInfo(String, int)} where
     * {@link IPlatformCompat} cannot be used because the cross-package READ_COMPAT_CHANGE_CONFIG
     * permission cannot be obtained.
     */
    public static android.content.pm.parsing.result.ParseTypeImpl forParsingWithoutPlatformCompat() {
        return new android.content.pm.parsing.result.ParseTypeImpl(( changeId, packageName, targetSdkVersion) -> {
            int gateSdkVersion = android.content.pm.parsing.result.ParseInput.DeferredError.getTargetSdkForChange(changeId);
            if (gateSdkVersion == (-1)) {
                return false;
            }
            return targetSdkVersion > gateSdkVersion;
        });
    }

    /**
     * Assumes {@link Context#PLATFORM_COMPAT_SERVICE} is available to the caller. For use
     * with {@link android.content.pm.parsing.ApkLiteParseUtils} or similar where parsing is
     * done outside of {@link com.android.server.pm.PackageManagerService}.
     */
    public static android.content.pm.parsing.result.ParseTypeImpl forDefaultParsing() {
        com.android.internal.compat.IPlatformCompat platformCompat = IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.PLATFORM_COMPAT_SERVICE));
        return new android.content.pm.parsing.result.ParseTypeImpl(( changeId, packageName, targetSdkVersion) -> {
            android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo();
            appInfo.packageName = packageName;
            appInfo.targetSdkVersion = targetSdkVersion;
            try {
                return platformCompat.isChangeEnabled(changeId, appInfo);
            } catch (java.lang.Exception e) {
                // This shouldn't happen, but assume enforcement if it does
                android.util.Slog.wtf(android.content.pm.parsing.ParsingUtils.TAG, "IPlatformCompat query failed", e);
                return true;
            }
        });
    }

    /**
     *
     *
     * @param callback
     * 		if nullable, fallback to manual targetSdk > Q check
     */
    public ParseTypeImpl(@android.annotation.NonNull
    android.content.pm.parsing.result.ParseInput.Callback callback) {
        mCallback = callback;
    }

    public android.content.pm.parsing.result.ParseInput reset() {
        mResult = null;
        mErrorCode = android.content.pm.PackageManager.INSTALL_SUCCEEDED;
        mErrorMessage = null;
        mException = null;
        if (mDeferredErrors != null) {
            // If the memory was already allocated, don't bother freeing and re-allocating,
            // as this could occur hundreds of times depending on what the caller is doing and
            // how many APKs they're going through.
            mDeferredErrors.erase();
        }
        return this;
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> success(ResultType result) {
        if (mErrorCode != android.content.pm.PackageManager.INSTALL_SUCCEEDED) {
            android.util.Slog.wtf(android.content.pm.parsing.ParsingUtils.TAG, "Cannot set to success after set to error, was " + mErrorMessage, mException);
        }
        mResult = result;
        // noinspection unchecked
        return ((android.content.pm.parsing.result.ParseResult<ResultType>) (this));
    }

    @java.lang.Override
    public android.content.pm.parsing.result.ParseResult<?> deferError(@android.annotation.NonNull
    java.lang.String parseError, long deferredError) {
        if (android.content.pm.parsing.result.ParseTypeImpl.DEBUG_THROW_ALL_ERRORS) {
            return error(parseError);
        }
        if (mTargetSdkVersion != null) {
            if ((mDeferredErrors != null) && mDeferredErrors.containsKey(deferredError)) {
                // If the map already contains the key, that means it's already been checked and
                // found to be disabled. Otherwise it would've failed when mTargetSdkVersion was
                // set to non-null.
                return success(null);
            }
            if (mCallback.isChangeEnabled(deferredError, mPackageName, mTargetSdkVersion)) {
                return error(parseError);
            } else {
                if (mDeferredErrors == null) {
                    mDeferredErrors = new android.util.ArrayMap();
                }
                mDeferredErrors.put(deferredError, null);
                return success(null);
            }
        }
        if (mDeferredErrors == null) {
            mDeferredErrors = new android.util.ArrayMap();
        }
        // Only save the first occurrence of any particular error
        mDeferredErrors.putIfAbsent(deferredError, parseError);
        return success(null);
    }

    @java.lang.Override
    public android.content.pm.parsing.result.ParseResult<?> enableDeferredError(java.lang.String packageName, int targetSdkVersion) {
        mPackageName = packageName;
        mTargetSdkVersion = targetSdkVersion;
        int size = com.android.internal.util.CollectionUtils.size(mDeferredErrors);
        for (int index = size - 1; index >= 0; index--) {
            long changeId = mDeferredErrors.keyAt(index);
            java.lang.String errorMessage = mDeferredErrors.valueAt(index);
            if (mCallback.isChangeEnabled(changeId, mPackageName, mTargetSdkVersion)) {
                return error(errorMessage);
            } else {
                // No point holding onto the string, but need to maintain the key to signal
                // that the error was checked with isChangeEnabled and found to be disabled.
                mDeferredErrors.setValueAt(index, null);
            }
        }
        return success(null);
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> skip(@android.annotation.NonNull
    java.lang.String parseError) {
        return error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_SKIPPED, parseError);
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int parseError) {
        return error(parseError, null);
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(@android.annotation.NonNull
    java.lang.String parseError) {
        return error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, parseError);
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int errorCode, @android.annotation.Nullable
    java.lang.String errorMessage) {
        return error(errorCode, errorMessage, null);
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(android.content.pm.parsing.result.ParseResult<?> intentResult) {
        return error(intentResult.getErrorCode(), intentResult.getErrorMessage(), intentResult.getException());
    }

    @java.lang.Override
    public <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int errorCode, @android.annotation.Nullable
    java.lang.String errorMessage, java.lang.Exception exception) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
        mException = exception;
        if (android.content.pm.parsing.result.ParseTypeImpl.DEBUG_FILL_STACK_TRACE) {
            if (exception == null) {
                mException = new java.lang.Exception();
            }
        }
        if (android.content.pm.parsing.result.ParseTypeImpl.DEBUG_LOG_ON_ERROR) {
            java.lang.Exception exceptionToLog = (mException != null) ? mException : new java.lang.Exception();
            android.util.Log.w(android.content.pm.parsing.result.ParseTypeImpl.TAG, (("ParseInput set to error " + errorCode) + ", ") + errorMessage, exceptionToLog);
        }
        // noinspection unchecked
        return ((android.content.pm.parsing.result.ParseResult<ResultType>) (this));
    }

    @java.lang.Override
    public java.lang.Object getResult() {
        return mResult;
    }

    @java.lang.Override
    public boolean isSuccess() {
        return mErrorCode == android.content.pm.PackageManager.INSTALL_SUCCEEDED;
    }

    @java.lang.Override
    public boolean isError() {
        return !isSuccess();
    }

    @java.lang.Override
    public int getErrorCode() {
        return mErrorCode;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getErrorMessage() {
        return mErrorMessage;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.Exception getException() {
        return mException;
    }
}

