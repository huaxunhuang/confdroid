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
 * Base class for true Gradient shader delegate.
 */
public abstract class Gradient_Delegate extends android.graphics.Shader_Delegate {
    protected final int[] mColors;

    protected final float[] mPositions;

    @java.lang.Override
    public boolean isSupported() {
        // all gradient shaders are supported.
        return true;
    }

    @java.lang.Override
    public java.lang.String getSupportMessage() {
        // all gradient shaders are supported, no need for a gradient support
        return null;
    }

    /**
     * Creates the base shader and do some basic test on the parameters.
     *
     * @param nativeMatrix
     * 		reference to the shader's native transformation matrix
     * @param colors
     * 		The colors to be distributed along the gradient line
     * @param positions
     * 		May be null. The relative positions [0..1] of each
     * 		corresponding color in the colors array. If this is null, the
     * 		the colors are distributed evenly along the gradient line.
     */
    protected Gradient_Delegate(long nativeMatrix, long[] colors, float[] positions) {
        super(nativeMatrix);
        assert colors.length >= 2 : "needs >= 2 number of colors";
        if (positions == null) {
            float spacing = 1.0F / (colors.length - 1);
            positions = new float[colors.length];
            positions[0] = 0.0F;
            positions[colors.length - 1] = 1.0F;
            for (int i = 1; i < (colors.length - 1); i++) {
                positions[i] = spacing * i;
            }
        } else {
            assert colors.length == positions.length : "color and position " + "arrays must be of equal length";
            positions[0] = java.lang.Math.min(java.lang.Math.max(0, positions[0]), 1);
            for (int i = 1; i < positions.length; i++) {
                positions[i] = java.lang.Math.min(java.lang.Math.max(positions[i - 1], positions[i]), 1);
            }
        }
        mColors = java.util.Arrays.stream(colors).mapToInt(android.graphics.Color::toArgb).toArray();
        mPositions = positions;
    }

    /**
     * Base class for (Java) Gradient Paints. This handles computing the gradient colors based
     * on the color and position lists, as well as the {@link TileMode}
     */
    protected static abstract class GradientPaint implements java.awt.Paint {
        private static final int GRADIENT_SIZE = 100;

        private final int[] mColors;

        private final float[] mPositions;

        private final android.graphics.Shader.TileMode mTileMode;

        private int[] mGradient;

        protected GradientPaint(int[] colors, float[] positions, android.graphics.Shader.TileMode tileMode) {
            mColors = colors;
            mPositions = positions;
            mTileMode = tileMode;
        }

        @java.lang.Override
        public int getTransparency() {
            return java.awt.Paint.TRANSLUCENT;
        }

        /**
         * Pre-computes the colors for the gradient. This must be called once before any call
         * to {@link #getGradientColor(float)}
         */
        protected void precomputeGradientColors() {
            if (mGradient == null) {
                // actually create an array with an extra size, so that we can really go
                // from 0 to SIZE (100%), or currentPos in the loop below will never equal 1.0
                mGradient = new int[android.graphics.Gradient_Delegate.GradientPaint.GRADIENT_SIZE + 1];
                int prevPos = 0;
                int nextPos = 1;
                for (int i = 0; i <= android.graphics.Gradient_Delegate.GradientPaint.GRADIENT_SIZE; i++) {
                    // compute current position
                    float currentPos = ((float) (i)) / android.graphics.Gradient_Delegate.GradientPaint.GRADIENT_SIZE;
                    if (currentPos < mPositions[0]) {
                        mGradient[i] = mColors[0];
                        continue;
                    }
                    while ((nextPos < mPositions.length) && (currentPos >= mPositions[nextPos])) {
                        prevPos = nextPos++;
                    } 
                    if ((nextPos == mPositions.length) || (currentPos == prevPos)) {
                        mGradient[i] = mColors[prevPos];
                    } else {
                        float percent = (currentPos - mPositions[prevPos]) / (mPositions[nextPos] - mPositions[prevPos]);
                        mGradient[i] = computeColor(mColors[prevPos], mColors[nextPos], percent);
                    }
                }
            }
        }

        /**
         * Returns the color based on the position in the gradient.
         * <var>pos</var> can be anything, even &lt; 0 or &gt; > 1, as the gradient
         * will use {@link TileMode} value to convert it into a [0,1] value.
         */
        protected int getGradientColor(float pos) {
            if (pos < 0.0F) {
                if (mTileMode != null) {
                    switch (mTileMode) {
                        case CLAMP :
                            pos = 0.0F;
                            break;
                        case REPEAT :
                            // remove the integer part to stay in the [0,1] range.
                            // we also need to invert the value from [-1,0] to [0, 1]
                            pos = pos - ((float) (java.lang.Math.floor(pos)));
                            break;
                        case MIRROR :
                            // this is the same as the positive side, just make the value positive
                            // first.
                            pos = java.lang.Math.abs(pos);
                            // get the integer and the decimal part
                            int intPart = ((int) (java.lang.Math.floor(pos)));
                            pos = pos - intPart;
                            // 0 -> 1 : normal order
                            // 1 -> 2: mirrored
                            // etc..
                            // this means if the intpart is odd we invert
                            if ((intPart % 2) == 1) {
                                pos = 1.0F - pos;
                            }
                            break;
                    }
                } else {
                    pos = 0.0F;
                }
            } else
                if (pos > 1.0F) {
                    if (mTileMode != null) {
                        switch (mTileMode) {
                            case CLAMP :
                                pos = 1.0F;
                                break;
                            case REPEAT :
                                // remove the integer part to stay in the [0,1] range
                                pos = pos - ((float) (java.lang.Math.floor(pos)));
                                break;
                            case MIRROR :
                                // get the integer and the decimal part
                                int intPart = ((int) (java.lang.Math.floor(pos)));
                                pos = pos - intPart;
                                // 0 -> 1 : normal order
                                // 1 -> 2: mirrored
                                // etc..
                                // this means if the intpart is odd we invert
                                if ((intPart % 2) == 1) {
                                    pos = 1.0F - pos;
                                }
                                break;
                        }
                    } else {
                        pos = 1.0F;
                    }
                }

            int index = ((int) ((pos * android.graphics.Gradient_Delegate.GradientPaint.GRADIENT_SIZE) + 0.5));
            return mGradient[index];
        }

        /**
         * Returns the color between c1, and c2, based on the percent of the distance
         * between c1 and c2.
         */
        private int computeColor(int c1, int c2, float percent) {
            int a = computeChannel((c1 >> 24) & 0xff, (c2 >> 24) & 0xff, percent);
            int r = computeChannel((c1 >> 16) & 0xff, (c2 >> 16) & 0xff, percent);
            int g = computeChannel((c1 >> 8) & 0xff, (c2 >> 8) & 0xff, percent);
            int b = computeChannel(c1 & 0xff, c2 & 0xff, percent);
            return (((a << 24) | (r << 16)) | (g << 8)) | b;
        }

        /**
         * Returns the channel value between 2 values based on the percent of the distance between
         * the 2 values..
         */
        private int computeChannel(int c1, int c2, float percent) {
            return c1 + ((int) ((percent * (c2 - c1)) + 0.5));
        }
    }
}

