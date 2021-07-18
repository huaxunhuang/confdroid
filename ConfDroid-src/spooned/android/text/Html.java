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
package android.text;


/**
 * This class processes HTML strings into displayable styled text.
 * Not all HTML tags are supported.
 */
public class Html {
    /**
     * Retrieves images for HTML &lt;img&gt; tags.
     */
    public static interface ImageGetter {
        /**
         * This method is called when the HTML parser encounters an
         * &lt;img&gt; tag.  The <code>source</code> argument is the
         * string from the "src" attribute; the return value should be
         * a Drawable representation of the image or <code>null</code>
         * for a generic replacement image.  Make sure you call
         * setBounds() on your Drawable if it doesn't already have
         * its bounds set.
         */
        public android.graphics.drawable.Drawable getDrawable(java.lang.String source);
    }

    /**
     * Is notified when HTML tags are encountered that the parser does
     * not know how to interpret.
     */
    public static interface TagHandler {
        /**
         * This method will be called whenn the HTML parser encounters
         * a tag that it does not know how to interpret.
         */
        public void handleTag(boolean opening, java.lang.String tag, android.text.Editable output, org.xml.sax.XMLReader xmlReader);
    }

    /**
     * Option for {@link #toHtml(Spanned, int)}: Wrap consecutive lines of text delimited by '\n'
     * inside &lt;p&gt; elements. {@link BulletSpan}s are ignored.
     */
    public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0x0;

    /**
     * Option for {@link #toHtml(Spanned, int)}: Wrap each line of text delimited by '\n' inside a
     * &lt;p&gt; or a &lt;li&gt; element. This allows {@link ParagraphStyle}s attached to be
     * encoded as CSS styles within the corresponding &lt;p&gt; or &lt;li&gt; element.
     */
    public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 0x1;

    /**
     * Flag indicating that texts inside &lt;p&gt; elements will be separated from other texts with
     * one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 0x1;

    /**
     * Flag indicating that texts inside &lt;h1&gt;~&lt;h6&gt; elements will be separated from
     * other texts with one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 0x2;

    /**
     * Flag indicating that texts inside &lt;li&gt; elements will be separated from other texts
     * with one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 0x4;

    /**
     * Flag indicating that texts inside &lt;ul&gt; elements will be separated from other texts
     * with one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 0x8;

    /**
     * Flag indicating that texts inside &lt;div&gt; elements will be separated from other texts
     * with one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 0x10;

    /**
     * Flag indicating that texts inside &lt;blockquote&gt; elements will be separated from other
     * texts with one newline character by default.
     */
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 0x20;

    /**
     * Flag indicating that CSS color values should be used instead of those defined in
     * {@link Color}.
     */
    public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 0x100;

    /**
     * Flags for {@link #fromHtml(String, int, ImageGetter, TagHandler)}: Separate block-level
     * elements with blank lines (two newline characters) in between. This is the legacy behavior
     * prior to N.
     */
    public static final int FROM_HTML_MODE_LEGACY = 0x0;

    /**
     * Flags for {@link #fromHtml(String, int, ImageGetter, TagHandler)}: Separate block-level
     * elements with line breaks (single newline character) in between. This inverts the
     * {@link Spanned} to HTML string conversion done with the option
     * {@link #TO_HTML_PARAGRAPH_LINES_INDIVIDUAL}.
     */
    public static final int FROM_HTML_MODE_COMPACT = ((((android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH | android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING) | android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM) | android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST) | android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV) | android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE;

    /**
     * The bit which indicates if lines delimited by '\n' will be grouped into &lt;p&gt; elements.
     */
    private static final int TO_HTML_PARAGRAPH_FLAG = 0x1;

    private Html() {
    }

    /**
     * Returns displayable styled text from the provided HTML string with the legacy flags
     * {@link #FROM_HTML_MODE_LEGACY}.
     *
     * @deprecated use {@link #fromHtml(String, int)} instead.
     */
    @java.lang.Deprecated
    public static android.text.Spanned fromHtml(java.lang.String source) {
        return android.text.Html.fromHtml(source, android.text.Html.FROM_HTML_MODE_LEGACY, null, null);
    }

    /**
     * Returns displayable styled text from the provided HTML string. Any &lt;img&gt; tags in the
     * HTML will display as a generic replacement image which your program can then go through and
     * replace with real images.
     *
     * <p>This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
     */
    public static android.text.Spanned fromHtml(java.lang.String source, int flags) {
        return android.text.Html.fromHtml(source, flags, null, null);
    }

    /**
     * Lazy initialization holder for HTML parser. This class will
     * a) be preloaded by the zygote, or b) not loaded until absolutely
     * necessary.
     */
    private static class HtmlParser {
        private static final org.ccil.cowan.tagsoup.HTMLSchema schema = new org.ccil.cowan.tagsoup.HTMLSchema();
    }

    /**
     * Returns displayable styled text from the provided HTML string with the legacy flags
     * {@link #FROM_HTML_MODE_LEGACY}.
     *
     * @deprecated use {@link #fromHtml(String, int, ImageGetter, TagHandler)} instead.
     */
    @java.lang.Deprecated
    public static android.text.Spanned fromHtml(java.lang.String source, android.text.Html.ImageGetter imageGetter, android.text.Html.TagHandler tagHandler) {
        return android.text.Html.fromHtml(source, android.text.Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler);
    }

    /**
     * Returns displayable styled text from the provided HTML string. Any &lt;img&gt; tags in the
     * HTML will use the specified ImageGetter to request a representation of the image (use null
     * if you don't want this) and the specified TagHandler to handle unknown tags (specify null if
     * you don't want this).
     *
     * <p>This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
     */
    public static android.text.Spanned fromHtml(java.lang.String source, int flags, android.text.Html.ImageGetter imageGetter, android.text.Html.TagHandler tagHandler) {
        org.ccil.cowan.tagsoup.Parser parser = new org.ccil.cowan.tagsoup.Parser();
        try {
            parser.setProperty(Parser.schemaProperty, android.text.Html.HtmlParser.schema);
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            // Should not happen.
            throw new java.lang.RuntimeException(e);
        } catch (org.xml.sax.SAXNotSupportedException e) {
            // Should not happen.
            throw new java.lang.RuntimeException(e);
        }
        android.text.HtmlToSpannedConverter converter = new android.text.HtmlToSpannedConverter(source, imageGetter, tagHandler, parser, flags);
        return converter.convert();
    }

    /**
     *
     *
     * @deprecated use {@link #toHtml(Spanned, int)} instead.
     */
    @java.lang.Deprecated
    public static java.lang.String toHtml(android.text.Spanned text) {
        return android.text.Html.toHtml(text, android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
    }

    /**
     * Returns an HTML representation of the provided Spanned text. A best effort is
     * made to add HTML tags corresponding to spans. Also note that HTML metacharacters
     * (such as "&lt;" and "&amp;") within the input text are escaped.
     *
     * @param text
     * 		input text to convert
     * @param option
     * 		one of {@link #TO_HTML_PARAGRAPH_LINES_CONSECUTIVE} or
     * 		{@link #TO_HTML_PARAGRAPH_LINES_INDIVIDUAL}
     * @return string containing input converted to HTML
     */
    public static java.lang.String toHtml(android.text.Spanned text, int option) {
        java.lang.StringBuilder out = new java.lang.StringBuilder();
        android.text.Html.withinHtml(out, text, option);
        return out.toString();
    }

    /**
     * Returns an HTML escaped representation of the given plain text.
     */
    public static java.lang.String escapeHtml(java.lang.CharSequence text) {
        java.lang.StringBuilder out = new java.lang.StringBuilder();
        android.text.Html.withinStyle(out, text, 0, text.length());
        return out.toString();
    }

    private static void withinHtml(java.lang.StringBuilder out, android.text.Spanned text, int option) {
        if ((option & android.text.Html.TO_HTML_PARAGRAPH_FLAG) == android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE) {
            android.text.Html.encodeTextAlignmentByDiv(out, text, option);
            return;
        }
        android.text.Html.withinDiv(out, text, 0, text.length(), option);
    }

    private static void encodeTextAlignmentByDiv(java.lang.StringBuilder out, android.text.Spanned text, int option) {
        int len = text.length();
        int next;
        for (int i = 0; i < len; i = next) {
            next = text.nextSpanTransition(i, len, android.text.style.ParagraphStyle.class);
            android.text.style.ParagraphStyle[] style = text.getSpans(i, next, android.text.style.ParagraphStyle.class);
            java.lang.String elements = " ";
            boolean needDiv = false;
            for (int j = 0; j < style.length; j++) {
                if (style[j] instanceof android.text.style.AlignmentSpan) {
                    android.text.Layout.Alignment align = ((android.text.style.AlignmentSpan) (style[j])).getAlignment();
                    needDiv = true;
                    if (align == android.text.Layout.Alignment.ALIGN_CENTER) {
                        elements = "align=\"center\" " + elements;
                    } else
                        if (align == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                            elements = "align=\"right\" " + elements;
                        } else {
                            elements = "align=\"left\" " + elements;
                        }

                }
            }
            if (needDiv) {
                out.append("<div ").append(elements).append(">");
            }
            android.text.Html.withinDiv(out, text, i, next, option);
            if (needDiv) {
                out.append("</div>");
            }
        }
    }

    private static void withinDiv(java.lang.StringBuilder out, android.text.Spanned text, int start, int end, int option) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, android.text.style.QuoteSpan.class);
            android.text.style.QuoteSpan[] quotes = text.getSpans(i, next, android.text.style.QuoteSpan.class);
            for (android.text.style.QuoteSpan quote : quotes) {
                out.append("<blockquote>");
            }
            android.text.Html.withinBlockquote(out, text, i, next, option);
            for (android.text.style.QuoteSpan quote : quotes) {
                out.append("</blockquote>\n");
            }
        }
    }

    private static java.lang.String getTextDirection(android.text.Spanned text, int start, int end) {
        final int len = end - start;
        final byte[] levels = com.android.internal.util.ArrayUtils.newUnpaddedByteArray(len);
        final char[] buffer = android.text.TextUtils.obtain(len);
        android.text.TextUtils.getChars(text, start, end, buffer, 0);
        int paraDir = /* no info */
        android.text.AndroidBidi.bidi(android.text.Layout.DIR_REQUEST_DEFAULT_LTR, buffer, levels, len, false);
        switch (paraDir) {
            case android.text.Layout.DIR_RIGHT_TO_LEFT :
                return " dir=\"rtl\"";
            case android.text.Layout.DIR_LEFT_TO_RIGHT :
            default :
                return " dir=\"ltr\"";
        }
    }

    private static java.lang.String getTextStyles(android.text.Spanned text, int start, int end, boolean forceNoVerticalMargin, boolean includeTextAlign) {
        java.lang.String margin = null;
        java.lang.String textAlign = null;
        if (forceNoVerticalMargin) {
            margin = "margin-top:0; margin-bottom:0;";
        }
        if (includeTextAlign) {
            final android.text.style.AlignmentSpan[] alignmentSpans = text.getSpans(start, end, android.text.style.AlignmentSpan.class);
            // Only use the last AlignmentSpan with flag SPAN_PARAGRAPH
            for (int i = alignmentSpans.length - 1; i >= 0; i--) {
                android.text.style.AlignmentSpan s = alignmentSpans[i];
                if ((text.getSpanFlags(s) & android.text.Spanned.SPAN_PARAGRAPH) == android.text.Spanned.SPAN_PARAGRAPH) {
                    final android.text.Layout.Alignment alignment = s.getAlignment();
                    if (alignment == android.text.Layout.Alignment.ALIGN_NORMAL) {
                        textAlign = "text-align:start;";
                    } else
                        if (alignment == android.text.Layout.Alignment.ALIGN_CENTER) {
                            textAlign = "text-align:center;";
                        } else
                            if (alignment == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                                textAlign = "text-align:end;";
                            }


                    break;
                }
            }
        }
        if ((margin == null) && (textAlign == null)) {
            return "";
        }
        final java.lang.StringBuilder style = new java.lang.StringBuilder(" style=\"");
        if ((margin != null) && (textAlign != null)) {
            style.append(margin).append(" ").append(textAlign);
        } else
            if (margin != null) {
                style.append(margin);
            } else
                if (textAlign != null) {
                    style.append(textAlign);
                }


        return style.append("\"").toString();
    }

    private static void withinBlockquote(java.lang.StringBuilder out, android.text.Spanned text, int start, int end, int option) {
        if ((option & android.text.Html.TO_HTML_PARAGRAPH_FLAG) == android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE) {
            android.text.Html.withinBlockquoteConsecutive(out, text, start, end);
        } else {
            android.text.Html.withinBlockquoteIndividual(out, text, start, end);
        }
    }

    private static void withinBlockquoteIndividual(java.lang.StringBuilder out, android.text.Spanned text, int start, int end) {
        boolean isInList = false;
        int next;
        for (int i = start; i <= end; i = next) {
            next = android.text.TextUtils.indexOf(text, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            if (next == i) {
                if (isInList) {
                    // Current paragraph is no longer a list item; close the previously opened list
                    isInList = false;
                    out.append("</ul>\n");
                }
                out.append("<br>\n");
            } else {
                boolean isListItem = false;
                android.text.style.ParagraphStyle[] paragraphStyles = text.getSpans(i, next, android.text.style.ParagraphStyle.class);
                for (android.text.style.ParagraphStyle paragraphStyle : paragraphStyles) {
                    final int spanFlags = text.getSpanFlags(paragraphStyle);
                    if (((spanFlags & android.text.Spanned.SPAN_PARAGRAPH) == android.text.Spanned.SPAN_PARAGRAPH) && (paragraphStyle instanceof android.text.style.BulletSpan)) {
                        isListItem = true;
                        break;
                    }
                }
                if (isListItem && (!isInList)) {
                    // Current paragraph is the first item in a list
                    isInList = true;
                    out.append("<ul").append(android.text.Html.getTextStyles(text, i, next, true, false)).append(">\n");
                }
                if (isInList && (!isListItem)) {
                    // Current paragraph is no longer a list item; close the previously opened list
                    isInList = false;
                    out.append("</ul>\n");
                }
                java.lang.String tagType = (isListItem) ? "li" : "p";
                out.append("<").append(tagType).append(android.text.Html.getTextDirection(text, i, next)).append(android.text.Html.getTextStyles(text, i, next, !isListItem, true)).append(">");
                android.text.Html.withinParagraph(out, text, i, next);
                out.append("</");
                out.append(tagType);
                out.append(">\n");
                if ((next == end) && isInList) {
                    isInList = false;
                    out.append("</ul>\n");
                }
            }
            next++;
        }
    }

    private static void withinBlockquoteConsecutive(java.lang.StringBuilder out, android.text.Spanned text, int start, int end) {
        out.append("<p").append(android.text.Html.getTextDirection(text, start, end)).append(">");
        int next;
        for (int i = start; i < end; i = next) {
            next = android.text.TextUtils.indexOf(text, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            int nl = 0;
            while ((next < end) && (text.charAt(next) == '\n')) {
                nl++;
                next++;
            } 
            android.text.Html.withinParagraph(out, text, i, next - nl);
            if (nl == 1) {
                out.append("<br>\n");
            } else {
                for (int j = 2; j < nl; j++) {
                    out.append("<br>");
                }
                if (next != end) {
                    /* Paragraph should be closed and reopened */
                    out.append("</p>\n");
                    out.append("<p").append(android.text.Html.getTextDirection(text, start, end)).append(">");
                }
            }
        }
        out.append("</p>\n");
    }

    private static void withinParagraph(java.lang.StringBuilder out, android.text.Spanned text, int start, int end) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, android.text.style.CharacterStyle.class);
            android.text.style.CharacterStyle[] style = text.getSpans(i, next, android.text.style.CharacterStyle.class);
            for (int j = 0; j < style.length; j++) {
                if (style[j] instanceof android.text.style.StyleSpan) {
                    int s = ((android.text.style.StyleSpan) (style[j])).getStyle();
                    if ((s & android.graphics.Typeface.BOLD) != 0) {
                        out.append("<b>");
                    }
                    if ((s & android.graphics.Typeface.ITALIC) != 0) {
                        out.append("<i>");
                    }
                }
                if (style[j] instanceof android.text.style.TypefaceSpan) {
                    java.lang.String s = ((android.text.style.TypefaceSpan) (style[j])).getFamily();
                    if ("monospace".equals(s)) {
                        out.append("<tt>");
                    }
                }
                if (style[j] instanceof android.text.style.SuperscriptSpan) {
                    out.append("<sup>");
                }
                if (style[j] instanceof android.text.style.SubscriptSpan) {
                    out.append("<sub>");
                }
                if (style[j] instanceof android.text.style.UnderlineSpan) {
                    out.append("<u>");
                }
                if (style[j] instanceof android.text.style.StrikethroughSpan) {
                    out.append("<span style=\"text-decoration:line-through;\">");
                }
                if (style[j] instanceof android.text.style.URLSpan) {
                    out.append("<a href=\"");
                    out.append(((android.text.style.URLSpan) (style[j])).getURL());
                    out.append("\">");
                }
                if (style[j] instanceof android.text.style.ImageSpan) {
                    out.append("<img src=\"");
                    out.append(((android.text.style.ImageSpan) (style[j])).getSource());
                    out.append("\">");
                    // Don't output the dummy character underlying the image.
                    i = next;
                }
                if (style[j] instanceof android.text.style.AbsoluteSizeSpan) {
                    android.text.style.AbsoluteSizeSpan s = ((android.text.style.AbsoluteSizeSpan) (style[j]));
                    float sizeDip = s.getSize();
                    if (!s.getDip()) {
                        android.app.Application application = android.app.ActivityThread.currentApplication();
                        sizeDip /= application.getResources().getDisplayMetrics().density;
                    }
                    // px in CSS is the equivalance of dip in Android
                    out.append(java.lang.String.format("<span style=\"font-size:%.0fpx\";>", sizeDip));
                }
                if (style[j] instanceof android.text.style.RelativeSizeSpan) {
                    float sizeEm = ((android.text.style.RelativeSizeSpan) (style[j])).getSizeChange();
                    out.append(java.lang.String.format("<span style=\"font-size:%.2fem;\">", sizeEm));
                }
                if (style[j] instanceof android.text.style.ForegroundColorSpan) {
                    int color = ((android.text.style.ForegroundColorSpan) (style[j])).getForegroundColor();
                    out.append(java.lang.String.format("<span style=\"color:#%06X;\">", 0xffffff & color));
                }
                if (style[j] instanceof android.text.style.BackgroundColorSpan) {
                    int color = ((android.text.style.BackgroundColorSpan) (style[j])).getBackgroundColor();
                    out.append(java.lang.String.format("<span style=\"background-color:#%06X;\">", 0xffffff & color));
                }
            }
            android.text.Html.withinStyle(out, text, i, next);
            for (int j = style.length - 1; j >= 0; j--) {
                if (style[j] instanceof android.text.style.BackgroundColorSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof android.text.style.ForegroundColorSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof android.text.style.RelativeSizeSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof android.text.style.AbsoluteSizeSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof android.text.style.URLSpan) {
                    out.append("</a>");
                }
                if (style[j] instanceof android.text.style.StrikethroughSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof android.text.style.UnderlineSpan) {
                    out.append("</u>");
                }
                if (style[j] instanceof android.text.style.SubscriptSpan) {
                    out.append("</sub>");
                }
                if (style[j] instanceof android.text.style.SuperscriptSpan) {
                    out.append("</sup>");
                }
                if (style[j] instanceof android.text.style.TypefaceSpan) {
                    java.lang.String s = ((android.text.style.TypefaceSpan) (style[j])).getFamily();
                    if (s.equals("monospace")) {
                        out.append("</tt>");
                    }
                }
                if (style[j] instanceof android.text.style.StyleSpan) {
                    int s = ((android.text.style.StyleSpan) (style[j])).getStyle();
                    if ((s & android.graphics.Typeface.BOLD) != 0) {
                        out.append("</b>");
                    }
                    if ((s & android.graphics.Typeface.ITALIC) != 0) {
                        out.append("</i>");
                    }
                }
            }
        }
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
                        if ((c >= 0xd800) && (c <= 0xdfff)) {
                            if ((c < 0xdc00) && ((i + 1) < end)) {
                                char d = text.charAt(i + 1);
                                if ((d >= 0xdc00) && (d <= 0xdfff)) {
                                    i++;
                                    int codepoint = (0x10000 | ((((int) (c)) - 0xd800) << 10)) | (((int) (d)) - 0xdc00);
                                    out.append("&#").append(codepoint).append(";");
                                }
                            }
                        } else
                            if ((c > 0x7e) || (c < ' ')) {
                                out.append("&#").append(((int) (c))).append(";");
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

