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
package android.databinding.tool.reflection.java;


public class JavaTypeUtil extends android.databinding.tool.reflection.TypeUtil {
    @java.lang.Override
    public java.lang.String getDescription(android.databinding.tool.reflection.ModelClass modelClass) {
        return modelClass.getCanonicalName().replace('.', '/');
    }

    @java.lang.Override
    public java.lang.String getDescription(android.databinding.tool.reflection.ModelMethod modelMethod) {
        java.lang.reflect.Method method = ((android.databinding.tool.reflection.java.JavaMethod) (modelMethod)).mMethod;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(method.getName());
        sb.append("(");
        for (java.lang.Class param : method.getParameterTypes()) {
            sb.append(getDescription(param));
        }
        sb.append(")");
        sb.append(getDescription(method.getReturnType()));
        return sb.toString();
    }

    private java.lang.String getDescription(java.lang.Class klass) {
        if (klass == null) {
            throw new java.lang.UnsupportedOperationException();
        }
        if (boolean.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.BOOLEAN;
        }
        if (byte.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.BYTE;
        }
        if (short.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.SHORT;
        }
        if (int.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.INT;
        }
        if (long.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.LONG;
        }
        if (char.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.CHAR;
        }
        if (float.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.FLOAT;
        }
        if (double.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.DOUBLE;
        }
        if (void.class.equals(klass)) {
            return android.databinding.tool.reflection.TypeUtil.VOID;
        }
        if (java.lang.Object.class.isAssignableFrom(klass)) {
            return (android.databinding.tool.reflection.TypeUtil.CLASS_PREFIX + klass.getCanonicalName().replace('.', '/')) + android.databinding.tool.reflection.TypeUtil.CLASS_SUFFIX;
        }
        if (java.lang.reflect.Array.class.isAssignableFrom(klass)) {
            return android.databinding.tool.reflection.TypeUtil.ARRAY + getDescription(klass.getComponentType());
        }
        java.lang.UnsupportedOperationException ex = new java.lang.UnsupportedOperationException(("cannot understand type " + klass.toString()) + ", kind:");
        android.databinding.tool.util.L.e(ex, "cannot create JNI type for %s", klass.getCanonicalName());
        throw ex;
    }
}

