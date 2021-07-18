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
package android.support.v7.util;


/**
 * A sparse collection of tiles sorted for efficient access.
 */
class TileList<T> {
    final int mTileSize;

    // Keyed by start position.
    private final android.util.SparseArray<android.support.v7.util.TileList.Tile<T>> mTiles = new android.util.SparseArray<android.support.v7.util.TileList.Tile<T>>(10);

    android.support.v7.util.TileList.Tile<T> mLastAccessedTile;

    public TileList(int tileSize) {
        mTileSize = tileSize;
    }

    public T getItemAt(int pos) {
        if ((mLastAccessedTile == null) || (!mLastAccessedTile.containsPosition(pos))) {
            final int startPosition = pos - (pos % mTileSize);
            final int index = mTiles.indexOfKey(startPosition);
            if (index < 0) {
                return null;
            }
            mLastAccessedTile = mTiles.valueAt(index);
        }
        return mLastAccessedTile.getByPosition(pos);
    }

    public int size() {
        return mTiles.size();
    }

    public void clear() {
        mTiles.clear();
    }

    public android.support.v7.util.TileList.Tile<T> getAtIndex(int index) {
        return mTiles.valueAt(index);
    }

    public android.support.v7.util.TileList.Tile<T> addOrReplace(android.support.v7.util.TileList.Tile<T> newTile) {
        final int index = mTiles.indexOfKey(newTile.mStartPosition);
        if (index < 0) {
            mTiles.put(newTile.mStartPosition, newTile);
            return null;
        }
        android.support.v7.util.TileList.Tile<T> oldTile = mTiles.valueAt(index);
        mTiles.setValueAt(index, newTile);
        if (mLastAccessedTile == oldTile) {
            mLastAccessedTile = newTile;
        }
        return oldTile;
    }

    public android.support.v7.util.TileList.Tile<T> removeAtPos(int startPosition) {
        android.support.v7.util.TileList.Tile<T> tile = mTiles.get(startPosition);
        if (mLastAccessedTile == tile) {
            mLastAccessedTile = null;
        }
        mTiles.delete(startPosition);
        return tile;
    }

    public static class Tile<T> {
        public final T[] mItems;

        public int mStartPosition;

        public int mItemCount;

        android.support.v7.util.TileList.Tile<T> mNext;// Used only for pooling recycled tiles.


        public Tile(java.lang.Class<T> klass, int size) {
            // noinspection unchecked
            mItems = ((T[]) (java.lang.reflect.Array.newInstance(klass, size)));
        }

        boolean containsPosition(int pos) {
            return (mStartPosition <= pos) && (pos < (mStartPosition + mItemCount));
        }

        T getByPosition(int pos) {
            return mItems[pos - mStartPosition];
        }
    }
}

