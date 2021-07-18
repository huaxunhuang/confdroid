/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.text.util;


/**
 * Linkify take a piece of text and a regular expression and turns all of the
 *  regex matches in the text into clickable links.  This is particularly
 *  useful for matching things like email addresses, web URLs, etc. and making
 *  them actionable.
 *
 *  Alone with the pattern that is to be matched, a URL scheme prefix is also
 *  required.  Any pattern match that does not begin with the supplied scheme
 *  will have the scheme prepended to the matched text when the clickable URL
 *  is created.  For instance, if you are matching web URLs you would supply
 *  the scheme <code>http://</code>. If the pattern matches example.com, which
 *  does not have a URL scheme prefix, the supplied scheme will be prepended to
 *  create <code>http://example.com</code> when the clickable URL link is
 *  created.
 */
public class Linkify {
    /**
     * Bit field indicating that web URLs should be matched in methods that
     *  take an options mask
     */
    public static final int WEB_URLS = 0x1;

    /**
     * Bit field indicating that email addresses should be matched in methods
     *  that take an options mask
     */
    public static final int EMAIL_ADDRESSES = 0x2;

    /**
     * Bit field indicating that phone numbers should be matched in methods that
     *  take an options mask
     */
    public static final int PHONE_NUMBERS = 0x4;

    /**
     * Bit field indicating that street addresses should be matched in methods that
     *  take an options mask. Note that this uses the
     *  {@link android.webkit.WebView#findAddress(String) findAddress()} method in
     *  {@link android.webkit.WebView} for finding addresses, which has various
     *  limitations.
     */
    public static final int MAP_ADDRESSES = 0x8;

    /**
     * Bit mask indicating that all available patterns should be matched in
     *  methods that take an options mask
     */
    public static final int ALL = ((android.text.util.Linkify.WEB_URLS | android.text.util.Linkify.EMAIL_ADDRESSES) | android.text.util.Linkify.PHONE_NUMBERS) | android.text.util.Linkify.MAP_ADDRESSES;

    /**
     * Don't treat anything with fewer than this many digits as a
     * phone number.
     */
    private static final int PHONE_NUMBER_MINIMUM_DIGITS = 5;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, value = { android.text.util.Linkify.WEB_URLS, android.text.util.Linkify.EMAIL_ADDRESSES, android.text.util.Linkify.PHONE_NUMBERS, android.text.util.Linkify.MAP_ADDRESSES, android.text.util.Linkify.ALL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface LinkifyMask {}

    /**
     * Filters out web URL matches that occur after an at-sign (@).  This is
     *  to prevent turning the domain name in an email address into a web link.
     */
    public static final android.text.util.Linkify.MatchFilter sUrlMatchFilter = new android.text.util.Linkify.MatchFilter() {
        public final boolean acceptMatch(java.lang.CharSequence s, int start, int end) {
            if (start == 0) {
                return true;
            }
            if (s.charAt(start - 1) == '@') {
                return false;
            }
            return true;
        }
    };

    /**
     * Filters out URL matches that don't have enough digits to be a
     *  phone number.
     */
    public static final android.text.util.Linkify.MatchFilter sPhoneNumberMatchFilter = new android.text.util.Linkify.MatchFilter() {
        public final boolean acceptMatch(java.lang.CharSequence s, int start, int end) {
            int digitCount = 0;
            for (int i = start; i < end; i++) {
                if (java.lang.Character.isDigit(s.charAt(i))) {
                    digitCount++;
                    if (digitCount >= android.text.util.Linkify.PHONE_NUMBER_MINIMUM_DIGITS) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    /**
     * Transforms matched phone number text into something suitable
     *  to be used in a tel: URL.  It does this by removing everything
     *  but the digits and plus signs.  For instance:
     *  &apos;+1 (919) 555-1212&apos;
     *  becomes &apos;+19195551212&apos;
     */
    public static final android.text.util.Linkify.TransformFilter sPhoneNumberTransformFilter = new android.text.util.Linkify.TransformFilter() {
        public final java.lang.String transformUrl(final java.util.regex.Matcher match, java.lang.String url) {
            return android.util.Patterns.digitsAndPlusOnly(match);
        }
    };

    /**
     * MatchFilter enables client code to have more control over
     *  what is allowed to match and become a link, and what is not.
     *
     *  For example:  when matching web URLs you would like things like
     *  http://www.example.com to match, as well as just example.com itelf.
     *  However, you would not want to match against the domain in
     *  support@example.com.  So, when matching against a web URL pattern you
     *  might also include a MatchFilter that disallows the match if it is
     *  immediately preceded by an at-sign (@).
     */
    public interface MatchFilter {
        /**
         * Examines the character span matched by the pattern and determines
         *  if the match should be turned into an actionable link.
         *
         * @param s
         * 		The body of text against which the pattern
         * 		was matched
         * @param start
         * 		The index of the first character in s that was
         * 		matched by the pattern - inclusive
         * @param end
         * 		The index of the last character in s that was
         * 		matched - exclusive
         * @return Whether this match should be turned into a link
         */
        boolean acceptMatch(java.lang.CharSequence s, int start, int end);
    }

    /**
     * TransformFilter enables client code to have more control over
     *  how matched patterns are represented as URLs.
     *
     *  For example:  when converting a phone number such as (919)  555-1212
     *  into a tel: URL the parentheses, white space, and hyphen need to be
     *  removed to produce tel:9195551212.
     */
    public interface TransformFilter {
        /**
         * Examines the matched text and either passes it through or uses the
         *  data in the Matcher state to produce a replacement.
         *
         * @param match
         * 		The regex matcher state that found this URL text
         * @param url
         * 		The text that was matched
         * @return The transformed form of the URL
         */
        java.lang.String transformUrl(final java.util.regex.Matcher match, java.lang.String url);
    }

    /**
     * Scans the text of the provided Spannable and turns all occurrences
     *  of the link types indicated in the mask into clickable links.
     *  If the mask is nonzero, it also removes any existing URLSpans
     *  attached to the Spannable, to avoid problems if you call it
     *  repeatedly on the same text.
     *
     * @param text
     * 		Spannable whose text is to be marked-up with links
     * @param mask
     * 		Mask to define which kinds of links will be searched.
     * @return True if at least one link is found and applied.
     */
    public static final boolean addLinks(@android.annotation.NonNull
    android.text.Spannable text, @android.text.util.Linkify.LinkifyMask
    int mask) {
        if (mask == 0) {
            return false;
        }
        android.text.style.URLSpan[] old = text.getSpans(0, text.length(), android.text.style.URLSpan.class);
        for (int i = old.length - 1; i >= 0; i--) {
            text.removeSpan(old[i]);
        }
        java.util.ArrayList<android.text.util.LinkSpec> links = new java.util.ArrayList<android.text.util.LinkSpec>();
        if ((mask & android.text.util.Linkify.WEB_URLS) != 0) {
            android.text.util.Linkify.gatherLinks(links, text, android.util.Patterns.AUTOLINK_WEB_URL, new java.lang.String[]{ "http://", "https://", "rtsp://" }, android.text.util.Linkify.sUrlMatchFilter, null);
        }
        if ((mask & android.text.util.Linkify.EMAIL_ADDRESSES) != 0) {
            android.text.util.Linkify.gatherLinks(links, text, android.util.Patterns.AUTOLINK_EMAIL_ADDRESS, new java.lang.String[]{ "mailto:" }, null, null);
        }
        if ((mask & android.text.util.Linkify.PHONE_NUMBERS) != 0) {
            android.text.util.Linkify.gatherTelLinks(links, text);
        }
        if ((mask & android.text.util.Linkify.MAP_ADDRESSES) != 0) {
            android.text.util.Linkify.gatherMapLinks(links, text);
        }
        android.text.util.Linkify.pruneOverlaps(links);
        if (links.size() == 0) {
            return false;
        }
        for (android.text.util.LinkSpec link : links) {
            android.text.util.Linkify.applyLink(link.url, link.start, link.end, text);
        }
        return true;
    }

    /**
     * Scans the text of the provided TextView and turns all occurrences of
     *  the link types indicated in the mask into clickable links.  If matches
     *  are found the movement method for the TextView is set to
     *  LinkMovementMethod.
     *
     * @param text
     * 		TextView whose text is to be marked-up with links
     * @param mask
     * 		Mask to define which kinds of links will be searched.
     * @return True if at least one link is found and applied.
     */
    public static final boolean addLinks(@android.annotation.NonNull
    android.widget.TextView text, @android.text.util.Linkify.LinkifyMask
    int mask) {
        if (mask == 0) {
            return false;
        }
        java.lang.CharSequence t = text.getText();
        if (t instanceof android.text.Spannable) {
            if (android.text.util.Linkify.addLinks(((android.text.Spannable) (t)), mask)) {
                android.text.util.Linkify.addLinkMovementMethod(text);
                return true;
            }
            return false;
        } else {
            android.text.SpannableString s = android.text.SpannableString.valueOf(t);
            if (android.text.util.Linkify.addLinks(s, mask)) {
                android.text.util.Linkify.addLinkMovementMethod(text);
                text.setText(s);
                return true;
            }
            return false;
        }
    }

    private static final void addLinkMovementMethod(@android.annotation.NonNull
    android.widget.TextView t) {
        android.text.method.MovementMethod m = t.getMovementMethod();
        if ((m == null) || (!(m instanceof android.text.method.LinkMovementMethod))) {
            if (t.getLinksClickable()) {
                t.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
            }
        }
    }

    /**
     * Applies a regex to the text of a TextView turning the matches into
     *  links.  If links are found then UrlSpans are applied to the link
     *  text match areas, and the movement method for the text is changed
     *  to LinkMovementMethod.
     *
     * @param text
     * 		TextView whose text is to be marked-up with links
     * @param pattern
     * 		Regex pattern to be used for finding links
     * @param scheme
     * 		URL scheme string (eg <code>http://</code>) to be
     * 		prepended to the links that do not start with this scheme.
     */
    public static final void addLinks(@android.annotation.NonNull
    android.widget.TextView text, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String scheme) {
        android.text.util.Linkify.addLinks(text, pattern, scheme, null, null, null);
    }

    /**
     * Applies a regex to the text of a TextView turning the matches into
     *  links.  If links are found then UrlSpans are applied to the link
     *  text match areas, and the movement method for the text is changed
     *  to LinkMovementMethod.
     *
     * @param text
     * 		TextView whose text is to be marked-up with links
     * @param pattern
     * 		Regex pattern to be used for finding links
     * @param scheme
     * 		URL scheme string (eg <code>http://</code>) to be
     * 		prepended to the links that do not start with this scheme.
     * @param matchFilter
     * 		The filter that is used to allow the client code
     * 		additional control over which pattern matches are
     * 		to be converted into links.
     */
    public static final void addLinks(@android.annotation.NonNull
    android.widget.TextView text, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String scheme, @android.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        android.text.util.Linkify.addLinks(text, pattern, scheme, null, matchFilter, transformFilter);
    }

    /**
     * Applies a regex to the text of a TextView turning the matches into
     *  links.  If links are found then UrlSpans are applied to the link
     *  text match areas, and the movement method for the text is changed
     *  to LinkMovementMethod.
     *
     * @param text
     * 		TextView whose text is to be marked-up with links.
     * @param pattern
     * 		Regex pattern to be used for finding links.
     * @param defaultScheme
     * 		The default scheme to be prepended to links if the link does not
     * 		start with one of the <code>schemes</code> given.
     * @param schemes
     * 		Array of schemes (eg <code>http://</code>) to check if the link found
     * 		contains a scheme. Passing a null or empty value means prepend defaultScheme
     * 		to all links.
     * @param matchFilter
     * 		The filter that is used to allow the client code additional control
     * 		over which pattern matches are to be converted into links.
     * @param transformFilter
     * 		Filter to allow the client code to update the link found.
     */
    public static final void addLinks(@android.annotation.NonNull
    android.widget.TextView text, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String defaultScheme, @android.annotation.Nullable
    java.lang.String[] schemes, @android.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        android.text.SpannableString spannable = android.text.SpannableString.valueOf(text.getText());
        boolean linksAdded = android.text.util.Linkify.addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        if (linksAdded) {
            text.setText(spannable);
            android.text.util.Linkify.addLinkMovementMethod(text);
        }
    }

    /**
     * Applies a regex to a Spannable turning the matches into
     *  links.
     *
     * @param text
     * 		Spannable whose text is to be marked-up with links
     * @param pattern
     * 		Regex pattern to be used for finding links
     * @param scheme
     * 		URL scheme string (eg <code>http://</code>) to be
     * 		prepended to the links that do not start with this scheme.
     */
    public static final boolean addLinks(@android.annotation.NonNull
    android.text.Spannable text, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String scheme) {
        return android.text.util.Linkify.addLinks(text, pattern, scheme, null, null, null);
    }

    /**
     * Applies a regex to a Spannable turning the matches into
     * links.
     *
     * @param spannable
     * 		Spannable whose text is to be marked-up with links
     * @param pattern
     * 		Regex pattern to be used for finding links
     * @param scheme
     * 		URL scheme string (eg <code>http://</code>) to be
     * 		prepended to the links that do not start with this scheme.
     * @param matchFilter
     * 		The filter that is used to allow the client code
     * 		additional control over which pattern matches are
     * 		to be converted into links.
     * @param transformFilter
     * 		Filter to allow the client code to update the link found.
     * @return True if at least one link is found and applied.
     */
    public static final boolean addLinks(@android.annotation.NonNull
    android.text.Spannable spannable, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String scheme, @android.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        return android.text.util.Linkify.addLinks(spannable, pattern, scheme, null, matchFilter, transformFilter);
    }

    /**
     * Applies a regex to a Spannable turning the matches into links.
     *
     * @param spannable
     * 		Spannable whose text is to be marked-up with links.
     * @param pattern
     * 		Regex pattern to be used for finding links.
     * @param defaultScheme
     * 		The default scheme to be prepended to links if the link does not
     * 		start with one of the <code>schemes</code> given.
     * @param schemes
     * 		Array of schemes (eg <code>http://</code>) to check if the link found
     * 		contains a scheme. Passing a null or empty value means prepend defaultScheme
     * 		to all links.
     * @param matchFilter
     * 		The filter that is used to allow the client code additional control
     * 		over which pattern matches are to be converted into links.
     * @param transformFilter
     * 		Filter to allow the client code to update the link found.
     * @return True if at least one link is found and applied.
     */
    public static final boolean addLinks(@android.annotation.NonNull
    android.text.Spannable spannable, @android.annotation.NonNull
    java.util.regex.Pattern pattern, @android.annotation.Nullable
    java.lang.String defaultScheme, @android.annotation.Nullable
    java.lang.String[] schemes, @android.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        final java.lang.String[] schemesCopy;
        if (defaultScheme == null)
            defaultScheme = "";

        if ((schemes == null) || (schemes.length < 1)) {
            schemes = libcore.util.EmptyArray.STRING;
        }
        schemesCopy = new java.lang.String[schemes.length + 1];
        schemesCopy[0] = defaultScheme.toLowerCase(java.util.Locale.ROOT);
        for (int index = 0; index < schemes.length; index++) {
            java.lang.String scheme = schemes[index];
            schemesCopy[index + 1] = (scheme == null) ? "" : scheme.toLowerCase(java.util.Locale.ROOT);
        }
        boolean hasMatches = false;
        java.util.regex.Matcher m = pattern.matcher(spannable);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            boolean allowed = true;
            if (matchFilter != null) {
                allowed = matchFilter.acceptMatch(spannable, start, end);
            }
            if (allowed) {
                java.lang.String url = android.text.util.Linkify.makeUrl(m.group(0), schemesCopy, m, transformFilter);
                android.text.util.Linkify.applyLink(url, start, end, spannable);
                hasMatches = true;
            }
        } 
        return hasMatches;
    }

    private static final void applyLink(java.lang.String url, int start, int end, android.text.Spannable text) {
        android.text.style.URLSpan span = new android.text.style.URLSpan(url);
        text.setSpan(span, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static final java.lang.String makeUrl(@android.annotation.NonNull
    java.lang.String url, @android.annotation.NonNull
    java.lang.String[] prefixes, java.util.regex.Matcher matcher, @android.annotation.Nullable
    android.text.util.Linkify.TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(matcher, url);
        }
        boolean hasPrefix = false;
        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0, prefixes[i].length())) {
                hasPrefix = true;
                // Fix capitalization if necessary
                if (!url.regionMatches(false, 0, prefixes[i], 0, prefixes[i].length())) {
                    url = prefixes[i] + url.substring(prefixes[i].length());
                }
                break;
            }
        }
        if ((!hasPrefix) && (prefixes.length > 0)) {
            url = prefixes[0] + url;
        }
        return url;
    }

    private static final void gatherLinks(java.util.ArrayList<android.text.util.LinkSpec> links, android.text.Spannable s, java.util.regex.Pattern pattern, java.lang.String[] schemes, android.text.util.Linkify.MatchFilter matchFilter, android.text.util.Linkify.TransformFilter transformFilter) {
        java.util.regex.Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if ((matchFilter == null) || matchFilter.acceptMatch(s, start, end)) {
                android.text.util.LinkSpec spec = new android.text.util.LinkSpec();
                java.lang.String url = android.text.util.Linkify.makeUrl(m.group(0), schemes, m, transformFilter);
                spec.url = url;
                spec.start = start;
                spec.end = end;
                links.add(spec);
            }
        } 
    }

    private static final void gatherTelLinks(java.util.ArrayList<android.text.util.LinkSpec> links, android.text.Spannable s) {
        com.android.i18n.phonenumbers.PhoneNumberUtil phoneUtil = com.android.i18n.phonenumbers.PhoneNumberUtil.getInstance();
        java.lang.Iterable<com.android.i18n.phonenumbers.PhoneNumberMatch> matches = phoneUtil.findNumbers(s.toString(), java.util.Locale.getDefault().getCountry(), Leniency.POSSIBLE, java.lang.Long.MAX_VALUE);
        for (com.android.i18n.phonenumbers.PhoneNumberMatch match : matches) {
            android.text.util.LinkSpec spec = new android.text.util.LinkSpec();
            spec.url = "tel:" + android.telephony.PhoneNumberUtils.normalizeNumber(match.rawString());
            spec.start = match.start();
            spec.end = match.end();
            links.add(spec);
        }
    }

    private static final void gatherMapLinks(java.util.ArrayList<android.text.util.LinkSpec> links, android.text.Spannable s) {
        java.lang.String string = s.toString();
        java.lang.String address;
        int base = 0;
        try {
            while ((address = android.webkit.WebView.findAddress(string)) != null) {
                int start = string.indexOf(address);
                if (start < 0) {
                    break;
                }
                android.text.util.LinkSpec spec = new android.text.util.LinkSpec();
                int length = address.length();
                int end = start + length;
                spec.start = base + start;
                spec.end = base + end;
                string = string.substring(end);
                base += end;
                java.lang.String encodedAddress = null;
                try {
                    encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    continue;
                }
                spec.url = "geo:0,0?q=" + encodedAddress;
                links.add(spec);
            } 
        } catch (java.lang.UnsupportedOperationException e) {
            // findAddress may fail with an unsupported exception on platforms without a WebView.
            // In this case, we will not append anything to the links variable: it would have died
            // in WebView.findAddress.
            return;
        }
    }

    private static final void pruneOverlaps(java.util.ArrayList<android.text.util.LinkSpec> links) {
        java.util.Comparator<android.text.util.LinkSpec> c = new java.util.Comparator<android.text.util.LinkSpec>() {
            public final int compare(android.text.util.LinkSpec a, android.text.util.LinkSpec b) {
                if (a.start < b.start) {
                    return -1;
                }
                if (a.start > b.start) {
                    return 1;
                }
                if (a.end < b.end) {
                    return 1;
                }
                if (a.end > b.end) {
                    return -1;
                }
                return 0;
            }
        };
        java.util.Collections.sort(links, c);
        int len = links.size();
        int i = 0;
        while (i < (len - 1)) {
            android.text.util.LinkSpec a = links.get(i);
            android.text.util.LinkSpec b = links.get(i + 1);
            int remove = -1;
            if ((a.start <= b.start) && (a.end > b.start)) {
                if (b.end <= a.end) {
                    remove = i + 1;
                } else
                    if ((a.end - a.start) > (b.end - b.start)) {
                        remove = i + 1;
                    } else
                        if ((a.end - a.start) < (b.end - b.start)) {
                            remove = i;
                        }


                if (remove != (-1)) {
                    links.remove(remove);
                    len--;
                    continue;
                }
            }
            i++;
        } 
    }
}

