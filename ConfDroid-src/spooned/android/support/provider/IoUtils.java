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
 * Simple static methods to perform common IO operations.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
final class IoUtils {
    static void closeQuietly(@android.support.annotation.Nullable
    java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (java.lang.RuntimeException e) {
                throw e;
            } catch (java.lang.Exception e) {
                // Ignore.
            }
        }
    }

    static void closeQuietly(@android.support.annotation.Nullable
    java.io.InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (java.lang.RuntimeException e) {
                throw e;
            } catch (java.lang.Exception e) {
                // Ignore.
            }
        }
    }
}

