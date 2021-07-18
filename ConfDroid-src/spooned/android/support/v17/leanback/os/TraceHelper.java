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
 * limitations under the License
 */
package android.support.v17.leanback.os;


/**
 * Helper for systrace events.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class TraceHelper {
    static final android.support.v17.leanback.os.TraceHelper.TraceHelperVersionImpl sImpl;

    interface TraceHelperVersionImpl {
        void beginSection(java.lang.String section);

        void endSection();
    }

    private static final class TraceHelperStubImpl implements android.support.v17.leanback.os.TraceHelper.TraceHelperVersionImpl {
        TraceHelperStubImpl() {
        }

        @java.lang.Override
        public void beginSection(java.lang.String section) {
        }

        @java.lang.Override
        public void endSection() {
        }
    }

    private static final class TraceHelperJbmr2Impl implements android.support.v17.leanback.os.TraceHelper.TraceHelperVersionImpl {
        TraceHelperJbmr2Impl() {
        }

        @java.lang.Override
        public void beginSection(java.lang.String section) {
            android.support.v17.leanback.os.TraceHelperJbmr2.beginSection(section);
        }

        @java.lang.Override
        public void endSection() {
            android.support.v17.leanback.os.TraceHelperJbmr2.endSection();
        }
    }

    private TraceHelper() {
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            sImpl = new android.support.v17.leanback.os.TraceHelper.TraceHelperJbmr2Impl();
        } else {
            sImpl = new android.support.v17.leanback.os.TraceHelper.TraceHelperStubImpl();
        }
    }

    public static void beginSection(java.lang.String section) {
        android.support.v17.leanback.os.TraceHelper.sImpl.beginSection(section);
    }

    public static void endSection() {
        android.support.v17.leanback.os.TraceHelper.sImpl.endSection();
    }
}

