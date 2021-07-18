/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Inserts default data from InputStream, should be in XML format.
 * If the provider syncs data to the server, the imported data will be synced to the server.
 * <p>Samples:</p>
 * <br/>
 *  Insert one row:
 * <pre>
 * &lt;row uri="content://contacts/people">
 *  &lt;Col column = "name" value = "foo feebe "/>
 *  &lt;Col column = "addr" value = "Tx"/>
 * &lt;/row></pre>
 * <br/>
 * Delete, it must be in order of uri, select, and arg:
 * <pre>
 * &lt;del uri="content://contacts/people" select="name=? and addr=?"
 *  arg1 = "foo feebe" arg2 ="Tx"/></pre>
 * <br/>
 *  Use first row's uri to insert into another table,
 *  content://contacts/people/1/phones:
 * <pre>
 * &lt;row uri="content://contacts/people">
 *  &lt;col column = "name" value = "foo feebe"/>
 *  &lt;col column = "addr" value = "Tx"/>
 *  &lt;row postfix="phones">
 *    &lt;col column="number" value="512-514-6535"/>
 *  &lt;/row>
 *  &lt;row postfix="phones">
 *    &lt;col column="cell" value="512-514-6535"/>
 *  &lt;/row>
 * &lt;/row></pre>
 * <br/>
 *  Insert multiple rows in to same table and same attributes:
 * <pre>
 * &lt;row uri="content://contacts/people" >
 *  &lt;row>
 *   &lt;col column= "name" value = "foo feebe"/>
 *   &lt;col column= "addr" value = "Tx"/>
 *  &lt;/row>
 *  &lt;row>
 *  &lt;/row>
 * &lt;/row></pre>
 *
 * @unknown 
 */
public class DefaultDataHandler implements android.content.ContentInsertHandler {
    private static final java.lang.String ROW = "row";

    private static final java.lang.String COL = "col";

    private static final java.lang.String URI_STR = "uri";

    private static final java.lang.String POSTFIX = "postfix";

    private static final java.lang.String DEL = "del";

    private static final java.lang.String SELECT = "select";

    private static final java.lang.String ARG = "arg";

    private java.util.Stack<android.net.Uri> mUris = new java.util.Stack<android.net.Uri>();

    private android.content.ContentValues mValues;

    private android.content.ContentResolver mContentResolver;

    public void insert(android.content.ContentResolver contentResolver, java.io.InputStream in) throws java.io.IOException, org.xml.sax.SAXException {
        mContentResolver = contentResolver;
        android.util.Xml.parse(in, Xml.Encoding.UTF_8, this);
    }

    public void insert(android.content.ContentResolver contentResolver, java.lang.String in) throws org.xml.sax.SAXException {
        mContentResolver = contentResolver;
        android.util.Xml.parse(in, this);
    }

    private void parseRow(org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
        java.lang.String uriStr = atts.getValue(android.content.DefaultDataHandler.URI_STR);
        android.net.Uri uri;
        if (uriStr != null) {
            // case 1
            uri = android.net.Uri.parse(uriStr);
            if (uri == null) {
                throw new org.xml.sax.SAXException(("attribute " + atts.getValue(android.content.DefaultDataHandler.URI_STR)) + " parsing failure");
            }
        } else
            if (mUris.size() > 0) {
                // case 2
                java.lang.String postfix = atts.getValue(android.content.DefaultDataHandler.POSTFIX);
                if (postfix != null) {
                    uri = android.net.Uri.withAppendedPath(mUris.lastElement(), postfix);
                } else {
                    uri = mUris.lastElement();
                }
            } else {
                throw new org.xml.sax.SAXException("attribute parsing failure");
            }

        mUris.push(uri);
    }

    private android.net.Uri insertRow() {
        android.net.Uri u = mContentResolver.insert(mUris.lastElement(), mValues);
        mValues = null;
        return u;
    }

    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String name, org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
        if (android.content.DefaultDataHandler.ROW.equals(localName)) {
            if (mValues != null) {
                // case 2, <Col> before <Row> insert last uri
                if (mUris.empty()) {
                    throw new org.xml.sax.SAXException("uri is empty");
                }
                android.net.Uri nextUri = insertRow();
                if (nextUri == null) {
                    throw new org.xml.sax.SAXException(("insert to uri " + mUris.lastElement().toString()) + " failure");
                } else {
                    // make sure the stack lastElement save uri for more than one row
                    mUris.pop();
                    mUris.push(nextUri);
                    parseRow(atts);
                }
            } else {
                int attrLen = atts.getLength();
                if (attrLen == 0) {
                    // case 3, share same uri as last level
                    mUris.push(mUris.lastElement());
                } else {
                    parseRow(atts);
                }
            }
        } else
            if (android.content.DefaultDataHandler.COL.equals(localName)) {
                int attrLen = atts.getLength();
                if (attrLen != 2) {
                    throw new org.xml.sax.SAXException("illegal attributes number " + attrLen);
                }
                java.lang.String key = atts.getValue(0);
                java.lang.String value = atts.getValue(1);
                if ((((key != null) && (key.length() > 0)) && (value != null)) && (value.length() > 0)) {
                    if (mValues == null) {
                        mValues = new android.content.ContentValues();
                    }
                    mValues.put(key, value);
                } else {
                    throw new org.xml.sax.SAXException("illegal attributes value");
                }
            } else
                if (android.content.DefaultDataHandler.DEL.equals(localName)) {
                    android.net.Uri u = android.net.Uri.parse(atts.getValue(android.content.DefaultDataHandler.URI_STR));
                    if (u == null) {
                        throw new org.xml.sax.SAXException(("attribute " + atts.getValue(android.content.DefaultDataHandler.URI_STR)) + " parsing failure");
                    }
                    int attrLen = atts.getLength() - 2;
                    if (attrLen > 0) {
                        java.lang.String[] selectionArgs = new java.lang.String[attrLen];
                        for (int i = 0; i < attrLen; i++) {
                            selectionArgs[i] = atts.getValue(i + 2);
                        }
                        mContentResolver.delete(u, atts.getValue(1), selectionArgs);
                    } else
                        if (attrLen == 0) {
                            mContentResolver.delete(u, atts.getValue(1), null);
                        } else {
                            mContentResolver.delete(u, null, null);
                        }

                } else {
                    throw new org.xml.sax.SAXException("unknown element: " + localName);
                }


    }

    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String name) throws org.xml.sax.SAXException {
        if (android.content.DefaultDataHandler.ROW.equals(localName)) {
            if (mUris.empty()) {
                throw new org.xml.sax.SAXException("uri mismatch");
            }
            if (mValues != null) {
                insertRow();
            }
            mUris.pop();
        }
    }

    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void endDocument() throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void endPrefixMapping(java.lang.String prefix) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void processingInstruction(java.lang.String target, java.lang.String data) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void setDocumentLocator(org.xml.sax.Locator locator) {
        // TODO Auto-generated method stub
    }

    public void skippedEntity(java.lang.String name) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void startDocument() throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }

    public void startPrefixMapping(java.lang.String prefix, java.lang.String uri) throws org.xml.sax.SAXException {
        // TODO Auto-generated method stub
    }
}

