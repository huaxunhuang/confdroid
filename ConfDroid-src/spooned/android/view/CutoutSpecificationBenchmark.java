/**
 * Copyright (C) 2020 The Android Open Source Project
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


@org.junit.runner.RunWith(androidx.test.runner.AndroidJUnit4.class)
@androidx.test.filters.LargeTest
public class CutoutSpecificationBenchmark {
    private static final java.lang.String TAG = "CutoutSpecificationBenchmark";

    private static final java.lang.String BOTTOM_MARKER = "@bottom";

    private static final java.lang.String DP_MARKER = "@dp";

    private static final java.lang.String RIGHT_MARKER = "@right";

    private static final java.lang.String LEFT_MARKER = "@left";

    private static final java.lang.String DOUBLE_CUTOUT_SPEC = "M 0,0\n" + (((((((((((((((("L -72, 0\n" + "L -69.9940446283, 20.0595537175\n") + "C -69.1582133885, 28.4178661152 -65.2, 32.0 -56.8, 32.0\n") + "L 56.8, 32.0\n") + "C 65.2, 32.0 69.1582133885, 28.4178661152 69.9940446283, 20.0595537175\n") + "L 72, 0\n") + "Z\n") + "@bottom\n") + "M 0,0\n") + "L -72, 0\n") + "L -69.9940446283, -20.0595537175\n") + "C -69.1582133885, -28.4178661152 -65.2, -32.0 -56.8, -32.0\n") + "L 56.8, -32.0\n") + "C 65.2, -32.0 69.1582133885, -28.4178661152 69.9940446283, -20.0595537175\n") + "L 72, 0\n") + "Z\n") + "@dp");

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    private android.content.Context mContext;

    private android.util.DisplayMetrics mDisplayMetrics;

    /**
     * Setup the necessary member field used by test methods.
     */
    @org.junit.Before
    public void setUp() {
        mContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDisplayMetrics = new android.util.DisplayMetrics();
        mContext.getDisplay().getRealMetrics(mDisplayMetrics);
    }

    private static void toRectAndAddToRegion(android.graphics.Path p, android.graphics.Region inoutRegion, android.graphics.Rect inoutRect) {
        final android.graphics.RectF rectF = new android.graphics.RectF();
        /* unused */
        p.computeBounds(rectF, false);
        rectF.round(inoutRect);
        inoutRegion.op(inoutRect, android.graphics.Region.Op.UNION);
    }

    private static void oldMethodParsingSpec(java.lang.String spec, int displayWidth, int displayHeight, float density) {
        android.graphics.Path p = null;
        android.graphics.Rect boundTop = null;
        android.graphics.Rect boundBottom = null;
        android.graphics.Rect safeInset = new android.graphics.Rect();
        java.lang.String bottomSpec = null;
        if (!android.text.TextUtils.isEmpty(spec)) {
            spec = spec.trim();
            final float offsetX;
            if (spec.endsWith(android.view.CutoutSpecificationBenchmark.RIGHT_MARKER)) {
                offsetX = displayWidth;
                spec = spec.substring(0, spec.length() - android.view.CutoutSpecificationBenchmark.RIGHT_MARKER.length()).trim();
            } else
                if (spec.endsWith(android.view.CutoutSpecificationBenchmark.LEFT_MARKER)) {
                    offsetX = 0;
                    spec = spec.substring(0, spec.length() - android.view.CutoutSpecificationBenchmark.LEFT_MARKER.length()).trim();
                } else {
                    offsetX = displayWidth / 2.0F;
                }

            final boolean inDp = spec.endsWith(android.view.CutoutSpecificationBenchmark.DP_MARKER);
            if (inDp) {
                spec = spec.substring(0, spec.length() - android.view.CutoutSpecificationBenchmark.DP_MARKER.length());
            }
            if (spec.contains(android.view.CutoutSpecificationBenchmark.BOTTOM_MARKER)) {
                java.lang.String[] splits = spec.split(android.view.CutoutSpecificationBenchmark.BOTTOM_MARKER, 2);
                spec = splits[0].trim();
                bottomSpec = splits[1].trim();
            }
            final android.graphics.Matrix m = new android.graphics.Matrix();
            final android.graphics.Region r = android.graphics.Region.obtain();
            if (!spec.isEmpty()) {
                try {
                    p = android.util.PathParser.createPathFromPathData(spec);
                } catch (java.lang.Throwable e) {
                    android.util.Log.wtf(android.view.CutoutSpecificationBenchmark.TAG, "Could not inflate cutout: ", e);
                }
                if (p != null) {
                    if (inDp) {
                        m.postScale(density, density);
                    }
                    m.postTranslate(offsetX, 0);
                    p.transform(m);
                    boundTop = new android.graphics.Rect();
                    android.view.CutoutSpecificationBenchmark.toRectAndAddToRegion(p, r, boundTop);
                    safeInset.top = boundTop.bottom;
                }
            }
            if (bottomSpec != null) {
                int bottomInset = 0;
                android.graphics.Path bottomPath = null;
                try {
                    bottomPath = android.util.PathParser.createPathFromPathData(bottomSpec);
                } catch (java.lang.Throwable e) {
                    android.util.Log.wtf(android.view.CutoutSpecificationBenchmark.TAG, "Could not inflate bottom cutout: ", e);
                }
                if (bottomPath != null) {
                    // Keep top transform
                    m.postTranslate(0, displayHeight);
                    bottomPath.transform(m);
                    p.addPath(bottomPath);
                    boundBottom = new android.graphics.Rect();
                    android.view.CutoutSpecificationBenchmark.toRectAndAddToRegion(bottomPath, r, boundBottom);
                    bottomInset = displayHeight - boundBottom.top;
                }
                safeInset.bottom = bottomInset;
            }
        }
    }

    @org.junit.Test
    public void parseByOldMethodForDoubleCutout() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            android.view.CutoutSpecificationBenchmark.oldMethodParsingSpec(android.view.CutoutSpecificationBenchmark.DOUBLE_CUTOUT_SPEC, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, mDisplayMetrics.density);
        } 
    }

    @org.junit.Test
    public void parseByNewMethodForDoubleCutout() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            new android.view.CutoutSpecification.Parser(mDisplayMetrics.density, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels).parse(android.view.CutoutSpecificationBenchmark.DOUBLE_CUTOUT_SPEC);
        } 
    }

    @org.junit.Test
    public void parseLongEdgeCutout() {
        final java.lang.String spec = "M 0,0\n" + ((((((((((((((((((("H 48\n" + "V 48\n") + "H -48\n") + "Z\n") + "@left\n") + "@center_vertical\n") + "M 0,0\n") + "H 48\n") + "V 48\n") + "H -48\n") + "Z\n") + "@left\n") + "@center_vertical\n") + "M 0,0\n") + "H -48\n") + "V 48\n") + "H 48\n") + "Z\n") + "@right\n") + "@dp");
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            new android.view.CutoutSpecification.Parser(mDisplayMetrics.density, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels).parse(spec);
        } 
    }

    @org.junit.Test
    public void parseShortEdgeCutout() {
        final java.lang.String spec = "M 0,0\n" + (((((((((("H 48\n" + "V 48\n") + "H -48\n") + "Z\n") + "@bottom\n") + "M 0,0\n") + "H 48\n") + "V -48\n") + "H -48\n") + "Z\n") + "@dp");
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            new android.view.CutoutSpecification.Parser(mDisplayMetrics.density, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels).parse(spec);
        } 
    }
}

