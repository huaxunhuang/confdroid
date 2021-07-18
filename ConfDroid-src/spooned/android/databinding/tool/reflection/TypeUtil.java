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
package android.databinding.tool.reflection;


public abstract class TypeUtil {
    public static final java.lang.String BYTE = "B";

    public static final java.lang.String CHAR = "C";

    public static final java.lang.String DOUBLE = "D";

    public static final java.lang.String FLOAT = "F";

    public static final java.lang.String INT = "I";

    public static final java.lang.String LONG = "J";

    public static final java.lang.String SHORT = "S";

    public static final java.lang.String VOID = "V";

    public static final java.lang.String BOOLEAN = "Z";

    public static final java.lang.String ARRAY = "[";

    public static final java.lang.String CLASS_PREFIX = "L";

    public static final java.lang.String CLASS_SUFFIX = ";";

    private static android.databinding.tool.reflection.TypeUtil sInstance;

    public abstract java.lang.String getDescription(android.databinding.tool.reflection.ModelClass modelClass);

    public abstract java.lang.String getDescription(android.databinding.tool.reflection.ModelMethod modelMethod);

    public static android.databinding.tool.reflection.TypeUtil getInstance() {
        if (android.databinding.tool.reflection.TypeUtil.sInstance == null) {
            android.databinding.tool.reflection.TypeUtil.sInstance = android.databinding.tool.reflection.ModelAnalyzer.getInstance().createTypeUtil();
        }
        return android.databinding.tool.reflection.TypeUtil.sInstance;
    }
}

