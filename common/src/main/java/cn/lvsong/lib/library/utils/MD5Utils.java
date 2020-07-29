package cn.lvsong.lib.library.utils;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 将字符串转化为MD5
 */

public class MD5Utils {

    public static String strToMd5By32(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    public static String strToMd5By16(String str) {
        String reStr = strToMd5By32(str);
        if (reStr != null) {
            reStr = reStr.substring(8, 24);
        }
        return reStr;
    }

    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuilder md5StrBuff = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public static String encryptionSHA1(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuilder sha1StrBuff = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                    sha1StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
                } else {
                    sha1StrBuff.append(Integer.toHexString(0xFF & aByteArray));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha1StrBuff.toString().toUpperCase();
    }

    public static long getAppCrc(String packageCodePath){
        try {
            ZipFile zip = new ZipFile(packageCodePath);
            ZipEntry entry = zip.getEntry("classes.dex");
            return entry.getCrc();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
