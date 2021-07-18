/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.graphics;


/**
 * Delegate implementing the native methods of android.graphics.NinePatch
 *
 * Through the layoutlib_create tool, the original native methods of NinePatch have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * Because it's a stateless class to start with, there's no need to keep a {@link DelegateManager}
 * around to map int to instance of the delegate.
 */
public final class NinePatch_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.NinePatch_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.NinePatch_Delegate.class);

    // ---- delegate helper data ----
    /**
     * Cache map for {@link NinePatchChunk}.
     * When the chunks are created they are serialized into a byte[], and both are put
     * in the cache, using a {@link SoftReference} for the chunk. The default Java classes
     * for {@link NinePatch} and {@link NinePatchDrawable} only reference to the byte[] data, and
     * provide this for drawing.
     * Using the cache map allows us to not have to deserialize the byte[] back into a
     * {@link NinePatchChunk} every time a rendering is done.
     */
    private static final java.util.Map<byte[], java.lang.ref.SoftReference<com.android.ninepatch.NinePatchChunk>> sChunkCache = new java.util.HashMap<>();

    // ---- delegate data ----
    private byte[] chunk;

    // ---- Public Helper methods ----
    /**
     * Serializes the given chunk.
     *
     * @return the serialized data for the chunk.
     */
    public static byte[] serialize(com.android.ninepatch.NinePatchChunk chunk) {
        // serialize the chunk to get a byte[]
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = null;
        try {
            oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(chunk);
        } catch (java.io.IOException e) {
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().error(null, "Failed to serialize NinePatchChunk.", e, null);
            return null;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (java.io.IOException ignored) {
                }
            }
        }
        // get the array and add it to the cache
        byte[] array = baos.toByteArray();
        android.graphics.NinePatch_Delegate.sChunkCache.put(array, new java.lang.ref.SoftReference(chunk));
        return array;
    }

    /**
     * Returns a {@link NinePatchChunk} object for the given serialized representation.
     *
     * If the chunk is present in the cache then the object from the cache is returned, otherwise
     * the array is deserialized into a {@link NinePatchChunk} object.
     *
     * @param array
     * 		the serialized representation of the chunk.
     * @return the NinePatchChunk or null if deserialization failed.
     */
    public static com.android.ninepatch.NinePatchChunk getChunk(byte[] array) {
        java.lang.ref.SoftReference<com.android.ninepatch.NinePatchChunk> chunkRef = android.graphics.NinePatch_Delegate.sChunkCache.get(array);
        com.android.ninepatch.NinePatchChunk chunk = (chunkRef == null) ? null : chunkRef.get();
        if (chunk == null) {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(array);
            try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais)) {
                chunk = ((com.android.ninepatch.NinePatchChunk) (ois.readObject()));
                // put back the chunk in the cache
                if (chunk != null) {
                    android.graphics.NinePatch_Delegate.sChunkCache.put(array, new java.lang.ref.SoftReference(chunk));
                }
            } catch (java.io.IOException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to deserialize NinePatchChunk content.", e, null);
                return null;
            } catch (java.lang.ClassNotFoundException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to deserialize NinePatchChunk class.", e, null);
                return null;
            }
        }
        return chunk;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean isNinePatchChunk(byte[] chunk) {
        com.android.ninepatch.NinePatchChunk chunkObject = android.graphics.NinePatch_Delegate.getChunk(chunk);
        return chunkObject != null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long validateNinePatchChunk(byte[] chunk) {
        // the default JNI implementation only checks that the byte[] has the same
        // size as the C struct it represent. Since we cannot do the same check (serialization
        // will return different size depending on content), we do nothing.
        android.graphics.NinePatch_Delegate newDelegate = new android.graphics.NinePatch_Delegate();
        newDelegate.chunk = chunk;
        return android.graphics.NinePatch_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeFinalize(long nativeNinePatch) {
        android.graphics.NinePatch_Delegate delegate = android.graphics.NinePatch_Delegate.sManager.getDelegate(nativeNinePatch);
        if ((delegate != null) && (delegate.chunk != null)) {
            android.graphics.NinePatch_Delegate.sChunkCache.remove(delegate.chunk);
        }
        android.graphics.NinePatch_Delegate.sManager.removeJavaReferenceFor(nativeNinePatch);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeGetTransparentRegion(long bitmapHandle, long chunk, android.graphics.Rect location) {
        return 0;
    }

    static byte[] getChunk(long nativeNinePatch) {
        android.graphics.NinePatch_Delegate delegate = android.graphics.NinePatch_Delegate.sManager.getDelegate(nativeNinePatch);
        if (delegate != null) {
            return delegate.chunk;
        }
        return null;
    }

    public static void clearCache() {
        android.graphics.NinePatch_Delegate.sChunkCache.clear();
    }
}

