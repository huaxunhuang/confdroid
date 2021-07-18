/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A ViewGroup for managing focus behavior between overlapping views.
 */
public class BrowseFrameLayout extends android.widget.FrameLayout {
    /**
     * Interface for selecting a focused view in a BrowseFrameLayout when the system focus finder
     * couldn't find a view to focus.
     */
    public interface OnFocusSearchListener {
        /**
         * Returns the view where focus should be requested given the current focused view and
         * the direction of focus search.
         */
        android.view.View onFocusSearch(android.view.View focused, int direction);
    }

    /**
     * Interface for managing child focus in a BrowseFrameLayout.
     */
    public interface OnChildFocusListener {
        /**
         * See {@link android.view.ViewGroup#onRequestFocusInDescendants(
         * int, android.graphics.Rect)}.
         */
        boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect);

        /**
         * See {@link android.view.ViewGroup#requestChildFocus(
         * android.view.View, android.view.View)}.
         */
        void onRequestChildFocus(android.view.View child, android.view.View focused);
    }

    public BrowseFrameLayout(android.content.Context context) {
        this(context, null, 0);
    }

    public BrowseFrameLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowseFrameLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener mListener;

    private android.support.v17.leanback.widget.BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener;

    /**
     * Sets a {@link OnFocusSearchListener}.
     */
    public void setOnFocusSearchListener(android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener listener) {
        mListener = listener;
    }

    /**
     * Returns the {@link OnFocusSearchListener}.
     */
    public android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener getOnFocusSearchListener() {
        return mListener;
    }

    /**
     * Sets a {@link OnChildFocusListener}.
     */
    public void setOnChildFocusListener(android.support.v17.leanback.widget.BrowseFrameLayout.OnChildFocusListener listener) {
        mOnChildFocusListener = listener;
    }

    /**
     * Returns the {@link OnChildFocusListener}.
     */
    public android.support.v17.leanback.widget.BrowseFrameLayout.OnChildFocusListener getOnChildFocusListener() {
        return mOnChildFocusListener;
    }

    @java.lang.Override
    protected boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
        if (mOnChildFocusListener != null) {
            return mOnChildFocusListener.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @java.lang.Override
    public android.view.View focusSearch(android.view.View focused, int direction) {
        if (mListener != null) {
            android.view.View view = mListener.onFocusSearch(focused, direction);
            if (view != null) {
                return view;
            }
        }
        return super.focusSearch(focused, direction);
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        super.requestChildFocus(child, focused);
        if (mOnChildFocusListener != null) {
            mOnChildFocusListener.onRequestChildFocus(child, focused);
        }
    }
}

