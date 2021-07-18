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
 * An XML element. Provides access to child elements and hooks to listen
 * for events related to this element.
 *
 * @see RootElement
 */
public class Element {
    final java.lang.String uri;

    final java.lang.String localName;

    final int depth;

    final android.sax.Element parent;

    android.sax.Children children;

    java.util.ArrayList<android.sax.Element> requiredChilden;

    boolean visited;

    android.sax.StartElementListener startElementListener;

    android.sax.EndElementListener endElementListener;

    android.sax.EndTextElementListener endTextElementListener;

    Element(android.sax.Element parent, java.lang.String uri, java.lang.String localName, int depth) {
        this.parent = parent;
        this.uri = uri;
        this.localName = localName;
        this.depth = depth;
    }

    /**
     * Gets the child element with the given name. Uses an empty string as the
     * namespace.
     */
    public android.sax.Element getChild(java.lang.String localName) {
        return getChild("", localName);
    }

    /**
     * Gets the child element with the given name.
     */
    public android.sax.Element getChild(java.lang.String uri, java.lang.String localName) {
        if (endTextElementListener != null) {
            throw new java.lang.IllegalStateException("This element already has an end" + " text element listener. It cannot have children.");
        }
        if (children == null) {
            children = new android.sax.Children();
        }
        return children.getOrCreate(this, uri, localName);
    }

    /**
     * Gets the child element with the given name. Uses an empty string as the
     * namespace. We will throw a {@link org.xml.sax.SAXException} at parsing
     * time if the specified child is missing. This helps you ensure that your
     * listeners are called.
     */
    public android.sax.Element requireChild(java.lang.String localName) {
        return requireChild("", localName);
    }

    /**
     * Gets the child element with the given name. We will throw a
     * {@link org.xml.sax.SAXException} at parsing time if the specified child
     * is missing. This helps you ensure that your listeners are called.
     */
    public android.sax.Element requireChild(java.lang.String uri, java.lang.String localName) {
        android.sax.Element child = getChild(uri, localName);
        if (requiredChilden == null) {
            requiredChilden = new java.util.ArrayList<android.sax.Element>();
            requiredChilden.add(child);
        } else {
            if (!requiredChilden.contains(child)) {
                requiredChilden.add(child);
            }
        }
        return child;
    }

    /**
     * Sets start and end element listeners at the same time.
     */
    public void setElementListener(android.sax.ElementListener elementListener) {
        setStartElementListener(elementListener);
        setEndElementListener(elementListener);
    }

    /**
     * Sets start and end text element listeners at the same time.
     */
    public void setTextElementListener(android.sax.TextElementListener elementListener) {
        setStartElementListener(elementListener);
        setEndTextElementListener(elementListener);
    }

    /**
     * Sets a listener for the start of this element.
     */
    public void setStartElementListener(android.sax.StartElementListener startElementListener) {
        if (this.startElementListener != null) {
            throw new java.lang.IllegalStateException("Start element listener has already been set.");
        }
        this.startElementListener = startElementListener;
    }

    /**
     * Sets a listener for the end of this element.
     */
    public void setEndElementListener(android.sax.EndElementListener endElementListener) {
        if (this.endElementListener != null) {
            throw new java.lang.IllegalStateException("End element listener has already been set.");
        }
        this.endElementListener = endElementListener;
    }

    /**
     * Sets a listener for the end of this text element.
     */
    public void setEndTextElementListener(android.sax.EndTextElementListener endTextElementListener) {
        if (this.endTextElementListener != null) {
            throw new java.lang.IllegalStateException("End text element listener has already been set.");
        }
        if (children != null) {
            throw new java.lang.IllegalStateException("This element already has children." + " It cannot have an end text element listener.");
        }
        this.endTextElementListener = endTextElementListener;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return android.sax.Element.toString(uri, localName);
    }

    static java.lang.String toString(java.lang.String uri, java.lang.String localName) {
        return ("'" + (uri.equals("") ? localName : (uri + ":") + localName)) + "'";
    }

    /**
     * Clears flags on required children.
     */
    void resetRequiredChildren() {
        java.util.ArrayList<android.sax.Element> requiredChildren = this.requiredChilden;
        if (requiredChildren != null) {
            for (int i = requiredChildren.size() - 1; i >= 0; i--) {
                requiredChildren.get(i).visited = false;
            }
        }
    }

    /**
     * Throws an exception if a required child was not present.
     */
    void checkRequiredChildren(org.xml.sax.Locator locator) throws org.xml.sax.SAXParseException {
        java.util.ArrayList<android.sax.Element> requiredChildren = this.requiredChilden;
        if (requiredChildren != null) {
            for (int i = requiredChildren.size() - 1; i >= 0; i--) {
                android.sax.Element child = requiredChildren.get(i);
                if (!child.visited) {
                    throw new android.sax.BadXmlException((((("Element named " + this) + " is missing required") + " child element named ") + child) + ".", locator);
                }
            }
        }
    }
}

