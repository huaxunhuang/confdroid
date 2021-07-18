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
 * Reads a JSON (<a href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>)
 * encoded value as a stream of tokens. This stream includes both literal
 * values (strings, numbers, booleans, and nulls) as well as the begin and
 * end delimiters of objects and arrays. The tokens are traversed in
 * depth-first order, the same order that they appear in the JSON document.
 * Within JSON objects, name/value pairs are represented by a single token.
 *
 * <h3>Parsing JSON</h3>
 * To create a recursive descent parser for your own JSON streams, first create
 * an entry point method that creates a {@code JsonReader}.
 *
 * <p>Next, create handler methods for each structure in your JSON text. You'll
 * need a method for each object type and for each array type.
 * <ul>
 *   <li>Within <strong>array handling</strong> methods, first call {@link #beginArray} to consume the array's opening bracket. Then create a
 *       while loop that accumulates values, terminating when {@link #hasNext}
 *       is false. Finally, read the array's closing bracket by calling {@link #endArray}.
 *   <li>Within <strong>object handling</strong> methods, first call {@link #beginObject} to consume the object's opening brace. Then create a
 *       while loop that assigns values to local variables based on their name.
 *       This loop should terminate when {@link #hasNext} is false. Finally,
 *       read the object's closing brace by calling {@link #endObject}.
 * </ul>
 * <p>When a nested object or array is encountered, delegate to the
 * corresponding handler method.
 *
 * <p>When an unknown name is encountered, strict parsers should fail with an
 * exception. Lenient parsers should call {@link #skipValue()} to recursively
 * skip the value's nested tokens, which may otherwise conflict.
 *
 * <p>If a value may be null, you should first check using {@link #peek()}.
 * Null literals can be consumed using either {@link #nextNull()} or {@link #skipValue()}.
 *
 * <h3>Example</h3>
 * Suppose we'd like to parse a stream of messages such as the following: <pre> {@code [
 *   {
 *     "id": 912345678901,
 *     "text": "How do I read JSON on Android?",
 *     "geo": null,
 *     "user": {
 *       "name": "android_newb",
 *       "followers_count": 41}
 *   },
 *   {
 *     "id": 912345678902,
 *     "text": "@android_newb just use android.util.JsonReader!",
 *     "geo": [50.454722, -104.606667],
 *     "user": {
 *       "name": "jesse",
 *       "followers_count": 2
 *     }
 *   }
 * ]}</pre>
 * This code implements the parser for the above structure: <pre>   {@code public List<Message> readJsonStream(InputStream in) throws IOException {
 *     JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
 *     try {
 *       return readMessagesArray(reader);} finally {
 *       reader.close();
 *     }
 *   }
 *
 *   public List<Message> readMessagesArray(JsonReader reader) throws IOException {
 *     List<Message> messages = new ArrayList<Message>();
 *
 *     reader.beginArray();
 *     while (reader.hasNext()) {
 *       messages.add(readMessage(reader));
 *     }
 *     reader.endArray();
 *     return messages;
 *   }
 *
 *   public Message readMessage(JsonReader reader) throws IOException {
 *     long id = -1;
 *     String text = null;
 *     User user = null;
 *     List<Double> geo = null;
 *
 *     reader.beginObject();
 *     while (reader.hasNext()) {
 *       String name = reader.nextName();
 *       if (name.equals("id")) {
 *         id = reader.nextLong();
 *       } else if (name.equals("text")) {
 *         text = reader.nextString();
 *       } else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
 *         geo = readDoublesArray(reader);
 *       } else if (name.equals("user")) {
 *         user = readUser(reader);
 *       } else {
 *         reader.skipValue();
 *       }
 *     }
 *     reader.endObject();
 *     return new Message(id, text, user, geo);
 *   }
 *
 *   public List<Double> readDoublesArray(JsonReader reader) throws IOException {
 *     List<Double> doubles = new ArrayList<Double>();
 *
 *     reader.beginArray();
 *     while (reader.hasNext()) {
 *       doubles.add(reader.nextDouble());
 *     }
 *     reader.endArray();
 *     return doubles;
 *   }
 *
 *   public User readUser(JsonReader reader) throws IOException {
 *     String username = null;
 *     int followersCount = -1;
 *
 *     reader.beginObject();
 *     while (reader.hasNext()) {
 *       String name = reader.nextName();
 *       if (name.equals("name")) {
 *         username = reader.nextString();
 *       } else if (name.equals("followers_count")) {
 *         followersCount = reader.nextInt();
 *       } else {
 *         reader.skipValue();
 *       }
 *     }
 *     reader.endObject();
 *     return new User(username, followersCount);
 *   }}</pre>
 *
 * <h3>Number Handling</h3>
 * This reader permits numeric values to be read as strings and string values to
 * be read as numbers. For example, both elements of the JSON array {@code [1, "1"]} may be read using either {@link #nextInt} or {@link #nextString}.
 * This behavior is intended to prevent lossy numeric conversions: double is
 * JavaScript's only numeric type and very large values like {@code 9007199254740993} cannot be represented exactly on that platform. To minimize
 * precision loss, extremely large values should be written and read as strings
 * in JSON.
 *
 * <p>Each {@code JsonReader} may be used to read a single JSON stream. Instances
 * of this class are not thread safe.
 */
public final class JsonReader implements java.io.Closeable {
    private static final java.lang.String TRUE = "true";

    private static final java.lang.String FALSE = "false";

    private final libcore.internal.StringPool stringPool = new libcore.internal.StringPool();

    /**
     * The input JSON.
     */
    private final java.io.Reader in;

    /**
     * True to accept non-spec compliant JSON
     */
    private boolean lenient = false;

    /**
     * Use a manual buffer to easily read and unread upcoming characters, and
     * also so we can create strings without an intermediate StringBuilder.
     * We decode literals directly out of this buffer, so it must be at least as
     * long as the longest token that can be reported as a number.
     */
    private final char[] buffer = new char[1024];

    private int pos = 0;

    private int limit = 0;

    /* The offset of the first character in the buffer. */
    private int bufferStartLine = 1;

    private int bufferStartColumn = 1;

    private final java.util.List<android.util.JsonScope> stack = new java.util.ArrayList<android.util.JsonScope>();

    {
        push(android.util.JsonScope.EMPTY_DOCUMENT);
    }

    /**
     * The type of the next token to be returned by {@link #peek} and {@link #advance}. If null, peek() will assign a value.
     */
    private android.util.JsonToken token;

    /**
     * The text of the next name.
     */
    private java.lang.String name;

    /* For the next literal value, we may have the text value, or the position
    and length in the buffer.
     */
    private java.lang.String value;

    private int valuePos;

    private int valueLength;

    /**
     * True if we're currently handling a skipValue() call.
     */
    private boolean skipping = false;

    /**
     * Creates a new instance that reads a JSON-encoded stream from {@code in}.
     */
    public JsonReader(java.io.Reader in) {
        if (in == null) {
            throw new java.lang.NullPointerException("in == null");
        }
        this.in = in;
    }

    /**
     * Configure this parser to be  be liberal in what it accepts. By default,
     * this parser is strict and only accepts JSON as specified by <a
     * href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>. Setting the
     * parser to lenient causes it to ignore the following syntax errors:
     *
     * <ul>
     *   <li>End of line comments starting with {@code //} or {@code #} and
     *       ending with a newline character.
     *   <li>C-style comments starting with {@code /*} and ending with
     *       {@code *}{@code /}. Such comments may not be nested.
     *   <li>Names that are unquoted or {@code 'single quoted'}.
     *   <li>Strings that are unquoted or {@code 'single quoted'}.
     *   <li>Array elements separated by {@code ;} instead of {@code ,}.
     *   <li>Unnecessary array separators. These are interpreted as if null
     *       was the omitted value.
     *   <li>Names and values separated by {@code =} or {@code =>} instead of
     *       {@code :}.
     *   <li>Name/value pairs separated by {@code ;} instead of {@code ,}.
     * </ul>
     */
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    /**
     * Returns true if this parser is liberal in what it accepts.
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * beginning of a new array.
     */
    public void beginArray() throws java.io.IOException {
        expect(android.util.JsonToken.BEGIN_ARRAY);
    }

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * end of the current array.
     */
    public void endArray() throws java.io.IOException {
        expect(android.util.JsonToken.END_ARRAY);
    }

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * beginning of a new object.
     */
    public void beginObject() throws java.io.IOException {
        expect(android.util.JsonToken.BEGIN_OBJECT);
    }

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * end of the current array.
     */
    public void endObject() throws java.io.IOException {
        expect(android.util.JsonToken.END_OBJECT);
    }

    /**
     * Consumes {@code expected}.
     */
    private void expect(android.util.JsonToken expected) throws java.io.IOException {
        peek();
        if (token != expected) {
            throw new java.lang.IllegalStateException((("Expected " + expected) + " but was ") + peek());
        }
        advance();
    }

    /**
     * Returns true if the current array or object has another element.
     */
    public boolean hasNext() throws java.io.IOException {
        peek();
        return (token != android.util.JsonToken.END_OBJECT) && (token != android.util.JsonToken.END_ARRAY);
    }

    /**
     * Returns the type of the next token without consuming it.
     */
    public android.util.JsonToken peek() throws java.io.IOException {
        if (token != null) {
            return token;
        }
        switch (peekStack()) {
            case EMPTY_DOCUMENT :
                replaceTop(android.util.JsonScope.NONEMPTY_DOCUMENT);
                android.util.JsonToken firstToken = nextValue();
                if (((!lenient) && (token != android.util.JsonToken.BEGIN_ARRAY)) && (token != android.util.JsonToken.BEGIN_OBJECT)) {
                    throw new java.io.IOException("Expected JSON document to start with '[' or '{' but was " + token);
                }
                return firstToken;
            case EMPTY_ARRAY :
                return nextInArray(true);
            case NONEMPTY_ARRAY :
                return nextInArray(false);
            case EMPTY_OBJECT :
                return nextInObject(true);
            case DANGLING_NAME :
                return objectValue();
            case NONEMPTY_OBJECT :
                return nextInObject(false);
            case NONEMPTY_DOCUMENT :
                try {
                    android.util.JsonToken token = nextValue();
                    if (lenient) {
                        return token;
                    }
                    throw syntaxError("Expected EOF");
                } catch (java.io.EOFException e) {
                    return token = android.util.JsonToken.END_DOCUMENT;// TODO: avoid throwing here?

                }
            case CLOSED :
                throw new java.lang.IllegalStateException("JsonReader is closed");
            default :
                throw new java.lang.AssertionError();
        }
    }

    /**
     * Advances the cursor in the JSON stream to the next token.
     */
    private android.util.JsonToken advance() throws java.io.IOException {
        peek();
        android.util.JsonToken result = token;
        token = null;
        value = null;
        name = null;
        return result;
    }

    /**
     * Returns the next token, a {@link JsonToken#NAME property name}, and
     * consumes it.
     *
     * @throws IOException
     * 		if the next token in the stream is not a property
     * 		name.
     */
    public java.lang.String nextName() throws java.io.IOException {
        peek();
        if (token != android.util.JsonToken.NAME) {
            throw new java.lang.IllegalStateException("Expected a name but was " + peek());
        }
        java.lang.String result = name;
        advance();
        return result;
    }

    /**
     * Returns the {@link JsonToken#STRING string} value of the next token,
     * consuming it. If the next token is a number, this method will return its
     * string form.
     *
     * @throws IllegalStateException
     * 		if the next token is not a string or if
     * 		this reader is closed.
     */
    public java.lang.String nextString() throws java.io.IOException {
        peek();
        if ((token != android.util.JsonToken.STRING) && (token != android.util.JsonToken.NUMBER)) {
            throw new java.lang.IllegalStateException("Expected a string but was " + peek());
        }
        java.lang.String result = value;
        advance();
        return result;
    }

    /**
     * Returns the {@link JsonToken#BOOLEAN boolean} value of the next token,
     * consuming it.
     *
     * @throws IllegalStateException
     * 		if the next token is not a boolean or if
     * 		this reader is closed.
     */
    public boolean nextBoolean() throws java.io.IOException {
        peek();
        if (token != android.util.JsonToken.BOOLEAN) {
            throw new java.lang.IllegalStateException("Expected a boolean but was " + token);
        }
        boolean result = value == android.util.JsonReader.TRUE;
        advance();
        return result;
    }

    /**
     * Consumes the next token from the JSON stream and asserts that it is a
     * literal null.
     *
     * @throws IllegalStateException
     * 		if the next token is not null or if this
     * 		reader is closed.
     */
    public void nextNull() throws java.io.IOException {
        peek();
        if (token != android.util.JsonToken.NULL) {
            throw new java.lang.IllegalStateException("Expected null but was " + token);
        }
        advance();
    }

    /**
     * Returns the {@link JsonToken#NUMBER double} value of the next token,
     * consuming it. If the next token is a string, this method will attempt to
     * parse it as a double using {@link Double#parseDouble(String)}.
     *
     * @throws IllegalStateException
     * 		if the next token is not a literal value.
     */
    public double nextDouble() throws java.io.IOException {
        peek();
        if ((token != android.util.JsonToken.STRING) && (token != android.util.JsonToken.NUMBER)) {
            throw new java.lang.IllegalStateException("Expected a double but was " + token);
        }
        double result = java.lang.Double.parseDouble(value);
        advance();
        return result;
    }

    /**
     * Returns the {@link JsonToken#NUMBER long} value of the next token,
     * consuming it. If the next token is a string, this method will attempt to
     * parse it as a long. If the next token's numeric value cannot be exactly
     * represented by a Java {@code long}, this method throws.
     *
     * @throws IllegalStateException
     * 		if the next token is not a literal value.
     * @throws NumberFormatException
     * 		if the next literal value cannot be parsed
     * 		as a number, or exactly represented as a long.
     */
    public long nextLong() throws java.io.IOException {
        peek();
        if ((token != android.util.JsonToken.STRING) && (token != android.util.JsonToken.NUMBER)) {
            throw new java.lang.IllegalStateException("Expected a long but was " + token);
        }
        long result;
        try {
            result = java.lang.Long.parseLong(value);
        } catch (java.lang.NumberFormatException ignored) {
            double asDouble = java.lang.Double.parseDouble(value);// don't catch this NumberFormatException

            result = ((long) (asDouble));
            if (((double) (result)) != asDouble) {
                throw new java.lang.NumberFormatException(value);
            }
        }
        advance();
        return result;
    }

    /**
     * Returns the {@link JsonToken#NUMBER int} value of the next token,
     * consuming it. If the next token is a string, this method will attempt to
     * parse it as an int. If the next token's numeric value cannot be exactly
     * represented by a Java {@code int}, this method throws.
     *
     * @throws IllegalStateException
     * 		if the next token is not a literal value.
     * @throws NumberFormatException
     * 		if the next literal value cannot be parsed
     * 		as a number, or exactly represented as an int.
     */
    public int nextInt() throws java.io.IOException {
        peek();
        if ((token != android.util.JsonToken.STRING) && (token != android.util.JsonToken.NUMBER)) {
            throw new java.lang.IllegalStateException("Expected an int but was " + token);
        }
        int result;
        try {
            result = java.lang.Integer.parseInt(value);
        } catch (java.lang.NumberFormatException ignored) {
            double asDouble = java.lang.Double.parseDouble(value);// don't catch this NumberFormatException

            result = ((int) (asDouble));
            if (((double) (result)) != asDouble) {
                throw new java.lang.NumberFormatException(value);
            }
        }
        advance();
        return result;
    }

    /**
     * Closes this JSON reader and the underlying {@link Reader}.
     */
    public void close() throws java.io.IOException {
        value = null;
        token = null;
        stack.clear();
        stack.add(android.util.JsonScope.CLOSED);
        in.close();
    }

    /**
     * Skips the next value recursively. If it is an object or array, all nested
     * elements are skipped. This method is intended for use when the JSON token
     * stream contains unrecognized or unhandled values.
     */
    public void skipValue() throws java.io.IOException {
        skipping = true;
        try {
            if ((!hasNext()) || (peek() == android.util.JsonToken.END_DOCUMENT)) {
                throw new java.lang.IllegalStateException("No element left to skip");
            }
            int count = 0;
            do {
                android.util.JsonToken token = advance();
                if ((token == android.util.JsonToken.BEGIN_ARRAY) || (token == android.util.JsonToken.BEGIN_OBJECT)) {
                    count++;
                } else
                    if ((token == android.util.JsonToken.END_ARRAY) || (token == android.util.JsonToken.END_OBJECT)) {
                        count--;
                    }

            } while (count != 0 );
        } finally {
            skipping = false;
        }
    }

    private android.util.JsonScope peekStack() {
        return stack.get(stack.size() - 1);
    }

    private android.util.JsonScope pop() {
        return stack.remove(stack.size() - 1);
    }

    private void push(android.util.JsonScope newTop) {
        stack.add(newTop);
    }

    /**
     * Replace the value on the top of the stack with the given value.
     */
    private void replaceTop(android.util.JsonScope newTop) {
        stack.set(stack.size() - 1, newTop);
    }

    private android.util.JsonToken nextInArray(boolean firstElement) throws java.io.IOException {
        if (firstElement) {
            replaceTop(android.util.JsonScope.NONEMPTY_ARRAY);
        } else {
            /* Look for a comma before each element after the first element. */
            switch (nextNonWhitespace()) {
                case ']' :
                    pop();
                    return token = android.util.JsonToken.END_ARRAY;
                case ';' :
                    checkLenient();// fall-through

                case ',' :
                    break;
                default :
                    throw syntaxError("Unterminated array");
            }
        }
        switch (nextNonWhitespace()) {
            case ']' :
                if (firstElement) {
                    pop();
                    return token = android.util.JsonToken.END_ARRAY;
                }
                // fall-through to handle ",]"
            case ';' :
            case ',' :
                /* In lenient mode, a 0-length literal means 'null' */
                checkLenient();
                pos--;
                value = "null";
                return token = android.util.JsonToken.NULL;
            default :
                pos--;
                return nextValue();
        }
    }

    private android.util.JsonToken nextInObject(boolean firstElement) throws java.io.IOException {
        /* Read delimiters. Either a comma/semicolon separating this and the
        previous name-value pair, or a close brace to denote the end of the
        object.
         */
        if (firstElement) {
            /* Peek to see if this is the empty object. */
            switch (nextNonWhitespace()) {
                case '}' :
                    pop();
                    return token = android.util.JsonToken.END_OBJECT;
                default :
                    pos--;
            }
        } else {
            switch (nextNonWhitespace()) {
                case '}' :
                    pop();
                    return token = android.util.JsonToken.END_OBJECT;
                case ';' :
                case ',' :
                    break;
                default :
                    throw syntaxError("Unterminated object");
            }
        }
        /* Read the name. */
        int quote = nextNonWhitespace();
        switch (quote) {
            case '\'' :
                checkLenient();// fall-through

            case '"' :
                name = nextString(((char) (quote)));
                break;
            default :
                checkLenient();
                pos--;
                name = nextLiteral(false);
                if (name.isEmpty()) {
                    throw syntaxError("Expected name");
                }
        }
        replaceTop(android.util.JsonScope.DANGLING_NAME);
        return token = android.util.JsonToken.NAME;
    }

    private android.util.JsonToken objectValue() throws java.io.IOException {
        /* Read the name/value separator. Usually a colon ':'. In lenient mode
        we also accept an equals sign '=', or an arrow "=>".
         */
        switch (nextNonWhitespace()) {
            case ':' :
                break;
            case '=' :
                checkLenient();
                if (((pos < limit) || fillBuffer(1)) && (buffer[pos] == '>')) {
                    pos++;
                }
                break;
            default :
                throw syntaxError("Expected ':'");
        }
        replaceTop(android.util.JsonScope.NONEMPTY_OBJECT);
        return nextValue();
    }

    private android.util.JsonToken nextValue() throws java.io.IOException {
        int c = nextNonWhitespace();
        switch (c) {
            case '{' :
                push(android.util.JsonScope.EMPTY_OBJECT);
                return token = android.util.JsonToken.BEGIN_OBJECT;
            case '[' :
                push(android.util.JsonScope.EMPTY_ARRAY);
                return token = android.util.JsonToken.BEGIN_ARRAY;
            case '\'' :
                checkLenient();// fall-through

            case '"' :
                value = nextString(((char) (c)));
                return token = android.util.JsonToken.STRING;
            default :
                pos--;
                return readLiteral();
        }
    }

    /**
     * Returns true once {@code limit - pos >= minimum}. If the data is
     * exhausted before that many characters are available, this returns
     * false.
     */
    private boolean fillBuffer(int minimum) throws java.io.IOException {
        // Before clobbering the old characters, update where buffer starts
        for (int i = 0; i < pos; i++) {
            if (buffer[i] == '\n') {
                bufferStartLine++;
                bufferStartColumn = 1;
            } else {
                bufferStartColumn++;
            }
        }
        if (limit != pos) {
            limit -= pos;
            java.lang.System.arraycopy(buffer, pos, buffer, 0, limit);
        } else {
            limit = 0;
        }
        pos = 0;
        int total;
        while ((total = in.read(buffer, limit, buffer.length - limit)) != (-1)) {
            limit += total;
            // if this is the first read, consume an optional byte order mark (BOM) if it exists
            if ((((bufferStartLine == 1) && (bufferStartColumn == 1)) && (limit > 0)) && (buffer[0] == '\ufeff')) {
                pos++;
                bufferStartColumn--;
            }
            if (limit >= minimum) {
                return true;
            }
        } 
        return false;
    }

    private int getLineNumber() {
        int result = bufferStartLine;
        for (int i = 0; i < pos; i++) {
            if (buffer[i] == '\n') {
                result++;
            }
        }
        return result;
    }

    private int getColumnNumber() {
        int result = bufferStartColumn;
        for (int i = 0; i < pos; i++) {
            if (buffer[i] == '\n') {
                result = 1;
            } else {
                result++;
            }
        }
        return result;
    }

    private int nextNonWhitespace() throws java.io.IOException {
        while ((pos < limit) || fillBuffer(1)) {
            int c = buffer[pos++];
            switch (c) {
                case '\t' :
                case ' ' :
                case '\n' :
                case '\r' :
                    continue;
                case '/' :
                    if ((pos == limit) && (!fillBuffer(1))) {
                        return c;
                    }
                    checkLenient();
                    char peek = buffer[pos];
                    switch (peek) {
                        case '*' :
                            // skip a /* c-style comment */
                            pos++;
                            if (!skipTo("*/")) {
                                throw syntaxError("Unterminated comment");
                            }
                            pos += 2;
                            continue;
                        case '/' :
                            // skip a // end-of-line comment
                            pos++;
                            skipToEndOfLine();
                            continue;
                        default :
                            return c;
                    }
                case '#' :
                    /* Skip a # hash end-of-line comment. The JSON RFC doesn't
                    specify this behaviour, but it's required to parse
                    existing documents. See http://b/2571423.
                     */
                    checkLenient();
                    skipToEndOfLine();
                    continue;
                default :
                    return c;
            }
        } 
        throw new java.io.EOFException("End of input");
    }

    private void checkLenient() throws java.io.IOException {
        if (!lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    /**
     * Advances the position until after the next newline character. If the line
     * is terminated by "\r\n", the '\n' must be consumed as whitespace by the
     * caller.
     */
    private void skipToEndOfLine() throws java.io.IOException {
        while ((pos < limit) || fillBuffer(1)) {
            char c = buffer[pos++];
            if ((c == '\r') || (c == '\n')) {
                break;
            }
        } 
    }

    private boolean skipTo(java.lang.String toFind) throws java.io.IOException {
        outer : for (; ((pos + toFind.length()) <= limit) || fillBuffer(toFind.length()); pos++) {
            for (int c = 0; c < toFind.length(); c++) {
                if (buffer[pos + c] != toFind.charAt(c)) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the string up to but not including {@code quote}, unescaping any
     * character escape sequences encountered along the way. The opening quote
     * should have already been read. This consumes the closing quote, but does
     * not include it in the returned string.
     *
     * @param quote
     * 		either ' or ".
     * @throws NumberFormatException
     * 		if any unicode escape sequences are
     * 		malformed.
     */
    private java.lang.String nextString(char quote) throws java.io.IOException {
        java.lang.StringBuilder builder = null;
        do {
            /* the index of the first character not yet appended to the builder. */
            int start = pos;
            while (pos < limit) {
                int c = buffer[pos++];
                if (c == quote) {
                    if (skipping) {
                        return "skipped!";
                    } else
                        if (builder == null) {
                            return stringPool.get(buffer, start, (pos - start) - 1);
                        } else {
                            builder.append(buffer, start, (pos - start) - 1);
                            return builder.toString();
                        }

                } else
                    if (c == '\\') {
                        if (builder == null) {
                            builder = new java.lang.StringBuilder();
                        }
                        builder.append(buffer, start, (pos - start) - 1);
                        builder.append(readEscapeCharacter());
                        start = pos;
                    }

            } 
            if (builder == null) {
                builder = new java.lang.StringBuilder();
            }
            builder.append(buffer, start, pos - start);
        } while (fillBuffer(1) );
        throw syntaxError("Unterminated string");
    }

    /**
     * Reads the value up to but not including any delimiter characters. This
     * does not consume the delimiter character.
     *
     * @param assignOffsetsOnly
     * 		true for this method to only set the valuePos
     * 		and valueLength fields and return a null result. This only works if
     * 		the literal is short; a string is returned otherwise.
     */
    private java.lang.String nextLiteral(boolean assignOffsetsOnly) throws java.io.IOException {
        java.lang.StringBuilder builder = null;
        valuePos = -1;
        valueLength = 0;
        int i = 0;
        findNonLiteralCharacter : while (true) {
            for (; (pos + i) < limit; i++) {
                switch (buffer[pos + i]) {
                    case '/' :
                    case '\\' :
                    case ';' :
                    case '#' :
                    case '=' :
                        checkLenient();// fall-through

                    case '{' :
                    case '}' :
                    case '[' :
                    case ']' :
                    case ':' :
                    case ',' :
                    case ' ' :
                    case '\t' :
                    case '\f' :
                    case '\r' :
                    case '\n' :
                        break findNonLiteralCharacter;
                }
            }
            /* Attempt to load the entire literal into the buffer at once. If
            we run out of input, add a non-literal character at the end so
            that decoding doesn't need to do bounds checks.
             */
            if (i < buffer.length) {
                if (fillBuffer(i + 1)) {
                    continue;
                } else {
                    buffer[limit] = '\u0000';
                    break;
                }
            }
            // use a StringBuilder when the value is too long. It must be an unquoted string.
            if (builder == null) {
                builder = new java.lang.StringBuilder();
            }
            builder.append(buffer, pos, i);
            valueLength += i;
            pos += i;
            i = 0;
            if (!fillBuffer(1)) {
                break;
            }
        } 
        java.lang.String result;
        if (assignOffsetsOnly && (builder == null)) {
            valuePos = pos;
            result = null;
        } else
            if (skipping) {
                result = "skipped!";
            } else
                if (builder == null) {
                    result = stringPool.get(buffer, pos, i);
                } else {
                    builder.append(buffer, pos, i);
                    result = builder.toString();
                }


        valueLength += i;
        pos += i;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (getClass().getSimpleName() + " near ") + getSnippet();
    }

    /**
     * Unescapes the character identified by the character or characters that
     * immediately follow a backslash. The backslash '\' should have already
     * been read. This supports both unicode escapes "u000A" and two-character
     * escapes "\n".
     *
     * @throws NumberFormatException
     * 		if any unicode escape sequences are
     * 		malformed.
     */
    private char readEscapeCharacter() throws java.io.IOException {
        if ((pos == limit) && (!fillBuffer(1))) {
            throw syntaxError("Unterminated escape sequence");
        }
        char escaped = buffer[pos++];
        switch (escaped) {
            case 'u' :
                if (((pos + 4) > limit) && (!fillBuffer(4))) {
                    throw syntaxError("Unterminated escape sequence");
                }
                java.lang.String hex = stringPool.get(buffer, pos, 4);
                pos += 4;
                return ((char) (java.lang.Integer.parseInt(hex, 16)));
            case 't' :
                return '\t';
            case 'b' :
                return '\b';
            case 'n' :
                return '\n';
            case 'r' :
                return '\r';
            case 'f' :
                return '\f';
            case '\'' :
            case '"' :
            case '\\' :
            default :
                return escaped;
        }
    }

    /**
     * Reads a null, boolean, numeric or unquoted string literal value.
     */
    private android.util.JsonToken readLiteral() throws java.io.IOException {
        value = nextLiteral(true);
        if (valueLength == 0) {
            throw syntaxError("Expected literal value");
        }
        token = decodeLiteral();
        if (token == android.util.JsonToken.STRING) {
            checkLenient();
        }
        return token;
    }

    /**
     * Assigns {@code nextToken} based on the value of {@code nextValue}.
     */
    private android.util.JsonToken decodeLiteral() throws java.io.IOException {
        if (valuePos == (-1)) {
            // it was too long to fit in the buffer so it can only be a string
            return android.util.JsonToken.STRING;
        } else
            if (((((valueLength == 4) && (('n' == buffer[valuePos]) || ('N' == buffer[valuePos]))) && (('u' == buffer[valuePos + 1]) || ('U' == buffer[valuePos + 1]))) && (('l' == buffer[valuePos + 2]) || ('L' == buffer[valuePos + 2]))) && (('l' == buffer[valuePos + 3]) || ('L' == buffer[valuePos + 3]))) {
                value = "null";
                return android.util.JsonToken.NULL;
            } else
                if (((((valueLength == 4) && (('t' == buffer[valuePos]) || ('T' == buffer[valuePos]))) && (('r' == buffer[valuePos + 1]) || ('R' == buffer[valuePos + 1]))) && (('u' == buffer[valuePos + 2]) || ('U' == buffer[valuePos + 2]))) && (('e' == buffer[valuePos + 3]) || ('E' == buffer[valuePos + 3]))) {
                    value = android.util.JsonReader.TRUE;
                    return android.util.JsonToken.BOOLEAN;
                } else
                    if ((((((valueLength == 5) && (('f' == buffer[valuePos]) || ('F' == buffer[valuePos]))) && (('a' == buffer[valuePos + 1]) || ('A' == buffer[valuePos + 1]))) && (('l' == buffer[valuePos + 2]) || ('L' == buffer[valuePos + 2]))) && (('s' == buffer[valuePos + 3]) || ('S' == buffer[valuePos + 3]))) && (('e' == buffer[valuePos + 4]) || ('E' == buffer[valuePos + 4]))) {
                        value = android.util.JsonReader.FALSE;
                        return android.util.JsonToken.BOOLEAN;
                    } else {
                        value = stringPool.get(buffer, valuePos, valueLength);
                        return decodeNumber(buffer, valuePos, valueLength);
                    }



    }

    /**
     * Determine whether the characters is a JSON number. Numbers are of the
     * form -12.34e+56. Fractional and exponential parts are optional. Leading
     * zeroes are not allowed in the value or exponential part, but are allowed
     * in the fraction.
     */
    private android.util.JsonToken decodeNumber(char[] chars, int offset, int length) {
        int i = offset;
        int c = chars[i];
        if (c == '-') {
            c = chars[++i];
        }
        if (c == '0') {
            c = chars[++i];
        } else
            if ((c >= '1') && (c <= '9')) {
                c = chars[++i];
                while ((c >= '0') && (c <= '9')) {
                    c = chars[++i];
                } 
            } else {
                return android.util.JsonToken.STRING;
            }

        if (c == '.') {
            c = chars[++i];
            while ((c >= '0') && (c <= '9')) {
                c = chars[++i];
            } 
        }
        if ((c == 'e') || (c == 'E')) {
            c = chars[++i];
            if ((c == '+') || (c == '-')) {
                c = chars[++i];
            }
            if ((c >= '0') && (c <= '9')) {
                c = chars[++i];
                while ((c >= '0') && (c <= '9')) {
                    c = chars[++i];
                } 
            } else {
                return android.util.JsonToken.STRING;
            }
        }
        if (i == (offset + length)) {
            return android.util.JsonToken.NUMBER;
        } else {
            return android.util.JsonToken.STRING;
        }
    }

    /**
     * Throws a new IO exception with the given message and a context snippet
     * with this reader's content.
     */
    private java.io.IOException syntaxError(java.lang.String message) throws java.io.IOException {
        throw new android.util.MalformedJsonException((((message + " at line ") + getLineNumber()) + " column ") + getColumnNumber());
    }

    private java.lang.CharSequence getSnippet() {
        java.lang.StringBuilder snippet = new java.lang.StringBuilder();
        int beforePos = java.lang.Math.min(pos, 20);
        snippet.append(buffer, pos - beforePos, beforePos);
        int afterPos = java.lang.Math.min(limit - pos, 20);
        snippet.append(buffer, pos, afterPos);
        return snippet;
    }
}

