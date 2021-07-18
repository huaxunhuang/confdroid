/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.os;


/**
 * Extends ResultReceiver to allow the server end of the ResultReceiver to synchronously wait
 * on the response from the client. This enables an RPC like system but with the ability to
 * timeout and discard late results.
 *
 * NOTE: Can only be used for one response. Subsequent responses on the same instance are ignored.
 * {@hide }
 */
public class SynchronousResultReceiver extends android.os.ResultReceiver {
    public static class Result {
        public int resultCode;

        @android.annotation.Nullable
        public android.os.Bundle bundle;

        public Result(int resultCode, @android.annotation.Nullable
        android.os.Bundle bundle) {
            this.resultCode = resultCode;
            this.bundle = bundle;
        }
    }

    private final java.util.concurrent.CompletableFuture<android.os.SynchronousResultReceiver.Result> mFuture = new java.util.concurrent.CompletableFuture<>();

    public SynchronousResultReceiver() {
        super(((android.os.Handler) (null)));
    }

    @java.lang.Override
    protected final void onReceiveResult(int resultCode, android.os.Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        mFuture.complete(new android.os.SynchronousResultReceiver.Result(resultCode, resultData));
    }

    /**
     * Blocks waiting for the result from the remote client.
     *
     * @return the Result
     * @throws TimeoutException
     * 		if the timeout in milliseconds expired.
     */
    @android.annotation.NonNull
    public android.os.SynchronousResultReceiver.Result awaitResult(long timeoutMillis) throws java.util.concurrent.TimeoutException {
        final long deadline = java.lang.System.currentTimeMillis() + timeoutMillis;
        while (timeoutMillis >= 0) {
            try {
                return mFuture.get(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.util.concurrent.ExecutionException e) {
                // This will NEVER happen.
                throw new java.lang.AssertionError("Error receiving response", e);
            } catch (java.lang.InterruptedException e) {
                // The thread was interrupted, try and get the value again, this time
                // with the remaining time until the deadline.
                timeoutMillis -= deadline - java.lang.System.currentTimeMillis();
            }
        } 
        throw new java.util.concurrent.TimeoutException();
    }
}

