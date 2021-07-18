/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.opengl;


/**
 * Utility methods for using ETC1 compressed textures.
 */
public class ETC1Util {
    /**
     * Convenience method to load an ETC1 texture whether or not the active OpenGL context
     * supports the ETC1 texture compression format.
     *
     * @param target
     * 		the texture target.
     * @param level
     * 		the texture level
     * @param border
     * 		the border size. Typically 0.
     * @param fallbackFormat
     * 		the format to use if ETC1 texture compression is not supported.
     * 		Must be GL_RGB.
     * @param fallbackType
     * 		the type to use if ETC1 texture compression is not supported.
     * 		Can be either GL_UNSIGNED_SHORT_5_6_5, which implies 16-bits-per-pixel,
     * 		or GL_UNSIGNED_BYTE, which implies 24-bits-per-pixel.
     * @param input
     * 		the input stream containing an ETC1 texture in PKM format.
     * @throws IOException
     * 		
     */
    public static void loadTexture(int target, int level, int border, int fallbackFormat, int fallbackType, java.io.InputStream input) throws java.io.IOException {
        android.opengl.ETC1Util.loadTexture(target, level, border, fallbackFormat, fallbackType, android.opengl.ETC1Util.createTexture(input));
    }

    /**
     * Convenience method to load an ETC1 texture whether or not the active OpenGL context
     * supports the ETC1 texture compression format.
     *
     * @param target
     * 		the texture target.
     * @param level
     * 		the texture level
     * @param border
     * 		the border size. Typically 0.
     * @param fallbackFormat
     * 		the format to use if ETC1 texture compression is not supported.
     * 		Must be GL_RGB.
     * @param fallbackType
     * 		the type to use if ETC1 texture compression is not supported.
     * 		Can be either GL_UNSIGNED_SHORT_5_6_5, which implies 16-bits-per-pixel,
     * 		or GL_UNSIGNED_BYTE, which implies 24-bits-per-pixel.
     * @param texture
     * 		the ETC1 to load.
     */
    public static void loadTexture(int target, int level, int border, int fallbackFormat, int fallbackType, android.opengl.ETC1Util.ETC1Texture texture) {
        if (fallbackFormat != android.opengl.GLES10.GL_RGB) {
            throw new java.lang.IllegalArgumentException("fallbackFormat must be GL_RGB");
        }
        if (!((fallbackType == android.opengl.GLES10.GL_UNSIGNED_SHORT_5_6_5) || (fallbackType == android.opengl.GLES10.GL_UNSIGNED_BYTE))) {
            throw new java.lang.IllegalArgumentException("Unsupported fallbackType");
        }
        int width = texture.getWidth();
        int height = texture.getHeight();
        java.nio.Buffer data = texture.getData();
        if (android.opengl.ETC1Util.isETC1Supported()) {
            int imageSize = data.remaining();
            android.opengl.GLES10.glCompressedTexImage2D(target, level, android.opengl.ETC1.ETC1_RGB8_OES, width, height, border, imageSize, data);
        } else {
            boolean useShorts = fallbackType != android.opengl.GLES10.GL_UNSIGNED_BYTE;
            int pixelSize = (useShorts) ? 2 : 3;
            int stride = pixelSize * width;
            java.nio.ByteBuffer decodedData = java.nio.ByteBuffer.allocateDirect(stride * height).order(java.nio.ByteOrder.nativeOrder());
            android.opengl.ETC1.decodeImage(data, decodedData, width, height, pixelSize, stride);
            android.opengl.GLES10.glTexImage2D(target, level, fallbackFormat, width, height, border, fallbackFormat, fallbackType, decodedData);
        }
    }

    /**
     * Check if ETC1 texture compression is supported by the active OpenGL ES context.
     *
     * @return true if the active OpenGL ES context supports ETC1 texture compression.
     */
    public static boolean isETC1Supported() {
        int[] results = new int[20];
        android.opengl.GLES10.glGetIntegerv(android.opengl.GLES10.GL_NUM_COMPRESSED_TEXTURE_FORMATS, results, 0);
        int numFormats = results[0];
        if (numFormats > results.length) {
            results = new int[numFormats];
        }
        android.opengl.GLES10.glGetIntegerv(android.opengl.GLES10.GL_COMPRESSED_TEXTURE_FORMATS, results, 0);
        for (int i = 0; i < numFormats; i++) {
            if (results[i] == android.opengl.ETC1.ETC1_RGB8_OES) {
                return true;
            }
        }
        return false;
    }

    /**
     * A utility class encapsulating a compressed ETC1 texture.
     */
    public static class ETC1Texture {
        public ETC1Texture(int width, int height, java.nio.ByteBuffer data) {
            mWidth = width;
            mHeight = height;
            mData = data;
        }

        /**
         * Get the width of the texture in pixels.
         *
         * @return the width of the texture in pixels.
         */
        public int getWidth() {
            return mWidth;
        }

        /**
         * Get the height of the texture in pixels.
         *
         * @return the width of the texture in pixels.
         */
        public int getHeight() {
            return mHeight;
        }

        /**
         * Get the compressed data of the texture.
         *
         * @return the texture data.
         */
        public java.nio.ByteBuffer getData() {
            return mData;
        }

        private int mWidth;

        private int mHeight;

        private java.nio.ByteBuffer mData;
    }

    /**
     * Create a new ETC1Texture from an input stream containing a PKM formatted compressed texture.
     *
     * @param input
     * 		an input stream containing a PKM formatted compressed texture.
     * @return an ETC1Texture read from the input stream.
     * @throws IOException
     * 		
     */
    public static android.opengl.ETC1Util.ETC1Texture createTexture(java.io.InputStream input) throws java.io.IOException {
        int width = 0;
        int height = 0;
        byte[] ioBuffer = new byte[4096];
        {
            if (input.read(ioBuffer, 0, android.opengl.ETC1.ETC_PKM_HEADER_SIZE) != android.opengl.ETC1.ETC_PKM_HEADER_SIZE) {
                throw new java.io.IOException("Unable to read PKM file header.");
            }
            java.nio.ByteBuffer headerBuffer = java.nio.ByteBuffer.allocateDirect(android.opengl.ETC1.ETC_PKM_HEADER_SIZE).order(java.nio.ByteOrder.nativeOrder());
            headerBuffer.put(ioBuffer, 0, android.opengl.ETC1.ETC_PKM_HEADER_SIZE).position(0);
            if (!android.opengl.ETC1.isValid(headerBuffer)) {
                throw new java.io.IOException("Not a PKM file.");
            }
            width = android.opengl.ETC1.getWidth(headerBuffer);
            height = android.opengl.ETC1.getHeight(headerBuffer);
        }
        int encodedSize = android.opengl.ETC1.getEncodedDataSize(width, height);
        java.nio.ByteBuffer dataBuffer = java.nio.ByteBuffer.allocateDirect(encodedSize).order(java.nio.ByteOrder.nativeOrder());
        for (int i = 0; i < encodedSize;) {
            int chunkSize = java.lang.Math.min(ioBuffer.length, encodedSize - i);
            if (input.read(ioBuffer, 0, chunkSize) != chunkSize) {
                throw new java.io.IOException("Unable to read PKM file data.");
            }
            dataBuffer.put(ioBuffer, 0, chunkSize);
            i += chunkSize;
        }
        dataBuffer.position(0);
        return new android.opengl.ETC1Util.ETC1Texture(width, height, dataBuffer);
    }

    /**
     * Helper function that compresses an image into an ETC1Texture.
     *
     * @param input
     * 		a native order direct buffer containing the image data
     * @param width
     * 		the width of the image in pixels
     * @param height
     * 		the height of the image in pixels
     * @param pixelSize
     * 		the size of a pixel in bytes (2 or 3)
     * @param stride
     * 		the width of a line of the image in bytes
     * @return the ETC1 texture.
     */
    public static android.opengl.ETC1Util.ETC1Texture compressTexture(java.nio.Buffer input, int width, int height, int pixelSize, int stride) {
        int encodedImageSize = android.opengl.ETC1.getEncodedDataSize(width, height);
        java.nio.ByteBuffer compressedImage = java.nio.ByteBuffer.allocateDirect(encodedImageSize).order(java.nio.ByteOrder.nativeOrder());
        android.opengl.ETC1.encodeImage(input, width, height, pixelSize, stride, compressedImage);
        return new android.opengl.ETC1Util.ETC1Texture(width, height, compressedImage);
    }

    /**
     * Helper function that writes an ETC1Texture to an output stream formatted as a PKM file.
     *
     * @param texture
     * 		the input texture.
     * @param output
     * 		the stream to write the formatted texture data to.
     * @throws IOException
     * 		
     */
    public static void writeTexture(android.opengl.ETC1Util.ETC1Texture texture, java.io.OutputStream output) throws java.io.IOException {
        java.nio.ByteBuffer dataBuffer = texture.getData();
        int originalPosition = dataBuffer.position();
        try {
            int width = texture.getWidth();
            int height = texture.getHeight();
            java.nio.ByteBuffer header = java.nio.ByteBuffer.allocateDirect(android.opengl.ETC1.ETC_PKM_HEADER_SIZE).order(java.nio.ByteOrder.nativeOrder());
            android.opengl.ETC1.formatHeader(header, width, height);
            byte[] ioBuffer = new byte[4096];
            header.get(ioBuffer, 0, android.opengl.ETC1.ETC_PKM_HEADER_SIZE);
            output.write(ioBuffer, 0, android.opengl.ETC1.ETC_PKM_HEADER_SIZE);
            int encodedSize = android.opengl.ETC1.getEncodedDataSize(width, height);
            for (int i = 0; i < encodedSize;) {
                int chunkSize = java.lang.Math.min(ioBuffer.length, encodedSize - i);
                dataBuffer.get(ioBuffer, 0, chunkSize);
                output.write(ioBuffer, 0, chunkSize);
                i += chunkSize;
            }
        } finally {
            dataBuffer.position(originalPosition);
        }
    }
}

