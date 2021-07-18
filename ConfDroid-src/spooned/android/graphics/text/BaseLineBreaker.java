/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.text;


// Based on the native implementation of LineBreaker in
// frameworks/base/core/jni/android_text_StaticLayout.cpp revision b808260
public abstract class BaseLineBreaker {
    protected static final int TAB_MASK = 0x20000000;// keep in sync with StaticLayout


    @android.annotation.NonNull
    protected final java.util.List<android.graphics.text.Primitive> mPrimitives;

    @android.annotation.NonNull
    protected final android.graphics.text.LineWidth mLineWidth;

    @android.annotation.NonNull
    protected final android.graphics.text.TabStops mTabStops;

    public BaseLineBreaker(@android.annotation.NonNull
    java.util.List<android.graphics.text.Primitive> primitives, @android.annotation.NonNull
    android.graphics.text.LineWidth lineWidth, @android.annotation.NonNull
    android.graphics.text.TabStops tabStops) {
        mPrimitives = java.util.Collections.unmodifiableList(primitives);
        mLineWidth = lineWidth;
        mTabStops = tabStops;
    }

    public abstract android.graphics.text.BaseLineBreaker.Result computeBreaks();

    public static class Result {
        java.util.List<java.lang.Integer> mLineBreakOffset = new java.util.ArrayList<>();

        java.util.List<java.lang.Float> mLineWidths = new java.util.ArrayList<>();

        java.util.List<java.lang.Float> mLineAscents = new java.util.ArrayList<>();

        java.util.List<java.lang.Float> mLineDescents = new java.util.ArrayList<>();

        java.util.List<java.lang.Integer> mLineFlags = new java.util.ArrayList<>();
    }
}

