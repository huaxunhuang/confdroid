/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Writes a JSON (<a href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>)
 * encoded value to a stream, one token at a time. The stream includes both
 * literal values (strings, numbers, booleans and nulls) as well as the begin
 * and end delimiters of objects and arrays.
 *
 * <h3>Encoding JSON</h3>
 * To encode your data as JSON, create a new {@code JsonWriter}. Each JSON
 * document must contain one top-level array or object. Call methods on the
 * writer as you walk the structure's contents, nesting arrays and objects as
 * necessary:
 * <ul>
 *   <li>To write <strong>arrays</strong>, first call {@link #beginArray()}.
 *       Write each of the array's elements with the appropriate {@link #value}
 *       methods or by nesting other arrays and objects. Finally close the array
 *       using {@link #endArray()}.
 *   <li>To write <strong>objects</strong>, first call {@link #beginObject()}.
 *       Write each of the object's properties by alternating calls to
 *       {@link #name} with the property's value. Write property values with the
 *       appropriate {@link #value} method or by nesting other objects or arrays.
 *       Finally close the object using {@link #endObject()}.
 * </ul>
 *
 * <h3>Example</h3>
 * Suppose we'd like to encode a stream of messages such as the following: <pre> {@code [
 *   {
 *     "id": 912345678901,
 *     "text": "How do I write JSON on Android?",
 *     "geo": null,
 *     "user": {
 *       "name": "android_newb",
 *       "followers_count": 41}
 *   },
 *   {
 *     "id": 912345678902,
 *     "text": "@android_newb just use android.util.JsonWriter!",
 *     "geo": [50.454722, -104.606667],
 *     "user": {
 *       "name": "jesse",
 *       "followers_count": 2
 *     }
 *   }
 * ]}</pre>
 * This code encodes the above structure: <pre>   {@code public void writeJsonStream(OutputStream out, List<Message> messages) throws IOException {
 *     JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
 *     writer.setIndent("  ");
 *     writeMessagesArray(writer, messages);
 *     writer.close();}
 *
 *   public void writeMessagesArray(JsonWriter writer, List<Message> messages) throws IOException {
 *     writer.beginArray();
 *     for (Message message : messages) {
 *       writeMessage(writer, message);
 *     }
 *     writer.endArray();
 *   }
 *
 *   public void writeMessage(JsonWriter writer, Message message) throws IOException {
 *     writer.beginObject();
 *     writer.name("id").value(message.getId());
 *     writer.name("text").value(message.getText());
 *     if (message.getGeo() != null) {
 *       writer.name("geo");
 *       writeDoublesArray(writer, message.getGeo());
 *     } else {
 *       writer.name("geo").nullValue();
 *     }
 *     writer.name("user");
 *     writeUser(writer, message.getUser());
 *     writer.endObject();
 *   }
 *
 *   public void writeUser(JsonWriter writer, User user) throws IOException {
 *     writer.beginObject();
 *     writer.name("name").value(user.getName());
 *     writer.name("followers_count").value(user.getFollowersCount());
 *     writer.endObject();
 *   }
 *
 *   public void writeDoublesArray(JsonWriter writer, List<Double> doubles) throws IOException {
 *     writer.beginArray();
 *     for (Double value : doubles) {
 *       writer.value(value);
 *     }
 *     writer.endArray();
 *   }}</pre>
 *
 * <p>Each {@code JsonWriter} may be used to write a single JSON stream.
 * Instances of this class are not thread safe. Calls that would result in a
 * malformed JSON string will fail with an {@link IllegalStateException}.
 */
public final class JsonWriter implements java.io.Closeable {
    /**
     * The output data, containing at most one top-level array or object.
     */
    private final java.io.Writer out;

    private final java.util.List<android.util.JsonScope> stack = new java.util.ArrayList<android.util.JsonScope>();

    {
        stack.add(android.util.JsonScope.EMPTY_DOCUMENT);
    }

    /**
     * A string containing a full set of spaces for a single level of
     * indentation, or null for no pretty printing.
     */
    private java.lang.String indent;

    /**
     * The name/value separator; either ":" or ": ".
     */
    private java.lang.String separator = ":";

    private boolean lenient;

    /**
     * Creates a new instance that writes a JSON-encoded stream to {@code out}.
     * For best performance, ensure {@link Writer} is buffered; wrapping in
     * {@link java.io.BufferedWriter BufferedWriter} if necessary.
     */
    public JsonWriter(java.io.Writer out) {
        if (out == null) {
            throw new java.lang.NullPointerException("out == null");
        }
        this.out = out;
    }

    /**
     * Sets the indentation string to be repeated for each level of indentation
     * in the encoded document. If {@code indent.isEmpty()} the encoded document
     * will be compact. Otherwise the encoded document will be more
     * human-readable.
     *
     * @param indent
     * 		a string containing only whitespace.
     */
    public void setIndent(java.lang.String indent) {
        if (indent.isEmpty()) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = indent;
            this.separator = ": ";
        }
    }

    /**
     * Configure this writer to relax its syntax rules. By default, this writer
     * only emits well-formed JSON as specified by <a
     * href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>. Setting the writer
     * to lenient permits the following:
     * <ul>
     *   <li>Top-level values of any type. With strict writing, the top-level
     *       value must be an object or an array.
     *   <li>Numbers may be {@link Double#isNaN() NaNs} or {@link Double#isInfinite() infinities}.
     * </ul>
     */
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    /**
     * Returns true if this writer has relaxed syntax rules.
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * Begins encoding a new array. Each call to this method must be paired with
     * a call to {@link #endArray}.
     *
     * @return this writer.
     */
    public android.util.JsonWriter beginArray() throws java.io.IOException {
        return open(android.util.JsonScope.EMPTY_ARRAY, "[");
    }

    /**
     * Ends encoding the current array.
     *
     * @return this writer.
     */
    public android.util.JsonWriter endArray() throws java.io.IOException {
        return close(android.util.JsonScope.EMPTY_ARRAY, android.util.JsonScope.NONEMPTY_ARRAY, "]");
    }

    /**
     * Begins encoding a new object. Each call to this method must be paired
     * with a call to {@link #endObject}.
     *
     * @return this writer.
     */
    public android.util.JsonWriter beginObject() throws java.io.IOException {
        return open(android.util.JsonScope.EMPTY_OBJECT, "{");
    }

    /**
     * Ends encoding the current object.
     *
     * @return this writer.
     */
    public android.util.JsonWriter endObject() throws java.io.IOException {
        return close(android.util.JsonScope.EMPTY_OBJECT, android.util.JsonScope.NONEMPTY_OBJECT, "}");
    }

    /**
     * Enters a new scope by appending any necessary whitespace and the given
     * bracket.
     */
    private android.util.JsonWriter open(android.util.JsonScope empty, java.lang.String openBracket) throws java.io.IOException {
        beforeValue(true);
        stack.add(empty);
        out.write(openBracket);
        return this;
    }

    /**
     * Closes the current scope by appending any necessary whitespace and the
     * given bracket.
     */
    private android.util.JsonWriter close(android.util.JsonScope empty, android.util.JsonScope nonempty, java.lang.String closeBracket) throws java.io.IOException {
        android.util.JsonScope context = peek();
        if ((context != nonempty) && (context != empty)) {
            throw new java.lang.IllegalStateException("Nesting problem: " + stack);
        }
        stack.remove(stack.size() - 1);
        if (context == nonempty) {
            newline();
        }
        out.write(closeBracket);
        return this;
    }

    /**
     * Returns the value on the top of the stack.
     */
    private android.util.JsonScope peek() {
        return stack.get(stack.size() - 1);
    }

    /**
     * Replace the value on the top of the stack with the given value.
     */
    private void replaceTop(android.util.JsonScope topOfStack) {
        stack.set(stack.size() - 1, topOfStack);
    }

    /**
     * Encodes the property name.
     *
     * @param name
     * 		the name of the forthcoming value. May not be null.
     * @return this writer.
     */
    public android.util.JsonWriter name(java.lang.String name) throws java.io.IOException {
        if (name == null) {
            throw new java.lang.NullPointerException("name == null");
        }
        beforeName();
        string(name);
        return this;
    }

    /**
     * Encodes {@code value}.
     *
     * @param value
     * 		the literal string value, or null to encode a null literal.
     * @return this writer.
     */
    public android.util.JsonWriter value(java.lang.String value) throws java.io.IOException {
        if (value == null) {
            return nullValue();
        }
        beforeValue(false);
        string(value);
        return this;
    }

    /**
     * Encodes {@code null}.
     *
     * @return this writer.
     */
    public android.util.JsonWriter nullValue() throws java.io.IOException {
        beforeValue(false);
        out.write("null");
        return this;
    }

    /**
     * Encodes {@code value}.
     *
     * @return this writer.
     */
    public android.util.JsonWriter value(boolean value) throws java.io.IOException {
        beforeValue(false);
        out.write(value ? "true" : "false");
        return this;
    }

    /**
     * Encodes {@code value}.
     *
     * @param value
     * 		a finite value. May not be {@link Double#isNaN() NaNs} or
     * 		{@link Double#isInfinite() infinities} unless this writer is lenient.
     * @return this writer.
     */
    public android.util.JsonWriter value(double value) throws java.io.IOException {
        if ((!lenient) && (java.lang.Double.isNaN(value) || java.lang.Double.isInfinite(value))) {
            throw new java.lang.IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
        beforeValue(false);
        out.append(java.lang.Double.toString(value));
        return this;
    }

    /**
     * Encodes {@code value}.
     *
     * @return this writer.
     */
    public android.util.JsonWriter value(long value) throws java.io.IOException {
        beforeValue(false);
        out.write(java.lang.Long.toString(value));
        return this;
    }

    /**
     * Encodes {@code value}.
     *
     * @param value
     * 		a finite value. May not be {@link Double#isNaN() NaNs} or
     * 		{@link Double#isInfinite() infinities} unless this writer is lenient.
     * @return this writer.
     */
    public android.util.JsonWriter value(java.lang.Number value) throws java.io.IOException {
        if (value == null) {
            return nullValue();
        }
        java.lang.String string = value.toString();
        if ((!lenient) && ((string.equals("-Infinity") || string.equals("Infinity")) || string.equals("NaN"))) {
            throw new java.lang.IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
        beforeValue(false);
        out.append(string);
        return this;
    }

    /**
     * Ensures all buffered data is written to the underlying {@link Writer}
     * and flushes that writer.
     */
    public void flush() throws java.io.IOException {
        out.flush();
    }

    /**
     * Flushes and closes this writer and the underlying {@link Writer}.
     *
     * @throws IOException
     * 		if the JSON document is incomplete.
     */
    public void close() throws java.io.IOException {
        out.close();
        if (peek() != android.util.JsonScope.NONEMPTY_DOCUMENT) {
            throw new java.io.IOException("Incomplete document");
        }
    }

    private void string(java.lang.String value) throws java.io.IOException {
        out.write("\"");
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            /* From RFC 4627, "All Unicode characters may be placed within the
            quotation marks except for the characters that must be escaped:
            quotation mark, reverse solidus, and the control characters
            (U+0000 through U+001F)."

            We also escape '\u2028' and '\u2029', which JavaScript interprets
            as newline characters. This prevents eval() from failing with a
            syntax error.
            http://code.google.com/p/google-gson/issues/detail?id=341
             */
            switch (c) {
                case '"' :
                case '\\' :
                    out.write('\\');
                    out.write(c);
                    break;
                case '\t' :
                    out.write("\\t");
                    break;
                case '\b' :
                    out.write("\\b");
                    break;
                case '\n' :
                    out.write("\\n");
                    break;
                case '\r' :
                    out.write("\\r");
                    break;
                case '\f' :
                    out.write("\\f");
                    break;
                case '\u2028' :
                case '\u2029' :
                    out.write(java.lang.String.format("\\u%04x", ((int) (c))));
                    break;
                default :
                    if (c <= 0x1f) {
                        out.write(java.lang.String.format("\\u%04x", ((int) (c))));
                    } else {
                        out.write(c);
                    }
                    break;
            }
        }
        out.write("\"");
    }

    private void newline() throws java.io.IOException {
        if (indent == null) {
            return;
        }
        out.write("\n");
        for (int i = 1; i < stack.size(); i++) {
            out.write(indent);
        }
    }

    /**
     * Inserts any necessary separators and whitespace before a name. Also
     * adjusts the stack to expect the name's value.
     */
    private void beforeName() throws java.io.IOException {
        android.util.JsonScope context = peek();
        if (context == android.util.JsonScope.NONEMPTY_OBJECT) {
            // first in object
            out.write(',');
        } else
            if (context != android.util.JsonScope.EMPTY_OBJECT) {
                // not in an object!
                throw new java.lang.IllegalStateException("Nesting problem: " + stack);
            }

        newline();
        replaceTop(android.util.JsonScope.DANGLING_NAME);
    }

    /**
     * Inserts any necessary separators and whitespace before a literal value,
     * inline array, or inline object. Also adjusts the stack to expect either a
     * closing bracket or another element.
     *
     * @param root
     * 		true if the value is a new array or object, the two values
     * 		permitted as top-level elements.
     */
    private void beforeValue(boolean root) throws java.io.IOException {
        switch (peek()) {
            case EMPTY_DOCUMENT :
                // first in document
                if ((!lenient) && (!root)) {
                    throw new java.lang.IllegalStateException("JSON must start with an array or an object.");
                }
                replaceTop(android.util.JsonScope.NONEMPTY_DOCUMENT);
                break;
            case EMPTY_ARRAY :
                // first in array
                replaceTop(android.util.JsonScope.NONEMPTY_ARRAY);
                newline();
                break;
            case NONEMPTY_ARRAY :
                // another in array
                out.append(',');
                newline();
                break;
            case DANGLING_NAME :
                // value for name
                out.append(separator);
                replaceTop(android.util.JsonScope.NONEMPTY_OBJECT);
                break;
            case NONEMPTY_DOCUMENT :
                throw new java.lang.IllegalStateException("JSON must have only one top-level value.");
            default :
                throw new java.lang.IllegalStateException("Nesting problem: " + stack);
        }
    }
}

