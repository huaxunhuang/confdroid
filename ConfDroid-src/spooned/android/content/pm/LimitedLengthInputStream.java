package android.content.pm;


/**
 * A class that limits the amount of data that is read from an InputStream. When
 * the specified length is reached, the stream returns an EOF even if the
 * underlying stream still has more data.
 *
 * @unknown 
 */
public class LimitedLengthInputStream extends java.io.FilterInputStream {
    /**
     * The end of the stream where we don't want to allow more data to be read.
     */
    private final long mEnd;

    /**
     * Current offset in the stream.
     */
    private long mOffset;

    /**
     *
     *
     * @param in
     * 		underlying stream to wrap
     * @param offset
     * 		offset into stream where data starts
     * @param length
     * 		length of data at offset
     * @throws IOException
     * 		if an error occurred with the underlying stream
     */
    public LimitedLengthInputStream(java.io.InputStream in, long offset, long length) throws java.io.IOException {
        super(in);
        if (in == null) {
            throw new java.io.IOException("in == null");
        }
        if (offset < 0) {
            throw new java.io.IOException("offset < 0");
        }
        if (length < 0) {
            throw new java.io.IOException("length < 0");
        }
        if (length > (java.lang.Long.MAX_VALUE - offset)) {
            throw new java.io.IOException("offset + length > Long.MAX_VALUE");
        }
        mEnd = offset + length;
        skip(offset);
        mOffset = offset;
    }

    @java.lang.Override
    public synchronized int read() throws java.io.IOException {
        if (mOffset >= mEnd) {
            return -1;
        }
        mOffset++;
        return super.read();
    }

    @java.lang.Override
    public int read(byte[] buffer, int offset, int byteCount) throws java.io.IOException {
        if (mOffset >= mEnd) {
            return -1;
        }
        final int arrayLength = buffer.length;
        libcore.util.ArrayUtils.throwsIfOutOfBounds(arrayLength, offset, byteCount);
        if (mOffset > (java.lang.Long.MAX_VALUE - byteCount)) {
            throw new java.io.IOException((("offset out of bounds: " + mOffset) + " + ") + byteCount);
        }
        if ((mOffset + byteCount) > mEnd) {
            byteCount = ((int) (mEnd - mOffset));
        }
        final int numRead = super.read(buffer, offset, byteCount);
        mOffset += numRead;
        return numRead;
    }

    @java.lang.Override
    public int read(byte[] buffer) throws java.io.IOException {
        return read(buffer, 0, buffer.length);
    }
}

