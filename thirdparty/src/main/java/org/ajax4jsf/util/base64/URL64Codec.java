package org.ajax4jsf.util.base64;

/**
 * @author wh1t3p1g
 * @since 2022/7/5
 */
public class URL64Codec {
    static final int CHUNK_SIZE = 76;
    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();
    static final int BASELENGTH = 255;
    static final int LOOKUPLENGTH = 64;
    static final int EIGHTBIT = 8;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    static final int FOURBYTE = 4;
    static final int SIGN = -128;
    static final byte PAD = 95;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    public URL64Codec() {
    }

    private static boolean isBase64(byte octect) {
        if (octect == 95) {
            return true;
        } else {
            return base64Alphabet[octect] != -1;
        }
    }

    public static boolean isArrayByteBase64(byte[] arrayOctect) {
        arrayOctect = discardWhitespace(arrayOctect);
        int length = arrayOctect.length;
        if (length == 0) {
            return true;
        } else {
            for(int i = 0; i < length; ++i) {
                if (!isBase64(arrayOctect[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public Object decode(Object pObject) throws Exception {
        if (!(pObject instanceof byte[])) {
            throw new Exception("NO_BYTE_ARRAY_ERROR");
        } else {
            return this.decode((byte[])pObject);
        }
    }

    public byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        byte[] encodedData = null;
        int encodedDataLength = 0;
        int nbrChunks = 0;
        if (fewerThan24bits != 0) {
            encodedDataLength = (numberTriplets + 1) * 4;
        } else {
            encodedDataLength = numberTriplets * 4;
        }

        if (isChunked) {
            nbrChunks = CHUNK_SEPARATOR.length == 0 ? 0 : (int)Math.ceil((double)((float)encodedDataLength / 76.0F));
            encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;
        }

        encodedData = new byte[encodedDataLength];
        byte k = 0;
        byte l = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        int i = 0;
        int nextSeparatorIndex = 76;
        int chunksSoFar = 0;

        for(i = 0; i < numberTriplets; ++i) {
            dataIndex = i * 3;
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            b3 = binaryData[dataIndex + 2];
            l = (byte)(b2 & 15);
            k = (byte)(b1 & 3);
            byte val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            byte val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
            byte val3 = (b3 & -128) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 252);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            encodedIndex += 4;
            if (isChunked && encodedIndex == nextSeparatorIndex) {
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
                nextSeparatorIndex = 76 * (++chunksSoFar + 1) + chunksSoFar * CHUNK_SEPARATOR.length;
                encodedIndex += CHUNK_SEPARATOR.length;
            }
        }

        dataIndex = i * 3;
        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte)(b1 & 3);
            byte val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex + 2] = 95;
            encodedData[encodedIndex + 3] = 95;
        } else if (fewerThan24bits == 16) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte)(b2 & 15);
            k = (byte)(b1 & 3);
            byte val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            byte val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = 95;
        }

        if (isChunked && chunksSoFar < nbrChunks) {
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }

        return encodedData;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        base64Data = discardNonBase64(base64Data);
        if (base64Data.length == 0) {
            return new byte[0];
        } else {
            int numberQuadruple = base64Data.length / 4;
            byte[] decodedData = null;
            byte b1 = 0;
            byte b2 = 0;
            byte b3 = 0;
            byte b4 = 0;
            byte marker0 = 0;
            byte marker1 = 0;
            int encodedIndex = 0;
            int dataIndex = 0;
            int lastData = base64Data.length;

            while(base64Data[lastData - 1] == 95) {
                if (--lastData == 0) {
                    return new byte[0];
                }
            }

            decodedData = new byte[lastData - numberQuadruple];

            for(int i = 0; i < numberQuadruple; ++i) {
                dataIndex = i * 4;
                marker0 = base64Data[dataIndex + 2];
                marker1 = base64Data[dataIndex + 3];
                b1 = base64Alphabet[base64Data[dataIndex]];
                b2 = base64Alphabet[base64Data[dataIndex + 1]];
                if (marker0 != 95 && marker1 != 95) {
                    b3 = base64Alphabet[marker0];
                    b4 = base64Alphabet[marker1];
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                    decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                    decodedData[encodedIndex + 2] = (byte)(b3 << 6 | b4);
                } else if (marker0 == 95) {
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                } else if (marker1 == 95) {
                    b3 = base64Alphabet[marker0];
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                    decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                }

                encodedIndex += 3;
            }

            return decodedData;
        }
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;

        for(int i = 0; i < data.length; ++i) {
            switch(data[i]) {
                default:
                    groomedData[bytesCopied++] = data[i];
                    break;
                case 9:
                case 10:
                case 13:
                case 32:
            }
        }

        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;

        for(int i = 0; i < data.length; ++i) {
            if (isBase64(data[i])) {
                groomedData[bytesCopied++] = data[i];
            }
        }

        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public Object encode(Object pObject) throws Exception {
        if (!(pObject instanceof byte[])) {
            throw new Exception("NO_BYTE_ARRAY_ERROR");
        } else {
            return this.encode((byte[])pObject);
        }
    }

    public byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false);
    }

    static {
        for(int i = 0; i < 255; ++i) {
            base64Alphabet[i] = -1;
        }

        for(int i = 90; i >= 65; --i) {
            base64Alphabet[i] = (byte)(i - 65);
        }

        for(int i = 122; i >= 97; --i) {
            base64Alphabet[i] = (byte)(i - 97 + 26);
        }

        for(int i = 57; i >= 48; --i) {
            base64Alphabet[i] = (byte)(i - 48 + 52);
        }

        base64Alphabet[45] = 62;
        base64Alphabet[33] = 63;

        for(int i = 0; i <= 25; ++i) {
            lookUpBase64Alphabet[i] = (byte)(65 + i);
        }

        int i = 26;

        for(int j = 0; i <= 51; ++j) {
            lookUpBase64Alphabet[i] = (byte)(97 + j);
            ++i;
        }

        i = 52;

        for(int j = 0; i <= 61; ++j) {
            lookUpBase64Alphabet[i] = (byte)(48 + j);
            ++i;
        }

        lookUpBase64Alphabet[62] = 45;
        lookUpBase64Alphabet[63] = 33;
    }
}
