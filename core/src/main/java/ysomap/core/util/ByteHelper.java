package ysomap.core.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * @author wh1t3p1g
 * @since 2022/4/12
 */
public class ByteHelper {

    static public byte[] int2bytes(int x) {
        byte[] bb = new byte[4];
        bb[0] = (byte) (x >> 24);
        bb[1] = (byte) (x >> 16);
        bb[2] = (byte) (x >> 8);
        bb[3] = (byte) (x >> 0);
        return bb;
    }

    static public int bytes2int(byte[] bb, int idx) {
        return ((bb[idx + 0] & 0xFF) << 24)
                | ((bb[idx + 1] & 0xFF) << 16)
                | ((bb[idx + 2] & 0xFF) << 8)
                | ((bb[idx + 3] & 0xFF) << 0);
    }

    static public byte[] long2bytes(long x) {
        byte[] bb = new byte[8];
        bb[0] = (byte) (x >> 56);
        bb[1] = (byte) (x >> 48);
        bb[2] = (byte) (x >> 40);
        bb[3] = (byte) (x >> 32);
        bb[4] = (byte) (x >> 24);
        bb[5] = (byte) (x >> 16);
        bb[6] = (byte) (x >> 8);
        bb[7] = (byte) (x >> 0);
        return bb;
    }

    static public long bytes2long(byte[] bb, int idx) {
        return (((long) bb[idx + 0] & 0xFF) << 56)
                | (((long) bb[idx + 1] & 0xFF) << 48)
                | (((long) bb[idx + 2] & 0xFF) << 40)
                | (((long) bb[idx + 3] & 0xFF) << 32)
                | (((long) bb[idx + 4] & 0xFF) << 24)
                | (((long) bb[idx + 5] & 0xFF) << 16)
                | (((long) bb[idx + 6] & 0xFF) << 8)
                | (((long) bb[idx + 7] & 0xFF) << 0);
    }

    public static byte[] combine(byte[] b1, byte[] b2){
        byte[] ret = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, ret, 0, b1.length);
        System.arraycopy(b2, 0, ret, b1.length, b2.length);
        return ret;
    }

    public static String bytesToHexString(byte[] src) {
        return Hex.encodeHexString(src);
    }

    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        try {
            return Hex.decodeHex(hexString);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * Copy the specified bytes into a new array
     *
     * @param array The array to copy from
     * @param from The index in the array to begin copying from
     * @param to The least index not copied
     * @return A new byte[] containing the copied bytes
     */
    public static byte[] copy(byte[] array, int from, int to) {
        if(to - from < 0) {
            return new byte[0];
        } else {
            byte[] a = new byte[to - from];
            System.arraycopy(array, from, a, 0, to - from);
            return a;
        }
    }

    public static int copy(byte[] des, int desPos, byte[] src, int srcPos, int length) {
        System.arraycopy(src, srcPos, des, desPos, length);
        int resPos = srcPos + length;
        return length;
    }

    /**
     * Write a long to the byte array starting at the given offset
     *
     * @param bytes The byte array
     * @param value The long to write
     * @param offset The offset to begin writing at
     */
    public static void writeLong(byte[] bytes, int offset, long value) {
        bytes[offset] = (byte) (0xFF & (value >> 56));
        bytes[offset + 1] = (byte) (0xFF & (value >> 48));
        bytes[offset + 2] = (byte) (0xFF & (value >> 40));
        bytes[offset + 3] = (byte) (0xFF & (value >> 32));
        bytes[offset + 4] = (byte) (0xFF & (value >> 24));
        bytes[offset + 5] = (byte) (0xFF & (value >> 16));
        bytes[offset + 6] = (byte) (0xFF & (value >> 8));
        bytes[offset + 7] = (byte) (0xFF & value);
    }

    /**
     * Write an int to the byte array starting at the given offset
     *
     * @param bytes The byte array
     * @param value The int to write
     * @param offset The offset to begin writing at
     */
    public static void writeInt(byte[] bytes, int offset, int value) {
        bytes[offset] = (byte) (0xFF & (value >> 24));
        bytes[offset + 1] = (byte) (0xFF & (value >> 16));
        bytes[offset + 2] = (byte) (0xFF & (value >> 8));
        bytes[offset + 3] = (byte) (0xFF & value);
    }

    public static void short2bytes(short value, byte[] bytes, int off) {
        bytes[off + 1] = (byte) value;
        bytes[off] = (byte) (value >>> 8);
    }

    public static byte[] short2bytes(short v) {
        byte[] ret = {0, 0};
        short2bytes(v, ret);
        return ret;
    }

    public static void short2bytes(short v, byte[] b) {
        short2bytes(v, b, 0);
    }

    public static void long2bytes(long value, byte[] bytes, int off) {
        bytes[off + 7] = (byte) value;
        bytes[off + 6] = (byte) (value >>> 8);
        bytes[off + 5] = (byte) (value >>> 16);
        bytes[off + 4] = (byte) (value >>> 24);
        bytes[off + 3] = (byte) (value >>> 32);
        bytes[off + 2] = (byte) (value >>> 40);
        bytes[off + 1] = (byte) (value >>> 48);
        bytes[off] = (byte) (value >>> 56);
    }

    public static void int2bytes(int value, byte[] bytes, int off) {
        bytes[off + 3] = (byte) value;
        bytes[off + 2] = (byte) (value >>> 8);
        bytes[off + 1] = (byte) (value >>> 16);
        bytes[off] = (byte) (value >>> 24);
    }
}
