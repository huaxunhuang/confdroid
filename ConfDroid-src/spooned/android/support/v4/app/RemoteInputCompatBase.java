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
package android.support.v4.app;


class RemoteInputCompatBase {
    public static abstract class RemoteInput {
        protected abstract java.lang.String getResultKey();

        protected abstract java.lang.CharSequence getLabel();

        protected abstract java.lang.CharSequence[] getChoices();

        protected abstract boolean getAllowFreeFormInput();

        protected abstract android.os.Bundle getExtras();

        public interface Factory {
            public android.support.v4.app.RemoteInputCompatBase.RemoteInput build(java.lang.String resultKey, java.lang.CharSequence label, java.lang.CharSequence[] choices, boolean allowFreeFormInput, android.os.Bundle extras);

            public android.support.v4.app.RemoteInputCompatBase.RemoteInput[] newArray(int length);
        }
    }
}

