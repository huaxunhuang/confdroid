package android.text;


class HtmlToSpannedConverter implements org.xml.sax.ContentHandler {
    private static final float[] HEADING_SIZES = new float[]{ 1.5F, 1.4F, 1.3F, 1.2F, 1.1F, 1.0F };

    private java.lang.String mSource;

    private org.xml.sax.XMLReader mReader;

    private android.text.SpannableStringBuilder mSpannableStringBuilder;

    private android.text.Html.ImageGetter mImageGetter;

    private android.text.Html.TagHandler mTagHandler;

    private int mFlags;

    private static java.util.regex.Pattern sTextAlignPattern;

    private static java.util.regex.Pattern sForegroundColorPattern;

    private static java.util.regex.Pattern sBackgroundColorPattern;

    private static java.util.regex.Pattern sTextDecorationPattern;

    /**
     * Name-value mapping of HTML/CSS colors which have different values in {@link Color}.
     */
    private static final java.util.Map<java.lang.String, java.lang.Integer> sColorMap;

    static {
        sColorMap = new java.util.HashMap<>();
        android.text.HtmlToSpannedConverter.sColorMap.put("darkgray", 0xffa9a9a9);
        android.text.HtmlToSpannedConverter.sColorMap.put("gray", 0xff808080);
        android.text.HtmlToSpannedConverter.sColorMap.put("lightgray", 0xffd3d3d3);
        android.text.HtmlToSpannedConverter.sColorMap.put("darkgrey", 0xffa9a9a9);
        android.text.HtmlToSpannedConverter.sColorMap.put("grey", 0xff808080);
        android.text.HtmlToSpannedConverter.sColorMap.put("lightgrey", 0xffd3d3d3);
        android.text.HtmlToSpannedConverter.sColorMap.put("green", 0xff008000);
    }

    private static java.util.regex.Pattern getTextAlignPattern() {
        if (android.text.HtmlToSpannedConverter.sTextAlignPattern == null) {
            android.text.HtmlToSpannedConverter.sTextAlignPattern = java.util.regex.Pattern.compile("(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b");
        }
        return android.text.HtmlToSpannedConverter.sTextAlignPattern;
    }

    private static java.util.regex.Pattern getForegroundColorPattern() {
        if (android.text.HtmlToSpannedConverter.sForegroundColorPattern == null) {
            android.text.HtmlToSpannedConverter.sForegroundColorPattern = java.util.regex.Pattern.compile("(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b");
        }
        return android.text.HtmlToSpannedConverter.sForegroundColorPattern;
    }

    private static java.util.regex.Pattern getBackgroundColorPattern() {
        if (android.text.HtmlToSpannedConverter.sBackgroundColorPattern == null) {
            android.text.HtmlToSpannedConverter.sBackgroundColorPattern = java.util.regex.Pattern.compile("(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b");
        }
        return android.text.HtmlToSpannedConverter.sBackgroundColorPattern;
    }

    private static java.util.regex.Pattern getTextDecorationPattern() {
        if (android.text.HtmlToSpannedConverter.sTextDecorationPattern == null) {
            android.text.HtmlToSpannedConverter.sTextDecorationPattern = java.util.regex.Pattern.compile("(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b");
        }
        return android.text.HtmlToSpannedConverter.sTextDecorationPattern;
    }

    public HtmlToSpannedConverter(java.lang.String source, android.text.Html.ImageGetter imageGetter, android.text.Html.TagHandler tagHandler, org.ccil.cowan.tagsoup.Parser parser, int flags) {
        mSource = source;
        mSpannableStringBuilder = new android.text.SpannableStringBuilder();
        mImageGetter = imageGetter;
        mTagHandler = tagHandler;
        mReader = parser;
        mFlags = flags;
    }

    public android.text.Spanned convert() {
        mReader.setContentHandler(this);
        try {
            mReader.parse(new org.xml.sax.InputSource(new java.io.StringReader(mSource)));
        } catch (java.io.IOException e) {
            // We are reading from a string. There should not be IO problems.
            throw new java.lang.RuntimeException(e);
        } catch (org.xml.sax.SAXException e) {
            // TagSoup doesn't throw parse exceptions.
            throw new java.lang.RuntimeException(e);
        }
        // Fix flags and range for paragraph-type markup.
        java.lang.Object[] obj = mSpannableStringBuilder.getSpans(0, mSpannableStringBuilder.length(), android.text.style.ParagraphStyle.class);
        for (int i = 0; i < obj.length; i++) {
            int start = mSpannableStringBuilder.getSpanStart(obj[i]);
            int end = mSpannableStringBuilder.getSpanEnd(obj[i]);
            // If the last line of the range is blank, back off by one.
            if ((end - 2) >= 0) {
                if ((mSpannableStringBuilder.charAt(end - 1) == '\n') && (mSpannableStringBuilder.charAt(end - 2) == '\n')) {
                    end--;
                }
            }
            if (end == start) {
                mSpannableStringBuilder.removeSpan(obj[i]);
            } else {
                mSpannableStringBuilder.setSpan(obj[i], start, end, android.text.Spannable.SPAN_PARAGRAPH);
            }
        }
        return mSpannableStringBuilder;
    }

    private void handleStartTag(java.lang.String tag, org.xml.sax.Attributes attributes) {
        if (tag.equalsIgnoreCase("br")) {
            // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
            // so we can safely emit the linebreaks when we handle the close tag.
        } else
            if (tag.equalsIgnoreCase("p")) {
                android.text.HtmlToSpannedConverter.startBlockElement(mSpannableStringBuilder, attributes, getMarginParagraph());
                startCssStyle(mSpannableStringBuilder, attributes);
            } else
                if (tag.equalsIgnoreCase("ul")) {
                    android.text.HtmlToSpannedConverter.startBlockElement(mSpannableStringBuilder, attributes, getMarginList());
                } else
                    if (tag.equalsIgnoreCase("li")) {
                        startLi(mSpannableStringBuilder, attributes);
                    } else
                        if (tag.equalsIgnoreCase("div")) {
                            android.text.HtmlToSpannedConverter.startBlockElement(mSpannableStringBuilder, attributes, getMarginDiv());
                        } else
                            if (tag.equalsIgnoreCase("span")) {
                                startCssStyle(mSpannableStringBuilder, attributes);
                            } else
                                if (tag.equalsIgnoreCase("strong")) {
                                    android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Bold());
                                } else
                                    if (tag.equalsIgnoreCase("b")) {
                                        android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Bold());
                                    } else
                                        if (tag.equalsIgnoreCase("em")) {
                                            android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Italic());
                                        } else
                                            if (tag.equalsIgnoreCase("cite")) {
                                                android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Italic());
                                            } else
                                                if (tag.equalsIgnoreCase("dfn")) {
                                                    android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Italic());
                                                } else
                                                    if (tag.equalsIgnoreCase("i")) {
                                                        android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Italic());
                                                    } else
                                                        if (tag.equalsIgnoreCase("big")) {
                                                            android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Big());
                                                        } else
                                                            if (tag.equalsIgnoreCase("small")) {
                                                                android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Small());
                                                            } else
                                                                if (tag.equalsIgnoreCase("font")) {
                                                                    startFont(mSpannableStringBuilder, attributes);
                                                                } else
                                                                    if (tag.equalsIgnoreCase("blockquote")) {
                                                                        startBlockquote(mSpannableStringBuilder, attributes);
                                                                    } else
                                                                        if (tag.equalsIgnoreCase("tt")) {
                                                                            android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Monospace());
                                                                        } else
                                                                            if (tag.equalsIgnoreCase("a")) {
                                                                                android.text.HtmlToSpannedConverter.startA(mSpannableStringBuilder, attributes);
                                                                            } else
                                                                                if (tag.equalsIgnoreCase("u")) {
                                                                                    android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Underline());
                                                                                } else
                                                                                    if (tag.equalsIgnoreCase("del")) {
                                                                                        android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Strikethrough());
                                                                                    } else
                                                                                        if (tag.equalsIgnoreCase("s")) {
                                                                                            android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Strikethrough());
                                                                                        } else
                                                                                            if (tag.equalsIgnoreCase("strike")) {
                                                                                                android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Strikethrough());
                                                                                            } else
                                                                                                if (tag.equalsIgnoreCase("sup")) {
                                                                                                    android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Super());
                                                                                                } else
                                                                                                    if (tag.equalsIgnoreCase("sub")) {
                                                                                                        android.text.HtmlToSpannedConverter.start(mSpannableStringBuilder, new android.text.HtmlToSpannedConverter.Sub());
                                                                                                    } else
                                                                                                        if ((((tag.length() == 2) && (java.lang.Character.toLowerCase(tag.charAt(0)) == 'h')) && (tag.charAt(1) >= '1')) && (tag.charAt(1) <= '6')) {
                                                                                                            startHeading(mSpannableStringBuilder, attributes, tag.charAt(1) - '1');
                                                                                                        } else
                                                                                                            if (tag.equalsIgnoreCase("img")) {
                                                                                                                android.text.HtmlToSpannedConverter.startImg(mSpannableStringBuilder, attributes, mImageGetter);
                                                                                                            } else
                                                                                                                if (mTagHandler != null) {
                                                                                                                    mTagHandler.handleTag(true, tag, mSpannableStringBuilder, mReader);
                                                                                                                }


























    }

    private void handleEndTag(java.lang.String tag) {
        if (tag.equalsIgnoreCase("br")) {
            android.text.HtmlToSpannedConverter.handleBr(mSpannableStringBuilder);
        } else
            if (tag.equalsIgnoreCase("p")) {
                android.text.HtmlToSpannedConverter.endCssStyle(mSpannableStringBuilder);
                android.text.HtmlToSpannedConverter.endBlockElement(mSpannableStringBuilder);
            } else
                if (tag.equalsIgnoreCase("ul")) {
                    android.text.HtmlToSpannedConverter.endBlockElement(mSpannableStringBuilder);
                } else
                    if (tag.equalsIgnoreCase("li")) {
                        android.text.HtmlToSpannedConverter.endLi(mSpannableStringBuilder);
                    } else
                        if (tag.equalsIgnoreCase("div")) {
                            android.text.HtmlToSpannedConverter.endBlockElement(mSpannableStringBuilder);
                        } else
                            if (tag.equalsIgnoreCase("span")) {
                                android.text.HtmlToSpannedConverter.endCssStyle(mSpannableStringBuilder);
                            } else
                                if (tag.equalsIgnoreCase("strong")) {
                                    android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Bold.class, new android.text.style.StyleSpan(android.graphics.Typeface.BOLD));
                                } else
                                    if (tag.equalsIgnoreCase("b")) {
                                        android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Bold.class, new android.text.style.StyleSpan(android.graphics.Typeface.BOLD));
                                    } else
                                        if (tag.equalsIgnoreCase("em")) {
                                            android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Italic.class, new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC));
                                        } else
                                            if (tag.equalsIgnoreCase("cite")) {
                                                android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Italic.class, new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC));
                                            } else
                                                if (tag.equalsIgnoreCase("dfn")) {
                                                    android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Italic.class, new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC));
                                                } else
                                                    if (tag.equalsIgnoreCase("i")) {
                                                        android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Italic.class, new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC));
                                                    } else
                                                        if (tag.equalsIgnoreCase("big")) {
                                                            android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Big.class, new android.text.style.RelativeSizeSpan(1.25F));
                                                        } else
                                                            if (tag.equalsIgnoreCase("small")) {
                                                                android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Small.class, new android.text.style.RelativeSizeSpan(0.8F));
                                                            } else
                                                                if (tag.equalsIgnoreCase("font")) {
                                                                    android.text.HtmlToSpannedConverter.endFont(mSpannableStringBuilder);
                                                                } else
                                                                    if (tag.equalsIgnoreCase("blockquote")) {
                                                                        android.text.HtmlToSpannedConverter.endBlockquote(mSpannableStringBuilder);
                                                                    } else
                                                                        if (tag.equalsIgnoreCase("tt")) {
                                                                            android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Monospace.class, new android.text.style.TypefaceSpan("monospace"));
                                                                        } else
                                                                            if (tag.equalsIgnoreCase("a")) {
                                                                                android.text.HtmlToSpannedConverter.endA(mSpannableStringBuilder);
                                                                            } else
                                                                                if (tag.equalsIgnoreCase("u")) {
                                                                                    android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Underline.class, new android.text.style.UnderlineSpan());
                                                                                } else
                                                                                    if (tag.equalsIgnoreCase("del")) {
                                                                                        android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Strikethrough.class, new android.text.style.StrikethroughSpan());
                                                                                    } else
                                                                                        if (tag.equalsIgnoreCase("s")) {
                                                                                            android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Strikethrough.class, new android.text.style.StrikethroughSpan());
                                                                                        } else
                                                                                            if (tag.equalsIgnoreCase("strike")) {
                                                                                                android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Strikethrough.class, new android.text.style.StrikethroughSpan());
                                                                                            } else
                                                                                                if (tag.equalsIgnoreCase("sup")) {
                                                                                                    android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Super.class, new android.text.style.SuperscriptSpan());
                                                                                                } else
                                                                                                    if (tag.equalsIgnoreCase("sub")) {
                                                                                                        android.text.HtmlToSpannedConverter.end(mSpannableStringBuilder, android.text.HtmlToSpannedConverter.Sub.class, new android.text.style.SubscriptSpan());
                                                                                                    } else
                                                                                                        if ((((tag.length() == 2) && (java.lang.Character.toLowerCase(tag.charAt(0)) == 'h')) && (tag.charAt(1) >= '1')) && (tag.charAt(1) <= '6')) {
                                                                                                            android.text.HtmlToSpannedConverter.endHeading(mSpannableStringBuilder);
                                                                                                        } else
                                                                                                            if (mTagHandler != null) {
                                                                                                                mTagHandler.handleTag(false, tag, mSpannableStringBuilder, mReader);
                                                                                                            }

























    }

    private int getMarginParagraph() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
    }

    private int getMarginHeading() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING);
    }

    private int getMarginListItem() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM);
    }

    private int getMarginList() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST);
    }

    private int getMarginDiv() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV);
    }

    private int getMarginBlockquote() {
        return getMargin(android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE);
    }

    /**
     * Returns the minimum number of newline characters needed before and after a given block-level
     * element.
     *
     * @param flag
     * 		the corresponding option flag defined in {@link Html} of a block-level element
     */
    private int getMargin(int flag) {
        if ((flag & mFlags) != 0) {
            return 1;
        }
        return 2;
    }

    private static void appendNewlines(android.text.Editable text, int minNewline) {
        final int len = text.length();
        if (len == 0) {
            return;
        }
        int existingNewlines = 0;
        for (int i = len - 1; (i >= 0) && (text.charAt(i) == '\n'); i--) {
            existingNewlines++;
        }
        for (int j = existingNewlines; j < minNewline; j++) {
            text.append("\n");
        }
    }

    private static void startBlockElement(android.text.Editable text, org.xml.sax.Attributes attributes, int margin) {
        final int len = text.length();
        if (margin > 0) {
            android.text.HtmlToSpannedConverter.appendNewlines(text, margin);
            android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Newline(margin));
        }
        java.lang.String style = attributes.getValue("", "style");
        if (style != null) {
            java.util.regex.Matcher m = android.text.HtmlToSpannedConverter.getTextAlignPattern().matcher(style);
            if (m.find()) {
                java.lang.String alignment = m.group(1);
                if (alignment.equalsIgnoreCase("start")) {
                    android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Alignment(android.text.Layout.Alignment.ALIGN_NORMAL));
                } else
                    if (alignment.equalsIgnoreCase("center")) {
                        android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Alignment(android.text.Layout.Alignment.ALIGN_CENTER));
                    } else
                        if (alignment.equalsIgnoreCase("end")) {
                            android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Alignment(android.text.Layout.Alignment.ALIGN_OPPOSITE));
                        }


            }
        }
    }

    private static void endBlockElement(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.Newline n = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Newline.class);
        if (n != null) {
            android.text.HtmlToSpannedConverter.appendNewlines(text, n.mNumNewlines);
            text.removeSpan(n);
        }
        android.text.HtmlToSpannedConverter.Alignment a = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Alignment.class);
        if (a != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, a, new android.text.style.AlignmentSpan.Standard(a.mAlignment));
        }
    }

    private static void handleBr(android.text.Editable text) {
        text.append('\n');
    }

    private void startLi(android.text.Editable text, org.xml.sax.Attributes attributes) {
        android.text.HtmlToSpannedConverter.startBlockElement(text, attributes, getMarginListItem());
        android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Bullet());
        startCssStyle(text, attributes);
    }

    private static void endLi(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.endCssStyle(text);
        android.text.HtmlToSpannedConverter.endBlockElement(text);
        android.text.HtmlToSpannedConverter.end(text, android.text.HtmlToSpannedConverter.Bullet.class, new android.text.style.BulletSpan());
    }

    private void startBlockquote(android.text.Editable text, org.xml.sax.Attributes attributes) {
        android.text.HtmlToSpannedConverter.startBlockElement(text, attributes, getMarginBlockquote());
        android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Blockquote());
    }

    private static void endBlockquote(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.endBlockElement(text);
        android.text.HtmlToSpannedConverter.end(text, android.text.HtmlToSpannedConverter.Blockquote.class, new android.text.style.QuoteSpan());
    }

    private void startHeading(android.text.Editable text, org.xml.sax.Attributes attributes, int level) {
        android.text.HtmlToSpannedConverter.startBlockElement(text, attributes, getMarginHeading());
        android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Heading(level));
    }

    private static void endHeading(android.text.Editable text) {
        // RelativeSizeSpan and StyleSpan are CharacterStyles
        // Their ranges should not include the newlines at the end
        android.text.HtmlToSpannedConverter.Heading h = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Heading.class);
        if (h != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, h, new android.text.style.RelativeSizeSpan(android.text.HtmlToSpannedConverter.HEADING_SIZES[h.mLevel]), new android.text.style.StyleSpan(android.graphics.Typeface.BOLD));
        }
        android.text.HtmlToSpannedConverter.endBlockElement(text);
    }

    private static <T> T getLast(android.text.Spanned text, java.lang.Class<T> kind) {
        /* This knows that the last returned object from getSpans()
        will be the most recently added.
         */
        T[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    private static void setSpanFromMark(android.text.Spannable text, java.lang.Object mark, java.lang.Object... spans) {
        int where = text.getSpanStart(mark);
        text.removeSpan(mark);
        int len = text.length();
        if (where != len) {
            for (java.lang.Object span : spans) {
                text.setSpan(span, where, len, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static void start(android.text.Editable text, java.lang.Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private static void end(android.text.Editable text, java.lang.Class kind, java.lang.Object repl) {
        int len = text.length();
        java.lang.Object obj = android.text.HtmlToSpannedConverter.getLast(text, kind);
        if (obj != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, obj, repl);
        }
    }

    private void startCssStyle(android.text.Editable text, org.xml.sax.Attributes attributes) {
        java.lang.String style = attributes.getValue("", "style");
        if (style != null) {
            java.util.regex.Matcher m = android.text.HtmlToSpannedConverter.getForegroundColorPattern().matcher(style);
            if (m.find()) {
                int c = getHtmlColor(m.group(1));
                if (c != (-1)) {
                    android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Foreground(c | 0xff000000));
                }
            }
            m = android.text.HtmlToSpannedConverter.getBackgroundColorPattern().matcher(style);
            if (m.find()) {
                int c = getHtmlColor(m.group(1));
                if (c != (-1)) {
                    android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Background(c | 0xff000000));
                }
            }
            m = android.text.HtmlToSpannedConverter.getTextDecorationPattern().matcher(style);
            if (m.find()) {
                java.lang.String textDecoration = m.group(1);
                if (textDecoration.equalsIgnoreCase("line-through")) {
                    android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Strikethrough());
                }
            }
        }
    }

    private static void endCssStyle(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.Strikethrough s = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Strikethrough.class);
        if (s != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, s, new android.text.style.StrikethroughSpan());
        }
        android.text.HtmlToSpannedConverter.Background b = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Background.class);
        if (b != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, b, new android.text.style.BackgroundColorSpan(b.mBackgroundColor));
        }
        android.text.HtmlToSpannedConverter.Foreground f = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Foreground.class);
        if (f != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, f, new android.text.style.ForegroundColorSpan(f.mForegroundColor));
        }
    }

    private static void startImg(android.text.Editable text, org.xml.sax.Attributes attributes, android.text.Html.ImageGetter img) {
        java.lang.String src = attributes.getValue("", "src");
        android.graphics.drawable.Drawable d = null;
        if (img != null) {
            d = img.getDrawable(src);
        }
        if (d == null) {
            d = android.content.res.Resources.getSystem().getDrawable(com.android.internal.R.drawable.unknown_image);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
        int len = text.length();
        text.append("\ufffc");
        text.setSpan(new android.text.style.ImageSpan(d, src), len, text.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void startFont(android.text.Editable text, org.xml.sax.Attributes attributes) {
        java.lang.String color = attributes.getValue("", "color");
        java.lang.String face = attributes.getValue("", "face");
        if (!android.text.TextUtils.isEmpty(color)) {
            int c = getHtmlColor(color);
            if (c != (-1)) {
                android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Foreground(c | 0xff000000));
            }
        }
        if (!android.text.TextUtils.isEmpty(face)) {
            android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Font(face));
        }
    }

    private static void endFont(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.Font font = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Font.class);
        if (font != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, font, new android.text.style.TypefaceSpan(font.mFace));
        }
        android.text.HtmlToSpannedConverter.Foreground foreground = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Foreground.class);
        if (foreground != null) {
            android.text.HtmlToSpannedConverter.setSpanFromMark(text, foreground, new android.text.style.ForegroundColorSpan(foreground.mForegroundColor));
        }
    }

    private static void startA(android.text.Editable text, org.xml.sax.Attributes attributes) {
        java.lang.String href = attributes.getValue("", "href");
        android.text.HtmlToSpannedConverter.start(text, new android.text.HtmlToSpannedConverter.Href(href));
    }

    private static void endA(android.text.Editable text) {
        android.text.HtmlToSpannedConverter.Href h = android.text.HtmlToSpannedConverter.getLast(text, android.text.HtmlToSpannedConverter.Href.class);
        if (h != null) {
            if (h.mHref != null) {
                android.text.HtmlToSpannedConverter.setSpanFromMark(text, h, new android.text.style.URLSpan(h.mHref));
            }
        }
    }

    private int getHtmlColor(java.lang.String color) {
        if ((mFlags & android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS) == android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS) {
            java.lang.Integer i = android.text.HtmlToSpannedConverter.sColorMap.get(color.toLowerCase(java.util.Locale.US));
            if (i != null) {
                return i;
            }
        }
        return android.graphics.Color.getHtmlColor(color);
    }

    public void setDocumentLocator(org.xml.sax.Locator locator) {
    }

    public void startDocument() throws org.xml.sax.SAXException {
    }

    public void endDocument() throws org.xml.sax.SAXException {
    }

    public void startPrefixMapping(java.lang.String prefix, java.lang.String uri) throws org.xml.sax.SAXException {
    }

    public void endPrefixMapping(java.lang.String prefix) throws org.xml.sax.SAXException {
    }

    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        handleStartTag(localName, attributes);
    }

    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws org.xml.sax.SAXException {
        handleEndTag(localName);
    }

    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        /* Ignore whitespace that immediately follows other whitespace;
        newlines count as spaces.
         */
        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            if ((c == ' ') || (c == '\n')) {
                char pred;
                int len = sb.length();
                if (len == 0) {
                    len = mSpannableStringBuilder.length();
                    if (len == 0) {
                        pred = '\n';
                    } else {
                        pred = mSpannableStringBuilder.charAt(len - 1);
                    }
                } else {
                    pred = sb.charAt(len - 1);
                }
                if ((pred != ' ') && (pred != '\n')) {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
            }
        }
        mSpannableStringBuilder.append(sb);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws org.xml.sax.SAXException {
    }

    public void processingInstruction(java.lang.String target, java.lang.String data) throws org.xml.sax.SAXException {
    }

    public void skippedEntity(java.lang.String name) throws org.xml.sax.SAXException {
    }

    private static class Bold {}

    private static class Italic {}

    private static class Underline {}

    private static class Strikethrough {}

    private static class Big {}

    private static class Small {}

    private static class Monospace {}

    private static class Blockquote {}

    private static class Super {}

    private static class Sub {}

    private static class Bullet {}

    private static class Font {
        public java.lang.String mFace;

        public Font(java.lang.String face) {
            mFace = face;
        }
    }

    private static class Href {
        public java.lang.String mHref;

        public Href(java.lang.String href) {
            mHref = href;
        }
    }

    private static class Foreground {
        private int mForegroundColor;

        public Foreground(int foregroundColor) {
            mForegroundColor = foregroundColor;
        }
    }

    private static class Background {
        private int mBackgroundColor;

        public Background(int backgroundColor) {
            mBackgroundColor = backgroundColor;
        }
    }

    private static class Heading {
        private int mLevel;

        public Heading(int level) {
            mLevel = level;
        }
    }

    private static class Newline {
        private int mNumNewlines;

        public Newline(int numNewlines) {
            mNumNewlines = numNewlines;
        }
    }

    private static class Alignment {
        private android.text.Layout.Alignment mAlignment;

        public Alignment(android.text.Layout.Alignment alignment) {
            mAlignment = alignment;
        }
    }
}

