/**
 * Copyright (C) 2017 The Android Open Source Project
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


/**
 * A helper for logging calls to generateLinks.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class GenerateLinksLogger {
    private static final java.lang.String LOG_TAG = "GenerateLinksLogger";

    private static final java.lang.String ZERO = "0";

    private final com.android.internal.logging.MetricsLogger mMetricsLogger;

    private final java.util.Random mRng;

    private final int mSampleRate;

    /**
     *
     *
     * @param sampleRate
     * 		the rate at which log events are written. (e.g. 100 means there is a 0.01
     * 		chance that a call to logGenerateLinks results in an event being written).
     * 		To write all events, pass 1.
     */
    public GenerateLinksLogger(int sampleRate) {
        mSampleRate = sampleRate;
        mRng = new java.util.Random(java.lang.System.nanoTime());
        mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    }

    @com.android.internal.annotations.VisibleForTesting
    public GenerateLinksLogger(int sampleRate, com.android.internal.logging.MetricsLogger metricsLogger) {
        mSampleRate = sampleRate;
        mRng = new java.util.Random(java.lang.System.nanoTime());
        mMetricsLogger = metricsLogger;
    }

    /**
     * Logs statistics about a call to generateLinks.
     */
    public void logGenerateLinks(java.lang.CharSequence text, android.view.textclassifier.TextLinks links, java.lang.String callingPackageName, long latencyMs) {
        com.android.internal.util.Preconditions.checkNotNull(text);
        com.android.internal.util.Preconditions.checkNotNull(links);
        com.android.internal.util.Preconditions.checkNotNull(callingPackageName);
        if (!shouldLog()) {
            return;
        }
        // Always populate the total stats, and per-entity stats for each entity type detected.
        final android.view.textclassifier.GenerateLinksLogger.LinkifyStats totalStats = new android.view.textclassifier.GenerateLinksLogger.LinkifyStats();
        final java.util.Map<java.lang.String, android.view.textclassifier.GenerateLinksLogger.LinkifyStats> perEntityTypeStats = new android.util.ArrayMap();
        for (android.view.textclassifier.TextLinks.TextLink link : links.getLinks()) {
            if (link.getEntityCount() == 0)
                continue;

            final java.lang.String entityType = link.getEntity(0);
            if (((entityType == null) || android.view.textclassifier.TextClassifier.TYPE_OTHER.equals(entityType)) || android.view.textclassifier.TextClassifier.TYPE_UNKNOWN.equals(entityType)) {
                continue;
            }
            totalStats.countLink(link);
            perEntityTypeStats.computeIfAbsent(entityType, ( k) -> new android.view.textclassifier.GenerateLinksLogger.LinkifyStats()).countLink(link);
        }
        final java.lang.String callId = java.util.UUID.randomUUID().toString();
        writeStats(callId, callingPackageName, null, totalStats, text, latencyMs);
        for (java.util.Map.Entry<java.lang.String, android.view.textclassifier.GenerateLinksLogger.LinkifyStats> entry : perEntityTypeStats.entrySet()) {
            writeStats(callId, callingPackageName, entry.getKey(), entry.getValue(), text, latencyMs);
        }
    }

    /**
     * Returns whether this particular event should be logged.
     *
     * Sampling is used to reduce the amount of logging data generated.
     */
    private boolean shouldLog() {
        if (mSampleRate <= 1) {
            return true;
        } else {
            return mRng.nextInt(mSampleRate) == 0;
        }
    }

    /**
     * Writes a log event for the given stats.
     */
    private void writeStats(java.lang.String callId, java.lang.String callingPackageName, @android.annotation.Nullable
    java.lang.String entityType, android.view.textclassifier.GenerateLinksLogger.LinkifyStats stats, java.lang.CharSequence text, long latencyMs) {
        final android.metrics.LogMaker log = new android.metrics.LogMaker(com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_CLASSIFIER_GENERATE_LINKS).setPackageName(callingPackageName).addTaggedData(MetricsEvent.FIELD_LINKIFY_CALL_ID, callId).addTaggedData(MetricsEvent.FIELD_LINKIFY_NUM_LINKS, stats.mNumLinks).addTaggedData(MetricsEvent.FIELD_LINKIFY_LINK_LENGTH, stats.mNumLinksTextLength).addTaggedData(MetricsEvent.FIELD_LINKIFY_TEXT_LENGTH, text.length()).addTaggedData(MetricsEvent.FIELD_LINKIFY_LATENCY, latencyMs);
        if (entityType != null) {
            log.addTaggedData(MetricsEvent.FIELD_LINKIFY_ENTITY_TYPE, entityType);
        }
        mMetricsLogger.write(log);
        android.view.textclassifier.GenerateLinksLogger.debugLog(log);
    }

    private static void debugLog(android.metrics.LogMaker log) {
        if (!android.view.textclassifier.Log.ENABLE_FULL_LOGGING) {
            return;
        }
        final java.lang.String callId = java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_CALL_ID), "");
        final java.lang.String entityType = java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_ENTITY_TYPE), "ANY_ENTITY");
        final int numLinks = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_NUM_LINKS), android.view.textclassifier.GenerateLinksLogger.ZERO));
        final int linkLength = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_LINK_LENGTH), android.view.textclassifier.GenerateLinksLogger.ZERO));
        final int textLength = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_TEXT_LENGTH), android.view.textclassifier.GenerateLinksLogger.ZERO));
        final int latencyMs = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(MetricsEvent.FIELD_LINKIFY_LATENCY), android.view.textclassifier.GenerateLinksLogger.ZERO));
        android.view.textclassifier.Log.v(android.view.textclassifier.GenerateLinksLogger.LOG_TAG, java.lang.String.format(java.util.Locale.US, "%s:%s %d links (%d/%d chars) %dms %s", callId, entityType, numLinks, linkLength, textLength, latencyMs, log.getPackageName()));
    }

    /**
     * Helper class for storing per-entity type statistics.
     */
    private static final class LinkifyStats {
        int mNumLinks;

        int mNumLinksTextLength;

        void countLink(android.view.textclassifier.TextLinks.TextLink link) {
            mNumLinks += 1;
            mNumLinksTextLength += link.getEnd() - link.getStart();
        }
    }
}

