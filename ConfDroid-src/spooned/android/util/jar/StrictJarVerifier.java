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
 * Non-public class used by {@link JarFile} and {@link JarInputStream} to manage
 * the verification of signed JARs. {@code JarFile} and {@code JarInputStream}
 * objects are expected to have a {@code JarVerifier} instance member which
 * can be used to carry out the tasks associated with verifying a signed JAR.
 * These tasks would typically include:
 * <ul>
 * <li>verification of all signed signature files
 * <li>confirmation that all signed data was signed only by the party or parties
 * specified in the signature block data
 * <li>verification that the contents of all signature files (i.e. {@code .SF}
 * files) agree with the JAR entries information found in the JAR manifest.
 * </ul>
 */
class StrictJarVerifier {
    /**
     * List of accepted digest algorithms. This list is in order from most
     * preferred to least preferred.
     */
    private static final java.lang.String[] DIGEST_ALGORITHMS = new java.lang.String[]{ "SHA-512", "SHA-384", "SHA-256", "SHA1" };

    private final java.lang.String jarName;

    private final android.util.jar.StrictJarManifest manifest;

    private final java.util.HashMap<java.lang.String, byte[]> metaEntries;

    private final int mainAttributesEnd;

    private final boolean signatureSchemeRollbackProtectionsEnforced;

    private final java.util.Hashtable<java.lang.String, java.util.HashMap<java.lang.String, java.util.jar.Attributes>> signatures = new java.util.Hashtable<java.lang.String, java.util.HashMap<java.lang.String, java.util.jar.Attributes>>(5);

    private final java.util.Hashtable<java.lang.String, java.security.cert.Certificate[]> certificates = new java.util.Hashtable<java.lang.String, java.security.cert.Certificate[]>(5);

    private final java.util.Hashtable<java.lang.String, java.security.cert.Certificate[][]> verifiedEntries = new java.util.Hashtable<java.lang.String, java.security.cert.Certificate[][]>();

    /**
     * Stores and a hash and a message digest and verifies that massage digest
     * matches the hash.
     */
    static class VerifierEntry extends java.io.OutputStream {
        private final java.lang.String name;

        private final java.security.MessageDigest digest;

        private final byte[] hash;

        private final java.security.cert.Certificate[][] certChains;

        private final java.util.Hashtable<java.lang.String, java.security.cert.Certificate[][]> verifiedEntries;

        VerifierEntry(java.lang.String name, java.security.MessageDigest digest, byte[] hash, java.security.cert.Certificate[][] certChains, java.util.Hashtable<java.lang.String, java.security.cert.Certificate[][]> verifedEntries) {
            this.name = name;
            this.digest = digest;
            this.hash = hash;
            this.certChains = certChains;
            this.verifiedEntries = verifedEntries;
        }

        /**
         * Updates a digest with one byte.
         */
        @java.lang.Override
        public void write(int value) {
            digest.update(((byte) (value)));
        }

        /**
         * Updates a digest with byte array.
         */
        @java.lang.Override
        public void write(byte[] buf, int off, int nbytes) {
            digest.update(buf, off, nbytes);
        }

        /**
         * Verifies that the digests stored in the manifest match the decrypted
         * digests from the .SF file. This indicates the validity of the
         * signing, not the integrity of the file, as its digest must be
         * calculated and verified when its contents are read.
         *
         * @throws SecurityException
         * 		if the digest value stored in the manifest does <i>not</i>
         * 		agree with the decrypted digest as recovered from the
         * 		<code>.SF</code> file.
         */
        void verify() {
            byte[] d = digest.digest();
            if (!java.security.MessageDigest.isEqual(d, libcore.io.Base64.decode(hash))) {
                throw android.util.jar.StrictJarVerifier.invalidDigest(java.util.jar.JarFile.MANIFEST_NAME, name, name);
            }
            verifiedEntries.put(name, certChains);
        }
    }

    private static java.lang.SecurityException invalidDigest(java.lang.String signatureFile, java.lang.String name, java.lang.String jarName) {
        throw new java.lang.SecurityException((((signatureFile + " has invalid digest for ") + name) + " in ") + jarName);
    }

    private static java.lang.SecurityException failedVerification(java.lang.String jarName, java.lang.String signatureFile) {
        throw new java.lang.SecurityException((jarName + " failed verification of ") + signatureFile);
    }

    private static java.lang.SecurityException failedVerification(java.lang.String jarName, java.lang.String signatureFile, java.lang.Throwable e) {
        throw new java.lang.SecurityException((jarName + " failed verification of ") + signatureFile, e);
    }

    /**
     * Constructs and returns a new instance of {@code JarVerifier}.
     *
     * @param name
     * 		the name of the JAR file being verified.
     * @param signatureSchemeRollbackProtectionsEnforced
     * 		{@code true} to enforce protections against
     * 		stripping newer signature schemes (e.g., APK Signature Scheme v2) from the file, or
     * 		{@code false} to ignore any such protections.
     */
    StrictJarVerifier(java.lang.String name, android.util.jar.StrictJarManifest manifest, java.util.HashMap<java.lang.String, byte[]> metaEntries, boolean signatureSchemeRollbackProtectionsEnforced) {
        jarName = name;
        this.manifest = manifest;
        this.metaEntries = metaEntries;
        this.mainAttributesEnd = manifest.getMainAttributesEnd();
        this.signatureSchemeRollbackProtectionsEnforced = signatureSchemeRollbackProtectionsEnforced;
    }

    /**
     * Invoked for each new JAR entry read operation from the input
     * stream. This method constructs and returns a new {@link VerifierEntry}
     * which contains the certificates used to sign the entry and its hash value
     * as specified in the JAR MANIFEST format.
     *
     * @param name
     * 		the name of an entry in a JAR file which is <b>not</b> in the
     * 		{@code META-INF} directory.
     * @return a new instance of {@link VerifierEntry} which can be used by
    callers as an {@link OutputStream}.
     */
    android.util.jar.StrictJarVerifier.VerifierEntry initEntry(java.lang.String name) {
        // If no manifest is present by the time an entry is found,
        // verification cannot occur. If no signature files have
        // been found, do not verify.
        if ((manifest == null) || signatures.isEmpty()) {
            return null;
        }
        java.util.jar.Attributes attributes = manifest.getAttributes(name);
        // entry has no digest
        if (attributes == null) {
            return null;
        }
        java.util.ArrayList<java.security.cert.Certificate[]> certChains = new java.util.ArrayList<java.security.cert.Certificate[]>();
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.util.HashMap<java.lang.String, java.util.jar.Attributes>>> it = signatures.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<java.lang.String, java.util.HashMap<java.lang.String, java.util.jar.Attributes>> entry = it.next();
            java.util.HashMap<java.lang.String, java.util.jar.Attributes> hm = entry.getValue();
            if (hm.get(name) != null) {
                // Found an entry for entry name in .SF file
                java.lang.String signatureFile = entry.getKey();
                java.security.cert.Certificate[] certChain = certificates.get(signatureFile);
                if (certChain != null) {
                    certChains.add(certChain);
                }
            }
        } 
        // entry is not signed
        if (certChains.isEmpty()) {
            return null;
        }
        java.security.cert.Certificate[][] certChainsArray = certChains.toArray(new java.security.cert.Certificate[certChains.size()][]);
        for (int i = 0; i < android.util.jar.StrictJarVerifier.DIGEST_ALGORITHMS.length; i++) {
            final java.lang.String algorithm = android.util.jar.StrictJarVerifier.DIGEST_ALGORITHMS[i];
            final java.lang.String hash = attributes.getValue(algorithm + "-Digest");
            if (hash == null) {
                continue;
            }
            byte[] hashBytes = hash.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
            try {
                return new android.util.jar.StrictJarVerifier.VerifierEntry(name, java.security.MessageDigest.getInstance(algorithm), hashBytes, certChainsArray, verifiedEntries);
            } catch (java.security.NoSuchAlgorithmException ignored) {
            }
        }
        return null;
    }

    /**
     * Add a new meta entry to the internal collection of data held on each JAR
     * entry in the {@code META-INF} directory including the manifest
     * file itself. Files associated with the signing of a JAR would also be
     * added to this collection.
     *
     * @param name
     * 		the name of the file located in the {@code META-INF}
     * 		directory.
     * @param buf
     * 		the file bytes for the file called {@code name}.
     * @see #removeMetaEntries()
     */
    void addMetaEntry(java.lang.String name, byte[] buf) {
        metaEntries.put(name.toUpperCase(java.util.Locale.US), buf);
    }

    /**
     * If the associated JAR file is signed, check on the validity of all of the
     * known signatures.
     *
     * @return {@code true} if the associated JAR is signed and an internal
    check verifies the validity of the signature(s). {@code false} if
    the associated JAR file has no entries at all in its {@code META-INF} directory. This situation is indicative of an invalid
    JAR file.
    <p>
    Will also return {@code true} if the JAR file is <i>not</i>
    signed.
     * @throws SecurityException
     * 		if the JAR file is signed and it is determined that a
     * 		signature block file contains an invalid signature for the
     * 		corresponding signature file.
     */
    synchronized boolean readCertificates() {
        if (metaEntries.isEmpty()) {
            return false;
        }
        java.util.Iterator<java.lang.String> it = metaEntries.keySet().iterator();
        while (it.hasNext()) {
            java.lang.String key = it.next();
            if ((key.endsWith(".DSA") || key.endsWith(".RSA")) || key.endsWith(".EC")) {
                verifyCertificate(key);
                it.remove();
            }
        } 
        return true;
    }

    /**
     * Verifies that the signature computed from {@code sfBytes} matches
     * that specified in {@code blockBytes} (which is a PKCS7 block). Returns
     * certificates listed in the PKCS7 block. Throws a {@code GeneralSecurityException}
     * if something goes wrong during verification.
     */
    static java.security.cert.Certificate[] verifyBytes(byte[] blockBytes, byte[] sfBytes) throws java.security.GeneralSecurityException {
        java.lang.Object obj = null;
        try {
            obj = sun.security.jca.Providers.startJarVerification();
            sun.security.pkcs.PKCS7 block = new sun.security.pkcs.PKCS7(blockBytes);
            sun.security.pkcs.SignerInfo[] verifiedSignerInfos = block.verify(sfBytes);
            if ((verifiedSignerInfos == null) || (verifiedSignerInfos.length == 0)) {
                throw new java.security.GeneralSecurityException("Failed to verify signature: no verified SignerInfos");
            }
            // Ignore any SignerInfo other than the first one, to be compatible with older Android
            // platforms which have been doing this for years. See
            // libcore/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java
            // verifySignature method of older platforms.
            sun.security.pkcs.SignerInfo verifiedSignerInfo = verifiedSignerInfos[0];
            java.util.List<java.security.cert.X509Certificate> verifiedSignerCertChain = verifiedSignerInfo.getCertificateChain(block);
            if (verifiedSignerCertChain == null) {
                // Should never happen
                throw new java.security.GeneralSecurityException("Failed to find verified SignerInfo certificate chain");
            } else
                if (verifiedSignerCertChain.isEmpty()) {
                    // Should never happen
                    throw new java.security.GeneralSecurityException("Verified SignerInfo certificate chain is emtpy");
                }

            return verifiedSignerCertChain.toArray(new java.security.cert.X509Certificate[verifiedSignerCertChain.size()]);
        } catch (java.io.IOException e) {
            throw new java.security.GeneralSecurityException("IO exception verifying jar cert", e);
        } finally {
            sun.security.jca.Providers.stopJarVerification(obj);
        }
    }

    /**
     *
     *
     * @param certFile
     * 		
     */
    private void verifyCertificate(java.lang.String certFile) {
        // Found Digital Sig, .SF should already have been read
        java.lang.String signatureFile = certFile.substring(0, certFile.lastIndexOf('.')) + ".SF";
        byte[] sfBytes = metaEntries.get(signatureFile);
        if (sfBytes == null) {
            return;
        }
        byte[] manifestBytes = metaEntries.get(java.util.jar.JarFile.MANIFEST_NAME);
        // Manifest entry is required for any verifications.
        if (manifestBytes == null) {
            return;
        }
        byte[] sBlockBytes = metaEntries.get(certFile);
        try {
            java.security.cert.Certificate[] signerCertChain = android.util.jar.StrictJarVerifier.verifyBytes(sBlockBytes, sfBytes);
            if (signerCertChain != null) {
                certificates.put(signatureFile, signerCertChain);
            }
        } catch (java.security.GeneralSecurityException e) {
            throw android.util.jar.StrictJarVerifier.failedVerification(jarName, signatureFile, e);
        }
        // Verify manifest hash in .sf file
        java.util.jar.Attributes attributes = new java.util.jar.Attributes();
        java.util.HashMap<java.lang.String, java.util.jar.Attributes> entries = new java.util.HashMap<java.lang.String, java.util.jar.Attributes>();
        try {
            android.util.jar.StrictJarManifestReader im = new android.util.jar.StrictJarManifestReader(sfBytes, attributes);
            im.readEntries(entries, null);
        } catch (java.io.IOException e) {
            return;
        }
        // If requested, check whether APK Signature Scheme v2 signature was stripped.
        if (signatureSchemeRollbackProtectionsEnforced) {
            java.lang.String apkSignatureSchemeIdList = attributes.getValue(android.util.apk.ApkSignatureSchemeV2Verifier.SF_ATTRIBUTE_ANDROID_APK_SIGNED_NAME);
            if (apkSignatureSchemeIdList != null) {
                // This field contains a comma-separated list of APK signature scheme IDs which
                // were used to sign this APK. If an ID is known to us, it means signatures of that
                // scheme were stripped from the APK because otherwise we wouldn't have fallen back
                // to verifying the APK using the JAR signature scheme.
                boolean v2SignatureGenerated = false;
                java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(apkSignatureSchemeIdList, ",");
                while (tokenizer.hasMoreTokens()) {
                    java.lang.String idText = tokenizer.nextToken().trim();
                    if (idText.isEmpty()) {
                        continue;
                    }
                    int id;
                    try {
                        id = java.lang.Integer.parseInt(idText);
                    } catch (java.lang.Exception ignored) {
                        continue;
                    }
                    if (id == android.util.apk.ApkSignatureSchemeV2Verifier.SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID) {
                        // This APK was supposed to be signed with APK Signature Scheme v2 but no
                        // such signature was found.
                        v2SignatureGenerated = true;
                        break;
                    }
                } 
                if (v2SignatureGenerated) {
                    throw new java.lang.SecurityException((((signatureFile + " indicates ") + jarName) + " is signed using APK Signature Scheme v2, but no such signature was") + " found. Signature stripped?");
                }
            }
        }
        // Do we actually have any signatures to look at?
        if (attributes.get(java.util.jar.Attributes.Name.SIGNATURE_VERSION) == null) {
            return;
        }
        boolean createdBySigntool = false;
        java.lang.String createdBy = attributes.getValue("Created-By");
        if (createdBy != null) {
            createdBySigntool = createdBy.indexOf("signtool") != (-1);
        }
        // Use .SF to verify the mainAttributes of the manifest
        // If there is no -Digest-Manifest-Main-Attributes entry in .SF
        // file, such as those created before java 1.5, then we ignore
        // such verification.
        if ((mainAttributesEnd > 0) && (!createdBySigntool)) {
            java.lang.String digestAttribute = "-Digest-Manifest-Main-Attributes";
            if (!verify(attributes, digestAttribute, manifestBytes, 0, mainAttributesEnd, false, true)) {
                throw android.util.jar.StrictJarVerifier.failedVerification(jarName, signatureFile);
            }
        }
        // Use .SF to verify the whole manifest.
        java.lang.String digestAttribute = (createdBySigntool) ? "-Digest" : "-Digest-Manifest";
        if (!verify(attributes, digestAttribute, manifestBytes, 0, manifestBytes.length, false, false)) {
            java.util.Iterator<java.util.Map.Entry<java.lang.String, java.util.jar.Attributes>> it = entries.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<java.lang.String, java.util.jar.Attributes> entry = it.next();
                android.util.jar.StrictJarManifest.Chunk chunk = manifest.getChunk(entry.getKey());
                if (chunk == null) {
                    return;
                }
                if (!verify(entry.getValue(), "-Digest", manifestBytes, chunk.start, chunk.end, createdBySigntool, false)) {
                    throw android.util.jar.StrictJarVerifier.invalidDigest(signatureFile, entry.getKey(), jarName);
                }
            } 
        }
        metaEntries.put(signatureFile, null);
        signatures.put(signatureFile, entries);
    }

    /**
     * Returns a <code>boolean</code> indication of whether or not the
     * associated jar file is signed.
     *
     * @return {@code true} if the JAR is signed, {@code false}
    otherwise.
     */
    boolean isSignedJar() {
        return certificates.size() > 0;
    }

    private boolean verify(java.util.jar.Attributes attributes, java.lang.String entry, byte[] data, int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        for (int i = 0; i < android.util.jar.StrictJarVerifier.DIGEST_ALGORITHMS.length; i++) {
            java.lang.String algorithm = android.util.jar.StrictJarVerifier.DIGEST_ALGORITHMS[i];
            java.lang.String hash = attributes.getValue(algorithm + entry);
            if (hash == null) {
                continue;
            }
            java.security.MessageDigest md;
            try {
                md = java.security.MessageDigest.getInstance(algorithm);
            } catch (java.security.NoSuchAlgorithmException e) {
                continue;
            }
            if ((ignoreSecondEndline && (data[end - 1] == '\n')) && (data[end - 2] == '\n')) {
                md.update(data, start, (end - 1) - start);
            } else {
                md.update(data, start, end - start);
            }
            byte[] b = md.digest();
            byte[] hashBytes = hash.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
            return java.security.MessageDigest.isEqual(b, libcore.io.Base64.decode(hashBytes));
        }
        return ignorable;
    }

    /**
     * Returns all of the {@link java.security.cert.Certificate} chains that
     * were used to verify the signature on the JAR entry called
     * {@code name}. Callers must not modify the returned arrays.
     *
     * @param name
     * 		the name of a JAR entry.
     * @return an array of {@link java.security.cert.Certificate} chains.
     */
    java.security.cert.Certificate[][] getCertificateChains(java.lang.String name) {
        return verifiedEntries.get(name);
    }

    /**
     * Remove all entries from the internal collection of data held about each
     * JAR entry in the {@code META-INF} directory.
     */
    void removeMetaEntries() {
        metaEntries.clear();
    }
}

