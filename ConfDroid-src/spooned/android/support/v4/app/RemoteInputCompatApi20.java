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


class RemoteInputCompatApi20 {
    static android.support.v4.app.RemoteInputCompatBase.RemoteInput[] toCompat(android.app.RemoteInput[] srcArray, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory factory) {
        if (srcArray == null) {
            return null;
        }
        android.support.v4.app.RemoteInputCompatBase.RemoteInput[] result = factory.newArray(srcArray.length);
        for (int i = 0; i < srcArray.length; i++) {
            android.app.RemoteInput src = srcArray[i];
            result[i] = factory.build(src.getResultKey(), src.getLabel(), src.getChoices(), src.getAllowFreeFormInput(), src.getExtras());
        }
        return result;
    }

    static android.app.RemoteInput[] fromCompat(android.support.v4.app.RemoteInputCompatBase.RemoteInput[] srcArray) {
        if (srcArray == null) {
            return null;
        }
        android.app.RemoteInput[] result = new android.app.RemoteInput[srcArray.length];
        for (int i = 0; i < srcArray.length; i++) {
            android.support.v4.app.RemoteInputCompatBase.RemoteInput src = srcArray[i];
            result[i] = new android.app.RemoteInput.Builder(src.getResultKey()).setLabel(src.getLabel()).setChoices(src.getChoices()).setAllowFreeFormInput(src.getAllowFreeFormInput()).addExtras(src.getExtras()).build();
        }
        return result;
    }

    static android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
        return android.app.RemoteInput.getResultsFromIntent(intent);
    }

    static void addResultsToIntent(android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
        android.app.RemoteInput.addResultsToIntent(android.support.v4.app.RemoteInputCompatApi20.fromCompat(remoteInputs), intent, results);
    }
}

