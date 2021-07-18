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
package android.util;


/**
 * XML utility methods.
 */
public class Xml {
    /**
     *
     *
     * @unknown 
     */
    public Xml() {
    }

    /**
     * {@link org.xmlpull.v1.XmlPullParser} "relaxed" feature name.
     *
     * @see <a href="http://xmlpull.org/v1/doc/features.html#relaxed">
    specification</a>
     */
    public static java.lang.String FEATURE_RELAXED = "http://xmlpull.org/v1/doc/features.html#relaxed";

    /**
     * Parses the given xml string and fires events on the given SAX handler.
     */
    public static void parse(java.lang.String xml, org.xml.sax.ContentHandler contentHandler) throws org.xml.sax.SAXException {
        try {
            org.xml.sax.XMLReader reader = new org.apache.harmony.xml.ExpatReader();
            reader.setContentHandler(contentHandler);
            reader.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
        } catch (java.io.IOException e) {
            throw new java.lang.AssertionError(e);
        }
    }

    /**
     * Parses xml from the given reader and fires events on the given SAX
     * handler.
     */
    public static void parse(java.io.Reader in, org.xml.sax.ContentHandler contentHandler) throws java.io.IOException, org.xml.sax.SAXException {
        org.xml.sax.XMLReader reader = new org.apache.harmony.xml.ExpatReader();
        reader.setContentHandler(contentHandler);
        reader.parse(new org.xml.sax.InputSource(in));
    }

    /**
     * Parses xml from the given input stream and fires events on the given SAX
     * handler.
     */
    public static void parse(java.io.InputStream in, android.util.Xml.Encoding encoding, org.xml.sax.ContentHandler contentHandler) throws java.io.IOException, org.xml.sax.SAXException {
        org.xml.sax.XMLReader reader = new org.apache.harmony.xml.ExpatReader();
        reader.setContentHandler(contentHandler);
        org.xml.sax.InputSource source = new org.xml.sax.InputSource(in);
        source.setEncoding(encoding.expatName);
        reader.parse(source);
    }

    /**
     * Returns a new pull parser with namespace support.
     */
    public static org.xmlpull.v1.XmlPullParser newPullParser() {
        try {
            org.kxml2.io.KXmlParser parser = new org.kxml2.io.KXmlParser();
            parser.setFeature(org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_DOCDECL, true);
            parser.setFeature(org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            return parser;
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new java.lang.AssertionError();
        }
    }

    /**
     * Creates a new xml serializer.
     */
    public static org.xmlpull.v1.XmlSerializer newSerializer() {
        try {
            return android.util.Xml.XmlSerializerFactory.instance.newSerializer();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new java.lang.AssertionError(e);
        }
    }

    /**
     * Factory for xml serializers. Initialized on demand.
     */
    static class XmlSerializerFactory {
        static final java.lang.String TYPE = "org.kxml2.io.KXmlParser,org.kxml2.io.KXmlSerializer";

        static final org.xmlpull.v1.XmlPullParserFactory instance;

        static {
            try {
                instance = org.xmlpull.v1.XmlPullParserFactory.newInstance(android.util.Xml.XmlSerializerFactory.TYPE, null);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                throw new java.lang.AssertionError(e);
            }
        }
    }

    /**
     * Supported character encodings.
     */
    public enum Encoding {

        US_ASCII("US-ASCII"),
        UTF_8("UTF-8"),
        UTF_16("UTF-16"),
        ISO_8859_1("ISO-8859-1");
        final java.lang.String expatName;

        Encoding(java.lang.String expatName) {
            this.expatName = expatName;
        }
    }

    /**
     * Finds an encoding by name. Returns UTF-8 if you pass {@code null}.
     */
    public static android.util.Xml.Encoding findEncodingByName(java.lang.String encodingName) throws java.io.UnsupportedEncodingException {
        if (encodingName == null) {
            return android.util.Xml.Encoding.UTF_8;
        }
        for (android.util.Xml.Encoding encoding : android.util.Xml.Encoding.values()) {
            if (encoding.expatName.equalsIgnoreCase(encodingName))
                return encoding;

        }
        throw new java.io.UnsupportedEncodingException(encodingName);
    }

    /**
     * Return an AttributeSet interface for use with the given XmlPullParser.
     * If the given parser itself implements AttributeSet, that implementation
     * is simply returned.  Otherwise a wrapper class is
     * instantiated on top of the XmlPullParser, as a proxy for retrieving its
     * attributes, and returned to you.
     *
     * @param parser
     * 		The existing parser for which you would like an
     * 		AttributeSet.
     * @return An AttributeSet you can use to retrieve the
    attribute values at each of the tags as the parser moves
    through its XML document.
     * @see AttributeSet
     */
    public static android.util.AttributeSet asAttributeSet(org.xmlpull.v1.XmlPullParser parser) {
        return parser instanceof android.util.AttributeSet ? ((android.util.AttributeSet) (parser)) : new android.util.XmlPullAttributes(parser);
    }
}

