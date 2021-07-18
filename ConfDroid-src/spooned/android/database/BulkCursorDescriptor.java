/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.database;


/**
 * Describes the properties of a {@link CursorToBulkCursorAdaptor} that are
 * needed to initialize its {@link BulkCursorToCursorAdaptor} counterpart on the client's end.
 *
 * {@hide }
 */
public final class BulkCursorDescriptor implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.database.BulkCursorDescriptor> CREATOR = new android.os.Parcelable.Creator<android.database.BulkCursorDescriptor>() {
        @java.lang.Override
        public android.database.BulkCursorDescriptor createFromParcel(android.os.Parcel in) {
            android.database.BulkCursorDescriptor d = new android.database.BulkCursorDescriptor();
            d.readFromParcel(in);
            return d;
        }

        @java.lang.Override
        public android.database.BulkCursorDescriptor[] newArray(int size) {
            return new android.database.BulkCursorDescriptor[size];
        }
    };

    public android.database.IBulkCursor cursor;

    public java.lang.String[] columnNames;

    public boolean wantsAllOnMoveCalls;

    public int count;

    public android.database.CursorWindow window;

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeStrongBinder(cursor.asBinder());
        out.writeStringArray(columnNames);
        out.writeInt(wantsAllOnMoveCalls ? 1 : 0);
        out.writeInt(count);
        if (window != null) {
            out.writeInt(1);
            window.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
    }

    public void readFromParcel(android.os.Parcel in) {
        cursor = android.database.BulkCursorNative.asInterface(in.readStrongBinder());
        columnNames = in.readStringArray();
        wantsAllOnMoveCalls = in.readInt() != 0;
        count = in.readInt();
        if (in.readInt() != 0) {
            window = android.database.CursorWindow.CREATOR.createFromParcel(in);
        }
    }
}

