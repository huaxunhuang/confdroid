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
package android.content;


/**
 * Content capture options for a given package.
 *
 * <p>This object is created by the Content Capture System Service and passed back to the app when
 * the application is created.
 *
 * @unknown 
 */
@android.annotation.TestApi
public final class ContentCaptureOptions implements android.os.Parcelable {
    private static final java.lang.String TAG = android.content.ContentCaptureOptions.class.getSimpleName();

    /**
     * Logging level for {@code logcat} statements.
     */
    public final int loggingLevel;

    /**
     * Maximum number of events that are buffered before sent to the app.
     */
    public final int maxBufferSize;

    /**
     * Frequency the buffer is flushed if idle.
     */
    public final int idleFlushingFrequencyMs;

    /**
     * Frequency the buffer is flushed if last event is a text change.
     */
    public final int textChangeFlushingFrequencyMs;

    /**
     * Size of events that are logging on {@code dump}.
     */
    public final int logHistorySize;

    /**
     * List of activities explicitly whitelisted for content capture (or {@code null} if whitelisted
     * for all acitivites in the package).
     */
    @android.annotation.Nullable
    public final android.util.ArraySet<android.content.ComponentName> whitelistedComponents;

    /**
     * Used to enable just a small set of APIs so it can used by activities belonging to the
     * content capture service APK.
     */
    public final boolean lite;

    /**
     * Constructor for "lite" objects that are just used to enable a {@link ContentCaptureManager}
     * for contexts belonging to the content capture service app.
     */
    public ContentCaptureOptions(int loggingLevel) {
        /* lite= */
        /* maxBufferSize= */
        /* idleFlushingFrequencyMs= */
        /* textChangeFlushingFrequencyMs= */
        /* logHistorySize= */
        /* whitelistedComponents= */
        this(true, loggingLevel, 0, 0, 0, 0, null);
    }

    /**
     * Default constructor.
     */
    public ContentCaptureOptions(int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, @android.annotation.Nullable
    android.util.ArraySet<android.content.ComponentName> whitelistedComponents) {
        /* lite= */
        this(false, loggingLevel, maxBufferSize, idleFlushingFrequencyMs, textChangeFlushingFrequencyMs, logHistorySize, whitelistedComponents);
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public ContentCaptureOptions(@android.annotation.Nullable
    android.util.ArraySet<android.content.ComponentName> whitelistedComponents) {
        this(android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_VERBOSE, android.view.contentcapture.ContentCaptureManager.DEFAULT_MAX_BUFFER_SIZE, android.view.contentcapture.ContentCaptureManager.DEFAULT_IDLE_FLUSHING_FREQUENCY_MS, android.view.contentcapture.ContentCaptureManager.DEFAULT_TEXT_CHANGE_FLUSHING_FREQUENCY_MS, android.view.contentcapture.ContentCaptureManager.DEFAULT_LOG_HISTORY_SIZE, whitelistedComponents);
    }

    private ContentCaptureOptions(boolean lite, int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, @android.annotation.Nullable
    android.util.ArraySet<android.content.ComponentName> whitelistedComponents) {
        this.lite = lite;
        this.loggingLevel = loggingLevel;
        this.maxBufferSize = maxBufferSize;
        this.idleFlushingFrequencyMs = idleFlushingFrequencyMs;
        this.textChangeFlushingFrequencyMs = textChangeFlushingFrequencyMs;
        this.logHistorySize = logHistorySize;
        this.whitelistedComponents = whitelistedComponents;
    }

    public static android.content.ContentCaptureOptions forWhitelistingItself() {
        final android.app.ActivityThread at = android.app.ActivityThread.currentActivityThread();
        if (at == null) {
            throw new java.lang.IllegalStateException("No ActivityThread");
        }
        final java.lang.String packageName = at.getApplication().getPackageName();
        if (!"android.contentcaptureservice.cts".equals(packageName)) {
            android.util.Log.e(android.content.ContentCaptureOptions.TAG, "forWhitelistingItself(): called by " + packageName);
            throw new java.lang.SecurityException("Thou shall not pass!");
        }
        final android.content.ContentCaptureOptions options = /* whitelistedComponents= */
        new android.content.ContentCaptureOptions(null);
        // Always log, as it's used by test only
        android.util.Log.i(android.content.ContentCaptureOptions.TAG, (("forWhitelistingItself(" + packageName) + "): ") + options);
        return options;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public boolean isWhitelisted(@android.annotation.NonNull
    android.content.Context context) {
        if (whitelistedComponents == null)
            return true;
        // whole package is whitelisted

        final android.view.contentcapture.ContentCaptureManager.ContentCaptureClient client = context.getContentCaptureClient();
        if (client == null) {
            // Shouldn't happen, but it doesn't hurt to check...
            android.util.Log.w(android.content.ContentCaptureOptions.TAG, "isWhitelisted(): no ContentCaptureClient on " + context);
            return false;
        }
        return whitelistedComponents.contains(client.contentCaptureClientGetComponentName());
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (lite) {
            return ("ContentCaptureOptions [loggingLevel=" + loggingLevel) + " (lite)]";
        }
        final java.lang.StringBuilder string = new java.lang.StringBuilder("ContentCaptureOptions [");
        string.append("loggingLevel=").append(loggingLevel).append(", maxBufferSize=").append(maxBufferSize).append(", idleFlushingFrequencyMs=").append(idleFlushingFrequencyMs).append(", textChangeFlushingFrequencyMs=").append(textChangeFlushingFrequencyMs).append(", logHistorySize=").append(logHistorySize);
        if (whitelistedComponents != null) {
            string.append(", whitelisted=").append(whitelistedComponents);
        }
        return string.append(']').toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public void dumpShort(@android.annotation.NonNull
    java.io.PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(loggingLevel);
        if (lite) {
            pw.print(", lite");
            return;
        }
        pw.print(", bufferSize=");
        pw.print(maxBufferSize);
        pw.print(", idle=");
        pw.print(idleFlushingFrequencyMs);
        pw.print(", textIdle=");
        pw.print(textChangeFlushingFrequencyMs);
        pw.print(", logSize=");
        pw.print(logHistorySize);
        if (whitelistedComponents != null) {
            pw.print(", whitelisted=");
            pw.print(whitelistedComponents);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeBoolean(lite);
        parcel.writeInt(loggingLevel);
        if (lite)
            return;

        parcel.writeInt(maxBufferSize);
        parcel.writeInt(idleFlushingFrequencyMs);
        parcel.writeInt(textChangeFlushingFrequencyMs);
        parcel.writeInt(logHistorySize);
        parcel.writeArraySet(whitelistedComponents);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.ContentCaptureOptions> CREATOR = new android.os.Parcelable.Creator<android.content.ContentCaptureOptions>() {
        @java.lang.Override
        public android.content.ContentCaptureOptions createFromParcel(android.os.Parcel parcel) {
            final boolean lite = parcel.readBoolean();
            final int loggingLevel = parcel.readInt();
            if (lite) {
                return new android.content.ContentCaptureOptions(loggingLevel);
            }
            final int maxBufferSize = parcel.readInt();
            final int idleFlushingFrequencyMs = parcel.readInt();
            final int textChangeFlushingFrequencyMs = parcel.readInt();
            final int logHistorySize = parcel.readInt();
            @java.lang.SuppressWarnings("unchecked")
            final ArraySet<android.content.ComponentName> whitelistedComponents = ((ArraySet<android.content.ComponentName>) (parcel.readArraySet(null)));
            return new android.content.ContentCaptureOptions(loggingLevel, maxBufferSize, idleFlushingFrequencyMs, textChangeFlushingFrequencyMs, logHistorySize, whitelistedComponents);
        }

        @java.lang.Override
        public android.content.ContentCaptureOptions[] newArray(int size) {
            return new android.content.ContentCaptureOptions[size];
        }
    };
}

