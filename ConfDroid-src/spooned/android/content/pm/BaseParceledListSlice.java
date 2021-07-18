/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.content.pm;


/**
 * Transfer a large list of Parcelable objects across an IPC.  Splits into
 * multiple transactions if needed.
 *
 * Caveat: for efficiency and security, all elements must be the same concrete type.
 * In order to avoid writing the class name of each object, we must ensure that
 * each object is the same type, or else unparceling then reparceling the data may yield
 * a different result if the class name encoded in the Parcelable is a Base type.
 * See b/17671747.
 *
 * @unknown 
 */
abstract class BaseParceledListSlice<T> implements android.os.Parcelable {
    private static java.lang.String TAG = "ParceledListSlice";

    private static boolean DEBUG = false;

    /* TODO get this number from somewhere else. For now set it to a quarter of
    the 1MB limit.
     */
    private static final int MAX_IPC_SIZE = android.os.IBinder.MAX_IPC_SIZE;

    private final java.util.List<T> mList;

    private int mInlineCountLimit = java.lang.Integer.MAX_VALUE;

    public BaseParceledListSlice(java.util.List<T> list) {
        mList = list;
    }

    @java.lang.SuppressWarnings("unchecked")
    BaseParceledListSlice(android.os.Parcel p, java.lang.ClassLoader loader) {
        final int N = p.readInt();
        mList = new java.util.ArrayList<T>(N);
        if (android.content.pm.BaseParceledListSlice.DEBUG)
            android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, ("Retrieving " + N) + " items");

        if (N <= 0) {
            return;
        }
        android.os.Parcelable.Creator<?> creator = readParcelableCreator(p, loader);
        java.lang.Class<?> listElementClass = null;
        int i = 0;
        while (i < N) {
            if (p.readInt() == 0) {
                break;
            }
            final T parcelable = readCreator(creator, p, loader);
            if (listElementClass == null) {
                listElementClass = parcelable.getClass();
            } else {
                android.content.pm.BaseParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
            }
            mList.add(parcelable);
            if (android.content.pm.BaseParceledListSlice.DEBUG)
                android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Read inline #" + i) + ": ") + mList.get(mList.size() - 1));

            i++;
        } 
        if (i >= N) {
            return;
        }
        final android.os.IBinder retriever = p.readStrongBinder();
        while (i < N) {
            if (android.content.pm.BaseParceledListSlice.DEBUG)
                android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (((("Reading more @" + i) + " of ") + N) + ": retriever=") + retriever);

            android.os.Parcel data = android.os.Parcel.obtain();
            android.os.Parcel reply = android.os.Parcel.obtain();
            data.writeInt(i);
            try {
                retriever.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.content.pm.BaseParceledListSlice.TAG, (("Failure retrieving array; only received " + i) + " of ") + N, e);
                return;
            }
            while ((i < N) && (reply.readInt() != 0)) {
                final T parcelable = readCreator(creator, reply, loader);
                android.content.pm.BaseParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                mList.add(parcelable);
                if (android.content.pm.BaseParceledListSlice.DEBUG)
                    android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Read extra #" + i) + ": ") + mList.get(mList.size() - 1));

                i++;
            } 
            reply.recycle();
            data.recycle();
        } 
    }

    private T readCreator(android.os.Parcelable.Creator<?> creator, android.os.Parcel p, java.lang.ClassLoader loader) {
        if (creator instanceof android.os.Parcelable.ClassLoaderCreator<?>) {
            android.os.Parcelable.ClassLoaderCreator<?> classLoaderCreator = ((android.os.Parcelable.ClassLoaderCreator<?>) (creator));
            return ((T) (classLoaderCreator.createFromParcel(p, loader)));
        }
        return ((T) (creator.createFromParcel(p)));
    }

    private static void verifySameType(final java.lang.Class<?> expected, final java.lang.Class<?> actual) {
        if (!actual.equals(expected)) {
            throw new java.lang.IllegalArgumentException((("Can't unparcel type " + (actual == null ? null : actual.getName())) + " in list of type ") + (expected == null ? null : expected.getName()));
        }
    }

    @android.annotation.UnsupportedAppUsage
    public java.util.List<T> getList() {
        return mList;
    }

    /**
     * Set a limit on the maximum number of entries in the array that will be included
     * inline in the initial parcelling of this object.
     */
    public void setInlineCountLimit(int maxCount) {
        mInlineCountLimit = maxCount;
    }

    /**
     * Write this to another Parcel. Note that this discards the internal Parcel
     * and should not be used anymore. This is so we can pass this to a Binder
     * where we won't have a chance to call recycle on this.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        final int N = mList.size();
        final int callFlags = flags;
        dest.writeInt(N);
        if (android.content.pm.BaseParceledListSlice.DEBUG)
            android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, ("Writing " + N) + " items");

        if (N > 0) {
            final java.lang.Class<?> listElementClass = mList.get(0).getClass();
            writeParcelableCreator(mList.get(0), dest);
            int i = 0;
            while (((i < N) && (i < mInlineCountLimit)) && (dest.dataSize() < android.content.pm.BaseParceledListSlice.MAX_IPC_SIZE)) {
                dest.writeInt(1);
                final T parcelable = mList.get(i);
                android.content.pm.BaseParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                writeElement(parcelable, dest, callFlags);
                if (android.content.pm.BaseParceledListSlice.DEBUG)
                    android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Wrote inline #" + i) + ": ") + mList.get(i));

                i++;
            } 
            if (i < N) {
                dest.writeInt(0);
                android.os.Binder retriever = new android.os.Binder() {
                    @java.lang.Override
                    protected boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
                        if (code != FIRST_CALL_TRANSACTION) {
                            return super.onTransact(code, data, reply, flags);
                        }
                        int i = data.readInt();
                        if (android.content.pm.BaseParceledListSlice.DEBUG)
                            android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Writing more @" + i) + " of ") + N);

                        while ((i < N) && (reply.dataSize() < android.content.pm.BaseParceledListSlice.MAX_IPC_SIZE)) {
                            reply.writeInt(1);
                            final T parcelable = mList.get(i);
                            android.content.pm.BaseParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                            writeElement(parcelable, reply, callFlags);
                            if (android.content.pm.BaseParceledListSlice.DEBUG)
                                android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Wrote extra #" + i) + ": ") + mList.get(i));

                            i++;
                        } 
                        if (i < N) {
                            if (android.content.pm.BaseParceledListSlice.DEBUG)
                                android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (("Breaking @" + i) + " of ") + N);

                            reply.writeInt(0);
                        }
                        return true;
                    }
                };
                if (android.content.pm.BaseParceledListSlice.DEBUG)
                    android.util.Log.d(android.content.pm.BaseParceledListSlice.TAG, (((("Breaking @" + i) + " of ") + N) + ": retriever=") + retriever);

                dest.writeStrongBinder(retriever);
            }
        }
    }

    protected abstract void writeElement(T parcelable, android.os.Parcel reply, int callFlags);

    @android.annotation.UnsupportedAppUsage
    protected abstract void writeParcelableCreator(T parcelable, android.os.Parcel dest);

    protected abstract android.os.Parcelable.Creator<?> readParcelableCreator(android.os.Parcel from, java.lang.ClassLoader loader);
}

