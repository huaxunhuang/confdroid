/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.util.apk;


/**
 * APK Signature Scheme v2 verifier.
 *
 * @unknown for internal use only.
 */
public class ApkSignatureSchemeV2Verifier {
    /**
     * {@code .SF} file header section attribute indicating that the APK is signed not just with
     * JAR signature scheme but also with APK Signature Scheme v2 or newer. This attribute
     * facilitates v2 signature stripping detection.
     *
     * <p>The attribute contains a comma-separated set of signature scheme IDs.
     */
    public static final java.lang.String SF_ATTRIBUTE_ANDROID_APK_SIGNED_NAME = "X-Android-APK-Signed";

    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 2;

    /**
     * Returns {@code true} if the provided APK contains an APK Signature Scheme V2 signature.
     *
     * <p><b>NOTE: This method does not verify the signature.</b>
     */
    public static boolean hasSignature(java.lang.String apkFile) throws java.io.IOException {
        try (java.io.RandomAccessFile apk = new java.io.RandomAccessFile(apkFile, "r")) {
            android.util.apk.ApkSignatureSchemeV2Verifier.findSignature(apk);
            return true;
        } catch (android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException e) {
            return false;
        }
    }

    /**
     * Verifies APK Signature Scheme v2 signatures of the provided APK and returns the certificates
     * associated with each signer.
     *
     * @throws SignatureNotFoundException
     * 		if the APK is not signed using APK Signature Scheme v2.
     * @throws SecurityException
     * 		if a APK Signature Scheme v2 signature of this APK does not verify.
     * @throws IOException
     * 		if an I/O error occurs while reading the APK file.
     */
    public static java.security.cert.X509Certificate[][] verify(java.lang.String apkFile) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException, java.io.IOException, java.lang.SecurityException {
        try (java.io.RandomAccessFile apk = new java.io.RandomAccessFile(apkFile, "r")) {
            return android.util.apk.ApkSignatureSchemeV2Verifier.verify(apk);
        }
    }

    /**
     * Verifies APK Signature Scheme v2 signatures of the provided APK and returns the certificates
     * associated with each signer.
     *
     * @throws SignatureNotFoundException
     * 		if the APK is not signed using APK Signature Scheme v2.
     * @throws SecurityException
     * 		if an APK Signature Scheme v2 signature of this APK does not
     * 		verify.
     * @throws IOException
     * 		if an I/O error occurs while reading the APK file.
     */
    private static java.security.cert.X509Certificate[][] verify(java.io.RandomAccessFile apk) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException, java.io.IOException, java.lang.SecurityException {
        android.util.apk.ApkSignatureSchemeV2Verifier.SignatureInfo signatureInfo = android.util.apk.ApkSignatureSchemeV2Verifier.findSignature(apk);
        return android.util.apk.ApkSignatureSchemeV2Verifier.verify(apk.getFD(), signatureInfo);
    }

    /**
     * APK Signature Scheme v2 block and additional information relevant to verifying the signatures
     * contained in the block against the file.
     */
    private static class SignatureInfo {
        /**
         * Contents of APK Signature Scheme v2 block.
         */
        private final java.nio.ByteBuffer signatureBlock;

        /**
         * Position of the APK Signing Block in the file.
         */
        private final long apkSigningBlockOffset;

        /**
         * Position of the ZIP Central Directory in the file.
         */
        private final long centralDirOffset;

        /**
         * Position of the ZIP End of Central Directory (EoCD) in the file.
         */
        private final long eocdOffset;

        /**
         * Contents of ZIP End of Central Directory (EoCD) of the file.
         */
        private final java.nio.ByteBuffer eocd;

        private SignatureInfo(java.nio.ByteBuffer signatureBlock, long apkSigningBlockOffset, long centralDirOffset, long eocdOffset, java.nio.ByteBuffer eocd) {
            this.signatureBlock = signatureBlock;
            this.apkSigningBlockOffset = apkSigningBlockOffset;
            this.centralDirOffset = centralDirOffset;
            this.eocdOffset = eocdOffset;
            this.eocd = eocd;
        }
    }

    /**
     * Returns the APK Signature Scheme v2 block contained in the provided APK file and the
     * additional information relevant for verifying the block against the file.
     *
     * @throws SignatureNotFoundException
     * 		if the APK is not signed using APK Signature Scheme v2.
     * @throws IOException
     * 		if an I/O error occurs while reading the APK file.
     */
    private static android.util.apk.ApkSignatureSchemeV2Verifier.SignatureInfo findSignature(java.io.RandomAccessFile apk) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException, java.io.IOException {
        // Find the ZIP End of Central Directory (EoCD) record.
        android.util.Pair<java.nio.ByteBuffer, java.lang.Long> eocdAndOffsetInFile = android.util.apk.ApkSignatureSchemeV2Verifier.getEocd(apk);
        java.nio.ByteBuffer eocd = eocdAndOffsetInFile.first;
        long eocdOffset = eocdAndOffsetInFile.second;
        if (android.util.apk.ZipUtils.isZip64EndOfCentralDirectoryLocatorPresent(apk, eocdOffset)) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("ZIP64 APK not supported");
        }
        // Find the APK Signing Block. The block immediately precedes the Central Directory.
        long centralDirOffset = android.util.apk.ApkSignatureSchemeV2Verifier.getCentralDirOffset(eocd, eocdOffset);
        android.util.Pair<java.nio.ByteBuffer, java.lang.Long> apkSigningBlockAndOffsetInFile = android.util.apk.ApkSignatureSchemeV2Verifier.findApkSigningBlock(apk, centralDirOffset);
        java.nio.ByteBuffer apkSigningBlock = apkSigningBlockAndOffsetInFile.first;
        long apkSigningBlockOffset = apkSigningBlockAndOffsetInFile.second;
        // Find the APK Signature Scheme v2 Block inside the APK Signing Block.
        java.nio.ByteBuffer apkSignatureSchemeV2Block = android.util.apk.ApkSignatureSchemeV2Verifier.findApkSignatureSchemeV2Block(apkSigningBlock);
        return new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureInfo(apkSignatureSchemeV2Block, apkSigningBlockOffset, centralDirOffset, eocdOffset, eocd);
    }

    /**
     * Verifies the contents of the provided APK file against the provided APK Signature Scheme v2
     * Block.
     *
     * @param signatureInfo
     * 		APK Signature Scheme v2 Block and information relevant for verifying it
     * 		against the APK file.
     */
    private static java.security.cert.X509Certificate[][] verify(java.io.FileDescriptor apkFileDescriptor, android.util.apk.ApkSignatureSchemeV2Verifier.SignatureInfo signatureInfo) throws java.lang.SecurityException {
        int signerCount = 0;
        java.util.Map<java.lang.Integer, byte[]> contentDigests = new android.util.ArrayMap<>();
        java.util.List<java.security.cert.X509Certificate[]> signerCerts = new java.util.ArrayList<>();
        java.security.cert.CertificateFactory certFactory;
        try {
            certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        } catch (java.security.cert.CertificateException e) {
            throw new java.lang.RuntimeException("Failed to obtain X.509 CertificateFactory", e);
        }
        java.nio.ByteBuffer signers;
        try {
            signers = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signatureInfo.signatureBlock);
        } catch (java.io.IOException e) {
            throw new java.lang.SecurityException("Failed to read list of signers", e);
        }
        while (signers.hasRemaining()) {
            signerCount++;
            try {
                java.nio.ByteBuffer signer = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signers);
                java.security.cert.X509Certificate[] certs = android.util.apk.ApkSignatureSchemeV2Verifier.verifySigner(signer, contentDigests, certFactory);
                signerCerts.add(certs);
            } catch (java.io.IOException | java.nio.BufferUnderflowException | java.lang.SecurityException e) {
                throw new java.lang.SecurityException(("Failed to parse/verify signer #" + signerCount) + " block", e);
            }
        } 
        if (signerCount < 1) {
            throw new java.lang.SecurityException("No signers found");
        }
        if (contentDigests.isEmpty()) {
            throw new java.lang.SecurityException("No content digests found");
        }
        android.util.apk.ApkSignatureSchemeV2Verifier.verifyIntegrity(contentDigests, apkFileDescriptor, signatureInfo.apkSigningBlockOffset, signatureInfo.centralDirOffset, signatureInfo.eocdOffset, signatureInfo.eocd);
        return signerCerts.toArray(new java.security.cert.X509Certificate[signerCerts.size()][]);
    }

    private static java.security.cert.X509Certificate[] verifySigner(java.nio.ByteBuffer signerBlock, java.util.Map<java.lang.Integer, byte[]> contentDigests, java.security.cert.CertificateFactory certFactory) throws java.io.IOException, java.lang.SecurityException {
        java.nio.ByteBuffer signedData = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signerBlock);
        java.nio.ByteBuffer signatures = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signerBlock);
        byte[] publicKeyBytes = android.util.apk.ApkSignatureSchemeV2Verifier.readLengthPrefixedByteArray(signerBlock);
        int signatureCount = 0;
        int bestSigAlgorithm = -1;
        byte[] bestSigAlgorithmSignatureBytes = null;
        java.util.List<java.lang.Integer> signaturesSigAlgorithms = new java.util.ArrayList<>();
        while (signatures.hasRemaining()) {
            signatureCount++;
            try {
                java.nio.ByteBuffer signature = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signatures);
                if (signature.remaining() < 8) {
                    throw new java.lang.SecurityException("Signature record too short");
                }
                int sigAlgorithm = signature.getInt();
                signaturesSigAlgorithms.add(sigAlgorithm);
                if (!android.util.apk.ApkSignatureSchemeV2Verifier.isSupportedSignatureAlgorithm(sigAlgorithm)) {
                    continue;
                }
                if ((bestSigAlgorithm == (-1)) || (android.util.apk.ApkSignatureSchemeV2Verifier.compareSignatureAlgorithm(sigAlgorithm, bestSigAlgorithm) > 0)) {
                    bestSigAlgorithm = sigAlgorithm;
                    bestSigAlgorithmSignatureBytes = android.util.apk.ApkSignatureSchemeV2Verifier.readLengthPrefixedByteArray(signature);
                }
            } catch (java.io.IOException | java.nio.BufferUnderflowException e) {
                throw new java.lang.SecurityException("Failed to parse signature record #" + signatureCount, e);
            }
        } 
        if (bestSigAlgorithm == (-1)) {
            if (signatureCount == 0) {
                throw new java.lang.SecurityException("No signatures found");
            } else {
                throw new java.lang.SecurityException("No supported signatures found");
            }
        }
        java.lang.String keyAlgorithm = android.util.apk.ApkSignatureSchemeV2Verifier.getSignatureAlgorithmJcaKeyAlgorithm(bestSigAlgorithm);
        android.util.Pair<java.lang.String, ? extends java.security.spec.AlgorithmParameterSpec> signatureAlgorithmParams = android.util.apk.ApkSignatureSchemeV2Verifier.getSignatureAlgorithmJcaSignatureAlgorithm(bestSigAlgorithm);
        java.lang.String jcaSignatureAlgorithm = signatureAlgorithmParams.first;
        java.security.spec.AlgorithmParameterSpec jcaSignatureAlgorithmParams = signatureAlgorithmParams.second;
        boolean sigVerified;
        try {
            java.security.PublicKey publicKey = java.security.KeyFactory.getInstance(keyAlgorithm).generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
            java.security.Signature sig = java.security.Signature.getInstance(jcaSignatureAlgorithm);
            sig.initVerify(publicKey);
            if (jcaSignatureAlgorithmParams != null) {
                sig.setParameter(jcaSignatureAlgorithmParams);
            }
            sig.update(signedData);
            sigVerified = sig.verify(bestSigAlgorithmSignatureBytes);
        } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException | java.security.InvalidKeyException | java.security.InvalidAlgorithmParameterException | java.security.SignatureException e) {
            throw new java.lang.SecurityException(("Failed to verify " + jcaSignatureAlgorithm) + " signature", e);
        }
        if (!sigVerified) {
            throw new java.lang.SecurityException(jcaSignatureAlgorithm + " signature did not verify");
        }
        // Signature over signedData has verified.
        byte[] contentDigest = null;
        signedData.clear();
        java.nio.ByteBuffer digests = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signedData);
        java.util.List<java.lang.Integer> digestsSigAlgorithms = new java.util.ArrayList<>();
        int digestCount = 0;
        while (digests.hasRemaining()) {
            digestCount++;
            try {
                java.nio.ByteBuffer digest = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(digests);
                if (digest.remaining() < 8) {
                    throw new java.io.IOException("Record too short");
                }
                int sigAlgorithm = digest.getInt();
                digestsSigAlgorithms.add(sigAlgorithm);
                if (sigAlgorithm == bestSigAlgorithm) {
                    contentDigest = android.util.apk.ApkSignatureSchemeV2Verifier.readLengthPrefixedByteArray(digest);
                }
            } catch (java.io.IOException | java.nio.BufferUnderflowException e) {
                throw new java.io.IOException("Failed to parse digest record #" + digestCount, e);
            }
        } 
        if (!signaturesSigAlgorithms.equals(digestsSigAlgorithms)) {
            throw new java.lang.SecurityException("Signature algorithms don't match between digests and signatures records");
        }
        int digestAlgorithm = android.util.apk.ApkSignatureSchemeV2Verifier.getSignatureAlgorithmContentDigestAlgorithm(bestSigAlgorithm);
        byte[] previousSignerDigest = contentDigests.put(digestAlgorithm, contentDigest);
        if ((previousSignerDigest != null) && (!java.security.MessageDigest.isEqual(previousSignerDigest, contentDigest))) {
            throw new java.lang.SecurityException(android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm) + " contents digest does not match the digest specified by a preceding signer");
        }
        java.nio.ByteBuffer certificates = android.util.apk.ApkSignatureSchemeV2Verifier.getLengthPrefixedSlice(signedData);
        java.util.List<java.security.cert.X509Certificate> certs = new java.util.ArrayList<>();
        int certificateCount = 0;
        while (certificates.hasRemaining()) {
            certificateCount++;
            byte[] encodedCert = android.util.apk.ApkSignatureSchemeV2Verifier.readLengthPrefixedByteArray(certificates);
            java.security.cert.X509Certificate certificate;
            try {
                certificate = ((java.security.cert.X509Certificate) (certFactory.generateCertificate(new java.io.ByteArrayInputStream(encodedCert))));
            } catch (java.security.cert.CertificateException e) {
                throw new java.lang.SecurityException("Failed to decode certificate #" + certificateCount, e);
            }
            certificate = new android.util.apk.ApkSignatureSchemeV2Verifier.VerbatimX509Certificate(certificate, encodedCert);
            certs.add(certificate);
        } 
        if (certs.isEmpty()) {
            throw new java.lang.SecurityException("No certificates listed");
        }
        java.security.cert.X509Certificate mainCertificate = certs.get(0);
        byte[] certificatePublicKeyBytes = mainCertificate.getPublicKey().getEncoded();
        if (!java.util.Arrays.equals(publicKeyBytes, certificatePublicKeyBytes)) {
            throw new java.lang.SecurityException("Public key mismatch between certificate and signature record");
        }
        return certs.toArray(new java.security.cert.X509Certificate[certs.size()]);
    }

    private static void verifyIntegrity(java.util.Map<java.lang.Integer, byte[]> expectedDigests, java.io.FileDescriptor apkFileDescriptor, long apkSigningBlockOffset, long centralDirOffset, long eocdOffset, java.nio.ByteBuffer eocdBuf) throws java.lang.SecurityException {
        if (expectedDigests.isEmpty()) {
            throw new java.lang.SecurityException("No digests provided");
        }
        // We need to verify the integrity of the following three sections of the file:
        // 1. Everything up to the start of the APK Signing Block.
        // 2. ZIP Central Directory.
        // 3. ZIP End of Central Directory (EoCD).
        // Each of these sections is represented as a separate DataSource instance below.
        // To handle large APKs, these sections are read in 1 MB chunks using memory-mapped I/O to
        // avoid wasting physical memory. In most APK verification scenarios, the contents of the
        // APK are already there in the OS's page cache and thus mmap does not use additional
        // physical memory.
        android.util.apk.ApkSignatureSchemeV2Verifier.DataSource beforeApkSigningBlock = new android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource(apkFileDescriptor, 0, apkSigningBlockOffset);
        android.util.apk.ApkSignatureSchemeV2Verifier.DataSource centralDir = new android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource(apkFileDescriptor, centralDirOffset, eocdOffset - centralDirOffset);
        // For the purposes of integrity verification, ZIP End of Central Directory's field Start of
        // Central Directory must be considered to point to the offset of the APK Signing Block.
        eocdBuf = eocdBuf.duplicate();
        eocdBuf.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        android.util.apk.ZipUtils.setZipEocdCentralDirectoryOffset(eocdBuf, apkSigningBlockOffset);
        android.util.apk.ApkSignatureSchemeV2Verifier.DataSource eocd = new android.util.apk.ApkSignatureSchemeV2Verifier.ByteBufferDataSource(eocdBuf);
        int[] digestAlgorithms = new int[expectedDigests.size()];
        int digestAlgorithmCount = 0;
        for (int digestAlgorithm : expectedDigests.keySet()) {
            digestAlgorithms[digestAlgorithmCount] = digestAlgorithm;
            digestAlgorithmCount++;
        }
        byte[][] actualDigests;
        try {
            actualDigests = android.util.apk.ApkSignatureSchemeV2Verifier.computeContentDigests(digestAlgorithms, new android.util.apk.ApkSignatureSchemeV2Verifier.DataSource[]{ beforeApkSigningBlock, centralDir, eocd });
        } catch (java.security.DigestException e) {
            throw new java.lang.SecurityException("Failed to compute digest(s) of contents", e);
        }
        for (int i = 0; i < digestAlgorithms.length; i++) {
            int digestAlgorithm = digestAlgorithms[i];
            byte[] expectedDigest = expectedDigests.get(digestAlgorithm);
            byte[] actualDigest = actualDigests[i];
            if (!java.security.MessageDigest.isEqual(expectedDigest, actualDigest)) {
                throw new java.lang.SecurityException(android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm) + " digest of contents did not verify");
            }
        }
    }

    private static byte[][] computeContentDigests(int[] digestAlgorithms, android.util.apk.ApkSignatureSchemeV2Verifier.DataSource[] contents) throws java.security.DigestException {
        // For each digest algorithm the result is computed as follows:
        // 1. Each segment of contents is split into consecutive chunks of 1 MB in size.
        // The final chunk will be shorter iff the length of segment is not a multiple of 1 MB.
        // No chunks are produced for empty (zero length) segments.
        // 2. The digest of each chunk is computed over the concatenation of byte 0xa5, the chunk's
        // length in bytes (uint32 little-endian) and the chunk's contents.
        // 3. The output digest is computed over the concatenation of the byte 0x5a, the number of
        // chunks (uint32 little-endian) and the concatenation of digests of chunks of all
        // segments in-order.
        long totalChunkCountLong = 0;
        for (android.util.apk.ApkSignatureSchemeV2Verifier.DataSource input : contents) {
            totalChunkCountLong += android.util.apk.ApkSignatureSchemeV2Verifier.getChunkCount(input.size());
        }
        if (totalChunkCountLong >= (java.lang.Integer.MAX_VALUE / 1024)) {
            throw new java.security.DigestException("Too many chunks: " + totalChunkCountLong);
        }
        int totalChunkCount = ((int) (totalChunkCountLong));
        byte[][] digestsOfChunks = new byte[digestAlgorithms.length][];
        for (int i = 0; i < digestAlgorithms.length; i++) {
            int digestAlgorithm = digestAlgorithms[i];
            int digestOutputSizeBytes = android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmOutputSizeBytes(digestAlgorithm);
            byte[] concatenationOfChunkCountAndChunkDigests = new byte[5 + (totalChunkCount * digestOutputSizeBytes)];
            concatenationOfChunkCountAndChunkDigests[0] = 0x5a;
            android.util.apk.ApkSignatureSchemeV2Verifier.setUnsignedInt32LittleEndian(totalChunkCount, concatenationOfChunkCountAndChunkDigests, 1);
            digestsOfChunks[i] = concatenationOfChunkCountAndChunkDigests;
        }
        byte[] chunkContentPrefix = new byte[5];
        chunkContentPrefix[0] = ((byte) (0xa5));
        int chunkIndex = 0;
        java.security.MessageDigest[] mds = new java.security.MessageDigest[digestAlgorithms.length];
        for (int i = 0; i < digestAlgorithms.length; i++) {
            java.lang.String jcaAlgorithmName = android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithms[i]);
            try {
                mds[i] = java.security.MessageDigest.getInstance(jcaAlgorithmName);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.RuntimeException(jcaAlgorithmName + " digest not supported", e);
            }
        }
        // TODO: Compute digests of chunks in parallel when beneficial. This requires some research
        // into how to parallelize (if at all) based on the capabilities of the hardware on which
        // this code is running and based on the size of input.
        int dataSourceIndex = 0;
        for (android.util.apk.ApkSignatureSchemeV2Verifier.DataSource input : contents) {
            long inputOffset = 0;
            long inputRemaining = input.size();
            while (inputRemaining > 0) {
                int chunkSize = ((int) (java.lang.Math.min(inputRemaining, android.util.apk.ApkSignatureSchemeV2Verifier.CHUNK_SIZE_BYTES)));
                android.util.apk.ApkSignatureSchemeV2Verifier.setUnsignedInt32LittleEndian(chunkSize, chunkContentPrefix, 1);
                for (int i = 0; i < mds.length; i++) {
                    mds[i].update(chunkContentPrefix);
                }
                try {
                    input.feedIntoMessageDigests(mds, inputOffset, chunkSize);
                } catch (java.io.IOException e) {
                    throw new java.security.DigestException((("Failed to digest chunk #" + chunkIndex) + " of section #") + dataSourceIndex, e);
                }
                for (int i = 0; i < digestAlgorithms.length; i++) {
                    int digestAlgorithm = digestAlgorithms[i];
                    byte[] concatenationOfChunkCountAndChunkDigests = digestsOfChunks[i];
                    int expectedDigestSizeBytes = android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmOutputSizeBytes(digestAlgorithm);
                    java.security.MessageDigest md = mds[i];
                    int actualDigestSizeBytes = md.digest(concatenationOfChunkCountAndChunkDigests, 5 + (chunkIndex * expectedDigestSizeBytes), expectedDigestSizeBytes);
                    if (actualDigestSizeBytes != expectedDigestSizeBytes) {
                        throw new java.lang.RuntimeException((("Unexpected output size of " + md.getAlgorithm()) + " digest: ") + actualDigestSizeBytes);
                    }
                }
                inputOffset += chunkSize;
                inputRemaining -= chunkSize;
                chunkIndex++;
            } 
            dataSourceIndex++;
        }
        byte[][] result = new byte[digestAlgorithms.length][];
        for (int i = 0; i < digestAlgorithms.length; i++) {
            int digestAlgorithm = digestAlgorithms[i];
            byte[] input = digestsOfChunks[i];
            java.lang.String jcaAlgorithmName = android.util.apk.ApkSignatureSchemeV2Verifier.getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm);
            java.security.MessageDigest md;
            try {
                md = java.security.MessageDigest.getInstance(jcaAlgorithmName);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.RuntimeException(jcaAlgorithmName + " digest not supported", e);
            }
            byte[] output = md.digest(input);
            result[i] = output;
        }
        return result;
    }

    /**
     * Returns the ZIP End of Central Directory (EoCD) and its offset in the file.
     *
     * @throws IOException
     * 		if an I/O error occurs while reading the file.
     * @throws SignatureNotFoundException
     * 		if the EoCD could not be found.
     */
    private static android.util.Pair<java.nio.ByteBuffer, java.lang.Long> getEocd(java.io.RandomAccessFile apk) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException, java.io.IOException {
        android.util.Pair<java.nio.ByteBuffer, java.lang.Long> eocdAndOffsetInFile = android.util.apk.ZipUtils.findZipEndOfCentralDirectoryRecord(apk);
        if (eocdAndOffsetInFile == null) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("Not an APK file: ZIP End of Central Directory record not found");
        }
        return eocdAndOffsetInFile;
    }

    private static long getCentralDirOffset(java.nio.ByteBuffer eocd, long eocdOffset) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException {
        // Look up the offset of ZIP Central Directory.
        long centralDirOffset = android.util.apk.ZipUtils.getZipEocdCentralDirectoryOffset(eocd);
        if (centralDirOffset >= eocdOffset) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException((("ZIP Central Directory offset out of range: " + centralDirOffset) + ". ZIP End of Central Directory offset: ") + eocdOffset);
        }
        long centralDirSize = android.util.apk.ZipUtils.getZipEocdCentralDirectorySizeBytes(eocd);
        if ((centralDirOffset + centralDirSize) != eocdOffset) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("ZIP Central Directory is not immediately followed by End of Central" + " Directory");
        }
        return centralDirOffset;
    }

    private static final long getChunkCount(long inputSizeBytes) {
        return ((inputSizeBytes + android.util.apk.ApkSignatureSchemeV2Verifier.CHUNK_SIZE_BYTES) - 1) / android.util.apk.ApkSignatureSchemeV2Verifier.CHUNK_SIZE_BYTES;
    }

    private static final int CHUNK_SIZE_BYTES = 1024 * 1024;

    private static final int SIGNATURE_RSA_PSS_WITH_SHA256 = 0x101;

    private static final int SIGNATURE_RSA_PSS_WITH_SHA512 = 0x102;

    private static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 = 0x103;

    private static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 = 0x104;

    private static final int SIGNATURE_ECDSA_WITH_SHA256 = 0x201;

    private static final int SIGNATURE_ECDSA_WITH_SHA512 = 0x202;

    private static final int SIGNATURE_DSA_WITH_SHA256 = 0x301;

    private static final int CONTENT_DIGEST_CHUNKED_SHA256 = 1;

    private static final int CONTENT_DIGEST_CHUNKED_SHA512 = 2;

    private static boolean isSupportedSignatureAlgorithm(int sigAlgorithm) {
        switch (sigAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_DSA_WITH_SHA256 :
                return true;
            default :
                return false;
        }
    }

    private static int compareSignatureAlgorithm(int sigAlgorithm1, int sigAlgorithm2) {
        int digestAlgorithm1 = android.util.apk.ApkSignatureSchemeV2Verifier.getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm1);
        int digestAlgorithm2 = android.util.apk.ApkSignatureSchemeV2Verifier.getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm2);
        return android.util.apk.ApkSignatureSchemeV2Verifier.compareContentDigestAlgorithm(digestAlgorithm1, digestAlgorithm2);
    }

    private static int compareContentDigestAlgorithm(int digestAlgorithm1, int digestAlgorithm2) {
        switch (digestAlgorithm1) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256 :
                switch (digestAlgorithm2) {
                    case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256 :
                        return 0;
                    case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512 :
                        return -1;
                    default :
                        throw new java.lang.IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512 :
                switch (digestAlgorithm2) {
                    case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256 :
                        return 1;
                    case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512 :
                        return 0;
                    default :
                        throw new java.lang.IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            default :
                throw new java.lang.IllegalArgumentException("Unknown digestAlgorithm1: " + digestAlgorithm1);
        }
    }

    private static int getSignatureAlgorithmContentDigestAlgorithm(int sigAlgorithm) {
        switch (sigAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_DSA_WITH_SHA256 :
                return android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256;
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA512 :
                return android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512;
            default :
                throw new java.lang.IllegalArgumentException("Unknown signature algorithm: 0x" + java.lang.Long.toHexString(sigAlgorithm & 0xffffffff));
        }
    }

    private static java.lang.String getContentDigestAlgorithmJcaDigestAlgorithm(int digestAlgorithm) {
        switch (digestAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256 :
                return "SHA-256";
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512 :
                return "SHA-512";
            default :
                throw new java.lang.IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
        }
    }

    private static int getContentDigestAlgorithmOutputSizeBytes(int digestAlgorithm) {
        switch (digestAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA256 :
                return 256 / 8;
            case android.util.apk.ApkSignatureSchemeV2Verifier.CONTENT_DIGEST_CHUNKED_SHA512 :
                return 512 / 8;
            default :
                throw new java.lang.IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
        }
    }

    private static java.lang.String getSignatureAlgorithmJcaKeyAlgorithm(int sigAlgorithm) {
        switch (sigAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA512 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 :
                return "RSA";
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA256 :
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA512 :
                return "EC";
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_DSA_WITH_SHA256 :
                return "DSA";
            default :
                throw new java.lang.IllegalArgumentException("Unknown signature algorithm: 0x" + java.lang.Long.toHexString(sigAlgorithm & 0xffffffff));
        }
    }

    private static android.util.Pair<java.lang.String, ? extends java.security.spec.AlgorithmParameterSpec> getSignatureAlgorithmJcaSignatureAlgorithm(int sigAlgorithm) {
        switch (sigAlgorithm) {
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA256 :
                return android.util.Pair.create("SHA256withRSA/PSS", new java.security.spec.PSSParameterSpec("SHA-256", "MGF1", java.security.spec.MGF1ParameterSpec.SHA256, 256 / 8, 1));
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PSS_WITH_SHA512 :
                return android.util.Pair.create("SHA512withRSA/PSS", new java.security.spec.PSSParameterSpec("SHA-512", "MGF1", java.security.spec.MGF1ParameterSpec.SHA512, 512 / 8, 1));
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 :
                return android.util.Pair.create("SHA256withRSA", null);
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 :
                return android.util.Pair.create("SHA512withRSA", null);
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA256 :
                return android.util.Pair.create("SHA256withECDSA", null);
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_ECDSA_WITH_SHA512 :
                return android.util.Pair.create("SHA512withECDSA", null);
            case android.util.apk.ApkSignatureSchemeV2Verifier.SIGNATURE_DSA_WITH_SHA256 :
                return android.util.Pair.create("SHA256withDSA", null);
            default :
                throw new java.lang.IllegalArgumentException("Unknown signature algorithm: 0x" + java.lang.Long.toHexString(sigAlgorithm & 0xffffffff));
        }
    }

    /**
     * Returns new byte buffer whose content is a shared subsequence of this buffer's content
     * between the specified start (inclusive) and end (exclusive) positions. As opposed to
     * {@link ByteBuffer#slice()}, the returned buffer's byte order is the same as the source
     * buffer's byte order.
     */
    private static java.nio.ByteBuffer sliceFromTo(java.nio.ByteBuffer source, int start, int end) {
        if (start < 0) {
            throw new java.lang.IllegalArgumentException("start: " + start);
        }
        if (end < start) {
            throw new java.lang.IllegalArgumentException((("end < start: " + end) + " < ") + start);
        }
        int capacity = source.capacity();
        if (end > source.capacity()) {
            throw new java.lang.IllegalArgumentException((("end > capacity: " + end) + " > ") + capacity);
        }
        int originalLimit = source.limit();
        int originalPosition = source.position();
        try {
            source.position(0);
            source.limit(end);
            source.position(start);
            java.nio.ByteBuffer result = source.slice();
            result.order(source.order());
            return result;
        } finally {
            source.position(0);
            source.limit(originalLimit);
            source.position(originalPosition);
        }
    }

    /**
     * Relative <em>get</em> method for reading {@code size} number of bytes from the current
     * position of this buffer.
     *
     * <p>This method reads the next {@code size} bytes at this buffer's current position,
     * returning them as a {@code ByteBuffer} with start set to 0, limit and capacity set to
     * {@code size}, byte order set to this buffer's byte order; and then increments the position by
     * {@code size}.
     */
    private static java.nio.ByteBuffer getByteBuffer(java.nio.ByteBuffer source, int size) throws java.nio.BufferUnderflowException {
        if (size < 0) {
            throw new java.lang.IllegalArgumentException("size: " + size);
        }
        int originalLimit = source.limit();
        int position = source.position();
        int limit = position + size;
        if ((limit < position) || (limit > originalLimit)) {
            throw new java.nio.BufferUnderflowException();
        }
        source.limit(limit);
        try {
            java.nio.ByteBuffer result = source.slice();
            result.order(source.order());
            source.position(limit);
            return result;
        } finally {
            source.limit(originalLimit);
        }
    }

    private static java.nio.ByteBuffer getLengthPrefixedSlice(java.nio.ByteBuffer source) throws java.io.IOException {
        if (source.remaining() < 4) {
            throw new java.io.IOException(("Remaining buffer too short to contain length of length-prefixed field." + " Remaining: ") + source.remaining());
        }
        int len = source.getInt();
        if (len < 0) {
            throw new java.lang.IllegalArgumentException("Negative length");
        } else
            if (len > source.remaining()) {
                throw new java.io.IOException(((("Length-prefixed field longer than remaining buffer." + " Field length: ") + len) + ", remaining: ") + source.remaining());
            }

        return android.util.apk.ApkSignatureSchemeV2Verifier.getByteBuffer(source, len);
    }

    private static byte[] readLengthPrefixedByteArray(java.nio.ByteBuffer buf) throws java.io.IOException {
        int len = buf.getInt();
        if (len < 0) {
            throw new java.io.IOException("Negative length");
        } else
            if (len > buf.remaining()) {
                throw new java.io.IOException((("Underflow while reading length-prefixed value. Length: " + len) + ", available: ") + buf.remaining());
            }

        byte[] result = new byte[len];
        buf.get(result);
        return result;
    }

    private static void setUnsignedInt32LittleEndian(int value, byte[] result, int offset) {
        result[offset] = ((byte) (value & 0xff));
        result[offset + 1] = ((byte) ((value >>> 8) & 0xff));
        result[offset + 2] = ((byte) ((value >>> 16) & 0xff));
        result[offset + 3] = ((byte) ((value >>> 24) & 0xff));
    }

    private static final long APK_SIG_BLOCK_MAGIC_HI = 0x3234206b636f6c42L;

    private static final long APK_SIG_BLOCK_MAGIC_LO = 0x20676953204b5041L;

    private static final int APK_SIG_BLOCK_MIN_SIZE = 32;

    private static final int APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 0x7109871a;

    private static android.util.Pair<java.nio.ByteBuffer, java.lang.Long> findApkSigningBlock(java.io.RandomAccessFile apk, long centralDirOffset) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException, java.io.IOException {
        // FORMAT:
        // OFFSET       DATA TYPE  DESCRIPTION
        // * @+0  bytes uint64:    size in bytes (excluding this field)
        // * @+8  bytes payload
        // * @-24 bytes uint64:    size in bytes (same as the one above)
        // * @-16 bytes uint128:   magic
        if (centralDirOffset < android.util.apk.ApkSignatureSchemeV2Verifier.APK_SIG_BLOCK_MIN_SIZE) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("APK too small for APK Signing Block. ZIP Central Directory offset: " + centralDirOffset);
        }
        // Read the magic and offset in file from the footer section of the block:
        // * uint64:   size of block
        // * 16 bytes: magic
        java.nio.ByteBuffer footer = java.nio.ByteBuffer.allocate(24);
        footer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        apk.seek(centralDirOffset - footer.capacity());
        apk.readFully(footer.array(), footer.arrayOffset(), footer.capacity());
        if ((footer.getLong(8) != android.util.apk.ApkSignatureSchemeV2Verifier.APK_SIG_BLOCK_MAGIC_LO) || (footer.getLong(16) != android.util.apk.ApkSignatureSchemeV2Verifier.APK_SIG_BLOCK_MAGIC_HI)) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("No APK Signing Block before ZIP Central Directory");
        }
        // Read and compare size fields
        long apkSigBlockSizeInFooter = footer.getLong(0);
        if ((apkSigBlockSizeInFooter < footer.capacity()) || (apkSigBlockSizeInFooter > (java.lang.Integer.MAX_VALUE - 8))) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("APK Signing Block size out of range: " + apkSigBlockSizeInFooter);
        }
        int totalSize = ((int) (apkSigBlockSizeInFooter + 8));
        long apkSigBlockOffset = centralDirOffset - totalSize;
        if (apkSigBlockOffset < 0) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("APK Signing Block offset out of range: " + apkSigBlockOffset);
        }
        java.nio.ByteBuffer apkSigBlock = java.nio.ByteBuffer.allocate(totalSize);
        apkSigBlock.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        apk.seek(apkSigBlockOffset);
        apk.readFully(apkSigBlock.array(), apkSigBlock.arrayOffset(), apkSigBlock.capacity());
        long apkSigBlockSizeInHeader = apkSigBlock.getLong(0);
        if (apkSigBlockSizeInHeader != apkSigBlockSizeInFooter) {
            throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException((("APK Signing Block sizes in header and footer do not match: " + apkSigBlockSizeInHeader) + " vs ") + apkSigBlockSizeInFooter);
        }
        return android.util.Pair.create(apkSigBlock, apkSigBlockOffset);
    }

    private static java.nio.ByteBuffer findApkSignatureSchemeV2Block(java.nio.ByteBuffer apkSigningBlock) throws android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException {
        android.util.apk.ApkSignatureSchemeV2Verifier.checkByteOrderLittleEndian(apkSigningBlock);
        // FORMAT:
        // OFFSET       DATA TYPE  DESCRIPTION
        // * @+0  bytes uint64:    size in bytes (excluding this field)
        // * @+8  bytes pairs
        // * @-24 bytes uint64:    size in bytes (same as the one above)
        // * @-16 bytes uint128:   magic
        java.nio.ByteBuffer pairs = android.util.apk.ApkSignatureSchemeV2Verifier.sliceFromTo(apkSigningBlock, 8, apkSigningBlock.capacity() - 24);
        int entryCount = 0;
        while (pairs.hasRemaining()) {
            entryCount++;
            if (pairs.remaining() < 8) {
                throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("Insufficient data to read size of APK Signing Block entry #" + entryCount);
            }
            long lenLong = pairs.getLong();
            if ((lenLong < 4) || (lenLong > java.lang.Integer.MAX_VALUE)) {
                throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException((("APK Signing Block entry #" + entryCount) + " size out of range: ") + lenLong);
            }
            int len = ((int) (lenLong));
            int nextEntryPos = pairs.position() + len;
            if (len > pairs.remaining()) {
                throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException((((("APK Signing Block entry #" + entryCount) + " size out of range: ") + len) + ", available: ") + pairs.remaining());
            }
            int id = pairs.getInt();
            if (id == android.util.apk.ApkSignatureSchemeV2Verifier.APK_SIGNATURE_SCHEME_V2_BLOCK_ID) {
                return android.util.apk.ApkSignatureSchemeV2Verifier.getByteBuffer(pairs, len - 4);
            }
            pairs.position(nextEntryPos);
        } 
        throw new android.util.apk.ApkSignatureSchemeV2Verifier.SignatureNotFoundException("No APK Signature Scheme v2 block in APK Signing Block");
    }

    private static void checkByteOrderLittleEndian(java.nio.ByteBuffer buffer) {
        if (buffer.order() != java.nio.ByteOrder.LITTLE_ENDIAN) {
            throw new java.lang.IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    public static class SignatureNotFoundException extends java.lang.Exception {
        private static final long serialVersionUID = 1L;

        public SignatureNotFoundException(java.lang.String message) {
            super(message);
        }

        public SignatureNotFoundException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Source of data to be digested.
     */
    private static interface DataSource {
        /**
         * Returns the size (in bytes) of the data offered by this source.
         */
        long size();

        /**
         * Feeds the specified region of this source's data into the provided digests. Each digest
         * instance gets the same data.
         *
         * @param offset
         * 		offset of the region inside this data source.
         * @param size
         * 		size (in bytes) of the region.
         */
        void feedIntoMessageDigests(java.security.MessageDigest[] mds, long offset, int size) throws java.io.IOException;
    }

    /**
     * {@link DataSource} which provides data from a file descriptor by memory-mapping the sections
     * of the file requested by
     * {@link DataSource#feedIntoMessageDigests(MessageDigest[], long, int) feedIntoMessageDigests}.
     */
    private static final class MemoryMappedFileDataSource implements android.util.apk.ApkSignatureSchemeV2Verifier.DataSource {
        private static final libcore.io.Os OS = libcore.io.Libcore.os;

        private static final long MEMORY_PAGE_SIZE_BYTES = android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource.OS.sysconf(android.system.OsConstants._SC_PAGESIZE);

        private final java.io.FileDescriptor mFd;

        private final long mFilePosition;

        private final long mSize;

        /**
         * Constructs a new {@code MemoryMappedFileDataSource} for the specified region of the file.
         *
         * @param position
         * 		start position of the region in the file.
         * @param size
         * 		size (in bytes) of the region.
         */
        public MemoryMappedFileDataSource(java.io.FileDescriptor fd, long position, long size) {
            mFd = fd;
            mFilePosition = position;
            mSize = size;
        }

        @java.lang.Override
        public long size() {
            return mSize;
        }

        @java.lang.Override
        public void feedIntoMessageDigests(java.security.MessageDigest[] mds, long offset, int size) throws java.io.IOException {
            // IMPLEMENTATION NOTE: After a lot of experimentation, the implementation of this
            // method was settled on a straightforward mmap with prefaulting.
            // 
            // This method is not using FileChannel.map API because that API does not offset a way
            // to "prefault" the resulting memory pages. Without prefaulting, performance is about
            // 10% slower on small to medium APKs, but is significantly worse for APKs in 500+ MB
            // range. FileChannel.load (which currently uses madvise) doesn't help. Finally,
            // invoking madvise (MADV_SEQUENTIAL) after mmap with prefaulting wastes quite a bit of
            // time, which is not compensated for by faster reads.
            // We mmap the smallest region of the file containing the requested data. mmap requires
            // that the start offset in the file must be a multiple of memory page size. We thus may
            // need to mmap from an offset less than the requested offset.
            long filePosition = mFilePosition + offset;
            long mmapFilePosition = (filePosition / android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource.MEMORY_PAGE_SIZE_BYTES) * android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource.MEMORY_PAGE_SIZE_BYTES;
            int dataStartOffsetInMmapRegion = ((int) (filePosition - mmapFilePosition));
            long mmapRegionSize = size + dataStartOffsetInMmapRegion;
            long mmapPtr = 0;
            try {
                mmapPtr = // let the OS choose the start address of the region in memory
                // "prefault" all pages
                android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource.OS.mmap(0, mmapRegionSize, android.system.OsConstants.PROT_READ, android.system.OsConstants.MAP_SHARED | android.system.OsConstants.MAP_POPULATE, mFd, mmapFilePosition);
                // Feeding a memory region into MessageDigest requires the region to be represented
                // as a direct ByteBuffer.
                java.nio.ByteBuffer buf = // not really needed, but just in case
                // no need to clean up -- it's taken care of by the finally block
                // read only buffer
                new java.nio.DirectByteBuffer(size, mmapPtr + dataStartOffsetInMmapRegion, mFd, null, true);
                for (java.security.MessageDigest md : mds) {
                    buf.position(0);
                    md.update(buf);
                }
            } catch (android.system.ErrnoException e) {
                throw new java.io.IOException(("Failed to mmap " + mmapRegionSize) + " bytes", e);
            } finally {
                if (mmapPtr != 0) {
                    try {
                        android.util.apk.ApkSignatureSchemeV2Verifier.MemoryMappedFileDataSource.OS.munmap(mmapPtr, mmapRegionSize);
                    } catch (android.system.ErrnoException ignored) {
                    }
                }
            }
        }
    }

    /**
     * {@link DataSource} which provides data from a {@link ByteBuffer}.
     */
    private static final class ByteBufferDataSource implements android.util.apk.ApkSignatureSchemeV2Verifier.DataSource {
        /**
         * Underlying buffer. The data is stored between position 0 and the buffer's capacity.
         * The buffer's position is 0 and limit is equal to capacity.
         */
        private final java.nio.ByteBuffer mBuf;

        public ByteBufferDataSource(java.nio.ByteBuffer buf) {
            // Defensive copy, to avoid changes to mBuf being visible in buf.
            mBuf = buf.slice();
        }

        @java.lang.Override
        public long size() {
            return mBuf.capacity();
        }

        @java.lang.Override
        public void feedIntoMessageDigests(java.security.MessageDigest[] mds, long offset, int size) throws java.io.IOException {
            // There's no way to tell MessageDigest to read data from ByteBuffer from a position
            // other than the buffer's current position. We thus need to change the buffer's
            // position to match the requested offset.
            // 
            // In the future, it may be necessary to compute digests of multiple regions in
            // parallel. Given that digest computation is a slow operation, we enable multiple
            // such requests to be fulfilled by this instance. This is achieved by serially
            // creating a new ByteBuffer corresponding to the requested data range and then,
            // potentially concurrently, feeding these buffers into MessageDigest instances.
            java.nio.ByteBuffer region;
            synchronized(mBuf) {
                mBuf.position(((int) (offset)));
                mBuf.limit(((int) (offset)) + size);
                region = mBuf.slice();
            }
            for (java.security.MessageDigest md : mds) {
                // Need to reset position to 0 at the start of each iteration because
                // MessageDigest.update below sets it to the buffer's limit.
                region.position(0);
                md.update(region);
            }
        }
    }

    /**
     * For legacy reasons we need to return exactly the original encoded certificate bytes, instead
     * of letting the underlying implementation have a shot at re-encoding the data.
     */
    private static class VerbatimX509Certificate extends android.util.apk.ApkSignatureSchemeV2Verifier.WrappedX509Certificate {
        private byte[] encodedVerbatim;

        public VerbatimX509Certificate(java.security.cert.X509Certificate wrapped, byte[] encodedVerbatim) {
            super(wrapped);
            this.encodedVerbatim = encodedVerbatim;
        }

        @java.lang.Override
        public byte[] getEncoded() throws java.security.cert.CertificateEncodingException {
            return encodedVerbatim;
        }
    }

    private static class WrappedX509Certificate extends java.security.cert.X509Certificate {
        private final java.security.cert.X509Certificate wrapped;

        public WrappedX509Certificate(java.security.cert.X509Certificate wrapped) {
            this.wrapped = wrapped;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getCriticalExtensionOIDs() {
            return wrapped.getCriticalExtensionOIDs();
        }

        @java.lang.Override
        public byte[] getExtensionValue(java.lang.String oid) {
            return wrapped.getExtensionValue(oid);
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getNonCriticalExtensionOIDs() {
            return wrapped.getNonCriticalExtensionOIDs();
        }

        @java.lang.Override
        public boolean hasUnsupportedCriticalExtension() {
            return wrapped.hasUnsupportedCriticalExtension();
        }

        @java.lang.Override
        public void checkValidity() throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException {
            wrapped.checkValidity();
        }

        @java.lang.Override
        public void checkValidity(java.util.Date date) throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException {
            wrapped.checkValidity(date);
        }

        @java.lang.Override
        public int getVersion() {
            return wrapped.getVersion();
        }

        @java.lang.Override
        public java.math.BigInteger getSerialNumber() {
            return wrapped.getSerialNumber();
        }

        @java.lang.Override
        public java.security.Principal getIssuerDN() {
            return wrapped.getIssuerDN();
        }

        @java.lang.Override
        public java.security.Principal getSubjectDN() {
            return wrapped.getSubjectDN();
        }

        @java.lang.Override
        public java.util.Date getNotBefore() {
            return wrapped.getNotBefore();
        }

        @java.lang.Override
        public java.util.Date getNotAfter() {
            return wrapped.getNotAfter();
        }

        @java.lang.Override
        public byte[] getTBSCertificate() throws java.security.cert.CertificateEncodingException {
            return wrapped.getTBSCertificate();
        }

        @java.lang.Override
        public byte[] getSignature() {
            return wrapped.getSignature();
        }

        @java.lang.Override
        public java.lang.String getSigAlgName() {
            return wrapped.getSigAlgName();
        }

        @java.lang.Override
        public java.lang.String getSigAlgOID() {
            return wrapped.getSigAlgOID();
        }

        @java.lang.Override
        public byte[] getSigAlgParams() {
            return wrapped.getSigAlgParams();
        }

        @java.lang.Override
        public boolean[] getIssuerUniqueID() {
            return wrapped.getIssuerUniqueID();
        }

        @java.lang.Override
        public boolean[] getSubjectUniqueID() {
            return wrapped.getSubjectUniqueID();
        }

        @java.lang.Override
        public boolean[] getKeyUsage() {
            return wrapped.getKeyUsage();
        }

        @java.lang.Override
        public int getBasicConstraints() {
            return wrapped.getBasicConstraints();
        }

        @java.lang.Override
        public byte[] getEncoded() throws java.security.cert.CertificateEncodingException {
            return wrapped.getEncoded();
        }

        @java.lang.Override
        public void verify(java.security.PublicKey key) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.SignatureException, java.security.cert.CertificateException {
            wrapped.verify(key);
        }

        @java.lang.Override
        public void verify(java.security.PublicKey key, java.lang.String sigProvider) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.SignatureException, java.security.cert.CertificateException {
            wrapped.verify(key, sigProvider);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return wrapped.toString();
        }

        @java.lang.Override
        public java.security.PublicKey getPublicKey() {
            return wrapped.getPublicKey();
        }
    }
}

