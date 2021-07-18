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
package android.content.res;


/**
 * Conveniences for retrieving data out of a compiled string resource.
 *
 * {@hide }
 */
final class StringBlock {
    private static final java.lang.String TAG = "AssetManager";

    private static final boolean localLOGV = false;

    private final long mNative;

    private final boolean mUseSparse;

    private final boolean mOwnsNative;

    private java.lang.CharSequence[] mStrings;

    private android.util.SparseArray<java.lang.CharSequence> mSparseStrings;

    @com.android.internal.annotations.GuardedBy("this")
    private boolean mOpen = true;

    android.content.res.StringBlock.StyleIDs mStyleIDs = null;

    public StringBlock(byte[] data, boolean useSparse) {
        mNative = android.content.res.StringBlock.nativeCreate(data, 0, data.length);
        mUseSparse = useSparse;
        mOwnsNative = true;
        if (android.content.res.StringBlock.localLOGV)
            android.util.Log.v(android.content.res.StringBlock.TAG, (("Created string block " + this) + ": ") + android.content.res.StringBlock.nativeGetSize(mNative));

    }

    public StringBlock(byte[] data, int offset, int size, boolean useSparse) {
        mNative = android.content.res.StringBlock.nativeCreate(data, offset, size);
        mUseSparse = useSparse;
        mOwnsNative = true;
        if (android.content.res.StringBlock.localLOGV)
            android.util.Log.v(android.content.res.StringBlock.TAG, (("Created string block " + this) + ": ") + android.content.res.StringBlock.nativeGetSize(mNative));

    }

    @android.annotation.UnsupportedAppUsage
    public java.lang.CharSequence get(int idx) {
        synchronized(this) {
            if (mStrings != null) {
                java.lang.CharSequence res = mStrings[idx];
                if (res != null) {
                    return res;
                }
            } else
                if (mSparseStrings != null) {
                    java.lang.CharSequence res = mSparseStrings.get(idx);
                    if (res != null) {
                        return res;
                    }
                } else {
                    final int num = android.content.res.StringBlock.nativeGetSize(mNative);
                    if (mUseSparse && (num > 250)) {
                        mSparseStrings = new android.util.SparseArray<java.lang.CharSequence>();
                    } else {
                        mStrings = new java.lang.CharSequence[num];
                    }
                }

            java.lang.String str = android.content.res.StringBlock.nativeGetString(mNative, idx);
            java.lang.CharSequence res = str;
            int[] style = android.content.res.StringBlock.nativeGetStyle(mNative, idx);
            if (android.content.res.StringBlock.localLOGV)
                android.util.Log.v(android.content.res.StringBlock.TAG, "Got string: " + str);

            if (android.content.res.StringBlock.localLOGV)
                android.util.Log.v(android.content.res.StringBlock.TAG, "Got styles: " + java.util.Arrays.toString(style));

            if (style != null) {
                if (mStyleIDs == null) {
                    mStyleIDs = new android.content.res.StringBlock.StyleIDs();
                }
                // the style array is a flat array of <type, start, end> hence
                // the magic constant 3.
                for (int styleIndex = 0; styleIndex < style.length; styleIndex += 3) {
                    int styleId = style[styleIndex];
                    if (((((((((((styleId == mStyleIDs.boldId) || (styleId == mStyleIDs.italicId)) || (styleId == mStyleIDs.underlineId)) || (styleId == mStyleIDs.ttId)) || (styleId == mStyleIDs.bigId)) || (styleId == mStyleIDs.smallId)) || (styleId == mStyleIDs.subId)) || (styleId == mStyleIDs.supId)) || (styleId == mStyleIDs.strikeId)) || (styleId == mStyleIDs.listItemId)) || (styleId == mStyleIDs.marqueeId)) {
                        // id already found skip to next style
                        continue;
                    }
                    java.lang.String styleTag = android.content.res.StringBlock.nativeGetString(mNative, styleId);
                    if (styleTag.equals("b")) {
                        mStyleIDs.boldId = styleId;
                    } else
                        if (styleTag.equals("i")) {
                            mStyleIDs.italicId = styleId;
                        } else
                            if (styleTag.equals("u")) {
                                mStyleIDs.underlineId = styleId;
                            } else
                                if (styleTag.equals("tt")) {
                                    mStyleIDs.ttId = styleId;
                                } else
                                    if (styleTag.equals("big")) {
                                        mStyleIDs.bigId = styleId;
                                    } else
                                        if (styleTag.equals("small")) {
                                            mStyleIDs.smallId = styleId;
                                        } else
                                            if (styleTag.equals("sup")) {
                                                mStyleIDs.supId = styleId;
                                            } else
                                                if (styleTag.equals("sub")) {
                                                    mStyleIDs.subId = styleId;
                                                } else
                                                    if (styleTag.equals("strike")) {
                                                        mStyleIDs.strikeId = styleId;
                                                    } else
                                                        if (styleTag.equals("li")) {
                                                            mStyleIDs.listItemId = styleId;
                                                        } else
                                                            if (styleTag.equals("marquee")) {
                                                                mStyleIDs.marqueeId = styleId;
                                                            }










                }
                res = applyStyles(str, style, mStyleIDs);
            }
            if (mStrings != null)
                mStrings[idx] = res;
            else
                mSparseStrings.put(idx, res);

            return res;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            super.finalize();
        } finally {
            close();
        }
    }

    public void close() throws java.lang.Throwable {
        synchronized(this) {
            if (mOpen) {
                mOpen = false;
                if (mOwnsNative) {
                    android.content.res.StringBlock.nativeDestroy(mNative);
                }
            }
        }
    }

    static final class StyleIDs {
        private int boldId = -1;

        private int italicId = -1;

        private int underlineId = -1;

        private int ttId = -1;

        private int bigId = -1;

        private int smallId = -1;

        private int subId = -1;

        private int supId = -1;

        private int strikeId = -1;

        private int listItemId = -1;

        private int marqueeId = -1;
    }

    private java.lang.CharSequence applyStyles(java.lang.String str, int[] style, android.content.res.StringBlock.StyleIDs ids) {
        if (style.length == 0)
            return str;

        android.text.SpannableString buffer = new android.text.SpannableString(str);
        int i = 0;
        while (i < style.length) {
            int type = style[i];
            if (android.content.res.StringBlock.localLOGV)
                android.util.Log.v(android.content.res.StringBlock.TAG, (((("Applying style span id=" + type) + ", start=") + style[i + 1]) + ", end=") + style[i + 2]);

            if (type == ids.boldId) {
                buffer.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else
                if (type == ids.italicId) {
                    buffer.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else
                    if (type == ids.underlineId) {
                        buffer.setSpan(new android.text.style.UnderlineSpan(), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        if (type == ids.ttId) {
                            buffer.setSpan(new android.text.style.TypefaceSpan("monospace"), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            if (type == ids.bigId) {
                                buffer.setSpan(new android.text.style.RelativeSizeSpan(1.25F), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else
                                if (type == ids.smallId) {
                                    buffer.setSpan(new android.text.style.RelativeSizeSpan(0.8F), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else
                                    if (type == ids.subId) {
                                        buffer.setSpan(new android.text.style.SubscriptSpan(), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else
                                        if (type == ids.supId) {
                                            buffer.setSpan(new android.text.style.SuperscriptSpan(), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        } else
                                            if (type == ids.strikeId) {
                                                buffer.setSpan(new android.text.style.StrikethroughSpan(), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            } else
                                                if (type == ids.listItemId) {
                                                    android.content.res.StringBlock.addParagraphSpan(buffer, new android.text.style.BulletSpan(10), style[i + 1], style[i + 2] + 1);
                                                } else
                                                    if (type == ids.marqueeId) {
                                                        buffer.setSpan(TextUtils.TruncateAt.MARQUEE, style[i + 1], style[i + 2] + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                    } else {
                                                        java.lang.String tag = android.content.res.StringBlock.nativeGetString(mNative, type);
                                                        if (tag.startsWith("font;")) {
                                                            java.lang.String sub;
                                                            sub = android.content.res.StringBlock.subtag(tag, ";height=");
                                                            if (sub != null) {
                                                                int size = java.lang.Integer.parseInt(sub);
                                                                android.content.res.StringBlock.addParagraphSpan(buffer, new android.content.res.StringBlock.Height(size), style[i + 1], style[i + 2] + 1);
                                                            }
                                                            sub = android.content.res.StringBlock.subtag(tag, ";size=");
                                                            if (sub != null) {
                                                                int size = java.lang.Integer.parseInt(sub);
                                                                buffer.setSpan(new android.text.style.AbsoluteSizeSpan(size, true), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                            sub = android.content.res.StringBlock.subtag(tag, ";fgcolor=");
                                                            if (sub != null) {
                                                                buffer.setSpan(android.content.res.StringBlock.getColor(sub, true), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                            sub = android.content.res.StringBlock.subtag(tag, ";color=");
                                                            if (sub != null) {
                                                                buffer.setSpan(android.content.res.StringBlock.getColor(sub, true), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                            sub = android.content.res.StringBlock.subtag(tag, ";bgcolor=");
                                                            if (sub != null) {
                                                                buffer.setSpan(android.content.res.StringBlock.getColor(sub, false), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                            sub = android.content.res.StringBlock.subtag(tag, ";face=");
                                                            if (sub != null) {
                                                                buffer.setSpan(new android.text.style.TypefaceSpan(sub), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                        } else
                                                            if (tag.startsWith("a;")) {
                                                                java.lang.String sub;
                                                                sub = android.content.res.StringBlock.subtag(tag, ";href=");
                                                                if (sub != null) {
                                                                    buffer.setSpan(new android.text.style.URLSpan(sub), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                                }
                                                            } else
                                                                if (tag.startsWith("annotation;")) {
                                                                    int len = tag.length();
                                                                    int next;
                                                                    for (int t = tag.indexOf(';'); t < len; t = next) {
                                                                        int eq = tag.indexOf('=', t);
                                                                        if (eq < 0) {
                                                                            break;
                                                                        }
                                                                        next = tag.indexOf(';', eq);
                                                                        if (next < 0) {
                                                                            next = len;
                                                                        }
                                                                        java.lang.String key = tag.substring(t + 1, eq);
                                                                        java.lang.String value = tag.substring(eq + 1, next);
                                                                        buffer.setSpan(new android.text.Annotation(key, value), style[i + 1], style[i + 2] + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                                    }
                                                                }


                                                    }










            i += 3;
        } 
        return new android.text.SpannedString(buffer);
    }

    /**
     * Returns a span for the specified color string representation.
     * If the specified string does not represent a color (null, empty, etc.)
     * the color black is returned instead.
     *
     * @param color
     * 		The color as a string. Can be a resource reference,
     * 		hexadecimal, octal or a name
     * @param foreground
     * 		True if the color will be used as the foreground color,
     * 		false otherwise
     * @return A CharacterStyle
     * @see Color#parseColor(String)
     */
    private static android.text.style.CharacterStyle getColor(java.lang.String color, boolean foreground) {
        int c = 0xff000000;
        if (!android.text.TextUtils.isEmpty(color)) {
            if (color.startsWith("@")) {
                android.content.res.Resources res = android.content.res.Resources.getSystem();
                java.lang.String name = color.substring(1);
                int colorRes = res.getIdentifier(name, "color", "android");
                if (colorRes != 0) {
                    android.content.res.ColorStateList colors = res.getColorStateList(colorRes, null);
                    if (foreground) {
                        return new android.text.style.TextAppearanceSpan(null, 0, 0, colors, null);
                    } else {
                        c = colors.getDefaultColor();
                    }
                }
            } else {
                try {
                    c = android.graphics.Color.parseColor(color);
                } catch (java.lang.IllegalArgumentException e) {
                    c = android.graphics.Color.BLACK;
                }
            }
        }
        if (foreground) {
            return new android.text.style.ForegroundColorSpan(c);
        } else {
            return new android.text.style.BackgroundColorSpan(c);
        }
    }

    /**
     * If a translator has messed up the edges of paragraph-level markup,
     * fix it to actually cover the entire paragraph that it is attached to
     * instead of just whatever range they put it on.
     */
    private static void addParagraphSpan(android.text.Spannable buffer, java.lang.Object what, int start, int end) {
        int len = buffer.length();
        if (((start != 0) && (start != len)) && (buffer.charAt(start - 1) != '\n')) {
            for (start--; start > 0; start--) {
                if (buffer.charAt(start - 1) == '\n') {
                    break;
                }
            }
        }
        if (((end != 0) && (end != len)) && (buffer.charAt(end - 1) != '\n')) {
            for (end++; end < len; end++) {
                if (buffer.charAt(end - 1) == '\n') {
                    break;
                }
            }
        }
        buffer.setSpan(what, start, end, Spannable.SPAN_PARAGRAPH);
    }

    private static java.lang.String subtag(java.lang.String full, java.lang.String attribute) {
        int start = full.indexOf(attribute);
        if (start < 0) {
            return null;
        }
        start += attribute.length();
        int end = full.indexOf(';', start);
        if (end < 0) {
            return full.substring(start);
        } else {
            return full.substring(start, end);
        }
    }

    /**
     * Forces the text line to be the specified height, shrinking/stretching
     * the ascent if possible, or the descent if shrinking the ascent further
     * will make the text unreadable.
     */
    private static class Height implements android.text.style.LineHeightSpan.WithDensity {
        private int mSize;

        private static float sProportion = 0;

        public Height(int size) {
            mSize = size;
        }

        public void chooseHeight(java.lang.CharSequence text, int start, int end, int spanstartv, int v, android.graphics.Paint.FontMetricsInt fm) {
            // Should not get called, at least not by StaticLayout.
            chooseHeight(text, start, end, spanstartv, v, fm, null);
        }

        public void chooseHeight(java.lang.CharSequence text, int start, int end, int spanstartv, int v, android.graphics.Paint.FontMetricsInt fm, android.text.TextPaint paint) {
            int size = mSize;
            if (paint != null) {
                size *= paint.density;
            }
            if ((fm.bottom - fm.top) < size) {
                fm.top = fm.bottom - size;
                fm.ascent = fm.ascent - size;
            } else {
                if (android.content.res.StringBlock.Height.sProportion == 0) {
                    /* Calculate what fraction of the nominal ascent
                    the height of a capital letter actually is,
                    so that we won't reduce the ascent to less than
                    that unless we absolutely have to.
                     */
                    android.graphics.Paint p = new android.graphics.Paint();
                    p.setTextSize(100);
                    android.graphics.Rect r = new android.graphics.Rect();
                    p.getTextBounds("ABCDEFG", 0, 7, r);
                    android.content.res.StringBlock.Height.sProportion = r.top / p.ascent();
                }
                int need = ((int) (java.lang.Math.ceil((-fm.top) * android.content.res.StringBlock.Height.sProportion)));
                if ((size - fm.descent) >= need) {
                    /* It is safe to shrink the ascent this much. */
                    fm.top = fm.bottom - size;
                    fm.ascent = fm.descent - size;
                } else
                    if (size >= need) {
                        /* We can't show all the descent, but we can at least
                        show all the ascent.
                         */
                        fm.top = fm.ascent = -need;
                        fm.bottom = fm.descent = fm.top + size;
                    } else {
                        /* Show as much of the ascent as we can, and no descent. */
                        fm.top = fm.ascent = -size;
                        fm.bottom = fm.descent = 0;
                    }

            }
        }
    }

    /**
     * Create from an existing string block native object.  This is
     * -extremely- dangerous -- only use it if you absolutely know what you
     *  are doing!  The given native object must exist for the entire lifetime
     *  of this newly creating StringBlock.
     */
    @android.annotation.UnsupportedAppUsage
    StringBlock(long obj, boolean useSparse) {
        mNative = obj;
        mUseSparse = useSparse;
        mOwnsNative = false;
        if (android.content.res.StringBlock.localLOGV)
            android.util.Log.v(android.content.res.StringBlock.TAG, (("Created string block " + this) + ": ") + android.content.res.StringBlock.nativeGetSize(mNative));

    }

    private static native long nativeCreate(byte[] data, int offset, int size);

    private static native int nativeGetSize(long obj);

    private static native java.lang.String nativeGetString(long obj, int idx);

    private static native int[] nativeGetStyle(long obj, int idx);

    private static native void nativeDestroy(long obj);
}

