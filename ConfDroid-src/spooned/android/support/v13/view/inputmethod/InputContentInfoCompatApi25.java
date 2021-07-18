/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v13.view.inputmethod;


final class InputContentInfoCompatApi25 {
    public static java.lang.Object create(android.net.Uri contentUri, android.content.ClipDescription description, android.net.Uri linkUri) {
        return new android.view.inputmethod.InputContentInfo(contentUri, description, linkUri);
    }

    public static android.net.Uri getContentUri(java.lang.Object inputContentInfo) {
        return ((android.view.inputmethod.InputContentInfo) (inputContentInfo)).getContentUri();
    }

    public static android.content.ClipDescription getDescription(java.lang.Object inputContentInfo) {
        return ((android.view.inputmethod.InputContentInfo) (inputContentInfo)).getDescription();
    }

    public static android.net.Uri getLinkUri(java.lang.Object inputContentInfo) {
        return ((android.view.inputmethod.InputContentInfo) (inputContentInfo)).getLinkUri();
    }

    public static void requestPermission(java.lang.Object inputContentInfo) {
        ((android.view.inputmethod.InputContentInfo) (inputContentInfo)).requestPermission();
    }

    public static void releasePermission(java.lang.Object inputContentInfo) {
        ((android.view.inputmethod.InputContentInfo) (inputContentInfo)).releasePermission();
    }
}

