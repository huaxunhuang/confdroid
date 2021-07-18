/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.graphics.drawable;


class TypedArrayUtils {
    private static final java.lang.String NAMESPACE = "http://schemas.android.com/apk/res/android";

    public static boolean hasAttribute(org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName) {
        return parser.getAttributeValue(android.support.graphics.drawable.TypedArrayUtils.NAMESPACE, attrName) != null;
    }

    public static float getNamedFloat(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, int resId, float defaultValue) {
        final boolean hasAttr = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, attrName);
        if (!hasAttr) {
            return defaultValue;
        } else {
            return a.getFloat(resId, defaultValue);
        }
    }

    public static boolean getNamedBoolean(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, int resId, boolean defaultValue) {
        final boolean hasAttr = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, attrName);
        if (!hasAttr) {
            return defaultValue;
        } else {
            return a.getBoolean(resId, defaultValue);
        }
    }

    public static int getNamedInt(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, int resId, int defaultValue) {
        final boolean hasAttr = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, attrName);
        if (!hasAttr) {
            return defaultValue;
        } else {
            return a.getInt(resId, defaultValue);
        }
    }

    public static int getNamedColor(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, int resId, int defaultValue) {
        final boolean hasAttr = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, attrName);
        if (!hasAttr) {
            return defaultValue;
        } else {
            return a.getColor(resId, defaultValue);
        }
    }
}

