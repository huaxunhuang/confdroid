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
package android.sax;


/**
 * The root XML element. The entry point for this API. Not safe for concurrent
 * use.
 *
 * <p>For example, passing this XML:
 *
 * <pre>
 * &lt;feed xmlns='http://www.w3.org/2005/Atom'>
 *   &lt;entry>
 *     &lt;id>bob&lt;/id>
 *   &lt;/entry>
 * &lt;/feed>
 * </pre>
 *
 * to this code:
 *
 * <pre>
 * static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
 *
 * ...
 *
 * RootElement root = new RootElement(ATOM_NAMESPACE, "feed");
 * Element entry = root.getChild(ATOM_NAMESPACE, "entry");
 * entry.getChild(ATOM_NAMESPACE, "id").setEndTextElementListener(
 *   new EndTextElementListener() {
 *     public void end(String body) {
 *       System.out.println("Entry ID: " + body);
 *     }
 *   });
 *
 * XMLReader reader = ...;
 * reader.setContentHandler(root.getContentHandler());
 * reader.parse(...);
 * </pre>
 *
 * would output:
 *
 * <pre>
 * Entry ID: bob
 * </pre>
 */
public class RootElement extends android.sax.Element {
    final android.sax.RootElement.Handler handler = new android.sax.RootElement.Handler();

    /**
     * Constructs a new root element with the given name.
     *
     * @param uri
     * 		the namespace
     * @param localName
     * 		the local name
     */
    public RootElement(java.lang.String uri, java.lang.String localName) {
        super(null, uri, localName, 0);
    }

    /**
     * Constructs a new root element with the given name. Uses an empty string
     * as the namespace.
     *
     * @param localName
     * 		the local name
     */
    public RootElement(java.lang.String localName) {
        this("", localName);
    }

    /**
     * Gets the SAX {@code ContentHandler}. Pass this to your SAX parser.
     */
    public org.xml.sax.ContentHandler getContentHandler() {
        return this.handler;
    }

    class Handler extends org.xml.sax.helpers.DefaultHandler {
        org.xml.sax.Locator locator;

        int depth = -1;

        android.sax.Element current = null;

        java.lang.StringBuilder bodyBuilder = null;

        @java.lang.Override
        public void setDocumentLocator(org.xml.sax.Locator locator) {
            this.locator = locator;
        }

        @java.lang.Override
        public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
            int depth = ++this.depth;
            if (depth == 0) {
                // This is the root element.
                startRoot(uri, localName, attributes);
                return;
            }
            // Prohibit mixed text and elements.
            if (bodyBuilder != null) {
                throw new android.sax.BadXmlException((("Encountered mixed content" + " within text element named ") + current) + ".", locator);
            }
            // If we're one level below the current element.
            if (depth == (current.depth + 1)) {
                // Look for a child to push onto the stack.
                android.sax.Children children = current.children;
                if (children != null) {
                    android.sax.Element child = children.get(uri, localName);
                    if (child != null) {
                        start(child, attributes);
                    }
                }
            }
        }

        void startRoot(java.lang.String uri, java.lang.String localName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
            android.sax.Element root = android.sax.RootElement.this;
            if ((root.uri.compareTo(uri) != 0) || (root.localName.compareTo(localName) != 0)) {
                throw new android.sax.BadXmlException(((("Root element name does" + " not match. Expected: ") + root) + ", Got: ") + android.sax.Element.toString(uri, localName), locator);
            }
            start(root, attributes);
        }

        void start(android.sax.Element e, org.xml.sax.Attributes attributes) {
            // Push element onto the stack.
            this.current = e;
            if (e.startElementListener != null) {
                e.startElementListener.start(attributes);
            }
            if (e.endTextElementListener != null) {
                this.bodyBuilder = new java.lang.StringBuilder();
            }
            e.resetRequiredChildren();
            e.visited = true;
        }

        @java.lang.Override
        public void characters(char[] buffer, int start, int length) throws org.xml.sax.SAXException {
            if (bodyBuilder != null) {
                bodyBuilder.append(buffer, start, length);
            }
        }

        @java.lang.Override
        public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws org.xml.sax.SAXException {
            android.sax.Element current = this.current;
            // If we've ended the current element...
            if (depth == current.depth) {
                current.checkRequiredChildren(locator);
                // Invoke end element listener.
                if (current.endElementListener != null) {
                    current.endElementListener.end();
                }
                // Invoke end text element listener.
                if (bodyBuilder != null) {
                    java.lang.String body = bodyBuilder.toString();
                    bodyBuilder = null;
                    // We can assume that this listener is present.
                    current.endTextElementListener.end(body);
                }
                // Pop element off the stack.
                this.current = current.parent;
            }
            depth--;
        }
    }
}

