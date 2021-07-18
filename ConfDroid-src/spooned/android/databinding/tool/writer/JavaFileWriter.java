/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.writer;


public abstract class JavaFileWriter {
    public abstract void writeToFile(java.lang.String canonicalName, java.lang.String contents);

    public void writeToFile(java.io.File exactPath, java.lang.String contents) {
        java.io.File parent = exactPath.getParentFile();
        parent.mkdirs();
        try {
            android.databinding.tool.util.L.d("writing file %s", exactPath.getAbsoluteFile());
            org.apache.commons.io.FileUtils.writeStringToFile(exactPath, contents);
        } catch (java.io.IOException e) {
            android.databinding.tool.util.L.e(e, "Could not write to %s", exactPath);
        }
    }
}

