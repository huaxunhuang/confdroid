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


public class Callable {
    public enum Type {

        METHOD,
        FIELD;}

    public static final int DYNAMIC = 1;

    public static final int CAN_BE_INVALIDATED = 1 << 1;

    public static final int STATIC = 1 << 2;

    public final android.databinding.tool.reflection.Callable.Type type;

    public final java.lang.String name;

    public final java.lang.String setterName;

    public final android.databinding.tool.reflection.ModelClass resolvedType;

    private final int mFlags;

    private final int mParameterCount;

    public Callable(android.databinding.tool.reflection.Callable.Type type, java.lang.String name, java.lang.String setterName, android.databinding.tool.reflection.ModelClass resolvedType, int parameterCount, int flags) {
        this.type = type;
        this.name = name;
        this.resolvedType = resolvedType;
        mParameterCount = parameterCount;
        this.setterName = setterName;
        mFlags = flags;
    }

    public java.lang.String getTypeCodeName() {
        return resolvedType.toJavaCode();
    }

    public int getParameterCount() {
        return mParameterCount;
    }

    public boolean isDynamic() {
        return (mFlags & android.databinding.tool.reflection.Callable.DYNAMIC) != 0;
    }

    public boolean isStatic() {
        return (mFlags & android.databinding.tool.reflection.Callable.STATIC) != 0;
    }

    public boolean canBeInvalidated() {
        return (mFlags & android.databinding.tool.reflection.Callable.CAN_BE_INVALIDATED) != 0;
    }

    public int getMinApi() {
        return 1;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((("Callable{" + "type=") + type) + ", name='") + name) + '\'') + ", resolvedType=") + resolvedType) + ", isDynamic=") + isDynamic()) + ", canBeInvalidated=") + canBeInvalidated()) + ", static=") + isStatic()) + '}';
    }
}

