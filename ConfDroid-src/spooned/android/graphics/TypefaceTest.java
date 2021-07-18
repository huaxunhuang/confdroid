/**
 * Copyright (C) 2008 The Android Open Source Project
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


public class TypefaceTest extends junit.framework.TestCase {
    // create array of all std faces
    private final android.graphics.Typeface[] mFaces = new android.graphics.Typeface[]{ android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, 0), android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, 1), android.graphics.Typeface.create(android.graphics.Typeface.SERIF, 0), android.graphics.Typeface.create(android.graphics.Typeface.SERIF, 1), android.graphics.Typeface.create(android.graphics.Typeface.SERIF, 2), android.graphics.Typeface.create(android.graphics.Typeface.SERIF, 3), android.graphics.Typeface.create(android.graphics.Typeface.MONOSPACE, 0) };

    @android.test.suitebuilder.annotation.SmallTest
    public void testBasic() throws java.lang.Exception {
        junit.framework.TestCase.assertTrue("basic", android.graphics.Typeface.DEFAULT != null);
        junit.framework.TestCase.assertTrue("basic", android.graphics.Typeface.DEFAULT_BOLD != null);
        junit.framework.TestCase.assertTrue("basic", android.graphics.Typeface.SANS_SERIF != null);
        junit.framework.TestCase.assertTrue("basic", android.graphics.Typeface.SERIF != null);
        junit.framework.TestCase.assertTrue("basic", android.graphics.Typeface.MONOSPACE != null);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testUnique() throws java.lang.Exception {
        final int n = mFaces.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                junit.framework.TestCase.assertTrue("unique", mFaces[i] != mFaces[j]);
            }
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testStyles() throws java.lang.Exception {
        junit.framework.TestCase.assertTrue("style", mFaces[0].getStyle() == android.graphics.Typeface.NORMAL);
        junit.framework.TestCase.assertTrue("style", mFaces[1].getStyle() == android.graphics.Typeface.BOLD);
        junit.framework.TestCase.assertTrue("style", mFaces[2].getStyle() == android.graphics.Typeface.NORMAL);
        junit.framework.TestCase.assertTrue("style", mFaces[3].getStyle() == android.graphics.Typeface.BOLD);
        junit.framework.TestCase.assertTrue("style", mFaces[4].getStyle() == android.graphics.Typeface.ITALIC);
        junit.framework.TestCase.assertTrue("style", mFaces[5].getStyle() == android.graphics.Typeface.BOLD_ITALIC);
        junit.framework.TestCase.assertTrue("style", mFaces[6].getStyle() == android.graphics.Typeface.NORMAL);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testUniformY() throws java.lang.Exception {
        android.graphics.Paint p = new android.graphics.Paint();
        final int n = mFaces.length;
        for (int i = 1; i <= 36; i++) {
            p.setTextSize(i);
            float ascent = 0;
            float descent = 0;
            for (int j = 0; j < n; j++) {
                p.setTypeface(mFaces[j]);
                android.graphics.Paint.FontMetrics fm = p.getFontMetrics();
                if (j == 0) {
                    ascent = fm.ascent;
                    descent = fm.descent;
                } else {
                    junit.framework.TestCase.assertTrue("fontMetrics", fm.ascent == ascent);
                    junit.framework.TestCase.assertTrue("fontMetrics", fm.descent == descent);
                }
            }
        }
    }
}

