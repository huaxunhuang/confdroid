/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.util;


/**
 * Provides an implementation of AttributeSet on top of an XmlPullParser.
 */
class XmlPullAttributes implements android.util.AttributeSet {
    public XmlPullAttributes(org.xmlpull.v1.XmlPullParser parser) {
        mParser = parser;
    }

    public int getAttributeCount() {
        return mParser.getAttributeCount();
    }

    public java.lang.String getAttributeName(int index) {
        return mParser.getAttributeName(index);
    }

    public java.lang.String getAttributeValue(int index) {
        return mParser.getAttributeValue(index);
    }

    public java.lang.String getAttributeValue(java.lang.String namespace, java.lang.String name) {
        return mParser.getAttributeValue(namespace, name);
    }

    public java.lang.String getPositionDescription() {
        return mParser.getPositionDescription();
    }

    public int getAttributeNameResource(int index) {
        return 0;
    }

    public int getAttributeListValue(java.lang.String namespace, java.lang.String attribute, java.lang.String[] options, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToList(getAttributeValue(namespace, attribute), options, defaultValue);
    }

    public boolean getAttributeBooleanValue(java.lang.String namespace, java.lang.String attribute, boolean defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToBoolean(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeResourceValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeIntValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeUnsignedIntValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToUnsignedInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public float getAttributeFloatValue(java.lang.String namespace, java.lang.String attribute, float defaultValue) {
        java.lang.String s = getAttributeValue(namespace, attribute);
        if (s != null) {
            return java.lang.Float.parseFloat(s);
        }
        return defaultValue;
    }

    public int getAttributeListValue(int index, java.lang.String[] options, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToList(getAttributeValue(index), options, defaultValue);
    }

    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToBoolean(getAttributeValue(index), defaultValue);
    }

    public int getAttributeResourceValue(int index, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToInt(getAttributeValue(index), defaultValue);
    }

    public int getAttributeIntValue(int index, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToInt(getAttributeValue(index), defaultValue);
    }

    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToUnsignedInt(getAttributeValue(index), defaultValue);
    }

    public float getAttributeFloatValue(int index, float defaultValue) {
        java.lang.String s = getAttributeValue(index);
        if (s != null) {
            return java.lang.Float.parseFloat(s);
        }
        return defaultValue;
    }

    public java.lang.String getIdAttribute() {
        return getAttributeValue(null, "id");
    }

    public java.lang.String getClassAttribute() {
        return getAttributeValue(null, "class");
    }

    public int getIdAttributeResourceValue(int defaultValue) {
        return getAttributeResourceValue(null, "id", defaultValue);
    }

    public int getStyleAttribute() {
        return getAttributeResourceValue(null, "style", 0);
    }

    /* package */
    org.xmlpull.v1.XmlPullParser mParser;
}

