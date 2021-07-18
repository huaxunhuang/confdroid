/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print;


/**
 * Adapter for printing PDF files. This class could be useful if you
 * want to print a file and intercept when the system is ready
 * spooling the data, so you can delete the file if it is a
 * temporary one. To achieve this one must override {@link #onFinish()}
 * and delete the file yourself.
 *
 * @unknown 
 */
public class PrintFileDocumentAdapter extends android.print.PrintDocumentAdapter {
    private static final java.lang.String LOG_TAG = "PrintedFileDocAdapter";

    private final android.content.Context mContext;

    private final java.io.File mFile;

    private final android.print.PrintDocumentInfo mDocumentInfo;

    private android.print.PrintFileDocumentAdapter.WriteFileAsyncTask mWriteFileAsyncTask;

    /**
     * Constructor.
     *
     * @param context
     * 		Context for accessing resources.
     * @param file
     * 		The PDF file to print.
     * @param documentInfo
     * 		The information about the printed file.
     */
    public PrintFileDocumentAdapter(android.content.Context context, java.io.File file, android.print.PrintDocumentInfo documentInfo) {
        if (file == null) {
            throw new java.lang.IllegalArgumentException("File cannot be null!");
        }
        if (documentInfo == null) {
            throw new java.lang.IllegalArgumentException("documentInfo cannot be null!");
        }
        mContext = context;
        mFile = file;
        mDocumentInfo = documentInfo;
    }

    @java.lang.Override
    public void onLayout(android.print.PrintAttributes oldAttributes, android.print.PrintAttributes newAttributes, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback callback, android.os.Bundle metadata) {
        callback.onLayoutFinished(mDocumentInfo, false);
    }

    @java.lang.Override
    public void onWrite(android.print.PageRange[] pages, android.os.ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.WriteResultCallback callback) {
        mWriteFileAsyncTask = new android.print.PrintFileDocumentAdapter.WriteFileAsyncTask(destination, cancellationSignal, callback);
        mWriteFileAsyncTask.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR, ((java.lang.Void[]) (null)));
    }

    private final class WriteFileAsyncTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> {
        private final android.os.ParcelFileDescriptor mDestination;

        private final android.print.PrintDocumentAdapter.WriteResultCallback mResultCallback;

        private final android.os.CancellationSignal mCancellationSignal;

        public WriteFileAsyncTask(android.os.ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.WriteResultCallback callback) {
            mDestination = destination;
            mResultCallback = callback;
            mCancellationSignal = cancellationSignal;
            mCancellationSignal.setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() {
                @java.lang.Override
                public void onCancel() {
                    cancel(true);
                }
            });
        }

        @java.lang.Override
        protected java.lang.Void doInBackground(java.lang.Void... params) {
            java.io.InputStream in = null;
            java.io.OutputStream out = new java.io.FileOutputStream(mDestination.getFileDescriptor());
            final byte[] buffer = new byte[8192];
            try {
                in = new java.io.FileInputStream(mFile);
                while (true) {
                    if (isCancelled()) {
                        break;
                    }
                    final int readByteCount = in.read(buffer);
                    if (readByteCount < 0) {
                        break;
                    }
                    out.write(buffer, 0, readByteCount);
                } 
            } catch (java.io.IOException ioe) {
                android.util.Log.e(android.print.PrintFileDocumentAdapter.LOG_TAG, "Error writing data!", ioe);
                mResultCallback.onWriteFailed(mContext.getString(R.string.write_fail_reason_cannot_write));
            } finally {
                libcore.io.IoUtils.closeQuietly(in);
                libcore.io.IoUtils.closeQuietly(out);
            }
            return null;
        }

        @java.lang.Override
        protected void onPostExecute(java.lang.Void result) {
            mResultCallback.onWriteFinished(new android.print.PageRange[]{ android.print.PageRange.ALL_PAGES });
        }

        @java.lang.Override
        protected void onCancelled(java.lang.Void result) {
            mResultCallback.onWriteFailed(mContext.getString(R.string.write_fail_reason_cancelled));
        }
    }
}

