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
package android.content.pm;


/**
 * Opaque, immutable representation of a signing certificate associated with an
 * application package.
 * <p>
 * This class name is slightly misleading, since it's not actually a signature.
 */
public class Signature implements android.os.Parcelable {
    private final byte[] mSignature;

    private int mHashCode;

    private boolean mHaveHashCode;

    private java.lang.ref.SoftReference<java.lang.String> mStringRef;

    private java.security.cert.Certificate[] mCertificateChain;

    /**
     * APK Signature Scheme v3 includes support for adding a proof-of-rotation record that
     * contains two pieces of information:
     *   1) the past signing certificates
     *   2) the flags that APK wants to assign to each of the past signing certificates.
     *
     * These flags represent the second piece of information and are viewed as capabilities.
     * They are an APK's way of telling the platform: "this is how I want to trust my old certs,
     * please enforce that." This is useful for situation where this app itself is using its
     * signing certificate as an authorization mechanism, like whether or not to allow another
     * app to have its SIGNATURE permission.  An app could specify whether to allow other apps
     * signed by its old cert 'X' to still get a signature permission it defines, for example.
     */
    private int mFlags;

    /**
     * Create Signature from an existing raw byte array.
     */
    public Signature(byte[] signature) {
        mSignature = signature.clone();
        mCertificateChain = null;
    }

    /**
     * Create signature from a certificate chain. Used for backward
     * compatibility.
     *
     * @throws CertificateEncodingException
     * 		
     * @unknown 
     */
    public Signature(java.security.cert.Certificate[] certificateChain) throws java.security.cert.CertificateEncodingException {
        mSignature = certificateChain[0].getEncoded();
        if (certificateChain.length > 1) {
            mCertificateChain = java.util.Arrays.copyOfRange(certificateChain, 1, certificateChain.length);
        }
    }

    private static final int parseHexDigit(int nibble) {
        if (('0' <= nibble) && (nibble <= '9')) {
            return nibble - '0';
        } else
            if (('a' <= nibble) && (nibble <= 'f')) {
                return (nibble - 'a') + 10;
            } else
                if (('A' <= nibble) && (nibble <= 'F')) {
                    return (nibble - 'A') + 10;
                } else {
                    throw new java.lang.IllegalArgumentException(("Invalid character " + nibble) + " in hex string");
                }


    }

    /**
     * Create Signature from a text representation previously returned by
     * {@link #toChars} or {@link #toCharsString()}. Signatures are expected to
     * be a hex-encoded ASCII string.
     *
     * @param text
     * 		hex-encoded string representing the signature
     * @throws IllegalArgumentException
     * 		when signature is odd-length
     */
    public Signature(java.lang.String text) {
        final byte[] input = text.getBytes();
        final int N = input.length;
        if ((N % 2) != 0) {
            throw new java.lang.IllegalArgumentException(("text size " + N) + " is not even");
        }
        final byte[] sig = new byte[N / 2];
        int sigIndex = 0;
        for (int i = 0; i < N;) {
            final int hi = android.content.pm.Signature.parseHexDigit(input[i++]);
            final int lo = android.content.pm.Signature.parseHexDigit(input[i++]);
            sig[sigIndex++] = ((byte) ((hi << 4) | lo));
        }
        mSignature = sig;
    }

    /**
     * Sets the flags representing the capabilities of the past signing certificate.
     *
     * @unknown 
     */
    public void setFlags(int flags) {
        this.mFlags = flags;
    }

    /**
     * Returns the flags representing the capabilities of the past signing certificate.
     *
     * @unknown 
     */
    public int getFlags() {
        return mFlags;
    }

    /**
     * Encode the Signature as ASCII text.
     */
    public char[] toChars() {
        return toChars(null, null);
    }

    /**
     * Encode the Signature as ASCII text in to an existing array.
     *
     * @param existingArray
     * 		Existing char array or null.
     * @param outLen
     * 		Output parameter for the number of characters written in
     * 		to the array.
     * @return Returns either <var>existingArray</var> if it was large enough
    to hold the ASCII representation, or a newly created char[] array if
    needed.
     */
    public char[] toChars(char[] existingArray, int[] outLen) {
        byte[] sig = mSignature;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = ((existingArray == null) || (N2 > existingArray.length)) ? new char[N2] : existingArray;
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = ((char) ((d >= 10) ? ('a' + d) - 10 : '0' + d));
            d = v & 0xf;
            text[(j * 2) + 1] = ((char) ((d >= 10) ? ('a' + d) - 10 : '0' + d));
        }
        if (outLen != null)
            outLen[0] = N;

        return text;
    }

    /**
     * Return the result of {@link #toChars()} as a String.
     */
    public java.lang.String toCharsString() {
        java.lang.String str = (mStringRef == null) ? null : mStringRef.get();
        if (str != null) {
            return str;
        }
        str = new java.lang.String(toChars());
        mStringRef = new java.lang.ref.SoftReference<java.lang.String>(str);
        return str;
    }

    /**
     *
     *
     * @return the contents of this signature as a byte array.
     */
    public byte[] toByteArray() {
        byte[] bytes = new byte[mSignature.length];
        java.lang.System.arraycopy(mSignature, 0, bytes, 0, mSignature.length);
        return bytes;
    }

    /**
     * Returns the public key for this signature.
     *
     * @throws CertificateException
     * 		when Signature isn't a valid X.509
     * 		certificate; shouldn't happen.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public java.security.PublicKey getPublicKey() throws java.security.cert.CertificateException {
        final java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        final java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(mSignature);
        final java.security.cert.Certificate cert = certFactory.generateCertificate(bais);
        return cert.getPublicKey();
    }

    /**
     * Used for compatibility code that needs to check the certificate chain
     * during upgrades.
     *
     * @throws CertificateEncodingException
     * 		
     * @unknown 
     */
    public android.content.pm.Signature[] getChainSignatures() throws java.security.cert.CertificateEncodingException {
        if (mCertificateChain == null) {
            return new android.content.pm.Signature[]{ this };
        }
        android.content.pm.Signature[] chain = new android.content.pm.Signature[1 + mCertificateChain.length];
        chain[0] = this;
        int i = 1;
        for (java.security.cert.Certificate c : mCertificateChain) {
            chain[i++] = new android.content.pm.Signature(c.getEncoded());
        }
        return chain;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        try {
            if (obj != null) {
                android.content.pm.Signature other = ((android.content.pm.Signature) (obj));
                return (this == other) || java.util.Arrays.equals(mSignature, other.mSignature);
            }
        } catch (java.lang.ClassCastException e) {
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        if (mHaveHashCode) {
            return mHashCode;
        }
        mHashCode = java.util.Arrays.hashCode(mSignature);
        mHaveHashCode = true;
        return mHashCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeByteArray(mSignature);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.Signature> CREATOR = new android.os.Parcelable.Creator<android.content.pm.Signature>() {
        public android.content.pm.Signature createFromParcel(android.os.Parcel source) {
            return new android.content.pm.Signature(source);
        }

        public android.content.pm.Signature[] newArray(int size) {
            return new android.content.pm.Signature[size];
        }
    };

    private Signature(android.os.Parcel source) {
        mSignature = source.createByteArray();
    }

    /**
     * Test if given {@link Signature} sets are exactly equal.
     *
     * @unknown 
     */
    public static boolean areExactMatch(android.content.pm.Signature[] a, android.content.pm.Signature[] b) {
        return ((a.length == b.length) && com.android.internal.util.ArrayUtils.containsAll(a, b)) && com.android.internal.util.ArrayUtils.containsAll(b, a);
    }

    /**
     * Test if given {@link Signature} sets are effectively equal. In rare
     * cases, certificates can have slightly malformed encoding which causes
     * exact-byte checks to fail.
     * <p>
     * To identify effective equality, we bounce the certificates through an
     * decode/encode pass before doing the exact-byte check. To reduce attack
     * surface area, we only allow a byte size delta of a few bytes.
     *
     * @throws CertificateException
     * 		if the before/after length differs
     * 		substantially, usually a signal of something fishy going on.
     * @unknown 
     */
    public static boolean areEffectiveMatch(android.content.pm.Signature[] a, android.content.pm.Signature[] b) throws java.security.cert.CertificateException {
        final java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
        final android.content.pm.Signature[] aPrime = new android.content.pm.Signature[a.length];
        for (int i = 0; i < a.length; i++) {
            aPrime[i] = android.content.pm.Signature.bounce(cf, a[i]);
        }
        final android.content.pm.Signature[] bPrime = new android.content.pm.Signature[b.length];
        for (int i = 0; i < b.length; i++) {
            bPrime[i] = android.content.pm.Signature.bounce(cf, b[i]);
        }
        return android.content.pm.Signature.areExactMatch(aPrime, bPrime);
    }

    /**
     * Test if given {@link Signature} objects are effectively equal. In rare
     * cases, certificates can have slightly malformed encoding which causes
     * exact-byte checks to fail.
     * <p>
     * To identify effective equality, we bounce the certificates through an
     * decode/encode pass before doing the exact-byte check. To reduce attack
     * surface area, we only allow a byte size delta of a few bytes.
     *
     * @throws CertificateException
     * 		if the before/after length differs
     * 		substantially, usually a signal of something fishy going on.
     * @unknown 
     */
    public static boolean areEffectiveMatch(android.content.pm.Signature a, android.content.pm.Signature b) throws java.security.cert.CertificateException {
        final java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
        final android.content.pm.Signature aPrime = android.content.pm.Signature.bounce(cf, a);
        final android.content.pm.Signature bPrime = android.content.pm.Signature.bounce(cf, b);
        return aPrime.equals(bPrime);
    }

    /**
     * Bounce the given {@link Signature} through a decode/encode cycle.
     *
     * @throws CertificateException
     * 		if the before/after length differs
     * 		substantially, usually a signal of something fishy going on.
     * @unknown 
     */
    public static android.content.pm.Signature bounce(java.security.cert.CertificateFactory cf, android.content.pm.Signature s) throws java.security.cert.CertificateException {
        final java.io.InputStream is = new java.io.ByteArrayInputStream(s.mSignature);
        final java.security.cert.X509Certificate cert = ((java.security.cert.X509Certificate) (cf.generateCertificate(is)));
        final android.content.pm.Signature sPrime = new android.content.pm.Signature(cert.getEncoded());
        if (java.lang.Math.abs(sPrime.mSignature.length - s.mSignature.length) > 2) {
            throw new java.security.cert.CertificateException((("Bounced cert length looks fishy; before " + s.mSignature.length) + ", after ") + sPrime.mSignature.length);
        }
        return sPrime;
    }
}

