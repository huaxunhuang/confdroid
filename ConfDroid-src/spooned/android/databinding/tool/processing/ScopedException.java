/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.processing;


/**
 * An exception that contains scope information.
 */
public class ScopedException extends java.lang.RuntimeException {
    public static final java.lang.String ERROR_LOG_PREFIX = "****/ data binding error ****";

    public static final java.lang.String ERROR_LOG_SUFFIX = "****\\ data binding error ****";

    public static final java.lang.String MSG_KEY = "msg:";

    public static final java.lang.String LOCATION_KEY = "loc:";

    public static final java.lang.String FILE_KEY = "file:";

    private static boolean sEncodeOutput = false;

    private android.databinding.tool.processing.ScopedErrorReport mScopedErrorReport;

    private java.lang.String mScopeLog;

    public ScopedException(java.lang.String message, java.lang.Object... args) {
        super(message == null ? "unknown data binding exception" : args.length == 0 ? message : java.lang.String.format(message, args));
        mScopedErrorReport = android.databinding.tool.processing.Scope.createReport();
        mScopeLog = (android.databinding.tool.util.L.isDebugEnabled()) ? android.databinding.tool.processing.Scope.produceScopeLog() : null;
    }

    ScopedException(java.lang.String message, android.databinding.tool.processing.ScopedErrorReport scopedErrorReport) {
        super(message);
        mScopedErrorReport = scopedErrorReport;
    }

    public java.lang.String getBareMessage() {
        return super.getMessage();
    }

    @java.lang.Override
    public java.lang.String getMessage() {
        return android.databinding.tool.processing.ScopedException.sEncodeOutput ? createEncodedMessage() : createHumanReadableMessage();
    }

    private java.lang.String createHumanReadableMessage() {
        android.databinding.tool.processing.ScopedErrorReport scopedError = getScopedErrorReport();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(super.getMessage()).append("\n").append("file://").append(scopedError.getFilePath());
        if ((scopedError.getLocations() != null) && (scopedError.getLocations().size() > 0)) {
            sb.append(" Line:");
            sb.append(scopedError.getLocations().get(0).startLine);
        }
        sb.append("\n");
        return sb.toString();
    }

    private java.lang.String createEncodedMessage() {
        android.databinding.tool.processing.ScopedErrorReport scopedError = getScopedErrorReport();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX).append(android.databinding.tool.processing.ScopedException.MSG_KEY).append(super.getMessage()).append("\n").append(android.databinding.tool.processing.ScopedException.FILE_KEY).append(scopedError.getFilePath()).append("\n");
        if (scopedError.getLocations() != null) {
            for (android.databinding.tool.store.Location location : scopedError.getLocations()) {
                sb.append(android.databinding.tool.processing.ScopedException.LOCATION_KEY).append(location.toUserReadableString()).append("\n");
            }
        }
        sb.append(android.databinding.tool.processing.ScopedException.ERROR_LOG_SUFFIX);
        return com.google.common.base.Joiner.on(' ').join(com.google.common.base.Splitter.on(android.databinding.tool.util.StringUtils.LINE_SEPARATOR).split(sb));
    }

    public android.databinding.tool.processing.ScopedErrorReport getScopedErrorReport() {
        return mScopedErrorReport;
    }

    public boolean isValid() {
        return mScopedErrorReport.isValid();
    }

    public static android.databinding.tool.processing.ScopedException createFromOutput(java.lang.String output) {
        java.lang.String message = "";
        java.lang.String file = "";
        java.util.List<android.databinding.tool.store.Location> locations = new java.util.ArrayList<android.databinding.tool.store.Location>();
        int msgStart = output.indexOf(android.databinding.tool.processing.ScopedException.MSG_KEY);
        if (msgStart < 0) {
            message = output;
        } else {
            int fileStart = output.indexOf(android.databinding.tool.processing.ScopedException.FILE_KEY, msgStart + android.databinding.tool.processing.ScopedException.MSG_KEY.length());
            if (fileStart < 0) {
                message = output;
            } else {
                message = output.substring(msgStart + android.databinding.tool.processing.ScopedException.MSG_KEY.length(), fileStart);
                int locStart = output.indexOf(android.databinding.tool.processing.ScopedException.LOCATION_KEY, fileStart + android.databinding.tool.processing.ScopedException.FILE_KEY.length());
                if (locStart < 0) {
                    file = output.substring(fileStart + android.databinding.tool.processing.ScopedException.FILE_KEY.length());
                } else {
                    file = output.substring(fileStart + android.databinding.tool.processing.ScopedException.FILE_KEY.length(), locStart);
                    int nextLoc = 0;
                    while (nextLoc >= 0) {
                        nextLoc = output.indexOf(android.databinding.tool.processing.ScopedException.LOCATION_KEY, locStart + android.databinding.tool.processing.ScopedException.LOCATION_KEY.length());
                        android.databinding.tool.store.Location loc;
                        if (nextLoc < 0) {
                            loc = android.databinding.tool.store.Location.fromUserReadableString(output.substring(locStart + android.databinding.tool.processing.ScopedException.LOCATION_KEY.length()));
                        } else {
                            loc = android.databinding.tool.store.Location.fromUserReadableString(output.substring(locStart + android.databinding.tool.processing.ScopedException.LOCATION_KEY.length(), nextLoc));
                        }
                        if ((loc != null) && loc.isValid()) {
                            locations.add(loc);
                        }
                        locStart = nextLoc;
                    } 
                }
            }
        }
        return new android.databinding.tool.processing.ScopedException(message.trim(), new android.databinding.tool.processing.ScopedErrorReport(com.google.common.base.Strings.isNullOrEmpty(file) ? null : file.trim(), locations));
    }

    public static java.util.List<android.databinding.tool.processing.ScopedException> extractErrors(java.lang.String output) {
        java.util.List<android.databinding.tool.processing.ScopedException> errors = new java.util.ArrayList<android.databinding.tool.processing.ScopedException>();
        int index = output.indexOf(android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX);
        final int limit = output.length();
        while ((index >= 0) && (index < limit)) {
            int end = output.indexOf(android.databinding.tool.processing.ScopedException.ERROR_LOG_SUFFIX, index + android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX.length());
            if (end == (-1)) {
                errors.add(android.databinding.tool.processing.ScopedException.createFromOutput(output.substring(index + android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX.length())));
                break;
            } else {
                errors.add(android.databinding.tool.processing.ScopedException.createFromOutput(output.substring(index + android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX.length(), end)));
                index = output.indexOf(android.databinding.tool.processing.ScopedException.ERROR_LOG_PREFIX, end + android.databinding.tool.processing.ScopedException.ERROR_LOG_SUFFIX.length());
            }
        } 
        return errors;
    }

    public static void encodeOutput(boolean encodeOutput) {
        android.databinding.tool.processing.ScopedException.sEncodeOutput = encodeOutput;
    }

    public static boolean issEncodeOutput() {
        return android.databinding.tool.processing.ScopedException.sEncodeOutput;
    }
}

