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


// TODO(b/122484602): add javadocs / implement Parcelable / implement
// TODO(b/122484602): for now it's extending ViewNode directly as it needs most of its properties,
// but it might be better to create a common, abstract android.view.ViewNode class that both extend
// instead
/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
public final class ViewNode extends android.app.assist.AssistStructure.ViewNode {
    private static final java.lang.String TAG = android.view.contentcapture.ViewNode.class.getSimpleName();

    private static final long FLAGS_HAS_TEXT = 1L << 0;

    private static final long FLAGS_HAS_COMPLEX_TEXT = 1L << 1;

    private static final long FLAGS_VISIBILITY_MASK = (android.view.View.VISIBLE | android.view.View.INVISIBLE) | android.view.View.GONE;

    private static final long FLAGS_HAS_CLASSNAME = 1L << 4;

    private static final long FLAGS_HAS_AUTOFILL_ID = 1L << 5;

    private static final long FLAGS_HAS_AUTOFILL_PARENT_ID = 1L << 6;

    private static final long FLAGS_HAS_ID = 1L << 7;

    private static final long FLAGS_HAS_LARGE_COORDS = 1L << 8;

    private static final long FLAGS_HAS_SCROLL = 1L << 9;

    private static final long FLAGS_ASSIST_BLOCKED = 1L << 10;

    private static final long FLAGS_DISABLED = 1L << 11;

    private static final long FLAGS_CLICKABLE = 1L << 12;

    private static final long FLAGS_LONG_CLICKABLE = 1L << 13;

    private static final long FLAGS_CONTEXT_CLICKABLE = 1L << 14;

    private static final long FLAGS_FOCUSABLE = 1L << 15;

    private static final long FLAGS_FOCUSED = 1L << 16;

    private static final long FLAGS_ACCESSIBILITY_FOCUSED = 1L << 17;

    private static final long FLAGS_CHECKABLE = 1L << 18;

    private static final long FLAGS_CHECKED = 1L << 19;

    private static final long FLAGS_SELECTED = 1L << 20;

    private static final long FLAGS_ACTIVATED = 1L << 21;

    private static final long FLAGS_OPAQUE = 1L << 22;

    private static final long FLAGS_HAS_CONTENT_DESCRIPTION = 1L << 23;

    private static final long FLAGS_HAS_EXTRAS = 1L << 24;

    private static final long FLAGS_HAS_LOCALE_LIST = 1L << 25;

    private static final long FLAGS_HAS_INPUT_TYPE = 1L << 26;

    private static final long FLAGS_HAS_MIN_TEXT_EMS = 1L << 27;

    private static final long FLAGS_HAS_MAX_TEXT_EMS = 1L << 28;

    private static final long FLAGS_HAS_MAX_TEXT_LENGTH = 1L << 29;

    private static final long FLAGS_HAS_TEXT_ID_ENTRY = 1L << 30;

    private static final long FLAGS_HAS_AUTOFILL_TYPE = 1L << 31;

    private static final long FLAGS_HAS_AUTOFILL_VALUE = 1L << 32;

    private static final long FLAGS_HAS_AUTOFILL_HINTS = 1L << 33;

    private static final long FLAGS_HAS_AUTOFILL_OPTIONS = 1L << 34;

    /**
     * Flags used to optimize what's written to the parcel
     */
    private long mFlags;

    private android.view.autofill.AutofillId mParentAutofillId;

    private android.view.autofill.AutofillId mAutofillId;

    private android.view.contentcapture.ViewNode.ViewNodeText mText;

    private java.lang.String mClassName;

    private int mId = android.view.View.NO_ID;

    private java.lang.String mIdPackage;

    private java.lang.String mIdType;

    private java.lang.String mIdEntry;

    private int mX;

    private int mY;

    private int mScrollX;

    private int mScrollY;

    private int mWidth;

    private int mHeight;

    private java.lang.CharSequence mContentDescription;

    private android.os.Bundle mExtras;

    private android.os.LocaleList mLocaleList;

    private int mInputType;

    private int mMinEms = -1;

    private int mMaxEms = -1;

    private int mMaxLength = -1;

    private java.lang.String mTextIdEntry;

    @android.view.View.AutofillType
    private int mAutofillType = android.view.View.AUTOFILL_TYPE_NONE;

    private java.lang.String[] mAutofillHints;

    private android.view.autofill.AutofillValue mAutofillValue;

    private java.lang.CharSequence[] mAutofillOptions;

    /**
     *
     *
     * @unknown 
     */
    public ViewNode() {
    }

    private ViewNode(long nodeFlags, @android.annotation.NonNull
    android.os.Parcel parcel) {
        mFlags = nodeFlags;
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_ID) != 0) {
            mAutofillId = parcel.readParcelable(null);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_PARENT_ID) != 0) {
            mParentAutofillId = parcel.readParcelable(null);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT) != 0) {
            mText = new android.view.contentcapture.ViewNode.ViewNodeText(parcel, (nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_COMPLEX_TEXT) == 0);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_CLASSNAME) != 0) {
            mClassName = parcel.readString();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_ID) != 0) {
            mId = parcel.readInt();
            if (mId != android.view.View.NO_ID) {
                mIdEntry = parcel.readString();
                if (mIdEntry != null) {
                    mIdType = parcel.readString();
                    mIdPackage = parcel.readString();
                }
            }
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_LARGE_COORDS) != 0) {
            mX = parcel.readInt();
            mY = parcel.readInt();
            mWidth = parcel.readInt();
            mHeight = parcel.readInt();
        } else {
            int val = parcel.readInt();
            mX = val & 0x7fff;
            mY = (val >> 16) & 0x7fff;
            val = parcel.readInt();
            mWidth = val & 0x7fff;
            mHeight = (val >> 16) & 0x7fff;
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_SCROLL) != 0) {
            mScrollX = parcel.readInt();
            mScrollY = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION) != 0) {
            mContentDescription = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_EXTRAS) != 0) {
            mExtras = parcel.readBundle();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_LOCALE_LIST) != 0) {
            mLocaleList = parcel.readParcelable(null);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_INPUT_TYPE) != 0) {
            mInputType = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MIN_TEXT_EMS) != 0) {
            mMinEms = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_EMS) != 0) {
            mMaxEms = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_LENGTH) != 0) {
            mMaxLength = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT_ID_ENTRY) != 0) {
            mTextIdEntry = parcel.readString();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_TYPE) != 0) {
            mAutofillType = parcel.readInt();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_HINTS) != 0) {
            mAutofillHints = parcel.readStringArray();
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_VALUE) != 0) {
            mAutofillValue = parcel.readParcelable(null);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_OPTIONS) != 0) {
            mAutofillOptions = parcel.readCharSequenceArray();
        }
    }

    /**
     * Returns the {@link AutofillId} of this view's parent, if the parent is also part of the
     * screen observation tree.
     */
    @android.annotation.Nullable
    public android.view.autofill.AutofillId getParentAutofillId() {
        return mParentAutofillId;
    }

    @java.lang.Override
    public android.view.autofill.AutofillId getAutofillId() {
        return mAutofillId;
    }

    @java.lang.Override
    public java.lang.CharSequence getText() {
        return mText != null ? mText.mText : null;
    }

    @java.lang.Override
    public java.lang.String getClassName() {
        return mClassName;
    }

    @java.lang.Override
    public int getId() {
        return mId;
    }

    @java.lang.Override
    public java.lang.String getIdPackage() {
        return mIdPackage;
    }

    @java.lang.Override
    public java.lang.String getIdType() {
        return mIdType;
    }

    @java.lang.Override
    public java.lang.String getIdEntry() {
        return mIdEntry;
    }

    @java.lang.Override
    public int getLeft() {
        return mX;
    }

    @java.lang.Override
    public int getTop() {
        return mY;
    }

    @java.lang.Override
    public int getScrollX() {
        return mScrollX;
    }

    @java.lang.Override
    public int getScrollY() {
        return mScrollY;
    }

    @java.lang.Override
    public int getWidth() {
        return mWidth;
    }

    @java.lang.Override
    public int getHeight() {
        return mHeight;
    }

    @java.lang.Override
    public boolean isAssistBlocked() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_ASSIST_BLOCKED) != 0;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_DISABLED) == 0;
    }

    @java.lang.Override
    public boolean isClickable() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_CLICKABLE) != 0;
    }

    @java.lang.Override
    public boolean isLongClickable() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_LONG_CLICKABLE) != 0;
    }

    @java.lang.Override
    public boolean isContextClickable() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_CONTEXT_CLICKABLE) != 0;
    }

    @java.lang.Override
    public boolean isFocusable() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_FOCUSABLE) != 0;
    }

    @java.lang.Override
    public boolean isFocused() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_FOCUSED) != 0;
    }

    @java.lang.Override
    public boolean isAccessibilityFocused() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED) != 0;
    }

    @java.lang.Override
    public boolean isCheckable() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_CHECKABLE) != 0;
    }

    @java.lang.Override
    public boolean isChecked() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_CHECKED) != 0;
    }

    @java.lang.Override
    public boolean isSelected() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_SELECTED) != 0;
    }

    @java.lang.Override
    public boolean isActivated() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_ACTIVATED) != 0;
    }

    @java.lang.Override
    public boolean isOpaque() {
        return (mFlags & android.view.contentcapture.ViewNode.FLAGS_OPAQUE) != 0;
    }

    @java.lang.Override
    public java.lang.CharSequence getContentDescription() {
        return mContentDescription;
    }

    @java.lang.Override
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public java.lang.String getHint() {
        return mText != null ? mText.mHint : null;
    }

    @java.lang.Override
    public int getTextSelectionStart() {
        return mText != null ? mText.mTextSelectionStart : -1;
    }

    @java.lang.Override
    public int getTextSelectionEnd() {
        return mText != null ? mText.mTextSelectionEnd : -1;
    }

    @java.lang.Override
    public int getTextColor() {
        return mText != null ? mText.mTextColor : TEXT_COLOR_UNDEFINED;
    }

    @java.lang.Override
    public int getTextBackgroundColor() {
        return mText != null ? mText.mTextBackgroundColor : TEXT_COLOR_UNDEFINED;
    }

    @java.lang.Override
    public float getTextSize() {
        return mText != null ? mText.mTextSize : 0;
    }

    @java.lang.Override
    public int getTextStyle() {
        return mText != null ? mText.mTextStyle : 0;
    }

    @java.lang.Override
    public int[] getTextLineCharOffsets() {
        return mText != null ? mText.mLineCharOffsets : null;
    }

    @java.lang.Override
    public int[] getTextLineBaselines() {
        return mText != null ? mText.mLineBaselines : null;
    }

    @java.lang.Override
    public int getVisibility() {
        return ((int) (mFlags & android.view.contentcapture.ViewNode.FLAGS_VISIBILITY_MASK));
    }

    @java.lang.Override
    public int getInputType() {
        return mInputType;
    }

    @java.lang.Override
    public int getMinTextEms() {
        return mMinEms;
    }

    @java.lang.Override
    public int getMaxTextEms() {
        return mMaxEms;
    }

    @java.lang.Override
    public int getMaxTextLength() {
        return mMaxLength;
    }

    @java.lang.Override
    public java.lang.String getTextIdEntry() {
        return mTextIdEntry;
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return mAutofillType;
    }

    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String[] getAutofillHints() {
        return mAutofillHints;
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.view.autofill.AutofillValue getAutofillValue() {
        return mAutofillValue;
    }

    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.CharSequence[] getAutofillOptions() {
        return mAutofillOptions;
    }

    @java.lang.Override
    public android.os.LocaleList getLocaleList() {
        return mLocaleList;
    }

    private void writeSelfToParcel(@android.annotation.NonNull
    android.os.Parcel parcel, int parcelFlags) {
        long nodeFlags = mFlags;
        if (mAutofillId != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_ID;
        }
        if (mParentAutofillId != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_PARENT_ID;
        }
        if (mText != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT;
            if (!mText.isSimple()) {
                nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_COMPLEX_TEXT;
            }
        }
        if (mClassName != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_CLASSNAME;
        }
        if (mId != android.view.View.NO_ID) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_ID;
        }
        if ((((mX & (~0x7fff)) != 0) || ((mY & (~0x7fff)) != 0)) || (((mWidth & (~0x7fff)) != 0) | ((mHeight & (~0x7fff)) != 0))) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_LARGE_COORDS;
        }
        if ((mScrollX != 0) || (mScrollY != 0)) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_SCROLL;
        }
        if (mContentDescription != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION;
        }
        if (mExtras != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_EXTRAS;
        }
        if (mLocaleList != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_LOCALE_LIST;
        }
        if (mInputType != 0) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_INPUT_TYPE;
        }
        if (mMinEms > (-1)) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_MIN_TEXT_EMS;
        }
        if (mMaxEms > (-1)) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_EMS;
        }
        if (mMaxLength > (-1)) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_LENGTH;
        }
        if (mTextIdEntry != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT_ID_ENTRY;
        }
        if (mAutofillValue != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_VALUE;
        }
        if (mAutofillType != android.view.View.AUTOFILL_TYPE_NONE) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_TYPE;
        }
        if (mAutofillHints != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_HINTS;
        }
        if (mAutofillOptions != null) {
            nodeFlags |= android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_OPTIONS;
        }
        parcel.writeLong(nodeFlags);
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_ID) != 0) {
            parcel.writeParcelable(mAutofillId, parcelFlags);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_PARENT_ID) != 0) {
            parcel.writeParcelable(mParentAutofillId, parcelFlags);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT) != 0) {
            mText.writeToParcel(parcel, (nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_COMPLEX_TEXT) == 0);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_CLASSNAME) != 0) {
            parcel.writeString(mClassName);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_ID) != 0) {
            parcel.writeInt(mId);
            if (mId != android.view.View.NO_ID) {
                parcel.writeString(mIdEntry);
                if (mIdEntry != null) {
                    parcel.writeString(mIdType);
                    parcel.writeString(mIdPackage);
                }
            }
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_LARGE_COORDS) != 0) {
            parcel.writeInt(mX);
            parcel.writeInt(mY);
            parcel.writeInt(mWidth);
            parcel.writeInt(mHeight);
        } else {
            parcel.writeInt((mY << 16) | mX);
            parcel.writeInt((mHeight << 16) | mWidth);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_SCROLL) != 0) {
            parcel.writeInt(mScrollX);
            parcel.writeInt(mScrollY);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION) != 0) {
            android.text.TextUtils.writeToParcel(mContentDescription, parcel, 0);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_EXTRAS) != 0) {
            parcel.writeBundle(mExtras);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_LOCALE_LIST) != 0) {
            parcel.writeParcelable(mLocaleList, 0);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_INPUT_TYPE) != 0) {
            parcel.writeInt(mInputType);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MIN_TEXT_EMS) != 0) {
            parcel.writeInt(mMinEms);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_EMS) != 0) {
            parcel.writeInt(mMaxEms);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_MAX_TEXT_LENGTH) != 0) {
            parcel.writeInt(mMaxLength);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_TEXT_ID_ENTRY) != 0) {
            parcel.writeString(mTextIdEntry);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_TYPE) != 0) {
            parcel.writeInt(mAutofillType);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_HINTS) != 0) {
            parcel.writeStringArray(mAutofillHints);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_VALUE) != 0) {
            parcel.writeParcelable(mAutofillValue, 0);
        }
        if ((nodeFlags & android.view.contentcapture.ViewNode.FLAGS_HAS_AUTOFILL_OPTIONS) != 0) {
            parcel.writeCharSequenceArray(mAutofillOptions);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void writeToParcel(@android.annotation.NonNull
    android.os.Parcel parcel, @android.annotation.Nullable
    android.view.contentcapture.ViewNode node, int flags) {
        if (node == null) {
            parcel.writeLong(0);
        } else {
            node.writeSelfToParcel(parcel, flags);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.Nullable
    public static android.view.contentcapture.ViewNode readFromParcel(@android.annotation.NonNull
    android.os.Parcel parcel) {
        final long nodeFlags = parcel.readLong();
        return nodeFlags == 0 ? null : new android.view.contentcapture.ViewNode(nodeFlags, parcel);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static final class ViewStructureImpl extends android.view.ViewStructure {
        final android.view.contentcapture.ViewNode mNode = new android.view.contentcapture.ViewNode();

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        public ViewStructureImpl(@android.annotation.NonNull
        android.view.View view) {
            mNode.mAutofillId = getAutofillId();
            final android.view.ViewParent parent = view.getParent();
            if (parent instanceof android.view.View) {
                mNode.mParentAutofillId = ((android.view.View) (parent)).getAutofillId();
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        public ViewStructureImpl(@android.annotation.NonNull
        android.view.autofill.AutofillId parentId, long virtualId, int sessionId) {
            mNode.mParentAutofillId = com.android.internal.util.Preconditions.checkNotNull(parentId);
            mNode.mAutofillId = new android.view.autofill.AutofillId(parentId, virtualId, sessionId);
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        public android.view.contentcapture.ViewNode getNode() {
            return mNode;
        }

        @java.lang.Override
        public void setId(int id, java.lang.String packageName, java.lang.String typeName, java.lang.String entryName) {
            mNode.mId = id;
            mNode.mIdPackage = packageName;
            mNode.mIdType = typeName;
            mNode.mIdEntry = entryName;
        }

        @java.lang.Override
        public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
            mNode.mX = left;
            mNode.mY = top;
            mNode.mScrollX = scrollX;
            mNode.mScrollY = scrollY;
            mNode.mWidth = width;
            mNode.mHeight = height;
        }

        @java.lang.Override
        public void setTransformation(android.graphics.Matrix matrix) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setTransformation() is not supported");
        }

        @java.lang.Override
        public void setElevation(float elevation) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setElevation() is not supported");
        }

        @java.lang.Override
        public void setAlpha(float alpha) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setAlpha() is not supported");
        }

        @java.lang.Override
        public void setVisibility(int visibility) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_VISIBILITY_MASK)) | (visibility & android.view.contentcapture.ViewNode.FLAGS_VISIBILITY_MASK);
        }

        @java.lang.Override
        public void setAssistBlocked(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_ASSIST_BLOCKED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_ASSIST_BLOCKED : 0);
        }

        @java.lang.Override
        public void setEnabled(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_DISABLED)) | (state ? 0 : android.view.contentcapture.ViewNode.FLAGS_DISABLED);
        }

        @java.lang.Override
        public void setClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_CLICKABLE)) | (state ? android.view.contentcapture.ViewNode.FLAGS_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setLongClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_LONG_CLICKABLE)) | (state ? android.view.contentcapture.ViewNode.FLAGS_LONG_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setContextClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_CONTEXT_CLICKABLE)) | (state ? android.view.contentcapture.ViewNode.FLAGS_CONTEXT_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setFocusable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_FOCUSABLE)) | (state ? android.view.contentcapture.ViewNode.FLAGS_FOCUSABLE : 0);
        }

        @java.lang.Override
        public void setFocused(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_FOCUSED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_FOCUSED : 0);
        }

        @java.lang.Override
        public void setAccessibilityFocused(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED : 0);
        }

        @java.lang.Override
        public void setCheckable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_CHECKABLE)) | (state ? android.view.contentcapture.ViewNode.FLAGS_CHECKABLE : 0);
        }

        @java.lang.Override
        public void setChecked(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_CHECKED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_CHECKED : 0);
        }

        @java.lang.Override
        public void setSelected(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_SELECTED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_SELECTED : 0);
        }

        @java.lang.Override
        public void setActivated(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_ACTIVATED)) | (state ? android.view.contentcapture.ViewNode.FLAGS_ACTIVATED : 0);
        }

        @java.lang.Override
        public void setOpaque(boolean opaque) {
            mNode.mFlags = (mNode.mFlags & (~android.view.contentcapture.ViewNode.FLAGS_OPAQUE)) | (opaque ? android.view.contentcapture.ViewNode.FLAGS_OPAQUE : 0);
        }

        @java.lang.Override
        public void setClassName(java.lang.String className) {
            mNode.mClassName = className;
        }

        @java.lang.Override
        public void setContentDescription(java.lang.CharSequence contentDescription) {
            mNode.mContentDescription = contentDescription;
        }

        @java.lang.Override
        public void setText(java.lang.CharSequence text) {
            final android.view.contentcapture.ViewNode.ViewNodeText t = getNodeText();
            t.mText = android.text.TextUtils.trimNoCopySpans(text);
            t.mTextSelectionStart = t.mTextSelectionEnd = -1;
        }

        @java.lang.Override
        public void setText(java.lang.CharSequence text, int selectionStart, int selectionEnd) {
            final android.view.contentcapture.ViewNode.ViewNodeText t = getNodeText();
            t.mText = android.text.TextUtils.trimNoCopySpans(text);
            t.mTextSelectionStart = selectionStart;
            t.mTextSelectionEnd = selectionEnd;
        }

        @java.lang.Override
        public void setTextStyle(float size, int fgColor, int bgColor, int style) {
            final android.view.contentcapture.ViewNode.ViewNodeText t = getNodeText();
            t.mTextColor = fgColor;
            t.mTextBackgroundColor = bgColor;
            t.mTextSize = size;
            t.mTextStyle = style;
        }

        @java.lang.Override
        public void setTextLines(int[] charOffsets, int[] baselines) {
            final android.view.contentcapture.ViewNode.ViewNodeText t = getNodeText();
            t.mLineCharOffsets = charOffsets;
            t.mLineBaselines = baselines;
        }

        @java.lang.Override
        public void setTextIdEntry(java.lang.String entryName) {
            mNode.mTextIdEntry = com.android.internal.util.Preconditions.checkNotNull(entryName);
        }

        @java.lang.Override
        public void setHint(java.lang.CharSequence hint) {
            getNodeText().mHint = (hint != null) ? hint.toString() : null;
        }

        @java.lang.Override
        public java.lang.CharSequence getText() {
            return mNode.getText();
        }

        @java.lang.Override
        public int getTextSelectionStart() {
            return mNode.getTextSelectionStart();
        }

        @java.lang.Override
        public int getTextSelectionEnd() {
            return mNode.getTextSelectionEnd();
        }

        @java.lang.Override
        public java.lang.CharSequence getHint() {
            return mNode.getHint();
        }

        @java.lang.Override
        public android.os.Bundle getExtras() {
            if (mNode.mExtras != null) {
                return mNode.mExtras;
            }
            mNode.mExtras = new android.os.Bundle();
            return mNode.mExtras;
        }

        @java.lang.Override
        public boolean hasExtras() {
            return mNode.mExtras != null;
        }

        @java.lang.Override
        public void setChildCount(int num) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setChildCount() is not supported");
        }

        @java.lang.Override
        public int addChildCount(int num) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "addChildCount() is not supported");
            return 0;
        }

        @java.lang.Override
        public int getChildCount() {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "getChildCount() is not supported");
            return 0;
        }

        @java.lang.Override
        public android.view.ViewStructure newChild(int index) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "newChild() is not supported");
            return null;
        }

        @java.lang.Override
        public android.view.ViewStructure asyncNewChild(int index) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "asyncNewChild() is not supported");
            return null;
        }

        @java.lang.Override
        public android.view.autofill.AutofillId getAutofillId() {
            return mNode.mAutofillId;
        }

        @java.lang.Override
        public void setAutofillId(android.view.autofill.AutofillId id) {
            mNode.mAutofillId = com.android.internal.util.Preconditions.checkNotNull(id);
        }

        @java.lang.Override
        public void setAutofillId(android.view.autofill.AutofillId parentId, int virtualId) {
            mNode.mParentAutofillId = com.android.internal.util.Preconditions.checkNotNull(parentId);
            mNode.mAutofillId = new android.view.autofill.AutofillId(parentId, virtualId);
        }

        @java.lang.Override
        public void setAutofillType(@android.view.View.AutofillType
        int type) {
            mNode.mAutofillType = type;
        }

        @java.lang.Override
        public void setAutofillHints(java.lang.String[] hints) {
            mNode.mAutofillHints = hints;
        }

        @java.lang.Override
        public void setAutofillValue(android.view.autofill.AutofillValue value) {
            mNode.mAutofillValue = value;
        }

        @java.lang.Override
        public void setAutofillOptions(java.lang.CharSequence[] options) {
            mNode.mAutofillOptions = options;
        }

        @java.lang.Override
        public void setInputType(int inputType) {
            mNode.mInputType = inputType;
        }

        @java.lang.Override
        public void setMinTextEms(int minEms) {
            mNode.mMinEms = minEms;
        }

        @java.lang.Override
        public void setMaxTextEms(int maxEms) {
            mNode.mMaxEms = maxEms;
        }

        @java.lang.Override
        public void setMaxTextLength(int maxLength) {
            mNode.mMaxLength = maxLength;
        }

        @java.lang.Override
        public void setDataIsSensitive(boolean sensitive) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setDataIsSensitive() is not supported");
        }

        @java.lang.Override
        public void asyncCommit() {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "asyncCommit() is not supported");
        }

        @java.lang.Override
        public android.graphics.Rect getTempRect() {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "getTempRect() is not supported");
            return null;
        }

        @java.lang.Override
        public void setWebDomain(java.lang.String domain) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setWebDomain() is not supported");
        }

        @java.lang.Override
        public void setLocaleList(android.os.LocaleList localeList) {
            mNode.mLocaleList = localeList;
        }

        @java.lang.Override
        public android.view.ViewStructure.HtmlInfo.Builder newHtmlInfoBuilder(java.lang.String tagName) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "newHtmlInfoBuilder() is not supported");
            return null;
        }

        @java.lang.Override
        public void setHtmlInfo(android.view.ViewStructure.HtmlInfo htmlInfo) {
            android.util.Log.w(android.view.contentcapture.ViewNode.TAG, "setHtmlInfo() is not supported");
        }

        private android.view.contentcapture.ViewNode.ViewNodeText getNodeText() {
            if (mNode.mText != null) {
                return mNode.mText;
            }
            mNode.mText = new android.view.contentcapture.ViewNode.ViewNodeText();
            return mNode.mText;
        }
    }

    // TODO(b/122484602): copied 'as-is' from AssistStructure, except for writeSensitive
    static final class ViewNodeText {
        java.lang.CharSequence mText;

        float mTextSize;

        int mTextStyle;

        int mTextColor = TEXT_COLOR_UNDEFINED;

        int mTextBackgroundColor = TEXT_COLOR_UNDEFINED;

        int mTextSelectionStart;

        int mTextSelectionEnd;

        int[] mLineCharOffsets;

        int[] mLineBaselines;

        java.lang.String mHint;

        ViewNodeText() {
        }

        boolean isSimple() {
            return (((((mTextBackgroundColor == TEXT_COLOR_UNDEFINED) && (mTextSelectionStart == 0)) && (mTextSelectionEnd == 0)) && (mLineCharOffsets == null)) && (mLineBaselines == null)) && (mHint == null);
        }

        ViewNodeText(android.os.Parcel in, boolean simple) {
            mText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            mTextSize = in.readFloat();
            mTextStyle = in.readInt();
            mTextColor = in.readInt();
            if (!simple) {
                mTextBackgroundColor = in.readInt();
                mTextSelectionStart = in.readInt();
                mTextSelectionEnd = in.readInt();
                mLineCharOffsets = in.createIntArray();
                mLineBaselines = in.createIntArray();
                mHint = in.readString();
            }
        }

        void writeToParcel(android.os.Parcel out, boolean simple) {
            android.text.TextUtils.writeToParcel(mText, out, 0);
            out.writeFloat(mTextSize);
            out.writeInt(mTextStyle);
            out.writeInt(mTextColor);
            if (!simple) {
                out.writeInt(mTextBackgroundColor);
                out.writeInt(mTextSelectionStart);
                out.writeInt(mTextSelectionEnd);
                out.writeIntArray(mLineCharOffsets);
                out.writeIntArray(mLineBaselines);
                out.writeString(mHint);
            }
        }
    }
}

