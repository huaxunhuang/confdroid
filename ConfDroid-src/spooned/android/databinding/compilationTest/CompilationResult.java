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
package android.databinding.compilationTest;


public class CompilationResult {
    public final int resultCode;

    public final java.lang.String output;

    public final java.lang.String error;

    public CompilationResult(int resultCode, java.lang.String output, java.lang.String error) {
        this.resultCode = resultCode;
        this.output = output;
        this.error = error;
    }

    public boolean resultContainsText(java.lang.String text) {
        return (resultCode == 0) && (output.indexOf(text) > 0);
    }

    public boolean errorContainsText(java.lang.String text) {
        return (resultCode != 0) && (error.indexOf(text) > 0);
    }

    public android.databinding.tool.processing.ScopedException getBindingException() {
        java.util.List<android.databinding.tool.processing.ScopedException> errors = android.databinding.tool.processing.ScopedException.extractErrors(error);
        if (errors.isEmpty()) {
            return null;
        }
        org.junit.Assert.assertEquals(error, 1, errors.size());
        return errors.get(0);
    }

    public java.util.List<android.databinding.tool.processing.ScopedException> getBindingExceptions() {
        return android.databinding.tool.processing.ScopedException.extractErrors(error);
    }
}

