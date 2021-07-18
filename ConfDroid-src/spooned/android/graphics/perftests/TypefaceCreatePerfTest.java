/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.graphics.perftests;


@androidx.test.filters.LargeTest
@org.junit.runner.RunWith(androidx.test.runner.AndroidJUnit4.class)
public class TypefaceCreatePerfTest {
    // A font file name in asset directory.
    private static final java.lang.String TEST_FONT_NAME = "DancingScript-Regular.ttf";

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testCreate_fromFamily() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            android.graphics.Typeface face = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL);
        } 
    }

    @org.junit.Test
    public void testCreate_fromFamilyName() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            android.graphics.Typeface face = android.graphics.Typeface.create("monospace", android.graphics.Typeface.NORMAL);
        } 
    }

    @org.junit.Test
    public void testCreate_fromAsset() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.content.Context context = androidx.test.InstrumentationRegistry.getContext();
        final android.content.res.AssetManager am = context.getAssets();
        while (state.keepRunning()) {
            android.graphics.Typeface face = android.graphics.Typeface.createFromAsset(am, android.graphics.perftests.TypefaceCreatePerfTest.TEST_FONT_NAME);
        } 
    }

    @org.junit.Test
    public void testCreate_fromFile() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.content.Context context = androidx.test.InstrumentationRegistry.getContext();
        final android.content.res.AssetManager am = context.getAssets();
        java.io.File outFile = null;
        try {
            outFile = java.io.File.createTempFile("example", "ttf", context.getCacheDir());
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        }
        try (java.io.InputStream in = am.open(android.graphics.perftests.TypefaceCreatePerfTest.TEST_FONT_NAME);java.io.OutputStream out = new java.io.FileOutputStream(outFile)) {
            byte[] buf = new byte[1024];
            int n = 0;
            while ((n = in.read(buf)) != (-1)) {
                out.write(buf, 0, n);
            } 
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        }
        while (state.keepRunning()) {
            android.graphics.Typeface face = android.graphics.Typeface.createFromFile(outFile);
        } 
        outFile.delete();
    }

    @org.junit.Test
    public void testCreate_fromResources() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.content.res.Resources r = androidx.test.InstrumentationRegistry.getContext().getResources();
        while (state.keepRunning()) {
            android.graphics.Typeface face = r.getFont(R.font.samplefont);
        } 
    }
}

