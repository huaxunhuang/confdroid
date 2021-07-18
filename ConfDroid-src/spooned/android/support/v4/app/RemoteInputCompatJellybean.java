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
package android.support.v4.app;


class RemoteInputCompatJellybean {
    /**
     * Label used to denote the clip data type used for remote input transport
     */
    public static final java.lang.String RESULTS_CLIP_LABEL = "android.remoteinput.results";

    /**
     * Extra added to a clip data intent object to hold the results bundle.
     */
    public static final java.lang.String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";

    private static final java.lang.String KEY_RESULT_KEY = "resultKey";

    private static final java.lang.String KEY_LABEL = "label";

    private static final java.lang.String KEY_CHOICES = "choices";

    private static final java.lang.String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";

    private static final java.lang.String KEY_EXTRAS = "extras";

    static android.support.v4.app.RemoteInputCompatBase.RemoteInput fromBundle(android.os.Bundle data, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory factory) {
        return factory.build(data.getString(android.support.v4.app.RemoteInputCompatJellybean.KEY_RESULT_KEY), data.getCharSequence(android.support.v4.app.RemoteInputCompatJellybean.KEY_LABEL), data.getCharSequenceArray(android.support.v4.app.RemoteInputCompatJellybean.KEY_CHOICES), data.getBoolean(android.support.v4.app.RemoteInputCompatJellybean.KEY_ALLOW_FREE_FORM_INPUT), data.getBundle(android.support.v4.app.RemoteInputCompatJellybean.KEY_EXTRAS));
    }

    static android.os.Bundle toBundle(android.support.v4.app.RemoteInputCompatBase.RemoteInput remoteInput) {
        android.os.Bundle data = new android.os.Bundle();
        data.putString(android.support.v4.app.RemoteInputCompatJellybean.KEY_RESULT_KEY, remoteInput.getResultKey());
        data.putCharSequence(android.support.v4.app.RemoteInputCompatJellybean.KEY_LABEL, remoteInput.getLabel());
        data.putCharSequenceArray(android.support.v4.app.RemoteInputCompatJellybean.KEY_CHOICES, remoteInput.getChoices());
        data.putBoolean(android.support.v4.app.RemoteInputCompatJellybean.KEY_ALLOW_FREE_FORM_INPUT, remoteInput.getAllowFreeFormInput());
        data.putBundle(android.support.v4.app.RemoteInputCompatJellybean.KEY_EXTRAS, remoteInput.getExtras());
        return data;
    }

    static android.support.v4.app.RemoteInputCompatBase.RemoteInput[] fromBundleArray(android.os.Bundle[] bundles, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory factory) {
        if (bundles == null) {
            return null;
        }
        android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs = factory.newArray(bundles.length);
        for (int i = 0; i < bundles.length; i++) {
            remoteInputs[i] = android.support.v4.app.RemoteInputCompatJellybean.fromBundle(bundles[i], factory);
        }
        return remoteInputs;
    }

    static android.os.Bundle[] toBundleArray(android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs) {
        if (remoteInputs == null) {
            return null;
        }
        android.os.Bundle[] bundles = new android.os.Bundle[remoteInputs.length];
        for (int i = 0; i < remoteInputs.length; i++) {
            bundles[i] = android.support.v4.app.RemoteInputCompatJellybean.toBundle(remoteInputs[i]);
        }
        return bundles;
    }

    static android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
        android.content.ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        android.content.ClipDescription clipDescription = clipData.getDescription();
        if (!clipDescription.hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_INTENT)) {
            return null;
        }
        if (clipDescription.getLabel().equals(android.support.v4.app.RemoteInputCompatJellybean.RESULTS_CLIP_LABEL)) {
            return clipData.getItemAt(0).getIntent().getExtras().getParcelable(android.support.v4.app.RemoteInputCompatJellybean.EXTRA_RESULTS_DATA);
        }
        return null;
    }

    static void addResultsToIntent(android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
        android.os.Bundle resultsBundle = new android.os.Bundle();
        for (android.support.v4.app.RemoteInputCompatBase.RemoteInput remoteInput : remoteInputs) {
            java.lang.Object result = results.get(remoteInput.getResultKey());
            if (result instanceof java.lang.CharSequence) {
                resultsBundle.putCharSequence(remoteInput.getResultKey(), ((java.lang.CharSequence) (result)));
            }
        }
        android.content.Intent clipIntent = new android.content.Intent();
        clipIntent.putExtra(android.support.v4.app.RemoteInputCompatJellybean.EXTRA_RESULTS_DATA, resultsBundle);
        intent.setClipData(android.content.ClipData.newIntent(android.support.v4.app.RemoteInputCompatJellybean.RESULTS_CLIP_LABEL, clipIntent));
    }
}

