/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.util;


/**
 *
 *
 * @unknown 
 */
public final class LocalLog {
    private java.util.LinkedList<java.lang.String> mLog;

    private int mMaxLines;

    private long mNow;

    public LocalLog(int maxLines) {
        mLog = new java.util.LinkedList<java.lang.String>();
        mMaxLines = maxLines;
    }

    public synchronized void log(java.lang.String msg) {
        if (mMaxLines > 0) {
            mNow = java.lang.System.currentTimeMillis();
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTimeInMillis(mNow);
            sb.append(java.lang.String.format("%tm-%td %tH:%tM:%tS.%tL", c, c, c, c, c, c));
            mLog.add((sb.toString() + " - ") + msg);
            while (mLog.size() > mMaxLines)
                mLog.remove();

        }
    }

    public synchronized void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.Iterator<java.lang.String> itr = mLog.listIterator(0);
        while (itr.hasNext()) {
            pw.println(itr.next());
        } 
    }

    public synchronized void reverseDump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        for (int i = mLog.size() - 1; i >= 0; i--) {
            pw.println(mLog.get(i));
        }
    }

    public static class ReadOnlyLocalLog {
        private final android.util.LocalLog mLog;

        ReadOnlyLocalLog(android.util.LocalLog log) {
            mLog = log;
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            mLog.dump(fd, pw, args);
        }
    }

    public android.util.LocalLog.ReadOnlyLocalLog readOnlyLocalLog() {
        return new android.util.LocalLog.ReadOnlyLocalLog(this);
    }
}

