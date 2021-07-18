/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.content;


/**
 * Structured description of Intent values to be matched.  An IntentFilter can
 * match against actions, categories, and data (either via its type, scheme,
 * and/or path) in an Intent.  It also includes a "priority" value which is
 * used to order multiple matching filters.
 *
 * <p>IntentFilter objects are often created in XML as part of a package's
 * {@link android.R.styleable#AndroidManifest AndroidManifest.xml} file,
 * using {@link android.R.styleable#AndroidManifestIntentFilter intent-filter}
 * tags.
 *
 * <p>There are three Intent characteristics you can filter on: the
 * <em>action</em>, <em>data</em>, and <em>categories</em>.  For each of these
 * characteristics you can provide
 * multiple possible matching values (via {@link #addAction},
 * {@link #addDataType}, {@link #addDataScheme}, {@link #addDataSchemeSpecificPart},
 * {@link #addDataAuthority}, {@link #addDataPath}, and {@link #addCategory}, respectively).
 * For actions, the field
 * will not be tested if no values have been given (treating it as a wildcard);
 * if no data characteristics are specified, however, then the filter will
 * only match intents that contain no data.
 *
 * <p>The data characteristic is
 * itself divided into three attributes: type, scheme, authority, and path.
 * Any that are
 * specified must match the contents of the Intent.  If you specify a scheme
 * but no type, only Intent that does not have a type (such as mailto:) will
 * match; a content: URI will never match because they always have a MIME type
 * that is supplied by their content provider.  Specifying a type with no scheme
 * has somewhat special meaning: it will match either an Intent with no URI
 * field, or an Intent with a content: or file: URI.  If you specify neither,
 * then only an Intent with no data or type will match.  To specify an authority,
 * you must also specify one or more schemes that it is associated with.
 * To specify a path, you also must specify both one or more authorities and
 * one or more schemes it is associated with.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about how to create and resolve intents, read the
 * <a href="{@docRoot }guide/topics/intents/intents-filters.html">Intents and Intent Filters</a>
 * developer guide.</p>
 * </div>
 *
 * <h3>Filter Rules</h3>
 * <p>A match is based on the following rules.  Note that
 * for an IntentFilter to match an Intent, three conditions must hold:
 * the <strong>action</strong> and <strong>category</strong> must match, and
 * the data (both the <strong>data type</strong> and
 * <strong>data scheme+authority+path</strong> if specified) must match
 * (see {@link #match(ContentResolver, Intent, boolean, String)} for more details
 * on how the data fields match).
 *
 * <p><strong>Action</strong> matches if any of the given values match the
 * Intent action; if the filter specifies no actions, then it will only match
 * Intents that do not contain an action.
 *
 * <p><strong>Data Type</strong> matches if any of the given values match the
 * Intent type.  The Intent
 * type is determined by calling {@link Intent#resolveType}.  A wildcard can be
 * used for the MIME sub-type, in both the Intent and IntentFilter, so that the
 * type "audio/*" will match "audio/mpeg", "audio/aiff", "audio/*", etc.
 * <em>Note that MIME type matching here is <b>case sensitive</b>, unlike
 * formal RFC MIME types!</em>  You should thus always use lower case letters
 * for your MIME types.
 *
 * <p><strong>Data Scheme</strong> matches if any of the given values match the
 * Intent data's scheme.
 * The Intent scheme is determined by calling {@link Intent#getData}
 * and {@link android.net.Uri#getScheme} on that URI.
 * <em>Note that scheme matching here is <b>case sensitive</b>, unlike
 * formal RFC schemes!</em>  You should thus always use lower case letters
 * for your schemes.
 *
 * <p><strong>Data Scheme Specific Part</strong> matches if any of the given values match
 * the Intent's data scheme specific part <em>and</em> one of the data schemes in the filter
 * has matched the Intent, <em>or</em> no scheme specific parts were supplied in the filter.
 * The Intent scheme specific part is determined by calling
 * {@link Intent#getData} and {@link android.net.Uri#getSchemeSpecificPart} on that URI.
 * <em>Note that scheme specific part matching is <b>case sensitive</b>.</em>
 *
 * <p><strong>Data Authority</strong> matches if any of the given values match
 * the Intent's data authority <em>and</em> one of the data schemes in the filter
 * has matched the Intent, <em>or</em> no authorities were supplied in the filter.
 * The Intent authority is determined by calling
 * {@link Intent#getData} and {@link android.net.Uri#getAuthority} on that URI.
 * <em>Note that authority matching here is <b>case sensitive</b>, unlike
 * formal RFC host names!</em>  You should thus always use lower case letters
 * for your authority.
 *
 * <p><strong>Data Path</strong> matches if any of the given values match the
 * Intent's data path <em>and</em> both a scheme and authority in the filter
 * has matched against the Intent, <em>or</em> no paths were supplied in the
 * filter.  The Intent authority is determined by calling
 * {@link Intent#getData} and {@link android.net.Uri#getPath} on that URI.
 *
 * <p><strong>Categories</strong> match if <em>all</em> of the categories in
 * the Intent match categories given in the filter.  Extra categories in the
 * filter that are not in the Intent will not cause the match to fail.  Note
 * that unlike the action, an IntentFilter with no categories
 * will only match an Intent that does not have any categories.
 */
public class IntentFilter implements android.os.Parcelable {
    private static final java.lang.String AGLOB_STR = "aglob";

    private static final java.lang.String SGLOB_STR = "sglob";

    private static final java.lang.String PREFIX_STR = "prefix";

    private static final java.lang.String LITERAL_STR = "literal";

    private static final java.lang.String PATH_STR = "path";

    private static final java.lang.String PORT_STR = "port";

    private static final java.lang.String HOST_STR = "host";

    private static final java.lang.String AUTH_STR = "auth";

    private static final java.lang.String SSP_STR = "ssp";

    private static final java.lang.String SCHEME_STR = "scheme";

    private static final java.lang.String TYPE_STR = "type";

    private static final java.lang.String CAT_STR = "cat";

    private static final java.lang.String NAME_STR = "name";

    private static final java.lang.String ACTION_STR = "action";

    private static final java.lang.String AUTO_VERIFY_STR = "autoVerify";

    /**
     * The filter {@link #setPriority} value at which system high-priority
     * receivers are placed; that is, receivers that should execute before
     * application code. Applications should never use filters with this or
     * higher priorities.
     *
     * @see #setPriority
     */
    public static final int SYSTEM_HIGH_PRIORITY = 1000;

    /**
     * The filter {@link #setPriority} value at which system low-priority
     * receivers are placed; that is, receivers that should execute after
     * application code. Applications should never use filters with this or
     * lower priorities.
     *
     * @see #setPriority
     */
    public static final int SYSTEM_LOW_PRIORITY = -1000;

    /**
     * The part of a match constant that describes the category of match
     * that occurred.  May be either {@link #MATCH_CATEGORY_EMPTY},
     * {@link #MATCH_CATEGORY_SCHEME}, {@link #MATCH_CATEGORY_SCHEME_SPECIFIC_PART},
     * {@link #MATCH_CATEGORY_HOST}, {@link #MATCH_CATEGORY_PORT},
     * {@link #MATCH_CATEGORY_PATH}, or {@link #MATCH_CATEGORY_TYPE}.  Higher
     * values indicate a better match.
     */
    public static final int MATCH_CATEGORY_MASK = 0xfff0000;

    /**
     * The part of a match constant that applies a quality adjustment to the
     * basic category of match.  The value {@link #MATCH_ADJUSTMENT_NORMAL}
     * is no adjustment; higher numbers than that improve the quality, while
     * lower numbers reduce it.
     */
    public static final int MATCH_ADJUSTMENT_MASK = 0xffff;

    /**
     * Quality adjustment applied to the category of match that signifies
     * the default, base value; higher numbers improve the quality while
     * lower numbers reduce it.
     */
    public static final int MATCH_ADJUSTMENT_NORMAL = 0x8000;

    /**
     * The filter matched an intent that had no data specified.
     */
    public static final int MATCH_CATEGORY_EMPTY = 0x100000;

    /**
     * The filter matched an intent with the same data URI scheme.
     */
    public static final int MATCH_CATEGORY_SCHEME = 0x200000;

    /**
     * The filter matched an intent with the same data URI scheme and
     * authority host.
     */
    public static final int MATCH_CATEGORY_HOST = 0x300000;

    /**
     * The filter matched an intent with the same data URI scheme and
     * authority host and port.
     */
    public static final int MATCH_CATEGORY_PORT = 0x400000;

    /**
     * The filter matched an intent with the same data URI scheme,
     * authority, and path.
     */
    public static final int MATCH_CATEGORY_PATH = 0x500000;

    /**
     * The filter matched an intent with the same data URI scheme and
     * scheme specific part.
     */
    public static final int MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 0x580000;

    /**
     * The filter matched an intent with the same data MIME type.
     */
    public static final int MATCH_CATEGORY_TYPE = 0x600000;

    /**
     * The filter didn't match due to different MIME types.
     */
    public static final int NO_MATCH_TYPE = -1;

    /**
     * The filter didn't match due to different data URIs.
     */
    public static final int NO_MATCH_DATA = -2;

    /**
     * The filter didn't match due to different actions.
     */
    public static final int NO_MATCH_ACTION = -3;

    /**
     * The filter didn't match because it required one or more categories
     * that were not in the Intent.
     */
    public static final int NO_MATCH_CATEGORY = -4;

    /**
     * HTTP scheme.
     *
     * @see #addDataScheme(String)
     * @unknown 
     */
    public static final java.lang.String SCHEME_HTTP = "http";

    /**
     * HTTPS scheme.
     *
     * @see #addDataScheme(String)
     * @unknown 
     */
    public static final java.lang.String SCHEME_HTTPS = "https";

    private int mPriority;

    @android.annotation.UnsupportedAppUsage
    private int mOrder;

    @android.annotation.UnsupportedAppUsage
    private final java.util.ArrayList<java.lang.String> mActions;

    private java.util.ArrayList<java.lang.String> mCategories = null;

    private java.util.ArrayList<java.lang.String> mDataSchemes = null;

    private java.util.ArrayList<android.os.PatternMatcher> mDataSchemeSpecificParts = null;

    private java.util.ArrayList<android.content.IntentFilter.AuthorityEntry> mDataAuthorities = null;

    private java.util.ArrayList<android.os.PatternMatcher> mDataPaths = null;

    private java.util.ArrayList<java.lang.String> mDataTypes = null;

    private boolean mHasPartialTypes = false;

    private static final int STATE_VERIFY_AUTO = 0x1;

    private static final int STATE_NEED_VERIFY = 0x10;

    private static final int STATE_NEED_VERIFY_CHECKED = 0x100;

    private static final int STATE_VERIFIED = 0x1000;

    private int mVerifyState;

    /**
     *
     *
     * @unknown 
     */
    public static final int VISIBILITY_NONE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int VISIBILITY_EXPLICIT = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int VISIBILITY_IMPLICIT = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "VISIBILITY_" }, value = { android.content.IntentFilter.VISIBILITY_NONE, android.content.IntentFilter.VISIBILITY_EXPLICIT, android.content.IntentFilter.VISIBILITY_IMPLICIT })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface InstantAppVisibility {}

    /**
     * Whether or not the intent filter is visible to instant apps.
     */
    @android.content.IntentFilter.InstantAppVisibility
    private int mInstantAppVisibility;

    // These functions are the start of more optimized code for managing
    // the string sets...  not yet implemented.
    private static int findStringInSet(java.lang.String[] set, java.lang.String string, int[] lengths, int lenPos) {
        if (set == null)
            return -1;

        final int N = lengths[lenPos];
        for (int i = 0; i < N; i++) {
            if (set[i].equals(string))
                return i;

        }
        return -1;
    }

    private static java.lang.String[] addStringToSet(java.lang.String[] set, java.lang.String string, int[] lengths, int lenPos) {
        if (android.content.IntentFilter.findStringInSet(set, string, lengths, lenPos) >= 0)
            return set;

        if (set == null) {
            set = new java.lang.String[2];
            set[0] = string;
            lengths[lenPos] = 1;
            return set;
        }
        final int N = lengths[lenPos];
        if (N < set.length) {
            set[N] = string;
            lengths[lenPos] = N + 1;
            return set;
        }
        java.lang.String[] newSet = new java.lang.String[((N * 3) / 2) + 2];
        java.lang.System.arraycopy(set, 0, newSet, 0, N);
        set = newSet;
        set[N] = string;
        lengths[lenPos] = N + 1;
        return set;
    }

    private static java.lang.String[] removeStringFromSet(java.lang.String[] set, java.lang.String string, int[] lengths, int lenPos) {
        int pos = android.content.IntentFilter.findStringInSet(set, string, lengths, lenPos);
        if (pos < 0)
            return set;

        final int N = lengths[lenPos];
        if (N > (set.length / 4)) {
            int copyLen = N - (pos + 1);
            if (copyLen > 0) {
                java.lang.System.arraycopy(set, pos + 1, set, pos, copyLen);
            }
            set[N - 1] = null;
            lengths[lenPos] = N - 1;
            return set;
        }
        java.lang.String[] newSet = new java.lang.String[set.length / 3];
        if (pos > 0)
            java.lang.System.arraycopy(set, 0, newSet, 0, pos);

        if ((pos + 1) < N)
            java.lang.System.arraycopy(set, pos + 1, newSet, pos, N - (pos + 1));

        return newSet;
    }

    /**
     * This exception is thrown when a given MIME type does not have a valid
     * syntax.
     */
    public static class MalformedMimeTypeException extends android.util.AndroidException {
        public MalformedMimeTypeException() {
        }

        public MalformedMimeTypeException(java.lang.String name) {
            super(name);
        }
    }

    /**
     * Create a new IntentFilter instance with a specified action and MIME
     * type, where you know the MIME type is correctly formatted.  This catches
     * the {@link MalformedMimeTypeException} exception that the constructor
     * can call and turns it into a runtime exception.
     *
     * @param action
     * 		The action to match, such as Intent.ACTION_VIEW.
     * @param dataType
     * 		The type to match, such as "vnd.android.cursor.dir/person".
     * @return A new IntentFilter for the given action and type.
     * @see #IntentFilter(String, String)
     */
    public static android.content.IntentFilter create(java.lang.String action, java.lang.String dataType) {
        try {
            return new android.content.IntentFilter(action, dataType);
        } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            throw new java.lang.RuntimeException("Bad MIME type", e);
        }
    }

    /**
     * New empty IntentFilter.
     */
    public IntentFilter() {
        mPriority = 0;
        mActions = new java.util.ArrayList<java.lang.String>();
    }

    /**
     * New IntentFilter that matches a single action with no data.  If
     * no data characteristics are subsequently specified, then the
     * filter will only match intents that contain no data.
     *
     * @param action
     * 		The action to match, such as Intent.ACTION_MAIN.
     */
    public IntentFilter(java.lang.String action) {
        mPriority = 0;
        mActions = new java.util.ArrayList<java.lang.String>();
        addAction(action);
    }

    /**
     * New IntentFilter that matches a single action and data type.
     *
     * <p><em>Note: MIME type matching in the Android framework is
     * case-sensitive, unlike formal RFC MIME types.  As a result,
     * you should always write your MIME types with lower case letters,
     * and any MIME types you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * <p>Throws {@link MalformedMimeTypeException} if the given MIME type is
     * not syntactically correct.
     *
     * @param action
     * 		The action to match, such as Intent.ACTION_VIEW.
     * @param dataType
     * 		The type to match, such as "vnd.android.cursor.dir/person".
     */
    public IntentFilter(java.lang.String action, java.lang.String dataType) throws android.content.IntentFilter.MalformedMimeTypeException {
        mPriority = 0;
        mActions = new java.util.ArrayList<java.lang.String>();
        addAction(action);
        addDataType(dataType);
    }

    /**
     * New IntentFilter containing a copy of an existing filter.
     *
     * @param o
     * 		The original filter to copy.
     */
    public IntentFilter(android.content.IntentFilter o) {
        mPriority = o.mPriority;
        mOrder = o.mOrder;
        mActions = new java.util.ArrayList<java.lang.String>(o.mActions);
        if (o.mCategories != null) {
            mCategories = new java.util.ArrayList<java.lang.String>(o.mCategories);
        }
        if (o.mDataTypes != null) {
            mDataTypes = new java.util.ArrayList<java.lang.String>(o.mDataTypes);
        }
        if (o.mDataSchemes != null) {
            mDataSchemes = new java.util.ArrayList<java.lang.String>(o.mDataSchemes);
        }
        if (o.mDataSchemeSpecificParts != null) {
            mDataSchemeSpecificParts = new java.util.ArrayList<android.os.PatternMatcher>(o.mDataSchemeSpecificParts);
        }
        if (o.mDataAuthorities != null) {
            mDataAuthorities = new java.util.ArrayList<android.content.IntentFilter.AuthorityEntry>(o.mDataAuthorities);
        }
        if (o.mDataPaths != null) {
            mDataPaths = new java.util.ArrayList<android.os.PatternMatcher>(o.mDataPaths);
        }
        mHasPartialTypes = o.mHasPartialTypes;
        mVerifyState = o.mVerifyState;
        mInstantAppVisibility = o.mInstantAppVisibility;
    }

    /**
     * Modify priority of this filter.  This only affects receiver filters.
     * The priority of activity filters are set in XML and cannot be changed
     * programmatically. The default priority is 0. Positive values will be
     * before the default, lower values will be after it. Applications should
     * use a value that is larger than {@link #SYSTEM_LOW_PRIORITY} and
     * smaller than {@link #SYSTEM_HIGH_PRIORITY} .
     *
     * @param priority
     * 		The new priority value.
     * @see #getPriority
     * @see #SYSTEM_LOW_PRIORITY
     * @see #SYSTEM_HIGH_PRIORITY
     */
    public final void setPriority(int priority) {
        mPriority = priority;
    }

    /**
     * Return the priority of this filter.
     *
     * @return The priority of the filter.
     * @see #setPriority
     */
    public final int getPriority() {
        return mPriority;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public final void setOrder(int order) {
        mOrder = order;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public final int getOrder() {
        return mOrder;
    }

    /**
     * Set whether this filter will needs to be automatically verified against its data URIs or not.
     * The default is false.
     *
     * The verification would need to happen only and only if the Intent action is
     * {@link android.content.Intent#ACTION_VIEW} and the Intent category is
     * {@link android.content.Intent#CATEGORY_BROWSABLE} and the Intent data scheme
     * is "http" or "https".
     *
     * True means that the filter will need to use its data URIs to be verified.
     *
     * @param autoVerify
     * 		The new autoVerify value.
     * @see #getAutoVerify()
     * @see #addAction(String)
     * @see #getAction(int)
     * @see #addCategory(String)
     * @see #getCategory(int)
     * @see #addDataScheme(String)
     * @see #getDataScheme(int)
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final void setAutoVerify(boolean autoVerify) {
        mVerifyState &= ~android.content.IntentFilter.STATE_VERIFY_AUTO;
        if (autoVerify)
            mVerifyState |= android.content.IntentFilter.STATE_VERIFY_AUTO;

    }

    /**
     * Return if this filter will needs to be automatically verified again its data URIs or not.
     *
     * @return True if the filter will needs to be automatically verified. False otherwise.
     * @see #setAutoVerify(boolean)
     * @unknown 
     */
    public final boolean getAutoVerify() {
        return (mVerifyState & android.content.IntentFilter.STATE_VERIFY_AUTO) == android.content.IntentFilter.STATE_VERIFY_AUTO;
    }

    /**
     * Return if this filter handle all HTTP or HTTPS data URI or not.  This is the
     * core check for whether a given activity qualifies as a "browser".
     *
     * @return True if the filter handle all HTTP or HTTPS data URI. False otherwise.

    This will check if:

    - either the Intent category is {@link android.content.Intent#CATEGORY_APP_BROWSER}
    - either the Intent action is {@link android.content.Intent#ACTION_VIEW} and
    the Intent category is {@link android.content.Intent#CATEGORY_BROWSABLE} and the Intent
    data scheme is "http" or "https" and that there is no specific host defined.
     * @unknown 
     */
    public final boolean handleAllWebDataURI() {
        return hasCategory(android.content.Intent.CATEGORY_APP_BROWSER) || (handlesWebUris(false) && (countDataAuthorities() == 0));
    }

    /**
     * Return if this filter handles HTTP or HTTPS data URIs.
     *
     * @return True if the filter handles ACTION_VIEW/CATEGORY_BROWSABLE,
    has at least one HTTP or HTTPS data URI pattern defined, and optionally
    does not define any non-http/https data URI patterns.

    This will check if if the Intent action is {@link android.content.Intent#ACTION_VIEW} and
    the Intent category is {@link android.content.Intent#CATEGORY_BROWSABLE} and the Intent
    data scheme is "http" or "https".
     * @param onlyWebSchemes
     * 		When true, requires that the intent filter declare
     * 		that it handles *only* http: or https: schemes.  This is a requirement for
     * 		the intent filter's domain linkage being verifiable.
     * @unknown 
     */
    public final boolean handlesWebUris(boolean onlyWebSchemes) {
        // Require ACTION_VIEW, CATEGORY_BROWSEABLE, and at least one scheme
        if ((((!hasAction(android.content.Intent.ACTION_VIEW)) || (!hasCategory(android.content.Intent.CATEGORY_BROWSABLE))) || (mDataSchemes == null)) || (mDataSchemes.size() == 0)) {
            return false;
        }
        // Now allow only the schemes "http" and "https"
        final int N = mDataSchemes.size();
        for (int i = 0; i < N; i++) {
            final java.lang.String scheme = mDataSchemes.get(i);
            final boolean isWebScheme = android.content.IntentFilter.SCHEME_HTTP.equals(scheme) || android.content.IntentFilter.SCHEME_HTTPS.equals(scheme);
            if (onlyWebSchemes) {
                // If we're specifically trying to ensure that there are no non-web schemes
                // declared in this filter, then if we ever see a non-http/https scheme then
                // we know it's a failure.
                if (!isWebScheme) {
                    return false;
                }
            } else {
                // If we see any http/https scheme declaration in this case then the
                // filter matches what we're looking for.
                if (isWebScheme) {
                    return true;
                }
            }
        }
        // We get here if:
        // 1) onlyWebSchemes and no non-web schemes were found, i.e success; or
        // 2) !onlyWebSchemes and no http/https schemes were found, i.e. failure.
        return onlyWebSchemes;
    }

    /**
     * Return if this filter needs to be automatically verified again its data URIs or not.
     *
     * @return True if the filter needs to be automatically verified. False otherwise.

    This will check if if the Intent action is {@link android.content.Intent#ACTION_VIEW} and
    the Intent category is {@link android.content.Intent#CATEGORY_BROWSABLE} and the Intent
    data scheme is "http" or "https".
     * @see #setAutoVerify(boolean)
     * @unknown 
     */
    public final boolean needsVerification() {
        return getAutoVerify() && handlesWebUris(true);
    }

    /**
     * Return if this filter has been verified
     *
     * @return true if the filter has been verified or if autoVerify is false.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public final boolean isVerified() {
        if ((mVerifyState & android.content.IntentFilter.STATE_NEED_VERIFY_CHECKED) == android.content.IntentFilter.STATE_NEED_VERIFY_CHECKED) {
            return (mVerifyState & android.content.IntentFilter.STATE_NEED_VERIFY) == android.content.IntentFilter.STATE_NEED_VERIFY;
        }
        return false;
    }

    /**
     * Set if this filter has been verified
     *
     * @param verified
     * 		true if this filter has been verified. False otherwise.
     * @unknown 
     */
    public void setVerified(boolean verified) {
        mVerifyState |= android.content.IntentFilter.STATE_NEED_VERIFY_CHECKED;
        mVerifyState &= ~android.content.IntentFilter.STATE_VERIFIED;
        if (verified)
            mVerifyState |= android.content.IntentFilter.STATE_VERIFIED;

    }

    /**
     *
     *
     * @unknown 
     */
    public void setVisibilityToInstantApp(@android.content.IntentFilter.InstantAppVisibility
    int visibility) {
        mInstantAppVisibility = visibility;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.content.IntentFilter.InstantAppVisibility
    public int getVisibilityToInstantApp() {
        return mInstantAppVisibility;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isVisibleToInstantApp() {
        return mInstantAppVisibility != android.content.IntentFilter.VISIBILITY_NONE;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isExplicitlyVisibleToInstantApp() {
        return mInstantAppVisibility == android.content.IntentFilter.VISIBILITY_EXPLICIT;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isImplicitlyVisibleToInstantApp() {
        return mInstantAppVisibility == android.content.IntentFilter.VISIBILITY_IMPLICIT;
    }

    /**
     * Add a new Intent action to match against.  If any actions are included
     * in the filter, then an Intent's action must be one of those values for
     * it to match.  If no actions are included, the Intent action is ignored.
     *
     * @param action
     * 		Name of the action to match, such as Intent.ACTION_VIEW.
     */
    public final void addAction(java.lang.String action) {
        if (!mActions.contains(action)) {
            mActions.add(action.intern());
        }
    }

    /**
     * Return the number of actions in the filter.
     */
    public final int countActions() {
        return mActions.size();
    }

    /**
     * Return an action in the filter.
     */
    public final java.lang.String getAction(int index) {
        return mActions.get(index);
    }

    /**
     * Is the given action included in the filter?  Note that if the filter
     * does not include any actions, false will <em>always</em> be returned.
     *
     * @param action
     * 		The action to look for.
     * @return True if the action is explicitly mentioned in the filter.
     */
    public final boolean hasAction(java.lang.String action) {
        return (action != null) && mActions.contains(action);
    }

    /**
     * Match this filter against an Intent's action.  If the filter does not
     * specify any actions, the match will always fail.
     *
     * @param action
     * 		The desired action to look for.
     * @return True if the action is listed in the filter.
     */
    public final boolean matchAction(java.lang.String action) {
        return hasAction(action);
    }

    /**
     * Return an iterator over the filter's actions.  If there are no actions,
     * returns null.
     */
    public final java.util.Iterator<java.lang.String> actionsIterator() {
        return mActions != null ? mActions.iterator() : null;
    }

    /**
     * Add a new Intent data type to match against.  If any types are
     * included in the filter, then an Intent's data must be <em>either</em>
     * one of these types <em>or</em> a matching scheme.  If no data types
     * are included, then an Intent will only match if it specifies no data.
     *
     * <p><em>Note: MIME type matching in the Android framework is
     * case-sensitive, unlike formal RFC MIME types.  As a result,
     * you should always write your MIME types with lower case letters,
     * and any MIME types you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * <p>Throws {@link MalformedMimeTypeException} if the given MIME type is
     * not syntactically correct.
     *
     * @param type
     * 		Name of the data type to match, such as "vnd.android.cursor.dir/person".
     * @see #matchData
     */
    public final void addDataType(java.lang.String type) throws android.content.IntentFilter.MalformedMimeTypeException {
        final int slashpos = type.indexOf('/');
        final int typelen = type.length();
        if ((slashpos > 0) && (typelen >= (slashpos + 2))) {
            if (mDataTypes == null)
                mDataTypes = new java.util.ArrayList<java.lang.String>();

            if ((typelen == (slashpos + 2)) && (type.charAt(slashpos + 1) == '*')) {
                java.lang.String str = type.substring(0, slashpos);
                if (!mDataTypes.contains(str)) {
                    mDataTypes.add(str.intern());
                }
                mHasPartialTypes = true;
            } else {
                if (!mDataTypes.contains(type)) {
                    mDataTypes.add(type.intern());
                }
            }
            return;
        }
        throw new android.content.IntentFilter.MalformedMimeTypeException(type);
    }

    /**
     * Is the given data type included in the filter?  Note that if the filter
     * does not include any type, false will <em>always</em> be returned.
     *
     * @param type
     * 		The data type to look for.
     * @return True if the type is explicitly mentioned in the filter.
     */
    public final boolean hasDataType(java.lang.String type) {
        return (mDataTypes != null) && findMimeType(type);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean hasExactDataType(java.lang.String type) {
        return (mDataTypes != null) && mDataTypes.contains(type);
    }

    /**
     * Return the number of data types in the filter.
     */
    public final int countDataTypes() {
        return mDataTypes != null ? mDataTypes.size() : 0;
    }

    /**
     * Return a data type in the filter.
     */
    public final java.lang.String getDataType(int index) {
        return mDataTypes.get(index);
    }

    /**
     * Return an iterator over the filter's data types.
     */
    public final java.util.Iterator<java.lang.String> typesIterator() {
        return mDataTypes != null ? mDataTypes.iterator() : null;
    }

    /**
     * Add a new Intent data scheme to match against.  If any schemes are
     * included in the filter, then an Intent's data must be <em>either</em>
     * one of these schemes <em>or</em> a matching data type.  If no schemes
     * are included, then an Intent will match only if it includes no data.
     *
     * <p><em>Note: scheme matching in the Android framework is
     * case-sensitive, unlike formal RFC schemes.  As a result,
     * you should always write your schemes with lower case letters,
     * and any schemes you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * @param scheme
     * 		Name of the scheme to match, such as "http".
     * @see #matchData
     */
    public final void addDataScheme(java.lang.String scheme) {
        if (mDataSchemes == null)
            mDataSchemes = new java.util.ArrayList<java.lang.String>();

        if (!mDataSchemes.contains(scheme)) {
            mDataSchemes.add(scheme.intern());
        }
    }

    /**
     * Return the number of data schemes in the filter.
     */
    public final int countDataSchemes() {
        return mDataSchemes != null ? mDataSchemes.size() : 0;
    }

    /**
     * Return a data scheme in the filter.
     */
    public final java.lang.String getDataScheme(int index) {
        return mDataSchemes.get(index);
    }

    /**
     * Is the given data scheme included in the filter?  Note that if the
     * filter does not include any scheme, false will <em>always</em> be
     * returned.
     *
     * @param scheme
     * 		The data scheme to look for.
     * @return True if the scheme is explicitly mentioned in the filter.
     */
    public final boolean hasDataScheme(java.lang.String scheme) {
        return (mDataSchemes != null) && mDataSchemes.contains(scheme);
    }

    /**
     * Return an iterator over the filter's data schemes.
     */
    public final java.util.Iterator<java.lang.String> schemesIterator() {
        return mDataSchemes != null ? mDataSchemes.iterator() : null;
    }

    /**
     * This is an entry for a single authority in the Iterator returned by
     * {@link #authoritiesIterator()}.
     */
    public static final class AuthorityEntry {
        private final java.lang.String mOrigHost;

        private final java.lang.String mHost;

        private final boolean mWild;

        private final int mPort;

        public AuthorityEntry(java.lang.String host, java.lang.String port) {
            mOrigHost = host;
            mWild = (host.length() > 0) && (host.charAt(0) == '*');
            mHost = (mWild) ? host.substring(1).intern() : host;
            mPort = (port != null) ? java.lang.Integer.parseInt(port) : -1;
        }

        AuthorityEntry(android.os.Parcel src) {
            mOrigHost = src.readString();
            mHost = src.readString();
            mWild = src.readInt() != 0;
            mPort = src.readInt();
        }

        void writeToParcel(android.os.Parcel dest) {
            dest.writeString(mOrigHost);
            dest.writeString(mHost);
            dest.writeInt(mWild ? 1 : 0);
            dest.writeInt(mPort);
        }

        void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            // The original host information is already contained in host and wild, no output now.
            proto.write(AuthorityEntryProto.HOST, mHost);
            proto.write(AuthorityEntryProto.WILD, mWild);
            proto.write(AuthorityEntryProto.PORT, mPort);
            proto.end(token);
        }

        public java.lang.String getHost() {
            return mOrigHost;
        }

        public int getPort() {
            return mPort;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean match(android.content.IntentFilter.AuthorityEntry other) {
            if (mWild != other.mWild) {
                return false;
            }
            if (!mHost.equals(other.mHost)) {
                return false;
            }
            if (mPort != other.mPort) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (obj instanceof android.content.IntentFilter.AuthorityEntry) {
                final android.content.IntentFilter.AuthorityEntry other = ((android.content.IntentFilter.AuthorityEntry) (obj));
                return match(other);
            }
            return false;
        }

        /**
         * Determine whether this AuthorityEntry matches the given data Uri.
         * <em>Note that this comparison is case-sensitive, unlike formal
         * RFC host names.  You thus should always normalize to lower-case.</em>
         *
         * @param data
         * 		The Uri to match.
         * @return Returns either {@link IntentFilter#NO_MATCH_DATA},
        {@link IntentFilter#MATCH_CATEGORY_PORT}, or
        {@link IntentFilter#MATCH_CATEGORY_HOST}.
         */
        public int match(android.net.Uri data) {
            java.lang.String host = data.getHost();
            if (host == null) {
                return android.content.IntentFilter.NO_MATCH_DATA;
            }
            if (false)
                android.util.Log.v("IntentFilter", (("Match host " + host) + ": ") + mHost);

            if (mWild) {
                if (host.length() < mHost.length()) {
                    return android.content.IntentFilter.NO_MATCH_DATA;
                }
                host = host.substring(host.length() - mHost.length());
            }
            if (host.compareToIgnoreCase(mHost) != 0) {
                return android.content.IntentFilter.NO_MATCH_DATA;
            }
            if (mPort >= 0) {
                if (mPort != data.getPort()) {
                    return android.content.IntentFilter.NO_MATCH_DATA;
                }
                return android.content.IntentFilter.MATCH_CATEGORY_PORT;
            }
            return android.content.IntentFilter.MATCH_CATEGORY_HOST;
        }
    }

    /**
     * Add a new Intent data "scheme specific part" to match against.  The filter must
     * include one or more schemes (via {@link #addDataScheme}) for the
     * scheme specific part to be considered.  If any scheme specific parts are
     * included in the filter, then an Intent's data must match one of
     * them.  If no scheme specific parts are included, then only the scheme must match.
     *
     * <p>The "scheme specific part" that this matches against is the string returned
     * by {@link android.net.Uri#getSchemeSpecificPart() Uri.getSchemeSpecificPart}.
     * For Uris that contain a path, this kind of matching is not generally of interest,
     * since {@link #addDataAuthority(String, String)} and
     * {@link #addDataPath(String, int)} can provide a better mechanism for matching
     * them.  However, for Uris that do not contain a path, the authority and path
     * are empty, so this is the only way to match against the non-scheme part.</p>
     *
     * @param ssp
     * 		Either a raw string that must exactly match the scheme specific part
     * 		path, or a simple pattern, depending on <var>type</var>.
     * @param type
     * 		Determines how <var>ssp</var> will be compared to
     * 		determine a match: either {@link PatternMatcher#PATTERN_LITERAL},
     * 		{@link PatternMatcher#PATTERN_PREFIX}, or
     * 		{@link PatternMatcher#PATTERN_SIMPLE_GLOB}.
     * @see #matchData
     * @see #addDataScheme
     */
    public final void addDataSchemeSpecificPart(java.lang.String ssp, int type) {
        addDataSchemeSpecificPart(new android.os.PatternMatcher(ssp, type));
    }

    /**
     *
     *
     * @unknown 
     */
    public final void addDataSchemeSpecificPart(android.os.PatternMatcher ssp) {
        if (mDataSchemeSpecificParts == null) {
            mDataSchemeSpecificParts = new java.util.ArrayList<android.os.PatternMatcher>();
        }
        mDataSchemeSpecificParts.add(ssp);
    }

    /**
     * Return the number of data scheme specific parts in the filter.
     */
    public final int countDataSchemeSpecificParts() {
        return mDataSchemeSpecificParts != null ? mDataSchemeSpecificParts.size() : 0;
    }

    /**
     * Return a data scheme specific part in the filter.
     */
    public final android.os.PatternMatcher getDataSchemeSpecificPart(int index) {
        return mDataSchemeSpecificParts.get(index);
    }

    /**
     * Is the given data scheme specific part included in the filter?  Note that if the
     * filter does not include any scheme specific parts, false will <em>always</em> be
     * returned.
     *
     * @param data
     * 		The scheme specific part that is being looked for.
     * @return Returns true if the data string matches a scheme specific part listed in the
    filter.
     */
    public final boolean hasDataSchemeSpecificPart(java.lang.String data) {
        if (mDataSchemeSpecificParts == null) {
            return false;
        }
        final int numDataSchemeSpecificParts = mDataSchemeSpecificParts.size();
        for (int i = 0; i < numDataSchemeSpecificParts; i++) {
            final android.os.PatternMatcher pe = mDataSchemeSpecificParts.get(i);
            if (pe.match(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean hasDataSchemeSpecificPart(android.os.PatternMatcher ssp) {
        if (mDataSchemeSpecificParts == null) {
            return false;
        }
        final int numDataSchemeSpecificParts = mDataSchemeSpecificParts.size();
        for (int i = 0; i < numDataSchemeSpecificParts; i++) {
            final android.os.PatternMatcher pe = mDataSchemeSpecificParts.get(i);
            if ((pe.getType() == ssp.getType()) && equals(ssp.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return an iterator over the filter's data scheme specific parts.
     */
    public final java.util.Iterator<android.os.PatternMatcher> schemeSpecificPartsIterator() {
        return mDataSchemeSpecificParts != null ? mDataSchemeSpecificParts.iterator() : null;
    }

    /**
     * Add a new Intent data authority to match against.  The filter must
     * include one or more schemes (via {@link #addDataScheme}) for the
     * authority to be considered.  If any authorities are
     * included in the filter, then an Intent's data must match one of
     * them.  If no authorities are included, then only the scheme must match.
     *
     * <p><em>Note: host name in the Android framework is
     * case-sensitive, unlike formal RFC host names.  As a result,
     * you should always write your host names with lower case letters,
     * and any host names you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * @param host
     * 		The host part of the authority to match.  May start with a
     * 		single '*' to wildcard the front of the host name.
     * @param port
     * 		Optional port part of the authority to match.  If null, any
     * 		port is allowed.
     * @see #matchData
     * @see #addDataScheme
     */
    public final void addDataAuthority(java.lang.String host, java.lang.String port) {
        if (port != null)
            port = port.intern();

        addDataAuthority(new android.content.IntentFilter.AuthorityEntry(host.intern(), port));
    }

    /**
     *
     *
     * @unknown 
     */
    public final void addDataAuthority(android.content.IntentFilter.AuthorityEntry ent) {
        if (mDataAuthorities == null)
            mDataAuthorities = new java.util.ArrayList<android.content.IntentFilter.AuthorityEntry>();

        mDataAuthorities.add(ent);
    }

    /**
     * Return the number of data authorities in the filter.
     */
    public final int countDataAuthorities() {
        return mDataAuthorities != null ? mDataAuthorities.size() : 0;
    }

    /**
     * Return a data authority in the filter.
     */
    public final android.content.IntentFilter.AuthorityEntry getDataAuthority(int index) {
        return mDataAuthorities.get(index);
    }

    /**
     * Is the given data authority included in the filter?  Note that if the
     * filter does not include any authorities, false will <em>always</em> be
     * returned.
     *
     * @param data
     * 		The data whose authority is being looked for.
     * @return Returns true if the data string matches an authority listed in the
    filter.
     */
    public final boolean hasDataAuthority(android.net.Uri data) {
        return matchDataAuthority(data) >= 0;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean hasDataAuthority(android.content.IntentFilter.AuthorityEntry auth) {
        if (mDataAuthorities == null) {
            return false;
        }
        final int numDataAuthorities = mDataAuthorities.size();
        for (int i = 0; i < numDataAuthorities; i++) {
            if (mDataAuthorities.get(i).match(auth)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return an iterator over the filter's data authorities.
     */
    public final java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authoritiesIterator() {
        return mDataAuthorities != null ? mDataAuthorities.iterator() : null;
    }

    /**
     * Add a new Intent data path to match against.  The filter must
     * include one or more schemes (via {@link #addDataScheme}) <em>and</em>
     * one or more authorities (via {@link #addDataAuthority}) for the
     * path to be considered.  If any paths are
     * included in the filter, then an Intent's data must match one of
     * them.  If no paths are included, then only the scheme/authority must
     * match.
     *
     * <p>The path given here can either be a literal that must directly
     * match or match against a prefix, or it can be a simple globbing pattern.
     * If the latter, you can use '*' anywhere in the pattern to match zero
     * or more instances of the previous character, '.' as a wildcard to match
     * any character, and '\' to escape the next character.
     *
     * @param path
     * 		Either a raw string that must exactly match the file
     * 		path, or a simple pattern, depending on <var>type</var>.
     * @param type
     * 		Determines how <var>path</var> will be compared to
     * 		determine a match: either {@link PatternMatcher#PATTERN_LITERAL},
     * 		{@link PatternMatcher#PATTERN_PREFIX}, or
     * 		{@link PatternMatcher#PATTERN_SIMPLE_GLOB}.
     * @see #matchData
     * @see #addDataScheme
     * @see #addDataAuthority
     */
    public final void addDataPath(java.lang.String path, int type) {
        addDataPath(new android.os.PatternMatcher(path.intern(), type));
    }

    /**
     *
     *
     * @unknown 
     */
    public final void addDataPath(android.os.PatternMatcher path) {
        if (mDataPaths == null)
            mDataPaths = new java.util.ArrayList<android.os.PatternMatcher>();

        mDataPaths.add(path);
    }

    /**
     * Return the number of data paths in the filter.
     */
    public final int countDataPaths() {
        return mDataPaths != null ? mDataPaths.size() : 0;
    }

    /**
     * Return a data path in the filter.
     */
    public final android.os.PatternMatcher getDataPath(int index) {
        return mDataPaths.get(index);
    }

    /**
     * Is the given data path included in the filter?  Note that if the
     * filter does not include any paths, false will <em>always</em> be
     * returned.
     *
     * @param data
     * 		The data path to look for.  This is without the scheme
     * 		prefix.
     * @return True if the data string matches a path listed in the
    filter.
     */
    public final boolean hasDataPath(java.lang.String data) {
        if (mDataPaths == null) {
            return false;
        }
        final int numDataPaths = mDataPaths.size();
        for (int i = 0; i < numDataPaths; i++) {
            final android.os.PatternMatcher pe = mDataPaths.get(i);
            if (pe.match(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean hasDataPath(android.os.PatternMatcher path) {
        if (mDataPaths == null) {
            return false;
        }
        final int numDataPaths = mDataPaths.size();
        for (int i = 0; i < numDataPaths; i++) {
            final android.os.PatternMatcher pe = mDataPaths.get(i);
            if ((pe.getType() == path.getType()) && equals(path.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return an iterator over the filter's data paths.
     */
    public final java.util.Iterator<android.os.PatternMatcher> pathsIterator() {
        return mDataPaths != null ? mDataPaths.iterator() : null;
    }

    /**
     * Match this intent filter against the given Intent data.  This ignores
     * the data scheme -- unlike {@link #matchData}, the authority will match
     * regardless of whether there is a matching scheme.
     *
     * @param data
     * 		The data whose authority is being looked for.
     * @return Returns either {@link #MATCH_CATEGORY_HOST},
    {@link #MATCH_CATEGORY_PORT}, {@link #NO_MATCH_DATA}.
     */
    public final int matchDataAuthority(android.net.Uri data) {
        if ((mDataAuthorities == null) || (data == null)) {
            return android.content.IntentFilter.NO_MATCH_DATA;
        }
        final int numDataAuthorities = mDataAuthorities.size();
        for (int i = 0; i < numDataAuthorities; i++) {
            final android.content.IntentFilter.AuthorityEntry ae = mDataAuthorities.get(i);
            int match = ae.match(data);
            if (match >= 0) {
                return match;
            }
        }
        return android.content.IntentFilter.NO_MATCH_DATA;
    }

    /**
     * Match this filter against an Intent's data (type, scheme and path). If
     * the filter does not specify any types and does not specify any
     * schemes/paths, the match will only succeed if the intent does not
     * also specify a type or data.  If the filter does not specify any schemes,
     * it will implicitly match intents with no scheme, or the schemes "content:"
     * or "file:" (basically performing a MIME-type only match).  If the filter
     * does not specify any MIME types, the Intent also must not specify a MIME
     * type.
     *
     * <p>Be aware that to match against an authority, you must also specify a base
     * scheme the authority is in.  To match against a data path, both a scheme
     * and authority must be specified.  If the filter does not specify any
     * types or schemes that it matches against, it is considered to be empty
     * (any authority or data path given is ignored, as if it were empty as
     * well).
     *
     * <p><em>Note: MIME type, Uri scheme, and host name matching in the
     * Android framework is case-sensitive, unlike the formal RFC definitions.
     * As a result, you should always write these elements with lower case letters,
     * and normalize any MIME types or Uris you receive from
     * outside of Android to ensure these elements are lower case before
     * supplying them here.</em></p>
     *
     * @param type
     * 		The desired data type to look for, as returned by
     * 		Intent.resolveType().
     * @param scheme
     * 		The desired data scheme to look for, as returned by
     * 		Intent.getScheme().
     * @param data
     * 		The full data string to match against, as supplied in
     * 		Intent.data.
     * @return Returns either a valid match constant (a combination of
    {@link #MATCH_CATEGORY_MASK} and {@link #MATCH_ADJUSTMENT_MASK}),
    or one of the error codes {@link #NO_MATCH_TYPE} if the type didn't match
    or {@link #NO_MATCH_DATA} if the scheme/path didn't match.
     * @see #match
     */
    public final int matchData(java.lang.String type, java.lang.String scheme, android.net.Uri data) {
        final java.util.ArrayList<java.lang.String> types = mDataTypes;
        final java.util.ArrayList<java.lang.String> schemes = mDataSchemes;
        int match = android.content.IntentFilter.MATCH_CATEGORY_EMPTY;
        if ((types == null) && (schemes == null)) {
            return (type == null) && (data == null) ? android.content.IntentFilter.MATCH_CATEGORY_EMPTY + android.content.IntentFilter.MATCH_ADJUSTMENT_NORMAL : android.content.IntentFilter.NO_MATCH_DATA;
        }
        if (schemes != null) {
            if (schemes.contains(scheme != null ? scheme : "")) {
                match = android.content.IntentFilter.MATCH_CATEGORY_SCHEME;
            } else {
                return android.content.IntentFilter.NO_MATCH_DATA;
            }
            final java.util.ArrayList<android.os.PatternMatcher> schemeSpecificParts = mDataSchemeSpecificParts;
            if ((schemeSpecificParts != null) && (data != null)) {
                match = (hasDataSchemeSpecificPart(data.getSchemeSpecificPart())) ? android.content.IntentFilter.MATCH_CATEGORY_SCHEME_SPECIFIC_PART : android.content.IntentFilter.NO_MATCH_DATA;
            }
            if (match != android.content.IntentFilter.MATCH_CATEGORY_SCHEME_SPECIFIC_PART) {
                // If there isn't any matching ssp, we need to match an authority.
                final java.util.ArrayList<android.content.IntentFilter.AuthorityEntry> authorities = mDataAuthorities;
                if (authorities != null) {
                    int authMatch = matchDataAuthority(data);
                    if (authMatch >= 0) {
                        final java.util.ArrayList<android.os.PatternMatcher> paths = mDataPaths;
                        if (paths == null) {
                            match = authMatch;
                        } else
                            if (hasDataPath(data.getPath())) {
                                match = android.content.IntentFilter.MATCH_CATEGORY_PATH;
                            } else {
                                return android.content.IntentFilter.NO_MATCH_DATA;
                            }

                    } else {
                        return android.content.IntentFilter.NO_MATCH_DATA;
                    }
                }
            }
            // If neither an ssp nor an authority matched, we're done.
            if (match == android.content.IntentFilter.NO_MATCH_DATA) {
                return android.content.IntentFilter.NO_MATCH_DATA;
            }
        } else {
            // Special case: match either an Intent with no data URI,
            // or with a scheme: URI.  This is to give a convenience for
            // the common case where you want to deal with data in a
            // content provider, which is done by type, and we don't want
            // to force everyone to say they handle content: or file: URIs.
            if ((((scheme != null) && (!"".equals(scheme))) && (!"content".equals(scheme))) && (!"file".equals(scheme))) {
                return android.content.IntentFilter.NO_MATCH_DATA;
            }
        }
        if (types != null) {
            if (findMimeType(type)) {
                match = android.content.IntentFilter.MATCH_CATEGORY_TYPE;
            } else {
                return android.content.IntentFilter.NO_MATCH_TYPE;
            }
        } else {
            // If no MIME types are specified, then we will only match against
            // an Intent that does not have a MIME type.
            if (type != null) {
                return android.content.IntentFilter.NO_MATCH_TYPE;
            }
        }
        return match + android.content.IntentFilter.MATCH_ADJUSTMENT_NORMAL;
    }

    /**
     * Add a new Intent category to match against.  The semantics of
     * categories is the opposite of actions -- an Intent includes the
     * categories that it requires, all of which must be included in the
     * filter in order to match.  In other words, adding a category to the
     * filter has no impact on matching unless that category is specified in
     * the intent.
     *
     * @param category
     * 		Name of category to match, such as Intent.CATEGORY_EMBED.
     */
    public final void addCategory(java.lang.String category) {
        if (mCategories == null)
            mCategories = new java.util.ArrayList<java.lang.String>();

        if (!mCategories.contains(category)) {
            mCategories.add(category.intern());
        }
    }

    /**
     * Return the number of categories in the filter.
     */
    public final int countCategories() {
        return mCategories != null ? mCategories.size() : 0;
    }

    /**
     * Return a category in the filter.
     */
    public final java.lang.String getCategory(int index) {
        return mCategories.get(index);
    }

    /**
     * Is the given category included in the filter?
     *
     * @param category
     * 		The category that the filter supports.
     * @return True if the category is explicitly mentioned in the filter.
     */
    public final boolean hasCategory(java.lang.String category) {
        return (mCategories != null) && mCategories.contains(category);
    }

    /**
     * Return an iterator over the filter's categories.
     *
     * @return Iterator if this filter has categories or {@code null} if none.
     */
    public final java.util.Iterator<java.lang.String> categoriesIterator() {
        return mCategories != null ? mCategories.iterator() : null;
    }

    /**
     * Match this filter against an Intent's categories.  Each category in
     * the Intent must be specified by the filter; if any are not in the
     * filter, the match fails.
     *
     * @param categories
     * 		The categories included in the intent, as returned by
     * 		Intent.getCategories().
     * @return If all categories match (success), null; else the name of the
    first category that didn't match.
     */
    public final java.lang.String matchCategories(java.util.Set<java.lang.String> categories) {
        if (categories == null) {
            return null;
        }
        java.util.Iterator<java.lang.String> it = categories.iterator();
        if (mCategories == null) {
            return it.hasNext() ? it.next() : null;
        }
        while (it.hasNext()) {
            final java.lang.String category = it.next();
            if (!mCategories.contains(category)) {
                return category;
            }
        } 
        return null;
    }

    /**
     * Test whether this filter matches the given <var>intent</var>.
     *
     * @param intent
     * 		The Intent to compare against.
     * @param resolve
     * 		If true, the intent's type will be resolved by calling
     * 		Intent.resolveType(); otherwise a simple match against
     * 		Intent.type will be performed.
     * @param logTag
     * 		Tag to use in debugging messages.
     * @return Returns either a valid match constant (a combination of
    {@link #MATCH_CATEGORY_MASK} and {@link #MATCH_ADJUSTMENT_MASK}),
    or one of the error codes {@link #NO_MATCH_TYPE} if the type didn't match,
    {@link #NO_MATCH_DATA} if the scheme/path didn't match,
    {@link #NO_MATCH_ACTION} if the action didn't match, or
    {@link #NO_MATCH_CATEGORY} if one or more categories didn't match.
     * @see #match(String, String, String, android.net.Uri , Set, String)
     */
    public final int match(android.content.ContentResolver resolver, android.content.Intent intent, boolean resolve, java.lang.String logTag) {
        java.lang.String type = (resolve) ? intent.resolveType(resolver) : intent.getType();
        return match(intent.getAction(), type, intent.getScheme(), intent.getData(), intent.getCategories(), logTag);
    }

    /**
     * Test whether this filter matches the given intent data.  A match is
     * only successful if the actions and categories in the Intent match
     * against the filter, as described in {@link IntentFilter}; in that case,
     * the match result returned will be as per {@link #matchData}.
     *
     * @param action
     * 		The intent action to match against (Intent.getAction).
     * @param type
     * 		The intent type to match against (Intent.resolveType()).
     * @param scheme
     * 		The data scheme to match against (Intent.getScheme()).
     * @param data
     * 		The data URI to match against (Intent.getData()).
     * @param categories
     * 		The categories to match against
     * 		(Intent.getCategories()).
     * @param logTag
     * 		Tag to use in debugging messages.
     * @return Returns either a valid match constant (a combination of
    {@link #MATCH_CATEGORY_MASK} and {@link #MATCH_ADJUSTMENT_MASK}),
    or one of the error codes {@link #NO_MATCH_TYPE} if the type didn't match,
    {@link #NO_MATCH_DATA} if the scheme/path didn't match,
    {@link #NO_MATCH_ACTION} if the action didn't match, or
    {@link #NO_MATCH_CATEGORY} if one or more categories didn't match.
     * @see #matchData
     * @see Intent#getAction
     * @see Intent#resolveType
     * @see Intent#getScheme
     * @see Intent#getData
     * @see Intent#getCategories
     */
    public final int match(java.lang.String action, java.lang.String type, java.lang.String scheme, android.net.Uri data, java.util.Set<java.lang.String> categories, java.lang.String logTag) {
        if ((action != null) && (!matchAction(action))) {
            if (false)
                android.util.Log.v(logTag, (("No matching action " + action) + " for ") + this);

            return android.content.IntentFilter.NO_MATCH_ACTION;
        }
        int dataMatch = matchData(type, scheme, data);
        if (dataMatch < 0) {
            if (false) {
                if (dataMatch == android.content.IntentFilter.NO_MATCH_TYPE) {
                    android.util.Log.v(logTag, (("No matching type " + type) + " for ") + this);
                }
                if (dataMatch == android.content.IntentFilter.NO_MATCH_DATA) {
                    android.util.Log.v(logTag, (("No matching scheme/path " + data) + " for ") + this);
                }
            }
            return dataMatch;
        }
        java.lang.String categoryMismatch = matchCategories(categories);
        if (categoryMismatch != null) {
            if (false) {
                android.util.Log.v(logTag, (("No matching category " + categoryMismatch) + " for ") + this);
            }
            return android.content.IntentFilter.NO_MATCH_CATEGORY;
        }
        // It would be nice to treat container activities as more
        // important than ones that can be embedded, but this is not the way...
        if (false) {
            if (categories != null) {
                dataMatch -= mCategories.size() - categories.size();
            }
        }
        return dataMatch;
    }

    /**
     * Write the contents of the IntentFilter as an XML stream.
     */
    public void writeToXml(org.xmlpull.v1.XmlSerializer serializer) throws java.io.IOException {
        if (getAutoVerify()) {
            serializer.attribute(null, android.content.IntentFilter.AUTO_VERIFY_STR, java.lang.Boolean.toString(true));
        }
        int N = countActions();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.ACTION_STR);
            serializer.attribute(null, android.content.IntentFilter.NAME_STR, mActions.get(i));
            serializer.endTag(null, android.content.IntentFilter.ACTION_STR);
        }
        N = countCategories();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.CAT_STR);
            serializer.attribute(null, android.content.IntentFilter.NAME_STR, mCategories.get(i));
            serializer.endTag(null, android.content.IntentFilter.CAT_STR);
        }
        N = countDataTypes();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.TYPE_STR);
            java.lang.String type = mDataTypes.get(i);
            if (type.indexOf('/') < 0)
                type = type + "/*";

            serializer.attribute(null, android.content.IntentFilter.NAME_STR, type);
            serializer.endTag(null, android.content.IntentFilter.TYPE_STR);
        }
        N = countDataSchemes();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.SCHEME_STR);
            serializer.attribute(null, android.content.IntentFilter.NAME_STR, mDataSchemes.get(i));
            serializer.endTag(null, android.content.IntentFilter.SCHEME_STR);
        }
        N = countDataSchemeSpecificParts();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.SSP_STR);
            android.os.PatternMatcher pe = mDataSchemeSpecificParts.get(i);
            switch (pe.getType()) {
                case android.os.PatternMatcher.PATTERN_LITERAL :
                    serializer.attribute(null, android.content.IntentFilter.LITERAL_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_PREFIX :
                    serializer.attribute(null, android.content.IntentFilter.PREFIX_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_SIMPLE_GLOB :
                    serializer.attribute(null, android.content.IntentFilter.SGLOB_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_ADVANCED_GLOB :
                    serializer.attribute(null, android.content.IntentFilter.AGLOB_STR, pe.getPath());
                    break;
            }
            serializer.endTag(null, android.content.IntentFilter.SSP_STR);
        }
        N = countDataAuthorities();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.AUTH_STR);
            android.content.IntentFilter.AuthorityEntry ae = mDataAuthorities.get(i);
            serializer.attribute(null, android.content.IntentFilter.HOST_STR, ae.getHost());
            if (ae.getPort() >= 0) {
                serializer.attribute(null, android.content.IntentFilter.PORT_STR, java.lang.Integer.toString(ae.getPort()));
            }
            serializer.endTag(null, android.content.IntentFilter.AUTH_STR);
        }
        N = countDataPaths();
        for (int i = 0; i < N; i++) {
            serializer.startTag(null, android.content.IntentFilter.PATH_STR);
            android.os.PatternMatcher pe = mDataPaths.get(i);
            switch (pe.getType()) {
                case android.os.PatternMatcher.PATTERN_LITERAL :
                    serializer.attribute(null, android.content.IntentFilter.LITERAL_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_PREFIX :
                    serializer.attribute(null, android.content.IntentFilter.PREFIX_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_SIMPLE_GLOB :
                    serializer.attribute(null, android.content.IntentFilter.SGLOB_STR, pe.getPath());
                    break;
                case android.os.PatternMatcher.PATTERN_ADVANCED_GLOB :
                    serializer.attribute(null, android.content.IntentFilter.AGLOB_STR, pe.getPath());
                    break;
            }
            serializer.endTag(null, android.content.IntentFilter.PATH_STR);
        }
    }

    public void readFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String autoVerify = parser.getAttributeValue(null, android.content.IntentFilter.AUTO_VERIFY_STR);
        setAutoVerify(android.text.TextUtils.isEmpty(autoVerify) ? false : java.lang.Boolean.getBoolean(autoVerify));
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals(android.content.IntentFilter.ACTION_STR)) {
                java.lang.String name = parser.getAttributeValue(null, android.content.IntentFilter.NAME_STR);
                if (name != null) {
                    addAction(name);
                }
            } else
                if (tagName.equals(android.content.IntentFilter.CAT_STR)) {
                    java.lang.String name = parser.getAttributeValue(null, android.content.IntentFilter.NAME_STR);
                    if (name != null) {
                        addCategory(name);
                    }
                } else
                    if (tagName.equals(android.content.IntentFilter.TYPE_STR)) {
                        java.lang.String name = parser.getAttributeValue(null, android.content.IntentFilter.NAME_STR);
                        if (name != null) {
                            try {
                                addDataType(name);
                            } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                            }
                        }
                    } else
                        if (tagName.equals(android.content.IntentFilter.SCHEME_STR)) {
                            java.lang.String name = parser.getAttributeValue(null, android.content.IntentFilter.NAME_STR);
                            if (name != null) {
                                addDataScheme(name);
                            }
                        } else
                            if (tagName.equals(android.content.IntentFilter.SSP_STR)) {
                                java.lang.String ssp = parser.getAttributeValue(null, android.content.IntentFilter.LITERAL_STR);
                                if (ssp != null) {
                                    addDataSchemeSpecificPart(ssp, PatternMatcher.PATTERN_LITERAL);
                                } else
                                    if ((ssp = parser.getAttributeValue(null, android.content.IntentFilter.PREFIX_STR)) != null) {
                                        addDataSchemeSpecificPart(ssp, PatternMatcher.PATTERN_PREFIX);
                                    } else
                                        if ((ssp = parser.getAttributeValue(null, android.content.IntentFilter.SGLOB_STR)) != null) {
                                            addDataSchemeSpecificPart(ssp, PatternMatcher.PATTERN_SIMPLE_GLOB);
                                        } else
                                            if ((ssp = parser.getAttributeValue(null, android.content.IntentFilter.AGLOB_STR)) != null) {
                                                addDataSchemeSpecificPart(ssp, PatternMatcher.PATTERN_ADVANCED_GLOB);
                                            }



                            } else
                                if (tagName.equals(android.content.IntentFilter.AUTH_STR)) {
                                    java.lang.String host = parser.getAttributeValue(null, android.content.IntentFilter.HOST_STR);
                                    java.lang.String port = parser.getAttributeValue(null, android.content.IntentFilter.PORT_STR);
                                    if (host != null) {
                                        addDataAuthority(host, port);
                                    }
                                } else
                                    if (tagName.equals(android.content.IntentFilter.PATH_STR)) {
                                        java.lang.String path = parser.getAttributeValue(null, android.content.IntentFilter.LITERAL_STR);
                                        if (path != null) {
                                            addDataPath(path, PatternMatcher.PATTERN_LITERAL);
                                        } else
                                            if ((path = parser.getAttributeValue(null, android.content.IntentFilter.PREFIX_STR)) != null) {
                                                addDataPath(path, PatternMatcher.PATTERN_PREFIX);
                                            } else
                                                if ((path = parser.getAttributeValue(null, android.content.IntentFilter.SGLOB_STR)) != null) {
                                                    addDataPath(path, PatternMatcher.PATTERN_SIMPLE_GLOB);
                                                } else
                                                    if ((path = parser.getAttributeValue(null, android.content.IntentFilter.AGLOB_STR)) != null) {
                                                        addDataPath(path, PatternMatcher.PATTERN_ADVANCED_GLOB);
                                                    }



                                    } else {
                                        android.util.Log.w("IntentFilter", "Unknown tag parsing IntentFilter: " + tagName);
                                    }






            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        } 
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (mActions.size() > 0) {
            java.util.Iterator<java.lang.String> it = mActions.iterator();
            while (it.hasNext()) {
                proto.write(IntentFilterProto.ACTIONS, it.next());
            } 
        }
        if (mCategories != null) {
            java.util.Iterator<java.lang.String> it = mCategories.iterator();
            while (it.hasNext()) {
                proto.write(IntentFilterProto.CATEGORIES, it.next());
            } 
        }
        if (mDataSchemes != null) {
            java.util.Iterator<java.lang.String> it = mDataSchemes.iterator();
            while (it.hasNext()) {
                proto.write(IntentFilterProto.DATA_SCHEMES, it.next());
            } 
        }
        if (mDataSchemeSpecificParts != null) {
            java.util.Iterator<android.os.PatternMatcher> it = mDataSchemeSpecificParts.iterator();
            while (it.hasNext()) {
                writeToProto(proto, IntentFilterProto.DATA_SCHEME_SPECS);
            } 
        }
        if (mDataAuthorities != null) {
            java.util.Iterator<android.content.IntentFilter.AuthorityEntry> it = mDataAuthorities.iterator();
            while (it.hasNext()) {
                it.next().writeToProto(proto, IntentFilterProto.DATA_AUTHORITIES);
            } 
        }
        if (mDataPaths != null) {
            java.util.Iterator<android.os.PatternMatcher> it = mDataPaths.iterator();
            while (it.hasNext()) {
                writeToProto(proto, IntentFilterProto.DATA_PATHS);
            } 
        }
        if (mDataTypes != null) {
            java.util.Iterator<java.lang.String> it = mDataTypes.iterator();
            while (it.hasNext()) {
                proto.write(IntentFilterProto.DATA_TYPES, it.next());
            } 
        }
        if ((mPriority != 0) || mHasPartialTypes) {
            proto.write(IntentFilterProto.PRIORITY, mPriority);
            proto.write(IntentFilterProto.HAS_PARTIAL_TYPES, mHasPartialTypes);
        }
        proto.write(IntentFilterProto.GET_AUTO_VERIFY, getAutoVerify());
        proto.end(token);
    }

    public void dump(android.util.Printer du, java.lang.String prefix) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(256);
        if (mActions.size() > 0) {
            java.util.Iterator<java.lang.String> it = mActions.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Action: \"");
                sb.append(it.next());
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (mCategories != null) {
            java.util.Iterator<java.lang.String> it = mCategories.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Category: \"");
                sb.append(it.next());
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (mDataSchemes != null) {
            java.util.Iterator<java.lang.String> it = mDataSchemes.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Scheme: \"");
                sb.append(it.next());
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (mDataSchemeSpecificParts != null) {
            java.util.Iterator<android.os.PatternMatcher> it = mDataSchemeSpecificParts.iterator();
            while (it.hasNext()) {
                android.os.PatternMatcher pe = it.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Ssp: \"");
                sb.append(pe);
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (mDataAuthorities != null) {
            java.util.Iterator<android.content.IntentFilter.AuthorityEntry> it = mDataAuthorities.iterator();
            while (it.hasNext()) {
                android.content.IntentFilter.AuthorityEntry ae = it.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Authority: \"");
                sb.append(ae.mHost);
                sb.append("\": ");
                sb.append(ae.mPort);
                if (ae.mWild)
                    sb.append(" WILD");

                du.println(sb.toString());
            } 
        }
        if (mDataPaths != null) {
            java.util.Iterator<android.os.PatternMatcher> it = mDataPaths.iterator();
            while (it.hasNext()) {
                android.os.PatternMatcher pe = it.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Path: \"");
                sb.append(pe);
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (mDataTypes != null) {
            java.util.Iterator<java.lang.String> it = mDataTypes.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Type: \"");
                sb.append(it.next());
                sb.append("\"");
                du.println(sb.toString());
            } 
        }
        if (((mPriority != 0) || (mOrder != 0)) || mHasPartialTypes) {
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPriority=");
            sb.append(mPriority);
            sb.append(", mOrder=");
            sb.append(mOrder);
            sb.append(", mHasPartialTypes=");
            sb.append(mHasPartialTypes);
            du.println(sb.toString());
        }
        if (getAutoVerify()) {
            sb.setLength(0);
            sb.append(prefix);
            sb.append("AutoVerify=");
            sb.append(getAutoVerify());
            du.println(sb.toString());
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.IntentFilter> CREATOR = new android.os.Parcelable.Creator<android.content.IntentFilter>() {
        public android.content.IntentFilter createFromParcel(android.os.Parcel source) {
            return new android.content.IntentFilter(source);
        }

        public android.content.IntentFilter[] newArray(int size) {
            return new android.content.IntentFilter[size];
        }
    };

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStringList(mActions);
        if (mCategories != null) {
            dest.writeInt(1);
            dest.writeStringList(mCategories);
        } else {
            dest.writeInt(0);
        }
        if (mDataSchemes != null) {
            dest.writeInt(1);
            dest.writeStringList(mDataSchemes);
        } else {
            dest.writeInt(0);
        }
        if (mDataTypes != null) {
            dest.writeInt(1);
            dest.writeStringList(mDataTypes);
        } else {
            dest.writeInt(0);
        }
        if (mDataSchemeSpecificParts != null) {
            final int N = mDataSchemeSpecificParts.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                writeToParcel(dest, flags);
            }
        } else {
            dest.writeInt(0);
        }
        if (mDataAuthorities != null) {
            final int N = mDataAuthorities.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                mDataAuthorities.get(i).writeToParcel(dest);
            }
        } else {
            dest.writeInt(0);
        }
        if (mDataPaths != null) {
            final int N = mDataPaths.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                writeToParcel(dest, flags);
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mPriority);
        dest.writeInt(mHasPartialTypes ? 1 : 0);
        dest.writeInt(getAutoVerify() ? 1 : 0);
        dest.writeInt(mInstantAppVisibility);
        dest.writeInt(mOrder);
    }

    /**
     * For debugging -- perform a check on the filter, return true if it passed
     * or false if it failed.
     *
     * {@hide }
     */
    public boolean debugCheck() {
        return true;
        // This code looks for intent filters that do not specify data.
        /* if (mActions != null && mActions.size() == 1
        && mActions.contains(Intent.ACTION_MAIN)) {
        return true;
        }

        if (mDataTypes == null && mDataSchemes == null) {
        Log.w("IntentFilter", "QUESTIONABLE INTENT FILTER:");
        dump(Log.WARN, "IntentFilter", "  ");
        return false;
        }

        return true;
         */
    }

    /**
     *
     *
     * @unknown 
     */
    public IntentFilter(android.os.Parcel source) {
        mActions = new java.util.ArrayList<java.lang.String>();
        source.readStringList(mActions);
        if (source.readInt() != 0) {
            mCategories = new java.util.ArrayList<java.lang.String>();
            source.readStringList(mCategories);
        }
        if (source.readInt() != 0) {
            mDataSchemes = new java.util.ArrayList<java.lang.String>();
            source.readStringList(mDataSchemes);
        }
        if (source.readInt() != 0) {
            mDataTypes = new java.util.ArrayList<java.lang.String>();
            source.readStringList(mDataTypes);
        }
        int N = source.readInt();
        if (N > 0) {
            mDataSchemeSpecificParts = new java.util.ArrayList<android.os.PatternMatcher>(N);
            for (int i = 0; i < N; i++) {
                mDataSchemeSpecificParts.add(new android.os.PatternMatcher(source));
            }
        }
        N = source.readInt();
        if (N > 0) {
            mDataAuthorities = new java.util.ArrayList<android.content.IntentFilter.AuthorityEntry>(N);
            for (int i = 0; i < N; i++) {
                mDataAuthorities.add(new android.content.IntentFilter.AuthorityEntry(source));
            }
        }
        N = source.readInt();
        if (N > 0) {
            mDataPaths = new java.util.ArrayList<android.os.PatternMatcher>(N);
            for (int i = 0; i < N; i++) {
                mDataPaths.add(new android.os.PatternMatcher(source));
            }
        }
        mPriority = source.readInt();
        mHasPartialTypes = source.readInt() > 0;
        setAutoVerify(source.readInt() > 0);
        setVisibilityToInstantApp(source.readInt());
        mOrder = source.readInt();
    }

    private final boolean findMimeType(java.lang.String type) {
        final java.util.ArrayList<java.lang.String> t = mDataTypes;
        if (type == null) {
            return false;
        }
        if (t.contains(type)) {
            return true;
        }
        // Deal with an Intent wanting to match every type in the IntentFilter.
        final int typeLength = type.length();
        if ((typeLength == 3) && type.equals("*/*")) {
            return !t.isEmpty();
        }
        // Deal with this IntentFilter wanting to match every Intent type.
        if (mHasPartialTypes && t.contains("*")) {
            return true;
        }
        final int slashpos = type.indexOf('/');
        if (slashpos > 0) {
            if (mHasPartialTypes && t.contains(type.substring(0, slashpos))) {
                return true;
            }
            if ((typeLength == (slashpos + 2)) && (type.charAt(slashpos + 1) == '*')) {
                // Need to look through all types for one that matches
                // our base...
                final int numTypes = t.size();
                for (int i = 0; i < numTypes; i++) {
                    final java.lang.String v = t.get(i);
                    if (type.regionMatches(0, v, 0, slashpos + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.ArrayList<java.lang.String> getHostsList() {
        java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>();
        java.util.Iterator<android.content.IntentFilter.AuthorityEntry> it = authoritiesIterator();
        if (it != null) {
            while (it.hasNext()) {
                android.content.IntentFilter.AuthorityEntry entry = it.next();
                result.add(entry.getHost());
            } 
        }
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String[] getHosts() {
        java.util.ArrayList<java.lang.String> list = getHostsList();
        return list.toArray(new java.lang.String[list.size()]);
    }
}

