/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view.textclassifier;


@androidx.test.filters.LargeTest
public class TextClassificationManagerPerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.After
    public void tearDown() {
        android.perftests.utils.SettingsHelper.delete(SettingsHelper.NAMESPACE_GLOBAL, Settings.Global.TEXT_CLASSIFIER_CONSTANTS);
    }

    @org.junit.Test
    public void testGetTextClassifier_systemTextClassifierDisabled() {
        android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        android.perftests.utils.SettingsHelper.set(SettingsHelper.NAMESPACE_GLOBAL, Settings.Global.TEXT_CLASSIFIER_CONSTANTS, "system_textclassifier_enabled=false");
        android.view.textclassifier.TextClassificationManager textClassificationManager = context.getSystemService(android.view.textclassifier.TextClassificationManager.class);
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            textClassificationManager.getTextClassifier();
            textClassificationManager.invalidateForTesting();
        } 
    }

    @org.junit.Test
    public void testGetTextClassifier_systemTextClassifierEnabled() {
        android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        android.perftests.utils.SettingsHelper.set(SettingsHelper.NAMESPACE_GLOBAL, Settings.Global.TEXT_CLASSIFIER_CONSTANTS, "system_textclassifier_enabled=true");
        android.view.textclassifier.TextClassificationManager textClassificationManager = context.getSystemService(android.view.textclassifier.TextClassificationManager.class);
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            textClassificationManager.getTextClassifier();
            textClassificationManager.invalidateForTesting();
        } 
    }
}

