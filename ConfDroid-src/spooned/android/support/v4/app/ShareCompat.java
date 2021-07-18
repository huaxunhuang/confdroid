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
package android.support.v4.app;


/**
 * Extra helper functionality for sharing data between activities.
 *
 * ShareCompat provides functionality to extend the {@link Intent#ACTION_SEND}/
 * {@link Intent#ACTION_SEND_MULTIPLE} protocol and support retrieving more info
 * about the activity that invoked a social sharing action.
 *
 * {@link IntentBuilder} provides helper functions for constructing a sharing
 * intent that always includes data about the calling activity and app.
 * This lets the called activity provide attribution for the app that shared
 * content. Constructing an intent this way can be done in a method-chaining style.
 * To obtain an IntentBuilder with info about your calling activity, use the static
 * method {@link IntentBuilder#from(Activity)}.
 *
 * {@link IntentReader} provides helper functions for parsing the defined extras
 * within an {@link Intent#ACTION_SEND} or {@link Intent#ACTION_SEND_MULTIPLE} intent
 * used to launch an activity. You can also obtain a Drawable for the caller's
 * application icon and the application's localized label (the app's human-readable name).
 * Social apps that enable sharing content are encouraged to use this information
 * to call out the app that the content was shared from.
 */
public final class ShareCompat {
    /**
     * Intent extra that stores the name of the calling package for an ACTION_SEND intent.
     * When an activity is started using startActivityForResult this is redundant info.
     * (It is also provided by {@link Activity#getCallingPackage()}.)
     *
     * Instead of using this constant directly, consider using {@link #getCallingPackage(Activity)}
     * or {@link IntentReader#getCallingPackage()}.
     */
    public static final java.lang.String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";

    /**
     * Intent extra that stores the {@link ComponentName} of the calling activity for
     * an ACTION_SEND intent.
     */
    public static final java.lang.String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";

    /**
     * Compatibility shims for sharing operations
     */
    interface ShareCompatImpl {
        void configureMenuItem(android.view.MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent);

        java.lang.String escapeHtml(java.lang.CharSequence text);
    }

    static class ShareCompatImplBase implements android.support.v4.app.ShareCompat.ShareCompatImpl {
        @java.lang.Override
        public void configureMenuItem(android.view.MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
            item.setIntent(shareIntent.createChooserIntent());
        }

        @java.lang.Override
        public java.lang.String escapeHtml(java.lang.CharSequence text) {
            java.lang.StringBuilder out = new java.lang.StringBuilder();
            android.support.v4.app.ShareCompat.ShareCompatImplBase.withinStyle(out, text, 0, text.length());
            return out.toString();
        }

        private static void withinStyle(java.lang.StringBuilder out, java.lang.CharSequence text, int start, int end) {
            for (int i = start; i < end; i++) {
                char c = text.charAt(i);
                if (c == '<') {
                    out.append("&lt;");
                } else
                    if (c == '>') {
                        out.append("&gt;");
                    } else
                        if (c == '&') {
                            out.append("&amp;");
                        } else
                            if ((c > 0x7e) || (c < ' ')) {
                                out.append(("&#" + ((int) (c))) + ";");
                            } else
                                if (c == ' ') {
                                    while (((i + 1) < end) && (text.charAt(i + 1) == ' ')) {
                                        out.append("&nbsp;");
                                        i++;
                                    } 
                                    out.append(' ');
                                } else {
                                    out.append(c);
                                }




            }
        }
    }

    static class ShareCompatImplICS extends android.support.v4.app.ShareCompat.ShareCompatImplBase {
        @java.lang.Override
        public void configureMenuItem(android.view.MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
            android.support.v4.app.ShareCompatICS.configureMenuItem(item, shareIntent.getActivity(), shareIntent.getIntent());
            if (shouldAddChooserIntent(item)) {
                item.setIntent(shareIntent.createChooserIntent());
            }
        }

        boolean shouldAddChooserIntent(android.view.MenuItem item) {
            return !item.hasSubMenu();
        }
    }

    static class ShareCompatImplJB extends android.support.v4.app.ShareCompat.ShareCompatImplICS {
        @java.lang.Override
        public java.lang.String escapeHtml(java.lang.CharSequence html) {
            return android.support.v4.app.ShareCompatJB.escapeHtml(html);
        }

        @java.lang.Override
        boolean shouldAddChooserIntent(android.view.MenuItem item) {
            return false;
        }
    }

    static android.support.v4.app.ShareCompat.ShareCompatImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            android.support.v4.app.ShareCompat.IMPL = new android.support.v4.app.ShareCompat.ShareCompatImplJB();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                android.support.v4.app.ShareCompat.IMPL = new android.support.v4.app.ShareCompat.ShareCompatImplICS();
            } else {
                android.support.v4.app.ShareCompat.IMPL = new android.support.v4.app.ShareCompat.ShareCompatImplBase();
            }

    }

    private ShareCompat() {
    }

    /**
     * Retrieve the name of the package that launched calledActivity from a share intent.
     * Apps that provide social sharing functionality can use this to provide attribution
     * for the app that shared the content.
     *
     * <p><em>Note:</em> This data may have been provided voluntarily by the calling
     * application. As such it should not be trusted for accuracy in the context of
     * security or verification.</p>
     *
     * @param calledActivity
     * 		Current activity that was launched to share content
     * @return Name of the calling package
     */
    public static java.lang.String getCallingPackage(android.app.Activity calledActivity) {
        java.lang.String result = calledActivity.getCallingPackage();
        if (result == null) {
            result = calledActivity.getIntent().getStringExtra(android.support.v4.app.ShareCompat.EXTRA_CALLING_PACKAGE);
        }
        return result;
    }

    /**
     * Retrieve the ComponentName of the activity that launched calledActivity from a share intent.
     * Apps that provide social sharing functionality can use this to provide attribution
     * for the app that shared the content.
     *
     * <p><em>Note:</em> This data may have been provided voluntarily by the calling
     * application. As such it should not be trusted for accuracy in the context of
     * security or verification.</p>
     *
     * @param calledActivity
     * 		Current activity that was launched to share content
     * @return ComponentName of the calling activity
     */
    public static android.content.ComponentName getCallingActivity(android.app.Activity calledActivity) {
        android.content.ComponentName result = calledActivity.getCallingActivity();
        if (result == null) {
            result = calledActivity.getIntent().getParcelableExtra(android.support.v4.app.ShareCompat.EXTRA_CALLING_ACTIVITY);
        }
        return result;
    }

    /**
     * Configure a {@link MenuItem} to act as a sharing action.
     *
     * <p>If the app is running on API level 14 or higher (Android 4.0/Ice Cream Sandwich)
     * this method will configure a ShareActionProvider to provide a more robust UI
     * for selecting the target of the share. History will be tracked for each calling
     * activity in a file named with the prefix ".sharecompat_" in the application's
     * private data directory. If the application wishes to set this MenuItem to show
     * as an action in the Action Bar it should use
     * {@link MenuItemCompat#setShowAsAction(MenuItem, int)} to request that behavior
     * in addition to calling this method.</p>
     *
     * <p>If the app is running on an older platform version this method will configure
     * a standard activity chooser dialog for the menu item.</p>
     *
     * <p>During the calling activity's lifecycle, if data within the share intent must
     * change the app should change that state in one of several ways:</p>
     * <ul>
     * <li>Call {@link ActivityCompat#invalidateOptionsMenu(Activity)}. If the app is running
     * on API level 11 or above and uses the Action Bar its menu will be recreated and rebuilt.
     * If not, the activity will receive a call to {@link Activity#onPrepareOptionsMenu(Menu)}
     * the next time the user presses the menu key to open the options menu panel. The activity
     * can then call configureMenuItem again with a new or altered IntentBuilder to reconfigure
     * the share menu item.</li>
     * <li>Keep a reference to the MenuItem object for the share item once it has been created
     * and call configureMenuItem to update the associated sharing intent as needed.</li>
     * </ul>
     *
     * @param item
     * 		MenuItem to configure for sharing
     * @param shareIntent
     * 		IntentBuilder with data about the content to share
     */
    public static void configureMenuItem(android.view.MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
        android.support.v4.app.ShareCompat.IMPL.configureMenuItem(item, shareIntent);
    }

    /**
     * Configure a menu item to act as a sharing action.
     *
     * @param menu
     * 		Menu containing the item to use for sharing
     * @param menuItemId
     * 		ID of the share item within menu
     * @param shareIntent
     * 		IntentBuilder with data about the content to share
     * @see #configureMenuItem(MenuItem, IntentBuilder)
     */
    public static void configureMenuItem(android.view.Menu menu, int menuItemId, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
        android.view.MenuItem item = menu.findItem(menuItemId);
        if (item == null) {
            throw new java.lang.IllegalArgumentException(("Could not find menu item with id " + menuItemId) + " in the supplied menu");
        }
        android.support.v4.app.ShareCompat.configureMenuItem(item, shareIntent);
    }

    /**
     * IntentBuilder is a helper for constructing {@link Intent#ACTION_SEND} and
     * {@link Intent#ACTION_SEND_MULTIPLE} sharing intents and starting activities
     * to share content. The ComponentName and package name of the calling activity
     * will be included.
     */
    public static class IntentBuilder {
        private android.app.Activity mActivity;

        private android.content.Intent mIntent;

        private java.lang.CharSequence mChooserTitle;

        private java.util.ArrayList<java.lang.String> mToAddresses;

        private java.util.ArrayList<java.lang.String> mCcAddresses;

        private java.util.ArrayList<java.lang.String> mBccAddresses;

        private java.util.ArrayList<android.net.Uri> mStreams;

        /**
         * Create a new IntentBuilder for launching a sharing action from launchingActivity.
         *
         * @param launchingActivity
         * 		Activity that the share will be launched from
         * @return a new IntentBuilder instance
         */
        public static android.support.v4.app.ShareCompat.IntentBuilder from(android.app.Activity launchingActivity) {
            return new android.support.v4.app.ShareCompat.IntentBuilder(launchingActivity);
        }

        private IntentBuilder(android.app.Activity launchingActivity) {
            mActivity = launchingActivity;
            mIntent = new android.content.Intent().setAction(android.content.Intent.ACTION_SEND);
            mIntent.putExtra(android.support.v4.app.ShareCompat.EXTRA_CALLING_PACKAGE, launchingActivity.getPackageName());
            mIntent.putExtra(android.support.v4.app.ShareCompat.EXTRA_CALLING_ACTIVITY, launchingActivity.getComponentName());
            mIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        /**
         * Retrieve the Intent as configured so far by the IntentBuilder. This Intent
         * is suitable for use in a ShareActionProvider or chooser dialog.
         *
         * <p>To create an intent that will launch the activity chooser so that the user
         * may select a target for the share, see {@link #createChooserIntent()}.
         *
         * @return The current Intent being configured by this builder
         */
        public android.content.Intent getIntent() {
            if (mToAddresses != null) {
                combineArrayExtra(android.content.Intent.EXTRA_EMAIL, mToAddresses);
                mToAddresses = null;
            }
            if (mCcAddresses != null) {
                combineArrayExtra(android.content.Intent.EXTRA_CC, mCcAddresses);
                mCcAddresses = null;
            }
            if (mBccAddresses != null) {
                combineArrayExtra(android.content.Intent.EXTRA_BCC, mBccAddresses);
                mBccAddresses = null;
            }
            // Check if we need to change the action.
            boolean needsSendMultiple = (mStreams != null) && (mStreams.size() > 1);
            boolean isSendMultiple = mIntent.getAction().equals(android.content.Intent.ACTION_SEND_MULTIPLE);
            if ((!needsSendMultiple) && isSendMultiple) {
                // Change back to a single send action; place the first stream into the
                // intent for single sharing.
                mIntent.setAction(android.content.Intent.ACTION_SEND);
                if ((mStreams != null) && (!mStreams.isEmpty())) {
                    mIntent.putExtra(android.content.Intent.EXTRA_STREAM, mStreams.get(0));
                } else {
                    mIntent.removeExtra(android.content.Intent.EXTRA_STREAM);
                }
                mStreams = null;
            }
            if (needsSendMultiple && (!isSendMultiple)) {
                // Change to a multiple send action; place the relevant ArrayList into the
                // intent for multiple sharing.
                mIntent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
                if ((mStreams != null) && (!mStreams.isEmpty())) {
                    mIntent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, mStreams);
                } else {
                    mIntent.removeExtra(android.content.Intent.EXTRA_STREAM);
                }
            }
            return mIntent;
        }

        android.app.Activity getActivity() {
            return mActivity;
        }

        private void combineArrayExtra(java.lang.String extra, java.util.ArrayList<java.lang.String> add) {
            java.lang.String[] currentAddresses = mIntent.getStringArrayExtra(extra);
            int currentLength = (currentAddresses != null) ? currentAddresses.length : 0;
            java.lang.String[] finalAddresses = new java.lang.String[currentLength + add.size()];
            add.toArray(finalAddresses);
            if (currentAddresses != null) {
                java.lang.System.arraycopy(currentAddresses, 0, finalAddresses, add.size(), currentLength);
            }
            mIntent.putExtra(extra, finalAddresses);
        }

        private void combineArrayExtra(java.lang.String extra, java.lang.String[] add) {
            // Add any items still pending
            android.content.Intent intent = getIntent();
            java.lang.String[] old = intent.getStringArrayExtra(extra);
            int oldLength = (old != null) ? old.length : 0;
            java.lang.String[] result = new java.lang.String[oldLength + add.length];
            if (old != null)
                java.lang.System.arraycopy(old, 0, result, 0, oldLength);

            java.lang.System.arraycopy(add, 0, result, oldLength, add.length);
            intent.putExtra(extra, result);
        }

        /**
         * Create an Intent that will launch the standard Android activity chooser,
         * allowing the user to pick what activity/app on the system should handle
         * the share.
         *
         * @return A chooser Intent for the currently configured sharing action
         */
        public android.content.Intent createChooserIntent() {
            return android.content.Intent.createChooser(getIntent(), mChooserTitle);
        }

        /**
         * Start a chooser activity for the current share intent.
         *
         * <p>Note that under most circumstances you should use
         * {@link ShareCompat#configureMenuItem(MenuItem, IntentBuilder)
         *  ShareCompat.configureMenuItem()} to add a Share item to the menu while
         * presenting a detail view of the content to be shared instead
         * of invoking this directly.</p>
         */
        public void startChooser() {
            mActivity.startActivity(createChooserIntent());
        }

        /**
         * Set the title that will be used for the activity chooser for this share.
         *
         * @param title
         * 		Title string
         * @return This IntentBuilder for method chaining
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setChooserTitle(java.lang.CharSequence title) {
            mChooserTitle = title;
            return this;
        }

        /**
         * Set the title that will be used for the activity chooser for this share.
         *
         * @param resId
         * 		Resource ID of the title string to use
         * @return This IntentBuilder for method chaining
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setChooserTitle(@android.support.annotation.StringRes
        int resId) {
            return setChooserTitle(mActivity.getText(resId));
        }

        /**
         * Set the type of data being shared
         *
         * @param mimeType
         * 		mimetype of the shared data
         * @return This IntentBuilder for method chaining
         * @see Intent#setType(String)
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setType(java.lang.String mimeType) {
            mIntent.setType(mimeType);
            return this;
        }

        /**
         * Set the literal text data to be sent as part of the share.
         * This may be a styled CharSequence.
         *
         * @param text
         * 		Text to share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_TEXT
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setText(java.lang.CharSequence text) {
            mIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            return this;
        }

        /**
         * Set an HTML string to be sent as part of the share.
         * If {@link Intent#EXTRA_TEXT EXTRA_TEXT} has not already been supplied,
         * a styled version of the supplied HTML text will be added as EXTRA_TEXT as
         * parsed by {@link android.text.Html#fromHtml(String) Html.fromHtml}.
         *
         * @param htmlText
         * 		A string containing HTML markup as a richer version of the text
         * 		provided by EXTRA_TEXT.
         * @return This IntentBuilder for method chaining
         * @see #setText(CharSequence)
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setHtmlText(java.lang.String htmlText) {
            mIntent.putExtra(android.support.v4.content.IntentCompat.EXTRA_HTML_TEXT, htmlText);
            if (!mIntent.hasExtra(android.content.Intent.EXTRA_TEXT)) {
                // Supply a default if EXTRA_TEXT isn't set
                setText(android.text.Html.fromHtml(htmlText));
            }
            return this;
        }

        /**
         * Set a stream URI to the data that should be shared.
         *
         * <p>This replaces all currently set stream URIs and will produce a single-stream
         * ACTION_SEND intent.</p>
         *
         * @param streamUri
         * 		URI of the stream to share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_STREAM
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setStream(android.net.Uri streamUri) {
            if (!mIntent.getAction().equals(android.content.Intent.ACTION_SEND)) {
                mIntent.setAction(android.content.Intent.ACTION_SEND);
            }
            mStreams = null;
            mIntent.putExtra(android.content.Intent.EXTRA_STREAM, streamUri);
            return this;
        }

        /**
         * Add a stream URI to the data that should be shared. If this is not the first
         * stream URI added the final intent constructed will become an ACTION_SEND_MULTIPLE
         * intent. Not all apps will handle both ACTION_SEND and ACTION_SEND_MULTIPLE.
         *
         * @param streamUri
         * 		URI of the stream to share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_STREAM
         * @see Intent#ACTION_SEND
         * @see Intent#ACTION_SEND_MULTIPLE
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addStream(android.net.Uri streamUri) {
            android.net.Uri currentStream = mIntent.getParcelableExtra(android.content.Intent.EXTRA_STREAM);
            if ((mStreams == null) && (currentStream == null)) {
                return setStream(streamUri);
            }
            if (mStreams == null) {
                mStreams = new java.util.ArrayList<android.net.Uri>();
            }
            if (currentStream != null) {
                mIntent.removeExtra(android.content.Intent.EXTRA_STREAM);
                mStreams.add(currentStream);
            }
            mStreams.add(streamUri);
            return this;
        }

        /**
         * Set an array of email addresses as recipients of this share.
         * This replaces all current "to" recipients that have been set so far.
         *
         * @param addresses
         * 		Email addresses to send to
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_EMAIL
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setEmailTo(java.lang.String[] addresses) {
            if (mToAddresses != null) {
                mToAddresses = null;
            }
            mIntent.putExtra(android.content.Intent.EXTRA_EMAIL, addresses);
            return this;
        }

        /**
         * Add an email address to be used in the "to" field of the final Intent.
         *
         * @param address
         * 		Email address to send to
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_EMAIL
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailTo(java.lang.String address) {
            if (mToAddresses == null) {
                mToAddresses = new java.util.ArrayList<java.lang.String>();
            }
            mToAddresses.add(address);
            return this;
        }

        /**
         * Add an array of email addresses to be used in the "to" field of the final Intent.
         *
         * @param addresses
         * 		Email addresses to send to
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_EMAIL
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailTo(java.lang.String[] addresses) {
            combineArrayExtra(android.content.Intent.EXTRA_EMAIL, addresses);
            return this;
        }

        /**
         * Set an array of email addresses to CC on this share.
         * This replaces all current "CC" recipients that have been set so far.
         *
         * @param addresses
         * 		Email addresses to CC on the share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_CC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setEmailCc(java.lang.String[] addresses) {
            mIntent.putExtra(android.content.Intent.EXTRA_CC, addresses);
            return this;
        }

        /**
         * Add an email address to be used in the "cc" field of the final Intent.
         *
         * @param address
         * 		Email address to CC
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_CC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailCc(java.lang.String address) {
            if (mCcAddresses == null) {
                mCcAddresses = new java.util.ArrayList<java.lang.String>();
            }
            mCcAddresses.add(address);
            return this;
        }

        /**
         * Add an array of email addresses to be used in the "cc" field of the final Intent.
         *
         * @param addresses
         * 		Email addresses to CC
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_CC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailCc(java.lang.String[] addresses) {
            combineArrayExtra(android.content.Intent.EXTRA_CC, addresses);
            return this;
        }

        /**
         * Set an array of email addresses to BCC on this share.
         * This replaces all current "BCC" recipients that have been set so far.
         *
         * @param addresses
         * 		Email addresses to BCC on the share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_BCC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setEmailBcc(java.lang.String[] addresses) {
            mIntent.putExtra(android.content.Intent.EXTRA_BCC, addresses);
            return this;
        }

        /**
         * Add an email address to be used in the "bcc" field of the final Intent.
         *
         * @param address
         * 		Email address to BCC
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_BCC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailBcc(java.lang.String address) {
            if (mBccAddresses == null) {
                mBccAddresses = new java.util.ArrayList<java.lang.String>();
            }
            mBccAddresses.add(address);
            return this;
        }

        /**
         * Add an array of email addresses to be used in the "bcc" field of the final Intent.
         *
         * @param addresses
         * 		Email addresses to BCC
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_BCC
         */
        public android.support.v4.app.ShareCompat.IntentBuilder addEmailBcc(java.lang.String[] addresses) {
            combineArrayExtra(android.content.Intent.EXTRA_BCC, addresses);
            return this;
        }

        /**
         * Set a subject heading for this share; useful for sharing via email.
         *
         * @param subject
         * 		Subject heading for this share
         * @return This IntentBuilder for method chaining
         * @see Intent#EXTRA_SUBJECT
         */
        public android.support.v4.app.ShareCompat.IntentBuilder setSubject(java.lang.String subject) {
            mIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            return this;
        }
    }

    /**
     * IntentReader is a helper for reading the data contained within a sharing (ACTION_SEND)
     * Intent. It provides methods to parse standard elements included with a share
     * in addition to extra metadata about the app that shared the content.
     *
     * <p>Social sharing apps are encouraged to provide attribution for the app that shared
     * the content. IntentReader offers access to the application label, calling activity info,
     * and application icon of the app that shared the content. This data may have been provided
     * voluntarily by the calling app and should always be displayed to the user before submission
     * for manual verification. The user should be offered the option to omit this information
     * from shared posts if desired.</p>
     *
     * <p>Activities that intend to receive sharing intents should configure an intent-filter
     * to accept {@link Intent#ACTION_SEND} intents ("android.intent.action.SEND") and optionally
     * accept {@link Intent#ACTION_SEND_MULTIPLE} ("android.intent.action.SEND_MULTIPLE") if
     * the activity is equipped to handle multiple data streams.</p>
     */
    public static class IntentReader {
        private static final java.lang.String TAG = "IntentReader";

        private android.app.Activity mActivity;

        private android.content.Intent mIntent;

        private java.lang.String mCallingPackage;

        private android.content.ComponentName mCallingActivity;

        private java.util.ArrayList<android.net.Uri> mStreams;

        /**
         * Get an IntentReader for parsing and interpreting the sharing intent
         * used to start the given activity.
         *
         * @param activity
         * 		Activity that was started to share content
         * @return IntentReader for parsing sharing data
         */
        public static android.support.v4.app.ShareCompat.IntentReader from(android.app.Activity activity) {
            return new android.support.v4.app.ShareCompat.IntentReader(activity);
        }

        private IntentReader(android.app.Activity activity) {
            mActivity = activity;
            mIntent = activity.getIntent();
            mCallingPackage = android.support.v4.app.ShareCompat.getCallingPackage(activity);
            mCallingActivity = android.support.v4.app.ShareCompat.getCallingActivity(activity);
        }

        /**
         * Returns true if the activity this reader was obtained for was
         * started with an {@link Intent#ACTION_SEND} or {@link Intent#ACTION_SEND_MULTIPLE}
         * sharing Intent.
         *
         * @return true if the activity was started with an ACTION_SEND
        or ACTION_SEND_MULTIPLE Intent
         */
        public boolean isShareIntent() {
            final java.lang.String action = mIntent.getAction();
            return android.content.Intent.ACTION_SEND.equals(action) || android.content.Intent.ACTION_SEND_MULTIPLE.equals(action);
        }

        /**
         * Returns true if the activity this reader was obtained for was started with an
         * {@link Intent#ACTION_SEND} intent and contains a single shared item.
         * The shared content should be obtained using either the {@link #getText()}
         * or {@link #getStream()} methods depending on the type of content shared.
         *
         * @return true if the activity was started with an ACTION_SEND intent
         */
        public boolean isSingleShare() {
            return android.content.Intent.ACTION_SEND.equals(mIntent.getAction());
        }

        /**
         * Returns true if the activity this reader was obtained for was started with an
         * {@link Intent#ACTION_SEND_MULTIPLE} intent. The Intent may contain more than
         * one stream item.
         *
         * @return true if the activity was started with an ACTION_SEND_MULTIPLE intent
         */
        public boolean isMultipleShare() {
            return android.content.Intent.ACTION_SEND_MULTIPLE.equals(mIntent.getAction());
        }

        /**
         * Get the mimetype of the data shared to this activity.
         *
         * @return mimetype of the shared data
         * @see Intent#getType()
         */
        public java.lang.String getType() {
            return mIntent.getType();
        }

        /**
         * Get the literal text shared with the target activity.
         *
         * @return Literal shared text or null if none was supplied
         * @see Intent#EXTRA_TEXT
         */
        public java.lang.CharSequence getText() {
            return mIntent.getCharSequenceExtra(android.content.Intent.EXTRA_TEXT);
        }

        /**
         * Get the styled HTML text shared with the target activity.
         * If no HTML text was supplied but {@link Intent#EXTRA_TEXT} contained
         * styled text, it will be converted to HTML if possible and returned.
         * If the text provided by {@link Intent#EXTRA_TEXT} was not styled text,
         * it will be escaped by {@link android.text.Html#escapeHtml(CharSequence)}
         * and returned. If no text was provided at all, this method will return null.
         *
         * @return Styled text provided by the sender as HTML.
         */
        public java.lang.String getHtmlText() {
            java.lang.String result = mIntent.getStringExtra(android.support.v4.content.IntentCompat.EXTRA_HTML_TEXT);
            if (result == null) {
                java.lang.CharSequence text = getText();
                if (text instanceof android.text.Spanned) {
                    result = android.text.Html.toHtml(((android.text.Spanned) (text)));
                } else
                    if (text != null) {
                        result = android.support.v4.app.ShareCompat.IMPL.escapeHtml(text);
                    }

            }
            return result;
        }

        /**
         * Get a URI referring to a data stream shared with the target activity.
         *
         * <p>This call will fail if the share intent contains multiple stream items.
         * If {@link #isMultipleShare()} returns true the application should use
         * {@link #getStream(int)} and {@link #getStreamCount()} to retrieve the
         * included stream items.</p>
         *
         * @return A URI referring to a data stream to be shared or null if one was not supplied
         * @see Intent#EXTRA_STREAM
         */
        public android.net.Uri getStream() {
            return mIntent.getParcelableExtra(android.content.Intent.EXTRA_STREAM);
        }

        /**
         * Get the URI of a stream item shared with the target activity.
         * Index should be in the range [0-getStreamCount()).
         *
         * @param index
         * 		Index of text item to retrieve
         * @return Requested stream item URI
         * @see Intent#EXTRA_STREAM
         * @see Intent#ACTION_SEND_MULTIPLE
         */
        public android.net.Uri getStream(int index) {
            if ((mStreams == null) && isMultipleShare()) {
                mStreams = mIntent.getParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM);
            }
            if (mStreams != null) {
                return mStreams.get(index);
            }
            if (index == 0) {
                return mIntent.getParcelableExtra(android.content.Intent.EXTRA_STREAM);
            }
            throw new java.lang.IndexOutOfBoundsException((("Stream items available: " + getStreamCount()) + " index requested: ") + index);
        }

        /**
         * Return the number of stream items shared. The return value will be 0 or 1 if
         * this was an {@link Intent#ACTION_SEND} intent, or 0 or more if it was an
         * {@link Intent#ACTION_SEND_MULTIPLE} intent.
         *
         * @return Count of text items contained within the Intent
         */
        public int getStreamCount() {
            if ((mStreams == null) && isMultipleShare()) {
                mStreams = mIntent.getParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM);
            }
            if (mStreams != null) {
                return mStreams.size();
            }
            return mIntent.hasExtra(android.content.Intent.EXTRA_STREAM) ? 1 : 0;
        }

        /**
         * Get an array of Strings, each an email address to share to.
         *
         * @return An array of email addresses or null if none were supplied.
         * @see Intent#EXTRA_EMAIL
         */
        public java.lang.String[] getEmailTo() {
            return mIntent.getStringArrayExtra(android.content.Intent.EXTRA_EMAIL);
        }

        /**
         * Get an array of Strings, each an email address to CC on this share.
         *
         * @return An array of email addresses or null if none were supplied.
         * @see Intent#EXTRA_CC
         */
        public java.lang.String[] getEmailCc() {
            return mIntent.getStringArrayExtra(android.content.Intent.EXTRA_CC);
        }

        /**
         * Get an array of Strings, each an email address to BCC on this share.
         *
         * @return An array of email addresses or null if none were supplied.
         * @see Intent#EXTRA_BCC
         */
        public java.lang.String[] getEmailBcc() {
            return mIntent.getStringArrayExtra(android.content.Intent.EXTRA_BCC);
        }

        /**
         * Get a subject heading for this share; useful when sharing via email.
         *
         * @return The subject heading for this share or null if one was not supplied.
         * @see Intent#EXTRA_SUBJECT
         */
        public java.lang.String getSubject() {
            return mIntent.getStringExtra(android.content.Intent.EXTRA_SUBJECT);
        }

        /**
         * Get the name of the package that invoked this sharing intent. If the activity
         * was not started for a result, IntentBuilder will read this from extra metadata placed
         * in the Intent by ShareBuilder.
         *
         * <p><em>Note:</em> This data may have been provided voluntarily by the calling
         * application. As such it should not be trusted for accuracy in the context of
         * security or verification.</p>
         *
         * @return Name of the package that started this activity or null if unknown
         * @see Activity#getCallingPackage()
         * @see ShareCompat#EXTRA_CALLING_PACKAGE
         */
        public java.lang.String getCallingPackage() {
            return mCallingPackage;
        }

        /**
         * Get the {@link ComponentName} of the Activity that invoked this sharing intent.
         * If the target sharing activity was not started for a result, IntentBuilder will read
         * this from extra metadata placed in the intent by ShareBuilder.
         *
         * <p><em>Note:</em> This data may have been provided voluntarily by the calling
         * application. As such it should not be trusted for accuracy in the context of
         * security or verification.</p>
         *
         * @return ComponentName of the calling Activity or null if unknown
         * @see Activity#getCallingActivity()
         * @see ShareCompat#EXTRA_CALLING_ACTIVITY
         */
        public android.content.ComponentName getCallingActivity() {
            return mCallingActivity;
        }

        /**
         * Get the icon of the calling activity as a Drawable if data about
         * the calling activity is available.
         *
         * <p><em>Note:</em> This data may have been provided voluntarily by the calling
         * application. As such it should not be trusted for accuracy in the context of
         * security or verification.</p>
         *
         * @return The calling Activity's icon or null if unknown
         */
        public android.graphics.drawable.Drawable getCallingActivityIcon() {
            if (mCallingActivity == null)
                return null;

            android.content.pm.PackageManager pm = mActivity.getPackageManager();
            try {
                return pm.getActivityIcon(mCallingActivity);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(android.support.v4.app.ShareCompat.IntentReader.TAG, "Could not retrieve icon for calling activity", e);
            }
            return null;
        }

        /**
         * Get the icon of the calling application as a Drawable if data
         * about the calling package is available.
         *
         * <p><em>Note:</em> This data may have been provided voluntarily by the calling
         * application. As such it should not be trusted for accuracy in the context of
         * security or verification.</p>
         *
         * @return The calling application's icon or null if unknown
         */
        public android.graphics.drawable.Drawable getCallingApplicationIcon() {
            if (mCallingPackage == null)
                return null;

            android.content.pm.PackageManager pm = mActivity.getPackageManager();
            try {
                return pm.getApplicationIcon(mCallingPackage);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(android.support.v4.app.ShareCompat.IntentReader.TAG, "Could not retrieve icon for calling application", e);
            }
            return null;
        }

        /**
         * Get the human-readable label (title) of the calling application if
         * data about the calling package is available.
         *
         * <p><em>Note:</em> This data may have been provided voluntarily by the calling
         * application. As such it should not be trusted for accuracy in the context of
         * security or verification.</p>
         *
         * @return The calling application's label or null if unknown
         */
        public java.lang.CharSequence getCallingApplicationLabel() {
            if (mCallingPackage == null)
                return null;

            android.content.pm.PackageManager pm = mActivity.getPackageManager();
            try {
                return pm.getApplicationLabel(pm.getApplicationInfo(mCallingPackage, 0));
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(android.support.v4.app.ShareCompat.IntentReader.TAG, "Could not retrieve label for calling application", e);
            }
            return null;
        }
    }
}

