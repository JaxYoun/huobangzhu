package c;

import com.troy.keeper.hbz.helper.Base64Encrypter;
import com.troy.keeper.hbz.helper.KeyStoreLoader;

import java.security.KeyPair;

/**
 * Created by leecheng on 2017/10/12.
 */
public class PrivateKeyEnPublicDeTest {

    public static void main(String[] args) {
        try {
            String publicKey = KeyStoreLoader.getStrPublicKey("d:/keystore/hbz.keystore", "hbz", "123456");
            String privateKey = KeyStoreLoader.getStrPrivateKey("d:/keystore/hbz.keystore", "hbz", "123456", "123456");

            KeyPair keyPair = KeyStoreLoader.generateKey();
            publicKey = Base64Encrypter.base64Encode(keyPair.getPublic().getEncoded());
            privateKey = Base64Encrypter.base64Encode(keyPair.getPrivate().getEncoded());

            System.out.println("公钥：" + publicKey);
            System.out.println("私钥：" + privateKey);
            String text = "你好";
            System.out.println("原始文本：" + text);
            String ss = KeyStoreLoader.encryptByPublicKey(publicKey, "你好");
            System.out.println("被公钥加密后：" + ss);
            String t = KeyStoreLoader.descryptByPrivateKey(privateKey, ss);
            System.out.println("被私钥解密后：" + t);

            String e = KeyStoreLoader.encryptByPrivateKey(privateKey, text);
            System.out.println("被私钥加密后：" + e);
            String d = KeyStoreLoader.descryptByPublicKey(publicKey, e);
            System.out.println("被公钥解：" + d);

        } catch (Exception e) {

        }
    }

}
