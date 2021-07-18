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
 * limitations under the License.
 */
package android.databinding.adapters;


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.AbsListView.class, attribute = "android:listSelector", method = "setSelector"), @android.databinding.BindingMethod(type = android.widget.AbsListView.class, attribute = "android:scrollingCache", method = "setScrollingCacheEnabled"), @android.databinding.BindingMethod(type = android.widget.AbsListView.class, attribute = "android:smoothScrollbar", method = "setSmoothScrollbarEnabled"), @android.databinding.BindingMethod(type = android.widget.AbsListView.class, attribute = "android:onMovedToScrapHeap", method = "setRecyclerListener") })
public class AbsListViewBindingAdapter {
    @android.databinding.BindingAdapter(value = { "android:onScroll", "android:onScrollStateChanged" }, requireAll = false)
    public static void setOnScroll(android.widget.AbsListView view, final android.databinding.adapters.AbsListViewBindingAdapter.OnScroll scrollListener, final android.databinding.adapters.AbsListViewBindingAdapter.OnScrollStateChanged scrollStateListener) {
        view.setOnScrollListener(new android.widget.AbsListView.OnScrollListener() {
            @java.lang.Override
            public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
                if (scrollStateListener != null) {
                    scrollStateListener.onScrollStateChanged(view, scrollState);
                }
            }

            @java.lang.Override
            public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollListener != null) {
                    scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
    }

    public interface OnScroll {
        void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public interface OnScrollStateChanged {
        void onScrollStateChanged(android.widget.AbsListView view, int scrollState);
    }
}

