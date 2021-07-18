/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.webkit;


/**
 * Public class representing a JavaScript console message from WebCore. This could be a issued
 * by a call to one of the <code>console</code> logging functions (e.g.
 * <code>console.log('...')</code>) or a JavaScript error on the  page. To receive notifications
 * of these messages, override the
 * {@link WebChromeClient#onConsoleMessage(ConsoleMessage)} function.
 */
public class ConsoleMessage {
    // This must be kept in sync with the WebCore enum in WebCore/page/Console.h
    public enum MessageLevel {

        TIP,
        LOG,
        WARNING,
        ERROR,
        DEBUG;}

    private android.webkit.ConsoleMessage.MessageLevel mLevel;

    private java.lang.String mMessage;

    private java.lang.String mSourceId;

    private int mLineNumber;

    public ConsoleMessage(java.lang.String message, java.lang.String sourceId, int lineNumber, android.webkit.ConsoleMessage.MessageLevel msgLevel) {
        mMessage = message;
        mSourceId = sourceId;
        mLineNumber = lineNumber;
        mLevel = msgLevel;
    }

    public android.webkit.ConsoleMessage.MessageLevel messageLevel() {
        return mLevel;
    }

    public java.lang.String message() {
        return mMessage;
    }

    public java.lang.String sourceId() {
        return mSourceId;
    }

    public int lineNumber() {
        return mLineNumber;
    }
}

