/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.opengl;


/**
 * An exception class for OpenGL errors.
 */
@java.lang.SuppressWarnings("serial")
public class GLException extends java.lang.RuntimeException {
    public GLException(final int error) {
        super(android.opengl.GLException.getErrorString(error));
        mError = error;
    }

    public GLException(final int error, final java.lang.String string) {
        super(string);
        mError = error;
    }

    private static java.lang.String getErrorString(int error) {
        java.lang.String errorString = android.opengl.GLU.gluErrorString(error);
        if (errorString == null) {
            errorString = "Unknown error 0x" + java.lang.Integer.toHexString(error);
        }
        return errorString;
    }

    int getError() {
        return mError;
    }

    private final int mError;
}

