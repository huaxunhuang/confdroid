/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.autofill;


/**
 * Abstracts how a {@link View} can be autofilled by an
 * {@link android.service.autofill.AutofillService}.
 *
 * <p>Each {@link AutofillValue} is associated with a {@code type}, as defined by
 * {@link View#getAutofillType()}.
 */
public final class AutofillValue implements android.os.Parcelable {
    private static final java.lang.String TAG = "AutofillValue";

    @android.view.View.AutofillType
    private final int mType;

    @android.annotation.NonNull
    private final java.lang.Object mValue;

    private AutofillValue(@android.view.View.AutofillType
    int type, @android.annotation.NonNull
    java.lang.Object value) {
        mType = type;
        mValue = value;
    }

    /**
     * Gets the value to autofill a text field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TEXT} for more info.</p>
     *
     * @throws IllegalStateException
     * 		if the value is not a text value
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getTextValue() {
        com.android.internal.util.Preconditions.checkState(isText(), "value must be a text value, not type=" + mType);
        return ((java.lang.CharSequence) (mValue));
    }

    /**
     * Checks is this is a text value.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TEXT} for more info.</p>
     */
    public boolean isText() {
        return mType == android.view.View.AUTOFILL_TYPE_TEXT;
    }

    /**
     * Gets the value to autofill a toggable field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TOGGLE} for more info.</p>
     *
     * @throws IllegalStateException
     * 		if the value is not a toggle value
     */
    public boolean getToggleValue() {
        com.android.internal.util.Preconditions.checkState(isToggle(), "value must be a toggle value, not type=" + mType);
        return ((java.lang.Boolean) (mValue));
    }

    /**
     * Checks is this is a toggle value.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TOGGLE} for more info.</p>
     */
    public boolean isToggle() {
        return mType == android.view.View.AUTOFILL_TYPE_TOGGLE;
    }

    /**
     * Gets the value to autofill a selection list field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_LIST} for more info.</p>
     *
     * @throws IllegalStateException
     * 		if the value is not a list value
     */
    public int getListValue() {
        com.android.internal.util.Preconditions.checkState(isList(), "value must be a list value, not type=" + mType);
        return ((java.lang.Integer) (mValue));
    }

    /**
     * Checks is this is a list value.
     *
     * <p>See {@link View#AUTOFILL_TYPE_LIST} for more info.</p>
     */
    public boolean isList() {
        return mType == android.view.View.AUTOFILL_TYPE_LIST;
    }

    /**
     * Gets the value to autofill a date field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_DATE} for more info.</p>
     *
     * @throws IllegalStateException
     * 		if the value is not a date value
     */
    public long getDateValue() {
        com.android.internal.util.Preconditions.checkState(isDate(), "value must be a date value, not type=" + mType);
        return ((java.lang.Long) (mValue));
    }

    /**
     * Checks is this is a date value.
     *
     * <p>See {@link View#AUTOFILL_TYPE_DATE} for more info.</p>
     */
    public boolean isDate() {
        return mType == android.view.View.AUTOFILL_TYPE_DATE;
    }

    /**
     * Used to define whether a field is empty so it's not sent to service on save.
     *
     * <p>Only applies to some types, like text.
     *
     * @unknown 
     */
    public boolean isEmpty() {
        return isText() && (((java.lang.CharSequence) (mValue)).length() == 0);
    }

    // ///////////////////////////////////
    // Object "contract" methods. //
    // ///////////////////////////////////
    @java.lang.Override
    public int hashCode() {
        return mType + mValue.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final android.view.autofill.AutofillValue other = ((android.view.autofill.AutofillValue) (obj));
        if (mType != other.mType)
            return false;

        if (isText()) {
            return mValue.toString().equals(other.mValue.toString());
        } else {
            return java.util.Objects.equals(mValue, other.mValue);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (!android.view.autofill.Helper.sDebug)
            return super.toString();

        final java.lang.StringBuilder string = new java.lang.StringBuilder().append("[type=").append(mType).append(", value=");
        if (isText()) {
            android.view.autofill.Helper.appendRedacted(string, ((java.lang.CharSequence) (mValue)));
        } else {
            string.append(mValue);
        }
        return string.append(']').toString();
    }

    // ///////////////////////////////////
    // Parcelable "contract" methods. //
    // ///////////////////////////////////
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mType);
        switch (mType) {
            case android.view.View.AUTOFILL_TYPE_TEXT :
                parcel.writeCharSequence(((java.lang.CharSequence) (mValue)));
                break;
            case android.view.View.AUTOFILL_TYPE_TOGGLE :
                parcel.writeInt(((java.lang.Boolean) (mValue)) ? 1 : 0);
                break;
            case android.view.View.AUTOFILL_TYPE_LIST :
                parcel.writeInt(((java.lang.Integer) (mValue)));
                break;
            case android.view.View.AUTOFILL_TYPE_DATE :
                parcel.writeLong(((java.lang.Long) (mValue)));
                break;
        }
    }

    private AutofillValue(@android.annotation.NonNull
    android.os.Parcel parcel) {
        mType = parcel.readInt();
        switch (mType) {
            case android.view.View.AUTOFILL_TYPE_TEXT :
                mValue = parcel.readCharSequence();
                break;
            case android.view.View.AUTOFILL_TYPE_TOGGLE :
                int rawValue = parcel.readInt();
                mValue = rawValue != 0;
                break;
            case android.view.View.AUTOFILL_TYPE_LIST :
                mValue = parcel.readInt();
                break;
            case android.view.View.AUTOFILL_TYPE_DATE :
                mValue = parcel.readLong();
                break;
            default :
                throw new java.lang.IllegalArgumentException(("type=" + mType) + " not valid");
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.autofill.AutofillValue> CREATOR = new android.os.Parcelable.Creator<android.view.autofill.AutofillValue>() {
        @java.lang.Override
        public android.view.autofill.AutofillValue createFromParcel(android.os.Parcel source) {
            return new android.view.autofill.AutofillValue(source);
        }

        @java.lang.Override
        public android.view.autofill.AutofillValue[] newArray(int size) {
            return new android.view.autofill.AutofillValue[size];
        }
    };

    // //////////////////
    // Factory methods //
    // //////////////////
    /**
     * Creates a new {@link AutofillValue} to autofill a {@link View} representing a text field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TEXT} for more info.
     *
     * <p><b>Note:</b> This method is not thread safe and can throw an exception if the
     * {@code value} is modified by a different thread before it returns.
     */
    public static android.view.autofill.AutofillValue forText(@android.annotation.Nullable
    java.lang.CharSequence value) {
        if (android.view.autofill.Helper.sVerbose && (!android.os.Looper.getMainLooper().isCurrentThread())) {
            android.util.Log.v(android.view.autofill.AutofillValue.TAG, "forText() not called on main thread: " + java.lang.Thread.currentThread());
        }
        return value == null ? null : new android.view.autofill.AutofillValue(android.view.View.AUTOFILL_TYPE_TEXT, android.text.TextUtils.trimNoCopySpans(value));
    }

    /**
     * Creates a new {@link AutofillValue} to autofill a {@link View} representing a toggable
     * field.
     *
     * <p>See {@link View#AUTOFILL_TYPE_TOGGLE} for more info.
     */
    public static android.view.autofill.AutofillValue forToggle(boolean value) {
        return new android.view.autofill.AutofillValue(android.view.View.AUTOFILL_TYPE_TOGGLE, value);
    }

    /**
     * Creates a new {@link AutofillValue} to autofill a {@link View} representing a selection
     * list.
     *
     * <p>See {@link View#AUTOFILL_TYPE_LIST} for more info.
     */
    public static android.view.autofill.AutofillValue forList(int value) {
        return new android.view.autofill.AutofillValue(android.view.View.AUTOFILL_TYPE_LIST, value);
    }

    /**
     * Creates a new {@link AutofillValue} to autofill a {@link View} representing a date.
     *
     * <p>See {@link View#AUTOFILL_TYPE_DATE} for more info.
     */
    public static android.view.autofill.AutofillValue forDate(long value) {
        return new android.view.autofill.AutofillValue(android.view.View.AUTOFILL_TYPE_DATE, value);
    }
}

