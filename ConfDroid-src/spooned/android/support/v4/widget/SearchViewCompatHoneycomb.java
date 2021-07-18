/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * Implementation of SearchView compatibility that can call Honeycomb APIs.
 */
class SearchViewCompatHoneycomb {
    public static void checkIfLegalArg(android.view.View searchView) {
        if (searchView == null) {
            throw new java.lang.IllegalArgumentException("searchView must be non-null");
        }
        if (!(searchView instanceof android.widget.SearchView)) {
            throw new java.lang.IllegalArgumentException("searchView must be an instance of" + "android.widget.SearchView");
        }
    }

    interface OnQueryTextListenerCompatBridge {
        boolean onQueryTextSubmit(java.lang.String query);

        boolean onQueryTextChange(java.lang.String newText);
    }

    interface OnCloseListenerCompatBridge {
        boolean onClose();
    }

    public static android.view.View newSearchView(android.content.Context context) {
        return new android.widget.SearchView(context);
    }

    public static void setSearchableInfo(android.view.View searchView, android.content.ComponentName searchableComponent) {
        android.widget.SearchView sv = ((android.widget.SearchView) (searchView));
        android.app.SearchManager searchManager = ((android.app.SearchManager) (sv.getContext().getSystemService(android.content.Context.SEARCH_SERVICE)));
        sv.setSearchableInfo(searchManager.getSearchableInfo(searchableComponent));
    }

    public static java.lang.Object newOnQueryTextListener(final android.support.v4.widget.SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge listener) {
        return new android.widget.SearchView.OnQueryTextListener() {
            @java.lang.Override
            public boolean onQueryTextSubmit(java.lang.String query) {
                return listener.onQueryTextSubmit(query);
            }

            @java.lang.Override
            public boolean onQueryTextChange(java.lang.String newText) {
                return listener.onQueryTextChange(newText);
            }
        };
    }

    public static void setOnQueryTextListener(android.view.View searchView, java.lang.Object listener) {
        ((android.widget.SearchView) (searchView)).setOnQueryTextListener(((android.widget.SearchView.OnQueryTextListener) (listener)));
    }

    public static java.lang.Object newOnCloseListener(final android.support.v4.widget.SearchViewCompatHoneycomb.OnCloseListenerCompatBridge listener) {
        return new android.widget.SearchView.OnCloseListener() {
            @java.lang.Override
            public boolean onClose() {
                return listener.onClose();
            }
        };
    }

    public static void setOnCloseListener(android.view.View searchView, java.lang.Object listener) {
        ((android.widget.SearchView) (searchView)).setOnCloseListener(((android.widget.SearchView.OnCloseListener) (listener)));
    }

    public static java.lang.CharSequence getQuery(android.view.View searchView) {
        return ((android.widget.SearchView) (searchView)).getQuery();
    }

    public static void setQuery(android.view.View searchView, java.lang.CharSequence query, boolean submit) {
        ((android.widget.SearchView) (searchView)).setQuery(query, submit);
    }

    public static void setQueryHint(android.view.View searchView, java.lang.CharSequence hint) {
        ((android.widget.SearchView) (searchView)).setQueryHint(hint);
    }

    public static void setIconified(android.view.View searchView, boolean iconify) {
        ((android.widget.SearchView) (searchView)).setIconified(iconify);
    }

    public static boolean isIconified(android.view.View searchView) {
        return ((android.widget.SearchView) (searchView)).isIconified();
    }

    public static void setSubmitButtonEnabled(android.view.View searchView, boolean enabled) {
        ((android.widget.SearchView) (searchView)).setSubmitButtonEnabled(enabled);
    }

    public static boolean isSubmitButtonEnabled(android.view.View searchView) {
        return ((android.widget.SearchView) (searchView)).isSubmitButtonEnabled();
    }

    public static void setQueryRefinementEnabled(android.view.View searchView, boolean enable) {
        ((android.widget.SearchView) (searchView)).setQueryRefinementEnabled(enable);
    }

    public static boolean isQueryRefinementEnabled(android.view.View searchView) {
        return ((android.widget.SearchView) (searchView)).isQueryRefinementEnabled();
    }

    public static void setMaxWidth(android.view.View searchView, int maxpixels) {
        ((android.widget.SearchView) (searchView)).setMaxWidth(maxpixels);
    }
}

