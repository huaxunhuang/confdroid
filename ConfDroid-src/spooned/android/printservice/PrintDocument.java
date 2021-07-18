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
package android.printservice;


/**
 * This class represents a printed document from the perspective of a print
 * service. It exposes APIs to query the document and obtain its data.
 * <p>
 * <strong>Note: </strong> All methods of this class must be executed on the
 * main application thread.
 * </p>
 */
public final class PrintDocument {
    private static final java.lang.String LOG_TAG = "PrintDocument";

    private final android.print.PrintJobId mPrintJobId;

    private final android.printservice.IPrintServiceClient mPrintServiceClient;

    private final android.print.PrintDocumentInfo mInfo;

    PrintDocument(android.print.PrintJobId printJobId, android.printservice.IPrintServiceClient printServiceClient, android.print.PrintDocumentInfo info) {
        mPrintJobId = printJobId;
        mPrintServiceClient = printServiceClient;
        mInfo = info;
    }

    /**
     * Gets the {@link PrintDocumentInfo} that describes this document.
     *
     * @return The document info.
     */
    @android.annotation.NonNull
    public android.print.PrintDocumentInfo getInfo() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return mInfo;
    }

    /**
     * Gets the data associated with this document.
     * <p>
     * <strong>Note: </strong> It is a responsibility of the client to open a
     * stream to the returned file descriptor, fully read the data, and close
     * the file descriptor.
     * </p>
     *
     * @return A file descriptor for reading the data.
     */
    @android.annotation.Nullable
    public android.os.ParcelFileDescriptor getData() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        android.os.ParcelFileDescriptor source = null;
        android.os.ParcelFileDescriptor sink = null;
        try {
            android.os.ParcelFileDescriptor[] fds = android.os.ParcelFileDescriptor.createPipe();
            source = fds[0];
            sink = fds[1];
            mPrintServiceClient.writePrintJobData(sink, mPrintJobId);
            return source;
        } catch (java.io.IOException ioe) {
            android.util.Log.e(android.printservice.PrintDocument.LOG_TAG, "Error calling getting print job data!", ioe);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintDocument.LOG_TAG, "Error calling getting print job data!", re);
        } finally {
            if (sink != null) {
                try {
                    sink.close();
                } catch (java.io.IOException ioe) {
                    /* ignore */
                }
            }
        }
        return null;
    }
}

