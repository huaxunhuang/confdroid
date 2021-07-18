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
package android.databinding;


/**
 * An observable class that holds a primitive double.
 * <p>
 * Observable field classes may be used instead of creating an Observable object:
 * <pre><code>public class MyDataObject {
 *     public final ObservableDouble temperature = new ObservableDouble();
 * }</code></pre>
 * Fields of this type should be declared final because bindings only detect changes in the
 * field's value, not of the field itself.
 * <p>
 * This class is parcelable and serializable but callbacks are ignored when the object is
 * parcelled / serialized. Unless you add custom callbacks, this will not be an issue because
 * data binding framework always re-registers callbacks when the view is bound.
 */
public class ObservableDouble extends android.databinding.BaseObservable implements android.os.Parcelable , java.io.Serializable {
    static final long serialVersionUID = 1L;

    private double mValue;

    /**
     * Creates an ObservableDouble with the given initial value.
     *
     * @param value
     * 		the initial value for the ObservableDouble
     */
    public ObservableDouble(double value) {
        mValue = value;
    }

    /**
     * Creates an ObservableDouble with the initial value of <code>0</code>.
     */
    public ObservableDouble() {
    }

    /**
     *
     *
     * @return the stored value.
     */
    public double get() {
        return mValue;
    }

    /**
     * Set the stored value.
     */
    public void set(double value) {
        if (value != mValue) {
            mValue = value;
            notifyChange();
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeDouble(mValue);
    }

    public static final android.os.Parcelable.Creator<android.databinding.ObservableDouble> CREATOR = new android.os.Parcelable.Creator<android.databinding.ObservableDouble>() {
        @java.lang.Override
        public android.databinding.ObservableDouble createFromParcel(android.os.Parcel source) {
            return new android.databinding.ObservableDouble(source.readDouble());
        }

        @java.lang.Override
        public android.databinding.ObservableDouble[] newArray(int size) {
            return new android.databinding.ObservableDouble[size];
        }
    };
}

