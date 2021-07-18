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
 * Helper for accessing features in {@link android.widget.SearchView}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class SearchViewCompat {
    interface SearchViewCompatImpl {
        android.view.View newSearchView(android.content.Context context);

        void setSearchableInfo(android.view.View searchView, android.content.ComponentName searchableComponent);

        void setImeOptions(android.view.View searchView, int imeOptions);

        void setInputType(android.view.View searchView, int inputType);

        java.lang.Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener);

        void setOnQueryTextListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener);

        java.lang.Object newOnCloseListener(android.support.v4.widget.SearchViewCompat.OnCloseListener listener);

        void setOnCloseListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnCloseListener listener);

        java.lang.CharSequence getQuery(android.view.View searchView);

        void setQuery(android.view.View searchView, java.lang.CharSequence query, boolean submit);

        void setQueryHint(android.view.View searchView, java.lang.CharSequence hint);

        void setIconified(android.view.View searchView, boolean iconify);

        boolean isIconified(android.view.View searchView);

        void setSubmitButtonEnabled(android.view.View searchView, boolean enabled);

        boolean isSubmitButtonEnabled(android.view.View searchView);

        void setQueryRefinementEnabled(android.view.View searchView, boolean enable);

        boolean isQueryRefinementEnabled(android.view.View searchView);

        void setMaxWidth(android.view.View searchView, int maxpixels);
    }

    static class SearchViewCompatStubImpl implements android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl {
        @java.lang.Override
        public android.view.View newSearchView(android.content.Context context) {
            return null;
        }

        @java.lang.Override
        public void setSearchableInfo(android.view.View searchView, android.content.ComponentName searchableComponent) {
        }

        @java.lang.Override
        public void setImeOptions(android.view.View searchView, int imeOptions) {
        }

        @java.lang.Override
        public void setInputType(android.view.View searchView, int inputType) {
        }

        @java.lang.Override
        public java.lang.Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener) {
            return null;
        }

        @java.lang.Override
        public void setOnQueryTextListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener) {
        }

        @java.lang.Override
        public java.lang.Object newOnCloseListener(android.support.v4.widget.SearchViewCompat.OnCloseListener listener) {
            return null;
        }

        @java.lang.Override
        public void setOnCloseListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnCloseListener listener) {
        }

        @java.lang.Override
        public java.lang.CharSequence getQuery(android.view.View searchView) {
            return null;
        }

        @java.lang.Override
        public void setQuery(android.view.View searchView, java.lang.CharSequence query, boolean submit) {
        }

        @java.lang.Override
        public void setQueryHint(android.view.View searchView, java.lang.CharSequence hint) {
        }

        @java.lang.Override
        public void setIconified(android.view.View searchView, boolean iconify) {
        }

        @java.lang.Override
        public boolean isIconified(android.view.View searchView) {
            return true;
        }

        @java.lang.Override
        public void setSubmitButtonEnabled(android.view.View searchView, boolean enabled) {
        }

        @java.lang.Override
        public boolean isSubmitButtonEnabled(android.view.View searchView) {
            return false;
        }

        @java.lang.Override
        public void setQueryRefinementEnabled(android.view.View searchView, boolean enable) {
        }

        @java.lang.Override
        public boolean isQueryRefinementEnabled(android.view.View searchView) {
            return false;
        }

        @java.lang.Override
        public void setMaxWidth(android.view.View searchView, int maxpixels) {
        }
    }

    static class SearchViewCompatHoneycombImpl extends android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl {
        @java.lang.Override
        public android.view.View newSearchView(android.content.Context context) {
            return android.support.v4.widget.SearchViewCompatHoneycomb.newSearchView(context);
        }

        @java.lang.Override
        public void setSearchableInfo(android.view.View searchView, android.content.ComponentName searchableComponent) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setSearchableInfo(searchView, searchableComponent);
        }

        @java.lang.Override
        public java.lang.Object newOnQueryTextListener(final android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener) {
            return android.support.v4.widget.SearchViewCompatHoneycomb.newOnQueryTextListener(new android.support.v4.widget.SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge() {
                @java.lang.Override
                public boolean onQueryTextSubmit(java.lang.String query) {
                    return listener.onQueryTextSubmit(query);
                }

                @java.lang.Override
                public boolean onQueryTextChange(java.lang.String newText) {
                    return listener.onQueryTextChange(newText);
                }
            });
        }

        @java.lang.Override
        public void setOnQueryTextListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setOnQueryTextListener(searchView, newOnQueryTextListener(listener));
        }

        @java.lang.Override
        public java.lang.Object newOnCloseListener(final android.support.v4.widget.SearchViewCompat.OnCloseListener listener) {
            return android.support.v4.widget.SearchViewCompatHoneycomb.newOnCloseListener(new android.support.v4.widget.SearchViewCompatHoneycomb.OnCloseListenerCompatBridge() {
                @java.lang.Override
                public boolean onClose() {
                    return listener.onClose();
                }
            });
        }

        @java.lang.Override
        public void setOnCloseListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnCloseListener listener) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setOnCloseListener(searchView, newOnCloseListener(listener));
        }

        @java.lang.Override
        public java.lang.CharSequence getQuery(android.view.View searchView) {
            checkIfLegalArg(searchView);
            return android.support.v4.widget.SearchViewCompatHoneycomb.getQuery(searchView);
        }

        @java.lang.Override
        public void setQuery(android.view.View searchView, java.lang.CharSequence query, boolean submit) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setQuery(searchView, query, submit);
        }

        @java.lang.Override
        public void setQueryHint(android.view.View searchView, java.lang.CharSequence hint) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setQueryHint(searchView, hint);
        }

        @java.lang.Override
        public void setIconified(android.view.View searchView, boolean iconify) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setIconified(searchView, iconify);
        }

        @java.lang.Override
        public boolean isIconified(android.view.View searchView) {
            checkIfLegalArg(searchView);
            return android.support.v4.widget.SearchViewCompatHoneycomb.isIconified(searchView);
        }

        @java.lang.Override
        public void setSubmitButtonEnabled(android.view.View searchView, boolean enabled) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setSubmitButtonEnabled(searchView, enabled);
        }

        @java.lang.Override
        public boolean isSubmitButtonEnabled(android.view.View searchView) {
            checkIfLegalArg(searchView);
            return android.support.v4.widget.SearchViewCompatHoneycomb.isSubmitButtonEnabled(searchView);
        }

        @java.lang.Override
        public void setQueryRefinementEnabled(android.view.View searchView, boolean enable) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setQueryRefinementEnabled(searchView, enable);
        }

        @java.lang.Override
        public boolean isQueryRefinementEnabled(android.view.View searchView) {
            checkIfLegalArg(searchView);
            return android.support.v4.widget.SearchViewCompatHoneycomb.isQueryRefinementEnabled(searchView);
        }

        @java.lang.Override
        public void setMaxWidth(android.view.View searchView, int maxpixels) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatHoneycomb.setMaxWidth(searchView, maxpixels);
        }

        protected void checkIfLegalArg(android.view.View searchView) {
            android.support.v4.widget.SearchViewCompatHoneycomb.checkIfLegalArg(searchView);
        }
    }

    static class SearchViewCompatIcsImpl extends android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl {
        @java.lang.Override
        public android.view.View newSearchView(android.content.Context context) {
            return android.support.v4.widget.SearchViewCompatIcs.newSearchView(context);
        }

        @java.lang.Override
        public void setImeOptions(android.view.View searchView, int imeOptions) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatIcs.setImeOptions(searchView, imeOptions);
        }

        @java.lang.Override
        public void setInputType(android.view.View searchView, int inputType) {
            checkIfLegalArg(searchView);
            android.support.v4.widget.SearchViewCompatIcs.setInputType(searchView, inputType);
        }
    }

    private static final android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            // ICS
            IMPL = new android.support.v4.widget.SearchViewCompat.SearchViewCompatIcsImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                // Honeycomb
                IMPL = new android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl();
            } else {
                IMPL = new android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl();
            }

    }

    private SearchViewCompat(android.content.Context context) {
        /* Hide constructor */
    }

    /**
     * Creates a new SearchView.
     *
     * @param context
     * 		The Context the view is running in.
     * @return A SearchView instance if the class is present on the current
    platform, null otherwise.
     */
    public static android.view.View newSearchView(android.content.Context context) {
        return android.support.v4.widget.SearchViewCompat.IMPL.newSearchView(context);
    }

    /**
     * Sets the SearchableInfo for this SearchView. Properties in the SearchableInfo are used
     * to display labels, hints, suggestions, create intents for launching search results screens
     * and controlling other affordances such as a voice button.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param searchableComponent
     * 		The application component whose
     * 		{@link android.app.SearchableInfo} should be loaded and applied to
     * 		the SearchView.
     */
    public static void setSearchableInfo(android.view.View searchView, android.content.ComponentName searchableComponent) {
        android.support.v4.widget.SearchViewCompat.IMPL.setSearchableInfo(searchView, searchableComponent);
    }

    /**
     * Sets the IME options on the query text field.  This is a no-op if
     * called on pre-{@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH}
     * platforms.
     *
     * @see TextView#setImeOptions(int)
     * @param searchView
     * 		The SearchView to operate on.
     * @param imeOptions
     * 		the options to set on the query text field
     */
    public static void setImeOptions(android.view.View searchView, int imeOptions) {
        android.support.v4.widget.SearchViewCompat.IMPL.setImeOptions(searchView, imeOptions);
    }

    /**
     * Sets the input type on the query text field.  This is a no-op if
     * called on pre-{@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH}
     * platforms.
     *
     * @see TextView#setInputType(int)
     * @param searchView
     * 		The SearchView to operate on.
     * @param inputType
     * 		the input type to set on the query text field
     */
    public static void setInputType(android.view.View searchView, int inputType) {
        android.support.v4.widget.SearchViewCompat.IMPL.setInputType(searchView, inputType);
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param searchView
     * 		The SearchView in which to register the listener.
     * @param listener
     * 		the listener object that receives callbacks when the user performs
     * 		actions in the SearchView such as clicking on buttons or typing a query.
     */
    public static void setOnQueryTextListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnQueryTextListener listener) {
        android.support.v4.widget.SearchViewCompat.IMPL.setOnQueryTextListener(searchView, listener);
    }

    /**
     *
     *
     * @deprecated Use {@link OnQueryTextListener} instead.
     */
    @java.lang.Deprecated
    public static abstract class OnQueryTextListenerCompat implements android.support.v4.widget.SearchViewCompat.OnQueryTextListener {
        @java.lang.Override
        public boolean onQueryTextSubmit(java.lang.String query) {
            return false;
        }

        @java.lang.Override
        public boolean onQueryTextChange(java.lang.String newText) {
            return false;
        }
    }

    /**
     * Callbacks for changes to the query text.
     */
    public interface OnQueryTextListener {
        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query
         * 		the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
        SearchView perform the default action.
         */
        boolean onQueryTextSubmit(java.lang.String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText
         * 		the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
        suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(java.lang.String newText);
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param searchView
     * 		The SearchView in which to register the listener.
     * @param listener
     * 		the listener to call when the user closes the SearchView.
     */
    public static void setOnCloseListener(android.view.View searchView, android.support.v4.widget.SearchViewCompat.OnCloseListener listener) {
        android.support.v4.widget.SearchViewCompat.IMPL.setOnCloseListener(searchView, listener);
    }

    /**
     *
     *
     * @deprecated Use {@link OnCloseListener} instead.
     */
    @java.lang.Deprecated
    public static abstract class OnCloseListenerCompat implements android.support.v4.widget.SearchViewCompat.OnCloseListener {
        @java.lang.Override
        public boolean onClose() {
            return false;
        }
    }

    /**
     * Callback for closing the query UI.
     */
    public interface OnCloseListener {
        /**
         * The user is attempting to close the SearchView.
         *
         * @return true if the listener wants to override the default behavior of clearing the
        text field and dismissing it, false otherwise.
         */
        boolean onClose();
    }

    /**
     * Returns the query string currently in the text field.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @return the query string
     */
    public static java.lang.CharSequence getQuery(android.view.View searchView) {
        return android.support.v4.widget.SearchViewCompat.IMPL.getQuery(searchView);
    }

    /**
     * Sets a query string in the text field and optionally submits the query as well.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param query
     * 		the query string. This replaces any query text already present in the
     * 		text field.
     * @param submit
     * 		whether to submit the query right now or only update the contents of
     * 		text field.
     */
    public static void setQuery(android.view.View searchView, java.lang.CharSequence query, boolean submit) {
        android.support.v4.widget.SearchViewCompat.IMPL.setQuery(searchView, query, submit);
    }

    /**
     * Sets the hint text to display in the query text field. This overrides any hint specified
     * in the SearchableInfo.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param hint
     * 		the hint text to display
     */
    public static void setQueryHint(android.view.View searchView, java.lang.CharSequence hint) {
        android.support.v4.widget.SearchViewCompat.IMPL.setQueryHint(searchView, hint);
    }

    /**
     * Iconifies or expands the SearchView. Any query text is cleared when iconified. This is
     * a temporary state and does not override the default iconified state set by
     * setIconifiedByDefault(boolean). If the default state is iconified, then
     * a false here will only be valid until the user closes the field. And if the default
     * state is expanded, then a true here will only clear the text field and not close it.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param iconify
     * 		a true value will collapse the SearchView to an icon, while a false will
     * 		expand it.
     */
    public static void setIconified(android.view.View searchView, boolean iconify) {
        android.support.v4.widget.SearchViewCompat.IMPL.setIconified(searchView, iconify);
    }

    /**
     * Returns the current iconified state of the SearchView.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @return true if the SearchView is currently iconified, false if the search field is
    fully visible.
     */
    public static boolean isIconified(android.view.View searchView) {
        return android.support.v4.widget.SearchViewCompat.IMPL.isIconified(searchView);
    }

    /**
     * Enables showing a submit button when the query is non-empty. In cases where the SearchView
     * is being used to filter the contents of the current activity and doesn't launch a separate
     * results activity, then the submit button should be disabled.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param enabled
     * 		true to show a submit button for submitting queries, false if a submit
     * 		button is not required.
     */
    public static void setSubmitButtonEnabled(android.view.View searchView, boolean enabled) {
        android.support.v4.widget.SearchViewCompat.IMPL.setSubmitButtonEnabled(searchView, enabled);
    }

    /**
     * Returns whether the submit button is enabled when necessary or never displayed.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @return whether the submit button is enabled automatically when necessary
     */
    public static boolean isSubmitButtonEnabled(android.view.View searchView) {
        return android.support.v4.widget.SearchViewCompat.IMPL.isSubmitButtonEnabled(searchView);
    }

    /**
     * Specifies if a query refinement button should be displayed alongside each suggestion
     * or if it should depend on the flags set in the individual items retrieved from the
     * suggestions provider. Clicking on the query refinement button will replace the text
     * in the query text field with the text from the suggestion. This flag only takes effect
     * if a SearchableInfo has been specified with {@link #setSearchableInfo(View, ComponentName)}
     * and not when using a custom adapter.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @param enable
     * 		true if all items should have a query refinement button, false if only
     * 		those items that have a query refinement flag set should have the button.
     * @see SearchManager#SUGGEST_COLUMN_FLAGS
     * @see SearchManager#FLAG_QUERY_REFINEMENT
     */
    public static void setQueryRefinementEnabled(android.view.View searchView, boolean enable) {
        android.support.v4.widget.SearchViewCompat.IMPL.setQueryRefinementEnabled(searchView, enable);
    }

    /**
     * Returns whether query refinement is enabled for all items or only specific ones.
     *
     * @param searchView
     * 		The SearchView to operate on.
     * @return true if enabled for all items, false otherwise.
     */
    public static boolean isQueryRefinementEnabled(android.view.View searchView) {
        return android.support.v4.widget.SearchViewCompat.IMPL.isQueryRefinementEnabled(searchView);
    }

    /**
     * Makes the view at most this many pixels wide
     *
     * @param searchView
     * 		The SearchView to operate on.
     */
    public static void setMaxWidth(android.view.View searchView, int maxpixels) {
        android.support.v4.widget.SearchViewCompat.IMPL.setMaxWidth(searchView, maxpixels);
    }
}

