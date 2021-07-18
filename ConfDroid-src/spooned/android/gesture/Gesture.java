/**
 * Copyright (C) 2008-2009 The Android Open Source Project
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
package android.gesture;


/**
 * A gesture is a hand-drawn shape on a touch screen. It can have one or multiple strokes.
 * Each stroke is a sequence of timed points. A user-defined gesture can be recognized by
 * a GestureLibrary.
 */
public class Gesture implements android.os.Parcelable {
    private static final long GESTURE_ID_BASE = java.lang.System.currentTimeMillis();

    private static final int BITMAP_RENDERING_WIDTH = 2;

    private static final boolean BITMAP_RENDERING_ANTIALIAS = true;

    private static final boolean BITMAP_RENDERING_DITHER = true;

    private static final java.util.concurrent.atomic.AtomicInteger sGestureCount = new java.util.concurrent.atomic.AtomicInteger(0);

    private final android.graphics.RectF mBoundingBox = new android.graphics.RectF();

    // the same as its instance ID
    private long mGestureID;

    private final java.util.ArrayList<android.gesture.GestureStroke> mStrokes = new java.util.ArrayList<android.gesture.GestureStroke>();

    public Gesture() {
        mGestureID = android.gesture.Gesture.GESTURE_ID_BASE + android.gesture.Gesture.sGestureCount.incrementAndGet();
    }

    @java.lang.Override
    public java.lang.Object clone() {
        android.gesture.Gesture gesture = new android.gesture.Gesture();
        gesture.mBoundingBox.set(mBoundingBox.left, mBoundingBox.top, mBoundingBox.right, mBoundingBox.bottom);
        final int count = mStrokes.size();
        for (int i = 0; i < count; i++) {
            android.gesture.GestureStroke stroke = mStrokes.get(i);
            gesture.mStrokes.add(((android.gesture.GestureStroke) (stroke.clone())));
        }
        return gesture;
    }

    /**
     *
     *
     * @return all the strokes of the gesture
     */
    public java.util.ArrayList<android.gesture.GestureStroke> getStrokes() {
        return mStrokes;
    }

    /**
     *
     *
     * @return the number of strokes included by this gesture
     */
    public int getStrokesCount() {
        return mStrokes.size();
    }

    /**
     * Adds a stroke to the gesture.
     *
     * @param stroke
     * 		
     */
    public void addStroke(android.gesture.GestureStroke stroke) {
        mStrokes.add(stroke);
        mBoundingBox.union(stroke.boundingBox);
    }

    /**
     * Calculates the total length of the gesture. When there are multiple strokes in
     * the gesture, this returns the sum of the lengths of all the strokes.
     *
     * @return the length of the gesture
     */
    public float getLength() {
        int len = 0;
        final java.util.ArrayList<android.gesture.GestureStroke> strokes = mStrokes;
        final int count = strokes.size();
        for (int i = 0; i < count; i++) {
            len += strokes.get(i).length;
        }
        return len;
    }

    /**
     *
     *
     * @return the bounding box of the gesture
     */
    public android.graphics.RectF getBoundingBox() {
        return mBoundingBox;
    }

    public android.graphics.Path toPath() {
        return toPath(null);
    }

    public android.graphics.Path toPath(android.graphics.Path path) {
        if (path == null)
            path = new android.graphics.Path();

        final java.util.ArrayList<android.gesture.GestureStroke> strokes = mStrokes;
        final int count = strokes.size();
        for (int i = 0; i < count; i++) {
            path.addPath(strokes.get(i).getPath());
        }
        return path;
    }

    public android.graphics.Path toPath(int width, int height, int edge, int numSample) {
        return toPath(null, width, height, edge, numSample);
    }

    public android.graphics.Path toPath(android.graphics.Path path, int width, int height, int edge, int numSample) {
        if (path == null)
            path = new android.graphics.Path();

        final java.util.ArrayList<android.gesture.GestureStroke> strokes = mStrokes;
        final int count = strokes.size();
        for (int i = 0; i < count; i++) {
            path.addPath(strokes.get(i).toPath(width - (2 * edge), height - (2 * edge), numSample));
        }
        return path;
    }

    /**
     * Sets the id of the gesture.
     *
     * @param id
     * 		
     */
    void setID(long id) {
        mGestureID = id;
    }

    /**
     *
     *
     * @return the id of the gesture
     */
    public long getID() {
        return mGestureID;
    }

    /**
     * Creates a bitmap of the gesture with a transparent background.
     *
     * @param width
     * 		width of the target bitmap
     * @param height
     * 		height of the target bitmap
     * @param edge
     * 		the edge
     * @param numSample
     * 		
     * @param color
     * 		
     * @return the bitmap
     */
    public android.graphics.Bitmap toBitmap(int width, int height, int edge, int numSample, int color) {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        final android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        canvas.translate(edge, edge);
        final android.graphics.Paint paint = new android.graphics.Paint();
        paint.setAntiAlias(android.gesture.Gesture.BITMAP_RENDERING_ANTIALIAS);
        paint.setDither(android.gesture.Gesture.BITMAP_RENDERING_DITHER);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        paint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        paint.setStrokeWidth(android.gesture.Gesture.BITMAP_RENDERING_WIDTH);
        final java.util.ArrayList<android.gesture.GestureStroke> strokes = mStrokes;
        final int count = strokes.size();
        for (int i = 0; i < count; i++) {
            android.graphics.Path path = strokes.get(i).toPath(width - (2 * edge), height - (2 * edge), numSample);
            canvas.drawPath(path, paint);
        }
        return bitmap;
    }

    /**
     * Creates a bitmap of the gesture with a transparent background.
     *
     * @param width
     * 		
     * @param height
     * 		
     * @param inset
     * 		
     * @param color
     * 		
     * @return the bitmap
     */
    public android.graphics.Bitmap toBitmap(int width, int height, int inset, int color) {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        final android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        final android.graphics.Paint paint = new android.graphics.Paint();
        paint.setAntiAlias(android.gesture.Gesture.BITMAP_RENDERING_ANTIALIAS);
        paint.setDither(android.gesture.Gesture.BITMAP_RENDERING_DITHER);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        paint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        paint.setStrokeWidth(android.gesture.Gesture.BITMAP_RENDERING_WIDTH);
        final android.graphics.Path path = toPath();
        final android.graphics.RectF bounds = new android.graphics.RectF();
        path.computeBounds(bounds, true);
        final float sx = (width - (2 * inset)) / bounds.width();
        final float sy = (height - (2 * inset)) / bounds.height();
        final float scale = (sx > sy) ? sy : sx;
        paint.setStrokeWidth(2.0F / scale);
        path.offset((-bounds.left) + ((width - (bounds.width() * scale)) / 2.0F), (-bounds.top) + ((height - (bounds.height() * scale)) / 2.0F));
        canvas.translate(inset, inset);
        canvas.scale(scale, scale);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    void serialize(java.io.DataOutputStream out) throws java.io.IOException {
        final java.util.ArrayList<android.gesture.GestureStroke> strokes = mStrokes;
        final int count = strokes.size();
        // Write gesture ID
        out.writeLong(mGestureID);
        // Write number of strokes
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            strokes.get(i).serialize(out);
        }
    }

    static android.gesture.Gesture deserialize(java.io.DataInputStream in) throws java.io.IOException {
        final android.gesture.Gesture gesture = new android.gesture.Gesture();
        // Gesture ID
        gesture.mGestureID = in.readLong();
        // Number of strokes
        final int count = in.readInt();
        for (int i = 0; i < count; i++) {
            gesture.addStroke(android.gesture.GestureStroke.deserialize(in));
        }
        return gesture;
    }

    public static final android.os.Parcelable.Creator<android.gesture.Gesture> CREATOR = new android.os.Parcelable.Creator<android.gesture.Gesture>() {
        public android.gesture.Gesture createFromParcel(android.os.Parcel in) {
            android.gesture.Gesture gesture = null;
            final long gestureID = in.readLong();
            final java.io.DataInputStream inStream = new java.io.DataInputStream(new java.io.ByteArrayInputStream(in.createByteArray()));
            try {
                gesture = android.gesture.Gesture.deserialize(inStream);
            } catch (java.io.IOException e) {
                android.util.Log.e(android.gesture.GestureConstants.LOG_TAG, "Error reading Gesture from parcel:", e);
            } finally {
                android.gesture.GestureUtils.closeStream(inStream);
            }
            if (gesture != null) {
                gesture.mGestureID = gestureID;
            }
            return gesture;
        }

        public android.gesture.Gesture[] newArray(int size) {
            return new android.gesture.Gesture[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(mGestureID);
        boolean result = false;
        final java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream(android.gesture.GestureConstants.IO_BUFFER_SIZE);
        final java.io.DataOutputStream outStream = new java.io.DataOutputStream(byteStream);
        try {
            serialize(outStream);
            result = true;
        } catch (java.io.IOException e) {
            android.util.Log.e(android.gesture.GestureConstants.LOG_TAG, "Error writing Gesture to parcel:", e);
        } finally {
            android.gesture.GestureUtils.closeStream(outStream);
            android.gesture.GestureUtils.closeStream(byteStream);
        }
        if (result) {
            out.writeByteArray(byteStream.toByteArray());
        }
    }

    public int describeContents() {
        return 0;
    }
}

