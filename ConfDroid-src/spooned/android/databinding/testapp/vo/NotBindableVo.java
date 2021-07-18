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
package android.databinding.testapp.vo;


public class NotBindableVo {
    private int mIntValue;

    private int mIntValueGetCount;

    private boolean mBoolValue;

    private int mBoolValueGetCount;

    private java.lang.String mStringValue;

    private int mStringValueGetCount;

    private final java.lang.String mFinalString = "this has final content";

    public final int publicField = 3;

    public NotBindableVo() {
    }

    public NotBindableVo(int intValue) {
        this.mIntValue = intValue;
    }

    public NotBindableVo(java.lang.String stringValue) {
        this.mStringValue = stringValue;
    }

    public NotBindableVo(int intValue, java.lang.String stringValue) {
        this.mIntValue = intValue;
        this.mStringValue = stringValue;
    }

    public int getIntValue() {
        mIntValueGetCount++;
        return mIntValue;
    }

    public java.lang.String getFinalString() {
        return mFinalString;
    }

    public void setIntValue(int intValue) {
        this.mIntValue = intValue;
    }

    public java.lang.String getStringValue() {
        mStringValueGetCount++;
        return mStringValue;
    }

    public void setStringValue(java.lang.String stringValue) {
        this.mStringValue = stringValue;
    }

    public java.lang.String mergeStringFields(android.databinding.testapp.vo.NotBindableVo other) {
        return mStringValue + (other == null ? "" : other.mStringValue);
    }

    public boolean getBoolValue() {
        mBoolValueGetCount++;
        return mBoolValue;
    }

    public void setBoolValue(boolean boolValue) {
        mBoolValue = boolValue;
    }

    public int getIntValueGetCount() {
        return mIntValueGetCount;
    }

    public int getBoolValueGetCount() {
        return mBoolValueGetCount;
    }

    public int getStringValueGetCount() {
        return mStringValueGetCount;
    }
}

