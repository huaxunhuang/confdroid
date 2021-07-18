/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.util.jar;


/**
 * Reads a JAR file manifest. The specification is here:
 * http://java.sun.com/javase/6/docs/technotes/guides/jar/jar.html
 */
class StrictJarManifestReader {
    // There are relatively few unique attribute names,
    // but a manifest might have thousands of entries.
    private final java.util.HashMap<java.lang.String, java.util.jar.Attributes.Name> attributeNameCache = new java.util.HashMap<java.lang.String, java.util.jar.Attributes.Name>();

    private final java.io.ByteArrayOutputStream valueBuffer = new java.io.ByteArrayOutputStream(80);

    private final byte[] buf;

    private final int endOfMainSection;

    private int pos;

    private java.util.jar.Attributes.Name name;

    private java.lang.String value;

    private int consecutiveLineBreaks = 0;

    public StrictJarManifestReader(byte[] buf, java.util.jar.Attributes main) throws java.io.IOException {
        this.buf = buf;
        while (readHeader()) {
            main.put(name, value);
        } 
        this.endOfMainSection = pos;
    }

    public void readEntries(java.util.Map<java.lang.String, java.util.jar.Attributes> entries, java.util.Map<java.lang.String, android.util.jar.StrictJarManifest.Chunk> chunks) throws java.io.IOException {
        int mark = pos;
        while (readHeader()) {
            if (!NAME.equals(name)) {
                throw new java.io.IOException("Entry is not named");
            }
            java.lang.String entryNameValue = value;
            java.util.jar.Attributes entry = entries.get(entryNameValue);
            if (entry == null) {
                entry = new java.util.jar.Attributes(12);
            }
            while (readHeader()) {
                entry.put(name, value);
            } 
            if (chunks != null) {
                if (chunks.get(entryNameValue) != null) {
                    // TODO A bug: there might be several verification chunks for
                    // the same name. I believe they should be used to update
                    // signature in order of appearance; there are two ways to fix
                    // this: either use a list of chunks, or decide on used
                    // signature algorithm in advance and reread the chunks while
                    // updating the signature; for now a defensive error is thrown
                    throw new java.io.IOException("A jar verifier does not support more than one entry with the same name");
                }
                chunks.put(entryNameValue, new android.util.jar.StrictJarManifest.Chunk(mark, pos));
                mark = pos;
            }
            entries.put(entryNameValue, entry);
        } 
    }

    public int getEndOfMainSection() {
        return endOfMainSection;
    }

    /**
     * Read a single line from the manifest buffer.
     */
    private boolean readHeader() throws java.io.IOException {
        if (consecutiveLineBreaks > 1) {
            // break a section on an empty line
            consecutiveLineBreaks = 0;
            return false;
        }
        readName();
        consecutiveLineBreaks = 0;
        readValue();
        // if the last line break is missed, the line
        // is ignored by the reference implementation
        return consecutiveLineBreaks > 0;
    }

    private void readName() throws java.io.IOException {
        int mark = pos;
        while (pos < buf.length) {
            if (buf[pos++] != ':') {
                continue;
            }
            java.lang.String nameString = new java.lang.String(buf, mark, (pos - mark) - 1, java.nio.charset.StandardCharsets.US_ASCII);
            if (buf[pos++] != ' ') {
                throw new java.io.IOException(java.lang.String.format("Invalid value for attribute '%s'", nameString));
            }
            try {
                name = attributeNameCache.get(nameString);
                if (name == null) {
                    name = new java.util.jar.Attributes.Name(nameString);
                    attributeNameCache.put(nameString, name);
                }
            } catch (java.lang.IllegalArgumentException e) {
                // new Attributes.Name() throws IllegalArgumentException but we declare IOException
                throw new java.io.IOException(e.getMessage());
            }
            return;
        } 
    }

    private void readValue() throws java.io.IOException {
        boolean lastCr = false;
        int mark = pos;
        int last = pos;
        valueBuffer.reset();
        while (pos < buf.length) {
            byte next = buf[pos++];
            switch (next) {
                case 0 :
                    throw new java.io.IOException("NUL character in a manifest");
                case '\n' :
                    if (lastCr) {
                        lastCr = false;
                    } else {
                        consecutiveLineBreaks++;
                    }
                    continue;
                case '\r' :
                    lastCr = true;
                    consecutiveLineBreaks++;
                    continue;
                case ' ' :
                    if (consecutiveLineBreaks == 1) {
                        valueBuffer.write(buf, mark, last - mark);
                        mark = pos;
                        consecutiveLineBreaks = 0;
                        continue;
                    }
            }
            if (consecutiveLineBreaks >= 1) {
                pos--;
                break;
            }
            last = pos;
        } 
        valueBuffer.write(buf, mark, last - mark);
        // A bit frustrating that that Charset.forName will be called
        // again.
        value = valueBuffer.toString(java.nio.charset.StandardCharsets.UTF_8.name());
    }
}

