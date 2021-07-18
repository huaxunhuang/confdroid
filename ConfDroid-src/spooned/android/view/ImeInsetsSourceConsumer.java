/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * limitations under the License
 */
package android.view;


/**
 * Controls the visibility and animations of IME window insets source.
 *
 * @unknown 
 */
public final class ImeInsetsSourceConsumer extends android.view.InsetsSourceConsumer {
    private android.view.inputmethod.EditorInfo mFocusedEditor;

    private android.view.inputmethod.EditorInfo mPreRenderedEditor;

    /**
     * Determines if IME would be shown next time IME is pre-rendered for currently focused
     * editor {@link #mFocusedEditor} if {@link #isServedEditorRendered} is {@code true}.
     */
    private boolean mShowOnNextImeRender;

    private boolean mHasWindowFocus;

    public ImeInsetsSourceConsumer(android.view.InsetsState state, java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionSupplier, android.view.InsetsController controller) {
        super(android.view.InsetsState.TYPE_IME, state, transactionSupplier, controller);
    }

    public void onPreRendered(android.view.inputmethod.EditorInfo info) {
        mPreRenderedEditor = info;
        if (mShowOnNextImeRender) {
            mShowOnNextImeRender = false;
            if (isServedEditorRendered()) {
                /* setVisible */
                applyImeVisibility(true);
            }
        }
    }

    public void onServedEditorChanged(android.view.inputmethod.EditorInfo info) {
        if (isDummyOrEmptyEditor(info)) {
            mShowOnNextImeRender = false;
        }
        mFocusedEditor = info;
    }

    public void applyImeVisibility(boolean setVisible) {
        if (!mHasWindowFocus) {
            // App window doesn't have focus, any visibility changes would be no-op.
            return;
        }
        mController.applyImeVisibility(setVisible);
    }

    @java.lang.Override
    public void onWindowFocusGained() {
        mHasWindowFocus = true;
        getImm().registerImeConsumer(this);
    }

    @java.lang.Override
    public void onWindowFocusLost() {
        mHasWindowFocus = false;
        getImm().unregisterImeConsumer(this);
    }

    /**
     * Request {@link InputMethodManager} to show the IME.
     *
     * @return @see {@link android.view.InsetsSourceConsumer.ShowResult}.
     */
    @java.lang.Override
    @android.view.InsetsSourceConsumer.ShowResult
    int requestShow(boolean fromIme) {
        // TODO: ResultReceiver for IME.
        // TODO: Set mShowOnNextImeRender to automatically show IME and guard it with a flag.
        if (fromIme) {
            return android.view.InsetsSourceConsumer.ShowResult.SHOW_IMMEDIATELY;
        }
        return /* resultReceiver */
        getImm().requestImeShow(null) ? android.view.InsetsSourceConsumer.ShowResult.SHOW_DELAYED : android.view.InsetsSourceConsumer.ShowResult.SHOW_FAILED;
    }

    /**
     * Notify {@link InputMethodService} that IME window is hidden.
     */
    @java.lang.Override
    void notifyHidden() {
        getImm().notifyImeHidden();
    }

    private boolean isDummyOrEmptyEditor(android.view.inputmethod.EditorInfo info) {
        // TODO(b/123044812): Handle dummy input gracefully in IME Insets API
        return (info == null) || ((info.fieldId <= 0) && (info.inputType <= 0));
    }

    private boolean isServedEditorRendered() {
        if ((((mFocusedEditor == null) || (mPreRenderedEditor == null)) || isDummyOrEmptyEditor(mFocusedEditor)) || isDummyOrEmptyEditor(mPreRenderedEditor)) {
            // No view is focused or ready.
            return false;
        }
        return android.view.ImeInsetsSourceConsumer.areEditorsSimilar(mFocusedEditor, mPreRenderedEditor);
    }

    @com.android.internal.annotations.VisibleForTesting
    public static boolean areEditorsSimilar(android.view.inputmethod.EditorInfo info1, android.view.inputmethod.EditorInfo info2) {
        // We don't need to compare EditorInfo.fieldId (View#id) since that shouldn't change
        // IME views.
        boolean areOptionsSimilar = ((info1.imeOptions == info2.imeOptions) && (info1.inputType == info2.inputType)) && android.text.TextUtils.equals(info1.packageName, info2.packageName);
        areOptionsSimilar &= (info1.privateImeOptions != null) ? info1.privateImeOptions.equals(info2.privateImeOptions) : true;
        if (!areOptionsSimilar) {
            return false;
        }
        // compare bundle extras.
        if (((info1.extras == null) && (info2.extras == null)) || (info1.extras == info2.extras)) {
            return true;
        }
        if (((info1.extras == null) && (info2.extras != null)) || ((info1.extras == null) && (info2.extras != null))) {
            return false;
        }
        if ((info1.extras.hashCode() == info2.extras.hashCode()) || info1.extras.equals(info1)) {
            return true;
        }
        if (info1.extras.size() != info2.extras.size()) {
            return false;
        }
        if (info1.extras.toString().equals(info2.extras.toString())) {
            return true;
        }
        // Compare bytes
        android.os.Parcel parcel1 = android.os.Parcel.obtain();
        info1.extras.writeToParcel(parcel1, 0);
        parcel1.setDataPosition(0);
        android.os.Parcel parcel2 = android.os.Parcel.obtain();
        info2.extras.writeToParcel(parcel2, 0);
        parcel2.setDataPosition(0);
        return java.util.Arrays.equals(parcel1.createByteArray(), parcel2.createByteArray());
    }

    private android.view.inputmethod.InputMethodManager getImm() {
        return mController.getViewRoot().mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
    }
}

