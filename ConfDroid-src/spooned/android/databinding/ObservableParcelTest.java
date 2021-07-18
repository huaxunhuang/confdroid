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


public class ObservableParcelTest extends android.test.AndroidTestCase {
    public void testParcelInt() {
        android.databinding.ObservableInt observableInt = new android.databinding.ObservableInt();
        observableInt.set(java.lang.Integer.MAX_VALUE - 1);
        android.databinding.ObservableInt read = parcelAndUnparcel(observableInt, android.databinding.ObservableInt.class);
        junit.framework.TestCase.assertEquals(java.lang.Integer.MAX_VALUE - 1, read.get());
    }

    public void testParcelBoolean() {
        android.databinding.ObservableBoolean obj = new android.databinding.ObservableBoolean(false);
        android.databinding.ObservableBoolean read = parcelAndUnparcel(obj, android.databinding.ObservableBoolean.class);
        junit.framework.TestCase.assertFalse(read.get());
        android.databinding.ObservableBoolean obj2 = new android.databinding.ObservableBoolean(true);
        android.databinding.ObservableBoolean read2 = parcelAndUnparcel(obj2, android.databinding.ObservableBoolean.class);
        junit.framework.TestCase.assertTrue(read2.get());
    }

    public void testParcelByte() {
        android.databinding.ObservableByte obj = new android.databinding.ObservableByte(((byte) (7)));
        android.databinding.ObservableByte read = parcelAndUnparcel(obj, android.databinding.ObservableByte.class);
        junit.framework.TestCase.assertEquals(((byte) (7)), read.get());
    }

    public void testParcelChar() {
        android.databinding.ObservableChar obj = new android.databinding.ObservableChar('y');
        android.databinding.ObservableChar read = parcelAndUnparcel(obj, android.databinding.ObservableChar.class);
        junit.framework.TestCase.assertEquals('y', read.get());
    }

    public void testParcelDouble() {
        android.databinding.ObservableDouble obj = new android.databinding.ObservableDouble(java.lang.Double.MAX_VALUE);
        android.databinding.ObservableDouble read = parcelAndUnparcel(obj, android.databinding.ObservableDouble.class);
        junit.framework.TestCase.assertEquals(java.lang.Double.MAX_VALUE, read.get());
    }

    public void testParcelFloat() {
        android.databinding.ObservableFloat obj = new android.databinding.ObservableFloat(java.lang.Float.MIN_VALUE);
        android.databinding.ObservableFloat read = parcelAndUnparcel(obj, android.databinding.ObservableFloat.class);
        junit.framework.TestCase.assertEquals(java.lang.Float.MIN_VALUE, read.get());
    }

    public void testParcelParcel() {
        android.databinding.ObservableParcelTest.MyParcelable myParcelable = new android.databinding.ObservableParcelTest.MyParcelable(5, "foo");
        android.databinding.ObservableParcelable<android.databinding.ObservableParcelTest.MyParcelable> obj = new android.databinding.ObservableParcelable<>(myParcelable);
        android.databinding.ObservableParcelable read = parcelAndUnparcel(obj, android.databinding.ObservableParcelable.class);
        junit.framework.TestCase.assertEquals(myParcelable, read.get());
    }

    public void testParcelLong() {
        android.databinding.ObservableLong obj = new android.databinding.ObservableLong(java.lang.Long.MAX_VALUE - 1);
        android.databinding.ObservableLong read = parcelAndUnparcel(obj, android.databinding.ObservableLong.class);
        junit.framework.TestCase.assertEquals(java.lang.Long.MAX_VALUE - 1, read.get());
    }

    public void testParcelShort() {
        android.databinding.ObservableShort obj = new android.databinding.ObservableShort(java.lang.Short.MIN_VALUE);
        android.databinding.ObservableShort read = parcelAndUnparcel(obj, android.databinding.ObservableShort.class);
        junit.framework.TestCase.assertEquals(java.lang.Short.MIN_VALUE, read.get());
    }

    public void testSerializeInt() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableInt observableInt = new android.databinding.ObservableInt();
        observableInt.set(java.lang.Integer.MAX_VALUE - 1);
        android.databinding.ObservableInt read = serializeAndDeserialize(observableInt, android.databinding.ObservableInt.class);
        junit.framework.TestCase.assertEquals(java.lang.Integer.MAX_VALUE - 1, read.get());
    }

    public void testSerializeBoolean() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableBoolean obj = new android.databinding.ObservableBoolean(false);
        android.databinding.ObservableBoolean read = serializeAndDeserialize(obj, android.databinding.ObservableBoolean.class);
        junit.framework.TestCase.assertFalse(read.get());
        android.databinding.ObservableBoolean obj2 = new android.databinding.ObservableBoolean(true);
        android.databinding.ObservableBoolean read2 = serializeAndDeserialize(obj2, android.databinding.ObservableBoolean.class);
        junit.framework.TestCase.assertTrue(read2.get());
    }

    public void testSerializeByte() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableByte obj = new android.databinding.ObservableByte(((byte) (7)));
        android.databinding.ObservableByte read = serializeAndDeserialize(obj, android.databinding.ObservableByte.class);
        junit.framework.TestCase.assertEquals(((byte) (7)), read.get());
    }

    public void testSerializeChar() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableChar obj = new android.databinding.ObservableChar('y');
        android.databinding.ObservableChar read = serializeAndDeserialize(obj, android.databinding.ObservableChar.class);
        junit.framework.TestCase.assertEquals('y', read.get());
    }

    public void testSerializeDouble() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableDouble obj = new android.databinding.ObservableDouble(java.lang.Double.MAX_VALUE);
        android.databinding.ObservableDouble read = serializeAndDeserialize(obj, android.databinding.ObservableDouble.class);
        junit.framework.TestCase.assertEquals(java.lang.Double.MAX_VALUE, read.get());
    }

    public void testSerializeFloat() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableFloat obj = new android.databinding.ObservableFloat(java.lang.Float.MIN_VALUE);
        android.databinding.ObservableFloat read = serializeAndDeserialize(obj, android.databinding.ObservableFloat.class);
        junit.framework.TestCase.assertEquals(java.lang.Float.MIN_VALUE, read.get());
    }

    public void testSerializeParcel() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableParcelTest.MyParcelable myParcelable = new android.databinding.ObservableParcelTest.MyParcelable(5, "foo");
        android.databinding.ObservableParcelable<android.databinding.ObservableParcelTest.MyParcelable> obj = new android.databinding.ObservableParcelable<>(myParcelable);
        android.databinding.ObservableParcelable read = serializeAndDeserialize(obj, android.databinding.ObservableParcelable.class);
        junit.framework.TestCase.assertEquals(myParcelable, read.get());
    }

    public void testSerializeField() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableParcelTest.MyParcelable myParcelable = new android.databinding.ObservableParcelTest.MyParcelable(5, "foo");
        android.databinding.ObservableField<android.databinding.ObservableParcelTest.MyParcelable> obj = new android.databinding.ObservableField<>(myParcelable);
        android.databinding.ObservableField read = serializeAndDeserialize(obj, android.databinding.ObservableField.class);
        junit.framework.TestCase.assertEquals(myParcelable, read.get());
    }

    public void testSerializeLong() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableLong obj = new android.databinding.ObservableLong(java.lang.Long.MAX_VALUE - 1);
        android.databinding.ObservableLong read = serializeAndDeserialize(obj, android.databinding.ObservableLong.class);
        junit.framework.TestCase.assertEquals(java.lang.Long.MAX_VALUE - 1, read.get());
    }

    public void testSerializeShort() throws java.io.IOException, java.lang.ClassNotFoundException {
        android.databinding.ObservableShort obj = new android.databinding.ObservableShort(java.lang.Short.MIN_VALUE);
        android.databinding.ObservableShort read = serializeAndDeserialize(obj, android.databinding.ObservableShort.class);
        junit.framework.TestCase.assertEquals(java.lang.Short.MIN_VALUE, read.get());
    }

    private <T extends android.os.Parcelable> T parcelAndUnparcel(T t, java.lang.Class<T> klass) {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        parcel.writeParcelable(t, 0);
        // we append a suffix to the parcelable to test out of bounds
        java.lang.String parcelSuffix = java.util.UUID.randomUUID().toString();
        parcel.writeString(parcelSuffix);
        // get ready to read
        parcel.setDataPosition(0);
        android.os.Parcelable parcelable = parcel.readParcelable(getClass().getClassLoader());
        junit.framework.TestCase.assertNotNull(parcelable);
        junit.framework.TestCase.assertEquals(klass, parcelable.getClass());
        junit.framework.TestCase.assertEquals(parcelSuffix, parcel.readString());
        return ((T) (parcelable));
    }

    private <T> T serializeAndDeserialize(T t, java.lang.Class<T> klass) throws java.io.IOException, java.lang.ClassNotFoundException {
        java.io.ObjectOutputStream oos = null;
        java.io.ByteArrayOutputStream bos = null;
        java.lang.String suffix = java.util.UUID.randomUUID().toString();
        try {
            bos = new java.io.ByteArrayOutputStream();
            oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(t);
            oos.writeObject(suffix);
        } finally {
            android.databinding.ObservableParcelTest.closeQuietly(bos);
            android.databinding.ObservableParcelTest.closeQuietly(oos);
        }
        java.io.ByteArrayInputStream bis = null;
        java.io.ObjectInputStream ois = null;
        try {
            bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ois = new java.io.ObjectInputStream(bis);
            java.lang.Object o = ois.readObject();
            junit.framework.TestCase.assertEquals(klass, o.getClass());
            junit.framework.TestCase.assertEquals(suffix, ois.readObject());
            return ((T) (o));
        } finally {
            android.databinding.ObservableParcelTest.closeQuietly(bis);
            android.databinding.ObservableParcelTest.closeQuietly(ois);
        }
    }

    private static void closeQuietly(java.io.Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (java.io.IOException ignored) {
        }
    }

    public static class MyParcelable implements android.os.Parcelable , java.io.Serializable {
        int x;

        java.lang.String y;

        public MyParcelable() {
        }

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
            android.databinding.ObservableParcelTest.MyParcelable that = ((android.databinding.ObservableParcelTest.MyParcelable) (o));
            if (x != that.x) {
                return false;
            }
            if (y != null ? !y.equals(that.y) : that.y != null) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = x;
            result = (31 * result) + (y != null ? y.hashCode() : 0);
            return result;
        }

        public static final android.os.Parcelable.Creator<android.databinding.ObservableParcelTest.MyParcelable> CREATOR = new android.os.Parcelable.Creator<android.databinding.ObservableParcelTest.MyParcelable>() {
            @java.lang.Override
            public android.databinding.ObservableParcelTest.MyParcelable createFromParcel(android.os.Parcel source) {
                return new android.databinding.ObservableParcelTest.MyParcelable(source.readInt(), source.readString());
            }

            @java.lang.Override
            public android.databinding.ObservableParcelTest.MyParcelable[] newArray(int size) {
                return new android.databinding.ObservableParcelTest.MyParcelable[size];
            }
        };
    }
}

