/**
 * Copyright (C) 2007-2010 The Android Open Source Project
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
package android.view.inputmethod;


/**
 * Information about a single text correction that an editor has reported to
 * an input method.
 */
public final class CorrectionInfo implements android.os.Parcelable {
    private final int mOffset;

    private final java.lang.CharSequence mOldText;

    private final java.lang.CharSequence mNewText;

    /**
     *
     *
     * @param offset
     * 		The offset in the edited text where the old and new text start.
     * @param oldText
     * 		The old text that has been replaced.
     * @param newText
     * 		The replacement text.
     */
    public CorrectionInfo(int offset, java.lang.CharSequence oldText, java.lang.CharSequence newText) {
        mOffset = offset;
        mOldText = oldText;
        mNewText = newText;
    }

    private CorrectionInfo(android.os.Parcel source) {
        mOffset = source.readInt();
        mOldText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        mNewText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
    }

    /**
     * Return the offset position of this correction in the text. Both the {@link #getOldText()} and
     * {@link #getNewText()} start at this offset.
     */
    public int getOffset() {
        return mOffset;
    }

    /**
     * Return the text that has actually been typed by the user, and which has been corrected.
     */
    public java.lang.CharSequence getOldText() {
        return mOldText;
    }

    /**
     * Return the new text that corrects what was typed by the user.
     */
    public java.lang.CharSequence getNewText() {
        return mNewText;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("CorrectionInfo{#" + mOffset) + " \"") + mOldText) + "\" -> \"") + mNewText) + "\"}";
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mOffset);
        android.text.TextUtils.writeToParcel(mOldText, dest, flags);
        android.text.TextUtils.writeToParcel(mNewText, dest, flags);
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.CorrectionInfo> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.CorrectionInfo>() {
        public android.view.inputmethod.CorrectionInfo createFromParcel(android.os.Parcel source) {
            return new android.view.inputmethod.CorrectionInfo(source);
        }

        public android.view.inputmethod.CorrectionInfo[] newArray(int size) {
            return new android.view.inputmethod.CorrectionInfo[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}

