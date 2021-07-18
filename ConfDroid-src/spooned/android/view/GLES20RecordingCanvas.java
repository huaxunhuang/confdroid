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
package android.view;


/**
 * An implementation of a GL canvas that records drawing operations.
 * This is intended for use with a DisplayList. This class keeps a list of all the Paint and
 * Bitmap objects that it draws, preventing the backing memory of Bitmaps from being freed while
 * the DisplayList is still holding a native reference to the memory.
 */
class GLES20RecordingCanvas extends android.view.GLES20Canvas {
    // The recording canvas pool should be large enough to handle a deeply nested
    // view hierarchy because display lists are generated recursively.
    private static final int POOL_LIMIT = 25;

    private static final android.util.Pools.SynchronizedPool<android.view.GLES20RecordingCanvas> sPool = new android.util.Pools.SynchronizedPool<android.view.GLES20RecordingCanvas>(android.view.GLES20RecordingCanvas.POOL_LIMIT);

    android.view.RenderNode mNode;

    private GLES20RecordingCanvas() {
        super();
    }

    static android.view.GLES20RecordingCanvas obtain(@android.annotation.NonNull
    android.view.RenderNode node) {
        if (node == null)
            throw new java.lang.IllegalArgumentException("node cannot be null");

        android.view.GLES20RecordingCanvas canvas = android.view.GLES20RecordingCanvas.sPool.acquire();
        if (canvas == null) {
            canvas = new android.view.GLES20RecordingCanvas();
        }
        canvas.mNode = node;
        return canvas;
    }

    void recycle() {
        mNode = null;
        android.view.GLES20RecordingCanvas.sPool.release(this);
    }

    long finishRecording() {
        return android.view.GLES20Canvas.nFinishRecording(mRenderer);
    }

    @java.lang.Override
    public boolean isRecordingFor(java.lang.Object o) {
        return o == mNode;
    }
}

