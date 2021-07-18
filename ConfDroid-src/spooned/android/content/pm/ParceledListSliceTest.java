package android.content.pm;


public class ParceledListSliceTest extends junit.framework.TestCase {
    public void testSmallList() throws java.lang.Exception {
        final int objectCount = 100;
        java.util.List<android.content.pm.ParceledListSliceTest.SmallObject> list = new java.util.ArrayList<android.content.pm.ParceledListSliceTest.SmallObject>();
        for (int i = 0; i < objectCount; i++) {
            list.add(new android.content.pm.ParceledListSliceTest.SmallObject(i * 2, (i * 2) + 1));
        }
        android.content.pm.ParceledListSlice<android.content.pm.ParceledListSliceTest.SmallObject> slice;
        android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            parcel.writeParcelable(new android.content.pm.ParceledListSlice<android.content.pm.ParceledListSliceTest.SmallObject>(list), 0);
            parcel.setDataPosition(0);
            slice = parcel.readParcelable(getClass().getClassLoader());
        } finally {
            parcel.recycle();
        }
        junit.framework.TestCase.assertNotNull(slice);
        junit.framework.TestCase.assertNotNull(slice.getList());
        junit.framework.TestCase.assertEquals(objectCount, slice.getList().size());
        for (int i = 0; i < objectCount; i++) {
            junit.framework.TestCase.assertEquals(i * 2, slice.getList().get(i).mFieldA);
            junit.framework.TestCase.assertEquals((i * 2) + 1, slice.getList().get(i).mFieldB);
        }
    }

    private static int measureLargeObject() {
        android.os.Parcel p = android.os.Parcel.obtain();
        try {
            new android.content.pm.ParceledListSliceTest.LargeObject(0, 0, 0, 0, 0).writeToParcel(p, 0);
            return p.dataPosition();
        } finally {
            p.recycle();
        }
    }

    /**
     * Test that when the list is large, the data is successfully parceled
     * and unparceled (the implementation will send pieces of the list in
     * separate round-trips to avoid the IPC limit).
     */
    public void testLargeList() throws java.lang.Exception {
        final int thresholdBytes = 256 * 1024;
        final int objectCount = thresholdBytes / android.content.pm.ParceledListSliceTest.measureLargeObject();
        java.util.List<android.content.pm.ParceledListSliceTest.LargeObject> list = new java.util.ArrayList<android.content.pm.ParceledListSliceTest.LargeObject>();
        for (int i = 0; i < objectCount; i++) {
            list.add(new android.content.pm.ParceledListSliceTest.LargeObject(i * 5, (i * 5) + 1, (i * 5) + 2, (i * 5) + 3, (i * 5) + 4));
        }
        android.content.pm.ParceledListSlice<android.content.pm.ParceledListSliceTest.LargeObject> slice;
        android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            parcel.writeParcelable(new android.content.pm.ParceledListSlice<android.content.pm.ParceledListSliceTest.LargeObject>(list), 0);
            parcel.setDataPosition(0);
            slice = parcel.readParcelable(getClass().getClassLoader());
        } finally {
            parcel.recycle();
        }
        junit.framework.TestCase.assertNotNull(slice);
        junit.framework.TestCase.assertNotNull(slice.getList());
        junit.framework.TestCase.assertEquals(objectCount, slice.getList().size());
        for (int i = 0; i < objectCount; i++) {
            junit.framework.TestCase.assertEquals(i * 5, slice.getList().get(i).mFieldA);
            junit.framework.TestCase.assertEquals((i * 5) + 1, slice.getList().get(i).mFieldB);
            junit.framework.TestCase.assertEquals((i * 5) + 2, slice.getList().get(i).mFieldC);
            junit.framework.TestCase.assertEquals((i * 5) + 3, slice.getList().get(i).mFieldD);
            junit.framework.TestCase.assertEquals((i * 5) + 4, slice.getList().get(i).mFieldE);
        }
    }

    /**
     * Test that only homogeneous elements may be unparceled.
     */
    public void testHomogeneousElements() throws java.lang.Exception {
        java.util.List<android.content.pm.ParceledListSliceTest.BaseObject> list = new java.util.ArrayList<android.content.pm.ParceledListSliceTest.BaseObject>();
        list.add(new android.content.pm.ParceledListSliceTest.LargeObject(0, 1, 2, 3, 4));
        list.add(new android.content.pm.ParceledListSliceTest.SmallObject(5, 6));
        list.add(new android.content.pm.ParceledListSliceTest.SmallObject(7, 8));
        android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            android.content.pm.ParceledListSliceTest.writeEvilParceledListSlice(parcel, list);
            parcel.setDataPosition(0);
            try {
                this.CREATOR.createFromParcel(parcel, getClass().getClassLoader());
                junit.framework.TestCase.assertTrue("Unparceled heterogeneous ParceledListSlice", false);
            } catch (java.lang.IllegalArgumentException e) {
                // Success, we're not allowed to process heterogeneous
                // elements in a ParceledListSlice.
            }
        } finally {
            parcel.recycle();
        }
    }

    /**
     * Write a ParcelableListSlice that uses the BaseObject base class as the Creator.
     * This is dangerous, as it may affect how the data is unparceled, then later parceled
     * by the system, leading to a self-modifying data security vulnerability.
     */
    private static <T extends android.content.pm.ParceledListSliceTest.BaseObject> void writeEvilParceledListSlice(android.os.Parcel dest, java.util.List<T> list) {
        final int listCount = list.size();
        // Number of items.
        dest.writeInt(listCount);
        // The type/creator to use when unparceling. Here we use the base class
        // to simulate an attack on ParceledListSlice.
        dest.writeString(android.content.pm.ParceledListSliceTest.BaseObject.class.getName());
        for (int i = 0; i < listCount; i++) {
            // 1 means the item is present.
            dest.writeInt(1);
            list.get(i).writeToParcel(dest, 0);
        }
    }

    public static abstract class BaseObject implements android.os.Parcelable {
        protected static final int TYPE_SMALL = 0;

        protected static final int TYPE_LARGE = 1;

        protected void writeToParcel(android.os.Parcel dest, int flags, int type) {
            dest.writeInt(type);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * This is *REALLY* bad, but we're doing it in the test to ensure that we handle
         * the possible exploit when unparceling an object with the BaseObject written as
         * Creator.
         */
        public static final android.content.pm.Creator<android.content.pm.ParceledListSliceTest.BaseObject> CREATOR = new android.content.pm.Creator<android.content.pm.ParceledListSliceTest.BaseObject>() {
            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.BaseObject createFromParcel(android.os.Parcel source) {
                switch (source.readInt()) {
                    case android.content.pm.ParceledListSliceTest.BaseObject.TYPE_SMALL :
                        return android.content.pm.ParceledListSliceTest.SmallObject.createFromParcelBody(source);
                    case android.content.pm.ParceledListSliceTest.BaseObject.TYPE_LARGE :
                        return android.content.pm.ParceledListSliceTest.LargeObject.createFromParcelBody(source);
                    default :
                        throw new java.lang.IllegalArgumentException("Unknown type");
                }
            }

            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.BaseObject[] newArray(int size) {
                return new android.content.pm.ParceledListSliceTest.BaseObject[size];
            }
        };
    }

    public static class SmallObject extends android.content.pm.ParceledListSliceTest.BaseObject {
        public int mFieldA;

        public int mFieldB;

        public SmallObject(int a, int b) {
            mFieldA = a;
            mFieldB = b;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags, android.content.pm.ParceledListSliceTest.BaseObject.TYPE_SMALL);
            dest.writeInt(mFieldA);
            dest.writeInt(mFieldB);
        }

        public static android.content.pm.ParceledListSliceTest.SmallObject createFromParcelBody(android.os.Parcel source) {
            return new android.content.pm.ParceledListSliceTest.SmallObject(source.readInt(), source.readInt());
        }

        public static final android.content.pm.Creator<android.content.pm.ParceledListSliceTest.SmallObject> CREATOR = new android.content.pm.Creator<android.content.pm.ParceledListSliceTest.SmallObject>() {
            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.SmallObject createFromParcel(android.os.Parcel source) {
                // Consume the type (as it is always written out).
                source.readInt();
                return android.content.pm.ParceledListSliceTest.SmallObject.createFromParcelBody(source);
            }

            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.SmallObject[] newArray(int size) {
                return new android.content.pm.ParceledListSliceTest.SmallObject[size];
            }
        };
    }

    public static class LargeObject extends android.content.pm.ParceledListSliceTest.BaseObject {
        public int mFieldA;

        public int mFieldB;

        public int mFieldC;

        public int mFieldD;

        public int mFieldE;

        public LargeObject(int a, int b, int c, int d, int e) {
            mFieldA = a;
            mFieldB = b;
            mFieldC = c;
            mFieldD = d;
            mFieldE = e;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags, android.content.pm.ParceledListSliceTest.BaseObject.TYPE_LARGE);
            dest.writeInt(mFieldA);
            dest.writeInt(mFieldB);
            dest.writeInt(mFieldC);
            dest.writeInt(mFieldD);
            dest.writeInt(mFieldE);
        }

        public static android.content.pm.ParceledListSliceTest.LargeObject createFromParcelBody(android.os.Parcel source) {
            return new android.content.pm.ParceledListSliceTest.LargeObject(source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readInt());
        }

        public static final android.content.pm.Creator<android.content.pm.ParceledListSliceTest.LargeObject> CREATOR = new android.content.pm.Creator<android.content.pm.ParceledListSliceTest.LargeObject>() {
            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.LargeObject createFromParcel(android.os.Parcel source) {
                // Consume the type (as it is always written out).
                source.readInt();
                return android.content.pm.ParceledListSliceTest.LargeObject.createFromParcelBody(source);
            }

            @java.lang.Override
            public android.content.pm.ParceledListSliceTest.LargeObject[] newArray(int size) {
                return new android.content.pm.ParceledListSliceTest.LargeObject[size];
            }
        };
    }
}

