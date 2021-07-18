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
package android.view.contentcapture;


/**
 * A session that is explicitly created by the app (and hence is a descendant of
 * {@link MainContentCaptureSession}).
 *
 * @unknown 
 */
final class ChildContentCaptureSession extends android.view.contentcapture.ContentCaptureSession {
    @android.annotation.NonNull
    private final android.view.contentcapture.ContentCaptureSession mParent;

    /**
     *
     *
     * @unknown 
     */
    protected ChildContentCaptureSession(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureSession parent, @android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext clientContext) {
        super(clientContext);
        mParent = parent;
    }

    @java.lang.Override
    android.view.contentcapture.MainContentCaptureSession getMainCaptureSession() {
        if (mParent instanceof android.view.contentcapture.MainContentCaptureSession) {
            return ((android.view.contentcapture.MainContentCaptureSession) (mParent));
        }
        return mParent.getMainCaptureSession();
    }

    @java.lang.Override
    android.view.contentcapture.ContentCaptureSession newChild(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext clientContext) {
        final android.view.contentcapture.ContentCaptureSession child = new android.view.contentcapture.ChildContentCaptureSession(this, clientContext);
        getMainCaptureSession().notifyChildSessionStarted(mId, child.mId, clientContext);
        return child;
    }

    @java.lang.Override
    void flush(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason) {
        mParent.flush(reason);
    }

    @java.lang.Override
    public void updateContentCaptureContext(@android.annotation.Nullable
    android.view.contentcapture.ContentCaptureContext context) {
        getMainCaptureSession().notifyContextUpdated(mId, context);
    }

    @java.lang.Override
    void onDestroy() {
        getMainCaptureSession().notifyChildSessionFinished(mParent.mId, mId);
    }

    @java.lang.Override
    void internalNotifyViewAppeared(@android.annotation.NonNull
    android.view.contentcapture.ViewNode.ViewStructureImpl node) {
        getMainCaptureSession().notifyViewAppeared(mId, node);
    }

    @java.lang.Override
    void internalNotifyViewDisappeared(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        getMainCaptureSession().notifyViewDisappeared(mId, id);
    }

    @java.lang.Override
    void internalNotifyViewTextChanged(@android.annotation.NonNull
    android.view.autofill.AutofillId id, @android.annotation.Nullable
    java.lang.CharSequence text) {
        getMainCaptureSession().notifyViewTextChanged(mId, id, text);
    }

    @java.lang.Override
    public void internalNotifyViewTreeEvent(boolean started) {
        getMainCaptureSession().notifyViewTreeEvent(mId, started);
    }

    @java.lang.Override
    boolean isContentCaptureEnabled() {
        return getMainCaptureSession().isContentCaptureEnabled();
    }
}

