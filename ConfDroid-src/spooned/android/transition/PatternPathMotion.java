/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.transition;


/**
 * A PathMotion that takes a Path pattern and applies it to the separation between two points.
 * The starting point of the Path will be moved to the origin and the end point will be scaled
 * and rotated so that it matches with the target end point.
 * <p>This may be used in XML as an element inside a transition.</p>
 * <pre>{@code <changeBounds>
 *     <patternPathMotion android:patternPathData="M0 0 L0 100 L100 100"/>
 * </changeBounds>}
 * </pre>
 */
public class PatternPathMotion extends android.transition.PathMotion {
    private android.graphics.Path mOriginalPatternPath;

    private final android.graphics.Path mPatternPath = new android.graphics.Path();

    private final android.graphics.Matrix mTempMatrix = new android.graphics.Matrix();

    /**
     * Constructs a PatternPathMotion with a straight-line pattern.
     */
    public PatternPathMotion() {
        mPatternPath.lineTo(1, 0);
        mOriginalPatternPath = mPatternPath;
    }

    public PatternPathMotion(android.content.Context context, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PatternPathMotion);
        try {
            java.lang.String pathData = a.getString(R.styleable.PatternPathMotion_patternPathData);
            if (pathData == null) {
                throw new java.lang.RuntimeException("pathData must be supplied for patternPathMotion");
            }
            android.graphics.Path pattern = android.util.PathParser.createPathFromPathData(pathData);
            setPatternPath(pattern);
        } finally {
            a.recycle();
        }
    }

    /**
     * Creates a PatternPathMotion with the Path defining a pattern of motion between two
     * coordinates. The pattern will be translated, rotated, and scaled to fit between the start
     * and end points. The pattern must not be empty and must have the end point differ from the
     * start point.
     *
     * @param patternPath
     * 		A Path to be used as a pattern for two-dimensional motion.
     */
    public PatternPathMotion(android.graphics.Path patternPath) {
        setPatternPath(patternPath);
    }

    /**
     * Returns the Path defining a pattern of motion between two coordinates.
     * The pattern will be translated, rotated, and scaled to fit between the start and end points.
     * The pattern must not be empty and must have the end point differ from the start point.
     *
     * @return the Path defining a pattern of motion between two coordinates.
     * @unknown ref android.R.styleable#PatternPathMotion_patternPathData
     */
    public android.graphics.Path getPatternPath() {
        return mOriginalPatternPath;
    }

    /**
     * Sets the Path defining a pattern of motion between two coordinates.
     * The pattern will be translated, rotated, and scaled to fit between the start and end points.
     * The pattern must not be empty and must have the end point differ from the start point.
     *
     * @param patternPath
     * 		A Path to be used as a pattern for two-dimensional motion.
     * @unknown ref android.R.styleable#PatternPathMotion_patternPathData
     */
    public void setPatternPath(android.graphics.Path patternPath) {
        android.graphics.PathMeasure pathMeasure = new android.graphics.PathMeasure(patternPath, false);
        float length = pathMeasure.getLength();
        float[] pos = new float[2];
        pathMeasure.getPosTan(length, pos, null);
        float endX = pos[0];
        float endY = pos[1];
        pathMeasure.getPosTan(0, pos, null);
        float startX = pos[0];
        float startY = pos[1];
        if ((startX == endX) && (startY == endY)) {
            throw new java.lang.IllegalArgumentException("pattern must not end at the starting point");
        }
        mTempMatrix.setTranslate(-startX, -startY);
        float dx = endX - startX;
        float dy = endY - startY;
        float distance = ((float) (java.lang.Math.hypot(dx, dy)));
        float scale = 1 / distance;
        mTempMatrix.postScale(scale, scale);
        double angle = java.lang.Math.atan2(dy, dx);
        mTempMatrix.postRotate(((float) (java.lang.Math.toDegrees(-angle))));
        patternPath.transform(mTempMatrix, mPatternPath);
        mOriginalPatternPath = patternPath;
    }

    @java.lang.Override
    public android.graphics.Path getPath(float startX, float startY, float endX, float endY) {
        double dx = endX - startX;
        double dy = endY - startY;
        float length = ((float) (java.lang.Math.hypot(dx, dy)));
        double angle = java.lang.Math.atan2(dy, dx);
        mTempMatrix.setScale(length, length);
        mTempMatrix.postRotate(((float) (java.lang.Math.toDegrees(angle))));
        mTempMatrix.postTranslate(startX, startY);
        android.graphics.Path path = new android.graphics.Path();
        mPatternPath.transform(mTempMatrix, path);
        return path;
    }
}

