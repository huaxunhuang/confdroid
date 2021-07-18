/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.util;


/**
 *
 *
 * @unknown 
 */
public class PathParser {
    static final java.lang.String LOGTAG = android.util.PathParser.class.getSimpleName();

    /**
     *
     *
     * @param pathString
     * 		The string representing a path, the same as "d" string in svg file.
     * @return the generated Path object.
     */
    public static android.graphics.Path createPathFromPathData(java.lang.String pathString) {
        if (pathString == null) {
            throw new java.lang.IllegalArgumentException("Path string can not be null.");
        }
        android.graphics.Path path = new android.graphics.Path();
        android.util.PathParser.nParseStringForPath(path.mNativePath, pathString, pathString.length());
        return path;
    }

    /**
     * Interpret PathData as path commands and insert the commands to the given path.
     *
     * @param data
     * 		The source PathData to be converted.
     * @param outPath
     * 		The Path object where path commands will be inserted.
     */
    public static void createPathFromPathData(android.graphics.Path outPath, android.util.PathParser.PathData data) {
        android.util.PathParser.nCreatePathFromPathData(outPath.mNativePath, data.mNativePathData);
    }

    /**
     *
     *
     * @param pathDataFrom
     * 		The source path represented in PathData
     * @param pathDataTo
     * 		The target path represented in PathData
     * @return whether the <code>nodesFrom</code> can morph into <code>nodesTo</code>
     */
    public static boolean canMorph(android.util.PathParser.PathData pathDataFrom, android.util.PathParser.PathData pathDataTo) {
        return android.util.PathParser.nCanMorph(pathDataFrom.mNativePathData, pathDataTo.mNativePathData);
    }

    /**
     * PathData class is a wrapper around the native PathData object, which contains
     * the result of parsing a path string. Specifically, there are verbs and points
     * associated with each verb stored in PathData. This data can then be used to
     * generate commands to manipulate a Path.
     */
    public static class PathData {
        long mNativePathData = 0;

        public PathData() {
            mNativePathData = android.util.PathParser.nCreateEmptyPathData();
        }

        public PathData(android.util.PathParser.PathData data) {
            mNativePathData = android.util.PathParser.nCreatePathData(data.mNativePathData);
        }

        public PathData(java.lang.String pathString) {
            mNativePathData = android.util.PathParser.nCreatePathDataFromString(pathString, pathString.length());
            if (mNativePathData == 0) {
                throw new java.lang.IllegalArgumentException("Invalid pathData: " + pathString);
            }
        }

        public long getNativePtr() {
            return mNativePathData;
        }

        /**
         * Update the path data to match the source.
         * Before calling this, make sure canMorph(target, source) is true.
         *
         * @param source
         * 		The source path represented in PathData
         */
        public void setPathData(android.util.PathParser.PathData source) {
            android.util.PathParser.nSetPathData(mNativePathData, source.mNativePathData);
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            if (mNativePathData != 0) {
                android.util.PathParser.nFinalize(mNativePathData);
                mNativePathData = 0;
            }
            super.finalize();
        }
    }

    /**
     * Interpolate between the <code>fromData</code> and <code>toData</code> according to the
     * <code>fraction</code>, and put the resulting path data into <code>outData</code>.
     *
     * @param outData
     * 		The resulting PathData of the interpolation
     * @param fromData
     * 		The start value as a PathData.
     * @param toData
     * 		The end value as a PathData
     * @param fraction
     * 		The fraction to interpolate.
     */
    public static boolean interpolatePathData(android.util.PathParser.PathData outData, android.util.PathParser.PathData fromData, android.util.PathParser.PathData toData, float fraction) {
        return android.util.PathParser.nInterpolatePathData(outData.mNativePathData, fromData.mNativePathData, toData.mNativePathData, fraction);
    }

    // Native functions are defined below.
    private static native void nParseStringForPath(long pathPtr, java.lang.String pathString, int stringLength);

    private static native void nCreatePathFromPathData(long outPathPtr, long pathData);

    private static native long nCreateEmptyPathData();

    private static native long nCreatePathData(long nativePtr);

    private static native long nCreatePathDataFromString(java.lang.String pathString, int stringLength);

    private static native boolean nInterpolatePathData(long outDataPtr, long fromDataPtr, long toDataPtr, float fraction);

    private static native void nFinalize(long nativePtr);

    private static native boolean nCanMorph(long fromDataPtr, long toDataPtr);

    private static native void nSetPathData(long outDataPtr, long fromDataPtr);
}

