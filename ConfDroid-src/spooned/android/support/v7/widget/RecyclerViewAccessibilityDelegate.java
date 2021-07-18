/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * The AccessibilityDelegate used by RecyclerView.
 * <p>
 * This class handles basic accessibility actions and delegates them to LayoutManager.
 */
public class RecyclerViewAccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
    final android.support.v7.widget.RecyclerView mRecyclerView;

    public RecyclerViewAccessibilityDelegate(android.support.v7.widget.RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    boolean shouldIgnore() {
        return mRecyclerView.hasPendingAdapterUpdates();
    }

    @java.lang.Override
    public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
        if (super.performAccessibilityAction(host, action, args)) {
            return true;
        }
        if ((!shouldIgnore()) && (mRecyclerView.getLayoutManager() != null)) {
            return mRecyclerView.getLayoutManager().performAccessibilityAction(action, args);
        }
        return false;
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        info.setClassName(android.support.v7.widget.RecyclerView.class.getName());
        if ((!shouldIgnore()) && (mRecyclerView.getLayoutManager() != null)) {
            mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(info);
        }
    }

    @java.lang.Override
    public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(host, event);
        event.setClassName(android.support.v7.widget.RecyclerView.class.getName());
        if ((host instanceof android.support.v7.widget.RecyclerView) && (!shouldIgnore())) {
            android.support.v7.widget.RecyclerView rv = ((android.support.v7.widget.RecyclerView) (host));
            if (rv.getLayoutManager() != null) {
                rv.getLayoutManager().onInitializeAccessibilityEvent(event);
            }
        }
    }

    /**
     * Gets the AccessibilityDelegate for an individual item in the RecyclerView.
     * A basic item delegate is provided by default, but you can override this
     * method to provide a custom per-item delegate.
     */
    public android.support.v4.view.AccessibilityDelegateCompat getItemDelegate() {
        return mItemDelegate;
    }

    final android.support.v4.view.AccessibilityDelegateCompat mItemDelegate = new android.support.v4.view.AccessibilityDelegateCompat() {
        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            if ((!shouldIgnore()) && (mRecyclerView.getLayoutManager() != null)) {
                mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(host, info);
            }
        }

        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            if ((!shouldIgnore()) && (mRecyclerView.getLayoutManager() != null)) {
                return mRecyclerView.getLayoutManager().performAccessibilityActionForItem(host, action, args);
            }
            return false;
        }
    };
}

