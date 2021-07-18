/**
 * Copyright (c) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.content;


/**
 * Representation of a clipped data on the clipboard.
 *
 * <p>ClipData is a complex type containing one or more Item instances,
 * each of which can hold one or more representations of an item of data.
 * For display to the user, it also has a label.</p>
 *
 * <p>A ClipData contains a {@link ClipDescription}, which describes
 * important meta-data about the clip.  In particular, its
 * {@link ClipDescription#getMimeType(int) getDescription().getMimeType(int)}
 * must return correct MIME type(s) describing the data in the clip.  For help
 * in correctly constructing a clip with the correct MIME type, use
 * {@link #newPlainText(CharSequence, CharSequence)},
 * {@link #newUri(ContentResolver, CharSequence, Uri)}, and
 * {@link #newIntent(CharSequence, Intent)}.
 *
 * <p>Each Item instance can be one of three main classes of data: a simple
 * CharSequence of text, a single Intent object, or a Uri.  See {@link Item}
 * for more details.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using the clipboard framework, read the
 * <a href="{@docRoot }guide/topics/clipboard/copy-paste.html">Copy and Paste</a>
 * developer guide.</p>
 * </div>
 *
 * <a name="ImplementingPaste"></a>
 * <h3>Implementing Paste or Drop</h3>
 *
 * <p>To implement a paste or drop of a ClipData object into an application,
 * the application must correctly interpret the data for its use.  If the {@link Item}
 * it contains is simple text or an Intent, there is little to be done: text
 * can only be interpreted as text, and an Intent will typically be used for
 * creating shortcuts (such as placing icons on the home screen) or other
 * actions.
 *
 * <p>If all you want is the textual representation of the clipped data, you
 * can use the convenience method {@link Item#coerceToText Item.coerceToText}.
 * In this case there is generally no need to worry about the MIME types
 * reported by {@link ClipDescription#getMimeType(int) getDescription().getMimeType(int)},
 * since any clip item can always be converted to a string.
 *
 * <p>More complicated exchanges will be done through URIs, in particular
 * "content:" URIs.  A content URI allows the recipient of a ClipData item
 * to interact closely with the ContentProvider holding the data in order to
 * negotiate the transfer of that data.  The clip must also be filled in with
 * the available MIME types; {@link #newUri(ContentResolver, CharSequence, Uri)}
 * will take care of correctly doing this.
 *
 * <p>For example, here is the paste function of a simple NotePad application.
 * When retrieving the data from the clipboard, it can do either two things:
 * if the clipboard contains a URI reference to an existing note, it copies
 * the entire structure of the note into a new note; otherwise, it simply
 * coerces the clip into text and uses that as the new note's contents.
 *
 * {@sample development/samples/NotePad/src/com/example/android/notepad/NoteEditor.java
 *      paste}
 *
 * <p>In many cases an application can paste various types of streams of data.  For
 * example, an e-mail application may want to allow the user to paste an image
 * or other binary data as an attachment.  This is accomplished through the
 * ContentResolver {@link ContentResolver#getStreamTypes(Uri, String)} and
 * {@link ContentResolver#openTypedAssetFileDescriptor(Uri, String, android.os.Bundle)}
 * methods.  These allow a client to discover the type(s) of data that a particular
 * content URI can make available as a stream and retrieve the stream of data.
 *
 * <p>For example, the implementation of {@link Item#coerceToText Item.coerceToText}
 * itself uses this to try to retrieve a URI clip as a stream of text:
 *
 * {@sample frameworks/base/core/java/android/content/ClipData.java coerceToText}
 *
 * <a name="ImplementingCopy"></a>
 * <h3>Implementing Copy or Drag</h3>
 *
 * <p>To be the source of a clip, the application must construct a ClipData
 * object that any recipient can interpret best for their context.  If the clip
 * is to contain a simple text, Intent, or URI, this is easy: an {@link Item}
 * containing the appropriate data type can be constructed and used.
 *
 * <p>More complicated data types require the implementation of support in
 * a ContentProvider for describing and generating the data for the recipient.
 * A common scenario is one where an application places on the clipboard the
 * content: URI of an object that the user has copied, with the data at that
 * URI consisting of a complicated structure that only other applications with
 * direct knowledge of the structure can use.
 *
 * <p>For applications that do not have intrinsic knowledge of the data structure,
 * the content provider holding it can make the data available as an arbitrary
 * number of types of data streams.  This is done by implementing the
 * ContentProvider {@link ContentProvider#getStreamTypes(Uri, String)} and
 * {@link ContentProvider#openTypedAssetFile(Uri, String, android.os.Bundle)}
 * methods.
 *
 * <p>Going back to our simple NotePad application, this is the implementation
 * it may have to convert a single note URI (consisting of a title and the note
 * text) into a stream of plain text data.
 *
 * {@sample development/samples/NotePad/src/com/example/android/notepad/NotePadProvider.java
 *      stream}
 *
 * <p>The copy operation in our NotePad application is now just a simple matter
 * of making a clip containing the URI of the note being copied:
 *
 * {@sample development/samples/NotePad/src/com/example/android/notepad/NotesList.java
 *      copy}
 *
 * <p>Note if a paste operation needs this clip as text (for example to paste
 * into an editor), then {@link Item#coerceToText(Context)} will ask the content
 * provider for the clip URI as text and successfully paste the entire note.
 */
public class ClipData implements android.os.Parcelable {
    static final java.lang.String[] MIMETYPES_TEXT_PLAIN = new java.lang.String[]{ android.content.ClipDescription.MIMETYPE_TEXT_PLAIN };

    static final java.lang.String[] MIMETYPES_TEXT_HTML = new java.lang.String[]{ android.content.ClipDescription.MIMETYPE_TEXT_HTML };

    static final java.lang.String[] MIMETYPES_TEXT_URILIST = new java.lang.String[]{ android.content.ClipDescription.MIMETYPE_TEXT_URILIST };

    static final java.lang.String[] MIMETYPES_TEXT_INTENT = new java.lang.String[]{ android.content.ClipDescription.MIMETYPE_TEXT_INTENT };

    // Constants used in {@link #writeHtmlTextToParcel}.
    static final int PARCEL_MAX_SIZE_BYTES = 800 * 1024;

    static final int PARCEL_TYPE_STRING = 0;

    static final int PARCEL_TYPE_PFD = 1;

    final android.content.ClipDescription mClipDescription;

    final android.graphics.Bitmap mIcon;

    final java.util.ArrayList<android.content.ClipData.Item> mItems;

    /**
     * Description of a single item in a ClipData.
     *
     * <p>The types than an individual item can currently contain are:</p>
     *
     * <ul>
     * <li> Text: a basic string of text.  This is actually a CharSequence,
     * so it can be formatted text supported by corresponding Android built-in
     * style spans.  (Custom application spans are not supported and will be
     * stripped when transporting through the clipboard.)
     * <li> Intent: an arbitrary Intent object.  A typical use is the shortcut
     * to create when pasting a clipped item on to the home screen.
     * <li> Uri: a URI reference.  This may be any URI (such as an http: URI
     * representing a bookmark), however it is often a content: URI.  Using
     * content provider references as clips like this allows an application to
     * share complex or large clips through the standard content provider
     * facilities.
     * </ul>
     */
    public static class Item {
        final java.lang.CharSequence mText;

        final java.lang.String mHtmlText;

        final android.content.Intent mIntent;

        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
        android.net.Uri mUri;

        /**
         *
         *
         * @unknown 
         */
        public Item(android.content.ClipData.Item other) {
            mText = other.mText;
            mHtmlText = other.mHtmlText;
            mIntent = other.mIntent;
            mUri = other.mUri;
        }

        /**
         * Create an Item consisting of a single block of (possibly styled) text.
         */
        public Item(java.lang.CharSequence text) {
            mText = text;
            mHtmlText = null;
            mIntent = null;
            mUri = null;
        }

        /**
         * Create an Item consisting of a single block of (possibly styled) text,
         * with an alternative HTML formatted representation.  You <em>must</em>
         * supply a plain text representation in addition to HTML text; coercion
         * will not be done from HTML formatted text into plain text.
         * <p class="note"><strong>Note:</strong> It is strongly recommended to
         * use content: URI for sharing large clip data. Starting on API 30,
         * ClipData.Item doesn't accept an HTML text if it's larger than 800KB.
         * </p>
         */
        public Item(java.lang.CharSequence text, java.lang.String htmlText) {
            mText = text;
            mHtmlText = htmlText;
            mIntent = null;
            mUri = null;
        }

        /**
         * Create an Item consisting of an arbitrary Intent.
         */
        public Item(android.content.Intent intent) {
            mText = null;
            mHtmlText = null;
            mIntent = intent;
            mUri = null;
        }

        /**
         * Create an Item consisting of an arbitrary URI.
         */
        public Item(android.net.Uri uri) {
            mText = null;
            mHtmlText = null;
            mIntent = null;
            mUri = uri;
        }

        /**
         * Create a complex Item, containing multiple representations of
         * text, Intent, and/or URI.
         */
        public Item(java.lang.CharSequence text, android.content.Intent intent, android.net.Uri uri) {
            mText = text;
            mHtmlText = null;
            mIntent = intent;
            mUri = uri;
        }

        /**
         * Create a complex Item, containing multiple representations of
         * text, HTML text, Intent, and/or URI.  If providing HTML text, you
         * <em>must</em> supply a plain text representation as well; coercion
         * will not be done from HTML formatted text into plain text.
         */
        public Item(java.lang.CharSequence text, java.lang.String htmlText, android.content.Intent intent, android.net.Uri uri) {
            if ((htmlText != null) && (text == null)) {
                throw new java.lang.IllegalArgumentException("Plain text must be supplied if HTML text is supplied");
            }
            mText = text;
            mHtmlText = htmlText;
            mIntent = intent;
            mUri = uri;
        }

        /**
         * Retrieve the raw text contained in this Item.
         */
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         * Retrieve the raw HTML text contained in this Item.
         */
        public java.lang.String getHtmlText() {
            return mHtmlText;
        }

        /**
         * Retrieve the raw Intent contained in this Item.
         */
        public android.content.Intent getIntent() {
            return mIntent;
        }

        /**
         * Retrieve the raw URI contained in this Item.
         */
        public android.net.Uri getUri() {
            return mUri;
        }

        /**
         * Turn this item into text, regardless of the type of data it
         * actually contains.
         *
         * <p>The algorithm for deciding what text to return is:
         * <ul>
         * <li> If {@link #getText} is non-null, return that.
         * <li> If {@link #getUri} is non-null, try to retrieve its data
         * as a text stream from its content provider.  If this succeeds, copy
         * the text into a String and return it.  If it is not a content: URI or
         * the content provider does not supply a text representation, return
         * the raw URI as a string.
         * <li> If {@link #getIntent} is non-null, convert that to an intent:
         * URI and return it.
         * <li> Otherwise, return an empty string.
         * </ul>
         *
         * @param context
         * 		The caller's Context, from which its ContentResolver
         * 		and other things can be retrieved.
         * @return Returns the item's textual representation.
         */
        // BEGIN_INCLUDE(coerceToText)
        public java.lang.CharSequence coerceToText(android.content.Context context) {
            // If this Item has an explicit textual value, simply return that.
            java.lang.CharSequence text = getText();
            if (text != null) {
                return text;
            }
            // If this Item has a URI value, try using that.
            android.net.Uri uri = getUri();
            if (uri != null) {
                // First see if the URI can be opened as a plain text stream
                // (of any sub-type).  If so, this is the best textual
                // representation for it.
                final android.content.ContentResolver resolver = context.getContentResolver();
                android.content.res.AssetFileDescriptor descr = null;
                java.io.FileInputStream stream = null;
                java.io.InputStreamReader reader = null;
                try {
                    try {
                        // Ask for a stream of the desired type.
                        descr = resolver.openTypedAssetFileDescriptor(uri, "text/*", null);
                    } catch (java.lang.SecurityException e) {
                        android.util.Log.w("ClipData", "Failure opening stream", e);
                    } catch (java.io.FileNotFoundException | java.lang.RuntimeException e) {
                        // Unable to open content URI as text...  not really an
                        // error, just something to ignore.
                    }
                    if (descr != null) {
                        try {
                            stream = descr.createInputStream();
                            reader = new java.io.InputStreamReader(stream, "UTF-8");
                            // Got it...  copy the stream into a local string and return it.
                            final java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
                            char[] buffer = new char[8192];
                            int len;
                            while ((len = reader.read(buffer)) > 0) {
                                builder.append(buffer, 0, len);
                            } 
                            return builder.toString();
                        } catch (java.io.IOException e) {
                            // Something bad has happened.
                            android.util.Log.w("ClipData", "Failure loading text", e);
                            return e.toString();
                        }
                    }
                } finally {
                    libcore.io.IoUtils.closeQuietly(descr);
                    libcore.io.IoUtils.closeQuietly(stream);
                    libcore.io.IoUtils.closeQuietly(reader);
                }
                // If we couldn't open the URI as a stream, use the URI itself as a textual
                // representation (but not for "content", "android.resource" or "file" schemes).
                final java.lang.String scheme = uri.getScheme();
                if ((android.content.ContentResolver.SCHEME_CONTENT.equals(scheme) || android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) || android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                    return "";
                }
                return uri.toString();
            }
            // Finally, if all we have is an Intent, then we can just turn that
            // into text.  Not the most user-friendly thing, but it's something.
            android.content.Intent intent = getIntent();
            if (intent != null) {
                return intent.toUri(android.content.Intent.URI_INTENT_SCHEME);
            }
            // Shouldn't get here, but just in case...
            return "";
        }

        // END_INCLUDE(coerceToText)
        /**
         * Like {@link #coerceToHtmlText(Context)}, but any text that would
         * be returned as HTML formatting will be returned as text with
         * style spans.
         *
         * @param context
         * 		The caller's Context, from which its ContentResolver
         * 		and other things can be retrieved.
         * @return Returns the item's textual representation.
         */
        public java.lang.CharSequence coerceToStyledText(android.content.Context context) {
            java.lang.CharSequence text = getText();
            if (text instanceof android.text.Spanned) {
                return text;
            }
            java.lang.String htmlText = getHtmlText();
            if (htmlText != null) {
                try {
                    java.lang.CharSequence newText = android.text.Html.fromHtml(htmlText);
                    if (newText != null) {
                        return newText;
                    }
                } catch (java.lang.RuntimeException e) {
                    // If anything bad happens, we'll fall back on the plain text.
                }
            }
            if (text != null) {
                return text;
            }
            return coerceToHtmlOrStyledText(context, true);
        }

        /**
         * Turn this item into HTML text, regardless of the type of data it
         * actually contains.
         *
         * <p>The algorithm for deciding what text to return is:
         * <ul>
         * <li> If {@link #getHtmlText} is non-null, return that.
         * <li> If {@link #getText} is non-null, return that, converting to
         * valid HTML text.  If this text contains style spans,
         * {@link Html#toHtml(Spanned) Html.toHtml(Spanned)} is used to
         * convert them to HTML formatting.
         * <li> If {@link #getUri} is non-null, try to retrieve its data
         * as a text stream from its content provider.  If the provider can
         * supply text/html data, that will be preferred and returned as-is.
         * Otherwise, any text/* data will be returned and escaped to HTML.
         * If it is not a content: URI or the content provider does not supply
         * a text representation, HTML text containing a link to the URI
         * will be returned.
         * <li> If {@link #getIntent} is non-null, convert that to an intent:
         * URI and return as an HTML link.
         * <li> Otherwise, return an empty string.
         * </ul>
         *
         * @param context
         * 		The caller's Context, from which its ContentResolver
         * 		and other things can be retrieved.
         * @return Returns the item's representation as HTML text.
         */
        public java.lang.String coerceToHtmlText(android.content.Context context) {
            // If the item has an explicit HTML value, simply return that.
            java.lang.String htmlText = getHtmlText();
            if (htmlText != null) {
                return htmlText;
            }
            // If this Item has a plain text value, return it as HTML.
            java.lang.CharSequence text = getText();
            if (text != null) {
                if (text instanceof android.text.Spanned) {
                    return android.text.Html.toHtml(((android.text.Spanned) (text)));
                }
                return android.text.Html.escapeHtml(text);
            }
            text = coerceToHtmlOrStyledText(context, false);
            return text != null ? text.toString() : null;
        }

        private java.lang.CharSequence coerceToHtmlOrStyledText(android.content.Context context, boolean styled) {
            // If this Item has a URI value, try using that.
            if (mUri != null) {
                // Check to see what data representations the content
                // provider supports.  We would like HTML text, but if that
                // is not possible we'll live with plan text.
                java.lang.String[] types = null;
                try {
                    types = context.getContentResolver().getStreamTypes(mUri, "text/*");
                } catch (java.lang.SecurityException e) {
                    // No read permission for mUri, assume empty stream types list.
                }
                boolean hasHtml = false;
                boolean hasText = false;
                if (types != null) {
                    for (java.lang.String type : types) {
                        if ("text/html".equals(type)) {
                            hasHtml = true;
                        } else
                            if (type.startsWith("text/")) {
                                hasText = true;
                            }

                    }
                }
                // If the provider can serve data we can use, open and load it.
                if (hasHtml || hasText) {
                    java.io.FileInputStream stream = null;
                    try {
                        // Ask for a stream of the desired type.
                        android.content.res.AssetFileDescriptor descr = context.getContentResolver().openTypedAssetFileDescriptor(mUri, hasHtml ? "text/html" : "text/plain", null);
                        stream = descr.createInputStream();
                        java.io.InputStreamReader reader = new java.io.InputStreamReader(stream, "UTF-8");
                        // Got it...  copy the stream into a local string and return it.
                        java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
                        char[] buffer = new char[8192];
                        int len;
                        while ((len = reader.read(buffer)) > 0) {
                            builder.append(buffer, 0, len);
                        } 
                        java.lang.String text = builder.toString();
                        if (hasHtml) {
                            if (styled) {
                                // We loaded HTML formatted text and the caller
                                // want styled text, convert it.
                                try {
                                    java.lang.CharSequence newText = android.text.Html.fromHtml(text);
                                    return newText != null ? newText : text;
                                } catch (java.lang.RuntimeException e) {
                                    return text;
                                }
                            } else {
                                // We loaded HTML formatted text and that is what
                                // the caller wants, just return it.
                                return text.toString();
                            }
                        }
                        if (styled) {
                            // We loaded plain text and the caller wants styled
                            // text, that is all we have so return it.
                            return text;
                        } else {
                            // We loaded plain text and the caller wants HTML
                            // text, escape it for HTML.
                            return android.text.Html.escapeHtml(text);
                        }
                    } catch (java.lang.SecurityException e) {
                        android.util.Log.w("ClipData", "Failure opening stream", e);
                    } catch (java.io.FileNotFoundException e) {
                        // Unable to open content URI as text...  not really an
                        // error, just something to ignore.
                    } catch (java.io.IOException e) {
                        // Something bad has happened.
                        android.util.Log.w("ClipData", "Failure loading text", e);
                        return android.text.Html.escapeHtml(e.toString());
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (java.io.IOException e) {
                            }
                        }
                    }
                }
                // If we couldn't open the URI as a stream, use the URI itself as a textual
                // representation (but not for "content", "android.resource" or "file" schemes).
                final java.lang.String scheme = mUri.getScheme();
                if ((android.content.ContentResolver.SCHEME_CONTENT.equals(scheme) || android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) || android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                    return "";
                }
                if (styled) {
                    return uriToStyledText(mUri.toString());
                } else {
                    return uriToHtml(mUri.toString());
                }
            }
            // Finally, if all we have is an Intent, then we can just turn that
            // into text.  Not the most user-friendly thing, but it's something.
            if (mIntent != null) {
                if (styled) {
                    return uriToStyledText(mIntent.toUri(android.content.Intent.URI_INTENT_SCHEME));
                } else {
                    return uriToHtml(mIntent.toUri(android.content.Intent.URI_INTENT_SCHEME));
                }
            }
            // Shouldn't get here, but just in case...
            return "";
        }

        private java.lang.String uriToHtml(java.lang.String uri) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder(256);
            builder.append("<a href=\"");
            builder.append(android.text.Html.escapeHtml(uri));
            builder.append("\">");
            builder.append(android.text.Html.escapeHtml(uri));
            builder.append("</a>");
            return builder.toString();
        }

        private java.lang.CharSequence uriToStyledText(java.lang.String uri) {
            android.text.SpannableStringBuilder builder = new android.text.SpannableStringBuilder();
            builder.append(uri);
            builder.setSpan(new android.text.style.URLSpan(uri), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder b = new java.lang.StringBuilder(128);
            b.append("ClipData.Item { ");
            toShortString(b);
            b.append(" }");
            return b.toString();
        }

        /**
         *
         *
         * @unknown 
         */
        public void toShortString(java.lang.StringBuilder b) {
            if (mHtmlText != null) {
                b.append("H:");
                b.append(mHtmlText);
            } else
                if (mText != null) {
                    b.append("T:");
                    b.append(mText);
                } else
                    if (mUri != null) {
                        b.append("U:");
                        b.append(mUri);
                    } else
                        if (mIntent != null) {
                            b.append("I:");
                            mIntent.toShortString(b, true, true, true, true);
                        } else {
                            b.append("NULL");
                        }



        }

        /**
         *
         *
         * @unknown 
         */
        public void toShortSummaryString(java.lang.StringBuilder b) {
            if (mHtmlText != null) {
                b.append("HTML");
            } else
                if (mText != null) {
                    b.append("TEXT");
                } else
                    if (mUri != null) {
                        b.append("U:");
                        b.append(mUri);
                    } else
                        if (mIntent != null) {
                            b.append("I:");
                            mIntent.toShortString(b, true, true, true, true);
                        } else {
                            b.append("NULL");
                        }



        }

        /**
         *
         *
         * @unknown 
         */
        public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
            final long token = proto.start(fieldId);
            if (mHtmlText != null) {
                proto.write(ClipDataProto.Item.HTML_TEXT, mHtmlText);
            } else
                if (mText != null) {
                    proto.write(ClipDataProto.Item.TEXT, mText.toString());
                } else
                    if (mUri != null) {
                        proto.write(ClipDataProto.Item.URI, mUri.toString());
                    } else
                        if (mIntent != null) {
                            mIntent.writeToProto(proto, ClipDataProto.Item.INTENT, true, true, true, true);
                        } else {
                            proto.write(ClipDataProto.Item.NOTHING, true);
                        }



            proto.end(token);
        }
    }

    /**
     * Create a new clip.
     *
     * @param label
     * 		Label to show to the user describing this clip.
     * @param mimeTypes
     * 		An array of MIME types this data is available as.
     * @param item
     * 		The contents of the first item in the clip.
     */
    public ClipData(java.lang.CharSequence label, java.lang.String[] mimeTypes, android.content.ClipData.Item item) {
        mClipDescription = new android.content.ClipDescription(label, mimeTypes);
        if (item == null) {
            throw new java.lang.NullPointerException("item is null");
        }
        mIcon = null;
        mItems = new java.util.ArrayList<android.content.ClipData.Item>();
        mItems.add(item);
    }

    /**
     * Create a new clip.
     *
     * @param description
     * 		The ClipDescription describing the clip contents.
     * @param item
     * 		The contents of the first item in the clip.
     */
    public ClipData(android.content.ClipDescription description, android.content.ClipData.Item item) {
        mClipDescription = description;
        if (item == null) {
            throw new java.lang.NullPointerException("item is null");
        }
        mIcon = null;
        mItems = new java.util.ArrayList<android.content.ClipData.Item>();
        mItems.add(item);
    }

    /**
     * Create a new clip.
     *
     * @param description
     * 		The ClipDescription describing the clip contents.
     * @param items
     * 		The items in the clip. Not that a defensive copy of this
     * 		list is not made, so caution should be taken to ensure the
     * 		list is not available for further modification.
     * @unknown 
     */
    public ClipData(android.content.ClipDescription description, java.util.ArrayList<android.content.ClipData.Item> items) {
        mClipDescription = description;
        if (items == null) {
            throw new java.lang.NullPointerException("item is null");
        }
        mIcon = null;
        mItems = items;
    }

    /**
     * Create a new clip that is a copy of another clip.  This does a deep-copy
     * of all items in the clip.
     *
     * @param other
     * 		The existing ClipData that is to be copied.
     */
    public ClipData(android.content.ClipData other) {
        mClipDescription = other.mClipDescription;
        mIcon = other.mIcon;
        mItems = new java.util.ArrayList<android.content.ClipData.Item>(other.mItems);
    }

    /**
     * Create a new ClipData holding data of the type
     * {@link ClipDescription#MIMETYPE_TEXT_PLAIN}.
     *
     * @param label
     * 		User-visible label for the clip data.
     * @param text
     * 		The actual text in the clip.
     * @return Returns a new ClipData containing the specified data.
     */
    public static android.content.ClipData newPlainText(java.lang.CharSequence label, java.lang.CharSequence text) {
        android.content.ClipData.Item item = new android.content.ClipData.Item(text);
        return new android.content.ClipData(label, android.content.ClipData.MIMETYPES_TEXT_PLAIN, item);
    }

    /**
     * Create a new ClipData holding data of the type
     * {@link ClipDescription#MIMETYPE_TEXT_HTML}.
     *
     * @param label
     * 		User-visible label for the clip data.
     * @param text
     * 		The text of clip as plain text, for receivers that don't
     * 		handle HTML.  This is required.
     * @param htmlText
     * 		The actual HTML text in the clip.
     * @return Returns a new ClipData containing the specified data.
     */
    public static android.content.ClipData newHtmlText(java.lang.CharSequence label, java.lang.CharSequence text, java.lang.String htmlText) {
        android.content.ClipData.Item item = new android.content.ClipData.Item(text, htmlText);
        return new android.content.ClipData(label, android.content.ClipData.MIMETYPES_TEXT_HTML, item);
    }

    /**
     * Create a new ClipData holding an Intent with MIME type
     * {@link ClipDescription#MIMETYPE_TEXT_INTENT}.
     *
     * @param label
     * 		User-visible label for the clip data.
     * @param intent
     * 		The actual Intent in the clip.
     * @return Returns a new ClipData containing the specified data.
     */
    public static android.content.ClipData newIntent(java.lang.CharSequence label, android.content.Intent intent) {
        android.content.ClipData.Item item = new android.content.ClipData.Item(intent);
        return new android.content.ClipData(label, android.content.ClipData.MIMETYPES_TEXT_INTENT, item);
    }

    /**
     * Create a new ClipData holding a URI.  If the URI is a content: URI,
     * this will query the content provider for the MIME type of its data and
     * use that as the MIME type.  Otherwise, it will use the MIME type
     * {@link ClipDescription#MIMETYPE_TEXT_URILIST}.
     *
     * @param resolver
     * 		ContentResolver used to get information about the URI.
     * @param label
     * 		User-visible label for the clip data.
     * @param uri
     * 		The URI in the clip.
     * @return Returns a new ClipData containing the specified data.
     */
    public static android.content.ClipData newUri(android.content.ContentResolver resolver, java.lang.CharSequence label, android.net.Uri uri) {
        android.content.ClipData.Item item = new android.content.ClipData.Item(uri);
        java.lang.String[] mimeTypes = android.content.ClipData.getMimeTypes(resolver, uri);
        return new android.content.ClipData(label, mimeTypes, item);
    }

    /**
     * Finds all applicable MIME types for a given URI.
     *
     * @param resolver
     * 		ContentResolver used to get information about the URI.
     * @param uri
     * 		The URI.
     * @return Returns an array of MIME types.
     */
    private static java.lang.String[] getMimeTypes(android.content.ContentResolver resolver, android.net.Uri uri) {
        java.lang.String[] mimeTypes = null;
        if (android.content.ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            java.lang.String realType = resolver.getType(uri);
            mimeTypes = resolver.getStreamTypes(uri, "*/*");
            if (realType != null) {
                if (mimeTypes == null) {
                    mimeTypes = new java.lang.String[]{ realType };
                } else
                    if (!com.android.internal.util.ArrayUtils.contains(mimeTypes, realType)) {
                        java.lang.String[] tmp = new java.lang.String[mimeTypes.length + 1];
                        tmp[0] = realType;
                        java.lang.System.arraycopy(mimeTypes, 0, tmp, 1, mimeTypes.length);
                        mimeTypes = tmp;
                    }

            }
        }
        if (mimeTypes == null) {
            mimeTypes = android.content.ClipData.MIMETYPES_TEXT_URILIST;
        }
        return mimeTypes;
    }

    /**
     * Create a new ClipData holding an URI with MIME type
     * {@link ClipDescription#MIMETYPE_TEXT_URILIST}.
     * Unlike {@link #newUri(ContentResolver, CharSequence, Uri)}, nothing
     * is inferred about the URI -- if it is a content: URI holding a bitmap,
     * the reported type will still be uri-list.  Use this with care!
     *
     * @param label
     * 		User-visible label for the clip data.
     * @param uri
     * 		The URI in the clip.
     * @return Returns a new ClipData containing the specified data.
     */
    public static android.content.ClipData newRawUri(java.lang.CharSequence label, android.net.Uri uri) {
        android.content.ClipData.Item item = new android.content.ClipData.Item(uri);
        return new android.content.ClipData(label, android.content.ClipData.MIMETYPES_TEXT_URILIST, item);
    }

    /**
     * Return the {@link ClipDescription} associated with this data, describing
     * what it contains.
     */
    public android.content.ClipDescription getDescription() {
        return mClipDescription;
    }

    /**
     * Add a new Item to the overall ClipData container.
     * <p> This method will <em>not</em> update the list of available MIME types in the
     * {@link ClipDescription}. It should be used only when adding items which do not add new
     * MIME types to this clip. If this is not the case, use {@link #addItem(ContentResolver, Item)}
     * or call {@link #ClipData(CharSequence, String[], Item)} with a complete list of MIME types.
     *
     * @param item
     * 		Item to be added.
     */
    public void addItem(android.content.ClipData.Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("item is null");
        }
        mItems.add(item);
    }

    /**
     *
     *
     * @unknown use #addItem(ContentResolver, Item) instead
     */
    @java.lang.Deprecated
    public void addItem(android.content.ClipData.Item item, android.content.ContentResolver resolver) {
        addItem(resolver, item);
    }

    /**
     * Add a new Item to the overall ClipData container.
     * <p> Unlike {@link #addItem(Item)}, this method will update the list of available MIME types
     * in the {@link ClipDescription}.
     *
     * @param resolver
     * 		ContentResolver used to get information about the URI possibly contained in
     * 		the item.
     * @param item
     * 		Item to be added.
     */
    public void addItem(android.content.ContentResolver resolver, android.content.ClipData.Item item) {
        addItem(item);
        if (item.getHtmlText() != null) {
            mClipDescription.addMimeTypes(android.content.ClipData.MIMETYPES_TEXT_HTML);
        } else
            if (item.getText() != null) {
                mClipDescription.addMimeTypes(android.content.ClipData.MIMETYPES_TEXT_PLAIN);
            }

        if (item.getIntent() != null) {
            mClipDescription.addMimeTypes(android.content.ClipData.MIMETYPES_TEXT_INTENT);
        }
        if (item.getUri() != null) {
            mClipDescription.addMimeTypes(android.content.ClipData.getMimeTypes(resolver, item.getUri()));
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.graphics.Bitmap getIcon() {
        return mIcon;
    }

    /**
     * Return the number of items in the clip data.
     */
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Return a single item inside of the clip data.  The index can range
     * from 0 to {@link #getItemCount()}-1.
     */
    public android.content.ClipData.Item getItemAt(int index) {
        return mItems.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setItemAt(int index, android.content.ClipData.Item item) {
        mItems.set(index, item);
    }

    /**
     * Prepare this {@link ClipData} to leave an app process.
     *
     * @unknown 
     */
    public void prepareToLeaveProcess(boolean leavingPackage) {
        // Assume that callers are going to be granting permissions
        prepareToLeaveProcess(leavingPackage, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    /**
     * Prepare this {@link ClipData} to leave an app process.
     *
     * @unknown 
     */
    public void prepareToLeaveProcess(boolean leavingPackage, int intentFlags) {
        final int size = mItems.size();
        for (int i = 0; i < size; i++) {
            final android.content.ClipData.Item item = mItems.get(i);
            if (item.mIntent != null) {
                item.mIntent.prepareToLeaveProcess(leavingPackage);
            }
            if ((item.mUri != null) && leavingPackage) {
                if (android.os.StrictMode.vmFileUriExposureEnabled()) {
                    item.mUri.checkFileUriExposed("ClipData.Item.getUri()");
                }
                if (android.os.StrictMode.vmContentUriWithoutPermissionEnabled()) {
                    item.mUri.checkContentUriWithoutPermission("ClipData.Item.getUri()", intentFlags);
                }
            }
        }
    }

    /**
     * {@hide }
     */
    public void prepareToEnterProcess() {
        final int size = mItems.size();
        for (int i = 0; i < size; i++) {
            final android.content.ClipData.Item item = mItems.get(i);
            if (item.mIntent != null) {
                item.mIntent.prepareToEnterProcess();
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void fixUris(int contentUserHint) {
        final int size = mItems.size();
        for (int i = 0; i < size; i++) {
            final android.content.ClipData.Item item = mItems.get(i);
            if (item.mIntent != null) {
                item.mIntent.fixUris(contentUserHint);
            }
            if (item.mUri != null) {
                item.mUri = android.content.ContentProvider.maybeAddUserId(item.mUri, contentUserHint);
            }
        }
    }

    /**
     * Only fixing the data field of the intents
     *
     * @unknown 
     */
    public void fixUrisLight(int contentUserHint) {
        final int size = mItems.size();
        for (int i = 0; i < size; i++) {
            final android.content.ClipData.Item item = mItems.get(i);
            if (item.mIntent != null) {
                android.net.Uri data = item.mIntent.getData();
                if (data != null) {
                    item.mIntent.setData(android.content.ContentProvider.maybeAddUserId(data, contentUserHint));
                }
            }
            if (item.mUri != null) {
                item.mUri = android.content.ContentProvider.maybeAddUserId(item.mUri, contentUserHint);
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder b = new java.lang.StringBuilder(128);
        b.append("ClipData { ");
        toShortString(b);
        b.append(" }");
        return b.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public void toShortString(java.lang.StringBuilder b) {
        boolean first;
        if (mClipDescription != null) {
            first = !mClipDescription.toShortString(b);
        } else {
            first = true;
        }
        if (mIcon != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("I:");
            b.append(mIcon.getWidth());
            b.append('x');
            b.append(mIcon.getHeight());
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append('{');
            mItems.get(i).toShortString(b);
            b.append('}');
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void toShortStringShortItems(java.lang.StringBuilder b, boolean first) {
        if (mItems.size() > 0) {
            if (!first) {
                b.append(' ');
            }
            mItems.get(0).toShortString(b);
            if (mItems.size() > 1) {
                b.append(" ...");
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        if (mClipDescription != null) {
            mClipDescription.writeToProto(proto, ClipDataProto.DESCRIPTION);
        }
        if (mIcon != null) {
            final long iToken = proto.start(ClipDataProto.ICON);
            proto.write(ClipDataProto.Icon.WIDTH, mIcon.getWidth());
            proto.write(ClipDataProto.Icon.HEIGHT, mIcon.getHeight());
            proto.end(iToken);
        }
        for (int i = 0; i < mItems.size(); i++) {
            mItems.get(i).writeToProto(proto, ClipDataProto.ITEMS);
        }
        proto.end(token);
    }

    /**
     *
     *
     * @unknown 
     */
    public void collectUris(java.util.List<android.net.Uri> out) {
        for (int i = 0; i < mItems.size(); ++i) {
            android.content.ClipData.Item item = getItemAt(i);
            if (item.getUri() != null) {
                out.add(item.getUri());
            }
            android.content.Intent intent = item.getIntent();
            if (intent != null) {
                if (intent.getData() != null) {
                    out.add(intent.getData());
                }
                if (intent.getClipData() != null) {
                    intent.getClipData().collectUris(out);
                }
            }
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        mClipDescription.writeToParcel(dest, flags);
        if (mIcon != null) {
            dest.writeInt(1);
            mIcon.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        final int N = mItems.size();
        dest.writeInt(N);
        for (int i = 0; i < N; i++) {
            android.content.ClipData.Item item = mItems.get(i);
            android.text.TextUtils.writeToParcel(item.mText, dest, flags);
            android.content.ClipData.writeHtmlTextToParcel(item.mHtmlText, dest, flags);
            if (item.mIntent != null) {
                dest.writeInt(1);
                item.mIntent.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            if (item.mUri != null) {
                dest.writeInt(1);
                item.mUri.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
        }
    }

    ClipData(android.os.Parcel in) {
        mClipDescription = new android.content.ClipDescription(in);
        if (in.readInt() != 0) {
            mIcon = this.CREATOR.createFromParcel(in);
        } else {
            mIcon = null;
        }
        mItems = new java.util.ArrayList<android.content.ClipData.Item>();
        final int N = in.readInt();
        for (int i = 0; i < N; i++) {
            java.lang.CharSequence text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            java.lang.String htmlText = android.content.ClipData.readHtmlTextFromParcel(in);
            android.content.Intent intent = (in.readInt() != 0) ? this.CREATOR.createFromParcel(in) : null;
            android.net.Uri uri = (in.readInt() != 0) ? Uri.CREATOR.createFromParcel(in) : null;
            mItems.add(new android.content.ClipData.Item(text, htmlText, intent, uri));
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.ClipData> CREATOR = new android.os.Parcelable.Creator<android.content.ClipData>() {
        @java.lang.Override
        public android.content.ClipData createFromParcel(android.os.Parcel source) {
            return new android.content.ClipData(source);
        }

        @java.lang.Override
        public android.content.ClipData[] newArray(int size) {
            return new android.content.ClipData[size];
        }
    };

    /**
     * Helper function for writing an HTML text into a parcel.
     * If the text size is larger than 400KB, it writes the text to a file descriptor to prevent the
     * parcel from exceeding 800KB binder size limit. {@link android.os.Binder#checkParcel()}
     * Otherwise, it directly writes the text into the parcel.
     * Note: This function is a workaround for existing applications that still use HTML for sharing
     * large clip data. We will ask application developers to use content: URI instead and remove
     * this function in API 30.
     */
    private static void writeHtmlTextToParcel(java.lang.String text, android.os.Parcel dest, int flags) {
        byte[] textData = (text != null) ? text.getBytes() : new byte[0];
        if ((textData.length > (android.content.ClipData.PARCEL_MAX_SIZE_BYTES / 2)) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
            try {
                android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.fromData(textData, null);
                dest.writeInt(android.content.ClipData.PARCEL_TYPE_PFD);
                dest.writeParcelable(pfd, flags);
            } catch (java.io.IOException e) {
                throw new java.lang.IllegalStateException("Error creating the shared memory area: " + e.toString());
            }
        } else {
            dest.writeInt(android.content.ClipData.PARCEL_TYPE_STRING);
            dest.writeString(text);
        }
    }

    /**
     * Reads a text written by writeHtmlTextToParcel.
     */
    private static java.lang.String readHtmlTextFromParcel(android.os.Parcel in) {
        if (in.readInt() == android.content.ClipData.PARCEL_TYPE_STRING) {
            return in.readString();
        }
        android.os.ParcelFileDescriptor pfd = in.readParcelable(android.os.ParcelFileDescriptor.class.getClassLoader());
        if (pfd == null) {
            throw new java.lang.IllegalStateException("Error reading ParcelFileDescriptor from Parcel");
        }
        java.io.FileInputStream fis = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
        java.io.InputStreamReader reader = new java.io.InputStreamReader(fis);
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        char[] buffer = new char[4096];
        int numRead;
        try {
            while ((numRead = reader.read(buffer)) != (-1)) {
                builder.append(buffer, 0, numRead);
            } 
            return builder.toString();
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Error reading data from ParcelFileDescriptor: " + e.toString());
        } finally {
            libcore.io.IoUtils.closeQuietly(fis);
        }
    }
}

