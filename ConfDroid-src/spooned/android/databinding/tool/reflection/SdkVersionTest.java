/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.reflection;


public class SdkVersionTest {
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
    }

    @org.junit.Test
    public void testApiVersionsFromResources() {
        android.databinding.tool.reflection.SdkUtil.ApiChecker apiChecker = android.databinding.tool.reflection.SdkUtil.sApiChecker;
        int minSdk = android.databinding.tool.reflection.SdkUtil.sMinSdk;
        try {
            android.databinding.tool.reflection.SdkUtil.sApiChecker = new android.databinding.tool.reflection.SdkUtil.ApiChecker(null);
            android.databinding.tool.reflection.ModelClass view = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass("android.widget.TextView", null);
            android.databinding.tool.reflection.ModelMethod isSuggestionsEnabled = view.getMethods("isSuggestionsEnabled", 0)[0];
            org.junit.Assert.assertEquals(14, android.databinding.tool.reflection.SdkUtil.getMinApi(isSuggestionsEnabled));
        } finally {
            android.databinding.tool.reflection.SdkUtil.sMinSdk = minSdk;
            android.databinding.tool.reflection.SdkUtil.sApiChecker = apiChecker;
        }
    }

    @org.junit.Test
    public void testNewApiMethod() {
        android.databinding.tool.reflection.ModelClass view = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass("android.view.View", null);
        android.databinding.tool.reflection.ModelMethod setElevation = view.getMethods("setElevation", 1)[0];
        org.junit.Assert.assertEquals(21, android.databinding.tool.reflection.SdkUtil.getMinApi(setElevation));
    }

    @org.junit.Test
    public void testCustomCode() {
        android.databinding.tool.reflection.ModelClass view = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass("android.databinding.tool.reflection.SdkVersionTest", null);
        android.databinding.tool.reflection.ModelMethod setElevation = view.getMethods("testCustomCode", 0)[0];
        org.junit.Assert.assertEquals(1, android.databinding.tool.reflection.SdkUtil.getMinApi(setElevation));
    }

    @org.junit.Test
    public void testSetForeground() {
        android.databinding.tool.reflection.ModelClass view = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass("android.widget.FrameLayout", null);
        android.databinding.tool.reflection.ModelMethod setForeground = view.getMethods("setForeground", 1)[0];
        org.junit.Assert.assertEquals(1, android.databinding.tool.reflection.SdkUtil.getMinApi(setForeground));
    }
}

