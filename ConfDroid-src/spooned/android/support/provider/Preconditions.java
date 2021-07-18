/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.provider;


/**
 * Simple static methods to be called at the start of your own methods to verify
 * correct arguments and state.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
final class Preconditions {
    static void checkArgument(boolean expression, java.lang.String message) {
        if (!expression) {
            throw new java.lang.IllegalArgumentException(message);
        }
    }

    static void checkArgumentNotNull(java.lang.Object object, java.lang.String message) {
        if (object == null) {
            throw new java.lang.IllegalArgumentException(message);
        }
    }

    static void checkArgumentEquals(java.lang.String expected, @android.support.annotation.Nullable
    java.lang.String actual, java.lang.String message) {
        if (!android.text.TextUtils.equals(expected, actual)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(message, java.lang.String.valueOf(expected), java.lang.String.valueOf(actual)));
        }
    }

    static void checkState(boolean expression, java.lang.String message) {
        if (!expression) {
            throw new java.lang.IllegalStateException(message);
        }
    }
}

