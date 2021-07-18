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
package android.text.style;


public class SuperscriptSpan extends android.text.style.MetricAffectingSpan implements android.text.ParcelableSpan {
    public SuperscriptSpan() {
    }

    public SuperscriptSpan(android.os.Parcel src) {
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSpanTypeIdInternal() {
        return android.text.TextUtils.SUPERSCRIPT_SPAN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcelInternal(android.os.Parcel dest, int flags) {
    }

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint tp) {
        tp.baselineShift += ((int) (tp.ascent() / 2));
    }

    @java.lang.Override
    public void updateMeasureState(android.text.TextPaint tp) {
        tp.baselineShift += ((int) (tp.ascent() / 2));
    }
}

