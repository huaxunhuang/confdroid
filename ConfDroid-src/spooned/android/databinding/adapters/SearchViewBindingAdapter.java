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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.SearchView.class, attribute = "android:onQueryTextFocusChange", method = "setOnQueryTextFocusChangeListener"), @android.databinding.BindingMethod(type = android.widget.SearchView.class, attribute = "android:onSearchClick", method = "setOnSearchClickListener"), @android.databinding.BindingMethod(type = android.widget.SearchView.class, attribute = "android:onClose", method = "setOnCloseListener") })
public class SearchViewBindingAdapter {
    @android.databinding.BindingAdapter(value = { "android:onQueryTextSubmit", "android:onQueryTextChange" }, requireAll = false)
    public static void setOnQueryTextListener(android.widget.SearchView view, final android.databinding.adapters.SearchViewBindingAdapter.OnQueryTextSubmit submit, final android.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange change) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if ((submit == null) && (change == null)) {
                view.setOnQueryTextListener(null);
            } else {
                view.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                    @java.lang.Override
                    public boolean onQueryTextSubmit(java.lang.String query) {
                        if (submit != null) {
                            return submit.onQueryTextSubmit(query);
                        } else {
                            return false;
                        }
                    }

                    @java.lang.Override
                    public boolean onQueryTextChange(java.lang.String newText) {
                        if (change != null) {
                            return change.onQueryTextChange(newText);
                        } else {
                            return false;
                        }
                    }
                });
            }
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onSuggestionSelect", "android:onSuggestionClick" }, requireAll = false)
    public static void setOnSuggestListener(android.widget.SearchView view, final android.databinding.adapters.SearchViewBindingAdapter.OnSuggestionSelect submit, final android.databinding.adapters.SearchViewBindingAdapter.OnSuggestionClick change) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if ((submit == null) && (change == null)) {
                view.setOnSuggestionListener(null);
            } else {
                view.setOnSuggestionListener(new android.widget.SearchView.OnSuggestionListener() {
                    @java.lang.Override
                    public boolean onSuggestionSelect(int position) {
                        if (submit != null) {
                            return submit.onSuggestionSelect(position);
                        } else {
                            return false;
                        }
                    }

                    @java.lang.Override
                    public boolean onSuggestionClick(int position) {
                        if (change != null) {
                            return change.onSuggestionClick(position);
                        } else {
                            return false;
                        }
                    }
                });
            }
        }
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public interface OnQueryTextSubmit {
        boolean onQueryTextSubmit(java.lang.String query);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public interface OnQueryTextChange {
        boolean onQueryTextChange(java.lang.String newText);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public interface OnSuggestionSelect {
        boolean onSuggestionSelect(int position);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public interface OnSuggestionClick {
        boolean onSuggestionClick(int position);
    }
}

