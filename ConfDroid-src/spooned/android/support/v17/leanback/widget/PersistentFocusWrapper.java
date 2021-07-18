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
 * limitations under the License
 */
package android.support.v17.leanback.widget;


/**
 * Saves the focused grandchild position.
 * Helps add persistent focus feature to various ViewGroups.
 */
class PersistentFocusWrapper extends android.widget.FrameLayout {
    private static final java.lang.String TAG = "PersistentFocusWrapper";

    private static final boolean DEBUG = false;

    private int mSelectedPosition = -1;

    /**
     * By default, focus is persisted when searching vertically
     * but not horizontally.
     */
    private boolean mPersistFocusVertical = true;

    public PersistentFocusWrapper(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public PersistentFocusWrapper(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int getGrandChildCount() {
        android.view.ViewGroup wrapper = ((android.view.ViewGroup) (getChildAt(0)));
        return wrapper == null ? 0 : wrapper.getChildCount();
    }

    /**
     * Clears the selected position and clears focus.
     */
    public void clearSelection() {
        mSelectedPosition = -1;
        if (hasFocus()) {
            clearFocus();
        }
    }

    /**
     * Persist focus when focus search direction is up or down.
     */
    public void persistFocusVertical() {
        mPersistFocusVertical = true;
    }

    /**
     * Persist focus when focus search direction is left or right.
     */
    public void persistFocusHorizontal() {
        mPersistFocusVertical = false;
    }

    private boolean shouldPersistFocusFromDirection(int direction) {
        return (mPersistFocusVertical && ((direction == android.view.View.FOCUS_UP) || (direction == android.view.View.FOCUS_DOWN))) || ((!mPersistFocusVertical) && ((direction == android.view.View.FOCUS_LEFT) || (direction == android.view.View.FOCUS_RIGHT)));
    }

    @java.lang.Override
    public void addFocusables(java.util.ArrayList<android.view.View> views, int direction, int focusableMode) {
        if (android.support.v17.leanback.widget.PersistentFocusWrapper.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.PersistentFocusWrapper.TAG, "addFocusables");

        if ((hasFocus() || (getGrandChildCount() == 0)) || (!shouldPersistFocusFromDirection(direction))) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            // Select a child in requestFocus
            views.add(this);
        }
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        super.requestChildFocus(child, focused);
        android.view.View view = focused;
        while ((view != null) && (view.getParent() != child)) {
            view = ((android.view.View) (view.getParent()));
        } 
        mSelectedPosition = (view == null) ? -1 : ((android.view.ViewGroup) (child)).indexOfChild(view);
        if (android.support.v17.leanback.widget.PersistentFocusWrapper.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.PersistentFocusWrapper.TAG, (("requestChildFocus focused " + focused) + " mSelectedPosition ") + mSelectedPosition);

    }

    @java.lang.Override
    public boolean requestFocus(int direction, android.graphics.Rect previouslyFocusedRect) {
        if (android.support.v17.leanback.widget.PersistentFocusWrapper.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.PersistentFocusWrapper.TAG, "requestFocus mSelectedPosition " + mSelectedPosition);

        android.view.ViewGroup wrapper = ((android.view.ViewGroup) (getChildAt(0)));
        if (((wrapper != null) && (mSelectedPosition >= 0)) && (mSelectedPosition < getGrandChildCount())) {
            if (wrapper.getChildAt(mSelectedPosition).requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    static class SavedState extends android.view.View.BaseSavedState {
        int mSelectedPosition;

        SavedState(android.os.Parcel in) {
            super(in);
            mSelectedPosition = in.readInt();
        }

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mSelectedPosition);
        }

        public static final android.os.Parcelable.Creator<android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState>() {
            @java.lang.Override
            public android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState(in);
            }

            @java.lang.Override
            public android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState[] newArray(int size) {
                return new android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState[size];
            }
        };
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        if (android.support.v17.leanback.widget.PersistentFocusWrapper.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.PersistentFocusWrapper.TAG, "onSaveInstanceState");

        android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState savedState = new android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = mSelectedPosition;
        return savedState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState savedState = ((android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState) (state));
        mSelectedPosition = ((android.support.v17.leanback.widget.PersistentFocusWrapper.SavedState) (state)).mSelectedPosition;
        if (android.support.v17.leanback.widget.PersistentFocusWrapper.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.PersistentFocusWrapper.TAG, "onRestoreInstanceState mSelectedPosition " + mSelectedPosition);

        super.onRestoreInstanceState(savedState.getSuperState());
    }
}

