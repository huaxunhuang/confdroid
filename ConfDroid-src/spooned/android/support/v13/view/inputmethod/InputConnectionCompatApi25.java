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


final class InputConnectionCompatApi25 {
    public static boolean commitContent(android.view.inputmethod.InputConnection ic, java.lang.Object inputContentInfo, int flags, android.os.Bundle opts) {
        return ic.commitContent(((android.view.inputmethod.InputContentInfo) (inputContentInfo)), flags, opts);
    }

    public interface OnCommitContentListener {
        boolean onCommitContent(java.lang.Object inputContentInfo, int flags, android.os.Bundle opts);
    }

    public static android.view.inputmethod.InputConnection createWrapper(android.view.inputmethod.InputConnection ic, final android.support.v13.view.inputmethod.InputConnectionCompatApi25.OnCommitContentListener onCommitContentListener) {
        return /* mutable */
        new android.view.inputmethod.InputConnectionWrapper(ic, false) {
            @java.lang.Override
            public boolean commitContent(android.view.inputmethod.InputContentInfo inputContentInfo, int flags, android.os.Bundle opts) {
                if (onCommitContentListener.onCommitContent(inputContentInfo, flags, opts)) {
                    return true;
                }
                return super.commitContent(inputContentInfo, flags, opts);
            }
        };
    }
}

