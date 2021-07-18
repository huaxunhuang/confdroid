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
package android.support.v4.widget;


class TextViewCompatGingerbread {
    private static final java.lang.String LOG_TAG = "TextViewCompatGingerbread";

    private static final int LINES = 1;

    private static java.lang.reflect.Field sMaximumField;

    private static boolean sMaximumFieldFetched;

    private static java.lang.reflect.Field sMaxModeField;

    private static boolean sMaxModeFieldFetched;

    private static java.lang.reflect.Field sMinimumField;

    private static boolean sMinimumFieldFetched;

    private static java.lang.reflect.Field sMinModeField;

    private static boolean sMinModeFieldFetched;

    static int getMaxLines(android.widget.TextView textView) {
        if (!android.support.v4.widget.TextViewCompatGingerbread.sMaxModeFieldFetched) {
            android.support.v4.widget.TextViewCompatGingerbread.sMaxModeField = android.support.v4.widget.TextViewCompatGingerbread.retrieveField("mMaxMode");
            android.support.v4.widget.TextViewCompatGingerbread.sMaxModeFieldFetched = true;
        }
        if ((android.support.v4.widget.TextViewCompatGingerbread.sMaxModeField != null) && (android.support.v4.widget.TextViewCompatGingerbread.retrieveIntFromField(android.support.v4.widget.TextViewCompatGingerbread.sMaxModeField, textView) == android.support.v4.widget.TextViewCompatGingerbread.LINES)) {
            // If the max mode is using lines, we can grab the maximum value
            if (!android.support.v4.widget.TextViewCompatGingerbread.sMaximumFieldFetched) {
                android.support.v4.widget.TextViewCompatGingerbread.sMaximumField = android.support.v4.widget.TextViewCompatGingerbread.retrieveField("mMaximum");
                android.support.v4.widget.TextViewCompatGingerbread.sMaximumFieldFetched = true;
            }
            if (android.support.v4.widget.TextViewCompatGingerbread.sMaximumField != null) {
                return android.support.v4.widget.TextViewCompatGingerbread.retrieveIntFromField(android.support.v4.widget.TextViewCompatGingerbread.sMaximumField, textView);
            }
        }
        return -1;
    }

    static int getMinLines(android.widget.TextView textView) {
        if (!android.support.v4.widget.TextViewCompatGingerbread.sMinModeFieldFetched) {
            android.support.v4.widget.TextViewCompatGingerbread.sMinModeField = android.support.v4.widget.TextViewCompatGingerbread.retrieveField("mMinMode");
            android.support.v4.widget.TextViewCompatGingerbread.sMinModeFieldFetched = true;
        }
        if ((android.support.v4.widget.TextViewCompatGingerbread.sMinModeField != null) && (android.support.v4.widget.TextViewCompatGingerbread.retrieveIntFromField(android.support.v4.widget.TextViewCompatGingerbread.sMinModeField, textView) == android.support.v4.widget.TextViewCompatGingerbread.LINES)) {
            // If the min mode is using lines, we can grab the maximum value
            if (!android.support.v4.widget.TextViewCompatGingerbread.sMinimumFieldFetched) {
                android.support.v4.widget.TextViewCompatGingerbread.sMinimumField = android.support.v4.widget.TextViewCompatGingerbread.retrieveField("mMinimum");
                android.support.v4.widget.TextViewCompatGingerbread.sMinimumFieldFetched = true;
            }
            if (android.support.v4.widget.TextViewCompatGingerbread.sMinimumField != null) {
                return android.support.v4.widget.TextViewCompatGingerbread.retrieveIntFromField(android.support.v4.widget.TextViewCompatGingerbread.sMinimumField, textView);
            }
        }
        return -1;
    }

    private static java.lang.reflect.Field retrieveField(java.lang.String fieldName) {
        java.lang.reflect.Field field = null;
        try {
            field = android.widget.TextView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (java.lang.NoSuchFieldException e) {
            android.util.Log.e(android.support.v4.widget.TextViewCompatGingerbread.LOG_TAG, ("Could not retrieve " + fieldName) + " field.");
        }
        return field;
    }

    private static int retrieveIntFromField(java.lang.reflect.Field field, android.widget.TextView textView) {
        try {
            return field.getInt(textView);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Log.d(android.support.v4.widget.TextViewCompatGingerbread.LOG_TAG, ("Could not retrieve value of " + field.getName()) + " field.");
        }
        return -1;
    }

    static void setTextAppearance(android.widget.TextView textView, int resId) {
        textView.setTextAppearance(textView.getContext(), resId);
    }

    static android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        return textView.getCompoundDrawables();
    }
}

