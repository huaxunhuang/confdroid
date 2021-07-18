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
 * Wrapper around a compiled XML file.
 *
 * {@hide }
 */
final class XmlBlock implements java.lang.AutoCloseable {
    private static final boolean DEBUG = false;

    @android.annotation.UnsupportedAppUsage
    public XmlBlock(byte[] data) {
        mAssets = null;
        mNative = android.content.res.XmlBlock.nativeCreate(data, 0, data.length);
        mStrings = new android.content.res.StringBlock(android.content.res.XmlBlock.nativeGetStringBlock(mNative), false);
    }

    public XmlBlock(byte[] data, int offset, int size) {
        mAssets = null;
        mNative = android.content.res.XmlBlock.nativeCreate(data, offset, size);
        mStrings = new android.content.res.StringBlock(android.content.res.XmlBlock.nativeGetStringBlock(mNative), false);
    }

    @java.lang.Override
    public void close() {
        synchronized(this) {
            if (mOpen) {
                mOpen = false;
                decOpenCountLocked();
            }
        }
    }

    private void decOpenCountLocked() {
        mOpenCount--;
        if (mOpenCount == 0) {
            android.content.res.XmlBlock.nativeDestroy(mNative);
            if (mAssets != null) {
                mAssets.xmlBlockGone(hashCode());
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    public android.content.res.XmlResourceParser newParser() {
        return newParser(android.content.res.Resources.ID_NULL);
    }

    public android.content.res.XmlResourceParser newParser(@android.annotation.AnyRes
    int resId) {
        synchronized(this) {
            if (mNative != 0) {
                return new android.content.res.XmlBlock.Parser(android.content.res.XmlBlock.nativeCreateParseState(mNative, resId), this);
            }
            return null;
        }
    }

    /* package */
    final class Parser implements android.content.res.XmlResourceParser {
        Parser(long parseState, android.content.res.XmlBlock block) {
            mParseState = parseState;
            mBlock = block;
            block.mOpenCount++;
        }

        @android.annotation.AnyRes
        public int getSourceResId() {
            return android.content.res.XmlBlock.nativeGetSourceResId(mParseState);
        }

        public void setFeature(java.lang.String name, boolean state) throws org.xmlpull.v1.XmlPullParserException {
            if (org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(name) && state) {
                return;
            }
            if (org.xmlpull.v1.XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES.equals(name) && state) {
                return;
            }
            throw new org.xmlpull.v1.XmlPullParserException("Unsupported feature: " + name);
        }

        public boolean getFeature(java.lang.String name) {
            if (org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(name)) {
                return true;
            }
            if (org.xmlpull.v1.XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES.equals(name)) {
                return true;
            }
            return false;
        }

        public void setProperty(java.lang.String name, java.lang.Object value) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("setProperty() not supported");
        }

        public java.lang.Object getProperty(java.lang.String name) {
            return null;
        }

        public void setInput(java.io.Reader in) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("setInput() not supported");
        }

        public void setInput(java.io.InputStream inputStream, java.lang.String inputEncoding) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("setInput() not supported");
        }

        public void defineEntityReplacementText(java.lang.String entityName, java.lang.String replacementText) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("defineEntityReplacementText() not supported");
        }

        public java.lang.String getNamespacePrefix(int pos) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("getNamespacePrefix() not supported");
        }

        public java.lang.String getInputEncoding() {
            return null;
        }

        public java.lang.String getNamespace(java.lang.String prefix) {
            throw new java.lang.RuntimeException("getNamespace() not supported");
        }

        public int getNamespaceCount(int depth) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("getNamespaceCount() not supported");
        }

        public java.lang.String getPositionDescription() {
            return "Binary XML file line #" + getLineNumber();
        }

        public java.lang.String getNamespaceUri(int pos) throws org.xmlpull.v1.XmlPullParserException {
            throw new org.xmlpull.v1.XmlPullParserException("getNamespaceUri() not supported");
        }

        public int getColumnNumber() {
            return -1;
        }

        public int getDepth() {
            return mDepth;
        }

        public java.lang.String getText() {
            int id = android.content.res.XmlBlock.nativeGetText(mParseState);
            return id >= 0 ? mStrings.get(id).toString() : null;
        }

        public int getLineNumber() {
            return android.content.res.XmlBlock.nativeGetLineNumber(mParseState);
        }

        public int getEventType() throws org.xmlpull.v1.XmlPullParserException {
            return mEventType;
        }

        public boolean isWhitespace() throws org.xmlpull.v1.XmlPullParserException {
            // whitespace was stripped by aapt.
            return false;
        }

        public java.lang.String getPrefix() {
            throw new java.lang.RuntimeException("getPrefix not supported");
        }

        public char[] getTextCharacters(int[] holderForStartAndLength) {
            java.lang.String txt = getText();
            char[] chars = null;
            if (txt != null) {
                holderForStartAndLength[0] = 0;
                holderForStartAndLength[1] = txt.length();
                chars = new char[txt.length()];
                txt.getChars(0, txt.length(), chars, 0);
            }
            return chars;
        }

        public java.lang.String getNamespace() {
            int id = android.content.res.XmlBlock.nativeGetNamespace(mParseState);
            return id >= 0 ? mStrings.get(id).toString() : "";
        }

        public java.lang.String getName() {
            int id = android.content.res.XmlBlock.nativeGetName(mParseState);
            return id >= 0 ? mStrings.get(id).toString() : null;
        }

        public java.lang.String getAttributeNamespace(int index) {
            int id = android.content.res.XmlBlock.nativeGetAttributeNamespace(mParseState, index);
            if (android.content.res.XmlBlock.DEBUG)
                java.lang.System.out.println((("getAttributeNamespace of " + index) + " = ") + id);

            if (id >= 0)
                return mStrings.get(id).toString();
            else
                if (id == (-1))
                    return "";


            throw new java.lang.IndexOutOfBoundsException(java.lang.String.valueOf(index));
        }

        public java.lang.String getAttributeName(int index) {
            int id = android.content.res.XmlBlock.nativeGetAttributeName(mParseState, index);
            if (android.content.res.XmlBlock.DEBUG)
                java.lang.System.out.println((("getAttributeName of " + index) + " = ") + id);

            if (id >= 0)
                return mStrings.get(id).toString();

            throw new java.lang.IndexOutOfBoundsException(java.lang.String.valueOf(index));
        }

        public java.lang.String getAttributePrefix(int index) {
            throw new java.lang.RuntimeException("getAttributePrefix not supported");
        }

        public boolean isEmptyElementTag() throws org.xmlpull.v1.XmlPullParserException {
            // XXX Need to detect this.
            return false;
        }

        public int getAttributeCount() {
            return mEventType == org.xmlpull.v1.XmlPullParser.START_TAG ? android.content.res.XmlBlock.nativeGetAttributeCount(mParseState) : -1;
        }

        public java.lang.String getAttributeValue(int index) {
            int id = android.content.res.XmlBlock.nativeGetAttributeStringValue(mParseState, index);
            if (android.content.res.XmlBlock.DEBUG)
                java.lang.System.out.println((("getAttributeValue of " + index) + " = ") + id);

            if (id >= 0)
                return mStrings.get(id).toString();

            // May be some other type...  check and try to convert if so.
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, index);
            if (t == android.util.TypedValue.TYPE_NULL) {
                throw new java.lang.IndexOutOfBoundsException(java.lang.String.valueOf(index));
            }
            int v = android.content.res.XmlBlock.nativeGetAttributeData(mParseState, index);
            return android.util.TypedValue.coerceToString(t, v);
        }

        public java.lang.String getAttributeType(int index) {
            return "CDATA";
        }

        public boolean isAttributeDefault(int index) {
            return false;
        }

        public int nextToken() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            return next();
        }

        public java.lang.String getAttributeValue(java.lang.String namespace, java.lang.String name) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, name);
            if (idx >= 0) {
                if (android.content.res.XmlBlock.DEBUG)
                    java.lang.System.out.println((((("getAttributeName of " + namespace) + ":") + name) + " index = ") + idx);

                if (android.content.res.XmlBlock.DEBUG)
                    java.lang.System.out.println((((("Namespace=" + getAttributeNamespace(idx)) + "Name=") + getAttributeName(idx)) + ", Value=") + getAttributeValue(idx));

                return getAttributeValue(idx);
            }
            return null;
        }

        public int next() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (!mStarted) {
                mStarted = true;
                return org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
            }
            if (mParseState == 0) {
                return org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
            }
            int ev = android.content.res.XmlBlock.nativeNext(mParseState);
            if (mDecNextDepth) {
                mDepth--;
                mDecNextDepth = false;
            }
            switch (ev) {
                case org.xmlpull.v1.XmlPullParser.START_TAG :
                    mDepth++;
                    break;
                case org.xmlpull.v1.XmlPullParser.END_TAG :
                    mDecNextDepth = true;
                    break;
            }
            mEventType = ev;
            if (ev == org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                // Automatically close the parse when we reach the end of
                // a document, since the standard XmlPullParser interface
                // doesn't have such an API so most clients will leave us
                // dangling.
                close();
            }
            return ev;
        }

        public void require(int type, java.lang.String namespace, java.lang.String name) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (((type != getEventType()) || ((namespace != null) && (!namespace.equals(getNamespace())))) || ((name != null) && (!name.equals(getName()))))
                throw new org.xmlpull.v1.XmlPullParserException(("expected " + org.xmlpull.v1.XmlPullParser.TYPES[type]) + getPositionDescription());

        }

        public java.lang.String nextText() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (getEventType() != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException(getPositionDescription() + ": parser must be on START_TAG to read next text", this, null);
            }
            int eventType = next();
            if (eventType == org.xmlpull.v1.XmlPullParser.TEXT) {
                java.lang.String result = getText();
                eventType = next();
                if (eventType != org.xmlpull.v1.XmlPullParser.END_TAG) {
                    throw new org.xmlpull.v1.XmlPullParserException(getPositionDescription() + ": event TEXT it must be immediately followed by END_TAG", this, null);
                }
                return result;
            } else
                if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                    return "";
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException(getPositionDescription() + ": parser must be on START_TAG or TEXT to read text", this, null);
                }

        }

        public int nextTag() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            int eventType = next();
            if ((eventType == org.xmlpull.v1.XmlPullParser.TEXT) && isWhitespace()) {
                // skip whitespace
                eventType = next();
            }
            if ((eventType != org.xmlpull.v1.XmlPullParser.START_TAG) && (eventType != org.xmlpull.v1.XmlPullParser.END_TAG)) {
                throw new org.xmlpull.v1.XmlPullParserException(getPositionDescription() + ": expected start or end tag", this, null);
            }
            return eventType;
        }

        public int getAttributeNameResource(int index) {
            return android.content.res.XmlBlock.nativeGetAttributeResource(mParseState, index);
        }

        public int getAttributeListValue(java.lang.String namespace, java.lang.String attribute, java.lang.String[] options, int defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeListValue(idx, options, defaultValue);
            }
            return defaultValue;
        }

        public boolean getAttributeBooleanValue(java.lang.String namespace, java.lang.String attribute, boolean defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeBooleanValue(idx, defaultValue);
            }
            return defaultValue;
        }

        public int getAttributeResourceValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeResourceValue(idx, defaultValue);
            }
            return defaultValue;
        }

        public int getAttributeIntValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeIntValue(idx, defaultValue);
            }
            return defaultValue;
        }

        public int getAttributeUnsignedIntValue(java.lang.String namespace, java.lang.String attribute, int defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeUnsignedIntValue(idx, defaultValue);
            }
            return defaultValue;
        }

        public float getAttributeFloatValue(java.lang.String namespace, java.lang.String attribute, float defaultValue) {
            int idx = android.content.res.XmlBlock.nativeGetAttributeIndex(mParseState, namespace, attribute);
            if (idx >= 0) {
                return getAttributeFloatValue(idx, defaultValue);
            }
            return defaultValue;
        }

        public int getAttributeListValue(int idx, java.lang.String[] options, int defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            int v = android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx);
            if (t == android.util.TypedValue.TYPE_STRING) {
                return com.android.internal.util.XmlUtils.convertValueToList(mStrings.get(v), options, defaultValue);
            }
            return v;
        }

        public boolean getAttributeBooleanValue(int idx, boolean defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            // Note: don't attempt to convert any other types, because
            // we want to count on aapt doing the conversion for us.
            if ((t >= android.util.TypedValue.TYPE_FIRST_INT) && (t <= android.util.TypedValue.TYPE_LAST_INT)) {
                return android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx) != 0;
            }
            return defaultValue;
        }

        public int getAttributeResourceValue(int idx, int defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            // Note: don't attempt to convert any other types, because
            // we want to count on aapt doing the conversion for us.
            if (t == android.util.TypedValue.TYPE_REFERENCE) {
                return android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx);
            }
            return defaultValue;
        }

        public int getAttributeIntValue(int idx, int defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            // Note: don't attempt to convert any other types, because
            // we want to count on aapt doing the conversion for us.
            if ((t >= android.util.TypedValue.TYPE_FIRST_INT) && (t <= android.util.TypedValue.TYPE_LAST_INT)) {
                return android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx);
            }
            return defaultValue;
        }

        public int getAttributeUnsignedIntValue(int idx, int defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            // Note: don't attempt to convert any other types, because
            // we want to count on aapt doing the conversion for us.
            if ((t >= android.util.TypedValue.TYPE_FIRST_INT) && (t <= android.util.TypedValue.TYPE_LAST_INT)) {
                return android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx);
            }
            return defaultValue;
        }

        public float getAttributeFloatValue(int idx, float defaultValue) {
            int t = android.content.res.XmlBlock.nativeGetAttributeDataType(mParseState, idx);
            // Note: don't attempt to convert any other types, because
            // we want to count on aapt doing the conversion for us.
            if (t == android.util.TypedValue.TYPE_FLOAT) {
                return java.lang.Float.intBitsToFloat(android.content.res.XmlBlock.nativeGetAttributeData(mParseState, idx));
            }
            throw new java.lang.RuntimeException("not a float!");
        }

        public java.lang.String getIdAttribute() {
            int id = android.content.res.XmlBlock.nativeGetIdAttribute(mParseState);
            return id >= 0 ? mStrings.get(id).toString() : null;
        }

        public java.lang.String getClassAttribute() {
            int id = android.content.res.XmlBlock.nativeGetClassAttribute(mParseState);
            return id >= 0 ? mStrings.get(id).toString() : null;
        }

        public int getIdAttributeResourceValue(int defaultValue) {
            // todo: create and use native method
            return getAttributeResourceValue(null, "id", defaultValue);
        }

        public int getStyleAttribute() {
            return android.content.res.XmlBlock.nativeGetStyleAttribute(mParseState);
        }

        public void close() {
            synchronized(mBlock) {
                if (mParseState != 0) {
                    android.content.res.XmlBlock.nativeDestroyParseState(mParseState);
                    mParseState = 0;
                    mBlock.decOpenCountLocked();
                }
            }
        }

        protected void finalize() throws java.lang.Throwable {
            close();
        }

        /* package */
        final java.lang.CharSequence getPooledString(int id) {
            return mStrings.get(id);
        }

        /* package */
        @android.annotation.UnsupportedAppUsage
        long mParseState;

        @android.annotation.UnsupportedAppUsage
        private final android.content.res.XmlBlock mBlock;

        private boolean mStarted = false;

        private boolean mDecNextDepth = false;

        private int mDepth = 0;

        private int mEventType = org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
    }

    protected void finalize() throws java.lang.Throwable {
        close();
    }

    /**
     * Create from an existing xml block native object.  This is
     * -extremely- dangerous -- only use it if you absolutely know what you
     *  are doing!  The given native object must exist for the entire lifetime
     *  of this newly creating XmlBlock.
     */
    XmlBlock(@android.annotation.Nullable
    android.content.res.AssetManager assets, long xmlBlock) {
        mAssets = assets;
        mNative = xmlBlock;
        mStrings = new android.content.res.StringBlock(android.content.res.XmlBlock.nativeGetStringBlock(xmlBlock), false);
    }

    @android.annotation.Nullable
    private final android.content.res.AssetManager mAssets;

    private final long mNative;

    /* package */
    final android.content.res.StringBlock mStrings;

    private boolean mOpen = true;

    private int mOpenCount = 1;

    private static final native long nativeCreate(byte[] data, int offset, int size);

    private static final native long nativeGetStringBlock(long obj);

    private static final native long nativeCreateParseState(long obj, int resId);

    private static final native void nativeDestroyParseState(long state);

    private static final native void nativeDestroy(long obj);

    // ----------- @FastNative ------------------
    /* package */
    @dalvik.annotation.optimization.FastNative
    static final native int nativeNext(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetNamespace(long state);

    /* package */
    @dalvik.annotation.optimization.FastNative
    static final native int nativeGetName(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetText(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetLineNumber(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeCount(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeNamespace(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeName(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeResource(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeDataType(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeData(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeStringValue(long state, int idx);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetIdAttribute(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetClassAttribute(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetStyleAttribute(long state);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetAttributeIndex(long state, java.lang.String namespace, java.lang.String name);

    @dalvik.annotation.optimization.FastNative
    private static final native int nativeGetSourceResId(long state);
}

