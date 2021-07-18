/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * A gesture stroke started on a touch down and ended on a touch up. A stroke
 * consists of a sequence of timed points. One or multiple strokes form a gesture.
 */
public class GestureStroke {
    static final float TOUCH_TOLERANCE = 3;

    public final android.graphics.RectF boundingBox;

    public final float length;

    public final float[] points;

    private final long[] timestamps;

    private android.graphics.Path mCachedPath;

    /**
     * A constructor that constructs a gesture stroke from a list of gesture points.
     *
     * @param points
     * 		
     */
    public GestureStroke(java.util.ArrayList<android.gesture.GesturePoint> points) {
        final int count = points.size();
        final float[] tmpPoints = new float[count * 2];
        final long[] times = new long[count];
        android.graphics.RectF bx = null;
        float len = 0;
        int index = 0;
        for (int i = 0; i < count; i++) {
            final android.gesture.GesturePoint p = points.get(i);
            tmpPoints[i * 2] = p.x;
            tmpPoints[(i * 2) + 1] = p.y;
            times[index] = p.timestamp;
            if (bx == null) {
                bx = new android.graphics.RectF();
                bx.top = p.y;
                bx.left = p.x;
                bx.right = p.x;
                bx.bottom = p.y;
                len = 0;
            } else {
                len += java.lang.Math.hypot(p.x - tmpPoints[(i - 1) * 2], p.y - tmpPoints[((i - 1) * 2) + 1]);
                bx.union(p.x, p.y);
            }
            index++;
        }
        timestamps = times;
        this.points = tmpPoints;
        boundingBox = bx;
        length = len;
    }

    /**
     * A faster constructor specially for cloning a stroke.
     */
    private GestureStroke(android.graphics.RectF bbx, float len, float[] pts, long[] times) {
        boundingBox = new android.graphics.RectF(bbx.left, bbx.top, bbx.right, bbx.bottom);
        length = len;
        points = pts.clone();
        timestamps = times.clone();
    }

    @java.lang.Override
    public java.lang.Object clone() {
        return new android.gesture.GestureStroke(boundingBox, length, points, timestamps);
    }

    /**
     * Draws the stroke with a given canvas and paint.
     *
     * @param canvas
     * 		
     */
    void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        if (mCachedPath == null) {
            makePath();
        }
        canvas.drawPath(mCachedPath, paint);
    }

    public android.graphics.Path getPath() {
        if (mCachedPath == null) {
            makePath();
        }
        return mCachedPath;
    }

    private void makePath() {
        final float[] localPoints = points;
        final int count = localPoints.length;
        android.graphics.Path path = null;
        float mX = 0;
        float mY = 0;
        for (int i = 0; i < count; i += 2) {
            float x = localPoints[i];
            float y = localPoints[i + 1];
            if (path == null) {
                path = new android.graphics.Path();
                path.moveTo(x, y);
                mX = x;
                mY = y;
            } else {
                float dx = java.lang.Math.abs(x - mX);
                float dy = java.lang.Math.abs(y - mY);
                if ((dx >= android.gesture.GestureStroke.TOUCH_TOLERANCE) || (dy >= android.gesture.GestureStroke.TOUCH_TOLERANCE)) {
                    path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
            }
        }
        mCachedPath = path;
    }

    /**
     * Converts the stroke to a Path of a given number of points.
     *
     * @param width
     * 		the width of the bounding box of the target path
     * @param height
     * 		the height of the bounding box of the target path
     * @param numSample
     * 		the number of points needed
     * @return the path
     */
    public android.graphics.Path toPath(float width, float height, int numSample) {
        final float[] pts = android.gesture.GestureUtils.temporalSampling(this, numSample);
        final android.graphics.RectF rect = boundingBox;
        android.gesture.GestureUtils.translate(pts, -rect.left, -rect.top);
        float sx = width / rect.width();
        float sy = height / rect.height();
        float scale = (sx > sy) ? sy : sx;
        android.gesture.GestureUtils.scale(pts, scale, scale);
        float mX = 0;
        float mY = 0;
        android.graphics.Path path = null;
        final int count = pts.length;
        for (int i = 0; i < count; i += 2) {
            float x = pts[i];
            float y = pts[i + 1];
            if (path == null) {
                path = new android.graphics.Path();
                path.moveTo(x, y);
                mX = x;
                mY = y;
            } else {
                float dx = java.lang.Math.abs(x - mX);
                float dy = java.lang.Math.abs(y - mY);
                if ((dx >= android.gesture.GestureStroke.TOUCH_TOLERANCE) || (dy >= android.gesture.GestureStroke.TOUCH_TOLERANCE)) {
                    path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
            }
        }
        return path;
    }

    void serialize(java.io.DataOutputStream out) throws java.io.IOException {
        final float[] pts = points;
        final long[] times = timestamps;
        final int count = points.length;
        // Write number of points
        out.writeInt(count / 2);
        for (int i = 0; i < count; i += 2) {
            // Write X
            out.writeFloat(pts[i]);
            // Write Y
            out.writeFloat(pts[i + 1]);
            // Write timestamp
            out.writeLong(times[i / 2]);
        }
    }

    static android.gesture.GestureStroke deserialize(java.io.DataInputStream in) throws java.io.IOException {
        // Number of points
        final int count = in.readInt();
        final java.util.ArrayList<android.gesture.GesturePoint> points = new java.util.ArrayList<android.gesture.GesturePoint>(count);
        for (int i = 0; i < count; i++) {
            points.add(android.gesture.GesturePoint.deserialize(in));
        }
        return new android.gesture.GestureStroke(points);
    }

    /**
     * Invalidates the cached path that is used to render the stroke.
     */
    public void clearPath() {
        if (mCachedPath != null)
            mCachedPath.rewind();

    }

    /**
     * Computes an oriented bounding box of the stroke.
     *
     * @return OrientedBoundingBox
     */
    public android.gesture.OrientedBoundingBox computeOrientedBoundingBox() {
        return android.gesture.GestureUtils.computeOrientedBoundingBox(points);
    }
}

