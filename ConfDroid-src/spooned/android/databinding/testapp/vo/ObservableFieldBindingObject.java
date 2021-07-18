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
package android.databinding.testapp.vo;


public class ObservableFieldBindingObject {
    public final android.databinding.ObservableBoolean bField = new android.databinding.ObservableBoolean();

    public final android.databinding.ObservableByte tField = new android.databinding.ObservableByte();

    public final android.databinding.ObservableShort sField = new android.databinding.ObservableShort();

    public final android.databinding.ObservableChar cField = new android.databinding.ObservableChar();

    public final android.databinding.ObservableInt iField = new android.databinding.ObservableInt();

    public final android.databinding.ObservableLong lField = new android.databinding.ObservableLong();

    public final android.databinding.ObservableFloat fField = new android.databinding.ObservableFloat();

    public final android.databinding.ObservableDouble dField = new android.databinding.ObservableDouble();

    public final android.databinding.ObservableParcelable<android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable> pField;

    public final android.databinding.ObservableField<java.lang.String> oField = new android.databinding.ObservableField<>();

    public ObservableFieldBindingObject() {
        oField.set("Hello");
        android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable myParcelable = new android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable(3, "abc");
        pField = new android.databinding.ObservableParcelable(myParcelable);
    }

    public static class MyParcelable implements android.os.Parcelable {
        int x;

        java.lang.String y;

        public MyParcelable(int x, java.lang.String y) {
            this.x = x;
            this.y = y;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(x);
            dest.writeString(y);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable that = ((android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable) (o));
            if (x != that.x) {
                return false;
            }
            if (y != null ? !y.equals(that.y) : that.y != null) {
                return false;
            }
            return true;
        }

        public int getX() {
            return x;
        }

        public java.lang.String getY() {
            return y;
        }

        @java.lang.Override
        public int hashCode() {
            int result = x;
            result = (31 * result) + (y != null ? y.hashCode() : 0);
            return result;
        }

        public static final android.os.Parcelable.Creator<android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable> CREATOR = new android.os.Parcelable.Creator<android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable>() {
            @java.lang.Override
            public android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable createFromParcel(android.os.Parcel source) {
                return new android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable(source.readInt(), source.readString());
            }

            @java.lang.Override
            public android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable[] newArray(int size) {
                return new android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable[size];
            }
        };
    }
}

