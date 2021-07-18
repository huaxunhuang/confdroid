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
 * The {@code StrictJarManifest} class is used to obtain attribute information for a
 * {@code StrictJarFile} and its entries.
 *
 * @unknown 
 */
public class StrictJarManifest implements java.lang.Cloneable {
    static final int LINE_LENGTH_LIMIT = 72;

    private static final byte[] LINE_SEPARATOR = new byte[]{ '\r', '\n' };

    private static final byte[] VALUE_SEPARATOR = new byte[]{ ':', ' ' };

    private final java.util.jar.Attributes mainAttributes;

    private final java.util.HashMap<java.lang.String, java.util.jar.Attributes> entries;

    static final class Chunk {
        final int start;

        final int end;

        Chunk(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private java.util.HashMap<java.lang.String, android.util.jar.StrictJarManifest.Chunk> chunks;

    /**
     * The end of the main attributes section in the manifest is needed in
     * verification.
     */
    private int mainEnd;

    /**
     * Creates a new {@code StrictJarManifest} instance.
     */
    public StrictJarManifest() {
        entries = new java.util.HashMap<java.lang.String, java.util.jar.Attributes>();
        mainAttributes = new java.util.jar.Attributes();
    }

    /**
     * Creates a new {@code StrictJarManifest} instance using the attributes obtained
     * from the input stream.
     *
     * @param is
     * 		{@code InputStream} to parse for attributes.
     * @throws IOException
     * 		if an IO error occurs while creating this {@code StrictJarManifest}
     */
    public StrictJarManifest(java.io.InputStream is) throws java.io.IOException {
        this();
        read(libcore.io.Streams.readFully(is));
    }

    /**
     * Creates a new {@code StrictJarManifest} instance. The new instance will have the
     * same attributes as those found in the parameter {@code StrictJarManifest}.
     *
     * @param man
     * 		{@code StrictJarManifest} instance to obtain attributes from.
     */
    @java.lang.SuppressWarnings("unchecked")
    public StrictJarManifest(android.util.jar.StrictJarManifest man) {
        mainAttributes = ((java.util.jar.Attributes) (man.mainAttributes.clone()));
        entries = ((java.util.HashMap<java.lang.String, java.util.jar.Attributes>) (((java.util.HashMap<java.lang.String, java.util.jar.Attributes>) (man.getEntries())).clone()));
    }

    StrictJarManifest(byte[] manifestBytes, boolean readChunks) throws java.io.IOException {
        this();
        if (readChunks) {
            chunks = new java.util.HashMap<java.lang.String, android.util.jar.StrictJarManifest.Chunk>();
        }
        read(manifestBytes);
    }

    /**
     * Resets the both the main attributes as well as the entry attributes
     * associated with this {@code StrictJarManifest}.
     */
    public void clear() {
        entries.clear();
        mainAttributes.clear();
    }

    /**
     * Returns the {@code Attributes} associated with the parameter entry
     * {@code name}.
     *
     * @param name
     * 		the name of the entry to obtain {@code Attributes} from.
     * @return the Attributes for the entry or {@code null} if the entry does
    not exist.
     */
    public java.util.jar.Attributes getAttributes(java.lang.String name) {
        return getEntries().get(name);
    }

    /**
     * Returns a map containing the {@code Attributes} for each entry in the
     * {@code StrictJarManifest}.
     *
     * @return the map of entry attributes.
     */
    public java.util.Map<java.lang.String, java.util.jar.Attributes> getEntries() {
        return entries;
    }

    /**
     * Returns the main {@code Attributes} of the {@code JarFile}.
     *
     * @return main {@code Attributes} associated with the source {@code JarFile}.
     */
    public java.util.jar.Attributes getMainAttributes() {
        return mainAttributes;
    }

    /**
     * Creates a copy of this {@code StrictJarManifest}. The returned {@code StrictJarManifest}
     * will equal the {@code StrictJarManifest} from which it was cloned.
     *
     * @return a copy of this instance.
     */
    @java.lang.Override
    public java.lang.Object clone() {
        return new android.util.jar.StrictJarManifest(this);
    }

    /**
     * Writes this {@code StrictJarManifest}'s name/attributes pairs to the given {@code OutputStream}.
     * The {@code MANIFEST_VERSION} or {@code SIGNATURE_VERSION} attribute must be set before
     * calling this method, or no attributes will be written.
     *
     * @throws IOException
     * 		If an error occurs writing the {@code StrictJarManifest}.
     */
    public void write(java.io.OutputStream os) throws java.io.IOException {
        android.util.jar.StrictJarManifest.write(this, os);
    }

    /**
     * Merges name/attribute pairs read from the input stream {@code is} into this manifest.
     *
     * @param is
     * 		The {@code InputStream} to read from.
     * @throws IOException
     * 		If an error occurs reading the manifest.
     */
    public void read(java.io.InputStream is) throws java.io.IOException {
        read(libcore.io.Streams.readFullyNoClose(is));
    }

    private void read(byte[] buf) throws java.io.IOException {
        if (buf.length == 0) {
            return;
        }
        android.util.jar.StrictJarManifestReader im = new android.util.jar.StrictJarManifestReader(buf, mainAttributes);
        mainEnd = im.getEndOfMainSection();
        im.readEntries(entries, chunks);
    }

    /**
     * Returns the hash code for this instance.
     *
     * @return this {@code StrictJarManifest}'s hashCode.
     */
    @java.lang.Override
    public int hashCode() {
        return mainAttributes.hashCode() ^ getEntries().hashCode();
    }

    /**
     * Determines if the receiver is equal to the parameter object. Two {@code StrictJarManifest}s are equal if they have identical main attributes as well as
     * identical entry attributes.
     *
     * @param o
     * 		the object to compare against.
     * @return {@code true} if the manifests are equal, {@code false} otherwise
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (!mainAttributes.equals(((android.util.jar.StrictJarManifest) (o)).mainAttributes)) {
            return false;
        }
        return getEntries().equals(((android.util.jar.StrictJarManifest) (o)).getEntries());
    }

    android.util.jar.StrictJarManifest.Chunk getChunk(java.lang.String name) {
        return chunks.get(name);
    }

    void removeChunks() {
        chunks = null;
    }

    int getMainAttributesEnd() {
        return mainEnd;
    }

    /**
     * Writes out the attribute information of the specified manifest to the
     * specified {@code OutputStream}
     *
     * @param manifest
     * 		the manifest to write out.
     * @param out
     * 		The {@code OutputStream} to write to.
     * @throws IOException
     * 		If an error occurs writing the {@code StrictJarManifest}.
     */
    static void write(android.util.jar.StrictJarManifest manifest, java.io.OutputStream out) throws java.io.IOException {
        java.nio.charset.CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(android.util.jar.StrictJarManifest.LINE_LENGTH_LIMIT);
        java.util.jar.Attributes.Name versionName = java.util.jar.Attributes.Name.MANIFEST_VERSION;
        java.lang.String version = manifest.mainAttributes.getValue(versionName);
        if (version == null) {
            versionName = java.util.jar.Attributes.Name.SIGNATURE_VERSION;
            version = manifest.mainAttributes.getValue(versionName);
        }
        if (version != null) {
            android.util.jar.StrictJarManifest.writeEntry(out, versionName, version, encoder, buffer);
            java.util.Iterator<?> entries = manifest.mainAttributes.keySet().iterator();
            while (entries.hasNext()) {
                java.util.jar.Attributes.Name name = ((java.util.jar.Attributes.Name) (entries.next()));
                if (!name.equals(versionName)) {
                    android.util.jar.StrictJarManifest.writeEntry(out, name, manifest.mainAttributes.getValue(name), encoder, buffer);
                }
            } 
        }
        out.write(android.util.jar.StrictJarManifest.LINE_SEPARATOR);
        java.util.Iterator<java.lang.String> i = manifest.getEntries().keySet().iterator();
        while (i.hasNext()) {
            java.lang.String key = i.next();
            android.util.jar.StrictJarManifest.writeEntry(out, NAME, key, encoder, buffer);
            java.util.jar.Attributes attributes = manifest.entries.get(key);
            java.util.Iterator<?> entries = attributes.keySet().iterator();
            while (entries.hasNext()) {
                java.util.jar.Attributes.Name name = ((java.util.jar.Attributes.Name) (entries.next()));
                android.util.jar.StrictJarManifest.writeEntry(out, name, attributes.getValue(name), encoder, buffer);
            } 
            out.write(android.util.jar.StrictJarManifest.LINE_SEPARATOR);
        } 
    }

    private static void writeEntry(java.io.OutputStream os, java.util.jar.Attributes.Name name, java.lang.String value, java.nio.charset.CharsetEncoder encoder, java.nio.ByteBuffer bBuf) throws java.io.IOException {
        java.lang.String nameString = name.toString();
        os.write(nameString.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        os.write(android.util.jar.StrictJarManifest.VALUE_SEPARATOR);
        encoder.reset();
        bBuf.clear().limit((android.util.jar.StrictJarManifest.LINE_LENGTH_LIMIT - nameString.length()) - 2);
        java.nio.CharBuffer cBuf = java.nio.CharBuffer.wrap(value);
        while (true) {
            java.nio.charset.CoderResult r = encoder.encode(cBuf, bBuf, true);
            if (java.nio.charset.CoderResult.UNDERFLOW == r) {
                r = encoder.flush(bBuf);
            }
            os.write(bBuf.array(), bBuf.arrayOffset(), bBuf.position());
            os.write(android.util.jar.StrictJarManifest.LINE_SEPARATOR);
            if (java.nio.charset.CoderResult.UNDERFLOW == r) {
                break;
            }
            os.write(' ');
            bBuf.clear().limit(android.util.jar.StrictJarManifest.LINE_LENGTH_LIMIT - 1);
        } 
    }
}

