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
package android.databinding.tool.util;


public class L {
    private static boolean sEnableDebug = false;

    private static final android.databinding.tool.util.L.Client sSystemClient = new android.databinding.tool.util.L.Client() {
        @java.lang.Override
        public void printMessage(javax.tools.Diagnostic.Kind kind, java.lang.String message, javax.lang.model.element.Element element) {
            if (kind == javax.tools.Diagnostic.Kind.ERROR) {
                java.lang.System.err.println(message);
            } else {
                java.lang.System.out.println(message);
            }
        }
    };

    private static android.databinding.tool.util.L.Client sClient = android.databinding.tool.util.L.sSystemClient;

    public static void setClient(android.databinding.tool.util.L.Client systemClient) {
        android.databinding.tool.util.L.sClient = systemClient;
    }

    public static void setDebugLog(boolean enabled) {
        android.databinding.tool.util.L.sEnableDebug = enabled;
    }

    public static void d(java.lang.String msg, java.lang.Object... args) {
        if (android.databinding.tool.util.L.sEnableDebug) {
            android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.NOTE, java.lang.String.format(msg, args));
        }
    }

    public static void d(javax.lang.model.element.Element element, java.lang.String msg, java.lang.Object... args) {
        if (android.databinding.tool.util.L.sEnableDebug) {
            android.databinding.tool.util.L.printMessage(element, javax.tools.Diagnostic.Kind.NOTE, java.lang.String.format(msg, args));
        }
    }

    public static void d(java.lang.Throwable t, java.lang.String msg, java.lang.Object... args) {
        if (android.databinding.tool.util.L.sEnableDebug) {
            android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.NOTE, (java.lang.String.format(msg, args) + " ") + android.databinding.tool.util.L.getStackTrace(t));
        }
    }

    public static void w(java.lang.String msg, java.lang.Object... args) {
        android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.WARNING, java.lang.String.format(msg, args));
    }

    public static void w(javax.lang.model.element.Element element, java.lang.String msg, java.lang.Object... args) {
        android.databinding.tool.util.L.printMessage(element, javax.tools.Diagnostic.Kind.WARNING, java.lang.String.format(msg, args));
    }

    public static void w(java.lang.Throwable t, java.lang.String msg, java.lang.Object... args) {
        android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.WARNING, (java.lang.String.format(msg, args) + " ") + android.databinding.tool.util.L.getStackTrace(t));
    }

    private static void tryToThrowScoped(java.lang.Throwable t, java.lang.String fullMessage) {
        if (t instanceof android.databinding.tool.processing.ScopedException) {
            android.databinding.tool.processing.ScopedException ex = ((android.databinding.tool.processing.ScopedException) (t));
            if (ex.isValid()) {
                throw ex;
            }
        }
        android.databinding.tool.processing.ScopedException ex = new android.databinding.tool.processing.ScopedException(fullMessage);
        if (ex.isValid()) {
            throw ex;
        }
    }

    public static void e(java.lang.String msg, java.lang.Object... args) {
        java.lang.String fullMsg = java.lang.String.format(msg, args);
        android.databinding.tool.util.L.tryToThrowScoped(null, fullMsg);
        android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.ERROR, fullMsg);
    }

    public static void e(javax.lang.model.element.Element element, java.lang.String msg, java.lang.Object... args) {
        java.lang.String fullMsg = java.lang.String.format(msg, args);
        android.databinding.tool.util.L.tryToThrowScoped(null, fullMsg);
        android.databinding.tool.util.L.printMessage(element, javax.tools.Diagnostic.Kind.ERROR, fullMsg);
    }

    public static void e(java.lang.Throwable t, java.lang.String msg, java.lang.Object... args) {
        java.lang.String fullMsg = java.lang.String.format(msg, args);
        android.databinding.tool.util.L.tryToThrowScoped(t, fullMsg);
        android.databinding.tool.util.L.printMessage(null, javax.tools.Diagnostic.Kind.ERROR, (fullMsg + " ") + android.databinding.tool.util.L.getStackTrace(t));
    }

    private static void printMessage(javax.lang.model.element.Element element, javax.tools.Diagnostic.Kind kind, java.lang.String message) {
        android.databinding.tool.util.L.sClient.printMessage(kind, message, element);
        if (kind == javax.tools.Diagnostic.Kind.ERROR) {
            throw new java.lang.RuntimeException("failure, see logs for details.\n" + message);
        }
    }

    public static boolean isDebugEnabled() {
        return android.databinding.tool.util.L.sEnableDebug;
    }

    public interface Client {
        void printMessage(javax.tools.Diagnostic.Kind kind, java.lang.String message, javax.lang.model.element.Element element);
    }

    private static java.lang.String getStackTrace(java.lang.Throwable t) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        try {
            t.printStackTrace(pw);
        } finally {
            pw.close();
        }
        return sw.toString();
    }
}

