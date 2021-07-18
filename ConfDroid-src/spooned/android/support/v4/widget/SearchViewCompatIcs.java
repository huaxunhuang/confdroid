/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Implementation of SearchView compatibility that can call ICS APIs.
 */
class SearchViewCompatIcs {
    public static class MySearchView extends android.widget.SearchView {
        public MySearchView(android.content.Context context) {
            super(context);
        }

        // The normal SearchView doesn't clear its search text when
        // collapsed, so we will do this for it.
        @java.lang.Override
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }

    public static android.view.View newSearchView(android.content.Context context) {
        return new android.support.v4.widget.SearchViewCompatIcs.MySearchView(context);
    }

    public static void setImeOptions(android.view.View searchView, int imeOptions) {
        ((android.widget.SearchView) (searchView)).setImeOptions(imeOptions);
    }

    public static void setInputType(android.view.View searchView, int inputType) {
        ((android.widget.SearchView) (searchView)).setInputType(inputType);
    }
}

