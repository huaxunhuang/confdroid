/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.text.util;


/**
 * LinkifyCompat brings in {@code Linkify} improvements for URLs and email addresses to older API
 * levels.
 */
public final class LinkifyCompat {
    private static final java.lang.String[] EMPTY_STRING = new java.lang.String[0];

    private static final java.util.Comparator<android.support.v4.text.util.LinkifyCompat.LinkSpec> COMPARATOR = new java.util.Comparator<android.support.v4.text.util.LinkifyCompat.LinkSpec>() {
        @java.lang.Override
        public final int compare(android.support.v4.text.util.LinkifyCompat.LinkSpec a, android.support.v4.text.util.LinkifyCompat.LinkSpec b) {
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

    @android.support.annotation.IntDef(flag = true, value = { android.text.util.Linkify.WEB_URLS, android.text.util.Linkify.EMAIL_ADDRESSES, android.text.util.Linkify.PHONE_NUMBERS, android.text.util.Linkify.MAP_ADDRESSES, android.text.util.Linkify.ALL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface LinkifyMask {}

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
    public static final boolean addLinks(@android.support.annotation.NonNull
    android.text.Spannable text, @android.support.v4.text.util.LinkifyCompat.LinkifyMask
    int mask) {
        if (mask == 0) {
            return false;
        }
        android.text.style.URLSpan[] old = text.getSpans(0, text.length(), android.text.style.URLSpan.class);
        for (int i = old.length - 1; i >= 0; i--) {
            text.removeSpan(old[i]);
        }
        // Use framework to linkify phone numbers.
        boolean frameworkReturn = false;
        if ((mask & android.text.util.Linkify.PHONE_NUMBERS) != 0) {
            frameworkReturn = android.text.util.Linkify.addLinks(text, android.text.util.Linkify.PHONE_NUMBERS);
        }
        java.util.ArrayList<android.support.v4.text.util.LinkifyCompat.LinkSpec> links = new java.util.ArrayList<android.support.v4.text.util.LinkifyCompat.LinkSpec>();
        if ((mask & android.text.util.Linkify.WEB_URLS) != 0) {
            android.support.v4.text.util.LinkifyCompat.gatherLinks(links, text, android.support.v4.util.PatternsCompat.AUTOLINK_WEB_URL, new java.lang.String[]{ "http://", "https://", "rtsp://" }, android.text.util.Linkify.sUrlMatchFilter, null);
        }
        if ((mask & android.text.util.Linkify.EMAIL_ADDRESSES) != 0) {
            android.support.v4.text.util.LinkifyCompat.gatherLinks(links, text, android.support.v4.util.PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new java.lang.String[]{ "mailto:" }, null, null);
        }
        if ((mask & android.text.util.Linkify.MAP_ADDRESSES) != 0) {
            android.support.v4.text.util.LinkifyCompat.gatherMapLinks(links, text);
        }
        android.support.v4.text.util.LinkifyCompat.pruneOverlaps(links, text);
        if (links.size() == 0) {
            return false;
        }
        for (android.support.v4.text.util.LinkifyCompat.LinkSpec link : links) {
            if (link.frameworkAddedSpan == null) {
                android.support.v4.text.util.LinkifyCompat.applyLink(link.url, link.start, link.end, text);
            }
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
    public static final boolean addLinks(@android.support.annotation.NonNull
    android.widget.TextView text, @android.support.v4.text.util.LinkifyCompat.LinkifyMask
    int mask) {
        if (mask == 0) {
            return false;
        }
        java.lang.CharSequence t = text.getText();
        if (t instanceof android.text.Spannable) {
            if (android.support.v4.text.util.LinkifyCompat.addLinks(((android.text.Spannable) (t)), mask)) {
                android.support.v4.text.util.LinkifyCompat.addLinkMovementMethod(text);
                return true;
            }
            return false;
        } else {
            android.text.SpannableString s = android.text.SpannableString.valueOf(t);
            if (android.support.v4.text.util.LinkifyCompat.addLinks(s, mask)) {
                android.support.v4.text.util.LinkifyCompat.addLinkMovementMethod(text);
                text.setText(s);
                return true;
            }
            return false;
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
    public static final void addLinks(@android.support.annotation.NonNull
    android.widget.TextView text, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String scheme) {
        android.support.v4.text.util.LinkifyCompat.addLinks(text, pattern, scheme, null, null, null);
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
    public static final void addLinks(@android.support.annotation.NonNull
    android.widget.TextView text, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String scheme, @android.support.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.support.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        android.support.v4.text.util.LinkifyCompat.addLinks(text, pattern, scheme, null, matchFilter, transformFilter);
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
    public static final void addLinks(@android.support.annotation.NonNull
    android.widget.TextView text, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String defaultScheme, @android.support.annotation.Nullable
    java.lang.String[] schemes, @android.support.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.support.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        android.text.SpannableString spannable = android.text.SpannableString.valueOf(text.getText());
        boolean linksAdded = android.support.v4.text.util.LinkifyCompat.addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        if (linksAdded) {
            text.setText(spannable);
            android.support.v4.text.util.LinkifyCompat.addLinkMovementMethod(text);
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
    public static final boolean addLinks(@android.support.annotation.NonNull
    android.text.Spannable text, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String scheme) {
        return android.support.v4.text.util.LinkifyCompat.addLinks(text, pattern, scheme, null, null, null);
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
    public static final boolean addLinks(@android.support.annotation.NonNull
    android.text.Spannable spannable, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String scheme, @android.support.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.support.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        return android.support.v4.text.util.LinkifyCompat.addLinks(spannable, pattern, scheme, null, matchFilter, transformFilter);
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
    public static final boolean addLinks(@android.support.annotation.NonNull
    android.text.Spannable spannable, @android.support.annotation.NonNull
    java.util.regex.Pattern pattern, @android.support.annotation.Nullable
    java.lang.String defaultScheme, @android.support.annotation.Nullable
    java.lang.String[] schemes, @android.support.annotation.Nullable
    android.text.util.Linkify.MatchFilter matchFilter, @android.support.annotation.Nullable
    android.text.util.Linkify.TransformFilter transformFilter) {
        final java.lang.String[] schemesCopy;
        if (defaultScheme == null)
            defaultScheme = "";

        if ((schemes == null) || (schemes.length < 1)) {
            schemes = android.support.v4.text.util.LinkifyCompat.EMPTY_STRING;
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
                java.lang.String url = android.support.v4.text.util.LinkifyCompat.makeUrl(m.group(0), schemesCopy, m, transformFilter);
                android.support.v4.text.util.LinkifyCompat.applyLink(url, start, end, spannable);
                hasMatches = true;
            }
        } 
        return hasMatches;
    }

    private static void addLinkMovementMethod(@android.support.annotation.NonNull
    android.widget.TextView t) {
        android.text.method.MovementMethod m = t.getMovementMethod();
        if ((m == null) || (!(m instanceof android.text.method.LinkMovementMethod))) {
            if (t.getLinksClickable()) {
                t.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
            }
        }
    }

    private static java.lang.String makeUrl(@android.support.annotation.NonNull
    java.lang.String url, @android.support.annotation.NonNull
    java.lang.String[] prefixes, java.util.regex.Matcher matcher, @android.support.annotation.Nullable
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

    private static void gatherLinks(java.util.ArrayList<android.support.v4.text.util.LinkifyCompat.LinkSpec> links, android.text.Spannable s, java.util.regex.Pattern pattern, java.lang.String[] schemes, android.text.util.Linkify.MatchFilter matchFilter, android.text.util.Linkify.TransformFilter transformFilter) {
        java.util.regex.Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if ((matchFilter == null) || matchFilter.acceptMatch(s, start, end)) {
                android.support.v4.text.util.LinkifyCompat.LinkSpec spec = new android.support.v4.text.util.LinkifyCompat.LinkSpec();
                java.lang.String url = android.support.v4.text.util.LinkifyCompat.makeUrl(m.group(0), schemes, m, transformFilter);
                spec.url = url;
                spec.start = start;
                spec.end = end;
                links.add(spec);
            }
        } 
    }

    private static void applyLink(java.lang.String url, int start, int end, android.text.Spannable text) {
        android.text.style.URLSpan span = new android.text.style.URLSpan(url);
        text.setSpan(span, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static final void gatherMapLinks(java.util.ArrayList<android.support.v4.text.util.LinkifyCompat.LinkSpec> links, android.text.Spannable s) {
        java.lang.String string = s.toString();
        java.lang.String address;
        int base = 0;
        try {
            while ((address = android.webkit.WebView.findAddress(string)) != null) {
                int start = string.indexOf(address);
                if (start < 0) {
                    break;
                }
                android.support.v4.text.util.LinkifyCompat.LinkSpec spec = new android.support.v4.text.util.LinkifyCompat.LinkSpec();
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

    private static final void pruneOverlaps(java.util.ArrayList<android.support.v4.text.util.LinkifyCompat.LinkSpec> links, android.text.Spannable text) {
        // Append spans added by framework
        android.text.style.URLSpan[] urlSpans = text.getSpans(0, text.length(), android.text.style.URLSpan.class);
        for (int i = 0; i < urlSpans.length; i++) {
            android.support.v4.text.util.LinkifyCompat.LinkSpec spec = new android.support.v4.text.util.LinkifyCompat.LinkSpec();
            spec.frameworkAddedSpan = urlSpans[i];
            spec.start = text.getSpanStart(urlSpans[i]);
            spec.end = text.getSpanEnd(urlSpans[i]);
            links.add(spec);
        }
        java.util.Collections.sort(links, android.support.v4.text.util.LinkifyCompat.COMPARATOR);
        int len = links.size();
        int i = 0;
        while (i < (len - 1)) {
            android.support.v4.text.util.LinkifyCompat.LinkSpec a = links.get(i);
            android.support.v4.text.util.LinkifyCompat.LinkSpec b = links.get(i + 1);
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
                    android.text.style.URLSpan span = links.get(remove).frameworkAddedSpan;
                    if (span != null) {
                        text.removeSpan(span);
                    }
                    links.remove(remove);
                    len--;
                    continue;
                }
            }
            i++;
        } 
    }

    /**
     * Do not create this static utility class.
     */
    private LinkifyCompat() {
    }

    private static class LinkSpec {
        android.text.style.URLSpan frameworkAddedSpan;

        java.lang.String url;

        int start;

        int end;

        LinkSpec() {
        }
    }
}

