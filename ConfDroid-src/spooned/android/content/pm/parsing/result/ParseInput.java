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
 * Used as a method parameter which is then transformed into a {@link ParseResult}. This is
 * generalized as it doesn't matter what type this input is for. It's simply to hide the
 * methods of {@link ParseResult}.
 *
 * @unknown 
 */
public interface ParseInput {
    /**
     * Errors encountered during parsing may rely on the targetSDK version of the application to
     * determine whether or not to fail. These are passed into {@link #deferError(String, long)}
     * when encountered, and the implementation will handle how to defer the errors until the
     * targetSdkVersion is known and sent to {@link #enableDeferredError(String, int)}.
     *
     * All of these must be marked {@link ChangeId}, as that is the mechanism used to check if the
     * error must be propagated. This framework also allows developers to pre-disable specific
     * checks if they wish to target a newer SDK version in a development environment without
     * having to migrate their entire app to validate on a newer platform.
     */
    final class DeferredError {
        /**
         * Missing an "application" or "instrumentation" tag.
         */
        @android.compat.annotation.ChangeId
        @android.compat.annotation.EnabledAfter(targetSdkVersion = Build.VERSION_CODES.Q)
        public static final long MISSING_APP_TAG = 150776642;

        /**
         * An intent filter's actor or category is an empty string. A bug in the platform before R
         * allowed this to pass through without an error. This does not include cases when the
         * attribute is null/missing, as that has always been a failure.
         */
        @android.compat.annotation.ChangeId
        @android.compat.annotation.EnabledAfter(targetSdkVersion = Build.VERSION_CODES.Q)
        public static final long EMPTY_INTENT_ACTION_CATEGORY = 151163173;

        /**
         * The {@code resources.arsc} of one of the APKs being installed is compressed or not
         * aligned on a 4-byte boundary. Resource tables that cannot be memory mapped exert excess
         * memory pressure on the system and drastically slow down construction of
         * {@link android.content.res.Resources} objects.
         */
        @android.compat.annotation.ChangeId
        @android.compat.annotation.EnabledAfter(targetSdkVersion = Build.VERSION_CODES.Q)
        public static final long RESOURCES_ARSC_COMPRESSED = 132742131;

        /**
         * TODO(chiuwinson): This is required because PackageManager#getPackageArchiveInfo
         *   cannot read the targetSdk info from the changeId because it requires the
         *   READ_COMPAT_CHANGE_CONFIG which cannot be obtained automatically without entering the
         *   server process. This should be removed once an alternative is found, or if the API
         *   is removed.
         *
         * @return the targetSdk that this change is gated on (> check), or -1 if disabled
         */
        @android.annotation.IntRange(from = -1, to = java.lang.Integer.MAX_VALUE)
        public static int getTargetSdkForChange(long changeId) {
            if (((changeId == android.content.pm.parsing.result.ParseInput.DeferredError.MISSING_APP_TAG) || (changeId == android.content.pm.parsing.result.ParseInput.DeferredError.EMPTY_INTENT_ACTION_CATEGORY)) || (changeId == android.content.pm.parsing.result.ParseInput.DeferredError.RESOURCES_ARSC_COMPRESSED)) {
                return Build.VERSION_CODES.Q;
            }
            return -1;
        }
    }

    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> success(ResultType result);

    /**
     * Used for errors gated by {@link DeferredError}. Will return an error result if the
     * targetSdkVersion is already known and this must be returned as a real error. The result
     * contains null and should not be unwrapped.
     *
     * @see #error(String)
     */
    android.content.pm.parsing.result.ParseResult<?> deferError(@android.annotation.NonNull
    java.lang.String parseError, long deferredError);

    /**
     * Called after targetSdkVersion is known. Returns an error result if a previously deferred
     * error was registered. The result contains null and should not be unwrapped.
     */
    android.content.pm.parsing.result.ParseResult<?> enableDeferredError(java.lang.String packageName, int targetSdkVersion);

    /**
     * This will assign errorCode to {@link PackageManager#INSTALL_PARSE_FAILED_SKIPPED, used for
     * packages which should be ignored by the caller.
     *
     * @see #error(int, String, Exception)
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> skip(@android.annotation.NonNull
    java.lang.String parseError);

    /**
     *
     *
     * @see #error(int, String, Exception)
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int parseError);

    /**
     * This will assign errorCode to {@link PackageManager#INSTALL_PARSE_FAILED_MANIFEST_MALFORMED}.
     *
     * @see #error(int, String, Exception)
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(@android.annotation.NonNull
    java.lang.String parseError);

    /**
     *
     *
     * @see #error(int, String, Exception)
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int parseError, @android.annotation.Nullable
    java.lang.String errorMessage);

    /**
     * Marks this as an error result. When this method is called, the return value <b>must</b>
     * be returned to the exit of the parent method that took in this {@link ParseInput} as a
     * parameter.
     *
     * The calling site of that method is then expected to check the result for error, and
     * continue to bubble up if it is an error.
     *
     * If the result {@link ParseResult#isSuccess()}, then it can be used as-is, as
     * overlapping/consecutive successes are allowed.
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(int parseError, @android.annotation.Nullable
    java.lang.String errorMessage, @android.annotation.Nullable
    java.lang.Exception exception);

    /**
     * Moves the error in {@param result} to this input's type. In practice this does nothing
     * but cast the type of the {@link ParseResult} for type safety, since the parameter
     * and the receiver should be the same object.
     */
    <ResultType> android.content.pm.parsing.result.ParseResult<ResultType> error(android.content.pm.parsing.result.ParseResult<?> result);

    /**
     * Implemented instead of a direct reference to
     * {@link com.android.internal.compat.IPlatformCompat}, allowing caching and testing logic to
     * be separated out.
     */
    interface Callback {
        /**
         *
         *
         * @return true if the changeId should be enabled
         */
        boolean isChangeEnabled(long changeId, @android.annotation.NonNull
        java.lang.String packageName, int targetSdkVersion);
    }
}

