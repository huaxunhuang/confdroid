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
package android.os;


/**
 * Helper for implementing {@link Binder#onShellCommand Binder.onShellCommand}.
 *
 * @unknown 
 */
public abstract class ShellCommand {
    static final java.lang.String TAG = "ShellCommand";

    static final boolean DEBUG = false;

    private android.os.Binder mTarget;

    private java.io.FileDescriptor mIn;

    private java.io.FileDescriptor mOut;

    private java.io.FileDescriptor mErr;

    private java.lang.String[] mArgs;

    private android.os.ResultReceiver mResultReceiver;

    private java.lang.String mCmd;

    private int mArgPos;

    private java.lang.String mCurArgData;

    private java.io.FileInputStream mFileIn;

    private java.io.FileOutputStream mFileOut;

    private java.io.FileOutputStream mFileErr;

    private com.android.internal.util.FastPrintWriter mOutPrintWriter;

    private com.android.internal.util.FastPrintWriter mErrPrintWriter;

    private java.io.InputStream mInputStream;

    public void init(android.os.Binder target, java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, int firstArgPos) {
        mTarget = target;
        mIn = in;
        mOut = out;
        mErr = err;
        mArgs = args;
        mResultReceiver = null;
        mCmd = null;
        mArgPos = firstArgPos;
        mCurArgData = null;
        mFileIn = null;
        mFileOut = null;
        mFileErr = null;
        mOutPrintWriter = null;
        mErrPrintWriter = null;
        mInputStream = null;
    }

    public int exec(android.os.Binder target, java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ResultReceiver resultReceiver) {
        java.lang.String cmd;
        int start;
        if ((args != null) && (args.length > 0)) {
            cmd = args[0];
            start = 1;
        } else {
            cmd = null;
            start = 0;
        }
        init(target, in, out, err, args, start);
        mCmd = cmd;
        mResultReceiver = resultReceiver;
        if (android.os.ShellCommand.DEBUG)
            android.util.Slog.d(android.os.ShellCommand.TAG, (("Starting command " + mCmd) + " on ") + mTarget);

        int res = -1;
        try {
            res = onCommand(mCmd);
            if (android.os.ShellCommand.DEBUG)
                android.util.Slog.d(android.os.ShellCommand.TAG, (("Executed command " + mCmd) + " on ") + mTarget);

        } catch (java.lang.SecurityException e) {
            java.io.PrintWriter eout = getErrPrintWriter();
            eout.println("Security exception: " + e.getMessage());
            eout.println();
            e.printStackTrace(eout);
        } catch (java.lang.Throwable e) {
            // Unlike usual calls, in this case if an exception gets thrown
            // back to us we want to print it back in to the dump data, since
            // that is where the caller expects all interesting information to
            // go.
            java.io.PrintWriter eout = getErrPrintWriter();
            eout.println();
            eout.println("Exception occurred while dumping:");
            e.printStackTrace(eout);
        } finally {
            if (android.os.ShellCommand.DEBUG)
                android.util.Slog.d(android.os.ShellCommand.TAG, "Flushing output streams on " + mTarget);

            if (mOutPrintWriter != null) {
                mOutPrintWriter.flush();
            }
            if (mErrPrintWriter != null) {
                mErrPrintWriter.flush();
            }
            if (android.os.ShellCommand.DEBUG)
                android.util.Slog.d(android.os.ShellCommand.TAG, "Sending command result on " + mTarget);

            mResultReceiver.send(res, null);
        }
        if (android.os.ShellCommand.DEBUG)
            android.util.Slog.d(android.os.ShellCommand.TAG, (("Finished command " + mCmd) + " on ") + mTarget);

        return res;
    }

    /**
     * Return direct raw access (not buffered) to the command's output data stream.
     */
    public java.io.OutputStream getRawOutputStream() {
        if (mFileOut == null) {
            mFileOut = new java.io.FileOutputStream(mOut);
        }
        return mFileOut;
    }

    /**
     * Return a PrintWriter for formatting output to {@link #getRawOutputStream()}.
     */
    public java.io.PrintWriter getOutPrintWriter() {
        if (mOutPrintWriter == null) {
            mOutPrintWriter = new com.android.internal.util.FastPrintWriter(getRawOutputStream());
        }
        return mOutPrintWriter;
    }

    /**
     * Return direct raw access (not buffered) to the command's error output data stream.
     */
    public java.io.OutputStream getRawErrorStream() {
        if (mFileErr == null) {
            mFileErr = new java.io.FileOutputStream(mErr);
        }
        return mFileErr;
    }

    /**
     * Return a PrintWriter for formatting output to {@link #getRawErrorStream()}.
     */
    public java.io.PrintWriter getErrPrintWriter() {
        if (mErr == null) {
            return getOutPrintWriter();
        }
        if (mErrPrintWriter == null) {
            mErrPrintWriter = new com.android.internal.util.FastPrintWriter(getRawErrorStream());
        }
        return mErrPrintWriter;
    }

    /**
     * Return direct raw access (not buffered) to the command's input data stream.
     */
    public java.io.InputStream getRawInputStream() {
        if (mFileIn == null) {
            mFileIn = new java.io.FileInputStream(mIn);
        }
        return mFileIn;
    }

    /**
     * Return buffered access to the command's {@link #getRawInputStream()}.
     */
    public java.io.InputStream getBufferedInputStream() {
        if (mInputStream == null) {
            mInputStream = new java.io.BufferedInputStream(getRawInputStream());
        }
        return mInputStream;
    }

    /**
     * Return the next option on the command line -- that is an argument that
     * starts with '-'.  If the next argument is not an option, null is returned.
     */
    public java.lang.String getNextOption() {
        if (mCurArgData != null) {
            java.lang.String prev = mArgs[mArgPos - 1];
            throw new java.lang.IllegalArgumentException(("No argument expected after \"" + prev) + "\"");
        }
        if (mArgPos >= mArgs.length) {
            return null;
        }
        java.lang.String arg = mArgs[mArgPos];
        if (!arg.startsWith("-")) {
            return null;
        }
        mArgPos++;
        if (arg.equals("--")) {
            return null;
        }
        if ((arg.length() > 1) && (arg.charAt(1) != '-')) {
            if (arg.length() > 2) {
                mCurArgData = arg.substring(2);
                return arg.substring(0, 2);
            } else {
                mCurArgData = null;
                return arg;
            }
        }
        mCurArgData = null;
        return arg;
    }

    /**
     * Return the next argument on the command line, whatever it is; if there are
     * no arguments left, return null.
     */
    public java.lang.String getNextArg() {
        if (mCurArgData != null) {
            java.lang.String arg = mCurArgData;
            mCurArgData = null;
            return arg;
        } else
            if (mArgPos < mArgs.length) {
                return mArgs[mArgPos++];
            } else {
                return null;
            }

    }

    public java.lang.String peekNextArg() {
        if (mCurArgData != null) {
            return mCurArgData;
        } else
            if (mArgPos < mArgs.length) {
                return mArgs[mArgPos];
            } else {
                return null;
            }

    }

    /**
     * Return the next argument on the command line, whatever it is; if there are
     * no arguments left, throws an IllegalArgumentException to report this to the user.
     */
    public java.lang.String getNextArgRequired() {
        java.lang.String arg = getNextArg();
        if (arg == null) {
            java.lang.String prev = mArgs[mArgPos - 1];
            throw new java.lang.IllegalArgumentException(("Argument expected after \"" + prev) + "\"");
        }
        return arg;
    }

    public int handleDefaultCommands(java.lang.String cmd) {
        if ("dump".equals(cmd)) {
            java.lang.String[] newArgs = new java.lang.String[mArgs.length - 1];
            java.lang.System.arraycopy(mArgs, 1, newArgs, 0, mArgs.length - 1);
            mTarget.doDump(mOut, getOutPrintWriter(), newArgs);
            return 0;
        } else
            if (((cmd == null) || "help".equals(cmd)) || "-h".equals(cmd)) {
                onHelp();
            } else {
                getOutPrintWriter().println("Unknown command: " + cmd);
            }

        return -1;
    }

    /**
     * Implement parsing and execution of a command.  If it isn't a command you understand,
     * call {@link #handleDefaultCommands(String)} and return its result as a last resort.
     * User {@link #getNextOption()}, {@link #getNextArg()}, and {@link #getNextArgRequired()}
     * to process additional command line arguments.  Command output can be written to
     * {@link #getOutPrintWriter()} and errors to {@link #getErrPrintWriter()}.
     *
     * <p class="caution">Note that no permission checking has been done before entering this function,
     * so you need to be sure to do your own security verification for any commands you
     * are executing.  The easiest way to do this is to have the ShellCommand contain
     * only a reference to your service's aidl interface, and do all of your command
     * implementations on top of that -- that way you can rely entirely on your executing security
     * code behind that interface.</p>
     *
     * @param cmd
     * 		The first command line argument representing the name of the command to execute.
     * @return Return the command result; generally 0 or positive indicates success and
    negative values indicate error.
     */
    public abstract int onCommand(java.lang.String cmd);

    /**
     * Implement this to print help text about your command to {@link #getOutPrintWriter()}.
     */
    public abstract void onHelp();
}

