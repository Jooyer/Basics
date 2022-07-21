package cn.lvsong.lib.net.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * https://blog.csdn.net/HelloMoney_/article/details/52870977
 * https://blog.csdn.net/voidmain_123/article/details/78962357
 * https://blog.csdn.net/jungle_pig/article/details/72621237
 * <p>
 * https://blog.csdn.net/wangqiuyun/article/details/42143957(这个讲解最为清晰)
 * https://www.2cto.com/kf/201712/703948.html
 * https://blog.csdn.net/ztx114/article/details/79236874 --> 获取秘钥对,得到结果和下面网站一样
 * https://docs.open.alipay.com/291/105971 --> 秘钥对生成网站
 * http://web.chacuo.net/netrsakeypair --> 秘钥对生成网站
 * <p>
 * <p>
 * //List转数组
 * List byteList = new ArrayList();
 * Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
 * //数组转list
 * Byte[] bytes1 = new Byte[1024];
 * List byteList2 = Arrays.asList(bytes1);
 * <p>
 * Desc: RSA 加密解密
 * Author: Molue2018
 * Date: 2018-11-11
 * Time: 22:42
 */
// 在移动端获取解密的Cipher类时要使用Cipher.getInstance(“RSA/ECB/PKCS1Padding”);
//在后端使用Cipher.getInstance(“RSA”);来获取.
//以上这篇完美解决Android客户端RSA解密部分乱码的问题
public class RSAUtil {
    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    private static final String RSA = "RSA";// 非对称加密密钥算法
    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    private static final int MAX_ENCRYPT_BLOCK = 245;
    private static final int MAX_DECRYPT_BLOCK = 256;
    /*
     * AES/CBC/NoPadding (128)
     * AES/CBC/PKCS5Padding (128)
     * AES/ECB/NoPadding (128)
     * AES/ECB/PKCS5Padding (128)
     * DES/CBC/NoPadding (56)
     * DES/CBC/PKCS5Padding (56)
     * DES/ECB/NoPadding (56)
     * DES/ECB/PKCS5Padding (56)
     * DESede/CBC/NoPadding (168)
     * DESede/CBC/PKCS5Padding (168)
     * DESede/ECB/NoPadding (168)
     * DESede/ECB/PKCS5Padding (168)
     * RSA/ECB/PKCS1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
     */
    // RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
    public static final int DEFAULT_KEY_SIZE = 2048;//秘钥默认长度, 如果长度=1024,则 MAX_ENCRYPT_BLOCK = 117, MAX_DECRYPT_BLOCK = 128
    private static final byte[] DEFAULT_SPLIT = "#PART#".getBytes(StandardCharsets.UTF_8);    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    private static final int DEFAULT_BUFFER_SIZE = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数

    private static KeyPair mKeyPair;

    /**
     * 一般密钥对服务器下发,或者在 Android 中本地保存公钥
     * 根据 encodeRules 规则生成RSA密钥对
     * keyLength 密钥长度，范围：512～2048,一般1024
     *
     * @return 公钥私钥密钥对
     */
    private static KeyPair generateRSAKeyPair(String encodeRules) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(DEFAULT_KEY_SIZE, new SecureRandom(encodeRules.getBytes(StandardCharsets.UTF_8)));
            KeyPair keyPair = kpg.genKeyPair();
            // 公钥
//            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
//            RSAPrivateKey rsaPrivateCrtKey = (RSAPrivateKey) keyPair.getPrivate();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static KeyPair getKeyPair(){
        if (null == mKeyPair){
            synchronized (RSAUtil.class){
                if (null == mKeyPair){
                    mKeyPair = generateRSAKeyPair("DianSi");
                }
            }
        }
        return mKeyPair;
    }



    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(byte[] publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(publicKeyBase64);
        return keyFactory.generatePublic(publicpkcs8KeySpec);
    }

    /**
     * 获取私钥对象
     *
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] privateKeyBase64)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PKCS8EncodedKeySpec privatekcs8KeySpec =
                new PKCS8EncodedKeySpec(privateKeyBase64);
        return keyFactory.generatePrivate(privatekcs8KeySpec);
    }

    /**
     * RSA 数字签名
     * 根据 encodeRules 规则生成RSA密钥对
     *
     * @param data 原文
     * @return 签名后的内容
     */
    public static String rsaSign(String encodeRules, String data) {
        // 获取 RSA 密钥对
        try {
            KeyPair keyPair = generateRSAKeyPair(encodeRules);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey); //用私钥初始化signature
            signature.update(data.getBytes(StandardCharsets.UTF_8));   //更新 原始字符串

            //用公钥初始化signature
//            signature.initVerify(keyFactory.generatePublic(pkcs8EncodedKeySpec));
            //更新原始字符串
//            signature.update(data.getBytes(StandardCharsets.UTF_8));
            //校验签名是否正确
//            boolean result = signature.verify(signature.sign());

            return Base64Util.encodeToString(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 用公钥对字符串进行加密
     *
     * @param data      原文
     * @param publicKey --> RSA 公钥 ===> ((RSAPublicKey) keyPair.getPublic()).getEncoded()
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        // java.security.InvalidKeyException: OAEP cannot be used to sign or verify signatures
        // 如果模式是 DECRYPT_mode，密钥是 RSAPublicKey/RSAPrivateKey，填充类型不是PAD_NONE或PAD_PKCS1
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey --> RSA 私钥 ==> ((RSAPrivateKey) keyPair.getPrivate()).getEncoded()
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey --> RSA 公钥 ===> ((RSAPublicKey) keyPair.getPublic()).getEncoded()
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyPublic);
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥进行解密
     *
     * @param encrypted  待解密数据
     * @param privateKey --> RSA 私钥 ==> ((RSAPrivateKey) keyPair.getPrivate()).getEncoded()
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);

        // 解密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        return cp.doFinal(encrypted);
    }

    /**
     * 公钥对数据进行分段加密
     *
     * @param publicKeyBase64 --> RSA 公钥 ===> ((RSAPublicKey) keyPair.getPublic()).getEncoded()
     */
    public static String encryptByPublicKeyForSpilt(String content, byte[] publicKeyBase64) throws Exception {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, MAX_ENCRYPT_BLOCK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 用私钥对数据分段加密
     *
     * @param content          要加密的原始数据
     * @param privateKeyBase64 --> RSA 私钥 ==> ((RSAPrivateKey) keyPair.getPrivate()).getEncoded()
     */
    public static String encryptByPrivateKeyForSpilt(String content, byte[] privateKeyBase64) throws Exception {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return encipher(content, privateKey, MAX_ENCRYPT_BLOCK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥对数据分段解密
     *
     * @param contentBase64   待解密数据
     * @param publicKeyBase64 密钥
     */
    public static String decryptByPublicKeyForSpilt(String contentBase64, byte[] publicKeyBase64) throws Exception {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return decipher(contentBase64, publicKey, MAX_DECRYPT_BLOCK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥对数据分段解密
     */
    public static String decryptByPrivateKeyForSpilt(String contentBase64, byte[] privateKeyBase64) throws Exception {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, MAX_DECRYPT_BLOCK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分段加密
     * segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
     *
     * @param ciphertext  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return
     */
    private static String encipher(String ciphertext, Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = ciphertext.getBytes(StandardCharsets.UTF_8);

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;

            if (segmentSize > 0)
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
            else
                resultBytes = cipher.doFinal(srcBytes);
            return Base64Util.encodeToString(resultBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段解密
     *
     * @param contentBase64 密文
     * @param key           解密秘钥
     * @param segmentSize   分段大小（小于等于0不分段）
     * @return
     */
    private static String decipher(String contentBase64, Key key, int segmentSize) {
        try {
            byte[] srcBytes = Base64Util.decodeFromString(contentBase64);
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 根据公钥，对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes = null;//deCipher.doFinal(srcBytes);
            if (segmentSize > 0)
                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize); //分段解密
            else
                decBytes = deCipher.doFinal(srcBytes);

            return new String(decBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分段处理
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

}
