package com.troy.keeper.hbz.helper;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by leecheng on 2017/10/12.
 */
public class KeyStoreLoader {

    /**
     * Java密钥库(Java Key Store，JKS)KEY_STORE
     */
    public static final String KEY_STORE = "JKS";

    public static final String X509 = "X.509";

    /**
     * 获得KeyStore
     *
     * @param keyStorePath
     * @param password
     * @return
     * @throws Exception
     * @author leecheng
     * @version 1.0
     */
    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws Exception {

        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance(KEY_STORE);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    /**
     * 由KeyStore获得私钥
     *
     * @param keyStorePath
     * @param alias
     * @param storePass
     * @return
     * @throws Exception
     * @author licheng
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String alias, String storePass, String keyPass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PrivateKey key = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
        return key;
    }

    /**
     * 由Certificate获得公钥
     *
     * @param keyStorePath KeyStore路径
     * @param alias        别名
     * @param storePass    KeyStore访问密码
     * @return
     * @throws Exception
     * @author leecheng
     */
    private static PublicKey getPublicKey(String keyStorePath, String alias, String storePass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PublicKey key = ks.getCertificate(alias).getPublicKey();
        return key;
    }

    /**
     * 从KeyStore中获取公钥，并经BASE64编码
     *
     * @param keyStorePath
     * @param alias
     * @param storePass
     * @return
     * @throws Exception
     * @author leecheng
     */
    public static String getStrPublicKey(String keyStorePath, String alias, String storePass) throws Exception {
        PublicKey key = getPublicKey(keyStorePath, alias, storePass);
        String strKey = Base64Encrypter.base64Encode(key.getEncoded());
        return strKey;
    }

    /**
     * 获取经BASE64编码后的私钥
     *
     * @param keyStorePath
     * @param alias
     * @param storePass
     * @param keyPass
     * @return
     * @throws Exception
     * @author leecheng
     */
    public static String getStrPrivateKey(String keyStorePath, String alias, String storePass, String keyPass) throws Exception {
        PrivateKey key = getPrivateKey(keyStorePath, alias, storePass, keyPass);
        String strKey = Base64Encrypter.base64Encode(key.getEncoded());
        return strKey;
    }

    /**
     * 使用公钥加密数据
     *
     * @param publicKeyBase64
     * @param srcData
     * @return
     * @throws Exception
     * @author leecheng
     */
    public static String encryptByPublicKey(String publicKeyBase64, String srcData) throws Exception {
        //解密
        byte[] pk = Base64Encrypter.base64Decode(publicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        //获取公钥
        PublicKey pubKey = kf.generatePublic(spec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] doFinal = cipher.doFinal(srcData.getBytes());
        return Base64Encrypter.base64Encode(doFinal);
    }

    /**
     * 使用私钥解密数据
     *
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     * @author leecheng
     */
    public static String descryptByPrivateKey(String privateKey, String data) throws Exception {
        // BASE64转码解密私钥
        byte[] pk = Base64Encrypter.base64Decode(privateKey);
        // BASE64转码解密密文
        byte[] text = Base64Encrypter.base64Decode(data);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // 获取私钥
        PrivateKey prvKey = kf.generatePrivate(spec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prvKey);
        byte[] doFinal = cipher.doFinal(text);
        return new String(doFinal);
    }


    /**
     * 使用私钥加密数据
     * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
     *
     * @param keyInByte 打包成byte[]的私钥
     * @param source    要签名的数据，一般应是数字摘要
     * @return 签名 byte[]
     */
    public static byte[] sign(byte[] keyInByte, byte[] source) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(keyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(privKey);
            sig.update(source);
            return sig.sign();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证数字签名
     *
     * @param keyInByte 打包成byte[]形式的公钥
     * @param source    原文的数字摘要
     * @param sign      签名（对原文的数字摘要的签名）
     * @return 是否证实 boolean
     */
    public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            Signature sig = Signature.getInstance("SHA1withRSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            sig.initVerify(pubKey);
            sig.update(source);
            return sig.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
     *
     * @return 包含打包成byte[]形式的私钥和公钥的object[], 其中，object[0]为私钥byte[],object[1]为公钥byte[]
     */
    public static Object[] giveRSAKeyPairInByte() {
        KeyPair newKeyPair = generateKey();
        if (newKeyPair == null)
            return null;
        Object[] re = new Object[2];
        if (newKeyPair != null) {
            PrivateKey priv = newKeyPair.getPrivate();
            byte[] b_priv = priv.getEncoded();
            PublicKey pub = newKeyPair.getPublic();
            byte[] b_pub = pub.getEncoded();
            re[0] = b_priv;
            re[1] = b_pub;
            return re;
        }
        return null;
    }

    /**
     * 新建密钥对
     *
     * @return KeyPair对象
     */
    public static KeyPair generateKey() {
        KeyPair myPair;
        long mySeed;
        mySeed = System.currentTimeMillis();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();
        } catch (Exception e1) {
            return null;
        }
        return myPair;
    }

    /**
     * 使用RSA公钥加密数据
     *
     * @param pubKeyInByte 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 用RSA私钥解密
     *
     * @param privKeyInByte 私钥打包成byte[]形式
     * @param data          要解密的数据
     * @return 解密数据
     */
    public static byte[] decryptByRSA(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 私钥加密
     *
     * @param privateKey 私钥Base64
     * @param data       原始文本
     * @return
     */
    public static String encryptByPrivateKey(String privateKey, String data) {
        byte[] dest = encryptByRSA1(Base64Encrypter.base64Decode(privateKey), data.getBytes());
        return Base64Encrypter.base64Encode(dest);
    }


    /**
     * 使用RSA私钥加密数据
     *
     * @param privKeyInByte 打包的byte[]形式私钥
     * @param data          要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSA1(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 公钥解密
     *
     * @param publicKey base64编码
     * @param data64    二进制base64编码
     * @return
     */
    public static String descryptByPublicKey(String publicKey, String data64) {
        byte[] dest = decryptByRSA1(Base64Encrypter.base64Decode(publicKey), Base64Encrypter.base64Decode(data64));
        return new String(dest);
    }

    /**
     * 用RSA公钥解密
     *
     * @param pubKeyInByte 公钥打包成byte[]形式
     * @param data         要解密的数据
     * @return 解密数据
     */
    public static byte[] decryptByRSA1(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算字符串的SHA数字摘要，以byte[]形式返回
     */
    public static byte[] MdigestSHA(String source) {
        //byte[] nullreturn = { 0 };
        try {
            MessageDigest thisMD = MessageDigest.getInstance("SHA");
            byte[] digest = thisMD.digest(source.getBytes("UTF-8"));
            return digest;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {

        try {

            KeyPair keyPair = generateKey();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();


            String text = "你好";
            System.out.print(new String(decryptByRSA1(publicKey.getEncoded(), encryptByRSA1(privateKey.getEncoded(), text.getBytes()))) + "\n");


            //私钥加密 公钥解密
            //生成私钥-公钥对
            Object[] v = giveRSAKeyPairInByte();
            //获得摘要
            byte[] source = MdigestSHA("假设这是要加密的客户数据");
            //使用私钥对摘要进行加密 获得密文 即数字签名
            byte[] sign = sign((byte[]) v[0], source);
            //使用公钥对密文进行解密,解密后与摘要进行匹配
            boolean yes = verify((byte[]) v[1], source, sign);
            if (yes)
                System.out.println("匹配成功 合法的签名!");


            //公钥加密私钥解密
            //获得摘要
            byte[] sourcepub_pri = ("13265986584||316494646546486498||01||public").getBytes("UTF-8");
            //使用公钥对摘要进行加密 获得密文
            byte[] signpub_pri = encryptByRSA((byte[]) v[1], sourcepub_pri);
            //System.out.println("公钥加密密文："+new String(Base64.encodeBase64(signpub_pri)));
            //使用私钥对密文进行解密 返回解密后的数据
            byte[] newSourcepub_pri = decryptByRSA((byte[]) v[0], signpub_pri);

            System.out.println("私钥解密：" + new String(newSourcepub_pri, "UTF-8"));
            //对比源数据与解密后的数据
            if (Arrays.equals(sourcepub_pri, newSourcepub_pri))
                System.out.println("匹配成功 合法的私钥!");


            //私钥加密公钥解密
            //获得摘要
            //byte[] sourcepri_pub = MdigestSHA("假设这是要加密的客户数据");
            byte[] sourcepri_pub = ("13265986584||316494646546486498||01||private").getBytes("UTF-8");

            //使用私钥对摘要进行加密 获得密文
            byte[] signpri_pub = encryptByRSA1((byte[]) v[0], sourcepri_pub);

            //   System.out.println("私钥加密密文："+new String(Base64.encodeBase64(sign11)));
            //使用公钥对密文进行解密 返回解密后的数据
            byte[] newSourcepri_pub = decryptByRSA1((byte[]) v[1], signpri_pub);


            System.out.println("公钥解密：" + new String(newSourcepri_pub, "UTF-8"));


            String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEGENnf3rdiO20isoLQqezw12FoWXII9FBw8nR1MWQ3X0CVzOsqY1hOmxD/YI9OB7WVIaVax5tj1l+wk6A0v85Z4OpGWqz4B5L3fCUlBwf/M6DXHlSN1OZttvQF3OeWvc6gvJHihR7pp18zc4KfCJx0Ry6IrGH/2SNOVE1AIgvRQIDAQAB";
            String PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIQYQ2d/et2I7bSKygtCp7PDXYWhZcgj0UHDydHUxZDdfQJXM6ypjWE6bEP9gj04HtZUhpVrHm2PWX7CToDS/zlng6kZarPgHkvd8JSUHB/8zoNceVI3U5m229AXc55a9zqC8keKFHumnXzNzgp8InHRHLoisYf/ZI05UTUAiC9FAgMBAAECgYAGNcHNds/G5G4QY8n1149cwx19b8YCL7Thu5ucUr1q/w6mcoUKY/oyjPWUCLH7wMyqVNTy51NJ4UhazjW0lrbK4ZbPDHFij9CiZ7QFASiQ/TQWaL+KSIWnE6/rK9IdouwFKxk+cvvLteZoAXP6mFcrsa7LzfkENiIMu7mjpTNHAQJBANXv9U5JWOAVhWHDQcEWKn7YKpAVRleXdeUeJrXcdkqBDI+P6suA9j+ahDREfu+x65wUsrJotPHUXgJG0TarJIUCQQCeEPLrv6Qvi5+nbn2Eifn/fjsmIdI0U2WZKDHWJEnLsRUuGDNYxVE/SPDNDedA2OHeFB6j0Kk/ECdsWnUq6zvBAkAgUGViFMwa1MVX1fFZo+p5TFdpef0s/9Cr8djxAULQ0BtAmAFkCa+oPcOYTXxK4jnvUmUHc69ZE7W7bEzvj/wtAkB50X4mClAzBFxK4XCC0QOG0HYtcStbgFpwqvWdn+Hvxc4Y9DW+WHPBXimXHvv2ki+gw8jJX2rQW1bGvwBFz30BAkASPkORJxVWv91StjI2f/HXDO5eG5/su/XIb3eajaLUSEdaQlcs3ywLrrJ0o3VAR0J9aq59cmp6em017AMnmbF7";

            byte[] signPrivate = Base64Encrypter.base64Decode(PRIVATEKEY);
            byte[] signPublic = Base64Encrypter.base64Decode(PUBLICKEY);

            String publicpwd = "N/b4nYbbLFVq0yTAIOpNNydtNQUCQxQy0B7bD6kzxLMW2guYxXtWOC/9Z5dpWecx/y7d5CezUJ6cf/8++msiNie4DcKBaFDFPh5rPbjeEB+DRfhjcdR2BsVGXWLsq3dLYLgZObQXG6Tb9rXakuH34Y+6KIIwCjiODH2QAU+PSiM=";
            String privatepwd = "MTMyNjU5ODY1ODR8fDMxNjQ5NDY0NjU0NjQ4NjQ5OHx8MDF8fHByaXZhdGU=";
            //使用私钥对密文进行解密 返回解密后的数据
            byte[] newSource111 = decryptByRSA(signPrivate, Base64Encrypter.base64Decode(publicpwd));
            System.out.println("私钥解密1：" + new String(newSource111, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}