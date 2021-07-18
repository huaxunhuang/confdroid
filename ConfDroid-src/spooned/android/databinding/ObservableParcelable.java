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
 * An observable class that holds a parcelable object.
 * <p>
 * Observable field classes may be used instead of creating an Observable object:
 * <pre><code>public class MyDataObject {
 *     public final ObservableParcelable&lt;String> name = new ObservableParcelable&lt;String>();
 *     public final ObservableInt age = new ObservableInt();
 * }</code></pre>
 * Fields of this type should be declared final because bindings only detect changes in the
 * field's value, not of the field itself.
 * <p>
 * This class is parcelable but you should keep in mind that listeners are ignored when the object
 * is parcelled. Unless you add custom observers, this should not be an issue because data binding
 * framework always re-registers observers when the view is bound.
 */
public class ObservableParcelable<T extends android.os.Parcelable> extends android.databinding.ObservableField<T> implements android.os.Parcelable , java.io.Serializable {
    static final long serialVersionUID = 1L;

    /**
     * Wraps the given object and creates an observable object
     *
     * @param value
     * 		The value to be wrapped as an observable.
     */
    public ObservableParcelable(T value) {
        super(value);
    }

    /**
     * Creates an empty observable object
     */
    public ObservableParcelable() {
        super();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(get(), 0);
    }

    public static final android.os.Parcelable.Creator<android.databinding.ObservableParcelable> CREATOR = new android.os.Parcelable.Creator<android.databinding.ObservableParcelable>() {
        @java.lang.Override
        public android.databinding.ObservableParcelable createFromParcel(android.os.Parcel source) {
            // noinspection unchecked
            return new android.databinding.ObservableParcelable(source.readParcelable(getClass().getClassLoader()));
        }

        @java.lang.Override
        public android.databinding.ObservableParcelable[] newArray(int size) {
            return new android.databinding.ObservableParcelable[size];
        }
    };
}

